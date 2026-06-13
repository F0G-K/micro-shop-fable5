<template>
  <div class="page-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div>
        <h3>{{ greeting }}，{{ user.nickname || user.username }}</h3>
        <p>欢迎使用硅谷进销存管理系统，祝您工作顺利</p>
      </div>
      <el-icon :size="56" color="rgba(255,255,255,0.25)"><DataLine /></el-icon>
    </div>

    <!-- 统计卡片 -->
    <div class="stat-grid" v-loading="loading">
      <div class="stat-card">
        <div class="stat-icon" style="background: #eef2ff; color: #6366f1">
          <el-icon :size="26"><Goods /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">商品总数</div>
          <div class="stat-value">{{ stats.productCount ?? '-' }}</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: #ecfdf5; color: #10b981">
          <el-icon :size="26"><Tickets /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">今日订单数</div>
          <div class="stat-value">{{ stats.todayOrderCount ?? '-' }}</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon" style="background: #fff7ed; color: #f59e0b">
          <el-icon :size="26"><Coin /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">销售总额 (元)</div>
          <div class="stat-value">{{ formatMoney(stats.totalSales) }}</div>
        </div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="page-card quick-panel">
      <div class="panel-title">快捷操作</div>
      <div class="quick-grid">
        <div class="quick-item" @click="router.push('/orders')">
          <el-icon :size="20" color="#6366f1"><CirclePlus /></el-icon>
          <span>销售开单</span>
        </div>
        <div class="quick-item" @click="router.push('/products')">
          <el-icon :size="20" color="#10b981"><Goods /></el-icon>
          <span>商品管理</span>
        </div>
        <div class="quick-item" @click="router.push('/customers')">
          <el-icon :size="20" color="#f59e0b"><User /></el-icon>
          <span>客户管理</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const stats = ref({})
const loading = ref(false)

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '夜深了'
  if (h < 12) return '早上好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const formatMoney = (v) => {
  if (v === undefined || v === null) return '-'
  return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await request.get('/dashboard/stats')
    stats.value = res.data.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.welcome-banner {
  background: linear-gradient(120deg, #6366f1, #3b82f6 70%, #0ea5e9);
  border-radius: 12px;
  color: #fff;
  padding: 24px 28px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  box-shadow: 0 8px 24px rgba(79, 70, 229, 0.25);
}

.welcome-banner h3 {
  font-size: 20px;
  margin-bottom: 6px;
}

.welcome-banner p {
  font-size: 13px;
  opacity: 0.85;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 22px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 4px rgba(15, 23, 42, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.1);
}

.stat-icon {
  width: 54px;
  height: 54px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-label {
  font-size: 13px;
  color: #94a3b8;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 26px;
  font-weight: 600;
  color: #1e293b;
}

.quick-panel .panel-title {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 14px;
}

.quick-grid {
  display: flex;
  gap: 14px;
}

.quick-item {
  flex: 1;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  color: #475569;
  font-size: 14px;
  transition: all 0.2s;
}

.quick-item:hover {
  border-color: #6366f1;
  color: #6366f1;
  background: #eef2ff;
}
</style>
