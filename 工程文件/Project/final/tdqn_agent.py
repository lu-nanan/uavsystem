import json
import numpy as np
import random
import tensorflow as tf
from tensorflow.keras import layers, models
from Drone import Drone

class TDQNAgent:
    def __init__(self, state_dim, action_dim):
        self.state_dim = state_dim
        self.action_dim = action_dim
        self.q_network = self.build_network()
        self.target_q_network = self.build_network()
        self.target_q_network.set_weights(self.q_network.get_weights())
        self.optimizer = tf.keras.optimizers.Adam(learning_rate=0.001)
        self.memory = []
        self.epsilon = 1.0
        self.epsilon_min = 0.01
        self.epsilon_decay = 0.995

    def build_network(self):
        state_input = layers.Input(shape=(self.state_dim,))
        x = layers.Dense(64, activation='relu')(state_input)
        x = layers.Dense(64, activation='relu')(x)
        q_value_output = layers.Dense(self.action_dim)(x)
        q_network = models.Model(state_input, q_value_output)
        return q_network

    def remember(self, state, action, reward, next_state, done):
        self.memory.append((state, action, reward, next_state, done))

    def act(self, state, drones, task):
        legal_actions = []
        legal_drones_info = []
        for i in range(self.action_dim):
            drone = drones[i]
            if drone.status == "空闲中" and drone.battery > 0 and task.content in drone.drone_type:
                legal_actions.append(i)
                distance_to_task = np.sqrt((drone.position[0] - task.location[0]) ** 2 + (drone.position[1] - task.location[1]) ** 2)
                battery_consumption = (distance_to_task / 10) * 2
                remaining_battery = drone.battery - battery_consumption
                if remaining_battery > 0:
                    legal_drones_info.append((i, remaining_battery, distance_to_task))
        if np.random.rand() <= self.epsilon:
            if legal_drones_info:
                selected_index = sorted(legal_drones_info, key=lambda x: (-x[1], x[2]))[0][0]
                return selected_index
            return random.choice(legal_actions) if legal_actions else random.randrange(self.action_dim)
        state = np.expand_dims(state, axis=0)
        q_values = self.q_network.predict(state)[0]
        legal_q_values = [q_values[action] for action in legal_actions]
        return legal_actions[np.argmax(legal_q_values)] if legal_actions else np.argmax(q_values)

    def replay(self, batch_size):
        if len(self.memory) < batch_size:
            return
        minibatch = random.sample(self.memory, batch_size)
        states = np.array([i[0] for i in minibatch])
        actions = np.array([i[1] for i in minibatch])
        rewards = np.array([i[2] for i in minibatch])
        next_states = np.array([i[3] for i in minibatch])
        dones = np.array([i[4] for i in minibatch])
        target_q_values = self.q_network.predict(states)
        target_next_q_values = self.target_q_network.predict(next_states)
        for i in range(batch_size):
            if dones[i]:
                target_q_values[i][actions[i]] = rewards[i]
            else:
                target_q_values[i][actions[i]] = rewards[i] + 0.95 * np.max(target_next_q_values[i])
        with tf.GradientTape() as tape:
            q_values = self.q_network(states)
            loss = tf.keras.losses.MSE(target_q_values, q_values)
        gradients = tape.gradient(loss, self.q_network.trainable_variables)
        self.optimizer.apply_gradients(zip(gradients, self.q_network.trainable_variables))
        if self.epsilon > self.epsilon_min:
            self.epsilon *= self.epsilon_decay

    def update_target_network(self, tau):
        new_weights = []
        q_network_weights = self.q_network.get_weights()
        target_q_network_weights = self.target_q_network.get_weights()
        for i in range(len(q_network_weights)):
            new_weights.append(
                tau * q_network_weights[i] + (1 - tau) * target_q_network_weights[i])
        self.target_q_network.set_weights(new_weights)

    def assign_task(self, task, drones):
        state = []
        max_distance = np.sqrt((100 - 0) ** 2 + (100 - 0) ** 2)  
        for drone in drones:
            drone_state = [
                1 if drone.status == "任务中" else 0,
                1 if drone.status == "充电中" else 0,
                1 if drone.status == "空闲中" else 0,
                1 if drone.status == "故障" else 0,
                drone.position[0] / 100,  
                drone.position[1] / 100,  
                drone.battery / 100,  
                1 if task.content in drone.drone_type else 0,
                np.sqrt((drone.position[0] - task.location[0]) ** 2 + (drone.position[1] - task.location[1]) ** 2) / max_distance  
            ]
            state.extend(drone_state)
        state = np.array(state)
        action = self.act(state, drones, task)
        selected_drone = drones[action]
        return selected_drone

    def store_experience(self, experience):
        try:
            with open('exp.json', 'r') as f:
                existing_experience = json.load(f)
        except FileNotFoundError:
            existing_experience = []
        existing_experience.append(experience)
        with open('exp.json', 'w') as f:
            json.dump(existing_experience, f)
