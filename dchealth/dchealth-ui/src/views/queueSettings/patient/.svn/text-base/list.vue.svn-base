<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
            <el-form-item label="患者：" prop="patientName">
                <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable style="width: 240px" />
            </el-form-item>
            <el-form-item label="患者ID：" prop="patientId">
                <el-input v-model="queryParams.patientId" placeholder="请输入患者ID" clearable style="width: 240px" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5" class="research_name">
                <span class="el-form-item__label">队列名称：</span>
                <span class="el-form-item__content">{{ researchName }}</span>
                <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['edc:manage:exportData']">导出</el-button> -->
            </el-col>
            <right-toolbar @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="configList" ref="selectTable" class="select_table_style"
            @row-click="clickRow" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="患者ID" align="center" prop="patientId" width="160" />
            <el-table-column label="患者姓名" align="center" prop="patientName" min-width="300"
                :show-overflow-tooltip="true" />
            <el-table-column label="当前随访" align="center" prop="followName" min-width="200"
                :show-overflow-tooltip="true" />
            <el-table-column label="状态" align="center" prop="followStatus" min-width="120">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.searchType==3?'success':scope.row.searchType==0?'warning':
                    scope.row.searchType==4?'danger':''">{{reversePatientStatus(scope.row.searchType)}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="160">
                <template slot-scope="scope">
                    <el-button type="text" @click="handleSee(scope.row)">查看详情
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
    </div>
</template>

<script>
import { getresearchPatient } from '@/api/research/info'

export default {
    name: "patintList",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 总条数
            total: 0,
            // 查询缓存
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                patientId: undefined,
                patientName: undefined,
                researchId: '',
            },
            configList: [],
            researchName: '',
            patientIds:'',
        };
    },
    created() {
        this.researchName = decodeURIComponent(this.$route.query.researchName)
        this.queryParams.researchId = this.$route.params.researchId
        this.getList()
    },
    methods: {
        // 患者状态
        reversePatientStatus(info) {
            let statusList = ['无状态', '待处理', '进行中', '已完成', '已关闭']
            return statusList[info]
        },
        /** 查询缓存列表 */
        getList() {
            this.loading = false;
            getresearchPatient(this.queryParams).then(response => {
                this.configList = response.rows;
                this.total = response.total;
            });
            this.loading = false;
        },
        handleSee(item) {
            this.$router.push({
                path: '/queueSettings/allpatient/details',
                query: {
                    patientId: item.patientId,
                    researchId: item.researchId,
                    recordId: item.recordId,
                }
            })
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        handleSelectionChange(event) {
            let ids = []
            if (event.length) {
                ids = event.map(m => m.patientId)
            }
            this.patientIds = ids.toString()
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNo = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('/patient/manage/edcExportData', {
                ...this.queryParams,
                patientIds:this.patientIds,
            }, `config_${new Date().getTime()}.xlsx`)
        },
        /** 刷新缓存按钮操作 */
        handleRefreshCache() {
            refreshCache().then(() => {
                this.$modal.msgSuccess("刷新成功");
            });
        }
    }
};
</script>
<style lang="scss" scoped>
.research_name {
    display: flex;
    color: #333;
    .el-form-item__label {
        font-weight: bold;
        padding: 0;
    }
    .el-button {
        margin-left: 10px;
    }
}
</style>