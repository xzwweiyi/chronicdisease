<template>
    <div class="template_form">
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="search_form">
            <el-form-item label="CRF名称" prop="templateName">
                <el-input v-model="queryParams.templateName" placeholder="请输入模板名称" clearable style="width: 240px"
                    @keyup.enter.native="getMedicalFormList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getMedicalFormList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <div v-loading="isLoadingManageList" class="templateList">
            <el-scrollbar class="templateList-scrollbar">
                <div class="card_list">
                    <el-card class="add_template" shadow="hover" v-if="dialogType!='import'"
                        v-hasPermi="[permissionInfo.add]">
                        <div class="add_template_cont" @click="createTemplate">
                            <i class="el-icon-plus"></i>
                            <p class="addBtn">新增</p>
                        </div>
                    </el-card>
                    <template v-for="(item,index) in templateList">
                        <el-card class="category_card" :class="followId?'':'category_cursor'" :key="index"
                            v-if="templateList.length">
                            <div class="category_content">
                                <div class="template_Info">
                                    <el-popover placement="bottom-start" width="200" trigger="hover"
                                        :disabled="item.templateName&&item.templateName.length<10" :content="item.templateName">
                                        <h4 slot="reference" class="category_title">{{item.templateName}}</h4>
                                    </el-popover>
                                    <p class="category_text">模块数量：{{item.formNum}}个</p>
                                    <p class="category_text">项目数量：{{item.propertyNum}}条</p>
                                </div>
                                <div class="card_btns">
                                    <el-button type="text" v-hasPermi="['base:template:list']"
                                        @click="toEditDetail(item)">
                                        查看详情
                                    </el-button>
                                    <template v-if="!followId">
                                        <template v-if="item.isJoin!=null">
                                            <el-button type="text" @click="addDeptTemplate(item)"
                                                v-if="item.isJoin=='0'&&queryParams.commonFlag==1"
                                                v-hasPermi="['template:fDeptTemplate:add']">加入科室库</el-button>
                                            <el-button type="text" class="delete_btn" disabled
                                                v-if="item.isJoin=='1'&&queryParams.commonFlag==1">已移入</el-button>
                                        </template>
                                        <el-button type="text" @click="editTemplateInfo(item)"
                                            v-hasPermi="[permissionInfo.edit]">修改名称</el-button>
                                        <el-button type="text" @click="deleteTemplate(item)"
                                            v-hasPermi="[permissionInfo.delete]" class="deleteBtn">删除</el-button>
                                    </template>
                                    <template v-else-if="followId">
                                        <el-button type="text" @click="previewTemplate(item)"
                                            v-hasPermi="['template:f_template:query']">预览</el-button>
                                        <el-button type="text" @click="application(item)" class="deleteBtn">
                                            应用</el-button>
                                    </template>
                                </div>
                            </div>
                        </el-card>
                    </template>
                </div>
            </el-scrollbar>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize" @pagination="getMedicalFormList" />
        </div>
        <el-dialog :title="crfTemplateInfo.templateId?'编辑':'新增'" :visible.sync="isShowDrawer" width="700px">
            <el-form :model="crfTemplateInfo" ref="crfTemplateInfo" label-width="120px" :rules="rules"
                class="add_question_form" v-if="isShowDrawer">
                <el-form-item label="模板名称" prop="templateName" required>
                    <el-input v-model="crfTemplateInfo.templateName" placeholder="请输入模板名称" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="saveCRFTemplateInfo">确定</el-button>
                    <el-button @click="cancleQuestionForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="选择科室库分类" :visible.sync="isShowAddDept" width="700px">
            <el-form :model="crfTemplateInfo" ref="crfTemplateInfo" label-width="120px" class="add_question_form"
                v-if="isShowAddDept">
                <el-form-item label="科室库分类名称">
                    <el-select :loading="isLoadingManageList" v-model="selectDeptCagegoryId">
                        <el-option v-for="(item,index) in deptCategory" :key="index" :label="item.templateCategoryName"
                            :value="item.templateCategoryId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="saveAddDeptInfo">确定</el-button>
                    <el-button @click="cancleAddDeptInfo">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>

</template>

<script>
import {
    getCRFMedicalFormData, deleteCRFTemplateInfo, addCRFTemplateInfo, editCRFTemplateInfo, addCRFTemplateForDept,
    getCRFTemplateData,
} from '@/api/crf/crf'
export default {
    name: 'medical-form',
    components: {},
    props: ['followId', 'categoryInfo', 'dialogType', 'commonFlag'],
    data() {
        return {
            isLoadingManageList: false,
            templateList: [],
            // 总条数
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                templateName: '',
                templateCategoryId: ''
            },
            isShowDrawer: false,
            crfTemplateInfo: {
                templateId: '',
                templateName: '',
                templateType: '',
                shareType: '',
                sortNum: ''
            },
            rules: {
                templateName: [{ required: true, message: '请输入模板名称', trigger: ['blur', 'change'] }],
            },
            isShowAddDept: false,
            currentTemplateInfo: undefined,
            deptCategory: [],
            selectDeptCagegoryId: '',
            permissionInfo: {
                add: 'templateDept:f_template:add',
                edit: 'templateDept:f_template:edit',
                delete: 'templateDept:f_template:remove'
            }
        }
    },
    created() {
        let paramsData = this.$route.params;
        if (paramsData.id) {
            this.queryParams.templateCategoryId = paramsData.id;
            this.queryParams.commonFlag = paramsData.commonFlag
            if (paramsData.commonFlag == '1') {
                this.permissionInfo.add = 'template:f_template:add'
                this.permissionInfo.edit = 'template:f_template:edit'
                this.permissionInfo.delete = 'template:f_template:remove'
            } else if (paramsData.commonFlag == '0') {
                this.permissionInfo.add = 'templateDept:f_template:add'
                this.permissionInfo.edit = 'templateDept:f_template:edit'
                this.permissionInfo.delete = 'templateDept:f_template:remove'
            }
        } else {
            this.queryParams.templateCategoryId = this.categoryInfo.templateCategoryId
            this.queryParams.commonFlag = this.commonFlag
        }
        let route = this.$route;
        this.getMedicalFormList()
    },
    watch: {},
    mounted() { },
    methods: {
        // 获取模板列表
        getMedicalFormList() {
            this.isLoadingManageList = true;

            getCRFMedicalFormData(this.queryParams).then(res => {
                this.templateList = res.rows;
                this.total = res.total
                this.isLoadingManageList = false;
            }).catch(error => {
                this.isLoadingManageList = false;
            })
        },
        // 重置筛选项
        resetQuery() {
            this.resetForm('queryForm')
            this.getMedicalFormList()
        },
        // 新建模板
        createTemplate() {
            this.isShowDrawer = true;
        },
        // 取消
        cancleQuestionForm() {
            this.closeDrawer();
        },
        closeDrawer() {
            this.isShowDrawer = false;
            this.crfTemplateInfo = {}
        },
        // 保存
        saveCRFTemplateInfo() {
            let length = this.templateList.length
            this.crfTemplateInfo.templateCategoryId = this.queryParams.templateCategoryId;
            this.crfTemplateInfo.sortNum = length ? this.templateList[length - 1].sortNum + 1 : length;
            this.$refs.crfTemplateInfo.validate((valid) => {
                if (valid) {
                    this.isLoadingManageList = true;
                    this.crfTemplateInfo.commonFlag = this.queryParams.commonFlag;
                    if (!this.crfTemplateInfo.templateId) {
                        addCRFTemplateInfo(this.crfTemplateInfo, this.queryParams.commonFlag).then(res => {
                            if (res.code == 200) {
                                this.getMedicalFormList()
                                this.$modal.msgSuccess(`新增成功`)
                                this.isShowDrawer = false;
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false;
                            this.closeDrawer()
                        })
                    } else {
                        editCRFTemplateInfo(this.crfTemplateInfo, this.queryParams.commonFlag).then(res => {
                            if (res.code == 200) {
                                this.getMedicalFormList()
                                this.$modal.msgSuccess(`修改成功`)
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
        // 删除模板
        deleteTemplate(event) {
            const id = event.templateId;
            this.$confirm(`请确认要删除模板“${event.templateName}”吗?`, '提示', {
                type: 'warning'
            }).then(() => {
                this.isLoadingManageList = true;
                deleteCRFTemplateInfo(id, this.queryParams.commonFlag).then(response => {
                    this.getMedicalFormList()
                    this.$modal.msgSuccess(`删除成功`)
                }).catch(error => {
                    this.isLoadingManageList = false;
                })
            }).catch(error => {

            })
        },
        // 更新模板
        updateCRFTemplateList(event) {
            const id = event.templateId;
            this.$confirm(`请确认要更新${event.templateName}模板吗?`, '提示', {
                type: 'warning'
            }).then(() => {

            }).catch(error => { })
        },
        // 编辑模板信息
        editTemplateInfo(event) {
            this.isShowDrawer = true;
            this.crfTemplateInfo = { ...event };
        },
        // 去编辑页面
        toEditDetail(event) {
            if (this.followId) {
                this.$emit('toformdetail', event)
            } else {
                let path = `/public/CRF/categoryTemplate/detail/1/${event.templateId}/${event.templateName}`
                if (this.queryParams.commonFlag == '0') {
                    path = `/privately/CRF/categoryTemplate/detail/0/${event.templateId}/${event.templateName}`
                }
                this.$router.push({
                    path
                })
            }
        },
        // 预览
        previewTemplate(event) {
            this.$emit('previewInfo', event)
        },
        // 应用
        application(event) {
            this.$emit('templateInfo', event)
        },
        // 加入
        addDeptTemplate(event) {
            this.getCategoryList()
            this.selectDeptCagegoryId = ''
            this.isShowAddDept = true;
            this.currentTemplateInfo = event;
        },
        saveAddDeptInfo() {
            if (!this.selectDeptCagegoryId) {
                this.$modal.warning(`请选择科室库分类名称`)
                return
            }
            this.isLoadingManageList = true
            let params = {
                templateId: this.currentTemplateInfo.templateId,
                templateCategoryId: this.selectDeptCagegoryId
            }
            addCRFTemplateForDept(params).then(res => {
                this.getMedicalFormList()
                this.$modal.msgSuccess(`添加成功`)
                this.cancleAddDeptInfo()
                this.isLoadingManageList = false
            }).catch(error => {
                this.isLoadingManageList = false
            })
        },
        cancleAddDeptInfo() {
            this.isShowAddDept = false;
        },
        // 获取模板列表
        getCategoryList() {
            this.isLoadingManageList = true;
            let paramsData = {
                commonFlag: '0',
                pageNum: 1,
                pageSize: 999,
            }
            getCRFTemplateData(paramsData).then(res => {
                this.deptCategory = res.rows;
                this.isLoadingManageList = false;
            }).catch(error => {
                this.isLoadingManageList = false;
            })
        },
    }
}
</script>

<style lang='scss' scoped>
.display-flex {
    display: flex;
}

.table-buttons {
    align-items: center;
    margin: 0 20px 10px;
}

.template_form {
    background: #fff;
    padding: 20px;
    margin: 15px;
}

.templateList {
    margin-top: 20px;

    .deleteBtn {
        color: #ff9292;
    }

    .templateList-scrollbar {
        min-height: 48vh;
    }

    .card_list {
        display: flex;
        flex-wrap: wrap;

        .category_card {
            margin: 0 1% 10px 0;
            color: #333;
            position: relative;
            width: 220px;
            flex-shrink: 0;

            .category_text {
                color: #939497;
                line-height: 20px;
            }

            .category_title {
                margin-bottom: 5px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .template_Info {
                padding: 15px 13px 8px;
            }

            .card_btns {
                display: flex;
                align-items: center;
                justify-content: space-between;
                width: 100%;
                border-top: 1px #ededed solid;

                .el-button {
                    display: block;
                    margin: 0;
                    flex: 1;
                    padding: 10px 0;

                    & + .el-button {
                        border-left: 1px #ededed solid;
                    }
                }
            }

            ::v-deep .el-card__body {
                padding: 0;
            }
        }

        .add_template {
            width: 220px;
            margin: 0 1% 10px 0;
            cursor: pointer;
            min-height: 100px;

            .template_Info {
                cursor: pointer;
            }

            .add_template_cont {
                display: flex;
                align-items: center;
                justify-content: center;
                flex-direction: column;
                height: 100%;
                color: #409eff;
                margin-top: 5px;

                .el-icon-plus {
                    font-size: 30px;
                }
            }

            ::v-deep .el-card__body {
                padding: 0;
                height: 100%;
            }
        }
    }
}
</style>