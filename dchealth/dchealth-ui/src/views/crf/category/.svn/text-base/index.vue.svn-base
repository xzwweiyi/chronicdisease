<template>
    <div class="template_form">
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="search_form">
            <el-form-item label="分类" prop="templateCategoryName">
                <el-input v-model="queryParams.templateCategoryName" placeholder="请输入分类" clearable style="width: 240px"
                    @keyup.enter.native="getCategoryList" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getCategoryList">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <div v-loading="isLoadingManageList" class="templateList">
            <el-scrollbar class="templateList-scrollbar">
                <div class="card_list">
                    <el-card class="add_template" v-if="dialogType!='import'" v-hasPermi="[permissionInfo.add]">
                        <div class="add_template_cont" @click="createCategory">
                            <i class="el-icon-folder-add"></i>
                            <p class="addBtn">新增</p>
                        </div>
                    </el-card>
                    <template v-for="(item,index) in categoryList">
                        <el-card class="category_card" v-if="categoryList.length" :key="index">
                            <div class="category_content">
                                <div class="el-icon-folder-opened file_img"></div>
                                <div class="item_right">
                                    <div class="display-flex category_name">
                                        <el-popover placement="bottom-start" width="300" trigger="hover"
                                            :disabled="item.templateCategoryName.length<15"
                                            :content="item.templateCategoryName">
                                            <p slot="reference" class="ellipsisTwo">{{item.templateCategoryName}}</p>
                                        </el-popover>
                                    </div>
                                    <p class="category_text">共{{item.templateNum}}个</p>
                                </div>
                            </div>
                            <div class="card_btns">
                                <el-button type="text" @click="toCategoryTemplateList(item)">
                                    {{dialogType=='export'?'添加到该文件夹':'查看详情'}}
                                </el-button>
                                <template v-if="!followId">
                                    <el-button type="text" @click="editTemplate(item)"
                                        v-hasPermi="[permissionInfo.edit]">修改名称</el-button>
                                    <el-button type="text" @click="deleteTemplate(item)"
                                        v-hasPermi="[permissionInfo.delete]" class="deleteBtn">删除</el-button>
                                </template>
                            </div>
                        </el-card>
                    </template>
                </div>
            </el-scrollbar>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize" @pagination="getCategoryList" />
        </div>
        <el-dialog :title="categoryInfo.templateCategoryId?'编辑':'新增'" :visible.sync="isShowDrawer" @close="closeDrawer"
            width="600px">
            <el-form :model="categoryInfo" ref="categoryInfo" label-width="120px" :rules="rules" v-if="isShowDrawer"
                class="add_question_form">
                <el-form-item label="分类名称" prop="templateCategoryName" required>
                    <el-input v-model="categoryInfo.templateCategoryName" placeholder="请输入模板名称" clearable />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addQuestionForm">确定</el-button>
                    <el-button @click="cancleQuestionForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { getCRFTemplateData, addCategoryInfo, editCategoryInfo, deleteCategoryInfo, } from '@/api/crf/crf'

export default {
    name: 'categoryList',
    components: {},
    props: ['followId', 'dialogType'],
    data() {
        return {
            isLoadingManageList: false,
            categoryList: [],
            // 总条数
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                templateCategoryName: '',
            },
            templateIds: [],
            selectName: [],
            checkAll: false,
            isIndeterminate: false,
            isShowDrawer: false,
            //新增题库信息
            categoryInfo: {
                templateCategoryName: '',
            },
            rules: {
                templateCategoryName: [{ required: true, message: '请输入模板名称', trigger: ['blur', 'change'] }],
            },
            commonFlag: '0',
            permissionInfo: {
                add: 'templateDept:category:add',
                edit: 'templateDept:category:edit',
                delete: 'templateDept:category:remove'
            }
        }
    },
    created() {
        let route = this.$route;
        if (route.path == '/public/categoryCRF') {
            this.commonFlag = '1'
            this.permissionInfo.add = 'template:category:add'
            this.permissionInfo.edit = 'template:category:edit'
            this.permissionInfo.delete = 'template:category:remove'
        } else if (route.path == '/privately/categoryCRF') {
            this.commonFlag = '0'
            this.permissionInfo.add = 'templateDept:category:add'
            this.permissionInfo.edit = 'templateDept:category:edit'
            this.permissionInfo.delete = 'templateDept:category:remove'
        }
    },
    watch: {
        isShowDrawer(newVal) {
            if (!newVal) {
                this.categoryInfo = {}
            }
        }

    },
    mounted() {
        this.getCategoryList()
    },
    methods: {
        // 获取模板列表
        getCategoryList() {
            this.isLoadingManageList = true;
            this.templateIds = []
            this.queryParams.commonFlag = this.commonFlag
            getCRFTemplateData(this.queryParams).then(res => {
                this.categoryList = res.rows;
                this.total = res.total
                this.isLoadingManageList = false;
            }).catch(error => {
                this.isLoadingManageList = false;
            })
        },
        // 管理模板表单列表
        toCategoryTemplateList(event) {
            if (this.followId) {
                // this.isShowTemplate = true;
                this.$emit('setcategoryInfo', event)
            } else {
                let path = `/public/CRF/categoryTemplate/1/${event.templateCategoryId}/${event.templateCategoryName}`
                if (this.commonFlag == '0') {
                    path = `/privately/CRF/categoryTemplate/0/${event.templateCategoryId}/${event.templateCategoryName}`
                }
                this.$router.push({
                    path,
                    query: {
                        commonFlag: this.commonFlag,
                    }
                });
            }
        },
        // 编辑模板
        editTemplate(event) {
            this.isShowDrawer = true;
            this.categoryInfo = {
                ...event
            };
        },
        addQuestionForm() {
            this.$refs.categoryInfo.validate((valid) => {
                if (valid) {
                    this.isLoadingManageList = true;
                    this.categoryInfo.commonFlag = this.commonFlag;
                    if (!this.categoryInfo.templateCategoryId) {
                        addCategoryInfo(this.categoryInfo, this.commonFlag).then(res => {
                            if (res.code == 200) {
                                this.getCategoryList()
                                this.$modal.msgSuccess(`CRF分类新增成功`)
                                this.isShowDrawer = false;
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false;
                        })
                    } else {
                        editCategoryInfo(this.categoryInfo, this.commonFlag).then(res => {
                            if (res.code == 200) {
                                this.getCategoryList()
                                this.$modal.msgSuccess(`CRF分类修改成功`)
                                this.isShowDrawer = false;
                            } else {
                                this.$modal.msgWraning(res.msg)
                            }
                            this.isLoadingManageList = false;
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
            const id = event.templateCategoryId;
            this.$confirm(`删除文件夹的同时删除文件夹下的内容，请确认是否删除?`, '提示', {
                type: 'warning'
            }).then(() => {
                deleteCategoryInfo(id, this.commonFlag).then(response => {
                    this.getCategoryList()
                    this.$modal.msgSuccess(`删除成功`)
                })
            }).catch(error => { })
        },
        // 新建模板
        createCategory() {
            this.isShowDrawer = true;
        },
        // 多选框选中数据
        tableListSelectionChange(selection) {
            this.templateIds = selection.map(item => item.templateFormId);
            this.selectName = selection.map(item => item.templateCategoryName);
        },
        resetQuery() {
            this.resetForm('queryForm')
            this.getCategoryList()
        },
        cancleQuestionForm() {
            this.closeDrawer()
        },
        closeDrawer() {
            this.isShowDrawer = false;
        },
    }
}
</script>

<style lang='scss' scoped>
.display-flex {
    display: flex;
}

.template_form {
    padding: 20px;
    margin: 15px;
    background: #fff;
}

.templateList {
    margin: 20px 0 0;

    .templateList-scrollbar {
        height: 50vh;
    }

    .deleteBtn {
        color: #ff9292;
    }

    .card_list {
        display: flex;
        flex-wrap: wrap;

        .category_card {
            margin: 0 1% 10px;
            color: #333;
            position: relative;

            .category_content {
                display: flex;
                padding: 12px 15px 10px;
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
                justify-content: space-around;
                border-top: 1px solid #e6ebf5;

                .el-button {
                    flex: 1;
                    margin: 0;
                    padding: 12px;

                    & + .el-button {
                        border-left: 1px solid #e6ebf5;
                    }
                }
            }

            .category_name {
                width: 120px;
                line-height: 23px;
                height: 50px;
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
            width: 220px;
            margin: 0 1% 10px;
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

.ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
}

.ellipsisTwo {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    /*行数*/
    -webkit-box-orient: vertical;
}
</style>