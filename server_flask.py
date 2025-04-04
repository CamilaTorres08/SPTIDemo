from transformers import AutoModelForSequenceClassification, AutoTokenizer
import torch

# Nombre del modelo en Hugging Face
model_name = "ealvaradob/bert-finetuned-phishing"

# Cargar el tokenizador y el modelo
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name)

# Enviar el modelo a la GPU si está disponible
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model.to(device)

# Función para clasificar el texto
def classify_text(text):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True)
    inputs = {key: val.to(device) for key, val in inputs.items()}  # Mover a GPU si es necesario

    with torch.no_grad():
        outputs = model(**inputs)

    logits = outputs.logits
    probabilities = torch.nn.functional.softmax(logits, dim=-1).cpu().numpy()[0]

    # Convertir las probabilidades a float para evitar el error de serialización JSON
    probabilities = probabilities.astype(float)

    # Definir etiquetas (ajusta según el modelo)
    labels = ["No Phishing", "Phishing"]

    predicted_class = int(torch.argmax(logits, dim=-1).item())
    predicted_label = labels[predicted_class]

    return predicted_label, probabilities


from flask import Flask, request, jsonify
from threading import Thread

# Inicialización de Flask
app = Flask(__name__)

# Ruta principal para la predicción
@app.route('/nlp/phishing/analyze', methods=['POST'])
def predict():
    # Obtener texto desde la solicitud
    data = request.get_json()
    text = data.get('content', '')

    if not text:
        return jsonify({"error": "No text provided"}), 400

    # Realizar la predicción
    predicted_label, probabilities = classify_text(text)

    # Responder con la predicción y las probabilidades
    response = {
        "prediction": predicted_label,
        "probabilities": {
            "No Phishing": round(probabilities[0], 4),
            "Phishing": round(probabilities[1], 4)
        }
    }

    return jsonify(response)

if __name__ == '__main__':
    app.run(debug=True, use_reloader=False)  # use_reloader=False para evitar que se reinicie dos veces

