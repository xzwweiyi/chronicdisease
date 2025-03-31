<template>
    <div class="queue-container">
        <el-scrollbar class="page_container" ref="queueScrollbar">
            <div class="scrollbar_content">
                <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px" class="search_form">
                    <el-form-item label="队列名称" prop="projectName">
                        <el-input v-model="queryParams.projectName" placeholder="请输入队列名称" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                    </el-form-item>
                </el-form>

                <el-row class="mb8">
                    <el-col :span="1.5">
                        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['research:diseases:add']">创建病种</el-button>
                        <el-button type="success" plain icon="el-icon-edit" @click="handleAddQueue" v-hasPermi="['research:info:add']">创建队列</el-button>
                    </el-col>
                    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
                </el-row>
                <!-- 表格风格 -->
                <el-table v-loading="loading" :data="configList" row-key="rowKey" :key="refreshTable" ref="queueTable" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" highlight-current-row @row-click="toggleExpandRow" :expand-row-keys="expandRowKeys" class="queue_table">
                    <el-table-column label="名称" align="left" prop="label" min-width="500" show-overflow-tooltip>
                        <template slot-scope="scope">
                            {{scope.row.label}}
                            <template v-if="scope.row.type != '0'&&scope.row.id!=100">
                                ({{scope.row.type == 1 ? scope.row.count + '科室' :
                                 scope.row.type == 2 ? scope.row.count + '病种' : 
                                 scope.row.type == 3 ? scope.row.count + '队列' : 
                                scope.row.type == 4 ? scope.row.count + '患者' : ''}})
                            </template>
                        </template>
                    </el-table-column>
                    <el-table-column label="状态" align="center" prop="researchStatus" width="200">
                        <template slot-scope="scope" v-if="scope.row.type == '4'">
                            <el-tag :type="scope.row.researchStatus == '0' ? 'warning' : 
                        scope.row.researchStatus =='1' ? 'success' : 'info'">
                                {{scope.row.researchStatus == '0' ? '待发布' : 
                        scope.row.researchStatus == '1' ? '进行中' : '已关闭'}}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="320" fixed="right">
                        <template slot-scope="scope">
                            <el-button type="text" v-if=" scope.row.type == 4" @click.stop="handleAddCodeOne(scope.row)">
                                二维码</el-button>
                            <el-button type="text" v-if="scope.row.type == 3" @click.stop="handleAdd(scope.row)" v-hasPermi="['research:info:edit']">
                                修改名称</el-button>
                            <el-button type="text" v-if="scope.row.type == 4&&scope.row.count" @click.stop="toPaientList(scope.row)" v-hasPermi="['research:patient:query']">
                                患者列表</el-button>
                            <el-button type="text" v-if="scope.row.type == 4" @click.stop="handleQueue(scope.row,scope)" v-hasPermi="['research:info:query']">随访设置</el-button>
                            <el-button type="text" v-if="scope.row.type == '3'&&!scope.row.count" @click.stop="handleDeleteDiseases(scope.row)" v-hasPermi="['research:diseases:remove']" class="el-button-text_delete">删除</el-button>
                            <el-button type="text" v-if="scope.row.type == '4'&&!scope.row.count" @click.stop="handleDelete(scope.row)" v-hasPermi="['research:info:remove']" class="el-button-text_delete">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-scrollbar>
        <!-- 添加或修改病种 -->
        <el-dialog :title="title" :visible.sync="open" width="600px">
            <el-form ref="form" :model="form" :rules="rules">
                <el-form-item label="选择科室" prop="deptId" v-show="flagShow">
                    <el-select v-model="form.deptId" @change="handSelect" :disabled="diseasesData.length==1" clearable style="width: 440px" filterable>
                        <el-option v-for="(dict,index) in diseasesData" :key="index" :label="dict.label" :value="dict.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="病种名称" prop="diseaseName">
                    <el-select v-model="form.diseasesGroupsId" @change="handDiseaseSelect" clearable style="width: 440px" filterable>
                        <el-option v-for="(dict,index) in diseasesDataGroups" :key="index" :label="dict.diseasesGroupsName" :value="dict.diseasesGroupsId" />
                    </el-select>
                </el-form-item>
                <el-form-item label="自定义" prop="diseaseName" clearable v-if="form.diseasesGroupsId=='custom'">
                    <el-input v-model="form.diseaseName" style="width:440px"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 创建队列 -->
        <el-drawer :title="title" :visible.sync="openForm" ref="formScroll" size="80%" :modal-append-to-body="false">
            <el-scrollbar class="create_queue_dialog" ref="createQueueScroll">
                <step-one :addform="addform" v-if="openForm" @closecreatedia="closeCreateDialog"></step-one>
            </el-scrollbar>
        </el-drawer>
        <!-- 生成二维码 -->
        <el-dialog :title="title" :visible.sync="openCode" width="600px" :close-on-click-modal="false" :destroy-on-close="true">
            <div class="imgCanvas">
                <div class="qrcode" ref="qrCodeUrl" id="printStyle"></div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="printHandle">打印</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import cache from '@/plugins/cache'
import {
    listDiseases, addDiseases, editDiseases,
    delDiseases, selectDiseases, infoDel
} from "@/api/info/config";
import { addDiseasesGroups, listDiseasesGroups } from "@/api/basics/config"
import QRCode from 'qrcodejs2'
import html2canvas from 'html2canvas'
import printJS from 'print-js'
import stepOne from '@/views/queueSettings/child/one'
export default {
    name: "queueIndex",
    components: {
        stepOne
    },
    data() {
        return {
            // 遮罩层
            loading: true,
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
                projectName: undefined,
                id: undefined,
            },
            // 表单参数
            form: {},
            researhRadio: [
                { label: '本中心队列', value: '1' },
                { label: '院外队列', value: '2' },
            ],
            researchStatusRadio: [
                { label: '公开', value: '1' },
                { label: '私有', value: '2' },
            ],
            // 是否展开，默认全部展开
            isExpandAll: false,
            // 病种数据
            diseasesData: [],
            diseasesDataGroups: [],
            flagShow: true,
            // 表单校验
            rules: {
                deptId: [{ required: true, message: "科室不能为空", trigger: "blur" }],
                diseaseName: [{ required: true, message: "病种不能为空", trigger: "blur" }]
            },
            openCode: false,
            openForm: false,
            addform: {},
            currentRowKey: '',
            expandRowKeys: [],
            refreshTable: '123',
        };
    },
    created() {
        this.getList();
    },
    activated() {
        this.getList()
    },
    methods: {
        // 获取病种结构接口
        getDiseases() {
            selectDiseases().then(response => {
                this.diseasesData = response.data
                if (response.data.length == 1) {
                    this.form.deptId = response.data[0].id
                }
            })
        },
        // 获取病种
        getDiseasesGroups() {
            let data = {
                diseasesGroupsName: ''
            }
            listDiseasesGroups(data).then(response => {
                this.diseasesDataGroups = response.data
                this.diseasesDataGroups.push({
                    diseasesGroupsName: '自定义病种',
                    diseasesGroupsId: 'custom'
                })
            })
        },
        // 跳转角色管理
        hadleRole(item) {
            this.$router.push({
                path: "/researh/role/roleList",
                query: {
                    id: item.id
                }
            });
        },
        /** 新建队列 */
        handleAddQueue() {
            this.openForm = true
            this.title = '创建队列简介'

            this.$nextTick(() => {
                if (this.$refs['createQueueScroll']) {
                    this.$refs['createQueueScroll'].wrap.scrollTop = 0
                }
            })
        },
        closeCreateDialog() {
            this.openForm = false
        },

        // 跳转队列简介
        handleQueue(item, scope) {
            // console.log(item, scope)
            cache.session.setJSON('researchStatus', item.researchStatus)
            let stage = item.researchStatus != '1' ? item.stage : '0'
            this.$router.push({
                path: '/queueSettings/introductions/index',
                query: {
                    id: item.id,
                    stage: stage
                }
            });
        },
        /** 展开/折叠操作 */
        toggleExpandRow(row, column, cell) {
            if (row.children && column.label != '操作') {
                if (cell.currentTarget.querySelector(".el-table__expand-icon")) {
                    cell.currentTarget.querySelector(".el-table__expand-icon").click()
                }
            }
        },
        /** 查询参数列表 */
        getList() {
            this.loading = true;
            let params = {
                ...this.queryParams
            }
            listDiseases(params).then(response => {
                this.configList = response.data.map(m => {
                    return {
                        ...m,
                        rowKey: `${m.id}_${m.nodeId}`,
                        children: this.convertChildren(m.children, m.id, m.label)
                    }
                });
                if (!this.queryParams.projectName || this.queryParams.projectName == '') {
                    this.expandRowKeys = [this.configList[0].rowKey]
                } else {
                    this.expandRowKeys = this.lookForAllId(this.configList)
                }
                this.loading = false;
            });
        },
        lookForAllId(data = [], arr = []) {
            for (let item of data) {
                arr.push(item.rowKey)
                if (item.children && item.children.length) this.lookForAllId(item.children, arr)
            }
            return arr
        },
        // 
        convertChildren(children, id, label, parentIds) {
            if (children && children.length) {
                let child = children.map(m => {
                    let parentName = Array.isArray(label) ? [...label] : [label];
                    let nextParentName = [...parentName, m.label]
                    let parent_ids = parentIds ? parentIds : [id]
                    let nextParentIds = [...parent_ids, m.id]
                    const returnItem = {
                        ...m,
                        rowKey: `${id}_${m.id}_${m.nodeId}`,
                        parentName: parentName,
                        parentIds: parent_ids,
                        children: this.convertChildren(m.children, id, nextParentName, nextParentIds)
                    }
                    return returnItem
                })
                return child
            } else {
                return null
            }
        },
        // 
        reverseTableData(list, parent) {
            list.forEach(item => {
                if ("children" in item) {
                    if (parent) {
                        item['parentLabel'] = parent.parentLabel ? parent.parentLabel + '-' + parent.label :
                            parent.label
                        item.newId = item.parentNodeId + '_' + item.id
                    }
                    item.children = this.reverseTableData(item.children, item)
                } else if (parent) {
                    item['parentLabel'] = parent.parentLabel ? parent.parentLabel + '-' + parent.label :
                        parent.label
                    item.newId = item.parentNodeId + '_' + item.id
                }
            })
            return list
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                diseaseName: undefined,
                deptId: undefined,
                diseaseId: undefined,
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.dateRange = [];
            this.resetForm("queryForm");
            this.handleQuery();
        },
        /** 新增按钮操作 */
        handleAdd(item) {
            console.log(item, 'item')
            if (item.id != undefined && item) {
                this.reset();
                this.open = true;
                this.title = "修改病种名称";
                this.form.diseaseName = item.label;
                this.form.diseaseId = item.id;
                this.flagShow = false
            } else {
                this.reset();
                this.open = true;
                this.title = "新建病种";
                this.getDiseases()
                this.getDiseasesGroups()
                this.flagShow = true
            }
        },
        /**
         * 选择科室
         */
        handSelect() {

        },
        handDiseaseSelect(event) {
            console.log(event)
            if (event != 'custom') {
                this.form.diseaseName = this.diseasesDataGroups.find(f => f.diseasesGroupsId == event).diseasesGroupsName
            } else if (event == 'custom') {
                this.form.diseaseName = ''
            } else {
                this.form.diseaseName = ''
            }
        },
        /** 提交按钮 */
        submitForm: function () {
            if (this.flagShow) {
                if (!this.form.deptId || !this.form.diseaseName) {
                    this.$message.warning('病种和科室信息不能为空')
                    return
                }
            } else {
                if (!this.form.diseaseName) {
                    this.$message.warning('病种信息不能为空')
                    return
                }
            }
            // 
            if (this.form.diseasesGroupsId == 'custom') {
                addDiseasesGroups({ diseasesGroupsName: this.form.diseaseName }).then(response => {
                    this.addDiseasesCustomInfo()
                });
            } else {
                this.addDiseasesCustomInfo()
            }
        },
        addDiseasesCustomInfo() {
            if (this.form.diseaseId != undefined) {
                editDiseases(this.form).then(response => {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                });
            } else {
                addDiseases(this.form).then(response => {
                    this.$modal.msgSuccess("新增成功");
                    this.open = false;
                    this.getList();
                });
            }

        },
        /** 删除队列按钮操作 */
        handleDelete(row) {
            this.$modal.confirm('是否确认删除队列为"' + row.label + '"的数据项？').then(function () {
                return infoDel(row.id);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 删除病种 */
        handleDeleteDiseases(row) {
            this.$modal.confirm('是否确认删除病种为"' + row.label + '"的数据项？').then(function () {
                return delDiseases(row.id);
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
        },
        creatQrCodeOne(row) {
            setTimeout(() => {
                var qrcode = new QRCode(this.$refs.qrCodeUrl, {
                    text: `{"type":"${row.type}","deptId":"${row.id}"}`, // 需要转换为二维码的内容
                    width: 200,
                    height: 200,
                    colorDark: '#000000',
                    colorLight: '#ffffff',
                    correctLevel: 3
                })
                qrcode._el.title = ''
            }, 0)
        },
        creatQrCodeTwo(row) {
            setTimeout(() => {
                var qrcode = new QRCode(this.$refs.qrCodeUrl, {
                    text: `{"type":"${row.type}","diseaseId":"${row.id}"}`, // 需要转换为二维码的内容
                    width: 200,
                    height: 200,
                    colorDark: '#000000',
                    colorLight: '#ffffff',
                    correctLevel: 3
                })
                qrcode._el.title = ''
            }, 0)
        },
        creatQrCodeThree(row) {
            let parentName = row.parentName
            // deptName: String 科室,   deptId: String 科室id, diseasesName: String 病种
            // diseaseId: String 病种id,  researchId: String 队列id, projectName: String 队列name,
            // type: String 2科室类型 3病种类型 4队列类型
            const qrcodeUrl = `{"type":"${row.type}","researchId":${row.id},"deptName":"${parentName[1]}","diseasesName":"${parentName[2]}","projectName":"${decodeURIComponent(row.label)}","deptId":${row.parentIds[1]},"diseaseId":${row.parentIds[2]}}`
            setTimeout(() => {
                var qrcode = new QRCode(this.$refs.qrCodeUrl, { // 需要转换为二维码的内容
                    text: qrcodeUrl,
                    width: 200,
                    height: 200,
                    colorDark: '#000000',
                    colorLight: '#ffffff',
                    correctLevel: 3,//设置成QRCode.CorrectLevel.H时二维码url字段过长会生成失败
                })
                qrcode._el.title = ''
            }, 0)
        },
        handleAddCodeOne(row) {
            this.title = '查看二维码'
            this.openCode = true
            if (row.type == 2) {
                this.creatQrCodeOne(row)
            } else if (row.type == 3) {
                this.creatQrCodeTwo(row)
            } else if (row.type == 4) {
                this.creatQrCodeThree(row)
            }

        },
        printHandle() {
            html2canvas(this.$refs.qrCodeUrl, {
                backgroundColor: null,
                useCORS: true,
                windowHeight: document.body.scrollHeight
            }).then((canvas) => {
                const url = canvas.toDataURL()
                this.img = url
                printJS({
                    printable: url,
                    type: 'image',
                    documentTitle: ''
                })
            })
        },
        toPaientList(event) {
            this.$router.push({
                path: `/queueSettings/patient/list/${event.id}`,
                query: {
                    researchName: encodeURIComponent(event.label)
                }
            });
        }
    }
};
</script>

<style lang="scss" scoped>
.imgCanvas {
    display: block;
    margin: 10% auto;
    width: 200px;
}

.create_queue_dialog {
    height: calc(100vh - 80px);
}

::v-deep .queue_table {
    .el-table__row--level-0,
    .el-table__row--level-1,
    .el-table__row--level-2 {
        cursor: pointer;
    }
}

.page_container {
    background: #fff;
    margin: 15px;
    border-radius: 5px;
    height: calc(100vh - 114px);

    .scrollbar_content {
        padding: 20px 20px 30px;
    }
}
</style>