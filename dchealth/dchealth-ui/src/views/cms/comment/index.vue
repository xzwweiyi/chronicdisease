<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="标题" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
                <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <div class="mb8">
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['cms:comment:remove']">删除</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['cms:comment:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </div>

        <el-table v-loading="loading" :data="commentList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="标题" align="center" min-width="300" prop="title" />
            <el-table-column label="用户名" align="center" width="100" prop="patientName" />
            <el-table-column label="评论内容" align="center" min-width="300" prop="content">
                <template slot-scope="scope">
                    <div v-if="scope.row.content" class="reveal-container" v-html="scope.row.content"></div>
                    <span v-else>--</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200" fixed="right">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['cms:comment:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['cms:comment:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改资讯评论对话框 -->
        <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" :close-on-press-escape="false"
            width="60%">
            <el-form ref="form" :model="form" :rules="rules" v-loading="isDialogLoading" label-width="80px">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="form.title" :disabled="true" placeholder="请输入文章标题" />
                </el-form-item>
                <el-form-item label="用户名" prop="realName">
                    <el-input v-model="form.realName" :disabled="true" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="评论内容" prop="content">
                    <el-input type="textarea" :autosize="{ minRows: 3}" v-model="form.content" placeholder="请输入评论内容" />
                </el-form-item>
                <el-form-item label="删除标记" prop="delFlag">
                    <el-select v-model="form.delFlag">
                        <el-option label="是" value="Y"></el-option>
                        <el-option label="否" checked value="N"></el-option>
                    </el-select>
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
import { listComment, getComment, delComment, addComment, updateComment } from "@/api/cms/comment";

export default {
    name: "Comment",
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
            // 资讯评论表格数据
            commentList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, title: null, realName: null, },
            // 表单参数
            form: {},
            // 表单校验
            rules: {},
            isDialogLoading: false,
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询资讯评论列表 */
        getList() {
            this.loading = true;
            listComment(this.queryParams).then(response => {
                this.commentList = response.rows;
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
                articleId: null,
                tenantId: null,
                accountId: null,
                content: null,
                createTime: null,
                delFlag: 'N',
                title: null,
                realName: null,
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
            this.title = "添加资讯评论";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getComment(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改资讯评论";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.isDialogLoading = true;
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateComment(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addComment(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                    this.isDialogLoading = false;
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除资讯评论编号为"' + ids + '"的数据项？').then(function () {
                return delComment(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.$modal.confirm('确定导出所有资讯评论吗？').then(() => {
                this.download('cms/comment/export', {
                    ...this.queryParams
                }, `comment_${new Date().getTime()}.xlsx`)
            }).catch(() => { });

        }
    }
};
</script>