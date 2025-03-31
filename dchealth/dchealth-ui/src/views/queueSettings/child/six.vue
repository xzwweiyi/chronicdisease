<template>
    <div>
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="患者姓名：" prop="patientName">
                <el-input v-model="queryParams.patientName" placeholder="请输入患者姓名" clearable class="query_params_input" />
            </el-form-item>
            <el-form-item label="绑定状态：" prop="bindStatus">
                <el-select v-model="queryParams.bindStatus" class="query_params_input">
                    <el-option value="0" label="未绑定"></el-option>
                    <el-option value="1" label="已绑定"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="入组状态：" prop="enrollmentHandlerStatus">
                <el-select v-model="queryParams.enrollmentHandlerStatus" class="query_params_input">
                    <el-option value="1" label="未入组"></el-option>
                    <el-option value="3" label="已入组"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="success" plain @click="handleImport" v-hasPermi="['medical:enrollmentCondition:select']">纳排入组</el-button>
            <el-button type="success" plain @click="handleAdd" v-hasPermi="['system:info:add']">
                快速导入</el-button>
            <el-button type="danger" plain @click="deletePatient(false)" :disabled="!patientIds.length" v-hasPermi="['system:config:export']">删除</el-button>
            <!-- <el-button type="warning" plain @click="handleExport"
                v-hasPermi="['system:config:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="enrollmentList" :height="tableHeight" ref="patientTable" @row-click="clickEnrollRow" @selection-change="patientSelectionChange" :row-class-name="setRowClassName">
            <el-table-column type="selection" width="55" align="center">
                <template slot-scope="scope" v-if="scope.row.enrollmentHandlerStatus!=3">
                    <el-checkbox v-model="scope.row.checked" @change="toggleSelection(scope.row)"></el-checkbox>
                </template>
            </el-table-column>
            <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="患者性别" align="center" prop="sex" width="100">
                <template slot-scope="scope">
                    {{ scope.row.sex | convertAgentStr }}
                </template>
            </el-table-column>
            <el-table-column label="身份证号" align="center" prop="idCard" min-width="160" />
            <el-table-column label="出生日期" align="center" prop="birthday" width="120" />
            <el-table-column label="患者手机号" align="center" prop="baseMobile" width="120" />
            <el-table-column label="患者id" align="center" prop="personId" width="120" />
            <el-table-column label="是否绑定" align="center" min-width="120" key="accountId" prop="accountId">
                <template slot-scope="scope">
                    {{scope.row.accountId>0?'绑定':'未绑定'}}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" min-width="170">
                <template slot-scope="soped">{{ soped.row.createTime|dateFormat }}</template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="200">
                <template slot-scope="scope">
                    <el-button type="text" @click.stop="handleSee(scope.row)">查看详情</el-button>
                    <el-button type="text" class="el-button-text_delete" v-if="scope.row.enrollmentHandlerStatus!=3" v-hasPermi="['system:info:delete']" @click.stop="deletePatient(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        <div slot="footer" class="dialog-footer" style="margin:20px auto 0;text-align: center;">
            <el-button type="primary" @click="handleSubmit('save')">保 存</el-button>
            <el-button type="primary" @click="handleSubmit('next')">保存并进入下一步</el-button>
        </div>
        <el-dialog :title="title" :visible.sync="addOpen" width="80%" top="8vh" @close="cancelAll" :modal-append-to-body="false">
            <el-form :model="allParams" ref="allForm" :inline="true" label-width="90px">
                <el-form-item label="患者姓名:">
                    <el-input v-model="allParams.patientName" placeholder="请输入患者姓名" clearable style="width: 160px" />
                </el-form-item>
                <el-form-item label="手机号:">
                    <el-input v-model="allParams.mobile" placeholder="请输入患者手机号" clearable style="width: 160px" />
                </el-form-item>
                <el-form-item label="身份证号:">
                    <el-input v-model="allParams.idCard" placeholder="请输入身份证号" clearable style="width: 160px" />
                </el-form-item>
                <el-form-item label="就诊卡号:">
                    <el-input v-model="allParams.tdPatientId" placeholder="请输入就诊卡号" clearable style="width: 160px" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="handleQueryAll">搜索</el-button>
                    <el-button icon="el-icon-refresh" @click="resetQueryAll">重置</el-button>
                </el-form-item>
            </el-form>
            <el-table v-loading="loading" :data="allList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange" height="calc(80vh - 280px)">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true" />
                <el-table-column label="性别" align="center" prop="sex" width="80">
                    <template slot-scope="scope">
                        {{ scope.row.sex == "1" ? "男" : "女" }}
                    </template>
                </el-table-column>
                <el-table-column label="手机号" align="center" prop="mobile" width="150" />
                <el-table-column label="身份证号" align="center" prop="idCard" width="240" />
                <el-table-column label="出生日期" align="center" prop="birthday" width="120" />
                <el-table-column label="创建时间" align="center" prop="createTime" width="200">
                    <template slot-scope="soped">{{ soped.row.createTime|dateFormat }}</template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="140" fixed="right">
                    <template slot-scope="soped">
                        <el-button type="text" @click.stop="toHealthrecordDetails(soped.row)">查看患者详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="allListTotal > 0" :total="allListTotal" :page.sync="allParams.pageNum" :limit.sync="allParams.pageSize" @pagination="getAll()" />
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitAll">确 定</el-button>
                <el-button @click="cancelAll">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listEnrollment,
    listNoEnrollment,
    subNoEnrollment,
    removeEnrollment
} from "@/api/info/config";

export default {
    name: "stepSix",
    props: {
        researchId: { type: String, default: null, },
        deptId: { type: Number, default: null, },
    },
    data() {
        return {
            // 遮罩层
            loading: true,
            // 总条数
            total: 0,
            showSearch: true,
            // 查询缓存
            queryParams: {
                pageNum: 1, pageSize: 10, patientName: undefined, researchId: undefined,
                bindStatus: undefined, enrollmentHandlerStatus: undefined,
            },
            // 未入组患者查询
            allParams: {
                mobile: undefined, patientName: undefined, researchId: undefined,
                idCard: undefined, tdPatientId: undefined, pageSize: 10, pageNum: 1,
            },
            title: "",
            enrollmentList: [],
            addOpen: false, //快速导入弹窗
            allList: [],
            arrList: [],
            tableHeight: 'calc(80vh - 260px)',
            allListTotal: 0,
            patientIds: []
        };
    },
    created() { },
    watch: {
        researchId: {
            immediate: true, // 监听到后，立即执行 handler方法
            handler(newVal, oldValue) {
                if (newVal && newVal != null) {
                    this.getList();
                }
            },
        },
    },
    methods: {
        /** 查询缓存列表 */
        getList() {
            this.loading = false;
            this.queryParams.researchId = this.researchId;
            this.enrollmentList = [];
            listEnrollment(this.queryParams).then((response) => {
                let responseData = response.rows
                if (responseData && responseData.length) {
                    this.enrollmentList = responseData.map(m => {
                        return { ...m, checked: false }
                    });
                }
                this.total = response.total;
            });
            this.loading = false;
        },
        setRowClassName(event) {
            if (event.row.enrollmentHandlerStatus != 3) {
                return 'select_table_row'
            } else {
                return ''
            }
        },
        clickEnrollRow(row) {
            if (row.enrollmentHandlerStatus != 3) {
                this.$refs.patientTable.toggleRowSelection(row);
            }
        },
        patientSelectionChange(event) {
            let ids = []
            if (event.length) {
                ids = event.filter(f => f.enrollmentHandlerStatus != '3').map(m => m.patientId)
                this.enrollmentList.forEach(item => {
                    if (item.enrollmentHandlerStatus != '3' && event.findIndex(f => f.patientId == item.patientId) > -1) {
                        item.checked = true
                    } else {
                        item.checked = false
                    }
                })
            } else {
                this.enrollmentList.forEach(item => {
                    item.checked = false
                })
            }
            this.patientIds = ids.toString()
        },
        toggleSelection(rows) {
            if (rows) {
                this.$refs.patientTable.toggleRowSelection(rows);
            } else {
                this.$refs.patientTable.clearSelection();
            }
        },
        toHealthrecordDetails(item) {
            this.$router.push({
                path: "/workstation/patients/opinionDetail",
                query: {
                    patientId: item.patientId,
                    personId: item.personId,
                    hideQueueTree: true,
                }
            });
        },
        handleSee(item) {
            let queryData = this.$route.query;
            let oldPath = `${this.$route.path}?id=${queryData.id}&stage=${queryData.stage}`
            this.$router.push({
                path: "/queueSettings/allpatient/details",
                query: {
                    patientId: item.patientId,
                    researchId: this.researchId,
                    oldPath: encodeURIComponent(oldPath)
                },
            });
        },
        handleImport() {
            let queryData = this.$route.query;
            let oldPath = `${this.$route.path}?id=${queryData.id}&stage=${queryData.stage}`
            this.$router.push({
                path: '/queueSettings/patient/import',
                query: {
                    researchId: this.researchId,
                    oldPath: encodeURIComponent(oldPath)
                },
            })
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
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
        /** 导出按钮操作 */
        handleExport() {
            this.download(
                "/patient/manage/exportData", {
                ...this.queryParams,
            },
                `config_${new Date().getTime()}.xlsx`
            );
        },
        handleAdd() {
            this.title = "快速导入患者";
            this.addOpen = true;
            this.allList = []
            this.allListTotal = 0
            // this.getAll();
        },
        /**
         *获取全部患者数据
         */
        getAll() {
            if (!this.allParams.patientName && !this.allParams.mobile &&
                !this.allParams.idCard && !this.allParams.tdPatientId) {
                this.$message.warning('请输入查询条件')
                return
            }
            this.loading = true;
            this.allParams.researchId = this.researchId,
                listNoEnrollment(this.allParams).then((response) => {
                    this.allList = response.rows;
                    this.allListTotal = response.total
                    this.loading = false;
                });
        },
        handleQueryAll() {
            this.allParams.pageNum = 1;
            this.getAll()
        },
        // 表单重置
        resetAll() {
            this.allParams = {
                mobile: undefined,
                patientName: undefined,
                idCard: undefined,
                tdPatientId: undefined,
                pageSize: 10,
                pageNum: 1,
            };
            this.resetForm("allParams");
            this.allList = []
        },
        /** 重置按钮操作 */
        resetQueryAll() {
            this.resetAll();
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            let arr = []
            selection.forEach(item => {
                item['deptId'] = this.deptId
                item['researchId'] = Number(this.researchId)
                arr.push(item)
            });
            this.arrList = arr
        },
        deletePatient(event) {
            let patientIds = event ? event.patientId : this.patientIds
            this.$modal.confirm('是否确认删除患者？').then(function () {
                return removeEnrollment(patientIds);
            }).then((res) => {
                this.getList();
                this.$modal.msgSuccess(res.msg || "删除成功");
            }).catch(() => { });
        },
        /**
         * 确认导入
         */
        submitAll() {
            if (this.arrList && this.arrList.length > 0) {
                this.loading = true
                subNoEnrollment(this.arrList).then(response => {
                    this.$nextTick(() => {
                        this.addOpen = false;
                        this.getList()
                    });
                    this.loading = false
                })
            }
        },
        cancelAll() {
            this.addOpen = false;
            this.resetAll()
        },
        /**
         * 保存/保存下一步
         */
        handleSubmit(valData) {
            if (valData == 'next') {
                this.$emit('nextSubmitSeven', true)
            } else if (valData == 'save') {
                this.$emit('nextSubmitSevenSave', true)
            }
        }
    },
};
</script>
<style lang="scss" scoped>
.query_params_input {
    width: 150px;
}
.el-table {
    ::v-deep .select_table_row {
        cursor: pointer;
    }
}
</style>