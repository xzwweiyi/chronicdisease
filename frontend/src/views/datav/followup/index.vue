<template>
  <div class="app-container">
    <h3 class="page-title">慢病随访数据分析</h3>

    <!-- KPI卡片 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">计划随访数</div>
          <div class="kpi-value">{{ summary.plannedCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">已完成随访</div>
          <div class="kpi-value" style="color:#67C23A">{{ summary.completedCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">随访完成率</div>
          <div class="kpi-value" style="color:#409EFF">{{ summary.completionRate }}%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-label">逾期未随访</div>
          <div class="kpi-value" style="color:#F56C6C">{{ summary.overdueCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表行1 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>各病种随访完成率</span></div>
          <div class="rate-list">
            <div class="rate-item" v-for="item in diseaseFollowUpData" :key="item.disease">
              <div class="rate-header">
                <span>{{ item.disease }}</span>
                <span :style="{ color: item.rate >= 90 ? '#67C23A' : item.rate >= 80 ? '#409EFF' : '#E6A23C' }">{{ item.rate }}%</span>
              </div>
              <el-progress :percentage="item.rate" :color="rateColor(item.rate)" :stroke-width="18" />
              <div class="rate-detail">应随访 {{ item.planned }} / 已完成 {{ item.completed }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>各机构随访完成率</span></div>
          <div class="rate-list">
            <div class="rate-item" v-for="item in institutionFollowUpData" :key="item.name">
              <div class="rate-header">
                <span>{{ item.name }}</span>
                <span :style="{ color: item.rate >= 90 ? '#67C23A' : item.rate >= 80 ? '#409EFF' : '#E6A23C' }">{{ item.rate }}%</span>
              </div>
              <el-progress :percentage="item.rate" :color="rateColor(item.rate)" :stroke-width="18" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 月度随访趋势表 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>月度随访统计</span></div>
          <el-table :data="monthlyFollowUpData" border stripe size="small">
            <el-table-column prop="month" label="月份" width="100" align="center" />
            <el-table-column prop="planned" label="计划随访" width="120" align="center" sortable />
            <el-table-column prop="completed" label="已完成" width="120" align="center" sortable />
            <el-table-column prop="rate" label="完成率" width="120" align="center" sortable>
              <template slot-scope="scope">
                <el-progress :percentage="Number(scope.row.rate)" :stroke-width="14"
                  :color="Number(scope.row.rate) >= 90 ? '#67C23A' : Number(scope.row.rate) >= 80 ? '#409EFF' : '#E6A23C'" />
              </template>
            </el-table-column>
            <el-table-column prop="phoneCount" label="电话随访" width="100" align="center" />
            <el-table-column prop="faceCount" label="面对面随访" width="110" align="center" />
            <el-table-column prop="homeCount" label="上门随访" width="100" align="center" />
            <el-table-column prop="onlineCount" label="在线随访" width="100" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'DatavFollowUp',
  data() {
    return {
      summary: { plannedCount: '12,580', completedCount: '10,210', completionRate: 81.2, overdueCount: '1,850' },
      diseaseFollowUpData: [
        { disease: '高血压', planned: 5200, completed: 4620, rate: 88.8 },
        { disease: '糖尿病', planned: 3800, completed: 3150, rate: 82.9 },
        { disease: '冠心病', planned: 1850, completed: 1520, rate: 82.2 },
        { disease: '慢阻肺', planned: 980, completed: 720, rate: 73.5 },
        { disease: '脑卒中', planned: 750, completed: 560, rate: 74.7 }
      ],
      institutionFollowUpData: [
        { name: '城关镇卫生院', rate: 92.3 },
        { name: '柳林镇卫生院', rate: 85.7 },
        { name: '张家川镇卫生院', rate: 88.1 },
        { name: '县人民医院', rate: 78.5 },
        { name: '东关村卫生室', rate: 95.6 },
        { name: '西关村卫生室', rate: 91.2 }
      ],
      monthlyFollowUpData: [
        { month: '1月', planned: 1020, completed: 850, rate: '83.3', phoneCount: 420, faceCount: 280, homeCount: 90, onlineCount: 60 },
        { month: '2月', planned: 950, completed: 780, rate: '82.1', phoneCount: 380, faceCount: 260, homeCount: 80, onlineCount: 60 },
        { month: '3月', planned: 1080, completed: 920, rate: '85.2', phoneCount: 450, faceCount: 300, homeCount: 100, onlineCount: 70 },
        { month: '4月', planned: 1050, completed: 880, rate: '83.8', phoneCount: 430, faceCount: 290, homeCount: 85, onlineCount: 75 },
        { month: '5月', planned: 1100, completed: 910, rate: '82.7', phoneCount: 460, faceCount: 280, homeCount: 95, onlineCount: 75 },
        { month: '6月', planned: 1150, completed: 980, rate: '85.2', phoneCount: 480, faceCount: 310, homeCount: 100, onlineCount: 90 }
      ]
    }
  },
  methods: {
    rateColor(rate) { return rate >= 90 ? '#67C23A' : rate >= 80 ? '#409EFF' : '#E6A23C' }
  }
}
</script>

<style scoped>
.page-title { margin: 0 0 20px; font-size: 20px; color: #303133; }
.kpi-row { margin-bottom: 20px; }
.kpi-card { text-align: center; }
.kpi-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.kpi-value { font-size: 28px; font-weight: 700; color: #303133; margin-bottom: 4px; }
.chart-row { margin-bottom: 20px; }
.card-header { font-weight: 600; color: #303133; }
.rate-list { padding: 10px 20px; }
.rate-item { margin-bottom: 20px; }
.rate-header { display: flex; justify-content: space-between; margin-bottom: 6px; font-size: 14px; color: #303133; }
.rate-detail { font-size: 12px; color: #909399; margin-top: 4px; }
</style>
