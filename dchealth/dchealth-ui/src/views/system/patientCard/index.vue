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
      <el-form-item label="是否主号" prop="patientMasterNo">
        <el-select v-model="queryParams.patientMasterNo" placeholder="请选择是否主号" clearable>
          <el-option
              v-for="dict in dict.type.patient_master_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="更新状态" prop="updateStatus">
        <el-select v-model="queryParams.updateStatus" placeholder="请选择更新状态" clearable>
          <el-option
              v-for="dict in dict.type.master_is_update"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="test2(queryParams.idCard)">测试</el-button>
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
                  v-hasPermi="['system:patientCard:add']"
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
            v-hasPermi="['system:patientCard:edit']"
        >修改</el-button>
      </el-col>
<!--            <el-col :span="1.5">-->
<!--              <el-button-->
<!--                  type="danger"-->
<!--                  plain-->
<!--                  icon="el-icon-delete"-->
<!--                  size="mini"-->
<!--                  :disabled="multiple"-->
<!--                  @click="handleDelete"-->
<!--                  v-hasPermi="['system:patientCard:remove']"-->
<!--              >删除</el-button>-->
<!--            </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--            type="warning"-->
      <!--            plain-->
      <!--            icon="el-icon-download"-->
      <!--            size="mini"-->
      <!--            @click="handleExport"-->
      <!--            v-hasPermi="['system:patientCard:export']"-->
      <!--        >导出</el-button>-->
      <!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="patientCardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="身份证" align="center" prop="idCard" />
      <el-table-column label="患者id" align="center" prop="personId" />
      <el-table-column label="是否主号" align="center" prop="patientMasterNo">
        <!--        <template slot-scope="scope">-->
        <!--          <dict-tag :options="dict.type.patient_master_no" :value="scope.row.patientMasterNo"/>-->
        <!--        </template>-->

        <template slot-scope="scope">
          <el-tag :type="scope.row.patientMasterNo==1?'danger':'gray'">{{reversePatientCardStatus(scope.row.patientMasterNo)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="更新状态" align="center" prop="updateStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.master_is_update" :value="scope.row.updateStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="患者id最近时间" align="center" prop="personIdGenerateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.personIdGenerateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:patientCard:edit']"
          >修改</el-button>
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['system:patientCard:remove']"
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

    <!-- 添加或修改患者主号管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证" />
        </el-form-item>
        <el-form-item label="患者id" prop="personId">
          <el-input v-model="form.personId" placeholder="请输入患者id" />
        </el-form-item>
        <el-form-item label="是否主号" prop="patientMasterNo">
          <el-select v-model="form.patientMasterNo" placeholder="请选择是否主号">
            <el-option
                v-for="dict in dict.type.patient_master_no"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
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
import {
  listPatientCard,
  getPatientCard,
  delPatientCard,
  addPatientCard,
  updatePatientCard,
  testgetResarchInfo
} from "@/api/system/patientCard";

export default {
  name: "PatientCard",
  dicts: ['patient_master_no', 'master_is_update'],
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
      // 患者主号管理表格数据
      patientCardList: [],
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
        patientMasterNo: null,
        updateStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patientMasterNo: [
          { required: true, message: "是否主号不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 患者状态
    reversePatientCardStatus(info) {
      let statusList = ['次号', '主号']
      return statusList[info]
    },
    /** 查询患者主号管理列表 */
    getList() {
      this.loading = true;
      listPatientCard(this.queryParams).then(response => {
        this.patientCardList = response.rows;
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
        updateStatus: null,
        personIdGenerateTime: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null
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
      this.title = "添加患者主号管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPatientCard(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改患者主号管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePatientCard(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPatientCard(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除患者主号管理编号为"' + ids + '"的数据项？').then(function() {
        return delPatientCard(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/patientCard/export', {
        ...this.queryParams
      }, `patientCard_${new Date().getTime()}.xlsx`)
    },
    /** 导出按钮操作 */
    test2(id) {
      //id为空则默认
      id = id || 10014852
      testgetResarchInfo(id).then(response => {
        this.testValue = response.data;
        this.$modal.msgSuccess(this.testValue);
      });
    }
  }
};
// const inds = ref([])
// inds.value = testgetResarchInfo(10014852)
// console.log(inds.value)

</script>
