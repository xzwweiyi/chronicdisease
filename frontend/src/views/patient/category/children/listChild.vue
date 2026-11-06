<!-- 已答问卷列表 -->
<template>
    <div>
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="标题" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 200px"
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row class="mb8">
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
        <el-table v-loading="loading" :data="userList">
            <el-table-column label="主键ID" align="center" key="id" prop="id" width="100" />
            <el-table-column label="标题" align="center" min-width="350" key="title" prop="title"
                :show-overflow-tooltip="true" />
            <el-table-column label="更新时间" align="center" key="updateTime" width="200" prop="updateTime"
                :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    {{scope.row.updateTime || '--'}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button type="text" @click="toDetails(scope.row)">查看详情</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="isShowDetails" width="78%" center v-loading="isLoadingDetails"
            top="8vh" :title="formList.medicalName">
            <el-scrollbar class="details-scrollbar">
                <DetailInfo :form-list="formList.subjectList" :error-message="errorMessage"></DetailInfo>
            </el-scrollbar>
        </el-dialog>
    </div>
</template>

<script>
    import {
        questionnaireAnsweredDetails
    } from '@/api/patient/config'
    import DetailInfo from './details.vue'
    export default {
        name: "listData",
        components: {
            DetailInfo
        },
        data() {
            return {
                // 遮罩层
                loading: false,
                // 显示搜索条件
                showSearch: true,
                // 总条数
                // total: 0,
                // 查询参数
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                    title: undefined, // 标题
                    categoryId: '',
                },
                // userList: [], //患教列表
                isShowDetails: false,
                isLoadingDetails: false,
                formList: [],
                errorMessage: '',
            };
        },
        props: ['total', 'userList', 'id'],
        computed: {},
        watch: {},
        created() {},
        methods: {
            /** 查询用户列表 */
            getList() {
                // console.log('serachList')
                this.$emit('serachList', this.queryParams)
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
            // 查看详情
            toDetails(event) {
                this.isShowDetails = true;
                this.isLoadingDetails = true;
                let params = {
                    articleId: this.id,
                    examRecordId: event.id
                }
                this.formList = []
                questionnaireAnsweredDetails(params).then(res => {
                    this.isLoadingDetails = false;
                    let resData = res.data;
                    if (resData) {
                        if (resData.subjectList && resData.subjectList.length) {
                            resData.subjectList = resData.subjectList.map(item => {
                                const {
                                    attribute,
                                    ...otherInfo
                                } = item
                                return {
                                    ...otherInfo,
                                    ...JSON.parse(attribute),
                                    disabled: true,
                                }
                            })
                        } else {
                            this.errorMessage = '暂无问卷信息'
                        }
                        this.formList = resData;
                    } else {
                        this.errorMessage = res.errorMessage
                    }
                })
            },
        }
    };
</script>
<style lang='scss' scoped>
    .app-container {
        background-color: #fff !important;
    }

    .details-scrollbar {
        height: 70vh;
    }

    ::v-deep .el-scrollbar__wrap {
        overflow-x: hidden !important;
        margin-bottom: 0 !important;
    }
</style>