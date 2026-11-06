<template>
    <div class="app-container edu-knowledge-container">
        <el-row :gutter="16">
            <!-- 左侧：宣教分类导航 -->
            <el-col :span="5">
                <div class="category-panel">
                    <div class="category-panel-header">
                        <i class="el-icon-collection" />
                        <span>宣教分类</span>
                    </div>
                    <div class="category-list">
                        <div class="category-item"
                            :class="{ active: queryParams.eduCategoryId == null || queryParams.eduCategoryId == '' }"
                            @click="selectCategory(null)">
                            <span class="category-name">全部宣教</span>
                            <span class="category-count">{{ allCount }}</span>
                        </div>
                        <div v-for="cat in categoryList" :key="cat.id" class="category-item"
                            :class="{ active: queryParams.eduCategoryId == cat.id }"
                            @click="selectCategory(cat.id)">
                            <span class="category-name">{{ cat.name }}</span>
                            <span class="category-count">{{ cat.articleCount || 0 }}</span>
                        </div>
                    </div>
                    <div class="category-panel-footer">
                        <el-button type="text" icon="el-icon-setting" @click="goCategoryManage"
                            v-hasPermi="['cms:eduCategory:list']">分类管理</el-button>
                    </div>
                </div>
            </el-col>

            <!-- 右侧：宣教知识列表 -->
            <el-col :span="19">
                <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
                    <el-form-item label="标题" prop="title">
                        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="宣教分类" prop="eduCategoryId">
                        <el-select v-model="queryParams.eduCategoryId" placeholder="请选择宣教分类" clearable>
                            <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name"
                                :value="cat.id" />
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                    </el-form-item>
                </el-form>

                <el-row class="mb8">
                    <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                        v-hasPermi="['system:AppArticle:add']">新增宣教</el-button>
                    <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                        v-hasPermi="['system:AppArticle:remove']">删除</el-button>
                    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
                </el-row>

                <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange"
                    @row-click="clickRow" ref="selectTable" class="select_table_style">
                    <el-table-column type="selection" width="45" align="center" />
                    <el-table-column label="宣教图片" align="center" prop="image" width="100">
                        <template slot-scope="scope">
                            <image-preview :src="scope.row.image || ''" :width="50" :height="50" />
                        </template>
                    </el-table-column>
                    <el-table-column label="标题" align="center" min-width="220" prop="title" show-overflow-tooltip />
                    <el-table-column label="宣教分类" align="center" width="110" prop="eduCategoryName">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.eduCategoryName" type="success" effect="plain">
                                {{ scope.row.eduCategoryName }}
                            </el-tag>
                            <span v-else>--</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="列表排序" width="80" align="center" prop="listOrderNum">
                        <template slot-scope="scope">
                            {{ scope.row.listOrderNum | nullFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="更新时间" width="160" align="center" prop="updateTime">
                        <template slot-scope="scope">
                            <span>{{ parseTime(scope.row.updateTime) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="150" fixed="right"
                        class-name="small-padding fixed-width">
                        <template slot-scope="scope">
                            <el-button type="text" @click.stop="handleUpdate(scope.row)" class="ptb710"
                                v-hasPermi="['system:AppArticle:edit']">修改内容</el-button>
                            <el-button type="text" @click.stop="handleDelete(scope.row)" class="el-button-text_delete"
                                v-hasPermi="['system:AppArticle:remove']">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize" @pagination="getList" />
            </el-col>
        </el-row>

        <!-- 添加或修改宣教知识对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false"
            :close-on-press-escape="false" @close="cancel">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px" v-if="open">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入标题" ref="projectNameInput" />
                </el-form-item>
                <el-form-item label="宣教分类" prop="eduCategoryId">
                    <el-select v-model="form.eduCategoryId" placeholder="请选择宣教分类" style="width: 100%">
                        <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name"
                            :value="cat.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="宣教图片" prop="image">
                    <image-upload :value="form.image" :limit="50" @input="imageUpload" />
                </el-form-item>
                <el-form-item label="列表排序" prop="listOrderNum">
                    <el-input v-model="form.listOrderNum" placeholder="请输入列表排序" />
                </el-form-item>
                <el-form-item label="宣教内容" prop="content">
                    <editor v-model="form.content" :min-height="192" class="my_editor" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listInformationArticle, getInformationArticleDetail, delInformationArticle,
    addInformationArticle, updateInformationArticle
} from "@/api/cms/article";
import { listEduCategoryAll } from "@/api/cms/eduCategory";

export default {
    name: "EduKnowledge",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 全部宣教数量
            allCount: 0,
            // 宣教知识表格数据
            articleList: [],
            // 宣教分类列表
            categoryList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, title: null, eduCategoryId: null, articleType: '2' },
            editor: null,
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
                eduCategoryId: [{ required: true, message: "请选择宣教分类", trigger: "change" }],
            },
        };
    },
    created() {
        this.getCategoryList();
        this.getList();
    },
    mounted() { },
    methods: {
        /** 查询宣教分类列表（左侧导航） */
        getCategoryList() {
            listEduCategoryAll().then(response => {
                this.categoryList = response.data || [];
            });
        },
        /** 点击分类导航 */
        selectCategory(eduCategoryId) {
            this.queryParams.eduCategoryId = eduCategoryId;
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 跳转宣教分类管理 */
        goCategoryManage() {
            this.$router.push({ path: '/knowledge/eduCategory' });
        },
        /** 查询宣教知识列表 */
        getList() {
            this.loading = true;
            listInformationArticle(this.queryParams).then(response => {
                this.articleList = response.rows;
                this.total = response.total;
                if (this.queryParams.eduCategoryId == null || this.queryParams.eduCategoryId == '') {
                    this.allCount = response.total;
                }
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {};
            if (this.editor) this.editor.txt.clear()
            this.resetForm("form");
        },
        // 上传文章显示图片
        imageUpload(res) {
            this.form.image = res;
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.queryParams.eduCategoryId = null;
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            // 默认带入当前选中的宣教分类
            if (this.queryParams.eduCategoryId != null && this.queryParams.eduCategoryId !== '') {
                this.form.eduCategoryId = this.queryParams.eduCategoryId;
            }
            this.open = true;
            this.title = "新增宣教知识";
            this.$nextTick(() => {
                let inputDom = this.$refs.projectNameInput;
                inputDom.focus();
            })
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getInformationArticleDetail(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改宣教知识";
                this.$nextTick(() => {
                    let inputDom = this.$refs.projectNameInput;
                    inputDom.focus();
                })
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    this.form.positionType = 1
                    this.form.articleType = '2'
                    if (this.form.id != null) {
                        updateInformationArticle(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                            this.getCategoryList();
                        });
                    } else {
                        addInformationArticle(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                            this.getCategoryList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal.confirm('确认是否删除当前选择的宣教知识？').then(function () {
                return delInformationArticle(ids);
            }).then(() => {
                this.getList();
                this.getCategoryList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
    }
};
</script>

<style lang="scss" scoped>
.edu-knowledge-container {
    .category-panel {
        background: #fff;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        min-height: 520px;
        display: flex;
        flex-direction: column;

        .category-panel-header {
            height: 44px;
            line-height: 44px;
            padding: 0 16px;
            border-bottom: 1px solid #ebeef5;
            font-size: 15px;
            font-weight: 600;
            color: #303133;

            i {
                margin-right: 6px;
                color: #409eff;
            }
        }

        .category-list {
            flex: 1;
            padding: 8px 0;
            overflow-y: auto;

            .category-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 9px 16px;
                cursor: pointer;
                font-size: 13px;
                color: #606266;

                &:hover {
                    background: #ecf5ff;
                }

                &.active {
                    background: #409eff;
                    color: #fff;

                    .category-count {
                        color: #fff;
                    }
                }

                .category-count {
                    font-size: 12px;
                    color: #909399;
                }
            }
        }

        .category-panel-footer {
            height: 44px;
            line-height: 44px;
            padding: 0 16px;
            border-top: 1px solid #ebeef5;
            text-align: right;
        }
    }
}
</style>
