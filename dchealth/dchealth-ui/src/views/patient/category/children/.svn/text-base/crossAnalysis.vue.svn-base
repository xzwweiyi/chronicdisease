<!-- 交叉分析列表 -->
<template>
    <div>
        <el-row>
            <el-col :span="12" class="analysis_content">
                <p class="analysis_title">自变量 X </p>
                <el-select v-model="valueX" clearable>
                    <el-option v-for="(item,index) in analysisData" :key="index" :label="item.propertyName"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="12" class="analysis_content">
                <p class="analysis_title">因变量 Y </p>
                <el-select v-model="valueY" clearable>
                    <el-option v-for="(item,index) in analysisData" :key="index" :label="item.propertyName"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="12" class="analysis_button">
                <el-button type="primary" plain @click="searchAnalysisCount">交叉分析</el-button>
            </el-col>
        </el-row>
        <div>
            <el-table :data="tableData" border style="width: 100%" v-show="isShowTable"
                :default-sort="{ prop: 'value', order: 'descending' }">
                <div>
                    <el-table-column :label="item" v-for="(item,index) in tableHeader" :key="index">
                        <template slot-scope="scope">
                            {{scope.row[index] ? scope.row[index]: " "}}
                        </template>
                    </el-table-column>

                </div>
                <el-table-column label="小计">
                    <template slot-scope="scope">
                        {{scope.row[tableHeader.length]}}
                    </template>
                </el-table-column>
            </el-table>
            <!-- 统计图 -->
            <analysis-child :echarts-data="echartsData" v-if="echartsData.length" @changeShowTable="changeIsShowTable">
            </analysis-child>
        </div>

    </div>
</template>

<script>
    import AnalysisChild from './child/analysisChild'
    import {
        searchAnalysis
    } from "@/api/patient/config";
    export default {
        name: "tabstatisticsChildData",
        components: {
            AnalysisChild,
        },
        data() {
            return {
                optionList: [],
                isShowTable: true,
                valueX: '',
                valueY: '',
                tableData: [],
                tableHeader: [],
                echartsData: [],
            }
        },
        props: ['id', 'analysisData'],
        computed: {},
        watch: {
            valueX(newVal) {
                if (newVal && newVal == this.valueY) {
                    this.$message.warning('题目不能重复')
                    this.valueX = ''
                }
            },
            valueY(newVal) {
                if (newVal && newVal == this.valueX) {
                    this.$message.warning('题目不能重复')
                    this.valueY = ''
                }

            }
        },
        created() {},
        methods: {
            changeIsShowTable(event) {
                this.isShowTable = event
            },
            searchAnalysisCount() {
                let params = {
                    medicalFormId: this.id,
                    xList: this.valueX,
                    yList: this.valueY
                    // medicalFormId: 227,
                    // xList: 186,
                    // yList: 187
                }
                searchAnalysis(params).then(response => {
                    let responseData = response.data[0];
                    let tableList = []
                    if (responseData && responseData.table && responseData.table.length) {
                        tableList = responseData.table.slice(1, responseData.table.length - 1)
                        tableList.forEach(item => {
                            let total = 0
                            total = this.sum(item.slice(1, item.length))
                            item.push(total)
                        })
                    }
                    this.tableData = tableList;
                    this.tableHeader = responseData && responseData.table && responseData.table.length ?
                        responseData.table[0] : [];
                    this.echartsData = responseData&&responseData.xline6?responseData.xline6:[]
                })
            },
            sum(arr) {
                return eval(arr.join("+"));
            },

        },
    };
</script>
<style lang="scss" scoped>
    .analysis_title {
        font-size: 14px;
        line-height: 28px;
        color: #333;
    }

    .analysis_button {
        margin: 10px 0;
    }

    .analysis_content {
        padding-right: 20px;

        ::v-deep .el-select {
            width: 100%;
        }

        &:last-child {
            padding-right: 0;
        }
    }
</style>