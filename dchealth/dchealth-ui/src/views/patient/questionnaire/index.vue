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
        <div class="mb8" style="overflow: hidden;">
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </div>

      <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button
                    type="warning"
                    plain
                    icon="el-icon-download"
                    size="mini"
                    @click="exportArticle"
                    v-hasPermi="['system:article:exportArticle']"
                >导出</el-button>
              </el-col>
      </el-row>

        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="patientList">
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
import { getQuestionnaireList } from "@/api/patient/questionnaire";

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
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询参数列表 */
        getList() {
            this.loading = true;
            getQuestionnaireList(this.queryParams).then(response => {
                this.patientList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        /** 问卷导出 */
        exportArticle() {
          // this.$download.zip("/patient/manage/exportArticle", "download");
            this.download('patient/manage/exportArticle', {
              ...this.queryParams
            }, `article_${new Date().getTime()}.zip`)
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