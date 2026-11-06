<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="管理路径" prop="pathwayId">
        <el-select v-model="queryParams.pathwayId" placeholder="请选择路径" clearable @change="handlePathwayChange" style="width:260px;">
          <el-option v-for="item in pathwayOptions" :key="item.id" :label="item.projectName" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>

    <div v-if="!queryParams.pathwayId" class="empty-hint">
      <el-empty description="请先选择一个管理路径" :image-size="120" />
    </div>

    <div v-else class="baseline-container">
      <!-- 操作按钮 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-check" size="mini" @click="handleSave">保存基线配置</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-upload" size="mini" @click="handlePublish">发布基线</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button icon="el-icon-refresh" size="mini" @click="handleReset">重置为默认</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-view" size="mini" @click="handlePreview">预览基线表单</el-button>
        </el-col>
      </el-row>

      <el-form ref="baselineForm" :model="baselineForm" :rules="rules" label-width="120px">
        <!-- 基本信息 -->
        <el-card class="section-card">
          <div slot="header" class="section-header">
            <i class="el-icon-user"></i>
            <span>基本信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="姓名" prop="patientName">
                <el-input v-model="baselineForm.patientName" placeholder="字段名：patient_name" disabled />
                <div class="field-hint">字段编码: patient_name | 类型: 文本</div>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="baselineForm.idCard" placeholder="字段名：id_card" disabled />
                <div class="field-hint">字段编码: id_card | 类型: 文本 | 校验: 身份证</div>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="地址" prop="address">
                <el-input v-model="baselineForm.address" placeholder="字段名：address" disabled />
                <div class="field-hint">字段编码: address | 类型: 文本</div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 慢病诊断 -->
        <el-card class="section-card">
          <div slot="header" class="section-header">
            <i class="el-icon-document-copy"></i>
            <span>慢病诊断</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="病种" prop="diseaseIds">
                <el-select v-model="baselineForm.diseaseIds" multiple placeholder="请选择病种" style="width:100%;" filterable>
                  <el-option v-for="item in diseaseOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
                <div class="field-hint">字段编码: disease_ids | 类型: 多选</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="确诊时间" prop="diagnoseTime">
                <el-date-picker v-model="baselineForm.diagnoseTime" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%;" />
                <div class="field-hint">字段编码: diagnose_time | 类型: 日期</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="分级分期" prop="diseaseStage">
                <el-select v-model="baselineForm.diseaseStage" placeholder="请选择" style="width:100%;">
                  <el-option label="I期" value="stage1" />
                  <el-option label="II期" value="stage2" />
                  <el-option label="III期" value="stage3" />
                  <el-option label="IV期" value="stage4" />
                </el-select>
                <div class="field-hint">字段编码: disease_stage | 类型: 单选</div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 既往并发症 -->
        <el-card class="section-card">
          <div slot="header" class="section-header">
            <i class="el-icon-warning-outline"></i>
            <span>既往并发症</span>
          </div>
          <el-form-item label="并发症" prop="complications">
            <el-checkbox-group v-model="baselineForm.complications">
              <el-row>
                <el-col :span="8"><el-checkbox label="高血压">高血压</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="糖尿病肾病">糖尿病肾病</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="糖尿病视网膜病变">糖尿病视网膜病变</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="冠心病">冠心病</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="脑卒中">脑卒中</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="周围神经病变">周围神经病变</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="糖尿病足">糖尿病足</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="心力衰竭">心力衰竭</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="肾功能不全">肾功能不全</el-checkbox></el-col>
              </el-row>
            </el-checkbox-group>
            <div class="field-hint">字段编码: complications | 类型: 多选</div>
          </el-form-item>
        </el-card>

        <!-- 既往住院史/手术史/过敏史 -->
        <el-card class="section-card">
          <div slot="header" class="section-header">
            <i class="el-icon-date"></i>
            <span>既往史</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="是否有住院史">
                <el-radio-group v-model="baselineForm.hasHospitalization">
                  <el-radio label="1">有</el-radio>
                  <el-radio label="0">无</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item v-if="baselineForm.hasHospitalization === '1'" label="住院史描述">
                <el-input v-model="baselineForm.hospitalizationDesc" type="textarea" :rows="2" placeholder="请输入住院史" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="是否有手术史">
                <el-radio-group v-model="baselineForm.hasSurgery">
                  <el-radio label="1">有</el-radio>
                  <el-radio label="0">无</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item v-if="baselineForm.hasSurgery === '1'" label="手术史描述">
                <el-input v-model="baselineForm.surgeryDesc" type="textarea" :rows="2" placeholder="请输入手术史" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="是否有过敏史">
                <el-radio-group v-model="baselineForm.hasAllergy">
                  <el-radio label="1">有</el-radio>
                  <el-radio label="0">无</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item v-if="baselineForm.hasAllergy === '1'" label="过敏史描述">
                <el-input v-model="baselineForm.allergyDesc" type="textarea" :rows="2" placeholder="请输入过敏史" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 当前健康状态 -->
        <el-card class="section-card">
          <div slot="header" class="section-header">
            <i class="el-icon-s-data"></i>
            <span>当前健康状态</span>
          </div>
          <el-form-item label="主要症状">
            <el-checkbox-group v-model="baselineForm.currentSymptoms">
              <el-row>
                <el-col :span="8"><el-checkbox label="头晕">头晕</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="胸闷">胸闷</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="心悸">心悸</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="视力模糊">视力模糊</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="肢体麻木">肢体麻木</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="多饮多尿">多饮多尿</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="乏力">乏力</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="水肿">水肿</el-checkbox></el-col>
                <el-col :span="8"><el-checkbox label="呼吸困难">呼吸困难</el-checkbox></el-col>
              </el-row>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="其他症状">
            <el-input v-model="baselineForm.otherSymptoms" type="textarea" :rows="2" placeholder="其他未列出的症状" />
          </el-form-item>
        </el-card>

        <!-- 体征测量 -->
        <el-card class="section-card">
          <div slot="header" class="section-header">
            <i class="el-icon-s-marketing"></i>
            <span>体征测量 <span style="color:#f56c6c;font-size:12px;">(带 * 的为必填项)</span></span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="身高(cm)" prop="height" required>
                <el-input-number v-model="baselineForm.height" :min="30" :max="250" :precision="1" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: height | 单位: cm | 范围: 30-250</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="体重(kg)" prop="weight" required>
                <el-input-number v-model="baselineForm.weight" :min="2" :max="300" :precision="1" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: weight | 单位: kg | 范围: 2-300</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="BMI" prop="bmi" required>
                <el-input-number v-model="baselineForm.bmi" :min="10" :max="60" :precision="1" placeholder="自动计算" style="width:100%;" disabled />
                <div class="field-hint">字段编码: bmi | 自动计算: 体重/(身高/100)^2</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="腰围(cm)" prop="waist">
                <el-input-number v-model="baselineForm.waist" :min="30" :max="200" :precision="1" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: waist | 单位: cm</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="收缩压(mmHg)" prop="systolic" required>
                <el-input-number v-model="baselineForm.systolic" :min="40" :max="300" :precision="0" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: systolic_bp | 单位: mmHg</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="舒张压(mmHg)" prop="diastolic" required>
                <el-input-number v-model="baselineForm.diastolic" :min="20" :max="200" :precision="0" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: diastolic_bp | 单位: mmHg</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="心率(次/分)" prop="heartRate" required>
                <el-input-number v-model="baselineForm.heartRate" :min="30" :max="250" :precision="0" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: heart_rate | 单位: 次/分</div>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="空腹血糖(mmol/L)" prop="fastingGlucose">
                <el-input-number v-model="baselineForm.fastingGlucose" :min="1" :max="40" :precision="1" placeholder="请输入" style="width:100%;" />
                <div class="field-hint">字段编码: fasting_glucose | 单位: mmol/L</div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
      </el-form>
    </div>

    <!-- 预览对话框 -->
    <el-dialog title="基线表单预览" :visible.sync="previewOpen" width="700px" append-to-body>
      <el-descriptions title="基线采集表单预览" :column="2" border size="small">
        <el-descriptions-item label="姓名">{{ baselineForm.patientName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ baselineForm.idCard || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ baselineForm.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="病种">{{ getDiseaseLabels() || '-' }}</el-descriptions-item>
        <el-descriptions-item label="确诊时间">{{ baselineForm.diagnoseTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="分级分期">{{ getStageLabel() }}</el-descriptions-item>
        <el-descriptions-item label="并发症">{{ baselineForm.complications.length ? baselineForm.complications.join('、') : '无' }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ baselineForm.height ? baselineForm.height + 'cm' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="体重">{{ baselineForm.weight ? baselineForm.weight + 'kg' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="BMI">{{ baselineForm.bmi || '-' }}</el-descriptions-item>
        <el-descriptions-item label="腰围">{{ baselineForm.waist ? baselineForm.waist + 'cm' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="血压">{{ baselineForm.systolic || '-' }}/{{ baselineForm.diastolic || '-' }} mmHg</el-descriptions-item>
        <el-descriptions-item label="心率">{{ baselineForm.heartRate ? baselineForm.heartRate + '次/分' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="空腹血糖">{{ baselineForm.fastingGlucose ? baselineForm.fastingGlucose + 'mmol/L' : '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PathwayBaseline',
  data() {
    return {
      showSearch: true,
      queryParams: { pathwayId: null },
      pathwayOptions: [],
      previewOpen: false,
      diseaseOptions: [
        { id: '1', name: '高血压' },
        { id: '2', name: '糖尿病' },
        { id: '3', name: '脑卒中' },
        { id: '4', name: '冠心病' },
        { id: '5', name: '慢阻肺' }
      ],
      stageOptions: [
        { value: 'stage1', label: 'I期' },
        { value: 'stage2', label: 'II期' },
        { value: 'stage3', label: 'III期' },
        { value: 'stage4', label: 'IV期' }
      ],
      baselineForm: {
        patientName: '',
        idCard: '',
        address: '',
        diseaseIds: [],
        diagnoseTime: '',
        diseaseStage: '',
        complications: [],
        hasHospitalization: '0',
        hospitalizationDesc: '',
        hasSurgery: '0',
        surgeryDesc: '',
        hasAllergy: '0',
        allergyDesc: '',
        currentSymptoms: [],
        otherSymptoms: '',
        height: null,
        weight: null,
        bmi: null,
        waist: null,
        systolic: null,
        diastolic: null,
        heartRate: null,
        fastingGlucose: null
      },
      rules: {
        height: [{ required: true, message: '身高不能为空', trigger: 'blur' }],
        weight: [{ required: true, message: '体重不能为空', trigger: 'blur' }],
        bmi: [{ required: true, message: 'BMI不能为空', trigger: 'blur' }],
        systolic: [{ required: true, message: '收缩压不能为空', trigger: 'blur' }],
        diastolic: [{ required: true, message: '舒张压不能为空', trigger: 'blur' }],
        heartRate: [{ required: true, message: '心率不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getPathwayOptions()
  },
  methods: {
    getPathwayOptions() {
      this.pathwayOptions = [
        { id: '1', projectName: '高血压慢病管理路径' },
        { id: '2', projectName: '糖尿病慢病管理路径' },
        { id: '3', projectName: '脑卒中康复管理路径' },
        { id: '4', projectName: '冠心病术后管理路径' }
      ]
    },
    handlePathwayChange(pathwayId) {
      const pathway = this.pathwayOptions.find(p => p.id === pathwayId)
      if (!pathway) return
      this.loadBaselineConfig(pathway)
    },
    loadBaselineConfig(pathway) {
      const mockBaselines = {
        '1': { patientName: '张三', idCard: '610102196804152536', address: '陕西省西安市城关区人民路100号', diseaseIds: ['1'], diagnoseTime: '2025-06-15', diseaseStage: 'stage2', complications: ['高血压'], hasHospitalization: '1', hospitalizationDesc: '2024年因高血压住院一次', hasSurgery: '0', surgeryDesc: '', hasAllergy: '1', allergyDesc: '青霉素过敏', currentSymptoms: ['头晕', '胸闷'], otherSymptoms: '夜间偶发头痛', height: 170, weight: 75, bmi: 25.9, waist: 92, systolic: 148, diastolic: 92, heartRate: 78, fastingGlucose: 5.6 },
        '2': { patientName: '李丽', idCard: '610102198106123245', address: '陕西省西安市七里河区长城路56号', diseaseIds: ['2'], diagnoseTime: '2024-10-20', diseaseStage: 'stage1', complications: ['糖尿病肾病'], hasHospitalization: '0', hospitalizationDesc: '', hasSurgery: '0', surgeryDesc: '', hasAllergy: '0', allergyDesc: '', currentSymptoms: ['多饮多尿', '视力模糊'], otherSymptoms: '', height: 162, weight: 68, bmi: 25.9, waist: 85, systolic: 135, diastolic: 85, heartRate: 72, fastingGlucose: 7.8 },
        '3': { patientName: '王建国', idCard: '610102196407082618', address: '陕西省西安市西固区工业路200号', diseaseIds: ['3'], diagnoseTime: '2025-12-01', diseaseStage: 'stage3', complications: ['脑卒中', '高血压'], hasHospitalization: '1', hospitalizationDesc: '2025年12月脑卒中急诊住院', hasSurgery: '1', surgeryDesc: '2025年12月开颅手术', hasAllergy: '0', allergyDesc: '', currentSymptoms: ['肢体麻木', '头晕'], otherSymptoms: '右侧肢体活动受限', height: 175, weight: 80, bmi: 26.1, waist: 96, systolic: 155, diastolic: 95, heartRate: 82, fastingGlucose: 5.2 },
        '4': { patientName: '赵敏', idCard: '610102198809051527', address: '陕西省西安市安宁区文化路88号', diseaseIds: ['4'], diagnoseTime: '2025-08-10', diseaseStage: 'stage2', complications: ['冠心病', '心力衰竭'], hasHospitalization: '1', hospitalizationDesc: '2025年8月因心绞痛入院', hasSurgery: '1', surgeryDesc: '2025年8月PCI支架手术', hasAllergy: '0', allergyDesc: '', currentSymptoms: ['胸闷', '心悸'], otherSymptoms: '活动后加重', height: 160, weight: 55, bmi: 21.5, waist: 72, systolic: 128, diastolic: 78, heartRate: 88, fastingGlucose: 4.8 }
      }
      const data = mockBaselines[pathway.id] || {}
      Object.assign(this.baselineForm, data)
      this.calcBMI()
    },
    calcBMI() {
      if (this.baselineForm.height && this.baselineForm.weight) {
        const h = this.baselineForm.height / 100
        this.baselineForm.bmi = Math.round((this.baselineForm.weight / (h * h)) * 10) / 10
      }
    },
    getDiseaseLabels() {
      return this.baselineForm.diseaseIds.map(id => {
        const d = this.diseaseOptions.find(item => item.id === id)
        return d ? d.name : id
      }).join('、')
    },
    getStageLabel() {
      const s = this.stageOptions.find(item => item.value === this.baselineForm.diseaseStage)
      return s ? s.label : '-'
    },
    handleSave() {
      this.$refs['baselineForm'].validate(valid => {
        if (valid) {
          this.$modal.loading('正在保存基线配置...')
          setTimeout(() => {
            this.$modal.closeLoading()
            this.$modal.msgSuccess('基线配置保存成功')
          }, 500)
        }
      })
    },
    handlePublish() {
      this.$refs['baselineForm'].validate(valid => {
        if (valid) {
          this.$modal.confirm('确认发布基线配置？发布后新入组患者将使用此基线。').then(() => {
            this.$modal.loading('正在发布基线配置...')
            setTimeout(() => {
              this.$modal.closeLoading()
              this.$modal.msgSuccess('基线配置发布成功')
            }, 600)
          }).catch(() => {})
        }
      })
    },
    handleReset() {
      this.$modal.confirm('确认重置为默认基线配置？此操作将覆盖当前配置。').then(() => {
        this.resetForm('baselineForm')
        this.baselineForm = {
          patientName: '', idCard: '', address: '', diseaseIds: [], diagnoseTime: '', diseaseStage: '',
          complications: [], hasHospitalization: '0', hospitalizationDesc: '', hasSurgery: '0', surgeryDesc: '',
          hasAllergy: '0', allergyDesc: '', currentSymptoms: [], otherSymptoms: '', height: null, weight: null,
          bmi: null, waist: null, systolic: null, diastolic: null, heartRate: null, fastingGlucose: null
        }
        this.$modal.msgSuccess('已重置为默认配置')
      }).catch(() => {})
    },
    handlePreview() {
      this.calcBMI()
      this.previewOpen = true
    }
  },
  watch: {
    'baselineForm.height': function() { this.calcBMI() },
    'baselineForm.weight': function() { this.calcBMI() }
  }
}
</script>

<style scoped>
.empty-hint { padding: 80px 0; text-align: center; }
.baseline-container { max-width: 1200px; }
.section-card { margin-bottom: 18px; }
.section-card .section-header { display: flex; align-items: center; gap: 6px; font-weight: bold; font-size: 14px; }
.section-card .section-header i { font-size: 16px; color: #409eff; }
.field-hint { font-size: 11px; color: #c0c4cc; line-height: 1.2; margin-top: 2px; }
</style>
