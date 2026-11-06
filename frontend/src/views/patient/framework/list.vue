<!-- 患者管理/ 患者列表 -->
<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      class="search_form"
    >
      <el-form-item label="患者姓名" prop="patientName">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入患者姓名"
          clearable
          @keyup.enter.native="searchPatientList"
        />
      </el-form-item>
      <el-form-item label="患者ID" prop="personId">
        <el-input
          v-model="queryParams.personId"
          placeholder="请输入患者ID"
          clearable
          @keyup.enter.native="searchPatientList"
        />
      </el-form-item>
      <el-form-item label="状态" prop="searchType">
        <el-select v-model="queryParams.patientStatus" clearable>
          <el-option
            v-for="(option, index) in patientStatusData"
            :key="index"
            :label="option.label"
            :value="option.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否留言" prop="messageStatus">
        <el-select v-model="queryParams.messageStatus" clearable>
          <el-option label="是" value="1"></el-option>
          <el-option label="否" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="searchPatientList"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格风格 -->
    <el-table v-loading="loading" :data="patientList">
      <el-table-column
        label="序号"
        align="center"
        prop="patientId"
        :show-overflow-tooltip="true"
        width="80"
      />
      <el-table-column
        label="患者ID"
        align="center"
        prop="personId"
        :show-overflow-tooltip="true"
        width="130"
      />
      <el-table-column
        label="患者姓名"
        align="center"
        prop="patientName"
        :show-overflow-tooltip="true"
        width="130"
      />
      <el-table-column
        label="科室"
        align="center"
        prop="deptName"
        :show-overflow-tooltip="true"
        width="220"
      />
      <el-table-column
        label="队列"
        align="center"
        prop="projectName"
        :show-overflow-tooltip="true"
        min-width="150"
      />
      <el-table-column
        label="患者状态"
        align="center"
        prop="searchType"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag
            :type="
              scope.row.searchType == 3
                ? 'success'
                : scope.row.searchType == 0
                ? 'warning'
                : scope.row.searchType == 4
                ? 'danger'
                : ''
            "
            >{{ scope.row.searchType }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="当前随访名称" align="center" min-width="150">
        <template slot-scope="scoped">
          {{ scoped.row.followName | nullFormat }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="300">
        <template slot-scope="scope">
          <el-button
            type="text"
            @click.stop="checkFollowUp(scope.row)"
            class="ptb710"
            >查看随访
          </el-button>
          <el-button
            type="text"
            @click.stop="medicationInfo(scope.row)"
            class="ptb710"
            v-if="showFlag"
            >用药信息
          </el-button>
          <el-button
            type="text"
            @click.stop="checkMessages(scope.row)"
            class="ptb710"
            style="position: relative"
            >留言
            <span v-if="scope.row.messageNums > 0" class="unreadMessage">
              {{ scope.row.messageNums }}
            </span>
          </el-button>
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
  </div>
</template>

<script>
import { getPatientList } from "@/api/patient/index";
import { obesityShow } from "@/api/info/config";

export default {
  name: "List",
  dicts: ["sys_yes_no"],
  data() {
    return {
      // 用药信息按钮展示控制
      showFlag: false,
      // 默认为卡片风格，为false时切换成列表风格
      cardType: true,
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
      // 参数表格数据
      patientList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientName: undefined,
        personId: undefined,
        searchType: "",
        researchId: "",
        messageStatus: "",
      },
      // 表单参数
      form: {},
      patientStatusData: [
        // { label: '进行中', value: 2 },
        // { label: '完成', value: 3 },
        // { label: '失访', value: 4 }
        { label: "进行中", value: 1 },
        { label: "完成", value: 3 },
        { label: "失访", value: 4 },
        { label: "退出待审核", value: 5 },
        { label: "已退出", value: 6 },
      ],
    };
  },
  created() {
    this.getList();
    this.obesityShow();
  },
  methods: {
    // 患者状态
    reversePatientStatus(info) {
      let statusList = [
        "",
        "",
        "进行中",
        "完成",
        "失访",
        "退出待审核",
        "已退出",
      ];
      return statusList[info];
    },
    /** 查询参数列表 */
    getList() {
      this.loading = true;
      this.queryParams.researchId = this.$route.query.researchId;
      getPatientList(this.queryParams).then((response) => {
        this.patientList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    searchPatientList() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.searchPatientList();
    },
    /** 查看随访按钮操作 */
    checkFollowUp(row) {
      this.$router.push({
        path: `/patient/detail`,
        query: {
          patientId: row.patientId,
          followId: row.followId,
          researchId: row.researchId,
          oldPath: encodeURIComponent(this.$route.fullPath),
        },
      });
    },
    // 用药信息
    medicationInfo(row) {
      this.$router.push({
        path: `/patient/framework/detail`,
        query: {
          personId: row.personId,
          oldPath: encodeURIComponent(this.$route.fullPath),
        },
      });
    },
    // 留言
    checkMessages(row) {
      this.$router.push({
        path: `/patient/framework/messages`,
        query: {
          row,
          oldPath: encodeURIComponent(this.$route.fullPath),
        },
      });
    },
    /** 导出按钮操作 */
    exportFile(event) {
      let params = {
        patientId: event.patientId,
        researchId: event.researchId,
      };
      this.download(
        "/patient/manage/exportData",
        {
          ...params,
        },
        `config_${new Date().getTime()}.xlsx`
      );
    },
    /** 刷新缓存按钮操作 */
    handleRefreshCache() {
      this.getList();
    },

    obesityShow() {
      obesityShow("-1").then((response) => {
        this.showFlag = response.data;
        console.log("showFlag", this.showFlag);
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.el-select {
  width: 100px;
}

.el-input {
  width: 150px;
}

.follow_name {
  color: #333;
  margin-right: 10px;
}

.unreadMessage {
  position: absolute;
  top: 0px;
  right: -5px;
  background: red;
  color: white;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
