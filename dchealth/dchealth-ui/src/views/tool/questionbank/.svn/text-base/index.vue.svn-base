<template>
    <div class="list_container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="search_form">
            <!-- <el-form-item label="分类编码" prop="name">
                <el-input v-model="queryParams.formCode" placeholder="请输入分类编码" clearable style="width: 240px"
                    @keyup.enter.native="getTemplateList" />
            </el-form-item> -->
            <el-form-item label="分类名称" prop="host">
                <el-input v-model="queryParams.formName" placeholder="请输入分类名称" clearable style="width: 240px" @keyup.enter.native="getTemplateList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getTemplateList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <div class="table-buttons display-flex">
            <el-button icon="el-icon-delete" @click="deleteQuestionBanklate" type="danger" plain :disabled="!libraryId.length">删除</el-button>
            <el-button icon="el-icon-plus" style="margin-left:10px;" @click="createFormList" type="primary" v-hasPermi="['base:library:add']">
                新增</el-button>
        </div>
        <div v-loading="isLoadingManageList" class="templateList">
            <el-table :data="manageList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="tableListSelectionChange" highlight-current-row>
                <el-table-column type="selection" width="45" align="center" />
                <el-table-column label="排序" width="70" align="center" prop="sortNum"></el-table-column>
                <el-table-column label="分类名称" min-width="150" align="center" prop="formName"></el-table-column>
                <el-table-column label="分类编码" width="200" align="center" prop="formCode"></el-table-column>
                <el-table-column label="二级分类数" width="130" align="center" prop="childNums" v-if="!formLibraryId">
                </el-table-column>
                <el-table-column label="项数" min-width="80" align="center" prop="propertyNums" v-if="formLibraryId">
                </el-table-column>

                <el-table-column label="操作" align="center" width="250" fixed="right" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                        <el-button type="text" v-if="formLibraryId" @click.stop="previewItemTemplate(scope.row)">预览</el-button>
                        <el-button type="text" v-if="!formLibraryId" @click.stop="toNextList(scope.row)">查看详情
                        </el-button>
                        <el-button type="text" @click.stop="editQuestionBankForm(scope.row)" v-hasPermi="['base:library:query']" v-if="formLibraryId">修改内容</el-button>
                        <el-button type="text" @click.stop="editTemplat(scope.row)" v-hasPermi="['base:library:edit']">
                            修改名称</el-button>
                        <el-button type="text" class="el-button-text_delete" @click.stop="deleteQuestionBanklate(scope.row)" v-hasPermi="['base:library:remove']">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getTemplateList" />
        </div>
        <el-drawer :title="questionInfo.libraryId?'修改'+categoryTitle:'新增'+categoryTitle" :visible.sync="isShowDrawer" size="60%">
            <el-form :model="questionInfo" ref="questionInfo" label-width="180px" :rules="rules" class="add_question_form" v-if="isShowDrawer">
                <el-form-item label="分类编码" prop="formCode" required>
                    <el-input v-model="questionInfo.formCode" :disabled="!!questionInfo.libraryId" placeholder="请输入分类编码" clearable />
                </el-form-item>
                <el-form-item label="分类名称" prop="formName" required>
                    <el-input v-model="questionInfo.formName" placeholder="请输入分类名称" clearable />
                </el-form-item>
                <el-form-item label="排序" prop="sortNum" required>
                    <el-input v-model="questionInfo.sortNum" placeholder="请输入排序号" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addQuestionForm">确定</el-button>
                    <el-button @click="resetQuestionForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
        <el-drawer :visible.sync="isShowPreView" :title="previewTitle" top="8vh" destroy-on-close size="70%">
            <el-scrollbar class="preview-scrollbar" v-loading="isLoadingPreview">
                <Preview-template v-if="isShowPreView&&drawingList.length" :preview-form-data="drawingList" :key="renderKey" class="preview_container_form" />
                <el-empty v-else class="no_content"></el-empty>
            </el-scrollbar>
        </el-drawer>
    </div>

</template>

<script>
import {
    getLibraryFormList, deleteLibraryFormList, addLibraryFormList, editLibraryFormList,
    getChildFormAndProperty
} from '@/api/tool/form'
import PreviewTemplate from '@/views/tool/build/previewForm/index'
const fromDialogInfo = (() => {
    return {
        formCode: '',
        formName: '',
        parentId: 0,
        openImage: '0',
        openMultiple: '0',
        isDoctorShow: true, //是否显示在医生端
        isPatientShow: true, //是否显示在患者端
        attribute: '',
        formType: '1',
    }
})
export default {
    name: 'questionbank',
    components: { PreviewTemplate },
    data() {
        return {
            isLoadingManageList: false,
            manageList: [],
            // 总条数
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                formCode: '',
                formName: '',
                formType: '1',
            },
            libraryId: [],
            selectName: [],
            checkAll: false,
            isIndeterminate: false,
            isShowDrawer: false,
            //新增题库信息
            questionInfo: fromDialogInfo(),
            rules: {
                formCode: [
                    { required: true, message: '请输入分类编码', trigger: ['blur', 'change'] },
                    { pattern: /^[a-zA-Z0-9_]+$/, message: '请输入英文或者数字', trigger: ['blur', 'change'] }
                ],
                formName: [{ required: true, message: '请输入分类名称', trigger: ['blur', 'change'] }],
            },
            formLibraryId: '',
            categoryTitle: '分类',
            isShowPreView: false, //是否显示预览
            isLoadingPreview: false, //预览的loading
            drawingList: [],
            previewTitle: '预览',
        }
    },
    created() { },
    watch: {},
    mounted() {
        this.formLibraryId = this.$route.params.libraryId;
        this.getTemplateList()
    },
    methods: {
        // 获取模板列表
        getTemplateList() {
            this.isLoadingManageList = true;
            this.libraryId = []
            let params = this.queryParams
            if (this.formLibraryId) {
                params.parentId = this.formLibraryId;
                this.categoryTitle = '二级分类';
            } else {
                params.parentId = 0;
                this.categoryTitle = '一级分类';
            }
            getLibraryFormList(params).then(res => {
                this.manageList = res.rows;
                this.total = res.total
                this.isLoadingManageList = false;
            }).catch(error => {
                this.isLoadingManageList = false;
            })
        },
        resetQuery() {
            this.queryParams = {
                pageNum: 1,
                pageSize: 10,
                formType: '1',
            }
            this.getTemplateList()
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        tableListSelectionChange(selection) {
            this.libraryId = selection.map(item => item.libraryId);
            this.selectName = selection.map(item => item.formName);
        },
        // 修改编辑
        editTemplat(event) {
            this.isShowDrawer = true;
            this.questionInfo = event;
        },
        // 编辑分类内容
        editQuestionBankForm(event) {
            this.$router.push({
                path: '/entry/tool/build',
                query: {
                    pageType: 'formAllEdit',
                    libraryId: event.libraryId,
                    formType: '1',
                    oldPath: encodeURIComponent(this.$route.fullPath)
                }
            })
        },
        // 删除模板
        deleteQuestionBanklate(event) {
            const id = event.libraryId || this.libraryId.toString();
            const formName = event.formName || this.selectName.toString();
            let params = {
                libraryIds: id
            }
            this.$confirm(`确定要删除“${formName}”吗?`, '提示', {
                type: 'warning'
            }).then(() => {
                deleteLibraryFormList(params).then(response => {
                    this.getTemplateList()
                    this.$modal.msgSuccess(`分类“${formName}”删除成功`)
                })
            }).catch(error => {
                return false
            })
        },
        // 新建模板
        createFormList() {
            this.isShowDrawer = true;
            this.questionInfo = fromDialogInfo();
        },
        resetFormAttribute(params) {
            let formInfo = {
                layout: 'rowFormItem',
                isShowDom: true,
                tagIcon: 'row',
                labelType: 'form',
                span: 24,
                document: 'https://element.eleme.cn/#/zh-CN/component/layout'
            }
            if (!this.formLibraryId) {
                formInfo['isDoctorShow'] = true
                formInfo['isPatientShow'] = true
            }
            if (!params.libraryId) {
                return {
                    ...formInfo,
                    ...this.questionInfo
                }
            } else {
                let attribute = JSON.parse(params.attribute);
                attribute.labelType = 'form';
                params.attribute = attribute;
                return params.attribute
            }
        },
        addQuestionForm() {
            let params = this.questionInfo
            if (!this.formLibraryId) {
                params.parentId = '0'
            } else {
                params.parentId = this.formLibraryId
            }
            params.formType = '1'
            this.$refs.questionInfo.validate((valid) => {
                if (valid) {
                    this.formInfoChange(params)
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        // 列表上开启按钮change
        formInfoChange(params) {
            this.isLoadingManageList = true;
            params.attribute = JSON.stringify(this.resetFormAttribute(params))
            if (!params.libraryId) {
                addLibraryFormList(params).then(res => {
                    if (res.code == 200) {
                        this.getTemplateList()
                        this.$modal.msgSuccess(`分类新增成功`)
                        this.isShowDrawer = false;
                    } else {
                        this.$modal.msgWraning(res.msg)
                    }
                    this.isLoadingManageList = false;
                }).catch(error => {
                    this.isLoadingManageList = false;
                })
            } else {
                editLibraryFormList(params).then(res => {
                    if (res.code == 200) {
                        this.getTemplateList()
                        this.$modal.msgSuccess(`分类修改成功`)
                        this.isShowDrawer = false;
                    } else {
                        this.$modal.msgWraning(res.msg)
                    }
                    this.isLoadingManageList = false;
                }).catch(error => {
                    this.isLoadingManageList = false;
                })
            }

        },
        resetQuestionForm() {
            this.isShowDrawer = false;
            this.questionInfo = fromDialogInfo()
        },
        toNextList(event) {
            this.$router.push(`/entryItem/${event.libraryId}`)
        },

        // 预览  
        previewItemTemplate(event) {
            this.renderKey = new Date() + '' + event.formCode
            this.previewTitle = event.formName + '预览'
            this.isLoadingPreview = true;
            this.isShowPreView = true;
            this.drawingList = []
            let paramsData = {
                libraryId: event.libraryId
            }
            getChildFormAndProperty(paramsData).then(res => {
                let fields = [];
                if (res.code == 200) {
                    let resData = res.data;
                    fields = resData.libraryProperties && resData.libraryProperties.length ? resData
                        .libraryProperties.map((m,
                            index) => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                libraryProperties,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                renderKey: new Date().getTime() + '' + index,
                                ...mInfo,
                            }
                        }) : [];
                    let libraryFormList = this.recoverTemplateForms('libraryProperties', 'libraryForms',
                        resData.libraryForms);
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
            }).catch(error => {
                this.isLoadingPreview = false
            })
        },
        // 处理基础表单
        recoverTemplateForms(properties, templateFormData, libraryForms) {
            let list = [];
            if (libraryForms && libraryForms.length) {
                libraryForms.forEach((item, index) => {
                    if (item.id) item.id = item.id.toString()
                    const {
                        attribute,
                        libraryProperties,
                        templateProperties,
                        templateForms,
                        libraryForms,
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
                                libraryProperties,
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
    }
}
</script>

<style lang='scss' scoped>
.display-flex {
    display: flex;
}

.list_container {
    background: #fff;
    padding: 20px;
    margin: 15px;

    .table-buttons {
        align-items: center;
    }

    .templateList {
        padding-top: 10px;
    }
}

.add_question_form {
    margin-top: 0;
}

.delete_btn {
    color: #ff9292;
}
.preview-scrollbar {
    height: 100%;
    .preview_container_form{
        padding:0 30px;
    }
}
</style>