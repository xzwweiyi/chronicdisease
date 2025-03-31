import filter from "@/filters/common.js";
const echarts = require("echarts/lib/echarts");

export default {
    getPictorialBarOption() {
        return {
            colors: 'rgba(54,224,198,1)',
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                backgroundColor: 'rgba(7,21,56,0.8)',
                textStyle: {
                    color: '#fff',
                    fontSize: filter.reversePixel(16),
                },
                formatter: (params) => {
                    let text = '';
                    params.forEach(item => {
                        text = `${item.marker}${item.name}：${item.value}`
                    })
                    return text
                }
            },
            grid: {
                left: 10,
                bottom: 30,
                top: 10,
                containLabel: true
            },
            xAxis: {
                data: [],
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                axisLabel: {
                    color: '#fff'
                }
            },
            yAxis: {
                type: 'value',
                position: 'top',
                axisTick: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                splitLine: {
                    show: false,
                }
            },
            series: [{
                name: 'hill',
                type: 'pictorialBar',
                barCategoryGap: '-50%',
                symbol: 'path://M0,10 L10,10 C5.5,10 5.5,5 5,0 C4.5,5 4.5,10 0,10 z',
                data: [],
                z: 10
            }]
        }
    },
    // 棱锥图形
    getPictorialOption() {
        return {
            colors: 'rgba(54,224,198,1)',
            tooltip: {
                trigger: 'axis',
                // axisPointer: {
                //     type: 'shadow'
                // },
                backgroundColor: 'rgba(7,21,56,0.8)',
                textStyle: {
                    color: '#fff',
                    fontSize: filter.reversePixel(16),
                },
                formatter: (params) => {
                    let text = '';
                    params.forEach(item => {
                        text = `${item.marker}${item.name}：${item.value}`
                    })
                    return text
                }
            },
            grid: {
                left: 10,
                bottom: 30,
                top: 10,
                containLabel: true
            },
            xAxis: {
                data: [],
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                axisLabel: {
                    color: '#fff'
                },
                splitArea: {
                    show: true,
                    areaStyle: {
                        color: ["rgba(250,250,250,0.05)", "rgba(250,250,250,0.0)"],
                    }
                }
            },
            yAxis: {
                type: 'value',
                position: 'top',
                axisLabel: {
                    show: false,
                },
                splitLine: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                }
            },
            series: [{
                name: 'hill',
                type: 'pictorialBar',
                barCategoryGap: '40%',
                symbolOffset: [0, -15],
                symbol: 'path://M35,0L35,70L0,70z M35,0L35,70L70,70z',
                data: [],
                z: 99,
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                            offset: 0,
                            color: 'rgba(10,92,176,1)'
                        },
                        {
                            offset: 0.5,
                            color: 'rgba(10,92,176,1)'
                        },
                        {
                            offset: 0.5,
                            color: 'rgba(51,189,216,1)'
                        },
                        {
                            offset: 1,
                            color: 'rgba(51,189,216,1)'
                        }
                    ]),
                    opacity: 1,
                }
            },{
                name: '',
                type: 'pictorialBar',
                symbol: 'diamond',
                symbolSize: ['100%', 30],
   
                z: 99,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 1, 1, 1, [{
                                offset: 0,
                                color: 'rgba(10,92,176,1)'
                            },
                            {
                                offset: 0.5,
                                color: 'rgba(10,92,176,1)'
                            },
   
                            {
                                offset: 0.5,
                                color: 'rgba(51,189,216,1)'
                            },
                            {
                                offset: 1,
                                color: 'rgba(51,189,216,1)'
                            }
                        ]),
                        opacity: 1,
                    }
                },
                data: [],
            }]
        }
    },
}