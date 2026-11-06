<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <div class="page-title-wrapper">
      <span class="page-title-bar"></span>
      <span class="page-title-text">工作台</span>
    </div>
    <div class="title-divider"></div>

    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="summary-cards">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon-circle">
            <svg-icon icon-class="people" />
          </div>
          <div class="stat-content">
            <div class="stat-label">管理患者总数</div>
            <div class="stat-value">
              <count-to :start-val="0" :end-val="summary.totalPatients || 24500" :duration="1500" />
            </div>
            <div class="stat-footer">
              <span class="stat-desc">较上年度</span>
              <span class="stat-divider">|</span>
              <span class="stat-change up">2.3% <i class="el-icon-top"></i></span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon-circle green">
            <svg-icon icon-class="form" />
          </div>
          <div class="stat-content">
            <div class="stat-label">本月随访完成率</div>
            <div class="stat-value">
              <count-to :start-val="0" :end-val="summary.followRate || 87.6" :duration="1500" :decimals="1" suffix="%" />
            </div>
            <div class="stat-footer">
              <span class="stat-desc">{{ summary.followCompleted || 2190 }}/{{ summary.followTotal || 2500 }}</span>
              <span class="stat-divider">|</span>
              <span class="stat-change up">5.2% <i class="el-icon-top"></i></span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon-circle orange">
            <svg-icon icon-class="guide" />
          </div>
          <div class="stat-content">
            <div class="stat-label">控制达标率</div>
            <div class="stat-value">
              <count-to :start-val="0" :end-val="summary.controlRate || 68.3" :duration="1500" :decimals="1" suffix="%" />
            </div>
            <div class="stat-footer">
              <span class="stat-desc">较上年度</span>
              <span class="stat-divider">|</span>
              <span class="stat-change up">3.2% <i class="el-icon-top"></i></span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon-circle red">
            <svg-icon icon-class="documentation" />
          </div>
          <div class="stat-content">
            <div class="stat-label">待处理转诊</div>
            <div class="stat-value danger">
              <count-to :start-val="0" :end-val="summary.pendingReferrals || 28" :duration="1500" />
            </div>
            <div class="stat-footer">
              <span class="stat-desc">需立即处理</span>
              <span class="stat-divider">|</span>
              <span class="stat-change down">12.5% <i class="el-icon-bottom"></i></span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 中间区域：趋势分析 + 待办事项 -->
    <el-row :gutter="20" class="middle-row">
      <el-col :xs="24" :lg="17">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">慢病趋势分析（近12个月）</span>
            <el-radio-group v-model="trendPeriod" size="small" class="period-selector">
              <el-radio-button label="month">月度</el-radio-button>
              <el-radio-button label="quarter">季度</el-radio-button>
              <el-radio-button label="year">年度</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-body">
            <div ref="trendChart" class="chart-container"></div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="7">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">慢病病种分布</span>
          </div>
          <div class="chart-body chart-body-pie">
            <div ref="pieChart" class="chart-container"></div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 底部区域：重点关注患者 + 机构排名 -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">关注患者</span>
            <el-button type="text" size="small">更多</el-button>
          </div>
          <div class="focus-patient-list">
            <div v-for="(patient, index) in focusPatients" :key="index" class="patient-item">
              <div class="patient-rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</div>
              <div class="patient-avatar" :style="{ background: riskColor(patient.riskLevel) }">
                {{ patient.name ? patient.name.substring(0, 1) : '患' }}
              </div>
              <div class="patient-info">
                <div class="patient-name">
                  {{ patient.name }}
                  <el-tag :type="riskTagType(patient.riskLevel)" size="mini" effect="plain">
                    {{ patient.riskLevel }}
                  </el-tag>
                </div>
                <div class="patient-meta">
                  {{ patient.disease || '高血压' }} · {{ patient.gender || '男' }} {{ patient.age || 65 }}岁
                  <el-tag
                    v-if="patient.followStatus"
                    size="mini"
                    :type="patient.followStatus === '已完成' ? 'success' : 'info'"
                    effect="plain"
                    style="margin-left: 6px;"
                  >
                    {{ patient.followStatus }}
                  </el-tag>
                </div>
              </div>
              <span class="patient-date">{{ patient.lastFollowDate || '06-25' }}</span>
            </div>
            <el-empty v-if="!focusPatients.length" description="暂无重点关注患者" :image-size="60" />
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="chart-card">
          <div class="card-header">
            <span class="card-title">机构管理随访对比</span>
            <el-button type="text" size="small">TOP 10</el-button>
          </div>
          <div class="chart-body chart-body-bar">
            <div ref="barChart" class="chart-container"></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import { getDashboardData } from '@/api/dashboard'
import * as echarts from 'echarts'
require('echarts/theme/macarons')

export default {
  name: 'Dashboard',
  components: { CountTo },
  data() {
    return {
      loading: false,
      trendPeriod: 'month',
      summary: {},
      todoList: [],
      focusPatients: [],
      diseaseDistribution: [],
      trendData: {},
      institutionRanking: [],
      trendChart: null,
      pieChart: null,
      barChart: null
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    this.disposeCharts()
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getDashboardData()
        if (res.code === 200) {
          this.summary = res.summary || {}
          this.todoList = res.todoList || []
          this.focusPatients = res.focusPatients || []
          this.diseaseDistribution = res.diseaseDistribution || []
          this.trendData = res.trendAnalysis || {}
          this.institutionRanking = (res.institutionRanking && res.institutionRanking.institutions) || []
          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (e) {
        console.error('Failed to fetch dashboard data', e)
        // Fallback: show mock data from prototype
        this.setMockData()
      } finally {
        this.loading = false
      }
    },
    setMockData() {
      // Use prototype-style fallback data
      if (!this.summary || Object.keys(this.summary).length === 0) {
        this.summary = { totalPatients: '24500', followRate: 87.6, controlRate: 68.3, followCompleted: 2190, followTotal: 2500, pendingReferrals: 28 }
      }
      this.diseaseDistribution = this.diseaseDistribution.length === 0 ? [
        { name: '高血压', value: 9800 },
        { name: '糖尿病', value: 7350 },
        { name: '冠心病', value: 3920 },
        { name: '慢阻肺', value: 1960 },
        { name: '脑卒中', value: 1470 }
      ] : this.diseaseDistribution
      if (!this.trendData || !this.trendData.months) {
        this.trendData = {
          months: ['7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月', '4月', '5月', '6月'],
          series: [
            { name: '高血压', data: [3200, 3400, 3600, 3800, 4000, 4200, 4600, 4800, 5000, 5200, 5400, 5800] },
            { name: '糖尿病', data: [2400, 2500, 2600, 2700, 2800, 2900, 3100, 3300, 3500, 3700, 3900, 4100] },
            { name: '冠心病', data: [1200, 1300, 1350, 1400, 1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200] }
          ]
        }
      }
      if (this.focusPatients.length === 0) {
        this.focusPatients = [
          { name: '张建平', gender: '男', age: 65, disease: '高血压3级', riskLevel: '高危', lastFollowDate: '06-25' },
          { name: '李桂芳', gender: '女', age: 58, disease: '2型糖尿病', riskLevel: '高危', lastFollowDate: '06-23' },
          { name: '王建国', gender: '男', age: 72, disease: '冠心病不稳定型心绞痛', riskLevel: '高危', lastFollowDate: '06-18' },
          { name: '赵美兰', gender: '女', age: 45, disease: '高血压2级', riskLevel: '中危', lastFollowDate: '06-15' },
          { name: '孙志明', gender: '男', age: 55, disease: '糖尿病高血压', riskLevel: '中危', lastFollowDate: '06-10' }
        ]
      }
      if (this.institutionRanking.length === 0) {
        this.institutionRanking = [
          { name: '城关镇卫生院', completionRate: 92.3, patientCount: 5820 },
          { name: '柳林镇卫生院', completionRate: 85.7, patientCount: 4150 },
          { name: '张家川镇卫生院', completionRate: 88.1, patientCount: 3680 },
          { name: '县人民医院', completionRate: 78.5, patientCount: 7250 },
          { name: '东关村卫生室', completionRate: 95.6, patientCount: 1850 }
        ]
      }
      this.$nextTick(() => { this.initCharts() })
    },
    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.pieChart) this.pieChart.resize()
      if (this.barChart) this.barChart.resize()
    },
    initCharts() {
      this.initTrendChart()
      this.initPieChart()
      this.initBarChart()
    },
    disposeCharts() {
      if (this.trendChart) { this.trendChart.dispose(); this.trendChart = null }
      if (this.pieChart) { this.pieChart.dispose(); this.pieChart = null }
      if (this.barChart) { this.barChart.dispose(); this.barChart = null }
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      if (this.trendChart) this.trendChart.dispose()
      this.trendChart = echarts.init(this.$refs.trendChart, 'macarons')

      const months = this.trendData.months || []
      const series = (this.trendData.series || []).map(s => ({
        name: s.name,
        type: 'line',
        smooth: true,
        data: s.data,
        animationDuration: 1500
      }))

      this.trendChart.setOption({
        tooltip: { trigger: 'axis', padding: [8, 12] },
        legend: { bottom: 0, data: series.map(s => s.name) },
        grid: { left: '3%', right: '4%', bottom: '12%', top: '8%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: months },
        yAxis: { type: 'value' },
        series: series
      })
    },
    initPieChart() {
      if (!this.$refs.pieChart) return
      if (this.pieChart) this.pieChart.dispose()
      this.pieChart = echarts.init(this.$refs.pieChart, 'macarons')

      const pieData = this.diseaseDistribution.map(d => ({
        name: d.name,
        value: d.value
      }))

      this.pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, type: 'scroll' },
        series: [{
          type: 'pie',
          radius: ['45%', '70%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
          label: { show: true, formatter: '{b}\n{d}%' },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          data: pieData
        }]
      })
    },
    initBarChart() {
      if (!this.$refs.barChart) return
      if (this.barChart) this.barChart.dispose()
      this.barChart = echarts.init(this.$refs.barChart, 'macarons')

      const institutions = this.institutionRanking || []
      const names = institutions.map(i => i.name)
      const rates = institutions.map(i => i.completionRate || 0)
      const patients = institutions.map(i => i.patientCount || 0)

      this.barChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, padding: [8, 12] },
        legend: { bottom: 0, data: ['随访完成率(%)', '管理患者数'] },
        grid: { left: '3%', right: '4%', bottom: '12%', top: '8%', containLabel: true },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30, fontSize: 10 } },
        yAxis: [
          { type: 'value', name: '%', max: 100 },
          { type: 'value', name: '人' }
        ],
        series: [
          { name: '随访完成率(%)', type: 'bar', data: rates, yAxisIndex: 0,
            itemStyle: { color: '#2984F8', borderRadius: [4, 4, 0, 0] }, barWidth: '40%' },
          { name: '管理患者数', type: 'bar', data: patients, yAxisIndex: 1,
            itemStyle: { color: '#52c41a', borderRadius: [4, 4, 0, 0] }, barWidth: '40%' }
        ]
      })
    },
    riskColor(level) {
      if (level === '高危') return '#ff4d4f'
      if (level === '中危') return '#faad14'
      return '#3A8CFF'
    },
    riskTagType(level) {
      if (level === '高危') return 'danger'
      if (level === '中危') return 'warning'
      return 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
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

// 统计卡片
.summary-cards {
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border: 1px solid #F2F2F2;
  border-radius: 5px;
  padding: 20px 20px 16px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0px 0px 15px rgba(242, 242, 242, 1);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
  margin-bottom: 10px;
  position: relative;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0px 4px 16px rgba(0, 0, 0, 0.08);
  }

  .stat-icon-circle {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    background: rgba(24, 144, 255, 0.08);
    position: absolute;
    right: 20px;
    top: 20px;

    .svg-icon {
      font-size: 24px;
      color: #1890ff;
    }

    &.green {
      background: rgba(82, 196, 26, 0.08);
      .svg-icon { color: #52c41a; }
    }
    &.orange {
      background: rgba(250, 173, 20, 0.08);
      .svg-icon { color: #faad14; }
    }
    &.red {
      background: rgba(255, 77, 79, 0.08);
      .svg-icon { color: #ff4d4f; }
    }
  }

  .stat-content {
    flex: 1;
    min-width: 0;
    padding-right: 60px;

    .stat-label {
      font-size: 14px;
      color: #666;
      margin-bottom: 8px;
      font-family: '微软雅黑', 'PingFang SC', sans-serif;
    }

    .stat-value {
      font-size: 28px;
      font-weight: 400;
      color: #333;
      line-height: 1.2;
      font-family: 'Arial', sans-serif;

      &.danger {
        color: #FF0000;
      }
    }

    .stat-footer {
      margin-top: 8px;
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .stat-desc {
      font-size: 12px;
      color: #999;
    }

    .stat-divider {
      color: #E8E8E8;
      font-size: 12px;
    }

    .stat-change {
      font-size: 12px;
      &.up { color: #00CC33; }
      &.down { color: #FF0000; }
    }
  }
}

// 图表卡片
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
      font-weight: 400;
      color: #1E1E1E;
      font-family: '微软雅黑', 'PingFang SC', sans-serif;
    }

    .period-selector {
      ::v-deep .el-radio-button__inner {
        padding: 6px 14px;
      }
    }
  }

  .chart-body {
    padding: 16px;
    height: 350px;

    &.chart-body-pie {
      height: 380px;
    }

    &.chart-body-bar {
      height: 350px;
    }
  }

  .chart-container {
    width: 100%;
    height: 100%;
  }
}

// 关注患者列表
.focus-patient-list {
  padding: 4px 16px;
  max-height: 350px;
  overflow-y: auto;
}

.patient-item {
  display: flex;
  align-items: center;
  padding: 10px 4px;
  border-bottom: 1px solid #F5F5F5;
  gap: 12px;

  &:last-child {
    border-bottom: none;
  }

  .patient-rank {
    width: 24px;
    height: 24px;
    border-radius: 4px;
    color: #fff;
    font-size: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    background: #C0C4CC;

    &.rank-1 { background: #FF4D4F; }
    &.rank-2 { background: #FF7A45; }
    &.rank-3 { background: #FFA940; }
  }

  .patient-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    color: #fff;
    font-size: 13px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .patient-info {
    flex: 1;
    min-width: 0;
  }

  .patient-name {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .patient-meta {
    font-size: 12px;
    color: #999;
    margin-top: 2px;
  }

  .patient-date {
    font-size: 12px;
    color: #999;
    flex-shrink: 0;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 0 10px 10px;

    .stat-card {
      padding: 14px 14px 12px;

      .stat-value {
        font-size: 22px;
      }

      .stat-icon-circle {
        width: 40px;
        height: 40px;
        right: 14px;
        top: 14px;

        .svg-icon { font-size: 20px; }
      }
    }

    .chart-card .chart-body {
      height: 260px;

      &.chart-body-pie {
        height: 280px;
      }

      &.chart-body-bar {
        height: 260px;
      }
    }

    .patient-item .patient-rank { width: 20px; height: 20px; font-size: 10px; }
  }
}
</style>
