<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="项目名称" prop="itemName">
        <el-input v-model="queryParams.itemName" placeholder="请输入项目名称" clearable />
      </el-form-item>
      <el-form-item label="项目类型" prop="itemType">
        <el-select v-model="queryParams.itemType" placeholder="请选择类型" clearable>
          <el-option label="付费项目" value="付费项目" />
          <el-option label="免费项目" value="免费项目" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增服务项目</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="项目编号" width="100" align="center" />
      <el-table-column prop="itemName" label="项目名称" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="itemType" label="项目类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.itemType === '免费项目' ? 'success' : 'warning'" size="small">{{ scope.row.itemType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="frequency" label="次数" width="140" align="center" />
      <el-table-column prop="description" label="项目介绍" min-width="250" align="center" show-overflow-tooltip />
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">{{ scope.row.status === '1' ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-form-item label="项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目类型" prop="itemType">
          <el-select v-model="form.itemType" placeholder="请选择类型" style="width:100%">
            <el-option label="付费项目" value="付费项目" />
            <el-option label="免费项目" value="免费项目" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务次数" prop="frequency">
          <el-input v-model="form.frequency" placeholder="如：每月1次、每季度1次、每年1次" />
        </el-form-item>
        <el-form-item label="服务介绍" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入服务项目描述" />
        </el-form-item>
        <el-form-item label="执行标准">
          <el-input v-model="form.executionStandard" type="textarea" :rows="2" placeholder="请输入执行标准（可选）" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listServiceItem, getServiceItem, addServiceItem, updateServiceItem, delServiceItem } from '@/api/contract/index'
import Pagination from '@/components/Pagination'

export default {
  name: 'ContractServiceItem',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, itemName: '', itemType: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      tableData: [],
      form: { itemName: '', itemType: '', frequency: '', description: '', executionStandard: '', status: '1' },
      rules: {
        itemName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        itemType: [{ required: true, message: '请选择项目类型', trigger: 'change' }],
        frequency: [{ required: true, message: '请输入服务次数', trigger: 'blur' }],
        description: [{ required: true, message: '请输入服务介绍', trigger: 'blur' }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listServiceItem(this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryParams = { pageNum: 1, pageSize: 10, itemName: '', itemType: '' }
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '新增服务项目'
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.resetFields()
        this.form = { itemName: '', itemType: '', frequency: '', description: '', executionStandard: '', status: '1' }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑服务项目'
      this.isEdit = true
      this.dialogVisible = true
      this.$nextTick(() => { this.form = { ...row } })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            updateServiceItem(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addServiceItem(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除服务项目"' + row.itemName + '"?', '警告', { type: 'warning' }).then(() => {
        delServiceItem(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; margin-bottom: 10px; }
.mb8 { margin: 10px 0; }
</style>
