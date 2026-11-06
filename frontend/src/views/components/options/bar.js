import filter from "@/filters/common.js";
const colors = ['rgba(54,224,198,0.6)', 'rgba(7,166,255,0.6)', '#3E98E3', '#2167E8', '#1F3B89', '#3BC4FC', '#50A7FF', ]

export default {
    colors,
    getOption() {
        return {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                backgroundColor: 'rgba(7,21,56,0.8)',
                textStyle: {
                    color: '#fff',
                    fontSize: filter.reversePixel(14),
                }
            },
            legend: {
                data: [{
                    name: '今日',
                    itemStyle: {
                        color: 'rgba(54,224,198,0.8)',
                        borderColor: 'rgba(54,224,198,1)',
                        borderWidth:1,
                    }
                }, {
                    name: '本月',
                    itemStyle: {
                        color: 'rgba(7,166,255,0.8)',
                        borderColor: 'rgba(7,166,255,1)',
                        borderWidth:1,
                    }
                }],
                textStyle: {
                    color: '#fff',
                    fontSize: filter.reversePixel(12),
                },
                left: filter.reversePixel(60),
                itemGap:filter.reversePixel(20),
                itemWidth: filter.reversePixel(18),
                itemHeight: filter.reversePixel(10),
                itemStyle: {
                    opacity: 0.6,
                },
                top: filter.reversePixel(10),
            },
            grid: {
                left: filter.reversePixel(10),
                bottom: filter.reversePixel(10),
                top: filter.reversePixel(40),
                containLabel: true
            },
            xAxis: {
                type: 'value',
                position: 'top',
                boundaryGap: [0, 0.01],
                name: '',
                nameTextStyle: {
                    color: '#999',
                    fontSize: filter.reversePixel(12),
                    padding: [0, 0, 8, 0],
                    align: 'right',
                    verticalAlign: 'bottom'
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                splitLine: {
                    lineStyle: {
                        color: ['#666']
                    }
                }
            },
            yAxis: {
                type: 'category',
                data: [],
                axisLabel: {
                    color: '#fff',
                    fontSize: filter.reversePixel(12),
                },
                axisTick: {
                    show: false
                },
                splitLine: {
                    show: false
                }
            },
            series: [{
                    name: '今日',
                    type: 'bar',
                    servekey:'today',
                    itemStyle: {
                        color: colors[0],
                        borderColor: 'rgba(54,224,198,1)',
                    },
                    barGap:'50%',
                    data: []
                },
                {
                    name: '本月',
                    type: 'bar',
                    servekey:'month',
                    itemStyle: {
                        color: colors[1],
                        borderColor: 'rgba(34,179,248,1)',
                    },
                    data: []
                }
            ]
        }
    },
}