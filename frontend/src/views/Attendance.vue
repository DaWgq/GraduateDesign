<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-input-number v-model="employeeId" placeholder="员工ID" style="width:120px" />
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button type="success" @click="handleAdd">录入考勤</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="employeeId" label="员工ID" width="100" />
        <el-table-column prop="dispatchId" label="派遣ID" width="100" />
        <el-table-column prop="workDate" label="日期" width="120" />
        <el-table-column prop="workHours" label="工时(小时)" width="100" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="['','success','warning','warning','danger'][row.status]">{{ ['','正常','迟到','早退','缺勤'][row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑考勤' : '录入考勤'" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="派遣ID"><el-input-number v-model="form.dispatchId" :min="1" /></el-form-item>
        <el-form-item label="员工ID"><el-input-number v-model="form.employeeId" :min="1" /></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="form.workDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="工时"><el-input-number v-model="form.workHours" :min="0" :max="24" :precision="1" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option :value="1" label="正常" /><el-option :value="2" label="迟到" />
            <el-option :value="3" label="早退" /><el-option :value="4" label="缺勤" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const loading = ref(false)
const employeeId = ref(null)
const dateRange = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ id: null, dispatchId: null, employeeId: null, workDate: '', workHours: 8, status: 1 })

const loadData = async () => {
  loading.value = true
  const params = { current: page.value, size: size.value }
  if (employeeId.value) params.employeeId = employeeId.value
  if (dateRange.value?.length === 2) {
    params.startDate = dateRange.value[0]
    params.endDate = dateRange.value[1]
  }
  const res = await request.get('/attendance/page', { params })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

const handleAdd = () => {
  Object.assign(form, { id: null, dispatchId: null, employeeId: null, workDate: '', workHours: 8, status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.id) {
    await request.put('/attendance', form)
  } else {
    await request.post('/attendance', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除？', '提示')
  await request.delete(`/attendance/${id}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; }
.el-pagination { margin-top: 16px; justify-content: flex-end; }
</style>
