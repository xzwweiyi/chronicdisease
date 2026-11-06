<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-select v-model="queryParams.region" placeholder="请选择区域" clearable size="small">
          <el-option label="全部" value="" />
          <el-option label="城关区" value="chengguan" />
          <el-option label="七里河区" value="qilihe" />
          <el-option label="西固区" value="xigu" />
          <el-option label="安宁区" value="anning" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value" style="color:#409eff;">1,286</div>
            <div class="stat-label">在管患者总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value" style="color:#67c23a;">92.5%</div>
            <div class="stat-label">管理率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value" style="color:#e6a23c;">85.3%</div>
            <div class="stat-label">规范管理率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value" style="color:#f56c6c;">78.9%</div>
            <div class="stat-label">血压/血糖控制率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header"><span>各区域在管患者分布</span></div>
          <div ref="regionChart" style="height:350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header"><span>慢病类型分布</span></div>
          <div ref="diseaseChart" style="height:350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header"><span>管理效果趋势</span></div>
          <div ref="trendChart" style="height:350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'StatisticsMonitorData',
  data() {
    return {
      showSearch: true,
      dateRange: [],
      pickerOptions: {
        shortcuts: [
          { text: '本周', onClick(picker) { const end = new Date(); const start = new Date(); start.setTime(start.getTime() - 3600 * 1000 * 24 * 7); picker.$emit('pick', [start, end]) } },
          { text: '本月', onClick(picker) { const end = new Date(); const start = new Date(); start.setMonth(start.getMonth() - 1); picker.$emit('pick', [start, end]) } },
          { text: '近三月', onClick(picker) { const end = new Date(); const start = new Date(); start.setMonth(start.getMonth() - 3); picker.$emit('pick', [start, end]) } }
        ]
      },
      queryParams: { region: '' }
    }
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    initCharts() {
      this.$nextTick(() => {
        this.initRegionChart()
        this.initDiseaseChart()
        this.initTrendChart()
      })
    },
    initRegionChart() {
      const chart = echarts.init(this.$refs.regionChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: ['城关区', '七里河区', '西固区', '安宁区', '红古区', '榆中县'] },
        yAxis: { type: 'value', name: '人数' },
        series: [{
          type: 'bar', name: '在管人数',
          data: [320, 260, 210, 185, 145, 166],
          itemStyle: { borderRadius: [4, 4, 0, 0] }
        }]
      })
    },
    initDiseaseChart() {
      const chart = echarts.init(this.$refs.diseaseChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, data: ['高血压', '糖尿病', '脑卒中', '冠心病', '慢阻肺'] },
        series: [{
          type: 'pie', radius: ['40%', '70%'],
          label: { formatter: '{b}\n{d}%' },
          data: [
            { value: 420, name: '高血压' },
            { value: 310, name: '糖尿病' },
            { value: 180, name: '脑卒中' },
            { value: 220, name: '冠心病' },
            { value: 156, name: '慢阻肺' }
          ]
        }]
      })
    },
    initTrendChart() {
      const chart = echarts.init(this.$refs.trendChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['管理率', '规范管理率', '控制率'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: ['2026-01', '2026-02', '2026-03', '2026-04', '2026-05', '2026-06'] },
        yAxis: { type: 'value', name: '%', min: 60, max: 100 },
        series: [
          { name: '管理率', type: 'line', data: [88, 89, 90, 91, 92, 92.5], smooth: true },
          { name: '规范管理率', type: 'line', data: [80, 81, 82, 83, 84, 85.3], smooth: true },
          { name: '控制率', type: 'line', data: [73, 74, 75, 76, 77, 78.9], smooth: true }
        ]
      })
    },
    handleQuery() {
      this.initCharts()
    },
    resetQuery() {
      this.dateRange = []
      this.queryParams.region = ''
      this.initCharts()
    }
  }
}
</script>

<style scoped>
.stat-cards { margin-bottom: 0; }
.stat-item { text-align: center; padding: 10px 0; }
.stat-value { font-size: 32px; font-weight: bold; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
.chart-card { margin-bottom: 0; }
</style>
