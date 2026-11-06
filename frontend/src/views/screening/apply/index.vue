<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="居民姓名" prop="personName">
        <el-input v-model="queryParams.personName" placeholder="请输入居民姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份证号" prop="cardId">
        <el-input v-model="queryParams.cardId" placeholder="请输入身份证号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="申请类型" prop="applyType">
        <el-select v-model="queryParams.applyType" placeholder="请选择申请类型" clearable size="small">
          <el-option label="慢病筛查" value="screening" />
          <el-option label="健康评估" value="assessment" />
          <el-option label="慢病管理" value="management" />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="auditStatus">
        <el-select v-model="queryParams.auditStatus" placeholder="请选择审核状态" clearable size="small">
          <el-option label="待审核" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已驳回" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" border stripe>
      <el-table-column label="序号" type="index" width="55" align="center" />
      <el-table-column prop="personName" label="居民姓名" min-width="100" align="center" />
      <el-table-column label="性别" width="60" align="center">
        <template slot-scope="scope">{{ genderLabel(scope.row) }}</template>
      </el-table-column>
      <el-table-column prop="cardId" label="身份证号" min-width="180" align="center" />
      <el-table-column prop="phoneNumber" label="联系电话" min-width="120" align="center" />
      <el-table-column prop="applyDate" label="申请日期" min-width="110" align="center" />
      <el-table-column prop="applyType" label="申请类型" min-width="110" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.applyType === 'screening'" type="primary">慢病筛查</el-tag>
          <el-tag v-else-if="scope.row.applyType === 'assessment'" type="warning">健康评估</el-tag>
          <el-tag v-else-if="scope.row.applyType === 'management'" type="success">慢病管理</el-tag>
          <span v-else>{{ scope.row.applyType }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="diseaseHistory" label="疾病史" min-width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="auditStatus" label="审核状态" min-width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.auditStatus === '0'" type="info">待审核</el-tag>
          <el-tag v-else-if="scope.row.auditStatus === '1'" type="success">已通过</el-tag>
          <el-tag v-else-if="scope.row.auditStatus === '2'" type="danger">已驳回</el-tag>
          <span v-else>{{ scope.row.auditStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryParams.pageNum"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      style="margin-top: 16px;"
    />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号（关联 mp_person）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请日期" prop="applyDate">
              <el-date-picker v-model="form.applyDate" type="date" placeholder="请选择申请日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请类型" prop="applyType">
              <el-select v-model="form.applyType" placeholder="请选择申请类型" style="width:100%">
                <el-option label="慢病筛查" value="screening" />
                <el-option label="健康评估" value="assessment" />
                <el-option label="慢病管理" value="management" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核状态" prop="auditStatus">
              <el-select v-model="form.auditStatus" placeholder="请选择审核状态" style="width:100%">
                <el-option label="待审核" value="0" />
                <el-option label="已通过" value="1" />
                <el-option label="已驳回" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="疾病史" prop="diseaseHistory">
          <el-input v-model="form.diseaseHistory" placeholder="请输入疾病史" />
        </el-form-item>
        <el-form-item label="申请说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入申请说明" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="居民申请详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="居民姓名">{{ detailData.personName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ genderLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.cardId }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phoneNumber }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ detailData.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="申请类型">{{ detailData.applyType }}</el-descriptions-item>
        <el-descriptions-item label="疾病史">{{ detailData.diseaseHistory }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">{{ detailData.auditStatus }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listApply, getApply, addApply, updateApply, delApply } from '@/api/screening/apply'

export default {
  name: 'ScreeningApply',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      total: 0,
      queryParams: {
        pageNum: 1, pageSize: 10, personName: '', cardId: '', applyType: '', auditStatus: ''
      },
      form: {},
      detailData: {},
      rules: {
        idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        applyDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }]
      },
      tableData: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    genderLabel(row) {
      const v = row && row.genderSourceValue
      if (v === '0') return '男'
      if (v === '1') return '女'
      return (row && row.sex) || '--'
    },
    getList() {
      this.loading = true
      listApply(this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = { pageNum: 1, pageSize: 10, personName: '', cardId: '', applyType: '', auditStatus: '' }
      this.handleQuery()
    },
    handleAdd() {
      this.dialogTitle = '新增居民申请'
      this.form = { idCard: '', applyDate: '', applyType: 'screening', auditStatus: '0', diseaseHistory: '', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑居民申请'
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleDetail(row) {
      this.detailData = { ...row }
      this.detailVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该条记录吗？', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        delApply(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateApply : addApply
          api(this.form).then(() => {
            this.$message.success(this.form.id ? '编辑成功' : '新增成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleExport() {
      this.$message.info('导出功能开发中')
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
</style>
