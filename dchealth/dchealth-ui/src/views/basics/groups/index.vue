<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="病种名称" prop="diseasesGroupsName">
                <el-input v-model="queryParams.diseasesGroupsName" placeholder="请输入病种名称" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['diseases:groups:add']">新增</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="hospitalList">
            <el-table-column label="病种名称" align="center" prop="diseasesGroupsName" min-width="240" />
            <el-table-column label="创建时间" align="center" prop="createTime" min-width="240">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="修改时间" align="center" prop="updateTime" min-width="240">
                <template slot-scope="scope">
                    <span>{{ scope.row.updateTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="240" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['diseases:groups:edit']" class="ptb710">修改病种</el-button>
                    <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['diseases:groups:remove']" class="el-button-text_delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <!-- 添加或修改中心管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="40%" :close-on-click-modal="false"
            :close-on-press-escape="false">
            <el-form ref="editForm" :model="editForm" :rules="rules" label-width="100px">
                <el-form-item label="病种名称" prop="diseasesGroupsName">
                    <el-input v-model="editForm.diseasesGroupsName" placeholder="请输入病种名称" />
                </el-form-item>
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
    listDiseasesGroupsPages, addDiseasesGroups, updateDiseasesGroups, delDiseasesGroups
} from "@/api/basics/config";

export default {
    name: "Department",
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
            // 中心管理表格数据
            hospitalList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, diseasesGroupsName: null, },
            // 表单参数
            editForm: {},
            // 表单校验
            rules: {
                diseasesGroupsName: [{ required: true, message: "病种名称不能为空", trigger: "blur" }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询科病种列表 */
        getList() {
            this.loading = true;
            listDiseasesGroupsPages(this.queryParams).then(response => {
                this.hospitalList = response.rows;
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
            this.editForm = {
                diseasesGroupsName: null
            };
            this.resetForm("editForm");
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
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加病种管理";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            this.editForm = {...row}
            this.open = true;
            this.title = "修改病种管理";
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["editForm"].validate(valid => {
                if (valid) {
                    if (this.editForm.diseasesGroupsId != null) {
                        updateDiseasesGroups(this.editForm).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addDiseasesGroups(this.editForm).then(response => {
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
            const ids = row.diseasesGroupsId || this.ids;
            this.$modal.confirm('是否确认删除病种"' + row.diseasesGroupsName + '"的数据项？').then(function () {
                return delDiseasesGroups({ diseasesGroupsId: ids });
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        }
    }
};
</script>
<style>
.selectStyle {
    width: 200px;
    margin-right: 10px;
}

.selectStyle:last-child {
    margin-right: 0;
}
</style>