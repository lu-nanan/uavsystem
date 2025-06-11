import mysql.connector
import numpy as np
import random
from scipy.spatial import Voronoi, voronoi_plot_2d
from shapely.geometry import Point, Polygon
import matplotlib.pyplot as plt
import math


def is_point_in_polygon(x, y, polygon):
    # 使用 Shapely 库来判断点是否在多边形内部
    point = Point(x, y)
    poly = Polygon(polygon)
    return poly.contains(point)


# 连接到MySQL数据库
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="123456",
    database="uavsystem"
)

mycursor = mydb.cursor()

num_plots = 10  # 限制地块数量小于等于10个
seed_points = []

# 随机生成种子点
for _ in range(num_plots):
    x = random.randint(0, 99)
    y = random.randint(0, 99)
    seed_points.append([x, y])

points = np.array(seed_points)

vor = Voronoi(points)

fig, ax = plt.subplots(figsize=(10, 10))
voronoi_plot_2d(vor, ax=ax, show_vertices=False)

ax.scatter(points[:, 0], points[:, 1], c='red', marker='x', label="地块中心点")
ax.legend()
plt.show()

i = 1

sql = "INSERT INTO 地图数据表 (x, y, 描述, 是否可飞越, 是否可停靠, 地块id) VALUES (%s, %s, %s, %s, %s, %s)"

for x in range(100):
    for y in range(100):
        description = "测试点" + str(i)
        is_flyable = 1
        is_dockable = random.randint(0, 1)

        # 查找点 (x, y) 属于哪个 Voronoi 区域
        plot_id = None
        for region_idx, region in enumerate(vor.regions):
            if -1 in region:  # 忽略无效区域（外部区域）
                continue
            polygon = [vor.vertices[i] for i in region]
            # 使用点在多边形内部检查
            if is_point_in_polygon(x, y, polygon):
                plot_id = region_idx + 1001  # 地块 ID 从 1001 开始
                if plot_id > 1010:  # 确保地块id不超过10个地块对应的范围
                    continue
                break

        # 如果点不在任何区域内，则跳过
        if plot_id is None:
            continue

        # 准备插入数据
        val = (x, y, description, is_flyable, is_dockable, plot_id)

        # 执行插入数据库操作
        mycursor.execute(sql, val)
        i += 1

mydb.commit()
mycursor.close()
mydb.close()