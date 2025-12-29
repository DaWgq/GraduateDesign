<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background:#409EFF"><el-icon><User /></el-icon></div>
            <div class="stat-info"><div class="stat-value">{{ stats.employeeCount }}</div><div class="stat-label">人才总数</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background:#67C23A"><el-icon><OfficeBuilding /></el-icon></div>
            <div class="stat-info"><div class="stat-value">{{ stats.enterpriseCount }}</div><div class="stat-label">合作企业</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background:#E6A23C"><el-icon><Briefcase /></el-icon></div>
            <div class="stat-info"><div class="stat-value">{{ stats.jobCount }}</div><div class="stat-label">在招岗位</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background:#F56C6C"><el-icon><Connection /></el-icon></div>
            <div class="stat-info"><div class="stat-value">{{ stats.dispatchCount }}</div><div class="stat-label">在职派遣</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, OfficeBuilding, Briefcase, Connection } from '@element-plus/icons-vue'
import request from '../utils/request'

const stats = ref({ employeeCount: 0, enterpriseCount: 0, jobCount: 0, dispatchCount: 0 })

onMounted(async () => {
  const res = await request.get('/statistics/dashboard')
  stats.value = res.data
})
</script>

<style scoped>
.dashboard { padding: 20px; }
.stat-card { display: flex; align-items: center; }
.stat-icon { width: 60px; height: 60px; border-radius: 8px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 28px; }
.stat-info { margin-left: 16px; }
.stat-value { font-size: 28px; font-weight: bold; color: #303133; }
.stat-label { color: #909399; margin-top: 4px; }
</style>
