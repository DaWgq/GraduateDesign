<template>
  <div class="page">
    <el-card>
      <template #header><span>企业信息</span></template>
      <el-form :model="form" label-width="120px" style="max-width:600px">
        <el-form-item label="企业名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="统一社会信用代码"><el-input v-model="form.creditCode" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contactPerson" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.contactPhone" /></el-form-item>
        <el-form-item label="企业地址"><el-input v-model="form.address" type="textarea" /></el-form-item>
        <el-form-item label="审核状态">
          <el-tag :type="['warning','success','danger'][form.auditStatus]">{{ ['待审核','已通过','已拒绝'][form.auditStatus] }}</el-tag>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import request from '../../utils/request'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const form = reactive({ id: null, name: '', creditCode: '', contactPerson: '', contactPhone: '', address: '', auditStatus: 0 })

onMounted(async () => {
  const res = await request.get('/enterprise/my')
  if (res.data) Object.assign(form, res.data)
})

const handleSave = async () => {
  if (form.id) {
    await request.put('/enterprise', form)
  } else {
    await request.post('/enterprise', { ...form, userId: userStore.userInfo.id })
  }
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.page { padding: 20px; }
</style>
