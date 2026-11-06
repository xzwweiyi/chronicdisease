<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="管理路径" prop="pathwayId">
        <el-select v-model="queryParams.pathwayId" placeholder="请选择路径" clearable @change="handleQuery" style="width:220px;">
          <el-option v-for="item in pathwayOptions" :key="item.id" :label="item.projectName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="患者姓名" prop="patientName">
        <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份证号" prop="idCard">
        <el-input v-model="queryParams.idCard" placeholder="请输入身份证号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="入组状态" prop="enrollStatus">
        <el-select v-model="queryParams.enrollStatus" placeholder="请选择状态" clearable>
          <el-option label="已入组" value="1" />
          <el-option label="待审核" value="0" />
          <el-option label="已退出" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-search" size="mini" @click="handleEsFilter" :disabled="!queryParams.pathwayId">筛选入组</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-upload2" size="mini" @click="handleQuickImport">快速导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-user" size="mini" :disabled="!selectedIds.length" @click="handleAssignDoctor">分配医生</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="!selectedIds.length" @click="handleBatchDelete">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <div v-if="!queryParams.pathwayId" class="empty-hint">
      <el-empty description="请先选择一个管理路径" :image-size="120" />
    </div>

    <el-table v-else v-loading="loading" :data="patientList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="patientName" show-overflow-tooltip min-width="80" />
      <el-table-column label="年龄" align="center" prop="age" width="60" />
      <el-table-column label="性别" align="center" prop="gender" width="60">
        <template slot-scope="scope">
          <span>{{ scope.row.gender === '1' ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="身份证号" align="center" prop="idCard" width="180" show-overflow-tooltip />
      <el-table-column label="联系电话" align="center" prop="phone" min-width="120" />
      <el-table-column label="病种" align="center" prop="diseaseName" min-width="100" />
      <el-table-column label="管理机构" align="center" prop="orgName" min-width="120" show-overflow-tooltip />
      <el-table-column label="入组时间" align="center" prop="enrollTime" width="160" />
      <el-table-column label="入组状态" align="center" prop="enrollStatus" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.enrollStatus === '1'" type="success">已入组</el-tag>
          <el-tag v-else-if="scope.row.enrollStatus === '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="scope.row.enrollStatus === '2'" type="info">已退出</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
          <el-button v-if="scope.row.enrollStatus === '0'" size="mini" type="text" icon="el-icon-check" @click="handleApprove(scope.row)">通过</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- ES筛选入组对话框 -->
    <el-dialog title="ES条件筛选入组" :visible.sync="esOpen" width="750px" append-to-body>
      <el-form ref="esForm" :model="esForm" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="esForm.gender" placeholder="不限" clearable style="width:100%;">
                <el-option label="男" value="1" />
                <el-option label="女" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄范围">
              <el-input-number v-model="esForm.ageMin" :min="0" :max="150" placeholder="最小" style="width:100px;" />
              <span style="margin:0 8px;">-</span>
              <el-input-number v-model="esForm.ageMax" :min="0" :max="150" placeholder="最大" style="width:100px;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="病种">
              <el-select v-model="esForm.diseaseId" placeholder="请选择病种" clearable style="width:100%;">
                <el-option label="高血压" value="1" />
                <el-option label="糖尿病" value="2" />
                <el-option label="脑卒中" value="3" />
                <el-option label="冠心病" value="4" />
                <el-option label="慢阻肺" value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理机构">
              <el-input v-model="esForm.orgName" placeholder="请输入管理机构" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="诊断时间范围">
              <el-date-picker v-model="esForm.diagnoseDateRange" type="daterange" range-separator="-" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" style="width:100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否有并发症">
              <el-select v-model="esForm.hasComplication" placeholder="不限" clearable style="width:100%;">
                <el-option label="是" value="1" />
                <el-option label="否" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="关键词搜索">
              <el-input v-model="esForm.keyword" placeholder="病历关键词、症状描述等" clearable />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div v-if="esResultTotal > 0" class="es-result-info">
        <el-alert :title="'筛选到 ' + esResultTotal + ' 名符合条件的患者'" type="success" :closable="false" show-icon />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="executeEsSearch">执行筛选</el-button>
        <el-button type="success" @click="batchEnroll" :disabled="esResultTotal === 0">批量入组</el-button>
        <el-button @click="esOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 快速导入对话框 -->
    <el-dialog title="快速导入患者" :visible.sync="importOpen" width="600px" append-to-body>
      <el-form ref="importForm" :model="importForm" label-width="100px">
        <el-form-item label="导入方式">
          <el-radio-group v-model="importForm.importType">
            <el-radio label="manual">手动输入</el-radio>
            <el-radio label="file">文件上传</el-radio>
          </el-radio-group>
        </el-form-item>
        <template v-if="importForm.importType === 'manual'">
          <el-form-item label="患者信息">
            <el-input v-model="importForm.patientIdsOrCards" type="textarea" :rows="4" placeholder="请输入患者ID或身份证号，每行一个或用逗号分隔" />
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item label="上传文件">
            <el-upload action="#" :auto-upload="false" :on-change="handleFileChange" :limit="1" accept=".xlsx,.xls,.csv">
              <el-button size="small" type="primary" icon="el-icon-upload">选择文件</el-button>
              <div slot="tip" class="el-upload__tip">支持 .xlsx, .xls, .csv 格式，文件需包含身份证号列</div>
            </el-upload>
          </el-form-item>
        </template>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="doImport">确认导入</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配医生对话框 -->
    <el-dialog title="分配管理医生" :visible.sync="assignOpen" width="500px" append-to-body>
      <el-form ref="assignForm" :model="assignForm" label-width="100px">
        <el-form-item label="选择医生">
          <el-select v-model="assignForm.doctorId" placeholder="请选择管理医生" style="width:100%;" filterable>
            <el-option v-for="item in doctorOptions" :key="item.id" :label="item.doctorName + ' - ' + item.deptName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分配说明">
          <el-input v-model="assignForm.remark" type="textarea" :rows="2" placeholder="可选填分配说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="doAssign">确认分配</el-button>
        <el-button @click="assignOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PathwayEnrollment',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      selectedIds: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pathwayId: null,
        patientName: null,
        idCard: null,
        enrollStatus: null
      },
      pathwayOptions: [],
      patientList: [],
      esOpen: false,
      esForm: {
        gender: null,
        ageMin: null,
        ageMax: null,
        diseaseId: null,
        orgName: null,
        diagnoseDateRange: null,
        hasComplication: null,
        keyword: null
      },
      esResultTotal: 0,
      importOpen: false,
      importForm: { importType: 'manual', patientIdsOrCards: '' },
      assignOpen: false,
      assignForm: { doctorId: null, remark: '' },
      doctorOptions: []
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
    getList() {
      if (!this.queryParams.pathwayId) {
        this.patientList = []
        this.total = 0
        return
      }
      this.loading = true
      setTimeout(() => {
        const mockList = [
          { id: '1', patientName: '张三', age: 58, gender: '1', idCard: '610102196804152536', phone: '13800138001', diseaseName: '高血压', orgName: '城关区社区卫生服务中心', enrollTime: '2026-01-15 10:30', enrollStatus: '1' },
          { id: '2', patientName: '李丽', age: 45, gender: '2', idCard: '610102198106123245', phone: '13900139002', diseaseName: '糖尿病', orgName: '七里河区人民医院', enrollTime: '2026-02-20 09:15', enrollStatus: '1' },
          { id: '3', patientName: '王建国', age: 62, gender: '1', idCard: '610102196407082618', phone: '13700137003', diseaseName: '脑卒中', orgName: '西固区中心卫生院', enrollTime: '2026-03-10 14:20', enrollStatus: '1' },
          { id: '4', patientName: '赵敏', age: 38, gender: '2', idCard: '610102198809051527', phone: '13600136004', diseaseName: '冠心病', orgName: '安宁区社区卫生服务中心', enrollTime: '2026-04-05 11:45', enrollStatus: '0' },
          { id: '5', patientName: '刘志强', age: 55, gender: '1', idCard: '610102197111223019', phone: '13500135005', diseaseName: '高血压', orgName: '城关区社区卫生服务中心', enrollTime: '2026-05-18 16:00', enrollStatus: '1' },
          { id: '6', patientName: '孙芳', age: 50, gender: '2', idCard: '610102197603144168', phone: '13400134006', diseaseName: '糖尿病', orgName: '七里河区人民医院', enrollTime: '2026-06-01 08:30', enrollStatus: '2' },
          { id: '7', patientName: '陈伟', age: 67, gender: '1', idCard: '610102195903148712', phone: '13300133007', diseaseName: '慢阻肺', orgName: '安宁区社区卫生服务中心', enrollTime: '2026-06-20 10:00', enrollStatus: '1' }
        ]
        let filtered = mockList
        if (this.queryParams.patientName) {
          filtered = filtered.filter(item => item.patientName.indexOf(this.queryParams.patientName) > -1)
        }
        if (this.queryParams.idCard) {
          filtered = filtered.filter(item => item.idCard.indexOf(this.queryParams.idCard) > -1)
        }
        if (this.queryParams.enrollStatus) {
          filtered = filtered.filter(item => item.enrollStatus === this.queryParams.enrollStatus)
        }
        this.patientList = filtered
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
    handleEsFilter() {
      this.esForm = { gender: null, ageMin: null, ageMax: null, diseaseId: null, orgName: null, diagnoseDateRange: null, hasComplication: null, keyword: null }
      this.esResultTotal = 0
      this.esOpen = true
    },
    executeEsSearch() {
      this.$modal.loading('正在ES筛选患者...')
      setTimeout(() => {
        this.$modal.closeLoading()
        const randomCount = Math.floor(Math.random() * 80) + 20
        this.esResultTotal = randomCount
      }, 800)
    },
    batchEnroll() {
      this.$modal.confirm('确认将筛选到的 ' + this.esResultTotal + ' 名患者批量入组？').then(() => {
        this.$modal.msgSuccess('成功入组 ' + this.esResultTotal + ' 名患者')
        this.esOpen = false
        this.getList()
      }).catch(() => {})
    },
    handleQuickImport() {
      this.importForm = { importType: 'manual', patientIdsOrCards: '' }
      this.importOpen = true
    },
    handleFileChange(file) {
      this.importForm.fileName = file.name
    },
    doImport() {
      this.$modal.loading('正在导入患者...')
      setTimeout(() => {
        this.$modal.closeLoading()
        this.$modal.msgSuccess('导入成功，新增15名患者')
        this.importOpen = false
        this.getList()
      }, 600)
    },
    handleAssignDoctor() {
      this.doctorOptions = [
        { id: '1', doctorName: '王医生', deptName: '心内科' },
        { id: '2', doctorName: '李医生', deptName: '内分泌科' },
        { id: '3', doctorName: '张医生', deptName: '神经内科' },
        { id: '4', doctorName: '刘医生', deptName: '呼吸内科' }
      ]
      this.assignForm = { doctorId: null, remark: '' }
      this.assignOpen = true
    },
    doAssign() {
      if (!this.assignForm.doctorId) {
        this.$modal.msgError('请选择管理医生')
        return
      }
      this.$modal.msgSuccess('成功为 ' + this.selectedIds.length + ' 名患者分配医生')
      this.assignOpen = false
    },
    handleExport() {
      this.$modal.loading('正在导出入组数据...')
      setTimeout(() => {
        this.$modal.closeLoading()
        this.$modal.msgSuccess('导出成功')
      }, 500)
    },
    handleView(row) {
      this.$modal.msgSuccess('查看患者详情: ' + row.patientName)
    },
    handleApprove(row) {
      this.$modal.confirm('确认通过患者"' + row.patientName + '"的入组申请？').then(() => {
        row.enrollStatus = '1'
        this.$modal.msgSuccess('已通过入组申请')
      }).catch(() => {})
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除患者"' + row.patientName + '"的入组记录？').then(() => {
        this.patientList = this.patientList.filter(item => item.id !== row.id)
        this.total = this.patientList.length
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleBatchDelete() {
      this.$modal.confirm('确认删除选中的 ' + this.selectedIds.length + ' 条入组记录？').then(() => {
        this.patientList = this.patientList.filter(item => !this.selectedIds.includes(item.id))
        this.total = this.patientList.length
        this.selectedIds = []
        this.$modal.msgSuccess('批量删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.empty-hint { padding: 80px 0; text-align: center; }
.es-result-info { margin-top: 16px; }
</style>
