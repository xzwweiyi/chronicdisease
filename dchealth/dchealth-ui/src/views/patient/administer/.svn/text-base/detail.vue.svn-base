<template>
    <el-row class="details_content">
        <el-col :span="8" v-loading="isLoadingTreeList">
            <!-- <p class="patientName">{{patientName}}</p> -->
            <el-scrollbar class="center-scrollbar">
                <el-tree :data="treeList" node-key="nodeKey" class="user_tree_line" :props="defaultProps" :indent="0" @node-click="nodeClick" ref="leftTree" highlight-current accordion :current-node-key="currentNodeKey" :default-expanded-keys="defaultExpandedKeys">
                </el-tree>
            </el-scrollbar>
        </el-col>
        <el-col :span="16" v-loading="isLoadingPatientInfo">
            <div class="patient_details">
                <p class="patient_title">{{patientTitle}}</p>
                <!-- 健康档案显示 -->
                <el-scrollbar class="patient_info-scrollbar">
                    <template v-if="currentTreeType == 1">
                        <el-descriptions :column="2" border class="patient_info" v-if="patientInfo">
                            <el-descriptions-item label-class-name="patient_label" content-class-name="patient_content" label="患者姓名">{{patientInfo.patientName | nullFormat}}</el-descriptions-item>
                            <el-descriptions-item label-class-name="patient_label" content-class-name="patient_content" label="性别">{{patientInfo.sex |convertAgentStr}}</el-descriptions-item>
                            <el-descriptions-item label-class-name="patient_label" content-class-name="patient_content" label="患者ID">{{patientInfo.patientId | nullFormat}}</el-descriptions-item>
                            <el-descriptions-item label-class-name="patient_label" content-class-name="patient_content" label="创建时间">
                                {{patientInfo.createTime|dateFormat}}
                            </el-descriptions-item>
                        </el-descriptions>
                        <template v-if="patientDetails&&patientDetails.length>0">
                            <el-descriptions :column="1" border class="medical_item" v-for="(info,infoIndex) in patientDetails" :key="infoIndex" :title="info.medicalName">
                                <template v-for="(item,index) in info.children">
                                    <el-descriptions-item class="patient_record_files" content-class-name="patient_content" v-if="item.openImage=='1'&&item.labelType=='form'&&item.children&&item.children.length" :label="item.medicalName">
                                        <el-image class="patient_record_img" :src="ittem" v-for="(ittem,ittemIndex) in item.children[0].imageUrls" :key="ittemIndex" :preview-src-list="item.children[0].imageUrls">
                                            <div slot="placeholder" class="el-image__error">
                                                加载中<span class="dot">...</span>
                                            </div>
                                        </el-image>
                                    </el-descriptions-item>
                                    <el-descriptions-item v-else-if="item.children&&item.children.length&&item.labelType=='form'" :key="index" :label="item.medicalName" content-class-name="patient_content" label-class-name="patient_label">
                                        <el-descriptions :column="1" border direction="vertical">
                                            <el-descriptions-item v-for="(element,itemIndex) in item.children" :key="itemIndex" :label="element.propertyName" label-class-name="patient_label">
                                                <div class="patient_record_files" v-if="element.patientRecordFiles&&element.patientRecordFiles.length">
                                                    <el-image class="patient_record_img" :src="ittem.imageUrl" v-for="(ittem,ittemIndex) in element.patientRecordFiles" :key="ittemIndex" :preview-src-list="element.imageUrlLst">
                                                        <div slot="placeholder" class="el-image__error">
                                                            加载中<span class="dot">...</span>
                                                        </div>
                                                    </el-image>
                                                </div>
                                                <component-echo class="property_value" :info="element" v-else>
                                                </component-echo>
                                            </el-descriptions-item>
                                        </el-descriptions>
                                    </el-descriptions-item>
                                    <el-descriptions-item :label="item.propertyName" content-class-name="patient_content" label-class-name="patient_label" v-else-if="item.patientRecordFiles&&item.patientRecordFiles.length">
                                        <el-image class="patient_record_img" :src="ittem.imageUrl" fit="contain" v-for="(ittem,ittemIndex) in item.patientRecordFiles" :key="ittemIndex" :preview-src-list="item.imageUrlLst">
                                            <div slot="placeholder" class="el-image__error">
                                                加载中<span class="dot">...</span>
                                            </div>
                                        </el-image>
                                    </el-descriptions-item>
                                    <el-descriptions-item :label="item.propertyName" content-class-name="patient_content" label-class-name="patient_label" v-else>
                                        <component-echo class="property_value" :info="item">
                                        </component-echo>
                                    </el-descriptions-item>
                                </template>
                            </el-descriptions>
                        </template>
                        <el-empty v-else :image-size="200"></el-empty>
                    </template>
                    <!-- 检查 -->
                    <el-collapse v-model="collapseActiveName" accordion v-else-if="(currentTreeType==2||currentTreeType==3||currentTreeType==4||currentTreeType==5)&&checkExamItemsInfo&&checkExamItemsInfo.length">
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
                    <template v-else-if="currentTreeType == 2||currentTreeType == 3||currentTreeType == 6">
                        <el-descriptions class="margin-top" :column="1" :labelStyle="widthStyle" border v-if="currentInfoType == 1&&basePatientInfo.length">
                            <el-descriptions-item v-for="(item,index) in basePatientInfo" :key="index">
                                <template slot="label">
                                    {{item.renderName}}：
                                </template>
                                {{item.value | nullFormat}}
                            </el-descriptions-item>
                        </el-descriptions>
                        <el-descriptions class="margin-top" :column="1" :labelStyle="widthStyle" border v-else-if="currentInfoType == 2&&physicalExamination.length">
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
                </el-scrollbar>
            </div>
        </el-col>
    </el-row>
</template>

<script>
import {
    getPatientQueueTreeList,
    getPatientHealthTreeList,
    getPatientPhysicalTreeList,
    getfMedicalInfoReq,
    queryMedicalExamForm,
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
import ComponentEcho from '../../workstation/components/assembly'
import filter from '@/filters/index'
import { inputComponents } from '@/utils/generator/config.js'
import TableItem from '@/views/patient/administer/tableItem'
import { convertFieldsDataLoop } from '@/utils/index'
export default {
    name: "patientDetail",
    components: {
        ComponentEcho,
        TableItem
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
            isLoadingPatientInfo: false,
            widthStyle: {
                "width": "200px"
            },
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
            checkOutpatient: [],
            outpatientList: [],
            hideQueueTree: false,
            collapseActiveName: '',
            checkCollapseActive: '',
        };
    },
    computed: {
        isInspect() {
            return this.patientTitle != '检查'
        }
    },
    created() {
        let queryInfo = this.$route.query
        this.patientId = queryInfo.patientId
        this.researchId = queryInfo.researchId
        this.followId = queryInfo.followId
        this.articleId = queryInfo.articleId
        this.initData()
    },
    methods: {
        initData() {
            this.getTreeList();
        },
        getTreeList() {
            let params = {
                patientId: this.patientId
            }
            this.isLoadingTreeList = true;
            Promise.all([getPatientHealthTreeList({ ...params, type: '1' }),
            getPatientPhysicalTreeList({ ...params, type: '2' }),
            getPatientQueueTreeList(params)]).then(response => {
                this.isLoadingTreeList = false;
                let treeData2 = this.convertTreeChildren([response[0].data], 'childrenList', 2)
                let treeData3 = this.convertTreeChildren([response[1].data], 'childrenList', 3)
                let treeData1 = this.convertTreeChildren(response[2].data, 'children', 1)
                let treeData4 = this.convertTreeOutpatient('门诊记录', 'Outpatient_record', 4);
                let treeData5 = this.convertTreeOutpatient('预住院记录', 'preAdmission_record', 5);
                let treeData6 = {
                    id: 0, level: 1, nodeId: "0", nodeName: '基本信息', nodeKey: 'baseInfo_record', treeType: 6,
                    parentNodeId: "0", personId: this.personId, patientId: this.patientId, type: 1,
                }
                this.treeList = [treeData6, ...treeData2, ...treeData3, ...treeData4, ...treeData5, ...treeData1]
                // if (this.treeList[0].childrenList && this.treeList[0].childrenList.length) {
                this.setDefaultExpanded(this.treeList)
                // }
            }).catch(error => {
                this.isLoadingTreeList = false;
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
        // 节点单击事件
        nodeClick(node, event) {
            // console.log('node', node, event)
            const followTypeData = ['MedicalInfo', 'PatientDetails']
            this.currentNodeInfo = node
            let title = node.nodeName;
            this.currentTreeType = node.treeType;
            this.currentInfoType = node.type

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
            // console.log('node, event', node, event)
            title = event.parent ? this.convertTitle(event.parent) + '--' : '' + title;
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
                } else if (node.treeType == 1) {
                    if (event.level == 6 && (node.type == 1 || node.type == 2)) {
                        const func = this[`get${followTypeData[0]}`]
                        func && func(node)
                    } else if (event.level == 5 && node.type == 3) {
                        const func = this[`get${followTypeData[1]}`]
                        func && func(node)
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
                // console.log('node, event', node, event)
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
            this.isLoadingPatientInfo = true;
            let params = {
                patientId: this.patientId,
                personId: this.personId
            }
            getBasePatientInfo(params).then(res => {
                let baseInfo = filter.convertBaseInfo();
                let resData = res.data
                this.basePatientInfo = [];
                if (resData) {
                    baseInfo.forEach(m => {
                        if (resData[m.renderKey] != null && resData[m.renderKey] != undefined) {
                            let pushInfo = { ...m, value: resData[m.renderKey] }
                            this.basePatientInfo.push(pushInfo)
                        }
                    })
                }
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
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
            this.isLoadingPatientInfo = true;
            getPatientGeneralInfo(paramsData).then(res => {
                let baseInfo = filter.convertBaseInfo();
                let resData = res.data
                this.physicalExamination = [];
                if (resData) {
                    baseInfo.forEach(m => {
                        if (resData[m.renderKey] != null && resData[m.renderKey] != undefined) {
                            this.physicalExamination.push({ ...m, value: resData[m.renderKey] })
                        }
                    })
                }
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        // 诊断
        getDiagnosis(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId,
                visitId: event.visitId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
            }
            this.isLoadingPatientInfo = true;
            getQueryDiagnosis(paramsData).then(res => {
                let resData = res.data
                if (resData && resData.obj) {
                    this.diagnosisPtaientInfo = resData.obj
                }
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  检查
        getCheckExamItems(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId,
                visitId: event.visitId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
            }
            this.isLoadingPatientInfo = true;
            this.checkExamItemsInfo = []
            getQueryCheckExamItems(paramsData).then(res => {
                let resData = res.data
                this.checkExamItemsInfo = resData
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  检验
        getCheckTestItems(event, parent) {
            let parentData = parent.data
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId,
                visitId: event.visitId,
                type: event.treeType == 2 ? '1' : (event.treeType == 3 ? '2' : ''),
                checkDate: parentData.nodeName
            }
            this.isLoadingPatientInfo = true;
            getQueryCheckTestItems(paramsData).then(res => {
                this.queryTestItemsInfo = res.data
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        //  门诊记录-检验
        getCheckOutpatient(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId,
            }
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
                personId: this.personId,
            }
            this.isLoadingPatientInfo = true;
            this.checkExamItemsInfo = []
            getQueryMzExamItemList(paramsData).then(res => {
                let resData = res.data
                let checkExamItems = [];
                if (resData && resData.length) {
                    checkExamItems = resData
                }
                this.checkExamItemsInfo = checkExamItems
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
                personId: this.personId,
            }
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
                personId: this.personId,
            }
            this.isLoadingPatientInfo = true;
            this.checkExamItemsInfo = []
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
                personId: this.personId,
            }
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
        // 患者信息详情
        getMedicalInfo(event) {
            let params = {
                patientId: this.patientId,
                personId: this.personId,
                researchId: this.researchId,
                followId: event.followId,
                medicalFormId: event.medicalFormId,
                recordId: event.recordId
            }
            this.isLoadingPatientInfo = true;
            this.patientDetails = []
            getfMedicalInfoReq(params).then(res => {
                let resData = res.data
                if (res && resData) {
                    this.patientInfo = resData.patientInfo;
                    this.patientName = this.patientInfo ? this.patientInfo['patientName'] : ''
                    let medicalInfos = resData.medicalInfos
                    if (medicalInfos && medicalInfos.length) {
                        let medicalInfoList = medicalInfos.map(element => {
                            const { attribute, ...itemInfo } = element
                            let attributeJson = inputComponents.find(info => info.labelType == 'form');
                            if (attribute) {
                                attributeJson = JSON.parse(attribute)
                            }
                            let infoData = {
                                ...attributeJson,
                                ...itemInfo,
                                children: this.convertLeftMedicalInfoArray(element, 'medicalProperties', true)
                            }
                            return infoData
                        })
                        this.patientDetails = medicalInfoList
                    }
                }
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        // 患教问卷详情
        getPatientDetails(event) {
            let paramsData = {
                patientId: this.patientId,
                personId: this.personId,
                articleId: event.articleId, //档案id
                recordId: event.recordId,
                researchId: event.researchId,
            }
            this.isLoadingPatientInfo = true;
            this.patientDetails = []
            queryMedicalExamForm(paramsData).then(response => {
                let resData = response.data
                if (resData) {
                    this.patientInfo = resData.patientInfo;
                    let fmedicalForm = resData.fmedicalExamForm
                    if (fmedicalForm) {
                        const { attribute, ...itemInfo } = fmedicalForm
                        let attributeJson = inputComponents.find(info => info.labelType == 'form');
                        if (attribute) {
                            attributeJson = JSON.parse(attribute)
                        }
                        this.patientDetails = [{
                            ...attributeJson,
                            ...itemInfo,
                            children: this.convertLeftMedicalInfoArray(fmedicalForm, 'subjectList', true)
                        }]
                    }
                }
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
        // 重置左侧值显示
        convertLeftMedicalInfoArray(valueListInfo, properties) {
            let medicalInfoList = []
            if (valueListInfo && valueListInfo[properties] && valueListInfo[properties].length) {
                valueListInfo[properties].forEach(element => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    let infoData = {
                        ...JSON.parse(element.attribute),
                        ...itemInfo,
                    }
                    if (infoData.defaultValue) {
                        if (infoData.labelType == 'Radio' || infoData.labelType == 'Select') {
                            let a = infoData.options.find(f => f.value === infoData
                                .defaultValue)
                            infoData['defaultValue'] = a && a.label
                        } else if (infoData.labelType == 'Checkbox') {
                            let arrCheckbox = []
                            infoData.defaultValue.map(item => {
                                let a = infoData.options.find(f => f.value === item)
                                if (a && a.label) {
                                    arrCheckbox.push(a.label)
                                }
                            })
                            infoData['defaultValue'] = arrCheckbox.toString()
                        }
                    }
                    if (element.imageUrls && element.imageUrls.length) {
                        infoData.imageUrls = element.imageUrls.map(m => {
                            return { imageUrl: m }
                        })
                        infoData['imageUrlLst'] = element.imageUrls
                    }
                    medicalInfoList.push(infoData)
                });
            }
            if (valueListInfo && valueListInfo.medicalFormList && valueListInfo.medicalFormList.length) {
                valueListInfo.medicalFormList.forEach(element => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    let infoData = {
                        ...JSON.parse(element.attribute),
                        ...itemInfo,
                        children: this.convertLeftMedicalInfoArray(element, properties, true)
                    }
                    if (element.patientRecordFiles && element.patientRecordFiles.length) {
                        infoData['imageUrlLst'] = element.patientRecordFiles.map(m => m.imageUrl)
                    }
                    medicalInfoList.push(infoData)
                })
            }
            medicalInfoList.sort((a, b) => {
                return a.sortNum - b.sortNum
            })
            medicalInfoList.forEach((item, index) => {
                if (item.labelType == 'form' && item.children && item.children.length) {
                    item.isShowDom = this.convertIsShowDom(item, medicalInfoList)
                    item.children.forEach((itemC, indexC) => {
                        itemC.isShowDom = this.convertIsShowDom(itemC, medicalInfoList)
                    })
                } else if (item.labelType !== 'form') {
                    item.isShowDom = this.convertIsShowDom(item, medicalInfoList)
                }
            })
            return medicalInfoList
        },
        // 是否显示
        convertIsShowDom(item, fields) {
            let fieldsData = convertFieldsDataLoop(fields)
            let isShowDom = false;
            if (item.relationInfo && item.relationInfo.relationQuestion.length) {
                let relationQuestoinList = item.relationInfo.relationQuestion;
                relationQuestoinList.forEach((itemR, indexR) => {
                    let fieldsItem = fieldsData.find(f => f.propertyCode == itemR.questionId);
                    let aggregate = [];
                    let valueList = fieldsItem && fieldsItem.defaultValue || []
                    if (fieldsItem && (fieldsItem.tag == 'el-radio-group' || fieldsItem.tag ==
                        'el-checkbox-group' || fieldsItem.tag == 'el-select')) {
                        if (!Array.isArray(valueList)) {
                            valueList = [valueList]
                        }

                        if (itemR.checkedOptions && itemR.checkedOptions.length) {
                            itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                                let checkItemString = checkItem.toString()
                                if (valueList.indexOf(checkItemString) > -1 || valueList.findIndex(f => f == checkItem) > -1) {
                                    aggregate.push(checkIndex)
                                }
                            })
                        }
                    } else if (fieldsItem && fieldsItem.tag == 'el-switch') {
                        if (itemR.checkedOptions && itemR.checkedOptions.length) {
                            itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                                if (valueList.indexOf(checkItem) > -1) {
                                    aggregate.push(checkIndex)
                                }
                            })
                        }
                    } else if (fieldsItem && fieldsItem.defaultValue) {
                        aggregate.push(true)
                    }
                    if (aggregate.length) {
                        isShowDom = true;
                    } else if (fieldsItem && (fieldsItem.tag == 'el-upload') &&
                        fieldsItem.defaultValue && fieldsItem.defaultValue.length) {
                        isShowDom = true;
                    } else {
                        isShowDom = false;
                    }
                })
            } else {
                isShowDom = true;
            }
            return isShowDom
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
                height: calc(100% - 45px);
                border-bottom: 1px #ededed solid;

                ::v-deep .el-scrollbar__wrap {
                    overflow-x: hidden;
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
            }

            ::v-deep .medical_item {
                border: 1px #ededed solid;
                border-radius: 3px;
                padding: 10px;
                margin: 10px 0;
                .el-descriptions__header {
                    margin-bottom: 10px;
                    color: #333;
                }
                .patient_content {
                    width: 75%;
                }
                .patient_label {
                    color: #333;
                }
                .patient_record_img {
                    width: 300px;
                    .el-image__error {
                        min-height: 80px;
                    }
                }
            }
        }
        .value_icon {
            color: #f56c6c;
        }
        .el-collapse{
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