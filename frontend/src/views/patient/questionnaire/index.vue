<!-- 问卷管理列表 -->
<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="问卷名称" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入问卷名称" clearable
                    @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item label="提交人" prop="submitor">
                <el-input v-model="queryParams.submitor" placeholder="请输入提交人" clearable
                    @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item label="提交时间" prop="submitTime">
                <el-date-picker v-model="queryParams.submitTime" type="date" placeholder="选择日期"
                    value-format="yyyy-MM-dd" format="yyyy-MM-dd" @keyup.enter.native="searchPatientList"
                    @change="dateChange">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="病种" prop="diseaseName">
                <el-input v-model="queryParams.diseaseName" placeholder="请输入病种" clearable
                    @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item label="队列" prop="projectName">
                <el-input v-model="queryParams.projectName" placeholder="请输入队列" clearable
                    @keyup.enter.native="searchPatientList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="searchPatientList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row class="mb8">
            <el-col :span="1.5" class="export_content">
                <el-button type="warning" plain icon="el-icon-download" :class="exportIsDisabled?'hoverContent':''"  @click="handleExport"
                :disabled="exportIsDisabled||!patientList.length">导出</el-button>
                <p class="button_tips">请在OA系统发起导出申请</p>
            </el-col>
            <el-col :span="1.5">
	            <p class="follow_name" v-hasPermi="['system:article:exportArticle']" style="font-weight: 400;padding-left:5px">未选择问卷时默认导出所有问卷数据</p>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="patientList" @selection-change="handleSelectionChange" :row-key="getRowKeys">
            <el-table-column type="selection" width="50" align="center" :reserve-selection="true"/>
            <el-table-column label="问卷名称" align="center" :show-overflow-tooltip="true" min-width="180">
                <template slot-scope="scoped">
                    {{ scoped.row.title | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="提交人" align="center" width="160">
                <template slot-scope="scoped">
                    {{ scoped.row.submitor | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="问卷病种" align="center" min-width="160" :show-overflow-tooltip="true">
                <template slot-scope="scoped">
                    {{ scoped.row.diseaseName | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="问卷队列" align="center" min-width="150" :show-overflow-tooltip="true">
                <template slot-scope="scoped">
                    {{ scoped.row.projectName | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="提交时间" align="center" width="190">
                <template slot-scope="scoped">
                    {{ scoped.row.submitTime | dateFormat }}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="180" fixed="right">
                <template slot-scope="scope">
                    <el-button type="text" @click="checkFollowUp(scope.row)">查看问卷详情
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
    </div>
</template>

<script>
import { getQuestionnaireList ,getExportAuthority} from "@/api/patient/questionnaire";

export default {
    name: "questionnaireList",
    dicts: ['sys_yes_no'],
    data() {
        return {
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 参数表格数据
            patientList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                title: undefined,
                submitor: undefined,
                diseaseName: '',
                projectName: '',
                submitTime: '',
            },
            datePickerOptions: undefined,
            examRecordIds:[],
            exportIsDisabled: true,
        };
    },
    created() {
        this.getList();
        this.getExportIsDisabled()
    },
    methods: {
        getRowKeys(row){
            return row.examRecordId
        },
        getExportIsDisabled() {
            getExportAuthority().then(res => {
                this.exportIsDisabled = !res
            })
        },
     /** 导出按钮操作 */
     handleExport() {
            this.download('patient/manage/exportArticle', {
                examRecordIds:this.examRecordIds
            }, `questionnaire_${new Date().getTime()}.zip`)
        },
        handleSelectionChange(event){
            let ids = []
            if (event.length) {
                ids = event.map(m => m.examRecordId)
            }
            this.examRecordIds = ids.toString()
        },
        /** 查询参数列表 */
        getList() {
            this.loading = true;
            getQuestionnaireList(this.queryParams).then(response => {
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
        checkFollowUp(event) {
            this.$router.push({
                path: `/patient/questionnaire/details`,
                query: {
                    accountId: event.accountId,
                    articleId: event.articleId,
                    examRecordId: event.examRecordId,
                    status: event.status,
                }
            })
        },
        dateChange(event) {
            console.log(event, this.queryParams.submitTime)
        },
        /** 刷新缓存按钮操作 */
        handleRefreshCache() {
            this.getList();
        },
    }
};
</script>
<style lang="scss" scoped>
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
</style>