<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="管理路径" prop="pathwayId">
        <el-select v-model="queryParams.pathwayId" placeholder="请选择路径" clearable @change="handleQuery" style="width:220px;">
          <el-option v-for="item in pathwayOptions" :key="item.id" :label="item.projectName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="节点名称" prop="nodeName">
        <el-input v-model="queryParams.nodeName" placeholder="请输入节点名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" :disabled="!queryParams.pathwayId">新增节点</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-sort" size="mini" @click="handleSort" :disabled="!queryParams.pathwayId || nodeList.length < 2">节点排序</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <div v-if="!queryParams.pathwayId" class="empty-hint">
      <el-empty description="请先选择一个管理路径" :image-size="120" />
    </div>

    <div v-else-if="nodeList.length === 0" class="empty-hint">
      <el-empty description="该路径下暂无节点，请点击新增节点" :image-size="120" />
    </div>

    <div v-else class="node-timeline">
      <div v-for="(node, index) in nodeList" :key="node.id" class="node-card-wrapper">
        <div class="timeline-connector">
          <div class="timeline-dot" :class="getNodeClass(node.nodeType)"></div>
          <div v-if="index < nodeList.length - 1" class="timeline-line"></div>
        </div>
        <el-card class="node-card" shadow="hover">
          <div class="node-header">
            <div class="node-order">第{{ node.sortOrder || (index + 1) }}步</div>
            <el-tag :type="getNodeTypeTag(node.nodeType)" size="small">{{ getNodeTypeLabel(node.nodeType) }}</el-tag>
            <el-tag v-if="node.isBaseline === '1'" type="warning" size="small" effect="plain">基线</el-tag>
            <div class="node-actions">
              <el-button type="text" icon="el-icon-edit" size="mini" @click="handleEdit(node)">编辑</el-button>
              <el-button type="text" icon="el-icon-delete" size="mini" style="color:#f56c6c;" @click="handleDelete(node)">删除</el-button>
            </div>
          </div>
          <div class="node-body">
            <div class="node-info">
              <span class="info-label">节点名称：</span>
              <span class="info-value">{{ node.nodeName }}</span>
            </div>
            <div class="node-info">
              <span class="info-label">间隔：</span>
              <span class="info-value">{{ node.intervalDays ? node.intervalDays + '天' : '立即' }}</span>
            </div>
            <div class="node-info">
              <span class="info-label">频次：</span>
              <span class="info-value">{{ node.frequency || '单次' }}</span>
            </div>
            <div class="node-info">
              <span class="info-label">CRF表单数：</span>
              <span class="info-value">{{ node.crfCount || 0 }}个</span>
            </div>
            <div class="node-info">
              <span class="info-label">提醒设置：</span>
              <span class="info-value">{{ node.remindConfig ? '已配置' : '未配置' }}</span>
            </div>
          </div>
          <div class="node-desc" v-if="node.nodeDesc">
            <span class="info-label">节点说明：</span>
            <span>{{ node.nodeDesc }}</span>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 新增/编辑节点对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="节点名称" prop="nodeName">
              <el-input v-model="form.nodeName" placeholder="请输入节点名称" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="节点类型" prop="nodeType">
              <el-select v-model="form.nodeType" placeholder="请选择节点类型" style="width:100%;">
                <el-option label="随访" value="follow" />
                <el-option label="评估" value="assess" />
                <el-option label="干预" value="intervene" />
                <el-option label="宣教" value="education" />
                <el-option label="提醒" value="remind" />
                <el-option label="医嘱" value="order" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基线标记" prop="isBaseline">
              <el-switch v-model="form.isBaseline" active-value="1" inactive-value="0" active-text="是" inactive-text="否" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="间隔天数" prop="intervalDays">
              <el-input-number v-model="form.intervalDays" :min="0" :max="365" placeholder="0表示立即" style="width:100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="频次" prop="frequency">
              <el-select v-model="form.frequency" placeholder="请选择频次" style="width:100%;">
                <el-option label="单次" value="once" />
                <el-option label="每日" value="daily" />
                <el-option label="每周" value="weekly" />
                <el-option label="每两周" value="biweekly" />
                <el-option label="每月" value="monthly" />
                <el-option label="每季" value="quarterly" />
                <el-option label="每年" value="yearly" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提醒方式" prop="remindType">
              <el-select v-model="form.remindType" placeholder="请选择提醒方式" style="width:100%;" clearable>
                <el-option label="无提醒" value="" />
                <el-option label="短信" value="sms" />
                <el-option label="微信" value="wechat" />
                <el-option label="APP推送" value="app" />
                <el-option label="电话" value="phone" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提前提醒" prop="remindBeforeDays">
              <el-input-number v-model="form.remindBeforeDays" :min="0" :max="30" placeholder="提前天数" style="width:100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="关联CRF表单" prop="crfIds">
              <el-select v-model="form.crfIds" multiple placeholder="请选择关联表单" style="width:100%;">
                <el-option label="基本信息采集表" value="1" />
                <el-option label="随访记录表" value="2" />
                <el-option label="用药评估表" value="3" />
                <el-option label="生活质量问卷" value="4" />
                <el-option label="体格检查表" value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="节点说明" prop="nodeDesc">
              <el-input v-model="form.nodeDesc" type="textarea" :rows="3" placeholder="请输入节点说明" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 排序对话框 -->
    <el-dialog title="节点排序" :visible.sync="sortOpen" width="500px" append-to-body>
      <el-alert title="拖拽节点调整顺序，点击保存生效" type="info" :closable="false" style="margin-bottom:15px;" />
      <draggable v-model="sortList" item-key="id" handle=".drag-handle" animation="200">
        <template #item="{ element, index }">
          <div class="sort-item">
            <i class="el-icon-rank drag-handle" style="cursor:move; margin-right:8px; color:#909399;"></i>
            <span class="sort-index">{{ index + 1 }}</span>
            <span class="sort-name">{{ element.nodeName }}</span>
            <el-tag :type="getNodeTypeTag(element.nodeType)" size="mini">{{ getNodeTypeLabel(element.nodeType) }}</el-tag>
          </div>
        </template>
      </draggable>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveSort">保存排序</el-button>
        <el-button @click="sortOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: 'PathwayNode',
  components: { draggable },
  data() {
    return {
      loading: false,
      showSearch: true,
      queryParams: {
        pathwayId: null,
        nodeName: null
      },
      pathwayOptions: [],
      nodeList: [],
      open: false,
      sortOpen: false,
      title: '',
      sortList: [],
      form: {
        isBaseline: '0',
        intervalDays: 0,
        frequency: 'once',
        remindBeforeDays: 0,
        crfIds: []
      },
      rules: {
        nodeName: [{ required: true, message: '节点名称不能为空', trigger: 'blur' }],
        nodeType: [{ required: true, message: '请选择节点类型', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getPathwayOptions()
  },
  methods: {
    getPathwayOptions() {
      const mockPathways = [
        { id: '1', projectName: '高血压慢病管理路径', diseaseName: '高血压' },
        { id: '2', projectName: '糖尿病慢病管理路径', diseaseName: '糖尿病' },
        { id: '3', projectName: '脑卒中康复管理路径', diseaseName: '脑卒中' },
        { id: '4', projectName: '冠心病术后管理路径', diseaseName: '冠心病' }
      ]
      this.pathwayOptions = mockPathways
    },
    getList() {
      if (!this.queryParams.pathwayId) {
        this.nodeList = []
        return
      }
      this.loading = true
      setTimeout(() => {
        const mockNodes = [
          { id: '1', nodeName: '入组评估', nodeType: 'assess', isBaseline: '1', sortOrder: 1, intervalDays: 0, frequency: 'once', crfCount: 2, remindConfig: true, remindType: 'app', remindBeforeDays: 0, nodeDesc: '患者入组时的初始评估节点' },
          { id: '2', nodeName: '首次随访', nodeType: 'follow', isBaseline: '0', sortOrder: 2, intervalDays: 7, frequency: 'once', crfCount: 3, remindConfig: true, remindType: 'sms', remindBeforeDays: 1, nodeDesc: '入组后首次随访' },
          { id: '3', nodeName: '用药指导', nodeType: 'intervene', isBaseline: '0', sortOrder: 3, intervalDays: 14, frequency: 'monthly', crfCount: 1, remindConfig: true, remindType: 'wechat', remindBeforeDays: 2, nodeDesc: '定期用药指导和调整' },
          { id: '4', nodeName: '健康宣教', nodeType: 'education', isBaseline: '0', sortOrder: 4, intervalDays: 7, frequency: 'weekly', crfCount: 2, remindConfig: false, remindType: '', remindBeforeDays: 0, nodeDesc: '推送健康知识文章' },
          { id: '5', nodeName: '复诊提醒', nodeType: 'remind', isBaseline: '0', sortOrder: 5, intervalDays: 90, frequency: 'quarterly', crfCount: 0, remindConfig: true, remindType: 'phone', remindBeforeDays: 3, nodeDesc: '提醒患者定期复诊' },
          { id: '6', nodeName: '季度评估', nodeType: 'assess', isBaseline: '0', sortOrder: 6, intervalDays: 90, frequency: 'quarterly', crfCount: 4, remindConfig: true, remindType: 'app', remindBeforeDays: 1, nodeDesc: '每季度综合评估' }
        ]
        this.nodeList = mockNodes.filter(n => n.nodeName.indexOf(this.queryParams.nodeName || '') > -1)
        this.loading = false
      }, 300)
    },
    handleQuery() {
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.getList()
    },
    getNodeTypeLabel(type) {
      const map = { follow: '随访', assess: '评估', intervene: '干预', education: '宣教', remind: '提醒', order: '医嘱' }
      return map[type] || type
    },
    getNodeTypeTag(type) {
      const map = { follow: 'primary', assess: 'success', intervene: 'warning', education: '', remind: 'info', order: 'danger' }
      return map[type] || ''
    },
    getNodeClass(type) {
      const map = { follow: 'dot-primary', assess: 'dot-success', intervene: 'dot-warning', education: 'dot-info', remind: 'dot-info', order: 'dot-danger' }
      return map[type] || ''
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增节点'
    },
    handleEdit(row) {
      this.reset()
      this.form = {
        id: row.id,
        nodeName: row.nodeName,
        nodeType: row.nodeType,
        isBaseline: row.isBaseline,
        intervalDays: row.intervalDays,
        frequency: row.frequency,
        remindType: row.remindType || '',
        remindBeforeDays: row.remindBeforeDays || 0,
        crfIds: row.crfIds || [],
        nodeDesc: row.nodeDesc || ''
      }
      this.open = true
      this.title = '编辑节点'
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除节点"' + row.nodeName + '"？').then(() => {
        this.nodeList = this.nodeList.filter(item => item.id !== row.id)
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleSort() {
      this.sortList = [...this.nodeList]
      this.sortOpen = true
    },
    saveSort() {
      this.nodeList = this.sortList.map((item, index) => {
        item.sortOrder = index + 1
        return item
      })
      this.sortOpen = false
      this.$modal.msgSuccess('排序已保存')
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id) {
            const idx = this.nodeList.findIndex(item => item.id === this.form.id)
            if (idx > -1) {
              Object.assign(this.nodeList[idx], this.form)
            }
            this.$modal.msgSuccess('修改成功')
          } else {
            const newNode = {
              ...this.form,
              id: 'n_' + Date.now(),
              sortOrder: this.nodeList.length + 1,
              crfCount: (this.form.crfIds || []).length
            }
            this.nodeList.push(newNode)
            this.$modal.msgSuccess('新增成功')
          }
          this.open = false
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        isBaseline: '0',
        intervalDays: 0,
        frequency: 'once',
        remindBeforeDays: 0,
        crfIds: [],
        nodeType: undefined,
        nodeName: undefined,
        remindType: '',
        nodeDesc: undefined
      }
      this.resetForm('form')
    }
  }
}
</script>

<style scoped>
.empty-hint { padding: 80px 0; text-align: center; }
.node-timeline { padding: 20px 0 20px 40px; }
.node-card-wrapper { display: flex; align-items: flex-start; }
.timeline-connector { display: flex; flex-direction: column; align-items: center; margin-right: 16px; flex-shrink: 0; }
.timeline-dot { width: 12px; height: 12px; border-radius: 50%; background: #409eff; border: 2px solid #fff; box-shadow: 0 0 0 2px #409eff; flex-shrink: 0; }
.timeline-dot.dot-primary { background: #409eff; box-shadow: 0 0 0 2px #409eff; }
.timeline-dot.dot-success { background: #67c23a; box-shadow: 0 0 0 2px #67c23a; }
.timeline-dot.dot-warning { background: #e6a23c; box-shadow: 0 0 0 2px #e6a23c; }
.timeline-dot.dot-info { background: #909399; box-shadow: 0 0 0 2px #909399; }
.timeline-dot.dot-danger { background: #f56c6c; box-shadow: 0 0 0 2px #f56c6c; }
.timeline-line { width: 2px; flex: 1; background: #dcdfe6; min-height: 40px; }
.node-card { flex: 1; margin-bottom: 12px; }
.node-card .node-header { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.node-card .node-order { font-weight: bold; color: #303133; font-size: 14px; }
.node-card .node-actions { margin-left: auto; }
.node-card .node-body { display: flex; flex-wrap: wrap; gap: 8px 24px; }
.node-card .node-info { font-size: 13px; }
.node-card .info-label { color: #909399; }
.node-card .info-value { color: #303133; }
.node-card .node-desc { font-size: 13px; color: #909399; margin-top: 8px; padding-top: 8px; border-top: 1px dashed #ebeef5; }
.sort-item { display: flex; align-items: center; padding: 10px 12px; margin-bottom: 6px; background: #f5f7fa; border-radius: 4px; }
.sort-index { display: inline-block; width: 24px; height: 24px; line-height: 24px; text-align: center; background: #409eff; color: #fff; border-radius: 50%; margin-right: 10px; font-size: 12px; }
.sort-name { flex: 1; }
</style>
