<template>
    <div class="echarts-container">
    <div ref="sexChart" class="sexecharts"></div>
        <!-- <div class="noCount" v-else>
            <p>暂无数据</p>
        </div> -->
    </div>
</template>

<script>
import * as echarts from 'echarts'
// import ECharts from "vue-echarts";
export default {
    name: 'chart',
    components: {
        // ECharts
    },
    data() {
    return {
      chartIns: null,
      colors: ['#6DE8FA', '#fc5531'],
      category: ['男', '女']
    }
    },
  props: {
    optionsData: {
      type: Array,
      default: () => []
    }
  },
    watch: {
    optionsData: {
      deep: true,
      immediate: true,
      handler() {
        this.$nextTick(() => {
          this.renderChart()
        })
      }
    }
  },
    mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.chartIns) {
      this.chartIns.dispose()
      this.chartIns = null
    }
    },
    methods: {
    initChart() {
      if (!this.$refs.sexChart) return
      this.chartIns = echarts.init(this.$refs.sexChart)
      this.renderChart()
    },
    handleResize() {
      this.chartIns && this.chartIns.resize()
    },
    getSexTotal(name) {
      const list = this.optionsData || []
      const hit = list.find(item => (item.name || '').includes(name))
      return Number(hit && hit.total) || 0
    },
    getChartData() {
      const maleTotal = this.getSexTotal('男')
      const femaleTotal = this.getSexTotal('女')
      const sum = maleTotal + femaleTotal
      const safeSum = sum > 0 ? sum : 1

      const malePercent = Math.round((maleTotal / safeSum) * 100)
      const femalePercent = Math.round((femaleTotal / safeSum) * 100)

      // 保证两项和为100（有数据时）
      const fixedFemalePercent = sum > 0 ? 100 - malePercent : 0

      return {
        barData: [
          { name: '男', value: malePercent },
          { name: '女', value: fixedFemalePercent }
        ],
        lineData: [
          { name: '男', value: 100 },
          { name: '女', value: 100 }
        ]
      }
    },
    getOption(barData, lineData) {
      return {
            // backgroundColor: '#0d073d',//背景色
            grid: [{//图形的位置
                left: '10',
                bottom: '30',
                top: 20,
                right: 30
            }],
            xAxis: {
              show: true,//是否展示X轴
        data: this.category,
              axisLabel: { //X轴文字样式
                color: '#fff',
                fontSize: 12,
                interval: 0,
                padding: [0, 0, 30, 0],
              },
              axisLine: {
                  show: false//不展示刻度
              }
            },
            yAxis: {
                show: false,
                axisLabel: {
                    inside: true,
                    verticalAlign: 'middle',
                    lineHeight: 150,
                    color: '#4488bc',
                    fontSize: 8
                },
                axisLine: {
                    show: false//不展示刻度
                }
            },
          series: [
            // { // 外边框
            //         name: '',
            //         type: 'pictorialBar',//echarts图的类型
            //         symbol: 'reat',//内部类型（方块，圆，svg，base64图片）
            //         barWidth: '3%',
            //         barMaxWidth: '10%',
            //         symbolOffset: [70, 0],//柱子的位置
            //         symbolSize: [130, 20],//size,单个symbol的大小
            //         itemStyle: {
            //             normal: {
            //                 color: '#3f559c'
            //             }
            //         },
            //         z: -180,//图层值
            //         symbolRepeat: null,//是否重复symbol
            //         data: [1, 1, 1, 1],
            //         barGap: 50,//柱子的“粗细”
            //         barCategoryGap: 0,
            //         animationEasing: 'elasticOut',

            //     },
            //     { // 内边框
            //         name: '',
            //         type: 'pictorialBar',
            //         symbol: 'reat',
            //         barWidth: '3%',
            //         barMaxWidth: '20%',
            //         symbolOffset: [72, 0],
            //         symbolSize: [125, 18],
            //         itemStyle: {
            //             normal: {

            //                 color: '#0d073d'
            //             }
            //         },
            //         z: -20,
            //         symbolRepeat: null,
            //         data: [1, 1, 1, 1],
            //         barGap: 45,
            //         barCategoryGap: 0,
            //         animationEasing: 'elasticOut',

            //     },

                { // 下层块
                    name: '',
                    type: 'pictorialBar',
                    symbol: 'roundRect',
                    barWidth: '20%',
                    barMaxWidth: '20%',
                    // symbolOffset: [75, 0],
                    itemStyle: {
                    color: '#1F4683'
                    },
                    z: -11,
                    symbolRepeat: true,
                    symbolSize: [22, 3],
                    data: lineData,
                    // barGap: 50,
                    // barCategoryGap: 0,
                    animationEasing: 'elasticOut',

                },

                { // 上层块
                    name: '', // blue bar
                    type: 'pictorialBar',
                    symbol: 'roundRect',
                    barWidth: '20%',
                    barMaxWidth: '20%',
                    // symbolOffset: [75, 0],
                    itemStyle: {
                        barMaxWidth: '20%',
                        barBorderRadius: 100,
                        color: (params) => {
                            let idx = params.dataIndex
                            return this.colors[idx]
                        },
                    },
                    label: {
                      show: true,
                      color: "#fff",
                      fontSize: 10,
                      formatter: '{c}%',
                      position: "top", 
                    },
                    symbolRepeat: true,
                    symbolSize: [22, 3],
                    // symbolClip: true,
                    data: barData,
                },
                /* 
                */

            ],

        }
    },
    renderChart() {
      if (!this.chartIns) return
      const { barData, lineData } = this.getChartData()
      const option = this.getOption(barData, lineData)
      this.chartIns.setOption(option, true)
    }
  },
}
</script>

<style lang="scss" scoped>
    .noCount,
    .sexecharts,
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