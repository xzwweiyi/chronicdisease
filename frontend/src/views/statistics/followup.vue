<template>
    <!-- 随访分布统计 -->
    <div class="app-container" v-loading="isLoading || isResearchStatusLoading">
        <el-form :model="distributeParams" class="search_form" inline v-hasPermi="['research:diseases:list']">
            <el-form-item label="科室：">
                <el-select v-model="distributeParams.deptId" placeholder="全部" clearable @change="getFollowStatusData">
                    <el-option v-for="(item, index) in administrativeOption" :key="index" :label="item.label"
                        :value="item.id"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <h4 class="com-details_title">已建队列分布</h4>
        <CustomEchart class="bar_content" :optionsData="distributeBarOption"></CustomEchart>
        <el-form :model="followupParams" ref="numberValidateForm" class="search_form" inline
            v-hasPermi="['research:diseases:list']">
            <el-form-item label="科室：">
                <el-select v-model="followupParams.deptId" placeholder="全部" clearable @change="deptOptionChange">
                    <el-option v-for="(item, index) in administrativeOption" :key="index" :label="item.label"
                        :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="病种：">
                <el-select v-model="followupParams.diseaseId" placeholder="请选择" clearable @change="diseaseChange"
                    :disabled="!followupParams.deptId">
                    <el-option v-for="(item, index) in diseasesOption" :key="index" :label="item.label"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="队列：">
                <el-select v-model="followupParams.researchId" :disabled="!followupParams.diseaseId" placeholder="请选择"
                    clearable @change="followupChange">
                    <el-option v-for="(item, index) in queueOption" :key="index" :label="item.label" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <h4 class="com-details_title">依从性</h4>
        <CustomEchart class="bar_content" :optionsData="barOption"></CustomEchart>
        <!-- <div class="date_search_content">
           
        </div> -->
        <div class="custom_chart">
            <div class="custom_chart_item">
                <h4 class="com-details_title">预警分布</h4>

                <el-radio-group v-model="followupParams.timeRegion" @change="timeRegionChange">
                    <el-radio-button label="0">近7天</el-radio-button>
                    <el-radio-button label="1">近30天</el-radio-button>
                    <el-radio-button label="2">近1年</el-radio-button>
                    <el-radio-button label="3">自定义</el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="dateRange" type="daterange" range-separator="-" start-placeholder="开始日期"
                    style="width:230px" end-placeholder="结束日期" @change="dateRangeChange"
                    v-if="followupParams.timeRegion == 3" class="date_range" value-format="yyyy-MM-dd">
                </el-date-picker>

                <custom-legend-chart class="pie_content" :legend-data="warnLegendData" :optionsData="warnPieOption">
                </custom-legend-chart>

            </div>
            <div class="custom_chart_item">
                <h4 class="com-details_title">随访率</h4>
                <el-radio-group v-model="followRateParams.timeRegion">
                    <el-radio-button label="1">患者入组时间</el-radio-button>
                    <el-radio-button label="2" :disabled="!this.followRateParams.inputStartTime">患者随访时间</el-radio-button>
                </el-radio-group>
                <el-date-picker v-model="dateRange1" type="daterange" range-separator="-" start-placeholder="开始日期"
                    style="width:230px" end-placeholder="结束日期" @change="rateRangeChange"  v-if="followRateParams.timeRegion == 1" class="date_range"
                    value-format="yyyy-MM-dd">
                </el-date-picker>
                 <el-date-picker v-model="dateRange2" type="daterange" range-separator="-" start-placeholder="开始日期"
                    style="width:230px" end-placeholder="结束日期" @change="rateFollowRangeChange"  v-if="followRateParams.timeRegion == 2" class="date_range"
                    value-format="yyyy-MM-dd">
                </el-date-picker>

                <custom-legend-chart class="pie_content" :legend-data="rateLegendData"
                    :optionsData="rateWindowPieOption"></custom-legend-chart>
            </div>
        </div>
    </div>
</template>

<script>
import CustomEchart from "@/views/components/echarts/index"
import chartOption from "@/views/components/options/mix_option.js"
import CustomLegendChart from "@/views/components/echarts/customLegendChart"
import {
    getFollowDistribute,
    getFollowupDiseaseOption,
    getFollowStatusNum,
    getFollowRate
} from "@/api/statistics/config"
import comFilters from "@/filters/index"
export default {
    name: "followup-distribution",
    components: {
        CustomEchart,
        CustomLegendChart
    },
    data() {
        return {
            isLoading: false,
            followupParams: {
                deptId: '',
                diseaseId: '',
                researchId: '',
                timeRegion: 0,
                startTime: null,
                endTime: null
            },
            followRateParams: {
                deptId: '',
                diseaseId: '',
                researchId: '',
                timeRegion: 0,
                inputStartTime: null,
                inputEndTime: null,
                followStartTime: null,
                followEndTime: null
            },
            distributeParams: {
                deptId: '',
            },
            dateRange: '',
            dateRange1: '',
            dateRange2: '',
            barOption: null,
            warnPieOption: null,
            hyperwindowPieOption: null,
            rateWindowPieOption: null,
            diseasesOption: [],
            queueOption: [],
            administrativeOption: [],
            warnLegendData: [],
            rateLegendData: [],
            hyperLegendData: [],
            distributeBarOption: null,
            isResearchStatusLoading: false,
        };
    },
    watch: {},
    created() {
        this.initData();
    },
    methods: {
        // 初始化数据
        initData() {
            this.getAdministrativeOption();
            this.getFollowDistributeStatistics();
            this.getFollowStatusData()
            this.getFollowRate()
        },
        // 获取科室下拉数据
        getAdministrativeOption() {
            getFollowupDiseaseOption().then(res => {
                this.administrativeOption = res.data
            })
        },
        // 科室选择
        deptOptionChange(event) {
            this.followupParams.researchId = ''
            this.diseasesOption = []
            this.followupParams.diseaseId = ''
            this.queueOption = []
            if (event) {
                this.diseasesOption = this.administrativeOption.find(f => f.id == this.followupParams.deptId)
                    .children
            }
            this.getFollowDistributeStatistics();
            this.getFollowRate()
        },
        // 病种选择
        diseaseChange(event) {
            this.followupParams.researchId = ''
            this.queueOption = []
            if (event) {
                this.queueOption = this.diseasesOption.find(f => f.id == this.followupParams.diseaseId).children
            }
            this.getFollowDistributeStatistics();
            this.getFollowRate()
        },
        // 队列选择
        followupChange(event) {
            this.getFollowDistributeStatistics();
            this.getFollowRate();
        },
        getFollowStatusData() {
            this.isResearchStatusLoading = true;
            getFollowStatusNum(this.distributeParams).then(response => {
                if (response && response.length) {
                    this.distributeBarOption = this.setStatusNumBarOption(response)
                } else {
                    this.distributeBarOption = null
                }
                this.isResearchStatusLoading = false;
            }).catch(error => {
                this.isResearchStatusLoading = false;
            })
        },
        // 时间类型切换
        timeRegionChange(event) {
            if (event != 3) {
                this.followupParams.startTime = null;
                this.followupParams.endTime = null;
                this.getFollowDistributeStatistics();
            }
        },
        // 自定义时间change
        dateRangeChange(event) {
            if (event) {
                this.followupParams.startTime = event[0];
                this.followupParams.endTime = event[1];
            } else {
                this.followupParams.startTime = null;
                this.followupParams.endTime = null;
            }
            this.getFollowDistributeStatistics();
        },

        // 随访率入队列时间
        rateRangeChange(event) {
            if (event) {
                this.followRateParams.inputStartTime = event[0];
                this.followRateParams.inputEndTime = event[1];
            } else {
                this.followRateParams.inputStartTime = null;
                this.followRateParams.inputEndTime = null;
            }
            this.getFollowRate();
        },

        rateFollowRangeChange(event) {
            if (event) {
                this.followRateParams.followStartTime = event[0];
                this.followRateParams.followEndTime = event[1];
            } else {
                this.followRateParams.followStartTime = null;
                this.followRateParams.followEndTime = null;
            }
            this.getFollowRate();
        },

        // 获取随访率
        getFollowRate() {
            this.isLoading = true;
            const parms = {
                ...this.followRateParams,
                deptId: this.followupParams.deptId,
                diseaseId: this.followupParams.diseaseId,
                researchId: this.followupParams.researchId,
            }
            getFollowRate(parms).then(response => {
                this.isLoading = false;
                if (response) {
                    this.setRatewindowOption(response)
                } else {
                    this.rateWindowPieOption = null
                }
            }).catch(() => {
                this.isLoading = false;
            })
        },

        // 获取随访分布统计
        getFollowDistributeStatistics() {
            this.isLoading = true;
            getFollowDistribute(this.followupParams).then(response => {
                if (response) {
                    if (response.rely && response.rely.length) {
                        this.setBarOption(response.rely)
                    } else {
                        this.barOption = null
                    }
                    if (response.warn && response.warn.length) {
                        this.setWarnPieOption(response.warn)
                    } else {
                        this.warnPieOption = null
                    }
                    if (response.overTime && response.overTime.length) {
                        this.setHyperwindowOption(response.overTime)
                    } else {
                        this.hyperwindowPieOption = null
                    }
                } else {
                    this.barOption = null
                    this.warnPieOption = null
                    this.hyperwindowPieOption = null
                }
                this.isLoading = false;
            }).catch(error => {
                this.barOption = null
                this.warnPieOption = null
                this.hyperwindowPieOption = null
                this.isLoading = false;
            })
        },
        // 队列状态数统计
        setStatusNumBarOption(data) {
            let complyBarOption = chartOption.getBarOption();
            let seriesData = {
                ...complyBarOption.series[0]
            }
            complyBarOption.color = ['#5A88EF', '#EC1309']
            complyBarOption.series.push(seriesData)
            complyBarOption.xAxis.data = data.map(m => m.name)
            complyBarOption.series.forEach((item, index) => {
                if (index == 0) {
                    item.data = data.map(m => {
                        return {
                            value: m.goingNum,
                            showVal: m.goingNum
                        }
                    })
                    item.name = '进行中队列'
                } else {
                    item.data = data.map(m => {
                        return {
                            value: m.closeNum,
                            showVal: m.closeNum
                        }
                    })
                    item.name = '已关闭队列'
                }
                item.stack = 'bar'
                item.barWidth = 40
                item.label = {
                    show: true,
                    color: '#fff',
                    fontSize: 12,
                    formatter: (params => {
                        if (params.data.showVal) {
                            return params.data.showVal
                        } else {
                            return ''
                        }
                    })
                }
            })

            complyBarOption.tooltip.formatter = (paramsData) => {
                let text = `${paramsData[0].name}`
                paramsData.forEach(item => {
                    text =
                        `${text}</br>${item.marker}${item.seriesName}: ${comFilters.nullFormat(item.data.showVal)}`
                })
                return text
            }
            return complyBarOption
        },
        // 依从性
        setBarOption(data) {
            let complyBarOption = chartOption.getBarOption();
            let seriesData = {
                ...complyBarOption.series[0]
            }
            complyBarOption.color = ['#5A88EF', '#EC1309']
            complyBarOption.series.push(seriesData)
            complyBarOption.xAxis.data = data.map(m => m.name)
            complyBarOption.series.forEach((item, index) => {
                if (index == 0) {
                    item.data = data.map(m => {
                        return {
                            value: m.followNums,
                            showVal: m.followNums
                        }
                    })
                    item.name = '随访人数'
                } else {
                    // item.data = data.map(m => m.dropNums)
                    item.data = data.map(m => {
                        return {
                            value: m.dropNums,
                            showVal: m.dropNums
                        }
                    })
                    item.name = '失访人数'
                }
                item.stack = 'bar'
                item.barWidth = 40
                item.label = {
                    show: true,
                    color: '#fff',
                    fontSize: 12,
                    formatter: (params => {
                        if (params.data.showVal) {
                            return params.data.showVal
                        } else {
                            return ''
                        }
                    })
                }
            })
            complyBarOption.tooltip.formatter = (paramsData) => {
                let text = `${paramsData[0].name}`
                paramsData.forEach(item => {
                    text =
                        `${text}</br>${item.marker}${item.seriesName}: ${comFilters.nullFormat(item.data.showVal)}人`
                })
                return text
            }
            this.barOption = complyBarOption
        },
        setWarnPieOption(data) {
            let pieOptions = chartOption.getPieOption();
            pieOptions.series[0].data = data.map(m => {
                return {
                    value: m.followNums,
                    name: m.name,
                    percent: m.followRate
                }
            })
            // pieOptions.legend.show = true
            // pieOptions.legend.data = data.map(m => m.name)
            // pieOptions.legend.top = 5
            pieOptions.series[0].center = ['50%', '40%']
            pieOptions.series[0].radius = [0, "60%"]
            pieOptions.series[0].label.lineHeight = 15
            pieOptions.series[0].label.formatter = (params) => {
                return `${params.data.name}：${params.data.value}\n占比：${params.data.percent}`
            }
            pieOptions.tooltip.formatter = (params => {
                return `${params.marker}${params.name}：${params.value}</br>占比：${params.data.percent}`
            })
            pieOptions.series[0].emphasis = {
                scale: false,
                label: {
                    show: true,
                    fontSize: "12",
                }
            }
            this.warnLegendData = data.map((m, index) => {
                return {
                    value: m.name,
                    checked: true,
                    color: chartOption.colors[index]
                }
            })
            this.warnPieOption = pieOptions
        },
        setHyperwindowOption(data) {
            let pieOptions = chartOption.getPieOption();
            pieOptions.series[0].data = data.map(m => {
                return {
                    value: m.followNums,
                    name: m.name,
                    percent: m.followRate
                }
            })
            // pieOptions.legend.show = true
            // pieOptions.legend.data = data.map(m => m.name)
            // pieOptions.legend.top = 5
            pieOptions.series[0].center = ['50%', '40%']
            pieOptions.series[0].radius = [0, "60%"]
            pieOptions.series[0].label.lineHeight = 15
            pieOptions.series[0].label.formatter = (params) => {
                return `${params.data.name}：${params.data.value}\n占比：${params.data.percent}`
            }
            pieOptions.series[0].emphasis = {
                scale: false,
                label: {
                    show: true,
                    fontSize: "12",
                }
            }
            pieOptions.tooltip.formatter = (params => {
                return `${params.marker}${params.name}：${params.value}</br>占比：${params.data.percent}`
            })
            this.hyperLegendData = data.map((m, index) => {
                return {
                    value: m.name,
                    checked: true,
                    color: chartOption.colors[index]
                }
            })
            this.hyperwindowPieOption = pieOptions
        },


        setRatewindowOption(res) {
            let pieOptions = chartOption.getPieOption();
            console.log(res)
            // 兼容处理：如果返回的是 {code: 200, data: {...}} 格式
            const data = res.data || res;
            const chartData = [
                {
                    name: data.followRateName || '随访率',
                    value: parseFloat(data.followRate) || 0,
                    percent: data.followRate,
                    followNum: data.followNum,
                    totalNum: data.totalNum
                },
                {
                    name:  '失访率',
                    value: parseFloat(data.notFollowRate) || 0,
                    percent: data.notFollowRate,
                    followNum: data.notFollowNum || (data.totalNum - data.followNum),
                    totalNum: data.totalNum
                }
            ]
            pieOptions.series[0].data = chartData
            // pieOptions.legend.show = true
            // pieOptions.legend.data = chartData.map(m => m.name)
            // pieOptions.legend.top = 5
            pieOptions.series[0].center = ['50%', '40%']
            pieOptions.series[0].radius = [0, "60%"]
            pieOptions.series[0].label.lineHeight = 15
            pieOptions.series[0].label.formatter = (params) => {
                const labelName = params.data.name === (data.followRateName || '随访率') ? '随访' : '未随访'
                return `${params.data.name}：${params.data.percent}\n(${labelName}：${params.data.followNum} / 总计：${params.data.totalNum})`
            }
            pieOptions.series[0].emphasis = {
                scale: false,
                label: {
                    show: true,
                    fontSize: "12",
                }
            }
            pieOptions.tooltip.formatter = (params => {
                const labelName = params.data.name === (data.followRateName || '随访率') ? '随访人数' : '未随访人数'
                return `${params.marker}${params.name}：${params.data.percent}<br/>${labelName}：${params.data.followNum}<br/>入组人数：${params.data.totalNum}`
            })
            this.rateLegendData = chartData.map((m, index) => {
                return {
                    value: m.name,
                    checked: true,
                    color: chartOption.colors[index]
                }
            })
            this.rateWindowPieOption = pieOptions
        }

    }
};
</script>
<style lang="scss" scoped>
.date_range {
    margin-left: 20px;
}

.bar_content {
    height: 45vh !important;
}

.custom_chart {
    display: flex;

    .custom_chart_item {
        width: 49%;

        .pie_content {
            margin-top: 30px;
        }

        &:first-child {
            margin-right: 2%;
        }
    }
}
</style>