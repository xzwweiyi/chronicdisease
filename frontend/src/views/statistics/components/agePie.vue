<template>
    <div class="echarts-container">
        <div class="echarts" ref="pieChart"></div>
        <!-- <div class="noCount" v-else>
            <p>暂无数据</p>
        </div> -->
    </div>
</template>

<script>
import * as echarts from 'echarts';
// import ECharts from "vue-echarts";
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
            handler() { this.renderChart() },
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
        this.chartInstance = null
      }
    },
  methods: {
      handleResize() {
        if (this._resizeTimer) clearTimeout(this._resizeTimer)
        this._resizeTimer = setTimeout(() => {
          if (this.chartInstance) this.chartInstance.resize()
        }, 120)
      },
      initChart() {
        const el = this.$refs.pieChart
        if (!el) return
        this.chartInstance = echarts.init(el)

        this.$nextTick(() => {
          this.chartInstance.on('mouseover', { componentType: 'series', seriesType: 'pie' }, (params) => {
            this.chartInstance.setOption({ title: { show: false } })
          });
          // 鼠标移出数据时
          this.chartInstance.on('mouseout', { componentType: 'series', seriesType: 'pie' }, (params) => {
            this.chartInstance.setOption({ title: { show: true } })
          });
        })

        this.renderChart()
      },

    renderChart() {
        if (!this.chartInstance) return
        const optionPie = this.getPieOption(this.chartInstance)

        const raw = this.optionsData || {}
        let data = []

        if (Array.isArray(raw)) {
          data = raw.map(d => ({ name: d.name || d.label, value: Number(d.total ?? d.value ?? d.count ?? d.patientCount ?? 0) }))
        } else if (raw && Array.isArray(raw.data)) {
          data = raw.data.map(d => ({ name: d.name || d.label, value: Number(d.total ?? d.value ?? d.count ?? 0) }))
        } else if (raw && Array.isArray(raw.list)) {
          data = raw.list.map(d => ({ name: d.name || d.label, value: Number(d.total ?? d.value ?? d.count ?? 0) }))
        }

        const total = data.reduce((sum, item) => sum + Number(item.value || 0), 0)
        optionPie.series[0].data = data
        optionPie.legend.formatter = function (name) {
          const find = optionPie.series[0].data.find(item => item.name === name)
          const num = find ? Number(find.value || 0) : 0
          const percent = total ? ((num / total) * 100).toFixed(1) : '0.0'
          return `${name}  ${num}人  ${percent}%`
        }

        this.chartInstance.setOption(optionPie, { notMerge: true })
      },
      getPieOption(myChart) {
        return {
          title: {
            text: '年龄分布',
            left: 'center',
            top: '42%',
            left: '20%',
            textStyle: {
              fontSize: 16,
              color: '#86E1FF',
              align: 'center'
            },
          },
          tooltip: {
            trigger: "item",
            formatter: (params) => {
              return `${params.marker}${params.name}<br/>${params.value}人  ${params.percent}%`
            }
          },
          legend: {
            orient: "vertical",
            left: "55%",
            top: "center",
            align:"left",
            itemWidth: 16,
            itemHeight: 12,
            textStyle: { //图例文字的样式
                color: '#fff',
                fontSize: 12
            },
            itemGap: 5, // 修改间距
          },
          series: [
            {
              type: "pie",
              radius: ["60%", "80%"],
              center: ['30%', '50%'],
              avoidLabelOverlap: false,
              label: {
                // formatter: '年龄分布',
                show: false,
                position: "center",
              },
              color: ['#2E53F3', '#8EE157', '#FFE822', '#FF9922', '#FF4122', 'indigo', 'purple'],
              itemStyle: {
                // borderRadius: 5,
                borderColor: '#06112D',
                borderWidth: 3
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: "20",
                  fontWeight: "bold",
                  rich: {
                    total:{
                        fontSize: 20,
                        fontWeight: 'bold',
                        color:'#fff'
                    },
                    active: {
                        fontSize: 14,
                        color:'#fff',
                        lineHeight:30,
                    },
                  }
                },
              },
              labelLine: {
                show: false,
              },
              data: [],
            },
          ],
        }
      },
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