<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="${comment}" prop="researchId">
                <el-input v-model="queryParams.researchId" placeholder="请输入${comment}" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="用户ID" prop="accountId">
                <el-input v-model="queryParams.accountId" placeholder="请输入用户ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="医院ID" prop="hospitalId">
                <el-input v-model="queryParams.hospitalId" placeholder="请输入医院ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="科室ID" prop="departId">
                <el-input v-model="queryParams.departId" placeholder="请输入科室ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                    v-hasPermi="['research:account:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['research:account:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['research:account:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['research:account:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="accountList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="${comment}" align="center" prop="id" />
            <el-table-column label="${comment}" align="center" prop="researchId" />
            <el-table-column label="用户ID" align="center" prop="accountId" />
            <el-table-column label="医院ID" align="center" prop="hospitalId" />
            <el-table-column label="科室ID" align="center" prop="departId" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['research:account:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['research:account:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改队列用户信息对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="${comment}" prop="researchId">
                    <el-input v-model="form.researchId" placeholder="请输入${comment}" />
                </el-form-item>
                <el-form-item label="用户ID" prop="accountId">
                    <el-input v-model="form.accountId" placeholder="请输入用户ID" />
                </el-form-item>
                <el-form-item label="医院ID" prop="hospitalId">
                    <el-input v-model="form.hospitalId" placeholder="请输入医院ID" />
                </el-form-item>
                <el-form-item label="科室ID" prop="departId">
                    <el-input v-model="form.departId" placeholder="请输入科室ID" />
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
    listAccount,
    getAccount,
    delAccount,
    addAccount,
    updateAccount
} from "@/api/research/account";

export default {
    name: "Account",
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
            // 队列用户信息表格数据
            accountList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                researchId: null,
                accountId: null,
                hospitalId: null,
                departId: null,
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
        /** 查询队列用户信息列表 */
        getList() {
            this.loading = true;
            listAccount(this.queryParams).then(response => {
                this.accountList = response.rows;
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
                researchId: null,
                accountId: null,
                hospitalId: null,
                departId: null,
                createTime: null
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
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加队列用户信息";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getAccount(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改队列用户信息";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateAccount(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addAccount(this.form).then(response => {
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
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除队列用户信息编号为"' + ids + '"的数据项？').then(function () {
                return delAccount(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('research/account/export', {
                ...this.queryParams
            }, `account_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>