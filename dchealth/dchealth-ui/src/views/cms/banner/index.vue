<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            <el-form-item label="名称" prop="bannerName">
                <el-input v-model="queryParams.bannerName" placeholder="请输入名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['system:banner:add']">添加资讯</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:banner:remove']">删除</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="bannerList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <!-- <el-table-column label="id" width="55" align="center" prop="主键" /> -->
            <el-table-column label="图片地址" align="center" width="100" prop="imageUrl">
                <template slot-scope="scope">
                    <image-preview :src="scope.row.imageUrl||''" :width="50" :height="50" />
                </template>
            </el-table-column>
            <el-table-column label="名称" align="center" min-width="320" prop="bannerName" />
            <!-- <el-table-column label="链接地址" align="center" min-width="110" prop="link">
                <template slot-scope="scope">
                    {{scope.row.link | nullFormat}}
                </template>
            </el-table-column> -->
            <el-table-column label="排序" align="center" width="80" prop="orderNum" />
            <el-table-column label="操作" align="center" width="220" fixed="right" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" @click.stop="handleUpdate(scope.row)" class="ptb710" v-hasPermi="['system:banner:edit']">修改内容</el-button>
                    <el-button type="text" @click.stop="handleDelete(scope.row)" class="el-button-text_delete" v-hasPermi="['system:banner:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加资讯对话框 -->
        <el-dialog title="添加资讯" :visible.sync="openAddDialog" width="80%" :close-on-click-modal="false" :close-on-press-escape="false" top="10vh">
            <add-info @cancleadd="closeAdd"></add-info>
        </el-dialog>
        <!-- 修改轮播管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false" :close-on-press-escape="false" top="10vh">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="业务数据值" prop="type">
                    <el-select v-model="form.type">
                        <el-option v-for="(item,index) of articleTypeList" :key="index" :label="item.label" :value="item.value">{{item.label}}</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名称" prop="bannerName">
                    <el-input v-model="form.bannerName" placeholder="请输入名称" />
                </el-form-item>
                <el-form-item label="链接地址" prop="link">
                    <el-input v-model="form.link" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="图片地址" prop="imageUrl">
                    <image-upload :value="form.imageUrl||''" :limit="50" @input="imageUpload" />
                </el-form-item>
                <el-form-item label="排序" prop="orderNum">
                    <el-input v-model="form.orderNum" placeholder="请输入排序" />
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
import { listBanner, getBanner, delBanner, addBanner, updateBanner } from "@/api/cms/banner";
import AddInfo from "./addinfo";

export default {
    name: "Banner",
    components: { AddInfo },
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
            // 轮播管理表格数据
            bannerList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1, pageSize: 10, dataId: null, type: null, tenantId: null, positionType: null,
                bannerName: null, link: null, imageUrl: null, orderNum: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                type: [{ required: true, message: "类型 1资讯 2 真实研究项目  3 外网地址不能为空", trigger: "change" }],
            },
            articleTypeList: [
                { value: '1', label: '资讯' },
                { value: '2', label: '真实研究项目' },
                { value: '3', label: '外网地址' },
            ],
            openAddDialog: false,
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询轮播管理列表 */
        getList() {
            this.loading = true;
            listBanner(this.queryParams).then(response => {
                this.bannerList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 上传文章显示图片
        imageUpload(res) {
            this.form.imageUrl = res;
        },
        // 表单重置
        reset() {
            this.form = {
                id: null,
                dataId: null,
                type: null,
                tenantId: null,
                positionType: null,
                bannerName: null,
                link: null,
                imageUrl: null,
                orderNum: null,
                remark: null,
                createTime: null,
                updateTime: null
            };
            this.resetForm("form");
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
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.openAddDialog = true;
            this.title = "选择资讯";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getBanner(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改轮播管理";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateBanner(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addBanner(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除轮播管理编号为"' + ids + '"的数据项？').then(function () {
                return delBanner(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/banner/export', {
                ...this.queryParams
            }, `banner_${new Date().getTime()}.xlsx`)
        },
        closeAdd(event) {
            if (event == 'save') {
                this.$modal.msgSuccess("添加成功");
            }
            this.openAddDialog = false;
            this.getList();
        }
    }
};
</script>