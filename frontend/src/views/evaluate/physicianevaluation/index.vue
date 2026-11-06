<template>
    <div class="app-container">
        <div class="evaluation_statistic">
            <div class="statistic_info">
                <p class="statistic_tit statistic_mediumn">综合满意度</p>
                <p class="statistic_val big_number_val">{{averageVal}}</p>
            </div>
            <div class="statistic_info">
                <p class="statistic_tit statistic_mediumn">总评价数</p>
                <p class="statistic_val big_number_val">{{sumTotalVal}}</p>
            </div>
        </div>
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="search_form">
            <el-form-item label="科室:" prop="deptId">
                <el-select v-model="queryParams.deptId" placeholder="全部" clearable>
                    <el-option v-for="(item,index) in administrativeOption" :key="index" :label="item.label"
                        :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="姓名:" prop="nickName">
                <el-input v-model="queryParams.nickName" placeholder="请输入姓名" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="满意度评分：" prop="score">
                <el-select v-model="queryParams.score" placeholder="全部" clearable style="width:100px">
                    <el-option v-for="(item,index) in [1,2,3,4,5]" :key="index" :label="item" :value="item"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <div class="table_list">当前筛选：{{ listTotal }}条</div>
        <el-table v-loading="loading" :data="userList">
            <el-table-column label="姓名" align="center" width="110" key="nickName" prop="nickName" />
            <el-table-column label="科室" align="center" min-width="140" key="deptName" prop="deptName" />
            <el-table-column label="满意度评分" align="center" width="120" key="score" prop="score"
                :show-overflow-tooltip="true" />
            <el-table-column label="评价时间" align="center" width="190" key="createTime" prop="createTime">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat}}</span>
                </template>
            </el-table-column>
            <el-table-column label="描述" align="center" key="suggestion" prop="suggestion" min-width="300">
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import { getEvaluationPhysicianList } from "@/api/evaluate/config";
import { treeselect, } from "@/api/system/dept"

export default {
    name: "physician_evaluatation",
    data() {
        return {
            // 遮罩层
            loading: false,
            // 显示搜索条件
            // 弹出层标题
            userList: [],

            // 查询参数
            queryParams: {
                deptId: '',
                nickName: '',
                score: '',
            },
            administrativeOption: [],
            satisfactionOption: [],
            sumTotalVal: 0,
            averageVal: 0,
            listTotal: 0,
        };
    },
    watch: {},
    created() {
        this.initData()
    },
    methods: {
        initData() {
            this.getEvaluateList();
            this.getAdministrativeOption();
        },
        // 获取科室下拉数据
        getAdministrativeOption() {
            treeselect().then(res => {
                this.administrativeOption = res.data[0].children
            })
        },
        /** 查询用户列表 */
        getEvaluateList(data) {
            this.loading = true;

            getEvaluationPhysicianList(this.queryParams).then((response) => {
                this.userList = response.list;
                this.loading = false;
                this.sumTotalVal = response.sumTotal;
                this.averageVal = response.average;
                this.listTotal = response.total;
            }
            );
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.getEvaluateList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
    },
};
</script>

<style lang="scss" scoped>
.app-container {
    height: calc(100vh - 114px);
}
.evaluation_statistic {
    display: flex;
    margin-bottom: 10px;
    .statistic_info {
        min-width: 200px;
        text-align: center;
        color: #fff;
        padding: 10px 0;
        border-radius: 8px;
        &:first-child {
            background: #67c23a;
            margin-right: 20px;
        }
        &:last-child {
            background: #409eff;
        }
    }
}
.table_list {
    color: #444;
    margin-bottom: 5px;
}
</style>