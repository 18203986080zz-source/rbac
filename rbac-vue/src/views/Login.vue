<template>
  <div class="login-page">
    <el-card class="login-card">
      <div slot="header" class="login-header">RBAC 登录</div>
      <el-form ref="form" :model="form" label-width="70px" @submit.native.prevent>
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width: 100%" @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-tip">测试账号：admin / admin（能看到全部数据）</div>
    </el-card>
  </div>
</template>

<script>
import { login } from '@/api/user'
import { setToken, setUser } from '@/utils/auth'

export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      form: {
        username: 'admin',
        password: 'admin'
      }
    }
  },
  methods: {
    async handleLogin() {
      if (!this.form.username || !this.form.password) {
        this.$message.warning('请输入用户名和密码')
        return
      }
      this.loading = true
      try {
        const res = await login(this.form)
        setToken(res.data.token)
        setUser(res.data)
        this.$message.success('登录成功')
        this.$router.replace('/')
      } catch (e) {
        // 错误提示已由 axios 响应拦截器统一处理
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}
.login-card {
  width: 380px;
}
.login-header {
  font-size: 18px;
  font-weight: 600;
}
.login-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  text-align: center;
}
</style>
