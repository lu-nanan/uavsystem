import numpy as np
import random
import json
import tensorflow as tf
from tensorflow.keras import layers, models
import mysql.connector
from mysql.connector import Error
import time
import threading


from path_planner import UAVPathPlanner
from database_collection import DatabaseConnectionManager
from update_manager import UpdateDatabaseManager



# 定义无人机类
class Drone:
    def __init__(self, drone_id, status, position, battery, drone_type):
        self.drone_id = drone_id
        self.status = status
        self.position = position
        self.battery = battery
        self.drone_type = drone_type

# 定义任务类
class Task:
    def __init__(self, task_id, location, content, deadline):
        self.task_id = task_id
        self.location = location
        self.content = content
        self.deadline = deadline

# 定义DQN智能体类
class DQNAgent:
    def __init__(self, state_dim, action_dim):
        self.state_dim = state_dim
        self.action_dim = action_dim
        # 定义Q网络
        self.q_network = self.build_network()
        # 定义目标Q网络
        self.target_q_network = self.build_network()
        # 初始化目标网络参数为与主网络相同
        self.target_q_network.set_weights(self.q_network.get_weights())
        # 定义优化器
        self.optimizer = tf.keras.optimizers.Adam(learning_rate=0.001)
        # 经验回放缓冲区，存储完整的经验元组 (state, action, reward, next_state, done)
        self.memory = []
        # 探索率
        self.epsilon = 1.0
        # 最小探索率
        self.epsilon_min = 0.01
        # 探索率衰减率
        self.epsilon_decay = 0.995

    def build_network(self):
        """
        构建Q网络模型
        """
        state_input = layers.Input(shape=(self.state_dim,))
        x = layers.Dense(64, activation='relu')(state_input)
        x = layers.Dense(64, activation='relu')(x)
        q_value_output = layers.Dense(self.action_dim)(x)
        q_network = models.Model(state_input, q_value_output)
        return q_network

    def remember(self, state, action, reward, next_state, done):
        """
        存储经验信息
        参数:
            state: 当前状态
            action: 选择的动作
            reward: 奖励值
            next_state: 下一个状态
            done: 是否完成任务
        """
        self.memory.append((state, action, reward, next_state, done))

    def act(self, state, drones, task):
        """
        根据当前状态选择动作
        参数:
            state: 当前状态
            drones: 无人机列表
            task: 任务对象
        返回:
            action: 选择的动作
        """
        legal_actions = []
        # 用于存储每个合法无人机的相关信息，格式为 (index, 剩余电量预估, 距离任务距离)
        legal_drones_info = []
        for i in range(self.action_dim):
            drone = drones[i]
            if drone.status == "空闲中" and drone.battery > 0 and task.content in drone.drone_type:
                legal_actions.append(i)
                # 计算距离任务地点的距离
                distance_to_task = np.sqrt((drone.position[0] - task.location[0]) ** 2 + (drone.position[1] - task.location[1]) ** 2)
                # 预估往返消耗电量（假设移动10需要1%的电量，简单计算往返）
                battery_consumption = (distance_to_task / 10) * 2
                remaining_battery = drone.battery - battery_consumption
                if remaining_battery > 0:
                    legal_drones_info.append((i, remaining_battery, distance_to_task))
        if np.random.rand() <= self.epsilon:
            if legal_drones_info:
                # 优先选择剩余电量多且距离近的无人机（简单加权方式示例，可以调整权重）
                selected_index = sorted(legal_drones_info, key=lambda x: (-x[1], x[2]))[0][0]
                return selected_index
            return random.choice(legal_actions) if legal_actions else random.randrange(self.action_dim)
        state = np.expand_dims(state, axis=0)
        q_values = self.q_network.predict(state)[0]
        legal_q_values = [q_values[action] for action in legal_actions]
        return legal_actions[np.argmax(legal_q_values)] if legal_actions else np.argmax(q_values)

    def replay(self, batch_size):
        """
        从经验回放缓冲区中采样一批经验元组并更新Q网络
        参数:
            batch_size: 批大小
        """
        if len(self.memory) < batch_size:
            return
        minibatch = random.sample(self.memory, batch_size)
        states = np.array([i[0] for i in minibatch])
        actions = np.array([i[1] for i in minibatch])
        rewards = np.array([i[2] for i in minibatch])
        next_states = np.array([i[3] for i in minibatch])
        dones = np.array([i[4] for i in minibatch])
        target_q_values = self.q_network.predict(states)
        target_next_q_values = self.target_q_network.predict(next_states)
        for i in range(batch_size):
            if dones[i]:
                target_q_values[i][actions[i]] = rewards[i]
            else:
                target_q_values[i][actions[i]] = rewards[i] + 0.95 * np.max(target_next_q_values[i])
        with tf.GradientTape() as tape:
            q_values = self.q_network(states)
            loss = tf.keras.losses.MSE(target_q_values, q_values)
        gradients = tape.gradient(loss, self.q_network.trainable_variables)
        self.optimizer.apply_gradients(zip(gradients, self.q_network.trainable_variables))
        if self.epsilon > self.epsilon_min:
            self.epsilon *= self.epsilon_decay

    def update_target_network(self, tau):
        """
        更新目标网络参数
        参数:
            tau: 插值参数
        """
        new_weights = []
        q_network_weights = self.q_network.get_weights()
        target_q_network_weights = self.target_q_network.get_weights()
        for i in range(len(q_network_weights)):
            new_weights.append(
                tau * q_network_weights[i] + (1 - tau) * target_q_network_weights[i])
        self.target_q_network.set_weights(new_weights)

# 分配任务函数
def assign_task(task, drones, dqn_agent):
    """
    分配任务给无人机
    参数:
        task: 任务对象
        drones: 无人机列表
        dqn_agent: DQNAgent对象
    返回:
        selected_drone: 选择的无人机对象
    """
    state = []
    max_distance = np.sqrt((100 - 0) ** 2 + (100 - 0) ** 2)  
    for drone in drones:
        drone_state = [
            1 if drone.status == "任务中" else 0,
            1 if drone.status == "充电中" else 0,
            1 if drone.status == "空闲中" else 0,
            1 if drone.status == "故障" else 0,
            drone.position[0] / 100,  
            drone.position[1] / 100,  
            drone.battery / 100,  
            1 if task.content in drone.drone_type else 0,
            np.sqrt((drone.position[0] - task.location[0]) ** 2 + (drone.position[1] - task.location[1]) ** 2) / max_distance  
        ]
        state.extend(drone_state)
    state = np.array(state)
    action = dqn_agent.act(state, drones, task)
    selected_drone = drones[action]
    return selected_drone

# 存储经验函数
def store_experience(experience):
    """
    将经验存储到JSON文件中
    参数:
        experience: 经验元组 (state, action, reward, next_state, done)
    存储路径: exp.json
    返回:
        None
    """
    try:
        with open('exp.json', 'r') as f:
            existing_experience = json.load(f)
    except FileNotFoundError:
        existing_experience = []
    existing_experience.append(experience)
    with open('exp.json', 'w') as f:
        json.dump(existing_experience, f)

# 数据库监听器类
class DatabaseListener:
    def __init__(self):
        self.prev_count = 0
        self.stop_flag = True
        self.lock = threading.Lock()
        self.db_manager = DatabaseConnectionManager()
        self.listen_thread = threading.Thread(target=self.do_listen)
        self.listen_thread.start()

    def start(self):
        self.stop_flag = False

    def stop(self):
        self.stop_flag = True

    def do_listen(self):
        """
        持续监听数据库变化并处理新增任务
        """
        while True:
            if not self.stop_flag:
                try:
                    connection = self.db_manager.get_connection()
                    current_count = self.get_table_record_count(connection)
                    if current_count > self.prev_count:
                        new_tasks = self.get_unassign_tasks(connection)
                        for task in new_tasks:
                            drones = self.get_drones_from_database()
                            state_dim = len(drones) * 9
                            action_dim = len(drones)
                            dqn_agent = DQNAgent(state_dim, action_dim)
                            selected_drone = self.process_new_task(task, drones, dqn_agent)
                            self.update_database(selected_drone, task)
                        with self.lock:
                            self.prev_count = current_count
                    self.db_manager.close_connection()
                except Error as e:
                    print(f"数据库连接或查询出现错误: {e}")
                time.sleep(10)

    def get_table_record_count(self, connection):
        """
        获取任务信息表的记录数量
        参数:
            connection: 数据库连接对象
        返回:
            count: 记录数量
        """
        cursor = connection.cursor()
        try:
            count_query = "SELECT COUNT(*) FROM 任务信息表"
            cursor.execute(count_query)
            result = cursor.fetchone()[0]
            return result
        except Error as e:
            print(f"获取记录数量出现错误: {e}")
            raise
        finally:
            cursor.close()

    def get_unassign_tasks(self, connection):
        """
        获取未分配的任务数据
        参数:
            connection: 数据库连接对象
        返回:
            task: 新增任务数据列表
        """
        cursor = connection.cursor()
        try:
            select_query = "SELECT * FROM 任务信息表 WHERE 任务状态 = '未分配'"
            cursor.execute(select_query)
            new_tasks = cursor.fetchall()
            tasks = []
            for task in new_tasks:
                task_id, task_time, user_id, task_content, task_status = task

                content_parts = task_content.split()
                location_x = float(content_parts[0])
                location_y = float(content_parts[1])
                action_type = ' '.join(content_parts[2:])
#---------------------------------------------------------------------------------------
                print(f"任务ID: {task_id}")
                print(f"任务下达时间: {task_time}")
                print(f"用户ID: {user_id}")
                print(f"位置坐标X: {location_x}, Y: {location_y}")
                print(f"任务类型: {action_type}")
                print(f"任务状态: {task_status}")
#-------------------------------------------------------------------------------------------
                tasks.append(Task(task_id, (location_x, location_y), action_type, task_status))
            return tasks
        except Error as e:
            print(f"获取新增任务数据出现错误: {e}")
            raise
        finally:
            cursor.close()

    def process_new_task(self, task, drones, dqn_agent):
        """
        处理新增任务的逻辑
        """
        return assign_task(task, drones, dqn_agent)

    def get_drones_from_database(self):
        """
        从数据库获取无人机信息
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        try:
            drones_query = "SELECT 无人机信息.无人机ID, 型号, 电量, 位置坐标X, 位置坐标Y,当前动作 FROM 无人机信息 JOIN 无人机状态 ON 无人机信息.无人机ID = 无人机状态.无人机ID"
            cursor.execute(drones_query)
            drones_data = cursor.fetchall()

            drones = []
            for drone_data in drones_data:
                drone_id = drone_data[0]
                drone_type = self.get_drone_type(drone_data[1])
                battery = drone_data[2]
                position = (drone_data[3], drone_data[4])
                status = drone_data[5]
                drones.append(Drone(drone_id, status, position, battery, drone_type))
            return drones
        except Error as e:
            print(f"获取无人机数据出现错误: {e}")
            raise
        finally:
            cursor.close()

    def get_drone_type(self, model):
        if model == 'A':
            return ["浇水"]
        elif model == 'B':
            return ["施肥"]
        elif model == 'C':
            return ["虫害情况勘察", "农作物生长状态勘察"]
        else:
            return []

    def update_database(self, drone, task):
        """
        协调调用各个更新函数，完成整体数据库更新操作
        """
        start_point = drone.position
        end_point = task.location
        planner = UAVPathPlanner()
        shortest_path = planner.find_shortest_path(start_point, end_point, drone.drone_id)
        print(f"无人机 {drone.drone_id} 前往任务 {task.task_id} 的最短路径: {shortest_path}")

        update_manager = UpdateDatabaseManager()
        
        update_manager.insert_drone_task(drone, task)
        update_manager.update_drone_status(drone, task)
        update_manager.update_task_status(task)

        print(f"无人机 {drone.drone_id} 已分配任务 {task.task_id}")


if __name__ == "__main__":
    listener = DatabaseListener()
    listener.start()
    try:
        while True:
            time.sleep(10)
    except KeyboardInterrupt:
        listener.stop()