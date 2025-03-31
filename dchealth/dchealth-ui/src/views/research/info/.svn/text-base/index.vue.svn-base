<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="项目名称" prop="projectName">
                <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['research:info:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:info:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['research:info:export']">导出</el-button>
            </el-col> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="infoList" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" align="center" />
            <el-table-column label="主键" align="center" width="80" prop="id" />
            <el-table-column label="主图" align="center" width="60" prop="imageUrl">
                <template slot-scope="scope">
                    <image-preview :src="scope.row.imageUrl||''" :width="50" :height="50" />
                </template>
            </el-table-column>
            <el-table-column label="项目名称" align="left" min-width="160" prop="projectName" />
            <el-table-column label="简介" align="left" min-width="200" prop="researchSynopsis">
                <template slot-scope="scope">
                    {{scope.row.researchSynopsis | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="已入组数" align="center" width="90" prop="patientNum">
                <template slot-scope="scope">
                    {{scope.row.patientNum | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="目标入组数" align="center" width="90" prop="totalNum">
                <template slot-scope="scope">
                    {{scope.row.totalNum | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="是否显示研究简介" align="center" width="160" prop="hospitalNum">
                <template slot-scope="scope">
                    {{scope.row.isShowStudyProfile=='1'?'是':'否'}}
                </template>
            </el-table-column>
            <el-table-column label="是否开放" align="center" width="80" prop="isOpen">
                <template slot-scope="scope">
                    {{scope.row.isOpen=='1'?'是':'否'}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="110" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)" v-hasPermi="['research:info:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)" v-hasPermi="['research:info:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改项目管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false" :close-on-press-escape="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="130px">
                <el-form-item label="项目名称" prop="projectName">
                    <el-input v-model="form.projectName" ref="projectNameInput" placeholder="请输入项目名称" />
                </el-form-item>
                <el-form-item label="主图" prop="imageUrl">
                    <image-upload v-model="form.imageUrl" />
                </el-form-item>
                <el-form-item label="简介" prop="researchSynopsis">
                    <el-input v-model="form.researchSynopsis" placeholder="请输入简介" />
                </el-form-item>
                <el-form-item label="是否显示研究简介">
                    <el-radio-group v-model="form.isShowStudyProfile">
                        <el-radio label="1">是</el-radio>
                        <el-radio label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="是否开放">
                    <el-radio-group v-model="form.isOpen">
                        <el-radio label="1">是</el-radio>
                        <el-radio label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="目标入组数" prop="totalNum">
                    <el-input v-model="form.totalNum" placeholder="请输入目标入组数" />
                </el-form-item>

                <el-form-item label="研究介绍" prop="researchIntro">
                    <!-- <editor v-model="form.researchIntro" class="my_editor"
                        :min-height="260" /> -->
                    <vue-ueditor-wrap v-model="form.researchIntro" :config="editorConfig" :key="1" editor-id="editor_one"></vue-ueditor-wrap>
                </el-form-item>
                <el-form-item label="入排标准" prop="researchStandard">
                    <!-- <editor v-model="form.researchStandard" class="my_editor"
                        :min-height="260" /> -->
                    <vue-ueditor-wrap v-model="form.researchStandard" :config="editorConfig" :key="2" editor-id="editor_two"></vue-ueditor-wrap>
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
import VueUeditorWrap from "@/components/VueUeditorWrap/vue-ueditor-wrap.vue";
import {
    listInfo,
    getInfo,
    delInfo,
    addInfo,
    updateInfo
} from "@/api/research/info";
import {
    getToken
} from '@/utils/auth'


export default {
    components: {
        VueUeditorWrap,
    },
    name: "Info",
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
            // 项目管理表格数据
            infoList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                projectName: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                projectName: [{ required: true, message: "中心编号不能为空", trigger: "blur" }],
            },
            editorConfig: {
                //禁用编辑器
                readonly: false,
                // 编辑器不自动被内容撑高
                autoHeightEnabled: false,
                // 初始容器高度
                initialFrameHeight: 260,
                // 初始容器宽度
                initialFrameWidth: "100%",
                UEDITOR_HOME_URL: "/UEditor/",
                // imageActionName: "/common/upload",
                serverUrl: process.env.VUE_APP_BASE_API + "/common/upload",
                token: getToken()
                // imageUrlPrefix:"",
                // imagePathFormat: process.env.VUE_APP_BASE_API+"/common/upload"
            },
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询项目管理列表 */
        getList() {
            this.loading = true;
            listInfo(this.queryParams).then(response => {
                this.infoList = response.rows;
                this.total = response.total;
                this.loading = false;
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
                id: null,
                imageUrl: null,
                projectName: null,
                tenantId: null,
                researchIntro: null,
                researchSynopsis: null,
                researchStandard: null,
                researchAgree: null,
                doctorType: null,
                templateType: null,
                judgeProgressCreate: null,
                patientNum: null,
                totalNum: null,
                hospitalNum: null,
                createTime: null,
                updateTime: null,
                createBy: null,
                updateBy: null,
                researchProcess: null,
                isShowStudyProfile: null,
                isOpen: null
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
            this.title = "添加项目管理";
            this.$nextTick(() => {
                let inputDom = this.$refs.projectNameInput;
                inputDom.focus();
            })
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getInfo(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改项目管理";
                this.$nextTick(() => {
                    let inputDom = this.$refs.projectNameInput;
                    inputDom.focus();
                })
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateInfo(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addInfo(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除项目管理编号为"' + ids + '"的数据项？').then(function () {
                return delInfo(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('medical/research_info/export', {
                ...this.queryParams
            }, `info_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>