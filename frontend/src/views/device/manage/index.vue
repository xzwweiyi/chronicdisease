<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="设备编码" prop="deviceCode">
        <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编码" clearable />
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable />
      </el-form-item>
      <el-form-item label="设备类型" prop="deviceType">
        <el-select v-model="queryParams.deviceType" placeholder="请选择类型" clearable>
          <el-option label="血压计" value="血压计" />
          <el-option label="血糖仪" value="血糖仪" />
          <el-option label="血氧仪" value="血氧仪" />
          <el-option label="心电仪" value="心电仪" />
          <el-option label="体脂秤" value="体脂秤" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="空闲" value="0" />
          <el-option label="使用中" value="1" />
          <el-option label="维修中" value="2" />
          <el-option label="报废" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增设备</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" @click="handleMaintenance(scope.row)">报修</el-button>
          <el-button v-if="scope.row.status === '3'" type="text" size="mini" @click="handleScrap(scope.row)">报废确认</el-button>
          <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="deviceCode" label="设备编码" width="140" align="center" show-overflow-tooltip />
      <el-table-column prop="deviceName" label="设备名称" min-width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="deviceType" label="设备类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag size="small">{{ scope.row.deviceType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="manufacturer" label="厂家" width="100" align="center" />
      <el-table-column prop="model" label="型号" width="120" align="center" />
      <el-table-column prop="purchaseDate" label="购置日期" width="120" align="center" />
      <el-table-column prop="statusName" label="设备状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="deviceStatusType(scope.row.status)">{{ scope.row.statusName }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备编码" prop="deviceCode">
              <el-input v-model="form.deviceCode" placeholder="请输入设备编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备类型" prop="deviceType">
              <el-select v-model="form.deviceType" placeholder="请选择类型" style="width:100%">
                <el-option label="血压计" value="血压计" />
                <el-option label="血糖仪" value="血糖仪" />
                <el-option label="血氧仪" value="血氧仪" />
                <el-option label="心电仪" value="心电仪" />
                <el-option label="体脂秤" value="体脂秤" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="厂家" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入厂家" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购置日期" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'DeviceManage',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, deviceCode: '', deviceName: '', deviceType: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      tableData: [],
      form: { deviceCode: '', deviceName: '', deviceType: '', manufacturer: '', model: '', purchaseDate: '', remark: '' },
      rules: {
        deviceCode: [{ required: true, message: '请输入设备编码', trigger: 'blur' }],
        deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
        deviceType: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
        manufacturer: [{ required: true, message: '请输入厂家', trigger: 'blur' }],
        model: [{ required: true, message: '请输入型号', trigger: 'blur' }],
        purchaseDate: [{ required: true, message: '请选择购置日期', trigger: 'change' }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      var that = this
      setTimeout(function() {
        that.loading = false
        that.total = that.mockData().length
        var start = (that.queryParams.pageNum - 1) * that.queryParams.pageSize
        that.tableData = that.mockData().slice(start, start + that.queryParams.pageSize)
      }, 300)
    },
    mockData() {
      var statusMap = { '0': '空闲', '1': '使用中', '2': '维修中', '3': '报废' }
      return [
        { id: 1, deviceCode: 'DEV-BP-001', deviceName: '欧姆龙智能血压计', deviceType: '血压计', manufacturer: '欧姆龙', model: 'HEM-7121', purchaseDate: '2025-06-15', status: '1', statusName: '使用中' },
        { id: 2, deviceCode: 'DEV-BP-002', deviceName: '鱼跃蓝牙血压计', deviceType: '血压计', manufacturer: '鱼跃', model: '660A', purchaseDate: '2025-08-20', status: '0', statusName: '空闲' },
        { id: 3, deviceCode: 'DEV-BG-001', deviceName: '罗氏血糖仪', deviceType: '血糖仪', manufacturer: '罗氏', model: 'Accu-Chek Guide', purchaseDate: '2025-05-10', status: '1', statusName: '使用中' },
        { id: 4, deviceCode: 'DEV-BG-002', deviceName: '三诺血糖仪', deviceType: '血糖仪', manufacturer: '三诺', model: 'GA-3', purchaseDate: '2025-09-01', status: '1', statusName: '使用中' },
        { id: 5, deviceCode: 'DEV-OX-001', deviceName: '鱼跃便携血氧仪', deviceType: '血氧仪', manufacturer: '鱼跃', model: 'YX301', purchaseDate: '2025-07-12', status: '0', statusName: '空闲' },
        { id: 6, deviceCode: 'DEV-ECG-001', deviceName: '迈瑞动态心电记录仪', deviceType: '心电仪', manufacturer: '迈瑞', model: 'ECG-01A', purchaseDate: '2025-04-25', status: '1', statusName: '使用中' },
        { id: 7, deviceCode: 'DEV-BF-001', deviceName: '华为智能体脂秤', deviceType: '体脂秤', manufacturer: '华为', model: 'Smart Scale 3', purchaseDate: '2025-10-08', status: '2', statusName: '维修中' },
        { id: 8, deviceCode: 'DEV-BP-003', deviceName: '欧姆龙臂式血压计', deviceType: '血压计', manufacturer: '欧姆龙', model: 'HEM-7124', purchaseDate: '2024-03-18', status: '3', statusName: '报废' },
        { id: 9, deviceCode: 'DEV-BG-003', deviceName: '雅培血糖仪', deviceType: '血糖仪', manufacturer: '雅培', model: 'FreeStyle Lite', purchaseDate: '2026-01-15', status: '0', statusName: '空闲' },
        { id: 10, deviceCode: 'DEV-OX-002', deviceName: '康泰血氧仪', deviceType: '血氧仪', manufacturer: '康泰', model: 'CMS50D', purchaseDate: '2026-02-20', status: '1', statusName: '使用中' }
      ]
    },
    deviceStatusType(status) { var map = { '0': 'info', '1': 'success', '2': 'warning', '3': 'danger' }; return map[status] || 'info' },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '新增设备'
      this.isEdit = false
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { deviceCode: '', deviceName: '', deviceType: '', manufacturer: '', model: '', purchaseDate: '', remark: '' }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑设备'
      this.isEdit = true
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() { that.form = Object.assign({}, row) })
    },
    submitForm() {
      var that = this
      this.$refs.form.validate(function(valid) {
        if (valid) { that.$message.success(that.isEdit ? '修改成功' : '新增成功'); that.dialogVisible = false; that.getList() }
      })
    },
    handleMaintenance(row) {
      var that = this
      this.$prompt('请输入报修原因', '设备报修', { inputType: 'textarea' }).then(function() {
        row.status = '2'; row.statusName = '维修中'; that.$message.success('已报修')
      })
    },
    handleScrap(row) {
      var that = this
      this.$confirm('确认该设备已报废?', '报废确认', { type: 'warning' }).then(function() {
        row.status = '3'; row.statusName = '报废'; that.$message.success('已确认报废')
      })
    },
    handleDelete(row) {
      var that = this
      this.$confirm('确认删除设备"' + row.deviceName + '"?', '警告', { type: 'warning' }).then(function() {
        that.$message.success('删除成功'); that.getList()
      })
    },
    handleExport() { this.$message.success('导出成功') }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
</style>
