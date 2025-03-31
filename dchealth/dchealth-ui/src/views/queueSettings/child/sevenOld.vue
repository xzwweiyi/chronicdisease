<template>
    <div class="tabPane">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="角色名称" prop="roleName">
                <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="角色状态" clearable style="width: 240px">
                    <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
                    range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <div class="mb8" style="margin-left:0px; margin-right:0px;">
            <el-button type="primary" plain icon="el-icon-plus" :disabled="this.researchStatus === '2'"
                @click="handleAdd" v-hasPermi="['researh:role:add']">新增</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['system:role:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </div>

        <el-table v-loading="loading" :data="roleList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column label="编号" align="center" prop="roleId" width="120" />
            <el-table-column label="角色名称" align="center" prop="roleName" :show-overflow-tooltip="true"
                min-width="150" />
            <el-table-column label="状态" align="center" width="100">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" :disabled="switchTog"
                        @change="handleStatusChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="190">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat}}</span>
                </template>
            </el-table-column>
            <el-table-column label="人员数" align="center" min-width="150">
                <template slot-scope="scope">
                    <span>{{ scope.row.userQuatity | nullFormat}}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260" fixed="right">
                <template slot-scope="scope" v-if="scope.row.roleId !== 1">
                    <!-- 暂时隐藏 -->
                    <!-- <el-button type="text" icon="el-icon-edit" :disabled="researchStatus ==='2'"
                        @click.stop="handleUpdate(scope.row)" v-hasPermi="['researh:role:update']">修改权限</el-button> -->
                    <el-button type="text" icon="el-icon-setting" :disabled="researchStatus ==='2'"
                        @click.stop="handleAuthUser(scope.row)" v-hasPermi="['system:role:query']">分配用户</el-button>
                    <el-button type="text" icon="el-icon-delete" :disabled="researchStatus ==='2'"
                        @click.stop="handleDelete(scope.row)" class="el-button-text_delete"
                        v-hasPermi="['researh:role:delete']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <div slot="footer" class="dialog-footer" style="margin:20px auto;text-align: center;">
            <el-button type="primary" @click="handleNext">进入下一步</el-button>
        </div>
        <!-- 添加或修改角色配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="800px" top="8vh">
            <el-scrollbar class="dialog_scrollbar">
                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                    <el-form-item label="角色名称" prop="roleName">
                        <el-input v-model="form.roleName" placeholder="请输入角色名称" />
                    </el-form-item>
                    <el-form-item label="角色顺序" prop="roleSort">
                        <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
                    </el-form-item>
                    <el-form-item label="状态">
                        <el-radio-group v-model="form.status">
                            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value"
                                :label="dict.value">
                                {{dict.label}}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="菜单权限">
                        <el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" default-expand-all
                            node-key="sourceId" :check-strictly="form.menuCheckStrictly" empty-text="加载中，请稍候"
                            :props="defaultProps"></el-tree>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
                    </el-form-item>
                </el-form>

            </el-scrollbar>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 分配角色数据权限对话框 -->
        <el-dialog :title="title" :visible.sync="openDataScope" width="800px" top="8vh" :destroy-on-close="true">
            <el-form :model="form" label-width="80px">
                <el-form-item label="角色名称">
                    <el-input v-model="form.roleName" :disabled="true" />
                </el-form-item>
                <el-form-item label="随访节点">
                    <el-checkbox-group v-model="form.followIds">
                        <el-checkbox v-for="moduleItem in moduleList" :label="moduleItem.followId"
                            :key="moduleItem.followId">{{moduleItem.followName}}</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="权限范围">
                    <el-select v-model="form.dataScope" @change="dataScopeSelectChange">
                        <el-option v-for="item in dataScopeOptions" :key="item.value" :label="item.label"
                            :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="数据权限" v-show="form.dataScope == 2">
                    <el-tree class="tree-border" :data="deptOptions" show-checkbox default-expand-all ref="dept"
                        node-key="deptId" empty-text="加载中，请稍候" @check="changeTree" :props="defaultDepts">
                        <span class="custom-tree-node" slot-scope="{ node, data }">
                            <span>{{ node.label }}</span>
                            <span>
                                <el-checkbox-group v-model="data.opers" @change="groupChangeTree(data)">
                                    <el-checkbox v-for="dict in dict.type.research_common_oper" :key="dict.value"
                                        :label="dict.value">{{dict.label}}</el-checkbox>
                                </el-checkbox-group>
                            </span>
                        </span>
                    </el-tree>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitDataScope" :disabled="researchStatus === '2'">确 定</el-button>
                <el-button @click="cancelDataScope">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import cache from '@/plugins/cache'
import {
    listRole, addRole, updateRole, delRole,
    getSourceByRoleId, changeRoleStatus, updateData
} from "@/api/researh/user";
import { listSource, treeSource, updateRoleSource, dataPermissionsData } from "@/api/researh/source";
export default {
    props: {
        researchId: { type: String, default: null, },
    },
    dicts: ['sys_normal_disable', 'research_common_oper'],
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
        //选择权限操作
        groupChangeTree(event) {
            if (this.roleDepts.findIndex(f => f.deptId == event.deptId) > -1) {
                this.roleDepts.find(f => f.deptId == event.deptId).opers = event.opers
            }
        },
        /** 查询角色列表 */
        getList() {
            this.queryParams.researchId = this.researchId
            this.loading = true;
            listRole(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                this.roleList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        /** 查询菜单树结构 */
        getMenuTreeselect() {
            listSource(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                this.menuOptions = response.rows;
            });
        },
        // 所有菜单节点数据
        getMenuAllCheckedKeys() {
            // 目前被选中的菜单节点
            let checkedKeys = this.$refs.menu.getCheckedKeys();
            // 半选中的菜单节点
            let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
            checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
            return checkedKeys;
        },
        changeTree(data, node) {
            console.log(data, node)
            this.hasChild = data.children.length;
            this.val = data.deptId;
            this.roleDepts = node.checkedNodes

        },
        getDeptAllChecked() {
            let checkedKeys = this.$refs.dept.getCheckedKeys();
            let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys();
            checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
            return checkedKeys;
        },


        /** 根据角色ID查询菜单树结构 */
        getRoleMenuTreeselect(roleId) {
            return getSourceByRoleId({
                roleId: roleId
            }).then(response => {
                return response;
            });
        },
        /**
         * 
         * @param {*} researchId 
         * @param {*} roleId 
         */
        loopOpers(data) {
            if (data && data.length) {
                data.forEach(item => {
                    if (item.opers == undefined) {
                        item.opers = []
                    }
                    item.children = this.loopOpers(item.children)
                })
                this.deptIds.push(...data.filter(f => f.check).map(m => m.deptId))
                return data
            } else {
                return []
            }
        },
        /** 根据队列ID和角色ID查询数据权限树结构 */
        getRoleDeptTreeselect(researchId, roleId) {
            return dataPermissionsData(researchId, roleId).then(response => {
                this.deptIds = []
                this.deptOptions = this.loopOpers(response.data.deptVOS);
                this.$refs.dept.setCheckedKeys(this.deptIds);
                this.moduleList = response.data.researchFollowVOS;
                this.moduleList.forEach(item => {
                    if (item.check === true) {
                        this.form.followIds.push(item.followId)
                    }

                })
                return response.data
            });
        },
        /** 更新/修改为角色分配菜单**/
        getRoleSource(roleId, menuIds) {
            let paramsDate = {
                roleId: roleId,
                sourceIds: menuIds,
            }
            updateRoleSource(paramsDate).then(res => {
                return res;
            })
        },
        // 角色状态修改
        handleStatusChange(row) {
            let text = row.status === "0" ? "启用" : "停用";
            this.$modal.confirm('确认要"' + text + '""' + row.roleName + '"角色吗？').then(function () {
                return changeRoleStatus({
                    roleId: row.roleId,
                    status: row.status
                });
            }).then(() => {
                this.$modal.msgSuccess(text + "成功");
            }).catch(function () {
                row.status = row.status === "0" ? "1" : "0";
            });
        },
        /** 选择角色权限范围触发 */
        dataScopeSelectChange(value) {
            if (value !== '2') {
                this.$refs.dept.setCheckedKeys([]);
            }
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 取消按钮（数据权限）
        cancelDataScope() {
            this.openDataScope = false;
            this.reset();
        },
        // 表单重置
        reset() {
            if (this.$refs.menu != undefined) {
                this.$refs.menu.setCheckedKeys([]);
            }
            this.form = {
                roleId: undefined,
                roleName: '',
                roleSort: 0,
                followIds: undefined || [],
                status: "0",
                remark: undefined
            };
            this.resetForm("form");
            this.getList();
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
            this.ids = selection.map(item => item.roleId)
            if (this.researchStatus != '2') {
                this.single = selection.length != 1
                this.multiple = !selection.length
            }
        },
        // 更多操作触发
        handleCommand(command, row) {
            switch (command) {
                case "handleDataScope":
                    // 数据权限
                    this.handleDataScope(row);
                    break;
                case "handleAuthUser":
                    this.handleAuthUser(row);
                    break;
                default:
                    break;
            }
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.getMenuTreeselect();
            this.open = true;
            this.title = "添加角色";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const roleId = row.roleId || this.ids
            treeSource().then(response => {
                this.menuOptions = response.data;
                this.open = true;
                this.title = "修改角色";
                this.form = row;
                this.$nextTick(() => {
                    getSourceByRoleId({
                        roleId: roleId
                    }).then(res => {
                        if (res.data) {
                            let checkedKeys = res.data
                            checkedKeys.forEach((v) => {
                                this.$refs.menu.setChecked(v, true, false);
                            });
                        }
                    });
                })

            })
        },
        /** 分配数据权限操作 */
        handleDataScope(row) {
            this.getRoleDeptTreeselect(row.researchId, row.roleId);
            this.reset();
            this.$set(row, 'followIds', [])
            this.form = row
            this.title = '数据权限'
            this.openDataScope = true;
        },
        /** 分配用户操作 */
        handleAuthUser: function (row) {
            const roleId = row.roleId;
            this.$router.push({
                path: "/queueSettings/role/authUser/" + roleId,
                query: {
                    researchId: this.researchId,
                    researchStatus: this.researchStatus
                }
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.roleId != undefined) {
                        this.form.sourceIds = this.getMenuAllCheckedKeys();
                        this.form.researchId = this.researchId
                        updateRole(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            // this.getRoleSource(this.form.roleId, this.form.sourceIds)
                            this.getList();
                        });
                    } else {
                        // this.form.sourceIds = this.getMenuAllCheckedKeys();
                        this.form.researchId = this.researchId
                        addRole(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        // 所有部门节点数据
        getDeptAllCheckedKeys() {
            // 目前被选中的部门节点
            let checkedKeys = this.$refs.dept.getCheckedNodes();
            return checkedKeys;
        },
        /** 提交按钮（数据权限） */
        submitDataScope: function () {
            let arr = []
            this.form.roleDepts = this.getDeptAllCheckedKeys();
            this.form.roleDepts.forEach(item => {
                let obj = {
                    deptId: item.deptId,
                    opers: item.opers,
                }
                arr.push(obj)
            })
            this.form.deptAndOperForms = arr
            this.$delete(this.form, 'createBy',)
            this.$delete(this.form, 'createTime')
            this.$delete(this.form, 'delFlag')
            this.$delete(this.form, 'deptIds')
            this.$delete(this.form, 'params')
            this.$delete(this.form, 'remark')
            this.$delete(this.form, 'researchId')
            this.$delete(this.form, 'roleDepts')
            this.$delete(this.form, 'roleModuleVos')
            this.$delete(this.form, 'roleName')
            this.$delete(this.form, 'roleSort')
            this.$delete(this.form, 'searchValue')
            this.$delete(this.form, 'sourceIds')
            this.$delete(this.form, 'status')
            this.$delete(this.form, 'updateBy')
            this.$delete(this.form, 'updateTime')
            updateData(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.openDataScope = false;
                this.getList();
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const roleIds = row.roleId || this.ids;
            this.$modal.confirm('是否确认删除角色编号为"' + roleIds + '"的数据项？').then(function () {
                return delRole({
                    roleIds: [roleIds]
                });
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/role/export', {
                ...this.queryParams
            }, `role_${new Date().getTime()}.xlsx`)
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