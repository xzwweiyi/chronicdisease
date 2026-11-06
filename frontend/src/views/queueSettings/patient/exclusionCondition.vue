<template>
    <el-scrollbar class="six_conditions" v-loading="loading">
        <div class="condition_content">
            <div class="conditions_retrieval">
                <div class="enrollment_time">
                    <div class="enrollment_time_label">纳排条件发生时间</div>
                    <el-tag class="condition_interval">诊断时间：</el-tag>
                    <el-date-picker v-model="queryParams.enrollmentTimeStart" type="date" format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd" placeholder="开始时间" :picker-options="startPickerOptions"
                        class="condition_dateRangeVal">
                    </el-date-picker> -
                    <el-date-picker v-model="queryParams.enrollmentTimeEnd" type="date" format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd" placeholder="结束时间" :picker-options="endPickerOptions"
                        class="condition_dateRangeVal">
                    </el-date-picker>
                    <el-button type="primary" @click="addCondition">添加新诊断</el-button>
                </div>
                <el-table :data="queryParams.paramList" :height="filterHeight" border>
                    <el-table-column align="center" width="80" label="序号" type="index"></el-table-column>
                    <el-table-column align="center" width="110" label="逻辑关系">
                        <template slot-scope="scoped">
                            <el-select v-model="scoped.row.relation" v-if="scoped.$index!=0">
                                <el-option value="AND" label="AND"></el-option>
                                <el-option value="OR" label="OR"></el-option>
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" width="150" label="检索主题">
                        <template slot-scope="scoped">
                            {{ scoped.row.tableName }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" width="150" label="检索条件">
                        <template slot-scope="scoped">
                            <el-select v-model="scoped.row.condition"
                                v-if="scoped.row.fieldType=='数值型'||scoped.row.fieldType=='日期型'">
                                <el-option value="区间" label="区间" />
                                <el-option value="等于" label="等于" />
                                <el-option value="大于" label="大于" />
                                <el-option value="大于等于" label="大于等于" />
                                <el-option value="小于" label="小于" />
                                <el-option value="小于等于" label="小于等于" />
                            </el-select>
                            <el-select v-model="scoped.row.condition" v-else-if="scoped.row.fieldType=='字符型'">
                                <el-option value="包含" label="包含" />
                                <el-option value="不包含" label="不包含" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="值域范围">
                        <template slot-scope="scoped">
                            <div class="condition_value_content" v-if="scoped.row.fieldType=='数值型'">
                                <el-input v-model="scoped.row.value" placeholder="请输入" />
                                <template v-if="scoped.row.condition=='区间'">
                                    <span class="condition_interval">—</span>
                                    <el-input v-model="scoped.row.endValue" placeholder="请输入" />
                                </template>
                            </div>
                            <div class="condition_value" v-else-if="scoped.row.fieldType=='字符型'">
                                <el-input v-model="scoped.row.value" placeholder="请输入" />
                            </div>
                            <div class="condition_value" v-else-if="scoped.row.fieldType=='日期型'">
                                <el-date-picker v-model="scoped.row.value" type="date" class="condition_datepicker"
                                    placeholder="开始日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd" />
                                <template v-if="scoped.row.condition=='区间'">
                                    <span class="condition_interval">-</span>
                                    <el-date-picker v-model="scoped.row.endValue" type="date"
                                        class="condition_datepicker" placeholder="开始日期" format="yyyy-MM-dd"
                                        value-format="yyyy-MM-dd" />
                                </template>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" width="100">
                        <template slot-scope="scoped">
                            <el-button type="danger" plain icon="el-icon-minus" @click="deleteCondition(scoped.$index)"
                                class="condition_delBtn"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="searches_val"></div>
                <div class="search_buttons">
                    <el-button type="primary" class="search_button" icon="el-icon-search"
                        @click="handleQuery">检索</el-button>
                </div>
            </div>
            <!-- 表格风格 -->
            <div class="enrollment_tips">当前可加入患者 共 <span class="enrollment_total">{{ total }}</span></div>
            <el-table :data="enrollmentList">
                <el-table-column label="患者ID" align="center" width="100">
                    <template slot-scope="scoped">
                        {{ scoped.row.patientId | nullFormat }}
                    </template>
                </el-table-column>
                <el-table-column label="患者姓名" align="center" width="140">
                    <template slot-scope="scoped">
                        {{ scoped.row.name | nullFormat }}
                    </template>
                </el-table-column>
                <el-table-column label="性别" align="center" prop="sex" width="60">
                    <template slot-scope="scoped">
                        {{ scoped.row.sex | nullFormat }}
                    </template>
                </el-table-column>
                <el-table-column label="出生日期" align="center" width="110">
                    <template slot-scope="scoped">
                        {{ scoped.row.birthday | dateFormat('yyyy-MM-DD') }}
                    </template>
                </el-table-column>
                <el-table-column label="检索内容" align="center" min-width="180">
                    <template slot-scope="scoped">
                        {{ scoped.row.searchText | nullFormat }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="180">
                    <template slot-scope="scoped">
                        <el-button type="text" @click="toPatientDetils(scoped.row)">查看患者详情</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" :page.sync="pageData.pageNum" :limit.sync="pageData.pageSize"
                @pagination="getConditionList" />
            <div class="dialog-footer">
                <el-button type="primary" @click="nextSubmitSeven('save')">保 存</el-button>
                <el-button type="primary" @click="nextSubmitSeven('next')">保存并进入下一步</el-button>
            </div>

        </div>
    </el-scrollbar>
</template>

<script>
import {
    getEnrollmentConditionList,
    saveEnrollmentCondition,
    getEnrollmentCondition,
    updateStageData,
    getEnrollmentConditionSearchTree
} from "@/api/info/config";
import comFilters from "@/filters/index"
import moment from "moment"

export default {
    name: "patient_import",
    data() {
        return {
            // 遮罩层
            loading: false,
            conditionSearchTrees: [],
            defaultProps: {
                children: 'childrenList',
                label: 'tableName'
            },
            filterTreeText: '',
            filterHeight: 'calc(48vh - 100px)',
            // 总条数
            total: 0,
            clinicDateRange: [],
            // 查询缓存
            queryParams: {
                enrollmentTimeStart: '',
                enrollmentTimeEnd: '',
                paramList: []
            },
            pageData: {
                pageNum: 1,
                pageSize: 10,
            },
            enrollmentList: [],
            researchId: '',
            sectionList: [],
        };
    },
    created() {
        this.researchId = this.$route.query.researchId
        this.initData()
    },
    watch: {
        filterTreeText(val) {
            this.$refs.conditionTree.filter(val);
        }
    },
    computed: {
        startPickerOptions() {
            let endDate = this.queryParams.enrollmentTimeEnd
            return {
                disabledDate(time) {
                    if (endDate) {
                        return moment(time).valueOf() > moment(endDate).valueOf()
                    }else{
                        return false
                    }
                }
            }
        },
        endPickerOptions() {
            let startDate = this.queryParams.enrollmentTimeStart;
            return {
                disabledDate(time) {
                    if (startDate) {
                        return moment(time).valueOf() < moment(startDate).valueOf()
                    }else{
                        return false
                    }
                }
            }
        }
    },
    methods: {
        initData() {
            this.getConditionSearchTree();
            this.getConditionInfo();
        },
        convertMonthDate(value) {
            let date = new Date(value)
            let year = date.getFullYear()
            let month = date.getMonth() + 1
            month = month < 10 ? ('0' + month) : month
            let curTimeStr = year + "-" + month
            let curTime = new Date(curTimeStr)
            return curTime.getTime()
        },
        /**
         * 获取检索树
         */
        getConditionSearchTree() {
            getEnrollmentConditionSearchTree().then(res => {
                this.conditionSearchTrees = res
            })
        },
        filterTreeNode(value, data, node) {
            // if (!value) return true;
            // return data.label.indexOf(value) !== -1;
            if (!value) return true;
            let parentNode = node.parent,
                labels = [node.label],
                level = 1
            while (level < node.level) {
                labels = [...labels, parentNode.label]
                parentNode = parentNode.parent
                level++
            }
            return labels.some(label => label.indexOf(value) !== -1)
        },
        treeNodeClick(node) {
            if (!node.childrenList || !node.childrenList.length) {
                this.queryParams.paramList.push({
                    relation: node.relation,
                    field: node.field,
                    tableCode: node.tableCode,
                    tableName: node.tableName,
                    fieldName: node.fieldName,
                    condition: node.condition,
                    value: node.value,
                    endValue: node.endValue,
                    fieldType: node.fieldType
                })
            }
        },
        deleteCondition(index) {
            this.queryParams.paramList.splice(index, 1)
        },
        addCondition() {
            let params = {
                condition: '包含', field: 'diagnosisDesc', fieldName: '诊断名称', value: '', relation: 'AND',
                tableName: '诊断名称', fieldType: '字符型',
            }
            this.queryParams.paramList.splice(this.queryParams.paramList.length, 0, params)
        },
        /**
         * 获取 队列纳排条件
         */
        getConditionInfo() {
            let params = {
                researchId: this.researchId
            }
            getEnrollmentCondition(params).then(res => {
                this.queryParams.enrollmentTimeStart = res.enrollmentTimeStart ? comFilters.dateFormat(res
                    .enrollmentTimeStart,
                    'yyyy-MM-DD') : ''
                this.queryParams.enrollmentTimeEnd = res.enrollmentTimeEnd ? comFilters.dateFormat(res
                    .enrollmentTimeEnd,
                    'yyyy-MM-DD') : ''
                this.queryParams.paramList = res.paramList || []
                this.pageData.pageNum = 1;
                this.getConditionList();
            })
        },
        /** 电子档案-检索 */
        handleQuery() {
            this.pageData.pageNum = 1;
            this.getConditionList();
        },
        // 电子档案--患者列表
        getConditionList() {
            let paramsData = {
                ...this.queryParams,
                ...this.pageData,
                researchId: this.researchId
            }
            this.loading = true;
            getEnrollmentConditionList(paramsData).then(response => {
                let resData = response.data
                this.enrollmentList = resData.data;
                this.total = resData.total||0
                this.loading = false;
            }).catch(error => {
                this.loading = false;
            })
        },
        handleSubmit(valData) {
            this.nextSubmitSeven(valData)
        },
        toPatientDetils(item) {
            this.$router.push({
                path: '/workstation/patients/opinionDetail',
                query: {
                    personId: item.patientId,
                    researchId: this.researchId,
                    recordId: item.recordId,
                    hideQueueTree: true,
                    oldPath:this.$route.fullPath
                }
            })
        },
        /**
         * 保存
         */
        nextSubmitSeven(valData) {
            if (!this.queryParams.enrollmentTimeEnd && !this.queryParams.enrollmentTimeStart) {
                this.queryParams.enrollmentTimeEnd = moment().endOf('day').format('yyyy-MM-DD')
                this.queryParams.enrollmentTimeStart = moment().subtract(3, 'years').endOf('day').format(
                    'yyyy-MM-DD')
            }
            let params = {
                ...this.queryParams,
                ...this.pageData,
                researchId: this.researchId
            }
            saveEnrollmentCondition(params).then(res => {
                if (res) {
                    if (valData == 'save') {
                        this.$message.success('保存成功')
                        let oldPath = decodeURIComponent(this.$route.query.oldPath)
                        this.$tab.closePage(this.$route)
                        this.$router.push({
                            path: oldPath.replace(/stage=[0-9]/g, 'stage=5')
                        })
                    } else if (valData == 'next') {
                        this.nextSubmitSevenSave()
                    }
                }
            })
        },
        /**
         * /保存下一步
         */
        nextSubmitSevenSave() {
            let stage = '6'
            updateStageData(this.researchId, stage).then(res => {
                this.$message.success('保存成功')
                this.$tab.closePage(this.$route)
                let oldPath = decodeURIComponent(this.$route.query.oldPath)

                this.$router.push({
                    path: oldPath.replace(/stage=[0-9]/g, `stage=${stage}`)
                })
            })
        }
    },
};
</script>
<style lang='scss' scoped>
.six_conditions {
    margin: 15px 15px 0;
    display: flex;
    flex-direction: column;
    height: calc(100vh - 100px);
    padding-bottom: 0;

    .condition_content {
        padding: 20px 15px;
        background: #fff;
    }

    .conditions_retrieval {
        height: 48vh;

        .enrollment_time {
            display: flex;
            color: #333;
            align-items: center;
            height: 50px;

            .condition_dateRangeVal {
                width: 150px;
                margin: 0 5px;
            }

            .condition_dateType {
                margin: 0 15px;
            }
        }

        .search_buttons {
            text-align: center;
            margin-top: 10px;

            .search_button {
                width: 200px;
            }
        }

        .condition_value_content {
            display: flex;
        }

        .condition_delBtn {
            font-size: 12px;
            padding: 2px 5px;
        }
        .condition_interval {
            margin: 0 5px;
        }
        .condition_datepicker {
            width: 160px;
        }
    }

    .enrollment_tips {
        display: flex;
        align-items: center;
        line-height: 50px;
        text-indent: 5px;

        .enrollment_total {
            color: #fada06;
            font-size: 18px;
            margin-left: 5px;
        }
    }

    .dialog-footer {
        text-align: center;
        margin: 10px 0 20px;
    }
}
</style>