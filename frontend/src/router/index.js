import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('../layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'Odometer' }
      },
      {
        path: 'dashboard/detail/:type',
        name: 'DashboardDetail',
        component: () => import('../views/DashboardDetail.vue'),
        meta: { title: '统计详情', hidden: true }
      },
      {
        path: 'products',
        name: 'Product',
        component: () => import('../views/Product.vue'),
        meta: { title: '商品管理', icon: 'Goods' }
      },
      {
        path: 'categories',
        name: 'Category',
        component: () => import('../views/Category.vue'),
        meta: { title: '分类管理', icon: 'Menu' }
      },
      {
        path: 'customers',
        name: 'Customer',
        component: () => import('../views/Customer.vue'),
        meta: { title: '客户管理', icon: 'User' }
      },
      {
        path: 'orders',
        name: 'Order',
        component: () => import('../views/Order.vue'),
        meta: { title: '销售订单', icon: 'Tickets' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫: 未登录跳转登录页
router.beforeEach((to) => {
  const user = localStorage.getItem('user')
  if (to.path !== '/login' && !user) {
    return '/login'
  }
  if (to.path === '/login' && user) {
    return '/dashboard'
  }
  document.title = (to.meta.title ? to.meta.title + ' · ' : '') + '硅谷进销存'
  return true
})

export default router
