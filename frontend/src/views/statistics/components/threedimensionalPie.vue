<template>
    <div class="echarts-container">
        <div class="echarts" ref="pieRef"></div>
        <!-- <div class="noCount" v-else>
            <p>暂无数据</p>
        </div> -->
    </div>
</template>

<script>
import * as echarts from "echarts";
import "echarts-gl"; // 3d图表库
import { getPie3D, cylinderEcharts } from '@/views/components/options/3dpie'
export default {
    name: 'chart',
    components: {
        // ECharts
    },
    data() {
        return {
            option: null
            ,
            chartInstance: null,
            _resizeTimer: null
        }
    },
    props: ['optionsData'],
    watch: {
        optionsData(newVal) { 
            this.initChart()
        }
  },
    mounted() {
      
            window.addEventListener('resize', this.handleResize)
    },
        beforeDestroy() {
            window.removeEventListener('resize', this.handleResize)
            if (this.chartInstance) {
                try { this.chartInstance.dispose() } catch (e) {}
                this.chartInstance = null
            }
        },
    methods: {
        handleResize() {
            if (this._resizeTimer) clearTimeout(this._resizeTimer)
            this._resizeTimer = setTimeout(() => {
                if (this.chartInstance) {
                    try { this.chartInstance.resize() } catch (e) {}
                }
            }, 150)
        },
      initChart() {
                const pieRef = this.$refs.pieRef;
                const obj = this.optionsData || {};
                const toNumber = (v) => {
                    if (v === undefined || v === null || v === '') return 0;
                    if (typeof v === 'number') return v;
                    const s = String(v).trim();
                    if (s.endsWith('%')) {
                        const p = parseFloat(s.slice(0, -1));
                        return isNaN(p) ? 0 : p;
                    }
                    const n = parseFloat(s.replace(/[^0-9.\-]/g, ''));
                    return isNaN(n) ? 0 : n;
                };
                const followVal = toNumber(obj.followNum);
                const totalVal = toNumber(obj.totalNum);
                let notFollowVal = toNumber(obj.notFollowNum);
                if ((obj.notFollowNum === undefined || obj.notFollowNum === null || obj.notFollowNum === '') && totalVal) {
                    notFollowVal = totalVal - followVal;
                }
                const optionData = [
                        {
                            name: obj.followRateName || '随访率',
                            value: followVal,
                            itemStyle: { opacity: 0.5 },
                            rate: obj.followRate
                        },
                        {
                            name: "失访率",
                            value: notFollowVal,
                            itemStyle: { opacity: 0.5 },
                            rate: obj.notFollowRate
                        },
                ];
        this.option = getPie3D(optionData, 0.7);
        // 叠加一层透明 2D 饼图，仅用于显示引导线和百分比文案
        this.option.series.push({
            name: 'pie2d',
            type: 'pie',
            startAngle: -20,
            clockwise: false,
            radius: ['20%', '50%'],
            center: ['50%', '50%'],
            label: {
                show: true,
                position: 'outside',
                color: '#FFFFFF',
                fontSize: 10,
                lineHeight: 14,
                textStyle: {
                    color: '#FFFFFF'
                },
                formatter: '{b}\n{d}%'
            },
            emphasis: {
                label: {
                    color: '#FFFFFF'
                }
            },
            labelLine: {
                show: true,
                length: 10,
                length2: 12,
                lineStyle: {
                    color: '#86E1FF'
                }
            },
            data: optionData,
            itemStyle: {
                opacity: 0,
                color: 'transparent'
            },
            tooltip: {
                show: false
            },
            z: 100
        });
        if (!pieRef) return
        if (!this.chartInstance) {
          this.chartInstance = echarts.init(pieRef)
        }
        try {
          this.chartInstance.setOption(this.option)
        } catch (e) {}
        // this.option.series.push({
        //     name: 'pie2d',
        //     type: 'pie',
        //     labelLine:{
        //         length:10,
        //         length2:10
        //     },
        //     startAngle: -20 , //起始角度，支持范围[0, 360]。
        //     clockwise: false,//饼图的扇区是否是顺时针排布。上述这两项配置主要是为了对齐3d的样式
        //     radius: ['20%', '50%'],
        //     center: ['50%', '50%'],
        //     data: optionData,
        //     itemStyle:{
        //         opacity:0
        //     }
        // });
        // echarts.init(pieRef).setOption(this.option);
        // this.bindListen(myChart);
        // const option = cylinderEcharts([
        //   { name: "随访",  value: 30,},
        //   { name: "失访",  value: 50,},
        // ]);
        // echarts.init(pieRef).setOption(option);
        },

        bindListen(myChart) {
            // 监听鼠标事件，实现饼图选中效果（单选），近似实现高亮（放大）效果。
            let that = this;
            let selectedIndex = '';
            let hoveredIndex = '';
            // 监听点击事件，实现选中效果（单选）
            myChart.on('click', function(params) {
                // 从 option.series 中读取重新渲染扇形所需的参数，将是否选中取反。
                let isSelected = !that.option.series[params.seriesIndex].pieStatus.selected;
                let isHovered = that.option.series[params.seriesIndex].pieStatus.hovered;
                let k = that.option.series[params.seriesIndex].pieStatus.k;
                let startRatio = that.option.series[params.seriesIndex].pieData.startRatio;
                let endRatio = that.option.series[params.seriesIndex].pieData.endRatio;
                // 如果之前选中过其他扇形，将其取消选中（对 option 更新）
                if (selectedIndex !== '' && selectedIndex !== params.seriesIndex) {
                    that.option.series[selectedIndex].parametricEquation = that.getParametricEquation(that.option.series[
                            selectedIndex].pieData
                        .startRatio, that.option.series[selectedIndex].pieData.endRatio, false, false, k, that.option.series[
                            selectedIndex].pieData
                        .value);
                    that.option.series[selectedIndex].pieStatus.selected = false;
                }
                // 对当前点击的扇形，执行选中/取消选中操作（对 option 更新）
                that.option.series[params.seriesIndex].parametricEquation = that.getParametricEquation(startRatio, endRatio,
                    isSelected,
                    isHovered, k, that.option.series[params.seriesIndex].pieData.value);
                that.option.series[params.seriesIndex].pieStatus.selected = isSelected;
                // 如果本次是选中操作，记录上次选中的扇形对应的系列号 seriesIndex
                isSelected ? selectedIndex = params.seriesIndex : null;
                // 使用更新后的 option，渲染图表
                myChart.setOption(that.option);
            });
            // 监听 mouseover，近似实现高亮（放大）效果
            myChart.on('mouseover', function(params) {
                // 准备重新渲染扇形所需的参数
                let isSelected;
                let isHovered;
                let startRatio;
                let endRatio;
                let k;
                // 如果触发 mouseover 的扇形当前已高亮，则不做操作
                if (hoveredIndex === params.seriesIndex) {
                    return;
                    // 否则进行高亮及必要的取消高亮操作
                } else {
                    // 如果当前有高亮的扇形，取消其高亮状态（对 option 更新）
                    if (hoveredIndex !== '') {
                        // 从 option.series 中读取重新渲染扇形所需的参数，将是否高亮设置为 false。
                        isSelected = that.option.series[hoveredIndex].pieStatus.selected;
                        isHovered = false;
                        startRatio = that.option.series[hoveredIndex].pieData.startRatio;
                        endRatio = that.option.series[hoveredIndex].pieData.endRatio;
                        k = that.option.series[hoveredIndex].pieStatus.k;
                        // 对当前点击的扇形，执行取消高亮操作（对 option 更新）
                        that.option.series[hoveredIndex].parametricEquation = that.getParametricEquation(startRatio, endRatio,
                            isSelected,
                            isHovered, k, that.option.series[hoveredIndex].pieData.value);
                        that.option.series[hoveredIndex].pieStatus.hovered = isHovered;
                        // 将此前记录的上次选中的扇形对应的系列号 seriesIndex 清空
                        hoveredIndex = '';
                    }
                    // 如果触发 mouseover 的扇形不是透明圆环，将其高亮（对 option 更新）
                    if (params.seriesName !== 'mouseoutSeries' && params.seriesName !== 'pie2d') {
                        // 从 option.series 中读取重新渲染扇形所需的参数，将是否高亮设置为 true。
                        isSelected = that.option.series[params.seriesIndex].pieStatus.selected;
                        isHovered = true;
                        startRatio = that.option.series[params.seriesIndex].pieData.startRatio;
                        endRatio = that.option.series[params.seriesIndex].pieData.endRatio;
                        k = that.option.series[params.seriesIndex].pieStatus.k;
                        // 对当前点击的扇形，执行高亮操作（对 option 更新）
                        that.option.series[params.seriesIndex].parametricEquation = that.getParametricEquation(startRatio, endRatio,
                            isSelected, isHovered, k, that.option.series[params.seriesIndex].pieData.value + 5);
                        that.option.series[params.seriesIndex].pieStatus.hovered = isHovered;
                        // 记录上次高亮的扇形对应的系列号 seriesIndex
                        hoveredIndex = params.seriesIndex;
                    }
                    // 使用更新后的 option，渲染图表
                    myChart.setOption(that.option);
                }
            });
            // 修正取消高亮失败的 bug
            myChart.on('globalout', function() {
                // 准备重新渲染扇形所需的参数
                let isSelected;
                let isHovered;
                let startRatio;
                let endRatio;
                let k;
                if (hoveredIndex !== '') {
                    // 从 option.series 中读取重新渲染扇形所需的参数，将是否高亮设置为 true。
                    isSelected = that.option.series[hoveredIndex].pieStatus.selected;
                    isHovered = false;
                    k = that.option.series[hoveredIndex].pieStatus.k;
                    startRatio = that.option.series[hoveredIndex].pieData.startRatio;
                    endRatio = that.option.series[hoveredIndex].pieData.endRatio;
                    // 对当前点击的扇形，执行取消高亮操作（对 option 更新）
                    that.option.series[hoveredIndex].parametricEquation = that.getParametricEquation(startRatio, endRatio,
                        isSelected,
                        isHovered, k, that.option.series[hoveredIndex].pieData.value);
                    that.option.series[hoveredIndex].pieStatus.hovered = isHovered;
                    // 将此前记录的上次选中的扇形对应的系列号 seriesIndex 清空
                    hoveredIndex = '';
                }
                // 使用更新后的 option，渲染图表
                myChart.setOption(that.option);
            });
        }
    }
}
</script>

<style lang="scss" scoped>
    .noCount,
    .echarts,
    .echarts-container {
        width: 100%;
        height: 100%;
    }

    .noCount {
        display: flex;
        justify-content: center;
        align-items: center;
        color: #fff;
        font-size: 1.6vh;
    }
</style>