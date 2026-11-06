<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" size="small" class="search-form">
      <el-form-item label="医生姓名" prop="doctorName">
        <el-input v-model="queryParams.doctorName" placeholder="请输入医生姓名" clearable />
      </el-form-item>
      <el-form-item label="科室" prop="dept">
        <el-select v-model="queryParams.dept" placeholder="请选择科室" clearable>
          <el-option label="心内科" value="心内科" />
          <el-option label="内分泌科" value="内分泌科" />
          <el-option label="神经内科" value="神经内科" />
          <el-option label="呼吸内科" value="呼吸内科" />
        </el-select>
      </el-form-item>
      <el-form-item label="排班日期" prop="dateRange">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增排班</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%">
      <el-table-column label="操作" align="center" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="doctorName" label="医生姓名" width="100" align="center" />
      <el-table-column prop="dept" label="科室" width="140" align="center" show-overflow-tooltip />
      <el-table-column prop="scheduleDate" label="排班日期" width="120" align="center" />
      <el-table-column prop="timeSlot" label="时间段" width="150" align="center" />
      <el-table-column prop="maxConsultation" label="最大会诊数" width="100" align="center" />
      <el-table-column prop="bookedCount" label="已预约数" width="100" align="center">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.bookedCount >= scope.row.maxConsultation ? '#F56C6C' : '#67C23A' }">
            {{ scope.row.bookedCount }}
          </span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" size="small">
        <el-form-item label="医生姓名" prop="doctorName">
          <el-select v-model="form.doctorName" placeholder="请选择医生" style="width:100%">
            <el-option label="张医生" value="张医生" />
            <el-option label="李医生" value="李医生" />
            <el-option label="王医生" value="王医生" />
            <el-option label="赵主任" value="赵主任" />
            <el-option label="陈医生" value="陈医生" />
          </el-select>
        </el-form-item>
        <el-form-item label="科室" prop="dept">
          <el-select v-model="form.dept" placeholder="请选择科室" style="width:100%">
            <el-option label="心内科" value="心内科" />
            <el-option label="内分泌科" value="内分泌科" />
            <el-option label="神经内科" value="神经内科" />
            <el-option label="呼吸内科" value="呼吸内科" />
          </el-select>
        </el-form-item>
        <el-form-item label="排班日期" prop="scheduleDate">
          <el-date-picker v-model="form.scheduleDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="时间段" prop="timeSlot">
          <el-select v-model="form.timeSlot" placeholder="请选择时间段" style="width:100%">
            <el-option label="08:00-10:00" value="08:00-10:00" />
            <el-option label="10:00-12:00" value="10:00-12:00" />
            <el-option label="14:00-16:00" value="14:00-16:00" />
            <el-option label="16:00-18:00" value="16:00-18:00" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大会诊数" prop="maxConsultation">
          <el-input-number v-model="form.maxConsultation" :min="1" :max="20" style="width:100%" />
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
  name: 'TeleSchedule',
  components: { Pagination },
  data() {
    return {
      loading: false,
      total: 0,
      dateRange: [],
      queryParams: { pageNum: 1, pageSize: 10, doctorName: '', dept: '' },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      tableData: [],
      form: { doctorName: '', dept: '', scheduleDate: '', timeSlot: '', maxConsultation: 5 },
      rules: {
        doctorName: [{ required: true, message: '请选择医生', trigger: 'change' }],
        dept: [{ required: true, message: '请选择科室', trigger: 'change' }],
        scheduleDate: [{ required: true, message: '请选择排班日期', trigger: 'change' }],
        timeSlot: [{ required: true, message: '请选择时间段', trigger: 'change' }],
        maxConsultation: [{ required: true, message: '请设置最大会诊数', trigger: 'blur' }]
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
      return [
        { id: 1, doctorName: '张医生', dept: '心内科', scheduleDate: '2026-06-30', timeSlot: '08:00-10:00', maxConsultation: 5, bookedCount: 3 },
        { id: 2, doctorName: '张医生', dept: '心内科', scheduleDate: '2026-06-30', timeSlot: '14:00-16:00', maxConsultation: 5, bookedCount: 5 },
        { id: 3, doctorName: '李医生', dept: '内分泌科', scheduleDate: '2026-07-01', timeSlot: '10:00-12:00', maxConsultation: 4, bookedCount: 2 },
        { id: 4, doctorName: '王医生', dept: '神经内科', scheduleDate: '2026-07-01', timeSlot: '08:00-10:00', maxConsultation: 6, bookedCount: 4 },
        { id: 5, doctorName: '赵主任', dept: '心内科', scheduleDate: '2026-07-02', timeSlot: '14:00-16:00', maxConsultation: 3, bookedCount: 3 },
        { id: 6, doctorName: '陈医生', dept: '呼吸内科', scheduleDate: '2026-07-02', timeSlot: '10:00-12:00', maxConsultation: 5, bookedCount: 1 },
        { id: 7, doctorName: '张医生', dept: '心内科', scheduleDate: '2026-07-03', timeSlot: '16:00-18:00', maxConsultation: 4, bookedCount: 0 },
        { id: 8, doctorName: '李医生', dept: '内分泌科', scheduleDate: '2026-07-03', timeSlot: '08:00-10:00', maxConsultation: 5, bookedCount: 3 }
      ]
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.dateRange = []; this.$refs.queryForm.resetFields(); this.queryParams.pageNum = 1; this.getList() },
    handleAdd() {
      this.dialogTitle = '新增排班'
      this.isEdit = false
      this.dialogVisible = true
      var that = this
      this.$nextTick(function() {
        if (that.$refs.form) that.$refs.form.resetFields()
        that.form = { doctorName: '', dept: '', scheduleDate: '', timeSlot: '', maxConsultation: 5 }
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑排班'
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
    handleDelete(row) {
      var that = this
      this.$confirm('确认删除该排班记录?', '警告', { type: 'warning' }).then(function() {
        that.$message.success('删除成功'); that.getList()
      })
    }
  }
}
</script>

<style scoped>
.search-form { background: #fff; padding: 18px 18px 4px; border-radius: 4px; border: 1px solid #e6e6e6; }
.mb8 { margin: 10px 0; }
</style>
