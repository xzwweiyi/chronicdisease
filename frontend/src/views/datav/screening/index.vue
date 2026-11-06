<template>
  <div class="app-container">
    <h3 class="page-title">慢病筛查数据分析</h3>

    <!-- KPI卡片 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">总筛查人次</div>
          <div class="kpi-value">18,520</div>
          <div class="kpi-sub">同比 +12.5%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">阳性检出数</div>
          <div class="kpi-value" style="color:#F56C6C">4,263</div>
          <div class="kpi-sub">检出率 23.0%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">确诊入组数</div>
          <div class="kpi-value">2,891</div>
          <div class="kpi-sub">入组率 67.8%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">筛查覆盖率</div>
          <div class="kpi-value">76.4%</div>
          <div class="kpi-sub">目标 ≥80%</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 筛查来源统计 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>筛查来源统计</span></div>
          <div class="source-stats">
            <div class="source-item" v-for="item in sourceData" :key="item.name">
              <div class="source-header">
                <span class="source-name">{{ item.name }}</span>
                <span class="source-count">{{ item.count }}</span>
              </div>
              <el-progress :percentage="item.percent" :color="item.color" :stroke-width="18" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>月度筛查趋势</span></div>
          <div class="chart-container">
            <div class="bar-chart">
              <div class="bar-item" v-for="item in monthlyData" :key="item.month">
                <div class="bar-value">{{ item.count }}</div>
                <div class="bar" :style="{ height: (item.count / 2800 * 180) + 'px', background: item.color }" />
                <div class="bar-label">{{ item.month }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 病种筛查统计表 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>各病种筛查统计</span></div>
          <el-table :data="diseaseScreeningData" border stripe size="small">
            <el-table-column prop="disease" label="病种" width="120" align="center" />
            <el-table-column prop="screeningCount" label="筛查人次" width="120" align="center" sortable />
            <el-table-column prop="positiveCount" label="阳性数" width="100" align="center" sortable />
            <el-table-column prop="positiveRate" label="检出率" width="100" align="center" sortable>
              <template slot-scope="scope">
                <el-tag :type="scope.row.positiveRate > 25 ? 'danger' : 'warning'" size="small">{{ scope.row.positiveRate }}%</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="enrollCount" label="入组数" width="100" align="center" sortable />
            <el-table-column prop="enrollRate" label="入组率" width="100" align="center" sortable>
              <template slot-scope="scope">
                <el-progress :percentage="scope.row.enrollRate" :stroke-width="14" :show-text="true" />
              </template>
            </el-table-column>
            <el-table-column prop="highRiskCount" label="高危人数" width="100" align="center" />
            <el-table-column prop="avgAge" label="平均年龄" width="100" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'DatavScreening',
  data() {
    return {
      sourceData: [
        { name: '机构外筛查', count: '6,820', percent: 37, color: '#409EFF' },
        { name: '体检筛查', count: '4,920', percent: 27, color: '#67C23A' },
        { name: '就诊机会性筛查', count: '3,850', percent: 21, color: '#E6A23C' },
        { name: '患者主动申请', count: '2,930', percent: 15, color: '#F56C6C' }
      ],
      monthlyData: [
        { month: '1月', count: 1200, color: '#409EFF' },
        { month: '2月', count: 950, color: '#409EFF' },
        { month: '3月', count: 1580, color: '#409EFF' },
        { month: '4月', count: 1320, color: '#409EFF' },
        { month: '5月', count: 1860, color: '#409EFF' },
        { month: '6月', count: 2100, color: '#67C23A' },
        { month: '7月', count: 1750, color: '#409EFF' },
        { month: '8月', count: 1520, color: '#409EFF' },
        { month: '9月', count: 1980, color: '#409EFF' },
        { month: '10月', count: 2260, color: '#409EFF' },
        { month: '11月', count: 2450, color: '#409EFF' },
        { month: '12月', count: 2600, color: '#67C23A' }
      ],
      diseaseScreeningData: [
        { disease: '高血压', screeningCount: 7520, positiveCount: 1850, positiveRate: 24.6, enrollCount: 1320, enrollRate: 71.4, highRiskCount: 430, avgAge: 56.3 },
        { disease: '糖尿病', screeningCount: 5200, positiveCount: 1210, positiveRate: 23.3, enrollCount: 820, enrollRate: 67.8, highRiskCount: 385, avgAge: 54.8 },
        { disease: '冠心病', screeningCount: 2800, positiveCount: 560, positiveRate: 20.0, enrollCount: 380, enrollRate: 67.9, highRiskCount: 180, avgAge: 62.1 },
        { disease: '慢阻肺', screeningCount: 1800, positiveCount: 380, positiveRate: 21.1, enrollCount: 221, enrollRate: 58.2, highRiskCount: 120, avgAge: 63.5 },
        { disease: '脑卒中', screeningCount: 1200, positiveCount: 263, positiveRate: 21.9, enrollCount: 150, enrollRate: 57.0, highRiskCount: 95, avgAge: 65.2 }
      ]
    }
  }
}
</script>

<style scoped>
.page-title { margin: 0 0 20px; font-size: 20px; color: #303133; }
.kpi-row { margin-bottom: 20px; }
.kpi-card { text-align: center; }
.kpi-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.kpi-value { font-size: 28px; font-weight: 700; color: #303133; margin-bottom: 4px; }
.kpi-sub { font-size: 12px; color: #909399; }
.chart-row { margin-bottom: 20px; }
.card-header { font-weight: 600; color: #303133; }
.source-stats { padding: 20px; }
.source-item { margin-bottom: 24px; }
.source-header { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 14px; }
.source-name { color: #303133; }
.source-count { font-weight: 600; color: #303133; }
.chart-container { min-height: 300px; display: flex; align-items: flex-end; justify-content: center; }
.bar-chart { display: flex; align-items: flex-end; justify-content: space-around; width: 100%; height: 250px; padding-bottom: 30px; }
.bar-item { display: flex; flex-direction: column; align-items: center; flex: 1; }
.bar-value { font-size: 10px; color: #909399; margin-bottom: 4px; }
.bar { width: 24px; border-radius: 4px 4px 0 0; min-height: 4px; transition: height 0.3s; }
.bar-label { font-size: 10px; color: #909399; margin-top: 6px; }
</style>
