<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-select v-model="status" placeholder="派遣状态" clearable style="width:150px" @change="loadData">
          <el-option :value="1" label="面试邀约" /><el-option :value="2" label="录用" /><el-option :value="3" label="签约" />
          <el-option :value="4" label="入职" /><el-option :value="5" label="在职" /><el-option :value="6" label="离职" /><el-option :value="7" label="退回" />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增派遣</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="employeeId" label="员工ID" />
        <el-table-column prop="jobId" label="岗位ID" />
        <el-table-column prop="enterpriseId" label="企业ID" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="statusTypes[row.status]">{{ statusLabels[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-dropdown @command="(cmd) => handleStatus(row.id, cmd)">
              <el-button type="primary" link>状态流转<el-icon><ArrowDown /></el-icon></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-for="(label, key) in statusLabels" :key="key" :command="Number(key)">{{ label }}</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" />
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增派遣" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="员工ID"><el-input-number v-model="form.employeeId" :min="1" /></el-form-item>
        <el-form-item label="岗位ID"><el-input-number v-model="form.jobId" :min="1" /></el-form-item>
        <el-form-item label="企业ID"><el-input-number v-model="form.enterpriseId" :min="1" /></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
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
import { ArrowDown } from '@element-plus/icons-vue'

const statusLabels = { 1: '面试邀约', 2: '录用', 3: '签约', 4: '入职', 5: '在职', 6: '离职', 7: '退回' }
const statusTypes = { 1: 'info', 2: 'warning', 3: 'primary', 4: 'success', 5: 'success', 6: 'danger', 7: 'danger' }

const tableData = ref([])
const loading = ref(false)
const status = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ employeeId: null, jobId: null, enterpriseId: null, startDate: '', remark: '' })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/dispatch/page', { params: { current: page.value, size: size.value, status: status.value } })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

const handleAdd = () => {
  Object.assign(form, { employeeId: null, jobId: null, enterpriseId: null, startDate: '', remark: '' })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await request.post('/dispatch', form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleStatus = async (id, newStatus) => {
  await request.post(`/dispatch/status/${id}`, null, { params: { status: newStatus } })
  ElMessage.success('状态更新成功')
  loadData()
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除？', '提示')
  await request.delete(`/dispatch/${id}`)
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
