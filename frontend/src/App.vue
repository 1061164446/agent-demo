<template>
  <div class="app">
    <div class="header">
      <div class="logo">AI 聊天助手</div>
    </div>
    <div class="chat">
      <div class="chat-container">
        <!-- 聊天消息区域 -->
        <div class="messages" ref="messagesContainer">
          <div v-if="messages.length === 0" class="welcome">
            <div class="welcome-icon">👋</div>
            <div class="welcome-text">有什么可以帮你的吗？</div>
          </div>
          <div v-for="(msg, index) in messages" 
               :key="index" 
               class="message"
               :class="msg.role">
            <div class="avatar">
              <span v-if="msg.role === 'user'">我</span>
              <span v-else>AI</span>
            </div>
            <div class="content">
              <div v-if="loading && index === messages.length - 1 && msg.role === 'assistant'" class="typing-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>
              <template v-else>
                {{ msg.content }}
              </template>
            </div>
          </div>
        </div>

        <!-- 输入框区域 -->
        <div class="input-box">
          <textarea
            v-model="input"
            placeholder="输入消息..."
            @keydown.enter.exact.prevent="sendMessage"
            @keydown.enter.shift.exact="newLine"
            ref="inputArea"
            rows="1"
          ></textarea>
          <button @click="sendMessage" :disabled="loading || !input.trim()">
            <svg v-if="!loading" viewBox="0 0 24 24" class="send-icon">
              <path fill="currentColor" d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
            </svg>
            <div v-else class="loading"></div>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      messages: [],
      input: '',
      loading: false
    }
  },
  async created() {
    await this.loadChatHistory()
  },
  methods: {
    async loadChatHistory() {
      try {
        const response = await fetch('/api/chat/history')
        const data = await response.json()
        
        if (data.success && data.data) {
          this.messages = data.data
        }
      } catch (error) {
        console.error('Failed to load chat history:', error)
      }
    },
    async sendMessage() {
      if (!this.input.trim() || this.loading) return
      
      const userInput = this.input.trim()
      
      // 添加用户消息
      const userMessage = {
        role: 'user',
        content: userInput,
        timestamp: Date.now()
      }
      this.messages = [...this.messages, userMessage]
      
      // 添加 AI 消息占位
      const assistantMessage = {
        role: 'assistant',
        content: '',
        timestamp: Date.now()
      }
      this.messages = [...this.messages, assistantMessage]
      
      // 清空输入框并滚动到底部
      this.input = ''
      this.loading = true
      this.$nextTick(() => {
        this.scrollToBottom()
      })
      
      try {
        // 调用后端 API
        const response = await fetch('/api/chat/send', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            content: userInput
          })
        })
        
        const data = await response.json()
        console.log('API Response:', data)
        
        if (data.success && data.data) {
          // 更新 AI 回复
          const lastMessage = this.messages[this.messages.length - 1]
          lastMessage.content = data.data.content
          lastMessage.timestamp = data.data.timestamp || Date.now()
          this.messages = [...this.messages]
        } else {
          throw new Error(data.message || '服务器响应错误')
        }
      } catch (error) {
        console.error('Error:', error)
        const lastMessage = this.messages[this.messages.length - 1]
        lastMessage.content = '抱歉，服务出现了问题，请稍后再试。'
      } finally {
        this.loading = false
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },
    newLine(e) {
      e.preventDefault()
      this.input += '\n'
    },
    scrollToBottom() {
      const container = this.$refs.messagesContainer
      container.scrollTop = container.scrollHeight
    },
    adjustTextareaHeight() {
      const textarea = this.$refs.inputArea
      textarea.style.height = 'auto'
      textarea.style.height = textarea.scrollHeight + 'px'
    }
  },
  watch: {
    input() {
      this.$nextTick(() => {
        this.adjustTextareaHeight()
      })
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f7;
}

.header {
  height: 60px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
}

.logo {
  font-size: 18px;
  font-weight: 500;
  color: #1d1d1f;
  display: flex;
  align-items: center;
  gap: 8px;
  letter-spacing: -0.5px;
}

.logo::before {
  content: "🤖";
  font-size: 22px;
}

.chat {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 20px;
  min-height: calc(100vh - 60px);
}

.chat-container {
  width: 100%;
  max-width: 800px;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  scroll-behavior: smooth;
}

.welcome {
  text-align: center;
  padding: 40px 0;
  color: #86868b;
}

.welcome-icon {
  font-size: 32px;
  margin-bottom: 16px;
}

.welcome-text {
  font-size: 16px;
  font-weight: 400;
}

.message {
  margin: 20px 0;
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  flex-shrink: 0;
  font-weight: 500;
}

.user {
  flex-direction: row-reverse;
}

.user .avatar {
  background: #0071e3;
  color: white;
}

.assistant .avatar {
  background: #f5f5f7;
  color: #1d1d1f;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.message .content {
  max-width: calc(100% - 60px);
  padding: 12px 16px;
  border-radius: 12px;
  font-size: 15px;
  line-height: 1.5;
  white-space: pre-wrap;
  position: relative;
  font-weight: 400;
}

.user .content {
  background: #0071e3;
  color: white;
  border-radius: 12px 12px 4px 12px;
  margin-left: auto;
}

.assistant .content {
  background: #f5f5f7;
  color: #1d1d1f;
  border-radius: 4px 12px 12px 12px;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.typing-dots {
  display: flex;
  gap: 4px;
  padding: 8px 0;
}

.typing-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #86868b;
  animation: typing 1s infinite;
}

.typing-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 100% {
    transform: translateY(0);
    opacity: 0.3;
  }
  50% {
    transform: translateY(-4px);
    opacity: 1;
  }
}

.input-box {
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  padding: 16px;
  display: flex;
  align-items: flex-end;
  gap: 12px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

textarea {
  flex: 1;
  padding: 12px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  font-size: 15px;
  line-height: 1.5;
  max-height: 150px;
  min-height: 44px;
  resize: none;
  outline: none;
  background: #f5f5f7;
  font-weight: 400;
}

textarea:focus {
  border-color: #0071e3;
  background: #fff;
}

button {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 12px;
  background: #0071e3;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  transition: all 0.2s;
}

button:hover:not(:disabled) {
  background: #0077ed;
  transform: translateY(-1px);
}

button:disabled {
  background: #e5e7eb;
  cursor: not-allowed;
}

.send-icon {
  width: 20px;
  height: 20px;
}

.loading {
  width: 20px;
  height: 20px;
  border: 2px solid #fff;
  border-radius: 50%;
  border-top-color: transparent;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

::-webkit-scrollbar {
  width: 4px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 2px;
}

@media (max-width: 768px) {
  .chat {
    padding: 0;
  }
  
  .chat-container {
    border-radius: 0;
    border: none;
  }
  
  .message .content {
    max-width: calc(100% - 52px);
  }
}
</style> 