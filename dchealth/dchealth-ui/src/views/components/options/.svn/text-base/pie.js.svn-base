import filter from "@/filters/common.js";
const colors = ['#296CFC', '#3BC4FC', '#50A7FF', '#52E0C9', '#3E98E3', '#2167E8', '#1F3B89']

export default {
    colors,
    getOption() {
        return {
            legend: {
                top: 'bottom',
                show: false
            },
            color:colors,
            tooltip: {
                trigger: 'item',
                backgroundColor: 'rgba(7,21,56,0.8)',
                textStyle: {
                    color: '#fff',
                    fontSize: filter.reversePixel(16),
                },
            },
            series: [{
                name: '',
                startAngle:270,
                minAngle:10,
                type: 'pie',
                radius: ['10%', '60%'],
                center: ['50%', '50%'],
                roseType: 'area',
                showEmptyCircle:false,
                itemStyle: {
                    borderRadius: 2
                },
                label: {
                    show: true,
                    fontSize: filter.reversePixel(12),
                    lineHeight: filter.reversePixel(15),
                    color: '#fff',
                    // formatter: '{b}\n ',
                    rich:{
                        a:{
                            color:'#fff',
                            fontSize: filter.reversePixel(12),
                        }
                    }
                },
                labelLine: {
                    show: true,
                    length: 10,
                    length2: 10,
                },
                data: []
            }]
        }
    },
}