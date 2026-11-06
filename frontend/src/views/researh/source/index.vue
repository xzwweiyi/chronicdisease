<template>
    <div class="queue-container">
        <el-scrollbar class="page_container" ref="queueScrollbar">
            <div class="scrollbar_content">
                <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
                    <el-form-item label="资源名称" prop="sourceName">
                        <el-input v-model="queryParams.sourceName" placeholder="请输入资源名称" clearable @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="状态" prop="status">
                        <el-select v-model="queryParams.status" placeholder="资源状态" clearable>
                            <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                    </el-form-item>
                </el-form>

                <el-row :gutter="10" class="mb8">
                    <el-col :span="1.5">
                        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['system:dept:add']">新增</el-button>
                    </el-col>
                    <!-- <el-col :span="1.5">
                        <el-button type="info" plain icon="el-icon-sort" @click="toggleExpandAll">展开/折叠</el-button>
                    </el-col> -->
                    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
                </el-row>

                <el-table v-if="refreshTable" v-loading="loading" :data="deptList" row-key="rowKey" highlight-current-row :key="refreshTable" @row-click="toggleExpandRow" :expand-row-keys="expandRowKeys" :default-expand-all="isExpandAll" class="dept_table" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                    <el-table-column prop="sourceName" label="资源名称" min-width="300"></el-table-column>
                    <el-table-column prop="perms" label="权限标识" min-width="200"></el-table-column>
                    <el-table-column prop="status" label="状态" min-width="200">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
                        </template>
                    </el-table-column>
                    <el-table-column label="创建时间" align="center" prop="createTime" min-width="280">
                        <template slot-scope="scope">
                            <span>{{ scope.row.createTime | dateFormat }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" width="300">
                        <template slot-scope="scope">
                            <el-button type="primary" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)" v-hasPermi="['system:dept:edit']" class="ptb710">修改</el-button>
                            <el-button type="success" icon="el-icon-plus" @click.stop="handleAdd(scope.row)" v-hasPermi="['system:dept:add']" class="ptb710">新增</el-button>
                            <el-button v-if="scope.row.parentId != 0" type="danger" icon="el-icon-delete" @click.stop="handleDelete(scope.row)" v-hasPermi="['system:dept:remove']" class="ptb710">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-scrollbar>

        <!-- 添加或修改资源对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="600px">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-row>
                    <el-col :span="24" v-if="form.parentId !== 0">
                        <el-form-item label="上级资源" prop="parentId">
                            <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级资源" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-form-item label="资源名称" prop="sourceName">
                        <el-input v-model="form.sourceName" placeholder="请输入资源名称" />
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="资源标识" prop="perms">
                            <el-input v-model="form.perms" placeholder="请输入标识" maxlength="50" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="资源状态">
                            <el-radio-group v-model="form.status">
                                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listSource,
    addSource,
    updateSource,
    delSource
} from "@/api/researh/source";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
    name: "research_source",
    dicts: ['sys_normal_disable'],
    components: {
        Treeselect
    },
    data() {
        return {
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: true,
            // 表格树数据
            deptList: [],
            // 资源树选项
            deptOptions: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否展开，默认全部展开
            isExpandAll: false,
            // 重新渲染表格状态
            refreshTable: true,
            // 查询参数
            queryParams: {
                sourceName: undefined,
                status: undefined
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                parentId: [{ required: true, message: "上级资源不能为空", trigger: "blur" }],
                sourceName: [{ required: true, message: "资源名称不能为空", trigger: "blur" }]

            },
            expandRowKeys: [],
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询资源列表 */
        getList() {
            this.loading = true;
            listSource(this.queryParams).then(response => {
                let responseData = this.handleTree(response.rows, "sourceId");
                this.deptList = responseData.map(m => {
                    return {
                        ...m,
                        rowKey: `${m.sourceId}`,
                        children: this.convertChildren(m.child, m.sourceId, m.deptName)
                    }
                });
                if (this.deptList.length) {
                    this.expandRowKeys.push(this.deptList[0].rowKey)
                }
                this.loading = false;
            });
        },
        // 
        convertChildren(children, id, deptName, parentIds) {
            if (children && children.length) {
                let child = children.map(m => {
                    let parentName = Array.isArray(deptName) ? [...deptName] : [deptName];
                    let nextParentName = [...parentName, m.deptName]
                    let parent_ids = parentIds ? parentIds : [id]
                    let nextParentIds = [...parent_ids, m.sourceId]
                    return {
                        ...m,
                        rowKey: `${id}_${m.sourceId}`,
                        parentName: parentName,
                        parentIds: parent_ids,
                        children: this.convertChildren(m.child, id, nextParentName, nextParentIds)
                    }
                })
                return child
            } else {
                return null
            }
        },
        /** 转换资源数据结构 */
        normalizer(node) {
            if (node.child && !node.child.length) {
                delete node.child;
            }
            return {
                id: node.sourceId,
                label: node.sourceName,
                children: node.child
            };
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                sourceId: undefined,
                parentId: undefined,
                sourceName: undefined,
                orderNum: undefined,
                leader: undefined,
                phone: undefined,
                email: undefined,
                status: "0"
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        /** 新增按钮操作 */
        handleAdd(row) {
            this.reset();
            if (row != undefined) {
                this.form.parentId = row.sourceId;
            }
            this.open = true;
            this.title = "添加资源";
            listSource(this.queryParams).then(response => {
                this.deptOptions = this.handleTree(response.rows, "sourceId");
            });
        },
        /** 展开/折叠操作 */
        toggleExpandRow(row, column, cell) {
            if (row.children && column.label != '操作') {
                if (cell.currentTarget.querySelector(".el-table__expand-icon")) {
                    cell.currentTarget.querySelector(".el-table__expand-icon").click()
                }
            }
        },
        /** 展开/折叠操作 */
        toggleExpandAll() {
            this.refreshTable = false;
            this.isExpandAll = !this.isExpandAll;
            this.$nextTick(() => {
                this.refreshTable = true;
            });
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            this.form = row;
            this.open = true;
            this.title = "修改资源";
            //   getDept(row.sourceId).then(response => {
            //     this.form = response.data;
            //     this.open = true;
            //     this.title = "修改资源";
            //   });
            listSource(this.queryParams).then(response => {
                this.deptOptions = this.handleTree(response.rows, "sourceId");
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.sourceId != undefined) {
                        updateSource(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addSource(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            this.$modal.confirm('是否确认删除名称为"' + row.sourceName + '"的数据项？').then(function () {
                return delSource({
                    sourceId: row.sourceId
                });
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        }
    }
};
</script>
<style lang="scss" scoped>
::v-deep .dept_table {
    .el-table__row--level-0,
    .el-table__row--level-1 {
        cursor: pointer;
    }
}

.page_container {
    background: #fff;
    margin: 15px;
    border-radius: 5px;
    height: calc(100vh - 114px);

    .scrollbar_content {
        padding: 20px 20px 30px;
    }
}
</style>