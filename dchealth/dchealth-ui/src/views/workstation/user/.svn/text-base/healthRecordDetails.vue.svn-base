<template>
    <el-row class="details_content">
        <el-col :span="7" v-loading="isLoadingTreeList">
            <!-- <p class="patientName">{{patientName}}</p> -->
            <el-scrollbar class="center-scrollbar">
                <el-tree :data="treeList" node-key="nodeKey" class="user_tree_line" :props="defaultProps" :indent="0" @node-click="nodeClick" ref="leftTree" highlight-current accordion :current-node-key="currentNodeKey" :default-expanded-keys="defaultExpandedKeys">
                </el-tree>
            </el-scrollbar>
        </el-col>
        <el-col :span="17" v-loading="isLoadingPatientInfo">
            <div class="patient_details">
                <p class="patient_title">{{patientTitle}}</p>
                <!-- 健康档案显示 -->
                <el-scrollbar class="patient_info-scrollbar">
                    <!-- 检查 -->
                    <el-collapse v-model="collapseActiveName" accordion v-if="(currentTreeType==2||currentTreeType==3||currentTreeType==4||currentTreeType==5)&&checkExamItemsInfo&&checkExamItemsInfo.length">
                        <el-collapse-item v-for="(checkInfo,checkIndex) in checkExamItemsInfo" :key="checkIndex" :name="checkIndex">
                            <template slot="title">
                                <h4 class="com-details_title patient_details_title">{{checkInfo.itemName}}</h4>
                            </template>
                            <el-collapse v-model="checkCollapseActive" accordion>
                                <template v-if="checkInfo.array&&checkInfo.array.length">
                                    <el-collapse-item v-for="(arrayItem,arrayIndex) in checkInfo.array" :key="arrayIndex">
                                        <template slot="title">
                                            <h4 class="child_collapse_title">{{arrayItem.itemTitle}}</h4>
                                        </template>
                                        <table-item :isShowNo="false" :show-header="isInspect" :is-border="isInspect" :list-data="arrayItem.itemList"></table-item>
                                    </el-collapse-item>
                                </template>
                                <template v-else-if="checkInfo.obj&&checkInfo.obj.length">
                                    <template v-if="checkInfo.obj[0].itemList">
                                        <el-collapse-item v-for="(objItem,objIndex) in checkInfo.obj" :key="objIndex">
                                            <template slot="title">
                                                <h4 class="child_collapse_title">{{objItem.itemTitle}}</h4>
                                            </template>
                                            <table-item :isShowNo="false" :show-header="isInspect" :is-border="isInspect" :list-data="objItem.itemList"></table-item>
                                        </el-collapse-item>
                                    </template>
                                    <template v-else>
                                        <table-item :isShowNo="false" :show-header="isInspect" :is-border="isInspect" :list-data="checkInfo.obj"></table-item>
                                    </template>
                                </template>
                                <el-empty v-else :image-size="200"></el-empty>
                            </el-collapse>
                        </el-collapse-item>
                    </el-collapse>
                    <template v-else-if="currentTreeType==2||currentTreeType==3||currentTreeType==6">
                        <el-descriptions class="margin-top" :column="1" :size="size" :labelStyle="widthStyle" border v-if="currentInfoType == 1&&basePatientInfo.length">
                            <el-descriptions-item v-for="(item,index) in basePatientInfo" :key="index">
                                <template slot="label">
                                    {{item.renderName}}：
                                </template>
                                {{item.value | nullFormat}}
                            </el-descriptions-item>
                        </el-descriptions>
                        <el-descriptions class="margin-top" :column="1" :size="size" :labelStyle="widthStyle" border v-else-if="currentInfoType == 2&&physicalExamination.length">
                            <el-descriptions-item v-for="(item,index) in physicalExamination" :key="index">
                                <template slot="label">
                                    {{item.renderName}}：
                                </template>
                                {{item.value | nullFormat}}
                            </el-descriptions-item>
                        </el-descriptions>
                        <template v-else-if="currentInfoType==3&&diagnosisPtaientInfo&&diagnosisPtaientInfo.length">
                            <el-collapse v-model="collapseActiveName" accordion>
                                <el-collapse-item v-for="(diagnosis,diagnosisIndex) in diagnosisPtaientInfo" :key="diagnosisIndex" :name="diagnosisIndex">
                                    <template slot="title">
                                        <h4 class="com-details_title patient_details_title">{{diagnosis.itemTitle}}</h4>
                                    </template>
                                    <table-item :isShowNo="patientTitle=='检验'" :show-header="isInspect" :is-border="isInspect" :list-data="diagnosis.itemList"></table-item>
                                </el-collapse-item>
                            </el-collapse>
                        </template>
                        <template v-else-if="currentInfoType==5&&queryTestItemsInfo&&queryTestItemsInfo.length">
                            <el-collapse v-model="collapseActiveName" accordion>
                                <el-collapse-item v-for="(checkInfo,checkIndex) in queryTestItemsInfo" :key="checkIndex" :name="checkIndex">
                                    <template slot="title">
                                        <h4 class="com-details_title patient_details_title">{{checkInfo.itemName}}</h4>
                                    </template>
                                    <table-item :isShowNo="patientTitle=='检验'" :show-header="isInspect" :is-border="isInspect" :list-data="checkInfo.obj"></table-item>
                                </el-collapse-item>
                            </el-collapse>
                        </template>
                        <el-empty v-else :image-size="200"></el-empty>
                    </template>
                    <template v-else-if="currentTreeType==4||currentTreeType==5">
                        <el-collapse v-model="collapseActiveName" accordion v-if="outpatientList&&outpatientList.length">
                            <el-collapse-item v-for="(checkInfo,checkIndex) in outpatientList" :key="checkIndex" :name="checkIndex">
                                <template slot="title">
                                    <h4 class="com-details_title patient_details_title">{{checkInfo.itemName}}</h4>
                                </template>
                                <template v-if="currentInfoType==2&&checkInfo.obj&&checkInfo.obj.length">
                                    <table-item :isShowNo="patientTitle=='检验'" :show-header="isInspect" :is-border="isInspect" :list-data="checkInfo.obj"></table-item>
                                </template>
                                <template v-else-if="(currentInfoType==1||currentInfoType==3)&&checkInfo.obj&&checkInfo.obj.length">
                                    <div v-for="(objItem,objIndex) in checkInfo.obj" :key="objIndex">
                                        <el-tag effect="plain" class="details_tagTit" v-if="isInspect"> {{ objItem.itemTitle }} </el-tag>
                                        <table-item :isShowNo="patientTitle=='检验'" :show-header="isInspect" :is-border="isInspect" :list-data="objItem.itemList"></table-item>
                                    </div>
                                </template>
                                <el-empty v-else :image-size="200"></el-empty>
                            </el-collapse-item>
                        </el-collapse>
                        <el-empty v-if="!outpatientList||!outpatientList.length" :image-size="200"></el-empty>
                    </template>
                    <el-empty v-else :image-size="200"></el-empty>

                    <!-- 入组开始  -->
                    <div class="user_detail_buttons" v-if="detailsType == 1">
                        <template v-if="nodeTypeStatus==1">
                            <el-button type="primary" v-if="enrollmentPatient.enrollmentHandlerStatus==1" @click="assignQueueFollow('3')" v-hasPermi="['follow:patient_enrollment:handle']">确认入组</el-button>
                            <!-- <el-button type="primary" plain @click="assignQueueFollow('2')" :disabled="enrollmentPatient.refuseCount>=2" v-hasPermi="['follow:patient_enrollment:handle']">修改入组</el-button> -->
                            <!-- <el-button type="warning" plain @click="assignQueueFollow('1')"
                        v-hasPermi="['follow:patient_enrollment:handle']">待分配</el-button> -->
                        </template>
                        <el-button type="primary" v-if="nodeTypeStatus==3&&enrollmentPatient.enrollmentHandlerStatus==3&&enrollmentPatient.enrollmentStatus==0" @click="assignQueueFollow('3')" :disabled="enrollmentPatient.refuseCount==2" v-hasPermi="['follow:patient_enrollment:handle']">重新入组</el-button>
                        <el-button type="danger" plain @click="callofEnrollment('1')" v-if="(nodeTypeStatus==2&&enrollmentPatient.enrollmentHandlerStatus==2||nodeTypeStatus==3&&enrollmentPatient.enrollmentHandlerStatus==3
                        &&enrollmentPatient.enrollmentStatus==1||nodeTypeStatus==1&&enrollmentPatient.enrollmentHandlerStatus==1)
                        &&!enrollmentPatient.haveSubmitted">取消入组</el-button>
                        <el-button @click="cancleUserList()">返回</el-button>
                    </div>
                    <!-- 入组结束 -->
                </el-scrollbar>

                <!-- 修改入组 -->
                <el-dialog title="修改确认入组" :visible.sync="openModify" width="500px" center @close="canleSeeForm">
                    <el-form ref="seeForm" :model="seeForm" :rules="rulesSee" label-width="120px">
                        <el-form-item label="选择病种队列：" prop="researchId">
                            <el-cascader v-model="seeForm.researchId" :options="bzList" class="dialog_cascader" :props="{value: 'nodeId', label: 'nodeName', children: 'researchDiseasesSelects'}" :show-all-levels="false" clearable @change="hadleSelect"></el-cascader>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog_footer">
                        <el-button @click="handleSeeForm" type="primary">确 认</el-button>
                        <el-button @click="canleSeeForm">取 消</el-button>
                    </div>
                </el-dialog>
                <!-- 取消入组 -->
                <el-dialog title="取消入组原因" :visible.sync="isShowCallofDialog" width="700px" center @close="canleCallOfEnrollment">
                    <el-form label-width="150px">
                        <el-form-item label="取消入组原因：" prop="refuseCode" required>
                            <el-select v-model="refuseCode" style="width:100%" @change="refuseChange">
                                <el-option v-for="(item,index) in refuseOptions" :key="index" :value="item.value" :label="item.label">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-input v-model="refuseReason" clearable v-if="refuseCode==3"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog_footer">
                        <el-button @click="sureCallofEnrollment" type="primary">确 认</el-button>
                        <el-button @click="canleCallOfEnrollment">取 消</el-button>
                    </div>
                </el-dialog>
            </div>
        </el-col>
    </el-row>
</template>

<script>
import {
    getPatientHealthTreeList,
    getPatientPhysicalTreeList,
    getBasePatientInfo, //健康档案
    getPatientGeneralInfo,
    getQueryDiagnosis,
    getQueryCheckExamItems,
    getQueryCheckTestItems,
    getQueryMzExamItemList,
    getQueryMzTestItemList,
    getQueryMzDiagnosisList,
    getAdmissionTestItemList,
    getQueryExamItemList,
    getQueryDiagnosisList,
} from '@/api/patient/index'
import {
    handleEnrollment, //确认入组/修改入组/待分配入组
    selectResearch, //病种及队列下拉框
    listEnrollment, //入组列表
} from "@/api/follow/work";
import ComponentEcho from '@/views/workstation/components/assembly'
import filter from '@/filters/index'
import TableItem from '@/views/patient/administer/tableItem'
export default {
    name: "patientDetail",
    components: {
        ComponentEcho, TableItem
    },
    data() {
        return {
            treeList: [],
            isLoadingTreeList: false,
            patientName: '患者名称',
            patientTitle: '患者当前信息',
            patientInfo: {},
            patientDetails: [],
            patientId: '',
            baseId: '',
            isLoadingPatientInfo: false,
            userInfo: {},
            widthStyle: { "width": "200px" },
            size: '12',
            defaultProps: { children: 'childrenList', label: 'nodeName', key: 'nodeKey' },
            currentTreeType: '',
            currentInfoType: '',
            currentTreeNodeName: '',
            currentNodeInfo: undefined,
            basePatientInfo: [],
            diagnosisPtaientInfo: [],
            queryTestItemsInfo: [],
            currentNodeKey: '',
            defaultExpandedKeys: [],
            physicalExamination: [],
            checkExamItemsInfo: [],
            detailsType: '',
            nodeTypeStatus: '',
            openModify: false,
            nodeIdTwo: '',
            //修改入组
            seeForm: {},
            rulesSee: {},
            bzList: [],
            outpatientList: [],
            isShowCallofDialog: false,
            refuseCode: '',
            refuseReason: '',
            refuseOptions: [
                { value: 1, label: '不符合入组条件' },
                { value: 2, label: '患者多次拒绝入组' },
                { value: 3, label: '自定义' },
            ],
            enrollmentPatient: {},
            collapseActiveName: '',
            checkCollapseActive: '',
        };
    },
    created() {
        let queryInfo = this.$route.query
        this.patientId = queryInfo.patientId
        this.researchId = queryInfo.researchId
        this.followId = queryInfo.followId
        this.articleId = queryInfo.articleId
        this.detailsType = queryInfo.type
        this.nodeTypeStatus = queryInfo.nodeTypeStatus
        this.personId = queryInfo.personId
        this.initData()
    },
    computed: {
        isInspect() {
            return this.patientTitle == '检验'
        }
    },
    methods: {
        initData() {
            this.getTreeList();
            this.getListEnrollment()
        },
        /** 查询入组列表 */
        getListEnrollment() {
            this.loading = true;
            const queryData = {
                // enrollmentHandlerStatus: this.nodeTypeStatus,
                researchId: this.researchId,
                patientId: this.patientId
            }
            listEnrollment(queryData).then((response) => {
                this.enrollmentPatient = response.rows[0];
                this.loading = false;
            }).catch(error => {
                this.loading = false;
            });
        },
        getTreeList() {
            let params = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.isLoadingTreeList = true;
            Promise.all([getPatientHealthTreeList({ ...params, type: '1' }),
            getPatientPhysicalTreeList({ ...params, type: '2' })]).then(([treeData2, treeData3]) => {
                treeData2 = this.convertTreeChildren([treeData2.data], 'childrenList', 2)
                treeData3 = this.convertTreeChildren([treeData3.data], 'childrenList', 3)
                let treeData4 = this.convertTreeOutpatient('门诊记录', 'Outpatient_record', 4);
                let treeData5 = this.convertTreeOutpatient('预住院记录', 'preAdmission_record', 5);
                let treeData6 = {
                    id: 0, level: 1, nodeId: "0", nodeName: '基本信息', nodeKey: 'baseInfo_record', treeType: 6,
                    parentNodeId: "0", personId: this.personId, patientId: this.patientId, type: 1,
                }
                this.treeList = [treeData6, ...treeData2, ...treeData3, ...treeData4, ...treeData5,]
                // if (this.treeList[0].childrenList && this.treeList[0].childrenList.length) {
                this.setDefaultExpanded(this.treeList)
                // }
                this.isLoadingTreeList = false;
            }).catch(error => {
                this.isLoadingTreeList = false;
            })
        },
        setDefaultExpanded(data, parent) {
            if (data && data.length) {
                let node = {}
                if (!parent) {
                    node = { level: 1, data: data[0] }
                } else {
                    node = {
                        level: parent.level + 1,
                        data: data[0],
                        parent: parent
                    }
                }
                this.defaultExpandedKeys.push(data[0].nodeKey)
                if (data[0].childrenList && data[0].childrenList.length) {
                    this.setDefaultExpanded(data[0].childrenList, node)
                } else {
                    this.nodeClick(data[0], node)
                    this.currentNodeKey = this.defaultExpandedKeys[this.defaultExpandedKeys.length - 1]
                    this.$nextTick(function () {
                        this.$refs.leftTree.setCurrentKey(this.currentNodeKey)
                    })
                }
            }
        },
        convertTreeChildren(data, childrenList, treeType, nodeKey) {
            const treeTtpeList = ['queueTree', 'recordTree', 'checkTree', 'outpatientTree', 'preAdmissionTre']
            return data.map((m, index) => {
                let nodeTreeKey = nodeKey ? `${nodeKey}_${m.id == null ? index : m.id}` :
                    `${treeTtpeList[treeType - 1]}_${m.id == null ? index : m.id}`
                let item = {
                    ...m,
                    treeType,
                    nodeKey: nodeTreeKey,
                }
                if (treeType == 1) {
                    item.nodeName = m.label
                }
                if (m[childrenList] && m[childrenList].length) {
                    item['childrenList'] = this.convertTreeChildren(m[childrenList], childrenList, treeType,
                        nodeTreeKey)
                }
                return item
            })
        },
        convertTreeOutpatient(nodeNameStr, nodeKeyStr, treeTypeNumber) {
            let outPatient = [{
                id: 0, level: 1, nodeId: "0", nodeName: nodeNameStr, nodeKey: nodeKeyStr,
                parentNodeId: "0", personId: this.personId, patientId: this.patientId, type: 0,
                childrenList: [{
                    id: 1, level: 2, nodeId: "1", nodeName: "检查", treeType: treeTypeNumber, nodeKey: `${nodeKeyStr}_check`,
                    parentNodeId: "0", personId: this.personId, patientId: this.patientId, type: '1',
                }, {
                    id: 2, level: 2, nodeId: "2", nodeName: "检验", treeType: treeTypeNumber, nodeKey: `${nodeKeyStr}_inspect`,
                    parentNodeId: "0", personId: this.personId, patientId: this.patientId, type: '2',
                }, {
                    id: 3, level: 2, nodeId: "3", nodeName: "诊断", treeType: treeTypeNumber, nodeKey: `${nodeKeyStr}_diagnosis`,
                    parentNodeId: "0", personId: this.personId, patientId: this.patientId, type: '3',
                }]
            }]
            return outPatient
        },
        nodeClick(node, event) {
            this.currentTreeType = node.treeType;
            this.currentInfoType = node.type
            this.currentNodeInfo = node
            let title = node.nodeName;
            this.patientTitle = title;
            if (node.nodeKey == this.currentNodeKey) {
                return
            } else {
                this.currentNodeKey = node.nodeKey
            }
            this.collapseActiveName = ''
            this.outpatientList = [];
            this.checkExamItemsInfo = [];
            this.diagnosisPtaientInfo = [];
            this.basePatientInfo = [];
            this.patientDetails = [];
            this.queryTestItemsInfo = [];
            this.physicalExamination = [];
            this.checkOutpatient = [];
            // console.log('treeType', this.currentTreeType, 'infoType', this.currentInfoType)
            if (event.level >= 2) {
                if (node.treeType == 2 || node.treeType == 3) {
                    if (node.type == '1') {
                        this.getPatientBaseInfo(node)
                    } else if (node.type == '2') {
                        this.getGeneralInfo(node)
                    } else if (node.type == '3') {
                        this.getDiagnosis(node)
                    } else if (node.type == '4') {
                        this.getCheckExamItems(node)
                    } else if (node.type == '5') {
                        this.getCheckTestItems(node, event.parent)
                    }
                } else if (node.treeType == 4) {
                    if (node.type == '1') {
                        this.getInspectOutpatient();
                    } else if (node.type == '2') {
                        this.getCheckOutpatient()
                    } else if (node.type == '3') {
                        this.getQueryMzDiagnosisPatient()
                    }
                } else if (node.treeType == 5) {
                    if (node.type == '1') {
                        this.getPreAdmissionInspect();
                    } else if (node.type == '2') {
                        this.getPreAdmissionCheckOut()
                    } else if (node.type == '3') {
                        this.getPreAdmissionQuery()
                    }
                }
            } else if (event.level == 1 && node.treeType == 6) {
                this.getPatientBaseInfo(node)
            }
        },
        convertTitle(event) {
            let title = event.data.nodeName
            if (event.parent && event.level > 1) {
                let parentTitle = this.convertTitle(event.parent) + '--' + title
                return parentTitle
            } else {
                return title
            }
        },
        // 基本信息
        getPatientBaseInfo() {
            let params = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.basePatientInfo = [];
            getBasePatientInfo(params).then(res => {
                let baseInfo = filter.convertBaseInfo();
                let resData = res.data
                if (resData) {
                    baseInfo.forEach(m => {
                        if (resData[m.renderKey] != null && resData[m.renderKey] != undefined) {
                            let pushInfo = { ...m, value: resData[m.renderKey] }
                            this.basePatientInfo.push(pushInfo)
                        }
                    })
                }
            })
        },
        // 一般检查
        getGeneralInfo(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId,
                visitId: event.visitId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
            }
            this.physicalExamination = [];
            getPatientGeneralInfo(paramsData).then(res => {
                let baseInfo = filter.convertBaseInfo();
                let resData = res.data
                if (resData) {
                    baseInfo.forEach(m => {
                        if (resData[m.renderKey] != null && resData[m.renderKey] != undefined) {
                            this.physicalExamination.push({ ...m, value: resData[m.renderKey] })
                        }
                    })
                }
            })
        },
        // 诊断
        getDiagnosis(event) {
            let paramsData = {
                patientId: this.patientId,
                visitId: event.visitId,
                personId: this.personId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
            }
            this.diagnosisPtaientInfo = []
            getQueryDiagnosis(paramsData).then(res => {
                let resData = res.data
                if (resData && resData.obj) {
                    this.diagnosisPtaientInfo = resData.obj
                }
            })
        },
        //  检查
        getCheckExamItems(event) {
            let paramsData = {
                patientId: this.patientId,
                visitId: event.visitId,
                personId: this.personId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
            }
            this.checkExamItemsInfo = []
            getQueryCheckExamItems(paramsData).then(res => {
                let resData = res.data
                this.checkExamItemsInfo = resData
            })
        },
        //  检验
        getCheckTestItems(event, parent) {
            let parentData = parent.data
            let paramsData = {
                patientId: this.patientId,
                visitId: event.visitId,
                personId: this.personId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
                checkDate: parentData.nodeName
            }
            this.queryTestItemsInfo = [];
            getQueryCheckTestItems(paramsData).then(res => {
                this.queryTestItemsInfo = res.data
            })
        },
        //  门诊记录-检验
        getCheckOutpatient(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.outpatientList = [];
            this.isLoadingPatientInfo = true;
            getQueryMzTestItemList(paramsData).then(res => {
                this.outpatientList = res.data
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  门诊记录-检查
        getInspectOutpatient(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.checkExamItemsInfo = [];
            this.isLoadingPatientInfo = true;
            getQueryMzExamItemList(paramsData).then(res => {
                let resData = res.data
                this.checkExamItemsInfo = resData
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  门诊记录-诊断
        getQueryMzDiagnosisPatient(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.outpatientList = [];
            this.isLoadingPatientInfo = true;
            getQueryMzDiagnosisList(paramsData).then(res => {
                let resData = res.data
                if (resData) {
                    resData = [resData]
                }
                this.outpatientList = resData
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  预住院记录-检验
        getPreAdmissionCheckOut(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.outpatientList = [];
            this.isLoadingPatientInfo = true;
            getAdmissionTestItemList(paramsData).then(res => {
                this.outpatientList = res.data
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  预住院记录-检查
        getPreAdmissionInspect(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.checkExamItemsInfo = [];
            this.isLoadingPatientInfo = true;
            getQueryExamItemList(paramsData).then(res => {
                let resData = res.data
                this.checkExamItemsInfo = resData
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  预住院记录-诊断
        getPreAdmissionQuery(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId
            }
            this.outpatientList = [];
            this.isLoadingPatientInfo = true;
            getQueryDiagnosisList(paramsData).then(res => {
                let resData = res.data
                if (resData) {
                    resData = [resData]
                }
                this.outpatientList = resData
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        /** 关闭按钮 */
        cancleUserList() {
            const obj = {
                path: this.$route.query.oldPath
            };
            this.$tab.closeOpenPage(obj);
        },
        /** 确认入组按钮操作 */
        assignQueueFollow(handleType) {
            let params = {
                researchId: this.researchId,
                handleType,
                patientId: this.patientId,
                personId: this.personId
            }
            let confirmTitle = '';
            let dangerouslyUseHTMLString = false;
            if (handleType == 1) {
                confirmTitle = `请确认患者暂不入组？`
            } else if (handleType == 3 && this.enrollmentPatient.accountId == 0) {
                // confirmTitle = `是否确认将患者加入队列？`
                confirmTitle = `<p>稍后会给“${this.enrollmentPatient.patientName}”发送短信，短信内容为:</p>
                <p>尊敬的“${this.enrollmentPatient.patientName}”，欢迎您加入唐都慢病管理平台：</p>
                <p>1.微信关注“唐都医院”公众号；</p>
                <p>2.进入“诊疗服务”选择“慢病管理”；</p>
                <p>3.输入患者信息完成绑定，即可享受专业的慢病管理服务。</p>
                <p>唐都医院为您的健康保驾护航！</p>
                <p>点击确定即发送短信，无需发送请点击取消。</p>`
            } else if (handleType == 3 && this.enrollmentPatient.accountId != 0) {
                confirmTitle = `是否确认将患者加入队列？`
            }
            if (handleType == 1 || handleType == 3) {
                this.$confirm(`${confirmTitle}`, '提示', {
                    type: 'warning',
                    dangerouslyUseHTMLString: true

                }).then(() => {
                    this.patientEnrollment(params)
                }).catch(error => {
                    return
                })
            } else if (handleType == 2 || handleType == 0) {
                this.openModify = true;
                this.getQueueSelect();
                this.enrollmentTitle = handleType == 2 ? "修改确认入组" : (handleType == 0 ? '分配队列' : '');
            }
        },
        // 患者入组方法
        patientEnrollment(params) {
            handleEnrollment(params).then(res => {
                this.$modal.msgSuccess("操作成功");
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        },
        /**
         * 获取队列
         */
        getQueueSelect() {
            selectResearch().then(response => {
                let valueArr = response.data
                this.bzList = valueArr.filter(f => f.researchDiseasesSelects)
            })
        },
        callofEnrollment() {
            this.isShowCallofDialog = true

        },
        refuseChange(event) {
            this.refuseReason = ''
        },
        sureCallofEnrollment() {
            this.isShowCallofDialog = true
            if (!this.refuseCode) {
                this.$message.warning('请选择取消入组的原因！')
                return
            }
            if (this.refuseCode == 3 && !this.refuseReason) {
                this.$message.warning('请填写取消入组的原因！')
                return
            }
            let params = {
                handleType: '4',
                researchId: this.researchId,
                patientId: this.patientId,
                personId: this.personId,
                refuseCode: this.refuseCode
            }
            if (this.refuseCode == 3) {
                params['refuseReason'] = this.refuseReason
            } else {
                params['refuseReason'] = this.refuseOptions.find(f => f.value == this.refuseCode).label
            }
            this.patientEnrollment(params)
        },
        canleCallOfEnrollment() {
            this.isShowCallofDialog = false
        },
        /**
         * 选择分组
         */
        hadleSelect(val) {
            let arr = val.slice(1, 2).toString();
            this.nodeIdTwo = arr
        },
        /**
         * 修改入组
         */
        handleSeeForm() {
            handleEnrollment({
                handleType: '2',
                researchId: this.nodeIdTwo,
                patientId: this.patientId,
                personId: this.personId
            }).then(response => {
                this.$modal.msgSuccess("修改入组成功");
                this.openModify = false;
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        },
        /**
         * 取消入组弹窗
         */
        canleSeeForm() {
            this.openModify = false
            this.seeForm.researchId = []
            this.nodeIdTwo = ''
        },
    }
};
</script>
<style lang="scss" scoped>
.details_content {
    height: calc(100vh - 114px);
    color: #333;
    padding: 20px 20px;
    margin: 15px;
    background-color: #fff;
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0px 0px 10px 10px rgb(0, 0, 0, 0.01);

    .el-col {
        height: 100%;
        border: 1px #ededed solid;
        overflow: hidden;

        ::v-deep.center-scrollbar {
            height: 100%;
            overflow-x: hidden;

            .el-scrollbar__view {
                height: 100%;

                .el-tree {
                    padding: 10px 0;
                }
            }
        }

        .patientName {
            text-align: center;
            font-size: 14px;
            line-height: 35px;
            color: #333;
            border-bottom: 1px #ededed solid;
        }
        .details_tagTit {
            width: 96%;
        }

        .patient_details {
            margin: 0 10px;
            height: 100%;

            .patient_title {
                text-indent: 5px;
                margin: 20px 0 10px;
                font-size: 1.2em;
                font-weight: bold;
                color: #409eff;
            }

            .patient_info-scrollbar {
                height: calc(100% - 105px);
                border-bottom: 1px #ededed solid;

                ::v-deep .el-scrollbar__wrap {
                    overflow-x: hidden;
                }
            }

            ::v-deep .patient_info {
                margin: 5px 0;
                .patient_label {
                    color: #333;
                }
                .patient_content {
                    width: 30%;
                }
            }

            .medical_item {
                border-bottom: 1px #ededed solid;
                border-left: 1px #ededed solid;
                border-right: 1px #ededed solid;

                .medical_info {
                    margin: 5px 15px 15px;

                    .el-row {
                        &:last-child .el-col {
                            border-bottom: 1px #ededed solid;
                        }
                    }

                    .el-col {
                        line-height: 40px;
                        border: none;
                        text-indent: 15px;
                        flex-grow: 1;
                        border: 1px #ededed solid;
                        border-bottom: 0;

                        &:first-child {
                            border-right: 0;
                        }
                    }
                }
            }

            .medical_name {
                line-height: 45px;
                text-indent: 15px;
            }
        }
        .value_icon {
            color: #f56c6c;
        }
        .user_detail_buttons {
            display: flex;
            justify-content: center;
            position: fixed;
            bottom:45px;
            left: 55%;
        }
        ::v-deep .el-collapse{
            ::v-deep .el-collapse-item__content{
                padding-top:5px;
            }
            .patient_details_title{
                background:transparent;
                padding: 0;
                margin:0;
                height:100%;
                display: flex;
                align-items: center;
            }
            .el-collapse{
                margin:0 15px;
                .child_collapse_title{
                    color:#6379bb;
                    font-weight: bold;
                }
            }
        }
    }
}
</style>