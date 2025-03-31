<template>
    <div class="app-container" v-loading="isPageLoading">
        <div class="ibox">
            <el-row :gutter="20">
                <el-form :model="userData" ref="userData" :inline="true">
                    <el-col :span="6" :sm="12" :xs="12">
                        <el-form-item label="患者姓名：" prop="patientName"> {{ userData.patientName }} </el-form-item>
                    </el-col>
                    <el-col :span="6" :sm="12" :xs="12">
                        <el-form-item label="性别：" prop="sex"> {{ userData.sex === "1" ? "男" : "女" }}</el-form-item>
                    </el-col>
                    <el-col :span="6" :sm="12" :xs="12">
                        <el-form-item label="患者ID：" prop="patientCode"> {{ userData.patientCode }}</el-form-item>
                    </el-col>
                    <el-col :span="6" :sm="12" :xs="12">
                        <el-form-item label="创建时间：" prop="createTime"> {{ userData.createTime | dateFormat }}
                        </el-form-item>
                    </el-col>
                </el-form>
            </el-row>
            <el-row :gutter="20" class="iboxCen">
                <el-col :span="12" :xs="24" class="leftCard">
                    <el-scrollbar>
                        <el-card class="box-card ">
                            <vue-image-editor v-show="isShowEditImage" @closeeditimg="closeImageEditor"
                                :img-url="basicInformationImages"></vue-image-editor>
                            <div slot="header" class="clearfix">
                                <span>{{leftData.patientRecords.name}}</span>
                            </div>
                            <el-form :model="leftData" ref="leftData" :inline="false" :label-position="labelPosition">
                                <el-form-item label="基础信息：">
                                    <el-image class="basicImage" style="width: 100px; height: 100px"
                                        :src="basicInformationImages" @click="showEditImage">
                                    </el-image>
                                </el-form-item>
                                <el-form-item label="是否延期治疗：" prop="isDelayTreatment">
                                    <el-radio-group v-model="leftData.patientRecordsEdc.isDelayTreatment"
                                        :disabled="true">
                                        <el-radio v-for="(item, index) in sexData" :key="index" :label="item.value"
                                            disabled>
                                            {{ item.label }}</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="治疗性质：" prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.therapeuticNature" :disabled="true"
                                        clearable :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in leftData.patientRecordsEdc.therapeuticNatures"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="化疗方案：" prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.chemotherapyRegimen" :disabled="true"
                                        clearable :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in leftData.patientRecordsEdc.chemotherapyRegimens"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                    <el-select v-model="leftData.patientRecordsEdc.chemotherapyRegimenTwo"
                                        :disabled="true" clearable :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="贝伐珠单抗处方时间：" prop="bevacizumabPrescriptionTime">
                                    <el-date-picker v-model="leftData.patientRecordsEdc.bevacizumabPrescriptionTime"
                                        format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
                                        placeholder="创建时间：" clearable readonly></el-date-picker>
                                </el-form-item>
                                <el-form-item label="贝伐珠单抗处方剂量：" prop="bevacizumabPrescriptionDose">
                                    <el-select v-model="leftData.patientRecordsEdc.bevacizumabPrescriptionDose"
                                        :disabled="true" clearable :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in leftData.patientRecordsEdc.bevacizumabPrescriptionDoses"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="疗效评价：" prop="efficacyEvaluation">
                                    <el-select v-model="leftData.patientRecordsEdc.efficacyEvaluation" :disabled="true"
                                        clearable :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in leftData.patientRecordsEdc.efficacyEvaluations"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <!-- <el-form-item label="常见不良反应评价-高血压："
                                    prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.concomitantDisease"
                                        :disabled="true"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="蛋白尿："
                                    prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.concomitantDisease"
                                        :disabled="true"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="动静脉血栓栓塞："
                                    prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.concomitantDisease"
                                        :disabled="true"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="出血："
                                    prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.concomitantDisease"
                                        :disabled="true"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="胃肠道穿孔："
                                    prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.concomitantDisease"
                                        :disabled="true"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="输注相关过敏反应："
                                    prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.concomitantDisease"
                                        :disabled="true"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in leftData.patientRecordsEdc.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <el-form-item label="是否继续治疗：" prop="isContinueTreatment">
                                    <el-radio-group v-model="leftData.patientRecordsEdc.isContinueTreatment"
                                        :disabled="true">
                                        <el-radio v-for="(item, index) in sexData" :key="index" :label="item.value"
                                            disabled>
                                            {{ item.label }}</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="常见不良反应评价" prop="userName">
                                    <el-select v-model="leftData.patientRecordsEdc.adverseReactionEvaluation" clearable
                                        :disabled="true" :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in leftData.patientRecordsEdc.adverseReactionEvaluations"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </el-card>
                    </el-scrollbar>
                </el-col>
                <el-col :span="12" :xs="24">
                    <el-scrollbar>
                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <span>{{rightData.patientRecords.name}}</span>
                            </div>
                            <el-form :model="rightData" ref="rightData" :inline="false" :label-position="labelPosition">
                                <el-form-item label="是否延期治疗：" prop="isDelayTreatment">
                                    <el-radio-group v-model="rightData.patientRecordsEdc.isDelayTreatment">
                                        <el-radio v-for="(item, index) in sexData" :key="index" :label="item.value">
                                            {{ item.label }}</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="治疗性质：" prop="userName">
                                    <el-select v-model="rightData.patientRecordsEdc.therapeuticNature" clearable
                                        :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in rightData.patientRecordsEdc.therapeuticNatures"
                                            :key="index" :label="item.codeLabel" :value="item.codeType"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="化疗方案：" prop="userName">
                                    <el-select v-model="rightData.patientRecordsEdc.chemotherapyRegimen" clearable
                                        :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in rightData.patientRecordsEdc.chemotherapyRegimens"
                                            :key="index" :label="item.codeLabel" :value="item.codeType"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                    <el-select v-model="rightData.patientRecordsEdc.chemotherapyRegimenTwo" clearable
                                        :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in rightData.patientRecordsEdc.chemotherapyRegimensTwo"
                                            :key="index" :label="item.codeLabel" :value="item.codeName"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="贝伐珠单抗处方时间：" prop="bevacizumabPrescriptionTime">
                                    <el-date-picker v-model="rightData.patientRecordsEdc.bevacizumabPrescriptionTime"
                                        format="yyyy-MM-dd" value-format="yyyy-MM-dd" :style="{ width: '100%' }"
                                        placeholder="创建时间：" clearable></el-date-picker>
                                </el-form-item>
                                <el-form-item label="贝伐珠单抗处方剂量：" prop="bevacizumabPrescriptionDose">
                                    <el-select v-model="rightData.patientRecordsEdc.bevacizumabPrescriptionDose"
                                        clearable :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in rightData.patientRecordsEdc.bevacizumabPrescriptionDoses"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="疗效评价：" prop="efficacyEvaluation">
                                    <el-select v-model="rightData.patientRecordsEdc.efficacyEvaluation" clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.efficacyEvaluations" :key="index"
                                            :label="item.label" :value="item.value" :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <!-- <el-form-item label="常见不良反应评价-高血压："
                                    prop="userName">
                                    <el-select v-model="rightData.adverseReactionEvaluation"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.adverseReactionEvaluations"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="蛋白尿："
                                    prop="userName">
                                    <el-select v-model="rightData.concomitantDisease"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="动静脉血栓栓塞："
                                    prop="userName">
                                    <el-select v-model="rightData.concomitantDisease"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="出血："
                                    prop="userName">
                                    <el-select v-model="rightData.concomitantDisease"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="胃肠道穿孔："
                                    prop="userName">
                                    <el-select v-model="rightData.concomitantDisease"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <!-- <el-form-item label="输注相关过敏反应："
                                    prop="userName">
                                    <el-select v-model="rightData.concomitantDisease"
                                        clearable
                                        :style="{ width: '100%' }">
                                        <el-option v-for="(item, index) in rightData.concomitantDiseases"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item> -->
                                <el-form-item label="是否继续治疗：" prop="isContinueTreatment">
                                    <el-radio-group v-model="rightData.patientRecordsEdc.isContinueTreatment">
                                        <el-radio v-for="(item, index) in sexData" :key="index" :label="item.value">
                                            {{ item.label }}</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                                <el-form-item label="常见不良反应评价" prop="userName">
                                    <el-select v-model="rightData.patientRecordsEdc.adverseReactionEvaluation" clearable
                                        :style="{ width: '100%' }">
                                        <el-option
                                            v-for="(item, index) in rightData.patientRecordsEdc.adverseReactionEvaluations"
                                            :key="index" :label="item.label" :value="item.value"
                                            :disabled="item.disabled">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </el-card>
                    </el-scrollbar>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import Cookies from 'js-cookie'
import { mapGetters } from "vuex";
import { getUserInfoUp, getUserInfoLeft, getUserInfoRight, } from "@/api/research/info";
export default {
    name: "medical-detail",
    data() {
        return {
            basicInformationImages: 'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
            isShowEditImage: false,
            isTopLoading: false,
            isLeftLoading: false,
            isRightLoading: false,
            labelPosition: "top",
            // 遮罩层
            loading: true,
            userData: {},
            leftData: {
                patientRecords: {},
                patientRecordsEdc: {},
            },
            rightData: {
                patientRecords: {},
                patientRecordsBasicInfoEdc: {},
                patientRecordsEdc: {},
            },
            sexData: [
                { label: '是', value: '1' },
                { label: '否', value: '0' }
            ],
            form: {},
            // 表单校验
            rules: {
                userName: [
                    { required: true, message: "用户名称不能为空", trigger: "blur", },
                    { min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur", },
                ],
                nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur", }],
                password: [
                    { required: true, message: "用户密码不能为空", trigger: "blur", },
                    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur", },
                ],
                email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"], }],
                phonenumber: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur", }],
            },
        };
    },
    computed: {
        ...mapGetters(["medicalTreeInfo"]),
        userQueryInfo() {
            let userQueryInfo = this.medicalTreeInfo.thirdMenus ? this.medicalTreeInfo.thirdMenus : JSON.parse(
                Cookies.get('medicalTreeInfo.thirdMenus'))
            return userQueryInfo
        },
        isPageLoading() {
            return this.isTopLoading && this.isLeftLoading && this.isRightLoading
        }
    },
    watch: {
        'medicalTreeInfo.thirdMenus'(newVal, oldVal) {
            this.$nextTick(() => {
                if (oldVal && newVal != oldVal && this.$route.path == '/medical/cycle') {
                    this.getUserInfo();
                    this.getLeft();
                    this.getRight();
                }
            })
        }
    },
    created() {
        if (this.$route.query.businessid) {
            this.getUserInfo();
            this.getLeft();
            this.getRight();
        }
    },
    methods: {
        // 关闭图片编辑
        closeImageEditor(event) {
            this.isShowEditImage = false;
        },
        showEditImage() {
            this.isShowEditImage = true;
        },
        /** 查询用户信息 */
        getUserInfo() {
            const ids = this.$route.query.businessid;
            this.isTopLoading = true;
            getUserInfoUp(ids).then((response) => {
                this.userData = response.data.patientRecords
                this.isTopLoading = false;
            }).catch(eroor => {
                this.isTopLoading = false;
            })
        },
        /** 查询用户周期信息 */
        getLeft() {
            const ids = this.$route.query.businessid;
            this.isLeftLoading = true;
            getUserInfoLeft(ids).then((response) => {
                this.leftData = response.data
                this.isLeftLoading = false;
            }).catch(eroor => {
                this.isLeftLoading = false;
            })
        },
        /** 查询用户周期信息 */
        getRight() {
            const ids = this.$route.query.businessid;
            this.isRightLoading = true;
            getUserInfoRight(ids).then((response) => {
                this.rightData = response.data
                this.isRightLoading = false;
            }).catch(eroor => {
                this.isRightLoading = false;
            })
        },
    },
};
</script>

<style lang="scss" scoped>
.iboxCen {
    display: block;
    padding: 15px;
    border: 1px solid #ddd;
    -webkit-box-shadow: 0 1px 1px rgb(0 0 0 / 5%);
    height: 100%;
}

.el-scrollbar {
    height: calc(100vh - 210px);

    &:deep .el-scrollbar__wrap {
        overflow-x: hidden !important;
    }
}

.basicImage {
    width: 100px;
    height: 100px;
    cursor: pointer;
}

.leftCard {
    position: relative;
}

.tui-image-editor-header {
    display: none;
}
</style>