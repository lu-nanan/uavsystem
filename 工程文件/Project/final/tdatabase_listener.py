import threading
import time
from mysql.connector import Error

from database_collection import DatabaseConnectionManager
from tdqn_agent import TDQNAgent
from Task import Task
from Drone import Drone
from update_manager import UpdateDatabaseManager
from path_planner import UAVPathPlanner

class TDatabaseListener:
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
                            dqn_agent = TDQNAgent(state_dim, action_dim)
                            selected_drone = self.process_new_task(task, drones, dqn_agent)
                            self.update_database(selected_drone, task)
                        with self.lock:
                            self.prev_count = current_count
                    self.db_manager.close_connection()
                except Error as e:
                    print(f"数据库连接或查询出现错误: {e}")
                time.sleep(10)

    def get_table_record_count(self, connection):
        cursor = connection.cursor()
        count_query = "SELECT COUNT(*) FROM 任务信息表"
        cursor.execute(count_query)
        result = cursor.fetchone()[0]
        return result

    def get_unassign_tasks(self, connection):
        cursor = connection.cursor()
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
            tasks.append(Task(task_id, (location_x, location_y), action_type, task_status))
        return tasks

    def process_new_task(self, task, drones, dqn_agent):
        return dqn_agent.assign_task(task, drones)

    def get_drones_from_database(self):
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
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
            print(drones_data)
        return drones

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
