<!-- 患者管理列表 -->
<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="患者姓名" prop="patientName">
                <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item label="就诊ID" prop="personId">
                <el-input v-model="queryParams.personId" placeholder="请输入就诊ID" clearable @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item label="患者ID" prop="patientId">
                <el-input v-model="queryParams.patientId" placeholder="请输入患者ID" @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item label="状态" prop="patientStatus">
                <el-select v-model="queryParams.patientStatus">
                    <el-option v-for="(option,index) in patientStatusData" :key="index" :label="option.label" :value="option.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="searchPatientList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row class="mb8">
            <el-col :span="1.5">
                <p class="follow_name">{{$route.params.followName}}</p>
            </el-col>
            <el-col :span="1.5" class="export_content" v-hasPermi="['archivesManage:list:exportData']">
                <el-button type="warning" plain icon="el-icon-download" @click="exportFile" :disabled="exportIsDisabled||!patientList.length" :class="exportIsDisabled?'hoverContent':''">导出</el-button>
                <p class="button_tips">请在OA系统发起导出申请</p>
            </el-col>
            <el-col :span="1.5" v-hasPermi="['archivesManage:list:exportData']">
	            <p class="follow_name" style="font-weight: 400;padding-left:5px">仅能导出edc工作站质控锁库后的数据，未选择患者时默认导出所有患者数据</p>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table :row-key="getRowKeys" v-loading="loading" :data="patientList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" :reserve-selection="true"/>
            <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" min-width="150" />
            <el-table-column label="患者状态" align="center" prop="patientStatus" min-width="90">
                <template slot-scope="scope">
                   <el-tag :type="scope.row.searchType==3?'success':scope.row.searchType==0?'warning':
                    scope.row.searchType==4?'danger':''">{{scope.row.searchType}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="当前随访名称" align="center" min-width="150">
                <template slot-scope="scoped">
                    {{ scoped.row.followName | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="已锁库" align="center" min-width="150">
                <template slot-scope="scoped">
                    {{ scoped.row.edcNames|nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="120" fixed="right">
                <template slot-scope="scope">
                    <el-button type="text" @click="checkFollowUp(scope.row)">查看随访</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
</template>

<script>
import { getPatientList, getExportAuthority } from "@/api/patient/index";

export default {
    name: "patintList",
    dicts: ['sys_yes_no'],
    data() {
        return {
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
                patientStatus: undefined,
                researchId: undefined,
                personId: undefined,
                // isSubmitBase: "1"
            },
            patientIds: '',
            patientStatusData: [
                // { label: '未填写', value: 0 },
                // { label: '基线', value: 1 },
                // { label: '随访', value: 2 },
                { label: '进行中', value: 1 },
                { label: '完成', value: 3 },
                { label: '失访', value: 4 },
                { label: '退出待审核', value: 5 },
                { label: '已退出', value: 6 }
            ],
            exportIsDisabled: true,
        };
    },
    created() {
        this.getList();
        this.getExportIsDisabled();
    },
    methods: {
        getRowKeys(row){
            return row.patientId
        },
        getExportIsDisabled() {
            getExportAuthority().then(res => {
                this.exportIsDisabled = !res
            })
        },
        // 患者状态
        reversePatientStatus(info) {
            let statusList = ['未填写', '基线', '随访', '完成', '失访','退出待审核','已退出']
            return statusList[info]
        },
        /** 查询参数列表 */
        getList() {
            this.loading = true;
            this.queryParams.researchId = this.$route.query.researchId;
            getPatientList(this.queryParams).then(response => {
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
            let queryData = this.$route.query;
            let researchName=decodeURIComponent(queryData.researchName)
            let oldPath = `${this.$route.path}?researchName=${researchName}`
            this.$router.push({
                path: `/patient/detail`,
                query: {
                    patientId: row.patientId || '',
                    followId: row.followId || '',
                    researchId: row.researchId || '',
                    baseId: row.baseId,
                    oldPath:encodeURIComponent(this.$route.fullPath)
                }
            })
        },
        /** 导出按钮操作 */
        exportFile() {
            this.download('/patient/manage/edcExportData', {
                ...this.queryParams,
                patientIds: this.patientIds,
            }, `config_${new Date().getTime()}.xlsx`)
        },
        /** 刷新缓存按钮操作 */
        handleRefreshCache() {
            this.getList();
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            let arr = []
            selection.forEach(item => {
                arr.push(item.patientId)
            });
            this.patientIds = arr.toString();
        },
    }
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
    font-weight: 600;
    margin-right: 10px;
    line-height: 30px;
}
.export_content{
    position: relative;
    .button_tips{
        border:1px #999 solid;
        border-radius: 4px;
        padding:3px 5px;
        color:#333;
        position: absolute;
        top:-999999999999px;
        left:-15px;
        right:-80px;
        z-index:9999;
        background:#fff;
    }

    .hoverContent{
        &:hover + .button_tips{
            top:35px;
        }
    }
}
</style>