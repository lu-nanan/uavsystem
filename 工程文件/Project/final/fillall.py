import mysql.connector
import random


# 连接数据库
def connect_database():
    try:
        connection = mysql.connector.connect(
            host='localhost',
            user='root',
            password='123456',
            database='uavsystem'
        )
        return connection
    except Exception as e:
        print(e)
        return None


# 检查并插入数据
def check_and_insert_data():
    connection = connect_database()
    if connection:
        try:
            cursor = connection.cursor()
            for x in range(0, 101):
                for y in range(0, 101):
                    # 查询数据库中是否存在该点
                    sql = "SELECT * FROM 地图数据表 WHERE x = %s AND y = %s"
                    cursor.execute(sql, (x, y))
                    result = cursor.fetchone()
                    if not result:
                        # 随机生成是否可停靠
                        is_dockable = random.randint(0, 1)
                        # 根据坐标确定地块id
                        if x > 50 and y > 50:
                            block_id = 1004
                        elif x > 50 and 0 <= y <= 50:
                            block_id = 1010
                        elif x <= 50 and y <= 50:
                            block_id = 1002
                        else:
                            block_id = 1009
                        # 插入数据
                        insert_sql = "INSERT INTO 地图数据表 (X, Y, 描述, 是否可飞越, 是否可停靠, 地块id) VALUES (%s, %s, %s, %s, %s, %s)"
                        cursor.execute(insert_sql, (x, y, "测试数据", 1, is_dockable, block_id))
            connection.commit()
        except Exception as e:
            print(e)
        finally:
            connection.close()


if __name__ == "__main__":
    check_and_insert_data()

    
    """
    def connect_database():
    try:
        connection = pymysql.connect(
            host='localhost',
            port=3306,
            user='cyx1',
            password='Cyxxyy17',
            database='uavdb'
        )
        return connection
    except Exception as e:
        print(e)
        return None

    
    """
