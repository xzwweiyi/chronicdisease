<template>
    <div class="template-container">
        <el-steps :active="activeIndex" class="record_list" align-center>
            <template v-for="(item,index) in recordList">
                <el-step v-if="index>activeIndex-3&&index<activeIndex+3" :key="index"
                    :status="index==activeIndex ?'finish':'wait'" :class="{active_step:index==activeIndex-1,
                    other_step:index!=activeIndex-1,step_single:item.frequencyType=='1'}">
                    <template slot="icon">
                        <div v-if="item.frequencyType=='2'" class="step_content">
                            {{ item.num - item.followTotal+1 }}</div>
                        <div class="lineDashed" v-if="item.frequencyType=='2'"></div>
                        <div class="step_content"> {{ item.num }} </div>
                    </template>
                    <template slot="title">
                        <div class="step_title">
                            <div class="stemp_title_item" v-if="item.frequencyType=='2'">
                                {{"随访"+(item.num - item.followTotal+1)}}
                            </div>
                            <div class="lineDashed" v-if="item.frequencyType=='2'"></div>
                            <div class="stemp_title_item">{{ item.followNameStr }}</div>
                        </div>
                    </template>
                    <template slot="description">
                        <div v-if="index>activeIndex-2">{{item.description}}</div>
                    </template>
                </el-step>
            </template>
        </el-steps>
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="200" class="search_form">
            <el-form-item label="随访名称" prop="medicalName">
                <el-input v-model="queryParams.medicalName" placeholder="请输入随访名称" clearable
                    @keyup.enter.native="getFllowupList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getFllowupList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row class="form_btn">
            <el-button type="primary" plain icon="el-icon-plus" @click="createMedicalForm(0)"
                v-hasPermi="['base:medical:add']">添加新模块</el-button>
            <el-button type="warning" plain icon="el-icon-plus" @click="createMedicalForm(3)"
                v-hasPermi="['base:medical:add']">添加自评量表</el-button>
            <el-button type="danger" plain icon="el-icon-plus" @click="createMedicalForm(4)"
                v-hasPermi="['base:medical:add']">添加预警</el-button>
        </el-row>
        <el-table v-loading="isLoadingFllowList" :data="fllowCRFList" :key="tableRenderKey" ref="dragTable"
            row-key="id">
            <el-table-column class-name="elTable_dragItem" label="序号" type="index" align="center" width="80">
            </el-table-column>
            <el-table-column label="随访名称" align="center" prop="medicalName" min-width="200px">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.formType=='4'?'danger':(scope.row.formType=='3'?'warning':'')"
                        effect="plain" class="medical_name">
                        {{ scope.row.medicalName }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="已添加条目数" align="center" prop="propertyNum" width="110" />
            <el-table-column label="APP上是否折叠该模块" align="center" prop="propertyNum" width="160">
                <template slot-scope="scope">
                    <el-switch active-value="1" inactive-value="0" v-model="scope.row.openCollapse"
                        @change="formInfoChange(scope.row)" />
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding" fixed="right" width="400">
                <template slot-scope="scope">
                    <el-button type="text" v-if="scope.row.formType=='3'" @click="setResearch(scope.row)"
                        v-hasPermi="['template:evaluation:list']">
                        设置测评结果
                    </el-button>
                    <el-button type="text" @click="addMedicalInfo(scope.row)">添加条目</el-button>
                    <el-button type="text" @click="previewItemTemplate(scope.row)">预览</el-button>
                    <el-button type="text" @click="editTemplate(scope.row)"
                        v-hasPermi="['base:medical:edit']">修改名称</el-button>
                    <el-button type="text" @click="deleteMedicalInfo(scope.row)" v-hasPermi="['base:medical:remove']"
                        class="el-button-text_delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row class="bottom_btn">
            <el-col :span="1.5" :offset="8" v-if="fllowCRFList.length">
                <el-button type="primary" plain @click="gobackPrePage(0)"
                    v-hasPermi="['base:medical:batchUpdateSortNum']">
                    保存</el-button>
            </el-col>
            <el-col :span="1.5" :offset="1" v-if="fllowCRFList.length">
                <el-button type="primary" plain @click="addFollowupToTemplate(3)">保存并添加到模板</el-button>
            </el-col>
        </el-row>
        <el-dialog :title="addEditTitle" :visible.sync="isShowDrawer" :close="closeDrawer" top="20vh" width="500px"
            center destroy-on-close>
            <el-form :model="medicalFormInfo" ref="medicalFormInfo" label-width="80px" :rules="rules"
                class="add_question_form">
                <el-form-item label="名称" prop="medicalName" required>
                    <el-input v-model="medicalFormInfo.medicalName" placeholder="请输入名称" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitMedicalForm">确定</el-button>
                    <el-button @click="cancleMedicalForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog :visible.sync="isShowPreView" :title="previewTitle" top="8vh" destroy-on-close>
            <el-scrollbar class="preview-scrollbar" v-loading="isLoadingPreview">
                <Preview-template v-if="isShowPreView&&drawingList.length" :preview-form-data="drawingList" :key="renderKey" class="preview_container_form" />
                <el-empty v-else class="no_content"></el-empty>
            </el-scrollbar>
        </el-dialog>
        <el-dialog :visible.sync="isShowcategory" width="78%" center top="8vh" destroy-on-close>
            <category :follow-id="followId" dialog-type="export" class="category_content" v-if="isShowcategory"
                @setcategoryInfo="setCategoryInfo"></category>
        </el-dialog>
        <el-dialog :visible.sync="isShowAddTemplate" width="500px" top="20vh" center destroy-on-close title="请给模板命名">
            <el-form label-width="100px">
                <el-form-item label="模板名称：" inline required>
                    <el-input v-model="saveTemplateName" clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="saveCategoryAndAdd">确认</el-button>
                    <el-button @click="cancelTemplateName">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog :visible.sync="isShowTemplate" width="78%" center v-loading="isLoadingTemplate" destroy-on-close
            top="8vh">
            <el-scrollbar class="preview-scrollbar">
                <templat-form :follow-id="followId" dialog-type="export" class="category_content"
                    :category-info="categoryInfo" v-if="isShowTemplate" @templateInfo="setTemplateInfo"
                    @previewInfo="previewTemplateInfo">
                </templat-form>
            </el-scrollbar>
        </el-dialog>
        <el-dialog title="设置测评结果" :visible.sync="isShowSetEvaluation" width="800px" center top="8vh" @close="()=>{}">
            <StandrdCom @close-dialog="closeSetEvaluation" v-if="isShowSetEvaluation" :searchList="searchList"
                @del-evaluate-item="deleteEvaluationItem" @add-evaluate-item="addEvaluationInfo"
                @edit-evaluate-item="editEvaluationItem" :evaluation-total="evaluationGradeTotal"></StandrdCom>
        </el-dialog>

    </div>
</template>

<script>
import Sortable from 'sortablejs'
import {
    getFllowupCRFData, editFllowupCRFInfo, addFllowupCRFInfo,
    deleteFllowupCRFInfo, getFllowupFormDetail, addImportOrUpdate, saveBatchUpdateSortNum,
} from "@/api/crf/fllowupCRF";
import { addCRFTemplateInfo } from '@/api/crf/crf'
import { getTemplateDetail } from "@/api/tool/form"
import { recordTemplateList } from '@/api/info/config'

import {
    getEvaluationFollow, addEvaluationFollow, editEvaluationFollow, deleteEvaluationFollow
} from "@/api/base/evaluation"
import { recoverCode } from "@/utils/index"
import StandrdCom from '../../tool/standardCom/index'
import PreviewTemplate from '@/views/tool/build/previewForm/index'
import { formConf, inputComponents } from '@/utils/generator/config'
import Category from '@/views/crf/category/index'
import TemplatForm from '@/views/crf/template/index'

export default {
    name: "crfSettings",
    dicts: ["sys_yes_no"],
    components: { Category, TemplatForm, PreviewTemplate, StandrdCom },
    data() {
        return {
            formConf,
            inputComponents,
            // 遮罩层
            isLoadingFllowList: false,
            // 表格数据
            fllowCRFList: [],
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, medicalCode: '', medicalName: '', },
            followId: '',
            medicalFormInfo: { medicalName: '', formType: '' },
            addEditTitle: '添加', //弹框的标题
            rules: {
                medicalName: [{ required: true, message: '请输入名称', trigger: ['blur', 'change'] }],
            },
            isShowDrawer: false,
            isShowPreView: false, //是否显示预览
            tableHeight: 'calc(100vh - 420px)',
            previewFormData: {},
            drawingList: [],
            previewTitle: '预览',
            isShowcategory: false, //是否显示保存到模板的弹窗
            isShowTemplate: false, //显示模板弹窗
            categoryInfo: {}, //分类信息
            isLoadingTemplate: false,
            templateFormInfo: null,
            renderKey: new Date() + '',
            isLoadingRecordList: false, //加载顶部内容的loading
            recordList: [], //周期内容
            activeIndex: 0,
            isLoadingPreview: false, //预览的loading
            isShowAddTemplate: false, //是否显示新增模板弹窗
            saveTemplateName: '',
            isShowSetEvaluation: false,
            evaluationLibraryId: '',
            searchList: [],
            evaluationIsLoading: false,
            evaluationGradeTotal: '',
            tableRenderKey: 0,

        };
    },
    created() {
        this.getREcordList();
        this.getFllowupList()
        let query = this.$route.query;
        this.followId = query.followId;
    },
    activated() {
        this.getCRFTemplateList()
    },
    mounted() {
        this.initDragFun()
    },
    watch: {
        tableRenderKey(newVal, oldVal) {
            if (newVal != oldVal) {
                this.$nextTick(() => {
                    this.initDragFun()
                })
            }
        }
    },
    methods: {
        initDragFun() {
            const el = this.$refs.dragTable.$el.querySelectorAll(".el-table__body-wrapper > table > tbody")[0];
            Sortable.create(el, {
                handle: ".elTable_dragItem",
                animation: 150,
                delay: 0,
                onEnd: evt => {
                    this.fllowCRFList.forEach((item, index) => {
                        if (index == evt.oldIndex) {
                            item.sortNum = evt.newIndex
                        } else if (index > evt.oldIndex && index <= evt.newIndex) {
                            item.sortNum = index - 1
                        } else if (index >= evt.newIndex && index < evt.oldIndex) {
                            item.sortNum = index + 1
                        }
                    })

                    this.fllowCRFList.sort((a, b) => {
                        return a.sortNum - b.sortNum
                    })
                    this.tableRenderKey++
                }
            });
        },
        // 获取顶部显示的队列内容
        getREcordList() {
            this.isLoadingRecordList = true;
            const queryParams = {
                researchId: this.$route.query.researchId,
            }
            recordTemplateList(queryParams).then(response => {
                let resData = response.rows;
                resData.forEach((item, index) => {
                    item["num"] = index == 0 ?
                        Number(item.followTotal == null ? 0 : item.followTotal + 1) :
                        Number((!item.followTotal ?
                            1 : item.followTotal) + resData[index - 1]["num"]);
                    if (index >= 1) {
                        let descripInfo = item.followIntervalUnit == '1' ? '天' :
                            (item.followIntervalUnit == '2' ? '周' :
                                item.followIntervalUnit == '3' ? '月' : '年')
                        item.description = `${item.followInterval}${descripInfo}后`

                        item.followNameStr = '随访' + item.num
                    } else {
                        item.followNameStr = '基线'
                    }

                    if (item.followFrequency == '2' && item.followTotal != '0' || item
                        .followFrequency == '1' && (item.followTotal == '0' || item.followTotal ==
                            '1' || !item.followTotal)) {
                        item.frequencyType = '1'
                    } else {
                        item.frequencyType = '2'
                    }
                });

                this.activeIndex = resData.findIndex(f => f.followId == this.followId)
                // console.log('this.activeIndex', this.activeIndex)
                this.recordList = resData
                this.isLoadingRecordList = false;
            }).catch(error => {
                this.isLoadingRecordList = false;
                this.$message.error(error)
            })
        },
        // 获取模板列表
        getFllowupList() {
            this.isLoadingFllowList = true;
            let params = {
                ...this.queryParams,
                followId: this.$route.query.followId,
                parentId: 0
            }
            getFllowupCRFData(params).then(res => {
                let resData = res.rows;
                if (resData.length) {
                    resData.forEach((item, index) => {
                        if (item.medicalFormList && item.medicalFormList.length) {
                            item.medicalFormNum = item.medicalFormList.length
                        } else {
                            item.medicalFormNum = 0
                        }
                        item.sortNum = index
                    })
                    resData.sort((a, b) => {
                        return a.sortNum - b.sortNum
                    })
                }
                this.fllowCRFList = resData;
                this.isLoadingFllowList = false;
            }).catch(error => {
                this.isLoadingFllowList = false;
            })
        },
        // 打开新建模板
        createMedicalForm(event) {
            this.isShowDrawer = true;
            this.medicalFormInfo.formType = event;
            this.addEditTitle = event == 0 ? '添加新模块' : (event == 3 ? '添加自评量表' : (event == 4 ? '添加预警' : '添加'))
        },
        // 新增 修改 模板
        submitMedicalForm() {
            let params = {
                formSource: '1',
                ...this.medicalFormInfo,
                followId: this.followId
            }
            this.$refs.medicalFormInfo.validate((valid) => {
                if (valid) {
                    this.isLoadingFllowList = true;
                    if (!params.medicalCode) {
                        this.addMedicalFormInfo(params)
                    } else {
                        this.editFollowupParams(params)
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            })
        },
        // 新增
        addMedicalFormInfo(params) {
            if (this.fllowCRFList.length) {
                let medicalCodeStr = this.fllowCRFList[this.fllowCRFList.length - 1].medicalCode
                params.medicalCode = 'model' + recoverCode(medicalCodeStr.slice(5))
            } else {
                params.medicalCode = 'modela'
            }
            params.sortNum = this.fllowCRFList.length
            addFllowupCRFInfo(params).then(res => {
                if (res.code == 200) {
                    this.getFllowupList()
                    this.$modal.msgSuccess(`新增成功`)
                    this.isShowDrawer = false;
                } else {
                    this.$modal.msgWraning(res.msg)
                }
                this.isLoadingFllowList = false;
                this.closeDrawer()
            })
        },
        // 修改APP上是否折叠模块
        editFollowupParams(params) {
            editFllowupCRFInfo(params).then(res => {
                if (res.code == 200) {
                    this.getFllowupList()
                    this.$modal.msgSuccess(`修改成功`)
                    this.isShowDrawer = false;
                } else {
                    this.$modal.msgWraning(res.msg)
                }
                this.isLoadingFllowList = false;
                this.closeDrawer()
            })
        },
        // 编辑模板
        editTemplate(event) {
            this.isShowDrawer = true;
            this.medicalFormInfo = event;
            this.addEditTitle = event.formType == 0 ? '修改模块' : (event.formType == 3 ? '修改自评量表' :
                (event.formType == 4 ? '修改预警' : '修改'))
        },
        cancleMedicalForm() {
            this.closeDrawer()
            this.getFllowupList();
        },
        closeDrawer() {
            this.isShowDrawer = false;
            this.medicalFormInfo = {}
        },
        // 删除模板
        deleteMedicalInfo(event) {
            const id = event.medicalFormId;
            this.$confirm(`请确认是否删除“${event.medicalName}”?`, '提示', {
                type: 'warning'
            }).then(() => {
                deleteFllowupCRFInfo(id).then(response => {
                    this.getFllowupList()
                    this.$modal.msgSuccess(`删除成功`)
                })
            }).catch(error => { })
        },
        // 重置
        resetQuery() {
            this.$refs['queryForm'].resetFields();
            this.getFllowupList();
        },
        // 预览  
        previewItemTemplate(event) {
            this.renderKey = new Date() + '' + event.medicalCode
            this.previewTitle = event.medicalName + '预览'
            this.isLoadingPreview = true;
            this.drawingList = []
            getFllowupFormDetail(event.medicalFormId).then(res => {
                this.isShowPreView = true;
                let fields = [];
                if (res.code == 200) {
                    let resData = res.data;
                    fields = resData.medicalProperties && resData.medicalProperties.length ? resData
                        .medicalProperties.map((m,
                            index) => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                medicalProperties,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                renderKey: new Date().getTime() + '' + index,
                                ...mInfo,
                            }
                        }) : [];
                    let libraryFormList = this.recoverTemplateForms('medicalProperties', 'medicalFormList',
                        resData.medicalFormList);
                    if (libraryFormList.length) {
                        fields.push(...libraryFormList)
                    }
                    fields.sort((a, b) => {
                        return a.sortNum - b.sortNum
                    })
                }
                if (fields && fields.length) {
                    fields.forEach(item => {
                        item.children = item.formType == '2' ? null : item.children
                    })
                }
                this.drawingList = fields;
                this.isLoadingPreview = false
            })
        },
        // 处理基础表单
        recoverTemplateForms(properties, templateFormData, medicalFormList) {
            let list = [];
            if (medicalFormList && medicalFormList.length) {
                medicalFormList.forEach((item, index) => {
                    if (item.id) item.id = item.id.toString()
                    const {
                        attribute,
                        medicalProperties,
                        templateProperties,
                        templateForms,
                        medicalFormList,
                        ...itemInfo
                    } = item
                    let info = {
                        ...JSON.parse(item.attribute),
                        ...itemInfo,
                        children: [],
                        renderKey: index + '' + new Date().getTime(),
                    }
                    if (!item.attribute) {
                        info = {
                            ...this.inputComponents.find(f => f.labelType == 'form'),
                            ...info,
                            formCode: info.templateCode,
                        }
                    }
                    if (item[properties] && item[properties].length) {
                        info.children = item[properties].map(m => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                medicalProperties,
                                templateProperties,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                ...mInfo
                            }
                        });
                    }
                    let libraryFormList = this.recoverTemplateForms(properties, templateFormData, item[
                        templateFormData]);
                    if (libraryFormList.length) {
                        info.children.push(...libraryFormList)
                    }
                    if (info.children && info.children.length) {
                        info.children.sort((a, b) => {
                            return a.sortNum - b.sortNum
                        })
                    }
                    list.push(info)
                })
            }
            return list
        },
        // 增加条目
        addMedicalInfo(event) {
            // 0模块1标准项2拍照项3自评量表4预警填报    
            // ********  预警、自评量表暂缓  *****              
            this.$router.push({
                path: `/queueSettings/buildCRF/${event.medicalFormId}/${event.followId}/${event.medicalName}/queue`,
                query: {
                    formType: event.formType,
                    oldPath: encodeURIComponent(this.$route.fullPath)
                }
            })
        },
        // 显示选择分类
        addFollowupToTemplate() {
            this.isShowcategory = true;
        },
        // 选择分类
        setCategoryInfo(event) {
            this.categoryInfo = event;
            if (this.followId) {
                this.isShowAddTemplate = true
            } else {
                this.isShowTemplate = true;
            }
        },
        // 选择模板
        setTemplateInfo(event) {
            let params = {
                templateId: event.templateId,
                followId: this.followId
            }
            this.isLoadingTemplate = true;
            addImportOrUpdate(params).then(res => {
                this.isLoadingTemplate = false;
                this.isShowTemplate = false;
                this.isShowcategory = false;
                this.isShowPreView = false;
                this.templateFormInfo = null;
                this.$message.success('模板保存成功')
            })
        },
        // 预览
        previewTemplateInfo(event) {
            console.log('previewTemplateInfo', event)
            this.templateFormInfo = event;
            this.renderKey = new Date() + '' + event.medicalCode
            this.previewTitle = event.templateName + '预览'
            this.isLoadingPreview = true
            this.drawingList = []
            getTemplateDetail(event.templateId).then(res => {
                this.isShowPreView = true;
                let fields = [];
                if (res.code == 200) {
                    let resData = res.data;
                    fields = resData.templateProperties && resData.templateProperties.length ? resData
                        .templateProperties.map((m,
                            index) => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                templateProperties,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                renderKey: new Date().getTime() + '' + index,
                                ...mInfo,
                            }
                        }) : [];
                    let libraryFormList = this.recoverTemplateForms('templateProperties', 'templateForms',
                        resData.templateForms);
                    if (libraryFormList.length) {
                        fields.push(...libraryFormList)
                    }
                    fields.sort((a, b) => {
                        return a.sortNum - b.sortNum
                    })
                }
                this.drawingList = fields;
                this.isLoadingPreview = false
            })
        },
        formInfoChange(event) {
            let params = {
                formSource: '1',
                ...event,
                followId: this.followId
            }
            this.editFollowupParams(params)
        },
        // 返回到上一个页面
        gobackPrePage() {
            let params = {
                formSortVos: this.fllowCRFList.map(m => {
                    return {
                        medicalFormId: m.medicalFormId,
                        sortNum: m.sortNum
                    }
                })
            }
            saveBatchUpdateSortNum(params).then(res => {
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        },
        // 保存并添加到模板
        saveCategoryAndAdd() {
            if (!this.saveTemplateName) {
                this.$message.warning('请填写模板名称')
                return
            }
            let params = {
                templateName: this.saveTemplateName,
                followId: this.followId,
                templateCategoryId: this.categoryInfo.templateCategoryId,
                commonFlag: '0',
            }
            addCRFTemplateInfo(params, '0').then(res => {
                if (res.code == 200) {
                    this.addCRFTemplateList(res.data)
                } else {
                    this.$modal.msgWraning(res.msg)
                }
            })
        },
        // 添加到模板
        addCRFTemplateList(templateId) {
            let params = {
                followId: this.followId,
                templateId
            }
            this.isLoadingTemplate = true;
            addImportOrUpdate(params).then(res => {
                this.isLoadingTemplate = false;
                this.isShowAddTemplate = false;
                this.isShowcategory = false;
                this.isShowAddTemplate = false;
                this.$message.success('模板保存成功')
                this.gobackPrePage()
            })
        },
        // 
        cancelTemplateName() {
            this.isShowAddTemplate = false;
            this.saveTemplateName = ''
        },
        // 设置测评结果
        setResearch(event) {
            this.isShowSetEvaluation = true;
            this.evaluationLibraryId = event.medicalFormId;
            this.evaluationGradeTotal = event.scoresTotalVal || 0
            this.getEvaluationList()
        },
        // 关闭测评结果
        closeSetEvaluation() {
            this.isShowSetEvaluation = false;
        },
        // 获取测评结果列表
        getEvaluationList() {
            this.evaluationIsLoading = true;
            this.searchList = []
            getEvaluationFollow({
                medicalFormId: this.evaluationLibraryId
            }).then(response => {
                this.searchList = response.rows;
                this.evaluationIsLoading = false;
            }).catch(error => {
                this.evaluationIsLoading = false;
            })
        },
        // 删除 测评结果
        deleteEvaluationItem(event) {
            this.evaluationIsLoading = true;
            deleteEvaluationFollow(event).then(res => {
                this.getEvaluationList()
            })
        },
        // 新增 测评
        addEvaluationInfo(event) {
            let params = {
                ...event,
                medicalFormId: this.evaluationLibraryId
            }
            this.evaluationIsLoading = true;
            addEvaluationFollow(params).then(res => {
                this.getEvaluationList()
            }).catch(error => {
                this.evaluationIsLoading = false;
            })
        },
        // 编辑 测评
        editEvaluationItem(event) {
            this.evaluationIsLoading = true;
            editEvaluationFollow(event).then(response => {
                this.getEvaluationList()
            }).catch(error => {
                this.evaluationIsLoading = false
            })
        }
    },
};
</script>
<style lang='scss' scoped>
.template-container {
    background: #fff;
    padding: 10px 20px;
}

.page_title {
    margin-bottom: 15px;
    margin-left: 15px;
}

.form_btn {
    margin-bottom: 10px;
}

.bottom_btn {
    padding: 10px 0;
    text-align: center;

    .el-button + .el-button {
        margin-left: 20px;
    }
}

.medical_name {
    border: none;
    background: transparent;
}

.preview-scrollbar {
    height: 70vh;
}

::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
}

.preview-form {
    margin: 0 20px;
}

.category_content {
    height: 68vh;
}

.preview_formItem {
    padding: 10px;
    margin-bottom: 10px;
    border: 1px dashed #c0ccda;
    border-radius: 8px;
}

// 顶部
.record_list {
    padding: 25px 0 10px;
    width: 80%;
    margin: 0 auto;
    justify-content: center;

    ::v-deep .el-step {
        position: relative;
        max-width: 200px !important;

        .el-step__description {
            padding: 0;
            width: 80px;
            text-align: center;
            position: absolute;
            top: -10px;
            left: 50%;
            margin-left: -40px;
            color: #333;
            z-index: 11;
        }

        .el-step__head.is-finish .el-step__line {
            background-color: #1890ff;
        }

        .el-step__head {
            .el-step__icon {
                width: 130px;
                border: none;
                padding: 0 5px;

                .step_content {
                    width: 26px;
                    height: 26px;
                    line-height: 26px;
                    border: 1px #c0c4cc solid;
                    border-radius: 50%;
                }

                .lineDashed {
                    width: 65px;
                    height: 2px;
                    border-bottom: 2px dashed rgb(64 158 255);
                    border-color: #c0c4cc;
                    margin: 0 5px;
                }
            }

            &.is-finish {
                .el-step__line {
                    background-color: #1890ff;
                }

                .lineDashed {
                    border-color: rgb(64 158 255);
                }

                .step_content {
                    border-color: #1890ff;
                }
            }
        }

        .el-step__description.is-finish {
            color: #1890ff;
        }

        .step_title {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 150px;
            margin: 0 auto;

            .stemp_title_item {
                flex: 1;
            }

            .lineDashed {
                width: 50px;
            }
        }

        &.step_single {
            .el-step__head .el-step__icon {
                width: 50px;
            }

            .el-step__description {
                left: 0;
            }
        }

        &.active_step {
            .el-step__head {
                .el-step__line {
                    border-color: #1890ff;
                    background-color: #1890ff;
                }
            }
        }
    }
}

::v-deep .elTable_dragItem {
    cursor: all-scroll;
}
</style>