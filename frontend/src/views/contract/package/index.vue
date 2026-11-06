<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="服务包名称" prop="packageName">
        <el-input v-model="queryParams.packageName" placeholder="请输入名称" clearable />
      </el-form-item>
      <el-form-item label="服务包类型" prop="packageType">
        <el-select v-model="queryParams.packageType" placeholder="请选择类型" clearable>
          <el-option label="基础包" value="基础包" />
          <el-option label="升级包" value="升级包" />
          <el-option label="综合包" value="综合包" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" value="1" />
          <el-option label="停用" value="0" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增服务包</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">查看服务</el-button>
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="包编号" width="80" align="center" />
      <el-table-column prop="packageName" label="服务包名称" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="packageType" label="服务包类型" width="100" align="center" />
      <el-table-column prop="price" label="订购价格(元)" width="120" align="center" />
      <el-table-column prop="applicableDiseases" label="适用病种" width="160" align="center" show-overflow-tooltip />
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">{{ scope.row.status === '1' ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务包名称" prop="packageName">
              <el-input v-model="form.packageName" placeholder="请输入服务包名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务包类型" prop="packageType">
              <el-select v-model="form.packageType" placeholder="请选择类型" style="width:100%">
                <el-option label="基础包" value="基础包" />
                <el-option label="升级包" value="升级包" />
                <el-option label="综合包" value="综合包" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订购价格(元)" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用病种" prop="applicableDiseases">
              <el-select v-model="form.applicableDiseases" multiple placeholder="请选择适用病种" style="width:100%">
                <el-option label="高血压" value="高血压" />
                <el-option label="糖尿病" value="糖尿病" />
                <el-option label="冠心病" value="冠心病" />
                <el-option label="慢阻肺" value="慢阻肺" />
                <el-option label="脑卒中" value="脑卒中" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="服务包介绍" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入服务包简介" />
        </el-form-item>
        <el-form-item label="服务项目" prop="serviceIds">
          <el-select v-model="form.serviceIds" multiple filterable placeholder="请选择包含的服务项目" style="width:100%">
            <el-option v-for="item in serviceItemOptions" :key="item.id" :label="item.itemName" :value="item.id" />
          </el-select>
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

    <!-- 查看服务详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailVisible" width="700px" append-to-body>
      <el-table :data="currentServices" border stripe size="small">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="itemName" label="服务项目" min-width="160" align="center" show-overflow-tooltip />
        <el-table-column prop="itemType" label="项目类型" width="100" align="center" />
        <el-table-column prop="frequency" label="服务频次" width="100" align="center" />
        <el-table-column prop="description" label="描述" min-width="180" align="center" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listPackage, getPackage, addPackage, updatePackage, delPackage } from '@/api/contract/index'
import { listServiceItem } from '@/api/contract/index'
import Pagination from '@/components/Pagination'

export default {
  name: 'ContractPackage',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, packageName: '', packageType: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      detailVisible: false,
      detailTitle: '',
      currentServices: [],
      tableData: [],
      serviceItemOptions: [],
      form: { packageName: '', packageType: '', price: 0, applicableDiseases: [], description: '', serviceIds: [], status: '1' },
      rules: {
        packageName: [{ required: true, message: '请输入服务包名称', trigger: 'blur' }],
        packageType: [{ required: true, message: '请选择类型', trigger: 'change' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        applicableDiseases: [{ required: true, message: '请选择适用病种', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.fetchServiceItems()
  },
  methods: {
    getList() {
      this.loading = true
      listPackage(this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    fetchServiceItems() {
      listServiceItem({ pageNum: 1, pageSize: 100 }).then(res => {
        this.serviceItemOptions = res.rows || []
      }).catch(() => {})
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryParams = { pageNum: 1, pageSize: 10, packageName: '', packageType: '', status: '' }
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '新增服务包'
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.resetFields()
        this.form = { packageName: '', packageType: '', price: 0, applicableDiseases: [], description: '', serviceIds: [], status: '1' }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑服务包'
      this.isEdit = true
      this.dialogVisible = true
      this.$nextTick(() => {
        this.form = { ...row }
        // Ensure arrays
        if (typeof this.form.applicableDiseases === 'string') {
          this.form.applicableDiseases = this.form.applicableDiseases.split(',').filter(Boolean)
        }
        if (typeof this.form.serviceIds === 'string') {
          this.form.serviceIds = this.form.serviceIds.split(',').map(Number).filter(Boolean)
        }
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const data = { ...this.form }
          // Convert arrays to comma-separated strings
          if (Array.isArray(data.applicableDiseases)) {
            data.applicableDiseases = data.applicableDiseases.join(',')
          }
          if (Array.isArray(data.serviceIds)) {
            data.serviceIds = data.serviceIds.join(',')
          }
          if (this.isEdit) {
            updatePackage(data).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addPackage(data).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDetail(row) {
      this.detailTitle = row.packageName + ' - 包含服务'
      // If serviceIds is a string, we can show a simplified view
      const ids = typeof row.serviceIds === 'string'
        ? row.serviceIds.split(',').map(Number).filter(Boolean)
        : (row.serviceIds || [])
      if (ids.length && this.serviceItemOptions.length) {
        // Match service items from the loaded options
        this.currentServices = this.serviceItemOptions.filter(s => ids.includes(s.id))
      } else {
        this.currentServices = []
      }
      this.detailVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除服务包"' + row.packageName + '"?', '警告', { type: 'warning' }).then(() => {
        delPackage(row.id).then(() => {
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
