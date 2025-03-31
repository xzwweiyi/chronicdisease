<!-- 统计分析 -->
<template>
    <div class="statisticsUl">
        <div class="statisticsLi" v-for="(item, index) in statisiucsListData" :key="index">
            <div class="listText">
                第{{ index + 1 }}题：{{ item.propertyName }}
            </div>
            <el-table :data="item.tableData" border style="width: 100%" show-summary :summary-method="getSummaries"
                :default-sort="{ prop: 'value', order: 'descending' }" v-show="item.isShowTable">
                <el-table-column label="选项" prop="name" sortable></el-table-column>
                <el-table-column label="小计" prop="value" sortable></el-table-column>
                <el-table-column label="比例" prop="propertyValue">
                    <template slot-scope="scope">
                        <el-progress :percentage="scope.row.propertyValue?scope.row.propertyValue:0"></el-progress>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 统计图 -->
            <statistics-child :option-data-child="item.xline6" :index="index" @changeShowTable="changeIsShowTable">
            </statistics-child>
        </div>

    </div>
</template>

<script>
    import statisticsChild from './child/statisticsChild'
    export default {
        name: "tabstatisticsChildData",
        components: {
            statisticsChild,
        },
        data() {
            return {
                // 遮罩层
                loading: false,
                isShowTable: true,
            }
        },
        props: ['statisiucsListData'],
        computed: {},
        watch: {},
        created() {},
        methods: {
            getSummaries(param) {
                const {
                    columns,
                    data
                } = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '本题有效填写人次';
                        return;
                    } else if (index === 2) {
                        sums[index] = ''
                        return
                    }
                    const values = data.map(item => Number(item[column.property]));
                    if (!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr);
                            if (!isNaN(value)) {
                                return prev + curr;
                            } else {
                                return prev;
                            }
                        }, 0);
                    }
                });
                return sums;
            },
            changeIsShowTable(event, index) {
                this.statisiucsListData[index]['isShowTable'] = event
            }

        },
    };
</script>
<style lang="scss" scoped>
    .statisticsTitle {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: center;
        font-size: 24px;
        line-height: 1.5;
        font-weight: bold;
    }

    .statisticsLi {
        display: flex;
        flex-direction: column;
        padding: 10px 0;

        .listText {
            font-size: 14px;
            line-height: 2;
        }
    }
</style>