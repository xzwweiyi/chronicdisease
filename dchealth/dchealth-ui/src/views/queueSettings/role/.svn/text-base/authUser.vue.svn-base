<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="姓名" prop="nickName">
                <el-input v-model="queryParams.nickName" placeholder="请输入姓名" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="手机号码" prop="phonenumber">
                <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" :disabled="this.$route.query.researchStatus === '2'"
                @click="openSelectUser" v-hasPermi="['researh:role:update']">添加用户</el-button>
            <el-button type="danger" plain icon="el-icon-circle-close" :disabled="multiple" @click="cancelAuthUserAll"
                v-hasPermi="['system:role:delete']">批量取消授权</el-button>
            <el-button type="warning" plain icon="el-icon-close" @click="handleClose">关闭</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="姓名" prop="nickName" align="center" :show-overflow-tooltip="true" />
            <el-table-column label="性别" prop="sex" align="center" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    {{ scope.row.sex == '0' ? '男' : scope.row.sex == '1' ? '女' : '未知'}}
                </template>
            </el-table-column>
            <el-table-column label="年龄" prop="birthday" align="center" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    {{ getage(scope.row.birthday) }}岁
                </template>
            </el-table-column>
            <el-table-column label="科室" prop="deptNames" align="center" :show-overflow-tooltip="true" />
            <el-table-column label="职级" prop="postName" align="center" :show-overflow-tooltip="true" />
            <el-table-column label="手机" prop="phonenumber" align="center" width="130" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-circle-close" :disabled="$route.query.researchStatus === '2'"
                        @click.stop="cancelAuthUser(scope.row)" v-hasPermi="['system:role:delete']">取消授权</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <select-user ref="select" :roleId="queryParams.roleId" :research-id="$route.query.researchId"
            @ok="handleQuery" />
    </div>
</template>

<script>
import {
    listFindAllUserByRole,
    cancelUserRole,
    cancelAllUserRole
} from "@/api/researh/user";
import selectUser from "./selectUser";

export default {
    name: "AuthUser-fllow",
    dicts: ['sys_normal_disable'],
    components: {
        selectUser
    },
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中用户组
            userIds: [],
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 用户表格数据
            userList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                roleId: undefined,
                nickName: undefined,
                phonenumber: undefined
            }
        };
    },
    created() {
        const roleId = this.$route.params && this.$route.params.roleId;
        if (roleId) {
            this.queryParams.roleId = roleId;
            this.getList();
        }
    },
    methods: {
        /** 查询授权用户列表 */
        getList() {
            this.loading = true;
            listFindAllUserByRole(this.queryParams).then(response => {
                this.userList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 返回按钮
        handleClose() {
            const obj = {
                path: "/queueSettings/introductions/index",
                query: {
                    id: this.$route.query.researchId
                }
            };
            this.$tab.closeOpenPage(obj);
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
            this.userIds = selection.map(item => item.userId)
            if (this.$route.query.researchStatus != '2') {
                this.multiple = !selection.length
            }
        },
        /** 打开授权用户表弹窗 */
        openSelectUser() {
            this.$refs.select.show();
        },
        /** 取消授权按钮操作 */
        cancelAuthUser(row) {
            const roleId = this.queryParams.roleId;
            this.$modal.confirm('确认要取消该用户"' + row.userName + '"账号吗？').then(function () {
                return cancelUserRole({
                    userId: row.userId,
                    roleId: roleId
                });
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("取消授权成功");
            }).catch(() => { });
        },
        /** 批量取消授权按钮操作 */
        cancelAuthUserAll(row) {
            const roleId = this.queryParams.roleId;
            const userIds = this.userIds;
            //   const userIds = this.userIds.join(",");
            this.$modal.confirm('是否取消选中用户授权数据项？').then(function () {
                return cancelAllUserRole({
                    roleId: roleId,
                    userIds: userIds
                });
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("取消授权成功");
            }).catch(() => { });
        },
        getage(birthday) {
            let birthdays = new Date(birthday);
            var d = new Date()
            var age = d.getFullYear() - birthdays.getFullYear() - (d.getMonth() < birthdays.getMonth() || (d
                .getMonth() == birthdays.getMonth() && d.getDate() < birthdays.getDate()) ? 1 : 0)
            return age
        },
    }
};
</script>