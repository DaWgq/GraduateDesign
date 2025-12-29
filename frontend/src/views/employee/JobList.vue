<template>
  <div class="page">
    <el-card>
      <template #header><span>岗位浏览</span></template>
      <div class="toolbar">
        <el-input v-model="keyword" placeholder="搜索岗位/技能" style="width:200px" @keyup.enter="loadData" />
        <el-input v-model="location" placeholder="工作地点" style="width:150px" />
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="job in tableData" :key="job.id">
          <el-card shadow="hover" class="job-card">
            <template #header>
              <div class="job-header">
                <span class="job-title">{{ job.title }}</span>
                <span class="job-salary">{{ job.salaryMin }}-{{ job.salaryMax }} {{ ['','元/时','元/天','元/月'][job.salaryType] }}</span>
              </div>
            </template>
            <p><el-icon><Location /></el-icon> {{ job.location || '不限' }}</p>
            <p><el-icon><Briefcase /></el-icon> {{ job.skills || '不限' }}</p>
            <p><el-icon><User /></el-icon> 招聘 {{ job.headcount }} 人</p>
            <div class="job-footer">
              <el-button type="primary" size="small" @click="handleApply(job)">申请岗位</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" @current-change="loadData" layout="total, prev, pager, next" style="margin-top:20px" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Location, Briefcase, User } from '@element-plus/icons-vue'

const tableData = ref([])
const keyword = ref('')
const location = ref('')
const page = ref(1)
const size = ref(9)
const total = ref(0)

const loadData = async () => {
  const res = await request.get('/job/page', { params: { current: page.value, size: size.value, keyword: keyword.value, location: location.value } })
  tableData.value = res.data.records
  total.value = res.data.total
}

const handleApply = async (job) => {
  await ElMessageBox.confirm(`确定申请【${job.title}】岗位？`, '申请确认')
  await request.post('/dispatch/apply', { jobId: job.id, enterpriseId: job.enterpriseId })
  ElMessage.success('申请已提交')
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 16px; }
.job-card { margin-bottom: 20px; }
.job-header { display: flex; justify-content: space-between; align-items: center; }
.job-title { font-weight: bold; }
.job-salary { color: #F56C6C; font-weight: bold; }
.job-card p { margin: 8px 0; color: #666; display: flex; align-items: center; gap: 5px; }
.job-footer { margin-top: 15px; text-align: right; }
</style>
