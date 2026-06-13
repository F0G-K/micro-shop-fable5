<template>
  <div class="page-container">
    <div class="page-card">
      <div class="toolbar">
        <el-button type="primary" :icon="CirclePlus" @click="openDrawer">创建订单</el-button>
        <el-input
          v-model="query.orderNo"
          placeholder="搜索订单号"
          style="width: 220px"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        <div class="spacer"></div>
        <el-button :icon="Download" @click="exportExcel" :loading="exporting">导出 Excel</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
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
                <el-table-column prop="quantity" label="数量" width="100" align="center" />
                <el-table-column label="小计" width="130" align="right">
                  <template #default="{ row: item }">¥{{ Number(item.amount).toFixed(2) }}</template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单号" min-width="160" />
        <el-table-column prop="customerName" label="客户" min-width="140" />
        <el-table-column prop="userName" label="操作员" width="100" />
        <el-table-column label="总金额" width="130" align="right">
          <template #default="{ row }">
            <span class="price">¥{{ Number(row.totalAmount).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type || 'info'" effect="light">
              {{ statusMap[row.status]?.label || row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-dropdown @command="(s) => changeStatus(row, s)">
              <el-button type="primary" link>
                状态流转<el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="(s, code) in statusMap"
                    :key="code"
                    :command="Number(code)"
                    :disabled="Number(code) === row.status"
                  >
                    {{ s.label }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </div>

    <!-- 创建订单抽屉 -->
    <el-drawer v-model="drawerVisible" title="创建销售订单" size="560px">
      <el-form label-width="70px">
        <el-form-item label="客户" required>
          <el-select v-model="orderForm.customerId" placeholder="请选择客户" style="width: 100%" filterable>
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>

      <div class="items-header">
        <span>商品明细</span>
        <el-button type="primary" link :icon="Plus" @click="addItem">添加商品</el-button>
      </div>

      <div v-for="(item, index) in orderForm.items" :key="index" class="item-row">
        <el-select
          v-model="item.productId"
          placeholder="选择商品"
          filterable
          style="flex: 1"
          @change="() => onProductChange(item)"
        >
          <el-option
            v-for="p in products"
            :key="p.id"
            :label="`${p.name} (库存${p.stock} · ¥${Number(p.price).toFixed(2)})`"
            :value="p.id"
            :disabled="p.stock <= 0"
          />
        </el-select>
        <el-input-number v-model="item.quantity" :min="1" :max="maxStock(item)" style="width: 130px" />
        <span class="item-amount">¥{{ itemAmount(item) }}</span>
        <el-button type="danger" link :icon="Delete" @click="orderForm.items.splice(index, 1)" />
      </div>

      <el-empty v-if="orderForm.items.length === 0" description="点击「添加商品」开始开单" :image-size="80" />

      <div class="drawer-footer">
        <div class="total-line">
          合计：<span class="total-amount">¥{{ totalAmount }}</span>
        </div>
        <div>
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOrder" :loading="submitting">提交订单</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { CirclePlus, Search, Download, Plus, Delete, ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const statusMap = {
  0: { label: '待支付', type: 'info' },
  1: { label: '已支付', type: 'primary' },
  2: { label: '已发货', type: 'warning' },
  3: { label: '已完成', type: 'success' },
  4: { label: '已取消', type: 'danger' }
}

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const exporting = ref(false)
const drawerVisible = ref(false)
const submitting = ref(false)
const customers = ref([])
const products = ref([])

const query = reactive({ page: 1, size: 10, orderNo: '' })

const orderForm = reactive({
  customerId: null,
  items: []
})

const productById = computed(() => Object.fromEntries(products.value.map((p) => [p.id, p])))
const productName = (id) => productById.value[id]?.name || `商品#${id}`
const maxStock = (item) => productById.value[item.productId]?.stock || 9999

const itemAmount = (item) => {
  const p = productById.value[item.productId]
  if (!p) return '0.00'
  return (Number(p.price) * item.quantity).toFixed(2)
}

const totalAmount = computed(() =>
  orderForm.items.reduce((sum, item) => sum + Number(itemAmount(item)), 0).toFixed(2)
)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/orders', {
      params: { page: query.page, size: query.size, orderNo: query.orderNo || undefined }
    })
    tableData.value = res.data.data.records
    total.value = res.data.data.total
  } finally {
    loading.value = false
  }
}

const fetchCustomers = async () => {
  const res = await request.get('/customers')
  customers.value = res.data.data
}

const fetchProducts = async () => {
  // 拉全量商品用于下拉与明细名称映射 (教学项目数据量小)
  const res = await request.get('/products', { params: { page: 1, size: 1000 } })
  products.value = res.data.data.records
}

const handleSearch = () => {
  query.page = 1
  fetchData()
}

const openDrawer = () => {
  orderForm.customerId = null
  orderForm.items = []
  drawerVisible.value = true
  fetchCustomers()
  fetchProducts()
}

const addItem = () => {
  orderForm.items.push({ productId: null, quantity: 1 })
}

const onProductChange = (item) => {
  item.quantity = 1
}

const submitOrder = async () => {
  if (!orderForm.customerId) {
    ElMessage.warning('请选择客户')
    return
  }
  if (orderForm.items.length === 0 || orderForm.items.some((i) => !i.productId)) {
    ElMessage.warning('请完善商品明细')
    return
  }
  submitting.value = true
  try {
    await request.post('/orders', orderForm)
    ElMessage.success('订单创建成功')
    drawerVisible.value = false
    fetchData()
    fetchProducts() // 刷新库存缓存
  } finally {
    submitting.value = false
  }
}

const changeStatus = async (row, status) => {
  await request.put(`/orders/${row.id}/status`, null, { params: { status } })
  ElMessage.success('状态已更新')
  fetchData()
}

const exportExcel = async () => {
  exporting.value = true
  try {
    const res = await request.get('/excel/orders/export', { responseType: 'blob' })
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = '销售订单报表.xlsx'
    a.click()
    URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } finally {
    exporting.value = false
  }
}

onMounted(() => {
  fetchData()
  fetchProducts()
})
</script>

<style scoped>
.price {
  color: #d97706;
  font-weight: 600;
}

.items-wrap {
  padding: 10px 40px;
}

.items-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  margin: 6px 0 12px;
}

.item-row {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 10px;
}

.item-amount {
  width: 90px;
  text-align: right;
  color: #d97706;
  font-size: 13px;
  font-weight: 600;
}

.drawer-footer {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-line {
  font-size: 14px;
  color: #475569;
}

.total-amount {
  font-size: 22px;
  font-weight: 700;
  color: #dc2626;
}
</style>
