<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="队列名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入队列名称" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 说明 -->
    <el-alert class="mb8" type="info" :closable="false" show-icon
      title="以下为慢病路径管理下所有已发布的随访队列，可对队列下的评估数据进行 AI 分析" />

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="110" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" @click="handleViewData(scope.row)">评估数据</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="projectName" label="队列名称" min-width="200" align="center" show-overflow-tooltip />
      <el-table-column prop="diseaseName" label="病种" width="110" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.diseaseName" type="primary" effect="plain">{{ scope.row.diseaseName }}</el-tag>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column prop="patientNum" label="目标患者数" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.patientNum || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="随访任务数" align="center" width="200">
        <template slot-scope="scope">
          <el-tag size="small" type="warning">待处理 {{ scope.row.pendingTasksNum || 0 }}</el-tag>
          <el-tag size="small" type="primary">进行中 {{ scope.row.ongoingTasksNum || 0 }}</el-tag>
          <el-tag size="small" type="success">已完成 {{ scope.row.completedTasksNum || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataCount" label="评估数据" width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.dataCount && scope.row.dataCount > 0" type="success">{{ scope.row.dataCount }}</el-tag>
          <span v-else>{{ scope.row.dataCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="发布时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.publishTime) }}</span>
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

    <!-- 评估数据列表对话框 -->
    <el-dialog :title="'评估数据列表 - ' + currentProjectName" :visible.sync="dataListVisible" width="1100px" append-to-body>
      <el-row class="mb8">
        <el-button type="success" plain icon="el-icon-s-data" size="mini" :loading="generating"
          @click="handleGenerateData" v-hasPermi="['ai:evaluation:project:add']">生成评估数据</el-button>
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddData"
          v-hasPermi="['ai:evaluation:project:add']">录入数据</el-button>
        <el-button type="warning" plain icon="el-icon-refresh" size="mini" @click="getDataList">刷新</el-button>
      </el-row>
      <el-table v-loading="dataLoading" :data="evalDataList" border stripe size="small">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="patientName" label="患者姓名" width="100" align="center" />
        <el-table-column prop="evalDate" label="评估日期" width="110" align="center" />
        <el-table-column prop="evalResult" label="评估结果" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="resultTag(scope.row.evalResult)">{{ scope.row.evalResult }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="评分" width="70" align="center" />
        <el-table-column prop="evaluator" label="评估人" width="90" align="center" />
        <el-table-column prop="aiStatus" label="分析状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="aiStatusTag(scope.row.aiStatus)">{{ aiStatusName(scope.row.aiStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="140" align="center" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="230" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-magic-stick" :loading="analyzingId === scope.row.id"
              @click="handleAnalyze(scope.row)" v-hasPermi="['ai:evaluation:project:query']">AI分析</el-button>
            <el-button type="text" size="mini" icon="el-icon-view" :disabled="scope.row.aiStatus !== '2'"
              @click="handleViewResult(scope.row)">分析结果</el-button>
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEditData(scope.row)"
              v-hasPermi="['ai:evaluation:project:edit']">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" style="color:#F56C6C" @click="handleDeleteData(scope.row)"
              v-hasPermi="['ai:evaluation:project:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="dataTotal > 0"
        :total="dataTotal"
        :page.sync="dataQueryParams.pageNum"
        :limit.sync="dataQueryParams.pageSize"
        @pagination="getDataList"
      />
    </el-dialog>

    <!-- 录入/编辑评估数据对话框 -->
    <el-dialog :title="dataDialogTitle" :visible.sync="dataDialogVisible" width="650px" append-to-body>
      <el-form ref="dataForm" :model="dataForm" :rules="dataRules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input v-model="dataForm.patientName" placeholder="请输入患者姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估日期" prop="evalDate">
              <el-date-picker v-model="dataForm.evalDate" type="date" value-format="yyyy-MM-dd"
                placeholder="请选择评估日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评估结果" prop="evalResult">
              <el-select v-model="dataForm.evalResult" placeholder="请选择评估结果" style="width:100%">
                <el-option label="高风险" value="高风险" />
                <el-option label="中风险" value="中风险" />
                <el-option label="低风险" value="低风险" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估评分" prop="score">
              <el-input-number v-model="dataForm.score" :min="0" :max="100" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评估人" prop="evaluator">
              <el-input v-model="dataForm.evaluator" placeholder="请输入评估人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="评估明细" prop="evalDetail">
          <el-input v-model="dataForm.evalDetail" type="textarea" :rows="4"
            placeholder="请输入评估明细数据，如：血压165/95mmHg、空腹血糖7.8mmol/L、BMI 28.5、吸烟史20年、冠心病家族史等" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dataForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dataDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitDataForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- AI分析结果对话框 -->
    <el-dialog title="AI 分析结果" :visible.sync="resultVisible" width="760px" append-to-body>
      <div class="ai-result-header">
        <el-tag :type="resultTag(resultData.evalResult)">{{ resultData.evalResult }}</el-tag>
        <span class="ai-result-patient">{{ resultData.patientName }}</span>
        <span class="ai-result-meta">{{ resultData.aiModel }} · {{ parseTime(resultData.aiTime) }}</span>
      </div>
      <div class="ai-result-block">
        <div class="ai-result-title">
          <i class="el-icon-document" /> 分析结果
        </div>
        <div class="ai-result-content" v-html="renderText(resultData.aiAnalysis)" />
      </div>
      <div class="ai-result-block">
        <div class="ai-result-title">
          <i class="el-icon-guide" /> 下一步采取的措施
        </div>
        <div class="ai-result-content" v-html="renderText(resultData.aiMeasures)" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="resultVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  listQueue,
  listEvaluationData, getEvaluationData, addEvaluationData, updateEvaluationData,
  delEvaluationData, analyzeEvaluationData, generateEvaluationData
} from '@/api/aieval/project'

export default {
  name: 'AievalProject',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, projectName: '' },
      tableData: [],
      // 评估数据
      dataListVisible: false,
      dataLoading: false,
      dataTotal: 0,
      currentProject: null,
      currentProjectName: '',
      dataQueryParams: { pageNum: 1, pageSize: 10, projectId: null },
      evalDataList: [],
      analyzingId: null,
      generating: false,
      // 数据录入
      dataDialogVisible: false,
      dataDialogTitle: '',
      dataForm: {},
      dataRules: {
        patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
        evalDate: [{ required: true, message: '请选择评估日期', trigger: 'change' }],
        evalResult: [{ required: true, message: '请选择评估结果', trigger: 'change' }]
      },
      // AI分析结果
      resultVisible: false,
      resultData: {}
    }
  },
  created() { this.getList() },
  methods: {
    /** 查询慢病路径管理下已发布的队列列表 */
    getList() {
      this.loading = true
      listQueue(this.queryParams).then(response => {
        this.tableData = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    /** 查看队列下的评估数据列表 */
    handleViewData(row) {
      this.currentProject = row
      this.currentProjectName = row.projectName
      this.dataQueryParams = { pageNum: 1, pageSize: 10, projectId: row.id }
      this.dataListVisible = true
      this.getDataList()
    },
    getDataList() {
      this.dataLoading = true
      listEvaluationData(this.dataQueryParams).then(response => {
        this.evalDataList = response.rows
        this.dataTotal = response.total
        this.dataLoading = false
      }).catch(() => { this.dataLoading = false })
    },
    /** 生成评估数据：根据队列下患者已填写完成的历次表单数据自动生成 */
    handleGenerateData() {
      this.$confirm('将根据该队列下患者已填写完成的历次随访表单数据，自动生成/更新评估数据，是否继续？', '提示', { type: 'warning' }).then(() => {
        this.generating = true
        generateEvaluationData({ projectId: this.currentProject.id }).then(response => {
          this.generating = false
          this.$message.success('已生成 ' + (response.data || 0) + ' 条评估数据')
          this.getDataList()
          this.getList()
        }).catch(() => { this.generating = false })
      }).catch(() => { })
    },
    /** 录入评估数据 */
    handleAddData() {
      this.dataDialogTitle = '录入评估数据'
      this.dataForm = { projectId: this.currentProject.id, evalDate: null }
      this.dataDialogVisible = true
      this.$nextTick(() => { if (this.$refs.dataForm) this.$refs.dataForm.resetFields() })
    },
    handleEditData(row) {
      this.dataDialogTitle = '编辑评估数据'
      getEvaluationData(row.id).then(response => {
        this.dataForm = response.data || {}
        this.dataDialogVisible = true
      })
    },
    submitDataForm() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          if (this.dataForm.id != null) {
            updateEvaluationData(this.dataForm).then(() => {
              this.$message.success('修改成功')
              this.dataDialogVisible = false
              this.getDataList()
              this.getList()
            })
          } else {
            addEvaluationData(this.dataForm).then(() => {
              this.$message.success('新增成功')
              this.dataDialogVisible = false
              this.getDataList()
              this.getList()
            })
          }
        }
      })
    },
    handleDeleteData(row) {
      this.$confirm('确认删除患者"' + row.patientName + '"的评估数据?', '警告', { type: 'warning' }).then(() => {
        return delEvaluationData(row.id)
      }).then(() => {
        this.$message.success('删除成功')
        this.getDataList()
        this.getList()
      }).catch(() => { })
    },
    /** AI 分析 */
    handleAnalyze(row) {
      this.analyzingId = row.id
      analyzeEvaluationData(row.id).then(response => {
        this.$message.success('AI分析完成')
        this.analyzingId = null
        this.getDataList()
        // 直接展示分析结果
        this.resultData = response.data || {}
        this.resultVisible = true
      }).catch(() => {
        this.analyzingId = null
      })
    },
    handleViewResult(row) {
      getEvaluationData(row.id).then(response => {
        this.resultData = response.data || {}
        this.resultVisible = true
      })
    },
    resultTag(result) {
      var map = { '高风险': 'danger', '中风险': 'warning', '低风险': 'success' }
      return map[result] || 'info'
    },
    aiStatusName(status) {
      var map = { '0': '未分析', '1': '分析中', '2': '已完成', '3': '失败' }
      return map[status] || '未分析'
    },
    aiStatusTag(status) {
      var map = { '0': 'info', '1': 'warning', '2': 'success', '3': 'danger' }
      return map[status] || 'info'
    },
    /** 分析结果文本渲染：换行 + 条目 */
    renderText(text) {
      if (!text) return '<span style="color:#909399">暂无内容</span>'
      var esc = String(text).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
      return esc.replace(/\n/g, '<br/>')
    }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }

.ai-result-header { padding: 4px 0 12px; border-bottom: 1px dashed #e6e6e6; display: flex; align-items: center; gap: 12px; }
.ai-result-patient { font-size: 15px; font-weight: 600; color: #303133; }
.ai-result-meta { font-size: 12px; color: #909399; }

.ai-result-block { margin-top: 16px; }
.ai-result-title { font-size: 14px; font-weight: 600; color: #1f6fb2; margin-bottom: 8px; }
.ai-result-title i { margin-right: 6px; }
.ai-result-content {
  background: #f7fafc; border: 1px solid #e6eef5; border-radius: 4px;
  padding: 14px 16px; font-size: 13px; line-height: 1.8; color: #303133;
  max-height: 320px; overflow-y: auto; white-space: normal;
}
</style>
