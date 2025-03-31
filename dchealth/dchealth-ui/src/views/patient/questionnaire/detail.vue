<template>
    <div class="details_content" v-loading="isLoadingPatientInfo">
        <el-scrollbar class="patient_info-scrollbar">
            <p class="patient_title">{{questionnaireDetails.medicalName}}</p>
            <el-descriptions class="margin-top" :column="1" :labelStyle="widthStyle" border direction="vertical">
                <el-descriptions-item v-for="(item,index) in subjectList" :key="index"
                    labelClassName="descriptions_label">
                    <template slot="label">
                        {{item.propertyName}}
                    </template>
                    <component-echo :info="item"></component-echo>
                </el-descriptions-item>
            </el-descriptions>
            <el-form class="result_info">
            <p class="result_title">问卷结果</p>
                <el-form-item label="得分">
                    {{ questionnaireDetails.scores | nullFormat}}
                </el-form-item>
                <el-form-item label="评价">{{questionnaireDetails.evaluation | nullFormat}}</el-form-item>
            </el-form>
        </el-scrollbar>
    </div>
</template>

<script>
import { getQuestionnaireDetails } from '@/api/patient/questionnaire'
import ComponentEcho from '@/views/workstation/components/assembly'
export default {
    name: "questionDetail",
    components: {
        ComponentEcho,
    },
    data() {
        return {
            questionnaireDetails: {},
            subjectList: [],
            isLoadingPatientInfo: false,
            widthStyle: {  "width": "300px" },
            isLoadDetails: false,
        };
    },
    mounted() {
        this.initData()
    },
    activated() {
        this.initData()
    },
    beforeRouteLeave(to, from, next) {
        this.isLoadDetails = false
        next();
    },
    methods: {
        initData() {
            if (!this.isLoadDetails) {
                this.getPatientDetails()
            }
        },
        // 患教问卷详情
        getPatientDetails() {
            this.isLoadDetails = true;
            let query = this.$route.query
            let paramsData = {
                accountId: query.accountId,
                articleId: query.articleId,
                examRecordId: query.examRecordId,
                status: '2',
            }
            this.questionnaireDetails = {};
            this.subjectList = []
            this.isLoadingPatientInfo = true;
            getQuestionnaireDetails(paramsData).then(response => {
                let resData = response.data
                this.questionnaireDetails = resData;
                this.subjectList = resData.subjectList
                this.isLoadingPatientInfo = false;
            }).catch(error => {
                this.isLoadingPatientInfo = false;
            })
        },
    }
};
</script>
<style lang="scss" scoped>
.details_content {
    height: calc(100vh - 114px);
    color: #333;
    padding: 20px 20px;
    margin: 15px;
    background-color: #fff;
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0px 0px 10px 10px rgb(0, 0, 0, 0.01);
    .patient_title {
        line-height: 45px;
        text-align: center;
        font-size: 1.5em;
        color: #409eff;
        margin-bottom: 10px;
        font-weight: bold;
    }
    ::v-deep .el-descriptions {
        padding-bottom: 20px;
        margin: 0 20px;
        .descriptions_label {
            background: rgba(24, 144, 255, 0.05);
            color: #666;
            font-size: 1.1em;
        }
    }

    .patient_info-scrollbar {
        height: calc(100% - 10px);

        ::v-deep .el-scrollbar__wrap {
            overflow-x: hidden;
        }
    }
    .result_title {
        font-weight: bold;
        font-size: 1.2em;
        margin-bottom:8px;
    }
    .result_info{
        margin:0 20px;
    }
}
</style>