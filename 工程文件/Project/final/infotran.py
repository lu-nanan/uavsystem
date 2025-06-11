import mysql.connector
import threading
import time
import numpy as np
import random
import json
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers, models
from enum import Enum

from database_collection import DatabaseConnectionManager
from update_manager import UpdateDatabaseManager


# 定义无人机类
class Drone:
    def __init__(self, drone_id, model, producer, battery, position, height, current_action):
        self.drone_id = drone_id
        self.model = model
        self.producer = producer
        self.battery = battery
        self.position = position
        self.height = height
        self.current_action = current_action
        self.drone_type = self.get_drone_type()

    def get_drone_type(self):
        if self.model == 'A':
            return ["浇水"]
        elif self.model == 'B':
            return ["施肥"]
        elif self.model == 'C':
            return ["虫害情况勘察", "农作物生长状态勘察"]
        else:
            return []


# 定义任务类
class Task:
    def __init__(self, task_id, task_time, user_id, content, task_status):
        self.task_id = task_id
        self.task_time = task_time
        self.user_id = user_id
        self.content = content
        self.task_status = task_status


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
        存储经验元组
        参数:
            state: 当前状态
            action: 选择的动作
            reward: 奖励值
            next_state: 下一个状态
            done: 是否结束
        """
        self.memory.append((state, action, reward, next_state, done))

    def act(self, state, drones, failed_drone):
        """
        根据当前状态选择动作
        参数:
            state: 当前状态
            drones: 无人机列表，每个元素为无人机对象
            failed_drone: 故障无人机对象
        返回:
            action: 选择的动作
        """
        legal_actions = []
        # 用于存储每个合法无人机的相关信息，格式为 (index, 剩余电量预估, 距离故障无人机距离, 距离中心距离)
        legal_drones_info = []
        center_position = (0, 0)
        for i in range(self.action_dim):
            drone = drones[i]
            if drone.current_action in ["空闲中", "任务中"] and drone.battery > 0 and drone.drone_id!= failed_drone.drone_id:
                legal_actions.append(i)
                # 计算距离故障无人机地点的距离
                distance_to_failed_drone = np.sqrt((drone.position[0] - failed_drone.position[0]) ** 2 + (drone.position[1] - failed_drone.position[1]) ** 2)
                # 计算距离中心的距离
                distance_to_center = np.sqrt((drone.position[0] - center_position[0]) ** 2 + (drone.position[1] - center_position[1]) ** 2)
                # 预估往返消耗电量（假设移动10需要1%的电量，简单计算往返）
                battery_consumption = (distance_to_failed_drone / 10) * 2
                remaining_battery = drone.battery - battery_consumption
                if remaining_battery > 0:
                    legal_drones_info.append((i, remaining_battery, distance_to_failed_drone, distance_to_center))
        if np.random.rand() <= self.epsilon:
            if legal_drones_info:
                # 优先选择剩余电量多、距离故障无人机近且距离中心近的无人机（简单加权方式）
                selected_index = sorted(legal_drones_info, key=lambda x: (-x[1], x[2], x[3]))[0][0]
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
        返回:
            None
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
            tau: 软更新参数
        """
        new_weights = []
        q_network_weights = self.q_network.get_weights()
        target_q_network_weights = self.target_q_network.get_weights()
        for i in range(len(q_network_weights)):
            new_weights.append(
                tau * q_network_weights[i] + (1 - tau) * target_q_network_weights[i])
        self.target_q_network.set_weights(new_weights)


def assign_transfer_task(failed_drone, drones, dqn_agent):
    """
    分配信息传输任务给无人机
    参数:
        failed_drone: 故障无人机对象
        drones: 无人机列表，每个元素为无人机对象
        dqn_agent: DQN智能体对象
    返回:
        selected_drone: 选择的无人机对象
    """
    state = []
    max_distance_to_failed = np.sqrt((100 - 0) ** 2 + (100 - 0) ** 2)  # 假设任务区域范围是0-100坐标
    max_distance_to_center = np.sqrt((100 - 0) ** 2 + (100 - 0) ** 2)
    for drone in drones:
        drone_state = [
            1 if drone.current_action == "任务中" else 0,
            1 if drone.current_action == "充电中" else 0,
            1 if drone.current_action == "空闲中" else 0,
            1 if drone.current_action == "故障" else 0,
            drone.position[0] / 100,  
            drone.position[1] / 100,  
            drone.battery / 100,  
            np.sqrt((drone.position[0] - failed_drone.position[0]) ** 2 + (drone.position[1] - failed_drone.position[1]) ** 2) / max_distance_to_failed,  
            np.sqrt((drone.position[0] - 0) ** 2 + (drone.position[1] - 0) ** 2) / max_distance_to_center  
        ]
        state.extend(drone_state)
    state = np.array(state)
    action = dqn_agent.act(state, drones, failed_drone)
    selected_drone = drones[action]
    return selected_drone


# 存储信息传输经验函数
def store_transfer_experience(experience):
    try:
        with open('transfer_exp.json', 'r') as f:
            existing_experience = json.load(f)
    except FileNotFoundError:
        existing_experience = []
    existing_experience.append(experience)
    with open('transfer_exp.json', 'w') as f:
        json.dump(existing_experience, f)


# 数据库监听器类（处理故障无人机情况）
class DatabaseListenerForFault:
    def __init__(self):
        self.prev_count = 0
        self.stop_flag = True
        self.lock = threading.Lock()
        self.listen_thread = threading.Thread(target=self.do_listen)
        self.listen_thread.start()
        self.db_manager = DatabaseConnectionManager()

    def start(self):
        self.stop_flag = False

    def stop(self):
        self.stop_flag = True

    def do_listen(self):
        """
        持续监听数据库，处理故障无人机信息
        参数：
            connection: 数据库连接对象
        返回：
            None
        """
        while True:
            if not self.stop_flag:
                try:
                    connection = self.db_manager.get_connection()
                    if self.check_failed_drones(connection):
                        failed_drones = self.get_failed_drones_in_task(connection)
                        for failed_drone in failed_drones:
                            drones = self.get_drones_from_database(connection)
                            state_dim = len(drones) * 9
                            action_dim = len(drones)
                            dqn_agent = DQNAgent(state_dim, action_dim)
                            selected_drone = self.process_failed_drone(failed_drone, drones, dqn_agent)
                            self.update_database(selected_drone, failed_drone)
                    connection.close()
                except Exception as e:
                    print(f"数据库连接或查询出现错误: {e}")
                time.sleep(10)

    def check_failed_drones(self, connection):
        """
        检查数据库中是否有故障无人机
        参数：
            connection: 数据库连接对象
        返回：
            has_failed_drones: 是否有故障无人机
        """
        try:
            cursor = connection.cursor()
            cursor.execute("SELECT COUNT(*) FROM 无人机信息 u JOIN 无人机状态 s ON u.无人机ID = s.无人机ID JOIN 无人机任务表 t ON u.无人机ID = t.无人机 JOIN 任务信息表 ti ON t.任务 = ti.任务ID WHERE ti.任务状态 = '进行中' AND s.当前动作 = '故障';")
            result = cursor.fetchone()[0]
            #print(result)
            return result > 0
        except Exception as e:
            print(f"查询故障无人机数量出现错误: {e}")
            raise
        finally:
            cursor.close()


    def get_table_record_count(self, connection):
        """
        获取数据库中记录的数量
        参数：
            connection: 数据库连接对象
        返回：
            count: 记录数量
        """
        cursor = connection.cursor()
        try:
            count_query = "SELECT COUNT(*) FROM 无人机状态"
            cursor.execute(count_query)
            result = cursor.fetchone()[0]
            return result
        except Exception as e:
            print(f"获取记录数量出现错误: {e}")
            raise
        finally:
            cursor.close()

    def get_failed_drones_in_task(self, connection):
        """
        获取在任务中的故障无人机信息
        参数：
            connection: 数据库连接对象
        返回：
            failed_drones: 故障无人机列表，每个元素为无人机对象
        """
        cursor = connection.cursor()
        try:
            select_query = "SELECT u.无人机ID, u.型号, u.生产商, s.电量, s.位置坐标X, s.位置坐标Y, s.高度坐标 FROM 无人机信息 u JOIN 无人机状态 s ON u.无人机ID = s.无人机ID JOIN 无人机任务表 t ON u.无人机ID = t.无人机 JOIN 任务信息表 ti ON t.任务 = ti.任务ID WHERE ti.任务状态 = '进行中' AND s.当前动作 = '故障';"
            cursor.execute(select_query)
            failed_drones_data = cursor.fetchall()
            failed_drones = []
            for data in failed_drones_data:
                drone_id = data[0]
                model = data[1]
                producer = data[2]
                battery = data[3]
                position = (data[4], data[5])
                height = data[6]
                current_action = '故障'
                failed_drones.append(Drone(drone_id, model, producer, battery, position, height, current_action))
            return failed_drones
        except Exception as e:
            print(f"获取故障无人机数据出现错误: {e}")
            raise
        finally:
            cursor.close()

    def process_failed_drone(self, failed_drone, drones, dqn_agent):
        """
        处理故障无人机信息
        参数：
            failed_drone: 故障无人机对象
            drones: 无人机列表，每个元素为无人机对象
            dqn_agent: DQN智能体对象
        返回：
            selected_drone: 选择的无人机对象
        """
        return assign_transfer_task(failed_drone, drones, dqn_agent)

    def get_drones_from_database(self, connection):
        """
        从数据库中获取无人机信息
        参数：
            connection: 数据库连接对象
        返回：
            drones: 无人机列表，每个元素为无人机对象
        """
        cursor = connection.cursor()
        try:
            drones_query = "SELECT 无人机信息.无人机ID, 型号, 生产商, 电量, 位置坐标X, 位置坐标Y, 高度坐标, 当前动作 FROM 无人机信息 JOIN 无人机状态 ON 无人机信息.无人机ID = 无人机状态.无人机ID"
            cursor.execute(drones_query)
            drones_data = cursor.fetchall()
            drones = []
            for data in drones_data:
                drone_id = data[0]
                model = data[1]
                producer = data[2]
                battery = data[3]
                position = (data[4], data[5])
                height = data[6]
                current_action = data[7]
                drones.append(Drone(drone_id, model, producer, battery, position, height, current_action))
            return drones
        except Exception as e:
            print(f"获取无人机数据出现错误: {e}")
            raise
        finally:
            cursor.close()
            connection.close()

    def update_database(self, selected_drone, failed_drone):
        """
        跟新数据库信息，将信息传输任务信息插入到信息转传输表中, 并更新故障无人机状态
        参数：
            selected_drone: 选择的无人机对象
            failed_drone: 故障无人机对象
        """
        current_time = time.strftime("%Y-%m-%d %H:%M:%S")
        update_manager = UpdateDatabaseManager()
        update_manager.insert_transport_info(selected_drone, failed_drone, current_time)
        update_manager.update_failed_drone_status(selected_drone, failed_drone)

        print(f"已安排无人机 {selected_drone.drone_id} 对故障无人机 {failed_drone.drone_id} 进行信息传输")

if __name__ == "__main__":
    listener = DatabaseListenerForFault()
    listener.start()
    try:
        while True:
            time.sleep(10)
    except KeyboardInterrupt:
        listener.stop()