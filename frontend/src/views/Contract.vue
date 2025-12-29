<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-select v-model="status" placeholder="合同状态" clearable style="width:120px" @change="loadData">
          <el-option :value="0" label="待签署" /><el-option :value="1" label="已签署" /><el-option :value="2" label="已终止" />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button type="success" @click="handleAdd">新增合同</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="contractNo" label="合同编号" width="150" />
        <el-table-column prop="contractType" label="合同类型">
          <template #default="{ row }">{{ ['','劳务派遣合同','用工协议'][row.contractType] }}</template>
        </el-table-column>
        <el-table-column prop="employeeId" label="员工ID" width="100" />
        <el-table-column prop="enterpriseId" label="企业ID" width="100" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="['warning','success','danger'][row.status]">{{ ['待签署','已签署','已终止'][row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="success" link v-if="row.status === 0" @click="handleSign(row.id)">签署</el-button>
            <el-button type="warning" link v-if="row.status === 1" @click="handleTerminate(row.id)">终止</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑合同' : '新增合同'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="合同编号"><el-input v-model="form.contractNo" /></el-form-item>
        <el-form-item label="合同类型">
          <el-select v-model="form.contractType">
            <el-option :value="1" label="劳务派遣合同" /><el-option :value="2" label="用工协议" />
          </el-select>
        </el-form-item>
        <el-form-item label="派遣ID"><el-input-number v-model="form.dispatchId" :min="1" /></el-form-item>
        <el-form-item label="员工ID"><el-input-number v-model="form.employeeId" :min="1" /></el-form-item>
        <el-form-item label="企业ID"><el-input-number v-model="form.enterpriseId" :min="1" /></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="结束日期"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
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
const status = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ id: null, contractNo: '', contractType: 1, dispatchId: null, employeeId: null, enterpriseId: null, startDate: '', endDate: '' })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/contract/page', { params: { current: page.value, size: size.value, status: status.value } })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

const handleAdd = () => {
  Object.assign(form, { id: null, contractNo: `HT${Date.now()}`, contractType: 1, dispatchId: null, employeeId: null, enterpriseId: null, startDate: '', endDate: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.id) {
    await request.put('/contract', form)
  } else {
    await request.post('/contract', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleSign = async (id) => {
  await request.post(`/contract/sign/${id}`)
  ElMessage.success('签署成功')
  loadData()
}

const handleTerminate = async (id) => {
  await ElMessageBox.confirm('确定终止该合同？', '提示')
  await request.post(`/contract/terminate/${id}`)
  ElMessage.success('已终止')
  loadData()
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除？', '提示')
  await request.delete(`/contract/${id}`)
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
