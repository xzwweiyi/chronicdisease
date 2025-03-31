<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="身份证" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输入身份证"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="患者id" prop="personId">
        <el-input
          v-model="queryParams.personId"
          placeholder="请输入患者id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出生日期" prop="dateOfBirth">
        <el-date-picker clearable
          v-model="queryParams.dateOfBirth"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择出生日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="出生地" prop="birthPlace">
        <el-input
          v-model="queryParams.birthPlace"
          placeholder="请输入出生地"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机" prop="phoneNumberHome">
        <el-input
          v-model="queryParams.phoneNumberHome"
          placeholder="请输入手机"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="nextOfKinPhone">
        <el-input
          v-model="queryParams.nextOfKinPhone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后访问" prop="lastVisitDate">
        <el-date-picker clearable
          v-model="queryParams.lastVisitDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最后访问">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="源数据库创建时间" prop="createDate">
        <el-date-picker clearable
          v-model="queryParams.createDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择源数据库创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:patientCardNotNull:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:patientCardNotNull:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:patientCardNotNull:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:patientCardNotNull:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="patientCardNotNullList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="身份证" align="center" prop="idCard" />
      <el-table-column label="患者id" align="center" prop="personId" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="sex" />
      <el-table-column label="出生日期" align="center" prop="dateOfBirth" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dateOfBirth, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出生地" align="center" prop="birthPlace" />
      <el-table-column label="手机" align="center" prop="phoneNumberHome" />
      <el-table-column label="电话" align="center" prop="nextOfKinPhone" />
      <el-table-column label="最后访问" align="center" prop="lastVisitDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastVisitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="源数据库创建时间" align="center" prop="createDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:patientCardNotNull:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:patientCardNotNull:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改增量患者号对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证" />
        </el-form-item>
        <el-form-item label="患者id" prop="personId">
          <el-input v-model="form.personId" placeholder="请输入患者id" />
        </el-form-item>
        <el-form-item label="1主患者号 0 不是主患者号" prop="patientMasterNo">
          <el-input v-model="form.patientMasterNo" placeholder="请输入1主患者号 0 不是主患者号" />
        </el-form-item>
        <el-form-item label="患者id生成时间" prop="personIdGenerateTime">
          <el-date-picker clearable
            v-model="form.personIdGenerateTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择患者id生成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="出生日期" prop="dateOfBirth">
          <el-date-picker clearable
            v-model="form.dateOfBirth"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出生日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="出生地" prop="birthPlace">
          <el-input v-model="form.birthPlace" placeholder="请输入出生地" />
        </el-form-item>
        <el-form-item label="手机" prop="phoneNumberHome">
          <el-input v-model="form.phoneNumberHome" placeholder="请输入手机" />
        </el-form-item>
        <el-form-item label="电话" prop="nextOfKinPhone">
          <el-input v-model="form.nextOfKinPhone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="最后访问" prop="lastVisitDate">
          <el-date-picker clearable
            v-model="form.lastVisitDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最后访问">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="源数据库创建时间" prop="createDate">
          <el-date-picker clearable
            v-model="form.createDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择源数据库创建时间">
          </el-date-picker>
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
import { listPatientCardNotNull, getPatientCardNotNull, delPatientCardNotNull, addPatientCardNotNull, updatePatientCardNotNull } from "@/api/system/patientCardNotNull";

export default {
  name: "PatientCardNotNull",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 增量患者号表格数据
      patientCardNotNullList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        idCard: null,
        personId: null,
        name: null,
        dateOfBirth: null,
        birthPlace: null,
        phoneNumberHome: null,
        nextOfKinPhone: null,
        lastVisitDate: null,
        createDate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询增量患者号列表 */
    getList() {
      this.loading = true;
      listPatientCardNotNull(this.queryParams).then(response => {
        this.patientCardNotNullList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        idCard: null,
        personId: null,
        patientMasterNo: null,
        updateStatus: "0",
        personIdGenerateTime: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
        name: null,
        sex: null,
        dateOfBirth: null,
        birthPlace: null,
        phoneNumberHome: null,
        nextOfKinPhone: null,
        lastVisitDate: null,
        createDate: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加增量患者号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPatientCardNotNull(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改增量患者号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePatientCardNotNull(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPatientCardNotNull(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除增量患者号编号为"' + ids + '"的数据项？').then(function() {
        return delPatientCardNotNull(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/patientCardNotNull/export', {
        ...this.queryParams
      }, `patientCardNotNull_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
