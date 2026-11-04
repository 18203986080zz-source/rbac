<template>
  <div class="home">
    <el-card>
      <div slot="header" class="card-header">
        <span>链路验证</span>
        <div>
          <el-button size="small" @click="$router.push('/employee')">员工管理（待开发）</el-button>
          <el-button size="small" type="danger" plain @click="handleLogout">退出登录</el-button>
        </div>
      </div>

      <el-descriptions :column="1" border>
        <el-descriptions-item label="当前用户">
          {{ user ? user.username : '-' }}（id: {{ user ? user.id : '-' }}）
        </el-descriptions-item>
        <el-descriptions-item label="可访问权限资源数">
          {{ user && user.resourceIds ? user.resourceIds.length : 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="订单数据总数（受数据权限过滤）">
          <span v-if="dataTotal !== null">{{ dataTotal }} 条</span>
          <span v-else>加载中…</span>
        </el-descriptions-item>
        <el-descriptions-item label="用户总数">
          <span v-if="userTotal !== null">{{ userTotal }} 条</span>
          <span v-else>加载中…</span>
        </el-descriptions-item>
      </el-descriptions>

      <el-alert
        class="hint"
        type="success"
        :closable="false"
        title="如果你能看到上面的数字，说明：proxy 代理、裸 token 认证、统一响应解析、数据权限全部打通。"
      />
    </el-card>
  </div>
</template>

<script>
import { getDataPage, getUserPage } from '@/api/user'
import { getUser, removeToken } from '@/utils/auth'

export default {
  name: 'Home',
  data() {
    return {
      user: getUser(),
      dataTotal: null,
      userTotal: null
    }
  },
  created() {
    this.loadTotals()
  },
  methods: {
    async loadTotals() {
      try {
        const data = await getDataPage(1)
        this.dataTotal = data.data.total
      } catch (e) {
        this.dataTotal = 0
      }
      try {
        const users = await getUserPage(1)
        this.userTotal = users.data.total
      } catch (e) {
        this.userTotal = 0
      }
    },
    handleLogout() {
      removeToken()
      this.$router.replace('/login')
    }
  }
}
</script>

<style scoped>
.home {
  padding: 24px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.hint {
  margin-top: 16px;
}
</style>
