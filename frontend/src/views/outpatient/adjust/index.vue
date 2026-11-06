<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="就诊卡号" prop="visitCardNo">
        <el-input v-model="queryParams.visitCardNo" placeholder="请输入就诊卡号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="调整日期" prop="adjustDate">
        <el-date-picker v-model="queryParams.adjustDate" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" size="small" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="待处理" value="0" />
          <el-option label="已处理" value="1" />
          <el-option label="已归档" value="2" />
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
      <el-table-column prop="name" label="姓名" min-width="100" align="center" />
      <el-table-column prop="visitCardNo" label="就诊卡号" min-width="140" align="center" />
      <el-table-column prop="sysTriageDept" label="系统分诊科室" min-width="140" align="center" />
      <el-table-column prop="doctorAdjustDept" label="医生调整科室" min-width="140" align="center" />
      <el-table-column prop="adjustReason" label="调整原因" min-width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" min-width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="info">待处理</el-tag>
          <el-tag v-if="scope.row.status === '1'" type="primary">已处理</el-tag>
          <el-tag v-if="scope.row.status === '2'" type="success">已归档</el-tag>
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
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="就诊卡号" prop="visitCardNo">
              <el-input v-model="form.visitCardNo" placeholder="请输入就诊卡号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="系统分诊科室" prop="sysTriageDept">
              <el-input v-model="form.sysTriageDept" placeholder="请输入系统分诊科室" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医生调整科室" prop="doctorAdjustDept">
              <el-input v-model="form.doctorAdjustDept" placeholder="请输入医生调整科室" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="调整原因" prop="adjustReason">
          <el-input v-model="form.adjustReason" type="textarea" placeholder="请输入调整原因" :rows="3" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width:100%">
                <el-option label="待处理" value="0" />
                <el-option label="已处理" value="1" />
                <el-option label="已归档" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="调整分诊详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="就诊卡号">{{ detailData.visitCardNo }}</el-descriptions-item>
        <el-descriptions-item label="系统分诊科室">{{ detailData.sysTriageDept }}</el-descriptions-item>
        <el-descriptions-item label="医生调整科室">{{ detailData.doctorAdjustDept }}</el-descriptions-item>
        <el-descriptions-item label="调整原因">{{ detailData.adjustReason }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'OutpatientAdjust',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      total: 0,
      queryParams: {
        pageNum: 1, pageSize: 10, name: '', visitCardNo: '', adjustDate: null, status: ''
      },
      form: {},
      detailData: {},
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        visitCardNo: [{ required: true, message: '请输入就诊卡号', trigger: 'blur' }]
      },
      tableData: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const mockData = [
        {"id": 1, "name": "张三", "visitCardNo": "VC202606001", "sysTriageDept": "心内科", "doctorAdjustDept": "高血压专病门诊", "adjustReason": "患者有高血压并发症，需专科诊治", "status": "1", "remark": ""},
        {"id": 2, "name": "李四", "visitCardNo": "VC202606002", "sysTriageDept": "全科门诊", "doctorAdjustDept": "内分泌科", "adjustReason": "血糖异常，需内分泌专科评估", "status": "1", "remark": ""},
        {"id": 3, "name": "王五", "visitCardNo": "VC202606003", "sysTriageDept": "神经内科", "doctorAdjustDept": "康复科", "adjustReason": "病情稳定，转入康复治疗", "status": "0", "remark": ""},
        {"id": 4, "name": "赵六", "visitCardNo": "VC202606004", "sysTriageDept": "呼吸内科", "doctorAdjustDept": "慢阻肺管理中心", "adjustReason": "慢阻肺长期管理需求", "status": "1", "remark": ""},
        {"id": 5, "name": "孙七", "visitCardNo": "VC202606005", "sysTriageDept": "心内科", "doctorAdjustDept": "老年科", "adjustReason": "多病共存需综合评估", "status": "0", "remark": ""}
      ]
      setTimeout(() => {
        this.tableData = mockData
        this.total = mockData.length
        this.loading = false
      }, 300)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },
    handleAdd() {
      this.dialogTitle = '新增调整分诊'
      this.form = { name: '', visitCardNo: '', sysTriageDept: '', doctorAdjustDept: '', adjustReason: '', status: '', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑调整分诊'
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
        this.$message.success('删除成功')
        this.getList()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id) {
            this.$message.success('编辑成功')
          } else {
            this.$message.success('新增成功')
          }
          this.dialogVisible = false
          this.getList()
        }
      })
    },
    handleExport() {
      this.$message.success('导出成功')
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
