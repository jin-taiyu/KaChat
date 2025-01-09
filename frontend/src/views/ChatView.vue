<template>
  <div :class="['chat-body', theme]" :style="{ backgroundImage: `url('/images/${seasonImage}')` }">
    <div class="container">
      <div class="header">
        <h3>您好，{{ username }} ! </h3>
        <div class="header-right">
          <h3>现在是{{ currentSeason }}的{{ currentTimeOfDay }}</h3>
          <button class="theme-toggle" @click="toggleTheme" title="切换主题">
            {{ themeIcon }}
          </button>
        </div>
      </div>

      <div class="chat-container">
        <contact-list :username="username" :contacts="contacts" @selectRecipient="selectRecipient"
          @refreshContacts="loadContacts" :currentRecipient="currentRecipient" />

        <chat-room :username="username" :recipient="currentRecipient" :messages="messages" @sendMessage="sendMessage" />
      </div>
    </div>
  </div>
</template>

<script>
import ContactList from '../components/ContactList.vue'
import ChatRoom from '../components/ChatRoom.vue'
import axios from '../axios'

export default {
  name: 'ChatView',
  components: { ContactList, ChatRoom },
  data() {
    return {
      username: localStorage.getItem('username') || '',
      theme: 'light',
      themeIcon: '🌙', // dark -> ☀️, light -> 🌙
      currentSeason: '',
      currentTimeOfDay: '',
      currentRecipient: '公共',
      messages: [],
      contacts: ['公共'],
    }
  },
  computed: {
    seasonImage() {
      switch (this.currentSeason) {
        case '春季':
          return 'spring.jpg'
        case '夏季':
          return 'summer.jpg'
        case '秋季':
          return 'autumn.jpg'
        default:
          return 'winter.jpg'
      }
    }
  },
  async created() {
    // 如果没有登录信息，就跳转回登录
    if (!this.username) {
      this.$router.push('/login')
      return
    }
    this.calculateSeasonAndTime()

    // 加载联系人列表
    await this.loadContacts()

    // 加载消息列表
    await this.updateMessages()

    // 定时刷新消息
    setInterval(() => {
      this.updateMessages()
    }, 60000)
  },
  methods: {
    toggleTheme() {
      if (this.theme === 'light') {
        this.theme = 'dark'
        this.themeIcon = '☀️'
      } else {
        this.theme = 'light'
        this.themeIcon = '🌙'
      }
    },

    calculateSeasonAndTime() {
      const month = new Date().getMonth() + 1
      if (month >= 3 && month <= 5) {
        this.currentSeason = '春季'
      } else if (month >= 6 && month <= 8) {
        this.currentSeason = '夏季'
      } else if (month >= 9 && month <= 11) {
        this.currentSeason = '秋季'
      } else {
        this.currentSeason = '冬季'
      }

      const hour = new Date().getHours()
      if (hour >= 6 && hour < 12) {
        this.currentTimeOfDay = '早上'
      } else if (hour >= 12 && hour < 18) {
        this.currentTimeOfDay = '下午'
      } else {
        this.currentTimeOfDay = '晚上'
      }
    },

    async loadContacts() {
      try {
        const res = await axios.get(`/auth/contacts/${this.username}`)

        this.contacts = ['公共', ...res.data]
      } catch (err) {
        console.error(err)
      }
    },

    selectRecipient(recipient) {
      this.currentRecipient = recipient
      this.updateMessages()
    },

    async updateMessages() {
      try {
        let roomType = 'PUBLIC'
        let senderParam = null
        let recipientParam = null
        if (this.currentRecipient === '公共') {
          roomType = 'PUBLIC'
        } else {
          roomType = 'PRIVATE'
          senderParam = this.username
          recipientParam = this.currentRecipient
        }

        const res = await axios.get('/chat/messages', {
          params: {
            roomType,
            sender: senderParam,
            recipient: recipientParam
          }
        })
        this.messages = res.data
      } catch (err) {
        console.error(err)
      }
    },

    async sendMessage(content) {
      let roomType = 'PUBLIC'
      if (this.currentRecipient !== '公共') {
        roomType = 'PRIVATE'
      }

      try {
        await axios.post('/chat/message', {
          senderName: this.username,
          recipientName: this.currentRecipient === '公共'
            ? null
            : this.currentRecipient,
          content,
          roomType
        })
        // 发送完毕后再刷新消息
        await this.updateMessages()
      } catch (err) {
        console.error(err)
      }
    }
  }
}
</script>

<style scoped>
.chat-body {
  font-family: 'Arial', sans-serif;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background-size: cover;
  background-position: center center;
}

.chat-body.light {
  color: #333;
}

.chat-body.dark {
  color: #ddd;
}

.chat-body.dark::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  z-index: 0;
}

.container {
  position: relative;
  z-index: 1;
  background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  padding-top: 15px;
  padding-right: 30px;
  padding-bottom: 30px;
  padding-left: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 25px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 1000px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chat-body.dark .container {
  background-color: rgba(0, 0, 0, 0.4);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 40px;
  flex-wrap: wrap;
  margin-top: 0px;
  margin-bottom: 0px;
  position: relative;
}

.header-right {
  display: flex;
  align-items: center;
}

.theme-toggle {
  margin-left: 10px;
  cursor: pointer;
  font-size: 20px;
  background: transparent;
  border: none;
  color: inherit;
  transition: transform 0.2s;
}

.theme-toggle:hover {
  transform: scale(1.1);
}

.chat-container {
  display: flex;
  width: 100%;
  background-color: rgba(249, 249, 249, 0.8);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  flex: 1;
  height: 600px;
  margin-top: 20px;
}

.chat-body.dark .chat-container {
  background-color: rgba(40, 40, 40, 0.7);
}

@media (max-width:768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-right {
    margin-top: 10px;
  }
}
</style>
