import time
import threading
from tdatabase_listener import TDatabaseListener
from idatabase_listener import IDatabaseListener


class DatabaseListenerManager:
    def __init__(self):
        self.listener_t = TDatabaseListener()
        self.listener_i = IDatabaseListener()
        self.lock = threading.Lock()

    def start_listeners(self):
        thread_t = threading.Thread(target=self.listener_t.start)
        thread_i = threading.Thread(target=self.listener_i.start)

        thread_t.start()
        thread_i.start()

        thread_t.join()
        thread_i.join()

    def stop_listeners(self):
        self.listener_t.stop()
        self.listener_i.stop()


if __name__ == "__main__":
    listener_manager = DatabaseListenerManager()
    try:
        listener_manager.start_listeners()
    except KeyboardInterrupt:
        listener_manager.stop_listeners()  # 
        print("监听器已停止。")


"""
if __name__ == "__main__":
    listener = TDatabaseListener()
    listener.start()
    try:
        while True:
            time.sleep(10)
    except KeyboardInterrupt:
        listener.stop()


if __name__ == "__main__":
    listener = IDatabaseListener()
    listener.start()
    try:
        while True:
            time.sleep(10)
    except KeyboardInterrupt:
        listener.stop()
"""



