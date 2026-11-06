<template>
    <div class="tabPane">
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
                    <el-button type="text" class="el-button-text_delete" :disabled="$route.query.researchStatus === '2'"
                        @click.stop="cancelAuthUser(scope.row)" v-hasPermi="['system:role:delete']">取消授权</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <div slot="footer" class="dialog-footer" style="margin:20px auto;text-align: center;">
            <el-button type="primary" @click="handleNext">进入下一步</el-button>
        </div>
        <select-user ref="select" :research-id="researchId" @ok="handleQuery" />
    </div>
</template>

<script>
import cache from '@/plugins/cache'
import {
    listFindAllUserByRole,
    cancelUserRole,
    cancelAllUserRole
} from "@/api/researh/user";
import selectUser from "@/views/queueSettings/role/selectUser";
export default {
    props: {
        researchId: { type: String, default: null, },
    },
    components: { selectUser },
    dicts: [],
    data() {
        return {
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
            // 角色表格数据
            roleList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否显示弹出层（数据权限）
            openDataScope: false,
            // 日期范围
            dateRange: [],
            // 菜单列表
            menuOptions: [],
            // 部门列表
            deptOptions: [],
            // 随访节点
            moduleList: [],
            // 查询参数
            queryParams: {
                pageNum: 1, pageSize: 10, roleName: undefined, status: undefined, researchId: undefined,
            },
            // 表单参数
            form: {},
            defaultProps: {
                children: "child", label: "sourceName"
            },
            defaultDepts: {
                children: "children", label: "deptName"
            },
            defaultModules: {
                children: "deptTreeVos", label: "moduleName"
            },
            rowId: '',
            // 表单校验
            rules: {
                roleName: [{ required: true, message: "角色名称不能为空", trigger: "blur" }],
                roleSort: [{ required: true, message: "角色顺序不能为空", trigger: "blur" }]
            },
            // 部门权限
            roleDepts: [],
            val: '',
            hasChild: '',
            // 数据范围选项
            dataScopeOptions: [
                { value: "1", label: "全部数据权限" },
                { value: "2", label: "自定数据权限" },
                { value: "3", label: "本部门数据权限" },
                { value: "4", label: "本部门及以下数据权限" },
                { value: "5", label: "仅本人数据权限" }
            ],
            deptIds: [],
            researchStatus: '', //0表示未发布，1表示已发布，2表示已结束  状态编码
            switchTog: false,

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
                nickName: undefined,
                phonenumber: undefined
            }
        };
    },
    watch: {
        researchId: {
            immediate: true, // 监听到后，立即执行 handler方法
            handler(newVal, oldValue) {
                if (newVal && newVal != null) {
                    this.getList()
                    this.researchStatus = cache.session.getJSON('researchStatus')
                    if (this.researchStatus == '2') {
                        this.switchTog = true
                    }
                }
            }
        }
    },
    computed: {

    },
    methods: {
        /** 查询角色列表 */
        getList() {
            this.loading = true;
            this.queryParams.researchId = this.researchId
            listFindAllUserByRole(this.queryParams).then(response => {
                this.userList = response.rows;
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
            let researchId = this.researchId
            this.$modal.confirm('确认要取消该用户"' + row.userName + '"账号吗？').then(function () {
                return cancelUserRole({
                    userId: row.userId,
                    researchId
                });
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("取消授权成功");
            }).catch(() => { });
        },
        /** 批量取消授权按钮操作 */
        cancelAuthUserAll(row) {
            const userIds = this.userIds;
            let researchId = this.researchId
            //   const userIds = this.userIds.join(",");
            this.$modal.confirm('是否取消选中用户授权数据项？').then(function () {
                return cancelAllUserRole({
                    researchId,
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
        /** 保存进入下一步 */
        // 进入下一步
        handleNext() {
            this.$emit('nextSubmitEightSaveClick', true)
        },
    }
};
</script>

<style lang="scss" scoped>
.dialog_scrollbar {
    height: calc(100vh - 330px);
}

.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}

.el-tree {
    max-height: 300px;
    width: 100%;
    overflow-y: scroll;
}

.el-tree > .el-tree-node {
    display: inline-block;
    min-width: 100%;
}
</style>