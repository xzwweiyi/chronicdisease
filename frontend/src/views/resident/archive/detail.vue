<template>
  <div class="detail-container">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item @click.native="goBack">居民管理</el-breadcrumb-item>
      <el-breadcrumb-item>档案详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="detail-layout">
      <!-- 左侧边栏 -->
      <div class="left-sidebar">
        <div class="sidebar-card patient-card">
          <div class="patient-avatar">{{ detail.personName ? detail.personName.charAt(0) : '患' }}</div>
          <div class="patient-title">{{ detail.personName }} <span class="patient-age">{{ detail.age }}岁</span></div>
          <div class="patient-gender"><i class="el-icon-user" /> {{ detail.genderSourceValue === '0' ? '男' : detail.genderSourceValue === '1' ? '女' : (detail.genderSourceValue || '--') }}</div>
          <div class="patient-tags">
            <span v-if="detail.diseaseTypes" class="tag-disease">{{ detail.diseaseTypes }}</span>
            <span v-if="riskLevel" class="tag-risk">{{ riskLevel }}</span>
            <span v-if="managementLevel" class="tag-level">{{ managementLevel }}</span>
          </div>
          <div class="sidebar-divider" />
          <div class="sidebar-info">
            <div><span class="lbl">身份证号</span><span class="val">{{ detail.cardId }}</span></div>
            <div><span class="lbl">电话</span><span class="val">{{ detail.phoneNumber }}</span></div>
            <div><span class="lbl">档案编码</span><span class="val">{{ detail.archiveCode || detail.personId }}</span></div>
          </div>
        </div>

        <div class="sidebar-card">
          <div class="sidebar-section-title">健康概况</div>
          <div class="sidebar-divider" />
          <div class="sidebar-info">
            <div><span class="lbl">身高</span><span class="val">{{ healthProfile.height || '--' }}</span></div>
            <div><span class="lbl">体重</span><span class="val">{{ healthProfile.weight || '--' }}</span></div>
            <div><span class="lbl">血型</span><span class="val">{{ healthProfile.bloodType || '--' }}</span></div>
            <div><span class="lbl">过敏史</span><span class="val">{{ detail.allergyHistory || '无' }}</span></div>
            <div><span class="lbl">既往史</span><span class="val">{{ '--' }}</span></div>
            <div><span class="lbl">家族病史</span><span class="val">{{ detail.familyHistory || '无' }}</span></div>
          </div>
        </div>

        <div class="sidebar-card">
          <div class="sidebar-section-title">风险评估</div>
          <div class="sidebar-divider" />
          <div class="sidebar-info"><div><span class="lbl">风险等级</span><span class="val" style="color:#f56c6c">{{ riskLevel || '未评估' }}</span></div></div>
        </div>

        <div class="sidebar-card">
          <div class="sidebar-section-title">签约情况</div>
          <div class="sidebar-divider" />
          <div class="sidebar-info">
            <div><span class="lbl">服务包</span><span class="val">{{ contractInfo.servicePackage || '--' }}</span></div>
            <div><span class="lbl">签约日期</span><span class="val">{{ contractInfo.signDate || '--' }}</span></div>
            <div><span class="lbl">到期日期</span><span class="val">{{ contractInfo.expireDate || '--' }}</span></div>
            <div><span class="lbl">签约医生</span><span class="val">{{ detail.managingDoctor || '--' }}</span></div>
          </div>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="right-content">
        <el-tabs v-model="activeTab" type="border-card" @tab-click="onTabClick">
          <!-- 总览 -->
          <el-tab-pane label="总览" name="overview">
            <el-row :gutter="16">
              <el-col :span="12">
                <div class="content-panel">
                  <div class="panel-title">待办事项</div>
                  <div class="panel-body"><p v-if="!todoItems.length" style="color:#999">暂无待办事项</p><p v-for="(t,i) in todoItems" :key="i">【{{ t.type }}】{{ t.content }} <el-button size="mini" type="primary" plain>{{ t.action }}</el-button></p></div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="content-panel">
                  <div class="panel-title">预警</div>
                  <div class="panel-body"><p v-if="!warnings.length" style="color:#999">暂无预警信息</p><p v-for="(w,i) in warnings" :key="i" style="color:#f56c6c">【{{ w.type }}】{{ w.content }} <el-button size="mini" type="danger" plain>{{ w.action }}</el-button></p></div>
                </div>
              </el-col>
            </el-row>

            <div class="content-panel" style="margin-top:14px">
              <div class="panel-title">诊断与管理信息</div>
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="诊断名称">{{ detail.diseaseTypes || '--' }}</el-descriptions-item>
                <el-descriptions-item label="确诊医院">{{ '--' }}</el-descriptions-item>
                <el-descriptions-item label="确诊日期">{{ '--' }}</el-descriptions-item>
                <el-descriptions-item label="管理机构">{{ detail.managingInstitution || '--' }}</el-descriptions-item>
                <el-descriptions-item label="管理医生">{{ detail.managingDoctor || '--' }}</el-descriptions-item>
                <el-descriptions-item label="管理分级">{{ managementLevel || '--' }}</el-descriptions-item>
              </el-descriptions>
            </div>

            <div class="content-panel" style="margin-top:14px">
              <div class="panel-title">最近用药</div>
              <el-table :data="medications" border size="small" v-if="medications.length">
                <el-table-column prop="drug_name" label="药物名称" />
                <el-table-column prop="drug_spec" label="剂量" />
                <el-table-column prop="dosage_form" label="用法" />
                <el-table-column prop="days_supply" label="天数" width="80" align="center" />
              </el-table>
              <p v-else style="color:#999;text-align:center;padding:20px">暂无用药记录</p>
            </div>
          </el-tab-pane>

          <!-- 慢病健康指数 -->
          <el-tab-pane label="慢病健康指数" name="healthIndex">
            <el-row :gutter="16">
              <el-col :span="6" v-for="h in healthIndexCards" :key="h.label">
                <div class="health-card" :style="{ borderTopColor: h.color }">
                  <div class="health-label">{{ h.label }}</div>
                  <div class="health-value" :style="{ color: h.color }">{{ h.value }}</div>
                  <div class="health-unit">{{ h.unit }}</div>
                  <div class="health-status"><el-tag :type="h.statusType" size="mini">{{ h.status }}</el-tag></div>
                </div>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 诊疗记录 -->
          <el-tab-pane label="诊疗记录" name="medicalRecords">
            <el-timeline v-if="diagnosisList.length">
              <el-timeline-item v-for="(item, idx) in diagnosisList" :key="idx" :timestamp="parseTime(item.diagnosis_date, '{y}-{m}-{d}')" placement="top">
                <el-card shadow="hover">
                  <h4>{{ item.diagnosis_desc }} <el-tag size="mini">{{ item.department_name }}</el-tag></h4>
                  <p><b>诊断：</b>{{ item.condition_name }}</p>
                  <p><b>医生：</b>{{ item.final_check_doctor || '--' }}</p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
            <el-empty v-else description="暂无诊疗记录" />
          </el-tab-pane>

          <!-- 随访管理 -->
          <el-tab-pane label="随访管理" name="followUp">
            <div v-loading="followLoading">
              <div class="section-title"><span class="title-bar" />待随访节点</div>
              <el-table :data="followNodes.pending" border size="small" v-if="followNodes.pending.length">
                <el-table-column prop="name" label="随访节点" width="110" align="center" />
                <el-table-column prop="projectName" label="所属队列" min-width="180" show-overflow-tooltip />
                <el-table-column label="计划随访时间" width="130" align="center">
                  <template slot-scope="scope">{{ formatNodeDate(scope.row.lockVisitTime || scope.row.visitTime) }}</template>
                </el-table-column>
                <el-table-column label="状态" width="90" align="center">
                  <template slot-scope="scope">
                    <el-tag v-if="scope.row.overTimeStatus === '1'" type="danger" size="mini">已超窗</el-tag>
                    <el-tag v-else type="warning" size="mini">待随访</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="110" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" plain @click="goFollow(scope.row)">去随访</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-empty v-else description="暂无待随访节点" :image-size="60" />

              <div class="section-title" style="margin-top:20px"><span class="title-bar" />已完成随访节点</div>
              <el-table :data="followNodes.completed" border size="small" v-if="followNodes.completed.length">
                <el-table-column prop="name" label="随访节点" width="110" align="center" />
                <el-table-column prop="projectName" label="所属队列" min-width="180" show-overflow-tooltip />
                <el-table-column label="计划随访时间" width="130" align="center">
                  <template slot-scope="scope">{{ formatNodeDate(scope.row.lockVisitTime || scope.row.visitTime) }}</template>
                </el-table-column>
                <el-table-column label="完成时间" width="130" align="center">
                  <template slot-scope="scope">{{ formatNodeDate(scope.row.followCompleteTime) }}</template>
                </el-table-column>
                <el-table-column label="状态" width="90" align="center">
                  <template slot-scope="scope"><el-tag type="success" size="mini">已完成</el-tag></template>
                </el-table-column>
                <el-table-column label="操作" width="110" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="viewFollow(scope.row)">查看随访</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-empty v-else description="暂无已完成随访节点" :image-size="60" />
            </div>
          </el-tab-pane>

          <!-- 健康评估 -->
          <el-tab-pane label="健康评估" name="healthEval">
            <el-row :gutter="16">
              <el-col :span="8" v-for="a in healthAssessments" :key="a.title">
                <el-card shadow="hover" style="text-align:center;margin-bottom:16px">
                  <div slot="header"><b>{{ a.title }}</b></div>
                  <el-progress type="circle" :percentage="a.score" :color="a.color" :width="100" />
                  <p style="margin-top:10px;color:#8c8c8c">{{ a.desc }}</p>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 签约管理 -->
          <el-tab-pane label="签约管理" name="contract">
            <el-descriptions title="签约信息" :column="2" border size="small">
              <el-descriptions-item label="签约状态"><el-tag :type="detail.isSigned === '1' ? 'success' : 'info'">{{ detail.isSigned === '1' ? '已签约' : detail.isSigned === '0' ? '未签约' : (detail.isSigned || '未签约') }}</el-tag></el-descriptions-item>
              <el-descriptions-item label="签约日期">{{ contractInfo.signDate || '--' }}</el-descriptions-item>
              <el-descriptions-item label="签约团队">{{ detail.managingInstitution || '--' }}家庭医生团队</el-descriptions-item>
              <el-descriptions-item label="团队长">{{ detail.managingDoctor || '--' }}</el-descriptions-item>
              <el-descriptions-item label="服务包">{{ contractInfo.servicePackage || '基础慢病管理服务包' }}</el-descriptions-item>
              <el-descriptions-item label="有效期">{{ contractInfo.signDate || '--' }} ~ {{ contractInfo.expireDate || '--' }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>

          <!-- 干预管理 -->
          <el-tab-pane label="干预管理" name="intervention">
            <el-empty description="暂无干预记录，功能开发中" />
          </el-tab-pane>

          <!-- 分级管理 -->
          <el-tab-pane label="分级管理" name="grading">
            <div class="section-title"><span class="title-bar" />分级记录</div>
            <el-table :data="gradingRecords" border size="small">
              <el-table-column prop="assessment_date" label="分级日期" width="120">
                <template slot-scope="scope">{{ parseTime(scope.row.assessment_date, '{y}-{m}-{d}') }}</template>
              </el-table-column>
              <el-table-column prop="bp_classification" label="血压分级" width="120" />
              <el-table-column prop="risk_stratification" label="分层" width="100" />
              <el-table-column prop="management_level" label="管理分层" />
              <el-table-column label="操作" width="100"><template slot-scope="scope"><el-button size="mini" type="text" @click="editGrading(scope.row)">查看</el-button></template></el-table-column>
            </el-table>

            <div class="section-title" style="margin-top:20px"><span class="title-bar" />新增分级评估</div>
            <el-form ref="gradingForm" :model="gradingForm" label-width="auto" size="small">
              <div class="panel-title">心血管疾病危险因素</div>
              <el-checkbox-group v-model="gradingForm.cardiovascularFactors" class="check-group">
                <el-checkbox label="大于55岁" /> <el-checkbox label="吸烟" /> <el-checkbox label="缺乏体力活动" />
                <el-checkbox label="超重或肥胖(BMI>24)" /> <el-checkbox label="早发心血管疾病家族史" />
                <el-checkbox label="血脂异常" /> <el-checkbox label="糖尿病" />
              </el-checkbox-group>
              <div class="panel-title" style="margin-top:14px">靶器官损害</div>
              <el-checkbox-group v-model="gradingForm.targetOrganDamage" class="check-group">
                <el-checkbox label="左心室肥厚" /> <el-checkbox label="颈动脉粥样硬化斑块" /> <el-checkbox label="微量白蛋白尿" />
              </el-checkbox-group>
              <div class="panel-title" style="margin-top:14px">并存临床情况</div>
              <el-checkbox-group v-model="gradingForm.comorbidConditions" class="check-group">
                <el-checkbox label="脑血管疾病" /> <el-checkbox label="心脏疾病" /> <el-checkbox label="肾脏疾病" />
                <el-checkbox label="外周血管疾病" /> <el-checkbox label="视网膜病变" />
              </el-checkbox-group>
              <el-row :gutter="16" style="margin-top:14px">
                <el-col :span="8"><el-form-item label="血压分级"><el-select v-model="gradingForm.bpClassification" style="width:100%"><el-option v-for="o in bpOptions" :key="o" :label="o" :value="o" /></el-select></el-form-item></el-col>
                <el-col :span="8"><el-form-item label="管理分级"><el-select v-model="gradingForm.managementLevel" style="width:100%"><el-option v-for="o in levelOptions" :key="o" :label="o" :value="o" /></el-select></el-form-item></el-col>
                <el-col :span="8"><el-form-item label="危险分层"><el-select v-model="gradingForm.riskStratification" style="width:100%"><el-option v-for="o in riskOptions" :key="o" :label="o" :value="o" /></el-select></el-form-item></el-col>
              </el-row>
              <el-button type="primary" size="small" @click="submitGrading">保存分级评估</el-button>
            </el-form>
          </el-tab-pane>

          <!-- 健康宣教 -->
          <el-tab-pane label="健康宣教" name="education">
            <el-row :gutter="16">
              <el-col :span="6" v-for="e in educationList" :key="e.id">
                <el-card shadow="hover" :body-style="{ padding: '0px' }" style="margin-bottom:16px">
                  <div :style="{ background: e.bg, height: '80px', display: 'flex', alignItems: 'center', justifyContent: 'center' }"><i :class="e.icon" style="font-size:36px;color:#fff" /></div>
                  <div style="padding:12px"><h4>{{ e.title }}</h4><p style="font-size:12px;color:#8c8c8c">{{ e.desc }}</p><div style="display:flex;justify-content:space-between;font-size:11px;color:#aaa"><span>{{ e.date }}</span><el-tag :type="e.tagType" size="mini">{{ e.readStatus }}</el-tag></div></div>
                </el-card>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import { getArchiveDetail, getDiagnosis, getMedications, getVisits, getGradingRecords, saveGrading } from '@/api/resident/archive'
import { listPatientFollowNodes } from '@/api/follow/work'

export default {
  name: 'ResidentArchiveDetail',
  data() {
    return {
      activeTab: 'overview',
      detail: {},
      medications: [], diagnosisList: [], visitList: [], gradingRecords: [],
      followNodes: { pending: [], completed: [] },
      followLoading: false,
      healthProfile: { height: '--', weight: '--', bloodType: '--' },
      riskLevel: '', managementLevel: '',
      contractInfo: { servicePackage: '', signDate: '', expireDate: '' },
      todoItems: [], warnings: [],
      healthIndexCards: [
        { label: '收缩压', value: '--', unit: 'mmHg', color: '#ff4d4f', status: '--', statusType: 'info' },
        { label: '舒张压', value: '--', unit: 'mmHg', color: '#faad14', status: '--', statusType: 'info' },
        { label: '空腹血糖', value: '--', unit: 'mmol/L', color: '#52c41a', status: '--', statusType: 'info' },
        { label: 'BMI', value: '--', unit: 'kg/m²', color: '#faad14', status: '--', statusType: 'info' },
        { label: '总胆固醇', value: '--', unit: 'mmol/L', color: '#faad14', status: '--', statusType: 'info' },
        { label: '甘油三酯', value: '--', unit: 'mmol/L', color: '#52c41a', status: '--', statusType: 'info' },
        { label: '心率', value: '--', unit: '次/分', color: '#52c41a', status: '--', statusType: 'info' },
        { label: '血氧饱和度', value: '--', unit: '%', color: '#52c41a', status: '--', statusType: 'info' }
      ],
      healthAssessments: [
        { title: '心血管风险评估', score: 0, color: '#faad14', desc: '待评估' },
        { title: '糖尿病风险评估', score: 0, color: '#52c41a', desc: '待评估' },
        { title: '用药依从性评估', score: 0, color: '#1890ff', desc: '待评估' }
      ],
      bpOptions: ['正常血压', '正常高值', 'I级高血压', 'II级高血压', 'III级高血压'],
      levelOptions: ['一级管理', '二级管理', '三级管理'],
      riskOptions: ['低危', '中危', '高危', '极高危'],
      gradingForm: { cardiovascularFactors: [], targetOrganDamage: [], comorbidConditions: [], bpClassification: '', managementLevel: '', riskStratification: '' },
      educationList: [
        { id: 1, title: '高血压饮食指南', desc: '低盐低脂饮食指导', date: '2025-01-10', readStatus: '已读', tagType: 'success', icon: 'el-icon-document', bg: 'linear-gradient(135deg,#1890ff,#096dd9)' },
        { id: 2, title: '家庭血压监测', desc: '正确测量血压方法', date: '2025-01-05', readStatus: '未读', tagType: 'danger', icon: 'el-icon-s-home', bg: 'linear-gradient(135deg,#52c41a,#389e0d)' }
      ]
    }
  },
  created() { this.fetchDetail() },
  activated() {
    // 从随访工作站（编辑/查看随访）返回时，若停留在随访管理页，刷新节点状态
    if (this.activeTab === 'followUp') {
      this.loadFollowNodes()
    }
  },
  computed: {
    cardId() { return this.$route.params.cardId }
  },
  methods: {
    fetchDetail() {
      const cid = this.cardId
      if (!cid) return
      getArchiveDetail(cid).then(res => {
        this.detail = res.data || {}
        if (this.detail.diseaseTypes) {
          this.riskLevel = this.detail.diseaseTypes.includes('高血压') ? '中危' : '低危'
          this.managementLevel = '二级管理'
        }
        // Load overview data
        this.loadDiagnosisAndMeds(cid)
      }).catch(() => {})
    },
    loadDiagnosisAndMeds(cid) {
      getDiagnosis(cid).then(res => { this.diagnosisList = res.data || [] }).catch(() => {})
      getMedications(cid).then(res => { this.medications = res.data || [] }).catch(() => {})
      getVisits(cid).then(res => { this.visitList = res.data || [] }).catch(() => {})
    },
    onTabClick(tab) {
      const cid = this.cardId
      if (!cid) return
      if (tab.name === 'overview' || tab.name === 'medicalRecords') {
        this.loadDiagnosisAndMeds(cid)
      }
      if (tab.name === 'grading') {
        getGradingRecords(cid).then(res => { this.gradingRecords = res.data || [] }).catch(() => {})
      }
      if (tab.name === 'followUp') {
        this.loadFollowNodes()
      }
    },
    loadFollowNodes() {
      const cid = this.cardId
      if (!cid) return
      this.followLoading = true
      listPatientFollowNodes(cid).then(res => {
        const d = res.data || {}
        this.followNodes.pending = d.pending || []
        this.followNodes.completed = d.completed || []
        this.followLoading = false
      }).catch(() => { this.followLoading = false })
    },
    formatNodeDate(val) {
      if (!val) return '--'
      const m = String(val).match(/(\d{4}-\d{2}-\d{2})/)
      return m ? m[1] : String(val)
    },
    goFollow(node) {
      this.$router.push({
        path: '/workstation/user/editFollowUp',
        query: {
          type: '2',
          patientId: node.patientId,
          recordId: node.recordId,
          personId: node.personId,
          researchId: node.researchId,
          nodeTypeStatus: '1',
          oldPath: `/resident-archive-detail/${this.cardId}`
        }
      })
    },
    viewFollow(node) {
      this.$router.push({
        path: '/workstation/user/opinionDetail',
        query: {
          type: '2',
          patientId: node.patientId,
          recordId: node.recordId,
          personId: node.personId,
          researchId: node.researchId,
          nodeTypeStatus: '3',
          oldPath: `/resident-archive-detail/${this.cardId}`
        }
      })
    },
    submitGrading() {
      const cid = this.cardId
      if (!cid) return
      saveGrading(cid, this.gradingForm).then(() => {
        this.$message.success('分级评估保存成功')
        getGradingRecords(cid).then(res => { this.gradingRecords = res.data || [] }).catch(() => {})
      }).catch(() => { this.$message.error('保存失败') })
    },
    editGrading(row) { this.gradingForm = { ...row } },
    goBack() { this.$router.go(-1) }
  }
}
</script>

<style scoped>
.detail-container { background: #F2F7FB; min-height: 100%; padding: 16px; }
.breadcrumb { margin-bottom: 14px; }
.detail-layout { display: flex; gap: 16px; }
.left-sidebar { width: 245px; flex-shrink: 0; }
.right-content { flex: 1; min-width: 0; }
.sidebar-card { background: #fff; border: 1px solid #F2F2F2; border-radius: 8px; padding: 16px; margin-bottom: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.patient-card { text-align: center; }
.patient-avatar { width: 60px; height: 60px; border-radius: 50%; background: linear-gradient(135deg, #1890ff, #096dd9); color: #fff; font-size: 24px; line-height: 60px; margin: 0 auto 8px; }
.patient-title { font-size: 18px; font-weight: 500; }
.patient-age { font-size: 12px; color: #ccc; }
.patient-gender { font-size: 12px; color: #8c8c8c; margin-bottom: 8px; }
.patient-tags { display: flex; justify-content: center; gap: 6px; flex-wrap: wrap; margin-bottom: 8px; }
.tag-disease { background: rgba(250,116,107,0.1); color: #FA746B; padding: 2px 10px; border-radius: 26px; font-size: 10px; }
.tag-risk { background: rgba(255,241,240,1); color: #f00; padding: 2px 8px; border-radius: 5px; font-size: 10px; }
.tag-level { background: rgba(0,204,51,1); color: #fff; padding: 2px 8px; border-radius: 24px; font-size: 10px; }
.sidebar-divider { height: 1px; background: #f0f0f0; margin: 10px 0; }
.sidebar-info { text-align: left; line-height: 28px; }
.sidebar-info .lbl { color: #999; font-size: 12px; margin-right: 12px; display: inline-block; min-width: 56px; }
.sidebar-info .val { color: #333; font-size: 13px; }
.sidebar-section-title { font-size: 16px; color: #333; text-align: left; }
.content-panel { background: #fff; border: 1px solid #F2F2F2; border-radius: 8px; padding: 16px; margin-bottom: 14px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.panel-title { font-size: 16px; color: #333; margin-bottom: 12px; }
.health-card { padding: 16px; background: #fff; border-radius: 8px; border-top: 3px solid #ccc; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,.06); margin-bottom: 16px; }
.health-label { font-size: 13px; color: #8c8c8c; margin-bottom: 6px; }
.health-value { font-size: 28px; font-weight: 700; }
.health-unit { font-size: 12px; color: #8c8c8c; margin-bottom: 6px; }
.section-title { font-size: 16px; color: #333; margin-bottom: 12px; display: flex; align-items: center; }
.title-bar { display: inline-block; width: 6px; height: 20px; background: #3A8CFF; border-radius: 8px; margin-right: 10px; }
.check-group { margin-bottom: 8px; }
.check-group .el-checkbox { margin-right: 16px; margin-bottom: 6px; }
</style>
