<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-input-number v-model="year" placeholder="年份" :min="2020" :max="2030" style="width:120px" />
        <el-select v-model="month" placeholder="月份" clearable style="width:100px">
          <el-option v-for="m in 12" :key="m" :value="m" :label="`${m}月`" />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button type="success" @click="showCalculate = true">核算薪资</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="employeeId" label="员工ID" />
        <el-table-column prop="year" label="年份" />
        <el-table-column prop="month" label="月份" />
        <el-table-column prop="baseSalary" label="基本工资" />
        <el-table-column prop="overtime" label="加班费" />
        <el-table-column prop="deduction" label="扣款" />
        <el-table-column prop="totalAmount" label="实发金额" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="['warning','primary','success'][row.status]">{{ ['待核算','已核算','已发放'][row.status] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" />
    </el-card>

    <el-dialog v-model="showCalculate" title="核算薪资" width="400px">
      <el-form :model="calcForm" label-width="80px">
        <el-form-item label="派遣ID"><el-input-number v-model="calcForm.dispatchId" :min="1" /></el-form-item>
        <el-form-item label="年份"><el-input-number v-model="calcForm.year" :min="2020" :max="2030" /></el-form-item>
        <el-form-item label="月份"><el-select v-model="calcForm.month"><el-option v-for="m in 12" :key="m" :value="m" :label="`${m}月`" /></el-select></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCalculate = false">取消</el-button>
        <el-button type="primary" @click="handleCalculate">核算</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const loading = ref(false)
const year = ref(new Date().getFullYear())
const month = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const showCalculate = ref(false)
const calcForm = reactive({ dispatchId: null, year: new Date().getFullYear(), month: new Date().getMonth() + 1 })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/salary/page', { params: { current: page.value, size: size.value, year: year.value, month: month.value } })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

const handleCalculate = async () => {
  await request.post('/salary/calculate', null, { params: calcForm })
  ElMessage.success('核算成功')
  showCalculate.value = false
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; }
.el-pagination { margin-top: 16px; justify-content: flex-end; }
</style>
