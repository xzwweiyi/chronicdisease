<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="管理路径" prop="pathwayId">
        <el-select v-model="queryParams.pathwayId" placeholder="请选择路径" clearable @change="handleQuery" style="width:220px;">
          <el-option v-for="item in pathwayOptions" :key="item.id" :label="item.projectName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="路径节点" prop="nodeId">
        <el-select v-model="queryParams.nodeId" placeholder="请选择节点" clearable @change="handleQuery" style="width:220px;" :disabled="!queryParams.pathwayId">
          <el-option v-for="item in nodeOptions" :key="item.id" :label="item.nodeName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div v-if="!queryParams.pathwayId" class="empty-hint">
      <el-empty description="请先选择一个管理路径" :image-size="120" />
    </div>

    <div v-else class="assignment-container">
      <!-- 左侧：待分配宣教内容 -->
      <div class="left-panel">
        <div class="panel-header">
          <span class="panel-title">待分配宣教内容</span>
          <el-input v-model="leftSearch" placeholder="搜索内容" size="small" prefix-icon="el-icon-search" clearable style="width:200px;" />
        </div>
        <div class="category-tree">
          <el-tree
            :data="categoryTreeData"
            node-key="id"
            :props="treeProps"
            :filter-node-method="filterNode"
            ref="leftTree"
            default-expand-all
            highlight-current
            @node-click="handleLeftTreeClick">
            <span class="tree-node-label" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span class="tree-count" v-if="data.type === 'article'">{{ data.viewCount }}次阅读</span>
            </span>
          </el-tree>
        </div>
      </div>

      <!-- 中间：操作按钮 -->
      <div class="center-panel">
        <el-button type="primary" icon="el-icon-d-arrow-right" circle @click="moveToRight" :disabled="!leftSelected.length" title="分配到右侧"></el-button>
        <el-button type="primary" icon="el-icon-d-arrow-left" circle @click="moveToLeft" :disabled="!rightSelected.length" title="移回左侧" style="margin-top:12px;"></el-button>
      </div>

      <!-- 右侧：已分配内容 -->
      <div class="right-panel">
        <div class="panel-header">
          <span class="panel-title">已分配宣教内容</span>
          <span class="panel-badge">{{ assignedList.length }} 篇</span>
        </div>
        <div class="assigned-list">
          <el-table :data="assignedList" size="small" max-height="520" @selection-change="handleRightSelectionChange" style="width:100%;">
            <el-table-column type="selection" width="40" align="center" />
            <el-table-column label="文章标题" prop="title" min-width="160" show-overflow-tooltip />
            <el-table-column label="类型" prop="articleType" width="70">
              <template slot-scope="scope">
                <el-tag :type="getTypeTag(scope.row.articleType)" size="mini">{{ getTypeLabel(scope.row.articleType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="推送时机" prop="pushTiming" width="140">
              <template slot-scope="scope">
                <el-select v-model="scope.row.pushTiming" size="mini" @change="handlePushTimingChange(scope.row)">
                  <el-option label="入组时" value="on_enroll" />
                  <el-option label="节点开始前1天" value="1_before" />
                  <el-option label="节点开始前3天" value="3_before" />
                  <el-option label="节点开始前7天" value="7_before" />
                  <el-option label="节点完成后" value="after_node" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="60" align="center">
              <template slot-scope="scope">
                <el-button type="text" icon="el-icon-delete" size="mini" style="color:#f56c6c;" @click="removeAssign(scope.row)"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <!-- 保存按钮 -->
    <el-row v-if="queryParams.pathwayId" style="margin-top:16px;">
      <el-col :span="24" style="text-align:center;">
        <el-button type="primary" icon="el-icon-check" @click="handleSaveAssign">保存分配方案</el-button>
        <el-button type="success" icon="el-icon-upload" @click="handlePublishAssign">发布方案</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'EducationAssignment',
  data() {
    return {
      showSearch: true,
      queryParams: {
        pathwayId: null,
        nodeId: null
      },
      pathwayOptions: [],
      nodeOptions: [],
      leftSearch: '',
      leftSelected: [],
      rightSelected: [],
      treeProps: {
        children: 'children',
        label: 'label'
      },
      categoryTreeData: [],
      allArticles: [],
      assignedList: []
    }
  },
  created() {
    this.getPathwayOptions()
    this.initArticles()
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
    getNodeOptions() {
      const mockNodes = {
        '1': [{ id: 'n1', nodeName: '入组评估(基线)' }, { id: 'n2', nodeName: '首次随访' }, { id: 'n3', nodeName: '用药指导' }, { id: 'n4', nodeName: '健康宣教' }, { id: 'n5', nodeName: '季度评估' }],
        '2': [{ id: 'n6', nodeName: '入组评估(基线)' }, { id: 'n7', nodeName: '首次随访' }, { id: 'n8', nodeName: '用药指导' }, { id: 'n9', nodeName: '健康宣教' }, { id: 'n10', nodeName: '复诊提醒' }],
        '3': [{ id: 'n11', nodeName: '入组评估(基线)' }, { id: 'n12', nodeName: '首次随访' }, { id: 'n13', nodeName: '康复指导' }, { id: 'n14', nodeName: '用药指导' }, { id: 'n15', nodeName: '季度评估' }],
        '4': [{ id: 'n16', nodeName: '入组评估(基线)' }, { id: 'n17', nodeName: '首次随访' }, { id: 'n18', nodeName: '康复指导' }, { id: 'n19', nodeName: '复诊提醒' }]
      }
      return mockNodes[this.queryParams.pathwayId] || []
    },
    initArticles() {
      this.allArticles = [
        { id: 'a1', title: '高血压患者日常饮食指南', articleType: 'text_image', categoryId: 'cat3', categoryName: '饮食营养', viewCount: 1256, pushTiming: 'on_enroll' },
        { id: 'a2', title: '糖尿病运动康复指导视频', articleType: 'video', categoryId: 'cat4', categoryName: '运动康复', viewCount: 892, pushTiming: 'on_enroll' },
        { id: 'a3', title: '脑卒中早期识别与急救', articleType: 'text_image', categoryId: 'cat1', categoryName: '慢病基础知识', viewCount: 2105, pushTiming: 'on_enroll' },
        { id: 'a4', title: '冠心病术后用药注意事项', articleType: 'text_image', categoryId: 'cat2', categoryName: '用药指导', viewCount: 567, pushTiming: 'on_enroll' },
        { id: 'a5', title: '慢阻肺患者肺功能自测问卷', articleType: 'questionnaire', categoryId: 'cat6', categoryName: '自我监测', viewCount: 654, pushTiming: 'on_enroll' },
        { id: 'a6', title: '慢性病患者心理调适指南', articleType: 'text_image', categoryId: 'cat5', categoryName: '心理健康', viewCount: 438, pushTiming: 'on_enroll' },
        { id: 'a7', title: '正确测量血压的方法', articleType: 'text_image', categoryId: 'cat6', categoryName: '自我监测', viewCount: 321, pushTiming: 'on_enroll' },
        { id: 'a8', title: '高血压药物服用时间表', articleType: 'text_image', categoryId: 'cat2', categoryName: '用药指导', viewCount: 723, pushTiming: 'on_enroll' },
        { id: 'a9', title: '糖尿病患者足部护理', articleType: 'text_image', categoryId: 'cat1', categoryName: '慢病基础知识', viewCount: 489, pushTiming: 'on_enroll' },
        { id: 'a10', title: '低盐饮食烹饪技巧', articleType: 'text_image', categoryId: 'cat3', categoryName: '饮食营养', viewCount: 612, pushTiming: 'on_enroll' }
      ]
      this.buildCategoryTree()
    },
    buildCategoryTree() {
      const assignedIds = this.assignedList.map(item => item.id)
      const unassigned = this.allArticles.filter(a => !assignedIds.includes(a.id))
      const categoryMap = {}
      unassigned.forEach(article => {
        if (!categoryMap[article.categoryId]) {
          categoryMap[article.categoryId] = {
            id: article.categoryId,
            label: article.categoryName,
            type: 'category',
            children: []
          }
        }
        categoryMap[article.categoryId].children.push({
          id: article.id,
          label: article.title,
          type: 'article',
          viewCount: article.viewCount,
          articleData: article
        })
      })
      this.categoryTreeData = Object.values(categoryMap)
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) > -1
    },
    handleQuery() {
      this.nodeOptions = this.getNodeOptions()
      if (this.queryParams.nodeId) {
        this.loadAssignedForNode()
      } else {
        this.assignedList = []
      }
      this.buildCategoryTree()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.nodeOptions = []
      this.assignedList = []
      this.buildCategoryTree()
    },
    loadAssignedForNode() {
      const mockAssignments = {
        'n1': [{ ...this.allArticles[0], pushTiming: 'on_enroll' }, { ...this.allArticles[6], pushTiming: '1_before' }],
        'n2': [{ ...this.allArticles[1], pushTiming: 'on_enroll' }, { ...this.allArticles[7], pushTiming: '7_before' }],
        'n3': [{ ...this.allArticles[2], pushTiming: '3_before' }, { ...this.allArticles[3], pushTiming: 'after_node' }],
        'n4': [{ ...this.allArticles[4], pushTiming: 'on_enroll' }, { ...this.allArticles[5], pushTiming: '1_before' }, { ...this.allArticles[9], pushTiming: 'after_node' }]
      }
      this.assignedList = mockAssignments[this.queryParams.nodeId] || []
    },
    handleLeftTreeClick(data) {
      if (data.type === 'article') {
        const idx = this.leftSelected.findIndex(item => item.id === data.id)
        if (idx > -1) {
          this.leftSelected.splice(idx, 1)
        } else {
          this.leftSelected.push(data.articleData)
        }
        if (this.$refs.leftTree) {
          this.$refs.leftTree.setCurrentKey(null)
        }
      }
    },
    handleRightSelectionChange(selection) {
      this.rightSelected = selection
    },
    moveToRight() {
      if (this.leftSelected.length === 0) {
        this.$modal.msgWarning('请在左侧选择要分配的文章')
        return
      }
      this.assignedList.push(...this.leftSelected)
      this.leftSelected = []
      this.buildCategoryTree()
    },
    moveToLeft() {
      if (this.rightSelected.length === 0) {
        this.$modal.msgWarning('请在右侧选择要移除的文章')
        return
      }
      this.assignedList = this.assignedList.filter(item => !this.rightSelected.some(s => s.id === item.id))
      this.rightSelected = []
      this.buildCategoryTree()
    },
    removeAssign(row) {
      this.assignedList = this.assignedList.filter(item => item.id !== row.id)
      this.buildCategoryTree()
    },
    handlePushTimingChange(row) {
      this.$modal.msgSuccess('推送时机已更新: ' + this.getPushTimingLabel(row.pushTiming))
    },
    getPushTimingLabel(timing) {
      const map = { on_enroll: '入组时', '1_before': '节点开始前1天', '3_before': '节点开始前3天', '7_before': '节点开始前7天', after_node: '节点完成后' }
      return map[timing] || timing
    },
    getTypeLabel(type) {
      const map = { text_image: '图文', video: '视频', questionnaire: '问卷' }
      return map[type] || type
    },
    getTypeTag(type) {
      const map = { text_image: 'primary', video: 'success', questionnaire: 'warning' }
      return map[type] || ''
    },
    handleSaveAssign() {
      if (!this.queryParams.nodeId) {
        this.$modal.msgWarning('请先选择路径节点')
        return
      }
      this.$modal.loading('正在保存分配方案...')
      setTimeout(() => {
        this.$modal.closeLoading()
        this.$modal.msgSuccess('分配方案保存成功')
      }, 500)
    },
    handlePublishAssign() {
      if (!this.queryParams.nodeId) {
        this.$modal.msgWarning('请先选择路径节点')
        return
      }
      this.$modal.confirm('确认发布该宣教方案？发布后患者将在指定时机收到对应宣教内容。').then(() => {
        this.$modal.loading('正在发布方案...')
        setTimeout(() => {
          this.$modal.closeLoading()
          this.$modal.msgSuccess('方案发布成功')
        }, 600)
      }).catch(() => {})
    }
  },
  watch: {
    leftSearch(val) {
      if (this.$refs.leftTree) {
        this.$refs.leftTree.filter(val)
      }
    }
  }
}
</script>

<style scoped>
.empty-hint { padding: 80px 0; text-align: center; }
.assignment-container { display: flex; gap: 16px; align-items: flex-start; }
.left-panel { flex: 1; min-width: 0; border: 1px solid #ebeef5; border-radius: 4px; overflow: hidden; }
.right-panel { flex: 1; min-width: 0; border: 1px solid #ebeef5; border-radius: 4px; overflow: hidden; }
.center-panel { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 40px 4px; flex-shrink: 0; }
.panel-header { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: #f5f7fa; border-bottom: 1px solid #ebeef5; }
.panel-title { font-weight: bold; font-size: 14px; }
.panel-badge { font-size: 12px; color: #909399; }
.category-tree { padding: 12px; }
.tree-node-label { display: flex; align-items: center; flex: 1; }
.tree-count { font-size: 11px; color: #c0c4cc; margin-left: auto; }
.assigned-list { padding: 8px; }
</style>
