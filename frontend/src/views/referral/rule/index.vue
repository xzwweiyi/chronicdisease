<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input v-model="queryParams.ruleName" placeholder="请输入规则名称" clearable />
      </el-form-item>
      <el-form-item label="适用病种" prop="disease">
        <el-select v-model="queryParams.disease" placeholder="请选择病种" clearable>
          <el-option label="高血压" value="高血压" />
          <el-option label="糖尿病" value="糖尿病" />
          <el-option label="冠心病" value="冠心病" />
          <el-option label="慢阻肺" value="慢阻肺" />
          <el-option label="脑卒中" value="脑卒中" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增规则</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">查看</el-button>
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" style="color:#F56C6C" @click="handleToggle(scope.row)">
            {{ scope.row.status === '1' ? '禁用' : '启用' }}
          </el-button>
          <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="ruleName" label="规则名称" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="disease" label="适用病种" width="120" align="center" show-overflow-tooltip />
      <el-table-column prop="upCondition" label="上转条件" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="downCondition" label="下转条件" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="urgentCondition" label="紧急转诊条件" min-width="180" align="center" show-overflow-tooltip />
      <el-table-column prop="priority" label="优先级" width="80" align="center" />
      <el-table-column prop="effectiveDate" label="生效日期" width="120" align="center" />
      <el-table-column prop="statusName" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">{{ scope.row.statusName }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用病种" prop="disease">
              <el-select v-model="form.disease" placeholder="请选择病种" style="width:100%">
                <el-option label="高血压" value="高血压" />
                <el-option label="糖尿病" value="糖尿病" />
                <el-option label="冠心病" value="冠心病" />
                <el-option label="慢阻肺" value="慢阻肺" />
                <el-option label="脑卒中" value="脑卒中" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number v-model="form.priority" :min="1" :max="10" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="form.effectiveDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="上转条件" prop="upCondition">
          <el-input v-model="form.upCondition" type="textarea" :rows="3" placeholder="请输入上转条件" />
        </el-form-item>
        <el-form-item label="下转条件" prop="downCondition">
          <el-input v-model="form.downCondition" type="textarea" :rows="3" placeholder="请输入下转条件" />
        </el-form-item>
        <el-form-item label="紧急转诊条件" prop="urgentCondition">
          <el-input v-model="form.urgentCondition" type="textarea" :rows="3" placeholder="请输入紧急转诊条件" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
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
import Pagination from '@/components/Pagination'

export default {
  name: 'ReferralRule',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, ruleName: '', disease: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      tableData: [],
      form: { ruleName: '', disease: '', priority: 1, effectiveDate: '', upCondition: '', downCondition: '', urgentCondition: '', remark: '' },
      rules: {
        ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        disease: [{ required: true, message: '请选择病种', trigger: 'change' }],
        priority: [{ required: true, message: '请设置优先级', trigger: 'blur' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }],
        upCondition: [{ required: true, message: '请输入上转条件', trigger: 'blur' }],
        downCondition: [{ required: true, message: '请输入下转条件', trigger: 'blur' }],
        urgentCondition: [{ required: true, message: '请输入紧急转诊条件', trigger: 'blur' }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      var that = this
      setTimeout(function() {
        that.loading = false
        that.total = that.mockData().length
        var start = (that.queryParams.pageNum - 1) * that.queryParams.pageSize
        that.tableData = that.mockData().slice(start, start + that.queryParams.pageSize)
      }, 300)
    },
    mockData() {
      var rules = [
        { id: 1, ruleName: '高血压分级转诊规则', disease: '高血压', upCondition: '血压持续>180/110mmHg且联合用药控制不佳', downCondition: '血压稳定<140/90mmHg达3个月', urgentCondition: '高血压危象（血压>220/130mmHg伴靶器官损害）', priority: 1, effectiveDate: '2026-01-01', status: '1', statusName: '启用' },
        { id: 2, ruleName: '糖尿病分级转诊规则', disease: '糖尿病', upCondition: 'HbA1c>9%且出现严重并发症', downCondition: 'HbA1c<7%且并发症稳定', urgentCondition: '糖尿病酮症酸中毒、高渗昏迷', priority: 2, effectiveDate: '2026-01-15', status: '1', statusName: '启用' },
        { id: 3, ruleName: '冠心病分级转诊规则', disease: '冠心病', upCondition: '不稳定心绞痛或心肌梗死后康复期', downCondition: 'PCI术后6个月病情稳定', urgentCondition: '急性心肌梗死、心源性休克', priority: 1, effectiveDate: '2026-02-01', status: '1', statusName: '启用' },
        { id: 4, ruleName: '慢阻肺分级转诊规则', disease: '慢阻肺', upCondition: 'FEV1<50%预计值且反复急性加重', downCondition: '急性加重后稳定>3个月', urgentCondition: '呼吸衰竭需机械通气', priority: 3, effectiveDate: '2026-02-15', status: '1', statusName: '启用' },
        { id: 5, ruleName: '脑卒中分级转诊规则', disease: '脑卒中', upCondition: '新发脑卒中急性期后需康复评估', downCondition: '康复期功能恢复良好', urgentCondition: '急性脑卒中发病4.5h内', priority: 1, effectiveDate: '2026-03-01', status: '1', statusName: '启用' },
        { id: 6, ruleName: '高血压合并糖尿病规则', disease: '高血压', upCondition: '血压>160/100且HbA1c>8.5%', downCondition: '双指标均达标持续6个月', urgentCondition: '急性心脑血管事件', priority: 2, effectiveDate: '2026-04-01', status: '0', statusName: '禁用' }
      ]
      return rules
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '新增转诊规则'
      this.isEdit = false
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { ruleName: '', disease: '', priority: 1, effectiveDate: '', upCondition: '', downCondition: '', urgentCondition: '', remark: '' }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑转诊规则'
      this.isEdit = true
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() { that.form = Object.assign({}, row) })
    },
    submitForm() {
      var that = this
      this.$refs.form.validate(function(valid) {
        if (valid) {
          that.$message.success(that.isEdit ? '修改成功' : '新增成功')
          that.dialogVisible = false
          that.getList()
        }
      })
    },
    handleDetail(row) {
      var items = [
        { label: '规则名称', value: row.ruleName },
        { label: '适用病种', value: row.disease },
        { label: '上转条件', value: row.upCondition },
        { label: '下转条件', value: row.downCondition },
        { label: '紧急转诊条件', value: row.urgentCondition },
        { label: '优先级', value: row.priority },
        { label: '生效日期', value: row.effectiveDate },
        { label: '状态', value: row.statusName }
      ]
      var html = items.map(function(item) { return '<p><strong>' + item.label + ':</strong> ' + item.value + '</p>' }).join('')
      this.$alert(html, '规则详情', { dangerouslyUseHTMLString: true })
    },
    handleToggle(row) {
      var newStatus = row.status === '1' ? '0' : '1'
      var newName = newStatus === '1' ? '启用' : '禁用'
      var that = this
      this.$confirm('确认' + newName + '规则"' + row.ruleName + '"?', '提示', { type: 'warning' }).then(function() {
        row.status = newStatus
        row.statusName = newName
        that.$message.success(newName + '成功')
      })
    },
    handleDelete(row) {
      var that = this
      this.$confirm('确认删除规则"' + row.ruleName + '"?', '警告', { type: 'warning' }).then(function() {
        that.$message.success('删除成功')
        that.getList()
      })
    },
    handleExport() { this.$message.success('导出成功') }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
</style>
