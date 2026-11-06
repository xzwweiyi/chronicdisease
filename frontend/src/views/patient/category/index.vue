<template>
    <div class="template_form">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="60px" class="search_form">
            <el-form-item label="分类" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入分类" clearable style="width: 240px" @keyup.enter.native="getCategoryList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getCategoryList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <div v-loading="isLoadingManageList" class="templateList">
            <el-scrollbar class="templateList-scrollbar">
                <div class="card_list">
                    <el-card class="add_template" v-hasPermi="[permissionInfo.add]">
                        <div class="add_template_cont" @click="createCategory">
                            <i class="el-icon-folder-add"></i>
                            <p class="addBtn">创建新分类</p>
                        </div>
                    </el-card>
                    <template v-for="(item,index) in categoryList">
                        <el-card class="category_card" v-if="categoryList.length" :key="index">
                            <div class="category_content" @click="toCategoryTemplateList(item)">
                                <div class="el-icon-folder-opened file_img"></div>
                                <div class="item_right">
                                    <div class="display-flex category_name">
                                        <el-popover placement="bottom-start" width="200" trigger="hover" :disabled="item.name.length<16" :content="item.name">
                                            <p slot="reference" class="ellipsisTwo">{{item.name}}</p>
                                        </el-popover>
                                    </div>
                                </div>
                            </div>
                            <div class="card_btns" v-if="!followId">
                                <el-dropdown @command="(command) => handleCommand(command, item)" v-hasPermi="[permissionInfo.move]" v-if="userDeptId==100" class="customer_dropdown">
                                    <el-button type="text">移动</el-button>
                                    <el-dropdown-menu slot="dropdown">
                                        <el-dropdown-item command="repositionFile" v-hasPermi="[permissionInfo.move]">调整文件位置</el-dropdown-item>
                                        <el-dropdown-item command="departmentLibrary" v-if="commonFlag==1" v-hasPermi="['system:deptArticle:batchAdd']">添加到科室库</el-dropdown-item>
                                    </el-dropdown-menu>
                                </el-dropdown>
                                <el-button type="text" @click="editTemplate(item)" v-hasPermi="[permissionInfo.edit]">设置名称</el-button>
                                <el-button type="text" @click="deleteTemplate(item)" v-hasPermi="[permissionInfo.delete]" class="el-button-text_delete">删除</el-button>
                            </div>
                        </el-card>
                    </template>
                </div>
            </el-scrollbar>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getCategoryList" />
        </div>
        <el-dialog :title="categoryInfo&&categoryInfo.id?'编辑':'新增'" :visible.sync="isShowDrawer" :close="closeDrawer" center>
            <el-form :model="categoryInfo" ref="categoryInfo" label-width="120px" :rules="rules" v-if="isShowDrawer" class="add_question_form">
                <el-form-item label="分类名称" prop="name" required>
                    <el-input v-model="categoryInfo.name" placeholder="请输入分类名称" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addQuestionForm">确定</el-button>
                    <el-button @click="cancleQuestionForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <el-dialog :title="rowMoveDeptTitle" center :visible.sync="isShowMoveDialog" :close="closeDeptMoveDrawer" width="500px">
            <el-form v-if="isShowMoveDialog" class="add_question_form" inline>
                <el-form-item label="选择分类">
                    <el-cascader :options="deptChoseList" v-model="moveCategoryId" :props="cascaderProps" v-if="moveFileType=='departmentLibrary'" style="width:400px" popper-class="dept_cascader">
                        <template slot-scope="{ node, data }">
                            <span :class="!!data.highLight?'hight_label':''"> {{ node.label }}</span>
                        </template>
                    </el-cascader>
                    <el-cascader :options="deptChoseList" v-model="moveCategoryId" :props="cascaderProps" clearable v-else>
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
    getPatientCategoryData, addPatientCategoryInfo, editPatientCategoryInfo, deletePatientCategoryInfo,
    deptFilterTreeTree, highLightTree, addPatientDept, movePatientCategoryInfo
} from '@/api/patient/config'

export default {
    name: 'patientList',
    components: {},
    props: ['followId'],
    data() {
        return {
            isLoadingManageList: false,
            categoryList: [],
            // 总条数
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 11,
                // name: '',
            },
            templateIds: [],
            selectName: [],
            checkAll: false,
            isIndeterminate: false,
            isShowDrawer: false,
            //新增题库信息
            categoryInfo: {
                name: '',
            },
            rules: {
                name: [
                    { required: true, message: '请输入模板名称', trigger: ['blur', 'change'] },
                ],
            },
            currentDeptVal: [],
            deptChoseList: [],
            rowMoveDeptTitle: '调整文件位置',
            isShowMoveDialog: false,
            currentRowInfo: undefined,
            moveCategoryId: undefined,
            commonFlag: '1',
            permissionInfo: {
                add: 'system:category:add',
                edit: 'system:category:edit',
                delete: 'system:category:remove',
                move: 'system:article:fileMove',
            },
            cascaderProps: {
                label: 'name',
                value: 'id',
                checkStrictly: true,
                emitPath: false,
            },
            userDeptId: '',
            moveFileType: '',
        }
    },
    created() {
        let route = this.$route
        if (route.path == '/public/patientlibrary') {
            this.commonFlag = '1'
            this.permissionInfo.add = 'system:category:add'
            this.permissionInfo.edit = 'system:category:edit'
            this.permissionInfo.delete = 'system:category:remove'
            this.permissionInfo.move = 'system:article:fileMove'
        } else if (route.path == '/privately/patientlibrary') {
            this.commonFlag = '0'
            this.permissionInfo.add = 'systemDept:category:add'
            this.permissionInfo.edit = 'systemDept:category:edit'
            this.permissionInfo.delete = 'systemDept:category:remove'
            this.permissionInfo.move = 'system:deptArticle:fileMove'
        }
        this.userDeptId = this.$store.state.user.userInfo.deptId
        this.getCategoryList()
    },
    watch: {
        isShowDrawer(newVal) {
            if (!newVal) {
                this.categoryInfo = {}
            }
        },
    },
    mounted() { },
    methods: {
        // 获取模板列表
        getCategoryList() {
            this.isLoadingManageList = true
            this.templateIds = []
            this.queryParams.commonFlag = this.commonFlag
            getPatientCategoryData(this.queryParams)
                .then((res) => {
                    this.categoryList = res.rows
                    this.total = res.total
                    this.isLoadingManageList = false
                })
                .catch((error) => {
                    this.isLoadingManageList = false
                })
        },
        // 管理模板表单列表
        toCategoryTemplateList(event) {
            // let path = `/public/patient/category/list/1/${event.id}`
            // if (this.commonFlag == '0') {
            //     path = `/privately/patient/category/list/0/${event.id}`
            // }
            let path = `/public/patient/category/list/1/${event.id}/${encodeURIComponent(event.name)}`
            if (this.commonFlag == '0') {
                path = `/privately/patient/category/list/0/${event.id}/${encodeURIComponent(event.name)}`
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
            }).catch((error) => {
                this.isShowMoveDialog = false
            })
        },
        departmentLibrary(event) {
            this.currentRowInfo = event
            this.rowMoveDeptTitle = '添加到科室库'
            this.isShowMoveDialog = true
            this.moveCategoryId = undefined
            let params = {
                commonFlag: this.commonFlag,
                categoryId: this.currentRowInfo.id
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
        moveCategory() {
            let newCategoryId = this.moveCategoryId
            if (!newCategoryId) {
                this.$message.warning('请选择分类！')
                return
            }
            if (this.moveFileType == 'repositionFile') {
                let params = {
                    categoryId: this.currentRowInfo.id,
                    newCategoryId,
                }
                if (this.commonFlag == 0) {
                    params['deptId'] = this.currentRowInfo.deptId
                }
                movePatientCategoryInfo(params, this.commonFlag).then((res) => {
                    this.$message.success('文件移动成功')
                    this.isShowMoveDialog = false
                    this.getCategoryList()
                })
            } else if (this.moveFileType == 'departmentLibrary') {
                let params = {
                    commonCategoryId: this.currentRowInfo.id || undefined,
                    deptCategoryId: newCategoryId,
                }
                addPatientDept(params).then((res) => {
                    this.isShowMoveDialog = false
                    this.$message.success('添加科室库成功')
                    this.isShowMoveDialog = false
                    this.getCategoryList()
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
        // 编辑模板
        editTemplate(event) {
            this.isShowDrawer = true
            this.categoryInfo = {
                ...event,
            }
        },
        addQuestionForm() {
            this.$refs.categoryInfo.validate((valid) => {
                if (valid) {
                    this.isLoadingManageList = true
                    this.categoryInfo.commonFlag = this.commonFlag
                    if (!this.categoryInfo.id) {
                        addPatientCategoryInfo(this.categoryInfo, this.commonFlag).then((res) => {
                            if (res.code == 200) {
                                this.getCategoryList()
                                this.$modal.msgSuccess(`分类新增成功`)
                                this.isShowDrawer = false
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false
                        })
                    } else {
                        editPatientCategoryInfo(this.categoryInfo, this.commonFlag).then((res) => {
                            if (res.code == 200) {
                                this.getCategoryList()
                                this.$modal.msgSuccess(`分类修改成功`)
                                this.isShowDrawer = false
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false
                        })
                    }
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },
        // 删除模板
        deleteTemplate(event) {
            const id = event.id
            this.$confirm(`删除文件夹的同时删除文件夹下的内容，请确认是否删除?`, '提示', {
                type: 'warning',
            })
                .then(() => {
                    deletePatientCategoryInfo(id, this.commonFlag).then((response) => {
                        this.getCategoryList()
                        this.$modal.msgSuccess(`删除成功`)
                    })
                })
                .catch((error) => { })
        },
        // 新建模板
        createCategory() {
            this.isShowDrawer = true
        },
        // 多选框选中数据
        tableListSelectionChange(selection) {
            this.templateIds = selection.map((item) => item.templateFormId)
            this.selectName = selection.map((item) => item.name)
        },
        resetQuery() {
            this.$refs['queryForm'].resetFields()
            this.getCategoryList()
        },
        cancleQuestionForm() {
            this.closeDrawer()
        },
        closeDrawer() {
            this.isShowDrawer = false
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
.display-flex {
    display: flex;
}

.table-buttons {
    align-items: center;
    margin: 0 20px 10px;
}

.hight_label{
    color:#1890ff;
}

.templateList {
    margin: 20px -10px 0;

    .card_list {
        display: flex;
        flex-wrap: wrap;
        padding-bottom: 10px;

        .category_card {
            margin: 0 10px 20px;
            color: #333;

            .category_content {
                display: flex;
                align-items: center;
                padding: 15px 15px 10px;
                cursor: pointer;
            }

            .file_img {
                text-align: center;
                font-size: 50px;
                color: #555;
                display: flex;
                align-items: center;
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
                    padding: 10px;

                    & + .el-button {
                        border-left: 1px #ededed solid;
                    }
                }

                .customer_dropdown {
                    border-right: 1px #ededed solid;
                }
            }

            .category_name {
                width: 120px;
                line-height: 24px;
                flex-direction: column;
                justify-content: center;
                font-weight: bold;
            }

            .item_right {
                margin-left: 10px;
            }

            .category_text {
                color: #939497;
                line-height: 20px;
            }

            ::v-deep .el-card__body {
                padding: 0;
            }
        }

        .add_template {
            width: 212px;
            margin: 0 10px 20px;
            cursor: pointer;
            min-height: 100px;

            .add_template_cont {
                display: flex;
                align-items: center;
                justify-content: center;
                flex-direction: column;
                height: 100%;
                color: #409eff;
                margin-top: 5px;

                .el-icon-folder-add {
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

.template_form {
    background: #fff;
    padding: 20px;
    margin: 15px;
}

.add_question_form {
    .el-cascader {
        width: 260px;
    }
}
</style>