<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="居民姓名" prop="personName">
        <el-input v-model="queryParams.personName" placeholder="请输入居民姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份证号" prop="cardId">
        <el-input v-model="queryParams.cardId" placeholder="请输入身份证号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="疾病类型" prop="diseaseType">
        <el-select v-model="queryParams.diseaseType" placeholder="请选择疾病类型" clearable size="small">
          <el-option label="高血压" value="高血压" />
          <el-option label="糖尿病" value="糖尿病" />
          <el-option label="脑卒中" value="脑卒中" />
          <el-option label="冠心病" value="冠心病" />
          <el-option label="慢阻肺" value="慢阻肺" />
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
      <el-table-column prop="screeningDate" label="筛查日期" min-width="110" align="center" />
      <el-table-column prop="screeningLocation" label="筛查地点" min-width="150" align="center" show-overflow-tooltip />
      <el-table-column prop="screeningResult" label="筛查结果" min-width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.screeningResult === 'positive' || scope.row.screeningResult === '阳性'" type="danger">阳性</el-tag>
          <el-tag v-else-if="scope.row.screeningResult === 'negative' || scope.row.screeningResult === '阴性'" type="success">阴性</el-tag>
          <el-tag v-else type="warning">{{ scope.row.screeningResult || '待确认' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="diseaseType" label="疾病类型" min-width="100" align="center" />
      <el-table-column prop="handler" label="处理人" min-width="100" align="center" />
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
            <el-form-item label="筛查日期" prop="screeningDate">
              <el-date-picker v-model="form.screeningDate" type="date" placeholder="请选择筛查日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="筛查地点" prop="screeningLocation">
          <el-input v-model="form.screeningLocation" placeholder="请输入筛查地点" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="筛查结果" prop="screeningResult">
              <el-select v-model="form.screeningResult" placeholder="请选择筛查结果" style="width:100%">
                <el-option label="阳性" value="阳性" />
                <el-option label="阴性" value="阴性" />
                <el-option label="待确认" value="待确认" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="疾病类型" prop="diseaseType">
              <el-select v-model="form.diseaseType" placeholder="请选择疾病类型" style="width:100%">
                <el-option label="高血压" value="高血压" />
                <el-option label="糖尿病" value="糖尿病" />
                <el-option label="脑卒中" value="脑卒中" />
                <el-option label="冠心病" value="冠心病" />
                <el-option label="慢阻肺" value="慢阻肺" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="处理人" prop="handler">
          <el-input v-model="form.handler" placeholder="请输入处理人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="机构外筛查详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="居民姓名">{{ detailData.personName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ genderLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.cardId }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phoneNumber }}</el-descriptions-item>
        <el-descriptions-item label="筛查日期">{{ detailData.screeningDate }}</el-descriptions-item>
        <el-descriptions-item label="筛查地点">{{ detailData.screeningLocation }}</el-descriptions-item>
        <el-descriptions-item label="筛查结果">{{ detailData.screeningResult }}</el-descriptions-item>
        <el-descriptions-item label="疾病类型">{{ detailData.diseaseType }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detailData.handler }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listOutside, getOutside, addOutside, updateOutside, delOutside } from '@/api/screening/outside'

export default {
  name: 'ScreeningOutside',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      total: 0,
      queryParams: {
        pageNum: 1, pageSize: 10, personName: '', cardId: '', diseaseType: ''
      },
      form: {},
      detailData: {},
      rules: {
        idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        screeningDate: [{ required: true, message: '请选择筛查日期', trigger: 'change' }]
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
      listOutside(this.queryParams).then(res => {
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
      this.queryParams = { pageNum: 1, pageSize: 10, personName: '', cardId: '', diseaseType: '' }
      this.handleQuery()
    },
    handleAdd() {
      this.dialogTitle = '新增机构外筛查'
      this.form = { idCard: '', screeningDate: '', screeningLocation: '', screeningResult: '', diseaseType: '', handler: '', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑机构外筛查'
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
        delOutside(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateOutside : addOutside
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
