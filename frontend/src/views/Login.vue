<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-brand">
        <div class="brand-logo">
          <el-icon :size="26" color="#fff"><Box /></el-icon>
        </div>
        <h2>硅谷进销存</h2>
        <p>Lite-IMS 轻量级进销存管理系统</p>
      </div>

      <el-form :model="form" ref="formRef" :rules="rules" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            placeholder="密码"
            :prefix-icon="Lock"
            type="password"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-button type="primary" class="login-btn" size="large" @click="handleLogin" :loading="loading">
          立即登录
        </el-button>
      </el-form>

      <div class="login-tip">演示账号：admin / 123456</div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const res = await request.post('/login', form)
      localStorage.setItem('user', JSON.stringify(res.data.data))
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } catch (e) {
      // 错误提示已由拦截器统一处理
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1e293b 0%, #312e81 55%, #1e3a8a 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰光斑 */
.login-container::before,
.login-container::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  filter: blur(90px);
  opacity: 0.35;
}

.login-container::before {
  width: 420px;
  height: 420px;
  background: #6366f1;
  top: -120px;
  right: -80px;
}

.login-container::after {
  width: 360px;
  height: 360px;
  background: #0ea5e9;
  bottom: -100px;
  left: -60px;
}

.login-card {
  width: 400px;
  background: rgba(255, 255, 255, 0.96);
  border-radius: 16px;
  padding: 40px 36px 28px;
  box-shadow: 0 24px 60px rgba(2, 6, 23, 0.45);
  position: relative;
  z-index: 1;
}

.login-brand {
  text-align: center;
  margin-bottom: 28px;
}

.brand-logo {
  width: 52px;
  height: 52px;
  margin: 0 auto 12px;
  border-radius: 14px;
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(79, 70, 229, 0.35);
}

.login-brand h2 {
  font-size: 22px;
  color: #1e293b;
  letter-spacing: 1px;
}

.login-brand p {
  margin-top: 6px;
  font-size: 13px;
  color: #94a3b8;
}

.login-btn {
  width: 100%;
  margin-top: 4px;
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  border: none;
  letter-spacing: 4px;
}

.login-tip {
  margin-top: 18px;
  text-align: center;
  font-size: 12px;
  color: #94a3b8;
}
</style>
