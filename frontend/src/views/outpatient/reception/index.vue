<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="待接诊" name="pending">
        <reception-table :status-filter="'pending'" ref="pendingTable" />
      </el-tab-pane>
      <el-tab-pane label="待回诊" name="review">
        <reception-table :status-filter="'review'" ref="reviewTable" />
      </el-tab-pane>
      <el-tab-pane label="紧急患者" name="emergency">
        <reception-table :status-filter="'emergency'" ref="emergencyTable" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
const ReceptionTable = {
  name: 'ReceptionTable',
  props: { statusFilter: { type: String, default: 'pending' } },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, name: '', visitCardNo: '', riskLevel: '' },
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      detailVisible: false,
      detailData: {}
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      const mockAll = [
        { id: 1, name: '张三', visitCardNo: 'VC202606001', regDate: '2026-06-20', gender: '男', age: 58, visitType: '初诊', riskLevel: 'high', dept: '心内科', status: 'pending', remark: '' },
        { id: 2, name: '李四', visitCardNo: 'VC202606002', regDate: '2026-06-20', gender: '女', age: 45, visitType: '复诊', riskLevel: 'medium', dept: '内分泌科', status: 'review', remark: '' },
        { id: 3, name: '王五', visitCardNo: 'VC202606003', regDate: '2026-06-21', gender: '男', age: 62, visitType: '急诊', riskLevel: 'high', dept: '神经内科', status: 'emergency', remark: '血压190/110，需紧急处理' },
        { id: 4, name: '赵六', visitCardNo: 'VC202606004', regDate: '2026-06-21', gender: '女', age: 55, visitType: '初诊', riskLevel: 'low', dept: '全科门诊', status: 'pending', remark: '' },
        { id: 5, name: '孙七', visitCardNo: 'VC202606005', regDate: '2026-06-22', gender: '男', age: 50, visitType: '复诊', riskLevel: 'medium', dept: '呼吸内科', status: 'review', remark: '' },
        { id: 6, name: '周八', visitCardNo: 'VC202606006', regDate: '2026-06-22', gender: '女', age: 68, visitType: '急诊', riskLevel: 'high', dept: '心内科', status: 'emergency', remark: '胸痛待查' },
        { id: 7, name: '吴九', visitCardNo: 'VC202606007', regDate: '2026-06-23', gender: '男', age: 72, visitType: '初诊', riskLevel: 'high', dept: '老年科', status: 'pending', remark: '' },
        { id: 8, name: '郑十', visitCardNo: 'VC202606008', regDate: '2026-06-23', gender: '女', age: 40, visitType: '复诊', riskLevel: 'low', dept: '内分泌科', status: 'review', remark: '' },
      ]
      const filtered = mockAll.filter(item => item.status === this.statusFilter)
      setTimeout(() => { this.tableData = filtered; this.total = filtered.length; this.loading = false }, 300)
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm && this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleReceive(row) {
      this.$confirm('确认接诊该患者吗？', '接诊确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'success' })
        .then(() => { this.$message.success('接诊成功'); this.getList() }).catch(() => {})
    },
    handleDetail(row) { this.detailData = { ...row }; this.detailVisible = true },
    handleSizeChange(val) { this.queryParams.pageSize = val; this.getList() },
    handleCurrentChange(val) { this.queryParams.pageNum = val; this.getList() }
  },
  template: `<div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="就诊卡号" prop="visitCardNo">
        <el-input v-model="queryParams.visitCardNo" placeholder="请输入就诊卡号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="风险等级" prop="riskLevel">
        <el-select v-model="queryParams.riskLevel" placeholder="请选择风险等级" clearable size="small">
          <el-option label="高风险" value="high" />
          <el-option label="中风险" value="medium" />
          <el-option label="低风险" value="low" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="tableData" border stripe>
      <el-table-column label="序号" type="index" width="55" align="center" />
      <el-table-column prop="name" label="姓名" min-width="100" align="center" />
      <el-table-column prop="visitCardNo" label="就诊卡号" min-width="140" align="center" />
      <el-table-column prop="regDate" label="挂号日期" min-width="110" align="center" />
      <el-table-column prop="gender" label="性别" width="60" align="center" />
      <el-table-column prop="age" label="年龄" width="60" align="center" />
      <el-table-column prop="visitType" label="就诊类型" min-width="90" align="center" />
      <el-table-column prop="riskLevel" label="风险等级" min-width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.riskLevel==='high'" type="danger">高风险</el-tag>
          <el-tag v-else-if="scope.row.riskLevel==='medium'" type="warning">中风险</el-tag>
          <el-tag v-else type="success">低风险</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dept" label="分诊科室" min-width="120" align="center" />
      <el-table-column prop="remark" label="备注" min-width="140" align="center" show-overflow-tooltip />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleReceive(scope.row)">接诊</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
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

    <el-dialog title="患者详情" :visible.sync="detailVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="就诊卡号">{{ detailData.visitCardNo }}</el-descriptions-item>
        <el-descriptions-item label="挂号日期">{{ detailData.regDate }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailData.age }}</el-descriptions-item>
        <el-descriptions-item label="就诊类型">{{ detailData.visitType }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ detailData.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="分诊科室">{{ detailData.dept }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>`
}

export default {
  name: 'OutpatientReception',
  components: { ReceptionTable },
  data() {
    return {
      activeTab: 'pending'
    }
  },
  methods: {
    handleTabClick(tab) {
      this.$nextTick(() => {
        const refMap = { pending: 'pendingTable', review: 'reviewTable', emergency: 'emergencyTable' }
        const ref = this.$refs[refMap[tab.name]]
        ref && ref.getList()
      })
    }
  }
}
</script>

<style scoped>
</style>
