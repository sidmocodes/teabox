import numpy as np
import tensorflow as tf
from tensorflow.keras import layers
from flask import Flask, jsonify, request
import requests

app = Flask(__name__)

# Build a simple Q-network for our RL agent.
def build_q_network(input_shape, output_shape):
    model = tf.keras.Sequential([
        layers.Dense(64, activation='relu', input_shape=input_shape),
        layers.Dense(64, activation='relu'),
        layers.Dense(output_shape, activation='linear')
    ])
    model.compile(optimizer='adam', loss='mse')
    return model

# Define the state shape based on 10 metrics.
state_shape = (10,)
# Allowed caching policies.
action_space = ["LRU", "LFU", "Random", "FIFO", "MRU"]

# Create the Q-network.
q_network = build_q_network(state_shape, len(action_space))

# Function to get the current system metrics from the dummy metrics service.
def get_metrics_state():
    try:
        response = requests.get("http://dummy-metrics-service:5000/api/metrics")
        if response.status_code == 200:
            metrics = response.json()
            # Use a fixed order for the 10 metrics.
            keys = [
                "access_frequency", "average_latency", "cache_hit_ratio",
                "cpu_usage", "memory_usage", "disk_io", "network_io",
                "error_rate", "request_rate", "cache_size"
            ]
            state = [metrics.get(key, 0) for key in keys]
            return np.array(state)
        else:
            return np.random.rand(10)
    except Exception as e:
        print("Error fetching metrics:", e)
        return np.random.rand(10)

# Use an epsilon-greedy strategy to select a caching policy.
def select_policy(state, epsilon=0.1):
    if np.random.rand() < epsilon:
        return np.random.choice(action_space)
    else:
        state = state.reshape(1, -1)
        q_values = q_network.predict(state, verbose=0)
        return action_space[np.argmax(q_values)]

# API endpoint to return a recommended caching policy using live metrics.
@app.route('/api/policy', methods=['GET'])
def get_policy():
    state = get_metrics_state()
    policy = select_policy(state)
    return jsonify(policy)

# Optional endpoint to train the RL model with historical data.
@app.route('/api/train', methods=['POST'])
def train_model():
    data = request.json
    states = np.array(data['states'])
    targets = np.array(data['targets'])
    q_network.fit(states, targets, epochs=10, verbose=0)
    return jsonify({"status": "Model trained successfully"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8081)
