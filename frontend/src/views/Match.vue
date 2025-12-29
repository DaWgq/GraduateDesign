<template>
  <div class="page">
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="为岗位匹配人才" name="job">
          <div class="search-box">
            <el-input-number v-model="jobId" placeholder="岗位ID" style="width:150px" />
            <el-button type="primary" @click="matchForJob">智能匹配</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="为人才推荐岗位" name="employee">
          <div class="search-box">
            <el-input-number v-model="employeeId" placeholder="员工ID" style="width:150px" />
            <el-button type="primary" @click="matchForEmployee">智能推荐</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>

      <el-table :data="matchResults" v-loading="loading" style="margin-top:20px">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="skills" label="技能" />
        <el-table-column prop="salary" label="薪资" width="100" />
        <el-table-column prop="location" label="地点" v-if="activeTab === 'employee'" />
        <el-table-column prop="matchScore" label="匹配度" width="100">
          <template #default="{ row }">
            <el-progress :percentage="row.matchScore" :color="getScoreColor(row.matchScore)" :stroke-width="10" />
          </template>
        </el-table-column>
        <el-table-column prop="matchReason" label="匹配说明" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const activeTab = ref('job')
const jobId = ref(null)
const employeeId = ref(null)
const matchResults = ref([])
const loading = ref(false)

const matchForJob = async () => {
  if (!jobId.value) return ElMessage.warning('请输入岗位ID')
  loading.value = true
  const res = await request.get(`/match/employees/${jobId.value}`)
  matchResults.value = res.data
  loading.value = false
}

const matchForEmployee = async () => {
  if (!employeeId.value) return ElMessage.warning('请输入员工ID')
  loading.value = true
  const res = await request.get(`/match/jobs/${employeeId.value}`)
  matchResults.value = res.data
  loading.value = false
}

const getScoreColor = (score) => {
  if (score >= 80) return '#67C23A'
  if (score >= 60) return '#E6A23C'
  return '#F56C6C'
}
</script>

<style scoped>
.page { padding: 20px; }
.search-box { display: flex; gap: 10px; margin-top: 10px; }
</style>
