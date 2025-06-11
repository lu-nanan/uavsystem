import mysql.connector
import numpy as np
import random
from scipy.spatial import Voronoi, voronoi_plot_2d
from shapely.geometry import Point, Polygon
import matplotlib.pyplot as plt
import math

from database_collection import DatabaseConnectionManager


def is_point_in_polygon(x, y, polygon):
    """
    判断点是否在多边形内部
    :param x: 点的x坐标
    :param y: 点的y坐标
    :param polygon: 多边形的顶点坐标数组
    :return: True表示点在多边形内部，False表示点在多边形外部
    """
    point = Point(x, y)
    poly = Polygon(polygon)
    return poly.contains(point)


class VoronoiDataProcessor:
    """
    用于处理Voronoi图数据的类
    """
    def __init__(self):
        """
        初始化方法，创建数据库连接管理对象
        :param db_manager: 数据库连接管理对象
        """
        self.db_manager = DatabaseConnectionManager()

    def generate_seed_points(self, num_plots):
        """
        随机生成指定数量的种子点坐标
        :param num_plots: 要生成的种子点数量
        """
        seed_points = []
        for _ in range(num_plots):
            x = random.randint(0, 99)
            y = random.randint(0, 99)
            seed_points.append([x, y])
        return np.array(seed_points)

    def generate_voronoi(self, points):
        """
        根据已有的种子点生成Voronoi图（泰森多边形）
        :param points: 种子点坐标数组
        :return: 生成的Voronoi图对象
        """
        return Voronoi(points)

    def visualize_voronoi(self, vor, points):
        """
        使用matplotlib可视化展示Voronoi图以及种子点（地块中心点）
        :param vor: Voronoi图对象
        :param points: 种子点坐标数组
        """
        fig, ax = plt.subplots(figsize=(10, 10))
        voronoi_plot_2d(vor, ax=ax, show_vertices=False)
        ax.scatter(points[:, 0], points[:, 1], c='red', marker='x', label="地块中心点")
        ax.legend()
        plt.show()

    def insert_data_into_database(self, vor):
        """
        遍历二维平面区域，将每个点对应的信息插入到数据库中（根据其所在的Voronoi区域确定地块id等信息）
        :param vor: Voronoi图对象
        """
        i = 1
        sql = "INSERT INTO 地图数据表 (x, y, 描述, 是否可飞越, 是否可停靠, 地块id) VALUES (%s, %s, %s, %s, %s, %s)"
        cursor = self.db_manager.get_cursor()
        if cursor is None:
            return
        try:
            for x in range(100):
                for y in range(100):
                    description = "测试点" + str(i)
                    is_flyable = 1
                    is_dockable = random.randint(0, 1)

                    plot_id = None
                    for region_idx, region in enumerate(vor.regions):
                        if -1 in region:
                            continue
                        polygon = [vor.vertices[i] for i in region]

                        if is_point_in_polygon(x, y, polygon):
                            plot_id = region_idx + 1001
                            if plot_id > 1010:
                                continue
                            break

                    if plot_id is None:
                        continue

                    val = (x, y, description, is_flyable, is_dockable, plot_id)
                    cursor.execute(sql, val)
                    i += 1
            self.db_manager.connection.commit()
        except mysql.connector.Error as err:
            print(f"插入数据到数据库时出错: {err}")
        finally:
            self.db_manager.close_connection()

# -----------------------------------------------------
"""
for x in range(100):
    for y in range(100):
        description = "测试点" + str(i)
        is_flyable = 1
        is_dockable = random.randint(0, 1)
        plot_id = random.randint(1001, 1004)
        val = (x, y, description, is_flyable, is_dockable, plot_id)
        mycursor.execute(sql, val)
        i += 1
"""         
# ------------------------------------------------------


if __name__ == "__main__":
    processor = VoronoiDataProcessor()
    points = processor.generate_seed_points(11)
    vor = processor.generate_voronoi(points)
    processor.visualize_voronoi(vor, points)
    processor.insert_data_into_database(vor)
    

