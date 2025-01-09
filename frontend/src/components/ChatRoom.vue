<template>
  <div id="chatArea">
    <div id="chatMessages">
      <div v-for="msg in messages" :key="msg.id" :class="['message', msg.senderId === myUserId ? 'me' : 'other']">
        <div class="username">
          {{ msg.senderId === myUserId ? '(我)' : userIdToUsername[msg.senderId] || '未知用户' }}
        </div>
        <div class="bubble" :class="msg.senderId === myUserId ? 'me' : 'other'">
          {{ msg.content }}
        </div>
      </div>
    </div>

    <form class="message-form" @submit.prevent="submitMessage">
      <textarea v-model="content" placeholder="请输入您的消息..." required></textarea>
      <div class="button-group">
        <input type="submit" value="发送" />
        <input type="reset" value="清空" @click="content = ''" />
        <input type="button" value="退出" @click="logout" />
      </div>
    </form>
  </div>
</template>

<script>
import axios from '../axios'

export default {
  name: 'ChatRoom',
  props: {
    username: String,      // 当前用户
    recipient: String,     // 收件人
    messages: Array        // 消息列表
  },
  data() {
    return {
      content: '',
      myUserIdValue: null,  // 当前用户ID
      userIdToUsername: {}  // 缓存 用户ID -> 用户名 的映射
    }
  },
  computed: {
    myUserId() {
      return this.myUserIdValue
    }
  },
  created() {
    this.loadMyUserId()
  },
  watch: {
    // 当消息有更新时，尝试获取对应用户的用户名
    messages: {
      handler(newMessages) {
        this.fetchUsernames(newMessages)
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    async loadMyUserId() {
      try {
        const res = await axios.get(`/auth/userId/${this.username}`)
        this.myUserIdValue = res.data.userId

        this.fetchUsernames(this.messages)
      } catch (err) {
        console.error('无法获取用户ID:', err)
      }
    },

    async fetchUsernames(messages) {
      if (!messages || messages.length === 0) return

      const myId = String(this.myUserId)

      const uniqueSenderIds = [
        ...new Set(
          messages.map(msg => String(msg.senderId)).filter(id => id !== myId)
        )
      ]

      const idsToFetch = uniqueSenderIds.filter(id => !(id in this.userIdToUsername))
      if (idsToFetch.length === 0) return

      try {
        const promises = idsToFetch.map(id =>
          axios.get(`/auth/username/${id}`)
            .then(res => ({ id, username: res.data.username }))
            .catch(() => ({ id, username: '未知用户' }))
        )
        const results = await Promise.all(promises)
        results.forEach(({ id, username }) => {
          this.userIdToUsername[id] = username
        })
      } catch (err) {
        console.error('无法获取用户名:', err)
      }
    },
    // 发送消息
    submitMessage() {
      if (!this.content.trim()) return
      this.$emit('sendMessage', this.content)
      this.content = ''
    },
    // 退出（清除登录信息，回到登录页）
    logout() {
      localStorage.removeItem('username')
      this.$router.push('/login')
    },
  }
}
</script>

<style scoped>
#chatArea {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
  background-color: rgba(255, 255, 255, 0.6);
}

#chatMessages {
  flex: 1;
  border: 1px solid #ddd;
  padding: 12px 15px;
  overflow-y: auto;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  max-height: 400px;
  margin-bottom: 20px;
  width: 100%;
  box-sizing: border-box;
}

.message {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.message.me {
  align-items: flex-end;
}

.message.other {
  align-items: flex-start;
}

.username {
  font-size: 12px;
  color: #888;
  margin-bottom: 5px;
}

.bubble {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 20px;
  line-height: 1.5;
  font-size: 14px;
  position: relative;
  word-wrap: break-word;
}

.bubble.me {
  background-color: #5a9bd3;
  color: #fff;
  border-bottom-right-radius: 0;
  text-align: right;
}

.bubble.me::after {
  content: "";
  position: absolute;
  bottom: 0;
  right: -10px;
  border-width: 10px 0 0 10px;
  border-style: solid;
  border-color: #5a9bd3 transparent;
}

.bubble.other {
  background-color: #f0f4f8;
  color: #333;
  border-bottom-left-radius: 0;
  text-align: left;
}

.bubble.other::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: -10px;
  border-width: 10px 10px 0 0;
  border-style: solid;
  border-color: #f0f4f8 transparent;
}

.message-form {
  margin-top: 20px;
  width: 100%;
  box-sizing: border-box;
}

.message-form textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 20px;
  font-size: 14px;
  resize: none;
  background-color: #fff;
  box-sizing: border-box;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 10px;
}

.button-group input,
.button-group a {
  background-color: #5a9bd3;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  text-decoration: none;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.button-group input:hover,
.button-group a:hover {
  background-color: #4178a8;
  transform: translateY(-2px);
}

.button-group input:active,
.button-group a:active {
  transform: translateY(0);
}
</style>
