import threading
import time
import random
from flask import Flask, jsonify

app = Flask(__name__)
metrics = {}

# Background thread to update metrics every 10 seconds.
def update_metrics():
    global metrics
    while True:
        metrics = {
            "access_frequency": random.uniform(0, 100),
            "average_latency": random.uniform(0, 500),  # in milliseconds
            "cache_hit_ratio": random.uniform(0, 1),
            "cpu_usage": random.uniform(0, 100),        # in percent
            "memory_usage": random.uniform(0, 100),     # in percent
            "disk_io": random.uniform(0, 1000),
            "network_io": random.uniform(0, 1000),
            "error_rate": random.uniform(0, 0.1),
            "request_rate": random.uniform(0, 200),
            "cache_size": random.uniform(0, 10000)
        }
        time.sleep(10)  # Update every 10 seconds.

@app.route('/api/metrics', methods=['GET'])
def get_metrics():
    return jsonify(metrics)

if __name__ == '__main__':
    # Start the background metrics updater.
    t = threading.Thread(target=update_metrics)
    t.daemon = True
    t.start()
    app.run(host='0.0.0.0', port=5000)
