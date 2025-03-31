<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="资讯标题" prop="viewNum">
                <el-input v-model="queryParams.title" placeholder="资讯标题" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <div class="mb8">
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['cms:stat:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </div>

        <el-table v-loading="loading" :data="statList">
            <!-- <el-table-column label="主键" width="55" align="center" prop="id" /> -->
            <el-table-column label="标题" align="center" min-width="400" prop="title" />
            <el-table-column label="查看数" align="center" width="100" prop="viewNum" />
            <el-table-column label="收藏数" align="center" width="100" prop="collectNum" />
            <el-table-column label="评论数" align="center" width="100" prop="commentNum" />
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
    </div>
</template>

<script>
import { listStat, getStat, delStat, addStat, updateStat } from "@/api/cms/stat";

export default {
    name: "Stat",
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
            // 资讯表格数据
            statList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                title: null
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {}
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询资讯列表 */
        getList() {
            this.loading = true;
            listStat(this.queryParams).then(response => {
                this.statList = response.rows;
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
                viewNum: null,
                collectNum: null,
                commentNum: null
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
        /** 导出按钮操作 */
        handleExport() {
            this.$modal.confirm('确定导出所有资讯统计吗？').then(() => {
                this.download('cms/stat/export', {
                    ...this.queryParams
                }, `stat_${new Date().getTime()}.xlsx`)
            }).catch(() => { });
        }
    }
};
</script>