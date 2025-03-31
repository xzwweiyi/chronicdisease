<template>
    <!-- 医护工作量统计 -->
    <div class="app-container">
        <el-form :model="workloadParams" ref="workloadParams" class="search_form" inline>
            <el-form-item label="科室:">
                <el-select v-model="workloadParams.deptId" placeholder="全部" clearable>
                    <el-option v-for="(item,index) in administrativeOption" :key="index" :label="item.label" :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="科室人员:">
                <el-input v-model="workloadParams.userName" placeholder="请输入" clearable />
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
            <el-table-column align="center" prop="researchNums" label="管理队列数" width="135" sortable>
            </el-table-column>
            <el-table-column align="center" prop="patientNums" label="随访患者数" width="135" sortable>
            </el-table-column>
            <el-table-column align="center" prop="prescriptionBackScore" label="质效评价-回复速度均分" width="138" sortable>
            </el-table-column>
            <el-table-column align="center" prop="qualityBackScore" label="质效评价-回复质量均分" width="138" sortable>
            </el-table-column>
            <el-table-column align="center" prop="prescriptionScore" label="患者评价-回复速度均分" width="138" sortable>
            </el-table-column>
            <el-table-column align="center" prop="qualityScore" label="患者评价-满意度均分" width="138" sortable>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="workloadParams.pageNum" :limit.sync="workloadParams.pageSize" @pagination="getWorkloadList" />
    </div>
</template>

<script>
import { getUser } from "@/api/system/user"
import { treeselect } from "@/api/system/dept"
import { getWorkLoadData } from "@/api/statistics/config"
export default {
    name: "workload",
    data() {
        return {
            isLoading: false,
            workloadParams: {
                pageNum: 1,
                pageSize: 10
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
        resetParams() {
            this.workloadParams = {
                pageNum: 1,
                pageSize: 10
            }
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