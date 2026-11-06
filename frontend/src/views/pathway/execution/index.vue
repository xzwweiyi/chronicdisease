<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="管理路径" prop="pathwayId">
        <el-select v-model="queryParams.pathwayId" placeholder="请选择路径" clearable @change="handleQuery" style="width:220px;">
          <el-option v-for="item in pathwayOptions" :key="item.id" :label="item.projectName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="患者姓名" prop="patientName">
        <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="执行状态" prop="execStatus">
        <el-select v-model="queryParams.execStatus" placeholder="请选择状态" clearable>
          <el-option label="执行中" value="active" />
          <el-option label="已完成" value="completed" />
          <el-option label="已退出" value="quit" />
          <el-option label="已超时" value="overdue" />
        </el-select>
      </el-form-item>
      <el-form-item label="管理医生" prop="doctorName">
        <el-input v-model="queryParams.doctorName" placeholder="请输入医生姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <div v-if="!queryParams.pathwayId" class="empty-hint">
      <el-empty description="请先选择一个管理路径" :image-size="120" />
    </div>

    <el-table v-else v-loading="loading" :data="execList" @row-click="handleRowClick" highlight-current-row>
      <el-table-column label="姓名" align="center" prop="patientName" show-overflow-tooltip min-width="80" />
      <el-table-column label="年龄" align="center" prop="age" width="60" />
      <el-table-column label="性别" align="center" prop="gender" width="60">
        <template slot-scope="scope">
          <span>{{ scope.row.gender === '1' ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="病种" align="center" prop="diseaseName" min-width="100" />
      <el-table-column label="诊断时间" align="center" prop="diagnoseTime" width="110" />
      <el-table-column label="管理机构" align="center" prop="orgName" min-width="140" show-overflow-tooltip />
      <el-table-column label="管理医生" align="center" prop="doctorName" width="100" />
      <el-table-column label="路径进度" align="center" prop="progress" width="180">
        <template slot-scope="scope">
          <div class="progress-cell">
            <el-progress :percentage="scope.row.progress" :stroke-width="8" :color="getProgressColor(scope.row.progress)" />
            <span class="progress-text">{{ scope.row.completedNodes }}/{{ scope.row.totalNodes }} 节点</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="当前节点" align="center" prop="currentNode" min-width="100" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="execStatus" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.execStatus === 'active'" type="primary">执行中</el-tag>
          <el-tag v-else-if="scope.row.execStatus === 'completed'" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.execStatus === 'quit'" type="danger">已退出</el-tag>
          <el-tag v-else-if="scope.row.execStatus === 'overdue'" type="warning">已超时</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click.stop="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 执行详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailOpen" width="900px" append-to-body>
      <div v-if="detailData" class="exec-detail">
        <!-- 患者基本信息 -->
        <el-descriptions title="患者基本信息" :column="3" border size="small" style="margin-bottom:20px;">
          <el-descriptions-item label="姓名">{{ detailData.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ detailData.gender === '1' ? '男' : '女' }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ detailData.age }}岁</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ detailData.idCard }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ detailData.diseaseName }}</el-descriptions-item>
          <el-descriptions-item label="管理机构">{{ detailData.orgName }}</el-descriptions-item>
          <el-descriptions-item label="管理医生">{{ detailData.doctorName }}</el-descriptions-item>
          <el-descriptions-item label="入组时间">{{ detailData.enrollTime }}</el-descriptions-item>
        </el-descriptions>

        <!-- 节点执行记录 -->
        <h4 style="margin: 20px 0 10px;">节点执行记录</h4>
        <el-timeline>
          <el-timeline-item v-for="(record, idx) in detailData.nodeRecords" :key="idx"
            :timestamp="record.execTime"
            :type="record.status === 'completed' ? 'success' : record.status === 'overdue' ? 'danger' : 'primary'"
            :color="record.status === 'completed' ? '#67c23a' : record.status === 'overdue' ? '#f56c6c' : '#409eff'"
            placement="top">
            <el-card shadow="hover" style="margin-top:0;">
              <div class="record-header">
                <span class="record-node-name">{{ record.nodeName }}</span>
                <el-tag :type="getRecordNodeTypeTag(record.nodeType)" size="small">{{ getRecordNodeTypeLabel(record.nodeType) }}</el-tag>
                <el-tag v-if="record.status === 'completed'" type="success" size="small">已完成</el-tag>
                <el-tag v-else-if="record.status === 'pending'" type="info" size="small">待执行</el-tag>
                <el-tag v-else-if="record.status === 'overdue'" type="danger" size="small">已超时</el-tag>
                <el-tag v-else type="warning" size="small">进行中</el-tag>
              </div>
              <div class="record-info" v-if="record.execTime">
                <span>执行时间：{{ record.execTime }}</span>
                <span v-if="record.completedTime" style="margin-left:16px;">完成时间：{{ record.completedTime }}</span>
              </div>
              <div class="record-info" v-if="record.remark">
                <span>备注：{{ record.remark }}</span>
              </div>
              <div class="record-info" v-if="record.crfs && record.crfs.length > 0">
                <span>已提交表单：</span>
                <el-tag v-for="crf in record.crfs" :key="crf.id" size="mini" type="success" style="margin-left:4px;">{{ crf.name }}</el-tag>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PathwayExecution',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pathwayId: null,
        patientName: null,
        execStatus: null,
        doctorName: null
      },
      pathwayOptions: [],
      execList: [],
      detailOpen: false,
      detailTitle: '',
      detailData: null
    }
  },
  created() {
    this.getPathwayOptions()
  },
  methods: {
    getPathwayOptions() {
      this.pathwayOptions = [
        { id: '1', projectName: '高血压慢病管理路径' },
        { id: '2', projectName: '糖尿病慢病管理路径' },
        { id: '3', projectName: '脑卒中康复管理路径' },
        { id: '4', projectName: '冠心病术后管理路径' }
      ]
    },
    getList() {
      if (!this.queryParams.pathwayId) {
        this.execList = []
        this.total = 0
        return
      }
      this.loading = true
      setTimeout(() => {
        const mockList = [
          { id: '1', patientName: '张三', age: 58, gender: '1', idCard: '610102196804152536', phone: '13800138001', diseaseName: '高血压', diagnoseTime: '2025-06-15', orgName: '城关区社区卫生服务中心', doctorName: '王医生', progress: 80, completedNodes: 4, totalNodes: 5, currentNode: '健康宣教', execStatus: 'active', enrollTime: '2026-01-15' },
          { id: '2', patientName: '李丽', age: 45, gender: '2', idCard: '610102198106123245', phone: '13900139002', diseaseName: '糖尿病', diagnoseTime: '2024-10-20', orgName: '七里河区人民医院', doctorName: '李医生', progress: 33, completedNodes: 2, totalNodes: 6, currentNode: '用药指导', execStatus: 'active', enrollTime: '2026-02-20' },
          { id: '3', patientName: '王建国', age: 62, gender: '1', idCard: '610102196407082618', phone: '13700137003', diseaseName: '脑卒中', diagnoseTime: '2025-12-01', orgName: '西固区中心卫生院', doctorName: '张医生', progress: 100, completedNodes: 5, totalNodes: 5, currentNode: '季度评估(终)', execStatus: 'completed', enrollTime: '2026-03-10' },
          { id: '4', patientName: '赵敏', age: 38, gender: '2', idCard: '610102198809051527', phone: '13600136004', diseaseName: '冠心病', diagnoseTime: '2025-08-10', orgName: '安宁区社区卫生服务中心', doctorName: '刘医生', progress: 50, completedNodes: 3, totalNodes: 6, currentNode: '复诊提醒', execStatus: 'active', enrollTime: '2026-04-05' },
          { id: '5', patientName: '刘志强', age: 55, gender: '1', idCard: '610102197111223019', phone: '13500135005', diseaseName: '高血压', diagnoseTime: '2025-05-20', orgName: '城关区社区卫生服务中心', doctorName: '王医生', progress: 17, completedNodes: 1, totalNodes: 6, currentNode: '首次随访', execStatus: 'overdue', enrollTime: '2026-05-18' },
          { id: '6', patientName: '孙芳', age: 50, gender: '2', idCard: '610102197603144168', phone: '13400134006', diseaseName: '糖尿病', diagnoseTime: '2025-03-15', orgName: '七里河区人民医院', doctorName: '李医生', progress: 0, completedNodes: 0, totalNodes: 6, currentNode: '-', execStatus: 'quit', enrollTime: '2026-06-01' },
          { id: '7', patientName: '陈伟', age: 67, gender: '1', idCard: '610102195903148712', phone: '13300133007', diseaseName: '慢阻肺', diagnoseTime: '2024-01-05', orgName: '安宁区社区卫生服务中心', doctorName: '刘医生', progress: 60, completedNodes: 3, totalNodes: 5, currentNode: '季度评估', execStatus: 'active', enrollTime: '2026-06-20' }
        ]
        let filtered = mockList
        if (this.queryParams.patientName) {
          filtered = filtered.filter(item => item.patientName.indexOf(this.queryParams.patientName) > -1)
        }
        if (this.queryParams.execStatus) {
          filtered = filtered.filter(item => item.execStatus === this.queryParams.execStatus)
        }
        if (this.queryParams.doctorName) {
          filtered = filtered.filter(item => item.doctorName.indexOf(this.queryParams.doctorName) > -1)
        }
        this.execList = filtered
        this.total = filtered.length
        this.loading = false
      }, 300)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.pageNum = 1
      this.getList()
    },
    getProgressColor(percentage) {
      if (percentage >= 100) return '#67c23a'
      if (percentage >= 50) return '#409eff'
      return '#e6a23c'
    },
    handleRowClick(row) {
      this.handleDetail(row)
    },
    handleDetail(row) {
      this.detailTitle = row.patientName + ' - 执行详情'
      this.detailData = {
        ...row,
        nodeRecords: this.generateMockNodeRecords(row.execStatus, row.completedNodes, row.totalNodes)
      }
      this.detailOpen = true
    },
    generateMockNodeRecords(status, completed, total) {
      const nodeNames = ['入组评估', '首次随访', '用药指导', '健康宣教', '复诊提醒', '季度评估']
      const nodeTypes = ['assess', 'follow', 'intervene', 'education', 'remind', 'assess']
      const records = []
      const now = new Date()
      for (let i = 0; i < total; i++) {
        const isCompleted = i < completed
        const isCurrent = i === completed && status !== 'quit' && status !== 'completed'
        const isOverdue = i === completed && status === 'overdue'
        let recordStatus = 'pending'
        if (isCompleted) recordStatus = 'completed'
        else if (isCurrent) recordStatus = 'in_progress'
        else if (isOverdue) recordStatus = 'overdue'
        if (status === 'quit' && i >= completed) recordStatus = 'quit'

        const execDate = new Date(now)
        execDate.setDate(execDate.getDate() - (total - i) * 7)
        records.push({
          nodeName: nodeNames[i] || '节点' + (i + 1),
          nodeType: nodeTypes[i] || 'follow',
          status: recordStatus,
          execTime: isCompleted || isCurrent ? execDate.toISOString().split('T')[0] + ' ' + ['08:00', '10:30', '14:00', '09:15', '11:45', '16:20'][i] : null,
          completedTime: isCompleted ? execDate.toISOString().split('T')[0] + ' ' + ['09:30', '11:00', '15:30', '10:45', '12:15', '17:00'][i] : null,
          remark: isCompleted ? '已完成随访，各项指标正常' : '',
          crfs: isCompleted ? [{ id: 'a' + i, name: '随访记录表' }, { id: 'b' + i, name: '用药评估表' }] : []
        })
      }
      return records
    },
    getRecordNodeTypeLabel(type) {
      const map = { follow: '随访', assess: '评估', intervene: '干预', education: '宣教', remind: '提醒', order: '医嘱' }
      return map[type] || type
    },
    getRecordNodeTypeTag(type) {
      const map = { follow: 'primary', assess: 'success', intervene: 'warning', education: '', remind: 'info', order: 'danger' }
      return map[type] || ''
    },
    handleExport() {
      this.$modal.loading('正在导出执行跟踪数据...')
      setTimeout(() => {
        this.$modal.closeLoading()
        this.$modal.msgSuccess('导出成功')
      }, 500)
    }
  }
}
</script>

<style scoped>
.empty-hint { padding: 80px 0; text-align: center; }
.progress-cell { display: flex; align-items: center; gap: 8px; }
.progress-text { font-size: 12px; color: #909399; white-space: nowrap; }
.exec-detail { max-height: 70vh; overflow-y: auto; }
.record-header { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.record-node-name { font-weight: bold; font-size: 14px; }
.record-info { font-size: 13px; color: #606266; margin-top: 4px; }
</style>
