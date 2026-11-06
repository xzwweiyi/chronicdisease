<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="居民姓名" prop="personName">
        <el-input v-model="queryParams.personName" placeholder="请输入居民姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份证号" prop="cardId">
        <el-input v-model="queryParams.cardId" placeholder="请输入身份证号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="体检机构" prop="examInstitution">
        <el-input v-model="queryParams.examInstitution" placeholder="请输入体检机构" clearable size="small" @keyup.enter.native="handleQuery" />
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
      <el-table-column prop="examDate" label="体检日期" min-width="110" align="center" />
      <el-table-column prop="examInstitution" label="体检机构" min-width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="bmi" label="BMI" width="80" align="center" />
      <el-table-column prop="bloodPressure" label="血压(mmHg)" min-width="120" align="center" />
      <el-table-column prop="bloodSugar" label="血糖(mmol/L)" min-width="120" align="center" />
      <el-table-column prop="diseaseRisk" label="疾病风险" min-width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.diseaseRisk === 'high' || scope.row.diseaseRisk === '高风险'" type="danger">高风险</el-tag>
          <el-tag v-else-if="scope.row.diseaseRisk === 'medium' || scope.row.diseaseRisk === '中风险'" type="warning">中风险</el-tag>
          <el-tag v-else-if="scope.row.diseaseRisk === 'low' || scope.row.diseaseRisk === '低风险'" type="success">低风险</el-tag>
          <span v-else>{{ scope.row.diseaseRisk }}</span>
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
            <el-form-item label="体检日期" prop="examDate">
              <el-date-picker v-model="form.examDate" type="date" placeholder="请选择体检日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="体检机构" prop="examInstitution">
          <el-input v-model="form.examInstitution" placeholder="请输入体检机构" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="BMI" prop="bmi">
              <el-input-number v-model="form.bmi" :precision="1" :min="0" :max="999" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="血压" prop="bloodPressure">
              <el-input v-model="form.bloodPressure" placeholder="请输入血压" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="血糖" prop="bloodSugar">
              <el-input v-model="form.bloodSugar" placeholder="请输入血糖" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="血脂" prop="bloodLipids">
              <el-input v-model="form.bloodLipids" placeholder="请输入血脂" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="疾病风险" prop="diseaseRisk">
              <el-select v-model="form.diseaseRisk" placeholder="请选择疾病风险" style="width:100%">
                <el-option label="高风险" value="high" />
                <el-option label="中风险" value="medium" />
                <el-option label="低风险" value="low" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="体检结果" prop="examResult">
          <el-input v-model="form.examResult" placeholder="请输入体检结果" />
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

    <el-dialog title="体检筛查详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="居民姓名">{{ detailData.personName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ genderLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.cardId }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phoneNumber }}</el-descriptions-item>
        <el-descriptions-item label="体检日期">{{ detailData.examDate }}</el-descriptions-item>
        <el-descriptions-item label="体检机构">{{ detailData.examInstitution }}</el-descriptions-item>
        <el-descriptions-item label="BMI">{{ detailData.bmi }}</el-descriptions-item>
        <el-descriptions-item label="血压(mmHg)">{{ detailData.bloodPressure }}</el-descriptions-item>
        <el-descriptions-item label="血糖(mmol/L)">{{ detailData.bloodSugar }}</el-descriptions-item>
        <el-descriptions-item label="血脂">{{ detailData.bloodLipids }}</el-descriptions-item>
        <el-descriptions-item label="疾病风险">{{ detailData.diseaseRisk }}</el-descriptions-item>
        <el-descriptions-item label="体检结果" :span="2">{{ detailData.examResult }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listPhysical, getPhysical, addPhysical, updatePhysical, delPhysical } from '@/api/screening/physical'

export default {
  name: 'ScreeningPhysical',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      total: 0,
      queryParams: {
        pageNum: 1, pageSize: 10, personName: '', cardId: '', examInstitution: ''
      },
      form: {},
      detailData: {},
      rules: {
        idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        examDate: [{ required: true, message: '请选择体检日期', trigger: 'change' }]
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
      listPhysical(this.queryParams).then(res => {
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
      this.queryParams = { pageNum: 1, pageSize: 10, personName: '', cardId: '', examInstitution: '' }
      this.handleQuery()
    },
    handleAdd() {
      this.dialogTitle = '新增体检筛查'
      this.form = { idCard: '', examDate: '', examInstitution: '', bmi: null, bloodPressure: '', bloodSugar: '', bloodLipids: '', examResult: '', diseaseRisk: '', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑体检筛查'
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
        delPhysical(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updatePhysical : addPhysical
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
