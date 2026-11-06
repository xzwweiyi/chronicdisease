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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header"><span>筛查类型分布</span></div>
          <div ref="typeChart" style="height:350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header"><span>筛查结果统计</span></div>
          <div ref="resultChart" style="height:350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header"><span>筛查趋势</span></div>
          <div ref="trendChart" style="height:350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'StatisticsScreeningData',
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
      queryParams: {}
    }
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    initCharts() {
      this.$nextTick(() => {
        this.initTypeChart()
        this.initResultChart()
        this.initTrendChart()
      })
    },
    initTypeChart() {
      const chart = echarts.init(this.$refs.typeChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, data: ['院外筛查', '体检筛查', '门诊机会性筛查', '主动申请筛查'] },
        series: [{
          type: 'pie', radius: ['40%', '70%'], avoidLabelOverlap: false,
          label: { show: true, formatter: '{b}: {c} ({d}%)' },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          data: [
            { value: 120, name: '院外筛查' },
            { value: 80, name: '体检筛查' },
            { value: 95, name: '门诊机会性筛查' },
            { value: 55, name: '主动申请筛查' }
          ]
        }]
      })
    },
    initResultChart() {
      const chart = echarts.init(this.$refs.resultChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: ['高血压', '糖尿病', '脑卒中', '冠心病', '慢阻肺'] },
        yAxis: { type: 'value' },
        series: [{
          name: '阳性', type: 'bar', data: [45, 32, 18, 22, 15], itemStyle: { color: '#f56c6c' }
        }, {
          name: '阴性', type: 'bar', data: [80, 65, 55, 48, 40], itemStyle: { color: '#67c23a' }
        }]
      })
    },
    initTrendChart() {
      const chart = echarts.init(this.$refs.trendChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['院外筛查', '门诊筛查', '体检筛查'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: ['2026-01', '2026-02', '2026-03', '2026-04', '2026-05', '2026-06'] },
        yAxis: { type: 'value' },
        series: [
          { name: '院外筛查', type: 'line', data: [25, 28, 32, 30, 35, 38], smooth: true },
          { name: '门诊筛查', type: 'line', data: [15, 18, 20, 22, 19, 24], smooth: true },
          { name: '体检筛查', type: 'line', data: [10, 13, 15, 12, 18, 20], smooth: true }
        ]
      })
    },
    handleQuery() {
      this.initCharts()
    },
    resetQuery() {
      this.dateRange = []
      this.initCharts()
    }
  }
}
</script>

<style scoped>
.chart-card { margin-bottom: 0; }
</style>
