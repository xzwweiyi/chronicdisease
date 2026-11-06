<template>
    <div class="tabPane_content">
        <div class="tabPane_title">患者端展示信息</div>
        <el-form ref="formQueue" :model="queueInfo" :rules="addformRules" label-width="120px">
            <el-form-item label="选择病种" prop="diseaseTypeId">
                <el-cascader v-model="queueInfo.diseaseTypeId" :options="diseaseTypeList" :props="{ value: 'id' }"
                    :show-all-levels="false" clearable @change="hadleSelect" filterable></el-cascader>
            </el-form-item>
            <el-form-item label="队列名称" prop="projectName">
                <el-input type="text" v-model="queueInfo.projectName"></el-input>
            </el-form-item>
            <el-form-item label="队列介绍" prop="researchSynopsis">
                <editor class="my_editor" v-model="queueInfo.researchSynopsis" :min-height="192" />
            </el-form-item>
            <el-form-item label="队列展示图" prop="imageUrl">
                <image-upload v-model="queueInfo.imageUrl" :limit="1" />
            </el-form-item>

            <div class="tabPane_title">队列信息</div>

            <el-form-item label="队列负责人" prop="doctorId">
                <el-input v-model="queueInfo.doctorName" placeholder="请选择" :disabled="true" style="width: 200px" />
                <el-button type="primary" @click="handeOpen">选择</el-button>
            </el-form-item>
            <el-form-item label="治疗方案" prop="researchScheme">
                <editor class="my_editor" v-model="queueInfo.researchScheme" :min-height="192" />
            </el-form-item>
            <el-form-item label="入组标准" prop="researchStandard">
                <editor class="my_editor" v-model="queueInfo.researchStandard" :min-height="192" />
            </el-form-item>
            <el-form-item label="开启随访统计" prop="isFollowRate">
                <el-switch v-model="queueInfo.isFollowRate" :active-value="1" :inactive-value="0" disabled></el-switch>
            </el-form-item>
        </el-form>
        <div class="dialog-footer" style="display: block; margin:20px auto 40px; text-align: center">
            <el-button type="primary" v-hasPermi="['research:info:add']" @click="submitFormInfo('save')">保 存</el-button>
            <el-button type="primary" @click="submitFormInfo('next')">保存并进入下一步</el-button>
        </div>
        <el-dialog :title="title" :visible.sync="open" width="70%" :close-on-click-modal="false" append-to-body
            :custom-class="'fontSize_'+$store.getters.size">
            <el-row :gutter="20">
                <!--科室数据-->
                <el-col :span="6" :xs="24">
                    <el-scrollbar class="left_scrollbar">
                        <el-tree :data="deptOptions" :props="defaultProps" class="one_tree_line"
                            :filter-node-method="filterNode" ref="tree" default-expand-all
                            @node-click="handleNodeClick">
                            <span class="custom-tree-node" slot-scope="{ data }">
                                <i class="el-icon-folder-opened"></i>
                                {{ data.label }}
                            </span>
                        </el-tree>
                    </el-scrollbar>
                </el-col>
                <!--用户数据-->
                <el-col :span="18" :xs="24">
                    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch"
                        class="search_form">
                        <el-form-item label="账号" prop="userName">
                            <el-input v-model="queryParams.userName" placeholder="请输入账号" clearable style="width: 240px"
                                @keyup.enter.native="handleQuery" />
                        </el-form-item>
                        <el-form-item label="手机号码" prop="phonenumber">
                            <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable
                                style="width: 240px" @keyup.enter.native="handleQuery" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索
                            </el-button>
                            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                        </el-form-item>
                    </el-form>

                    <el-row class="mb8">
                        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList">
                        </right-toolbar>
                    </el-row>
                    <el-scrollbar class="right_scrollbar">
                        <el-table v-loading="loading" :data="userList">
                            <el-table-column label="账号" align="center" key="userName" prop="userName"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="姓名" align="center" key="nickName" prop="nickName"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="科室" align="center" key="deptName" prop="dept.deptName"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber"
                                width="120">
                                <template slot-scope="scoped">{{ scoped.row.phonenumber | nullFormat }}</template>
                            </el-table-column>
                            <el-table-column label="创建时间" align="center" prop="createTime" min-width="160">
                                <template slot-scope="scope">
                                    <span>{{ scope.row.createTime | dateFormat}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" fixed="right" width="160"
                                class-name="small-padding fixed-width">
                                <template slot-scope="scope">
                                    <el-button type="text" icon="el-icon-edit" @click="handleDoctorId(scope.row)">选择
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-scrollbar>
                    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                        :limit.sync="queryParams.pageSize" @pagination="getList" />
                </el-col>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
import cache from '@/plugins/cache'
import { infoAdd, infoEdit, selectDiseases, updateStageData, listRoleUser, } from "@/api/info/config";
import { listUser, changeUserStatus } from "@/api/system/user";
import { treeselect } from "@/api/system/dept";
import { isEditEmpty } from "@/utils/validate.js"
export default {
    props: {
        researchId: { type: String, default: null, },
        addform: { type: Object, default: null, },
    },
    data() {
        return {
            researchStatus: "",
            listRole: [],
            limit: 1,
            diseaseTypeList: [],
            // 表单校验
            addformRules: {
                diseaseTypeId: [{ required: true, message: "队列病种不能为空", trigger: "change" }],
                projectName: [{ required: true, message: "队列名称不能为空", trigger: "blur" }],
                researchSynopsis: [{ required: true, validator: isEditEmpty, message: "队列介绍不能为空", trigger: "blur" }],
                imageUrl: [{ required: true, message: "队列展示图不能为空", trigger: "change" }],
                doctorId: [{ required: true, message: "队列负责人不能为空", trigger: "change" }],
                researchScheme: [{ required: true, validator: isEditEmpty, message: "治疗方案不能为空", trigger: "blur" }],
                researchStandard: undefined,
            },
            // 遮罩层
            loading: true,
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
            deptCascaderOptions: [],
            // 是否显示弹出层
            open: false,
            defaultProps: { children: "children", label: "label", },
            // 查询参数
            queryParams: {
                pageNum: 1, pageSize: 10, userName: undefined, phonenumber: undefined,
                status: undefined, deptId: undefined,
            },
            queueInfo: {
                isFollowRate: 1,
                imageUrl: window.origin == 'http://172.100.45.33'
                    ? 'http://172.100.45.33' + '/files/research_default.png'
                    : 'https://td.dchealthclinical.com' + '/files/research_default.png'
            },
        };
    },
    watch: {
        addform(newVal) {
            this.queueInfo = {
                ...newVal,
                isFollowRate: newVal && newVal.isFollowRate !== undefined ? Number(newVal.isFollowRate) : 1
            }
        }
    },
    created() {
        this.getDiseases();
    },
    methods: {
        // 选择病种
        hadleSelect(value) {
            let valArr = value;
            console.log('valArr', valArr)
            // valArr.forEach((item, index) => {
            if (this.deptCascaderOptions.length == 1) {
                this.queueInfo.deptId = this.deptCascaderOptions[0].id;
                this.queueInfo.diseaseTypeId = valArr[0];
            } else {
                this.queueInfo.deptId = valArr[0];
                this.queueInfo.diseaseTypeId = valArr[1];
            }
            // });
        },
        // 表单重置
        reset() {
            this.queueInfo = {
                diseaseTypeId: null,
                projectName: null,
                researchSynopsis: null,
                imageUrl: null,
                doctorId: null,
                doctorName: null,
                name: null,
                researchScheme: null,
                researchStandard: null,
                isFollowRate: 1,
            };
            this.resetForm("queueInfo");
        },
        // 获取病种结构接口
        getDiseases() {
            selectDiseases().then((response) => {
                response.data.forEach((item) => {
                    if (item.hasOwnProperty("children")) {
                        this.$set(item, "disabled", false);
                    } else {
                        this.$set(item, "disabled", true);
                    }
                });
                if (response.data.length == 1) {
                    this.deptCascaderOptions = response.data;
                    this.diseaseTypeList = response.data[0].children;
                } else {
                    this.diseaseTypeList = response.data
                }
                return response.data;
            });
        },
        imgUpload(imgUrl) {
            this.queueInfo.imageUrl = imgUrl;
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 队列简介
        submitFormInfo(valData) {
            this.$refs["formQueue"].validate((valid) => {
                if (valid) {
                    if (this.queueInfo.id != undefined) {
                        infoEdit(this.queueInfo).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            let ids = this.queueInfo.id;
                            this.$nextTick(() => {
                                if (valData == "next") {
                                    updateStageData(ids, 1).then((res) => {
                                        this.$emit("nextSubmitTwo", true);
                                    });
                                }
                            });
                        });
                    } else {
                        infoAdd(this.queueInfo).then((response) => {
                            this.$modal.msgSuccess("新增成功");
                            let ids = response.data;
                            cache.session.setJSON('researchStatus', '0')
                            this.$emit('closecreatedia')
                            this.$nextTick(() => {
                                if (valData == "next") {
                                    updateStageData(ids, 1).then((res) => {
                                        this.$router.push({
                                            path: "/queueSettings/introductions/index",
                                            query: {
                                                id: ids,
                                                stage: 1
                                            },
                                        });
                                    });
                                } else {
                                    this.$router.push({
                                        path: "/queueSettings/introductions/index",
                                        query: {
                                            id: ids,
                                            stage: 0
                                        },
                                    });
                                }
                            });
                        });
                    }
                } else {
                    this.$message.warning('有内容未填写')
                }
            });
        },
        /** 查询用户列表 */
        getList() {
            this.loading = true;
            listUser(this.addDateRange(this.queryParams, this.dateRange)).then(
                (response) => {
                    this.userList = response.rows;
                    this.total = response.total;
                    this.loading = false;
                }
            );
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
        // 用户状态修改
        handleStatusChange(row) {
            let text = row.status === "0" ? "启用" : "停用";
            this.$modal
                .confirm('确认要"' + text + '""' + row.userName + '"用户吗？')
                .then(function () {
                    return changeUserStatus(row.userId, row.status);
                })
                .then(() => {
                    this.$modal.msgSuccess(text + "成功");
                })
                .catch(function () {
                    row.status = row.status === "0" ? "1" : "0";
                });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
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
        /**
         * 选择队列负责人
         */
        handeOpen() {
            this.open = true;
            this.title = '选择队列负责人'
            this.getList();
            this.getTreeselect();
        },
        handleDoctorId(e) {
            this.open = false;
            // this.queueInfo.doctorId = e.userId;
            this.queueInfo.doctorName = e.nickName;
            this.$set(this.queueInfo, 'doctorId', e.userId);
        },
    },
};
</script>

<style lang="scss" scoped>
.tabPane_content {
    padding: 0 40px 0 20px;

    .tabPane_title {
        margin-bottom: 20px;
        margin-left: 20px;
        font-weight: bold;
        line-height: 45px;
        text-indent: 15px;
        border-left: 4px #409eff solid;
        background: rgba(24, 144, 255, 0.08);
    }
}

.left_scrollbar {
    height: 434px;
}

.right_scrollbar {
    height: 303px;
}

.one_tree_line {
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