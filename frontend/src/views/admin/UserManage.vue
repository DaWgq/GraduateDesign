<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-select v-model="role" placeholder="用户角色" clearable style="width:120px" @change="loadData">
          <el-option :value="1" label="管理员" /><el-option :value="2" label="企业客户" /><el-option :value="3" label="派遣员工" />
        </el-select>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button type="success" @click="handleAdd">新增用户</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-tag :type="['','danger','warning',''][row.role]">{{ ['','管理员','企业客户','派遣员工'][row.role] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button :type="row.status === 1 ? 'danger' : 'success'" link @click="handleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="密码" v-if="!form.id"><el-input v-model="form.password" type="password" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option :value="1" label="管理员" /><el-option :value="2" label="企业客户" /><el-option :value="3" label="派遣员工" />
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
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const loading = ref(false)
const role = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ id: null, username: '', password: '', realName: '', phone: '', email: '', role: 3 })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/user/list', { params: { current: page.value, size: size.value, role: role.value } })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

const handleAdd = () => {
  Object.assign(form, { id: null, username: '', password: '', realName: '', phone: '', email: '', role: 3 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, { ...row, password: '' })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.id) {
    await request.put('/user', form)
  } else {
    await request.post('/user/register', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleStatus = async (row) => {
  await request.post(`/user/status/${row.id}`, null, { params: { status: row.status === 1 ? 0 : 1 } })
  ElMessage.success('操作成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; }
.el-pagination { margin-top: 16px; justify-content: flex-end; }
</style>
