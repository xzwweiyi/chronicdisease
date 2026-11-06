<template>
  <div class="app-container">
    <el-page-header @back="goBack" content="创建管理路径" />

    <el-steps :active="activeStep" finish-status="success" align-center style="margin: 20px 0">
      <el-step title="路径简介" />
      <el-step title="路径节点" />
      <el-step title="节点表单" />
      <el-step title="宣教知识" />
      <el-step title="提醒设置" />
      <el-step title="筛选入组" />
      <el-step title="分配人员" />
      <el-step title="路径状态" />
    </el-steps>

    <div class="step-content">
      <!-- Step 0: 路径简介 -->
      <div v-show="activeStep === 0">
        <el-form ref="introForm" :model="pathway" :rules="introRules" label-width="120px" size="medium">
          <el-form-item label="路径名称" prop="projectName">
            <el-input v-model="pathway.projectName" placeholder="请输入路径名称" />
          </el-form-item>
          <el-form-item label="病种" prop="diseaseTypeId">
            <el-select v-model="pathway.diseaseTypeId" placeholder="请选择病种">
              <el-option v-for="d in diseaseOptions" :key="d.diseaseId" :label="d.diseaseName" :value="d.diseaseId" />
            </el-select>
          </el-form-item>
          <el-form-item label="路径描述" prop="researchSynopsis">
            <el-input v-model="pathway.researchSynopsis" type="textarea" :rows="4" placeholder="请输入路径描述" />
          </el-form-item>
          <el-form-item label="展示图片">
            <image-upload v-model="pathway.imageUrl" />
          </el-form-item>
          <el-form-item label="目标患者数">
            <el-input-number v-model="pathway.targetPatientNum" :min="0" />
          </el-form-item>
          <el-form-item label="退出标准">
            <el-input v-model="pathway.exitCriteria" type="textarea" :rows="3" placeholder="请输入退出标准" />
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 1: 路径节点设置 -->
      <div v-show="activeStep === 1">
        <div class="mb10">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="addNode">添加节点</el-button>
        </div>
        <el-table :data="nodes" row-key="followId" border>
          <el-table-column label="排序" width="60" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.nodeOrder || scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="节点名称" prop="followName" />
          <el-table-column label="节点类型" prop="nodeType" width="130">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.isBaseline === 1" type="info">基线</el-tag>
              <el-tag v-else-if="scope.row.nodeType === 'follow_up'" type="primary">随访</el-tag>
              <el-tag v-else-if="scope.row.nodeType === 'assessment'" type="success">评估</el-tag>
              <el-tag v-else-if="scope.row.nodeType === 'intervention'" type="warning">干预</el-tag>
              <el-tag v-else-if="scope.row.nodeType === 'education'" type="info">宣教</el-tag>
              <el-tag v-else-if="scope.row.nodeType === 'reminder'" type="danger">提醒</el-tag>
              <el-tag v-else-if="scope.row.nodeType === 'orders'" type="">医嘱</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="间隔" width="200">
            <template slot-scope="scope">
              <span v-if="scope.row.isBaseline === 1">--</span>
              <span v-else>{{ scope.row.followInterval }}
                {{ scope.row.followIntervalUnit == '1' ? '天' : scope.row.followIntervalUnit == '2' ? '周' : '月' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="频次" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.isBaseline === 1">--</span>
              <span v-else>{{ scope.row.followFrequency == '1' ? '限制' + scope.row.followTotal + '次' : '不限次' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template slot-scope="scope">
              <el-button v-if="scope.row.isBaseline !== 1" size="mini" type="text" icon="el-icon-edit"
                @click="editNode(scope.row)">编辑</el-button>
              <el-button v-if="scope.row.isBaseline !== 1" size="mini" type="text" icon="el-icon-delete"
                @click="removeNode(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 添加/编辑节点对话框 -->
        <el-dialog :title="nodeDialogTitle" :visible.sync="nodeDialogOpen" width="500px" append-to-body>
          <el-form ref="nodeForm" :model="currentNode" label-width="100px">
            <el-form-item label="节点名称" prop="followName">
              <el-input v-model="currentNode.followName" placeholder="如：随访1、高血压评估" />
            </el-form-item>
            <el-form-item label="节点类型" prop="nodeType">
              <el-select v-model="currentNode.nodeType" placeholder="请选择" :disabled="currentNode.isBaseline === 1">
                <el-option label="随访" value="follow_up" />
                <el-option label="评估" value="assessment" />
                <el-option label="干预" value="intervention" />
                <el-option label="宣教" value="education" />
                <el-option label="提醒" value="reminder" />
                <el-option label="医嘱" value="orders" />
              </el-select>
            </el-form-item>
            <el-form-item label="间隔单位" prop="followIntervalUnit" v-if="currentNode.isBaseline !== 1">
              <el-select v-model="currentNode.followIntervalUnit">
                <el-option label="天" value="1" />
                <el-option label="周" value="2" />
                <el-option label="月" value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="间隔数" prop="followInterval" v-if="currentNode.isBaseline !== 1">
              <el-input-number v-model="currentNode.followInterval" :min="1" />
            </el-form-item>
            <el-form-item label="随访频次" prop="followFrequency" v-if="currentNode.isBaseline !== 1">
              <el-radio-group v-model="currentNode.followFrequency">
                <el-radio label="1">限制次数</el-radio>
                <el-radio label="2">不限次数</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="总次数" prop="followTotal"
              v-if="currentNode.followFrequency == '1' && currentNode.isBaseline !== 1">
              <el-input-number v-model="currentNode.followTotal" :min="1" />
            </el-form-item>
            <el-form-item label="提醒提前天数" prop="reminderAdvanceDays">
              <el-input-number v-model="currentNode.reminderAdvanceDays" :min="0" placeholder="0=不提醒" />
            </el-form-item>
          </el-form>
          <div slot="footer">
            <el-button type="primary" @click="saveNode">确 定</el-button>
            <el-button @click="nodeDialogOpen = false">取 消</el-button>
          </div>
        </el-dialog>
      </div>

      <!-- Step 2: 节点表单设置（复用CRF配置） -->
      <div v-show="activeStep === 2">
        <el-alert title="请先在"步骤 2: 路径节点"中完成节点配置，然后在此步骤为每个节点绑定CRF表单"
          type="info" :closable="false" show-icon style="margin-bottom: 15px" />
        <el-tabs v-model="crfActiveNode" type="card">
          <el-tab-pane v-for="node in nodes" :key="node.followId" :label="node.followName" :name="String(node.followId)">
            <div class="mb10">
              <el-button type="primary" size="small" icon="el-icon-plus" @click="addCrfForm(node)">添加CRF表单</el-button>
            </div>
            <el-table :data="getNodeCrfForms(node)" border>
              <el-table-column label="表单名称" prop="medicalName" />
              <el-table-column label="表单类型" width="120">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.formType == '0'">模块</el-tag>
                  <el-tag v-else-if="scope.row.formType == '3'" type="warning">自评量表</el-tag>
                  <el-tag v-else-if="scope.row.formType == '4'" type="danger">预警</el-tag>
                  <el-tag v-else-if="scope.row.formType == '5'" type="success">健康方案</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="是否必填" width="100">
                <template slot-scope="scope">
                  <el-switch v-model="scope.row.required" active-text="必填" inactive-text="可选" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete"
                    @click="removeCrfForm(node, scope.$index)">移除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div v-if="!getNodeCrfForms(node).length" class="empty-tip">
              暂无表单，请点击"添加CRF表单"从CRF库中选择
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- Step 3-7: 占位 -->
      <div v-show="activeStep === 3">
        <el-empty description="宣教知识设置（建设中）" />
      </div>
      <div v-show="activeStep === 4">
        <el-empty description="提醒设置（建设中）" />
      </div>
      <div v-show="activeStep === 5">
        <el-empty description="筛选患者入组（建设中）" />
      </div>
      <div v-show="activeStep === 6">
        <el-empty description="分配随访人员（建设中）" />
      </div>
      <div v-show="activeStep === 7">
        <el-empty description="路径状态管理（建设中）" />
      </div>
    </div>

    <div class="step-actions">
      <el-button v-if="activeStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="activeStep < 7" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="activeStep === 7" type="success" @click="finishSetup">完成配置</el-button>
    </div>
  </div>
</template>

<script>
import { getPathway, updatePathway } from '@/api/pathway/definition'
import { listNodes, addNode, updateNode, delNode, reorderNodes } from '@/api/pathway/node'
import { listDiseases } from '@/api/info/config'
import ImageUpload from '@/components/ImageUpload'

export default {
  name: 'PathwayCreate',
  components: { ImageUpload },
  data() {
    return {
      activeStep: 0,
      pathwayId: null,
      pathway: { targetPatientNum: 0 },
      nodes: [],
      diseaseOptions: [],
      nodeDialogOpen: false,
      nodeDialogTitle: '',
      currentNode: {},
      crfActiveNode: '',
      introRules: {
        projectName: [{ required: true, message: '路径名称不能为空', trigger: 'blur' }],
        diseaseTypeId: [{ required: true, message: '请选择病种', trigger: 'change' }]
      }
    }
  },
  created() {
    this.pathwayId = this.$route.params.id
    if (this.pathwayId) {
      this.loadPathway()
    }
    this.loadDiseases()
  },
  methods: {
    loadPathway() {
      getPathway(this.pathwayId).then(response => {
        this.pathway = response.data
        this.activeStep = parseInt(this.pathway.stage || '0')
      })
      listNodes(this.pathwayId).then(response => {
        this.nodes = response.data || []
        if (this.nodes.length > 0) {
          this.crfActiveNode = String(this.nodes[0].followId)
        }
      })
    },
    loadDiseases() {
      listDiseases().then(response => {
        this.diseaseOptions = response.data || response.rows || []
      })
    },
    prevStep() {
      if (this.activeStep > 0) this.activeStep--
    },
    nextStep() {
      if (this.activeStep < 7) this.activeStep++
    },
    // 节点管理
    addNode() {
      this.currentNode = {
        researchId: this.pathwayId,
        nodeType: 'follow_up',
        followInterval: 30,
        followIntervalUnit: '1',
        followFrequency: '1',
        followTotal: 1,
        reminderAdvanceDays: 0,
        isBaseline: 0,
        nodeOrder: this.nodes.length + 1
      }
      this.nodeDialogTitle = '添加节点'
      this.nodeDialogOpen = true
    },
    editNode(node) {
      this.currentNode = { ...node }
      this.nodeDialogTitle = '编辑节点'
      this.nodeDialogOpen = true
    },
    saveNode() {
      if (!this.currentNode.followName) {
        this.$message.warning('请输入节点名称')
        return
      }
      if (this.currentNode.followId) {
        updateNode(this.currentNode).then(() => {
          this.$message.success('修改成功')
          this.nodeDialogOpen = false
          this.loadNodes()
        })
      } else {
        addNode(this.currentNode).then(() => {
          this.$message.success('添加成功')
          this.nodeDialogOpen = false
          this.loadNodes()
        })
      }
    },
    removeNode(node) {
      this.$confirm('确认删除节点"' + node.followName + '"？', '提示', { type: 'warning' }).then(() => {
        delNode(node.followId).then(() => {
          this.$message.success('删除成功')
          this.loadNodes()
        })
      })
    },
    loadNodes() {
      listNodes(this.pathwayId).then(response => {
        this.nodes = response.data || []
      })
    },
    // CRF表单
    getNodeCrfForms(node) {
      return node.fmedicalForms || []
    },
    addCrfForm(node) {
      // 导航到CRF选择页面
      this.$router.push({
        path: '/queueSettings/cycle/crfSettings',
        query: { followId: node.followId, researchId: this.pathwayId }
      })
    },
    removeCrfForm(node, index) {
      node.fmedicalForms.splice(index, 1)
    },
    // 完成
    finishSetup() {
      updatePathway({ id: this.pathwayId, stage: '7', pathwayStatus: '0' }).then(() => {
        this.$message.success('路径配置完成')
        this.$router.push({ path: '/pathway/list' })
      })
    },
    goBack() {
      this.$router.push({ path: '/pathway/list' })
    }
  }
}
</script>

<style scoped>
.step-content {
  min-height: 400px;
  padding: 20px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
.step-actions {
  text-align: center;
  margin-top: 20px;
}
.empty-tip {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>
