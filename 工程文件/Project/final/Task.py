
class Task:
    """
    任务类，用于存储任务信息
    属性:
        task_id: 任务ID
        location: 任务位置
        content: 任务内容
        deadline: 任务截止时间
    """
    def __init__(self, task_id, location, content, deadline):
        self.task_id = task_id
        self.location = location
        self.content = content
        self.deadline = deadline