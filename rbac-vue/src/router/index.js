import Vue from 'vue'
import VueRouter from 'vue-router'
import { getToken } from '@/utils/auth'

Vue.use(VueRouter)

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/', name: 'Home', component: () => import('@/views/Home.vue') },
  { path: '/employee', name: 'EmployeeList', component: () => import('@/views/EmployeeList.vue') },
  { path: '*', redirect: '/' }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  const hasToken = !!getToken()
  if (to.path !== '/login' && !hasToken) {
    next('/login')
    return
  }
  if (to.path === '/login' && hasToken) {
    next('/')
    return
  }
  next()
})

export default router
