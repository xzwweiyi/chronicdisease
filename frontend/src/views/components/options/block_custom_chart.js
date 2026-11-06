import filter from "@/filters/common.js";
const echarts = require("echarts/lib/echarts");

const leftShape = echarts.graphic.extendShape({
  buildPath(ctx, shape) {
    const {
      topBasicsYAxis,
      bottomYAxis,
      basicsXAxis
    } = shape
    // 侧面宽度
    const WIDTH = 15
    // 斜角高度
    const OBLIQUE_ANGLE_HEIGHT = 8

    const p1 = [basicsXAxis - WIDTH, topBasicsYAxis - OBLIQUE_ANGLE_HEIGHT]
    const p2 = [basicsXAxis - WIDTH, bottomYAxis]
    const p3 = [basicsXAxis, bottomYAxis]
    const p4 = [basicsXAxis, topBasicsYAxis]

    ctx.moveTo(p1[0], p1[1])
    ctx.lineTo(p2[0], p2[1])
    ctx.lineTo(p3[0], p3[1])
    ctx.lineTo(p4[0], p4[1])
  },
})
const rightShape = echarts.graphic.extendShape({
  buildPath(ctx, shape) {
    const {
      topBasicsYAxis,
      bottomYAxis,
      basicsXAxis
    } = shape
    // 侧面宽度
    const WIDTH = 15
    // 斜角高度
    const OBLIQUE_ANGLE_HEIGHT = 8

    const p1 = [basicsXAxis, topBasicsYAxis]
    const p2 = [basicsXAxis, bottomYAxis]
    const p3 = [basicsXAxis + WIDTH, bottomYAxis]
    const p4 = [basicsXAxis + WIDTH, topBasicsYAxis - OBLIQUE_ANGLE_HEIGHT]

    ctx.moveTo(p1[0], p1[1])
    ctx.lineTo(p2[0], p2[1])
    ctx.lineTo(p3[0], p3[1])
    ctx.lineTo(p4[0], p4[1])
  },
})

const topShape = echarts.graphic.extendShape({
  buildPath(ctx, shape) {
    const {
      topBasicsYAxis,
      basicsXAxis
    } = shape
    // 侧面宽度
    const WIDTH = 15
    // 斜角高度
    const OBLIQUE_ANGLE_HEIGHT = 8

    const p1 = [basicsXAxis, topBasicsYAxis]
    const p2 = [basicsXAxis + WIDTH, topBasicsYAxis - OBLIQUE_ANGLE_HEIGHT]
    const p3 = [basicsXAxis, topBasicsYAxis - OBLIQUE_ANGLE_HEIGHT * 2]
    const p4 = [basicsXAxis - WIDTH, topBasicsYAxis - OBLIQUE_ANGLE_HEIGHT]

    ctx.moveTo(p1[0], p1[1])
    ctx.lineTo(p2[0], p2[1])
    ctx.lineTo(p3[0], p3[1])
    ctx.lineTo(p4[0], p4[1])
  },
})

echarts.graphic.registerShape("leftShape", leftShape)
echarts.graphic.registerShape("rightShape", rightShape)
echarts.graphic.registerShape("topShape", topShape)

export default {
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
              color: '#caf1ff',
              fontSize: filter.reversePixel(12),
              lineHeight: filter.reversePixel(14),
              interval: 0,
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
          name: "人数",
          nameTextStyle: {
              color: '#caf1ff',
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
              color: '#caf1ff',
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
      visualMap: {
        type: 'piecewise',
        pieces: [{
            min: 0,
            max: 10,
            color: '#5470C6'
          }, // 0-10 的颜色
          {
            min: 10,
            max: 20,
            color: '#91CC75'
          } // 10-20 的颜色
        ],
        show: false
      },
      series: [
        {
          type: "custom",
          data: [],
          barWidth: 30,
          renderItem(params, api) {
            // 基础坐标

            //api.value(0)可以获取到对应的Index，value(1)获取到y轴value值
            //api.coord可以获取到某个点(索引,value)的坐标
            // console.log("coord", api.coord([api.value(0), 0]))
            const basicsCoord = api.coord([api.value(0), api.value(1)])
            // console.log("basic", basicsCoord)
            // 顶部基础 y 轴,注意：是从顶部空白处到头部的距离
            const topBasicsYAxis = basicsCoord[1]
            // console.log("topBasic", topBasicsYAxis)
            // 基础 x 轴
            const basicsXAxis = basicsCoord[0]
            // 底部 y 轴  注意：y轴的距离都是从canvas左上角开始，到当前点位的距离
            const bottomYAxis = api.coord([api.value(0), 0])[1]
            // console.log("bottomYAxis", bottomYAxis)

            return {
              type: "group",
              children: [{
                  type: "leftShape",
                  shape: {
                    topBasicsYAxis,
                    basicsXAxis,
                    bottomYAxis,
                  },
                  style: {
                    fill: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                        offset: 0,
                        color: "#00306C",
                      },

                      {
                        offset: 0.25,
                        color: "#007BCA",
                      },
                      {
                        offset: 0.28,
                        color: "#00306C",
                      },
                      {
                        offset: 0.5,
                        color: "#18DFF5",
                      },
                      {
                        offset: 0.53,
                        color: "#00306C",
                      },
                      {
                        offset: 0.75,
                        color: "#F0DB3B",
                      },
                      {
                        offset: 0.78,
                        color: "#00306C",
                      },
                      {
                        offset: 1,
                        color: "#F95E21",
                      },
                    ]),
                    emphasis: {
                      fill: "yellow", // 鼠标高亮时的填充颜色
                    },
                  },
                },
                {
                  type: "rightShape",
                  shape: {
                    topBasicsYAxis,
                    basicsXAxis,
                    bottomYAxis,
                  },
                  style: {
                    fill: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                        offset: 0,
                          color: "#00306C",
                        },

                        {
                          offset: 0.25,
                          color: "#007BCA",
                        }, {
                          offset: 0.28,
                          color: "#00306C",
                        }, {
                          offset: 0.5,
                          color: "#18DFF5",
                        }, {
                          offset: 0.53,
                          color: "#00306C",
                        }, {
                          offset: 0.75,
                          color: "#F0DB3B",
                        }, {
                          offset: 0.78,
                          color: "#00306C",
                        }, {
                          offset: 1,
                          color: "#F95E21",
                        },
                    ]),
                  },
                },
                {
                  type: "topShape",
                  shape: {
                    topBasicsYAxis,
                    basicsXAxis,
                    bottomYAxis,
                  },
                  style: {
                    fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0.3,
                        color: "#F95E21",
                      },
                      {
                        offset: 1,
                        color: "#F95E21",
                      },
                    ]),
                  },
                },
              ],
            }
          },
        },
        {
          type: 'bar',
          label: {
            normal: {
              show: true,
              position: 'top',
              formatter: (e) => {
                return e.value;
              },
              fontSize: 12,
              color: "#fff",
              // fontFamily: 'siyuan',
              // fontWeight: 'bold',
              offset: [0, -15],
            },
          },
          // itemStyle: {
          //   color: 'transparent',
          // },
          tooltip: {},
          data: [],
        },
      ],
    }
  }
}