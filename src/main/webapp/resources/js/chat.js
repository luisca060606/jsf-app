let socket;
const miNombre = miNombreChat;

function conectar() {
  const protocol = window.location.protocol === "https:" ? "wss://" : "ws://";
  const cp = miContextPath;
  const endpointURL = protocol + window.location.host + cp + "/chatServer";

  console.log("Intentando conectar a:", endpointURL);
  socket = new WebSocket(endpointURL);

  socket.onopen = () => console.log("✅ Conexión establecida con el servidor");

  socket.onmessage = function (event) {
    const chatBox = document.getElementById("chatBox");
    const data = event.data;
    const esMio = data.includes("<strong>" + miNombre + ":</strong>");
    const claseBubble = esMio ? "msg-me" : "msg-others";
    const htmlMensaje = `<div class="msg ${claseBubble}">${data}</div>`;

    chatBox.innerHTML += htmlMensaje;

    let historial = sessionStorage.getItem("chat_history") || "";
    sessionStorage.setItem("chat_history", historial + htmlMensaje);
    chatBox.scrollTop = chatBox.scrollHeight;
  };

  socket.onerror = (error) => console.error("❌ Error en WebSocket:", error);
  socket.onclose = () => console.warn("🔌 Conexión cerrada. Reintentando...");
}

function enviar() {
  const input = document.getElementById("mensajeInput");
  const texto = input.value.trim();

  if (socket && socket.readyState === WebSocket.OPEN) {
    if (texto !== "") {
      socket.send("<strong>" + miNombre + ":</strong> " + texto);
      input.value = "";
    }
  } else {
    console.error("No se puede enviar: El socket está cerrado.");
    conectar();
  }
}

window.addEventListener('DOMContentLoaded', () => {
  conectar();

  const input = document.getElementById("mensajeInput");
  input.addEventListener("keypress", (e) => {
    if (e.key === "Enter") { e.preventDefault(); enviar(); }
  });

  const h = sessionStorage.getItem("chat_history");
  if (h) document.getElementById("chatBox").innerHTML = h;
});

function toggleChat() {
  const content = document.getElementById('chat-content');
  content.style.display = (content.style.display === 'none' || content.style.display === '') ? 'flex' : 'none';
  document.getElementById("chatBox").scrollTop = chatBox.scrollHeight;
}