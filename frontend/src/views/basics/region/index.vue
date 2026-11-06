<template>
    <div class="app-container" v-loading="loading">
        <el-row :gutter="20">
            <el-col :span="4" :xs="24">
                <div class="head-container">
                    <el-input v-model="areaName" placeholder="请输入地区" clearable prefix-icon="el-icon-search"
                        style="margin-bottom: 20px" />
                </div>
                <el-scrollbar class="right_scrollbar">
                    <el-tree :data="areaOptions" class="user_tree_line" :props="defaultProps"
                        :filter-node-method="filterNode" ref="leftTree" highlight-current node-key="areaId" :indent="0"
                        :current-node-key="currentNodeKey" @node-click="handleNodeClick" accordion
                        :default-expanded-keys="[1]">
                        <span class="custom-tree-node" slot-scope="{ data }">
                            {{ data.areaName }}
                        </span>
                    </el-tree>
                </el-scrollbar>
            </el-col>
            <el-col :span="20" :xs="24">
                <el-table :data="areaList" height="calc(100vh - 180px)">
                    <el-table-column label="地区编号" align="center" key="areaCode" prop="areaCode" />
                    <el-table-column label="地区名称" align="center" key="areaName" prop="areaName"
                        :show-overflow-tooltip="true" />
                    <el-table-column label="城市编码" align="center" key="cityCode" prop="cityCode"
                        :show-overflow-tooltip="true" />
                    <el-table-column label="地区纬度" align="center" key="latitude" prop="latitude"
                        :show-overflow-tooltip="true" />
                    <el-table-column label="地区经度 " align="center" key="longitude" prop="longitude" width="120" />
                    <el-table-column label="创建时间" align="center" prop="createTime" width="190">
                        <template slot-scope="scope">
                            <span>{{ scope.row.createTime | dateFormat}}</span>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column label="操作" align="center" width="260" fixed="right"
                        class-name="small-padding fixed-width">
                        <template slot-scope="scope" v-if="scope.row.userId !== 1">
                            <el-button type="primary" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                                v-hasPermi="['system:user:edit']" class="ptb710">修改</el-button>
                            <el-button type="success" icon="el-icon-delete" @click="handleDelete(scope.row)"
                                v-hasPermi="['system:user:remove']" class="ptb710">删除</el-button>
                        </template>
                    </el-table-column> -->
                </el-table>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {
    listArea,
    treeArea,
    areaChild
} from "@/api/basics/area";

export default {
    name: "Region",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            areaName: '',
            // 地区
            areaOptions: [],
            defaultProps: {
                children: "child",
                label: "areaName"
            },
            areaList: [],
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                areaName: ''
            },
            // 表单参数
            form: {},
            areaId: '',
            currentNodeKey: '',
        };
    },
    created() {
        this.getTree()
    },
    methods: {
        /**
         * 查询地区树
         */
        getTree() {
            this.loading = true;
            treeArea().then(response => {
                this.areaOptions = response.data
                this.areaId = response.data[0].areaId

                this.currentNodeKey = this.areaId
                this.$nextTick(function () {
                    this.$refs.leftTree.setCurrentKey(this.areaId)
                })
                this.getListChild()
                this.loading = false
            })
        },
        /** 查询二三地区列表 */
        getListChild() {
            let dataObj = {
                areaId: this.areaId
            }
            this.loading = true;
            areaChild(dataObj).then((response) => {
                this.areaList = response
                this.loading = false;
            });
        },
        // 筛选节点
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        /**
         * 查询子级地区下的列表
         */
        handleNodeClick(data, node) {
            this.areaId = data.areaId
            this.currentNodeKey = node.data.areaId
            this.getListChild()
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                diseasesGroupsName: null,
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getListChild();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加病种管理";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.diseasesGroupsId != null) {
                        updateDiseasesGroups(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getListChild();
                        });
                    } else {
                        addDiseasesGroups(this.form).then((response) => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getListChild();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.areaId || this.ids;
            this.$modal
                .confirm('是否确认删除地区"' + row.areaName + '"的数据项？')
                .then(function () {
                    // return delDiseasesGroups(ids);
                })
                .then(() => {
                    this.getListChild();
                    this.$modal.msgSuccess("删除成功");
                })
                .catch(() => { });
        },
    },
};
</script>
<style lang="scss">
.selectStyle {
    width: 200px;
    margin-right: 10px;
}

.selectStyle:last-child {
    margin-right: 0;
}

.right_scrollbar {
    height: calc(100vh - 220px);
}

.query_params {
    .el-input {
        width: 240px;
    }
}
</style>