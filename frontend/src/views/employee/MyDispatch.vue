<template>
  <div class="page">
    <el-card>
      <template #header><span>我的派遣记录</span></template>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="jobTitle" label="岗位名称" />
        <el-table-column prop="enterpriseName" label="企业名称" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="statusTypes[row.status]">{{ statusLabels[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'

const statusLabels = { 1: '面试邀约', 2: '录用', 3: '签约', 4: '入职', 5: '在职', 6: '离职', 7: '退回' }
const statusTypes = { 1: 'info', 2: 'warning', 3: 'primary', 4: 'success', 5: 'success', 6: 'danger', 7: 'danger' }

const tableData = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  const res = await request.get('/dispatch/my')
  tableData.value = res.data || []
  loading.value = false
})
</script>

<style scoped>
.page { padding: 20px; }
</style>
