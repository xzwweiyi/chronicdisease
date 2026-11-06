<template>
    <div class="app-container">
        <div class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['cms:guestbook:add']">新增</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['cms:guestbook:remove']">删除</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['cms:guestbook:export']">导出</el-button> -->
            <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> -->
        </div>

        <el-table v-loading="loading" :data="guestbookList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center">
            </el-table-column>
            <el-table-column label="姓名" align="center" width="120" prop="userName">
                <template slot-scope="scope">
                    {{scope.row.userName | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="电话" align="center" width="120" prop="mobile">
                <template slot-scope="scope">
                    {{scope.row.mobile | nullFormat}}
                </template>
            </el-table-column>
            <!-- <el-table-column label="真实姓名" align="center" width="100" prop="realName">
                <template slot-scope="scope">
                    {{scope.row.realName | nullFormat}}
                </template>
            </el-table-column> -->
            <el-table-column label="留言内容" align="center" min-width="160" prop="content">
                <template slot-scope="scope">
                    <div class="reveal-container" v-html="scope.row.content"></div>
                </template>
            </el-table-column>
            <el-table-column label="删除标记" align="center" width="100" prop="delFlag">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.delFlag=='N'?'danger':''">{{scope.row.delFlag=='Y'?'是':"否"}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="160" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['cms:guestbook:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        class="el-button-text_delete" v-hasPermi="['cms:guestbook:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改【请填写功能名称】对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false"
            :close-on-press-escape="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="留言内容" prop="content">
                    <el-input type="textarea" v-model="form.content" :autosize="{ minRows: 3}" placeholder="请输入留言内容" />
                </el-form-item>
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="form.userName" placeholder="请输入用户ID" />
                </el-form-item>
                <el-form-item label="真实姓名" prop="realName">
                    <el-input v-model="form.realName" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="电话" prop="mobile">
                    <el-input v-model="form.mobile" placeholder="请输入电话" />
                </el-form-item>
                <el-form-item label="删除标记" prop="delFlag">
                    <el-select v-model="form.delFlag">
                        <el-option label="是" value="Y"></el-option>
                        <el-option label="否" value="N"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="照片" prop="image">
                    <image-upload :value="form.image" :limit="50" @input="imageUpload" />
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
import { listGuestbook, getGuestbook, delGuestbook, addGuestbook, updateGuestbook } from "@/api/cms/guestbook";

export default {
    name: "Guestbook",
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
            guestbookList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: { pageNum: 1, pageSize: 10, },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                content: [{ required: true, message: "留言内容不能为空", trigger: "blur" }]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询【请填写功能名称】列表 */
        getList() {
            this.loading = true;
            listGuestbook(this.queryParams).then(response => {
                this.guestbookList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 上传文章显示图片
        imageUpload(res) {
            this.form.guestImages = res.split(',');
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                id: null,
                content: null,
                accountId: null,
                userName: null,
                mobile: null,
                tenantId: null,
                createTime: null,
                delFlag: null,
                image: ''
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
            this.reset();
            this.open = true;
            this.title = "添加反馈";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getGuestbook(id).then(response => {
                let resData = response.data;
                if (resData.cmsGuestImagesList && resData.cmsGuestImagesList.length) {
                    resData.image = resData.cmsGuestImagesList.map(res => res.imageUrl).toString()
                }
                this.form = resData
                this.open = true;
                this.title = "修改反馈";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateGuestbook(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addGuestbook(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除反馈编号为"' + ids + '"的数据项？').then(function () {
                return delGuestbook(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.$modal.confirm('确定导出所有反馈管理吗？').then(() => {
                this.download('cms/guestbook/export', {
                    ...this.queryParams
                }, `guestbook_${new Date().getTime()}.xlsx`)
            }).catch(() => { });

        }
    }
};
</script>