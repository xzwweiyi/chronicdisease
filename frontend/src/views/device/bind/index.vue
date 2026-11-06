<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="设备编码" prop="deviceCode">
        <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编码" clearable />
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable />
      </el-form-item>
      <el-form-item label="绑定居民" prop="bindUser">
        <el-input v-model="queryParams.bindUser" placeholder="请输入居民姓名" clearable />
      </el-form-item>
      <el-form-item label="绑定状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="已绑定" value="1" />
          <el-option label="已解绑" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增绑定</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-connection" size="mini" @click="handleScanBind">扫码绑定</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status === '1'" type="text" size="mini" style="color:#F56C6C" @click="handleUnbind(scope.row)">解绑</el-button>
          <el-button v-if="scope.row.status === '0'" type="text" size="mini" @click="handleRebind(scope.row)">重新绑定</el-button>
          <el-button type="text" size="mini" @click="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="deviceCode" label="设备编码" width="140" align="center" show-overflow-tooltip />
      <el-table-column prop="deviceName" label="设备名称" width="160" align="center" show-overflow-tooltip />
      <el-table-column prop="bindUser" label="绑定居民" width="100" align="center" />
      <el-table-column prop="bindDate" label="绑定日期" width="120" align="center" />
      <el-table-column prop="statusName" label="绑定状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">{{ scope.row.statusName }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增绑定对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-form-item label="设备编码" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备编码或扫码获取" />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="自动获取设备名称" :disabled="true" />
        </el-form-item>
        <el-form-item label="绑定居民" prop="bindUser">
          <el-select v-model="form.bindUser" placeholder="请选择绑定居民" filterable style="width:100%">
            <el-option label="王建国 - 高血压" value="王建国" />
            <el-option label="李秀兰 - 糖尿病" value="李秀兰" />
            <el-option label="张明华 - 冠心病" value="张明华" />
            <el-option label="刘桂英 - 高血压" value="刘桂英" />
            <el-option label="赵志强 - 慢阻肺" value="赵志强" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="绑定详情" :visible.sync="detailVisible" width="550px" append-to-body>
      <el-descriptions v-if="detailData" :column="2" border size="small">
        <el-descriptions-item label="设备编码">{{ detailData.deviceCode }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ detailData.deviceName }}</el-descriptions-item>
        <el-descriptions-item label="绑定居民">{{ detailData.bindUser }}</el-descriptions-item>
        <el-descriptions-item label="绑定状态">{{ detailData.statusName }}</el-descriptions-item>
        <el-descriptions-item label="绑定日期">{{ detailData.bindDate }}</el-descriptions-item>
        <el-descriptions-item label="设备类型">{{ detailData.deviceType }}</el-descriptions-item>
        <el-descriptions-item label="厂家">{{ detailData.manufacturer }}</el-descriptions-item>
        <el-descriptions-item label="型号">{{ detailData.model }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'DeviceBind',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, deviceCode: '', deviceName: '', bindUser: '', status: '' },
      dialogVisible: false,
      dialogTitle: '',
      detailVisible: false,
      detailData: null,
      tableData: [],
      form: { deviceCode: '', deviceName: '', bindUser: '', remark: '' },
      rules: {
        deviceCode: [{ required: true, message: '请输入设备编码', trigger: 'blur' }],
        bindUser: [{ required: true, message: '请选择绑定居民', trigger: 'change' }]
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
      var devices = ['智能血压计', '血糖仪', '血氧仪', '动态心电记录仪', '智能体脂秤', '蓝牙血压计', '便携血糖仪']
      var users = ['王建国', '李秀兰', '张明华', '刘桂英', '赵志强', '陈丽华', '周伟', '吴美琴']
      var types = ['血压计', '血糖仪', '血氧仪', '心电仪', '体脂秤']
      var manufacturers = ['欧姆龙', '鱼跃', '迈瑞', '华为', '小米']
      var models = ['HEM-7121', '660A', 'YX301', 'ECG-01A', 'Smart Scale 2']
      var data = []
      for (var i = 0; i < 8; i++) {
        data.push({
          id: i + 1,
          deviceCode: 'DEV-' + String(Date.now()).slice(-8) + String(i).padStart(2, '0'),
          deviceName: devices[i],
          bindUser: users[i],
          bindDate: '2026-0' + (Math.floor(Math.random() * 6) + 1) + '-' + String(Math.floor(Math.random() * 28) + 1).padStart(2, '0'),
          status: i < 6 ? '1' : '0',
          statusName: i < 6 ? '已绑定' : '已解绑',
          deviceType: types[i % types.length],
          manufacturer: manufacturers[i % manufacturers.length],
          model: models[i % models.length],
          remark: ''
        })
      }
      return data
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '新增绑定'
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { deviceCode: '', deviceName: '', bindUser: '', remark: '' }
      })
    },
    handleScanBind() {
      var that = this
      this.$prompt('请扫描设备二维码或输入设备编码', '扫码绑定', { inputPlaceholder: '设备编码' }).then(function(res) {
        that.form.deviceCode = res.value
        that.form.deviceName = '智能血压计（欧姆龙 HEM-7121）'
        that.dialogTitle = '扫码绑定'
        that.dialogVisible = true
      })
    },
    submitForm() {
      var that = this
      this.$refs.form.validate(function(valid) {
        if (valid) { that.$message.success('绑定成功'); that.dialogVisible = false; that.getList() }
      })
    },
    handleUnbind(row) {
      var that = this
      this.$confirm('确认解绑"' + row.bindUser + '"的' + row.deviceName + '?', '警告', { type: 'warning' }).then(function() {
        row.status = '0'; row.statusName = '已解绑'; that.$message.success('已解绑')
      })
    },
    handleRebind(row) {
      this.form = { deviceCode: row.deviceCode, deviceName: row.deviceName, bindUser: '', remark: '' }
      this.dialogTitle = '重新绑定'
      this.dialogVisible = true
    },
    handleDetail(row) { this.detailData = Object.assign({}, row); this.detailVisible = true }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
</style>
