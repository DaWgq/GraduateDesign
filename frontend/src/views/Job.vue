<template>
  <div class="page">
    <el-card>
      <div class="toolbar">
        <el-input v-model="keyword" placeholder="搜索岗位/技能" style="width:200px" @keyup.enter="loadData" />
        <el-input v-model="location" placeholder="工作地点" style="width:150px" />
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="success" @click="handleAdd">发布岗位</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="title" label="岗位名称" />
        <el-table-column prop="skills" label="技能要求" />
        <el-table-column label="薪资范围">
          <template #default="{ row }">{{ row.salaryMin }}-{{ row.salaryMax }} {{ ['','元/时','元/天','元/月'][row.salaryType] }}</template>
        </el-table-column>
        <el-table-column prop="location" label="工作地点" />
        <el-table-column prop="headcount" label="招聘人数" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '招聘中' : '已关闭' }}</el-tag>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑岗位' : '发布岗位'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="岗位名称"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="岗位描述"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="技能要求"><el-input v-model="form.skills" /></el-form-item>
        <el-form-item label="薪资类型">
          <el-select v-model="form.salaryType">
            <el-option :value="1" label="时薪" /><el-option :value="2" label="日薪" /><el-option :value="3" label="月薪" />
          </el-select>
        </el-form-item>
        <el-form-item label="薪资范围">
          <el-input-number v-model="form.salaryMin" :min="0" /> - <el-input-number v-model="form.salaryMax" :min="0" />
        </el-form-item>
        <el-form-item label="工作地点"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="招聘人数"><el-input-number v-model="form.headcount" :min="1" /></el-form-item>
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
const keyword = ref('')
const location = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', description: '', skills: '', salaryType: 3, salaryMin: 0, salaryMax: 0, location: '', headcount: 1 })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/job/page', { params: { current: page.value, size: size.value, keyword: keyword.value, location: location.value } })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

const handleAdd = () => {
  Object.assign(form, { id: null, title: '', description: '', skills: '', salaryType: 3, salaryMin: 0, salaryMax: 0, location: '', headcount: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (form.id) {
    await request.put('/job', form)
  } else {
    await request.post('/job', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除？', '提示')
  await request.delete(`/job/${id}`)
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
