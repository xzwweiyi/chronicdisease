<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="队列ID" prop="researchId">
                <el-input v-model="queryParams.researchId" placeholder="请输入队列ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="中心ID" prop="hospitalId">
                <el-input v-model="queryParams.hospitalId" placeholder="请输入中心ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="计划数" prop="planNum">
                <el-input v-model="queryParams.planNum" placeholder="请输入计划数" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="实际患者数" prop="patientNum">
                <el-input v-model="queryParams.patientNum" placeholder="请输入实际患者数" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="${comment}" prop="yearStat">
                <el-input v-model="queryParams.yearStat" placeholder="请输入${comment}" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="年月" prop="monthStat">
                <el-input v-model="queryParams.monthStat" placeholder="请输入年月" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="${comment}" prop="researchPatientStatId">
                <el-input v-model="queryParams.researchPatientStatId" placeholder="请输入${comment}" clearable
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
                    v-hasPermi="['research:log:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['research:log:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['research:log:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['research:log:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="logList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="${comment}" align="center" prop="id" />
            <el-table-column label="队列ID" align="center" prop="researchId" />
            <el-table-column label="中心ID" align="center" prop="hospitalId" />
            <el-table-column label="计划数" align="center" prop="planNum" />
            <el-table-column label="实际患者数" align="center" prop="patientNum" />
            <el-table-column label="${comment}" align="center" prop="yearStat" />
            <el-table-column label="年月" align="center" prop="monthStat" />
            <el-table-column label="${comment}" align="center" prop="researchPatientStatId" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['research:log:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['research:log:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改计划入组日志对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="队列ID" prop="researchId">
                    <el-input v-model="form.researchId" placeholder="请输入队列ID" />
                </el-form-item>
                <el-form-item label="中心ID" prop="hospitalId">
                    <el-input v-model="form.hospitalId" placeholder="请输入中心ID" />
                </el-form-item>
                <el-form-item label="计划数" prop="planNum">
                    <el-input v-model="form.planNum" placeholder="请输入计划数" />
                </el-form-item>
                <el-form-item label="实际患者数" prop="patientNum">
                    <el-input v-model="form.patientNum" placeholder="请输入实际患者数" />
                </el-form-item>
                <el-form-item label="${comment}" prop="yearStat">
                    <el-input v-model="form.yearStat" placeholder="请输入${comment}" />
                </el-form-item>
                <el-form-item label="年月" prop="monthStat">
                    <el-input v-model="form.monthStat" placeholder="请输入年月" />
                </el-form-item>
                <el-form-item label="${comment}" prop="researchPatientStatId">
                    <el-input v-model="form.researchPatientStatId" placeholder="请输入${comment}" />
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
    listLog,
    getLog,
    delLog,
    addLog,
    updateLog
} from "@/api/research/log";

export default {
    name: "Log",
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
            // 计划入组日志表格数据
            logList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                researchId: null,
                hospitalId: null,
                planNum: null,
                patientNum: null,
                yearStat: null,
                monthStat: null,
                researchPatientStatId: null
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
        /** 查询计划入组日志列表 */
        getList() {
            this.loading = true;
            listLog(this.queryParams).then(response => {
                this.logList = response.rows;
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
                hospitalId: null,
                planNum: null,
                patientNum: null,
                yearStat: null,
                monthStat: null,
                createTime: null,
                updateTime: null,
                createBy: null,
                updateBy: null,
                researchPatientStatId: null
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
            this.title = "添加计划入组日志";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getLog(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改计划入组日志";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateLog(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addLog(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除计划入组日志编号为"' + ids + '"的数据项？').then(function () {
                return delLog(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('research/log/export', {
                ...this.queryParams
            }, `log_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>