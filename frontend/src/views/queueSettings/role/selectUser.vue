<template>
    <!-- 授权用户 -->
    <el-dialog title="选择医生" :visible.sync="visible" width="70%" top="8vh">
        <el-row :gutter="20">
            <!--科室数据-->
            <el-col :span="6" :xs="24">
                <el-scrollbar class="left_scrollbar">
                    <el-tree :data="deptOptions" :props="defaultProps" class="one_tree_line"
                        :filter-node-method="filterNode" ref="tree" default-expand-all @node-click="handleNodeClick">
                        <span class="custom-tree-node" slot-scope="{ data }">
                            <i class="el-icon-folder-opened"></i>
                            {{ data.label }}
                        </span>
                    </el-tree>
                </el-scrollbar>
            </el-col>
            <!--用户数据-->
            <el-col :span="18" :xs="24">
                <el-form :model="queryParams" ref="queryForm" :inline="true">
                    <el-form-item label="姓名" prop="nickName">
                        <el-input v-model="queryParams.nickName" placeholder="请输入姓名" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="手机号码" prop="phonenumber">
                        <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                    </el-form-item>
                </el-form>
                <el-row>
                    <el-table @row-click="clickRow" ref="table" :data="userList"
                        @selection-change="handleSelectionChange" height="calc(100vh - 460px)">
                        <el-table-column align="center" type="selection" width="55"></el-table-column>
                        <el-table-column align="center" label="姓名" prop="nickName" :show-overflow-tooltip="true" />
                        <el-table-column align="center" label="性别" prop="sex" :show-overflow-tooltip="true">
                            <template slot-scope="scope">
                                {{ scope.row.sex == '0' ? '男' : scope.row.sex == '1' ? '女' : '未知'}}
                            </template>
                        </el-table-column>
                        <el-table-column align="center" label="年龄" prop="birthday" :show-overflow-tooltip="true">
                            <template slot-scope="scope">
                                {{ getage(scope.row.birthday) }}岁
                            </template>
                        </el-table-column>
                        <el-table-column align="center" label="科室" prop="deptNames" :show-overflow-tooltip="true" />
                        <el-table-column align="center" label="职级" prop="postName" :show-overflow-tooltip="true" />
                        <el-table-column align="center" label="手机" prop="phonenumber" :show-overflow-tooltip="true" />
                    </el-table>
                    <!-- <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum"
                        :limit.sync="queryParams.pageSize" @pagination="getList" /> -->
                </el-row>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="handleSelectUser">确 定</el-button>
            <el-button @click="visible = false">取 消</el-button>
        </div>
    </el-dialog>
</template>

<script>
import {
    listAllUserByRole,
    authUserByRole
} from "@/api/researh/user";
import {
    treeselect
} from "@/api/system/dept";
export default {
    dicts: ['sys_normal_disable'],
    props: {
        // 角色编号
        roleId: {
            type: [Number, String]
        },
        researchId: ''
    },
    data() {
        return {
            // 遮罩层
            visible: false,
            // 选中数组值
            userIds: [],
            // 总条数
            total: 0,
            // 未授权用户数据
            userList: [],
            // 科室树选项
            deptOptions: undefined,
            defaultProps: {
                children: "children",
                label: "label",
            },
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                deptId: undefined,
                nickName: undefined,
                phonenumber: undefined
            }
        };
    },
    created() {
        this.getTreeselect();
    },
    methods: {
        // 显示弹框
        show() {
            this.getList();
            this.visible = true;
        },
        clickRow(row) {
            this.$refs.table.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.userIds = selection.map(item => item.userId);
        },
        // 查询表数据
        getList() {
            this.queryParams.researchId = this.researchId
            listAllUserByRole(this.queryParams).then(res => {
                this.userList = res.rows;
                this.total = res.total;
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
        /** 选择授权用户操作 */
        handleSelectUser() {
            const roleId = this.queryParams.roleId;
            //   const userIds = this.userIds.join(",");
            const userIds = this.userIds;

            if (userIds == "") {
                this.$modal.msgError("请选择要分配的用户");
                return;
            }
            authUserByRole({
                roleId: roleId,
                userIds: userIds,
                researchId: this.researchId
            }).then(res => {
                this.$modal.msgSuccess(res.msg);
                if (res.code === 200) {
                    this.visible = false;
                    this.$emit("ok");
                }
            });
        },
        getage(birthday) {
            let birthdays = new Date(birthday);
            var d = new Date()
            var age = d.getFullYear() - birthdays.getFullYear() - (d.getMonth() < birthdays.getMonth() || (d
                .getMonth() == birthdays.getMonth() && d.getDate() < birthdays.getDate()) ? 1 : 0)
            return age
        },
        /** 查询科室下拉树结构 */
        getTreeselect() {
            treeselect().then((response) => {
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
    }
};
</script>

<style lang="scss" scoped>
.left_scrollbar {
    height: 70vh;
}
</style>