<template>
    <!-- 医护工作量统计 -->
    <div class="app-container">
        <el-form :model="workloadParams" ref="workloadParams" class="search_form" inline>
            <el-form-item label="科室:">
                <el-cascader ref="deptCascader" v-model="workloadParams.deptIds" :options="administrativeOption"
                    placeholder="全部" clearable :show-all-levels="false" collapse-tags :props="cascaderProps" filterable
                    @change="handleCascaderChange">
                </el-cascader>
            </el-form-item>
            <el-form-item label="科室人员:">
                <el-input v-model="workloadParams.userName" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="时间范围:">
                <el-date-picker v-model="dateRange" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" @change="dateRangeChange" class="date_range" value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="searchList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetParams">重置</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="workloadData" style="width: 100%" border>
            <el-table-column align="center" prop="deptName" label="科室名称" min-width="160" fixed="left">
                <template slot-scope="scope">
                    {{ scope.row.deptName | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column align="center" prop="nickName" label="科室人员" min-width="160" fixed="left">
                <template slot-scope="scope">
                    {{ scope.row.nickName | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column align="center" prop="enrollmentHandleNums" label="入组审批量" width="135" sortable>
            </el-table-column>
            <el-table-column align="center" prop="setOpinionHandleNums" label="处理随访量" width="135" sortable>
            </el-table-column>
            <el-table-column align="center" prop="warnGuideNums" label="处理预警量" width="135" sortable>
            </el-table-column>
            <el-table-column align="center" prop="visitTimeHandleNums" label="处理超窗量" width="135" sortable>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="workloadParams.pageNum" :limit.sync="workloadParams.pageSize" @pagination="getWorkloadList" />
    </div>
</template>

<script>
import {
    getUser
} from "@/api/system/user"
import {
    treeselect,
} from "@/api/system/dept"
import {
    getWorkLoadData
} from "@/api/statistics/config"
export default {
    name: "workload",
    data() {
        return {
            isLoading: false,
            workloadParams: {
                pageNum: 1,
                pageSize: 10,
                deptIds: []
            },
            cascaderProps: {
                checkStrictly: true,
                value: 'id',
                label: 'label',
                children: 'children',
                multiple: true,
                emitPath: false,
                expandTrigger: 'hover'
            },
            workloadData: [],
            roseList: [],
            administrativeOption: [],
            total: 0,
            dateRange: '',
        };
    },
    created() {
        this.initData();
    },
    methods: {
        // 初始化数据
        initData() {
            this.getAdministrativeOption()
            this.getRoleOption()
            this.getWorkloadList()
        },
        // 获取科室下拉数据
        getAdministrativeOption() {
            treeselect().then(res => {
                this.administrativeOption = res.data[0].children
            })
        },
        // 获取角色下拉数据
        getRoleOption() {
            getUser().then(res => {
                this.roseList = res.roles
            })
        },
        handleCascaderChange(val) {
            if (val && val.length > 0) {
                if (Array.isArray(val[0])) {
                    this.workloadParams.deptIds = val.map(item => item[item.length - 1]);
                } else {
                    this.workloadParams.deptIds = val;
                }
            } else {
                this.workloadParams.deptIds = [];
            }
        },
        // 自定义时间change
        dateRangeChange(event) {
            if (event) {
                this.workloadParams.startTime = event[0];
                this.workloadParams.endTime = event[1];
            } else {
                this.workloadParams.startTime = null;
                this.workloadParams.endTime = null;
            }
        },
        resetParams() {
            this.workloadParams = {
                pageNum: 1,
                pageSize: 10,
                deptIds: []
            }
            this.dateRange = []
            this.resetForm("workloadParams");
            this.searchList()
        },
        searchList() {
            this.workloadParams.pageNum = 1;
            this.getWorkloadList()
        },
        // 获取医护工作量统计
        getWorkloadList() {
            getWorkLoadData(this.workloadParams).then(res => {
                this.workloadData = res.rows;
                this.total = res.total
            })
        }
    }
};
</script>
<style lang="scss" scoped>
    .el-form{
        .el-select{
            width:170px;
        }
    }
</style>