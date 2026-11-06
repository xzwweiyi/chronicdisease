import filter from "@/filters/common.js";
const echarts = require("echarts/lib/echarts");
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

function getlegendData() {
  const data = [];
  labels.forEach((item, index) => {
    data.push({
      name: item,
      itemStyle: {
        color: new echarts.graphic.LinearGradient(1, 0, 0, 0, colors[index]),
      },
    })
  })
  return data
}

function getSeriesData() {
  const data = [];
  seriesData.forEach((item, index) => {
    data.push({
      type: 'custom',
      name: item.label,
      renderItem: function (params, api) {
        return getRenderItem(params, api);
      },
      data: item.value,
      itemStyle: {
        color: () => {
          return new echarts.graphic.LinearGradient(0, 0, 0, 1, colors[index]);
        },
      },
    })
  })
  return data
}

function getRenderItem(params, api) {
  const index = params.seriesIndex;
  let location = api.coord([api.value(0) + index, api.value(1)]);
  var extent = api.size([0, api.value(1)]);
  return {
    type: 'group',
    children: [{
        type: 'leftRect',
        shape: {
          api,
          xValue: api.value(0) + index,
          yValue: api.value(1),
          x: location[0],
          y: location[1]
        },
        style: api.style()
      },
      {
        type: 'rightRect',
        shape: {
          api,
          xValue: api.value(0) + index,
          yValue: api.value(1),
          x: location[0],
          y: location[1]
        },
        style: api.style()
      }
    ]
  };
}

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
              color: '#fff',
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
      series: getSeriesData()
    }
  }
}