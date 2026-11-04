import axios from 'axios'
import { Message } from 'element-ui'
import { getToken, removeToken } from '@/utils/auth'

// rbac 后端所有接口都在 /API/** 下，dev 环境由 vue.config.js 的 proxy 转发到 8080
const service = axios.create({
  baseURL: '/API',
  timeout: 10000,
  // Spring 绑定 Long[] ids 需要 ids=1&ids=2 格式；
  // axios 默认会序列化成 ids[]=1&ids[]=2，后端取不到参数会返回 1003 参数校验失败
  paramsSerializer: {
    indexes: null
  }
})

service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      // 注意：rbac 的 LoginInterceptor 直接解析 Authorization，不能加 Bearer 前缀
      config.headers.Authorization = token
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const res = response.data

    // 1001 = 没有登录，清掉本地状态并回登录页
    if (res.code === 1001) {
      removeToken()
      Message.warning('登录已失效，请重新登录')
      window.location.hash = '#/login'
      return Promise.reject(new Error(res.msg))
    }

    // rbac 成功码是 0（ResultCode.SUCCESS 用的是八进制字面量 0000）
    if (res.code !== 0) {
      // 错误文案放在 data 里，msg 只是枚举的通用描述
      Message.warning(res.data || res.msg || '请求失败')
      return Promise.reject(new Error(res.data || res.msg))
    }

    return res
  },
  error => {
    Message.error(error.message || '系统错误')
    return Promise.reject(error)
  }
)

export default service
