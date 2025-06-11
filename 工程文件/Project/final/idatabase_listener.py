import threading
import time
from idqn_agent import IDQNAgent
from IDrone import IDrone
from ITask import ITask
from database_collection import DatabaseConnectionManager
from update_manager import UpdateDatabaseManager

class IDatabaseListener:
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
                            dqn_agent = IDQNAgent(state_dim, action_dim)
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
                failed_drones.append(IDrone(drone_id, model, producer, battery, position, height, current_action))
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
        return dqn_agent.assign_transfer_task(failed_drone, drones)

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
                drones.append(IDrone(drone_id, model, producer, battery, position, height, current_action))
            return drones
        except Exception as e:
            print(f"获取无人机数据出现错误: {e}")
            raise
        finally:
            cursor.close()
            connection.close()

    def update_database(self, selected_drone, failed_drone):
        current_time = time.strftime("%Y-%m-%d %H:%M:%S")
        update_manager = UpdateDatabaseManager()
        update_manager.insert_transport_info(selected_drone, failed_drone, current_time)
        update_manager.update_failed_drone_status(selected_drone, failed_drone)
        print(f"已安排无人机 {selected_drone.drone_id} 对故障无人机 {failed_drone.drone_id} 进行信息传输")
