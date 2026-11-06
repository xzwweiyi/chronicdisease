<template>
    <div ref="mapEcharts" class="map-echart"></div>
</template>

<script>
import * as echarts from 'echarts'
import { shanxi } from '@/views/components/options/shanxi.js'
import filter from "@/filters/common.js";
const tooltipImg = require('../../../assets/statistics/background.png')
export default ({
    name: "myMap",
    props: ['provinceMapData'],
    components: {},
    provide: {

    },
    data() {
        return {
            map: null,
            currentIndex: 0,
            toolTipTimers: null,

            _resizeTimer: null,

        }
    },
    watch: {
        provinceMapData: {
            immediate: true,
            deep: true,
            handler(newVal) {
                // this.initData(newVal)
                this.$nextTick(() => {
                    this.setOption(newVal)
                })

            }
        }
    },
    created() {
    },
    methods: {
        handleResize() {
            if (this._resizeTimer) clearTimeout(this._resizeTimer)
            this._resizeTimer = setTimeout(() => {
                try { this.map && this.map.resize() } catch (e) {}
            }, 150)
        },
        setOption(resData) {
            // hospitalPatient 住院患者 outPatient 门诊患者 emergencyPatient 急诊患者 testPatient 体检患者
            let seriesData = resData.map((m, index) => {
                let statistics = m.visitStatistics
                return {
                    name: m.cityName,
                    value: m.patientCount,
                    // outPatient: statistics && statistics.outPatient || statistics['门诊患者'],
                    // hospitalPatient: statistics && statistics.hospitalPatient || statistics['住院患者'],
                    // emergencyPatient: statistics && statistics.emergencyPatient || statistics['急诊患者'],
                    // testPatient: statistics && statistics.testPatient || statistics['体检患者'],
                }
            })
            echarts.registerMap('shanxi', shanxi)

            // 初始化
            this.map = echarts.init(this.$refs['mapEcharts'])
            this.map.showLoading()
            const numberOpt = { digit: 2, isThousand: true, isShortNum: true }
            // 设置基础配置项
            const option = {
                // 悬浮窗
                tooltip: {
                    trigger: 'item',
                    position: function (point, params, dom, rect, size) {
                        let x = point[0] - size.contentSize[0] / 2
                        let y = point[1] + 20

                        return [x, y];
                    },
                    formatter: function (params) {
                        let text = ''
                        let paramsData = params.data;
                        text = params.marker + '<span style="font-size:16px;">' + params.name + '：</span>'
                            + filter.numberFormat(params.value, numberOpt) + '人'
                        // if (paramsData) {
                        //     text += `<div style="border-top:1px #5470c6 solid;height:1px;margin:8px 0;"></div>`
                        //     text += '<div style="margin:5px 0;"><span style="display:inline-block;width:150px;">门诊患者：'
                        //         + filter.numberFormat(paramsData.outPatient, numberOpt) + '人</span>'
                        //     text += '<span style="display:inline-block;width:10px;height:5px;"></span>'
                        //         + '住院患者：' + filter.numberFormat(paramsData.hospitalPatient, numberOpt) + '人</div>'
                        //     text += '<div style="margin:5px 0;"><span style="display:inline-block;width:150px;">急诊患者：'
                        //         + filter.numberFormat(paramsData.emergencyPatient, numberOpt) + '人</span>'
                        //     text += '<span style="display:inline-block;width:10px;height:5px;"></span>'
                        //         + '体检患者：' + filter.numberFormat(paramsData.testPatient, numberOpt) + '人</div>'
                        // }
                        return `${text}
                        <div style="width:0;height:0;position:absolute;left:calc(50% - 2px);border:4px solid transparent;z-index:999;top:-8px;border-bottom-color:rgba(0,0,0,.9)"></div>`
                    },
                    backgroundColor: 'rgba(0,0,0,0.9)',
                    textStyle: {
                        color: "#fff"
                    }
                },
                geo: {
                    top: 0,
                    bottom: 0,
                    map: 'shanxi',
                    roam: true,
                    itemStyle: {
                        areaColor: 'rgba(6,20,55,0.6)',
                        areaColor: '#071436',
                        color: '#fff',
                        borderColor: '#94A1C1',
                        borderWidth: 2
                    },
                    label: {
                        show: true,
                        color: '#face1f',
                        fontSize: filter.reversePixel(12),
                    },
                    emphasis: {
                        itemStyle: {
                            areaColor: 'rgba(68,239,255,0.4)',
                        },
                        label: {
                            color: '#fff'
                        }
                    },
                },
                // 要显示的散点数据
                series: [{
                    type: 'map',
                    map: "shanxi",
                    geoIndex: 0,
                    symbolSize: 1,
                    data: seriesData,
                    label: {
                        show: true,
                        position: 'right',
                        distance: filter.reversePixel(60),
                        color: '#fff',
                        borderColor: '#00C9D3',
                        backgroundColor: {
                            image: tooltipImg
                        },
                        borderWidth: 1,
                        padding: [filter.reversePixel(5), filter.reversePixel(10)],
                    },
                    labelLine: {
                        show: true,
                        showAbove: true,
                        minTurnAngle: filter.reverseMapPixel(120),
                        lineStyle: {
                            color: '#00C9D3'
                        }
                    }
                },]
            }
            // 将配置应用到地图上
            this.map.setOption(option)
            let _this = this;
            // 清理并（重新）启动自动轮播
            if (this.toolTipTimers) {
                try { clearInterval(this.toolTipTimers) } catch (e) {}
                this.toolTipTimers = null;
            }
            this.currentIndex = 0;
            this.intervalMap(option)
            try { this.map.hideLoading() } catch (e) {}
            // 鼠标移入暂停并清理定时器
            this.map.on('mouseover', function (params) {
                if (_this.toolTipTimers) {
                    try {
                        const prev = _this.currentIndex === 0 ? (option.series[0].data.length - 1) : (_this.currentIndex - 1);
                        _this.map.dispatchAction({ type: 'downplay', seriesIndex: 0, dataIndex: prev });
                    } catch (e) {}
                    try { clearInterval(_this.toolTipTimers) } catch (e) {}
                    _this.toolTipTimers = null;
                }
            });
            // 鼠标移出后重新启动轮播
            this.map.on('mouseout', function (params) {
                if (!_this.toolTipTimers) {
                    _this.intervalMap(option);
                }
            })
            this.map.on('globalout', function (params) {
                try { if (_this.toolTipTimers) { clearInterval(_this.toolTipTimers); _this.toolTipTimers = null } } catch (e) {}
                _this.intervalMap(option)
            });
        },
        intervalMap(option) {
            // 使用 setInterval 控制轮播，启动前清理旧计时器
            if (this.toolTipTimers) {
                try { clearInterval(this.toolTipTimers) } catch (e) {}
                this.toolTipTimers = null;
            }
            const _this = this;
            const len = option && option.series && option.series[0] && option.series[0].data ? option.series[0].data.length : 0;
            if (!len) return;
            this.toolTipTimers = setInterval(() => {
                try {
                    const prevIndex = _this.currentIndex === 0 ? (len - 1) : (_this.currentIndex - 1);
                    _this.map.dispatchAction({ type: 'downplay', seriesIndex: 0, dataIndex: prevIndex });
                    _this.map.dispatchAction({ type: 'showTip', seriesIndex: 0, dataIndex: _this.currentIndex });
                    _this.map.dispatchAction({ type: 'highlight', seriesIndex: 0, dataIndex: _this.currentIndex });
                } catch (e) {}
                _this.currentIndex = (_this.currentIndex + 1) % len;
            }, 3000);
        }
    },
    mounted() {
        window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy() {
        if (this.toolTipTimers) {
            clearTimeout(this.toolTipTimers);
        }
        if (this._resizeTimer) {
            clearTimeout(this._resizeTimer)
            this._resizeTimer = null
        }
        window.removeEventListener('resize', this.handleResize)
        if (this.map) {
            try { this.map.dispose() } catch (e) {}
            this.map = null
        }
    },
        
})
</script>

<style scoped>
.map-echart {
    height: 97%;
    width: 100%;
}
</style>