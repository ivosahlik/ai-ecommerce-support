import React, { useState, useRef, useEffect } from "react";
import useStompWebSocket from "../hook/useStompWebSocket";
import { FaPaperPlane } from "react-icons/fa";
import { sendMessage } from "../service/apiService";

const Chat = () => {
  const [sessionId, setSessionId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [waitingForAI, setWaitingForAI] = useState(false);
  const messagesEndRef = useRef(null);

  // Use Stomp Websocket hook to recieve message asynchronously from the AI(assistant)
  const { messages: wsMessages } = useStompWebSocket(sessionId);

  // Generate a or load a sessionId for the chat when the component mounts
  useEffect(() => {
    let chatId = localStorage.getItem("sessionId");
    if (!chatId) {
      chatId = crypto.randomUUID();
      localStorage.setItem("sessionId", chatId);
    }
    setSessionId(chatId);
  }, []);

  // Combine the local messages with the messages from the websocket
  useEffect(() => {
    if (!wsMessages.length) return;
    setMessages((prev) => {
      //Filter out the wsMessages that are already in the local messages
      const newWs = wsMessages.filter(
        (wsMsg) =>
          !prev.some(
            (msg) => msg.content === wsMsg.content && msg.role === wsMsg.role
          )
      );
      if (newWs.length > 0) setWaitingForAI(false);
      return [...prev, ...newWs];
    });
  }, [wsMessages]);

  const chatWithAI = async () => {
    if (!message.trim()) return;
    setMessages((prev) => [...prev, { role: "user", content: message }]);
    setWaitingForAI(true);
    try {
      const response = await sendMessage(sessionId, message);
      setMessages((prev) => [
        ...prev,
        { role: "assistant", content: response },
      ]);
      setWaitingForAI(false);
    } catch (error) {
      console.error(error);
      setMessages((prev) => [
        ...prev,
        { role: "assistant", content: "Error: Unable to send message." },
      ]);
      setWaitingForAI(false);
    }
    setMessage("");
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      chatWithAI();
    }
  };

  // Scroll to the bottom of the chat when new messages are added
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, waitingForAI]);

  // Find if the last message is from the user
  const lastMsg = messages[messages.length - 1];
  const showTyping = waitingForAI && lastMsg && lastMsg.role === "user";

  return (
    <main className='support-page mt-5'>
      <main className='main-content'>
        <section className='chat-section'>
          <div className='chat-container'>
            <div className='chat-messages'>
              {messages.map((msg, i) => (
                <div
                  key={i}
                  className={`message ${
                    msg.role === "user" ? "user" : "assistant"
                  }`}>
                  {msg.content}
                </div>
              ))}

              {showTyping && (
                <div className='ai-typing-indicator'>
                  <strong>Ecommerce Support: </strong>
                  <em> is typing...</em>
                </div>
              )}

              <div ref={messagesEndRef} />
            </div>

            <div className='chat-input-container'>
              <div className='input-with-button'>
                <textarea
                  rows={2}
                  className='chat-textarea'
                  placeholder='Type your message...'
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  onKeyDown={handleKeyPress}
                />

                <button
                  className='send-button'
                  onClick={chatWithAI}
                  disabled={!message.trim()}
                  aria-label='Send message'>
                  <FaPaperPlane size={20} />
                </button>
              </div>
            </div>
          </div>
        </section>
      </main>
    </main>
  );
};

export default Chat;
