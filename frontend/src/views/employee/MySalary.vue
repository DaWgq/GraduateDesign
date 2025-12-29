<template>
  <div class="page">
    <el-card>
      <template #header><span>我的薪资明细</span></template>
      <div class="toolbar">
        <el-input-number v-model="year" :min="2020" :max="2030" style="width:100px" />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading" show-summary>
        <el-table-column prop="year" label="年份" width="80" />
        <el-table-column prop="month" label="月份" width="80">
          <template #default="{ row }">{{ row.month }}月</template>
        </el-table-column>
        <el-table-column prop="baseSalary" label="基本工资">
          <template #default="{ row }">¥{{ row.baseSalary?.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="overtime" label="加班费">
          <template #default="{ row }">¥{{ row.overtime?.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="deduction" label="扣款">
          <template #default="{ row }">¥{{ row.deduction?.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="实发金额">
          <template #default="{ row }"><span style="color:#67C23A;font-weight:bold">¥{{ row.totalAmount?.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="['warning','primary','success'][row.status]">{{ ['待核算','已核算','已发放'][row.status] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'

const tableData = ref([])
const loading = ref(false)
const year = ref(new Date().getFullYear())

const loadData = async () => {
  loading.value = true
  const res = await request.get('/salary/my', { params: { year: year.value } })
  tableData.value = res.data || []
  loading.value = false
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; }
</style>
