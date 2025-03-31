<template>
    <div class="app-container quatilty_evaluation_content">
        <el-form ref="userInfo" :model="userInfo" class="patient_info_form search_form" inline>
            <el-form-item label="患者姓名:">
                {{userInfo.patientName | nullFormat}}
            </el-form-item>
            <el-form-item label="性别:">
                {{userInfo.sex | convertAgentStr}}
            </el-form-item>
            <el-form-item label="患者ID:">
                {{patientId | nullFormat}}
            </el-form-item>
            <el-form-item label="随访时间:">
                {{userInfo.createTime | dateFormat}}
            </el-form-item>
            <el-form-item label="首次处理时间:">
                {{userInfo.createTime | dateFormat}}
            </el-form-item>
            <el-form-item label="完成处理时间:">
                {{userInfo.createTime | dateFormat}}
            </el-form-item>
        </el-form>
        <!-- 质效开始 待评价 -->
        <div v-for="(item, index) in medicalFormVOSList" :key="index">
            <h4 class="com-details_title">{{item.medicalName}}</h4>
            <el-descriptions class="margin-top" :column="1" :size="size" :labelStyle="widthStyle" border>
                <template slot="title">
                    <el-checkbox @change="medicalFormIdChange(item)"
                        v-if="(item.propertyVOS||item.childMedicalFormVOS)&&evaluateStatus!=3&&detailType==2" label="选择"
                        v-model="item.checked">
                    </el-checkbox>
                </template>
                <el-descriptions-item v-for="(items, indexs) in item.propertyVOS" :key="indexs">
                    <template slot="label">
                        {{items.propertyName}}
                    </template>
                    <component-echo :info="items"></component-echo>
                </el-descriptions-item>
                <template v-if="item.childMedicalFormVOS&&item.childMedicalFormVOS.length">
                    <el-descriptions-item v-for="(items, indexs) in item.childMedicalFormVOS" :key="indexs">
                        <template slot="label">
                            {{items.medicalName}}
                        </template>
                        <template v-if="items.openImage!='1'">
                            <div v-for="(propertyInfo,propertyIndex) in items.propertyVOS" :key="propertyIndex"
                                class="childMedicalFormVOS_property">
                                {{ propertyInfo.propertyName }}：
                                <component-echo :info="propertyInfo"></component-echo>
                            </div>
                        </template>
                        <template v-else-if="items.openImage=='1'">
                            <div v-for="(propertyInfo,propertyIndex) in [items.propertyVOS[0]]" :key="propertyIndex"
                                class="childMedicalFormVOS_property">
                                {{ propertyInfo.propertyName }}：
                                <component-echo :info="propertyInfo"></component-echo>
                            </div>
                        </template>
                    </el-descriptions-item>

                </template>
            </el-descriptions>
        </div>
        <h4 class="com-details_title">质疑记录</h4>
        <el-table :data="questionList" border v-if="questionList&&questionList.length">
            <el-table-column label="质疑" align="center" width="80">
                <template slot-scope="scope">质疑{{ scope.$index+1 }}</template>
            </el-table-column>
            <el-table-column label="质疑内容" prop="fromContent" align="center">
                <template slot-scope="scope">
                    <p class="reveal-container" v-html="scope.row.fromContent"></p>
                </template>
            </el-table-column>
            <el-table-column label="质疑时间" prop="fromTime" align="center" width="160">
                <template slot-scope="scope">{{scope.row.fromTime | dateFormat}}</template>
            </el-table-column>
            <el-table-column label="回复内容" prop="replyContent" align="center">
                <template slot-scope="scope">{{scope.row.replyContent | nullFormat}}</template>
            </el-table-column>
            <el-table-column label="回复时间" prop="replyTime" align="center" width="160">
                <template slot-scope="scope">{{scope.row.replyTime | dateFormat}}</template>
            </el-table-column>
        </el-table>
        <el-empty description="暂无记录" v-else></el-empty>
        <h4 class="com-details_title">预警模块</h4>
        <el-table :data="warnEarlyRecordData" border style="width: 100%">
            <el-table-column prop="propertyName" />
            <el-table-column prop="propertyValue" label="填入选项" />
            <el-table-column prop="warnDesc" label="预警规则" />
            <el-table-column prop="earlyWarnLevel" label="预警等级">
                <template slot-scope="scoped">
                    {{ scoped.row.earlyWarnLevel=='1'?'异常':'危机' }}
                </template>
            </el-table-column>
        </el-table>
        <h4 class="com-details_title">随访意见</h4>
        <div class="el-textarea__inner follow_opinion">{{ userInfo.followOpinion }}</div>
        <h4 class="com-details_title">调整队列</h4>
        <div class="plan_research_name">{{userInfo.adjustType==1?'保持原队列':'更新新队列'}}：
            {{ userInfo.planResearchName }}</div>
        <el-form ref="userInfo" :model="userInfo" label-width="100px">
            <h4 class="com-details_title">质效评价</h4>
            <el-form-item label="回复时效:">
                <el-rate v-model="evaluatePrescription" :disabled="evaluateStatus!='1'" show-text
                    :texts="['极差', '失望', '一般', '及时', '优秀']">
                </el-rate>
            </el-form-item>
            <el-form-item label="回复质量:">
                <el-rate v-model="evaluateQuality" :disabled="evaluateStatus!='1'" show-text
                    :texts="['极差', '失望', '一般', '及时', '优秀']">
                </el-rate>
            </el-form-item>
            <el-form-item label="回复评价:">
                <el-input type="textarea" :rows="6" placeholder="请输入回复评价" v-model="evaluateReply"
                    v-if="evaluateStatus=='1'" />
                <div class="el-textarea__inner follow_opinion" v-else>{{ evaluateReply }}</div>
            </el-form-item>
            <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
                <el-button type="primary" @click="submitEvalute" :loading="btnIsLoading"
                    v-if="evaluateStatus=='1'">提交</el-button>
                <el-button @click="cancleList()">返回</el-button>
            </el-form-item>
        </el-form>
        <!-- 质效结束 待评价 -->
    </div>
</template>

<script>
import { submitEvaluteInto } from "@/api/evaluate/config";
import {
    workDetails, //详情接口
    quseiontList, //质疑列表
} from "@/api/follow/work";
import ComponentEcho from '../../workstation/components/assembly'
export default {
    name: "qualityevaluationDetail",
    components: {
        ComponentEcho
    },
    data() {
        return {
            widthStyle: {
                "width": "300px"
            },
            // 遮罩层
            loading: true,
            // 用户信息
            userInfo: {
                adjustType: '1'
            },
            size: '12',
            researchIdRadio: [
                { value: '1', label: '保持原队列' },
                { value: '2', label: '更新新队列' },
            ],
            medicalFormVOSList: [],
            questionList: [], //质疑列表
            detailType: '',
            evaluateStatus: '',
            patientId: '',
            recordId: '',
            evaluateReply: '',
            evaluateQuality: 0,
            evaluatePrescription: 0,
            btnIsLoading: false,
            warnEarlyRecordData: [],
            belongType: '',
        };
    },
    watch: {
        "$route.query.patientId": {
            immediate: true, //第一次的数据，也要当做是一种变化
            // deep: true,
            handler(newVal, oldVal) {
                if (newVal != undefined && newVal != 0) {
                    this.detailType = this.$route.query.detailType
                }
            }
        }
    },
    created() {
        this.patientId = this.$route.query.patientId
        this.recordId = this.$route.query.recordId
        this.detailType = this.$route.query.detailType
        this.evaluateStatus = this.$route.query.evaluateStatus
        this.belongType = this.$route.query.belongType
        this.getDetails()
        this.getQtList()

    },
    methods: {
        /**
         * 获取详情
         */
        getDetails() {
            this.loading = true
            let dataObject = {
                patientId: this.patientId,
                recordId: this.recordId,
                detailType: 2,
            }
            workDetails(dataObject).then(response => {
                let objData = response.data
                objData['adjustType'] = '1'
                this.medicalFormVOSList = objData.medicalFormVOS
                this.warnEarlyRecordData = objData.warnEarlyRecordList;
                if (this.belongType == '1') {
                    this.evaluatePrescription = Number(objData.evaluatePrescriptionBack);
                    this.evaluateQuality = Number(objData.evaluateQualityBack);
                    this.evaluateReply = objData.evaluateReplyBack
                } else if (this.belongType == '3') {
                    this.evaluatePrescription = Number(objData.overEvaluatePrescriptionBack);
                    this.evaluateQuality = Number(objData.overEvaluateQualityBack);
                    this.evaluateReply = objData.overEvaluateReplyBack
                } else if (this.belongType == '4') {
                    this.evaluatePrescription = Number(objData.warnEvaluatePrescriptionBack);
                    this.evaluateQuality = Number(objData.warnEvaluateQualityBack);
                    this.evaluateReply = objData.warnEvaluateReplyBack
                }

                this.userInfo = objData
                this.loading = false
            })
        },
        /** 质疑记录 */
        getQtList() {
            this.loading = true
            quseiontList(this.recordId).then(response => {
                this.loading = false
                this.questionList = response.data.map(m => {
                    return {
                        ...m,
                        fromContent: m.fromContent.replace(/\n/g, '<br>')
                    }
                })
            })
        },
        // 质效评价
        submitEvalute() {
            if (!this.evaluateQuality || !this.evaluatePrescription) {
                this.$message.warning('回复时效和回复质量不能为空')
                return
            }
            if (!this.evaluateReply) {
                this.$message.warning('请填写回复评价')
                return
            }
            let params = {
                recordId: this.recordId,
                belongType: this.belongType
            }
            if (this.belongType == '1') {
                params = {
                    ...params,
                    evaluatePrescriptionBack: this.evaluatePrescription,
                    evaluateQualityBack: this.evaluateQuality,
                    evaluateReplyBack: this.evaluateReply,
                }
            } else if (this.belongType == '3') {
                params = {
                    ...params,
                    overEvaluatePrescriptionBack: this.evaluatePrescription,
                    overEvaluateQualityBack: this.evaluateQuality,
                    overEvaluateReplyBack: this.evaluateReply,
                }
            } else if (this.belongType == '4') {
                params = {
                    ...params,
                    warnEvaluatePrescriptionBack: this.evaluatePrescription,
                    warnEvaluateQualityBack: this.evaluateQuality,
                    warnEvaluateReplyBack: this.evaluateReply,
                }
            }
            this.btnIsLoading = true
            submitEvaluteInto(params).then(res => {
                this.btnIsLoading = false
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            }).catch(error => {

                this.btnIsLoading = false
            })
        },
        cancleList() {
            this.$tab.closePage(this.$route)
            this.$router.push({
                path: this.$route.query.oldPath
            })
        }
    },
};
</script>

<style lang="scss" scoped>
::v-deep.quatilty_evaluation_content {
    .patient_info_form {
        padding-bottom: 20px;

        .el-form-item {
            margin-right: 15px;
            margin-bottom: 0px;

            .el-form-item__content {
                white-space: nowrap;
            }
        }
    }

    .el-descriptions__header {
        justify-content: start;
    }

    .box-card {
        .item {
            line-height: 1.5;
            font-size: 14px;
        }
    }

    .el-rate {
        height: 36px;
        line-height: 36px;

        .el-rate__icon {
            font-size: 24px;
            margin-top: 6px;
        }
    }

    .follow_opinion {
        min-height: 96px;
    }

    .plan_research_name {
        color: #333;
        margin-bottom: 10px;
    }

    .childMedicalFormVOS_property {
        display: flex;
    }
}

.el-dialog__wrapper {
    /*隐藏ie和edge中遮罩的滚动条*/
    overflow: hidden;
}
</style>