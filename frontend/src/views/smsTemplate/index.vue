<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="短信模版名称" prop="templateName">
                <el-input v-model="queryParams.templateName" placeholder="请输入短信模版名称" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
            <!-- <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                    v-hasPermi="['system:config:add']">新增</el-button>
            </el-col> -->
            <!-- <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['system:config:edit']">修改</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="configList">
            <el-table-column label="模版名称" align="center" prop="templateName" width="160" :show-overflow-tooltip="true" />
            <el-table-column label="模版内容" align="center" prop="content" min-width="200" :show-overflow-tooltip="true" />
            <el-table-column label="模版状态" align="center" prop="status" width="120">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.status == '0' ? 'warning' : 'success'">
                        {{scope.row.status == '0' ? '失效' : '生效'}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" width="190" prop="createTime">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="140" fixed="right">
                <template slot-scope="scope">
                    <el-button type="text" @click.stop="handleUpdate(scope.row)" v-hasPermi="['system:config:edit']">查看</el-button>
                    <!-- <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)" 
                            v-hasPermi="['system:config:remove']">删除</el-button> -->
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        <!-- 添加或修改短信模版配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" label-width="100px">
                <el-form-item label="模版名称" prop="templateName">
                    <!-- <el-input v-model="form.templateName" placeholder="请输入模版名称" /> -->
                    {{ form.templateName }}
                </el-form-item>
                <el-form-item label="模板内容" prop="content">
                    {{ form.content }}
                    <!-- <el-input v-model="form.content" placeholder="请输入模板内容" /> -->
                </el-form-item>
                <el-form-item label="模版状态" prop="status">
                    {{ form.status=='0'?"失效":'生效' }}
                    <!-- <el-radio-group v-model="form.status">
                        <el-radio label="0">失效</el-radio>
                        <el-radio label="1">生效</el-radio>
                    </el-radio-group> -->
                </el-form-item>
            </el-form>
            <!-- <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div> -->
        </el-dialog>
    </div>
</template>

<script>
import {
    listSms,
    addSms,
    editSms,
    getSms
} from "@/api/smsTemplate/sms";

export default {
    name: "SmsTemplate",
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
            // 短信模版表格数据
            configList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询短信模版
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                templateName: undefined,
                host: undefined,
            },
            // 表单短信模版
            form: {},
            // 表单校验
            rules: {
                templateName: [{ required: true, message: "短信模版名称不能为空", trigger: "blur" }],
                content: [{ required: true, message: "短信模版内容不能为空", trigger: "blur" }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询短信模版列表 */
        getList() {
            this.loading = true;
            listSms(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
                templateName: undefined,
                content: undefined,
                status: "1",
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
            this.dateRange = [];
            this.resetForm("queryForm");
            this.handleQuery();
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加短信模版";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getSms(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "查看短信模版";
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != undefined) {
                        editSms(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addSms(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除短信模版编号为"' + ids + '"的数据项？').then(function () {
                return delRedis(ids);
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
        },
        /** 刷新短信模版按钮操作 */
        handleRefreshCache() {
            refreshCache().then(() => {
                this.$modal.msgSuccess("刷新成功");
            });
        }
    }
};
</script>