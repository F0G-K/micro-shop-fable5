<template>
  <div class="page-container">
    <!-- 顶部返回栏 -->
    <div class="detail-header">
      <el-button :icon="ArrowLeft" @click="router.push('/dashboard')">返回仪表盘</el-button>
      <h3>{{ titleMap[type] || '统计详情' }}</h3>
    </div>

    <!-- 商品详情: 库存预警 + 分类分布 -->
    <div v-if="type === 'products'" class="two-col" v-loading="loading">
      <div class="page-card">
        <div class="card-title">
          <el-icon color="#dc2626"><Warning /></el-icon>
          库存预警 (库存 &lt; 10)
        </div>
        <el-table :data="productDetail.lowStock" stripe size="default">
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column prop="name" label="商品名称" min-width="140" />
          <el-table-column label="价格" width="110" align="right">
            <template #default="{ row }">¥{{ Number(row.price).toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="库存" width="100" align="center">
            <template #default="{ row }">
              <el-tag type="danger" effect="dark" size="small">{{ row.stock }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="90" align="center">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="light" size="small">
                {{ row.status === 1 ? '上架' : '下架' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!productDetail.lowStock?.length" description="库存充足，暂无预警" :image-size="70" />
      </div>

      <div class="page-card">
        <div class="card-title">
          <el-icon color="#6366f1"><PieChart /></el-icon>
          分类商品分布
        </div>
        <div v-for="c in productDetail.categoryStats" :key="c.name" class="cat-row">
          <span class="cat-name">{{ c.name }}</span>
          <el-progress
            :percentage="maxCatCount ? Math.round((c.count / maxCatCount) * 100) : 0"
            :stroke-width="14"
            :show-text="false"
            class="cat-bar"
          />
          <span class="cat-count">{{ c.count }} 件</span>
        </div>
      </div>
    </div>

    <!-- 今日订单详情 -->
    <div v-else-if="type === 'orders'" class="page-card" v-loading="loading">
      <div class="card-title">
        <el-icon color="#10b981"><Tickets /></el-icon>
        今日订单 ({{ todayOrders.length }} 笔)
      </div>
      <el-table :data="todayOrders" stripe>
        <el-table-column type="expand">
          <template #default="{ row }">
            <div class="items-wrap">
              <el-table :data="row.items" size="small" border>
                <el-table-column label="商品" min-width="160">
                  <template #default="{ row: item }">{{ productName(item.productId) }}</template>
                </el-table-column>
                <el-table-column label="单价" width="120" align="right">
                  <template #default="{ row: item }">¥{{ Number(item.price).toFixed(2) }}</template>
                </el-table-column>
                <el-table-column prop="quantity" label="数量" width="90" align="center" />
                <el-table-column label="小计" width="120" align="right">
                  <template #default="{ row: item }">¥{{ Number(item.amount).toFixed(2) }}</template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单号" min-width="150" />
        <el-table-column prop="customerName" label="客户" min-width="130" />
        <el-table-column prop="userName" label="操作员" width="100" />
        <el-table-column label="金额" width="130" align="right">
          <template #default="{ row }">
            <span class="price">¥{{ Number(row.totalAmount).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type || 'info'" effect="light">
              {{ statusMap[row.status]?.label || row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="170" />
      </el-table>
      <el-empty v-if="!todayOrders.length" description="今天还没有订单" :image-size="70" />
    </div>

    <!-- 销售详情: 状态汇总 + 近7日趋势 -->
    <div v-else-if="type === 'sales'" v-loading="loading">
      <div class="page-card" style="margin-bottom: 16px">
        <div class="card-title">
          <el-icon color="#f59e0b"><Coin /></el-icon>
          订单状态汇总
        </div>
        <div class="status-grid">
          <div v-for="s in salesDetail.statusStats" :key="s.status" class="status-card">
            <el-tag :type="statusMap[s.status]?.type || 'info'" effect="light">
              {{ statusMap[s.status]?.label || s.status }}
            </el-tag>
            <div class="status-count">{{ s.count }} 笔</div>
            <div class="status-amount">¥{{ Number(s.amount).toFixed(2) }}</div>
          </div>
        </div>
      </div>

      <div class="page-card">
        <div class="card-title">
          <el-icon color="#6366f1"><TrendCharts /></el-icon>
          近 7 日销售趋势 (排除已取消)
        </div>
        <div class="trend-chart">
          <div v-for="p in salesDetail.trend" :key="p.date" class="trend-col">
            <div class="trend-amount">{{ shortMoney(p.amount) }}</div>
            <div class="trend-bar-track">
              <div class="trend-bar" :style="{ height: barHeight(p.amount) }"></div>
            </div>
            <div class="trend-date">{{ p.date.slice(5) }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Warning, PieChart, Tickets, Coin, TrendCharts } from '@element-plus/icons-vue'
import request from '../utils/request'

const route = useRoute()
const router = useRouter()
const type = route.params.type

const titleMap = {
  products: '商品统计详情',
  orders: '今日订单详情',
  sales: '销售统计详情'
}

const statusMap = {
  0: { label: '待支付', type: 'info' },
  1: { label: '已支付', type: 'primary' },
  2: { label: '已发货', type: 'warning' },
  3: { label: '已完成', type: 'success' },
  4: { label: '已取消', type: 'danger' }
}

const loading = ref(false)
const productDetail = ref({ lowStock: [], categoryStats: [] })
const todayOrders = ref([])
const salesDetail = ref({ statusStats: [], trend: [] })
const products = ref([])

const maxCatCount = computed(() =>
  Math.max(...(productDetail.value.categoryStats || []).map((c) => Number(c.count)), 0)
)

const maxTrend = computed(() =>
  Math.max(...(salesDetail.value.trend || []).map((p) => Number(p.amount)), 0)
)

const barHeight = (amount) => {
  if (!maxTrend.value) return '2px'
  const h = (Number(amount) / maxTrend.value) * 100
  return Math.max(h, 1) + '%'
}

const shortMoney = (v) => {
  const n = Number(v)
  if (n >= 10000) return (n / 10000).toFixed(1) + '万'
  return n.toFixed(0)
}

const productById = computed(() => Object.fromEntries(products.value.map((p) => [p.id, p])))
const productName = (id) => productById.value[id]?.name || `商品#${id}`

onMounted(async () => {
  loading.value = true
  try {
    if (type === 'products') {
      const res = await request.get('/dashboard/product-detail')
      productDetail.value = res.data.data
    } else if (type === 'orders') {
      const [ordersRes, productsRes] = await Promise.all([
        request.get('/dashboard/today-orders'),
        request.get('/products', { params: { page: 1, size: 1000 } })
      ])
      todayOrders.value = ordersRes.data.data
      products.value = productsRes.data.data.records
    } else if (type === 'sales') {
      const res = await request.get('/dashboard/sales-detail')
      salesDetail.value = res.data.data
    }
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.detail-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.detail-header h3 {
  font-size: 17px;
  color: #1e293b;
}

.two-col {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 16px;
  align-items: start;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 16px;
}

.cat-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}

.cat-name {
  width: 90px;
  font-size: 13px;
  color: #475569;
  text-align: right;
  flex-shrink: 0;
}

.cat-bar {
  flex: 1;
}

.cat-count {
  width: 56px;
  font-size: 13px;
  color: #6366f1;
  font-weight: 600;
  flex-shrink: 0;
}

.items-wrap {
  padding: 10px 40px;
}

.price {
  color: #d97706;
  font-weight: 600;
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
}

.status-card {
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  padding: 14px;
  text-align: center;
}

.status-count {
  margin-top: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
}

.status-amount {
  margin-top: 4px;
  font-size: 13px;
  color: #94a3b8;
}

.trend-chart {
  display: flex;
  gap: 14px;
  height: 220px;
  padding: 10px 6px 0;
}

.trend-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.trend-amount {
  font-size: 12px;
  color: #6366f1;
  font-weight: 600;
  margin-bottom: 6px;
}

.trend-bar-track {
  flex: 1;
  width: 100%;
  max-width: 56px;
  display: flex;
  align-items: flex-end;
  background: #f8fafc;
  border-radius: 8px;
}

.trend-bar {
  width: 100%;
  border-radius: 8px;
  background: linear-gradient(180deg, #6366f1, #3b82f6);
  transition: height 0.4s ease;
}

.trend-date {
  margin-top: 8px;
  font-size: 12px;
  color: #94a3b8;
}
</style>
