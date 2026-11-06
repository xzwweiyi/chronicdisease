<template>
  <div class="chronic-chart-container">
    <div ref="chart" class="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'ChronicDiseaseChart',
  props: ['optionsData'],
  data() {
    return {
      chartInstance: null,
    }
  },
  watch: {
    optionsData: {
      handler() {
        this.initChart()
      },
      deep: true
    }
  },
  mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.chartInstance) {
      this.chartInstance.dispose()
    }
  },
  methods: {
    initChart() {
      if (!Array.isArray(this.optionsData) || this.optionsData.length === 0) {
        if (this.chartInstance) {
          this.chartInstance.clear()
        }
        return
      }
      if (this.chartInstance) {
        this.chartInstance.dispose()
      }
      const sortedTop10Data = [...this.optionsData]
        .sort((a, b) => Number(b.total || 0) - Number(a.total || 0))
        .slice(0, 10)

      this.chartInstance = echarts.init(this.$refs.chart)
      let option;
      const labels = sortedTop10Data.map(item => item.name);
      const seriesData = sortedTop10Data.map(item => {
        // 规范化后端可能不同的字段命名
        const label = item.name || item.label || item.deptName || item.title || ''
        const value = Number(item.total ?? item.value ?? item.count ?? 0)
        const children = Array.isArray(item.children) ? item.children : (Array.isArray(item.childList) ? item.childList : [])
        return {
          label,
          value,
          children
        }
      })
      const colors = [
        [
          { offset: 0, color: 'rgba(26, 132, 191, 1)' },
          { offset: 1, color: 'rgba(52, 163, 224, 0.08)' },
        ],
        [
          { offset: 0, color: 'rgba(137, 163, 164, 1)' },
          { offset: 1, color: 'rgba(137, 163, 164, 0.08)' },
        ],
        [
          { offset: 0, color: 'rgba(44, 166, 166, 1)' },
          { offset: 1, color: 'rgba(44, 166, 166, 0.08)' },
        ],
        [
          { offset: 0, color: 'rgba(34, 66, 186, 1)' },
          { offset: 1, color: 'rgba(34, 66, 186, 0.08)' },
        ],
      ];

      option = {
        title:{
          right: 10,
          subtext:`TOP10`,
          subtextStyle: {
            color:'#caf1ff',
          }
        },
          tooltip: {
            trigger: 'item',
            axisPointer: {
                type: 'shadow'
            },
            backgroundColor: 'rgba(7,21,56,0.9)',
            confine: true,
            extraCssText: 'padding:10px;border-radius:8px;box-shadow:0 4px 12px rgba(0,0,0,0.45);',
            textStyle: {
              color: '#fff',
              fontSize: 12,
            },
            formatter: (params) => {
              // 支持 custom 系列传入的 data 对象，优先从 params.data 或 params.value 中获取值
              let value = null
              if (params && params.data && params.data.value) {
                value = Array.isArray(params.data.value) ? params.data.value[1] : params.data.value
              } else {
                value = Array.isArray(params.value) ? params.value[1] : params.value
              }
              // marker 使用 data 中的 colorStops 第一项颜色（如有）
              const markerColor = (params && params.data && params.data.colorStops && params.data.colorStops[0] && params.data.colorStops[0].color) || '#caf1ff'
              const marker = `<span style="display:inline-block;width:8px;height:8px;background:${markerColor};border-radius:50%;margin-right:8px;vertical-align:middle;"></span>`
              let html = ''
              html += `<div style="white-space:nowrap; font-weight:600; color:#fff;">${marker}${params.name}: ${value}人</div>`
              const children = params && params.data && params.data.children ? params.data.children : null
              if (Array.isArray(children) && children.length) {
                html += '<div style="margin-top:6px;padding-left:14px;color:#d8f3ff;">'
                children.forEach(child => {
                  const childName = child.name || child.label || ''
                  const childTotal = child.total != null ? child.total : (child.value != null ? child.value : 0)
                  html += `<div style="font-size:12px;margin:2px 0">${childName}: ${childTotal}人</div>`
                })
                html += '</div>'
              }
              return html
            }
        },
        grid: {
          top: 30,
          bottom: 30
        },
        xAxis: {
          name: "科室",
          axisLabel: {
              color: '#caf1ff',
              fontSize: 12,
              lineHeight: 14,
              interval: 0,
          },
          axisTick: {
            show: false
          },
          nameTextStyle: {
            color: '#fff'
          },
          data: labels,
        },
        yAxis: {
          name: "人数",
          nameTextStyle: {
              color: '#caf1ff',
              fontSize: 10,
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
              fontSize: 12
          },
        },
        series: getSeriesData()
      };
      // 定义柱状图左侧图形元素
      const leftRect = echarts.graphic.extendShape({
        shape: {
          x: 0,
          y: 0,
          width: 19, //柱状图宽
          zWidth: 8, //阴影折角宽
          zHeight: 4 //阴影折角高
        },
        buildPath: function (ctx, shape) {
          const api = shape.api;
          const xAxisPoint = api.coord([shape.xValue, 0]);
          const p0 = [shape.x - shape.width / 2, shape.y - shape.zHeight];
          const p1 = [shape.x - shape.width / 2, shape.y - shape.zHeight];
          const p2 = [xAxisPoint[0] - shape.width / 2, xAxisPoint[1]];
          const p3 = [xAxisPoint[0] + shape.width / 2, xAxisPoint[1]];
          const p4 = [shape.x + shape.width / 2, shape.y];

          ctx.moveTo(p0[0], p0[1]);
          ctx.lineTo(p1[0], p1[1]);
          ctx.lineTo(p2[0], p2[1]);
          ctx.lineTo(p3[0], p3[1]);
          ctx.lineTo(p4[0], p4[1]);
          ctx.lineTo(p0[0], p0[1]);
          ctx.closePath();
        }
      });
      // 定义柱状图右侧以及顶部图形元素
      const rightRect = echarts.graphic.extendShape({
        shape: {
          x: 0,
          y: 0,
          width: 18,
          zWidth: 15,
          zHeight: 8
        },
        buildPath: function (ctx, shape) {
          const api = shape.api;
          const xAxisPoint = api.coord([shape.xValue, 0]);
          const p1 = [shape.x - shape.width / 2, shape.y - shape.zHeight / 2];
          const p3 = [xAxisPoint[0] + shape.width / 2, xAxisPoint[1]];
          const p4 = [shape.x + shape.width / 2, shape.y];
          const p5 = [xAxisPoint[0] + shape.width / 2 + shape.zWidth, xAxisPoint[1]];
          const p6 = [
            shape.x + shape.width / 2 + shape.zWidth,
            shape.y - shape.zHeight / 2
          ];
          const p7 = [
            shape.x - shape.width / 2 + shape.zWidth,
            shape.y - shape.zHeight
          ];
          ctx.moveTo(p4[0], p4[1]);
          ctx.lineTo(p3[0], p3[1]);
          ctx.lineTo(p5[0], p5[1]);
          ctx.lineTo(p6[0], p6[1]);
          ctx.lineTo(p4[0], p4[1]);

          ctx.moveTo(p4[0], p4[1]);
          ctx.lineTo(p6[0], p6[1]);
          ctx.lineTo(p7[0], p7[1]);
          ctx.lineTo(p1[0], p1[1]);
          ctx.lineTo(p4[0], p4[1]);
          ctx.closePath();
        }
      });

      // 注册图形元素
      echarts.graphic.registerShape('leftRect', leftRect);
      echarts.graphic.registerShape('rightRect', rightRect);

      function getSeriesData() {
        const data = [];
        seriesData.forEach((item, index) => {
          data.push(
            {
              type: 'custom',
              name: item.label,
              renderItem: function (params, api) {
                return getRenderItem(params, api);
              },
              // label: {
              //   normal: {
              //     show: true,
              //     position: 'top',
              //     formatter: (e) => {
              //       return e.value;
              //     },
              //     fontSize: 12,
              //     color: "#fff",
              //     offset: [0, -15],
              //   },
              // },
                    // 将数据对象放入 data，以便 tooltip 和 renderItem 使用
                    data: [{
                      value: [index, item.value],
                      children: item.children || [],
                      label: item.label,
                      colorStops: colors[index % colors.length]
                    }],
                    itemStyle: {
                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, colors[index % colors.length]),
                    },
            }
          )
        })
        return data
      }

      function getRenderItem(params, api) {
        const xValue = api.value(0)
        const yValue = api.value(1)
        const currentValue = yValue
        let location = api.coord([xValue, yValue]);
        return {
          type: 'group',
          children: [
            {
              type: 'leftRect',
              shape: {
                api,
                xValue,
                yValue,
                x: location[0],
                y: location[1]
              },
              style: api.style(),
            },
            {
              type: 'rightRect',
              shape: {
                api,
                xValue,
                yValue,
                x: location[0],
                y: location[1]
              },
              style: api.style(),
            },
            {
              type: 'text',
              style: {
                text: `${currentValue}`,
                x: location[0] + 7,
                y: location[1] - 16,
                fill: '#fff',
                textAlign: 'center',
                textVerticalAlign: 'bottom',
                fontSize: 12,
              }
            }
          ]
        };
      }


      option && this.chartInstance.setOption(option);
    },
    handleResize() {
      if (this.chartInstance) {
        this.chartInstance.resize()
      }
    }
  }
}
</script>

<style scoped>
.noCount, .chronic-chart-container {
  width: 100%;
  height: 100%;
}
.chart {
  width: 100%;
  height: 100%;
}
</style>