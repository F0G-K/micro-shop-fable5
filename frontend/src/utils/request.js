import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 响应拦截器: 统一处理业务错误与登录失效
request.interceptors.response.use(
  (response) => {
    // blob 响应 (文件下载) 直接放行
    if (response.config.responseType === 'blob') {
      return response
    }
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return response
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      ElMessage.warning('登录已失效，请重新登录')
      localStorage.removeItem('user')
      window.location.href = '/login'
    } else {
      const msg = error.response?.data?.message || error.message || '网络异常'
      ElMessage.error(msg)
    }
    return Promise.reject(error)
  }
)

export default request
