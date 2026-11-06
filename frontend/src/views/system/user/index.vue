<template>
    <div class="app-container">
        <el-row :gutter="20">
            <!--科室数据-->
            <el-col :span="5" :xs="24">
                <div class="head-container">
                    <el-input v-model="deptName" placeholder="请输入科室名称" clearable prefix-icon="el-icon-search" style="margin-bottom: 20px" />
                </div>
                <el-scrollbar class="right_scrollbar">
                    <el-tree :data="deptOptions" :props="defaultProps" class="tree-line user_tree_line" :filter-node-method="filterNode" ref="tree" default-expand-all :indent="0" accordion @node-click="handleNodeClick">
                        <span class="custom-tree-node" slot-scope="{ data }">
                            <i class="el-icon-folder-opened"></i>
                            {{ data.label }}
                        </span>
                    </el-tree>
                </el-scrollbar>
            </el-col>
            <!--用户数据-->
            <el-col :span="19" :xs="24">
                <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form query_params">
                    <el-form-item label="账号" prop="userName">
                        <el-input v-model="queryParams.userName" placeholder="请输入账号" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="手机号码" prop="phonenumber">
                        <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable style="width: 240px" @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="状态" prop="status">
                        <el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 240px">
                            <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="创建时间">
                        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                    </el-form-item>
                </el-form>

                <el-row class="mb8">
                    <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>
                    <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate" v-hasPermi="['system:user:edit']">修改</el-button>
                    <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:user:remove']">删除</el-button>
                    <el-button type="info" plain icon="el-icon-upload2" @click="handleImport" v-hasPermi="['system:user:import']">导入</el-button>
                    <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                        v-hasPermi="['system:user:export']">导出</el-button> -->
                    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns">
                    </right-toolbar>
                </el-row>

                <el-table v-loading="loading" :data="userList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="50" align="center" />
                    <el-table-column label="账号" align="center" key="userName" prop="userName" width="130" v-if="columns[1].visible" />
                    <el-table-column label="姓名" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" min-width="150" />
                    <el-table-column label="归属科室" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible" :show-overflow-tooltip="true" min-width="180" />
                    <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" v-if="columns[4].visible" width="130" />
                    <el-table-column label="状态" width="75" align="center" key="status" v-if="columns[5].visible">
                        <template slot-scope="scope">
                            <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="190">
                        <template slot-scope="scope">
                            <span>{{ scope.row.createTime | dateFormat }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" align="center" width="280">
                        <template slot-scope="scope" v-if="scope.row.userId !== 1">
                            <el-button type="primary" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']" class="ptb710">修改</el-button>
                            <el-button type="danger" icon="el-icon-delete" @click.stop="handleDelete(scope.row)" v-hasPermi="['system:user:remove']" class="ptb710">删除</el-button>
                            <el-dropdown @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                                <span class="el-dropdown-link ptb710 back-more text-white border-radius3">
                                    <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item command="handleResetPwd" icon="el-icon-key" v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>
                                    <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check" v-hasPermi="['system:user:edit']">分配角色</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </template>
                    </el-table-column>
                </el-table>

                <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
            </el-col>
        </el-row>

        <!-- 添加或修改用户配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="800px">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="姓名" prop="nickName">
                            <el-input v-model="form.nickName" placeholder="请输入用户姓名" maxlength="30" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="归属科室" prop="deptId">
                            <Comtreeselect v-model="form.deptId" noResultsText="暂无结果" :options="deptOptions" :show-count="true" placeholder="请选择归属科室" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="手机号码" prop="phonenumber">
                            <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
                        </el-form-item>
                    </el-col>
                    <!-- <el-col :span="12">
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
                        </el-form-item>
                    </el-col> -->
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item v-if="form.userId == undefined" label="账号" prop="userName">
                            <el-input v-model="form.userName" placeholder="请输入账号" maxlength="30" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
                            <el-input v-model="form.password" placeholder="请输入用户密码" type="text" maxlength="20" auto-complete="new-password" show-password />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户性别" prop="sex">
                            <el-select v-model="form.sex" placeholder="请选择性别">
                                <el-option v-for="dict in dict.type.sys_user_sex" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <!--el-col :span="12">
                        <el-form-item label="出生日期" prop="birthday">
                            <el-date-picker v-model="form.birthday" type="date" value-format="yyyy-MM-dd" placeholder="选择出生日期">
                            </el-date-picker>
                        </el-form-item>
                    </el-col -->
                  <el-col :span="12">
                    <el-form-item label="身份证号" prop="idCardNo">
                      <el-input v-model="form.idCardNo" placeholder="请输入身份证号" maxlength="18" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="职称" prop="postId">
                            <el-select v-model="form.postId" placeholder="请选择职称">
                                <el-option v-for="item in postOptions" :key="item.postId" :label="item.postName" :value="item.postId" :disabled="item.status == 1"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="角色">
                            <el-select v-model="form.roleIds" multiple placeholder="请选择角色">
                                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId" :disabled="item.status == 1"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="状态">
                            <el-radio-group v-model="form.status">
                                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="查看科室全部队列" label-width="150px">
                            <el-radio-group v-model="form.isAlldeptResearch">
                                <el-radio :label="'1'">是</el-radio>
                                <el-radio :label="'0'">否</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="备注">
                            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 用户导入对话框 -->
        <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
            <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip text-center" slot="tip">
                    <div class="el-upload__tip" slot="tip">
                        <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
                    </div>
                    <span>仅允许导入xls、xlsx格式文件。</span>
                    <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
                </div>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus
} from "@/api/system/user";
import { getToken } from "@/utils/auth";
import { treeDeptSelectAll } from "@/api/system/dept";
import Comtreeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
    name: "User",
    dicts: ['sys_normal_disable', 'sys_user_sex'],
    components: {
        Comtreeselect
    },
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
            // 用户表格数据
            userList: null,
            // 弹出层标题
            title: "",
            // 科室树选项
            deptOptions: undefined,
            // 是否显示弹出层
            open: false,
            // 科室名称
            deptName: undefined,
            // 默认密码
            initPassword: undefined,
            // 日期范围
            dateRange: [],
            // 职称选项
            postOptions: [],
            // 角色选项
            roleOptions: [],
            // 表单参数
            form: { isAlldeptResearch: '0' },
            defaultProps: { children: "children", label: "label" },
            // 用户导入参数
            upload: {
                // 是否显示弹出层（用户导入）
                open: false,
                // 弹出层标题（用户导入）
                title: "",
                // 是否禁用上传
                isUploading: false,
                // 是否更新已经存在的用户数据
                updateSupport: 0,
                // 设置上传的请求头部
                headers: {
                    Authorization: "Bearer " + getToken()
                },
                // 上传的地址
                url: process.env.VUE_APP_BASE_API + "/system/user/importData"
            },
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                userName: undefined,
                phonenumber: undefined,
                status: undefined,
                deptId: undefined
            },
            // 列信息
            columns: [
                { key: 0, label: `用户编号`, visible: true },
                { key: 1, label: `账号`, visible: true },
                { key: 2, label: `姓名`, visible: true },
                { key: 3, label: `科室`, visible: true },
                { key: 4, label: `手机号码`, visible: true },
                { key: 5, label: `状态`, visible: true },
                { key: 6, label: `创建时间`, visible: true }
            ],
            // 表单校验
            rules: {
                userName: [
                    { required: true, message: "账号不能为空", trigger: "blur" },
                    { min: 2, max: 20, message: '账号长度必须介于 2 和 20 之间', trigger: 'blur' }
                ],
                nickName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
                deptId: [{ required: true, message: "归属部门不能为空", trigger: "blur" }],
                phonenumber: [{
                    required: true, pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
                    message: "请输入正确的手机号码", trigger: "blur"
                }],
                password: [
                    { required: true, message: "用户密码不能为空", trigger: "blur" },
                    { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
                ],
                sex: [{ required: true, message: "用户性别不能为空", trigger: "blur" }],
                postIds: [{ required: true, message: "用户职称不能为空", trigger: "blur" }],
                birthday: [{ required: true, message: "用户出生日期不能为空", trigger: "blur" }],
                idCardNo: [
                    { required: true, message: "身份证号不能为空", trigger: "blur" },
                    { pattern: /(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: "请输入合法的18位身份证号", trigger: "blur" }
                ],
                email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
            }
        };
    },
    watch: {
        // 根据名称筛科室树
        deptName(val) {
            this.$refs.tree.filter(val);
        }
    },
    created() {
        this.getList();
        this.getTreeselect();
        this.getConfigKey("sys.user.initPassword").then(response => {
            this.initPassword = response.msg;
        });
    },
    methods: {
        /** 查询用户列表 */
        getList() {
            this.loading = true;
            listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                this.userList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        /** 查询科室下拉树结构 */
        getTreeselect() {
            treeDeptSelectAll().then(response => {
                this.deptOptions = response.data;
            });
        },
        // 筛选节点
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        // 节点单击事件
        handleNodeClick(data) {
            this.queryParams.deptId = data.id;
            this.handleQuery();
        },
        // 用户状态修改
        handleStatusChange(row) {
            let text = row.status === "0" ? "启用" : "停用";
            this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function () {
                return changeUserStatus(row.userId, row.status);
            }).then(() => {
                this.$modal.msgSuccess(text + "成功");
            }).catch(function () {
                row.status = row.status === "0" ? "1" : "0";
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                userId: undefined,
                deptId: undefined,
                userName: undefined,
                nickName: undefined,
                password: undefined,
                phonenumber: undefined,
                email: undefined,
                sex: undefined,
                status: "0",
                remark: undefined,
                isAlldeptResearch: '0',
                postIds: [],
                roleIds: []
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
            this.dateRange = [];
            this.resetForm("queryForm");
            this.handleQuery();
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.userId);
            this.single = selection.length != 1;
            this.multiple = !selection.length;
        },
        // 更多操作触发
        handleCommand(command, row) {
            switch (command) {
                case "handleResetPwd":
                    this.handleResetPwd(row);
                    break;
                case "handleAuthRole":
                    this.handleAuthRole(row);
                    break;
                default:
                    break;
            }
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.getTreeselect();
            getUser().then(response => {
                this.postOptions = response.posts;
                this.roleOptions = response.roles;
                this.open = true;
                this.title = "添加用户";
                console.log(this.initPassword)
                this.form.password = this.initPassword;
            });
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            this.getTreeselect();
            const userId = row.userId || this.ids;
            getUser(userId).then(response => {
                this.form = response.data;
                this.postOptions = response.posts;
                this.roleOptions = response.roles;
                this.form.postIds = response.postIds;
                this.form.roleIds = response.roleIds;
                this.open = true;
                this.title = "修改用户";
                this.form.password = "";
            });
        },
        /** 重置密码按钮操作 */
        handleResetPwd(row) {
            this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnClickModal: false,
                inputPattern: /^.{5,20}$/,
                inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
            }).then(({
                value
            }) => {
                resetUserPwd(row.userId, value).then(response => {
                    this.$modal.msgSuccess("修改成功，新密码是：" + value);
                });
            }).catch(() => { });
        },
        /** 分配角色操作 */
        handleAuthRole: function (row) {
            const userId = row.userId;
            this.$router.push("/system/user-auth/role/" + userId);
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.userId != undefined) {
                        updateUser(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addUser(this.form).then(response => {
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
            const userIds = row.userId || this.ids;
            this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function () {
                return delUser(userIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/user/export', {
                ...this.queryParams
            }, `user_${new Date().getTime()}.xlsx`)
        },
        /** 导入按钮操作 */
        handleImport() {
            this.upload.title = "用户导入";
            this.upload.open = true;
        },
        /** 下载模板操作 */
        importTemplate() {
            this.download('system/user/importTemplate', {}, `user_template_${new Date().getTime()}.xlsx`)
        },
        // 文件上传中处理
        handleFileUploadProgress(event, file, fileList) {
            this.upload.isUploading = true;
        },
        // 文件上传成功处理
        handleFileSuccess(response, file, fileList) {
            this.upload.open = false;
            this.upload.isUploading = false;
            this.$refs.upload.clearFiles();
            this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
                response.msg + "</div>", "导入结果", {
                dangerouslyUseHTMLString: true
            });
            this.getList();
        },
        // 提交上传文件
        submitFileForm() {
            this.$refs.upload.submit();
        }
    }
};
</script>

<style lang="scss" scoped>
.right_scrollbar {
    height: calc(100vh - 220px);
}

.query_params {
    .el-input {
        width: 240px;
    }
}

.tree-line {
    .el-tree-node {
        position: relative;
        padding-left: 16px; // 缩进量
    }

    .el-tree-node__children {
        padding-left: 16px; // 缩进量
    }

    // 竖线
    .el-tree-node::before {
        content: "";
        height: 100%;
        width: 1px;
        position: absolute;
        left: -3px;
        top: -26px;
        border-width: 1px;
        border-left: 1px dashed #bbbbbb;
    }

    // 当前层最后一个节点的竖线高度固定
    .el-tree-node:last-child::before {
        height: 38px; // 可以自己调节到合适数值
    }

    // 横线
    .el-tree-node::after {
        content: "";
        width: 24px;
        height: 20px;
        position: absolute;
        left: -3px;
        top: 12px;
        border-width: 1px;
        border-top: 1px dashed #bbbbbb;
    }

    // 去掉最顶层的虚线，放最下面样式才不会被上面的覆盖了
    & > .el-tree-node::after {
        border-top: none;
    }

    & > .el-tree-node::before {
        border-left: none;
    }

    // 展开关闭的icon
    .el-tree-node__expand-icon {
        // font-size: 16px;
        // 叶子节点（无子节点）
        &.is-leaf {
            color: transparent;
            // display: none; // 也可以去掉
        }
    }
}
</style>