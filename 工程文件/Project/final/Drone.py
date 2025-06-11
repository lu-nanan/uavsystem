
class Drone:
    """
    无人机类，用于表示无人机的属性和状态
    属性:
        drone_id: 无人机ID
        status: 无人机状态
        position: 无人机位置坐标
        battery: 无人机电量
        drone_type: 无人机类型
    """
    def __init__(self, drone_id, status, position, battery, drone_type):
        self.drone_id = drone_id
        self.status = status
        self.position = position
        self.battery = battery
        self.drone_type = drone_type
