<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="标题" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:AppArticle:add']">新增</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['system:AppArticle:remove']">删除</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange"
            @row-click="clickRow" ref="selectTable" class="select_table_style">
            <el-table-column type="selection" width="45" align="center" />
            <el-table-column label="资讯图片" align="center" prop="image" width="100">
                <template slot-scope="scope">
                    <image-preview :src="scope.row.image||''" :width="50" :height="50" />
                </template>
            </el-table-column>
            <el-table-column label="标题" align="center" min-width="300" prop="title" />
            <el-table-column label="列表排序" width="80" align="center" prop="listOrderNum">
                <template slot-scope="scope">
                    {{scope.row.listOrderNum | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200" fixed="right" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" @click.stop="handleUpdate(scope.row)" class="ptb710"
                        v-hasPermi="['system:AppArticle:edit']">修改内容</el-button>
                    <el-button type="text" @click.stop="handleDelete(scope.row)" class="el-button-text_delete"
                        v-hasPermi="['system:AppArticle:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改【请填写功能名称】对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false"
            :close-on-press-escape="false" @close="cancel">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px" v-if="open">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入标题" ref="projectNameInput" />
                </el-form-item>
                <el-form-item label="文章图片" prop="image">
                    <image-upload :value="form.image" :limit="50" @input="imageUpload" />
                </el-form-item>
                <!-- <el-form-item label="视频路径" prop="videoUrl">
                    <el-input v-model="form.videoUrl" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="视频显示图">
                    <image-upload :value="form.videoImage" @input="videoImgUpload" />
                </el-form-item> -->
                <el-form-item label="列表排序" prop="listOrderNum">
                    <el-input v-model="form.listOrderNum" placeholder="请输入列表排序" />
                </el-form-item>
                <el-form-item label="资讯内容" prop="content">
                    <editor v-model="form.content" :min-height="192" class="my_editor" />
                </el-form-item>
                <!-- <el-form-item label="文章链接" prop="link">
                    <el-input v-model="form.link" placeholder="请输入文章链接" />
                </el-form-item> -->
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

export default {
    name: "Article",
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
            // 【请填写功能名称】表格数据
            articleList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, title: null, articleType: '2' },
            editor: null,
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
            },
        };
    },
    created() {
        this.getList();
    },
    mounted() { },
    methods: {
        /** 查询【请填写功能名称】列表 */
        getList() {
            this.loading = true;
            listInformationArticle(this.queryParams).then(response => {
                this.articleList = response.rows;
                this.total = response.total;
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
        // 视频显示图片上传
        videoImgUpload(videoImgURLS) {
            this.form.videoImage = videoImgURLS
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
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
            this.open = true;
            this.title = "添加APP资讯";
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
                this.title = "修改APP资讯";
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
                        });
                    } else {
                        addInformationArticle(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal.confirm('确认是否删除当前选择的资讯？').then(function () {
                return delInformationArticle(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
    }
};
</script>