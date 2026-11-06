<template>
    <div class="">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"
            class="search_form">
            <el-form-item label="标题" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="articleList" height="380" ref="multipleTable"
            @selection-change="handleSelectionChange" @row-click="clickRow">
            <el-table-column type="selection" width="45" align="center" />
            <el-table-column label="资讯图片" align="center" prop="image" width="100">
                <template slot-scope="scope">
                    <image-preview :src="scope.row.image||''" :width="50" :height="50" />
                </template>
            </el-table-column>
            <el-table-column label="标题" align="center" min-width="300" prop="title" />
            <el-table-column label="排序" align="center" prop="listOrderNum">
                <template slot-scope="scope">
                    {{scope.row.listOrderNum | nullFormat}}
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <div class="dialogBtns">
            <el-button type="primary" @click="addInfoList">确定</el-button>
            <el-button @click="cancleList">关闭</el-button>
        </div>
    </div>
</template>

<script>
import { listInformationArticle } from "@/api/cms/article";
import { addInformation } from "@/api/cms/banner";

export default {
    name: "Article",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 【请填写功能名称】表格数据
            articleList: [],
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, title: null, articleType: '2', isShow: '1' },
            editor: null,
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                categoryId: [{ required: true, message: "栏目编号不能为空", trigger: "blur" }],
                title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
                delFlag: [{ required: true, message: "删除标记不能为空", trigger: "blur" }]
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
        },
        clickRow(row) {
            this.$refs.multipleTable.toggleRowSelection(row);
        },
        // 点确定按钮提交添加数据
        addInfoList() {
            this.loading = true;
            addInformation(this.ids.toString()).then(res => {
                this.loading = false;
                this.ids = ''
                this.$refs.multipleTable.clearSelection();
                let text = this.ids ? 'save' : ''
                this.$emit('cancleadd', text)
            })
        },
        // 返回列表页
        cancleList() {
            this.$emit('cancleadd', 'cancle')
        }
    }
};
</script>
<style scoped lang="scss">
::v-deep .el-select {
    width: 100%;
}

.app-container {
    padding-top: 0;
    padding-bottom: 0;
}

.dialogBtns {
    display: flex;
    justify-content: right;
    margin-top: 30px;
}
</style>