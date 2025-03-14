from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/api/content', methods=['GET'])
def get_content():
    key = request.args.get('key', 'default')
    # Generate dummy content based on the key.
    content = f"This is dummy content for key '{key}'"
    return jsonify(content)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)
