<template>
  <div class="app-container">
    <h3 class="page-title">慢病综合数据分析</h3>

    <!-- KPI卡片 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-blue">
          <div class="kpi-content">
            <div class="kpi-icon"><i class="el-icon-user-solid" /></div>
            <div class="kpi-info">
              <div class="kpi-label">总患者数</div>
              <div class="kpi-value">{{ kpiData.totalPatients }}</div>
              <div class="kpi-sub">较上月 <span class="up">+3.2%</span></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-green">
          <div class="kpi-content">
            <div class="kpi-icon"><i class="el-icon-plus" /></div>
            <div class="kpi-info">
              <div class="kpi-label">本月新增</div>
              <div class="kpi-value">{{ kpiData.newPatients }}</div>
              <div class="kpi-sub">较上月 <span class="down">-5.1%</span></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-orange">
          <div class="kpi-content">
            <div class="kpi-icon"><i class="el-icon-circle-check" /></div>
            <div class="kpi-info">
              <div class="kpi-label">随访完成率</div>
              <div class="kpi-value">{{ kpiData.followUpRate }}%</div>
              <div class="kpi-sub">较上月 <span class="up">+1.8%</span></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card kpi-purple">
          <div class="kpi-content">
            <div class="kpi-icon"><i class="el-icon-data-line" /></div>
            <div class="kpi-info">
              <div class="kpi-label">控制达标率</div>
              <div class="kpi-value">{{ kpiData.controlRate }}%</div>
              <div class="kpi-sub">较上月 <span class="up">+0.5%</span></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>慢病趋势分析（近12个月）</span></div>
          <div class="chart-container">
            <div class="mock-chart mock-line-chart">
              <div class="chart-axis chart-y-axis">
                <span>5000</span><span>4000</span><span>3000</span><span>2000</span><span>1000</span><span>0</span>
              </div>
              <div class="chart-body">
                <div class="mock-line hypertension" :style="{height: '70%', bottom: '20%'}" title="高血压">
                  <span class="line-label">高血压</span>
                </div>
                <div class="mock-line diabetes" :style="{height: '50%', bottom: '15%'}" title="糖尿病">
                  <span class="line-label">糖尿病</span>
                </div>
                <div class="mock-line chd" :style="{height: '30%', bottom: '10%'}" title="冠心病">
                  <span class="line-label">冠心病</span>
                </div>
              </div>
              <div class="chart-x-axis">
                <span v-for="m in months" :key="m">{{ m }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>慢病病种分布</span></div>
          <div class="chart-container">
            <div class="mock-chart mock-donut-chart">
              <svg viewBox="0 0 200 200" width="200" height="200">
                <circle cx="100" cy="100" r="80" fill="none" stroke="#eee" stroke-width="30" />
                <circle cx="100" cy="100" r="80" fill="none" stroke="#F56C6C" stroke-width="30"
                  stroke-dasharray="201 502" stroke-dashoffset="0" transform="rotate(-90 100 100)" />
                <circle cx="100" cy="100" r="80" fill="none" stroke="#E6A23C" stroke-width="30"
                  stroke-dasharray="150 502" stroke-dashoffset="-201" transform="rotate(-90 100 100)" />
                <circle cx="100" cy="100" r="80" fill="none" stroke="#409EFF" stroke-width="30"
                  stroke-dasharray="80 502" stroke-dashoffset="-351" transform="rotate(-90 100 100)" />
                <circle cx="100" cy="100" r="80" fill="none" stroke="#67C23A" stroke-width="30"
                  stroke-dasharray="40 502" stroke-dashoffset="-431" transform="rotate(-90 100 100)" />
                <circle cx="100" cy="100" r="80" fill="none" stroke="#909399" stroke-width="30"
                  stroke-dasharray="30 502" stroke-dashoffset="-471" transform="rotate(-90 100 100)" />
                <text x="100" y="95" text-anchor="middle" font-size="16" font-weight="bold" fill="#303133">2.45万</text>
                <text x="100" y="115" text-anchor="middle" font-size="12" fill="#909399">总患者数</text>
              </svg>
              <div class="donut-legend">
                <div class="legend-item"><span class="dot" style="background:#F56C6C" /> 高血压 40%</div>
                <div class="legend-item"><span class="dot" style="background:#E6A23C" /> 糖尿病 30%</div>
                <div class="legend-item"><span class="dot" style="background:#409EFF" /> 冠心病 16%</div>
                <div class="legend-item"><span class="dot" style="background:#67C23A" /> 慢阻肺 8%</div>
                <div class="legend-item"><span class="dot" style="background:#909399" /> 脑卒中 6%</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 机构对比表格 -->
    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>机构管理对比</span></div>
          <el-table :data="institutionData" border stripe size="small">
            <el-table-column prop="name" label="机构名称" min-width="160" align="center" />
            <el-table-column prop="totalPatients" label="管理患者数" width="120" align="center" sortable />
            <el-table-column prop="followUpRate" label="随访完成率" width="120" align="center" sortable>
              <template slot-scope="scope">
                <el-progress :percentage="scope.row.followUpRate" :color="progressColor(scope.row.followUpRate)" :stroke-width="16" />
              </template>
            </el-table-column>
            <el-table-column prop="controlRate" label="控制达标率" width="120" align="center" sortable>
              <template slot-scope="scope">
                <el-progress :percentage="scope.row.controlRate" :color="progressColor(scope.row.controlRate)" :stroke-width="16" />
              </template>
            </el-table-column>
            <el-table-column prop="transferCount" label="转诊数" width="80" align="center" />
            <el-table-column prop="alertCount" label="预警数" width="80" align="center">
              <template slot-scope="scope">
                <span :style="{ color: scope.row.alertCount > 10 ? '#F56C6C' : '#67C23A' }">{{ scope.row.alertCount }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'DatavOverview',
  data() {
    return {
      months: ['7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月', '4月', '5月', '6月'],
      kpiData: { totalPatients: '24,528', newPatients: '386', followUpRate: 87.6, controlRate: 68.3 },
      institutionData: [
        { name: '城关镇卫生院', totalPatients: 5820, followUpRate: 92.3, controlRate: 71.5, transferCount: 45, alertCount: 8 },
        { name: '柳林镇卫生院', totalPatients: 4150, followUpRate: 85.7, controlRate: 66.2, transferCount: 32, alertCount: 15 },
        { name: '张家川镇卫生院', totalPatients: 3680, followUpRate: 88.1, controlRate: 69.8, transferCount: 28, alertCount: 6 },
        { name: '县人民医院', totalPatients: 7250, followUpRate: 78.5, controlRate: 62.4, transferCount: 120, alertCount: 23 },
        { name: '东关村卫生室', totalPatients: 1850, followUpRate: 95.6, controlRate: 75.3, transferCount: 8, alertCount: 2 },
        { name: '西关村卫生室', totalPatients: 1620, followUpRate: 91.2, controlRate: 72.1, transferCount: 6, alertCount: 3 }
      ]
    }
  },
  methods: {
    progressColor(rate) { return rate >= 90 ? '#67C23A' : rate >= 75 ? '#409EFF' : rate >= 60 ? '#E6A23C' : '#F56C6C' }
  }
}
</script>

<style scoped>
.page-title { margin: 0 0 20px; font-size: 20px; color: #303133; }
.kpi-row { margin-bottom: 20px; }
.kpi-card { border-left: 4px solid; }
.kpi-card.kpi-blue { border-left-color: #409EFF; }
.kpi-card.kpi-green { border-left-color: #67C23A; }
.kpi-card.kpi-orange { border-left-color: #E6A23C; }
.kpi-card.kpi-purple { border-left-color: #9B59B6; }
.kpi-content { display: flex; align-items: center; gap: 16px; }
.kpi-icon { font-size: 40px; color: #C0C4CC; }
.kpi-label { font-size: 14px; color: #909399; margin-bottom: 4px; }
.kpi-value { font-size: 28px; font-weight: 700; color: #303133; }
.kpi-sub { font-size: 12px; color: #909399; }
.kpi-sub .up { color: #F56C6C; }
.kpi-sub .down { color: #67C23A; }
.chart-row { margin-bottom: 20px; }
.card-header { font-weight: 600; color: #303133; }
.chart-container { min-height: 300px; display: flex; align-items: center; justify-content: center; }
.mock-line-chart { display: flex; width: 100%; height: 250px; position: relative; padding-left: 40px; padding-bottom: 30px; }
.chart-y-axis { display: flex; flex-direction: column; justify-content: space-between; font-size: 11px; color: #909399; position: absolute; left: 0; top: 0; bottom: 30px; }
.chart-body { flex: 1; display: flex; justify-content: space-around; align-items: flex-end; position: relative; padding: 10px 0; border-left: 1px solid #eee; border-bottom: 1px solid #eee; }
.mock-line { position: absolute; width: 100%; background: linear-gradient(0deg, transparent, currentColor); opacity: 0.2; border-radius: 4px 4px 0 0; }
.mock-line.hypertension { color: #F56C6C; }
.mock-line.diabetes { color: #E6A23C; }
.mock-line.chd { color: #409EFF; }
.line-label { position: absolute; top: -20px; right: 10px; font-size: 10px; white-space: nowrap; }
.chart-x-axis { display: flex; justify-content: space-around; font-size: 11px; color: #909399; position: absolute; bottom: 0; left: 40px; right: 0; }
.mock-donut-chart { display: flex; align-items: center; gap: 24px; }
.donut-legend { font-size: 13px; }
.legend-item { display: flex; align-items: center; gap: 8px; margin: 8px 0; }
.dot { display: inline-block; width: 12px; height: 12px; border-radius: 50%; }
.table-row { margin-bottom: 20px; }
</style>
