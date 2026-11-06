<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"
            class="search_form">
            <el-form-item label="系统模块" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入系统模块" clearable style="width: 180px;"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="操作人员" prop="operName">
                <el-input v-model="queryParams.operName" placeholder="请输入操作人员" clearable style="width: 180px;"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="类型" prop="businessType">
                <el-select v-model="queryParams.businessType" placeholder="操作类型" clearable style="width: 100px">
                    <el-option v-for="dict in dict.type.sys_oper_type" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="操作状态" clearable style="width: 100px">
                    <el-option v-for="dict in dict.type.sys_common_status" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="操作时间">
                <el-date-picker v-model="dateRange" style="width: 230px" value-format="yyyy-MM-dd" type="daterange"
                    range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['monitor:operlog:remove']">删除</el-button>
            <el-button type="danger" plain icon="el-icon-delete" @click="handleClean"
                v-hasPermi="['monitor:operlog:remove']">清空</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['monitor:operlog:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table ref="tables" v-loading="loading" :data="list" @row-click="clickRow"
            @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="日志编号" align="center" prop="operId" width="100" />
            <el-table-column label="系统模块" align="center" prop="title" min-width="130" />
            <el-table-column label="操作类型" align="center" prop="businessType" width="80">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.sys_oper_type" :value="scope.row.businessType" />
                </template>
            </el-table-column>
            <el-table-column label="请求方式" align="center" prop="requestMethod" width="80" />
            <el-table-column label="操作人员" align="center" prop="operName" width="120" :show-overflow-tooltip="true"
                sortable="custom" :sort-orders="['descending', 'ascending']" />
            <el-table-column label="操作地址" align="center" prop="operIp" width="140" :show-overflow-tooltip="true" />
            <el-table-column label="操作地点" align="center" prop="operLocation" min-width="120"
                :show-overflow-tooltip="true" />
            <el-table-column label="操作状态" align="center" prop="status" width="90">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status" />
                </template>
            </el-table-column>
            <el-table-column label="操作日期" align="center" prop="operTime" sortable="custom"
                :sort-orders="['descending', 'ascending']" width="150">
                <template slot-scope="scope">
                    <span>{{ scope.row.operTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="100">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-view" @click.stop="handleView(scope.row,scope.index)"
                        v-hasPermi="['monitor:operlog:query']">详细</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 操作日志详细 -->
        <el-dialog title="操作日志详细" :visible.sync="open" width="70%" top="8vh">
            <el-scrollbar class="dialog_scrollbar">
                <el-descriptions :column="2" border label-class-name="operlog_label"
                    content-class-name="operlog_content">
                    <el-descriptions-item label="操作模块：">{{ form.title }} / {{ typeFormat(form) }}</el-descriptions-item>
                    <el-descriptions-item label="请求地址：">{{ form.operUrl | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="登录信息：">
                        {{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}</el-descriptions-item>
                    <el-descriptions-item label="请求方式：">{{ form.requestMethod | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="操作方法：" :span="2">{{ form.method | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="请求参数："
                        :span="2">{{ form.operParam | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="返回参数："
                        :span="2">{{ form.jsonResult | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="操作状态：">
                        {{ form.status === 0?'正常':(form.status === 1?'失败':'--') }}
                    </el-descriptions-item>
                    <el-descriptions-item label="操作时间：">{{ form.operTime | dateFormat }}</el-descriptions-item>
                    <el-descriptions-item label="异常信息：" v-if="form.status === 1">
                        {{ form.errorMsg | nullFormat}}</el-descriptions-item>
                </el-descriptions>
            </el-scrollbar>
            <div slot="footer" class="dialog-footer">
                <el-button @click="open = false">关 闭</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { list, delOperlog, cleanOperlog } from "@/api/monitor/operlog";

export default {
    name: "Operlog",
    dicts: ['sys_oper_type', 'sys_common_status'],
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 表格数据
            list: [],
            // 是否显示弹出层
            open: false,
            // 日期范围
            dateRange: [],
            // 默认排序
            defaultSort: {
                prop: 'operTime',
                order: 'descending'
            },
            // 表单参数
            form: {},
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                title: undefined,
                operName: undefined,
                businessType: undefined,
                status: undefined
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询登录日志 */
        getList() {
            this.loading = true;
            list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                this.list = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 操作日志类型字典翻译
        typeFormat(row, column) {
            return this.selectDictLabel(this.dict.type.sys_oper_type, row.businessType);
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
            this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
            this.handleQuery();
        },
        clickRow(row) {
            this.$refs.tables.toggleRowSelection(row);
        },
        /** 多选框选中数据 */
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.operId)
            this.multiple = !selection.length
        },
        /** 排序触发事件 */
        handleSortChange(column, prop, order) {
            this.queryParams.orderByColumn = column.prop;
            this.queryParams.isAsc = column.order;
            this.getList();
        },
        /** 详细按钮操作 */
        handleView(row) {
            this.open = true;
            this.form = row;
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const operIds = row.operId || this.ids;
            this.$modal.confirm('是否确认删除日志编号为"' + operIds + '"的数据项？').then(function () {
                return delOperlog(operIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 清空按钮操作 */
        handleClean() {
            this.$modal.confirm('是否确认清空所有操作日志数据项？').then(function () {
                return cleanOperlog();
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("清空成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('monitor/operlog/export', {
                ...this.queryParams
            }, `operlog_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>
<style scoped lang="scss">
.dialog_scrollbar {
    height: 65vh;
    ::v-deep .el-descriptions {
        .operlog_label {
            color: #333;
            width: 120px;
        }
    }
}
</style>