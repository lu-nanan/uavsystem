import mysql.connector

class DatabaseConnectionManager:
    def __init__(self):
        """
        初始化数据库连接相关配置
        :param host: 数据库主机地址
        :param user: 数据库用户名
        :param password: 数据库用户密码
        :param database: 数据库名称
        """
        self.host = "localhost"
        self.user = "root"
        self.password = "123456"
        self.database = "uavsystem"
        self.connection = None

    def get_connection(self):
        """
        获取数据库连接对象，如果连接不存在则创建一个新的连接
        :return: 数据库连接对象
        """
        if self.connection is None or not self.connection.is_connected():
            try:
                self.connection = mysql.connector.connect(
                    host=self.host,
                    user=self.user,
                    password=self.password,
                    database=self.database
                )
                print("成功建立数据库连接")
            except mysql.connector.Error as err:
                print(f"连接数据库时出错: {err}")
        return self.connection

    def close_connection(self):
        """
        关闭数据库连接
        """
        if self.connection is not None and self.connection.is_connected():
            try:
                self.connection.close()
                print("数据库连接已关闭")
            except mysql.connector.Error as err:
                print(f"关闭数据库连接时出错: {err}")
            finally:
                self.connection = None

    def get_cursor(self):
        """
        获取数据库游标对象，基于当前的数据库连接
        :return: 数据库游标对象
        """
        connection = self.get_connection()
        try:
            cursor = connection.cursor()
            return cursor
        except mysql.connector.Error as err:
            print(f"获取游标时出错: {err}")
            return None
        
