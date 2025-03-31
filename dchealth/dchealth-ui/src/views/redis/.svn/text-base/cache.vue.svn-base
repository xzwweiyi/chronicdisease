<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="主机" prop="host">
                <el-select v-model="queryParams.id" placeholder="主机地址" @change="handSelect" clearable
                    style="width: 240px">
                    <el-option v-for="dict in redisData" :key="dict.id" :label="dict.host" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="DB" prop="db">
                <el-select v-model="queryParams.db" placeholder="请选择" clearable style="width: 240px">
                    <el-option v-for="dbItem in dbData" :key="dbItem.db" :label="dbItem.name" :value="dbItem.db" />
                </el-select>
            </el-form-item>
            <el-form-item label="key">
                <el-input v-model="queryParams.key" placeholder="请输入key值" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                    v-hasPermi="['system:config:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['system:config:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['system:config:remove']">删除</el-button>
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                    v-hasPermi="['system:config:export']">导出</el-button>
            </el-col> -->
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-refresh" @click="handleRefreshCache"
                    v-hasPermi="['system:config:remove']">刷新缓存</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <!-- 表格风格 -->
        <el-table v-loading="loading" :data="configList" @row-click="clickRow" ref="selectTable"
            class="select_table_style" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="类型" align="center" prop="dataType" width="160" />
            <el-table-column label="缓存键" align="center" prop="redisKey" :show-overflow-tooltip="true" />
            <el-table-column label="过期时间" align="center" prop="expire" />
            <el-table-column label="元素总数" align="center" prop="elCount" :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" @click.stop="handleUpdate(scope.row)"
                        v-hasPermi="['system:config:edit']">修改</el-button>
                    <el-button type="text" icon="el-icon-delete" @click.stop="handleDelete(scope.row)"
                        v-hasPermi="['system:config:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改缓存配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="1000px">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="类型" prop="dataType">
                    <el-select v-model="form.dataType" placeholder="类型" :disabled="disabledFlag" clearable
                        style="width: 30%">
                        <el-option v-for="item in datatypeData" :key="item.id" :label="item.label"
                            :value="item.label" />
                    </el-select>
                </el-form-item>
                <el-form-item label="缓存键" prop="redisKey">
                    <el-input v-model="form.redisKey" placeholder="请输入缓存键" @change="submitrRedis('name')" />
                    <!-- <el-button type="primary" @click="submitrRedisKey" v-if="form.id">更新缓存键</el-button> -->
                </el-form-item>
                <el-form-item label="缓存值" prop="redisValue">
                    <el-input type="textarea" :rows="5" v-model="form.redisValue" placeholder="请输入缓存键"
                        @change="submitrRedis('value')" />
                    <!-- <el-button type="primary" @click="submitRedisValue" v-if="form.id">更新缓存值</el-button> -->
                </el-form-item>
                <el-form-item label="过期时间" prop="expire">
                    <el-input v-model="form.expire" placeholder="请输入过期时间" @change="submitrRedis('expire')" />
                    <!-- <el-button type="primary" @click="submitExpire" v-if="form.id">更新过期时间</el-button> -->
                </el-form-item>
                <el-form-item label="主机名称" prop="id">
                    <el-select v-model="form.id" placeholder="主机地址" @change="handSelect" clearable
                        :disabled="disabledFlag" style="width: 30%">
                        <el-option v-for="dict in redisData" :key="dict.id" :label="dict.host" :value="dict.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="DB" prop="db">
                    <el-select v-model="form.db" placeholder="请选择" clearable :disabled="disabledFlag"
                        style="width: 30%">
                        <el-option v-for="dbItem in dbData" :key="dbItem.db" :label="dbItem.name" :value="dbItem.db" />
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" v-show="showBtn">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listRedis
} from "@/api/redis/config";
import {
    listCache,
    listDb,
    addCache,
    updateCahe,
    getCache,
    delCache,
    updateName,
    updateValue,
    updateExpire
} from "@/api/redis/cache";
import {
    refreshCache
} from '@/api/system/dict/type.js'

export default {
    name: "Cache",
    data() {
        return {
            // 回显不可编辑
            disabledFlag: false,
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
            // 缓存表格数据
            configList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询缓存
            queryParams: {
                pageNo: 1,
                pageSize: 10,
                id: undefined,
                db: undefined,
                keyword: undefined,
            },
            // 表单缓存
            form: {},
            redisData: [],
            dbData: [],
            datatypeData: [{
                label: 'hash',
                id: '1'
            },
            {
                label: 'string',
                id: '2'
            },
            {
                label: 'none',
                id: '3'
            },
            {
                label: 'list',
                id: '4'
            },
            {
                label: 'set',
                id: '5'
            },
            {
                label: 'zset',
                id: '6'
            },
            ],
            // 表单校验
            rules: {
                dataType: [{
                    required: true,
                    message: "类型不能为空",
                    trigger: "blur"
                }],
                redisKey: [{
                    required: true,
                    message: "缓存键不能为空",
                    trigger: "blur"
                }],
                redisValue: [{
                    required: true,
                    message: "缓存键不能为空",
                    trigger: "blur"
                }],
                expire: [{
                    required: true,
                    message: "过期时间不能为空",
                    trigger: "blur"
                }],
                id: [{
                    required: true,
                    message: "主机不能为空",
                    trigger: "blur"
                }],
                db: [{
                    required: true,
                    message: "db不能为空",
                    trigger: "blur"
                }]
            },
            showBtn: true,
        };
    },
    created() {
        this.getRedis()
        // this.getListDb()
    },
    methods: {
        /** 查询缓存列表 */
        getList() {
            this.loading = true;
            listCache(this.queryParams).then(response => {
                this.configList = response.rows;
                this.total = response.total;
            });
            this.loading = false;
        },
        handSelect(val) {
            return this.getListDb(val)
        },
        /** 查询缓存列表 */
        getRedis() {
            this.loading = true;
            listRedis().then(response => {
                this.redisData = response.rows;
                this.loading = false;
            });
        },

        getListDb(val) {
            listDb({
                id: val
            }).then(response => {
                this.dbData = response.data
            })
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                dataType: undefined,
                redisKey: undefined,
                redisValue: undefined,
                expire: undefined,
                id: undefined,
                db: undefined
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNo = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.queryParams.id = ''
            this.handleQuery();
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.showBtn = true;
            this.disabledFlag = false;
            this.title = "添加缓存";
        },
        clickRow(row) {
            this.$refs.selectTable.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            this.disabledFlag = true;
            this.showBtn = false;
            const id = this.queryParams.id;
            const db = this.queryParams.db;
            const type = row.dataType;
            const keyword = row.redisKey;
            getCache({
                id: id,
                db: db,
                type: type,
                keyword: keyword
            }).then(response => {
                this.form = response.data;
                this.form.id = this.queryParams.id;
                this.form.db = this.queryParams.db;
                this.open = true;
                this.title = "修改缓存";
            });
        },
        /** 提交按钮 */
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    addCache(this.form).then(response => {
                        this.$modal.msgSuccess("新增成功");
                        this.open = false;
                        this.getList();
                    });
                    //   if (this.form.dataType != undefined) {
                    //     updateRedis(this.form).then(response => {
                    //       this.$modal.msgSuccess("修改成功");
                    //       this.open = false;
                    //       this.getList();
                    //     });
                    //   } else {

                    //   }
                }
            });
        },
        // 更新缓存键
        submitrRedis: function (val) {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (val == 'name') {
                        updateName(this.form).then(response => {
                            this.$modal.msgSuccess("缓存键修改成功");
                            this.open = false;
                            this.queryParams.pageNo = 1;
                            this.getList();
                        })
                    } else if (val == 'value') {
                        updateValue(this.form).then(response => {
                            this.$modal.msgSuccess("缓存值修改成功");
                            this.open = false;
                            this.queryParams.pageNo = 1;
                            this.getList();
                        })
                    } else if (val == 'expire') {
                        updateExpire(this.form).then(response => {
                            this.$modal.msgSuccess("元素时间修改成功");
                            this.open = false;
                            this.queryParams.pageNo = 1;
                            this.getList();
                        })
                    }

                }
            })
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal.confirm('是否确认删除缓存编号为"' + ids + '"的数据项？').then(function () {
                return delCache(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/config/export', {
                ...this.queryParams
            }, `config_${new Date().getTime()}.xlsx`)
        },
        /** 刷新缓存按钮操作 */
        handleRefreshCache() {
            refreshCache().then(() => {
                this.$modal.msgSuccess("刷新成功");
            });
        }
    }
};
</script>