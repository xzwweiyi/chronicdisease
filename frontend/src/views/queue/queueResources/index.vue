<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="参数名称" prop="configName">
                <el-input v-model="queryParams.configName" placeholder="请输入参数名称" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="参数键名" prop="configKey">
                <el-input v-model="queryParams.configKey" placeholder="请输入参数键名" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="系统内置" prop="configType">
                <el-select v-model="queryParams.configType" placeholder="系统内置" clearable>
                    <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
                    range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:config:add']">新增</el-button>
            <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                v-hasPermi="['system:config:edit']">修改</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['system:config:remove']">删除</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['system:config:export']">导出</el-button> -->
            <el-button type="danger" plain icon="el-icon-refresh" @click="handleRefreshCache"
                v-hasPermi="['system:config:remove']">刷新缓存</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
            <el-button-group class="card-toggle-table">
                <el-tooltip v-if="cardType" class="item" effect="dark" content="切换成表格" placement="top-start">
                    <el-button plain icon="el-icon-s-grid" @click="toggle" />
                </el-tooltip>
                <el-tooltip v-else class="item" effect="dark" content="切换成卡片" placement="top-start">
                    <el-button plain icon="el-icon-bank-card" @click="toggle" />
                </el-tooltip>
            </el-button-group>
        </el-row>

        <!-- 卡片风格 -->
        <el-row v-if="cardType">
            <el-col v-for="item in configList" :key="item.id" :span="6" style="margin-bottom: 10px">
                <el-card shadow="always" style="margin:0 5px;">
                    <div slot="header" class="clearfix">
                        <i class="el-icon-user" />
                        <span style="margin-left: 5px">{{item.configName}}</span>
                        <div style="display: inline-block; float: right; cursor: pointer" @click="doEdit(item.id)">
                            <el-tag :type="item.configType == 'Y' ? 'success' : 'warning'">
                                {{item.configType == 'Y' ? '是' : '否'}}</el-tag>
                        </div>
                    </div>
                    <div>
                        <ul class="role-info">
                            <li>
                                <div class="role-left">描述信息：{{ item.remark }}</div>
                            </li>
                            <li>
                                <div class="role-left">
                                    创建时间：{{ item.createTime | dateFormat }}
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div style="text-align:center">
                        <el-col :span="6">
                            <el-tooltip effect="dark" content="角色管理" placement="bottom">
                                <el-button type="text" @click="handleRole"><i class="el-icon-menu" /></el-button>
                            </el-tooltip>
                        </el-col>
                        <el-col :span="6">
                            <el-tooltip effect="dark" content="设置" placement="bottom">
                                <el-button type="text"><i class="el-icon-setting" /></el-button>
                            </el-tooltip>
                        </el-col>
                        <el-col :span="6">
                            <el-tooltip effect="dark" content="编辑" placement="bottom">
                                <el-button type="text" @click="handleUpdate(item)"><i class="el-icon-edit-outline" />
                                </el-button>

                            </el-tooltip>
                        </el-col>
                        <el-col :span="6">
                            <el-tooltip effect="dark" content="删除" placement="bottom">
                                <el-button type="text" @click="handleDelete(item)"><i class="el-icon-delete" />
                                </el-button>
                            </el-tooltip>
                        </el-col>
                    </div>

                </el-card>
            </el-col>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-else v-loading="loading" :data="configList" @row-click="clickRow" ref="selectTable"
            class="select_table_style" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="参数主键" align="center" prop="configId" />
            <el-table-column label="参数名称" align="center" prop="configName" :show-overflow-tooltip="true" />
            <el-table-column label="参数键名" align="center" prop="configKey" :show-overflow-tooltip="true" />
            <el-table-column label="参数键值" align="center" prop="configValue" />
            <el-table-column label="系统内置" align="center" prop="configType">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.configType" />
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['system:config:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['system:config:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改参数配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="参数名称" prop="configName">
                    <el-input v-model="form.configName" placeholder="请输入参数名称" />
                </el-form-item>
                <el-form-item label="参数键名" prop="configKey">
                    <el-input v-model="form.configKey" placeholder="请输入参数键名" />
                </el-form-item>
                <el-form-item label="参数键值" prop="configValue">
                    <el-input v-model="form.configValue" placeholder="请输入参数键值" />
                </el-form-item>
                <el-form-item label="系统内置" prop="configType">
                    <el-radio-group v-model="form.configType">
                        <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">
                            {{dict.label}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item>
                    <el-cascader v-model="form.value" :options="options" @change="handleChange"></el-cascader>
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
    listConfig,
    getConfig,
    delConfig,
    addConfig,
    updateConfig
} from "@/api/system/config";
import {
    refreshCache
} from '@/api/system/dict/type.js'

export default {
    name: "Config",
    dicts: ['sys_yes_no'],
    data() {
        return {
            // 默认为卡片风格，为false时切换成列表风格
            cardType: true,
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
            // 参数表格数据
            configList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 日期范围
            dateRange: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                configName: undefined,
                configKey: undefined,
                configType: undefined
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                configName: [{
                    required: true,
                    message: "参数名称不能为空",
                    trigger: "blur"
                }],
                configKey: [{
                    required: true,
                    message: "参数键名不能为空",
                    trigger: "blur"
                }],
                configValue: [{
                    required: true,
                    message: "参数键值不能为空",
                    trigger: "blur"
                }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        handleRole() {
            this.$router.push({
                path: `/queue/role`
            });
        },
        toggle() {
            this.cardType = !this.cardType
        },
        /** 查询参数列表 */
        getList() {
            this.loading = true;
            listConfig(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
                configId: undefined,
                configName: undefined,
                configKey: undefined,
                configValue: undefined,
                configType: "Y",
                remark: undefined
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
            this.title = "添加参数";
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.configId)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const configId = row.configId || this.ids
            getConfig(configId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改参数";
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.configId != undefined) {
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
            const configIds = row.configId || this.ids;
            this.$modal.confirm('是否确认删除参数编号为"' + configIds + '"的数据项？').then(function () {
                return delConfig(configIds);
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