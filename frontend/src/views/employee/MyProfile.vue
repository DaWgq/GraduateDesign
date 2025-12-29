<template>
  <div class="page">
    <el-card>
      <template #header><span>我的简历</span></template>
      <el-form :model="form" label-width="100px" style="max-width:600px">
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="身份证号"><el-input v-model="form.idCard" /></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">男</el-radio><el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期"><el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="技能特长"><el-input v-model="form.skills" type="textarea" :rows="3" placeholder="多个技能用逗号分隔" /></el-form-item>
        <el-form-item label="期望薪资"><el-input-number v-model="form.expectedSalary" :min="0" :step="500" /> 元/月</el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const form = reactive({ id: null, name: '', idCard: '', gender: 1, birthDate: '', phone: '', skills: '', expectedSalary: 5000 })

onMounted(async () => {
  const res = await request.get('/employee/my')
  if (res.data) Object.assign(form, res.data)
})

const handleSave = async () => {
  if (form.id) {
    await request.put('/employee', form)
  } else {
    await request.post('/employee', { ...form, userId: userStore.userInfo.id })
  }
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.page { padding: 20px; }
</style>
