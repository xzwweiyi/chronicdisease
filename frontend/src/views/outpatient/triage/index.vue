<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="就诊卡号" prop="visitCardNo">
        <el-input v-model="queryParams.visitCardNo" placeholder="请输入就诊卡号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="挂号日期" prop="regDate">
        <el-date-picker v-model="queryParams.regDate" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" size="small" />
      </el-form-item>
      <el-form-item label="风险等级" prop="riskLevel">
        <el-select v-model="queryParams.riskLevel" placeholder="请选择风险等级" clearable size="small">
          <el-option label="高风险" value="high" />
          <el-option label="中风险" value="medium" />
          <el-option label="低风险" value="low" />
        </el-select>
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
      <el-table-column prop="regDate" label="挂号日期" min-width="110" align="center" />
      <el-table-column prop="gender" label="性别" width="70" align="center" />
      <el-table-column prop="age" label="年龄" width="70" align="center" />
      <el-table-column prop="visitType" label="就诊类型" min-width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.visitType === 'first'" type="primary">初诊</el-tag>
          <el-tag v-if="scope.row.visitType === 'follow'" type="success">复诊</el-tag>
          <el-tag v-if="scope.row.visitType === 'emergency'" type="danger">急诊</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="riskLevel" label="风险等级" min-width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.riskLevel === 'high'" type="danger">高风险</el-tag>
          <el-tag v-if="scope.row.riskLevel === 'medium'" type="warning">中风险</el-tag>
          <el-tag v-if="scope.row.riskLevel === 'low'" type="success">低风险</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="triageDept" label="分诊科室" min-width="120" align="center" />
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
            <el-form-item label="挂号日期" prop="regDate">
              <el-date-picker v-model="form.regDate" type="date" placeholder="请选择挂号日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="就诊类型" prop="visitType">
              <el-select v-model="form.visitType" placeholder="请选择就诊类型" style="width:100%">
                <el-option label="初诊" value="first" />
                <el-option label="复诊" value="follow" />
                <el-option label="急诊" value="emergency" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别" style="width:100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" :precision="1" :min="0" :max="999" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width:100%">
                <el-option label="高风险" value="high" />
                <el-option label="中风险" value="medium" />
                <el-option label="低风险" value="low" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分诊科室" prop="triageDept">
              <el-select v-model="form.triageDept" placeholder="请选择分诊科室" style="width:100%">
                <el-option label="心内科" value="cardiology" />
                <el-option label="内分泌科" value="endocrinology" />
                <el-option label="神经内科" value="neurology" />
                <el-option label="呼吸内科" value="respiratory" />
                <el-option label="全科门诊" value="general" />
                <el-option label="老年科" value="geriatrics" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width:100%">
                <el-option label="待处理" value="0" />
                <el-option label="已处理" value="1" />
                <el-option label="已归档" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="分诊说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入分诊说明" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="智能分诊详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="就诊卡号">{{ detailData.visitCardNo }}</el-descriptions-item>
        <el-descriptions-item label="挂号日期">{{ detailData.regDate }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailData.age }}</el-descriptions-item>
        <el-descriptions-item label="就诊类型">{{ detailData.visitType }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ detailData.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="分诊科室">{{ detailData.triageDept }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'OutpatientTriage',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      total: 0,
      queryParams: {
        pageNum: 1, pageSize: 10, name: '', visitCardNo: '', regDate: null, riskLevel: '', status: ''
      },
      form: {},
      detailData: {},
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        visitCardNo: [{ required: true, message: '请输入就诊卡号', trigger: 'blur' }],
        regDate: [{ required: true, message: '请选择挂号日期', trigger: 'change' }]
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
        {"id": 1, "name": "张三", "visitCardNo": "VC202606001", "regDate": "2026-06-20", "gender": "男", "age": 58, "visitType": "first", "riskLevel": "high", "triageDept": "心内科", "status": "0", "remark": ""},
        {"id": 2, "name": "李四", "visitCardNo": "VC202606002", "regDate": "2026-06-20", "gender": "女", "age": 45, "visitType": "follow", "riskLevel": "medium", "triageDept": "内分泌科", "status": "1", "remark": ""},
        {"id": 3, "name": "王五", "visitCardNo": "VC202606003", "regDate": "2026-06-21", "gender": "男", "age": 62, "visitType": "emergency", "riskLevel": "high", "triageDept": "神经内科", "status": "0", "remark": ""},
        {"id": 4, "name": "赵六", "visitCardNo": "VC202606004", "regDate": "2026-06-21", "gender": "女", "age": 55, "visitType": "first", "riskLevel": "low", "triageDept": "全科门诊", "status": "2", "remark": ""},
        {"id": 5, "name": "孙七", "visitCardNo": "VC202606005", "regDate": "2026-06-22", "gender": "男", "age": 50, "visitType": "follow", "riskLevel": "medium", "triageDept": "呼吸内科", "status": "1", "remark": ""}
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
      this.dialogTitle = '新增智能分诊'
      this.form = { name: '', visitCardNo: '', regDate: '', visitType: '', gender: '', age: null, riskLevel: '', triageDept: '', status: '', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑智能分诊'
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
