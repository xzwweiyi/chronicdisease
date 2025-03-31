<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="研究ID" prop="researchId">
                <el-input v-model="queryParams.researchId" placeholder="请输入研究ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="医院ID" prop="hospitalId">
                <el-input v-model="queryParams.hospitalId" placeholder="请输入医院ID" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="计划数" prop="targetNum">
                <el-input v-model="queryParams.targetNum" placeholder="请输入计划数" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="基线病例" prop="baseNum">
                <el-input v-model="queryParams.baseNum" placeholder="请输入基线病例" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="治疗访视" prop="cureVisitNum">
                <el-input v-model="queryParams.cureVisitNum" placeholder="请输入治疗访视" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="随访病例" prop="followNum">
                <el-input v-model="queryParams.followNum" placeholder="请输入随访病例" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="AE病例" prop="aeNum">
                <el-input v-model="queryParams.aeNum" placeholder="请输入AE病例" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="SAE病例" prop="saeNum">
                <el-input v-model="queryParams.saeNum" placeholder="请输入SAE病例" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="提前终止" prop="fallOffNum">
                <el-input v-model="queryParams.fallOffNum" placeholder="请输入提前终止" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="启动时间" prop="startTime">
                <el-date-picker clearable v-model="queryParams.startTime" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择启动时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="研究者id" prop="researcherId">
                <el-input v-model="queryParams.researcherId" placeholder="请输入研究者id" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="CRCid" prop="crcId">
                <el-input v-model="queryParams.crcId" placeholder="请输入CRCid" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="CRAid" prop="craId">
                <el-input v-model="queryParams.craId" placeholder="请输入CRAid" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="${comment}" prop="craName">
                <el-input v-model="queryParams.craName" placeholder="请输入${comment}" clearable
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
                    v-hasPermi="['research:stat:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['research:stat:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['research:stat:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['research:stat:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="statList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="${comment}" align="center" prop="id" />
            <el-table-column label="研究ID" align="center" prop="researchId" />
            <el-table-column label="医院ID" align="center" prop="hospitalId" />
            <el-table-column label="计划数" align="center" prop="targetNum" />
            <el-table-column label="基线病例" align="center" prop="baseNum" />
            <el-table-column label="治疗访视" align="center" prop="cureVisitNum" />
            <el-table-column label="随访病例" align="center" prop="followNum" />
            <el-table-column label="AE病例" align="center" prop="aeNum" />
            <el-table-column label="SAE病例" align="center" prop="saeNum" />
            <el-table-column label="提前终止" align="center" prop="fallOffNum" />
            <el-table-column label="启动时间" align="center" prop="startTime" width="180">
                <template slot-scope="scope">
                    <span>{{ scope.row.startTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="研究者id" align="center" prop="researcherId" />
            <el-table-column label="CRCid" align="center" prop="crcId" />
            <el-table-column label="CRAid" align="center" prop="craId" />
            <el-table-column label="${comment}" align="center" prop="craName" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['research:stat:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['research:stat:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改中心统计对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="研究ID" prop="researchId">
                    <el-input v-model="form.researchId" placeholder="请输入研究ID" />
                </el-form-item>
                <el-form-item label="医院ID" prop="hospitalId">
                    <el-input v-model="form.hospitalId" placeholder="请输入医院ID" />
                </el-form-item>
                <el-form-item label="计划数" prop="targetNum">
                    <el-input v-model="form.targetNum" placeholder="请输入计划数" />
                </el-form-item>
                <el-form-item label="基线病例" prop="baseNum">
                    <el-input v-model="form.baseNum" placeholder="请输入基线病例" />
                </el-form-item>
                <el-form-item label="治疗访视" prop="cureVisitNum">
                    <el-input v-model="form.cureVisitNum" placeholder="请输入治疗访视" />
                </el-form-item>
                <el-form-item label="随访病例" prop="followNum">
                    <el-input v-model="form.followNum" placeholder="请输入随访病例" />
                </el-form-item>
                <el-form-item label="AE病例" prop="aeNum">
                    <el-input v-model="form.aeNum" placeholder="请输入AE病例" />
                </el-form-item>
                <el-form-item label="SAE病例" prop="saeNum">
                    <el-input v-model="form.saeNum" placeholder="请输入SAE病例" />
                </el-form-item>
                <el-form-item label="提前终止" prop="fallOffNum">
                    <el-input v-model="form.fallOffNum" placeholder="请输入提前终止" />
                </el-form-item>
                <el-form-item label="启动时间" prop="startTime">
                    <el-date-picker clearable v-model="form.startTime" type="date" value-format="yyyy-MM-dd"
                        placeholder="请选择启动时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="研究者id" prop="researcherId">
                    <el-input v-model="form.researcherId" placeholder="请输入研究者id" />
                </el-form-item>
                <el-form-item label="CRCid" prop="crcId">
                    <el-input v-model="form.crcId" placeholder="请输入CRCid" />
                </el-form-item>
                <el-form-item label="CRAid" prop="craId">
                    <el-input v-model="form.craId" placeholder="请输入CRAid" />
                </el-form-item>
                <el-form-item label="${comment}" prop="craName">
                    <el-input v-model="form.craName" placeholder="请输入${comment}" />
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
    listStat,
    getStat,
    delStat,
    addStat,
    updateStat
} from "@/api/research/stat";

export default {
    name: "Stat",
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
            // 中心统计表格数据
            statList: [],
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
                targetNum: null,
                baseNum: null,
                cureVisitNum: null,
                followNum: null,
                aeNum: null,
                saeNum: null,
                fallOffNum: null,
                startTime: null,
                researcherId: null,
                crcId: null,
                craId: null,
                craName: null
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
        /** 查询中心统计列表 */
        getList() {
            this.loading = true;
            listStat(this.queryParams).then(response => {
                this.statList = response.rows;
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
                targetNum: null,
                baseNum: null,
                cureVisitNum: null,
                followNum: null,
                aeNum: null,
                saeNum: null,
                fallOffNum: null,
                createTime: null,
                updateTime: null,
                startTime: null,
                researcherId: null,
                crcId: null,
                craId: null,
                craName: null
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
            this.title = "添加中心统计";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getStat(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改中心统计";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateStat(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addStat(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除中心统计编号为"' + ids + '"的数据项？').then(function () {
                return delStat(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('research/stat/export', {
                ...this.queryParams
            }, `stat_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>