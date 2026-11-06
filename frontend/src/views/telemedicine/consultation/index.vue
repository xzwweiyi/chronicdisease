<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="会诊编号" prop="consultationNo">
        <el-input v-model="queryParams.consultationNo" placeholder="请输入编号" clearable />
      </el-form-item>
      <el-form-item label="患者姓名" prop="patientName">
        <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable />
      </el-form-item>
      <el-form-item label="会诊类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable>
          <el-option label="常规" value="常规" />
          <el-option label="紧急" value="紧急" />
          <el-option label="疑难" value="疑难" />
        </el-select>
      </el-form-item>
      <el-form-item label="会诊状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待会诊" value="0" />
          <el-option label="进行中" value="1" />
          <el-option label="已完成" value="2" />
          <el-option label="已取消" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">发起会诊</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === '0'" type="text" size="mini" @click="handleStart(scope.row)">开始会诊</el-button>
          <el-button v-if="scope.row.status === '1'" type="text" size="mini" @click="handleJoin(scope.row)">加入会诊</el-button>
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">详情</el-button>
          <el-button v-if="scope.row.status === '0'" type="text" size="mini" style="color:#F56C6C" @click="handleCancel(scope.row)">取消</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="consultationNo" label="会诊编号" width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="patientName" label="患者姓名" width="100" align="center" />
      <el-table-column prop="applicant" label="申请人" width="100" align="center" />
      <el-table-column prop="type" label="会诊类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="typeTag(scope.row.type)" size="small">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="appointmentTime" label="预约时间" width="160" align="center" />
      <el-table-column prop="participants" label="参与人员" min-width="200" align="center" show-overflow-tooltip />
      <el-table-column prop="statusName" label="会诊状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="statusTag(scope.row.status)">{{ scope.row.statusName }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 发起会诊对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input v-model="form.patientName" placeholder="请输入患者姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会诊类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择类型" style="width:100%">
                <el-option label="常规" value="常规" />
                <el-option label="紧急" value="紧急" />
                <el-option label="疑难" value="疑难" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预约时间" prop="appointmentTime">
              <el-date-picker v-model="form.appointmentTime" type="datetime" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参与人员" prop="participants">
              <el-select v-model="form.participants" multiple placeholder="请选择参与人员" style="width:100%">
                <el-option label="张医生（心内科）" value="张医生" />
                <el-option label="李医生（内分泌科）" value="李医生" />
                <el-option label="王医生（神经内科）" value="王医生" />
                <el-option label="赵主任（心内科）" value="赵主任" />
                <el-option label="陈医生（呼吸内科）" value="陈医生" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="会诊原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请输入会诊原因" />
        </el-form-item>
        <el-form-item label="病情摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="4" placeholder="请输入病情摘要" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">发起会诊</el-button>
      </div>
    </el-dialog>

    <!-- 会诊详情对话框 -->
    <el-dialog title="会诊详情" :visible.sync="detailVisible" width="700px" append-to-body>
      <el-descriptions v-if="detailData" :column="2" border size="small">
        <el-descriptions-item label="会诊编号">{{ detailData.consultationNo }}</el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ detailData.patientName }}</el-descriptions-item>
        <el-descriptions-item label="会诊类型">{{ detailData.type }}</el-descriptions-item>
        <el-descriptions-item label="会诊状态">{{ detailData.statusName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicant }}</el-descriptions-item>
        <el-descriptions-item label="预约时间">{{ detailData.appointmentTime }}</el-descriptions-item>
        <el-descriptions-item label="参与人员" :span="2">{{ detailData.participants }}</el-descriptions-item>
        <el-descriptions-item label="会诊原因" :span="2">{{ detailData.reason }}</el-descriptions-item>
        <el-descriptions-item label="会诊结论" :span="2">{{ detailData.conclusion || '暂无结论' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'TeleConsultation',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, consultationNo: '', patientName: '', type: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      detailVisible: false,
      detailData: null,
      tableData: [],
      form: { patientName: '', type: '', appointmentTime: '', participants: [], reason: '', summary: '' },
      rules: {
        patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
        type: [{ required: true, message: '请选择会诊类型', trigger: 'change' }],
        appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
        participants: [{ required: true, message: '请选择参与人员', trigger: 'change' }],
        reason: [{ required: true, message: '请输入会诊原因', trigger: 'blur' }]
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
      var statusMap = { '0': '待会诊', '1': '进行中', '2': '已完成', '3': '已取消' }
      var types = ['常规', '紧急', '疑难']
      var patients = ['王建国', '李秀兰', '张明华', '刘桂英', '赵志强']
      var applicants = ['陈医生', '李医生', '王医生', '赵医生']
      var participants = ['张医生(心内科)、李医生(内分泌科)、王医生(神经内科)', '赵主任(心内科)、陈医生(呼吸内科)、刘医生(影像科)', '张医生(心内科)、赵主任(心内科)、孙医生(营养科)', '李医生(内分泌科)、王医生(神经内科)、周医生(康复科)']
      var reasons = ['高血压合并冠心病，血压控制不佳', '糖尿病并发肾病，需多学科评估', '脑卒中后康复方案制定', '不明原因胸痛待查', '慢阻肺急性加重期管理']
      var data = []
      for (var i = 0; i < 8; i++) {
        data.push({
          id: i + 1,
          consultationNo: 'CONS-2026' + String(Math.floor(Math.random() * 9000 + 1000)),
          patientName: patients[i % patients.length],
          applicant: applicants[i % applicants.length],
          type: types[i % 3],
          appointmentTime: '2026-0' + (Math.floor(Math.random() * 6) + 1) + '-' + String(Math.floor(Math.random() * 28) + 1).padStart(2, '0') + ' ' + String(Math.floor(Math.random() * 12) + 8).padStart(2, '0') + ':00:00',
          participants: participants[i % participants.length],
          status: String(i % 4),
          statusName: statusMap[String(i % 4)],
          reason: reasons[i % reasons.length],
          conclusion: i >= 2 ? '经多学科会诊讨论，建议调整降压方案并加强随访' : ''
        })
      }
      return data
    },
    typeTag(type) { var map = { '常规': 'primary', '紧急': 'danger', '疑难': 'warning' }; return map[type] || 'info' },
    statusTag(status) { var map = { '0': 'warning', '1': 'primary', '2': 'success', '3': 'info' }; return map[status] || 'info' },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '发起会诊'
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { patientName: '', type: '', appointmentTime: '', participants: [], reason: '', summary: '' }
      })
    },
    submitForm() {
      var that = this
      this.$refs.form.validate(function(valid) {
        if (valid) { that.$message.success('会诊申请已提交'); that.dialogVisible = false; that.getList() }
      })
    },
    handleStart(row) {
      var that = this
      this.$confirm('确认开始会诊"' + row.consultationNo + '"?', '提示', { type: 'info' }).then(function() {
        row.status = '1'; row.statusName = '进行中'; that.$message.success('会诊已开始')
      })
    },
    handleJoin(row) { this.$message.info('正在加入会诊室...（模拟）') },
    handleCancel(row) {
      var that = this
      this.$confirm('确认取消会诊"' + row.consultationNo + '"?', '警告', { type: 'warning' }).then(function() {
        row.status = '3'; row.statusName = '已取消'; that.$message.success('已取消')
      })
    },
    handleDetail(row) { this.detailData = Object.assign({}, row); this.detailVisible = true }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
</style>
