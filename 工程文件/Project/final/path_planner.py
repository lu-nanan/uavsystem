import mysql.connector
from collections import deque
from database_collection import DatabaseConnectionManager
from update_manager import UpdateDatabaseManager


class UAVPathPlanner:
    def __init__(self):
        """
        初始化数据库连接相关配置，并建立连接和游标
        """
        self.db_manager = DatabaseConnectionManager()
        self.mydb = self.db_manager.get_connection()
        self.mycursor = self.mydb.cursor()

    def get_map_data(self):
        """
        从数据库中获取地图数据（X, Y, 是否可飞越, 是否可停靠, 地块id）
        返回值:
            list: 包含地图数据的列表，每个元素为一条记录对应的元组
        """
        sql = "SELECT X, Y, 是否可飞越, 是否可停靠, 地块id FROM 地图数据表"
        self.mycursor.execute(sql)
        return self.mycursor.fetchall()

    def is_valid_move(self, x, y, map_data):
        """
        判断给定坐标点是否是有效可移动的（在地图范围内且可飞越）
        参数:
            x (int): 横坐标
            y (int): 纵坐标
            map_data (list): 地图数据列表
        返回值:
            bool: 如果是有效可移动的点返回True，否则返回False
        """
        for data in map_data:
            data_x, data_y, can_fly, _, _ = data
            if data_x == x and data_y == y and can_fly == 1:
                return True
        return False

    def get_neighbors(self, x, y, map_data):
        """
        获取给定坐标点的上下左右四个相邻的有效可移动的邻居坐标
        参数:
            x (int): 当前点横坐标
            y (int): 当前点纵坐标
            map_data (list): 地图数据列表
        返回值:
            list: 包含有效邻居坐标的列表，每个元素为 (x, y) 形式的元组
        """
        neighbors = []
        directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]  # 上下左右四个方向
        for dx, dy in directions:
            new_x = x + dx
            new_y = y + dy
            if self.is_valid_move(new_x, new_y, map_data):
                neighbors.append((new_x, new_y))
        return neighbors

    def find_shortest_path(self, start, end, drone_id):
        """
        使用广度优先搜索（BFS）算法规划从起点到终点的最短路径
        参数:
            start (tuple): 起点坐标 (x, y)
            end (tuple): 终点坐标 (x, y)
        返回值:
            str: 以字符串形式表示的路径信息，如果找不到路径则返回提示信息
        """
        map_data = self.get_map_data()
        queue = deque([(start, [])])  
        visited = set()  

        while queue:
            current, path = queue.popleft()
            if current == end:
                path_str = f"{' -> '.join([str(point) for point in path + [current]])}"
                
                updatemanager = UpdateDatabaseManager()
                
                updatemanager.insert_path(drone_id, start, end, path_str)

                return path_str
            
            if current in visited:
                continue
            visited.add(current)
            neighbors = self.get_neighbors(current[0], current[1], map_data)
            for neighbor in neighbors:
                queue.append((neighbor, path + [current]))

        return "无法找到从起点到终点的有效路径"

    def close_connection(self):
        """
        关闭游标和数据库连接，释放资源
        """
        self.mycursor.close()
        self.mydb.close()


# 测试
if __name__ == "__main__":
    start_point = (0, 0)
    end_point = (5, 5)  
    planner = UAVPathPlanner()
    drone_id = 1
    shortest_path = planner.find_shortest_path(start_point, end_point,drone_id)
    print(shortest_path)
    planner.close_connection()