<template>
    <!-- 队列患者分布 -->
    <div class="app-container" v-loading="isLoading">
        <el-form :model="queuePatientParams" ref="numberValidateForm" class="search_form" inline
            v-hasPermi="['research:diseases:list']">
            <el-form-item label="科室:">
                <el-select v-model="queuePatientParams.deptId" placeholder="全部" @change="deptOptionChange" clearable>
                    <el-option v-for="(item,index) in administrativeOption" :key="index" :label="item.label"
                        :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="病种:">
                <el-select v-model="queuePatientParams.diseaseId" placeholder="请选择" clearable @change="diseaseChange"
                    :disabled="!queuePatientParams.deptId">
                    <el-option v-for="(item,index) in diseasesOption" :key="index" :label="item.label" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>

        <h4 class="com-details_title">病种分布</h4>
        <CustomEchart class="bar_content" :optionsData="barOption"></CustomEchart>
        <div class="custom_chart">
            <div class="custom_chart_item">
                <h4 class="com-details_title">地域分布</h4>
                <CustomEchart class="pie_content" :optionsData="warnPieOption"></CustomEchart>
            </div>
            <div class="custom_chart_item">
                <h4 class="com-details_title">性别分布</h4>
                <CustomEchart class="pie_content" :optionsData="hyperwindowPieOption"></CustomEchart>
            </div>
            <div class="custom_chart_item">
                <h4 class="com-details_title">年龄分布</h4>
                <CustomEchart class="pie_content" :optionsData="agePieOption"></CustomEchart>
            </div>
        </div>
    </div>
</template>

<script>
    import CustomEchart from "@/views/components/echarts/index"
    import chartOption from "@/views/components/options/mix_option.js"
    import {
        getPatientDistribute,
        getFollowupDiseaseOption
    } from "@/api/statistics/config"
    import comFilters from "@/filters/index"
    export default {
        name: "followup-distribution",
        components: {
            CustomEchart
        },
        data() {
            return {
                isLoading: false,
                queuePatientParams: {
                    deptId: '',
                },
                dateType: '1',
                dateRange: '',
                administrativeOption: [],
                diseasesOption: [],
                barOption: null,
                warnPieOption: null,
                hyperwindowPieOption: null,
                agePieOption: null,
            };
        },
        created() {
            this.initData();
        },
        methods: {
            // 获取科室下拉数据
            getAdministrativeOption() {
                getFollowupDiseaseOption().then(res => {
                    this.administrativeOption = res.data
                })
            },
            // 初始化数据
            initData() {
                this.getAdministrativeOption()
                this.getFollowDistributeStatistics()
            },
            // 科室选择
            deptOptionChange(event) {
                this.diseasesOption = []
                this.queuePatientParams.diseaseId = ''
                if (event) {
                    this.diseasesOption = this.administrativeOption.find(f => f.id == this.queuePatientParams.deptId)
                        .children
                }
                this.getFollowDistributeStatistics();
            },
            // 病种选择
            diseaseChange(event) {
                this.getFollowDistributeStatistics();
            },
            // 获取随访分布统计
            getFollowDistributeStatistics() {
                this.isLoading = true;
                getPatientDistribute(this.queuePatientParams).then(response => {
                    if (response) {
                        if (response.disease && response.disease.length) {
                            this.setBarOption(response.disease)
                        } else {
                            this.barOption = null
                        }
                        if (response.city && response.city.length) {
                            this.setWarnPieOption(response.city)
                        } else {
                            this.warnPieOption = null
                        }
                        if (response.sex && response.sex.length) {
                            this.setHyperwindowOption(response.sex)
                        } else {
                            this.hyperwindowPieOption = null
                        }
                        if (response.age && response.age.length) {
                            this.setAgePieOption(response.age)
                        } else {
                            this.agePieOption = null
                        }
                    } else {
                        this.barOption = null
                        this.warnPieOption = null
                        this.hyperwindowPieOption = null
                        this.agePieOption = null
                    }
                    this.isLoading = false;
                }).catch(error => {
                    this.barOption = null
                    this.warnPieOption = null
                    this.hyperwindowPieOption = null
                    this.agePieOption = null;
                    this.isLoading = false;
                })
            },
            // 病种分布
            setBarOption(data) {
                let complyBarOption = chartOption.getBarOption();
                complyBarOption.color = ['#5A88EF', '#EC1309']
                complyBarOption.xAxis.data = data.map(m => m.name)
                complyBarOption.series.forEach((item, index) => {
                    item.data = data.map(m => m.total)
                    item.name = '随访人数'
                    item.stack = 'bar'
                    item.barWidth = 40
                    item.label = {
                        show: true,
                        color: '#fff',
                        fontSize: 12
                    }
                    item.position = 'top'
                })

                complyBarOption.tooltip.formatter = (paramsData) => {
                    let text = `${paramsData[0].name}`
                    paramsData.forEach(item => {
                        text =
                            `${text}</br>${item.marker}${item.seriesName}: ${comFilters.nullFormat(item.value)}人`
                    })
                    return text
                }
                this.barOption = complyBarOption
            },
            // 地域分布
            setWarnPieOption(data) {
                let pieOptions = chartOption.getPieOption();
                pieOptions.series[0].data = data.map(m => {
                    return {
                        value: m.total,
                        name: m.name
                    }
                })
                pieOptions.legend.show = true
                pieOptions.legend.data = data.map(m => m.name)
                pieOptions.series[0].radius = [0, "48%"]
                pieOptions.series[0].label.formatter = (params) => {
                    return `${params.data.name}：${params.data.value}`
                }
                pieOptions.series[0].emphasis = {
                    label: {
                        show: true,
                        fontSize: "14",
                        fontWeight: "bold",
                    }
                }
                this.warnPieOption = pieOptions

            },
            // 性别分布
            setHyperwindowOption(data) {
                let pieOptions = chartOption.getPieOption();
                pieOptions.series[0].data = data.map(m => {
                    return {
                        value: m.total,
                        name: m.name
                    }
                })
                pieOptions.legend.show = true
                pieOptions.legend.data = data.map(m => m.name)
                pieOptions.series[0].radius = [0, "48%"]
                pieOptions.series[0].emphasis = {
                    label: {
                        show: true,
                        fontSize: "14",
                        fontWeight: "bold",
                    }
                }
                this.hyperwindowPieOption = pieOptions
            },
            // 年龄分布
            setAgePieOption(data) {
                let pieOptions = chartOption.getPieOption();
                pieOptions.series[0].data = data.map(m => {
                    return {
                        value: m.total,
                        name: m.name
                    }
                })
                pieOptions.legend.show = true
                pieOptions.legend.data = data.map(m => m.name)
                pieOptions.series[0].radius = [0, "45%"]
                pieOptions.series[0].center = ['50%', "60%"]
                pieOptions.series[0].labelLine.length = 20
                pieOptions.series[0].emphasis = {
                    label: {
                        show: true,
                        fontSize: "14",
                        fontWeight: "bold",
                    }
                }
                this.agePieOption = pieOptions
            }

        }
    };
</script>
<style lang="scss" scoped>
    ::v-deep .el-form {
        .date_search_content {
            .el-form-item__content {
                display: flex;
            }

            .date_range {
                margin-left: 10px;
            }

        }
    }

    .bar_content {
        height: 45vh !important;
    }

    .custom_chart {
        display: flex;

        .custom_chart_item {
            width: 30%;
            margin-left: 10px;


            .pie_content {
                height: 45vh !important;
            }

            &:first-child {
                margin-left: 0;
            }

            &:nth-child(3) {
                width: 40%;
            }
        }
    }
</style>