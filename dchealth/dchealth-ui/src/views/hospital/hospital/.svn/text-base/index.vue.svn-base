<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="中心名称" prop="hospitalName">
                <el-input v-model="queryParams.hospitalName" placeholder="请输入中心名称" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="中心编号" prop="hospitalCode">
                <el-input v-model="queryParams.hospitalCode" placeholder="请输入中心编号" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="省份" prop="provinceId">
                <el-select v-model="queryParams.provinceId" placeholder="请选择省份" clearable @change="selectChange">
                    <el-option v-for="(item,index) of provinceList" :key="index" :label="item.areaName"
                        :value="item.areaId">{{item.areaName}}</el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="城市" prop="cityId">
                <el-select v-model="queryParams.cityId" placeholder="请选择城市" clearable @change="selectChange">
                    <el-option v-for="(item,index) of cityList" :key="index" :label="item.areaName"
                        :value="item.areaId">{{item.areaName}}</el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="区县" prop="areaId">
                <el-select v-model="queryParams.areaId" clearable placeholder="请选择区县">
                    <el-option v-for="(item,index) of areaList" :key="index" :label="item.areaName"
                        :value="item.areaId">{{item.areaName}}</el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <div class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                v-hasPermi="['system:hospital:add']">新增</el-button>
            <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['system:hospital:remove']">删除</el-button>
            <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                v-hasPermi="['system:hospital:export']">导出</el-button> -->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </div>

        <el-table v-loading="loading" :data="hospitalList" @row-click="clickRow" ref="selectTable" class="select_table_style"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" align="center" />
            <el-table-column label="主键" align="center" width="80" prop="id" />
            <el-table-column label="中心名称" align="center" min-width="260" prop="hospitalName" />
            <el-table-column label="中心编号" align="center" min-width="100" prop="hospitalCode">
                <template slot-scope="scope">
                    {{scope.row.hospitalCode | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="省份" align="center" width="100" prop="provinceName">
                <template slot-scope="scope">
                    {{scope.row.provinceName | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="城市" align="center" width="100" prop="areaName">
                <template slot-scope="scope">
                    {{scope.row.areaName | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="区县" align="center" width="100" prop="areaName">
                <template slot-scope="scope">
                    {{scope.row.areaName | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="地址" align="center" min-width="100" prop="address">
                <template slot-scope="scope">
                    {{scope.row.address | nullFormat}}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createDate" width="120">
                <template slot-scope="scope">
                    <span>{{ scope.row.createDate | dateFormat}}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="120" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['system:hospital:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['system:hospital:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改中心管理对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" class="cmElDialog" :close-on-click-modal="false"
            :close-on-press-escape="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="中心名称" prop="hospitalName">
                    <el-input v-model="form.hospitalName" placeholder="请输入中心名称" />
                </el-form-item>
                <el-form-item label="中心编号" prop="hospitalCode">
                    <el-input v-model="form.hospitalCode" placeholder="请输入中心编号" />
                </el-form-item>
                <el-form-item label="选择地区" prop="provinceId">
                    <el-select v-model="form.provinceId" placeholder="请选择省份" clearable class="selectStyle"
                        @change="formSelectChange">
                        <el-option v-for="(item,index) of formProvinceList" :key="index" :label="item.areaName"
                            :value="item.areaId">{{item.areaName}}</el-option>
                    </el-select>
                    <el-select v-model="form.cityId" placeholder="请选择城市" clearable class="selectStyle"
                        @change="formSelectChange">
                        <el-option v-for="(item,index) of formCityList" :key="index" :label="item.areaName"
                            :value="item.areaId">{{item.areaName}}</el-option>
                    </el-select>
                    <el-select v-model="form.areaId" placeholder="请选择县区" class="selectStyle" clearable>
                        <el-option v-for="(item,index) of formAreaList" :key="index" :label="item.areaName"
                            :value="item.areaId">{{item.areaName}}</el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="地址" prop="address">
                    <el-input v-model="form.address" placeholder="请输入地址" />
                </el-form-item>
                <el-form-item label="中心介绍" prop="hospitalIntro">
                    <el-input v-model="form.hospitalIntro" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="中心图片">
                    <image-upload v-model="form.hospitalImage" />
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
import {
    listHospital, getHospital, delHospital, addHospital, updateHospital, getAreaList
} from "@/api/hospital/hospital";

export default {
    name: "Hospital",
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
            // 中心管理表格数据
            hospitalList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1, pageSize: 10, hospitalCode: null, hospitalName: null,
                areaId: null, cityId: null, provinceId: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                hospitalCode: [{ required: true, message: "中心编号不能为空", trigger: "blur" }],
                hospitalName: [{ required: true, message: "中心名称不能为空", trigger: "blur" }]
            },
            // 省列表
            provinceList: [],
            // 城市列表
            cityList: [],
            // 区县列表
            areaList: [],
            // 编辑和新增的 省列表
            formProvinceList: [],
            // 编辑和新增的 城市列表
            formCityList: [],
            // 编辑和新增的 区县列表
            formAreaList: [],
        };
    },
    created() {
        this.getList();
        this.getAreaData()
    },
    methods: {
        /** 查询中心管理列表 */
        getList() {
            this.loading = true;
            listHospital(this.queryParams).then(response => {
                this.hospitalList = response.rows;
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
                hospitalCode: null,
                hospitalName: null,
                provinceId: null,
                cityId: null,
                areaId: null,
                hospitalIntro: null,
                hospitalImage: null,
                address: null,
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
            this.title = "添加中心管理";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getHospital(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改中心管理";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateHospital(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addHospital(this.form).then(response => {
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
            this.$modal.confirm('是否确认删除中心管理编号为"' + ids + '"的数据项？').then(function () {
                return delHospital(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/hospital/export', {
                ...this.queryParams
            }, `hospital_${new Date().getTime()}.xlsx`)
        },
        selectChange(event) {
            if (event) {
                this.getAreaData(event, 'queryParams')
            } else {
                if (!this.queryParams.provinceId) {
                    this.queryParams.cityId = '';
                    this.queryParams.areaId = '';
                    this.cityList = [];
                    this.areaList = [];
                }
                if (!this.queryParams.cityId) {
                    this.queryParams.areaId = '';
                    this.areaList = [];
                }
            }
        },
        formSelectChange(event) {
            if (event) {
                this.getAreaData(event, 'form')
            } else {
                if (!this.form.provinceId) {
                    this.form.cityId = '';
                    this.form.areaId = '';
                    this.formCityList = [];
                    this.formAreaList = [];
                }
                if (!this.form.cityId) {
                    this.form.areaId = '';
                    this.formAreaList = [];
                }
            }
        },
        getAreaData(areaId, type) {
            getAreaList(areaId).then(response => {
                if (!areaId) {
                    this.formProvinceList = this.provinceList = response
                }
                if (type == 'queryParams') {
                    if (this.queryParams.provinceId && this.queryParams.provinceId == areaId) {
                        this.cityList = response
                    } else if (this.queryParams.cityId && this.queryParams.cityId == areaId) {
                        this.areaList = response
                    }
                } else {
                    if (this.form.provinceId && this.form.provinceId == areaId) {
                        this.formCityList = response
                    } else if (this.form.cityId && this.form.cityId == areaId) {
                        this.formAreaList = response
                    }
                }
            })
        }
    }
};
</script>
<style>
.selectStyle {
    width: 200px;
    margin-right: 10px;
}

.selectStyle:last-child {
    margin-right: 0;
}
</style>