<template>
  <div class="page">
    <el-card>
      <template #header><span>我的考勤记录</span></template>
      <div class="toolbar">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="workDate" label="日期" width="120" />
        <el-table-column prop="workHours" label="工时(小时)" width="100" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="['','success','warning','warning','danger'][row.status]">{{ ['','正常','迟到','早退','缺勤'][row.status] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" />
    </el-card>

    <el-card style="margin-top:20px">
      <template #header><span>本月统计</span></template>
      <el-row :gutter="20">
        <el-col :span="6"><el-statistic title="出勤天数" :value="stats.normalDays" /></el-col>
        <el-col :span="6"><el-statistic title="迟到次数" :value="stats.lateDays" /></el-col>
        <el-col :span="6"><el-statistic title="早退次数" :value="stats.earlyDays" /></el-col>
        <el-col :span="6"><el-statistic title="缺勤天数" :value="stats.absentDays" /></el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'

const tableData = ref([])
const loading = ref(false)
const dateRange = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const stats = reactive({ normalDays: 0, lateDays: 0, earlyDays: 0, absentDays: 0 })

const loadData = async () => {
  loading.value = true
  const params = { current: page.value, size: size.value }
  if (dateRange.value?.length === 2) {
    params.startDate = dateRange.value[0]
    params.endDate = dateRange.value[1]
  }
  const res = await request.get('/attendance/my', { params })
  tableData.value = res.data.records || []
  total.value = res.data.total || 0
  
  // 统计
  stats.normalDays = tableData.value.filter(a => a.status === 1).length
  stats.lateDays = tableData.value.filter(a => a.status === 2).length
  stats.earlyDays = tableData.value.filter(a => a.status === 3).length
  stats.absentDays = tableData.value.filter(a => a.status === 4).length
  loading.value = false
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; }
.el-pagination { margin-top: 16px; justify-content: flex-end; }
</style>
