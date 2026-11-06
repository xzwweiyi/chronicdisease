<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="团队名称" prop="teamName">
        <el-input v-model="queryParams.teamName" placeholder="请输入团队名称" clearable />
      </el-form-item>
      <el-form-item label="团队长" prop="leaderName">
        <el-input v-model="queryParams.leaderName" placeholder="请输入团队长" clearable />
      </el-form-item>
      <el-form-item label="服务区域" prop="serviceArea">
        <el-input v-model="queryParams.serviceArea" placeholder="请输入服务区域" clearable />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增团队</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="id" label="团队编号" width="90" align="center" />
      <el-table-column prop="teamName" label="团队名称" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="leaderName" label="团队长" width="100" align="center" />
      <el-table-column prop="leaderPhone" label="联系方式" width="130" align="center" />
      <el-table-column prop="serviceArea" label="服务区域" width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="members" label="成员" min-width="160" align="center" show-overflow-tooltip />
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            active-text="启用"
            inactive-text="停用"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="团队名称" prop="teamName">
              <el-input v-model="form.teamName" placeholder="请输入团队名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="团队长" prop="leaderName">
              <el-input v-model="form.leaderName" placeholder="请输入团队长姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系方式" prop="leaderPhone">
              <el-input v-model="form.leaderPhone" placeholder="请输入联系方式" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务区域" prop="serviceArea">
              <el-input v-model="form.serviceArea" placeholder="请输入服务区域" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="团队成员" prop="members">
          <el-input v-model="form.members" type="textarea" :rows="2" placeholder="请输入团队成员（多人用逗号分隔）" />
        </el-form-item>
        <el-form-item label="团队介绍">
          <el-input v-model="form.teamIntro" type="textarea" :rows="3" placeholder="请输入团队介绍" />
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

    <!-- 详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailVisible" width="550px" append-to-body>
      <el-descriptions v-if="detailData" :column="2" border size="small">
        <el-descriptions-item label="团队编号">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="团队名称">{{ detailData.teamName }}</el-descriptions-item>
        <el-descriptions-item label="团队长">{{ detailData.leaderName }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ detailData.leaderPhone }}</el-descriptions-item>
        <el-descriptions-item label="服务区域" :span="2">{{ detailData.serviceArea }}</el-descriptions-item>
        <el-descriptions-item label="团队成员" :span="2">{{ detailData.members }}</el-descriptions-item>
        <el-descriptions-item label="团队介绍" :span="2">{{ detailData.teamIntro || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === '1' ? 'success' : 'danger'">{{ detailData.status === '1' ? '启用' : '停用' }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listTeam, getTeam, addTeam, updateTeam, delTeam } from '@/api/contract/index'
import Pagination from '@/components/Pagination'

export default {
  name: 'ContractTeam',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, teamName: '', leaderName: '', serviceArea: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      detailVisible: false,
      detailTitle: '',
      detailData: null,
      tableData: [],
      form: { teamName: '', leaderName: '', leaderPhone: '', members: '', serviceArea: '', teamIntro: '', status: '1' },
      rules: {
        teamName: [{ required: true, message: '请输入团队名称', trigger: 'blur' }],
        leaderName: [{ required: true, message: '请输入团队长', trigger: 'blur' }],
        leaderPhone: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
        serviceArea: [{ required: true, message: '请输入服务区域', trigger: 'blur' }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listTeam(this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryParams = { pageNum: 1, pageSize: 10, teamName: '', leaderName: '', serviceArea: '', status: '' }
      this.getList()
    },
    handleStatusChange(row) {
      updateTeam({ id: row.id, status: row.status }).then(() => {
        this.$message.success(row.status === '1' ? '已启用' : '已停用')
      }).catch(() => { this.getList() })
    },
    handleAdd() {
      this.dialogTitle = '新增团队'
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.resetFields()
        this.form = { teamName: '', leaderName: '', leaderPhone: '', members: '', serviceArea: '', teamIntro: '', status: '1' }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑团队'
      this.isEdit = true
      this.dialogVisible = true
      this.$nextTick(() => { this.form = { ...row } })
    },
    handleDetail(row) {
      getTeam(row.id).then(res => {
        this.detailData = res.data
        this.detailTitle = '团队详情 - ' + (this.detailData.teamName || '')
        this.detailVisible = true
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            updateTeam(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addTeam(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除团队"' + row.teamName + '"?', '警告', { type: 'warning' }).then(() => {
        delTeam(row.id).then(() => {
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
