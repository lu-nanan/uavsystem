class IDrone:
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
