<template>
  <div class="app-container">
    <div class="transfer-container">
      <!-- 左侧机构树 -->
      <div class="left-sidebar">
        <div class="sidebar-header">
          <span>机构树</span>
        </div>
        <el-input
          v-model="treeFilter"
          placeholder="输入关键字过滤"
          size="small"
          clearable
          class="tree-filter"
        />
        <el-tree
          ref="tree"
          :data="treeData"
          :props="treeProps"
          :filter-node-method="filterNode"
          node-key="id"
          default-expand-all
          highlight-current
          @node-click="handleNodeClick"
        >
          <span slot-scope="{ node, data }" class="tree-node">
            <i :class="data.icon" style="margin-right:6px;color:#409EFF" />
            <span>{{ node.label }}</span>
          </span>
        </el-tree>
      </div>

      <!-- 右侧内容 -->
      <div class="right-content">
        <!-- 搜索区域 -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="queryParams.idCard" placeholder="请输入身份证号" clearable />
          </el-form-item>
          <el-form-item label="病种" prop="disease">
            <el-select v-model="queryParams.disease" placeholder="请选择病种" clearable>
              <el-option label="高血压" value="高血压" />
              <el-option label="糖尿病" value="糖尿病" />
              <el-option label="冠心病" value="冠心病" />
              <el-option label="慢阻肺" value="慢阻肺" />
              <el-option label="脑卒中" value="脑卒中" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option label="待接收" value="0" />
              <el-option label="已接收" value="1" />
              <el-option label="已拒绝" value="2" />
              <el-option label="已完成" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请时间" prop="dateRange">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增转诊</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
          </el-col>
        </el-row>

        <!-- 数据表格 -->
        <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
          <el-table-column label="操作" align="center" width="180" fixed="right">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === '0'" type="text" size="mini" @click="handleReceive(scope.row)">接收</el-button>
              <el-button v-if="scope.row.status === '0'" type="text" size="mini" style="color:#F56C6C" @click="handleReject(scope.row)">拒绝</el-button>
              <el-button type="text" size="mini" @click="handleDetail(scope.row)">详情</el-button>
              <el-button v-if="scope.row.status === '1'" type="text" size="mini" @click="handleComplete(scope.row)">完成</el-button>
            </template>
          </el-table-column>
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="name" label="姓名" width="100" align="center" show-overflow-tooltip />
          <el-table-column prop="age" label="年龄" width="70" align="center" />
          <el-table-column prop="gender" label="性别" width="70" align="center" />
          <el-table-column prop="idCard" label="身份证号" width="180" align="center" show-overflow-tooltip />
          <el-table-column prop="disease" label="病种" width="100" align="center" show-overflow-tooltip />
          <el-table-column prop="reason" label="转诊原因" min-width="180" align="center" show-overflow-tooltip />
          <el-table-column prop="applyDoctor" label="申请医生" width="100" align="center" />
          <el-table-column prop="applyTime" label="申请时间" width="160" align="center" />
          <el-table-column prop="applyHospital" label="申请医院" width="160" align="center" show-overflow-tooltip />
          <el-table-column prop="statusName" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="statusTagType(scope.row.status)">{{ scope.row.statusName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="receiveDept" label="接收科室" width="140" align="center" show-overflow-tooltip />
        </el-table>

        <!-- 分页 -->
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入患者姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" :min="0" :max="150" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别" style="width:100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="病种" prop="disease">
              <el-select v-model="form.disease" placeholder="请选择病种" style="width:100%">
                <el-option label="高血压" value="高血压" />
                <el-option label="糖尿病" value="糖尿病" />
                <el-option label="冠心病" value="冠心病" />
                <el-option label="慢阻肺" value="慢阻肺" />
                <el-option label="脑卒中" value="脑卒中" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接收科室" prop="receiveDept">
              <el-select v-model="form.receiveDept" placeholder="请选择接收科室" style="width:100%">
                <el-option label="心内科" value="心内科" />
                <el-option label="内分泌科" value="内分泌科" />
                <el-option label="呼吸内科" value="呼吸内科" />
                <el-option label="神经内科" value="神经内科" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="转诊原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请输入转诊原因" />
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
    <el-dialog title="转诊详情" :visible.sync="detailVisible" width="650px" append-to-body>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="患者姓名">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailData.age }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.idCard }}</el-descriptions-item>
        <el-descriptions-item label="病种">{{ detailData.disease }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailData.statusName }}</el-descriptions-item>
        <el-descriptions-item label="申请医生">{{ detailData.applyDoctor }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ detailData.applyTime }}</el-descriptions-item>
        <el-descriptions-item label="申请医院">{{ detailData.applyHospital }}</el-descriptions-item>
        <el-descriptions-item label="接收科室">{{ detailData.receiveDept }}</el-descriptions-item>
        <el-descriptions-item label="转诊原因" :span="2">{{ detailData.reason }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'ReferralTransfer',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      treeFilter: '',
      treeData: [
        {
          id: 1,
          label: '县医院',
          icon: 'el-icon-office-building',
          children: [
            {
              id: 11,
              label: '城关镇卫生院',
              icon: 'el-icon-school',
              children: [
                { id: 111, label: '东关村卫生室', icon: 'el-icon-house' },
                { id: 112, label: '西关村卫生室', icon: 'el-icon-house' },
                { id: 113, label: '南关村卫生室', icon: 'el-icon-house' }
              ]
            },
            {
              id: 12,
              label: '柳林镇卫生院',
              icon: 'el-icon-school',
              children: [
                { id: 121, label: '柳林村卫生室', icon: 'el-icon-house' },
                { id: 122, label: '大庄村卫生室', icon: 'el-icon-house' }
              ]
            },
            {
              id: 13,
              label: '张家川镇卫生院',
              icon: 'el-icon-school',
              children: [
                { id: 131, label: '张家川村卫生室', icon: 'el-icon-house' },
                { id: 132, label: '李家村卫生室', icon: 'el-icon-house' }
              ]
            }
          ]
        }
      ],
      treeProps: { children: 'children', label: 'label' },
      dateRange: [],
      dialogVisible: false,
      dialogTitle: '',
      detailVisible: false,
      detailData: {},
      queryParams: { pageNum: 1, pageSize: 10, name: '', idCard: '', disease: '', status: '' },
      form: { name: '', idCard: '', age: null, gender: '', disease: '', receiveDept: '', reason: '', remark: '' },
      rules: {
        name: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
        idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        disease: [{ required: true, message: '请选择病种', trigger: 'change' }],
        receiveDept: [{ required: true, message: '请选择接收科室', trigger: 'change' }],
        reason: [{ required: true, message: '请输入转诊原因', trigger: 'blur' }]
      },
      tableData: []
    }
  },
  watch: {
    treeFilter(val) { this.$refs.tree.filter(val) }
  },
  created() { this.getList() },
  methods: {
    filterNode(value, data) { if (!value) return true; return data.label.indexOf(value) !== -1 },
    handleNodeClick(data) { this.queryParams.orgId = data.id; this.getList() },
    getList() {
      this.loading = true
      var that = this
      setTimeout(function() {
        that.loading = false
        that.total = 27
        that.tableData = that.mockData().slice(
          (that.queryParams.pageNum - 1) * that.queryParams.pageSize,
          that.queryParams.pageNum * that.queryParams.pageSize
        )
      }, 300)
    },
    mockData() {
      var statusMap = { '0': '待接收', '1': '已接收', '2': '已拒绝', '3': '已完成' }
      var diseases = ['高血压', '糖尿病', '冠心病', '慢阻肺', '脑卒中']
      var doctors = ['张医生', '李医生', '王医生', '赵医生', '刘医生']
      var hospitals = ['县人民医院', '市中心医院', '省第一医院', '中医院']
      var depts = ['心内科', '内分泌科', '呼吸内科', '神经内科']
      var reasons = ['病情复杂需上级医院诊治', '患者要求转诊', '基层设备不足', '需专科进一步治疗', '急性发作需紧急转诊']
      var names = ['张三', '李四', '王五', '赵六', '刘七', '陈八', '周九', '吴十',
        '郑十一', '钱十二', '孙丽', '朱晓', '马明', '胡芳', '林强', '何伟',
        '郭静', '高远', '罗平', '梁红', '宋涛', '唐波', '韩梅', '曹峰', '许艳', '邓刚', '彭华']
      var data = []
      for (var i = 0; i < 27; i++) {
        data.push({
          id: i + 1,
          name: names[i],
          age: Math.floor(Math.random() * 30) + 40,
          gender: i % 2 === 0 ? '男' : '女',
          idCard: '610' + String(Math.floor(Math.random() * 900000 + 100000)) + String(Math.floor(Math.random() * 9000 + 1000)) + String(Math.floor(Math.random() * 9000 + 1000)),
          disease: diseases[i % diseases.length],
          reason: reasons[i % reasons.length],
          applyDoctor: doctors[i % doctors.length],
          applyTime: '2026-0' + (Math.floor(Math.random() * 6) + 1) + '-' + String(Math.floor(Math.random() * 28) + 1).padStart(2, '0') + ' 09:30:00',
          applyHospital: hospitals[i % hospitals.length],
          status: String(i % 4),
          statusName: statusMap[String(i % 4)],
          receiveDept: depts[i % depts.length]
        })
      }
      return data
    },
    statusTagType(status) {
      var map = { '0': 'warning', '1': 'primary', '2': 'danger', '3': 'success' }
      return map[status] || 'info'
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.dateRange = []; this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '新增转诊'
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { name: '', idCard: '', age: null, gender: '', disease: '', receiveDept: '', reason: '', remark: '' }
      })
    },
    submitForm() {
      var that = this
      this.$refs.form.validate(function(valid) {
        if (valid) { that.$message.success('操作成功'); that.dialogVisible = false; that.getList() }
      })
    },
    handleReceive(row) {
      var that = this
      this.$confirm('确认接收患者"' + row.name + '"的转诊申请？', '提示', { type: 'info' }).then(function() {
        row.status = '1'; row.statusName = '已接收'; that.$message.success('已接收')
      })
    },
    handleReject(row) {
      var that = this
      this.$prompt('请输入拒绝原因', '拒绝转诊', { inputType: 'textarea' }).then(function() {
        row.status = '2'; row.statusName = '已拒绝'; that.$message.success('已拒绝')
      })
    },
    handleComplete(row) {
      var that = this
      this.$confirm('确认完成患者"' + row.name + '"的转诊？', '提示', { type: 'success' }).then(function() {
        row.status = '3'; row.statusName = '已完成'; that.$message.success('已完成')
      })
    },
    handleDetail(row) { this.detailData = Object.assign({}, row); this.detailVisible = true },
    handleExport() { this.$message.success('导出成功') }
  }
}
</script>

<style scoped>
.transfer-container { display: flex; gap: 16px; }
.left-sidebar { width: 260px; min-height: 600px; background: #fff; border: 1px solid #e6e6e6; border-radius: 4px; padding: 12px; flex-shrink: 0; }
.sidebar-header { font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 10px; padding-bottom: 8px; border-bottom: 1px solid #eee; }
.tree-filter { margin-bottom: 10px; }
.right-content { flex: 1; min-width: 0; }
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
</style>
