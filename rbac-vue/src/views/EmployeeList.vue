<template>
  <div class="page">
    <el-page-header content="员工管理" @back="$router.push('/')" />

    <el-card class="table-card">
      <div class="toolbar">
        <el-button v-if="hasPerm(4001)" type="primary" icon="el-icon-plus" @click="openCreate">新增员工</el-button>
        <el-button
          v-if="hasPerm(4002)"
          type="danger"
          icon="el-icon-delete"
          :disabled="!selectedIds.length"
          @click="handleBatchDelete"
        >
          批量删除（{{ selectedIds.length }}）
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="records"
        border
        stripe
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="45" :selectable="() => hasPerm(4002)" />
        <el-table-column prop="name" label="姓名" width="110" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="170">
          <template slot-scope="{ row }">{{ row.email || '-' }}</template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" align="center">
          <template slot-scope="{ row }">{{ row.age === null || row.age === undefined ? '-' : row.age }}</template>
        </el-table-column>
        <el-table-column prop="salary" label="薪资（元）" width="120" align="right">
          <template slot-scope="{ row }">{{ Number(row.salary).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="companyName" label="所属公司" width="120" />
        <el-table-column prop="createTime" label="入职时间" width="170">
          <template slot-scope="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="{ row }">
            <el-button v-if="hasPerm(4003)" type="text" icon="el-icon-edit" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="hasPerm(4002)" type="text" class="danger-text" icon="el-icon-delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pager"
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        :page-size="size"
        :current-page.sync="current"
        @current-change="loadPage"
      />
    </el-card>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog :title="form.id ? '编辑员工' : '新增员工'" :visible.sync="dialogVisible" width="480px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" maxlength="50" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" maxlength="11" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="选填" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="form.age" :min="0" :max="150" :precision="0" placeholder="选填" style="width: 100%" />
        </el-form-item>
        <el-form-item label="薪资" prop="salary">
          <el-input-number v-model="form.salary" :min="0" :precision="2" :step="500" style="width: 100%" />
        </el-form-item>
        <el-form-item label="所属公司" prop="companyId">
          <el-select v-model="form.companyId" placeholder="请选择" style="width: 100%">
            <el-option v-for="c in companies" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getEmployeePage, createEmployee, updateEmployee, deleteEmployee, getCompanyList } from '@/api/employee'
import { getUser } from '@/utils/auth'

// 按钮权限 id，与后端 @Auth(id = 4000 + n) 对应，resource 表中登记
const PERM = { CREATE: 4001, DELETE: 4002, UPDATE: 4003, PAGE: 4004 }

export default {
  name: 'EmployeeList',
  data() {
    return {
      loading: false,
      submitting: false,
      records: [],
      total: 0,
      size: 10,
      current: 1,
      selectedIds: [],
      companies: [],
      dialogVisible: false,
      form: this.emptyForm(),
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
        salary: [{ required: true, message: '请输入薪资', trigger: 'blur' }],
        companyId: [{ required: true, message: '请选择公司', trigger: 'change' }]
      }
    }
  },
  created() {
    this.loadPage(1)
    // 无 4004 权限的用户不该看到本页（路由层后续可加 meta 控制）
    if (!this.hasPerm(PERM.PAGE)) {
      this.$message.error('你没有查询员工的权限')
    }
    this.loadCompanies()
  },
  methods: {
    emptyForm() {
      return { id: null, name: '', phone: '', email: '', age: null, salary: 0, companyId: null }
    },
    // 按钮级权限：登录返回的 resourceIds 是否包含该权限 id
    hasPerm(id) {
      const user = getUser()
      return !!(user && user.resourceIds && user.resourceIds.includes(id))
    },
    async loadPage(page) {
      this.loading = true
      try {
        const res = await getEmployeePage(page)
        this.records = res.data.records
        this.total = res.data.total
        this.size = res.data.size
        this.current = res.data.current
      } catch (e) {
        // 拦截器已统一弹提示
      } finally {
        this.loading = false
      }
    },
    async loadCompanies() {
      try {
        const res = await getCompanyList()
        this.companies = res.data
      } catch (e) { /* 忽略，下拉框为空 */ }
    },
    onSelectionChange(rows) {
      this.selectedIds = rows.map(r => r.id)
    },
    openCreate() {
      this.form = this.emptyForm()
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    openEdit(row) {
      this.form = { id: row.id, name: row.name, phone: row.phone, email: row.email || '', age: row.age, salary: Number(row.salary), companyId: row.companyId }
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.submitting = true
        try {
          if (this.form.id) {
            await updateEmployee(this.form)
          } else {
            await createEmployee(this.form)
          }
          this.$message.success(this.form.id ? '编辑成功' : '新增成功')
          this.dialogVisible = false
          this.loadPage(this.current)
        } catch (e) {
          // 拦截器已统一弹提示（如"手机号已被使用"）
        } finally {
          this.submitting = false
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除员工「${row.name}」？`, '提示', { type: 'warning' })
        .then(async () => {
          await deleteEmployee([row.id])
          this.$message.success('删除成功')
          // 删的是最后一页最后一条时回退一页
          if (this.records.length === 1 && this.current > 1) {
            this.loadPage(this.current - 1)
          } else {
            this.loadPage(this.current)
          }
        })
        .catch(() => {})
    },
    handleBatchDelete() {
      this.$confirm(`确定删除选中的 ${this.selectedIds.length} 名员工？`, '提示', { type: 'warning' })
        .then(async () => {
          await deleteEmployee(this.selectedIds)
          this.$message.success('删除成功')
          this.loadPage(1)
        })
        .catch(() => {})
    },
    formatTime(t) {
      if (!t) return '-'
      return String(t).replace('T', ' ')
    }
  }
}
</script>

<style scoped>
.page {
  padding: 24px;
}
.table-card {
  margin-top: 16px;
}
.toolbar {
  margin-bottom: 14px;
  display: flex;
  gap: 8px;
}
.pager {
  margin-top: 14px;
  text-align: right;
}
.danger-text {
  color: #f56c6c;
}
</style>
