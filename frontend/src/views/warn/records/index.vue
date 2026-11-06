<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="患者姓名" prop="patientName">
        <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="预警类型" prop="warnType">
        <el-select v-model="queryParams.warnType" placeholder="请选择预警类型" clearable>
          <el-option label="血压危急" value="bp_critical" />
          <el-option label="血糖异常" value="glucose_abnormal" />
          <el-option label="超窗" value="over_window" />
          <el-option label="质疑" value="query" />
          <el-option label="失访" value="lost_follow" />
          <el-option label="指标异常" value="indicator_abnormal" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警级别" prop="warnLevel">
        <el-select v-model="queryParams.warnLevel" placeholder="请选择预警级别" clearable>
          <el-option label="异常" value="abnormal" />
          <el-option label="危急" value="critical" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="handleStatus">
        <el-select v-model="queryParams.handleStatus" placeholder="请选择状态" clearable>
          <el-option label="待处理" value="pending" />
          <el-option label="已处理" value="done" />
          <el-option label="已忽略" value="ignored" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width:240px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="!selectedIds.length" @click="handleBatchIgnore">批量忽略</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <el-col :span="6">
        <span class="stat-text">
          待处理 <b style="color:#f56c6c;">{{ pendingCount }}</b> 条 &nbsp;
          危急 <b style="color:#e6a23c;">{{ criticalCount }}</b> 条
        </span>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="warnList" @selection-change="handleSelectionChange" :row-class-name="tableRowClassName">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="患者姓名" align="center" prop="patientName" min-width="80" show-overflow-tooltip />
      <el-table-column label="预警类型" align="center" prop="warnType" width="110">
        <template slot-scope="scope">
          <el-tag :type="getWarnTypeTag(scope.row.warnType)" size="small">{{ scope.row.warnTypeLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预警内容" align="center" prop="warnContent" min-width="200" show-overflow-tooltip />
      <el-table-column label="预警级别" align="center" prop="warnLevel" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.warnLevel === 'critical' ? 'danger' : 'warning'" size="small" effect="dark">
            {{ scope.row.warnLevel === 'critical' ? '危急' : '异常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预警时间" align="center" prop="warnTime" width="160" />
      <el-table-column label="处理状态" align="center" prop="handleStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.handleStatus === 'pending'" type="danger">待处理</el-tag>
          <el-tag v-else-if="scope.row.handleStatus === 'done'" type="success">已处理</el-tag>
          <el-tag v-else-if="scope.row.handleStatus === 'ignored'" type="info">已忽略</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="处理人" align="center" prop="handlerName" width="90" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button v-if="scope.row.handleStatus === 'pending'" size="mini" type="text" icon="el-icon-check" @click="handleDeal(scope.row)">处理</el-button>
          <el-button v-if="scope.row.handleStatus === 'pending'" size="mini" type="text" icon="el-icon-circle-close" @click="handleIgnore(scope.row)">忽略</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 处理预警对话框 -->
    <el-dialog :title="dealTitle" :visible.sync="dealOpen" width="600px" append-to-body>
      <el-form ref="dealForm" :model="dealForm" :rules="dealRules" label-width="100px">
        <el-form-item label="预警信息">
          <el-alert :title="dealForm.warnContent" :type="dealForm.warnLevel === 'critical' ? 'error' : 'warning'" :closable="false" show-icon />
        </el-form-item>
        <el-form-item label="处理方式" prop="dealType">
          <el-radio-group v-model="dealForm.dealType">
            <el-radio label="phone">电话随访</el-radio>
            <el-radio label="sms">短信通知</el-radio>
            <el-radio label="app">APP推送</el-radio>
            <el-radio label="manual">手工标记</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理说明" prop="dealRemark">
          <el-input v-model="dealForm.dealRemark" type="textarea" :rows="4" placeholder="请输入处理说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDeal">确认处理</el-button>
        <el-button @click="dealOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 预警详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="700px" append-to-body>
      <div v-if="detailData">
        <el-descriptions title="预警信息" :column="2" border size="small">
          <el-descriptions-item label="患者姓名">{{ detailData.patientName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">{{ detailData.warnTypeLabel }}</el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag :type="detailData.warnLevel === 'critical' ? 'danger' : 'warning'" size="small">
              {{ detailData.warnLevel === 'critical' ? '危急' : '异常' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警时间">{{ detailData.warnTime }}</el-descriptions-item>
          <el-descriptions-item label="预警内容" :span="2">{{ detailData.warnContent }}</el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag v-if="detailData.handleStatus === 'pending'" type="danger">待处理</el-tag>
            <el-tag v-else-if="detailData.handleStatus === 'done'" type="success">已处理</el-tag>
            <el-tag v-else type="info">已忽略</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理人">{{ detailData.handlerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处理时间" :span="2">{{ detailData.handleTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处理说明" :span="2">{{ detailData.handleRemark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">患者信息</el-divider>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="年龄">{{ detailData.age }}岁</el-descriptions-item>
          <el-descriptions-item label="性别">{{ detailData.gender === '1' ? '男' : '女' }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ detailData.diseaseName }}</el-descriptions-item>
          <el-descriptions-item label="管理医生">{{ detailData.doctorName }}</el-descriptions-item>
          <el-descriptions-item label="所属路径">{{ detailData.pathwayName }}</el-descriptions-item>
          <el-descriptions-item label="当前节点">{{ detailData.currentNode }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'WarnRecords',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      selectedIds: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientName: null,
        warnType: null,
        warnLevel: null,
        handleStatus: null
      },
      warnList: [],
      pendingCount: 0,
      criticalCount: 0,
      dealOpen: false,
      dealTitle: '',
      dealForm: { warnContent: '', warnLevel: '', dealType: 'phone', dealRemark: '' },
      dealRules: {
        dealType: [{ required: true, message: '请选择处理方式', trigger: 'change' }],
        dealRemark: [{ required: true, message: '处理说明不能为空', trigger: 'blur' }]
      },
      detailOpen: false,
      detailTitle: '',
      detailData: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      setTimeout(() => {
        const mockList = [
          { id: '1', patientName: '张三', age: 58, gender: '1', diseaseName: '高血压', warnType: 'bp_critical', warnTypeLabel: '血压危急', warnContent: '收缩压210mmHg，舒张压120mmHg，超过危急阈值，请立即处理', warnLevel: 'critical', warnTime: '2026-06-30 08:15', handleStatus: 'pending', handlerName: '', handleTime: '', handleRemark: '', doctorName: '王医生', pathwayName: '高血压慢病管理路径', currentNode: '用药指导' },
          { id: '2', patientName: '李丽', age: 45, gender: '2', diseaseName: '糖尿病', warnType: 'glucose_abnormal', warnTypeLabel: '血糖异常', warnContent: '空腹血糖连续3次超过8.0mmol/L，建议调整用药方案', warnLevel: 'abnormal', warnTime: '2026-06-29 14:30', handleStatus: 'done', handlerName: '李医生', handleTime: '2026-06-29 16:00', handleRemark: '已电话通知患者调整胰岛素用量', doctorName: '李医生', pathwayName: '糖尿病慢病管理路径', currentNode: '首次随访' },
          { id: '3', patientName: '王建国', age: 62, gender: '1', diseaseName: '脑卒中', warnType: 'over_window', warnTypeLabel: '超窗', warnContent: '季度评估节点已超过预期完成时间7天，患者未提交评估表单', warnLevel: 'abnormal', warnTime: '2026-06-28 10:00', handleStatus: 'pending', handlerName: '', handleTime: '', handleRemark: '', doctorName: '张医生', pathwayName: '脑卒中康复管理路径', currentNode: '季度评估' },
          { id: '4', patientName: '赵敏', age: 38, gender: '2', diseaseName: '冠心病', warnType: 'query', warnTypeLabel: '质疑', warnContent: '患者自报"已戒烟"，但上次记录仍为"吸烟中"，数据存在矛盾', warnLevel: 'abnormal', warnTime: '2026-06-27 09:45', handleStatus: 'ignored', handlerName: '刘医生', handleTime: '2026-06-27 11:00', handleRemark: '电话确认后患者已戒烟，上期数据录入存在延迟', doctorName: '刘医生', pathwayName: '冠心病术后管理路径', currentNode: '健康宣教' },
          { id: '5', patientName: '刘志强', age: 55, gender: '1', diseaseName: '高血压', warnType: 'lost_follow', warnTypeLabel: '失访', warnContent: '患者连续3次未接电话，最近一次随访记录超过30天', warnLevel: 'critical', warnTime: '2026-06-26 16:20', handleStatus: 'pending', handlerName: '', handleTime: '', handleRemark: '', doctorName: '王医生', pathwayName: '高血压慢病管理路径', currentNode: '首次随访' },
          { id: '6', patientName: '陈伟', age: 67, gender: '1', diseaseName: '慢阻肺', warnType: 'indicator_abnormal', warnTypeLabel: '指标异常', warnContent: '血氧饱和度降至89%，低于安全阈值(94%)，需紧急关注', warnLevel: 'critical', warnTime: '2026-06-30 07:50', handleStatus: 'pending', handlerName: '', handleTime: '', handleRemark: '', doctorName: '刘医生', pathwayName: '冠心病术后管理路径', currentNode: '入组评估' },
          { id: '7', patientName: '周华', age: 52, gender: '1', diseaseName: '糖尿病', warnType: 'glucose_abnormal', warnTypeLabel: '血糖异常', warnContent: '餐后2小时血糖15.2mmol/L，显著高于控制目标', warnLevel: 'abnormal', warnTime: '2026-06-25 11:30', handleStatus: 'done', handlerName: '李医生', handleTime: '2026-06-25 14:20', handleRemark: '发送短信提醒，建议患者来院复查', doctorName: '李医生', pathwayName: '糖尿病慢病管理路径', currentNode: '健康宣教' }
        ]
        let filtered = mockList
        if (this.queryParams.patientName) {
          filtered = filtered.filter(item => item.patientName.indexOf(this.queryParams.patientName) > -1)
        }
        if (this.queryParams.warnType) {
          filtered = filtered.filter(item => item.warnType === this.queryParams.warnType)
        }
        if (this.queryParams.warnLevel) {
          filtered = filtered.filter(item => item.warnLevel === this.queryParams.warnLevel)
        }
        if (this.queryParams.handleStatus) {
          filtered = filtered.filter(item => item.handleStatus === this.queryParams.handleStatus)
        }
        this.warnList = filtered
        this.total = filtered.length
        this.pendingCount = mockList.filter(item => item.handleStatus === 'pending').length
        this.criticalCount = mockList.filter(item => item.warnLevel === 'critical').length
        this.loading = false
      }, 300)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.dateRange = []
      this.queryParams.pageNum = 1
      this.getList()
    },
    getWarnTypeTag(type) {
      const map = { bp_critical: 'danger', glucose_abnormal: 'warning', over_window: 'info', query: '', lost_follow: 'danger', indicator_abnormal: 'warning' }
      return map[type] || ''
    },
    tableRowClassName({ row }) {
      if (row.warnLevel === 'critical' && row.handleStatus === 'pending') {
        return 'warning-row'
      }
      return ''
    },
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.id)
    },
    handleDeal(row) {
      this.dealForm = {
        warnId: row.id,
        warnContent: row.warnContent,
        warnLevel: row.warnLevel,
        dealType: 'phone',
        dealRemark: ''
      }
      this.dealTitle = '处理预警 - ' + row.patientName
      this.dealOpen = true
    },
    submitDeal() {
      this.$refs['dealForm'].validate(valid => {
        if (valid) {
          const target = this.warnList.find(item => item.id === this.dealForm.warnId)
          if (target) {
            target.handleStatus = 'done'
            target.handlerName = '当前用户'
            target.handleTime = new Date().toISOString().replace('T', ' ').substring(0, 19)
            target.handleRemark = this.dealForm.dealRemark
          }
          this.$modal.msgSuccess('预警已处理')
          this.dealOpen = false
          this.getList()
        }
      })
    },
    handleIgnore(row) {
      this.$modal.confirm('确认忽略该预警？').then(() => {
        row.handleStatus = 'ignored'
        row.handlerName = '当前用户'
        row.handleTime = new Date().toISOString().replace('T', ' ').substring(0, 19)
        row.handleRemark = '已忽略'
        this.$modal.msgSuccess('已忽略该预警')
        this.getList()
      }).catch(() => {})
    },
    handleBatchIgnore() {
      this.$modal.confirm('确认批量忽略选中的 ' + this.selectedIds.length + ' 条预警？').then(() => {
        this.warnList.forEach(item => {
          if (this.selectedIds.includes(item.id) && item.handleStatus === 'pending') {
            item.handleStatus = 'ignored'
            item.handlerName = '当前用户'
            item.handleTime = new Date().toISOString().replace('T', ' ').substring(0, 19)
          }
        })
        this.selectedIds = []
        this.$modal.msgSuccess('批量忽略成功')
        this.getList()
      }).catch(() => {})
    },
    handleDetail(row) {
      this.detailData = row
      this.detailTitle = '预警详情 - ' + row.patientName
      this.detailOpen = true
    },
    handleExport() {
      this.$modal.loading('正在导出预警记录...')
      setTimeout(() => {
        this.$modal.closeLoading()
        this.$modal.msgSuccess('导出成功')
      }, 500)
    }
  }
}
</script>

<style scoped>
.stat-text { font-size: 13px; color: #606266; line-height: 28px; }
::v-deep .warning-row { background-color: #fef0f0 !important; }
</style>
