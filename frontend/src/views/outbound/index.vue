<!-- 患者管理/ 患者列表 -->
<template>
    <el-row class="app-container">
        <el-col v-loading="isLoadingTreeList" :span="5">
            <div class="user_tree_title">外呼任务</div>
            <el-scrollbar class="scrollbar_tree">
                <el-tree :data="centerTreeList" class="user_tree_line" :props="defaultProps" highlight-current
                    ref="centralTree" :indent="0" accordion @node-click="nodeClick"
                    :default-expanded-keys="defaultExpandedKeys" node-key="nodeId" :current-node-key="currentNodeKey">
                    <span class="custom-tree-node" slot-scope="{ data }">
                        <span>{{ data.nodeName ? data.nodeName : '无' }}</span>
                        <span v-if="data.childrenList"> （待处理：{{ data.handleNum ? data.handleNum : '0'}}） </span>
                    </span>
                </el-tree>
            </el-scrollbar>
        </el-col>
        <el-col :span="19">
            <el-form :model="queryParams" ref="queryForm" :inline="true" class="search_form" @submit.native.prevent>
                <el-form-item label="患者姓名" prop="patientName">
                    <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable />
                </el-form-item>
                <el-form-item label="患者ID" prop="patientId">
                    <el-input v-model="queryParams.patientId" placeholder="请输入患者ID" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="searchPatientList">搜索</el-button>
                    <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
            <!-- 表格风格 -->
            <el-table v-loading="loading" :data="patientList">
                <el-table-column label="患者ID" align="center" prop="patientId" :show-overflow-tooltip="true"
                    min-width="110" />
                <el-table-column label="患者" align="center" prop="patientName" :show-overflow-tooltip="true"
                    min-width="130">
                    <template slot-scope="scope">
                        {{scope.row.patientName | nullFormat}}
                    </template>
                </el-table-column>
                <el-table-column label="阶段" align="center" prop="name" :show-overflow-tooltip="true" min-width="150">
                    <template slot-scope="scope">
                        {{scope.row.name | nullFormat}}
                    </template>
                </el-table-column>
                <el-table-column label="随访日期" align="center" prop="planVisitTime" :show-overflow-tooltip="true"
                    min-width="150">
                    <template slot-scope="scope">
                        {{scope.row.planVisitTime | nullFormat}}
                    </template>
                </el-table-column>
                <el-table-column label="计划提醒时间" align="center" prop="outboundTime" :show-overflow-tooltip="true"
                    min-width="150">
                    <template slot-scope="scope">
                        {{scope.row.outboundTime | nullFormat}}
                    </template>
                </el-table-column>
                <el-table-column label="智能外呼状态" align="center" prop="xunfeiCallResult" :show-overflow-tooltip="true"
                    min-width="150" v-if="nodeTypeStatus==2">
                    <template slot-scope="scope">
                        {{scope.row.xunfeiEndNode | convertOutboundEndCode}}
                    </template>
                </el-table-column>
                <el-table-column label="智能外呼结果" align="center" prop="xunfeiCallResult" :show-overflow-tooltip="true"
                    min-width="150" v-if="nodeTypeStatus==2||nodeTypeStatus==3">
                    <template slot-scope="scope">
                        {{scope.row.xunfeiCallResult | nullFormat}}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" fixed="right" width="120">
                    <template slot-scope="scope">
                        <el-button type="text" @click="checkFollowUp(scope.row)" :disabled="nodeTypeStatus==1"
                            class="el-button-text_warning">查看外呼</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize" @pagination="getList($route.query)" />
        </el-col>
    </el-row>
</template>

<script>
import { getOutboundTree, getOutboundList } from '@/api/outbound/index'

export default {
    name: "outbound_list",
    data() {
        return {
            defaultProps: { children: 'childrenList', label: 'nodeName', key: 'nodeId' },
            isLoadingTreeList: false,
            defaultExpandedKeys: [],
            currentNodeKey: '',
            currentDeptId: '',
            researchId: '',
            nodeLevel: '',
            nodeType: '',
            nodeTypeStatus: '',
            centerTreeList: [],
            // 遮罩层
            loading: false,
            total: 0,
            // 参数表格数据
            patientList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                patientName: undefined,
                patientId: undefined,
            },
        };
    },
    created() {
        const queryDaya = this.$route.query
        if (queryDaya.defaultExpandedKeys) {
            this.defaultExpandedKeys = queryDaya.defaultExpandedKeys ? queryDaya.defaultExpandedKeys.split(',') : []
            this.currentNodeKey = this.defaultExpandedKeys[this.defaultExpandedKeys.length - 1]
            this.researchId = queryDaya.researchId || ''
            this.nodeLevel = queryDaya.nodeLevel
            this.nodeType = queryDaya.nodeType
            this.nodeTypeStatus = queryDaya.nodeTypeStatus
            this.getList({
                nodeType: queryDaya.nodeType,
                nodeTypeStatus: queryDaya.nodeTypeStatus,
                researchId: queryDaya.researchId,
                nodeLevel: queryDaya.nodeLevel
            });
        }
        this.getTreeList();
    },
    methods: {
        getTreeList() {
            this.isLoadingTreeList = true;
            getOutboundTree().then(response => {
                let responseData = response.data && response.data.childrenList ? response.data.childrenList : []
                this.centerTreeList = responseData.filter(f=>f&&f.childrenList);
                this.isLoadingTreeList = false;
            }).catch(error => {
                this.isLoadingTreeList = false;
            })
        },
        setDefaultExpanded(data) {
            if (data && data.length) {
                this.defaultExpandedKeys.push(data[0].nodeId)
                this.setDefaultExpanded(data[0].children)
            }
        },
        /** 查询参数列表 */
        getList(data) {
            this.loading = true;
            let params = {
                ...this.queryParams,
            }
            if (data) {
                params = {
                    ...params,
                    researchId: data.researchId,
                    outboundType: 1,
                    remindType: data.nodeType,
                    status: data.nodeTypeStatus
                }
            }
            getOutboundList(params).then(response => {
                this.patientList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        nodeClick(data, node) {
            this.defaultExpandedKeys = []
            this.currentNodeKey = data.nodeId
            this.nodeLevel = data.nodeLevel
            this.nodeType = data.nodeType
            this.nodeTypeStatus = data.nodeTypeStatus
            this.convertDefaultKeys(node)
            this.getList(data);
            this.researchId = data.researchId
            this.$router.push({
                path: this.$route.path,
                query: {
                    defaultExpandedKeys: this.defaultExpandedKeys.toString(),
                    nodeTypeStatus: this.nodeTypeStatus,
                    nodeType: this.nodeType,
                    researchId: this.researchId,
                    nodeLevel: this.nodeLevel
                }
            })
        },
        //添加当前组件选中的keys值
        convertDefaultKeys(node) {
            if (node.level > 0) {
                this.defaultExpandedKeys.unshift(node.data.nodeId)
                this.convertDefaultKeys(node.parent)
            }
        },
        /** 搜索按钮操作 */
        searchPatientList() {
            this.queryParams.pageNum = 1;
            this.getList(this.$route.query);
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
                path: `/outbound/details?outboundId=${row.id}`
            })
        },
    }
};
</script>
<style lang="scss" scoped>
.user_tree_title {
    height: 40px;
    font-size: 16px;
    font-weight: bold;
}
.scrollbar_tree {
    height: calc(100vh - 194px);
    .my_tree {
        margin-left: -20px;
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
    margin-right: 10px;
}
</style>