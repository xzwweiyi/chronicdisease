<template>
  <div class="app-container">
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6">
        <div class="kpi-card kpi-card-primary">
          <div class="kpi-icon"><i class="el-icon-user-solid"></i></div>
          <div class="kpi-info">
            <div class="kpi-value">{{ kpiData.totalPatients }}</div>
            <div class="kpi-label">管理患者</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="kpi-card kpi-card-success">
          <div class="kpi-icon"><i class="el-icon-plus"></i></div>
          <div class="kpi-info">
            <div class="kpi-value">{{ kpiData.newThisMonth }}</div>
            <div class="kpi-label">本月新增</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="kpi-card kpi-card-info">
          <div class="kpi-icon"><i class="el-icon-data-line"></i></div>
          <div class="kpi-info">
            <div class="kpi-value">{{ pct(kpiData.controlRate) }}</div>
            <div class="kpi-label">控制达标率</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="kpi-card kpi-card-warning">
          <div class="kpi-icon"><i class="el-icon-warning-outline"></i></div>
          <div class="kpi-info">
            <div class="kpi-value">{{ pct(kpiData.interventionRate) }}</div>
            <div class="kpi-label">高危干预率</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px" class="search-form">
      <el-form-item label="居民姓名" prop="personName">
        <el-input v-model="queryParams.personName" placeholder="请输入居民姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份证号" prop="cardId">
        <el-input v-model="queryParams.cardId" placeholder="请输入身份证号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
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
      <el-table-column prop="age" label="年龄" width="60" align="center" />
      <el-table-column prop="cardId" label="身份证号" min-width="180" align="center" />
      <el-table-column prop="phoneNumber" label="联系电话" min-width="120" align="center" />
      <el-table-column prop="diseaseTypes" label="慢病类型" min-width="120" align="center" show-overflow-tooltip />
      <el-table-column prop="managingInstitution" label="管理机构" min-width="140" align="center" show-overflow-tooltip />
      <el-table-column prop="managingDoctor" label="管理医生" min-width="100" align="center" />
      <el-table-column prop="isSigned" label="签约状态" min-width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isSigned === '1' || scope.row.isSigned === '已签约'" type="success" size="small">已签约</el-tag>
          <el-tag v-else-if="scope.row.isSigned === '0' || scope.row.isSigned === '未签约'" type="info" size="small">未签约</el-tag>
          <span v-else>{{ scope.row.isSigned || '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="建档时间" width="140" align="center">
        <template slot-scope="scope">{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center" fixed="right">
        <template slot-scope="scope">
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

    <el-dialog title="脑卒中患者详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="居民姓名">{{ detailData.personName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ genderLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.cardId }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailData.age != null ? detailData.age + '岁' : '--' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phoneNumber || '--' }}</el-descriptions-item>
        <el-descriptions-item label="居住地址">{{ detailData.address || '--' }}</el-descriptions-item>
        <el-descriptions-item label="慢病类型">{{ detailData.diseaseTypes || '--' }}</el-descriptions-item>
        <el-descriptions-item label="管理机构">{{ detailData.managingInstitution || '--' }}</el-descriptions-item>
        <el-descriptions-item label="管理医生">{{ detailData.managingDoctor || '--' }}</el-descriptions-item>
        <el-descriptions-item label="签约状态">{{ detailData.isSigned === '1' || detailData.isSigned === '已签约' ? '已签约' : '未签约' }}</el-descriptions-item>
        <el-descriptions-item label="档案来源">{{ detailData.archiveSource || '--' }}</el-descriptions-item>
        <el-descriptions-item label="建档时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '--' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getDiseaseList, getDiseaseStats } from '@/api/resident/disease'
export default {
  name: 'ResidentStroke',
  data() {
    return {
      loading: false,
      detailVisible: false,
      total: 0,
      disease: 'stroke',
      kpiData: { totalPatients: 0, newThisMonth: 0, controlRate: 0, interventionRate: 0 },
      queryParams: { pageNum: 1, pageSize: 10, personName: '', cardId: '' },
      detailData: {},
      tableData: []
    }
  },
  created() {
    this.getList()
    this.getStats()
  },
  methods: {
    genderLabel(row) {
      const v = row && row.genderSourceValue
      if (v === '0') return '男'
      if (v === '1') return '女'
      return (row && row.sex) || '--'
    },
    pct(v) {
      return (v === undefined || v === null || v === 0) ? '--' : v + '%'
    },
    getList() {
      this.loading = true
      getDiseaseList(this.disease, this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    getStats() {
      getDiseaseStats(this.disease).then(res => {
        const d = res.data || {}
        this.kpiData.totalPatients = d.totalPatients || 0
        this.kpiData.newThisMonth = d.newThisMonth || 0
        this.kpiData.controlRate = d.controlRate || 0
        this.kpiData.interventionRate = d.interventionRate || 0
      }).catch(() => {})
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.queryParams = { pageNum: 1, pageSize: 10, personName: '', cardId: '' }; this.handleQuery() },
    handleDetail(row) { this.detailData = { ...row }; this.detailVisible = true },
    handleExport() { this.$message.info('导出功能开发中') },
    handleSizeChange(val) { this.queryParams.pageSize = val; this.getList() },
    handleCurrentChange(val) { this.queryParams.pageNum = val; this.getList() }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.search-form { margin-top: 16px; }
.kpi-row { margin-bottom: 16px; }
.kpi-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  transition: transform 0.2s;
}
.kpi-card:hover { transform: translateY(-2px); }
.kpi-card-primary { background: linear-gradient(135deg, #409EFF, #337ECC); }
.kpi-card-success { background: linear-gradient(135deg, #67C23A, #529B2E); }
.kpi-card-info { background: linear-gradient(135deg, #909399, #73767A); }
.kpi-card-warning { background: linear-gradient(135deg, #E6A23C, #CF9236); }
.kpi-icon { font-size: 36px; margin-right: 16px; opacity: 0.7; }
.kpi-info { flex: 1; }
.kpi-value { font-size: 28px; font-weight: bold; line-height: 1.2; }
.kpi-label { font-size: 14px; opacity: 0.9; margin-top: 4px; }
</style>
