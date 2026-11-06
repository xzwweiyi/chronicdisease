<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="缓存名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入缓存名称" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="IP地址" prop="host">
                <el-input v-model="queryParams.host" placeholder="请输入IP地址" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
            </el-form-item>
            <!-- <el-form-item label="创建时间">
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
                    range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
            </el-form-item> -->
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['system:config:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate" v-hasPermi="['system:config:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:config:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['system:config:export']">导出</el-button>
            </el-col> -->
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-refresh" @click="handleRefreshCache" v-hasPermi="['system:config:remove']">刷新缓存</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="configList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="缓存名称" align="center" prop="name" :show-overflow-tooltip="true" />
            <el-table-column label="IP地址" align="center" prop="host" :show-overflow-tooltip="true" />
            <el-table-column label="端口" align="center" prop="port" />
            <!-- <el-table-column label="密码" align="center" prop="password" :show-overflow-tooltip="true" /> -->
            <el-table-column label="状态" align="center" prop="status">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.status == '0' ? 'success' : 'warning'">
                        {{scope.row.status == '0' ? '正常' : '停用'}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)" v-hasPermi="['system:config:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)" v-hasPermi="['system:config:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改缓存配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="缓存名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入缓存名称" />
                </el-form-item>
                <el-form-item label="IP地址" prop="host">
                    <el-input v-model="form.host" placeholder="请输入IP地址" />
                </el-form-item>
                <el-form-item label="端口" prop="port">
                    <el-input v-model="form.port" placeholder="请输入端口" />
                </el-form-item>
                <div v-if="!form.id">
                    <el-form-item label="密码" prop="password">
                        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
                    </el-form-item>
                </div>

                <div v-else>
                    <el-form-item label="修改密码">
                        <el-radio-group v-model="flag">
                            <el-radio v-for="dict in radioPassword" :key="dict.value" :label="dict.value">{{dict.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <div v-if="flag == 0">
                        <el-form-item label="密码" prop="password">
                            <el-input v-model="form.password" type="password" placeholder="请输入密码" />
                        </el-form-item>
                    </div>
                    <div v-else>
                        <el-form-item label="原密码" prop="password">
                            <el-input v-model="form.password" type="password" placeholder="请输入原密码" />
                        </el-form-item>
                        <el-form-item label="新密码" prop="newPassword">
                            <el-input v-model="form.newPassword" type="password" placeholder="请输入新密码" />
                        </el-form-item>
                    </div>

                </div>

                <el-form-item label="状态">
                    <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in statusData" :key="dict.value" :label="dict.value">{{dict.label}}
                        </el-radio>
                    </el-radio-group>
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
    listRedis,
    addRedis,
    getRedis,
    updateRedis,
    delRedis
} from "@/api/redis/config";
import {
    refreshCache
} from '@/api/system/dict/type.js'

export default {
    name: "Config",
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
            // 缓存表格数据
            configList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 日期范围
            dateRange: [],
            // 查询缓存
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                name: undefined,
                host: undefined,
            },
            // 表单缓存
            form: {},
            statusData: [
                { label: '正常', value: 0 },
                { label: '停用', value: 1 },
            ],
            flag: 0,
            radioPassword: [
                { label: '否', value: 0 },
                { label: '是', value: 1 },
            ],
            // 表单校验
            rules: {
                name: [{ required: true, message: "缓存名称不能为空", trigger: "blur" }],
                host: [{ required: true, message: "IP地址不能为空", trigger: "blur" }],
                port: [{ required: true, message: "端口不能为空", trigger: "blur" }],
                password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
                newPassword: [{ required: true, message: "新密码不能为空", trigger: "blur" }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询缓存列表 */
        getList() {
            this.loading = true;
            listRedis(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                this.configList = response.rows;
                //   this.configList.forEach( (item) => {
                //     item.status = parseInt(item.status)
                //     console.log(item)
                //   });
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
                name: undefined,
                host: undefined,
                port: undefined,
                status: 0,
                password: undefined
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
            this.title = "添加缓存";
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getRedis(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改缓存";
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != undefined) {
                        if (this.form.password == this.form.newPassword) {
                            this.$modal.msgWarning("密码与新密码不能一致！");
                        }
                        updateRedis(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addRedis(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除缓存编号为"' + ids + '"的数据项？').then(function () {
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
        /** 刷新缓存按钮操作 */
        handleRefreshCache() {
            refreshCache().then(() => {
                this.$modal.msgSuccess("刷新成功");
            });
        }
    }
};
</script>