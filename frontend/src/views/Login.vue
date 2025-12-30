<template>
  <div class="login-container">
    <div class="login-left">
      <div class="welcome-text">
        <h1>劳务派遣管理平台</h1>
        <p>高效、便捷的人力资源管理解决方案</p>
      </div>
    </div>
    <div class="login-right">
      <el-card class="login-card">
        <h2>用户登录</h2>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" @keyup.enter="handleLogin" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleLogin" style="width:100%">登录</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container { 
  height: 100%; 
  display: flex; 
}
.login-left { 
  flex: 1; 
  background: #FFF8DC; 
  display: flex; 
  justify-content: center; 
  align-items: center; 
}
.welcome-text { 
  text-align: center; 
  color: #8B7355; 
}
.welcome-text h1 { 
  font-size: 36px; 
  margin-bottom: 16px; 
}
.welcome-text p { 
  font-size: 18px; 
  opacity: 0.8; 
}
.login-right { 
  width: 480px; 
  display: flex; 
  justify-content: center; 
  align-items: center; 
  background: #fff; 
  box-shadow: -4px 0 20px rgba(0,0,0,0.1); 
}
.login-card { 
  width: 360px; 
  border: none; 
  box-shadow: none; 
}
.login-card h2 { 
  text-align: center; 
  margin-bottom: 30px; 
  color: #303133; 
}
</style>
