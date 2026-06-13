import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 同源代理: 前端请求 /api/** 转发到后端, Session Cookie 正常携带
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
