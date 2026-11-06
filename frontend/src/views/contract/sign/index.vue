<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="管理机构" prop="managingInstitution">
        <el-select v-model="queryParams.managingInstitution" placeholder="请选择管理机构" clearable filterable>
          <el-option v-for="item in institutionOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="签约日期" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item label="管理医生" prop="managingDoctor">
        <el-select v-model="queryParams.managingDoctor" placeholder="请选择管理医生" clearable filterable>
          <el-option v-for="item in doctorOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="签约状态" prop="contractStatus">
        <el-select v-model="queryParams.contractStatus" placeholder="请选择状态" clearable>
          <el-option label="待生效" value="0" />
          <el-option label="生效中" value="1" />
          <el-option label="已到期" value="2" />
          <el-option label="已解约" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="病种" prop="diseaseTypes">
        <el-select v-model="queryParams.diseaseTypes" placeholder="请选择病种" clearable>
          <el-option label="高血压" value="高血压" />
          <el-option label="糖尿病" value="糖尿病" />
          <el-option label="脑卒中" value="脑卒中" />
          <el-option label="冠心病" value="冠心病" />
          <el-option label="慢阻肺" value="慢阻肺" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增签约</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">详情</el-button>
          <el-button v-if="scope.row.contractStatus === '1'" type="text" size="mini" style="color:#F56C6C" @click="handleCancel(scope.row)">解约</el-button>
          <el-button v-if="scope.row.contractStatus === '2'" type="text" size="mini" style="color:#67C23A" @click="handleRenew(scope.row)">续约</el-button>
          <el-button v-if="scope.row.contractStatus === '1'" type="text" size="mini" style="color:#E6A23C" @click="handleAdjust(scope.row)">调整</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column label="姓名" width="100" align="center">
        <template slot-scope="scope">{{ personName(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="年龄" width="60" align="center">
        <template slot-scope="scope">{{ ageLabel(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="性别" width="60" align="center">
        <template slot-scope="scope">{{ genderLabel(scope.row) }}</template>
      </el-table-column>
      <el-table-column prop="idCard" label="身份证号" width="180" align="center" show-overflow-tooltip />
      <el-table-column label="联系电话" width="130" align="center">
        <template slot-scope="scope">{{ phoneLabel(scope.row) }}</template>
      </el-table-column>
      <el-table-column prop="signDate" label="签约时间" width="120" align="center" />
      <el-table-column label="签约周期" width="220" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.startDate && scope.row.endDate">{{ scope.row.startDate }} ~ {{ scope.row.endDate }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="diseaseTypes" label="病种" width="120" align="center" show-overflow-tooltip />
      <el-table-column label="签约状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.contractStatus)">{{ statusMap[scope.row.contractStatus] || scope.row.contractStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="managingInstitution" label="管理机构" width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="managingDoctor" label="管理医生" width="100" align="center" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑签约对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" append-to-body @close="dialogClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="居民姓名" prop="residentName">
              <el-input v-model="form.residentName" placeholder="请输入居民姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签约机构" prop="managingInstitution">
              <el-select v-model="form.managingInstitution" placeholder="请选择机构" style="width:100%" filterable>
                <el-option v-for="item in institutionOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签约团队" prop="teamId">
              <el-select v-model="form.teamId" placeholder="请选择签约团队" style="width:100%">
                <el-option v-for="t in teamOptions" :key="t.id" :label="t.teamName" :value="t.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主管医生" prop="managingDoctor">
              <el-select v-model="form.managingDoctor" placeholder="请选择主管医生" style="width:100%" filterable>
                <el-option v-for="item in doctorOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务包" prop="packageId">
              <el-select v-model="form.packageId" placeholder="请选择服务包" style="width:100%" @change="onPackageChange">
                <el-option v-for="p in packageOptions" :key="p.id" :label="p.packageName + ' (¥' + (p.price || 0) + ')'" :value="p.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管理级别" prop="managementLevel">
              <el-select v-model="form.managementLevel" placeholder="请选择管理级别" style="width:100%">
                <el-option label="Ⅰ级" value="Ⅰ级" />
                <el-option label="Ⅱ级" value="Ⅱ级" />
                <el-option label="Ⅲ级" value="Ⅲ级" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签约开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签约结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签约日期" prop="signDate">
              <el-date-picker v-model="form.signDate" type="date" placeholder="选择签约日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签约类型" prop="signType">
              <el-select v-model="form.signType" placeholder="请选择签约类型" style="width:100%">
                <el-option label="首签" value="首签" />
                <el-option label="续签" value="续签" />
                <el-option label="调整" value="调整" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 服务包信息预览 -->
        <el-form-item v-if="selectedPackage" label="服务包信息">
          <el-tag type="success" style="margin-right:8px">{{ selectedPackage.packageName }}</el-tag>
          <span style="color:#999">费用：¥{{ selectedPackage.price || '0.00' }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog :title="detailTitle" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions v-if="detailData" :column="2" border size="small">
        <el-descriptions-item label="签约编号">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="签约状态">
          <el-tag :type="statusTagType(detailData.contractStatus)">{{ statusMap[detailData.contractStatus] || detailData.contractStatus }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="居民姓名">{{ personName(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ genderLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ ageLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.idCard }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ phoneLabel(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="签约团队">{{ detailData.teamName }}</el-descriptions-item>
        <el-descriptions-item label="服务包">{{ detailData.packageName }}</el-descriptions-item>
        <el-descriptions-item label="签约日期">{{ detailData.signDate }}</el-descriptions-item>
        <el-descriptions-item label="签约周期">{{ detailData.startDate }} ~ {{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="签约类型">{{ detailData.signType }}</el-descriptions-item>
        <el-descriptions-item label="管理机构" :span="2">{{ detailData.managingInstitution }}</el-descriptions-item>
        <el-descriptions-item label="管理医生">{{ detailData.managingDoctor }}</el-descriptions-item>
        <el-descriptions-item label="慢病类型">{{ detailData.diseaseTypes }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 续约对话框 -->
    <el-dialog title="续约" :visible.sync="renewVisible" width="500px" append-to-body>
      <el-form ref="renewForm" :model="renewForm" label-width="110px" size="small">
        <el-form-item label="签约开始日期" prop="startDate">
          <el-date-picker v-model="renewForm.startDate" type="date" placeholder="选择开始日期" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="签约结束日期" prop="endDate">
          <el-date-picker v-model="renewForm.endDate" type="date" placeholder="选择结束日期" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="服务包" prop="packageId">
          <el-select v-model="renewForm.packageId" placeholder="请选择服务包" style="width:100%">
            <el-option v-for="p in packageOptions" :key="p.id" :label="p.packageName" :value="p.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="renewVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRenew">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSign, getSign, addSign, updateSign, delSign } from '@/api/contract/index'
import { listTeam } from '@/api/contract/index'
import { listPackage } from '@/api/contract/index'
import Pagination from '@/components/Pagination'
import request from '@/utils/request'

export default {
  name: 'ContractSign',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      dateRange: [],
      statusMap: { '0': '待生效', '1': '生效中', '2': '已到期', '3': '已解约' },
      queryParams: { pageNum: 1, pageSize: 10, residentName: '', managingInstitution: '', managingDoctor: '', contractStatus: '', diseaseTypes: '' },
      dialogVisible: false,
      dialogTitle: '',
      detailVisible: false,
      detailTitle: '',
      detailData: null,
      renewVisible: false,
      renewForm: { startDate: '', endDate: '', packageId: null },
      renewRow: null,
      tableData: [],
      institutionOptions: [],
      doctorOptions: [],
      teamOptions: [],
      packageOptions: [],
      selectedPackage: null,
      form: { residentName: '', managingInstitution: '', teamId: null, managingDoctor: '', packageId: null, managementLevel: '', startDate: '', endDate: '', signDate: '', signType: '首签', remark: '' },
      rules: {
        residentName: [{ required: true, message: '请输入居民姓名', trigger: 'blur' }],
        teamId: [{ required: true, message: '请选择签约团队', trigger: 'change' }],
        packageId: [{ required: true, message: '请选择服务包', trigger: 'change' }],
        startDate: [{ required: true, message: '请选择服务开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择服务结束日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.fetchDropdownData()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginSignDate = this.dateRange[0]
        params.endSignDate = this.dateRange[1]
      }
      delete params.dateRange
      listSign(params).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    fetchDropdownData() {
      // 管理机构列表
      request({ url: '/contract/sign/institutions', method: 'get' }).then(res => {
        this.institutionOptions = res.data || []
      }).catch(() => {})
      // 管理医生列表
      request({ url: '/contract/sign/doctors', method: 'get' }).then(res => {
        this.doctorOptions = res.data || []
      }).catch(() => {})
      // 签约团队列表
      listTeam({ pageNum: 1, pageSize: 100 }).then(res => {
        this.teamOptions = res.rows || []
      }).catch(() => {})
      // 服务包列表
      listPackage({ pageNum: 1, pageSize: 100 }).then(res => {
        this.packageOptions = res.rows || []
      }).catch(() => {})
    },
    statusTagType(status) {
      const map = { '0': 'warning', '1': 'success', '2': 'info', '3': 'danger' }
      return map[status] || 'info'
    },
    // 人口学信息优先取 mp_person 实时字段，resident_archive 副本仅作兜底
    personName(row) {
      return (row && (row.mpPersonName || row.residentName)) || '-'
    },
    genderLabel(row) {
      const v = row && row.mpGenderSourceValue
      if (v === '0') return '男'
      if (v === '1') return '女'
      if (row && row.mpSex) return row.mpSex
      return (row && row.gender) || '-'
    },
    phoneLabel(row) {
      return (row && (row.mpPhoneNumber || row.phone)) || '-'
    },
    ageLabel(row) {
      const idCard = row && (row.idCard || '')
      if (idCard && idCard.length === 18) {
        const birth = idCard.substring(6, 14)
        if (/^\d{8}$/.test(birth)) {
          const year = parseInt(birth.substring(0, 4), 10)
          const month = parseInt(birth.substring(4, 6), 10) - 1
          const day = parseInt(birth.substring(6, 8), 10)
          const birthDate = new Date(year, month, day)
          const now = new Date()
          let age = now.getFullYear() - birthDate.getFullYear()
          if (now.getMonth() < birthDate.getMonth() || (now.getMonth() === birthDate.getMonth() && now.getDate() < birthDate.getDate())) {
            age--
          }
          return age >= 0 ? age : '-'
        }
      }
      return (row && row.age != null) ? row.age : '-'
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() {
      this.dateRange = []
      this.$refs.queryForm.resetFields()
      this.queryParams = { pageNum: 1, pageSize: 10, residentName: '', managingInstitution: '', managingDoctor: '', contractStatus: '', diseaseTypes: '' }
      this.getList()
    },
    onPackageChange(val) {
      this.selectedPackage = this.packageOptions.find(p => p.id === val) || null
    },
    handleAdd() {
      this.dialogTitle = '新增签约'
      this.dialogVisible = true
      this.selectedPackage = null
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.resetFields()
        this.form = { residentName: '', managingInstitution: '', teamId: null, managingDoctor: '', packageId: null, managementLevel: '', startDate: '', endDate: '', signDate: '', signType: '首签', remark: '' }
      })
    },
    dialogClose() {
      this.selectedPackage = null
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const data = { ...this.form }
          if (data.signDate) { data.signDate = data.signDate + ' 00:00:00' }
          if (data.startDate) { data.startDate = data.startDate + ' 00:00:00' }
          if (data.endDate) { data.endDate = data.endDate + ' 00:00:00' }
          data.contractStatus = '0'
          addSign(data).then(() => {
            this.$message.success('新增签约成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleDetail(row) {
      this.detailTitle = '签约详情 - ' + this.personName(row)
      getSign(row.id).then(res => {
        this.detailData = res.data
        this.detailVisible = true
      })
    },
    handleCancel(row) {
      this.$confirm('确认对"' + this.personName(row) + '"执行解约操作?', '警告', { type: 'warning' }).then(() => {
        updateSign({ id: row.id, contractStatus: '3' }).then(() => {
          this.$message.success('已解约')
          this.getList()
        })
      }).catch(() => {})
    },
    handleRenew(row) {
      this.renewRow = row
      this.renewForm = { startDate: '', endDate: '', packageId: row.packageId }
      this.renewVisible = true
    },
    submitRenew() {
      if (!this.renewForm.startDate || !this.renewForm.endDate) {
        this.$message.warning('请选择签约周期')
        return
      }
      updateSign({
        id: this.renewRow.id,
        startDate: this.renewForm.startDate + ' 00:00:00',
        endDate: this.renewForm.endDate + ' 00:00:00',
        packageId: this.renewForm.packageId,
        contractStatus: '1',
        signType: '续签'
      }).then(() => {
        this.$message.success('续约成功')
        this.renewVisible = false
        this.getList()
      })
    },
    handleAdjust(row) {
      this.dialogTitle = '调整签约 - ' + this.personName(row)
      this.dialogVisible = true
      this.$nextTick(() => {
        this.form = {
          id: row.id,
          residentName: row.residentName,
          managingInstitution: row.managingInstitution,
          teamId: row.teamId,
          managingDoctor: row.managingDoctor,
          packageId: row.packageId,
          managementLevel: '',
          startDate: row.startDate,
          endDate: row.endDate,
          signDate: '',
          signType: '调整',
          remark: ''
        }
        this.selectedPackage = this.packageOptions.find(p => p.id === row.packageId) || null
      })
    },
    handleExport() {
      this.$message.info('导出功能开发中')
    }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; margin-bottom: 10px; }
.mb8 { margin: 10px 0; }
</style>
