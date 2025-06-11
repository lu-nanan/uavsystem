class ITask:
    def __init__(self, task_id, task_time, user_id, content, task_status):
        self.task_id = task_id
        self.task_time = task_time
        self.user_id = user_id
        self.content = content
        self.task_status = task_status
