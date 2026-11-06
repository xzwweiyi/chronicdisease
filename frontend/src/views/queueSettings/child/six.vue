<template>
  <div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="患者ID：" prop="patientCode">
        <el-input v-model="queryParams.patientCode" placeholder="请输入患者ID" clearable class="query_params_input" />
      </el-form-item>
      <el-form-item label="患者姓名：" prop="patientName">
        <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable class="query_params_input" />
      </el-form-item>
      <el-form-item label="绑定状态：" prop="bindStatus">
        <el-select v-model="queryParams.bindStatus" class="query_params_input">
          <el-option value="0" label="未绑定"></el-option>
          <el-option value="1" label="已绑定"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="入组状态：" prop="enrollmentStatus">
        <el-select v-model="queryParams.enrollmentStatus" class="query_params_input">
          <el-option value="0" label="未入组"></el-option>
          <el-option value="1" label="已入组"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row class="mb8">
      <el-button type="success" plain @click="handleImport" v-hasPermi="['medical:enrollmentCondition:select']">纳排入组</el-button>
      <el-button type="success" plain @click="handleAdd" v-hasPermi="['system:info:add']"> 快速导入</el-button>
      <el-button type="danger" plain @click="deletePatient(false)" :disabled="!patientIds.length" v-hasPermi="['system:config:export']"
        >删除</el-button
      >
      <el-button type="primary" plain @click="handleEnrollment" v-hasPermi="['system:info:enrollment']">入组其他队列</el-button>
      <!-- <el-button type="warning" plain @click="handleExport"
                v-hasPermi="['system:config:export']">导出</el-button> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格风格 -->
    <el-table
      v-loading="loading"
      :data="enrollmentList"
      :height="tableHeight"
      ref="patientTable"
      @row-click="clickEnrollRow"
      @selection-change="patientSelectionChange"
      :row-class-name="setRowClassName"
    >
      <el-table-column type="selection" width="55" align="center">
        <template slot-scope="scope" v-if="scope.row.enrollmentStatus != 1">
          <el-checkbox v-model="scope.row.checked" @change="toggleSelection(scope.row)"></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column label="患者编码" align="center" prop="patientCode" v-if="isShow" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="患者性别" align="center" prop="sex" width="100">
        <template slot-scope="scope">
          {{ scope.row.sex | convertAgentStr }}
        </template>
      </el-table-column>
      <el-table-column label="身份证号" align="center" prop="idCard" min-width="160" />
      <el-table-column label="出生日期" align="center" prop="birthday" width="120" />
      <el-table-column label="患者手机号" align="center" prop="baseMobile" width="120" />
      <el-table-column label="患者ID" align="center" prop="patientId" width="120" />
      <el-table-column label="患者就诊ID" align="center" prop="personId" width="120" />
      <el-table-column label="是否绑定" align="center" min-width="120" key="accountId" prop="accountId">
        <template slot-scope="scope">
          {{ scope.row.accountId > 0 ? "绑定" : "未绑定" }}
        </template>
      </el-table-column>
      <el-table-column label="入组状态" align="center" min-width="120" key="enrollmentStatus" prop="enrollmentStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.enrollmentStatus == '0'">未入组</span>
          <span v-if="scope.row.enrollmentStatus == '1'">已入组</span>
        </template>
      </el-table-column>
      <el-table-column
        label="入组详情"
        align="center"
        show-overflow-tooltip
        min-width="150"
        key="enrollmentResultContent"
        prop="enrollmentResultContent"
      >
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="170">
        <template slot-scope="soped">{{ soped.row.createTime | dateFormat }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button type="text" @click.stop="handleSee(scope.row)">查看详情</el-button>
          <el-button type="text" @click.stop="handleSeeDrug(scope.row)" v-if="showFlag">查看用药信息</el-button>
          <el-button type="text" class="el-button-text_delete" v-hasPermi="['system:info:delete']" @click.stop="deletePatient(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <div slot="footer" class="dialog-footer" style="margin: 20px auto 0; text-align: center">
      <el-button type="primary" @click="handleSubmit('save')">保 存</el-button>
      <el-button type="primary" @click="handleSubmit('next')">保存并进入下一步</el-button>
    </div>
    <el-dialog :title="title" :visible.sync="addOpen" width="80%" top="8vh" @close="cancelAll" :modal-append-to-body="false">
      <el-form :model="allParams" ref="allForm" :inline="true" label-width="90px">
        <el-form-item label="患者姓名:">
          <el-input v-model="allParams.patientName" placeholder="请输入患者姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="手机号:">
          <el-input v-model="allParams.mobile" placeholder="请输入患者手机号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="身份证号:">
          <el-input v-model="allParams.idCard" placeholder="请输入身份证号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="就诊卡号:">
          <el-input v-model="allParams.tdPatientId" placeholder="请输入就诊卡号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQueryAll">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQueryAll">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="loading"
        :data="allList"
        @row-click="clickRow"
        ref="selectTable"
        class="select_table_style"
        @selection-change="handleSelectionChange"
        height="calc(80vh - 280px)"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" />
        <el-table-column label="性别" align="center" prop="sex" width="80">
          <template slot-scope="scope">
            {{ scope.row.sex == "1" ? "男" : "女" }}
          </template>
        </el-table-column>
        <el-table-column label="手机号" align="center" prop="mobile" width="150" />
        <el-table-column label="身份证号" align="center" prop="idCard" width="240" />
        <el-table-column label="出生日期" align="center" prop="birthday" width="120" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="200">
          <template slot-scope="soped">{{ soped.row.createTime | dateFormat }}</template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="240" fixed="right">
          <template slot-scope="soped">
            <el-button type="text" @click.stop="toHealthrecordDetails(soped.row)">查看患者详情</el-button>
            <el-button type="text" @click.stop="handleSeeDrug(soped.row)" v-if="showFlag">查看用药信息</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="allListTotal > 0"
        :total="allListTotal"
        :page.sync="allParams.pageNum"
        :limit.sync="allParams.pageSize"
        @pagination="getAll()"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAll">确 定</el-button>
        <el-button @click="cancelAll">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 药品信息弹窗 -->
    <el-dialog title="用药信息" :visible.sync="drugInfoVisible" width="800px" :modal-append-to-body="false">
      <el-scrollbar style="max-height: 400px; width: 100%">
        <el-table
          v-if="drugData.length > 0"
          :data="drugData"
          stripe
          border
          style="width: 100%"
          class="drug-table"
          :fit="false"
          :show-header-overflow="true"
          :show-overflow-tooltip="true"
        >
          <el-table-column prop="drugExposureStartDate" label="用药时间" width="150" align="center">
            <template slot-scope="scope">
              {{ scope.row.drugExposureStartDate || "无" }}
            </template>
          </el-table-column>

          <el-table-column prop="departmentName" label="开单科室" width="150" align="center">
            <template slot-scope="scope">
              {{ scope.row.departmentName || "无" }}
            </template>
          </el-table-column>
          <el-table-column prop="drugName" label="药品名称" width="200" align="center">
            <template slot-scope="scope">
              {{ scope.row.drugName || "无" }}
            </template>
          </el-table-column>
          <el-table-column prop="drugType" label="药品类型" width="120" align="center">
            <template slot-scope="scope">
              {{ scope.row.drugType || "无" }}
            </template>
          </el-table-column>
          <el-table-column prop="dosageForm" label="剂型名称" width="120" align="center">
            <template slot-scope="scope">
              {{ scope.row.dosageForm || "无" }}
            </template>
          </el-table-column>

          <!-- <el-table-column
            prop="daysSupply"
            label="用药天数"
            width="100"
            align="center"
          >
            <template slot-scope="scope">
              {{ scope.row.daysSupply || "无" }}
            </template>
          </el-table-column> -->

          <el-table-column prop="drugSpec" label="药品规格" width="120" align="center">
            <template slot-scope="scope">
              {{ scope.row.drugSpec || "无" }}
            </template>
          </el-table-column>
          <el-table-column prop="drugUnit" label="药品单位" width="100" align="center">
            <template slot-scope="scope">
              {{ scope.row.drugUnit || "无" }}
            </template>
          </el-table-column>
        </el-table>
        <div v-else class="empty-drug-info">
          <el-empty description="暂无用药信息"></el-empty>
        </div>
      </el-scrollbar>
      <span slot="footer" class="dialog-footer">
        <el-button @click="drugInfoVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
    <!-- 入组其他队列弹窗 -->
    <el-dialog title="入组其他队列" :visible.sync="enrollmentVisible" width="1200px" :modal-append-to-body="false" @close="handleEnrollmentDialogClose">
      <el-form :model="popupParams" ref="popupParamsForm" :inline="true" label-width="90px">
        <el-form-item label="患者姓名:">
          <el-input v-model="popupParams.patientName" placeholder="请输入患者姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="手机号:">
          <el-input v-model="popupParams.mobile" placeholder="请输入患者手机号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="身份证号:">
          <el-input v-model="popupParams.idCard" placeholder="请输入身份证号" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="患者ID:">
          <el-input v-model="popupParams.patientId" placeholder="请输入患者ID" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="入组状态：" prop="enrollmentStatus">
          <el-select v-model="popupParams.enrollmentStatus" class="query_params_input">
            <el-option value="0" label="未入组"></el-option>
            <el-option value="1" label="已入组"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="就诊卡号:">
          <el-input v-model="popupParams.tdPatientId" placeholder="请输入就诊卡号" clearable style="width: 160px" />
        </el-form-item> -->
        <!-- <el-form-item label="入组时间" prop="enrollmentTime">
            <el-date-picker v-model="popupParams.enrollmentTime" type="date" placeholder="选择日期"
                value-format="yyyy-MM-dd" format="yyyy-MM-dd">
            </el-date-picker>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="enrollInOthersSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetEnrollInOthers">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-checkbox 
            :value="isSelectAllPages"
            :indeterminate="isIndeterminate"
            @change="handleSelectAllPagesChange"
          >
            所有数据全选
          </el-checkbox>
          <span style="margin-left: 15px; color: #606266">
            已选中 {{ selectedIds.length }} / {{ selectQueueTotal }} 条数据
          </span>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="popupLoading"
        :data="popupPatientList"
        :height="tableHeight"
        ref="enrollmentTable"
        @selection-change="handleEnrollmentSelectionChange"
        row-key="patientId"
        :reserve-selection="true"
      >
        <el-table-column type="selection" width="55" align="center" :reserve-selection="true"> </el-table-column>
        <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" min-width="100" />
        <el-table-column label="患者性别" align="center" prop="sex" width="100">
          <template slot-scope="scope">
            {{ scope.row.sex | convertAgentStr }}
          </template>
        </el-table-column>
        <el-table-column label="身份证号" align="center" prop="idCard" min-width="160" />
        <el-table-column label="出生日期" align="center" prop="birthday" width="120" />
        <el-table-column label="患者手机号" align="center" prop="baseMobile" width="120" />
        <el-table-column label="患者ID" align="center" prop="patientId" width="120" />
        <el-table-column label="患者就诊ID" align="center" prop="personId" width="120" />
        <el-table-column label="是否绑定" align="center" min-width="120" key="accountId" prop="accountId">
          <template slot-scope="scope">
            {{ scope.row.accountId > 0 ? "绑定" : "未绑定" }}
          </template>
        </el-table-column>
        <el-table-column label="入组状态" align="center" min-width="120" key="enrollmentStatus" prop="enrollmentStatus">
          <template slot-scope="scope">
            <span v-if="scope.row.enrollmentStatus == '0'">未入组</span>
            <span v-if="scope.row.enrollmentStatus == '1'">已入组</span>
          </template>
        </el-table-column>
        <el-table-column
          label="入组详情"
          align="center"
          show-overflow-tooltip
          min-width="150"
          key="enrollmentResultContent"
          prop="enrollmentResultContent"
        >
        </el-table-column>
      </el-table>

      <pagination
        v-show="selectQueueTotal > 0"
        :total="selectQueueTotal"
        :page.sync="popupParams.pageNum"
        :limit.sync="popupParams.pageSize"
        @pagination="getPopupList"
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="enrollmentVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEnrollmentSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="选择目标队列" :visible.sync="targetQueueVisible" width="500px" :modal-append-to-body="false">
      <el-form :model="targetQueueForm" label-width="100px">
        <el-form-item label="目标队列：">
          <el-select v-model="targetQueueForm.queueId" placeholder="请选择目标队列" style="width: 100%">
            <el-option v-for="item in queueList" :key="item.researchId" :label="item.projectName" :value="item.researchId"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="targetQueueVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmEnrollment">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listEnrollment,
  listNoEnrollment,
  subNoEnrollment,
  removeEnrollment,
  obesityShow,
  listEnrollmentList,
  moveEnrollment,
  listAllEnrollmentIds,
  get360Url,
} from "@/api/info/config";
import { getDrugInfoDetail } from "@/api/patient";

export default {
  name: "stepSix",
  props: {
    researchId: { type: String, default: null },
    deptId: { type: Number, default: null },
  },
  data() {
    return {
      // 肥胖需求控制用药信息按钮
      showFlag: false,

      // 控制肥胖需求
      isShow: false,

      // 遮罩层
      loading: true,
      popupLoading: false, // 入组其他队列弹窗loading
      // 总条数
      total: 0,
      selectQueueTotal: 0, // 入组其他队列弹窗总条数
      showSearch: true,
      // 查询缓存
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientCode: undefined,
        patientName: undefined,
        researchId: undefined,
        bindStatus: undefined,
        enrollmentStatus: undefined,
      },
      // 未入组患者查询
      allParams: {
        mobile: undefined,
        patientName: undefined,
        researchId: undefined,
        idCard: undefined,
        tdPatientId: undefined,
        pageSize: 10,
        pageNum: 1,
      },
      title: "",
      enrollmentList: [],
      popupPatientList: [], // 入组其他队列弹窗患者列表
      enrollmentVisible: false, // 入组其他队列弹窗显示状态
      popupParams: {
        researchId: undefined,
        pageNum: 1,
        pageSize: 10,
        patientName: undefined,
        mobile: undefined,
        idCard: undefined,
        // tdPatientId: undefined,
        patientId: undefined,
        enrollmentStatus: undefined,
        // enrollmentTime: undefined,
      },
      addOpen: false, //快速导入弹窗
      allList: [],
      arrList: [],
      tableHeight: "calc(80vh - 260px)",
      allListTotal: 0,
      patientIds: [],
      drugInfoVisible: false, // 用药信息弹窗显示状态
      drugData: [], // 药品信息数据
      targetQueueVisible: false, // 选择目标队列弹窗显示状态
      queueList: [], // 队列列表
      targetQueueForm: {
        queueId: null, // 选中的目标队列ID
      },
      // 全选所有页面数据的功能
      selectedIds: [],    // 只存储选中的ID，不存储完整对象
      allDataObj: [],     // 当前查询条件下的所有数据对象列表
      allDataIds: [],
      isRestoringEnrollmentSelection: false,
    };
  },
  computed: {
    // 是否所有数据全选
    isSelectAllPages() {
      const totalCount = this.allDataIds.length || this.selectQueueTotal;
      if (totalCount === 0) return false;
      return this.selectedIds.length > 0 && this.selectedIds.length === totalCount;
    },
    
    // 半选状态
    isIndeterminate() {
      const totalCount = this.allDataIds.length || this.selectQueueTotal;
      if (totalCount === 0) return false;
      const selectedCount = this.selectedIds.length;
      return selectedCount > 0 && selectedCount < totalCount;
    }
  },
  created() {
  },
  watch: {
    researchId: {
      immediate: true, // 监听到后，立即执行 handler方法
      handler(newVal, oldValue) {
        if (newVal && newVal != null) {
          this.obesityShow();
          this.obesityShow2();
          this.getList();
        }
      },
    },
  },
  methods: {
    /** 查询缓存列表 */
    getList() {
      this.loading = false;
      this.queryParams.researchId = this.researchId;
      this.enrollmentList = [];
      listEnrollment(this.queryParams).then((response) => {
        let responseData = response.rows;
        if (responseData && responseData.length) {
          this.enrollmentList = responseData.map((m) => {
            return { ...m, checked: false };
          });
        }
        this.total = response.total;
        this.selectQueueTotal = response.total;
      });
      this.loading = false;
    },
    // 获取全部患者ID列表，供入组其他队列全选使用
    async fetchAllIds() {
      this.popupParams.researchId = this.researchId;
      const res = await listAllEnrollmentIds(this.popupParams);
      this.allDataObj = Array.isArray(res.data) ? res.data : [];
      this.allDataIds = this.allDataObj.map((item) => item.patientId);
    },
    getPopupList() {
      this.popupLoading = true;
      this.popupParams.researchId = this.researchId;
      this.popupPatientList = [];
      return listEnrollment(this.popupParams)
        .then((response) => {
          let responseData = response.rows;
          if (responseData && responseData.length) {
            this.popupPatientList = responseData.map((m) => {
              return { ...m, checked: false };
            });
          }
          this.selectQueueTotal = response.total;

          this.$nextTick(() => {
            this.restoreCurrentPageSelection();
          });
        })
        .catch(() => {
          this.$message.error("获取入组患者列表失败");
        })
        .finally(() => {
          this.popupLoading = false;
        });
    },
    // 判断当前页数据是否被选中
    restoreCurrentPageSelection() {
      if (!this.$refs.enrollmentTable) return;
      this.isRestoringEnrollmentSelection = true;
      this.$refs.enrollmentTable.clearSelection();
      this.popupPatientList.forEach((row) => {
        if (this.selectedIds.includes(row.patientId)) {
          this.$refs.enrollmentTable.toggleRowSelection(row, true);
        }
      });
      this.$nextTick(() => {
        this.isRestoringEnrollmentSelection = false;
      });
    },

    obesityShow() {
      console.log("==============");
      obesityShow(this.researchId).then((response) => {
        this.isShow = response.data;
        console.log("showFlag", this.showFlag);
      });
    },

    obesityShow2() {
      obesityShow("-1").then((response) => {
        this.showFlag = response.data;
        console.log("showFlag", this.showFlag);
      });
    },

    setRowClassName(event) {
      if (event.row.enrollmentStatus != 1) {
        return "select_table_row";
      } else {
        return "";
      }
    },
    clickEnrollRow(row) {
      if (row.enrollmentStatus != 1) {
        this.$refs.patientTable.toggleRowSelection(row);
      }
    },
    patientSelectionChange(event) {
      let ids = [];
      if (event.length) {
        ids = event.filter((f) => f.enrollmentStatus != "1").map((m) => m.patientId);
        this.enrollmentList.forEach((item) => {
          if (item.enrollmentStatus != "1" && event.findIndex((f) => f.patientId == item.patientId) > -1) {
            item.checked = true;
          } else {
            item.checked = false;
          }
        });
      } else {
        this.enrollmentList.forEach((item) => {
          item.checked = false;
        });
      }
      this.patientIds = ids.toString();
    },
    toggleSelection(rows) {
      if (rows) {
        this.$refs.patientTable.toggleRowSelection(rows);
      } else {
        this.$refs.patientTable.clearSelection();
      }
    },
    toHealthrecordDetails(item) {
      let queryData = this.$route.query;
      let oldPath = `${this.$route.path}?id=${queryData.id}&stage=5`;
      this.$router.push({
        path: "/workstation/patients/opinionDetail",
        query: {
          patientId: item.patientId,
          personId: item.personId,
          hideQueueTree: true,
          type: 1,
          oldPath: oldPath,
        },
      });
    },
    get360UrlFun(item) {
      get360Url(item.patientId).then((response) => {
        const url = response.data;
        if (url) {
          window.open(url, "_blank");
        } else {
          this.$message.error("获取360详情链接失败");
        }
      });
    },
    handleSee(item) {
      let queryData = this.$route.query;
      let oldPath = `${this.$route.path}?id=${queryData.id}&stage=5`;
      this.$router.push({
        path: "/queueSettings/allpatient/details",
        query: {
          patientId: item.patientId,
          researchId: this.researchId,
          oldPath: encodeURIComponent(oldPath),
        },
      });
    },
    handleImport() {
      let queryData = this.$route.query;
      let oldPath = `${this.$route.path}?id=${queryData.id}&stage=${queryData.stage}`;
      this.$router.push({
        path: "/queueSettings/patient/import",
        query: {
          researchId: this.researchId,
          oldPath: encodeURIComponent(oldPath),
        },
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "/patient/manage/exportData",
        {
          ...this.queryParams,
        },
        `config_${new Date().getTime()}.xlsx`
      );
    },
    handleAdd() {
      this.title = "快速导入患者";
      this.addOpen = true;
      this.allList = [];
      this.allListTotal = 0;
      // this.getAll();
    },
    /**
     *获取全部患者数据
     */
    getAll() {
      if (!this.allParams.patientName && !this.allParams.mobile && !this.allParams.idCard && !this.allParams.tdPatientId) {
        this.$message.warning("请输入查询条件");
        return;
      }
      this.loading = true;
      (this.allParams.researchId = this.researchId),
        listNoEnrollment(this.allParams).then((response) => {
          this.allList = response.rows;
          this.allListTotal = response.total;
          this.loading = false;
        });
    },
    handleQueryAll() {
      this.allParams.pageNum = 1;
      this.getAll();
    },
    // 表单重置
    resetAll() {
      this.allParams = {
        mobile: undefined,
        patientName: undefined,
        idCard: undefined,
        tdPatientId: undefined,
        pageSize: 10,
        pageNum: 1,
      };
      this.resetForm("allParams");
      this.allList = [];
    },
    /** 重置按钮操作 */
    resetQueryAll() {
      this.resetAll();
    },
    clickRow(row) {
      this.$refs.selectTable.toggleRowSelection(row);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      let arr = [];
      selection.forEach((item) => {
        item["deptId"] = this.deptId;
        item["researchId"] = Number(this.researchId);
        arr.push(item);
      });
      this.arrList = arr;
    },
    deletePatient(event) {
      let patientIds = event ? event.patientId : this.patientIds;
      this.$modal
        .confirm("是否确认删除患者？")
        .then(function () {
          return removeEnrollment(patientIds);
        })
        .then((res) => {
          this.getList();
          this.$modal.msgSuccess(res.msg || "删除成功");
        })
        .catch(() => {});
    },
    /**
     * 确认导入
     */
    submitAll() {
      if (this.arrList && this.arrList.length > 0) {
        this.loading = true;
        subNoEnrollment(this.arrList).then((response) => {
          this.$nextTick(() => {
            this.addOpen = false;
            this.getList();
          });
          this.loading = false;
        });
      }
    },
    cancelAll() {
      this.addOpen = false;
      this.resetAll();
    },
    /**
     * 保存/保存下一步
     */
    handleSubmit(valData) {
      if (valData == "next") {
        this.$emit("nextSubmitSeven", true);
      } else if (valData == "save") {
        this.$emit("nextSubmitSevenSave", true);
      }
    },
    // 查看用药信息
    handleSeeDrug(row) {
      getDrugInfoDetail({ personId: row.personId })
        .then((res) => {
          this.drugData = res.data || [];
          // 打开弹窗
          this.drugInfoVisible = true;
        })
        .catch(() => {
          this.$message.error("获取用药信息失败");
        });
    },
    // 入组其他队列
    handleEnrollment() {
      this.enrollmentVisible = true;
      this.targetQueueVisible = false;
      this.targetQueueForm.queueId = null;
      this.resetEnrollmentSelectionState();
      Promise.all([this.fetchAllIds(), this.getPopupList()]).catch(() => {
        this.$message.error("初始化入组患者列表失败");
      });
    },
    // 确定入组其他队列
    handleEnrollmentSubmit() {
      if (!this.selectedIds.length) {
        this.$message.warning("请先选择要入组的患者");
        return;
      }
      // 获取队列列表
      this.getQueueList();
    },
    handleEnrollmentSelectionChange(selection) {
      if (this.isRestoringEnrollmentSelection) {
        return;
      }
      // 获取当前页所有行的ID
      const currentPageIds = this.popupPatientList.map((item) => item.patientId);
      // 获取当前页选中的行的ID
      const selectedIdsInCurrentPage = selection.map((item) => item.patientId);
      
      // 移除当前页中不再选中的ID
      this.selectedIds = this.selectedIds.filter((id) => !currentPageIds.includes(id));
      // 添加当前页新选中的ID
      this.selectedIds.push(...selectedIdsInCurrentPage);
      // 去重
      this.selectedIds = [...new Set(this.selectedIds)];
    },
    async handleSelectAllPagesChange(value) {
      if (value) {
        // 全选所有数据
        if (this.allDataIds.length === 0) {
          await this.fetchAllIds();
        }
        this.selectedIds = [...this.allDataIds];
        await this.getPopupList();
        this.$message.success(`已全选所有 ${this.selectedIds.length} 条数据`);
      } else {
        // 取消全选所有数据
        this.selectedIds = [];
        if (this.$refs.enrollmentTable) {
          this.$refs.enrollmentTable.clearSelection();
        }
        this.$message.success("已取消全选所有数据");
      }
    },
    handleEnrollmentDialogClose() {
      this.targetQueueVisible = false;
      this.targetQueueForm.queueId = null;
      this.resetEnrollmentSelectionState();
      this.resetEnrollInOthers(false);
    },
    // 获取队列列表（排除当前队列）
    getQueueList() {
      this.loading = true;
      const params = {
        deptId: this.deptId,
        researchId: this.researchId,
      };
      listEnrollmentList(params)
        .then((response) => {
          if (response.code === 200) {
            this.queueList = response.data || [];

            // 打开选择目标队列弹窗
            this.targetQueueVisible = true;
            this.targetQueueForm.queueId = this.queueList[0]?.researchId || null;
          }
          this.loading = false;
        })
        .catch(() => {
          this.$message.error("获取队列列表失败");
          this.loading = false;
        });
    },

    // 确认入组到目标队列
    confirmEnrollment() {
      if (!this.targetQueueForm.queueId) {
        this.$message.warning("请选择目标队列");
        return;
      }

      // 按照指定格式准备数据
      const enrollmentData = this.selectedIds.map((id) => {
        const patient = this.allDataObj.find(item => item.patientId === id) || {};
        return {
          researchId: Number(this.targetQueueForm.queueId),
          cardId: patient.idCard || "",
          mobile: patient.baseMobile || "",
          accountId: patient.accountId || 0,
          deptId: this.deptId || 0,
        };
      });
      this.loading = true;
      moveEnrollment({ selectedList: enrollmentData })
        .then((response) => {
          if (response.code === 200) {
            this.$message.success(response.msg || "入组成功");
            // 关闭所有弹窗
            this.enrollmentVisible = false;
            this.targetQueueVisible = false;
            // 重置选中状态
            this.resetEnrollmentSelectionState();
            // 刷新列表
            this.getList();
          } else {
            this.$message.error(response.msg || "入组失败");
          }
          this.loading = false;
        })
        .catch((error) => {
          this.$message.error("入组操作失败");
          this.loading = false;
        });
    },
    enrollInOthersSearch() {
      this.popupParams.pageNum = 1;
      this.getPopupList();
    },
    resetEnrollInOthers(needQuery = true) {
      this.popupParams = {
        researchId: this.researchId,
        pageNum: 1,
        pageSize: 10,
        patientName: undefined,
        mobile: undefined,
        idCard: undefined,
        // tdPatientId: undefined,
        patientId: undefined,
        enrollmentStatus: undefined,
        // enrollmentTime: undefined,
      };
      this.popupPatientList = [];
      this.selectQueueTotal = 0;
      this.resetEnrollmentSelectionState();
      this.resetForm("popupParamsForm");
      if (needQuery) {
        this.getPopupList();
      }
    },
    resetEnrollmentSelectionState() {
      this.selectedIds = [];
      this.allDataIds = [];
      this.allDataObj = [];
      this.isRestoringEnrollmentSelection = false;
    },
  },
};
</script>
<style lang="scss" scoped>
.query_params_input {
  width: 150px;
}
.el-table {
  ::v-deep .select_table_row {
    cursor: pointer;
  }
}
.drug-info-card {
  margin: 0;

  .drug-info-content {
    p {
      margin: 8px 0;
      font-size: 14px;
      line-height: 1.5;

      strong {
        color: #409eff;
        font-weight: 500;
      }
    }
  }
}
.empty-drug-info {
  text-align: center;
  padding: 40px 0;
}

// 添加el-scrollbar样式
::v-deep .el-scrollbar__wrap {
  max-height: 400px;
  overflow: auto;
}

::v-deep .el-scrollbar__view {
  min-height: 100%;
}

::v-deep .el-scrollbar__bar.is-vertical {
  right: 0;
  width: 6px;
}

::v-deep .el-scrollbar__bar.is-horizontal {
  bottom: 0;
  height: 6px;
}

::v-deep .el-scrollbar__thumb {
  background-color: rgba(144, 147, 153, 0.3);
  border-radius: 3px;

  &:hover {
    background-color: rgba(144, 147, 153, 0.5);
  }
}

// 确保表格内容超出时能触发水平滚动
::v-deep .drug-table {
  min-width: max-content;
}
</style>
