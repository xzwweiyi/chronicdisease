<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="评估项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
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
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="220" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" @click="handleViewRules(scope.row)">查看规则</el-button>
          <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="projectName" label="评估项目名称" min-width="200" align="center" show-overflow-tooltip />
      <el-table-column prop="ruleCount" label="规则个数" width="100" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
      <el-table-column prop="statusName" label="状态" width="100" align="center">
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" size="small">
        <el-form-item label="评估项目名称" prop="projectName">
          <el-select v-model="form.projectName" placeholder="请选择评估项目" style="width:100%">
            <el-option label="高血压心血管风险AI评估" value="高血压心血管风险AI评估" />
            <el-option label="糖尿病并发症预测模型" value="糖尿病并发症预测模型" />
            <el-option label="冠心病预后评估系统" value="冠心病预后评估系统" />
          </el-select>
        </el-form-item>
        <el-divider content-position="left">规则配置</el-divider>
        <div v-for="(rule, index) in form.rules" :key="index" class="rule-item">
          <el-row :gutter="10">
            <el-col :span="8">
              <el-form-item label="指标名称" :prop="'rules.' + index + '.indicator'" :rules="[{required: true, message:'请输入'}]">
                <el-input v-model="rule.indicator" placeholder="如: 收缩压" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="运算符" :prop="'rules.' + index + '.operator'" :rules="[{required: true}]">
                <el-select v-model="rule.operator" placeholder="选择" style="width:100%">
                  <el-option label=">" value=">" />
                  <el-option label=">=" value=">=" />
                  <el-option label="<" value="<" />
                  <el-option label="<=" value="<=" />
                  <el-option label="=" value="=" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="阈值">
                <el-input v-model="rule.threshold" placeholder="阈值" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button type="danger" icon="el-icon-delete" size="small" @click="removeRule(index)">删除</el-button>
            </el-col>
          </el-row>
          <el-form-item label="风险等级">
            <el-select v-model="rule.riskLevel" placeholder="选择风险等级" style="width:200px">
              <el-option label="高危" value="高危" />
              <el-option label="中危" value="中危" />
              <el-option label="低危" value="低危" />
            </el-select>
          </el-form-item>
        </div>
        <el-button type="primary" plain size="small" icon="el-icon-plus" @click="addRule">添加规则条件</el-button>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看规则详情对话框 -->
    <el-dialog title="规则详情" :visible.sync="ruleDetailVisible" width="800px" append-to-body>
      <div v-if="currentRuleSet">
        <h4>{{ currentRuleSet.projectName }} - 评估规则</h4>
        <el-table :data="currentRuleSet.ruleDetails" border stripe size="small">
          <el-table-column type="index" label="序号" width="50" align="center" />
          <el-table-column prop="indicator" label="指标名称" width="160" align="center" />
          <el-table-column prop="operator" label="运算符" width="80" align="center" />
          <el-table-column prop="threshold" label="阈值" width="100" align="center" />
          <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="riskTag(scope.row.riskLevel)" size="small">{{ scope.row.riskLevel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="weight" label="权重" width="80" align="center" />
          <el-table-column prop="description" label="说明" min-width="180" align="center" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'AievalRule',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      dateRange: [],
      queryParams: { pageNum: 1, pageSize: 10, projectName: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      ruleDetailVisible: false,
      currentRuleSet: null,
      tableData: [],
      form: { projectName: '', rules: [] },
      rules: {
        projectName: [{ required: true, message: '请选择评估项目', trigger: 'change' }]
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
      return [
        { id: 1, projectName: '高血压心血管风险AI评估', ruleCount: 8, createTime: '2026-01-15 10:30:00', status: '1', statusName: '启用',
          ruleDetails: [
            { indicator: '收缩压', operator: '>', threshold: '140', riskLevel: '高危', weight: 30, description: '收缩压超过140mmHg' },
            { indicator: '舒张压', operator: '>', threshold: '90', riskLevel: '高危', weight: 20, description: '舒张压超过90mmHg' },
            { indicator: '总胆固醇', operator: '>', threshold: '6.2', riskLevel: '中危', weight: 15, description: '总胆固醇>6.2mmol/L' },
            { indicator: '吸烟史', operator: '=', threshold: '是', riskLevel: '中危', weight: 15, description: '当前吸烟' },
            { indicator: '年龄', operator: '>', threshold: '55', riskLevel: '中危', weight: 10, description: '年龄超过55岁' },
            { indicator: 'BMI', operator: '>', threshold: '28', riskLevel: '低危', weight: 5, description: 'BMI超过28' },
            { indicator: '家族史', operator: '=', threshold: '有', riskLevel: '低危', weight: 3, description: '有心血管病家族史' },
            { indicator: '运动', operator: '=', threshold: '缺乏', riskLevel: '低危', weight: 2, description: '缺乏运动' }
          ]
        },
        { id: 2, projectName: '糖尿病并发症预测模型', ruleCount: 6, createTime: '2026-02-20 14:20:00', status: '1', statusName: '启用',
          ruleDetails: [
            { indicator: 'HbA1c', operator: '>', threshold: '9.0', riskLevel: '高危', weight: 35, description: '糖化血红蛋白>9%' },
            { indicator: '空腹血糖', operator: '>', threshold: '11.1', riskLevel: '高危', weight: 25, description: '空腹血糖>11.1mmol/L' },
            { indicator: '尿微量白蛋白', operator: '>', threshold: '30', riskLevel: '中危', weight: 20, description: '尿微量白蛋白>30mg/24h' },
            { indicator: '病程', operator: '>', threshold: '10年', riskLevel: '中危', weight: 10, description: '糖尿病病程>10年' },
            { indicator: '视网膜病变', operator: '=', threshold: '有', riskLevel: '高危', weight: 5, description: '已有视网膜病变' },
            { indicator: '足部检查', operator: '=', threshold: '异常', riskLevel: '中危', weight: 5, description: '足部检查异常' }
          ]
        },
        { id: 3, projectName: '冠心病预后评估系统', ruleCount: 5, createTime: '2026-03-10 09:00:00', status: '0', statusName: '禁用',
          ruleDetails: [
            { indicator: 'LVEF', operator: '<', threshold: '40%', riskLevel: '高危', weight: 30, description: '左室射血分数<40%' },
            { indicator: 'cTnI', operator: '>', threshold: '0.5', riskLevel: '高危', weight: 25, description: '肌钙蛋白I>0.5ng/mL' },
            { indicator: '多支病变', operator: '=', threshold: '是', riskLevel: '高危', weight: 20, description: '冠状动脉多支病变' },
            { indicator: '糖尿病合并', operator: '=', threshold: '是', riskLevel: '中危', weight: 15, description: '合并糖尿病' },
            { indicator: '年龄', operator: '>', threshold: '70', riskLevel: '中危', weight: 10, description: '年龄>70岁' }
          ]
        }
      ]
    },
    riskTag(level) {
      var map = { '高危': 'danger', '中危': 'warning', '低危': 'success' }
      return map[level] || 'info'
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.dateRange = []; this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '新增规则设置'
      this.isEdit = false
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { projectName: '', rules: [{ indicator: '', operator: '', threshold: '', riskLevel: '中危' }] }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑规则设置'
      this.isEdit = true
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        that.form = {
          projectName: row.projectName,
          rules: row.ruleDetails.map(function(r) { return Object.assign({}, r) })
        }
      })
    },
    addRule() { this.form.rules.push({ indicator: '', operator: '', threshold: '', riskLevel: '中危' }) },
    removeRule(index) { if (this.form.rules.length > 1) this.form.rules.splice(index, 1) },
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
    handleViewRules(row) {
      this.currentRuleSet = row
      this.ruleDetailVisible = true
    },
    handleDelete(row) {
      var that = this
      this.$confirm('确认删除"' + row.projectName + '"的规则设置?', '警告', { type: 'warning' }).then(function() {
        that.$message.success('删除成功')
        that.getList()
      })
    }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
.rule-item { background: #fafafa; padding: 12px; margin-bottom: 10px; border: 1px solid #eee; border-radius: 4px; }
</style>
