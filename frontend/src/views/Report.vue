<template>
  <div class="page">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>薪资报表</span>
              <div>
                <el-input-number v-model="year" :min="2020" :max="2030" :controls="false" style="width:80px" />
                <el-button type="primary" @click="loadSalaryReport" style="margin-left:10px">查询</el-button>
              </div>
            </div>
          </template>
          <el-table :data="salaryReport" border>
            <el-table-column prop="month" label="月份" width="80">
              <template #default="{ row }">{{ row.month }}月</template>
            </el-table-column>
            <el-table-column prop="employeeCount" label="发薪人数" />
            <el-table-column prop="totalSalary" label="薪资总额">
              <template #default="{ row }">¥{{ row.totalSalary?.toLocaleString() || 0 }}</template>
            </el-table-column>
            <el-table-column prop="avgSalary" label="人均薪资">
              <template #default="{ row }">¥{{ row.avgSalary?.toLocaleString() || 0 }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>派遣趋势</span>
              <div>
                <el-input-number v-model="trendYear" :min="2020" :max="2030" :controls="false" style="width:80px" />
                <el-button type="primary" @click="loadDispatchTrend" style="margin-left:10px">查询</el-button>
              </div>
            </div>
          </template>
          <el-table :data="dispatchTrend" border>
            <el-table-column prop="month" label="月份" width="80">
              <template #default="{ row }">{{ row.month }}月</template>
            </el-table-column>
            <el-table-column prop="count" label="新增派遣数" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const year = ref(new Date().getFullYear())
const trendYear = ref(new Date().getFullYear())
const salaryReport = ref([])
const dispatchTrend = ref([])

const validateYear = (value) => {
  if (!value || value < 2020 || value > 2030) {
    ElMessage.warning('请输入正确的年份（2020-2030）')
    return false
  }
  return true
}

const loadSalaryReport = async () => {
  if (!validateYear(year.value)) return
  const res = await request.get('/statistics/salary/report', { params: { year: year.value } })
  salaryReport.value = res.data
}

const loadDispatchTrend = async () => {
  if (!validateYear(trendYear.value)) return
  const res = await request.get('/statistics/dispatch/trend', { params: { year: trendYear.value } })
  dispatchTrend.value = res.data
}

onMounted(() => {
  loadSalaryReport()
  loadDispatchTrend()
})
</script>

<style scoped>
.page { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
