import filter from "@/filters/common.js";
const colors = ['rgba(0,192,253,1)', 'rgba(61,145,255,1)', 'rgba(88,218,153,1)', 'rgba(255,176,115,1)',
    'rgba(213,215,54,1)', '#90BE6D', '#EBB638', '#EC6C26', '#0869E0', '#AB956B', "#3AA0FF", "#D0FF23",
    "#FAD337", "#F2637B", "#5164FE", "#B37BF0", '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
    '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'
]
export default {
    colors,
    getPieOption() {
        return {
            tooltip: {
                trigger: "item",
                formatter: (params) => {
                    return `${params.marker} ${params.data.name}：${params.data.value}`
                },
                textStyle: {
                    color: '#000',
                    fontSize: filter.reversePixel(16),
                },
                confine: true,
            },
            color: colors,
            legend: {
                top: 0,
                left: "center",
                show: false,
            },
            series: [{
                name: "Access From",
                type: "pie",
                radius: [0, "60%"],
                avoidLabelOverlap: true,
                minAngle: 2,
                showEmptyCircle: true,
                label: {
                    show: true,
                    formatter: (params) => {
                        return `${params.data.name}：${params.data.value}`
                    }
                },
                itemStyle: {
                    borderRadius: 5,
                    borderColor: 'rgba(22, 34, 58, 0.7)',
                    borderWidth: 1
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: "20",
                        fontWeight: "bold",
                    },
                },
                labelLine: {
                    length2: 40
                },
                data: [],
            }],
        };
    },
    getColumnarOption() {
        return {
            grid: {
                top: 15,
                left: 22,
                right: 10,
                bottom: 10,
                width: '90%',
                containLabel: true
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                textStyle: {
                    color: '#000',
                    fontSize: filter.reversePixel(16),
                },
                formatter: (params) => {
                    let text = '';
                    params.forEach(item => {
                        text = `${item.marker}  ${item.name}：${item.value}`
                    })
                    return text
                }
            },
            legend: {
                top: "5%",
                left: "center",
            },
            xAxis: {
                type: "category",
                data: [],
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    show: true,
                    symbol: ['none', 'arrow'],
                    symbolOffset: [0, 12],
                    lineStyle: {
                        color: '#999'
                    }
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        color: '#999'
                    }
                },
                axisLabel: {
                    color: '#333'
                }
            },
            yAxis: {
                type: "value",
                data: [],
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    show: true,
                    symbol: ['none', 'arrow'],
                    symbolOffset: [0, 12],
                    lineStyle: {
                        color: '#999'
                    },
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        color: '#999'
                    }
                },
                axisLabel: {
                    color: '#333'
                }
            },
            color: colors,
            series: [{
                data: [],
                type: "bar",
                showBackground: true,
                barMaxWidth: 30,
                itemStyle: {
                    borderRadius: 5,
                    borderColor: 'rgba(22, 34, 58, 0.7)',
                    borderWidth: 1,
                    opacity: 0.8
                },
            }],
        };
    },
    getLineOption() {
        return {
            legend: {
                top: "5%",
                left: "center",
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'line'
                },
                textStyle: {
                    color: '#000',
                    fontSize: filter.reversePixel(16),
                },
                formatter: (params) => {
                    let text = '';
                    params.forEach(item => {
                        text = `${item.marker}  ${item.name}：${item.value}`
                    })
                    return text
                }
            },
            grid: {
                top: 15,
                left: 22,
                right: 10,
                bottom: 10,
                width: '90%',
                containLabel: true
            },
            color: colors,
            xAxis: {
                type: 'category',
                data: [],
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    symbol: ['none', 'arrow'],
                    symbolOffset: [0, 12],
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        color: '#999'
                    },
                },
            },
            yAxis: {
                type: 'value',
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    show: true,
                    symbol: ['none', 'arrow'],
                    symbolOffset: [0, 12],
                    lineStyle: {
                        color: '#999'
                    },
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        color: '#999'
                    },
                },
                axisLabel: {
                    color: '#333'
                }
            },
            series: [{
                data: [],
                type: 'line'
            }]
        }
    },
    getRadarOptions() {
        return {
            legend: {
                data: []
            },
            tooltip: {
                trigger: 'item',
            },
            radar: {
                // shape: 'circle',
                indicator: []
            },
            series: [{
                name: '分析',
                type: 'radar',
                data: []
            }]
        }
    },
    getBarOption() {
        return {
            legend: {
                left: "center",
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                textStyle: {
                    color: '#000',
                    fontSize: filter.reversePixel(16),
                },
                formatter: (params) => {
                    let text = '';
                    params.forEach(item => {
                        text = `${item.marker}  ${item.name}：${item.value}`
                    })
                    return text
                }
            },
            grid: {
                top: 40,
                left: 22,
                right: 10,
                bottom: 10,
                width: '95%',
                containLabel: true
            },
            color: colors,
            xAxis: {
                type: 'category',
                data: [],
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    symbol: ['none', 'arrow'],
                    symbolOffset: [0, 12],
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        color: '#999'
                    },
                },
            },
            yAxis: {
                type: 'value',
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    show: true,
                    symbol: ['none', 'arrow'],
                    symbolOffset: [0, 12],
                    lineStyle: {
                        color: '#999'
                    },
                },
                axisTick: {
                    show: true,
                    lineStyle: {
                        color: '#999'
                    },
                },
                axisLabel: {
                    color: '#333'
                }
            },
            series: [{
                data: [],
                type: 'bar',
                barWidth: 28,
            }]
        }
    }
};