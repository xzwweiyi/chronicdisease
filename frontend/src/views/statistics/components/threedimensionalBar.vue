<template>
    <div class="echarts-container">
        <div ref="chartContainer" style="width: 100%; height: 500px;"></div>
        <!-- <div class="noCount" v-else>
            <p>暂无数据</p>
        </div> -->
    </div>
</template>

<script>
import * as echarts from "echarts";
import "echarts-gl";
export default {
    name: 'chart',
    components: {
        // ECharts
    },
    data() {
      return {
          chart: null,
          data: [
            { category: '内科', value: 82, segments: [60, 22] },
            { category: '外科', value: 78, segments: [55, 23] },
            { category: '内分泌科', value: 74, segments: [50, 24] },
            { category: '外二科', value: 90, segments: [70, 20] },
            { category: '内分泌科', value: 74, segments: [50, 24] },
            { category: '内科', value: 74, segments: [55, 19] },
          ],
        }
    },
    props: ['optionsData'],
    watch: {
        optionsData(newVal) { }
  },
    mounted() {
      this.initChart()
  },
    beforeDestroy() {
      if (this.chart) {
        this.chart.dispose();
      }
    },
    methods: {
        initChart() {
      const container = this.$refs.chartContainer;
      this.chart = echarts.init(container);

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}人'
        },
        xAxis3D: {
          type: 'category',
          data: this.data.map(item => item.category),
          name: '科目',
        },
        yAxis3D: {
          type: 'category',
          data: ['人数'],
          name: '数量',
        },
        zAxis3D: {
          type: 'value',
          name: '值',
        },
        grid3D: {
          boxWidth: 250,
          boxDepth: 100,
          viewControl: {
            projection: 'perspective',
            distance: 200,
            alpha: 40,
            beta: 30,
            rotateMouseButton: 'right'
          },
          light: {
            main: {
              intensity: 1.2,
              color: '#fff',
              shadow: true
            },
            ambient: {
              intensity: 0.3
            }
          }
        },
        series: [
          {
            type: 'bar3D',
            data: this.generate3DData(),
            shading: 'color',
            itemStyle: {
              borderColor: '#fff',
              borderWidth: 1,
            },
            barWidth: 30,
            label: {
              show: true,
              formatter: '{c}',
              position: 'top',
              color: '#fff',
            }
          }
        ]
      };

      this.chart.setOption(option);
    },

    // 生成3D数据，每个柱子分段并为每个段分配不同的颜色
    generate3DData() {
      const data = [];
      this.data.forEach((item, index) => {
        let offsetZ = 0;
        item.segments.forEach((segment, segmentIndex) => {
          data.push([
            index, // X轴位置
            0, // Y轴位置，固定
            offsetZ, // Z轴坐标，控制每个段的深度
            segment, // 柱子的值
            this.getColorForSegment(segmentIndex) // 为每个段指定颜色
          ]);
          offsetZ += segment; // 更新Z轴坐标
        });
      });
      return data;
    },

    // 根据分段返回不同的颜色
    getColorForSegment(segmentIndex) {
      const colors = [
        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#ffcc00' },
          { offset: 1, color: '#ff9900' }
        ]), // 黄色渐变
        new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#3399ff' },
          { offset: 1, color: '#66b3ff' }
        ]) // 蓝色渐变
      ];
      return colors[segmentIndex % colors.length];
    }
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