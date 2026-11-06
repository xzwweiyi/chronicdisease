<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"
            class="search_form">
            <el-form-item label="科室名称" prop="departName">
                <el-input v-model="queryParams.departName" placeholder="请输入科室名称" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:department:add']">新增</el-button>
            <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                v-hasPermi="['system:department:edit']">修改</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['system:department:remove']">删除</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['system:department:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="hospitalList" @selection-change="handleSelectionChange" @row-click="clickRow"
            ref="selectTable" class="select_table_style">
            <el-table-column type="selection" width="45" align="center" />
            <el-table-column label="科室ID" align="center" width="120" prop="id" />
            <el-table-column label="科室名称" align="center" min-width="260" prop="departName" />
            <el-table-column label="创建时间" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="修改时间" align="center" prop="updateTime">
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="120">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['system:department:edit']">编辑</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['system:department:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改中心管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="40%" :close-on-click-modal="false"
            :close-on-press-escape="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="医院ID" prop="hospitalId">
                    <el-input v-model="form.departName" placeholder="请输入科室名称" />
                </el-form-item>
                <el-form-item label="科室名称" prop="departName">
                    <el-input v-model="form.departName" placeholder="请输入科室名称" />
                </el-form-item>
                <!-- <el-form-item label="中心图片">
                    <image-upload v-model="form.hospitalImage" />
                </el-form-item> -->

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
    listDepartment, getDepart, addDepart, updateDepart, delDepart
} from "@/api/base/department";

export default {
    name: "Department",
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
            // 中心管理表格数据
            hospitalList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, departName: null, },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                hospitalId: [{ required: true, message: "医院id不能为空", trigger: "blur" }],
                departName: [{ required: true, message: "科室名称不能为空", trigger: "blur" }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询科室管理列表 */
        getList() {
            this.loading = true;
            listDepartment(this.queryParams).then(response => {
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
            this.form = {
                departName: null
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
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加科室管理";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getDepart(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改科室管理";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateDepart(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addDepart(this.form).then(response => {
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
            // console.log(row)
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除中心管理编号为"' + ids + '"的数据项？').then(function () {
                return delDepart(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/department/export', {
                ...this.queryParams
            }, `department_${new Date().getTime()}.xlsx`)
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