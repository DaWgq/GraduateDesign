import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
      
      // 管理员路由
      { path: 'user', name: 'UserManage', component: () => import('../views/admin/UserManage.vue'), meta: { roles: [1] } },
      { path: 'employee', name: 'Employee', component: () => import('../views/Employee.vue'), meta: { roles: [1] } },
      { path: 'enterprise', name: 'Enterprise', component: () => import('../views/Enterprise.vue'), meta: { roles: [1] } },
      { path: 'report', name: 'Report', component: () => import('../views/Report.vue'), meta: { roles: [1] } },
      
      // 管理员和企业共用
      { path: 'job', name: 'Job', component: () => import('../views/Job.vue'), meta: { roles: [1, 2] } },
      { path: 'match', name: 'Match', component: () => import('../views/Match.vue'), meta: { roles: [1, 2] } },
      { path: 'dispatch', name: 'Dispatch', component: () => import('../views/Dispatch.vue'), meta: { roles: [1, 2] } },
      { path: 'contract', name: 'Contract', component: () => import('../views/Contract.vue'), meta: { roles: [1, 2] } },
      { path: 'attendance', name: 'Attendance', component: () => import('../views/Attendance.vue'), meta: { roles: [1, 2] } },
      { path: 'salary', name: 'Salary', component: () => import('../views/Salary.vue'), meta: { roles: [1, 2] } },
      
      // 企业专属
      { path: 'enterprise/profile', name: 'EnterpriseProfile', component: () => import('../views/enterprise/EnterpriseProfile.vue'), meta: { roles: [2] } },
      
      // 员工专属
      { path: 'employee/profile', name: 'MyProfile', component: () => import('../views/employee/MyProfile.vue'), meta: { roles: [3] } },
      { path: 'job/list', name: 'JobList', component: () => import('../views/employee/JobList.vue'), meta: { roles: [3] } },
      { path: 'employee/dispatch', name: 'MyDispatch', component: () => import('../views/employee/MyDispatch.vue'), meta: { roles: [3] } },
      { path: 'employee/attendance', name: 'MyAttendance', component: () => import('../views/employee/MyAttendance.vue'), meta: { roles: [3] } },
      { path: 'employee/salary', name: 'MySalary', component: () => import('../views/employee/MySalary.vue'), meta: { roles: [3] } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.meta.roles) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (to.meta.roles.includes(userInfo.role)) {
      next()
    } else {
      next('/dashboard')
    }
  } else {
    next()
  }
})

export default router
