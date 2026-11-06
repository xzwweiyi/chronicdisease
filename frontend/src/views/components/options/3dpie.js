function getParametricEquation(startRatio, endRatio, isSelected, isHovered, k, h) {
  // 计算
  let midRatio = (startRatio + endRatio) / 2;
  let startRadian = startRatio * Math.PI * 2;
  let endRadian = endRatio * Math.PI * 2;
  let midRadian = midRatio * Math.PI * 2;
  isSelected = false;
  k = typeof k !== "undefined" ? k : 1 / 3;
  let offsetX = isSelected ? Math.sin(midRadian) * 0.1 : 0;
  let offsetY = isSelected ? Math.cos(midRadian) * 0.1 : 0;
  let hoverRate = isHovered ? 1.05 : 1;
  return {
    u: {
      min: -Math.PI,
      max: Math.PI * 3,
      step: Math.PI / 32,
    },
    v: {
      min: 0,
      max: Math.PI * 2,
      step: Math.PI / 20,
    },
    x: function (u, v) {
      if (u < startRadian) {
        return (offsetX + Math.cos(startRadian) * (1 + Math.cos(v) * k) * hoverRate);
      }
      if (u > endRadian) {
        return (offsetX + Math.cos(endRadian) * (1 + Math.cos(v) * k) * hoverRate);
      }
      return offsetX + Math.cos(u) * (1 + Math.cos(v) * k) * hoverRate;
    },
    y: function (u, v) {
      if (u < startRadian) {
        return (offsetY + Math.sin(startRadian) * (1 + Math.cos(v) * k) * hoverRate);
      }
      if (u > endRadian) {
        return (offsetY + Math.sin(endRadian) * (1 + Math.cos(v) * k) * hoverRate);
      }
      return offsetY + Math.sin(u) * (1 + Math.cos(v) * k) * hoverRate;
    },
    z: function (u, v) {
      if (u < -Math.PI * 0.5) {
        return Math.sin(u);
      }
      if (u > Math.PI * 2.5) {
        return Math.sin(u) * h * 0.1;
      }
      return Math.sin(v) > 0 ? 1 * h * 0.1 : -1;
    },
  };
}

export function getPie3D(pieData, internalDiameterRatio) {
  let series = [];
  let sumValue = 0;
  let startValue = 0;
  let endValue = 0;
  let legendData = [];
  let k = typeof internalDiameterRatio !== "undefined" ? (1 - internalDiameterRatio) / (1 + internalDiameterRatio) : 1 / 3;
  let widthValue = nowSize(378);
  const optionsData = pieData;
  const color = ["#2E53F3", "#FFE822", "rgba(210, 0, 255, 1)"]

  for (let i = 0; i < pieData.length; i++) {
    sumValue += pieData[i].value;
    let seriesItem = {
      name: typeof pieData[i].name === "undefined" ? `series${i}` : pieData[i].name,
      type: "surface",
      parametric: true,
      wireframe: {
        show: false,
      },
      pieData: pieData[i],
      pieStatus: {
        selected: false,
        hovered: false,
        k: 1 / 10,
      },
    };
    if (typeof pieData[i].itemStyle != "undefined") {
      let itemStyle = {};
      typeof pieData[i].itemStyle.color != "undefined" ? (itemStyle.color = pieData[i].itemStyle.color) : null;
      typeof pieData[i].itemStyle.opacity != "undefined" ? (itemStyle.opacity = pieData[i].itemStyle.opacity) : null;
      seriesItem.itemStyle = itemStyle;
    }
    series.push(seriesItem);
  }
  for (let i = 0; i < series.length; i++) {
    endValue = startValue + series[i].pieData.value;
    series[i].pieData.startRatio = startValue / sumValue;
    series[i].pieData.endRatio = endValue / sumValue;
    series[i].parametricEquation = getParametricEquation(
      series[i].pieData.startRatio,
      series[i].pieData.endRatio,
      false,
      false,
      k,
      series[i].pieData.value
    );
    startValue = endValue;
    legendData.push(series[i].name);
  }

  // series.push({
  //   name: "pie2d",
  //   type: "pie",
  //   label: {
  //     opacity: 1,
  //     edgeDistance: "1%",
  //     alignTo: "edge",
  //     distanceToLabelLine: 0,
  //     formatter: (params) => {
  //       return `{name|${params.name}}\n{${
  //           params.dataIndex == 0
  //             ? "red"
  //             : params.dataIndex == 1
  //             ? "blue"
  //             : "green"
  //         }|(${params.percent}%)}`;
  //     },
  //     color: "#fff",
  //     rich: {
  //       red: {
  //         fontSize: nowSize(16),
  //         color: "#00BCEE",
  //       },
  //       blue: {
  //         fontSize: nowSize(16),
  //         color: "#0BE19E",
  //       },
  //       green: {
  //         fontSize: nowSize(16),
  //         color: "#EAA031",
  //       },
  //       name: {
  //         fontSize: nowSize(14),
  //         color: "#fff",
  //         align: "center",
  //       },
  //     },
  //   },
  //   labelLine: {
  //     length: "10%",
  //     length2: 0,
  //   },
  //   labelLayout: function (params) {
  //     const isLeft = params.labelRect.x < widthValue / 2;
  //     const points = params.labelLinePoints;
  //     // Update the end point.
  //     points[2][0] = isLeft ?
  //       params.labelRect.x :
  //       params.labelRect.x + params.labelRect.width;
  //     return {
  //       labelLinePoints: points,
  //       verticalAlign: "bottom",
  //     };
  //   },
  //   startAngle: -50, //起始角度，支持范围[0, 360]。
  //   clockwise: false, //饼图的扇区是否是顺时针排布。上述这两项配置主要是为了对齐3d的样式
  //   radius: ["20%", "40%"],

  //   center: ["50%", "50%"],
  //   data: optionsData,
  //   itemStyle: {
  //     opacity: 0,
  //     color: (params) => {
  //       return color[params.dataIndex];
  //     },
  //   },
  // });
    
  let option = {
    color: ["#2E53F3", "#FFE822", "rgba(210, 0, 255, 1)"],
    title: {
      text: "",
      right: 'center',
      top: '35%',
      textStyle: {
        color: "#fff",
        fontWeight: 400,
        fontSize: 24,
        rich: {
          num: {
            fontSize: 14
          }
        },
      },
    },
    legend: {
      bottom: "10",
      itemWidth: 10,
      itemHeight: 10,
      icon: "rund",
      textStyle: {
        color: "#fff"
      },
      formatter: function (param) {
        let sum = 0;
        pieData.forEach(item => {
          sum += Number(item.value) || 0;
        });
        let item = pieData.find(item => item.name == param) || { name: param, value: 0 };
        let percent = sum ? ((Number(item.value) || 0) / sum * 100).toFixed(2) : '0.00';
        return `${item.name}  ${percent}%`;
      }
    },
    xAxis3D: {},
    yAxis3D: {},
    zAxis3D: {},
    grid3D: {
      // distance缩小、alpha放大、beta调整角度
      viewControl: {
        autoRotate: false,
        distance: 120,
        alpha: 26,
        beta: 60,
      },
      left: "center",
      top: -20,
      show: false,
      boxHeight: 20,
    },
    series: series,
  };
  return option;
}



  function nowSize(val, initWidth = 3280) {
    var nowClientWidth = document.documentElement.clientWidth;
    return val * (nowClientWidth / initWidth);
}
  
// export function getPie3D(pieData, internalDiameterRatio) {
//   //internalDiameterRatio:透明的空心占比
//   let series = [];
//   let sumValue = 0;
//   let startValue = 0;
//   let endValue = 0;
//   let legendData = [];
//   let legendBfb = [];
//   let k = 1 - internalDiameterRatio;
//   pieData.sort((a, b) => {
//     return (b.value - a.value);
//   });
//   // 为每一个饼图数据，生成一个 series-surface 配置
//   for (let i = 0; i < pieData.length; i++) {
//     sumValue += pieData[i].value;
//     let seriesItem = {
//       name: typeof pieData[i].name === 'undefined' ? `series${i}` : pieData[i].name,
//       type: 'surface',
//       parametric: true,
//       wireframe: {
//         show: false
//       },
//       pieData: pieData[i],
//       pieStatus: {
//         selected: false,
//         hovered: false,
//         k: k
//       },
//       center: ['10%', '50%']
//     };

//     if (typeof pieData[i].itemStyle != 'undefined') {
//       let itemStyle = {};
//       typeof pieData[i].itemStyle.color != 'undefined' ? itemStyle.color = pieData[i].itemStyle.color : null;
//       typeof pieData[i].itemStyle.opacity != 'undefined' ? itemStyle.opacity = pieData[i].itemStyle.opacity : null;
//       seriesItem.itemStyle = itemStyle;
//     }
//     series.push(seriesItem);
//   }

//   // 使用上一次遍历时，计算出的数据和 sumValue，调用 getParametricEquation 函数，
//   // 向每个 series-surface 传入不同的参数方程 series-surface.parametricEquation，也就是实现每一个扇形。
//   legendData = [];
//   legendBfb = [];
//   for (let i = 0; i < series.length; i++) {
//     endValue = startValue + series[i].pieData.value;
//     series[i].pieData.startRatio = startValue / sumValue;
//     series[i].pieData.endRatio = endValue / sumValue;
//     series[i].parametricEquation = getParametricEquation(series[i].pieData.startRatio, series[i].pieData.endRatio,
//       false, false, k, series[i].pieData.value);
//     startValue = endValue;
//     let bfb = fomatFloat(series[i].pieData.value / sumValue, 4);
//     legendData.push({
//       name: series[i].name,
//       value: bfb
//     });
//     legendBfb.push({
//       name: series[i].name,
//       value: bfb
//     });
//   }
//   let boxHeight = getHeight3D(series, 26); //通过传参设定3d饼/环的高度，26代表26px
//   // 准备待返回的配置项，把准备好的 legendData、series 传入。
//   let option = {
//     legend: {
//       data: legendData,
//       orient: 'horizontal',
//       left: 10,
//       top: 10,
//       itemGap: 10,
//       textStyle: {
//         color: '#A1E2FF',
//       },
//       show: true,
//       icon: "circle",
//       formatter: function (param) {
//         let item = legendBfb.filter(item => item.name == param)[0];
//         let bfs = fomatFloat(item.value * 100, 2) + "%";
//         return `${item.name}  ${bfs}`;
//       }
//     },
//     labelLine: {
//       show: true,
//       lineStyle: {
//         color: '#7BC0CB'
//       }
//     },
//     label: {
//       show: true,
//       position: 'outside',
//       rich: {
//         b: {
//           color: '#7BC0CB',
//           fontSize: 12,
//           lineHeight: 20
//         },
//         c: {
//           fontSize: 16,
//         },
//       },
//       formatter: '{b|{b} \n}{c|{c}}{b|  条}',

//     },
//     tooltip: {
//       formatter: params => {
//         if (params.seriesName !== 'mouseoutSeries' && params.seriesName !== 'pie2d') {
//           let bfb = ((option.series[params.seriesIndex].pieData.endRatio - option.series[params.seriesIndex].pieData.startRatio) *
//             100).toFixed(2);
//           return `${params.seriesName}<br/>` +
//             `<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${params.color};"></span>` +
//             `${ bfb }%`;
//         }
//       }
//     },
//     xAxis3D: {
//       min: -1,
//       max: 1
//     },
//     yAxis3D: {
//       min: -1,
//       max: 1
//     },
//     zAxis3D: {
//       min: -1,
//       max: 1
//     },
//     grid3D: {
//       show: false,
//       boxHeight: boxHeight, //圆环的高度
//       viewControl: { //3d效果可以放大、旋转等，请自己去查看官方配置
//         alpha: 40, //角度
//         distance: 300, //调整视角到主体的距离，类似调整zoom
//         rotateSensitivity: 0, //设置为0无法旋转
//         zoomSensitivity: 0, //设置为0无法缩放
//         panSensitivity: 0, //设置为0无法平移
//         autoRotate: false //自动旋转
//       }
//     },
//     series: series
//   };
//   return option;
// }

// //获取3d丙图的最高扇区的高度
// function getHeight3D(series, height) {
//   series.sort((a, b) => {
//     return (b.pieData.value - a.pieData.value);
//   })
//   return height * 25 / series[0].pieData.value;
// }

// // 生成扇形的曲面参数方程，用于 series-surface.parametricEquation
// function getParametricEquation(startRatio, endRatio, isSelected, isHovered, k, h) {
//   // 计算
//   let midRatio = (startRatio + endRatio) / 2;
//   let startRadian = startRatio * Math.PI * 2;
//   let endRadian = endRatio * Math.PI * 2;
//   let midRadian = midRatio * Math.PI * 2;
//   // 如果只有一个扇形，则不实现选中效果。
//   if (startRatio === 0 && endRatio === 1) {
//     isSelected = false;
//   }
//   // 通过扇形内径/外径的值，换算出辅助参数 k（默认值 1/3）
//   k = typeof k !== 'undefined' ? k : 1 / 3;
//   // 计算选中效果分别在 x 轴、y 轴方向上的位移（未选中，则位移均为 0）
//   let offsetX = isSelected ? Math.cos(midRadian) * 0.1 : 0;
//   let offsetY = isSelected ? Math.sin(midRadian) * 0.1 : 0;
//   // 计算高亮效果的放大比例（未高亮，则比例为 1）
//   let hoverRate = isHovered ? 1.05 : 1;
//   // 返回曲面参数方程
//   return {
//     u: {
//       min: -Math.PI,
//       max: Math.PI * 3,
//       step: Math.PI / 32
//     },
//     v: {
//       min: 0,
//       max: Math.PI * 2,
//       step: Math.PI / 20
//     },
//     x: function (u, v) {
//       if (u < startRadian) {
//         return offsetX + Math.cos(startRadian) * (1 + Math.cos(v) * k) * hoverRate;
//       }
//       if (u > endRadian) {
//         return offsetX + Math.cos(endRadian) * (1 + Math.cos(v) * k) * hoverRate;
//       }
//       return offsetX + Math.cos(u) * (1 + Math.cos(v) * k) * hoverRate;
//     },
//     y: function (u, v) {
//       if (u < startRadian) {
//         return offsetY + Math.sin(startRadian) * (1 + Math.cos(v) * k) * hoverRate;
//       }
//       if (u > endRadian) {
//         return offsetY + Math.sin(endRadian) * (1 + Math.cos(v) * k) * hoverRate;
//       }
//       return offsetY + Math.sin(u) * (1 + Math.cos(v) * k) * hoverRate;
//     },
//     z: function (u, v) {
//       if (u < -Math.PI * 0.5) {
//         return Math.sin(u);
//       }
//       if (u > Math.PI * 2.5) {
//         return Math.sin(u) * h * .1;
//       }
//       return Math.sin(v) > 0 ? 1 * h * .1 : -1;
//     }
//   };
// }

function fomatFloat(num, n) {
  var f = parseFloat(num);
  if (isNaN(f)) {
    return false;
  }
  f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂   
  var s = f.toString();
  var rs = s.indexOf('.');
  //判定如果是整数，增加小数点再补0
  if (rs < 0) {
    rs = s.length;
    s += '.';
  }
  while (s.length <= rs + n) {
    s += '0';
  }
  return s;
}