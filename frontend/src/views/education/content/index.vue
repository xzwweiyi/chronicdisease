<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="文章标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入文章标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
          <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="文章类型" prop="articleType">
        <el-select v-model="queryParams.articleType" placeholder="请选择类型" clearable>
          <el-option label="图文" value="text_image" />
          <el-option label="视频" value="video" />
          <el-option label="问卷" value="questionnaire" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="已发布" value="published" />
          <el-option label="草稿" value="draft" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增文章</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="!selectedIds.length" @click="handleBatchDelete">批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-s-management" size="mini" @click="handleQuickCategory">分类管理</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文章标题" align="center" prop="title" min-width="200" show-overflow-tooltip />
      <el-table-column label="文章类型" align="center" prop="articleType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.articleType === 'text_image'" type="primary">图文</el-tag>
          <el-tag v-else-if="scope.row.articleType === 'video'" type="success">视频</el-tag>
          <el-tag v-else-if="scope.row.articleType === 'questionnaire'" type="warning">问卷</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="categoryName" width="120" />
      <el-table-column label="作者" align="center" prop="author" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'published'" type="success">已发布</el-tag>
          <el-tag v-else-if="scope.row.status === 'draft'" type="info">草稿</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="阅读量" align="center" prop="viewCount" width="80" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button v-if="scope.row.status === 'draft'" size="mini" type="text" icon="el-icon-upload" @click="handlePublish(scope.row)">发布</el-button>
          <el-button v-if="scope.row.status === 'published'" size="mini" type="text" icon="el-icon-takeaway-box" @click="handleUnpublish(scope.row)">下架</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑文章对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="850px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="文章标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入文章标题" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="文章类型" prop="articleType">
              <el-select v-model="form.articleType" placeholder="请选择类型" style="width:100%;">
                <el-option label="图文" value="text_image" />
                <el-option label="视频" value="video" />
                <el-option label="问卷" value="questionnaire" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width:100%;">
                <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="封面图片">
              <el-upload action="#" :auto-upload="false" list-type="picture-card" :limit="1">
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="摘要" prop="summary">
              <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="请输入文章摘要" maxlength="200" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="正文内容" prop="content">
              <div class="editor-placeholder">
                <el-alert title="富文本编辑器区域" type="info" :closable="false" show-icon description="实际项目中请替换为 Tinymce / WangEditor / Quill 等富文本编辑器组件" />
                <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入文章正文内容（此处为富文本编辑器占位）" />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.articleType === 'video'">
            <el-form-item label="视频链接" prop="videoUrl">
              <el-input v-model="form.videoUrl" placeholder="请输入视频URL或上传视频文件" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.articleType === 'questionnaire'">
            <el-form-item label="关联问卷" prop="questionnaireId">
              <el-select v-model="form.questionnaireId" placeholder="请选择关联问卷" style="width:100%;">
                <el-option label="糖尿病知识问卷" value="1" />
                <el-option label="高血压自测问卷" value="2" />
                <el-option label="生活方式评估问卷" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存草稿</el-button>
        <el-button type="success" @click="submitAndPublish">保存并发布</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog :title="'预览: ' + previewData.title" :visible.sync="previewOpen" width="750px" append-to-body>
      <div v-if="previewData" class="article-preview">
        <div class="preview-meta">
          <span>作者: {{ previewData.author }}</span>
          <span>分类: {{ previewData.categoryName }}</span>
          <span>发布时间: {{ previewData.publishTime || previewData.createTime }}</span>
          <span>阅读量: {{ previewData.viewCount }}</span>
        </div>
        <div class="preview-summary" v-if="previewData.summary">
          <blockquote>{{ previewData.summary }}</blockquote>
        </div>
        <div class="preview-content" v-html="previewData.content || '<p>暂无正文内容</p>'"></div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 分类管理对话框 -->
    <el-dialog title="分类管理" :visible.sync="categoryOpen" width="600px" append-to-body>
      <el-row :gutter="10" style="margin-bottom:12px;">
        <el-col :span="18">
          <el-input v-model="newCategoryName" placeholder="请输入新分类名称" size="small" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="addCategory">添加分类</el-button>
        </el-col>
      </el-row>
      <el-table :data="categoryOptions" size="small">
        <el-table-column label="分类名称" prop="name" />
        <el-table-column label="文章数" prop="articleCount" width="80" />
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c;" @click="delCategory(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="categoryOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'EducationContent',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      selectedIds: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        categoryId: null,
        articleType: null,
        status: null
      },
      categoryOptions: [
        { id: '1', name: '慢病基础知识', articleCount: 12 },
        { id: '2', name: '用药指导', articleCount: 8 },
        { id: '3', name: '饮食营养', articleCount: 15 },
        { id: '4', name: '运动康复', articleCount: 10 },
        { id: '5', name: '心理健康', articleCount: 6 },
        { id: '6', name: '自我监测', articleCount: 9 }
      ],
      articleList: [],
      open: false,
      title: '',
      form: {},
      rules: {
        title: [{ required: true, message: '文章标题不能为空', trigger: 'blur' }],
        articleType: [{ required: true, message: '请选择文章类型', trigger: 'change' }],
        categoryId: [{ required: true, message: '请选择所属分类', trigger: 'change' }]
      },
      previewOpen: false,
      previewData: {},
      categoryOpen: false,
      newCategoryName: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      setTimeout(() => {
        const mockList = [
          { id: '1', title: '高血压患者日常饮食指南', articleType: 'text_image', categoryId: '3', categoryName: '饮食营养', author: '王医生', createTime: '2026-06-28 10:00', status: 'published', viewCount: 1256, summary: '为高血压患者提供科学的日常饮食建议', content: '<p>控制钠盐摄入量，每日不超过6g...</p>' },
          { id: '2', title: '糖尿病运动康复指导视频', articleType: 'video', categoryId: '4', categoryName: '运动康复', author: '李医生', createTime: '2026-06-27 14:30', status: 'published', viewCount: 892, summary: '适合糖尿病患者的运动方案', content: '<p>视频内容：糖尿病患者运动指导</p>', videoUrl: 'https://example.com/video/diabetes.mp4' },
          { id: '3', title: '脑卒中早期识别与急救', articleType: 'text_image', categoryId: '1', categoryName: '慢病基础知识', author: '张医生', createTime: '2026-06-25 09:00', status: 'published', viewCount: 2105, summary: '掌握脑卒中早期症状，及时就医', content: '<p>脑卒中早期识别口诀...</p>' },
          { id: '4', title: '冠心病术后用药注意事项', articleType: 'text_image', categoryId: '2', categoryName: '用药指导', author: '刘医生', createTime: '2026-06-30 08:45', status: 'draft', viewCount: 0, summary: '冠心病术后用药注意事项', content: '<p>正在编辑中...</p>' },
          { id: '5', title: '慢阻肺患者肺功能自测问卷', articleType: 'questionnaire', categoryId: '6', categoryName: '自我监测', author: '王医生', createTime: '2026-06-22 11:20', status: 'published', viewCount: 654, summary: '帮助慢阻肺患者自评肺功能状态', content: '<p>问卷内容：自测题</p>', questionnaireId: '3' },
          { id: '6', title: '慢性病患者心理调适指南', articleType: 'text_image', categoryId: '5', categoryName: '心理健康', author: '陈护士', createTime: '2026-06-20 15:00', status: 'published', viewCount: 438, summary: '帮助慢性病患者保持积极心态', content: '<p>心理健康调适方法...</p>' },
          { id: '7', title: '正确测量血压的方法', articleType: 'text_image', categoryId: '6', categoryName: '自我监测', author: '赵医生', createTime: '2026-06-29 16:30', status: 'draft', viewCount: 0, summary: '家庭自测血压的正确方法', content: '<p>正在编辑中...</p>' }
        ]
        let filtered = mockList
        if (this.queryParams.title) {
          filtered = filtered.filter(item => item.title.indexOf(this.queryParams.title) > -1)
        }
        if (this.queryParams.categoryId) {
          filtered = filtered.filter(item => item.categoryId === this.queryParams.categoryId)
        }
        if (this.queryParams.articleType) {
          filtered = filtered.filter(item => item.articleType === this.queryParams.articleType)
        }
        if (this.queryParams.status) {
          filtered = filtered.filter(item => item.status === this.queryParams.status)
        }
        this.articleList = filtered
        this.total = filtered.length
        this.loading = false
      }, 300)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.id)
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增宣教文章'
    },
    handleEdit(row) {
      this.reset()
      this.form = { ...row }
      this.open = true
      this.title = '编辑宣教文章'
    },
    handlePublish(row) {
      this.$modal.confirm('确认发布文章"' + row.title + '"？').then(() => {
        row.status = 'published'
        row.publishTime = new Date().toISOString().replace('T', ' ').substring(0, 19)
        this.$modal.msgSuccess('发布成功')
      }).catch(() => {})
    },
    handleUnpublish(row) {
      this.$modal.confirm('确认下架文章"' + row.title + '"？下架后患者将无法查看。').then(() => {
        row.status = 'draft'
        this.$modal.msgSuccess('已下架')
      }).catch(() => {})
    },
    handlePreview(row) {
      this.previewData = row
      this.previewOpen = true
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除文章"' + row.title + '"？').then(() => {
        this.articleList = this.articleList.filter(item => item.id !== row.id)
        this.total = this.articleList.length
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleBatchDelete() {
      this.$modal.confirm('确认删除选中的 ' + this.selectedIds.length + ' 篇文章？').then(() => {
        this.articleList = this.articleList.filter(item => !this.selectedIds.includes(item.id))
        this.total = this.articleList.length
        this.selectedIds = []
        this.$modal.msgSuccess('批量删除成功')
      }).catch(() => {})
    },
    handleQuickCategory() {
      this.newCategoryName = ''
      this.categoryOpen = true
    },
    addCategory() {
      if (!this.newCategoryName.trim()) {
        this.$modal.msgError('请输入分类名称')
        return
      }
      this.categoryOptions.push({
        id: 'c_' + Date.now(),
        name: this.newCategoryName.trim(),
        articleCount: 0
      })
      this.newCategoryName = ''
      this.$modal.msgSuccess('分类添加成功')
    },
    delCategory(row) {
      this.$modal.confirm('确认删除分类"' + row.name + '"？（分类下的文章将移至未分类）').then(() => {
        this.categoryOptions = this.categoryOptions.filter(item => item.id !== row.id)
        this.$modal.msgSuccess('分类已删除')
      }).catch(() => {})
    },
    submitForm() {
      this.saveArticle('draft')
    },
    submitAndPublish() {
      this.saveArticle('published')
    },
    saveArticle(status) {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id) {
            const idx = this.articleList.findIndex(item => item.id === this.form.id)
            if (idx > -1) {
              Object.assign(this.articleList[idx], this.form, { status: status })
            }
            this.$modal.msgSuccess('修改成功')
          } else {
            const newArticle = {
              ...this.form,
              id: 'a_' + Date.now(),
              status: status,
              createTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
              viewCount: 0,
              categoryName: (this.categoryOptions.find(c => c.id === this.form.categoryId) || {}).name || ''
            }
            this.articleList.unshift(newArticle)
            this.total = this.articleList.length
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
      this.form = { articleType: 'text_image' }
      this.resetForm('form')
    }
  }
}
</script>

<style scoped>
.editor-placeholder { width: 100%; }
.editor-placeholder .el-alert { margin-bottom: 8px; }
.article-preview { max-height: 65vh; overflow-y: auto; padding: 10px; }
.preview-meta { display: flex; gap: 20px; font-size: 13px; color: #909399; margin-bottom: 12px; padding-bottom: 8px; border-bottom: 1px solid #ebeef5; }
.preview-summary blockquote { border-left: 4px solid #409eff; padding: 8px 16px; margin: 12px 0; background: #f5f7fa; color: #606266; font-size: 14px; }
.preview-content { font-size: 15px; line-height: 1.8; }
</style>
