<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-logo">
          <el-icon :size="18" color="#fff"><Box /></el-icon>
        </div>
        <span>硅谷进销存</span>
      </div>

      <el-menu
        :default-active="route.path"
        router
        class="side-menu"
        background-color="transparent"
        text-color="#94a3b8"
        active-text-color="#ffffff"
      >
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          <el-icon><component :is="item.meta.icon" /></el-icon>
          <span>{{ item.meta.title }}</span>
        </el-menu-item>
      </el-menu>

      <div class="sidebar-footer">Lite-IMS v1.0</div>
    </aside>

    <!-- 主区域 -->
    <div class="main">
      <header class="header">
        <div class="header-title">{{ route.meta.title }}</div>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="30" class="user-avatar">{{ avatarText }}</el-avatar>
            <span class="user-name">{{ user.nickname || user.username }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </header>

      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { SwitchButton } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const route = useRoute()
const router = useRouter()

const user = JSON.parse(localStorage.getItem('user') || '{}')
const avatarText = computed(() => (user.nickname || user.username || '?').charAt(0))

// 从路由配置生成菜单
const menus = router.options.routes
  .find((r) => r.path === '/')
  .children.map((c) => ({ path: '/' + c.path, meta: c.meta }))

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await request.get('/logout')
    } finally {
      localStorage.removeItem('user')
      ElMessage.success('已退出登录')
      router.push('/login')
    }
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  background: #f1f5f9;
}

.sidebar {
  width: 220px;
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.brand {
  height: 60px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 20px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.12);
}

.brand-logo {
  width: 32px;
  height: 32px;
  border-radius: 9px;
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  display: flex;
  align-items: center;
  justify-content: center;
}

.side-menu {
  border-right: none;
  margin-top: 10px;
  flex: 1;
}

.side-menu .el-menu-item {
  height: 46px;
  margin: 4px 12px;
  border-radius: 8px;
}

.side-menu .el-menu-item:hover {
  background: rgba(99, 102, 241, 0.15);
}

.side-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.35);
}

.sidebar-footer {
  padding: 14px 20px;
  font-size: 12px;
  color: #475569;
  text-align: center;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.header {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(15, 23, 42, 0.06);
  z-index: 10;
}

.header-title {
  font-size: 17px;
  font-weight: 600;
  color: #1e293b;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #475569;
  outline: none;
}

.user-avatar {
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  font-size: 14px;
}

.user-name {
  font-size: 14px;
}

.content {
  flex: 1;
  overflow-y: auto;
}
</style>
