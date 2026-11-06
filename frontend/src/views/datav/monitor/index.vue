<template>
  <div class="app-container">
    <h3 class="page-title">慢病监管数据分析</h3>

    <!-- 预警统计卡片 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="8">
        <el-card shadow="hover" class="alert-card" style="border-left: 4px solid #F56C6C;">
          <div class="alert-title">红色预警</div>
          <div class="alert-count" style="color:#F56C6C;">{{ alertStats.red }}</div>
          <div class="alert-desc">需要立即处理</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="alert-card" style="border-left: 4px solid #E6A23C;">
          <div class="alert-title">黄色预警</div>
          <div class="alert-count" style="color:#E6A23C;">{{ alertStats.yellow }}</div>
          <div class="alert-desc">需近期关注</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="alert-card" style="border-left: 4px solid #409EFF;">
          <div class="alert-title">蓝色预警</div>
          <div class="alert-count" style="color:#409EFF;">{{ alertStats.blue }}</div>
          <div class="alert-desc">常规监测提醒</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 质量指标 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>逾期随访统计（按病种）</span></div>
          <el-table :data="overdueData" border stripe size="small">
            <el-table-column prop="disease" label="病种" width="100" align="center" />
            <el-table-column prop="totalFollowUp" label="应随访数" width="100" align="center" />
            <el-table-column prop="overdueCount" label="逾期数" width="100" align="center" sortable>
              <template slot-scope="scope">
                <span style="color:#F56C6C;font-weight:600;">{{ scope.row.overdueCount }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="overdueRate" label="逾期率" width="100" align="center" sortable>
              <template slot-scope="scope">
                <el-tag :type="scope.row.overdueRate > 15 ? 'danger' : 'warning'" size="small">{{ scope.row.overdueRate }}%</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="avgOverdueDays" label="平均逾期天数" width="120" align="center" sortable />
            <el-table-column prop="mostOverdueDept" label="逾期最多机构" min-width="160" align="center" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>质量管理指标</span></div>
          <div class="quality-metrics">
            <div class="metric-item" v-for="item in qualityMetrics" :key="item.label">
              <div class="metric-header">
                <span class="metric-label">{{ item.label }}</span>
                <span class="metric-value" :style="{ color: item.status === 'normal' ? '#67C23A' : item.status === 'warning' ? '#E6A23C' : '#F56C6C' }">
                  {{ item.value }}{{ item.unit }}
                </span>
              </div>
              <div class="metric-target">目标值: {{ item.target }}{{ item.unit }}</div>
              <el-progress :percentage="item.percent" :color="item.status === 'normal' ? '#67C23A' : '#E6A23C'" :stroke-width="14" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 机构监管对比 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>机构监管指标对比</span></div>
          <el-table :data="institutionMonitorData" border stripe size="small">
            <el-table-column prop="name" label="机构名称" min-width="160" align="center" />
            <el-table-column prop="patientCount" label="管理患者" width="100" align="center" sortable />
            <el-table-column prop="followUpRate" label="随访率(%)" width="100" align="center" sortable>
              <template slot-scope="scope">
                <span :style="{ color: scope.row.followUpRate >= 90 ? '#67C23A' : scope.row.followUpRate >= 80 ? '#409EFF' : '#E6A23C' }">{{ scope.row.followUpRate }}%</span>
              </template>
            </el-table-column>
            <el-table-column prop="controlRate" label="控制率(%)" width="100" align="center" sortable />
            <el-table-column prop="alertCount" label="预警数" width="80" align="center" />
            <el-table-column prop="transferRate" label="上转率(%)" width="100" align="center" />
            <el-table-column prop="complaintCount" label="投诉数" width="80" align="center" />
            <el-table-column prop="satisfaction" label="满意度" width="100" align="center" sortable />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'DatavMonitor',
  data() {
    return {
      alertStats: { red: 58, yellow: 203, blue: 512 },
      overdueData: [
        { disease: '高血压', totalFollowUp: 5200, overdueCount: 580, overdueRate: 11.2, avgOverdueDays: 5.3, mostOverdueDept: '县人民医院' },
        { disease: '糖尿病', totalFollowUp: 3800, overdueCount: 650, overdueRate: 17.1, avgOverdueDays: 7.8, mostOverdueDept: '柳林镇卫生院' },
        { disease: '冠心病', totalFollowUp: 1850, overdueCount: 330, overdueRate: 17.8, avgOverdueDays: 6.2, mostOverdueDept: '县人民医院' },
        { disease: '慢阻肺', totalFollowUp: 980, overdueCount: 260, overdueRate: 26.5, avgOverdueDays: 9.1, mostOverdueDept: '张家川镇卫生院' },
        { disease: '脑卒中', totalFollowUp: 750, overdueCount: 190, overdueRate: 25.3, avgOverdueDays: 8.5, mostOverdueDept: '柳林镇卫生院' }
      ],
      qualityMetrics: [
        { label: '随访及时率', value: 85.3, unit: '%', target: '90', percent: 85, status: 'warning' },
        { label: '数据完整率', value: 92.1, unit: '%', target: '95', percent: 92, status: 'normal' },
        { label: '血压控制率', value: 68.5, unit: '%', target: '70', percent: 68, status: 'warning' },
        { label: '血糖控制率', value: 62.3, unit: '%', target: '65', percent: 62, status: 'warning' },
        { label: '规范管理率', value: 78.9, unit: '%', target: '80', percent: 79, status: 'warning' },
        { label: '患者满意度', value: 94.2, unit: '%', target: '90', percent: 94, status: 'normal' }
      ],
      institutionMonitorData: [
        { name: '城关镇卫生院', patientCount: 5820, followUpRate: 92.3, controlRate: 71.5, alertCount: 45, transferRate: 8.2, complaintCount: 1, satisfaction: 95.6 },
        { name: '柳林镇卫生院', patientCount: 4150, followUpRate: 85.7, controlRate: 66.2, alertCount: 78, transferRate: 12.5, complaintCount: 3, satisfaction: 89.2 },
        { name: '张家川镇卫生院', patientCount: 3680, followUpRate: 88.1, controlRate: 69.8, alertCount: 52, transferRate: 9.8, complaintCount: 2, satisfaction: 91.3 },
        { name: '县人民医院', patientCount: 7250, followUpRate: 78.5, controlRate: 62.4, alertCount: 156, transferRate: 18.6, complaintCount: 5, satisfaction: 85.7 },
        { name: '东关村卫生室', patientCount: 1850, followUpRate: 95.6, controlRate: 75.3, alertCount: 12, transferRate: 5.2, complaintCount: 0, satisfaction: 98.2 }
      ]
    }
  }
}
</script>

<style scoped>
.page-title { margin: 0 0 20px; font-size: 20px; color: #303133; }
.kpi-row { margin-bottom: 20px; }
.alert-card { text-align: center; }
.alert-title { font-size: 15px; color: #303133; margin-bottom: 8px; }
.alert-count { font-size: 36px; font-weight: 700; }
.alert-desc { font-size: 12px; color: #909399; margin-top: 4px; }
.chart-row { margin-bottom: 20px; }
.card-header { font-weight: 600; color: #303133; }
.quality-metrics { padding: 10px 20px; }
.metric-item { margin-bottom: 18px; }
.metric-header { display: flex; justify-content: space-between; margin-bottom: 4px; font-size: 14px; }
.metric-label { color: #303133; }
.metric-value { font-weight: 600; }
.metric-target { font-size: 12px; color: #C0C4CC; margin-bottom: 6px; }
</style>
