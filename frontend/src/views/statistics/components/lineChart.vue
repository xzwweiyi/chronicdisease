<template>
    <div class="echarts-container">
        <div class="lineChartecharts" ref="lineChart"></div>
        <!-- <div class="noCount" v-else>
            <p>暂无数据</p>
        </div> -->
    </div>
</template>

<script>
import * as echarts from 'echarts';
// import ECharts from "vue-echarts";
import filter from "@/filters/common.js";
export default {
    name: 'chart',
    components: {
        // ECharts
    },
    data() {
      return {
        chartInstance: null
      }
    },
    props: ['optionsData'],
    watch: {
      optionsData: {
        handler() { this.initChart() },
        deep: true
      }
    },
    mounted() {
      window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.handleResize)
      if (this.chartInstance) {
        this.chartInstance.dispose()
        this.chartInstance = null
      }
    },
  methods: {
      handleResize() {
        if (this.chartInstance) {
          this.chartInstance.resize()
        }
      },
      getYAxisConfig(seriesList) {
        const values = (seriesList || [])
          .flat()
          .map(value => Number(value || 0))
          .filter(value => !Number.isNaN(value) && value >= 0)

        const maxValue = values.length ? Math.max(...values) : 0

        if (maxValue <= 0) {
          return {
            max: 10,
            interval: 2
          }
        }

        const splitNumber = 5
        const rawInterval = maxValue / splitNumber
        const magnitude = Math.pow(10, Math.floor(Math.log10(rawInterval)))
        const normalized = rawInterval / magnitude

        let niceNormalized = 1
        if (normalized > 5) {
          niceNormalized = 10
        } else if (normalized > 2) {
          niceNormalized = 5
        } else if (normalized > 1) {
          niceNormalized = 2
        }

        const interval = niceNormalized * magnitude
        return {
          max: interval * splitNumber,
          interval
        }
      },
      initChart() {
        if (this.chartInstance) {
          this.chartInstance.dispose()
          this.chartInstance = null
        }
        let pieCharts = document.querySelectorAll(".lineChartecharts")
        const optionPie = this.getPieOption()
        this.chartInstance = echarts.init(pieCharts[0])
				this.chartInstance.setOption(optionPie)
      },
      getPieOption() {
        const colors = ['#0164EB', '#FFE66E'];
        const xAxisData = this.optionsData ? this.optionsData.months : [];
        const seriesData1 = this.optionsData ? this.optionsData.monthlyActivityNumber : []
        const seriesData2 = this.optionsData ? this.optionsData.monthlyActivityPatients : []
        const yAxisConfig = this.getYAxisConfig([seriesData1, seriesData2])
        return {
          color: colors,
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            },
            backgroundColor: '#CAF1FF',
            textStyle: {
                color: '#000',
                fontSize: filter.reversePixel(10),
            },
            formatter: function(params) {
              let astr = ""
              let b = ""
              params.forEach(ele => {
                const data = ele.data
                if (params.length >10) {
                  astr += `
                    <div style="display: block;height:20px;width: 50%;float:left;">
                      <i style="width: 10px;height: 10px;display: inline-block;border-radius: 10px;"></i>
                      <span>${ele.seriesName}: ${data}</span>
                    </div>
                  `
                  b =`<p>${ele.axisValueLabel}</p>
                  <div style='max-width: 350px;'>${astr}<div>`
                } else {
                  astr += `
                    <div style="display: block;">
                      <i style="width: 10px;height: 10px;display: inline-block;border-radius: 10px;"></i>
                      <span>${ele.seriesName}: ${data}</span>
                    </div>
                  `
                  b = `<p>${ele.axisValueLabel}</p>
                  <div>${astr}<div>`
                }
              })
              return b
            }
          },
          legend: {
            color:['#0164EB', '#FFE66E'],
            data:["月活跃数", "月活跃患者数"],
            textStyle:{
              color:"#caf1ff",
              fontSize:12
            }
          },
          grid: {
            top: 30,
            bottom: 30
          },
          xAxis: {
            type: 'category',
            axisTick: {
              alignWithLabel: true
            },
            axisLine: {
              onZero: false,
              lineStyle: {
                color: "#2F5C95"
              }
            },
            axisLabel: {
                color: '#caf1ff',
                fontSize: filter.reversePixel(12),
                lineHeight: filter.reversePixel(14),
                interval: 0,
            },
            axisPointer: {
              label: {
                formatter: function (params) {
                  return (
                    params.value + '：'
                  );
                }
              }
            },
            // prettier-ignore
            data: xAxisData
          },
          yAxis: {
            name: "人数",
              min: 0,
              max: yAxisConfig.max,
              interval: yAxisConfig.interval,
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
                  fontSize: filter.reversePixel(12),
                //   formatter: (val) => {
                //       return filter.numberFormat(val, true)
                // },
                  
              },
          },
          series: [
            {
              name: '月活跃数',
              type: 'line',
              smooth: false,
              emphasis: {
                focus: 'series'
              },
              data: (seriesData1 || []).map(v => Number(v || 0))
            },
            {
              name: '月活跃患者数',
              type: 'line',
              smooth: false,
              emphasis: {
                focus: 'series'
              },
                data: (seriesData2 || []).map(v => Number(v || 0))
            },
          ]
        };
      },
    }
}
</script>

<style lang="scss" scoped>
    .noCount,
    .lineChartecharts,
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