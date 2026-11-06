<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="分类名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入分类名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                    <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['cms:eduCategory:add']">新增</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['cms:eduCategory:remove']">删除</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="categoryList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" align="center" />
            <el-table-column label="分类名称" align="center" prop="name" />
            <el-table-column label="文章数量" align="center" prop="articleCount" width="100">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.articleCount && scope.row.articleCount > 0" type="success">
                        {{ scope.row.articleCount }}
                    </el-tag>
                    <span v-else>0</span>
                </template>
            </el-table-column>
            <el-table-column label="显示排序" align="center" prop="sort" width="90">
                <template slot-scope="scope">
                    {{ scope.row.sort | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="status" width="90">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark">
                <template slot-scope="scope">
                    {{ scope.row.remark | nullFormat }}
                </template>
            </el-table-column>
            <el-table-column label="更新时间" align="center" prop="updateTime" width="170">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.updateTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['cms:eduCategory:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['cms:eduCategory:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改宣教分类对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="90px">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入分类名称" />
                </el-form-item>
                <el-form-item label="显示排序" prop="sort">
                    <el-input-number v-model="form.sort" :min="0" :max="9999" controls-position="right" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">
                            {{ dict.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listEduCategory, getEduCategory, delEduCategory, addEduCategory, updateEduCategory } from "@/api/cms/eduCategory";

export default {
    name: "EduCategory",
    dicts: ['sys_normal_disable'],
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
            // 宣教分类表格数据
            categoryList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                name: null,
                status: null
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                name: [
                    { required: true, message: "分类名称不能为空", trigger: "blur" }
                ]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询宣教分类列表 */
        getList() {
            this.loading = true;
            listEduCategory(this.queryParams).then(response => {
                this.categoryList = response.rows;
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
                name: null,
                sort: 0,
                status: "0",
                remark: null
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
            this.title = "添加宣教分类";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getEduCategory(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改宣教分类";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateEduCategory(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addEduCategory(this.form).then(response => {
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
            this.$modal.confirm('确认删除所选宣教分类吗？').then(function () {
                return delEduCategory(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        }
    }
};
</script>
