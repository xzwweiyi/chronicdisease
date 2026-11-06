<template>
  <div class="app-container">
    <el-page-header @back="goBack" :content="'路径详情: ' + pathway.projectName" />

    <el-tabs v-model="activeTab" type="border-card" style="margin-top: 15px">
      <!-- 基本信息 -->
      <el-tab-pane label="基本信息" name="info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="路径名称">{{ pathway.projectName }}</el-descriptions-item>
          <el-descriptions-item label="病种">{{ pathway.diseaseName }}</el-descriptions-item>
          <el-descriptions-item label="路径状态">
            <el-tag v-if="pathway.pathwayStatus === '0'" type="info">草稿</el-tag>
            <el-tag v-else-if="pathway.pathwayStatus === '1'" type="success">已发布</el-tag>
            <el-tag v-else-if="pathway.pathwayStatus === '2'" type="primary">进行中</el-tag>
            <el-tag v-else-if="pathway.pathwayStatus === '3'" type="danger">已关闭</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="管理患者数">{{ pathway.patientNum || 0 }}</el-descriptions-item>
          <el-descriptions-item label="目标患者数">{{ pathway.targetPatientNum || 0 }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ pathway.createTime }}</el-descriptions-item>
          <el-descriptions-item label="路径描述" :span="2">{{ pathway.researchSynopsis }}</el-descriptions-item>
          <el-descriptions-item label="退出标准" :span="2">{{ pathway.exitCriteria || '未设置' }}</el-descriptions-item>
        </el-descriptions>
      </el-tab-pane>

      <!-- 路径节点 -->
      <el-tab-pane label="路径节点" name="nodes">
        <el-timeline>
          <el-timeline-item v-for="node in nodes" :key="node.followId"
            :icon="node.isBaseline === 1 ? 'el-icon-star-on' : 'el-icon-more'"
            :type="node.isBaseline === 1 ? 'primary' : ''"
            :timestamp="'序号: ' + (node.nodeOrder || 0)">
            <el-card>
              <div slot="header" class="clearfix">
                <span><strong>{{ node.followName }}</strong></span>
                <el-tag v-if="node.isBaseline === 1" type="info" size="small" style="float:right">基线</el-tag>
                <el-tag v-else-if="node.nodeType === 'follow_up'" type="primary" size="small" style="float:right">随访</el-tag>
                <el-tag v-else-if="node.nodeType === 'assessment'" type="success" size="small" style="float:right">评估</el-tag>
                <el-tag v-else-if="node.nodeType === 'intervention'" type="warning" size="small" style="float:right">干预</el-tag>
                <el-tag v-else-if="node.nodeType === 'education'" type="info" size="small" style="float:right">宣教</el-tag>
                <el-tag v-else-if="node.nodeType === 'reminder'" type="danger" size="small" style="float:right">提醒</el-tag>
                <el-tag v-else size="small" style="float:right">医嘱</el-tag>
              </div>
              <div v-if="node.isBaseline !== 1">
                <p>间隔: {{ node.followInterval }} {{ node.followIntervalUnit == '1' ? '天' : node.followIntervalUnit == '2' ? '周' : '月' }}</p>
                <p>频次: {{ node.followFrequency == '1' ? '限制' + node.followTotal + '次' : '不限次' }}</p>
                <p v-if="node.reminderAdvanceDays > 0">提前提醒: {{ node.reminderAdvanceDays }}天</p>
              </div>
              <div>
                <p>表单: {{ (node.fmedicalForms && node.fmedicalForms.length) ? node.fmedicalForms.length + '个' : '未配置' }}</p>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-if="!nodes.length" description="暂无节点配置" />
      </el-tab-pane>

      <!-- 入组患者 -->
      <el-tab-pane label="入组患者" name="patients">
        <el-empty description="入组患者列表（建设中）" />
      </el-tab-pane>

      <!-- 执行情况 -->
      <el-tab-pane label="执行情况" name="execution">
        <el-empty description="路径执行跟踪（建设中）" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { getPathway } from '@/api/pathway/definition'
import { listNodes } from '@/api/pathway/node'

export default {
  name: 'PathwayDetail',
  data() {
    return {
      activeTab: 'info',
      pathwayId: null,
      pathway: {},
      nodes: []
    }
  },
  created() {
    this.pathwayId = this.$route.params.id
    this.loadData()
  },
  methods: {
    loadData() {
      getPathway(this.pathwayId).then(response => {
        this.pathway = response.data || {}
      })
      listNodes(this.pathwayId).then(response => {
        this.nodes = response.data || []
      })
    },
    goBack() {
      this.$router.push({ path: '/pathway/list' })
    }
  }
}
</script>
