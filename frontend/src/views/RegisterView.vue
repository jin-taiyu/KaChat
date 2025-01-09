<template>
  <div class="login-bg">
    <div class="login-container">
      <h2>注册</h2>
      <form @submit.prevent="handleRegister">
        <input type="text" v-model="username" class="login-input" placeholder="用户名" required />
        <input type="password" v-model="password" class="login-input" placeholder="密码" required />
        <button type="submit" class="login-btn">注册</button>
      </form>
      <p>
        已有账号？
        <router-link to="/login" class="register-link">去登录</router-link>
      </p>
      <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    </div>
  </div>
</template>

<script>
import axios from '../axios'

export default {
  name: 'RegisterView',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    }
  },
  methods: {
    async handleRegister() {
      try {
        const res = await axios.post('/auth/register', {
          username: this.username,
          password: this.password
        })
        if (res.data.msg) {
          this.$router.push('/login')
        }
      } catch (err) {
        this.errorMessage = err.response?.data?.error || '注册失败'
      }
    }
  }
}
</script>

<style scoped>
.login-bg {
  font-family: 'Arial', sans-serif;
  background: linear-gradient(to bottom right, #f0f0f0, #d6d6d6);
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  color: #333;
}

.login-container {
  background-color: #ffffff;
  padding: 30px 40px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 360px;
  text-align: center;
}

.login-input {
  width: 80%;
  padding: 12px;
  margin: 12px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 15px;
  background-color: #f9f9f9;
}

.login-btn {
  width: 80%;
  padding: 12px;
  margin-top: 20px;
  background-color: #5f6368;
  border: none;
  color: #fff;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.login-btn:hover {
  background-color: #444;
}

.register-link {
  color: #5f6368;
  text-decoration: none;
  font-size: 14px;
  display: inline-block;
  margin-top: 20px;
}

.register-link:hover {
  text-decoration: underline;
}

.error {
  color: red;
  margin-top: 10px;
  font-size: 14px;
}
</style>
