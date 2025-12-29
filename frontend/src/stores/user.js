import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  const login = async (username, password) => {
    const res = await request.post('/user/login', { username, password })
    token.value = res.data.token
    userInfo.value = res.data.userInfo
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userInfo', JSON.stringify(res.data.userInfo))
  }

  const getUserInfo = async () => {
    if (userInfo.value) return userInfo.value
    const cached = localStorage.getItem('userInfo')
    if (cached) {
      userInfo.value = JSON.parse(cached)
      return userInfo.value
    }
    const res = await request.get('/user/info')
    userInfo.value = res.data
    localStorage.setItem('userInfo', JSON.stringify(res.data))
    return res.data
  }

  const logout = async () => {
    await request.post('/user/logout')
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const isAdmin = () => userInfo.value?.role === 1
  const isEnterprise = () => userInfo.value?.role === 2
  const isEmployee = () => userInfo.value?.role === 3

  return { userInfo, token, login, getUserInfo, logout, isAdmin, isEnterprise, isEmployee }
})
