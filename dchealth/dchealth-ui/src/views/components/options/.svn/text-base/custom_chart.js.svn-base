import filter from "@/filters/common.js";
const echarts = require("echarts/lib/echarts");
// --------------- 立体柱状图 --------------
// 绘制左侧面
const CubeLeft = echarts.graphic.extendShape({
    shape: {
        x: 0,
        y: 0,
    },
    buildPath: function (ctx, shape) {
        // 会canvas的应该都能看得懂，shape是从custom传入的
        const xAxisPoint = shape.xAxisPoint;
        const c0 = [shape.x, shape.y];
        const c1 = [shape.x - 6, shape.y - 5];
        const c2 = [xAxisPoint[0] - 6, xAxisPoint[1] - 0];
        const c3 = [xAxisPoint[0], xAxisPoint[1] + 5];
        ctx.moveTo(c0[0], c0[1]).lineTo(c1[0], c1[1]).lineTo(c2[0], c2[1]).lineTo(c3[0], c3[1]).closePath();
    },
});
// 绘制右侧面
const CubeRight = echarts.graphic.extendShape({
    shape: {
        x: 0,
        y: 0,
    },
    buildPath: function (ctx, shape) {
        const xAxisPoint = shape.xAxisPoint;
        const c1 = [shape.x, shape.y];
        const c2 = [xAxisPoint[0], xAxisPoint[1] + 5];
        const c3 = [xAxisPoint[0] + 6, xAxisPoint[1] - 0];
        const c4 = [shape.x + 6, shape.y - 5];
        ctx.moveTo(c1[0], c1[1]).lineTo(c2[0], c2[1]).lineTo(c3[0], c3[1]).lineTo(c4[0], c4[1]).closePath();
    },
});
// 绘制顶面   下、右、上、左
const CubeTop = echarts.graphic.extendShape({
    shape: {
        x: 0,
        y: 0,
    },
    buildPath: function (ctx, shape) {
        const c1 = [shape.x, shape.y];
        const c2 = [shape.x + 6, shape.y - 5];
        const c3 = [shape.x, shape.y - 8];
        const c4 = [shape.x - 6, shape.y - 5];
        ctx.moveTo(c1[0], c1[1]).lineTo(c2[0], c2[1]).lineTo(c3[0], c3[1]).lineTo(c4[0], c4[1]).closePath();
    },
});

// 注册三个面图形;
echarts.graphic.registerShape('CubeLeft', CubeLeft);
echarts.graphic.registerShape('CubeRight', CubeRight);
echarts.graphic.registerShape('CubeTop', CubeTop);
export default {
    getCustomOption() {
        return {
            tooltip: {
                show: false,
            },
            xAxis: [{
                type: 'category',
                axisTick: {
                    show: false
                },
                axisLabel: {
                    color: '#fff',
                    fontSize: filter.reversePixel(12),
                },
                splitLine: {
                    show: false
                },
                axisLine: {
                    show: false,
                },
                data: [],
            }, {
                type: 'category',
                name: '',
                nameTextStyle: {
                    color: '#999',
                    fontSize: filter.reversePixel(12),
                    padding: [0, 20, 8, 0],
                    align: 'right',
                    verticalAlign: 'bottom'
                },
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false,
                },
                axisLabel: {
                    show: false
                },
                data: [],
            }, ],
            yAxis: {
                nameTextStyle: {
                    color: '#76A3DF',
                    fontSize: filter.reversePixel(10),
                    padding: [0, 0, 0, 0]
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: '#2F5C95'
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        type: 'dashed',
                        color: 'rgba(89,184,218,0.26)'
                    }
                },
                axisLabel: {
                    color: '#fff',
                    fontSize: filter.reversePixel(11),
                    formatter: (val) => {
                        return filter.numberFormat(val, true)
                    }
                },
            },
            grid: {
                left: filter.reversePixel(40, 30),
                right: 0,
                top: filter.reversePixel(30),
                bottom: filter.reversePixel(40, 20),
            },
            series: [{
                type: 'custom',
                z: 2,
                labelLayout: {
                    moveOverlap: 'shiftY'
                },
                renderItem: function (params, api) {
                    const location = api.coord([api.value(0), api.value(1)]);
                    return {
                        type: 'group',
                        children: [{
                                type: 'CubeLeft',
                                shape: {
                                    api,
                                    x: location[0],
                                    y: location[1],
                                    xAxisPoint: api.coord([api.value(0), 0]),
                                },
                                style: {
                                    fill: 'rgba(114,145,238,0.20)',
                                },
                            },
                            {
                                type: 'CubeRight',
                                shape: {
                                    api,
                                    x: location[0],
                                    y: location[1],
                                    xAxisPoint: api.coord([api.value(0), 0]),
                                },
                                style: {
                                    fill: 'rgba(114,145,238,0.30)',
                                },
                            },
                            {
                                type: 'CubeTop',
                                shape: {
                                    api,
                                    x: location[0],
                                    y: location[1],
                                    xAxisPoint: api.coord([api.value(0), 0]),
                                },
                                style: {
                                    fill: 'rgba(114,145,238,0.4)',
                                },
                            }
                        ],
                    };
                },
                data: [],
            }, {

                type: 'custom',
                z: 3,
                renderItem: (params, api) => {
                    const location = api.coord([api.value(0), api.value(1)]);
                    return {
                        type: 'group',
                        children: [{
                                type: 'CubeLeft',
                                shape: {
                                    api,
                                    xValue: api.value(0),
                                    yValue: api.value(1),
                                    x: location[0],
                                    y: location[1],
                                    xAxisPoint: api.coord([api.value(0), 0]),
                                },
                                style: {
                                    fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                            offset: 0,
                                            color: '#30A4C1',
                                        },
                                        {
                                            offset: 1,
                                            color: '#235D9A',
                                        },
                                    ]),
                                },
                            },
                            {
                                type: 'CubeRight',
                                shape: {
                                    api,
                                    xValue: api.value(0),
                                    yValue: api.value(1),
                                    x: location[0],
                                    y: location[1],
                                    xAxisPoint: api.coord([api.value(0), 0]),
                                },
                                style: {
                                    fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                            offset: 0,
                                            color: '#64CAE4',
                                        },
                                        {
                                            offset: 1,
                                            color: '#2E72B7',
                                        }
                                    ]),
                                },
                            },
                            {
                                type: 'CubeTop',
                                shape: {
                                    api,
                                    xValue: api.value(0),
                                    yValue: api.value(1),
                                    x: location[0],
                                    y: location[1],
                                    xAxisPoint: api.coord([api.value(0), 0]),
                                },
                                style: {
                                    fill: '#69D7FF',
                                },
                            },
                        ],
                    };
                },
                data: [],
            }, {
                type: 'bar',
                label: {
                    show: false,
                    position: 'top',
                    fontSize: filter.reversePixel(10),
                    color: '#fff',
                    offset: [0, -10]
                },
                itemStyle: {
                    color: 'transparent'
                },
                data: []
            }]
        }
    },
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
                    fontSize: filter.reversePixel(16),
                },
                formatter: (params) => {
                    let text = '';
                    params.forEach(item => {
                        let name = item.name.replace(/\s*/g,"")
                        text = `${item.marker}${name}：${item.data.chartValue}`
                    })
                    return text
                }
            },
            xAxis: [{
                type: 'category',
                axisTick: {
                    show: false
                },
                axisLabel: {
                    color: '#fff',
                    fontSize: filter.reversePixel(12),
                    lineHeight:filter.reversePixel(14),
                },
                splitLine: {
                    show: false
                },
                axisLine: {
                    show: false,
                },
                data: [],
            }, {
                type: 'category',
                name: '',
                nameTextStyle: {
                    color: '#999',
                    fontSize: filter.reversePixel(12),
                    padding: [0, 20, 8, 0],
                    align: 'right',
                    verticalAlign: 'bottom'
                },
                splitLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false,
                },
                axisLabel: {
                    show: false
                },
                data: [],
            }],
            yAxis: {
                nameTextStyle: {
                    color: '#76A3DF',
                    fontSize: filter.reversePixel(10),
                    padding: [0, 0, 0, 0]
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: '#2F5C95'
                    }
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        type: 'dashed',
                        color: 'rgba(89,184,218,0.26)'
                    }
                },
                axisLabel: {
                    color: '#fff',
                    fontSize: filter.reversePixel(11),
                    formatter: (val) => {
                        return filter.numberFormat(val, true)
                    }
                },
            },
            grid: {
                left: filter.reversePixel(10),
                right:filter.reversePixel(10),
                top: filter.reversePixel(35),
                bottom: filter.reversePixel(10),
                containLabel: true
            },
            series: [{ // 圆柱体底面
                    name: 'a',
                    tooltip: {
                        show: false
                    },
                    type: 'pictorialBar',
                    itemStyle: {
                        color: '#244DA7'
                    },
                    symbol: 'circle',
                    symbolSize: [filter.reversePixel(20), filter.reversePixel(6)],
                    symbolPosition: 'start',
                    data: [],
                    z: 3
                },
                { // 圆柱
                    type: 'bar',
                    barWidth: filter.reversePixel(20),
                    backgroundStyle: {},
                    barGap: '-100%',
                    label: {
                        show: false,
                    },
                    itemStyle: {
                        borderRadius: filter.reversePixel(5),
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgba(2,169,241,1)',
                            },
                            {
                                offset: 1,
                                color: 'rgba(22,64,152,0.4)',
                            },
                        ]),
                    },
                    data: []
                },
                { // 圆柱体顶面
                    name: 'a',
                    tooltip: {
                        show: false
                    },
                    type: 'pictorialBar',
                    itemStyle: {
                        color: '#0FC1FC'
                    },
                    symbol: 'circle',
                    symbolSize: [filter.reversePixel(20), filter.reversePixel(5)],
                    symbolPosition: 'end',
                    data: [],
                    z: 3
                },
            ]
        }
    }
}