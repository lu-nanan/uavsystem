import mysql
from database_collection import DatabaseConnectionManager

class UpdateDatabaseManager:
    """
    数据库更新管理器类
    属性：
        db_manager: 数据库连接管理器对象
    方法：
        insert_drone_task: 向无人机任务表插入无人机和任务关联记录
        update_drone_status: 更新无人机状态表中对应无人机的位置坐标和当前动作状态
        update_task_status: 更新任务信息表中对应任务的任务状态为“进行中”
        insert_path: 向无人机路径表插入无人机和路径关联记录
    """
    def __init__(self):
        self.db_manager = DatabaseConnectionManager()

    def insert_drone_task(self, drone, task):
        """
        向无人机任务表插入无人机和任务关联记录
        参数:
            drone: 无人机对象
            task: 任务对象
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        insert_query = "INSERT INTO 无人机任务表 (无人机, 任务) VALUES (%s, %s)"
        try:
            cursor.execute(insert_query, (drone.drone_id, task.task_id))
            connection.commit()
            print(f"成功向无人机任务表插入任务 {task.task_id} 与无人机 {drone.drone_id} 的关联记录")
        except mysql.connector.Error as e:
            print(f"向无人机任务表插入记录时出错: {e}")
            connection.rollback()
        finally:
            cursor.close()
            connection.close()

    def update_drone_status(self, drone, task):
        """
        更新无人机状态表中对应无人机的位置坐标和当前动作状态
        参数:
            drone: 无人机对象
            task: 任务对象
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        update_uav_query = "UPDATE 无人机状态 SET 位置坐标X = %s, 位置坐标Y = %s, 当前动作 = '任务中' WHERE 无人机ID = %s"
        try:
            cursor.execute(update_uav_query, (task.location[0], task.location[1], drone.drone_id))
            connection.commit()
            print(f"成功更新无人机 {drone.drone_id} 在无人机状态表中的状态信息")
        except mysql.connector.Error as e:
            print(f"更新无人机状态表时出错: {e}")
            connection.rollback()
        finally:
            cursor.close()
            connection.close()

    def update_task_status(self, task):
        """
        更新任务信息表中对应任务的任务状态为“进行中”
        参数:
            task: 任务对象
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        update_task_query = "UPDATE 任务信息表 SET 任务状态 = '进行中' WHERE 任务ID = %s"
        try:
            cursor.execute(update_task_query, (task.task_id,))
            connection.commit()
            print(f"成功更新任务 {task.task_id} 在任务信息表中的状态信息")
        except mysql.connector.Error as e:
            print(f"更新任务信息表时出错: {e}")
            connection.rollback()
        finally:
            cursor.close()
            connection.close()
    
    def insert_path(self, drone_id, start, end, path):
        """
        向无人机路径表插入无人机和路径关联记录
        参数:
            drone_id: 无人机ID
            start: 起点坐标
            end: 终点坐标
            path: 路径
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        insert_query = "INSERT INTO 无人机路径信息表 (无人机, 起点X, 起点Y, 终点X, 终点Y, 路径) VALUES (%s, %s, %s, %s, %s, %s)"
        try:
            cursor.execute(insert_query, (drone_id, start[0], start[1], end[0], end[1], path))
            connection.commit()
            print(f"成功向无人机路径表插入路径信息")
        except mysql.connector.Error as e:
            print(f"向无人机路径表插入路径信息时出错: {e}")
            connection.rollback()
        finally:
            cursor.close()
            connection.close()

    def insert_transport_info(self, failed_drone, selected_drone, current_time):
        """
        向信息转传输表中插入信息转传输的记录
        参数:
            failed_drone: 故障无人机对象
            selected_drone: 选择的无人机对象
            current_time: 当前时间
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        insert_query = "INSERT INTO 信息转传输情况表 (发送方, 转送方, 时间, 是否成功传送给中心) VALUES (%s, %s, %s, %s)"
        try:
            cursor.execute(insert_query, (failed_drone.drone_id, selected_drone.drone_id, current_time, 1))
            connection.commit()
            print(f"成功向信息转传输表插入信息转传输记录")
        except mysql.connector.Error as e:
            print(f"向信息转传输表插入信息转传输记录时出错: {e}")
            connection.rollback()
        finally:
            cursor.close()
            connection.close()

    def update_failed_drone_status(self, select_drone,failed_drone):
        """
        更新无人机状态表中对应无人机的当前动作状态
        参数:
            failed_drone: 故障无人机对象
        """
        connection = self.db_manager.get_connection()
        cursor = connection.cursor()
        update_uav_query = "UPDATE 无人机状态 SET 当前动作 = '故障,数据由%s传输到中心' WHERE 无人机ID = %s"
        try:
            cursor.execute(update_uav_query, (select_drone.drone_id, failed_drone.drone_id,))
            connection.commit()
            print(f"成功更新无人机 {failed_drone.drone_id} 在无人机状态表中的状态信息")
        except mysql.connector.Error as e:
            print(f"更新无人机状态表时出错: {e}")
            connection.rollback()
        finally:
            cursor.close()
            connection.close()