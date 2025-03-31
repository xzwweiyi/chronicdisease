<template>
    <div class="template-container" v-loading="loading">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="标题" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="类型" prop="type">
                <el-select v-model="queryParams.type" ref="projectNameInput">
                    <el-option v-for="(item, index) of categoryTypeList" :key="index" :label="item.label" :value="item.value">
                        {{ item.label }}</el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-scrollbar class="template_list">
            <div class="mb8">
                <el-button type="primary" plain @click="createCategory" v-hasPermi="[permissionInfo.add]">创建子分类</el-button>
                <template v-if="commonFlag == '1'">
                    <el-button type="primary" plain @click="showCreateArticle('1')" v-hasPermi="[permissionInfo.artAdd]">创建新图文</el-button>
                    <el-button type="primary" plain v-hasPermi="[permissionInfo.quesAdd]" @click="openQuestionDialog(null)">创建新问卷</el-button>
                    <el-button type="primary" plain @click="showCreateArticle('3')" v-hasPermi="[permissionInfo.artAdd]">创建视频</el-button>
                </template>
                <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
            </div>
            <el-table :data="userList">
                <el-table-column label="标题" align="center" min-width="350" key="title" prop="title" :show-overflow-tooltip="true" />
                <el-table-column label="类型" align="center" key="type" width="100" prop="type" :show-overflow-tooltip="true">
                    <template slot-scope="scope">{{ scope.row.type | convertArticleType }}</template>
                </el-table-column>
                <el-table-column label="更新时间" align="center" key="updateTime" width="200" prop="updateTime" :show-overflow-tooltip="true">
                    <template slot-scope="scope">{{ scope.row.updateTimeStr || '--' }}</template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="330" fixed="right">
                    <template slot-scope="scope">
                        <template v-if="commonFlag == '1'">
                            <el-button type="text" @click="editDetailInfo(scope.row)" v-if="scope.row.type == '1'" v-hasPermi="[permissionInfo.artEdit]">编辑文章</el-button>
                            <el-button type="text" @click="editDetailInfo(scope.row)" v-if="scope.row.type == '3'" v-hasPermi="[permissionInfo.artEdit]">编辑视频</el-button>
                        </template>
                        <el-button type="text" @click="openQuestionDialog(scope.row)" v-hasPermi="[permissionInfo.quesEdit]" v-if="scope.row.type == '2'">修改名称</el-button>
                        <el-button type="text" @click="editTemplate(scope.row)" v-if="scope.row.type == '4'" v-hasPermi="[permissionInfo.edit]">修改名称</el-button>
                        <el-button type="text" @click="handleArticleDetail(scope.row)" v-if="scope.row.type == '1'" v-hasPermi="[permissionInfo.artDetails]">查看详情</el-button>
                        <el-button type="text" v-if="scope.row.type == '2'" v-hasPermi="[permissionInfo.disposeEvalLst]" @click="setResearch(scope.row)">设置测评结果</el-button>
                        <el-button type="text" v-if="scope.row.type == '2'" @click="editQuestionnaire(scope.row)" v-hasPermi="[permissionInfo.disposeQues]">配置问卷</el-button>
                        <!-- <el-button type="text" v-if="scope.row.type=='2'" @click="statisticsQuestionnaire(scope.row)"
                            v-hasPermi="['medical:record:list']">问卷统计
                        </el-button>-->
                        <el-button type="text" @click="toChildrenList(scope.row)" v-if="scope.row.type == '4'">查看详情</el-button>
                        <el-dropdown @command="(command) => handleCommand(command, scope.row)" v-hasPermi="[permissionInfo.move, permissionInfo.move]" class="customer_dropdown">
                            <el-button type="text">移动</el-button>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="repositionFile" v-hasPermi="[permissionInfo.move]">调整文件位置</el-dropdown-item>
                                <el-dropdown-item command="departmentLibrary" v-if="commonFlag == 1" v-hasPermi="[permissionInfo.move]">添加到科室库</el-dropdown-item>
                                <el-dropdown-item command="removeDepartment" v-if="commonFlag == 0" v-hasPermi="['system:deptArticle:batchRemove']">移除科室库</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                        <el-button type="text" class="el-button-text_delete" @click="handleDelete(scope.row)" v-if="scope.row.type != '4' && commonFlag == '1'" v-hasPermi="[permissionInfo.artDelete]">删除</el-button>
                        <el-button type="text" class="el-button-text_delete" @click="childCategoryDelete(scope.row)" v-else-if="scope.row.type == '4'" v-hasPermi="[permissionInfo.delete]">删除</el-button>
                        <el-button type="text" @click="addDeptLibrary(scope.row)" v-if="scope.row.type != '4' && scope.row.isJoin == '0' && commonFlag == '1'" v-hasPermi="['system:deptArticle:add']">加入科室库</el-button>
                        <el-button type="text" class="el-button-text_delete" @click="removeDArteptCategory(scope.row)" v-if="scope.row.type != '4' && scope.row.isJoin == '1'" v-hasPermi="['system:deptArticle:remove']">移除科室库</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </el-scrollbar>

        <!-- 添加或修改【请填写功能名称】对话框 -->
        <el-dialog :title="title" :visible.sync="isOpenArticleEdit" width="60%" @close="editAtricleCancel" class="cmElDialog" top="10vh">
            <el-scrollbar class="edit-scrollbar" ref="articleScroll" v-if="isOpenArticleEdit">
                <el-form ref="articleForm" :model="articleForm" :rules="articleRules" label-width="150px">
                    <el-form-item label="标题" prop="title">
                        <el-input v-model="articleForm.title" placeholder="请输入标题" />
                    </el-form-item>
                    <el-form-item label="分类" prop="tenantId">
                        <el-select v-model="articleForm.type" ref="projectNameInput" disabled>
                            <el-option v-for="(item, index) of categoryTypeList" :key="index" :label="item.label" :value="item.value">{{ item.label }}</el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文章图片" prop="image">
                        <image-upload :value="articleForm.image" :limit="1" @input="imageUpload" />
                    </el-form-item>
                    <el-form-item label="选择文章来源" prop="uploadType">
                        <el-radio-group v-model="articleForm.uploadType">
                            <el-radio label="1" v-if="articleForm.type == '1'">本地上传</el-radio>
                            <el-radio label="2">转发</el-radio>
                            <el-radio label="3" v-if="articleForm.type == '3'">云视频</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="转发连接" prop="forwardLink" v-if="articleForm.uploadType == '2'">
                        <el-input v-model="articleForm.forwardLink" placeholder="请输入转发链接" />
                    </el-form-item>
                    <el-form-item label="上传视频" prop="videoUrl" v-if="articleForm.uploadType == '3'">
                        <el-input v-model="articleForm.videoUrl" placeholder="请输入输入链接" />
                    </el-form-item>
                    <el-form-item label="文章内容" prop="content" v-if="articleForm.uploadType != 2">
                        <editor v-model="articleForm.content" :height="400" class="my_editor" />
                    </el-form-item>
                </el-form>
            </el-scrollbar>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="editAtricleForm">确 定</el-button>
                <el-button @click="editAtricleCancel">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 添加或修改问卷名称 -->
        <el-dialog :title="title" :visible.sync="isOpenQuestionnarieEdit" width="800px" class :close-on-click-modal="false" :close-on-press-escape="false" top="15vh">
            <el-form ref="questionForm" :model="questionForm" :rules="questionRules" label-width="100px" class="question_form">
                <el-form-item label="问卷名称" prop="medicalName" required>
                    <el-input v-model="questionForm.medicalName" placeholder="请输入问卷名称" clearable />
                </el-form-item>
                <el-form-item label="浏览图" prop="imageUrl" required>
                    <image-upload v-model="questionForm.imageUrl" :limit="1" />
                </el-form-item>
                <el-form-item label="说明" prop="description">
                    <el-input type="textarea" v-model="questionForm.description" placeholder="请输入说明" clearable />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="setQuestionnaire">确 定</el-button>
                <el-button @click="setQuestionCancel">取 消</el-button>
            </div>
        </el-dialog>
        <el-dialog title="设置测评结果" :visible.sync="isShowSetEvaluation" width="900px" center top="8vh" @close="() => { }">
            <StandrdCom @close-dialog="closeSetEvaluation" v-if="isShowSetEvaluation" :searchList="searchList" @del-evaluate-item="deleteEvaluationItem" @add-evaluate-item="addEvaluationInfo" @edit-evaluate-item="editEvaluationItem" :evaluation-total="evaluationGradeTotal" :permis-add="permissionInfo.disposeEvalAdd" :permis-edit="permissionInfo.disposeEvalEdit" :permis-delete="permissionInfo.disposeEvalDelete">
            </StandrdCom>
        </el-dialog>
        <!-- 患教文章详情详情 -->
        <el-dialog :title="articleContent.title" :visible.sync="articleContentOpen" width="70%" top="8vh">
            <div class="sketch_content" v-if="articleContentOpen">
                <div class="reveal-container" v-html="articleContent.content" v-if="articleContent.content"></div>
                <div class="qrcode_content" ref="qrCodeUrl" v-else-if="articleContent.forwardLink" id="printStyle"></div>
                <el-empty v-else></el-empty>
            </div>
        </el-dialog>
        <!-- 创建子分类 -->
        <el-dialog :title="categoryInfo && categoryInfo.id ? '编辑' : '新增'" :visible.sync="isShowCategoryDrawer" :close="closeDrawer">
            <el-form :model="categoryInfo" ref="categoryInfo" label-width="120px" :rules="categoryRules" v-if="isShowCategoryDrawer" class="add_question_form">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="categoryInfo.name" placeholder="请输入分类名称" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addQuestionForm">确定</el-button>
                    <el-button @click="cancleQuestionForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 加入科室库 -->
        <el-dialog :visible.sync="isShowAddDeptCategory" :close="closeDeptDrawer" center>
            <el-form label-width="150px" v-if="isShowAddDeptCategory" class="add_question_form">
                <el-form-item label="选择分类" prop="name">
                    <el-cascader :options="deptCategoryOptions" ref="cascader" :show-all-levels="false" v-model="addDeptCategoryId" :props="cascaderProps" @change="cascaderChange"></el-cascader>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addDeptCategoryForm">确定</el-button>
                    <el-button @click="cancleDeptCategoryForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 移动 -->
        <el-dialog :title="rowMoveDeptTitle" center :visible.sync="isShowMoveDialog" :close="closeDeptMoveDrawer" width="60%">
            <el-form v-if="isShowMoveDialog" class="add_question_form" inline>
                <el-form-item label="选择分类" label-width="100px">
                    <el-cascader :options="deptChoseList" v-model="moveCategoryId" :props="cascaderProps" v-if="moveFileType=='departmentLibrary'" style="width:400px" popper-class="dept_cascader">
                        <template slot-scope="{ node, data }">
                            <span :class="!!data.highLight?'hight_label':''"> {{ node.label }}</span>
                        </template>
                    </el-cascader>
                    <el-cascader :options="deptChoseList" v-model="moveCategoryId" :props="cascaderProps" clearable v-else style="width:400px">
                    </el-cascader>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button type="primary" @click="moveCategory">确定</el-button>
                <el-button @click="closeDeptMoveDrawer">取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    delPatientArticle,
    updateQuestionnaireName, addPatientCategoryInfo, editPatientCategoryInfo,
    addQuestionnaire, addDeptCategoryOptions, addDeptCategory, removeDeptCategory,
    deletePatientCategoryInfo, listPatientArticle, addArticle, updateArticle, getArticleDetail,
    deptFilterTreeTree, movePatientCategoryInfo, removePatientDept, addPatientDept, highLightTree
} from '@/api/patient/config'

import StandrdCom from '../../tool/standardCom/index'
import { getEvaluationCmsArticle, addEvaluationCmsArticle, editEvaluationCmsArticle, deleteEvaluationCmsArticle } from '@/api/base/evaluation'
import QRCode from 'qrcodejs2'
export default {
    name: 'User',
    components: {
        StandrdCom,
    },
    data() {
        return {
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 弹出层标题
            title: '',
            // 是否显示弹出层
            isOpenArticleEdit: false,
            // 表单参数
            articleForm: {
                type: '1',
                articleType: '1',
            },
            positionType: '2',
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                title: undefined, // 标题
                type: undefined, // 类型
                positionType: '2',
                categoryId: '',
                articleType: '1',
            },
            userList: [], //患教列表
            // 表单校验
            articleRules: {
                title: [
                    { required: true, message: '名称不能为空', trigger: 'blur', },
                ],
                image: [
                    { required: true, message: '文章图片不能为空', trigger: 'change', },
                ],
                content: [
                    { required: true, message: '文章内容不能为空', trigger: 'change', },
                ],
                forwardLink: [
                    { required: true, message: '转发连接不能为空', trigger: 'change', },
                ],
                videoUrl: [
                    { required: true, message: '视频地址不能为空', trigger: 'change', },
                ],
                uploadType: [
                    { required: true, message: '选择文章来源', trigger: 'change', },
                ],
            },
            categoryTypeList: [
                { label: '文章', value: '1' },
                { label: '问卷', value: '2' },
                { label: '视频', value: '3' },
            ],
            renderkey: new Date() + '',
            questionForm: {
                medicalName: '',
                formSource: '2',
                categoryId: '',
                attribute: '',
                formType: '2',
                imageUrl: '',
            },
            questionRules: {
                medicalName: [
                    { required: true, message: '请输入问卷名称', trigger: ['blur', 'change'] },
                ],
                imageUrl: [
                    { required: true, message: '流览图不能为空', trigger: ['change'] },
                ],
            },
            isOpenQuestionnarieEdit: false,
            categoryId: '',
            isShowSetEvaluation: false,
            evaluationIsLoading: false,
            searchList: [],
            cmsArticleId: '',
            articleContent: {
                title: '',
            },
            articleContentOpen: false,
            evaluationGradeTotal: '',
            isShowCategoryDrawer: false,
            commonFlag: '1',
            parentPatientId: '0',
            moveCategoryId: undefined,
            //新增题库信息
            categoryInfo: {
                name: '',
            },
            userDeptId: '',
            moveFileType: '',
            categoryRules: {
                name: [
                    { required: true, message: '请输入子分类名称', trigger: ['blur', 'change'] },
                ],
            },
            isShowAddDeptCategory: false,
            addDeptCategoryId: '',
            deptCategoryOptions: [],
            currentArticleId: '',
            deptChoseList: [],
            rowMoveDeptTitle: '调整文件位置',
            isShowMoveDialog: false,
            currentRowInfo: undefined,
            deptHightTree: [],
            cascaderProps: {
                value: 'id',
                label: 'name',
                checkStrictly: true,
                emitPath: false,
                expandTrigger: 'hover',
            },
            permissionInfo: {
                add: 'system:category:add',
                edit: 'system:category:edit',
                delete: 'system:category:remove',
                quesAdd: 'template:form:add',
                quesEdit: 'template:form:edit',
                artAdd: 'system:article:add',
                artEdit: 'system:article:edit',
                artDelete: 'system:article:remove',
                artDetails: 'system:article:query',
                disposeQues: 'template:subject:add',
                disposeEvalLst: 'cms:cms_article_set_evaluation:list',
                disposeEvalAdd: 'cms:cms_article_set_evaluation:add',
                disposeEvalEdit: 'cms:cms_article_set_evaluation:edit',
                disposeEvalDelete: 'cms:cms_article_set_evaluation:delete',
                move: 'system:article:fileMove',
            },
        }
    },
    computed: {},
    watch: {
        isShowCategoryDrawer(newVal) {
            if (!newVal) {
                this.categoryInfo = {}
            }
        },
    },
    created() {
        let queryData = this.$route.query
        if (queryData) {
            this.commonFlag = queryData.commonFlag
            if (this.commonFlag == '1') {
                this.permissionInfo.add = 'system:category:add'
                this.permissionInfo.edit = 'system:category:edit'
                this.permissionInfo.delete = 'system:category:remove'
                this.permissionInfo.quesAdd = 'template:form:add'
                this.permissionInfo.quesEdit = 'template:form:edit'
                this.permissionInfo.artAdd = 'system:article:add'
                this.permissionInfo.artEdit = 'system:article:edit'
                this.permissionInfo.artDelete = 'system:article:remove'
                this.permissionInfo.artDetails = 'system:article:query'
                this.permissionInfo.disposeQues = 'template:subject:add'
                this.permissionInfo.disposeEvalLst = 'cms:cms_article_set_evaluation:list'
                this.permissionInfo.disposeEvalAdd = 'cms:cms_article_set_evaluation:add'
                this.permissionInfo.disposeEvalEdit = 'cms:cms_article_set_evaluation:edit'
                this.permissionInfo.disposeEvalDelete = 'cms:cms_article_set_evaluation:delete'
                this.permissionInfo.move = 'system:article:fileMove'
            } else {
                this.permissionInfo.add = 'systemDept:category:add'
                this.permissionInfo.edit = 'systemDept:category:edit'
                this.permissionInfo.delete = 'systemDept:category:remove'
                this.permissionInfo.quesAdd = 'templateDept:form:add'
                this.permissionInfo.quesEdit = 'templateDept:form:edit'
                this.permissionInfo.artAdd = 'systemDept:article:add'
                this.permissionInfo.artEdit = 'systemDept:article:edit'
                this.permissionInfo.artDelete = 'systemDept:article:remove'
                this.permissionInfo.artDetails = 'systemDept:article:query'
                this.permissionInfo.disposeQues = 'templateDept:subject:add'
                this.permissionInfo.disposeEvalLst = 'cmsDept:cms_article_set_evaluation:list'
                this.permissionInfo.disposeEvalAdd = 'cmsDept:cms_article_set_evaluation:add'
                this.permissionInfo.disposeEvalEdit = 'cmsDept:cms_article_set_evaluation:edit'
                this.permissionInfo.disposeEvalDelete = 'cmsDept:cms_article_set_evaluation:delete'
                this.permissionInfo.move = 'system:deptArticle:fileMove'
            }
            this.categoryId = queryData.id
            this.parentPatientId = queryData.parentId
        }
        this.getList()
    },
    methods: {
        /** 查询用户列表 */
        getList() {
            this.loading = true
            this.queryParams['categoryId'] = this.categoryId
            this.queryParams.commonFlag = this.commonFlag
            this.queryParams.parentId = this.parentPatientId
            listPatientArticle(this.queryParams).then((response) => {
                this.userList = response.rows
                this.total = response.total
                this.loading = false
            })
        },
        // 取消按钮
        editAtricleCancel() {
            this.$nextTick(() => {
                this.articleForm = {
                    title: null,
                    type: '1',
                    image: null,
                    categoryId: this.categoryId,
                    positionType: this.positionType,
                    content: null,
                    articleType: '1',
                }
            })
            this.isOpenArticleEdit = false
        },
        // 表单重置
        reset() {
            this.articleForm = {
                title: null,
                type: '1',
                image: null,
                categoryId: this.categoryId,
                positionType: this.positionType,
                content: null,
                articleType: '1',
            }

            this.resetForm('articleForm')
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1
            this.getList()
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.dateRange = []
            this.resetForm('queryForm')
            this.handleQuery()
        },
        /** 新增按钮操作 */
        showCreateArticle(type) {
            this.reset()
            this.isOpenArticleEdit = true
            this.articleForm.type = type
            this.title = type == '1' ? '添加文章' : type == '3' ? '添加视频' : '标题'
            this.$nextTick(() => {
                this.$refs['articleScroll'].wrap.scrollTop = 0
            })
        },
        /** 提交按钮 */
        editAtricleForm() {
            this.$refs['articleForm'].validate((valid) => {
                if (valid) {
                    if (this.articleForm.id != undefined) {
                        updateArticle(this.articleForm, this.commonFlag).then((response) => {
                            this.$modal.msgSuccess('修改成功')
                            this.isOpenArticleEdit = false
                            this.getList()
                        })
                    } else {
                        this.articleForm['categoryId'] = this.categoryId
                        this.articleForm['positionType'] = '2'
                        addArticle(this.articleForm, this.commonFlag).then((response) => {
                            this.$modal.msgSuccess('新增成功')
                            this.isOpenArticleEdit = false
                            this.getList()
                        })
                    }
                }
            })
        },
        // 上传文章显示图片
        imageUpload(res) {
            this.articleForm.image = res
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const userIds = row.id
            this.$confirm(`确认是否删除该${row.type == '1' ? '文章' : '问卷'}？`, '提示', {
                type: 'warning',
            })
                .then(() => {
                    delPatientArticle(userIds)
                        .then(() => {
                            this.getList()
                            this.$modal.msgSuccess('删除成功')
                        })
                        .catch(() => { })
                })
                .catch((error) => { })
        },
        // 编辑
        editDetailInfo(event) {
            this.reset()
            this.isOpenArticleEdit = true
            this.title = '编辑文章'
            this.articleForm = {
                ...event,
            }
            if (!event.articleType) this.articleForm['articleType'] = '1'
            this.$nextTick(() => {
                this.$refs['articleScroll'].wrap.scrollTop = 0
            })
        },
        // 创建问卷   患教库问卷调查   区别于CRF模板
        openQuestionDialog(event) {
            this.isOpenQuestionnarieEdit = true
            this.title = event && event.id ? '编辑' : '添加'
            this.questionForm.medicalName = ''
            if (this.$refs['questionForm']) {
                this.$refs['questionForm'].resetFields()
            }
            if (event) {
                this.questionForm = {
                    medicalName: event.title,
                    articleId: event.id,
                    categoryId: event.categoryId,
                    imageUrl: event.image,
                }
            }
        },
        //
        setQuestionCancel() {
            this.isOpenQuestionnarieEdit = false
            this.$refs['questionForm'].resetFields()
        },
        setQuestionnaire() {
            this.$refs['questionForm'].validate((valid) => {
                if (valid) {
                    if (this.questionForm.articleId != undefined) {
                        updateQuestionnaireName(this.questionForm, this.commonFlag).then((response) => {
                            this.$modal.msgSuccess('修改问卷成功')
                            this.isOpenQuestionnarieEdit = false
                            this.getList()
                        })
                    } else {
                        this.questionForm.categoryId = this.categoryId
                        addQuestionnaire(this.questionForm, this.commonFlag).then((response) => {
                            this.$modal.msgSuccess('新增问卷成功')
                            this.isOpenQuestionnarieEdit = false
                            this.getList()
                        })
                    }
                }
            })
        },
        // 配置问卷
        editQuestionnaire(row) {
            let pathUrl = `/patient/category/createForm/${this.commonFlag}/${row.id}/2`
            if (this.commonFlag == '0') {
                pathUrl = `/privately/patient/category/createForm/${this.commonFlag}/${row.id}/2`
            }
            this.$router.push({
                path: pathUrl,
                query: {
                    pageType: 'patient',
                    formType: '3',
                    oldPath: this.$route.fullPath,
                },
            })
        },
        /**
         * 问卷统计
         */
        statisticsQuestionnaire(row) {
            let pathUrl = `/patient/category/statistics/1`
            if (this.commonFlag == '0') {
                pathUrl = `/privately/patient/category/statistics/1`
            }
            this.$router.push({
                path: pathUrl,
                query: {
                    id: row.id,
                },
            })
        },
        // 设置测评结果
        setResearch(event) {
            this.isShowSetEvaluation = true
            this.evaluationGradeTotal = event.scoresTotalVal || 0
            this.cmsArticleId = event.id
            this.getEvaluationList()
        },
        // 关闭测评结果
        closeSetEvaluation() {
            this.isShowSetEvaluation = false
        },
        // 获取测评结果列表
        getEvaluationList() {
            this.evaluationIsLoading = true
            this.searchList = []
            getEvaluationCmsArticle(
                {
                    cmsArticleId: this.cmsArticleId,
                },
                this.commonFlag
            )
                .then((response) => {
                    this.searchList = response.rows
                    this.evaluationIsLoading = false
                })
                .catch((error) => {
                    this.evaluationIsLoading = false
                })
        },
        // 删除 测评结果
        deleteEvaluationItem(event) {
            this.evaluationIsLoading = true
            deleteEvaluationCmsArticle(event, this.commonFlag).then((res) => {
                this.getEvaluationList()
            })
        },
        // 新增 测评
        addEvaluationInfo(event) {
            let params = {
                ...event,
                cmsArticleId: this.cmsArticleId,
            }
            this.evaluationIsLoading = true
            addEvaluationCmsArticle(params, this.commonFlag)
                .then((res) => {
                    this.getEvaluationList()
                })
                .catch((error) => {
                    this.evaluationIsLoading = false
                })
        },
        // 编辑 测评
        editEvaluationItem(event) {
            this.evaluationIsLoading = true
            editEvaluationCmsArticle(event, this.commonFlag)
                .then((response) => {
                    this.getEvaluationList()
                })
                .catch((error) => {
                    this.evaluationIsLoading = false
                })
        },
        // 患教详情
        handleArticleDetail(event) {
            this.articleContentOpen = true
            this.articleContent = event
            if (event.type == '1' && event.uploadType == '1') {
                getArticleDetail(event.id).then((response) => {
                    this.articleContent = response.data
                }).catch((error) => {
                    this.articleContentOpen = false
                })
            } else if (event.forwardLink) {
                this.creatQrCodeOne(event)
            }
        },
        creatQrCodeOne(event) {
            let recordUrl = ''
            if (event.type == '1' && event.uploadType == '2') {
                recordUrl = event.forwardLink
            } else if (event.type == '3' && event.uploadType == '3') {
                recordUrl = event.forwardLink
            }
            setTimeout(() => {
                var qrcode = new QRCode(this.$refs.qrCodeUrl, {
                    text: `${recordUrl}`, // 需要转换为二维码的内容
                    width: 200,
                    height: 200,
                    colorDark: '#000000',
                    colorLight: '#ffffff',
                    correctLevel: QRCode.CorrectLevel.H,
                })
                qrcode._el.title = ''
            }, 0)
        },
        // 新建子分类
        createCategory() {
            this.isShowCategoryDrawer = true
        },
        // 编辑子分类
        editTemplate(event) {
            this.isShowCategoryDrawer = true
            this.categoryInfo = {
                ...event,
            }
            this.categoryInfo.name = event.title
            // this.categoryInfo.name = event.categoryName
        },
        // 新增确认子分类
        addQuestionForm() {
            this.$refs.categoryInfo.validate((valid) => {
                if (valid) {
                    this.loading = true
                    this.categoryInfo.commonFlag = this.commonFlag
                    this.categoryInfo.parentId = this.parentPatientId
                    if (!this.categoryInfo.id) {
                        addPatientCategoryInfo(this.categoryInfo, this.commonFlag).then((res) => {
                            if (res.code == 200) {
                                this.getList()
                                this.$modal.msgSuccess(`子分类新增成功`)
                                this.isShowCategoryDrawer = false
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.loading = false
                        })
                    } else {
                        editPatientCategoryInfo(this.categoryInfo, this.commonFlag).then((res) => {
                            if (res.code == 200) {
                                this.getList()
                                this.$modal.msgSuccess(`子分类修改成功`)
                                this.isShowCategoryDrawer = false
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.loading = false
                        })
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },
        cancleQuestionForm() {
            this.closeDrawer()
        },
        closeDrawer() {
            this.isShowCategoryDrawer = false
        },
        toChildrenList(event) {
            let path = `/public/patient/category/list/1/${event.id}/${encodeURIComponent(event.title)}`
            if (this.commonFlag == '0') {
                path = `/privately/patient/category/list/0/${event.id}/${encodeURIComponent(event.title)}`
            }
            this.$router.push({
                path,
                query: {
                    id: event.id,
                    positionType: event.positionType,
                    commonFlag: this.commonFlag,
                    parentId: event.id,
                },
            })
        },
        // 加入科室库
        addDeptLibrary(event) {
            this.isShowAddDeptCategory = true
            this.currentArticleId = event.id
            addDeptCategoryOptions().then((response) => {
                let resData = response.data
                if (resData && resData.length) {
                    resData = resData.map((m) => {
                        return {
                            ...m,
                            children: this.convertChildren(m.children),
                        }
                    })
                } else {
                }
                this.deptCategoryOptions = resData
            })
        },
        closeDeptDrawer() {
            this.isShowAddDeptCategory = false
        },
        convertChildren(list) {
            if (list && list.length) {
                list = list.map((m) => {
                    return {
                        ...m,
                        children: this.convertChildren(m.children),
                    }
                })
                return list
            } else {
                return undefined
            }
        },
        addDeptCategoryForm() {
            let params = {
                articleId: this.currentArticleId,
                categoryId: this.addDeptCategoryId,
            }
            addDeptCategory(params).then((res) => {
                this.$message.success('添加成功')
                this.getList()
                this.closeDeptDrawer()
            })
        },
        cancleDeptCategoryForm() {
            this.closeDeptDrawer()
        },
        cascaderChange() {
            this.$refs.cascader.toggleDropDownVisible()
        },
        removeDArteptCategory(event) {
            this.$modal
                .confirm(`确认是否从科室库移除该${event.type == '1' ? '文章' : '问卷'}？`)
                .then(function () {
                    return removeDeptCategory(event.id)
                })
                .then(() => {
                    this.getList()
                    this.$modal.msgSuccess('移除成功')
                })
                .catch(() => { })
        },
        childCategoryDelete(event) {
            const id = event.id
            const _this = this
            this.$confirm(`删除文件夹的同时删除文件夹下的内容，请确认是否删除?`, '提示', {
                type: 'warning',
            })
                .then(() => {
                    deletePatientCategoryInfo(id, _this.commonFlag).then((response) => {
                        this.getList()
                        this.$modal.msgSuccess(`删除成功`)
                    })
                })
                .catch((error) => { })
        },
        // 更多操作触发
        handleCommand(command, row) {
            switch (command) {
                case 'repositionFile':
                    this.moveFileType = 'repositionFile'
                    this.repositionFile(row)
                    break
                case 'departmentLibrary':
                    this.moveFileType = 'departmentLibrary'
                    this.departmentLibrary(row)
                    break
                case 'removeDepartment':
                    this.moveFileType = 'removeDepartment'
                    this.removeDepartment(row)
                default:
                    break
            }
        },
        repositionFile(event) {
            this.currentRowInfo = event
            this.rowMoveDeptTitle = '调整文件位置'
            this.isShowMoveDialog = true
            this.moveCategoryId = undefined
            let params = {
                commonFlag: this.commonFlag,
            }
            if (this.commonFlag == 0) {
                params['deptId'] = event.deptId
            }
            this.deptChoseList = []
            deptFilterTreeTree(params).then((res) => {
                if (res.data && res.data.length) {
                    this.deptChoseList = this.convertTreeChildren(res.data)
                }
            })
        },
        departmentLibrary(event) {
            this.currentRowInfo = event
            this.rowMoveDeptTitle = '添加到科室库'
            this.isShowMoveDialog = true
            this.moveCategoryId = undefined
            let type = this.currentRowInfo.type
            let params = {
                commonFlag: this.commonFlag,
            }
            if (type == '4') {
                params['categoryId'] = this.currentRowInfo.id
            } else {
                params['articleId'] = this.currentRowInfo.id
            }
            this.deptChoseList = []
            highLightTree(params).then(res => {
                let resData = res.data
                if (resData && resData.length) {
                    this.deptChoseList = resData.map(item => {
                        item['name'] = item.name ? item.name : item.label
                        item.children = item.cmsCategoryTreeList && item.cmsCategoryTreeList.length ?
                            this.convertTreeChildren(item.cmsCategoryTreeList) : undefined
                        return item
                    })
                }
            })

        },
        removeDepartment(event) {
            this.currentRowInfo = event
            this.$confirm(`请确认是否移除?`, '提示', {
                type: 'warning',
            })
                .then(() => {
                    let type = this.currentRowInfo.type
                    let params = {
                        deptId: type == '4' ? this.currentRowInfo.id : this.currentRowInfo.deptId,
                        articleId: type != '4' ? this.currentRowInfo.id : undefined,
                    }
                    removePatientDept(params).then((response) => {
                        this.getList()
                        this.$modal.msgSuccess(`移除成功`)
                    })
                })
                .catch((error) => { })
        },
        moveCategory() {
            let newCategoryId = this.moveCategoryId
            if (!newCategoryId) {
                this.$message.warning('请选择分类！')
                return
            }
            let type = this.currentRowInfo.type
            if (this.moveFileType == 'repositionFile') {
                let params = {
                    newCategoryId,
                }
                if (type == '4') {
                    params['categoryId'] = this.currentRowInfo.id
                } else {
                    params['articleId'] = this.currentRowInfo.id
                }
                if (this.commonFlag == 0) {
                    params['deptId'] = this.currentRowInfo.deptId
                }
                movePatientCategoryInfo(params, this.commonFlag).then((res) => {
                    this.$message.success('文件移动成功')
                    this.isShowMoveDialog = false
                    this.getList()
                })
            } else if (this.moveFileType == 'departmentLibrary') {
                let params = {
                    deptCategoryId: newCategoryId,
                }
                if (type == '4') {
                    params['commonCategoryId'] = this.currentRowInfo.id
                } else {
                    params['articleId'] = this.currentRowInfo.id
                }
                addPatientDept(params).then((res) => {
                    this.isShowMoveDialog = false
                    this.$message.success('添加科室库成功')
                    this.isShowMoveDialog = false
                    this.getList()
                })
            }
        },
        closeDeptMoveDrawer() {
            this.isShowMoveDialog = false
        },
        convertTreeChildren(array) {
            array.forEach((item) => {
                if (item.children && item.children.length) {
                    item.children = this.convertTreeChildren(item.children)
                } else {
                    item.children = undefined
                }
            })
            return array
        },
    },
}
</script>
<style lang='scss'>
.dept_cascader{
     .el-cascader-panel{
        .el-cascader-menu:nth-child(1){
            .el-radio{
                display: none;
            }
        }
    }
}
</style>
<style lang='scss' scoped>
.edit-scrollbar {
  height: 100%;

  .el-form {
    padding-right: 10px;
  }
}

.dialog-footer {
  text-align: center;
}

.template-container {
  background: #fff;
  padding: 20px;
  margin: 15px;

  .template_list {
    height: calc(100vh - 245px);
  }
}

.customer_dropdown {
  margin: 0 8px;
}

.sketch_content {
  overflow: auto;
  height: calc(92vh - 200px);

  ::v-deep p {
    max-width: 100vw;
  }

  ::v-deep img {
    max-width: 100%;
  }

  &::-webkit-scrollbar {
    width: 3px;
  }

  // 滚动条里面默认的小方块,自定义样式
  &::-webkit-scrollbar-thumb {
    background: #8798af;
    border-radius: 2px;
  }

  // 滚动条里面的轨道
  &::-webkit-scrollbar-track {
    background: transparent;
  }

  .qrcode_content {
    display: flex;
    justify-content: center;
  }
}
.hight_label{
    color:#1890ff;
}
</style>