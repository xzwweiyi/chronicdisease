<template>
    <div class="app-container">
        <el-row :gutter="20">
            <el-col :span="6" :xs="24" style="height:calc( 100vh - 134px)">
                <el-scrollbar style="height:100%" wrap-style="overflow-x:hidden;">
                    <div class="head-container">
                        <el-tree :data="deptOptions" :props="defaultProps" ref="tree" accordion @node-click="handleNodeClick" highlight-current :default-expanded-keys="defaultExpandedKeys" node-key="nodeId" :current-node-key="currentNodeKey">
                            <span class="custom-tree-node" slot-scope="{ data }">
                                <span>{{ data.nodeName }}</span>
                                <span v-if="data.childrenList"> （待处理：{{ data.handleNum }}） </span>
                            </span>
                        </el-tree>
                    </div>
                </el-scrollbar>
            </el-col>
            <el-col :span="18" :xs="24">
                <div class="fatherBox">
                    <el-scrollbar style="height:100%">
                        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" class="search_form">
                            <el-form-item label="患者名称" prop="patientName">
                                <el-input v-model="queryParams.patientName" placeholder="请输入患者名称" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
                            </el-form-item>
                            <el-form-item label="患者ID" prop="patientId">
                                <el-input v-model="queryParams.patientId" placeholder="请输入患者ID" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索
                                </el-button>
                                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                            </el-form-item>
                        </el-form>

                        <el-row class="mb8">
                            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                                v-hasPermi="['system:user:export']">导出</el-button> -->
                            <right-toolbar :showSearch.sync="showSearch" @queryTable="getEvaluateList" :columns="columns"></right-toolbar>
                        </el-row>

                        <el-table v-loading="loading" :data="userList">
                            <el-table-column type="selection" width="50" align="center" />
                            <el-table-column label="患者ID" align="center" width="80" key="patientId" prop="patientId" v-if="columns[0].visible" />
                            <el-table-column label="患者名称" align="center" width="120" key="patientName" prop="patientName" v-if="columns[1].visible" />
                            <el-table-column label="阶段名称" align="center" key="name" prop="name" v-if="columns[2].visible" :show-overflow-tooltip="true" />
                            <el-table-column label="队列名称" align="center" key="projectName" prop="projectName" v-if="columns[3].visible" :show-overflow-tooltip="true" />
                            <el-table-column label="创建时间" align="center" prop="createTime" width="200" v-if="columns[4].visible">
                                <template slot-scope="scope">
                                    <span>{{ scope.row.createTime | dateFormat }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" fixed="right" width="140" class-name="small-padding fixed-width">
                                <template slot-scope="scope">
                                    <el-button type="text" icon="el-icon-edit" @click="handleUserDetail(scope.row)" v-hasPermi="['system:user:edit']">查看随访
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>

                        <!-- <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                            :limit.sync="queryParams.pageSize" @pagination="getEvaluateList" /> -->
                    </el-scrollbar>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {
    treeEvaluate,
    evaluateList
} from "@/api/evaluate/config";

export default {
    name: "evaluate",
    data() {
        return {
            // 遮罩层
            loading: false,
            // 显示搜索条件
            showSearch: true,
            // 弹出层标题
            title: "",
            userList: [],
            deptOptions: [],
            // 表单参数
            form: {},
            defaultProps: { children: "childrenList", label: "nodeName", },

            // 查询参数
            queryParams: { patientName: '', patientId: '', },
            // 列信息
            columns: [
                { key: 0, label: `患者ID`, visible: true },
                { key: 1, label: `患者名称`, visible: true },
                { key: 2, label: `阶段名称`, visible: true },
                { key: 3, label: `队列名称`, visible: true },
                { key: 4, label: `创建时间`, visible: true },
            ],
            belongType: '',
            evaluateStatus: '',
            researchId: '',
            defaultExpandedKeys: [],
            currentNodeKey: '',
        };
    },
    watch: {
        // 根据名称筛选部门树
        deptName(val) {
            this.$refs.tree.filter(val);
        },
    },
    created() {
        const queryDaya = this.$route.query
        if (queryDaya.defaultExpandedKeys) {
            this.defaultExpandedKeys = queryDaya.defaultExpandedKeys ? queryDaya.defaultExpandedKeys.split(',') : []
            this.currentNodeKey = this.defaultExpandedKeys[this.defaultExpandedKeys.length - 1]
            this.researchId = queryDaya.researchId
            this.nodeLevelType = queryDaya.nodeLevelType
            this.nodeType = queryDaya.nodeType
            this.handleQuery({
                nodeType: queryDaya.nodeType,
                nodeTypeStatus: queryDaya.nodeTypeStatus,
                researchId: queryDaya.researchId,
                nodeLevelType: queryDaya.nodeLevelType
            });
        }
        this.getTreeselect();
    },
    methods: {
        /** 查询用户列表 */
        getEvaluateList(data) {
            this.loading = true;
            let objData = {
                belongType: this.belongType,
                evaluateStatus: this.evaluateStatus,
                researchId: this.researchId,
                patientId: this.queryParams.patientId,
                patientName: this.queryParams.patientName,
            }
            evaluateList(objData).then(
                (response) => {
                    this.userList = response.rows;
                    this.loading = false;
                }
            );
        },
        /** 查询部门下拉树结构 */
        getTreeselect() {
            treeEvaluate().then((response) => {
                this.deptOptions = response.data.childrenList;
            });
        },
        // 筛选节点
        // filterNode(value, data) {
        //   if (!value) return true;
        //   return data.label.indexOf(value) !== -1;
        // },
        // 节点单击事件
        handleNodeClick(data, node) {
            this.defaultExpandedKeys = []
            this.currentNodeKey = node.data.nodeId
            this.convertDefaultKeys(node)
            this.belongType = String(data.belongType)
            this.evaluateStatus = data.nodeType
            this.researchId = data.researchId
            if (data.childrenList == null) {
                this.getEvaluateList()
            }

            this.$router.push({
                path: this.$route.path,
                query: {
                    defaultExpandedKeys: this.defaultExpandedKeys.toString(),
                    nodeTypeStatus: this.nodeTypeStatus,
                    nodeType: this.nodeType,
                    researchId: this.researchId,
                    nodeLevelType: this.nodeLevelType
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

        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                patientName: undefined,
                patientId: undefined,
            };
            this.resetForm("form");
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
        /** 导出按钮操作 */
        handleExport() {
            this.download(
                "system/user/export", {
                ...this.queryParams,
            },
                `user_${new Date().getTime()}.xlsx`
            );
        },
        // 随访意见
        handleUserDetail(item) {
            this.$router.push({
                path: "/quality/evaluateDetail",
                query: {
                    patientId: item.patientId,
                    followId: item.followId,
                    recordId: item.recordId,
                    oldPath: `${this.$route.path}?defaultExpandedKeys=${this.defaultExpandedKeys.toString()}&nodeTypeStatus=${this.nodeTypeStatus}&nodeType=${this.nodeType}&researchId=${this.researchId}&nodeLevelType=${this.nodeLevelType}`
                }

            });
        },
    },
};
</script>

<style lang="scss" scoped>
    .app-container {
        height: calc(100vh - 114px);
    }

    .custom-tree-node {
        font-size: 14px;
    }

    .fatherBox {
        height: calc(100vh - 140px);
        margin-left: 10px;

        .el-scrollbar__wrap {
            overflow-x: hidden;

            &::-webkit-scrollbar {
                width: 17px;
            }

        }
    }

    /* element滚动条组件 隐藏水平滚动条 */

    .is-horizontal {
        display: none;
    }
</style>