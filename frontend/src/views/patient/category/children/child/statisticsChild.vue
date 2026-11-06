<template>
    <div class="echarts_container">
        <el-checkbox-button label="表格" v-model="isShowTable" @change="changeIsShowTable">表格</el-checkbox-button>
        <el-radio-group v-model="echartActived" @change="echartTypeChange">
            <el-radio-button v-for="item in chartType" :key="item.value" :label="item.value">
                {{item.label}}
            </el-radio-button>
        </el-radio-group>
        <div v-show="echartActived==1" class="chart_component">
            <ring-echart :optionsData="pieOption"></ring-echart>
        </div>
        <div v-show="echartActived==2" class="chart_component">
            <ring-echart :optionsData="pieCenterOption"></ring-echart>
        </div>
        <div v-show="echartActived==3" class="chart_component">
            <ring-echart :optionsData="barVerticalOption"></ring-echart>
        </div>
        <div v-show="echartActived==4" class="chart_component">
            <ring-echart :optionsData="barHorizontalOption"></ring-echart>
        </div>
        <div v-show="echartActived==5" class="chart_component">
            <ring-echart :optionsData="lineOption"></ring-echart>
        </div>
    </div>
</template>

<script>
    import mixOptions from '@/views/components/options/mix_option'
    import ringEchart from '@/views/components/echarts/index'
    export default {
        name: "statisticsChild",
        props: ['optionDataChild','index'],
        components: {
            ringEchart
        },
        data() {
            return {
                // 遮罩层
                isShowTable: true,
                echartActived: 1,
                chartType: [{
                        label: '饼状图',
                        value: 1
                    },
                    {
                        label: '环形图',
                        value: 2
                    },
                    {
                        label: '柱状图',
                        value: 3
                    },
                    {
                        label: '条形图',
                        value: 4
                    },
                    {
                        label: '折线图',
                        value: 5
                    }
                ],
                pieOption: null,
                pieCenterOption: null,
                barVerticalOption: null,
                barHorizontalOption: null,
                lineOption: null,
            };
        },
        computed: {},
        watch: {
        },
        mounted() {
            this.echartTypeChange(this.echartActived)
        },
        methods: {
            changeIsShowTable(event){
                this.$emit('changeShowTable',event,this.index)
            },
            echartTypeChange(event) {
                if (event == 1) {
                    let options = mixOptions.getPieOption()
                    options.series[0].data = this.optionDataChild;
                    this.pieOption = options
                } else if (event == 2) {
                    let options = mixOptions.getPieOption()
                    options.series[0].radius = ['26%', '60%'];
                    options.series[0].data = this.optionDataChild;
                    this.pieCenterOption = options
                } else if (event == 3) {
                    let options = mixOptions.getColumnarOption()
                    options.xAxis.data = this.optionDataChild.map(m => m.name)
                    options.series[0].data = this.optionDataChild;
                    this.barVerticalOption = options
                } else if (event == 4) {
                    let options = mixOptions.getColumnarOption()
                    options.yAxis.type = 'category';
                    options.xAxis.type = 'value';
                    options.series[0].data = this.optionDataChild;
                    options.yAxis.data = this.optionDataChild.map(m => m.name)
                    this.barHorizontalOption = options;
                } else if (event == 5) {
                    let options = mixOptions.getLineOption()
                    options.series[0].data = this.optionDataChild;
                    options.xAxis.data = this.optionDataChild.map(m => m.name)
                    this.lineOption = options
                }
            },
        },
    };
</script>
<style lang="scss" scoped>
    .echarts_container {
        margin-top: 10px;

        .chart_component {
            width: 100%;
            height: 300px;
            margin-top: 20px;
        }
    }
</style>