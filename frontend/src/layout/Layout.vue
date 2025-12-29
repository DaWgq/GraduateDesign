<template>
  <el-container class="layout">
    <el-aside width="200px">
      <div class="logo">劳务派遣平台</div>
      <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <el-menu-item index="/dashboard"><el-icon><HomeFilled /></el-icon>首页</el-menu-item>
        
        <!-- 管理员菜单 -->
        <template v-if="isAdmin">
          <el-menu-item index="/user"><el-icon><UserFilled /></el-icon>用户管理</el-menu-item>
          <el-menu-item index="/employee"><el-icon><User /></el-icon>人才库</el-menu-item>
          <el-menu-item index="/enterprise"><el-icon><OfficeBuilding /></el-icon>企业库</el-menu-item>
          <el-menu-item index="/job"><el-icon><Briefcase /></el-icon>岗位管理</el-menu-item>
          <el-menu-item index="/match"><el-icon><Search /></el-icon>智能匹配</el-menu-item>
          <el-menu-item index="/dispatch"><el-icon><Connection /></el-icon>派遣管理</el-menu-item>
          <el-menu-item index="/contract"><el-icon><Document /></el-icon>合同管理</el-menu-item>
          <el-menu-item index="/attendance"><el-icon><Calendar /></el-icon>考勤管理</el-menu-item>
          <el-menu-item index="/salary"><el-icon><Money /></el-icon>薪酬结算</el-menu-item>
          <el-menu-item index="/report"><el-icon><DataLine /></el-icon>统计报表</el-menu-item>
        </template>

        <!-- 企业客户菜单 -->
        <template v-if="isEnterprise">
          <el-menu-item index="/enterprise/profile"><el-icon><OfficeBuilding /></el-icon>企业信息</el-menu-item>
          <el-menu-item index="/job"><el-icon><Briefcase /></el-icon>岗位管理</el-menu-item>
          <el-menu-item index="/match"><el-icon><Search /></el-icon>人才匹配</el-menu-item>
          <el-menu-item index="/dispatch"><el-icon><Connection /></el-icon>派遣管理</el-menu-item>
          <el-menu-item index="/contract"><el-icon><Document /></el-icon>合同管理</el-menu-item>
          <el-menu-item index="/attendance"><el-icon><Calendar /></el-icon>考勤管理</el-menu-item>
          <el-menu-item index="/salary"><el-icon><Money /></el-icon>费用结算</el-menu-item>
        </template>

        <!-- 派遣员工菜单 -->
        <template v-if="isEmployee">
          <el-menu-item index="/employee/profile"><el-icon><User /></el-icon>我的简历</el-menu-item>
          <el-menu-item index="/job/list"><el-icon><Briefcase /></el-icon>岗位浏览</el-menu-item>
          <el-menu-item index="/employee/dispatch"><el-icon><Connection /></el-icon>我的派遣</el-menu-item>
          <el-menu-item index="/employee/attendance"><el-icon><Calendar /></el-icon>我的考勤</el-menu-item>
          <el-menu-item index="/employee/salary"><el-icon><Money /></el-icon>我的薪资</el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-right">
          <el-tag :type="roleTagType" size="small">{{ roleName }}</el-tag>
          <span>{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</span>
          <el-button type="text" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { HomeFilled, User, UserFilled, OfficeBuilding, Briefcase, Connection, Money, Search, Document, Calendar, DataLine } from '@element-plus/icons-vue'
import { computed, onMounted } from 'vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
  await userStore.getUserInfo()
})

const isAdmin = computed(() => userStore.userInfo?.role === 1)
const isEnterprise = computed(() => userStore.userInfo?.role === 2)
const isEmployee = computed(() => userStore.userInfo?.role === 3)

const roleName = computed(() => {
  const roles = { 1: '系统管理员', 2: '企业客户', 3: '派遣员工' }
  return roles[userStore.userInfo?.role] || ''
})

const roleTagType = computed(() => {
  const types = { 1: 'danger', 2: 'warning', 3: '' }
  return types[userStore.userInfo?.role] || 'info'
})

const handleLogout = async () => {
  await userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100%; }
.logo { height: 60px; line-height: 60px; text-align: center; color: #fff; font-size: 18px; background: #263445; }
.el-aside { background: #304156; }
.el-header { display: flex; justify-content: flex-end; align-items: center; background: #fff; box-shadow: 0 1px 4px rgba(0,21,41,.08); }
.header-right { display: flex; align-items: center; gap: 16px; }
.el-main { background: #f0f2f5; }
</style>
