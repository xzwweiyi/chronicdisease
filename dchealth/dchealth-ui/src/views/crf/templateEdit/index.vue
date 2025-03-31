<template>
    <div class="template-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="search_form">
            <el-form-item label="CRF标题" prop="templateName">
                <el-input v-model="queryParams.templateName" placeholder="请输入CRF标题" clearable
                    @keyup.enter.native="getCRFTemplateList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getCRFTemplateList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <div class="table_list">
            <div class="form_btn">
                <div v-if="!followId">
                    <el-button type="primary" plain icon="el-icon-plus" @click="createMedicalForm(0)"
                        v-hasPermi="[permissionInfo.add]">添加新模块</el-button>
                    <el-button type="warning" plain icon="el-icon-plus" @click="createMedicalForm(3)"
                        v-hasPermi="[permissionInfo.add]">添加自评量表</el-button>
                    <el-button type="danger" plain icon="el-icon-plus" @click="createMedicalForm(4)"
                        v-hasPermi="[permissionInfo.add]">添加预警</el-button>
                </div>
                <div v-if="followId">
                    <el-button :disabled="!templateFormsId.length" @click="importSelectTemplate">
                        导入所选模块
                    </el-button>
                </div>
            </div>
            <el-table v-loading="isLoadingFllowList" :data="fllowCRFList" @row-click="clickRow" ref="selectTable" class="select_table_style"
                @selection-change="selectionChange">
                <el-table-column type="selection" width="55" v-if="dialogType=='import'" />
                <el-table-column label="CRF标题" align="center" prop="templateName">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.formType=='4'?'danger':(scope.row.formType=='3'?'warning':'')"
                            effect="plain" class="medical_name">
                            {{ scope.row.templateName }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="已添加条目数" align="center" prop="propertyNum" />
                <el-table-column label="操作" align="center" width="420">
                    <template slot-scope="scope">
                        <el-button type="text" @click.stop="addMedicalInfo(scope.row)" v-if="!followId"
                            v-hasPermi="[permissionInfo.add]">添加条目
                        </el-button>
                        <el-button type="text" v-if="scope.row.formType=='3'&&!followId"
                            v-hasPermi="[permissionInfo.disposeEvalLst]" @click.stop="setResearch(scope.row)">设置测评结果
                        </el-button>
                        <el-button type="text" @click.stop="previewTemplateInfo(scope.row)">预览</el-button>
                        <el-button type="text" @click.stop="editTemplate(scope.row)" v-hasPermi="[permissionInfo.edit]"
                            v-if="!followId">修改</el-button>
                        <el-button type="text" @click.stop="deleteMedicalInfo(scope.row)"
                            v-hasPermi="[permissionInfo.delete]" v-if="!followId" class="delete_btn">删除</el-button>
                        <el-button type="text" @click.stop="application(scope.row)" v-if="followId">应用
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog :title="addEditTitle" :visible.sync="isShowDrawer" :close="closeDrawer" destroy-on-close>
            <el-form :model="medicalFormInfo" ref="medicalFormInfo" label-width="120px" :rules="rules"
                class="add_question_form">
                <el-form-item label="名称" prop="templateName" required>
                    <el-input v-model="medicalFormInfo.templateName" placeholder="请输入名称" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addMedicalFormInfo">确定</el-button>
                    <el-button @click="cancleMedicalForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog :visible.sync="isShowPreView" :title="previewTitle" top="8vh" @close="closePreviewDrawer"
            destroy-on-close append-to-body>
            <el-scrollbar class="preview-scrollbar" v-loading="isLoadingPreview">
                <Preview-template v-if="isShowPreView&&drawingList.length" :preview-form-data="drawingList"
                    :key="renderKey" class="preview_container_form" />
                <el-empty v-else class="no_content" description="暂无内容"></el-empty>
            </el-scrollbar>
        </el-dialog>

        <el-dialog title="设置测评结果" :visible.sync="isShowSetEvaluation" width="900px" center top="8vh" @close="()=>{}">
            <StandrdCom @close-dialog="closeSetEvaluation" v-if="isShowSetEvaluation" :searchList="searchList"
                @del-evaluate-item="deleteEvaluationItem" @add-evaluate-item="addEvaluationInfo"
                @edit-evaluate-item="editEvaluationItem" :evaluation-total="evaluationGradeTotal"
                :permis-add="permissionInfo.disposeEvalAdd" :permis-edit="permissionInfo.disposeEvalEdit"
                :permis-delete="permissionInfo.disposeEvalDelete"></StandrdCom>
        </el-dialog>
    </div>
</template>

<script>
import {
    getTemplateFormData, addTemplateFormInfo, editTemplateFormData,
    deleteTemplateFormInfo, getTemplateFormDetail
} from "@/api/tool/form";
import {
    getEvaluationTemplate, addEvaluationTemplate, editEvaluationTemplate, deleteEvaluationTemplate
} from "@/api/base/evaluation"
import { recoverCode } from "@/utils/index"
import PreviewTemplate from '@/views/tool/build/previewForm/index'
import { formConf } from '@/utils/generator/config'
import StandrdCom from '../../tool/standardCom/index'

export default {
    name: "categoryTemplate_detail",
    props: ['followId', 'templateFormId', 'dialogType'],
    components: {
        PreviewTemplate,
        StandrdCom
    },
    data() {
        return {
            formConf,
            // 遮罩层
            isLoadingFllowList: false,
            // 表格数据
            fllowCRFList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                templateCode: '',
                templateName: '',
                templateId: '',
            },
            templateId: '',
            medicalFormInfo: {
                templateName: '',
                formType: ''
            },
            addEditTitle: '添加', //弹框的标题
            rules: {
                templateName: [{ required: true, message: '请输入名称', trigger: ['blur', 'change'] }],
            },
            isShowDrawer: false,
            isShowPreView: false, //是否显示预览
            tableHeight: 'calc(100vh - 360px)',
            previewFormData: {},
            drawingList: [],
            previewTitle: '预览',
            renderKey: new Date() + '',
            templateFormsId: [],
            selectionTemplates: [],
            // 设置测评结果
            isShowSetEvaluation: false,
            evaluationTemplateId: '',
            evaluationIsLoading: false,
            searchList: [],
            evaluationGradeTotal: '',
            isLoadingPreview: false,
            permissionInfo: {
                add: 'base:template:add',
                edit: 'base:template:edit',
                delete: 'base:template:remove',
                disposeEvalLst: 'template:f_template_form_set_evaluation:list',
                disposeEvalAdd: 'template:f_template_form_set_evaluation:add',
                disposeEvalEdit: 'template:f_template_form_set_evaluation:edit',
                disposeEvalDelete: 'template:f_template_form_set_evaluation:delete',
            }
        };
    },
    created() {
        let paramsData = this.$route.params;
        this.tableHeight = this.templateFormId ? '50vh' : 'calc(100vh - 360px)'
        if (paramsData.templateId) {
            this.queryParams.templateId = paramsData.templateId;
            this.queryParams.commonFlag = paramsData.commonFlag
            if (paramsData.commonFlag == '1') {
                this.permissionInfo.add = 'base:template:add'
                this.permissionInfo.edit = 'base:template:edit'
                this.permissionInfo.delete = 'base:template:remove'
                this.permissionInfo.disposeEvalLst = 'template:f_template_form_set_evaluation:list'
                this.permissionInfo.disposeEvalAdd = 'template:f_template_form_set_evaluation:add'
                this.permissionInfo.disposeEvalEdit = 'template:f_template_form_set_evaluation:edit'
                this.permissionInfo.disposeEvalDelete = 'template:f_template_form_set_evaluation:delete'
            } else if (paramsData.commonFlag == '0') {
                this.permissionInfo.add = 'baseDept:template:add'
                this.permissionInfo.edit = 'baseDept:template:edit'
                this.permissionInfo.delete = 'baseDept:template:remove'
                this.permissionInfo.disposeEvalLst = 'templateDept:f_template_form_set_evaluation:list'
                this.permissionInfo.disposeEvalAdd = 'templateDept:f_template_form_set_evaluation:add'
                this.permissionInfo.disposeEvalEdit = 'templateDept:f_template_form_set_evaluation:edit'
                this.permissionInfo.disposeEvalDelete = 'templateDept:f_template_form_set_evaluation:delete'
            }
        } else {
            this.queryParams.templateId = this.templateFormId
            this.queryParams.commonFlag = this.commonFlag
        }
        this.getCRFTemplateList()
    },
    activated() {
        this.getCRFTemplateList()
    },
    methods: {
        // 获取模板列表
        getCRFTemplateList() {
            this.isLoadingFllowList = true;
            getTemplateFormData(this.queryParams).then(res => {
                let resData = res.rows;
                if (resData.length) {
                    resData.forEach(item => {
                        if (item.medicalFormList && item.medicalFormList.length) {
                            item.medicalFormNum = item.medicalFormList.length
                        } else {
                            item.medicalFormNum = 0
                        }
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
            this.addEditTitle = event == 0 ? '添加新模块' : (event == 3 ? '添加自评量表' :
                (event == 4 ? '添加预警' : '添加'))
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        selectionChange(event) {
            let ids = []
            if (event.length) ids = event.map(m => m.templateFormId)
            this.templateFormsId = ids;
            this.selectionTemplates = event
        },
        // 新增
        addMedicalFormInfo() {
            const {
                attribute,
                ...otherInfo
            } = this.medicalFormInfo;
            let info = {
                layout: 'rowFormItem',
                tagIcon: 'row',
                labelType: 'form',
                justify: 'start',
                align: 'top',
                formCode: 'formCode',
                isShowSubTitle: false, //副标题
                subTitle: '',
                span: 24,
                formName: '问题组',
                formSource: '1',
                ...otherInfo,
            }
            let params = {
                formSource: '1',
                ...this.medicalFormInfo,
                attribute: JSON.stringify(info),
                templateId: this.queryParams.templateId
            }
            this.$refs.medicalFormInfo.validate((valid) => {
                if (valid) {
                    this.isLoadingManageList = true;
                    if (!params.templateCode) {
                        if (this.fllowCRFList.length) {
                            let medicalCodeStr = this.fllowCRFList[this.fllowCRFList.length - 1]
                                .templateCode
                            params.templateCode = 'model' + recoverCode(medicalCodeStr.slice(5))
                        } else {
                            params.templateCode = 'modela'
                        }
                        addTemplateFormInfo(params, this.queryParams.commonFlag).then(res => {
                            if (res.code == 200) {
                                this.getCRFTemplateList()
                                this.$modal.msgSuccess(`${this.addEditTitle}新增成功`)
                                this.isShowDrawer = false;
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false;
                            this.closeDrawer()
                        })
                    } else {
                        editTemplateFormData(params, this.queryParams.commonFlag).then(res => {
                            if (res.code == 200) {
                                this.getCRFTemplateList()
                                this.$modal.msgSuccess(`${this.addEditTitle}修改成功`)
                                this.isShowDrawer = false;
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false;
                            this.closeDrawer()
                        })
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });

        },
        // 编辑模板
        editTemplate(event) {
            this.isShowDrawer = true;
            this.medicalFormInfo = {
                ...event
            };
            this.addEditTitle = event.formType == 0 ? '修改模块' : (event.formType == 3 ? '修改自评量表' :
                (event.formType == 4 ? '修改预警' : '修改'))
        },
        cancleMedicalForm() {
            this.closeDrawer()
        },
        closeDrawer() {
            this.isShowDrawer = false;
            this.medicalFormInfo = {}
        },
        closePreviewDrawer() {
            this.isShowPreView = false
        },
        // 删除模板
        deleteMedicalInfo(event) {
            const id = event.templateFormId;
            this.$confirm(`请确认是否删除“${event.templateName}”?`, '提示', {
                type: 'warning'
            }).then(() => {
                deleteTemplateFormInfo(id, this.queryParams.commonFlag).then(response => {
                    this.getCRFTemplateList()
                    this.$modal.msgSuccess(`删除成功`)
                })
            }).catch(error => { })
        },
        // 重置
        resetQuery() {
            this.resetForm('queryForm')
            this.getCRFTemplateList()
        },
        // 预览  
        previewTemplateInfo(event) {
            this.isLoadingPreview = true;
            this.isShowPreView = true;
            this.previewTitle = `“${event.templateName}” ` + '预览'
            getTemplateFormDetail(event.templateFormId).then(res => {
                let fields = [];
                if (res.code == 200) {
                    let resData = res.data;
                    fields = resData.templateProperties && resData.templateProperties.length ?
                        resData.templateProperties.map((m,
                            index) => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                renderKey: new Date().getTime() + '' + index,
                                ...mInfo,
                            }
                        }) : [];

                    let libraryFormList = this.recoverTemplateForms(resData.templateForms);
                    if (libraryFormList.length) {
                        fields.push(...libraryFormList)
                    }
                    fields.sort((a, b) => {
                        return a.sortNum - b.sortNum
                    })
                }
                this.drawingList = fields;
                setTimeout(() => {
                    this.isLoadingPreview = false
                }, 300)
                this.renderKey = new Date() + ''
            }).catch(error => {
                this.isLoadingPreview = false
            })
        },
        // 处理基础表单
        recoverTemplateForms(templateForms) {
            let list = [];
            if (templateForms && templateForms.length) {
                templateForms.forEach((item, index) => {
                    if (item.id) item.id = item.id.toString()
                    const {
                        attribute,
                        templateProperties,
                        templateForms,
                        ...itemInfo
                    } = item
                    let info = {
                        ...JSON.parse(item.attribute),
                        ...itemInfo,
                        children: [],
                        renderKey: index + '' + new Date().getTime(),
                    }
                    if (item.templateProperties && item.templateProperties.length) {
                        info.children = item.templateProperties.map(m => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                templateProperties,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                ...mInfo
                            }
                        });
                    }
                    let libraryFormList = this.recoverTemplateForms(item.templateForms);
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
            let params = this.$route.params
            let name = encodeURIComponent(event.templateName)
            let path = `/public/CRF/categoryTemplate/buildCRF/${event.templateFormId}/${name}/CRF`
            if (params.commonFlag == '0') {
                path = `/privately/CRF/categoryTemplate/buildCRF/${event.templateFormId}/${name}/CRF`
            }
            this.$router.push({
                path,
                query: {
                    formType: event.formType,
                    templateId: event.templateId,
                    templateName: params.templateName,
                    commonFlag: params.commonFlag,
                    oldPath: encodeURIComponent(this.$route.fullPath)
                }
            })
        },
        // 导入已选模块
        importSelectTemplate() {
            let params = {
                templateId: this.selectionTemplates[0].templateId,
                templateFormsIds: this.templateFormsId.toString()
            }
            this.application(params)
        },
        // 应用
        application(event) {
            let params = event
            if (!event.templateFormsIds) {
                params = {
                    templateId: event.templateId,
                    templateFormsIds: event.templateFormId
                }
            }
            this.$emit('applicationInfo', params)
        },

        // 设置测评结果
        setResearch(event) {
            this.evaluationTemplateId = event.templateFormId
            this.isShowSetEvaluation = true;
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
            getEvaluationTemplate({
                templateFormId: this.evaluationTemplateId
            }, this.queryParams.commonFlag).then(response => {
                this.searchList = response.rows;
                this.evaluationIsLoading = false;
            }).catch(error => {
                this.evaluationIsLoading = false;
            })
        },
        // 删除 测评结果
        deleteEvaluationItem(event) {
            this.evaluationIsLoading = true;
            deleteEvaluationTemplate(event, this.queryParams.commonFlag).then(res => {
                this.getEvaluationList()
            })
        },
        // 新增 测评
        addEvaluationInfo(event) {
            let params = {
                ...event,
                templateFormId: this.evaluationTemplateId
            }
            this.evaluationIsLoading = true;
            addEvaluationTemplate(params, this.queryParams.commonFlag).then(res => {
                this.getEvaluationList()
            }).catch(error => {
                this.evaluationIsLoading = false;
            })
        },
        // 编辑 测评
        editEvaluationItem(event) {
            this.evaluationIsLoading = true;
            editEvaluationTemplate(event, this.queryParams.commonFlag).then(response => {
                this.getEvaluationList()
            }).catch(error => {
                this.evaluationIsLoading = false
            })
        }
    }
};
</script>
<style lang='scss' scoped>
.delete_btn {
    color: #ff9292;
}

.template-container {
    background: #fff;
    margin: 15px;
    padding: 20px;
}

.page_title {
    margin-bottom: 15px;
    margin-left: 15px;
}

.form_btn {
    margin: 0 20px 10px 0;
}

.medical_name {
    border: none;
    background: transparent;
}

.bottom_btn {
    margin-top: 20px;
}

.preview-scrollbar {
    height: 65vh;
}

.preview-form {
    margin: 0 20px;
}
</style>