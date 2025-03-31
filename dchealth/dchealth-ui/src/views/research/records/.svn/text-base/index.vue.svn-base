<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="患者ID" prop="patientId">
                <el-input v-model="queryParams.patientId" placeholder="请输入患者ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="研究项目ID" prop="researchId">
                <el-input v-model="queryParams.researchId" placeholder="请输入研究项目ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="${comment}" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入${comment}" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="租户ID" prop="tenantId">
                <el-input v-model="queryParams.tenantId" placeholder="请输入租户ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="第几次录入1=基线病例 2=治疗访视 3=AE  4=SAE 5=随访 6=提前终止 " prop="recordNum">
                <el-input v-model="queryParams.recordNum" placeholder="请输入第几次录入1=基线病例 2=治疗访视 3=AE  4=SAE 5=随访 6=提前终止 "
                    clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="第几次治疗访视随访或第随访" prop="stageNum">
                <el-input v-model="queryParams.stageNum" placeholder="请输入第几次治疗访视随访或第随访" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="创建人ID" prop="createAccountId">
                <el-input v-model="queryParams.createAccountId" placeholder="请输入创建人ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="后台录入人员" prop="entryPersonnel">
                <el-input v-model="queryParams.entryPersonnel" placeholder="请输入后台录入人员" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="组编号" prop="groupNo">
                <el-input v-model="queryParams.groupNo" placeholder="请输入组编号" clearable
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
                    v-hasPermi="['research:records:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['research:records:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['research:records:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['research:records:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="recordsList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="${comment}" align="center" prop="id" />
            <el-table-column label="患者ID" align="center" prop="patientId" />
            <el-table-column label="研究项目ID" align="center" prop="researchId" />
            <el-table-column label="${comment}" align="center" prop="name" />
            <el-table-column label="租户ID" align="center" prop="tenantId" />
            <el-table-column label="第几次录入1=基线病例 2=治疗访视 3=AE  4=SAE 5=随访 6=提前终止 " align="center" prop="recordNum" />
            <el-table-column label="第几次治疗访视随访或第随访" align="center" prop="stageNum" />
            <el-table-column label="创建人ID" align="center" prop="createAccountId" />
            <el-table-column label="档案进度" align="center" prop="progressRate" />
            <el-table-column label="后台录入人员" align="center" prop="entryPersonnel" />
            <el-table-column label="组编号" align="center" prop="groupNo" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['research:records:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['research:records:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改患者记录对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="患者ID" prop="patientId">
                    <el-input v-model="form.patientId" placeholder="请输入患者ID" />
                </el-form-item>
                <el-form-item label="研究项目ID" prop="researchId">
                    <el-input v-model="form.researchId" placeholder="请输入研究项目ID" />
                </el-form-item>
                <el-form-item label="${comment}" prop="name">
                    <el-input v-model="form.name" placeholder="请输入${comment}" />
                </el-form-item>
                <el-form-item label="租户ID" prop="tenantId">
                    <el-input v-model="form.tenantId" placeholder="请输入租户ID" />
                </el-form-item>
                <el-form-item label="第几次录入1=基线病例 2=治疗访视 3=AE  4=SAE 5=随访 6=提前终止 " prop="recordNum">
                    <el-input v-model="form.recordNum" placeholder="请输入第几次录入1=基线病例 2=治疗访视 3=AE  4=SAE 5=随访 6=提前终止 " />
                </el-form-item>
                <el-form-item label="第几次治疗访视随访或第随访" prop="stageNum">
                    <el-input v-model="form.stageNum" placeholder="请输入第几次治疗访视随访或第随访" />
                </el-form-item>
                <el-form-item label="创建人ID" prop="createAccountId">
                    <el-input v-model="form.createAccountId" placeholder="请输入创建人ID" />
                </el-form-item>
                <el-form-item label="档案进度" prop="progressRate">
                    <el-input v-model="form.progressRate" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="后台录入人员" prop="entryPersonnel">
                    <el-input v-model="form.entryPersonnel" placeholder="请输入后台录入人员" />
                </el-form-item>
                <el-form-item label="组编号" prop="groupNo">
                    <el-input v-model="form.groupNo" placeholder="请输入组编号" />
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
    listRecords,
    getRecords,
    delRecords,
    addRecords,
    updateRecords
} from "@/api/research/records";

export default {
    name: "Records",
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
            // 患者记录表格数据
            recordsList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                patientId: null,
                researchId: null,
                name: null,
                tenantId: null,
                recordNum: null,
                stageNum: null,
                createAccountId: null,
                progressRate: null,
                entryPersonnel: null,
                groupNo: null
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
        /** 查询患者记录列表 */
        getList() {
            this.loading = true;
            listRecords(this.queryParams).then(response => {
                this.recordsList = response.rows;
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
                patientId: null,
                researchId: null,
                name: null,
                tenantId: null,
                recordNum: null,
                stageNum: null,
                createAccountId: null,
                progressRate: null,
                entryPersonnel: null,
                createTime: null,
                createBy: null,
                updateTime: null,
                updateBy: null,
                groupNo: null
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
            this.title = "添加患者记录";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getRecords(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改患者记录";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateRecords(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addRecords(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除患者记录编号为"' + ids + '"的数据项？').then(function () {
                return delRecords(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('research/records/export', {
                ...this.queryParams
            }, `records_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>