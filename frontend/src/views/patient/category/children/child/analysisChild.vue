<template>
    <div class="echarts_container">
        <el-checkbox-button label="表格" v-model="isShowTable" @change="changeIsShowTable">表格</el-checkbox-button>
        <el-radio-group v-model="echartActived" @change="echartTypeChange">
            <el-radio-button v-for="item in chartType" :key="item.value" :label="item.value">
                {{item.label}}
            </el-radio-button>
        </el-radio-group>
        <div v-show="echartActived==1" class="chart_component">
            <ring-echart :optionsData="barVerticalOption"></ring-echart>
        </div>
        <div v-show="echartActived==2" class="chart_component">
            <ring-echart :optionsData="barHorizontalOption"></ring-echart>
        </div>
        <div v-show="echartActived==3" class="chart_component">
            <ring-echart :optionsData="lineOption"></ring-echart>
        </div>
        <div v-show="echartActived==4" class="chart_component">
            <ring-echart :optionsData="radarOption"></ring-echart>
        </div>
    </div>
</template>

<script>
    import mixOptions from '@/views/components/options/mix_option'
    import ringEchart from '@/views/components/echarts/index'
    export default {
        name: "statisticsChild",
        props: ['echartsData'],
        components: {
            ringEchart
        },
        data() {
            return {
                // 遮罩层
                isShowTable: true,
                echartActived: 1,
                chartType: [
                    { label: '柱状图', value: 1 },
                    { label: '条形图', value: 2 },
                    { label: '折线图', value: 3 },
                    { label: '雷达图', value: 4 },
                ],
                barVerticalOption: null,
                barHorizontalOption: null,
                lineOption: null,
                radarOption:null,
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
                    let options = mixOptions.getColumnarOption()
                    options.xAxis.data = this.echartsData.map(m => m.name)
                    options.series[0].data = this.echartsData;
                    this.barVerticalOption = options
                } else if (event == 2) {
                    let options = mixOptions.getColumnarOption()
                    options.yAxis.type = 'category';
                    options.xAxis.type = 'value';
                    options.series[0].data = this.echartsData;
                    options.yAxis.data = this.echartsData.map(m => m.name)
                    this.barHorizontalOption = options;
                } else if (event == 3) {
                    let options = mixOptions.getLineOption()
                    options.series[0].data = this.echartsData;
                    options.xAxis.data = this.echartsData.map(m => m.name)
                    this.lineOption = options
                }else if(event == 4){
                    let options = mixOptions.getRadarOptions()
                    let valueList = this.echartsData.map(m=>m.value);
                    valueList.sort((a,b)=>{
                        return b-a
                    })
                    let maxList = this.echartsData.map(m=>{
                        return {
                            max:valueList[0]+10,
                            ...m
                        }
                    })
                    options.radar.indicator = maxList
                    options.series[0].data.push({
                        value:this.echartsData.map(m=>m.value),
                        name:''
                    })
                    options.series[0].name = ''
                    this.radarOption = options
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
            height: 360px;
            margin-top: 20px;
        }
    }
</style>