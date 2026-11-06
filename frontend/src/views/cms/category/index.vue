<template>
    <div class="app-container">
        <el-row class="mb8">
            <right-toolbar @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" row-key="id" default-expand-all
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}" :data="categoryList">
            <el-table-column width="30" align="center" />
            <el-table-column label="栏目模块" align="center" width="80" prop="module">
                <template slot-scope="scope">
                    {{scope.row.module=='1'?'文章':(scope.row.module==2?'链接类':'--')}}
                </template>
            </el-table-column>
            <el-table-column label="栏目名称" align="center" width="80" prop="name" />
            <el-table-column label="栏目图片" align="center" prop="image" width="100">
                <template slot-scope="scope">
                    <image-preview :src="scope.row.image||''" :width="50" :height="50" />
                </template>
            </el-table-column>
            <el-table-column label="描述" align="center" prop="description">
                <template slot-scope="scope">
                    {{scope.row.description | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="关键字" align="center" width="100" prop="keywords">
                <template slot-scope="scope">
                    {{scope.row.keywords | nullFormat}}
                </template>
            </el-table-column>

            <el-table-column label="是否显示" align="center" width="75" prop="isShow">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.isShow=='N'?'danger':''">{{scope.row.isShow=='Y'?'是':"否"}}</el-tag>
                </template>
            </el-table-column>

            <el-table-column label="医生端排序" align="center" width="90" prop="sort">
                <template slot-scope="scope">
                    {{scope.row.sort | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="患者排序" align="center" width="75" prop="patientSort">
                <template slot-scope="scope">
                    {{scope.row.patientSort | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="小程序排序" align="center" width="90" prop="minSort">
                <template slot-scope="scope">
                    {{scope.row.minSort | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="是否允许评论" align="center" width="80" prop="allowComment">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.allowComment=='N'?'danger':''">{{scope.row.allowComment=='Y'?'是':"否"}}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="是否需要审核" align="center" width="80" prop="isAudit">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.isAudit=='N'?'danger':''">{{scope.row.isAudit=='Y'?'是':"否"}}</el-tag>
                </template>
            </el-table-column>

            <el-table-column label="备注信息" align="center" prop="remark">
                <template slot-scope="scope">
                    {{scope.row.remark | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="110" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['cms:category:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['cms:category:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改资讯分类对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false"
            :close-on-press-escape="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="父级编号" prop="parentId">
                    <el-input v-model="form.parentId" placeholder="请输入父级编号" />
                </el-form-item>
                <el-form-item label="所有父级编号" prop="parentIds">
                    <el-input v-model="form.parentIds" placeholder="请输入所有父级编号" />
                </el-form-item>
                <el-form-item label="租户ID" prop="tenantId">
                    <el-input v-model="form.tenantId" placeholder="请输入租户ID" />
                </el-form-item>
                <el-form-item label="栏目模块" prop="module">
                    <el-select v-model="form.module">
                        <el-option v-for="(item,index) of moduleList" :key="index" :label="item.moduleName"
                            :value="item.module">{{item.moduleName}}</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="栏目名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入栏目名称" />
                </el-form-item>
                <el-form-item label="栏目图片">
                    <image-upload v-model="form.image" />
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="form.description" placeholder="请输入描述" />
                </el-form-item>
                <el-form-item label="关键字" prop="keywords">
                    <el-input v-model="form.keywords" placeholder="请输入关键字" />
                </el-form-item>
                <el-form-item label="医生端排序" prop="sort">
                    <el-input v-model="form.sort" placeholder="请输入医生端排序" />
                </el-form-item>
                <el-form-item label="患者排序" prop="patientSort">
                    <el-input v-model="form.patientSort" placeholder="请输入患者排序" />
                </el-form-item>
                <el-form-item label="小程序排序" prop="minSort">
                    <el-input v-model="form.minSort" placeholder="请输入小程序排序" />
                </el-form-item>
                <el-form-item label="是否允许评论" prop="allowComment">
                    <el-select v-model="form.allowComment">
                        <el-option label="是" value="Y"></el-option>
                        <el-option label="否" value="N"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否需要审核" prop="isAudit">
                    <el-select v-model="form.isAudit">
                        <el-option label="是" value="Y"></el-option>
                        <el-option label="否" value="N"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注信息" prop="remark">
                    <el-input v-model="form.remark" placeholder="请输入备注信息" />
                </el-form-item>
                <el-form-item label="删除标记" prop="delFlag">
                    <el-select v-model="form.delFlag">
                        <el-option label="是" :value="form.parentId==0?'1':'Y'"></el-option>
                        <el-option label="否" :value="form.parentId==0?'0':'N'"></el-option>
                    </el-select>
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
import { listCategory, getCategory, delCategory, addCategory, updateCategory } from "@/api/cms/category";

export default {
    name: "CategoryList",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: false,
            // 总条数
            total: 0,
            // 资讯分类表格数据
            categoryList: [],
            moduleList: [
                { module: '1', moduleName: '文章' },
                { module: '2', moduleName: '链接' },
            ],
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
                parentId: [{ required: true, message: "父级编号不能为空", trigger: "blur" }],
                parentIds: [{ required: true, message: "所有父级编号不能为空", trigger: "blur" }],
                name: [{ required: true, message: "栏目名称不能为空", trigger: "blur" }],
                delFlag: [{ required: true, message: "删除标记不能为空", trigger: "blur" }],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询资讯分类列表 */
        getList() {
            this.loading = true;
            listCategory(this.queryParams).then(response => {
                let resData = response.rows;
                let seniorList = resData.filter(m => !m.parentId)

                this.categoryList = seniorList.map(item => {
                    item.children = resData.filter(f => f.parentId == item.id)
                    return item
                });
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
                parentId: null,
                parentIds: null,
                tenantId: null,
                module: '1',
                name: null,
                image: null,
                description: null,
                keywords: null,
                isShow: null,
                sort: null,
                patientSort: null,
                minSort: null,
                allowComment: null,
                isAudit: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null,
                delFlag: null,
                positionType: null
            };
            this.resetForm("form");
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getCategory(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改资讯分类";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateCategory(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addCategory(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
    }
};
</script>