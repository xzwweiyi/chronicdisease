<template>
    <div class="user-detail-container" v-loading="isLoading">
        <el-row :gutter="20">
            <el-col :span="4" :xs="24">
                <el-scrollbar class="head-container">
                    <el-tree :data="treeOptions" :props="defaultProps" highlight-current :filter-node-method="filterNode" ref="leftTree" accordion node-key="id" default-expand-all :current-node-key="currentNodeKey" @node-click="handleNodeClick">
                        <span class="custom-tree-node" slot-scope="{ data }">
                            <el-tooltip effect="dark" :content="data.nodeName" placement="top-start" :disabled="!data.nodeName||!data.nodeName.length||(data.nodeName&&data.nodeName.length<7)">
                                <p class="ellipsis node_name"> {{data.nodeName ? data.nodeName  : '无'}}</p>
                            </el-tooltip>
                        </span>
                    </el-tree>
                </el-scrollbar>
            </el-col>
            <el-col :span="20" :xs="24">
                <el-descriptions :column="4" border class="patient_info" label-class-name="patient_label" content-class-name="patient_content">
                    <el-descriptions-item label="患者姓名">{{infoPatientInfo.patientName | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="性别">{{infoPatientInfo.sex |convertAgentStr}}</el-descriptions-item>
                    <el-descriptions-item label="患者ID">{{infoPatientInfo.patientId | nullFormat}}</el-descriptions-item>
                    <el-descriptions-item label="创建时间">{{infoPatientInfo.createTime|dateFormat}}</el-descriptions-item>
                </el-descriptions>
                <!-- 内容开始 -->
                <el-row :gutter="24">
                    <el-col :span="12" :xs="24">
                        <el-scrollbar class="descriptions_form_info">
                            <el-descriptions :column="1" :size="size" border direction="vertical">
                                <el-descriptions-item v-for="(item, index) in appMedicalInfoArray"
                                    :key="'descriptions' + index" label-class-name="descriptions_label">
                                    <template slot="label">
                                        {{item.propertyName ||item.formName}}
                                    </template>
                                    <div class="description_info">
                                        <div class="properties_list" v-if="item.children&&item.children.length&&item.labelType=='form'">
                                            <div v-for="(ittem,ittemIndex) in item.children" :key="ittemIndex" class="properties_item">
                                                <div class="properties_item_compont">
                                                    <div class="property_name">{{ittem.propertyName}} ：</div>
                                                    <component-echo class="property_value" :info="ittem" :key="ittem.id+ittem.propertyValue">
                                                    </component-echo>
                                                </div>
                                                <el-button type="text" v-if="ittem.propertyValue" @click="tohistoryLog(ittem)" v-hasPermi="['edc:details:showLog']">日志查看
                                                </el-button>
                                            </div>
                                        </div>
                                        <div class="patient_record_files" style="width: 100%;" v-else-if="item.imageUrlLst&&item.imageUrlLst.length">
                                            <div class="imageBox" v-loading="isLoadingText">
                                                <div class="imageBoxLeft" v-for="(ittem,ittemIndex) in item.imageUrlLst" :key="ittemIndex">
                                                    <el-image class="patient_record_img" :src="ittem" fit="contain" @click="openImageViewer(item.imageUrlLst)">
                                                    </el-image>
                                                    <el-button type="success" plain icon="el-icon-edit" @click="handleOCR(index, ittem)">一键识别</el-button>
                                                </div>
                                                <div class="imageBoxRight">
                                                    <el-input type="textarea" v-model="item.ittemText" :rows="6" placeholder="请输入内容"></el-input>
                                                </div>
                                            </div>
                                            <el-image-viewer v-if="isShowViewer" :class="previewImgClass" :on-close="closeViewer" :url-list="preImageUrlLst" :showOverlay="true">
                                            </el-image-viewer>
                                        </div>
                                        <component-echo class="info_val" :info="item" :key="item.id+item.propertyValue" v-else>
                                        </component-echo>
                                        <el-button type="text" v-if="item.propertyValue" @click="tohistoryLog(item)" v-hasPermi="['edc:details:showLog']">日志查看
                                        </el-button>
                                    </div>
                                </el-descriptions-item>
                            </el-descriptions>
                            <el-descriptions title="医护质疑记录" :column="1" :size="size" border direction="vertical">
                                <el-descriptions-item v-for="(item, index) in FllowQuestionRecord" :key="index" label-class-name="question_record_label">
                                    <div>
                                        医生提问：{{ item.fromContent | nullFormat}}
                                    </div>
                                    <div>
                                        患者回复：{{ item.replyContent | nullFormat }}
                                    </div>
                                </el-descriptions-item>
                            </el-descriptions>
                            <el-empty v-if="!FllowQuestionRecord.length" description="暂无医护质疑记录"></el-empty>
                        </el-scrollbar>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-scrollbar class="descriptions_form_info">
                            <el-form label-position="top" :disabled="!infoData.showSaveSubmit">
                                <el-descriptions class="margin-top" :column="1" :size="size" border direction="vertical">
                                    <el-descriptions-item v-for="(item, index) in infoMedicalInfoArray" :key="index" label-class-name="descriptions_label">
                                        <template slot="label">
                                            {{item.propertyName ||item.formName}}
                                        </template>
                                        <template v-if="item.children&&item.children.length&&item.labelType=='form'">
                                            <el-form-item v-for="(ittem,elementIndex) in item.children" :key="elementIndex+ittem.isShowDom" :label="ittem.propertyName+' ：'" v-if="ittem.isShowDom" :required="ittem.required">
                                                <myFormInfo v-if="ittem.tag!=='el-upload'" :info="ittem" :disabled="!infoData.showSaveSubmit" :isShowName="true" :previewFormData="infoMedicalInfoArray" class="descriptions_item" @change="valueChange" />
                                            </el-form-item>
                                        </template>
                                        <template v-else>
                                            <myFormInfo v-if="item.tag!='el-upload'" :info="item" :isShowName="false" class="descriptions_item_component" @change="valueChange" :previewFormData="infoMedicalInfoArray" :disabled="!infoData.showSaveSubmit" :key="item.id+item.propertyValue" />
                                        </template>
                                    </el-descriptions-item>
                                </el-descriptions>
                            </el-form>
                            <el-descriptions title="EDC质疑记录" :column="1" :size="size" border direction="vertical">
                                <el-descriptions-item v-for="(item, index) in listQuestionRecord" :key="index" label-class-name="question_record_label">
                                    <div>
                                        提问：{{ item.fromContent | nullFormat}}
                                    </div>
                                    <div class="replay_content">
                                        <template v-if="!item.canReplay">
                                            回复：{{ item.replyContent | nullFormat }}
                                        </template>
                                        <template v-else>
                                            回复：
                                            <el-input v-model="medicalReplyContent" />
                                        </template>
                                    </div>
                                </el-descriptions-item>
                            </el-descriptions>
                            <el-empty v-if="!listQuestionRecord.length" description="暂无EDC质疑记录"></el-empty>
                        </el-scrollbar>
                        <div class="dialog-footer button_style" v-if="infoMedicalInfoArray&&infoMedicalInfoArray.length">
                            <el-button type="primary" :disabled="!infoData.showSaveSubmit" v-hasPermi="['edc:details:save']" @click="submitFormInfo('save')">保 存</el-button>
                            <el-button type="primary" :disabled="!infoData.showSaveSubmit" v-hasPermi="['edc:details:submit']" @click="submitFormInfo('next')">提 交
                            </el-button>
                            <el-button type="primary" :disabled="!infoData.showReplay" v-hasPermi="['question:qt:saveedcask']" @click="challenge">发起质疑
                            </el-button>
                            <el-button type="primary" :disabled="!infoData.showQueryLock" v-hasPermi="['question:qt:edit']" @click="lockEDCMedicalForm()">锁 库
                            </el-button>
                        </div>
                    </el-col>
                </el-row>
                <!-- 内容结束 -->
            </el-col>
        </el-row>
        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="openDialog" width="600px">
            <el-input type="textarea" v-model="fromContent" :rows="10" placeholder="请输入内容" />
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancelForm">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="日志修改" :visible.sync="isOpenHistory" width="60%" destroy-on-close>
            <el-table :data="historyList" height="60vh" empty-text="暂无日志记录">
                <el-table-column prop="propertyValue" align="center" label="修改值">
                </el-table-column>
                <el-table-column prop="createTime" align="center" label="操作时间">
                </el-table-column>
                <el-table-column prop="createBy" align="center" label="操作人">
                    <template slot-scope="scope">
                        {{ scope.$index==0?scope.row.createBy :scope.row.updateBy  | nullFormat}}
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </div>
</template>

<script>
import {
    treeFollow,
    workEdcDetail,
    findRecord,
    subEdcInsert,
    getListQuestionRecord,
    subSaveQuestionRecord,
    subReplayQuestionRecord,
    lockEDCQuestion,
    quseiontList, //质疑列表
} from "@/api/follow/work";
import cityData from '@/utils/city.json'
import {
    formConf
} from '@/utils/generator/config'
import DraggableItem from '@/views/tool/build/DraggableItem'
import ComponentEcho from '../components/assembly'
import EditForm from '../components/editForm'
import EditItem from '../components/editItem'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import myFormInfo from '@/views/tool/build/previewForm/item'
import { convertFieldsDataLoop } from '@/utils/index'
import Tesseract from 'tesseract.js';
export default {
    name: "userDetails",
    components: {
        DraggableItem,
        ComponentEcho,
        EditForm,
        myFormInfo,
        EditItem,
        ElImageViewer
    },
    data() {
        return {
            formConf,
            open: false,
            // 遮罩层
            isLoading: true,
            treeOptions: [],
            defaultProps: {
                children: "childrenList",
                label: "nodeName",
            },
            infoData: {},
            size: 'medium',
            infoPatientInfo: {},
            infoMedicalInfo: {},
            infoMedicalInfoArray: [],
            appMedicalInfo: {},
            appMedicalInfoArray: [],
            openDialog: false,
            title: null,
            fromContent: '',
            rules: {
                nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }]
            },
            isOpenHistory: false,
            historyList: [],
            researchId: '',
            currentNodeKey: '',
            currentNodeInfo: undefined,
            edcStatus: '',
            patientId: '',
            followId: '',
            recordId: '',
            medicalReplyContent: '',
            medicalFormId: '', //表单id
            listQuestionRecord: [], //EDC 质疑记录
            FllowQuestionRecord: [], //随访质疑记录
            customTagList: ['com-BMI', 'com-TimeNumeral', 'com-Maternity', 'com-popQ', 'com-DValue', 'com-complication',
                'com-yearMonthDay', 'com-questionnaire'],
            arrayValTagList: ['el-checkbox-group', 'el-cascader'],
            isLoading: false,
            isShowViewer: false,
            nextPreviewFormFileds: [],
            isLoadingText: false,
            preImageUrlLst: [],
        };
    },
    created() {
        this.initData()
    },
    watch: {
        '$route.query.patientId'(newVal) {
            if (newVal && this.$route.query.edcStatus) {
                this.initData()
            }
        },
    },
    computed: {
        previewImgClass() {
            let sidebarStatus = this.$store.getters.sidebar
            if (sidebarStatus.opened) {
                return 'preview_sideOpen_image'
            } else if (!sidebarStatus.opened) {
                return 'preview_sideClose_image'
            }
        }
    },
    methods: {
        initData() {
            let queryData = this.$route.query;
            this.edcStatus = queryData.edcStatus;
            this.researchId = queryData.researchId;
            this.recordId = queryData.recordId;
            this.patientId = queryData.patientId;
            this.followId = queryData.followId;
            this.getListTree()
            this.getQuseiontRecord();
        },
        /**
         * 获取详情
         */
        getListTree() {
            this.isLoading = true;
            treeFollow({
                patientId: this.patientId,
                followId: this.followId,
                recordId: this.recordId
            }).then((response) => {
                let treeLists = []
                let responseData = response.data ? [response.data] : [];
                if (response.data) {
                    treeLists = responseData.map(m => {
                        return {
                            ...m,
                            id: m.id && m.id.toString(),
                            childrenList: m.childrenList && m.childrenList.length ?
                                m.childrenList.map(item => {
                                    return {
                                        ...item,
                                        id: item.id && item.id.toString()
                                    }
                                }) : null
                        }
                    })
                    if (responseData[0].childrenList && responseData[0].childrenList.length) {
                        if (treeLists !== undefined && treeLists != null && treeLists.length > 0) {
                            let currentNode = treeLists[0].childrenList[0]
                            this.currentNodeKey = currentNode.id
                            this.$nextTick(function () {
                                this.$refs.leftTree.setCurrentKey(this.currentNodeKey)
                            })
                            this.currentNodeInfo = currentNode
                            this.getEDCWorkList(currentNode)
                        }
                    }

                }
                this.treeOptions = treeLists;
                this.isLoading = false;
            })
        },
        // 筛选节点
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        // 节点单击事件
        handleNodeClick(data) {
            this.researchId = data.researchId
            this.currentNodeKey = data.id
            this.currentNodeInfo = data
            if (data.childrenList == null) {
                this.getEDCWorkList(data)
            }
        },
        //模块详情
        getEDCWorkList(item) {
            this.medicalFormId = item.id
            let dataObj = {
                medicalFormId: item.id,
                researchId: this.researchId,
                patientId: item.patientId,
                followId: item.followId,
                recordId: this.recordId,
            }
            this.isLoading = true;
            workEdcDetail(dataObj).then(response => {
                this.isLoading = false;
                this.infoData = response.data
                this.infoPatientInfo = this.infoData.patientInfo || {}
                let appMedicalInfo = this.infoData.appMedicalInfo || {}
                let infoMedicalInfo = this.infoData.medicalInfo || {}
                this.infoMedicalInfoArray = this.convertRightFormProperties(infoMedicalInfo, false,
                    JSON.parse(JSON.stringify(appMedicalInfo)))
                this.infoMedicalInfoArray.sort((a, b) => {
                    return a.sortNum - b.sortNum
                })
                this.appMedicalInfoArray = this.convertLeftMedicalInfoArray(appMedicalInfo)
                this.appMedicalInfoArray.sort((a, b) => {
                    return a.sortNum - b.sortNum
                })
                if (this.edcStatus != '1') {
                    this.getListQuesList();
                }
            }).catch(error => {
                this.infoPatientInfo = {};
                this.infoMedicalInfoArray = [];
                this.appMedicalInfoArray = [];
            })
        },
        // 重置右侧表单显示
        convertRightFormProperties(listInfo, showName, appMedicalInfo) {
            let formList = []
            if (listInfo && listInfo.medicalProperties && listInfo.medicalProperties.length) {
                let medicalList = listInfo.medicalProperties.filter(f => f.propertyType != 'Upload' &&
                    f.propertyType != 'upload')
                let appProperties = appMedicalInfo.medicalProperties
                let appMedicalList = appProperties && appProperties.length
                    && appProperties.filter(f => f.propertyType != 'Upload' && f.propertyType != 'upload') || []
                medicalList.forEach((element, index) => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    const appMedicalItem = appMedicalList[index]
                    let infoData = {
                        ...JSON.parse(element.attribute),
                        ...itemInfo,
                        isEditOption: false,
                        isHideBtn: true,
                        isHideName: !showName,
                        id: element.id.toString(),
                    }
                    if (infoData.tagType == 'province') {
                        infoData['options'] = cityData
                    }
                    if (!element.propertyValue && appMedicalItem && appMedicalItem.propertyValue != null
                        && appMedicalItem.propertyValue != undefined) {
                        element.propertyValue = appMedicalItem.propertyValue
                    }
                    if (element.propertyValue) {
                        let defaultValue = element.propertyValue;
                        if (infoData.options && infoData.options.length && infoData.tag !== 'el-cascader') {
                            infoData.options.forEach(itemF => {
                                itemF.value = itemF.value.toString()
                            })
                        }
                        if (infoData.tag == 'el-cascader' || infoData.labelType == 'Checkbox') {
                            defaultValue = defaultValue.split(',')
                            if (infoData.labelType == 'Cascader') defaultValue = defaultValue.map(m =>
                                Number(
                                    m))
                        } else if (infoData.labelType == 'Rate' || infoData.labelType == 'slider') {
                            defaultValue = Number(defaultValue)
                        } else if (infoData.labelType == 'Radio' && infoData.options.find(f => f
                            .isShowInputValue)) {
                            infoData.isShowInputValue = true;
                            infoData.options.find(f => f.isShowInputValue)['inputVal'] = infoData.other
                        } else { }
                        infoData['defaultValue'] = defaultValue
                    } else if (infoData.imageUrls) {
                        infoData['fileList'] = infoData.imageUrls.map((m, index) => {
                            return {
                                url: m,
                                name: index
                            }
                        })
                    }
                    formList.push(infoData)
                });
            }
            if (listInfo && listInfo.medicalFormList && listInfo.medicalFormList
                .length) {
                listInfo.medicalFormList.forEach((element, index) => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    let infoData = {
                        ...JSON.parse(element.attribute),
                        ...itemInfo,
                        isEditOption: false,
                        isHideBtn: true,
                        isHideName: !showName,
                        children: this.convertRightFormProperties(element, true, appMedicalInfo.medicalFormList[index])
                    }
                    formList.push(infoData)
                })
            }
            return formList
        },
        // 重置左侧值显示
        convertLeftMedicalInfoArray(valueListInfo) {
            let medicalInfoList = []
            if (valueListInfo && valueListInfo.medicalProperties &&
                valueListInfo.medicalProperties.length) {
                valueListInfo.medicalProperties.forEach(element => {
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
                        infoData.imageUrls = element.imageUrls
                        infoData['imageUrlLst'] = element.imageUrls
                    }
                    medicalInfoList.push(infoData)
                });
            }
            if (valueListInfo && valueListInfo.medicalFormList && valueListInfo.medicalFormList
                .length) {
                valueListInfo.medicalFormList.forEach(element => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    let infoData = { ...itemInfo }
                    if (attribute) {
                        infoData = {
                            ...JSON.parse(attribute),
                            ...infoData,
                        }
                    }
                    if (infoData.labelType == 'form') {
                        infoData.children = this.convertLeftMedicalInfoArray(element, true)
                    }
                    if (element.patientRecordFiles && element.patientRecordFiles.length) {
                        infoData['imageUrlLst'] = element.patientRecordFiles.map(m => m.imageUrl)
                    }
                    if (element.imageUrls && element.imageUrls.length) {
                        infoData['imageUrlLst'] = element.imageUrls.map(m => m.imageUrl)
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
        /**
         * 提交
         */
        submitFormInfo(tip) {
            let valueVos = []
            let isSaveValue = true;
            let isSaveItem = undefined
            this.infoMedicalInfoArray.forEach(item => {
                if (item.labelType == 'form') {
                    item.children.forEach(formItem => {
                        valueVos.push(this.convertTemplateProperty(formItem))
                        if (formItem.required && !formItem.propertyValue && !formItem.defaultValue) {
                            isSaveValue = false
                            isSaveItem = formItem
                        }
                    })
                } else {
                    if (item.required && !item.propertyValue && !item.defaultValue) {
                        isSaveValue = false
                        isSaveItem = item
                    }
                    valueVos.push(this.convertTemplateProperty(item))
                }
            })
            if (!isSaveValue) {
                this.$message.warning(`${isSaveItem.propertyName}未填写内容`)
                return
            }
            let paramsData = {
                recordId: this.recordId,
                flag: tip === 'save' ? false : true,
                valueVos,
                medicalFormId: this.medicalFormId,
            }
            if (tip == 'next' && !!this.listQuestionRecord.find(f => f.canReplay) && !this.medicalReplyContent) {
                this.$message.warning('请填写质疑回复内容！')
                return
            }
            if (this.medicalReplyContent) {
                paramsData.edcQuestionVo = {
                    questionId: this.listQuestionRecord.find(f => f.canReplay).questionId,
                    replyContent: this.medicalReplyContent,
                    replyUserId: this.$store.state.user.userInfo.userId,
                    recordId: this.recordId
                }
            }
            this.isLoading = true;
            subEdcInsert(paramsData).then(response => {
                this.isLoading = false;
                this.$modal.msgSuccess(tip == 'save' ? '保存成功' : '提交成功')
                this.getEDCWorkList(this.currentNodeInfo)
                // if (tip != 'save') {
                //     this.$tab.closePage(this.$route)
                //     this.$router.push({
                //         path: `${decodeURIComponent(this.$route.query.oldPath)}`
                //     })
                // }
            }).catch(eror => {
                this.isLoading = false;
            })
        },
        // 转换保存的值
        convertTemplateProperty(propertyInfo) {
            let propertyValue = propertyInfo.defaultValue || propertyInfo.propertyValue

            if (Array.isArray(propertyValue)) {
                propertyValue = propertyValue.toString()
            } else if (!propertyValue) {
                propertyValue = ''
            }

            let objData = {
                id: propertyInfo.id || '',
                type: '2', // 来源(1-患者，2-EDC)
                recordId: this.recordId,
                number: propertyInfo.sortNum,
                fromId: propertyInfo.medicalFormId,
                propertyName: propertyInfo.propertyName,
                propertyCode: propertyInfo.propertyCode,
                propertyType: propertyInfo.propertyType,
                propertyValue: propertyValue,
                propertyValues: propertyInfo.propertyValues,
                propertyId: propertyInfo.id,
                researchId: this.researchId,
                articleId: propertyInfo.articleId,
                patientId: this.patientId,
                followId: propertyInfo.followId || this.followId,
            }
            let userInfo = this.$store.state.user.userInfo
            if (propertyInfo.propertyValue) {
                objData['updateBy'] = userInfo.userName
            } else {
                objData['createBy'] = userInfo.userName
            }
            if (propertyInfo.tag == 'el-upload') {
                objData.imageUrls = propertyInfo.imageUrls || []
            } else if (propertyInfo.labelType == 'Radio') {
                objData['other'] = propertyInfo.other || ''
            }
            return objData
        },
        //发起EDC 质疑
        challenge() {
            this.openDialog = true
            this.title = '发起质疑'
        },
        //EDC 质疑弹窗取消
        cancelForm() {
            this.openDialog = false
        },
        //获取 EDC 质疑列表
        getListQuesList() {
            let objData = {
                recordId: this.recordId, //档案id
                medicalFormId: this.medicalFormId, //模块id,
                patientId: this.patientId
            }
            this.listQuestionRecord = []
            this.medicalReplyContent = ''
            getListQuestionRecord(objData).then(response => {
                let resData = response.data
                if (resData && resData.length) {
                    this.listQuestionRecord = resData
                    let canReplayInfo = resData.find(f => f.canReplay)
                    this.medicalReplyContent = canReplayInfo ? canReplayInfo.replyContent : ''
                }
            })
        },
        //EDC 质疑弹窗提交
        submitForm() {
            if (this.fromContent) {
                let objData = {
                    fromContent: this.fromContent,
                    recordId: this.recordId, //档案id
                    medicalFormId: this.medicalFormId, //模块id
                    fromUserId: this.$store.state.user.userInfo.userId,
                    patientId: this.patientId
                }
                this.saveQuestionRecord(objData)
            }
        },
        // 发送EDC质疑质疑
        saveQuestionRecord(objData) {
            subSaveQuestionRecord(objData).then(response => {
                if (response.code == 200) {
                    this.fromContent = '';
                    this.openDialog = false;
                    this.getEDCWorkList(this.currentNodeInfo)
                    // this.$tab.closePage(this.$route)
                    // this.$router.push({
                    //     path: `${decodeURIComponent(this.$route.query.oldPath)}`
                    // })
                }
            })
        },
        // 发送回复质疑
        saveReplayQuestionRecord(objData) {
            subReplayQuestionRecord(objData).then(response => {
                if (response.code == 200) {
                    this.getListQuesList()
                    this.fromContent = '';
                    this.openDialog = false;
                    // this.$tab.closePage(this.$route)
                    // this.$router.push({
                    //     path: `${decodeURIComponent(this.$route.query.oldPath)}`
                    // })
                }
            })
        },
        // 获取 随访质疑记录
        getQuseiontRecord() {
            quseiontList(this.recordId).then(res => {
                this.FllowQuestionRecord = res.data
            })
        },
        lockEDCMedicalForm() {
            lockEDCQuestion(this.recordId, this.medicalFormId).then(res => {
                this.$message.success('锁库成功')
                this.getEDCWorkList(this.currentNodeInfo)
                // this.$router.push({
                //     path: `${decodeURIComponent(this.$route.query.oldPath)}`
                // })
            })
        },
        // 查看日志
        tohistoryLog(event) {
            this.isOpenHistory = true;
            let params = {
                researchId: this.researchId,
                recordId: this.recordId,
                formId: event.medicalFormId,
                propertyId: event.id,
                propertyCode: event.propertyCode,
                propertyType: event.propertyType,
                patientId: this.patientId
            }
            findRecord(params).then(responed => {
                this.historyList = responed.data
            }).catch(error => {
                // console.log('查看日志 error', error)
            })
        },
        // 子组件 Radio option的input值
        optionValueChane(event) {
            const {
                value,
                info,
                parent
            } = event
            if (parent) {
                this.infoMedicalInfoArray.forEach(item => {
                    if (item.medicalFormId == parent.medicalFormId) {
                        item.children.find(f => f.id == info.id).other = value
                    }
                })
            } else {
                this.infoMedicalInfoArray.find(f => f.id == info.id).other = value
            }
        },
        // 子组件 上传成功 处理方法
        imgUploadSuccess(event) {
            const {
                params,
                info,
                parent
            } = event
            let imageUrl = params.url
            if (parent) {
                this.infoMedicalInfoArray.forEach(item => {
                    if (item.medicalFormId == parent.medicalFormId) {
                        let imageValue = item.children.find(f => f.id == info.id).imageUrls
                        if (imageValue && imageValue.length) {
                            imageValue.push(imageUrl)
                        } else {
                            imageValue = [imageUrl]
                        }
                        item.children.find(f => f.id == info.id).imageUrls = imageValue
                    }
                })
            } else {
                let imageValue = this.infoMedicalInfoArray.find(f => f.id == info.id).imageUrls;
                if (imageValue && imageValue.length) {
                    imageValue.push(imageUrl)
                } else {
                    imageValue = [imageUrl]
                }
                this.infoMedicalInfoArray.find(f => f.id == info.id).imageUrls = imageValue
            }
        },
        closeViewer() {
            this.isShowViewer = false
            this.preImageUrlLst = []
        },
        openImageViewer(event) {
            this.isShowViewer = true
            this.preImageUrlLst = event
        },
        valueChange(event, info) {
            let defaultVal = event.value
            let propertyVal = event.value
            if (this.customTagList.findIndex(f => f == event.item.tag) > -1 && defaultVal) {
                defaultVal = JSON.parse(event.value)
            }
            if (this.arrayValTagList.findIndex(f => f == event.item.tag) > -1 && defaultVal) {
                propertyVal = event.value.toString()
            }
            let medicalInfoArray = this.infoMedicalInfoArray
            let currentIndex = this.infoMedicalInfoArray.findIndex(f => f.propertyCode == event.item.propertyCode)
            if (currentIndex > -1) {
                medicalInfoArray[currentIndex].propertyValue = propertyVal
                medicalInfoArray[currentIndex].defaultValue = defaultVal
            } else {
                medicalInfoArray.forEach(item => {
                    if (item.labelType == 'form' && item.children.length) {
                        let childIndex = item.children.findIndex(f => f.propertyCode == event.item.propertyCode)
                        if (childIndex > -1) {
                            item.children[childIndex].propertyValue = propertyVal;
                            item.children[childIndex].defaultValue = defaultVal
                        }
                    }
                })
            }
            medicalInfoArray.forEach((item, index) => {
                if (item.labelType == 'form' && item.children && item.children.length) {
                    item.isShowDom = this.convertIsShowDom(item, medicalInfoArray)
                    item.children.forEach((itemC, indexC) => {
                        itemC.isShowDom = this.convertIsShowDom(itemC, medicalInfoArray)
                        if (itemC.propertyCode == info.propertyCode) {
                            itemC.inputVal = info.inputVal
                        }
                    })
                } else if (item.labelType !== 'form') {
                    item.isShowDom = this.convertIsShowDom(item, medicalInfoArray)
                    if (item.propertyCode == info.propertyCode) {
                        item.inputVal = info.inputVal
                    }
                }
            })
            this.infoMedicalInfoArray = medicalInfoArray
        },
        // 一键识别图片
        handleOCR(index, imageUrl) {
            this.isLoadingText = true
            if (imageUrl) {
                Tesseract.recognize(
                    imageUrl,
                    'chi_sim', // 可选择其他语言，如'chi_sim'（简体中文）
                ).then(({ data: { text } }) => {
                    this.isLoadingText = false
                    this.$set(this.appMedicalInfoArray[index], 'ittemText', text)
                }).catch(err => {
                    this.isLoadingText = false
                    this.ittemText = '文字提取失败，请重试。';
                });
            }
        },
    },
};
</script>

<style lang="scss" scoped>
.user-detail-container {
    background: #fff;
    margin: 10px;
    padding: 20px 10px 20px 0;

    .head-container {
        height: calc(100vh - 160px);

        .el-tree {
            padding: 10px 0;
            background: rgba(24, 144, 255, 0.05);
            border-radius: 5px;
        }

        .custom-tree-node {
            width: 80%;
        }

        .el-tree-node {
            border-top: 1px #f8f8f8 solid;
        }

        .search_form {
            border-radius: 5px;
        }
    }
    ::v-deep .patient_info {
        padding: 15px;
        background: rgba(24, 144, 255, 0.05);
        margin-bottom: 15px;
        .patient_label {
            color: #333;
            font-weight: bold;
            background: rgba(24, 144, 255, 0.05);
            width: 100px;
        }
        .patient_content {
            background: rgba(24, 144, 255, 0.05);
        }
    }
    .description_info {
        display: flex;

        .info_val {
            flex: 1;
        }

        .properties_list {
            width: 100%;
        }

        .patient_record_img {
            width: 150px;
            height: 100px;
            background: #f5f5f5;
            margin: 0 3px;
            cursor: pointer;
        }

        .patient_record_files {
            position: relative;
        }
    }

    .button_style {
        display: block;
        margin: 20px auto 0;
        text-align: center;
    }

    .descriptions_form_info {
        height: calc(100vh - 290px);

        .descriptions_item {
            padding: 5px;
        }

        .descriptions_item_component {
            width: 100%;

            .el-cascader {
                width: 80%;
            }
        }

        .el-descriptions-item__cell {
            padding: 15px 10px;

            .replay_content {
                display: flex;
                align-items: center;
                margin-top: 5px;

                .el-input {
                    flex: 1;
                }
            }
        }
    }

    ::v-deep .el-descriptions {
        .el-descriptions__header {
            margin: 15px 0 10px;
            padding: 8px 0;
            font-weight: bold;
            text-indent: 12px;
            border-left: 2px #409eff solid;
            background: rgba(24, 144, 255, 0.08);
        }

        .descriptions_label {
            background: rgba(24, 144, 255, 0.05);
            color: #666;
        }

        .question_record_label {
            padding: 0 !important;
            border-top: 0;
        }
    }

    .properties_item {
        color: #333;
        margin: 5px 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        flex-wrap: wrap;

        .property_value {
            color: #666;
        }

        .properties_item_compont {
            display: flex;
        }
    }

    .upload_recordFiles {
        width: 200px;
        height: 100px;
    }

    .el-radio-group {
        width: 100%;
    }

    .patient_info_form {
        display: flex;
        flex-wrap: wrap;

        .el-form-item {
            margin-right: 10px;
            margin-bottom: 20px;
            flex: 1;

            .el-form-item__content {
                white-space: nowrap;
            }
        }
    }
}

.preview_sideClose_image {
    position: absolute;
    right: 43%;
}

.preview_sideOpen_image {
    position: absolute;
    right: 37%;
    overflow: hidden;
}
.imageBox{
    display:flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
}
.imageBoxLeft{
    display: flex;
    flex-direction: column;
}
.imageBoxRight{
    width: 100%;
    margin-left: 15px;
    margin-right: 10px;
}
</style>