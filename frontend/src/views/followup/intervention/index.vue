<template>
  <div class="app-container">
    <div class="page-header">
      <h3>健康干预</h3>
    </div>

    <!-- 病种筛选标签 -->
    <div class="disease-tabs">
      <el-radio-group v-model="activeDisease" size="small" @change="filterPatients">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="高血压">高血压</el-radio-button>
        <el-radio-button label="糖尿病">糖尿病</el-radio-button>
        <el-radio-button label="冠心病">冠心病</el-radio-button>
        <el-radio-button label="慢阻肺">慢阻肺</el-radio-button>
        <el-radio-button label="脑卒中">脑卒中</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 搜索 -->
    <el-form :inline="true" size="small" class="search-bar">
      <el-form-item label="姓名">
        <el-input v-model="searchName" placeholder="搜索患者姓名" clearable @clear="filterPatients" @keyup.enter.native="filterPatients" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="filterPatients">搜索</el-button>
      </el-form-item>
    </el-form>

    <!-- 患者卡片网格 -->
    <div v-loading="loading" class="card-grid">
      <el-row :gutter="20">
        <el-col :span="8" v-for="patient in displayedPatients" :key="patient.id" style="margin-bottom:20px;">
          <el-card class="patient-card" shadow="hover">
            <div class="card-header">
              <span class="patient-name">{{ patient.name }}</span>
              <span class="patient-age">{{ patient.age }}岁 · {{ patient.gender }}</span>
            </div>
            <div class="card-body">
              <div class="card-tags">
                <el-tag size="mini" :type="diseaseTagType(patient.disease)">{{ patient.disease }}</el-tag>
                <el-tag size="mini" :type="riskTagType(patient.riskLevel)">{{ patient.riskLevel }}</el-tag>
              </div>
              <div class="card-vitals">
                <div class="vital-item">
                  <span class="vital-label">血压</span>
                  <span class="vital-value" :class="{ 'abnormal': patient.bpAbnormal }">{{ patient.bp }}</span>
                </div>
                <div class="vital-item">
                  <span class="vital-label">BMI</span>
                  <span class="vital-value" :class="{ 'abnormal': patient.bmiAbnormal }">{{ patient.bmi }}</span>
                </div>
              </div>
              <div class="card-interventions">
                <el-tag v-for="item in patient.interventions" :key="item" size="mini" class="intervention-tag">{{ item }}</el-tag>
              </div>
            </div>
            <div class="card-footer">
              <el-button type="primary" size="mini" plain @click="handleHealthRecord(patient)">健康档案</el-button>
              <el-button type="warning" size="mini" plain @click="handleAdjustPlan(patient)">调整方案</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 空状态 -->
    <el-empty v-if="!loading && displayedPatients.length === 0" description="暂无患者数据" />

    <!-- 健康档案对话框 -->
    <el-dialog title="健康档案" :visible.sync="recordVisible" width="700px" append-to-body>
      <el-descriptions v-if="currentPatient" :column="2" border size="small">
        <el-descriptions-item label="姓名">{{ currentPatient.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ currentPatient.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ currentPatient.age }}岁</el-descriptions-item>
        <el-descriptions-item label="病种">{{ currentPatient.disease }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ currentPatient.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="血压">{{ currentPatient.bp }}</el-descriptions-item>
        <el-descriptions-item label="BMI">{{ currentPatient.bmi }}</el-descriptions-item>
        <el-descriptions-item label="干预类型">{{ currentPatient.interventions.join('、') }}</el-descriptions-item>
        <el-descriptions-item label="最近随访">2026-06-{{ Math.floor(Math.random() * 28) + 1 }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">138{{ String(Math.floor(Math.random() * 90000000 + 10000000)) }}</el-descriptions-item>
        <el-descriptions-item label="住址">陕西省西安市长安区</el-descriptions-item>
        <el-descriptions-item label="建档日期">2025-0{{ Math.floor(Math.random() * 12) + 1 }}-01</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 调整干预方案对话框 -->
    <el-dialog title="调整干预方案" :visible.sync="planVisible" width="600px" append-to-body>
      <el-form v-if="currentPatient" :model="planForm" label-width="100px" size="small">
        <el-form-item label="患者">
          <span>{{ currentPatient.name }}</span>
        </el-form-item>
        <el-form-item label="干预类型">
          <el-checkbox-group v-model="planForm.interventions">
            <el-checkbox label="药物" />
            <el-checkbox label="饮食" />
            <el-checkbox label="运动" />
            <el-checkbox label="心理" />
            <el-checkbox label="健康教育" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="方案说明">
          <el-input v-model="planForm.description" type="textarea" :rows="4" placeholder="请输入干预方案说明" />
        </el-form-item>
        <el-form-item label="目标血压">
          <el-input v-model="planForm.targetBp" placeholder="例: <140/90 mmHg" style="width:200px" />
        </el-form-item>
        <el-form-item label="饮食建议">
          <el-input v-model="planForm.dietAdvice" type="textarea" :rows="3" placeholder="请输入饮食建议" />
        </el-form-item>
        <el-form-item label="运动建议">
          <el-input v-model="planForm.exerciseAdvice" type="textarea" :rows="3" placeholder="请输入运动建议" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="planVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePlan">保存方案</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'HealthIntervention',
  data() {
    return {
      loading: false,
      activeDisease: 'all',
      searchName: '',
      allPatients: [],
      displayedPatients: [],
      recordVisible: false,
      planVisible: false,
      currentPatient: null,
      planForm: { interventions: [], description: '', targetBp: '', dietAdvice: '', exerciseAdvice: '' }
    }
  },
  created() { this.initData() },
  methods: {
    initData() {
      this.loading = true
      var that = this
      setTimeout(function() {
        that.allPatients = that.generateMockPatients()
        that.filterPatients()
        that.loading = false
      }, 300)
    },
    generateMockPatients() {
      var diseases = ['高血压', '糖尿病', '冠心病', '慢阻肺', '脑卒中']
      var riskLevels = ['高危', '中危', '低危']
      var names = ['王建国', '李秀兰', '张明华', '刘桂英', '赵志强', '陈丽华', '周伟', '吴美琴', '郑国强', '钱玉兰', '孙志军', '朱红梅']
      var patients = []
      for (var i = 0; i < 12; i++) {
        var sys = Math.floor(Math.random() * 40) + 110
        var dia = Math.floor(Math.random() * 20) + 70
        var bmi = (Math.random() * 10 + 20).toFixed(1)
        var bpAbnormal = sys > 140 || dia > 90
        var bmiAbnormal = Number(bmi) > 25
        var interventions = []
        if (Math.random() > 0.3) interventions.push('药物')
        if (Math.random() > 0.4) interventions.push('饮食')
        if (Math.random() > 0.5) interventions.push('运动')
        if (Math.random() > 0.7) interventions.push('心理')
        patients.push({
          id: i + 1,
          name: names[i],
          age: Math.floor(Math.random() * 30) + 45,
          gender: i % 2 === 0 ? '男' : '女',
          disease: diseases[i % diseases.length],
          riskLevel: riskLevels[Math.floor(Math.random() * 3)],
          bp: sys + '/' + dia + ' mmHg',
          bmi: bmi,
          bpAbnormal: bpAbnormal,
          bmiAbnormal: bmiAbnormal,
          interventions: interventions
        })
      }
      return patients
    },
    filterPatients() {
      var that = this
      this.displayedPatients = this.allPatients.filter(function(p) {
        var matchDisease = that.activeDisease === 'all' || p.disease === that.activeDisease
        var matchName = !that.searchName || p.name.indexOf(that.searchName) !== -1
        return matchDisease && matchName
      })
    },
    diseaseTagType(disease) {
      var map = { '高血压': 'danger', '糖尿病': 'warning', '冠心病': '', '慢阻肺': 'info', '脑卒中': 'success' }
      return map[disease] || ''
    },
    riskTagType(risk) {
      var map = { '高危': 'danger', '中危': 'warning', '低危': 'success' }
      return map[risk] || 'info'
    },
    handleHealthRecord(patient) {
      this.currentPatient = patient
      this.recordVisible = true
    },
    handleAdjustPlan(patient) {
      this.currentPatient = patient
      this.planForm = {
        interventions: patient.interventions.slice(),
        description: '',
        targetBp: '',
        dietAdvice: '',
        exerciseAdvice: ''
      }
      this.planVisible = true
    },
    savePlan() {
      var that = this
      this.$confirm('确认保存干预方案调整？', '提示', { type: 'info' }).then(function() {
        that.currentPatient.interventions = that.planForm.interventions.slice()
        that.$message.success('干预方案已更新')
        that.planVisible = false
      })
    }
  }
}
</script>

<style scoped>
.page-header { margin-bottom: 16px; }
.page-header h3 { margin: 0; font-size: 18px; color: #303133; }
.disease-tabs { margin-bottom: 16px; background: #fff; padding: 12px; border-radius: 4px; border: 1px solid #e6e6e6; }
.search-bar { background: #fff; padding: 12px 12px 0; border-radius: 4px; border: 1px solid #e6e6e6; margin-bottom: 16px; }
.card-grid { min-height: 400px; }
.patient-card { border-radius: 8px; transition: all .3s; }
.patient-card:hover { transform: translateY(-2px); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.patient-name { font-size: 16px; font-weight: 600; color: #303133; }
.patient-age { font-size: 13px; color: #909399; }
.card-body { margin-bottom: 12px; }
.card-tags { margin-bottom: 10px; }
.card-tags .el-tag { margin-right: 6px; }
.card-vitals { display: flex; gap: 24px; margin-bottom: 10px; padding: 8px 0; border-top: 1px dashed #eee; border-bottom: 1px dashed #eee; }
.vital-label { font-size: 12px; color: #909399; display: block; }
.vital-value { font-size: 14px; font-weight: 600; color: #303133; }
.vital-value.abnormal { color: #F56C6C; }
.card-interventions { display: flex; gap: 6px; flex-wrap: wrap; }
.intervention-tag { border: 1px dashed #409EFF; color: #409EFF; background: #ecf5ff; }
.card-footer { display: flex; gap: 8px; justify-content: flex-end; border-top: 1px solid #eee; padding-top: 12px; }
</style>
