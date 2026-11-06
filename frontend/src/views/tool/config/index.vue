<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
            <el-form-item label="表中文名" prop="tableCnName">
                <el-input v-model="queryParams.tableCnName" placeholder="请输入表中文名" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="表英文名" prop="tableEnName">
                <el-input v-model="queryParams.tableEnName" placeholder="请输入表英文名" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="字段中文名" prop="fieldCn">
                <el-input v-model="queryParams.fieldCn" placeholder="请输入字段中文名" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="字段英文名" prop="fieldEn">
                <el-input v-model="queryParams.fieldEn" placeholder="请输入字段英文名" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:config:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:config:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:config:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:config:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="id" align="center" prop="id" />
            <el-table-column label="表中文名" align="center" prop="tableCnName" />
            <el-table-column label="表英文名" align="center" prop="tableEnName" />
            <el-table-column label="字段中文名" align="center" prop="fieldCn" />
            <el-table-column label="字段英文名" align="center" prop="fieldEn" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:config:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:config:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改数据检查配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="表中文名" prop="tableCnName">
                    <el-input v-model="form.tableCnName" placeholder="请输入表中文名" />
                </el-form-item>
                <el-form-item label="表英文名" prop="tableEnName">
                    <el-input v-model="form.tableEnName" placeholder="请输入表英文名" />
                </el-form-item>
                <el-form-item label="字段中文名" prop="fieldCn">
                    <el-input v-model="form.fieldCn" placeholder="请输入字段中文名" />
                </el-form-item>
                <el-form-item label="字段英文名" prop="fieldEn">
                    <el-input v-model="form.fieldEn" placeholder="请输入字段英文名" />
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
import { listConfig, getConfig, delConfig, addConfig, updateConfig } from "@/api/tool/config";

export default {
    name: "Config",
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
            // 数据检查配置表格数据
            configList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                tableCnName: null,
                tableEnName: null,
                fieldCn: null,
                fieldEn: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: { }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询数据检查配置列表 */
        getList() {
            this.loading = true;
            listConfig(this.queryParams).then(response => {
                this.configList = response.rows;
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
                tableCnName: null,
                tableEnName: null,
                fieldCn: null,
                fieldEn: null,
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
            this.title = "添加数据检查配置";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getConfig(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改数据检查配置";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateConfig(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addConfig(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除数据检查配置编号为"' + ids + '"的数据项？').then(function () {
                return delConfig(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/config/export', {
                ...this.queryParams
            }, `config_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>
