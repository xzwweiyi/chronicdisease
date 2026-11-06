<template>
  <div class="archive-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-label">{{ item.label }}</div>
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-compare">较上月 {{ item.vsLastMonth }}</div>
          </div>
          <div class="stat-icon" :style="{ background: item.color }">
            <i :class="item.icon" />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 慢病筛选标签 -->
    <div class="section-title"><span class="title-bar" />慢病病种分布</div>
    <div class="disease-tabs">
      <span v-for="d in diseaseFilters" :key="d.key"
        class="disease-tab" :class="{ active: activeDisease === d.key }"
        @click="switchDisease(d.key)">{{ d.label }}</span>
    </div>

    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
      <el-form-item label="居民姓名">
        <el-input v-model="queryParams.personName" placeholder="请输入居民姓名" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份证号">
        <el-input v-model="queryParams.cardId" placeholder="请输入身份证号" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport">导出</el-button></el-col>
    </el-row>

    <!-- 主数据表格 -->
    <div class="section-title"><span class="title-bar" />档案列表</div>
    <el-table v-loading="loading" :data="tableData" border stripe size="small" style="width:100%">
      <el-table-column prop="archiveCode" label="档案编码" width="110" align="center" />
      <el-table-column prop="personName" label="姓名" width="90" align="center" />
      <el-table-column prop="age" label="年龄" width="70" align="center" />
      <el-table-column label="性别" width="60" align="center">
        <template slot-scope="scope">{{ scope.row.genderSourceValue === '0' ? '男' : scope.row.genderSourceValue === '1' ? '女' : scope.row.genderSourceValue || '--' }}</template>
      </el-table-column>
      <el-table-column prop="cardId" label="身份证号" width="170" align="center" />
      <el-table-column prop="phoneNumber" label="联系电话" width="130" align="center" />
      <el-table-column prop="managingInstitution" label="管理机构" width="140" align="center" show-overflow-tooltip />
      <el-table-column prop="managingDoctor" label="管理医生" width="100" align="center" />
      <el-table-column prop="diseaseTypes" label="慢病类型" width="120" align="center">
        <template slot-scope="scope"><el-tag size="mini" v-if="scope.row.diseaseTypes">{{ scope.row.diseaseTypes }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="建档时间" width="140" align="center">
        <template slot-scope="scope">{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</template>
      </el-table-column>
      <el-table-column prop="archiveSource" label="档案来源" width="100" align="center" />
      <el-table-column prop="transferInDate" label="转入管理日期" width="120" align="center">
        <template slot-scope="scope">{{ parseTime(scope.row.transferInDate, '{y}-{m}-{d}') }}</template>
      </el-table-column>
      <el-table-column prop="isSigned" label="是否签约" width="90" align="center">
        <template slot-scope="scope"><el-tag :type="scope.row.isSigned === '1' ? 'success' : 'info'" size="small">{{ scope.row.isSigned === '1' ? '已签约' : scope.row.isSigned === '0' ? '未签约' : (scope.row.isSigned || '--') }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDetail(scope.row)">档案详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(scope.row)" />
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryParams.pageNum" :page-sizes="[10, 20, 50, 100]" :page-size="queryParams.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top:16px" />

    <!-- 各机构慢病分布 -->
    <div class="section-title" style="margin-top:24px"><span class="title-bar" />各机构慢病分布</div>
    <el-table :data="crossDistData" border stripe size="small" style="width:100%">
      <el-table-column label="操作" width="120" align="center"><template slot-scope="scope"><el-button size="mini" type="text" @click="viewInstitutionDetail(scope.row)">查看明细</el-button></template></el-table-column>
      <el-table-column prop="institutionName" label="机构名称" min-width="140" />
      <el-table-column prop="totalArchives" label="建档总数" width="100" align="center" />
      <el-table-column prop="newThisMonth" label="本月新增" width="100" align="center" />
      <el-table-column prop="hypertension" label="高血压" width="90" align="center" />
      <el-table-column prop="diabetes" label="糖尿病" width="90" align="center" />
      <el-table-column prop="stroke" label="脑卒中" width="90" align="center" />
      <el-table-column prop="chd" label="冠心病" width="90" align="center" />
      <el-table-column prop="copd" label="慢阻肺" width="90" align="center" />
    </el-table>

    <!-- 跨机构调取日志 -->
    <div class="section-title" style="margin-top:24px"><span class="title-bar" />跨机构调取</div>
    <el-table :data="crossOrgData" border stripe size="small" style="width:100%">
      <el-table-column label="操作" width="120" align="center"><template slot-scope="scope"><el-button size="mini" type="text">查看详情</el-button><el-button size="mini" type="text">查看明细</el-button></template></el-table-column>
      <el-table-column prop="person_name" label="姓名" width="80" align="center" />
      <el-table-column prop="card_id" label="身份证号" width="170" align="center" />
      <el-table-column prop="source_institution" label="管理机构" min-width="140" show-overflow-tooltip />
      <el-table-column prop="source_doctor" label="管理医生" width="100" align="center" />
      <el-table-column prop="target_institution" label="调取机构" min-width="140" show-overflow-tooltip />
      <el-table-column prop="target_doctor" label="调取医生" width="100" align="center" />
      <el-table-column prop="access_time" label="调取时间" width="160" align="center">
        <template slot-scope="scope">{{ parseTime(scope.row.access_time, '{y}-{m}-{d} {h}:{i}') }}</template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="居民姓名" prop="personName"><el-input v-model="form.personName" placeholder="请输入" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="身份证号" prop="cardId"><el-input v-model="form.cardId" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="性别"><el-select v-model="form.gender" style="width:100%"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="年龄"><el-input-number v-model="form.age" :min="0" :max="150" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="联系电话"><el-input v-model="form.phone" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="居住地址"><el-input v-model="form.address" placeholder="请输入" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="管理机构"><el-input v-model="form.managingInstitution" placeholder="请输入" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="管理医生"><el-input v-model="form.managingDoctor" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="慢病类型"><el-input v-model="form.diseaseTypes" placeholder="多个用逗号分隔" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="档案来源"><el-select v-model="form.archiveSource" style="width:100%"><el-option label="筛查建档" value="筛查建档" /><el-option label="HIS系统" value="HIS系统" /><el-option label="社区转入" value="社区转入" /><el-option label="体检发现" value="体检发现" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="是否签约"><el-select v-model="form.isSigned" style="width:100%"><el-option label="已签约" value="已签约" /><el-option label="未签约" value="未签约" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="备注"><el-input v-model="form.remark" placeholder="请输入" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="档案管理详情" :visible.sync="detailVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="档案编码">{{ detailData.archiveCode || '--' }}</el-descriptions-item>
        <el-descriptions-item label="居民姓名">{{ detailData.personName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.genderSourceValue }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailData.age }}岁</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.cardId }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phoneNumber }}</el-descriptions-item>
        <el-descriptions-item label="居住地址">{{ detailData.address }}</el-descriptions-item>
        <el-descriptions-item label="管理机构">{{ detailData.managingInstitution || '--' }}</el-descriptions-item>
        <el-descriptions-item label="管理医生">{{ detailData.managingDoctor || '--' }}</el-descriptions-item>
        <el-descriptions-item label="慢病类型">{{ detailData.diseaseTypes || '--' }}</el-descriptions-item>
        <el-descriptions-item label="档案来源">{{ detailData.archiveSource || '--' }}</el-descriptions-item>
        <el-descriptions-item label="建档时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="是否签约">{{ detailData.isSigned || '--' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '--' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listArchive, getArchiveStats, getArchiveDistribution, getCrossOrgLog, addArchive, updateArchive, delArchive } from '@/api/resident/archive'

export default {
  name: 'ResidentArchive',
  data() {
    return {
      loading: false, dialogVisible: false, detailVisible: false, dialogTitle: '', total: 0,
      activeDisease: '',
      statCards: [
        { label: '患者总数', value: '0', vsLastMonth: '+0%', icon: 'el-icon-user-solid', color: 'linear-gradient(135deg, #1890ff, #096dd9)' },
        { label: '本月新增', value: '0', vsLastMonth: '+0%', icon: 'el-icon-plus', color: 'linear-gradient(135deg, #52c41a, #389e0d)' },
        { label: '档案完成率', value: '0%', vsLastMonth: '+0%', icon: 'el-icon-document-checked', color: 'linear-gradient(135deg, #faad14, #d48806)' },
        { label: '跨机构调取', value: '0', vsLastMonth: '+0%', icon: 'el-icon-connection', color: 'linear-gradient(135deg, #ff4d4f, #cf1322)' }
      ],
      diseaseFilters: [
        { key: '', label: '全部' }, { key: '高血压', label: '高血压' }, { key: '糖尿病', label: '糖尿病' },
        { key: '脑卒中', label: '脑卒中' }, { key: '冠心病', label: '冠心病' }, { key: '慢阻肺', label: '慢阻肺' }
      ],
      queryParams: { pageNum: 1, pageSize: 10, personName: '', cardId: '', params: {} },
      form: {}, detailData: {},
      rules: {
        personName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        cardId: [{ required: true, message: '请输入身份证号', trigger: 'blur' }]
      },
      tableData: [], crossDistData: [], crossOrgData: []
    }
  },
  created() { this.getList(); this.fetchStats(); this.fetchDistribution(); this.fetchCrossOrgLog() },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.activeDisease) { params.diseaseTypes = this.activeDisease }
      listArchive(params).then(res => { this.tableData = res.rows; this.total = res.total; this.loading = false }).catch(() => { this.loading = false })
    },
    fetchStats() {
      getArchiveStats().then(res => {
        const d = res.data
        if (d) {
          this.statCards[0].value = String(d.totalPatients || 0)
          this.statCards[1].value = String(d.newThisMonth || 0)
          this.statCards[2].value = (d.completionRate || 0) + '%'
          this.statCards[3].value = String(d.crossInstitution || 0)
        }
      }).catch(() => {})
    },
    fetchDistribution() { getArchiveDistribution().then(res => { this.crossDistData = res.data || [] }).catch(() => {}) },
    fetchCrossOrgLog() { getCrossOrgLog().then(res => { this.crossOrgData = res.data || [] }).catch(() => {}) },
    switchDisease(key) { this.activeDisease = key; this.handleQuery() },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.queryParams = { pageNum: 1, pageSize: 10, personName: '', cardId: '', params: {} }; this.activeDisease = ''; this.handleQuery() },
    handleAdd() { this.dialogTitle = '新增档案'; this.form = { personName: '', cardId: '', gender: '', age: null, phone: '', address: '', managingInstitution: '', managingDoctor: '', diseaseTypes: '', archiveSource: '', isSigned: '', remark: '' }; this.dialogVisible = true; this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() }) },
    handleEdit(row) { this.dialogTitle = '编辑档案'; this.form = { ...row }; this.dialogVisible = true; this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() }) },
    handleDetail(row) { this.$router.push('/resident-archive-detail/' + row.cardId) },
    handleDelete(row) { this.$confirm('确认删除该条记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { delArchive(row.id).then(() => { this.$message.success('删除成功'); this.getList() }) }).catch(() => {}) },
    submitForm() { this.$refs.form.validate(valid => { if (valid) { const api = this.form.id ? updateArchive : addArchive; api(this.form).then(() => { this.$message.success(this.form.id ? '编辑成功' : '新增成功'); this.dialogVisible = false; this.getList() }) } }) },
    handleExport() { this.$message.info('导出功能开发中') },
    viewInstitutionDetail(row) { this.$message.info('机构明细查看：' + row.institutionName) },
    handleSizeChange(v) { this.queryParams.pageSize = v; this.getList() },
    handleCurrentChange(v) { this.queryParams.pageNum = v; this.getList() }
  }
}
</script>

<style scoped>
.archive-container { background: #F2F7FB; min-height: 100%; padding: 16px; }
.stat-row { margin-bottom: 16px; }
.stat-card { background: #fff; border: 1px solid #F2F2F2; border-radius: 8px; padding: 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.stat-label { font-size: 13px; color: #8c8c8c; margin-bottom: 6px; }
.stat-value { font-size: 28px; font-weight: 700; font-family: Arial; color: #333; }
.stat-compare { font-size: 12px; color: #00CC33; margin-top: 4px; }
.stat-icon { width: 50px; height: 50px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 22px; }
.section-title { font-size: 16px; color: #333; margin-bottom: 12px; display: flex; align-items: center; font-weight: 400; }
.title-bar { display: inline-block; width: 6px; height: 20px; background: #3A8CFF; border-radius: 8px; margin-right: 10px; }
.disease-tabs { display: flex; gap: 0; margin-bottom: 16px; }
.disease-tab { display: inline-block; padding: 10px 24px; cursor: pointer; font-size: 14px; color: #666; background: #fff; border: 1px solid #F2F2F2; border-bottom: 2px solid transparent; transition: .2s; margin-right: 0; }
.disease-tab:hover { color: #2984F8; }
.disease-tab.active { color: #2984F8; border-bottom-color: #2984F8; font-weight: 500; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 10px; }
.mb8 { margin-bottom: 8px; }
</style>
