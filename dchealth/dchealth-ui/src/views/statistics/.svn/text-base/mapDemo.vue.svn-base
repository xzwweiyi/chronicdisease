<template>
    <!-- 院级管理图 -->
    <div class="wapper_page" v-loading="isLoading">
        <!-- <div class="top_page">
            <div class="top_title">空军军医大学唐都医院健康管理全景图</div>
        </div> -->
        <div class="container_page">
            <!-- <div class="left_container">
                <div class="hospital_info">
                    <com-title title="慢病管理概览"></com-title>
                    <div class="hospital_info_list">
                        <info-item class="hospital_info_item" v-for="(item,index) in infoList" :key="index"
                            :hospital-info="item"></info-item>
                    </div>
                </div>
                <div class="patient_origin">
                    <com-title title="患者来源省区分布"></com-title>
                    <div class="hospital_info_list">
                        <province :list="provinceList" />
                    </div>
                </div>
            </div> -->
            <div class="center_container">
                <!-- <div class="patient_quantity">
                    <p class="patient_quantity_tit">慢病管理患者</p>
                    <span class="patient_quantity_num" v-for="(item,index) in totalPatientNum"
                        :key="index">{{item}}</span>
                </div> -->
                <!-- <div class="map_title">省内慢病患者分布</div> -->
                <div class="map_chart">
                    <my-map v-if="tableList&&tableList.length" :provinceMapData="provinceMapData"></my-map>
                </div>
                <!-- <myTable class="table_list" :tableList="tableList"></myTable> -->
            </div>
            <!-- <div class="right_container">
                <div class="disease_info">
                    <com-title title="科室慢病管理情况"></com-title>
                    <div class="right_chart">
                        <CustomChart class="v_chart" :optionsData="customDepartmentOption" />
                    </div>
                </div>
                <div class="disease_info">
                    <com-title title="慢病病种统计"></com-title>
                    <div class="right_chart">
                        <CustomChart class="v_chart" :optionsData="diseasePieOption" />
                    </div>
                </div>
                <div class="disease_info">
                    <com-title title="性别分布"></com-title>
                    <div class="right_chart">
                        <CustomChart class="v_chart" :optionsData="diseaseBarOption" />
                    </div>
                </div>
            </div> -->
        </div>
    </div>
</template>

<script>
import InfoItem from './components/info'
import ComTitle from './components/title'

import pieOption from '@/views/components/options/pie'
import pictorialBarOption from '@/views/components/options/pictorialBar'
import customOption from '@/views/components/options/custom_chart'
import province from './components/province'
import myTable from './components/table'
import myMap from './components/map'
import CustomChart from "./components/chart"
import { getHospitalView, getPatientDistributeOrgin } from "@/api/statistics/config"

export default {
    name: "hospital-chart",
    components: {
        InfoItem,
        ComTitle,
        province,
        myTable,
        myMap,
        CustomChart
    },
    provide: {},
    data() {
        return {
            infoList: null,
            diseasePieOption: null,
            diseaseBarOption: null,
            customDepartmentOption: null,
            provinceList: [],
            mapOptionData: null,
            intervalTime: null,
            tableList: [],
            isLoading: false,
            totalPatientNum: 0,
            backgroundImg: require('@/assets/statistics/background.jpg'),
            provinceMapData: [],
        }
    },
    created() { },
    beforeDestory() {
        this.intervalTime = null;
        clearInterval(this.intervalTime)
    },
    mounted() {
        this.initData();
    },
    methods: {
        initData() {
            this.getQueryPatientDistributeOrgin();
            // this.getProviceList();
            // this.getTabeList();
            this.getPageData();
        },
        getPageData() {
            this.isLoading = true;
            getHospitalView().then(res => {
                this.totalPatientNum = res.totalPatientNum ? res.totalPatientNum.toString() : 0
                this.getHospitalInfo(res)
                this.setPatientSexOptions(res.patientSex);
                this.setPieOptions(res.patientDisease)
                this.setDepartment(res.patientDept);
                this.isLoading = false;
            }).catch(error => {
                this.isLoading = false;
            })
        },
        getQueryPatientDistributeOrgin() {
            getPatientDistributeOrgin().then(res => {
                let resData = res.data
                // console.log(resData)
                if (resData) {
                    this.provinceMapData = resData.cityVisitVos
                    this.getTabeList(resData.cityVisitVos)
                    this.getProviceList(resData.provinceVos)
                }

            })
        },
        getTabeList(data) {
            this.tableList = [{
                cityName: '地区',
                newPatientCount: '新增患者',
                patientCount: '患者总量'
            }]
            this.tableList = this.tableList.concat(data)
        },
        // 慢病管理橄榄
        getHospitalInfo(resData) {
            let list = [{
                name: '今日随访人数',
                num: resData.todayFollowPatientNum,
            }, {
                name: '今日新增入组人数',
                num: resData.todayEnrollmentPatientNum,
            }, {
                name: '随访科室数',
                num: resData.todayFollowDeptNum,
            }, {
                name: '随访病种数',
                num: resData.todayFollowDiseaseNum,
            }, {
                name: '随访队列数',
                num: resData.todayFollowResearchNum,
            }, {
                name: '累计随访人数',
                num: resData.totalFollowPatientNum,
            }, {
                name: '月活跃患者',
                num: resData.monthlyActivityPatients,
            }, {
                name: '月活跃数',
                num: resData.monthlyActivityNumber,
            }]
            this.infoList = list.map((m, index) => {
                return {
                    ...m,
                    class: 'icon_' + (index + 1)
                }
            })
        },
        // 慢病病种统计
        setPieOptions(resData) {
            let option = pieOption.getOption()
            option.series[0].label.formatter = (params) => {
                let text = '';
                text = `{a|${params.data.name}}\n${params.data.value}例`
                return text
            }
            option.series[0].data = resData.map(m => {
                return {
                    value: m.total,
                    name: m.name,
                }
            })
            option.tooltip.formatter = (params) => {
                return `${params.marker}${params.name}：${params.value}例`
            }
            this.diseasePieOption = option
        },
        // 性别分布
        setPatientSexOptions(resData) {
            let option = pictorialBarOption.getPictorialOption();
            option.series.forEach(item => {
                item.data = resData.map(m => m.total)
            })
            option.xAxis.data = resData.map(m => m.name)
            this.diseaseBarOption = option
        },
        // 科室慢病管理情况
        setDepartment(resData) {
            let option = customOption.getOption()
            option.series.forEach((item) => {
                item.data = resData.map(m => {
                    return {
                        value: m.total ? m.total + 10 : m.total,
                        name: m.name,
                        chartValue: m.total
                    }
                })
            });
            option.xAxis[0].data = resData.map(m => m.name)
            option.xAxis[1].data = resData.map(m => m.total)
            option.xAxis[0].axisLabel.formatter = (params) => {
                let text = params;
                text = `${params.slice(0, 3)}\n${params.slice(3, params.length)}`
                return text
            }
            this.customDepartmentOption = option
        },
        // 
        getProviceList(provinceVos) {
            let countArr = provinceVos.map(m => m.patientCount)
            countArr.sort((a, b) => {
                return a - b
            })
            let maxNum = countArr[0] + 100;
            provinceVos.forEach(item => {
                item['percent'] = Number((item.patientCount / maxNum) * 100)
            })
            this.provinceList = provinceVos
        },
    },
};
</script>
<style lang="scss" scoped>
.wapper_page {
    // background: url("../../assets/statistics/background.jpg") no-repeat;
    // background-size: 100% 100%;
    // color: #fff;
    // height: calc(100vh - 86px);
}

.top_page {
    width: 100%;
    height: 7vh;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.4vw;
    // background: url("../../assets/statistics/top_1.png") no-repeat;
    background-size: 98%;
    background-position: center center;
}

.container_page {
    display: flex;
    opacity: 0.9;
}

.left_container,
.right_container {
    width: 28%;
    height: calc(93vh - 86px);
}

.left_container > div,
.right_container > div {
    width: 92%;
    margin: 0 auto;
    overflow: hidden;
}

.center_container {
    width: 100%;
    height: 90vh;
    display: flex;
    flex-direction: column;
}

.hospital_info {
    height: 36%;
    display: flex;
    flex-direction: column;
}

.hospital_info_list {
    display: flex;
    flex-wrap: wrap;
    flex: 1;
}

.patient_origin {
    height: 63%;
    display: flex;
    flex-direction: column;
}

.hospital_info_item {
    width: 40%;
    height: 25%;
    align-items: center;
    text-align: left;
}

.hospital_info_item:nth-child(2n) {
    width: 56%;
}

.disease_info {
    width: 100%;
    height: 33%;
    display: flex;
    flex-direction: column;
}

.right_chart {
    flex: 1;
}

.patient_quantity {
    color: #face1f;
    display: flex;
    justify-content: center;
    align-items: center;
    .patient_quantity_tit {
        font-size: 2.6vh;
    }
}

.patient_quantity_num {
    border: 1px #face1f solid;
    border-radius: 0.5vh;
    width: 5vh;
    height: 5vh;
    line-height: 4.5vh;
    text-align: center;
    margin-left: 2vh;
    font-size: 4vh;
    font-weight: bold;
}

.map_title {
    text-align: left;
    line-height: 4vh;
    font-size: 1.7vh;
    background: url("../../assets/statistics/title_2.png") no-repeat;
    background-size: 5vh;
    background-position: left center;
    text-indent: 6vh;
}

.map_chart {
    flex: 1;
}

.v_chart {
    height: 100%;
}
</style>