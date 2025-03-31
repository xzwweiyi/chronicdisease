<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="医生ID" prop="doctorId">
                <el-input v-model="queryParams.doctorId" placeholder="请输入医生ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="研究ID" prop="researchId">
                <el-input v-model="queryParams.researchId" placeholder="请输入研究ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="${comment}" prop="hospitalId">
                <el-input v-model="queryParams.hospitalId" placeholder="请输入${comment}" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="排序 数字越大越靠后" prop="orderNum">
                <el-input v-model="queryParams.orderNum" placeholder="请输入排序 数字越大越靠后" clearable
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
                    v-hasPermi="['research:doctor:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['research:doctor:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['research:doctor:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['research:doctor:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="doctorList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="主键" align="center" prop="id" />
            <el-table-column label="医生ID" align="center" prop="doctorId" />
            <el-table-column label="研究ID" align="center" prop="researchId" />
            <el-table-column label="${comment}" align="center" prop="hospitalId" />
            <el-table-column label="排序 数字越大越靠后" align="center" prop="orderNum" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['research:doctor:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['research:doctor:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改项目专家对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="医生ID" prop="doctorId">
                    <el-input v-model="form.doctorId" placeholder="请输入医生ID" />
                </el-form-item>
                <el-form-item label="研究ID" prop="researchId">
                    <el-input v-model="form.researchId" placeholder="请输入研究ID" />
                </el-form-item>
                <el-form-item label="${comment}" prop="hospitalId">
                    <el-input v-model="form.hospitalId" placeholder="请输入${comment}" />
                </el-form-item>
                <el-form-item label="排序 数字越大越靠后" prop="orderNum">
                    <el-input v-model="form.orderNum" placeholder="请输入排序 数字越大越靠后" />
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
    listDoctor,
    getDoctor,
    delDoctor,
    addDoctor,
    updateDoctor
} from "@/api/research/doctor";

export default {
    name: "Doctor",
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
            // 项目专家表格数据
            doctorList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                doctorId: null,
                researchId: null,
                hospitalId: null,
                orderNum: null,
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
        /** 查询项目专家列表 */
        getList() {
            this.loading = true;
            listDoctor(this.queryParams).then(response => {
                this.doctorList = response.rows;
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
                doctorId: null,
                researchId: null,
                hospitalId: null,
                orderNum: null,
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
            this.title = "添加项目专家";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getDoctor(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改项目专家";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateDoctor(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addDoctor(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除项目专家编号为"' + ids + '"的数据项？').then(function () {
                return delDoctor(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('research/doctor/export', {
                ...this.queryParams
            }, `doctor_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>