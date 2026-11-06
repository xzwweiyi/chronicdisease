<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="路径名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入路径名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="病种" prop="diseaseTypeId">
        <el-select v-model="queryParams.diseaseTypeId" placeholder="请选择病种" clearable>
          <el-option v-for="item in diseaseOptions" :key="item.diseaseId" :label="item.diseaseName" :value="item.diseaseId" />
        </el-select>
      </el-form-item>
      <el-form-item label="路径状态" prop="pathwayStatus">
        <el-select v-model="queryParams.pathwayStatus" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="0" />
          <el-option label="已发布" value="1" />
          <el-option label="进行中" value="2" />
          <el-option label="已关闭" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['pathway:definition:add']">创建路径</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="pathwayList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="路径名称" align="center" prop="projectName" show-overflow-tooltip />
      <el-table-column label="病种" align="center" prop="diseaseName" />
      <el-table-column label="管理患者数" align="center" prop="patientNum" />
      <el-table-column label="目标患者数" align="center" prop="targetPatientNum" />
      <el-table-column label="路径状态" align="center" prop="pathwayStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.pathwayStatus === '0'" type="info">草稿</el-tag>
          <el-tag v-else-if="scope.row.pathwayStatus === '1'" type="success">已发布</el-tag>
          <el-tag v-else-if="scope.row.pathwayStatus === '2'" type="primary">进行中</el-tag>
          <el-tag v-else-if="scope.row.pathwayStatus === '3'" type="danger">已关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)"
            v-hasPermi="['pathway:definition:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)"
            v-hasPermi="['pathway:definition:edit']">编辑</el-button>
          <el-button v-if="scope.row.pathwayStatus === '0'" size="mini" type="text" icon="el-icon-upload"
            @click="handlePublish(scope.row)" v-hasPermi="['pathway:definition:edit']">发布</el-button>
          <el-button v-if="scope.row.pathwayStatus === '1' || scope.row.pathwayStatus === '2'" size="mini"
            type="text" icon="el-icon-turn-off" @click="handleClose(scope.row)"
            v-hasPermi="['pathway:definition:edit']">关闭</el-button>
          <el-button size="mini" type="text" icon="el-icon-copy-document" @click="handleCopy(scope.row)"
            v-hasPermi="['pathway:definition:add']">复制</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['pathway:definition:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 创建/编辑路径对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="路径名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入路径名称" />
        </el-form-item>
        <el-form-item label="病种" prop="diseaseTypeId">
          <el-select v-model="form.diseaseTypeId" placeholder="请选择病种">
            <el-option v-for="item in diseaseOptions" :key="item.diseaseId" :label="item.diseaseName"
              :value="item.diseaseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属科室" prop="deptId">
          <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择科室" />
        </el-form-item>
        <el-form-item label="路径描述" prop="researchSynopsis">
          <el-input v-model="form.researchSynopsis" type="textarea" placeholder="请输入路径描述" />
        </el-form-item>
        <el-form-item label="目标患者数" prop="targetPatientNum">
          <el-input-number v-model="form.targetPatientNum" :min="0" placeholder="目标管理患者数" />
        </el-form-item>
        <el-form-item label="退出标准" prop="exitCriteria">
          <el-input v-model="form.exitCriteria" type="textarea" placeholder="请输入退出标准" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPathway, getPathway, addPathway, updatePathway, delPathway, updatePathwayStatus, copyPathway } from '@/api/pathway/definition'
import { listDiseases } from '@/api/info/config'
import { treeselect } from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'PathwayList',
  components: { Treeselect },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      pathwayList: [],
      diseaseOptions: [],
      deptOptions: [],
      open: false,
      title: '',
      ids: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectName: null,
        diseaseTypeId: null,
        pathwayStatus: null
      },
      form: {},
      rules: {
        projectName: [{ required: true, message: '路径名称不能为空', trigger: 'blur' }],
        diseaseTypeId: [{ required: true, message: '请选择病种', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDiseases()
    this.getDeptTree()
  },
  methods: {
    getList() {
      this.loading = true
      listPathway(this.queryParams).then(response => {
        this.pathwayList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getDiseases() {
      listDiseases().then(response => {
        this.diseaseOptions = response.data || response.rows || []
      })
    },
    getDeptTree() {
      treeselect().then(response => {
        this.deptOptions = response.data
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '创建管理路径'
    },
    handleEdit(row) {
      this.reset()
      getPathway(row.id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '编辑管理路径'
      })
    },
    handleDetail(row) {
      this.$router.push({ path: '/pathway/detail/' + row.id })
    },
    handlePublish(row) {
      this.$modal.confirm('确认发布路径"' + row.projectName + '"？发布后患者可入组。').then(function () {
        return updatePathwayStatus(row.id, '1')
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('发布成功')
      })
    },
    handleClose(row) {
      this.$modal.confirm('确认关闭路径"' + row.projectName + '"？关闭后新患者无法入组。').then(function () {
        return updatePathwayStatus(row.id, '3')
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('关闭成功')
      })
    },
    handleCopy(row) {
      this.$modal.confirm('确认复制路径"' + row.projectName + '"？').then(function () {
        return copyPathway(row.id)
      }).then(response => {
        this.getList()
        this.$modal.msgSuccess('复制成功')
      })
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除路径"' + row.projectName + '"？').then(function () {
        return delPathway(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePathway(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addPathway(this.form).then(response => {
              this.$modal.msgSuccess('创建成功')
              this.open = false
              this.getList()
              // 跳转到创建向导继续配置节点
              const newId = response.data
              if (newId) {
                this.$router.push({ path: '/pathway/create/' + newId })
              }
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { targetPatientNum: 0 }
      this.resetForm('form')
    }
  }
}
</script>
