<template>
  <div class="comprehensive-container">
    <!-- 页面标题 -->
    <div class="page-title-wrapper">
      <span class="page-title-bar"></span>
      <span class="page-title-text">慢病综合数据分析</span>
    </div>
    <div class="title-divider"></div>

    <!-- KPI指标卡片 -->
    <el-row :gutter="20" class="kpi-cards">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-blue">
          <div class="kpi-icon-bg">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="kpi-info">
            <div class="kpi-label">管理患者总数</div>
            <div class="kpi-value">
              <count-to :start-val="0" :end-val="kpiData.totalPatients" :duration="1200" />
            </div>
            <div class="kpi-sub">
              <span>较上月 </span>
              <span class="up" v-if="kpiData.patientChange >= 0">+{{ kpiData.patientChange }}% <i class="el-icon-top"></i></span>
              <span class="down" v-else>{{ kpiData.patientChange }}% <i class="el-icon-bottom"></i></span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-green">
          <div class="kpi-icon-bg green">
            <i class="el-icon-circle-check"></i>
          </div>
          <div class="kpi-info">
            <div class="kpi-label">本月随访完成率</div>
            <div class="kpi-value">
              <count-to :start-val="0" :end-val="kpiData.followUpRate" :duration="1200" :decimals="1" suffix="%" />
            </div>
            <div class="kpi-sub">
              <span>完成 {{ kpiData.followCompleted }}/{{ kpiData.followTotal }}</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-orange">
          <div class="kpi-icon-bg orange">
            <i class="el-icon-data-line"></i>
          </div>
          <div class="kpi-info">
            <div class="kpi-label">控制达标率</div>
            <div class="kpi-value">
              <count-to :start-val="0" :end-val="kpiData.controlRate" :duration="1200" :decimals="1" suffix="%" />
            </div>
            <div class="kpi-sub">
              <span>较上月 </span>
              <span class="up" v-if="kpiData.controlChange >= 0">+{{ kpiData.controlChange }}% <i class="el-icon-top"></i></span>
              <span class="down" v-else>{{ kpiData.controlChange }}% <i class="el-icon-bottom"></i></span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card kpi-red">
          <div class="kpi-icon-bg red">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="kpi-info">
            <div class="kpi-label">本月预警数量</div>
            <div class="kpi-value danger">
              <count-to :start-val="0" :end-val="kpiData.warnCount" :duration="1200" />
            </div>
            <div class="kpi-sub">
              <span>待处理 {{ kpiData.pendingWarnCount || 0 }} 条</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 趋势分析 + 病种分布 -->
    <el-row :gutter="20" class="chart-section">
      <el-col :xs="24" :lg="16">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">慢病趋势分析（近12个月）</span>
          </div>
          <div class="chart-body">
            <div ref="trendChart" class="chart-dom" v-loading="chartLoading"></div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="8">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">慢病病种分布</span>
          </div>
          <div class="chart-body">
            <div ref="diseaseChart" class="chart-dom" v-loading="chartLoading"></div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 机构对比 + 年龄分布 -->
    <el-row :gutter="20" class="chart-section">
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">机构管理对比</span>
          </div>
          <div class="table-body">
            <el-table :data="institutionData" border stripe size="small" v-loading="tableLoading">
              <el-table-column prop="name" label="机构名称" min-width="140" align="center" />
              <el-table-column prop="patientCount" label="管理患者数" width="100" align="center" sortable />
              <el-table-column prop="followUpRate" label="随访完成率" width="130" align="center" sortable>
                <template slot-scope="scope">
                  <el-progress :percentage="scope.row.followUpRate || 0"
                    :color="progressColor(scope.row.followUpRate)" :stroke-width="14" />
                </template>
              </el-table-column>
              <el-table-column prop="controlRate" label="控制达标率" width="130" align="center" sortable>
                <template slot-scope="scope">
                  <el-progress :percentage="scope.row.controlRate || 0"
                    :color="progressColor(scope.row.controlRate)" :stroke-width="14" />
                </template>
              </el-table-column>
              <el-table-column prop="warnCount" label="预警数" width="80" align="center">
                <template slot-scope="scope">
                  <span :style="{ color: (scope.row.warnCount || 0) > 10 ? '#FF0000' : '#00CC33' }">
                    {{ scope.row.warnCount || 0 }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">患者年龄分布</span>
          </div>
          <div class="chart-body chart-body-sm">
            <div ref="ageChart" class="chart-dom" v-loading="chartLoading"></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import * as echarts from 'echarts'
import {
  getHospitalView,
  getPatientDistribute,
  getTrendChart,
  getFollowRate,
  getAgeDistribution
} from '@/api/statistics/config'

export default {
  name: 'ComprehensiveAnalysis',
  components: { CountTo },
  data() {
    return {
      chartLoading: false,
      tableLoading: false,
      kpiData: {
        totalPatients: 0,
        patientChange: 3.2,
        followUpRate: 0,
        followCompleted: 0,
        followTotal: 0,
        controlRate: 0,
        controlChange: 0.5,
        warnCount: 0,
        pendingWarnCount: 0
      },
      institutionData: [],
      diseaseDistribution: [],
      trendMonths: [],
      trendSeries: [],
      ageDistribution: [],
      trendChart: null,
      diseaseChart: null,
      ageChart: null
    }
  },
  mounted() {
    this.fetchAllData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    this.disposeCharts()
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    async fetchAllData() {
      this.chartLoading = true
      this.tableLoading = true
      try {
        // Fetch multiple APIs concurrently
        const [hospitalRes, patientRes, trendRes, followRes, ageRes] = await Promise.allSettled([
          getHospitalView(),
          getPatientDistribute(),
          getTrendChart(),
          getFollowRate(),
          getAgeDistribution()
        ])

        // Process hospital view (院级全景图) for KPIs
        if (hospitalRes.status === 'fulfilled' && hospitalRes.value) {
          this.processHospitalData(hospitalRes.value)
        }

        // Process patient distribution for disease breakdown
        if (patientRes.status === 'fulfilled' && patientRes.value) {
          this.processPatientData(patientRes.value)
        }

        // Process trend data
        if (trendRes.status === 'fulfilled' && trendRes.value) {
          this.processTrendData(trendRes.value)
        }

        // Process follow rate
        if (followRes.status === 'fulfilled' && followRes.value) {
          this.processFollowData(followRes.value)
        }

        // Process age distribution
        if (ageRes.status === 'fulfilled' && ageRes.value) {
          this.processAgeData(ageRes.value)
        }

        // Initialize charts after data is ready
        this.$nextTick(() => {
          this.initCharts()
        })
      } catch (e) {
        console.error('Failed to fetch comprehensive analysis data:', e)
        // Fallback mock data
        this.setMockData()
        this.$nextTick(() => { this.initCharts() })
      } finally {
        this.chartLoading = false
        this.tableLoading = false
      }
    },

    processHospitalData(data) {
      const d = data.data || data
      if (d.totalPatients !== undefined) this.kpiData.totalPatients = Number(d.totalPatients) || 0
      if (d.followUpRate !== undefined) this.kpiData.followUpRate = Number(d.followUpRate) || 0
      if (d.controlRate !== undefined) this.kpiData.controlRate = Number(d.controlRate) || 0
      if (d.warnCount !== undefined) this.kpiData.warnCount = Number(d.warnCount) || 0
      if (d.followCompleted !== undefined) this.kpiData.followCompleted = Number(d.followCompleted) || 0
      if (d.followTotal !== undefined) this.kpiData.followTotal = Number(d.followTotal) || 0
    },

    processPatientData(data) {
      const d = data.data || data
      // Patient distribution by disease
      if (Array.isArray(d)) {
        this.diseaseDistribution = d.map(item => ({
          name: item.name || item.diseaseName || '',
          value: Number(item.total || item.count || item.value || 0)
        })).filter(item => item.value > 0)
      }
      if (d.institutions && Array.isArray(d.institutions)) {
        this.institutionData = d.institutions
      }
    },

    processTrendData(data) {
      const d = data.data || data
      if (d.months) this.trendMonths = d.months
      if (d.series) this.trendSeries = d.series
      if (d.values) {
        // Flattened data
        this.trendMonths = d.months || d.labels || []
        this.trendSeries = d.values || []
      }
    },

    processFollowData(data) {
      const d = data.data || data
      if (d.rate !== undefined) this.kpiData.followUpRate = Number(d.rate) || 0
      if (d.completed !== undefined) this.kpiData.followCompleted = Number(d.completed) || 0
      if (d.total !== undefined) this.kpiData.followTotal = Number(d.total) || 0
    },

    processAgeData(data) {
      const d = data.data || data
      if (Array.isArray(d)) {
        this.ageDistribution = d.map(item => ({
          name: item.name || item.ageGroup || '',
          value: Number(item.total || item.count || item.value || 0)
        }))
      }
    },

    setMockData() {
      this.kpiData = {
        totalPatients: 24528, patientChange: 3.2,
        followUpRate: 87.6, followCompleted: 2190, followTotal: 2500,
        controlRate: 68.3, controlChange: 0.5,
        warnCount: 28, pendingWarnCount: 12
      }
      this.diseaseDistribution = [
        { name: '高血压', value: 9800 },
        { name: '糖尿病', value: 7350 },
        { name: '冠心病', value: 3920 },
        { name: '慢阻肺', value: 1960 },
        { name: '脑卒中', value: 1470 }
      ]
      this.trendMonths = ['7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月', '4月', '5月', '6月']
      this.trendSeries = [
        { name: '高血压', data: [3200, 3400, 3600, 3800, 4000, 4200, 4600, 4800, 5000, 5200, 5400, 5800] },
        { name: '糖尿病', data: [2400, 2500, 2600, 2700, 2800, 2900, 3100, 3300, 3500, 3700, 3900, 4100] },
        { name: '冠心病', data: [1200, 1300, 1350, 1400, 1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200] }
      ]
      this.ageDistribution = [
        { name: '0-14岁', value: 120 },
        { name: '15-44岁', value: 1850 },
        { name: '45-59岁', value: 8200 },
        { name: '60-74岁', value: 10500 },
        { name: '75-89岁', value: 3650 },
        { name: '90岁以上', value: 208 }
      ]
      this.institutionData = [
        { name: '城关镇卫生院', patientCount: 5820, followUpRate: 92.3, controlRate: 71.5, warnCount: 8 },
        { name: '柳林镇卫生院', patientCount: 4150, followUpRate: 85.7, controlRate: 66.2, warnCount: 15 },
        { name: '张家川镇卫生院', patientCount: 3680, followUpRate: 88.1, controlRate: 69.8, warnCount: 6 },
        { name: '县人民医院', patientCount: 7250, followUpRate: 78.5, controlRate: 62.4, warnCount: 23 },
        { name: '东关村卫生室', patientCount: 1850, followUpRate: 95.6, controlRate: 75.3, warnCount: 2 }
      ]
    },

    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.diseaseChart) this.diseaseChart.resize()
      if (this.ageChart) this.ageChart.resize()
    },

    initCharts() {
      this.initTrendChart()
      this.initDiseaseChart()
      this.initAgeChart()
    },

    disposeCharts() {
      if (this.trendChart) { this.trendChart.dispose(); this.trendChart = null }
      if (this.diseaseChart) { this.diseaseChart.dispose(); this.diseaseChart = null }
      if (this.ageChart) { this.ageChart.dispose(); this.ageChart = null }
    },

    initTrendChart() {
      if (!this.$refs.trendChart) return
      if (this.trendChart) this.trendChart.dispose()
      this.trendChart = echarts.init(this.$refs.trendChart)

      const series = (this.trendSeries || []).map(s => ({
        name: s.name,
        type: 'line',
        smooth: true,
        data: s.data || [],
        animationDuration: 1500
      }))

      this.trendChart.setOption({
        tooltip: { trigger: 'axis', padding: [8, 12] },
        legend: { bottom: 0, data: series.map(s => s.name) },
        grid: { left: '3%', right: '4%', bottom: '12%', top: '8%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: this.trendMonths },
        yAxis: { type: 'value' },
        series
      })
    },

    initDiseaseChart() {
      if (!this.$refs.diseaseChart) return
      if (this.diseaseChart) this.diseaseChart.dispose()
      this.diseaseChart = echarts.init(this.$refs.diseaseChart)

      this.diseaseChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, type: 'scroll' },
        series: [{
          type: 'pie',
          radius: ['45%', '70%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
          label: { show: true, formatter: '{b}\n{d}%' },
          emphasis: { label: { fontSize: 14, fontWeight: 'bold' } },
          data: this.diseaseDistribution
        }]
      })
    },

    initAgeChart() {
      if (!this.$refs.ageChart) return
      if (this.ageChart) this.ageChart.dispose()
      this.ageChart = echarts.init(this.$refs.ageChart)

      const names = (this.ageDistribution || []).map(d => d.name)
      const values = (this.ageDistribution || []).map(d => d.value)

      this.ageChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, padding: [8, 12] },
        grid: { left: '3%', right: '8%', bottom: '3%', top: '8%', containLabel: true },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30, fontSize: 11 } },
        yAxis: { type: 'value', name: '人数' },
        series: [{
          type: 'bar',
          data: values,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#3A8CFF' },
              { offset: 1, color: '#69B1FF' }
            ]),
            borderRadius: [4, 4, 0, 0]
          },
          barWidth: '50%'
        }]
      })
    },

    progressColor(rate) {
      if (rate >= 90) return '#00CC33'
      if (rate >= 75) return '#3A8CFF'
      if (rate >= 60) return '#FEA213'
      return '#FF0000'
    }
  }
}
</script>

<style lang="scss" scoped>
.comprehensive-container {
  padding: 0 20px 20px;
  background: #F2F7FB;
  min-height: 100%;
  font-family: '微软雅黑', 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

// 页面标题
.page-title-wrapper {
  display: flex;
  align-items: center;
  padding-top: 12px;
  margin-bottom: 0;
}

.page-title-bar {
  width: 6px;
  height: 22px;
  background: #3A8CFF;
  border-radius: 8px;
  display: inline-block;
  margin-right: 10px;
}

.page-title-text {
  font-size: 18px;
  color: rgba(51, 51, 51, 0.9);
  font-weight: 400;
  font-family: '微软雅黑', 'PingFang SC', sans-serif;
}

.title-divider {
  height: 1px;
  background: #E8EEF4;
  margin: 12px 0 20px;
}

// KPI 卡片
.kpi-cards {
  margin-bottom: 16px;
}

.kpi-card {
  background: #fff;
  border: 1px solid #F2F2F2;
  border-radius: 5px;
  padding: 20px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0px 0px 15px rgba(242, 242, 242, 1);
  margin-bottom: 10px;
  position: relative;
  min-height: 115px;

  .kpi-icon-bg {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    background: rgba(24, 144, 255, 0.08);
    font-size: 24px;
    color: #1890ff;
    position: absolute;
    right: 20px;
    top: 20px;

    &.green { background: rgba(82, 196, 26, 0.08); color: #52c41a; }
    &.orange { background: rgba(250, 173, 20, 0.08); color: #faad14; }
    &.red { background: rgba(255, 77, 79, 0.08); color: #ff4d4f; }
  }

  .kpi-info {
    flex: 1;
    min-width: 0;
    padding-right: 60px;

    .kpi-label {
      font-size: 14px;
      color: #666;
      margin-bottom: 8px;
      font-family: '微软雅黑', 'PingFang SC', sans-serif;
    }

    .kpi-value {
      font-size: 28px;
      font-weight: 400;
      color: #333;
      line-height: 1.2;
      font-family: 'Arial', sans-serif;

      &.danger { color: #FF0000; }
    }

    .kpi-sub {
      margin-top: 6px;
      font-size: 12px;
      color: #999;

      .up { color: #00CC33; }
      .down { color: #FF0000; }
    }
  }
}

// 图表卡片
.chart-section {
  margin-bottom: 0;
}

.chart-card {
  background: #fff;
  border: 1px solid #F2F2F2;
  border-radius: 5px;
  box-shadow: 0px 0px 15px rgba(242, 242, 242, 1);
  margin-bottom: 20px;
  overflow: hidden;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 20px;
    border-bottom: 1px solid #F5F5F5;

    .card-title {
      font-size: 16px;
      color: #1E1E1E;
      font-family: '微软雅黑', 'PingFang SC', sans-serif;
    }
  }

  .chart-body {
    padding: 16px;
    height: 350px;

    &.chart-body-sm {
      height: 380px;
    }
  }

  .chart-dom {
    width: 100%;
    height: 100%;
  }
}

.table-body {
  padding: 12px 16px;
}

@media (max-width: 768px) {
  .comprehensive-container {
    padding: 0 10px 10px;

    .kpi-card {
      padding: 14px;

      .kpi-value { font-size: 22px; }
      .kpi-icon-bg { width: 40px; height: 40px; font-size: 20px; right: 14px; top: 14px; }
    }

    .chart-card .chart-body { height: 280px; }
  }
}
</style>
