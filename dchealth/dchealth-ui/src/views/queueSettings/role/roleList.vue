<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="角色名称" prop="roleName">
                <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="权限字符" prop="roleKey">
                <el-input v-model="queryParams.roleKey" placeholder="请输入权限字符" clearable style="width: 240px"
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

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                    v-hasPermi="['researh:role:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['researh:role:update']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['researh:role:delete']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['system:role:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="roleList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="角色编号" prop="roleId" width="120" />
            <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150" />
            <el-table-column label="权限字符" prop="roleKey" :show-overflow-tooltip="true" width="150" />
            <el-table-column label="显示顺序" prop="roleSort" width="100" />
            <el-table-column label="角色状态" align="center" width="100">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
                        @change="handleStatusChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
            </el-table-column>
            <el-table-column label="显示顺序" prop="roleSort" width="100">
                <template slot-scope="scope">
                    <span>
                        <el-tag type="scope.row.delFlag == 0 ? success : danger">
                            {{ scope.row.delFlag == 0 ? '存在' : '删除'}}</el-tag>
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260" fixed="right">
                <template slot-scope="scope" v-if="scope.row.roleId !== 1">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['researh:role:update']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['researh:role:delete']">删除</el-button>
                    <el-dropdown @command="(command) => handleCommand(command, scope.row)"
                        v-hasPermi="['system:role:edit']">
                        <span class="el-dropdown-link">
                            <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="handleDataScope" icon="el-icon-circle-check"
                                v-hasPermi="['system:role:edit']">数据权限</el-dropdown-item>
                            <el-dropdown-item command="handleAuthUser" icon="el-icon-user"
                                v-hasPermi="['system:role:edit']">分配用户</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改角色配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" top="8vh">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="form.roleName" placeholder="请输入角色名称" />
                </el-form-item>
                <el-form-item prop="roleKey">
                    <span slot="label">
                        <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasRole('admin')`)" placement="top">
                            <i class="el-icon-question"></i>
                        </el-tooltip>
                        权限字符
                    </span>
                    <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
                </el-form-item>
                <el-form-item label="角色顺序" prop="roleSort">
                    <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">
                            {{dict.label}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="菜单权限">
                    <!-- <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox> -->
                    <!-- <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox> -->
                    <!-- <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动</el-checkbox> -->
                    <el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" default-expand-all
                        node-key="sourceId" :check-strictly="form.menuCheckStrictly" empty-text="加载中，请稍候"
                        :props="defaultProps"></el-tree>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 分配角色数据权限对话框 -->
        <el-dialog :title="title" :visible.sync="openDataScope" width="800px" top="8vh">
            <el-form :model="form" label-width="80px">
                <el-form-item label="角色名称">
                    <el-input v-model="form.roleName" :disabled="true" />
                </el-form-item>
                <el-form-item label="权限字符">
                    <el-input v-model="form.roleKey" :disabled="true" />
                </el-form-item>
                <el-form-item label="权限范围">
                    <!-- <el-checkbox-group v-model="form.roleKey">
                <el-checkbox v-for="moduleItem in moduleList" :label="moduleItem.moduleName" :key="moduleItem.id"></el-checkbox>
            </el-checkbox-group> -->
                    <el-tree class="tree-border" :data="moduleList" show-checkbox default-expand-all ref="module"
                        node-key="moduleId" :check-strictly="form.deptCheckStrictly" empty-text="加载中，请稍候"
                        @check="checkAll" :props="defaultModules">
                        <span class="custom-tree-node" slot-scope="{ node, data }">
                            <span>{{ node.label }}</span>
                            <span>
                                <el-checkbox-group v-model="data.authType" @change="groupChangeModule(data)">
                                    <el-checkbox v-for="(item, index) in checkArry" :key="index" :label="item.value">
                                        {{item.label}}</el-checkbox>
                                </el-checkbox-group>
                            </span>
                        </span>
                    </el-tree>
                </el-form-item>
                <el-form-item label="数据权限">
                    <el-tree class="tree-border" :data="deptOptions" show-checkbox default-expand-all ref="dept"
                        node-key="deptId" :check-strictly="form.deptCheckStrictly" empty-text="加载中，请稍候"
                        @check="changeTree" :props="defaultDepts">
                        <span class="custom-tree-node" slot-scope="{ node, data }">
                            <span>{{ node.label }}</span>
                            <span>
                                <el-checkbox-group v-model="data.authType" @change="groupChangeTree(data)">
                                    <el-checkbox v-for="(item, index) in checkArry" :key="index" :label="item.value">
                                        {{item.label}}</el-checkbox>
                                </el-checkbox-group>
                            </span>
                        </span>
                    </el-tree>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitDataScope">确 定</el-button>
                <el-button @click="cancelDataScope">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listRole,
    addRole,
    updateRole,
    delRole,
    getSourceByRoleId,
    changeRoleStatus,
    listFindRoleAuthByRoleId,
    listFindDeptByRoleId,
    updateData,
    getFindRoleByRoleId,
    getFindModuleByRoleId
} from "@/api/researh/user";
import {
    listSource,
    treeSource,
    updateRoleSource
} from "@/api/researh/source";

export default {
    name: "Role",
    dicts: ['sys_normal_disable'],
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
            menuExpand: false,
            menuNodeAll: false,
            deptExpand: true,
            deptNodeAll: false,
            // 日期范围
            dateRange: [],
            // 菜单列表
            menuOptions: [],
            // 部门列表
            deptOptions: [],
            // 模块列表
            moduleList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                roleName: undefined,
                roleKey: undefined,
                status: undefined,
                researchId: undefined,
            },
            // 表单参数
            form: {},
            defaultProps: {
                children: "child",
                label: "sourceName"
            },
            defaultDepts: {
                children: "deptTreeVos",
                label: "deptName"
            },
            defaultModules: {
                children: "deptTreeVos",
                label: "moduleName"
            },
            checkList: [],
            checkArry: [{
                label: '新增',
                value: '1'
            },
            {
                label: '修改',
                value: '2'
            },
            {
                label: '查看',
                value: '3'
            },
            {
                label: '删除',
                value: '4'
            },
            ],
            rowId: '',
            // 表单校验
            rules: {
                roleName: [{
                    required: true,
                    message: "角色名称不能为空",
                    trigger: "blur"
                }],
                roleKey: [{
                    required: true,
                    message: "权限字符不能为空",
                    trigger: "blur"
                }],
                roleSort: [{
                    required: true,
                    message: "角色顺序不能为空",
                    trigger: "blur"
                }]
            },
            // 部门权限
            roleDepts: [],
            // 模块范围
            roleModuleVos: [],
            val: '',
            hasChild: '',
        };
    },
    created() {
        this.getList(this.$route.query.id);
    },
    methods: {
        //选择权限操作
        groupChangeTree(event) {
            if (this.roleDepts.findIndex(f => f.deptId == event.deptId) > -1) {
                this.roleDepts.find(f => f.deptId == event.deptId).authType = event.authType
            }
        },
        //选择模块操作
        groupChangeModule(event) {
            if (this.roleModuleVos.findIndex(f => f.moduleId == event.moduleId) > -1) {
                this.roleModuleVos.find(f => f.moduleId == event.moduleId).authType = event.authType
            }
        },
        /** 查询角色列表 */
        getList(ids) {
            this.queryParams.researchId = ids
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
        // changeTree(data, node) {
        //     console.log(data)
        //     const obj = {};
        //     this.$set(obj, 'authType', data.authType)
        //     this.$set(obj, 'deptId', data.deptId)
        //     if(node) {
        //         this.roleDepts.push(obj)
        //     } else {
        //         const index = this.roleDepts.findIndex( f => f.deptId === data.deptId)
        //         this.roleDepts.splice(index, 1)
        //     }
        // },
        changeTree(data, node) {
            this.hasChild = data.deptTreeVos.length;
            this.val = data.deptId;
            this.roleDepts = node.checkedNodes
            this.deptOptions[0].deptTreeVos = this.loop(this.deptOptions[0].deptTreeVos, data.deptId)

            if (!data.parentId) {
                if (this.roleDepts.findIndex(f => f.deptId === data.deptId) > -1) {
                    this.deptOptions[0].authType = ['1', '2', '3', '4']
                } else {
                    this.deptOptions[0].authType = []
                }
            }
        },
        loop(data) {
            if (data && data.length) {
                data.forEach(item => {
                    if (this.roleDepts.findIndex(f => f.deptId === item.deptId) > -1) {
                        if (item.deptId == this.val) {
                            item.authType = ['1', '2', '3', '4']
                        }
                        if (this.hasChild > 0) {
                            item.authType = ['1', '2', '3', '4']
                        }
                    } else {
                        item.authType = []
                    }
                    item.deptTreeVos = this.loop(item.deptTreeVos)
                })
                return data
            } else {
                return []
            }
        },
        // changeModule(data, node){
        //     const obj = {};
        //     this.$set(obj, 'authType', data.authType)
        //     this.$set(obj, 'moduleId', data.moduleId)
        //     if(node) {
        //         this.roleModuleVos.push(obj)
        //         // this.moduleList.find( f => f.moduleId === data.moduleId).authType = ['1','2','3','4']
        //     } else {
        //         const index = this.roleDepts.findIndex( f => f.moduleId === data.moduleId)
        //         this.roleModuleVos.splice(index, 1)
        //     }
        // },
        checkAll(data, node) {
            this.roleModuleVos = node.checkedNodes
            if (node.checkedNodes.findIndex(f => f.id === data.id) > -1) {
                this.roleModuleVos.find(f => f.moduleId === data.moduleId).authType = ['1', '2', '3', '4']
            } else {
                this.moduleList.find(f => f.moduleId === data.moduleId).authType = []
            }
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
        /** 根据角色ID查询部门树结构 */
        getRoleDept(researchId) {
            return listFindRoleAuthByRoleId({
                researchId: researchId
            }).then(response => {
                this.moduleList = response.data;
                return response;
            });
        },
        getRoleDeptTreeselect(researchId, roleId) {
            return listFindDeptByRoleId({
                researchId: researchId,
                roleId: roleId
            }).then(response => {
                this.deptOptions = response.data;
                return response;
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
            this.menuExpand = false,
                this.menuNodeAll = false,
                this.deptExpand = true,
                this.deptNodeAll = false,
                this.form = {
                    roleId: undefined,
                    roleName: undefined,
                    roleKey: undefined,
                    roleSort: 0,
                    status: "0",
                    menuIds: [],
                    deptIds: [],
                    menuCheckStrictly: true,
                    deptCheckStrictly: true,
                    remark: undefined
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
            this.ids = selection.map(item => item.roleId)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        // 更多操作触发
        handleCommand(command, row) {
            switch (command) {
                case "handleDataScope":
                    this.handleDataScope(row);
                    break;
                case "handleAuthUser":
                    this.handleAuthUser(row);
                    break;
                default:
                    break;
            }
        },
        // 树权限（父子联动）
        handleCheckedTreeConnect(value, type) {
            if (type == 'menu') {
                this.form.menuCheckStrictly = value ? true : false;
            } else if (type == 'dept') {
                this.form.deptCheckStrictly = value ? true : false;
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
            this.rowId = row.researchId;
            this.reset();
            const moduleData = this.getRoleDept(row.researchId)
            const roleDeptTreeselect = this.getRoleDeptTreeselect(row.researchId, row.roleId);
            this.form = row
            this.openDataScope = true;
            roleDeptTreeselect.then(res => {
                this.$nextTick(() => {
                    getFindRoleByRoleId({
                        roleId: row.roleId
                    }).then(res => {
                        if (res.data) {
                            let checkedKeys = res.data
                            this.roleDepts = checkedKeys
                            checkedKeys.forEach((v) => {
                                this.$refs.dept.setChecked(v, true, false);
                                this.deptOptions.forEach(item => {
                                    if (v.deptId == item.deptId) {
                                        item.authType = v.authType
                                    }
                                    if (item.deptTreeVos.length > 0) {
                                        item.deptTreeVos.forEach(
                                            towItem => {
                                                if (v.deptId == towItem
                                                    .deptId) {
                                                    towItem.authType = v
                                                        .authType
                                                }
                                                if (towItem.deptTreeVos
                                                    .length > 0) {
                                                    towItem.deptTreeVos
                                                        .forEach(
                                                            threeItem => {
                                                                if (v
                                                                    .deptId ==
                                                                    threeItem
                                                                        .deptId
                                                                ) {
                                                                    threeItem
                                                                        .authType =
                                                                        v
                                                                            .authType
                                                                }
                                                            })
                                                }
                                            })

                                    }
                                })
                            });

                        }
                    })
                })
            })
            moduleData.then(res => {
                this.$nextTick(() => {
                    //根据角色id查询分配的模块
                    getFindModuleByRoleId({
                        roleId: row.roleId
                    }).then(res => {
                        if (res.data) {
                            let checkedKeys = res.data
                            this.roleModuleVos = checkedKeys
                            checkedKeys.forEach((v) => {
                                this.$refs.module.setChecked(v, true, false);
                                if (this.moduleList.findIndex(f => f.moduleId == v
                                    .moduleId) > -1) {
                                    this.moduleList.find(f => f.moduleId == v
                                        .moduleId).authType = v.authType
                                }
                            })
                        }
                    })
                })
            })
        },
        /** 分配用户操作 */
        handleAuthUser: function (row) {
            const roleId = row.roleId;
            this.$router.push("/researh/role-auth/user/" + roleId);
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.roleId != undefined) {
                        this.form.menuIds = this.getMenuAllCheckedKeys();
                        updateRole(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getRoleSource(this.form.roleId, this.form.menuIds)
                            this.getList();
                        });
                    } else {
                        // this.form.menuIds = this.getMenuAllCheckedKeys();
                        addRole(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 提交按钮（数据权限） */
        submitDataScope: function () {
            // if(this.roleDepts.length >0 ){
            //     this.form.roleDepts = this.roleDepts;
            // } else {
            //     this.form.roleDepts = this.roleDeptsData;
            // }
            this.form.roleDepts = this.roleDepts;
            this.form.roleModuleVos = this.roleModuleVos;
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
        }
    }
};
</script>

<style>
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
}
</style>