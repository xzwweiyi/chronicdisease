<template>
  <div class="app-container dashboard">
    <h2 class="page-title">工作台</h2>

    <!-- KPI卡片 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-blue">
          <div class="kpi-content">
            <div class="kpi-icon-bg blue"><i class="el-icon-user-solid" /></div>
            <div class="kpi-info">
              <div class="kpi-label">管理患者总数</div>
              <div class="kpi-value">{{ formatCount(summary.totalPatients) }}</div>
              <div class="kpi-sub">来源于基础表 mp_person</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-green">
          <div class="kpi-content">
            <div class="kpi-icon-bg green"><i class="el-icon-circle-check" /></div>
            <div class="kpi-info">
              <div class="kpi-label">本月随访完成率</div>
              <div class="kpi-value">{{ summary.followRate || 0 }}%</div>
              <div class="kpi-sub">已完成 {{ summary.followCompleted || 0 }} / {{ summary.followTotal || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-orange">
          <div class="kpi-content">
            <div class="kpi-icon-bg orange"><i class="el-icon-data-line" /></div>
            <div class="kpi-info">
              <div class="kpi-label">控制达标率</div>
              <div class="kpi-value">{{ summary.controlRate || 0 }}%</div>
              <div class="kpi-sub">基于已完成随访记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-red">
          <div class="kpi-content">
            <div class="kpi-icon-bg red"><i class="el-icon-bell" /></div>
            <div class="kpi-info">
              <div class="kpi-label">待处理转诊</div>
              <div class="kpi-value">{{ summary.pendingReferrals || 0 }}</div>
              <div class="kpi-sub">需要立即处理</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表行 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>慢病趋势分析（近12个月新增管理居民）</span></div>
          <div ref="trendChart" class="chart-container" />
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>慢病病种分布</span></div>
          <div ref="diseaseChart" class="chart-container" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 机构排名 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>机构管理居民排名</span></div>
          <div ref="institutionChart" class="chart-container institution-container" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部面板 -->
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card shadow="never" class="bottom-card">
          <div slot="header" class="card-header"><span>重点关注患者</span></div>
          <el-table v-loading="loading" :data="focusPatients" border stripe size="small">
            <el-table-column prop="name" label="姓名" width="90" align="center" />
            <el-table-column prop="age" label="年龄" width="60" align="center">
              <template slot-scope="scope">{{ scope.row.age == null ? '--' : scope.row.age }}</template>
            </el-table-column>
            <el-table-column prop="gender" label="性别" width="60" align="center" />
            <el-table-column prop="disease" label="病种" width="90" align="center">
              <template slot-scope="scope">
                <el-tag size="mini" type="danger">{{ scope.row.disease }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="riskLevel" label="风险等级" width="80" align="center">
              <template slot-scope="scope">
                <el-tag size="mini" :type="scope.row.riskLevel === '高危' ? 'danger' : 'warning'">{{ scope.row.riskLevel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastFollowDate" label="最近随访" width="120" align="center" />
            <el-table-column prop="phone" label="联系电话" min-width="120" align="center" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="bottom-card">
          <div slot="header" class="card-header"><span>待办事项</span></div>
          <div class="todo-list">
            <div class="todo-item" v-for="(item, idx) in todoList" :key="idx">
              <el-tag :type="item.tagType" size="small" class="todo-tag">{{ item.tag }}</el-tag>
              <span class="todo-text">{{ item.title }}</span>
              <span class="todo-time">{{ item.dueDate }}</span>
            </div>
            <div v-if="!todoList || todoList.length === 0" class="todo-empty">暂无待办事项</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardData } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      loading: false,
      summary: {},
      diseaseDistribution: [],
      trendAnalysis: { months: [], series: [] },
      institutionRanking: { institutions: [] },
      focusPatients: [],
      todoList: [],
      charts: {}
    }
  },
  created() {
    this.getData()
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    Object.keys(this.charts).forEach(k => {
      if (this.charts[k]) this.charts[k].dispose()
    })
  },
  methods: {
    getData() {
      this.loading = true
      getDashboardData().then(response => {
        this.loading = false
        if (!response) return
        this.summary = response.summary || {}
        this.diseaseDistribution = response.diseaseDistribution || []
        this.trendAnalysis = response.trendAnalysis || { months: [], series: [] }
        this.institutionRanking = response.institutionRanking || { institutions: [] }
        this.focusPatients = response.focusPatients || []
        this.todoList = response.todoList || []
        this.$nextTick(() => this.initCharts())
      }).catch(() => {
        this.loading = false
      })
    },
    initCharts() {
      this.renderTrendChart()
      this.renderDiseaseChart()
      this.renderInstitutionChart()
    },
    renderTrendChart() {
      const dom = this.$refs.trendChart
      if (!dom) return
      if (this.charts.trend) this.charts.trend.dispose()
      const chart = echarts.init(dom)
      this.charts.trend = chart
      const months = this.trendAnalysis.months || []
      const series = (this.trendAnalysis.series || []).map(s => ({
        name: s.name,
        type: 'line',
        smooth: true,
        data: s.data || []
      }))
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: series.map(s => s.name) },
        grid: { left: 40, right: 20, top: 40, bottom: 30 },
        xAxis: { type: 'category', data: months },
        yAxis: { type: 'value', minInterval: 1 },
        series
      })
    },
    renderDiseaseChart() {
      const dom = this.$refs.diseaseChart
      if (!dom) return
      if (this.charts.disease) this.charts.disease.dispose()
      const chart = echarts.init(dom)
      this.charts.disease = chart
      const data = this.diseaseDistribution || []
      const total = data.reduce((sum, d) => sum + (d.value || 0), 0)
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, data: data.map(d => d.name) },
        series: [{
          type: 'pie',
          radius: ['45%', '70%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: true,
          itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          data
        }],
        graphic: [{
          type: 'text',
          left: 'center',
          top: '38%',
          style: {
            text: this.formatCount(total),
            textAlign: 'center',
            fill: '#303133',
            fontSize: 20,
            fontWeight: 'bold'
          }
        }, {
          type: 'text',
          left: 'center',
          top: '48%',
          style: {
            text: '总患者',
            textAlign: 'center',
            fill: '#909399',
            fontSize: 12
          }
        }]
      })
    },
    renderInstitutionChart() {
      const dom = this.$refs.institutionChart
      if (!dom) return
      if (this.charts.institution) this.charts.institution.dispose()
      const chart = echarts.init(dom)
      this.charts.institution = chart
      const institutions = this.institutionRanking.institutions || []
      const names = institutions.map(i => i.name)
      const values = institutions.map(i => i.patientCount || 0)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 120, right: 30, top: 20, bottom: 30 },
        xAxis: { type: 'value', minInterval: 1 },
        yAxis: { type: 'category', data: names },
        series: [{
          type: 'bar',
          barMaxWidth: 20,
          itemStyle: { color: '#409EFF' },
          data: values
        }]
      })
    },
    handleResize() {
      Object.keys(this.charts).forEach(k => {
        if (this.charts[k]) this.charts[k].resize()
      })
    },
    formatCount(n) {
      if (n == null) return '0'
      const num = Number(n)
      if (num >= 10000) {
        return (num / 10000).toFixed(2) + '万'
      }
      return String(num)
    }
  }
}
</script>

<style scoped>
.dashboard { background: #f0f2f5; padding: 20px; min-height: 100%; }
.page-title { margin: 0 0 20px; font-size: 22px; font-weight: 600; color: #303133; }
.kpi-row { margin-bottom: 20px; }
.kpi-card { border-left: 4px solid; border-radius: 8px; }
.kpi-card.kpi-blue { border-left-color: #409EFF; }
.kpi-card.kpi-green { border-left-color: #67C23A; }
.kpi-card.kpi-orange { border-left-color: #E6A23C; }
.kpi-card.kpi-red { border-left-color: #F56C6C; }
.kpi-content { display: flex; align-items: center; gap: 16px; }
.kpi-icon-bg { width: 52px; height: 52px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 24px; color: #fff; }
.kpi-icon-bg.blue { background: linear-gradient(135deg, #409EFF, #66b1ff); }
.kpi-icon-bg.green { background: linear-gradient(135deg, #67C23A, #85ce61); }
.kpi-icon-bg.orange { background: linear-gradient(135deg, #E6A23C, #ebb563); }
.kpi-icon-bg.red { background: linear-gradient(135deg, #F56C6C, #f78989); }
.kpi-label { font-size: 13px; color: #909399; margin-bottom: 4px; }
.kpi-value { font-size: 26px; font-weight: 700; color: #303133; }
.kpi-sub { font-size: 12px; color: #909399; }
.chart-row { margin-bottom: 20px; }
.chart-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: 600; color: #303133; }
.chart-container { height: 320px; width: 100%; }
.institution-container { height: 360px; }
.bottom-card { border-radius: 8px; }
.todo-list { padding: 4px 0; }
.todo-item { display: flex; align-items: center; padding: 8px 0; border-bottom: 1px solid #f5f5f5; gap: 10px; }
.todo-item:last-child { border-bottom: none; }
.todo-tag { flex-shrink: 0; }
.todo-text { flex: 1; font-size: 13px; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.todo-time { font-size: 11px; color: #C0C4CC; flex-shrink: 0; }
.todo-empty { text-align: center; color: #C0C4CC; font-size: 13px; padding: 20px 0; }
</style>
