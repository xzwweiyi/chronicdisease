<template>
    <div class="app-container" v-loading="loading">
        <div class="m">
            <el-form ref="elForm" :model="formData" label-width="170px">
                <div class="ibox">
                    <div class="ibox-title">基础信息</div>
                    <el-col :span="12">
                        <el-form-item label="患者姓名：" prop="patientName">
                            <el-input v-model="formData.patientInfo.patientName" readonly disabled
                                :style="{ width: '90%' }"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="患者ID：" prop="patientCode">
                            <el-input v-model="formData.patientInfo.patientCode" readonly disabled
                                :style="{ width: '90%' }"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="身份证号：" prop="idCard">
                            <el-input v-model="formData.patientInfo.idCard" readonly disabled :style="{ width: '90%' }">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="创建时间：" prop="createTime">
                            <el-date-picker v-model="formData.patientInfo.createTime" format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd" :style="{ width: '90%' }" placeholder="创建时间：" disabled
                                readonly></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="性别：" prop="sex">
                            <el-radio-group v-model="formData.patientInfo.sex" :disabled="true">
                                <el-radio-group v-model="formData.patientInfo.sex">
                                    <el-radio v-for="(item, index) in sexData" :key="index" :label="item.value"
                                        disabled>{{ item.label }}</el-radio>
                                </el-radio-group>
                                <!-- <el-radio-button v-for="(item, index) in sexData"
                                    :key="index"
                                    :label="item.value"
                                    :disabled="item.disabled">{{ item.label }}</el-radio-button> -->
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="出生日期：" prop="birthday">
                            <el-date-picker v-model="formData.patientInfo.birthday" format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd" :style="{ width: '90%' }" placeholder="请选择出生日期：" disabled
                                readonly></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <div class="ibox-title">基础信息图片</div>
                    <el-form-item label="基础信息：">
                        <!-- :src="formData.patientInfo.basicInformationImages" -->
                        <el-image style="width: 100px; height: 100px"
                            src="https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg"
                            :preview-src-list="srcList">
                        </el-image>
                    </el-form-item>
                    <div class="ibox-title">疾病诊断</div>
                    <el-form-item label="肿瘤原发部位：">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.primarySiteOfTumor" :disabled="true"
                            clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.primarySiteOfTumors"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.primarySiteOfTumorOther"
                            :disabled="true" clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.primarySiteOfTumors"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="肿瘤转移部位：">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.tumorMetastasisSite" :disabled="true"
                            clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.tumorMetastasisSites"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="T：">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.tumorMetastasisSite" :disabled="true"
                            clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.tumorMetastasisSites"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="N：">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.tumorMetastasisSite" :disabled="true"
                            clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.tumorMetastasisSites"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="M：">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.tumorMetastasisSite" :disabled="true"
                            clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.tumorMetastasisSites"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <!-- <el-form-item label="临床分期："
                        prop="clinicalStage">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.clinicalStage"
                            disabled
                            :style="{ width: '40%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.clinicalStages"
                                :key="index"
                                :label="item.codeLabel"
                                :value="item.codeType"
                                :disabled="item.disabled"></el-option>
                        </el-select>
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.clinicalStageTwo"
                            :disabled="true"
                            clearable
                            :style="{ width: '40%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.clinicalStageTwos"
                                :key="index"
                                :label="item.codeLabel"
                                :value="item.codeType"
                                :disabled="item.disabled"></el-option>
                        </el-select>
                    </el-form-item> -->
                    <el-form-item label="基因状态：" prop="geneStatus">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.geneStatus" :disabled="true" clearable
                            :style="{ width: '40%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.geneStatuss"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="" prop="geneStatus">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.geneStatusTwo" :disabled="true"
                            clearable :style="{ width: '40%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.geneStatusTwos"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="伴随疾病：" prop="concomitantDisease">
                        <el-select v-model="formData.patientRecordsBasicInfoEdc.concomitantDisease" :disabled="true"
                            clearable :style="{ width: '60%' }">
                            <el-option v-for="(item, index) in formData.patientRecordsBasicInfoEdc.concomitantDiseases"
                                :key="index" :label="item.codeLabel" :value="item.codeType" :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <div class="ibox-title">疾病诊断图片</div>
                    <el-form-item label="基础信息：">
                        <el-image style="width: 100px; height: 100px" :src="formData.patientInfo.basicInformationImages"
                            :preview-src-list="srcList">
                        </el-image>
                    </el-form-item>
                    <div class="ibox-title">既往治疗</div>
                    <el-col :span="12">
                        <el-form-item label="既往是否进行手术：" prop="priorSurgery">
                            <el-radio-group v-model="formData.patientRecordsBasicInfoEdc.priorSurgery" :disabled="true">
                                <el-radio v-for="(item, index) in priorSurgeryData" :key="index" :label="item.value"
                                    disabled>{{ item.label }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="手术日期：" prop="createTime"
                            v-if="formData.patientRecordsBasicInfoEdc.priorSurgery == 1">
                            <el-date-picker v-model="formData.patientInfo.createTime" format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd" :style="{ width: '90%' }" clearable readonly></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="既往是否进行过放疗：" prop="field111">
                            <el-radio-group v-model="formData.field111" :disabled="true">
                                <el-radio v-for="(item, index) in priorSurgeryData" :key="index" :label="item.value"
                                    disabled>{{ item.label }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="既往是否进行过化疗：" prop="field111">
                            <el-radio-group v-model="formData.field111" :disabled="true">
                                <el-radio v-for="(item, index) in priorSurgeryData" :key="index" :label="item.value"
                                    disabled>{{ item.label }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <div class="ibox-title">既往治疗图片</div>
                    <el-form-item label="既往治疗图片：">
                        <!-- <el-image style="width: 100px; height: 100px"
                            :src="formData.patientInfo.previousTreatmentImages"
                            :preview-src-list="srcList">
                        </el-image> -->
                    </el-form-item>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import Cookies from 'js-cookie'
import { mapGetters } from "vuex";
import { getUserInfo } from "@/api/research/info";

export default {
    name: "userinfo",
    data() {
        return {
            basicInformationImages: 'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
            loading: false,
            formData: {
                patientInfo: {
                    patientName: "",
                },
                patientRecordsBasicInfoEdc: {}
            },
            sexData: [
                { label: "男", value: "1", },
                { label: "女", value: "2", },
            ],
            priorSurgeryData: [
                { label: "是", value: "1", },
                { label: "否", value: "2", },
            ],
            srcList: [
                "https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
                "https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg",
            ],
            field102Options: [
                { label: "选项一", value: 1, },
                { label: "选项二", value: 2, },
            ],

        };
    },
    created() {
        this.getList();
    },
    computed: {
        ...mapGetters(["medicalTreeInfo"]),
        userQueryInfo() {
            let userQueryInfo = this.medicalTreeInfo.secondMenus ? this.medicalTreeInfo : JSON.parse(Cookies.get(
                'medicalTreeInfo'))
            return userQueryInfo.secondMenus
        }
    },
    watch: {
        // "medicalTreeInfo.thirdMenus"(newVal, oldVal) {
        //     if (oldVal && newVal != oldVal && this.$route.path == '/medical/userinfo') {
        //         this.getList()
        //     }
        // }
    },
    methods: {
        /** 获取基础信息 */
        getList() {
            // console.log(this.medicalTreeInfo)
            const ids = this.$route.query.patientId;
            this.loading = true;
            getUserInfo(ids).then((response) => {
                let resData = response.data;
                this.formData = resData;
                console.log("info", this.formData.patientInfo);
                console.log("info", this.formData.patientRecordsBasicInfoEdc);
                this.loading = false;
            }).catch(error => {
                this.loading = false;
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.app-container {
    .m {
        margin: 15px;

        .ibox {
            margin-bottom: 25px;
            background-color: #fff;
            //   border: 1px solid #ddd;
            border-radius: 4px;
            height: 100%;
            //   -webkit-box-shadow: 0 1px 1px rgb(0 0 0 / 5%);
            margin: 0 15px;

            .ibox-title {
                width: 100%;
                overflow: hidden;
                background-color: #eee;
                border-color: #e7eaec;
                font-size: 16px;
                font-weight: bold;
                -webkit-border-image: none;
                -o-border-image: none;
                border-image: none;
                border-style: solid solid none;
                border-width: 0px 0px 0;
                color: inherit;
                margin-bottom: 20px;
                padding: 14px 15px 14px;
                min-height: 40px;
            }
        }
    }
}
</style>