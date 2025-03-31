<template>
    <div class="echarts-container">
        <div class="custom_chart_content" v-if="chartOptions">
            <el-checkbox-group v-model="checkedLegend" @change="legendChange" class="legend_list">
                <el-checkbox v-for="(item,index) in legendList" :key="index" :label="item.value">
                    <div class="legend_item_label">
                        <span class="legend_item_style"
                            :style="{backgroundColor:item.checked?item.color:'#ccc'}"></span>
                        <p :style="{color:item.checked?'#333':'#ccc'}">{{ item.value }}</p>
                    </div>
                </el-checkbox>
            </el-checkbox-group>
            <e-charts class="echarts custom_chart" :option="chartOptions" :key="renderKey" autoresize theme="light">
            </e-charts>
        </div>
        <div class="noCount" v-else>
            <p>暂无数据</p>
        </div>
    </div>
</template>

<script>
import * as echarts from 'echarts';
import ECharts from "vue-echarts";
export default {
    name: 'chart',
    components: {
        ECharts
    },
    props: ['optionsData', 'colors', 'legendData'],
    data() {
        return {
            checkedLegend: [],
            legendList: [],
            chartOptions: null,
            renderKey: 0
        }
    },
    watch: {
        optionsData(newVal) {
            if (newVal) {
                this.chartOptions = JSON.parse(JSON.stringify(newVal))
                this.chartOptions.series[0].label = {
                    ...newVal.series[0].label
                }
                this.chartOptions.tooltip = {
                    ...newVal.tooltip
                }
            } else {
                this.chartOptions = null
            }
        },
        legendData(newVal) {
            this.legendList = [...newVal.map(m => m)]
            this.checkedLegend = newVal.map(m => m.value)
        }
    },
    computed: {},
    methods: {
        legendChange(event) {
            let chartDataOption = JSON.parse(JSON.stringify(this.optionsData))
            chartDataOption.series[0].label = {
                ...this.optionsData.series[0].label
            }
            chartDataOption.tooltip = {
                ...this.optionsData.tooltip
            }
            this.legendList.forEach(item => {
                if (this.checkedLegend.findIndex(f => f == item.value) > -1) {
                    item.checked = true
                } else {
                    item.checked = false
                }
            })
            let seriesData = chartDataOption.series[0].data;

            chartDataOption.series[0].data = event.length ? seriesData.filter(f => event.findIndex(eventF =>
                eventF == f.name) > -1) : []
            this.chartOptions = chartDataOption
            this.renderKey++
        },
    }
}
</script>

<style lang="scss" scoped>
.noCount,
.echarts,
.echarts-container {
    width: 100%;
    height: 100%;
    min-height: 300px;
}

.custom_chart_content {
    height: 100%;

    .legend_list {
        padding: 0 10px 10px;

        ::v-deep.el-checkbox {
            margin-right: 5px;
            margin-bottom: 5px;

            .el-checkbox__input {
                display: none;
            }

            .el-checkbox__label {
                margin-left: 0;
                color: #333;
            }

            .legend_item_label {
                display: flex;
                align-items: center;

                .legend_item_style {
                    width: 25px;
                    height: 14px;
                    display: inline-block;
                    border: 2px #666 solid;
                    border-radius: 5px;
                    margin-right: 5px;
                }
            }
        }
    }

    .custom_chart {
        height: 45vh;
    }
}

.noCount {
    background: #f8f8f8;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #666;
}
</style>