<template>
    <!-- 院级管理图 -->
    <div class="wapper_page" v-loading="isLoading" :class="{ 'fullscreen': isFullscreen }">
        <div class="top_page">
            <div class="top_title">空军军医大学唐都医院健康管理全景图</div>
            <div class="fullscreen_btn" @click="toggleFullscreen">
                <i class="el-icon-full-screen"></i>
            </div>
        </div>
        <div class="container_page">
            <div class="left_container">
                <div class="hospital_info">
                    <com-title title="慢病管理概览"></com-title>
                    <div class="hospital_info_list">
                        <info-item class="hospital_info_item" v-for="(item,index) in infoList" :key="index"
                            :hospital-info="item"></info-item>
                    </div>
                </div>
                <div class="patient_origin">
                    <div class="patient_origin_header">
                        <div class="patient_origin_title_wrap">
                            <com-title :title="activeOriginType === 'inside' ? '省内慢病患者分布' : '省外慢病患者分布'"></com-title>
                        </div>
                        <div class="origin_switch_btns">
                            <span
                                :class="['origin_switch_btn', { active: activeOriginType === 'inside' }]"
                                @click="handleOriginTypeChange('inside')">省内：<span>{{ insidePatientNum }}</span></span>
                            <span
                                :class="['origin_switch_btn', { active: activeOriginType === 'outside' }]"
                                @click="handleOriginTypeChange('outside')">省外：<span>{{ outsidePatientNum }}</span></span>
                        </div>
                    </div>
                    <div class="hospital_info_list hospital_info_list_origin">
                        <province v-if="activeOriginType === 'outside'" :list="provinceList" />
                        <my-map v-if="activeOriginType === 'inside'" :provinceMapData="provinceMapData"></my-map>
                    </div>
                </div>
            </div>
            <div class="center_container">
                <div class="patient_quantity">
                    <p class="patient_quantity_tit">慢病管理患者</p>
                    <!-- <span class="patient_quantity_num" v-for="(item,index) in totalPatientNum"
                        :key="index">{{item}}</span> -->
                </div>
                <!-- <div class="map_title">省内慢病患者分布</div> -->
                <div class="map_chart">
                    <div class="center_ANIMATIONS">
                        <div class="center_ANIMATIONS1">
                            <img src="@/assets/largeScreen/center3.png" alt="">
                        </div>
                        <div class="center_ANIMATIONS2">
                            <img src="@/assets/largeScreen/center1.png" alt="">
                        </div>
                        <div class="center_data">
                            <div class="center_data_all">{{ totalPatientNum }}</div>
                            <div :class="['center_data_item1', `center_data_item1_${index + 1}`]" v-for="(item, index) in patientDiseaseList" :key="index">
                                {{ item.name }}：{{ item.value }}例
                            </div>
                        </div>
                    </div>
                </div>
                <div class="departmentDiseaseManage">
                  <com-title title="科室慢病管理情况"></com-title>
                  <div class="departmentDiseaseManageChart">
                    <CustomChart class="v_chart" v-if="customDepartmentOption" :optionsData="customDepartmentOption" />
                    <!-- <threedimensionalBar class="v_chart"></threedimensionalBar> -->
                  </div>
                </div>
            </div>
            <div class="right_container">
                <div class="disease_info1">
                    <com-title title="慢病平台使用趋势图"></com-title>
                    <div class="right_chart">
                        <lineChart class="v_chart" :optionsData="trendChartData" />
                    </div>
                </div>
                <div class="disease_info2">
                    <com-title title="年龄分布"></com-title>
                    <div class="right_chart">
                        <agePie class="v_chart" :optionsData="patientAgeDistribution"></agePie>
                    </div>
                </div>
                <div class="disease_info3">
                    <div class="disease_info3_left">
                        <com-title title="性别分布"></com-title>
                        <div class="right_chart">
                            <sexChart v-if="patientSex" :optionsData="patientSex" class="v_chart"/>
                        </div>
                    </div>
                    <div class="disease_info3_right">
                        <com-title title="随访与失访比率"></com-title>
                        <div class="right_chart">
                            <threedimensionalPie class="v_chart" :optionsData="followAndLostRateData"/>
                        </div>

                    </div>
                    
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import InfoItem from './components/info'
import ComTitle from './components/Newtitle'
import screenfull from 'screenfull'

import pieOption from '@/views/components/options/pie'
// import customOption from '@/views/components/options/custom_chart'
// import customOption from '@/views/components/options/block_custom_chart'
import province from './components/province'
import myTable from './components/table'
import myMap from './components/map'
import CustomChart from "./components/chartTest"
import agePie from "./components/agePie"
import threedimensionalPie from "./components/threedimensionalPie"
import sexChart from "./components/sexChart"
import threedimensionalBar from "./components/threedimensionalBar"
import lineChart from "./components/lineChart"
import {
    getHospitalView,
    getPatientDistributeOrgin,
    getTrendChart,
    getAgeDistribution,
    getFollowAndLostRate
} from "@/api/statistics/config"

export default {
    name: "hospital-chart",
    components: {
        InfoItem,
        ComTitle,
        province,
        myTable,
        myMap,
        CustomChart,
        agePie,
        threedimensionalPie,
        sexChart,
        threedimensionalBar,
        lineChart
    },
    provide: {},
    data() {
        return {
            infoList: null,
            customDepartmentOption: null,
            trendChartData: null,
            provinceList: [],
            intervalTime: null,
            isLoading: false,
            totalPatientNum: 0,
            backgroundImg: require('@/assets/statistics/background.jpg'),
            provinceMapData: [],
            originDataMap: {
                inside: [],
                outside: []
            },
            insidePatientNum: 0,
            outsidePatientNum: 0,
            activeOriginType: 'inside',
            patientDiseaseList: [],
            allPatientDiseaseList: [],
            patientDiseaseStartIndex: 0,
            patientSex: null,
            patientAgeDistribution: null,
            followAndLostRateData: null,
            isFullscreen: false
        }
    },
    created() { },
    beforeDestroy() {
        document.removeEventListener('visibilitychange', this.handleVisibilityChange)
        this.clearPatientDiseaseLoop()
    },
    mounted() {
        document.addEventListener('visibilitychange', this.handleVisibilityChange)
        this.initData();
    },
    methods: {
        clearPatientDiseaseLoop() {
            if (this.intervalTime) {
                clearInterval(this.intervalTime)
                this.intervalTime = null
            }
        },
        handleVisibilityChange() {
            if (document.hidden) {
                this.clearPatientDiseaseLoop()
                return
            }
            if (this.allPatientDiseaseList && this.allPatientDiseaseList.length) {
                this.startPatientDiseaseLoop()
            }
        },
        initData() {
            this.activeOriginType = 'inside'
            this.provinceMapData = []
            // 省内省外患者分布
            this.getQueryPatientDistributeOrgin();
            // 页面初始化数据请求
            this.getPageData();
            // 慢病平台使用趋势图
            this.setTrendChart()

            // 年龄分布
            this.ageDistribution()

            // 随访和失访比率
            this.followAndLostRate()
        },
        getPageData() {
            this.isLoading = true;
            getHospitalView().then(res => {
                this.totalPatientNum = res.totalPatientNum ? res.totalPatientNum.toString() : 0
                this.getHospitalInfo(res)
                this.patientSex = res.patientSex;
                // this.setPieOptions(res.patientDisease)

                // 慢病病种较多时，单页展示会比较拥挤，可以分批展示，下面是分批展示的实现方式
                this.setPatientDisease(res.patientDisease)
                // 科室慢病管理情况
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
                    const insideList = resData.cityVisitVos || []
                    const outsideList = resData.provinceVos || []
                    this.insidePatientNum = resData.inProvinceCount
                    this.outsidePatientNum = resData.outProvinceCount
                    this.originDataMap = {
                        inside: insideList,
                        outside: outsideList
                    }
                    this.handleOriginTypeChange('inside')
                    
                }

            })
        },
        handleOriginTypeChange(type) {
            this.activeOriginType = type
            if (type === 'inside') {
                this.provinceMapData = this.originDataMap.inside
            } else {
                this.getProviceList(this.originDataMap.outside)
            }
            
        },
        // 慢病管理概览
        getHospitalInfo(resData) {
            let list = [{
                name: '今日随访人数',
                num: resData.todayFollowPatientNum,
            }, {
                name: '今日新增入组人数',
                num: resData.todayEnrollmentPatientNum,
            }, {
                name: '随访科室/病区数',
                num: `${resData.todayFollowDeptNum}/${resData.todayFollowWardNum}`,
                type: 'dept'
            }, {
                name: '随访病种数',
                num: resData.todayFollowDiseaseNum,
            }, {
                name: '随访队列数',
                num: resData.todayFollowResearchNum,
            }, {
                name: '累计随访人次',
                num: resData.totalFollowPatientNum,
            }, {
                name: '月活跃患者',
                num: resData.monthlyActivityPatients,
            }, {
                name: '月活跃数',
                num: resData.monthlyActivityNumber,
                },
                {
                name: '医生端用户数',
                num: resData.doctorUserNum,
                },
                {
                name: '患者端用户数',
                num: resData.patientUserNum,
                }
            ]
            this.infoList = list.map((m, index) => {
                return {
                    ...m,
                    class: 'icon_' + (index + 1)
                }
            })
        },
        setPatientDisease(resData) {
            const sortedDiseaseList = (resData || [])
                .slice()
                .sort((a, b) => (Number(b.total) || 0) - (Number(a.total) || 0))
            this.allPatientDiseaseList = sortedDiseaseList.map(m => {
                return {
                    value: m.total,
                    name: m.name,
                }
            })
            this.patientDiseaseStartIndex = 0
            this.startPatientDiseaseLoop()
        },
        startPatientDiseaseLoop() {
            this.clearPatientDiseaseLoop()
            if (!this.allPatientDiseaseList || this.allPatientDiseaseList.length === 0) {
                this.patientDiseaseList = []
                return
            }
            this.updatePatientDiseaseWindow()
            this.intervalTime = setInterval(() => {
                if (this._isDestroyed || this._isBeingDestroyed) {
                    this.clearPatientDiseaseLoop()
                    return
                }
                this.patientDiseaseStartIndex += 7
                if (this.patientDiseaseStartIndex >= this.allPatientDiseaseList.length) {
                    this.patientDiseaseStartIndex = 0
                }
                this.updatePatientDiseaseWindow()
            }, 5000)
        },
        updatePatientDiseaseWindow() {
            const source = this.allPatientDiseaseList || []
            if (!source.length) {
                this.patientDiseaseList = []
                return
            }
            const pageSize = 7
            const start = this.patientDiseaseStartIndex
            let current = source.slice(start, start + pageSize)
            if (current.length < pageSize) {
                current = current.concat(source.slice(0, pageSize - current.length))
            }
            this.patientDiseaseList = current
        },
        setDepartment(resData) {
            this.customDepartmentOption = resData
        },
        // 
        getProviceList(provinceVos) {
            if (!Array.isArray(provinceVos)) {
                this.provinceList = [];
                return;
            }
            // 按 patientCount 从大到小排序
            provinceVos.sort((a, b) => (Number(b.patientCount) || 0) - (Number(a.patientCount) || 0));
            const maxNum = (Number(provinceVos[0] && provinceVos[0].patientCount) || 0) + 100;
            provinceVos.forEach(item => {
                item['percent'] = Number(((Number(item.patientCount) || 0) / maxNum) * 100);
            });
            this.provinceList = provinceVos;
        },
        setTrendChart() {
            getTrendChart().then(res => {
                this.trendChartData = res.data
            })
        },
        // 年龄分布
        ageDistribution() {
            getAgeDistribution().then(res => {
                this.$nextTick(() => {
                    this.patientAgeDistribution = res.data
                })
            })
        },
        followAndLostRate() {
            getFollowAndLostRate().then(res => {
                this.$nextTick(() => {
                    this.followAndLostRateData = res.data
                })
            })
        },
        toggleFullscreen() {
            console.log('toggleFullscreen called')
            if (screenfull.isEnabled) {
                console.log('Screenfull is enabled')
                try {
                    screenfull.toggle(document.querySelector('.wapper_page'))
                    this.isFullscreen = !this.isFullscreen
                    console.log('Fullscreen toggled, isFullscreen:', this.isFullscreen)
                } catch (err) {
                    console.error('Error toggling fullscreen:', err)
                }
            } else {
                console.error('Screenfull is not enabled')
            }
        }
    },
};
</script>
<style lang="scss" scoped>
.wapper_page {
    background: url("../../assets/largeScreen/bg.png") no-repeat;
    background-size: 100% 100%;
    color: #fff;
    height: calc(100vh - 86px);
    position: relative;
    &.fullscreen {
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        z-index: 9999;
    }
}

.top_page {
    width: 100%;
    height: 7vh;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.3vw;
    background: url("../../assets/largeScreen/headright.png") no-repeat;
    background-size: 100%;
    background-position: center center;
    position: relative;
    .top_title {
      margin-right: 30px;
    }
    .fullscreen_btn {
        position: absolute;
        right: 20px;
        top: 50%;
        transform: translateY(-50%);
        cursor: pointer;
        font-size: 20px;
        color: #fff;
        padding: 5px;
        z-index: 10;
        &:hover {
            color: #fee66e;
        }
    }
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
    width: 44%;
    height: calc(93vh - 86px);
    display: flex;
    flex-direction: column;
}

.hospital_info {
    height: 39%;
    display: flex;
    flex-direction: column;
    background: url("../../assets/largeScreen/item1.png") no-repeat;
    background-size: 100%  100%;
    padding-bottom: 1%;
}

.hospital_info_list {
    display: flex;
    flex-wrap: wrap;
    flex: 1;
    padding: 0 5%;
    padding-top: 3%;
}
.hospital_info_list_origin {
    overflow: auto;
    -ms-overflow-style: none;
    scrollbar-width: none;

    &::-webkit-scrollbar {
        width: 0;
        height: 0;
        display: none;
    }
}

.left_container .patient_origin {
    height: 56%;
    margin-top: 4% !important;
    margin-bottom: 2% !important;
    display: flex;
    flex-direction: column;
    background: url("../../assets/largeScreen/item2.png") no-repeat;
    background-size: 100%  100%;
}

.patient_origin_header {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 12px;
}

.patient_origin_title_wrap {
    width: 100%;
}

.origin_switch_btns {
    position: absolute;
    right: 12px;
    top: calc(50% + 12px);
    transform: translateY(-50%);
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 4px;
}

.origin_switch_btn {
    font-size: 10px;
    line-height: 1.2;
    white-space: nowrap;
    color: #8fb3e6;
    border: 1px solid rgba(143, 179, 230, 0.45);
    border-radius: 10px;
    padding: 2px 6px;
    cursor: pointer;
    user-select: none;
}

.origin_switch_btn.active {
    color: #fee66e;
    border-color: rgba(254, 230, 110, 0.9);
    box-shadow: 0 0 8px rgba(254, 230, 110, 0.35);
}

.hospital_info_item {
    width: 50%;
    height: 20%;
    align-items: center;
    text-align: left;
}

.hospital_info_item:nth-child(2n) {
    width: 50%;
}

.disease_info1, .disease_info2, .disease_info3 {
    width: 100%;
    height: 31%;
    display: flex;
    flex-direction: column;
}
.disease_info1 {
    background: url("../../assets/largeScreen/item4.png") no-repeat;
    background-size: 100%  100%;
    margin-bottom: 3% !important;
}
.disease_info2 {
    background: url("../../assets/largeScreen/item5.png") no-repeat;
    background-size: 100%  100%;
    margin-bottom: 3% !important;
}
.disease_info3 {
    background: url("../../assets/largeScreen/item6.png") no-repeat;
    background-size: 100%  100%;
    margin-bottom: 2% !important;
     height: 32%;
     display: flex;
     flex-direction: row;
    .disease_info3_left {
        width: 50%;
    }
    .disease_info3_right {
        flex: 1;
    }
}

.right_chart {
    flex: 1;
    width: 100%;
    height: calc(100% - 35px);
}

.patient_quantity {
    // color: #face1f;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 2.5vh;
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
@keyframes innerLoading {
  /*以百分比来规定改变发生的时间 也可以通过"from"和"to",等价于0% 和 100%*/
  from {
    /*rotate(2D旋转) scale(放大或者缩小) translate(移动) skew(翻转)*/
    transform: rotateY(0deg);
  }
  to {
    transform: rotateY(360deg);
  }
}

@keyframes outerLoading {
  /*以百分比来规定改变发生的时间 也可以通过"from"和"to",等价于0% 和 100%*/
  from {
    /*rotate(2D旋转) scale(放大或者缩小) translate(移动) skew(翻转)*/
    transform: rotateY(360deg);
  }
  to {
    transform: rotateY(0deg);
  }
}
.map_chart {
    // flex: 1;
    height: 47%;
    margin-bottom: 0.5%;
    .center_ANIMATIONS {
        width: 100%;
        height: 88%;
        position: relative;
        background: url("../../assets/largeScreen/center2.png") no-repeat;
        background-size: 100%  100%;
        .center_ANIMATIONS1 {
            width: 100%;
            height: 100%;
            position: absolute;
            // perspective: 1000px;
            img {
                width: 100%;
                height: 100%;
                // animation: innerLoading 5s linear infinite;
                // transform-style: preserve-3d;
                // transform-origin: center center;
                // backface-visibility: hidden;
                // will-change: transform;
            }
        }
        .center_ANIMATIONS2 {
            width: 65%;
            height: 45%;
            position: absolute;
            bottom: -10%;
            left: 17%;
            img {
                width: 100%;
                height: 100%;
                // animation: outerLoading 5s linear infinite;
            }
        }
        .center_data {
            width: 100%;
            height: 100%;
            position: relative;
            display: flex;
            justify-content: center;
            // align-items: center;
            .center_data_all {
                position: absolute;
                // left: 32%;
                top: 42%;
                font-size: 7.7vh;
                font-family: D-DIN-PRO, D-DIN-PRO-SemiBold;
                font-weight: 600;
                text-align: center;
                color: #fee66e;
                text-shadow: 1px 1.73px 3.92px rgba(195,143,19,0.83);
            }
            .center_data_item1 {
                position: absolute;
                font-size: 1.6vh;
                font-family: Source Han Sans CN, Source Han Sans CN-Regular;
                font-weight: 400;
                text-align: center;
                color: #fee66e;
            }
            .center_data_item1_1 { 
                left: 1%;
                top: 77%; 
            }
            .center_data_item1_2 { 
                left: 6%;
                top: 40%;
            }
            .center_data_item1_3 { 
                left: 39%;
                top: 6%;
            }
            .center_data_item1_4 { 
                left: 73%;
                top: 14%; 
            }
            .center_data_item1_5 { 
                left: 82%;
                top: 48.5%;
            }
            .center_data_item1_6 { 
                left: 80%;
                top: 83%;
            }
            .center_data_item1_7 { 
                left: 39.5%;
                top: 87%;
             }
        }
    }
}
.departmentDiseaseManage {
  height: 42%;
  background: url("../../assets/largeScreen/item3.png") no-repeat;
  background-size: 100%  100%;
  display: flex;
  flex-direction: column;
  .departmentDiseaseManageChart {
    flex: 1;
  }
}
.v_chart {
    height: 100%;
}
</style>