from flask import Flask, request

app = Flask(__name__)

@app.route("/log", methods=["POST"])
def log_event():
    data = request.json
    print("📥 Получен лог:", data)
    return {"status": "ok"}, 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5050)
