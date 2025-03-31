<template>
    <div class="app-container">
        <el-breadcrumb separator="/" class="breadcrumb_content">
            <el-breadcrumb-item :to="{ path: '/workstation/user' }">随访工作站</el-breadcrumb-item>
            <el-breadcrumb-item><a href="/">高血压轻度患者队列</a></el-breadcrumb-item>
            <el-breadcrumb-item>随访01</el-breadcrumb-item>
        </el-breadcrumb>
        <el-form ref="userInfo" :model="userInfo" class="search_form" label-width="100px">
            <el-row>
                <el-col :span="6">
                    <el-form-item label="患者姓名：" prop="patientName">
                        {{userInfo.patientName}}
                    </el-form-item>
                </el-col>
                <el-col :span="5">
                    <el-form-item label="性别：" prop="sex">
                        {{userInfo.sex == 1 ? '男' : '女'}}
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="患者ID：" prop="patientCode">
                        {{userInfo.patientCode | nullFormat}}
                    </el-form-item>
                </el-col>
                <el-col :span="7">
                    <el-form-item label="创建时间：" prop="createTime">
                        {{userInfo.createTime | dateFormat}}
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <!-- 质效开始 待评价 -->
        <div>
            <div class="" v-for="(item, index) in medicalFormVOSList" :key="index">
                <h4 class="com-details_title">{{item.medicalName}}</h4>
                <el-descriptions class="margin-top" :column="1" :size="size" :labelStyle="widthStyle" border>
                    <template slot="title">
                        <el-checkbox v-if="item.propertyVOS&&nodeTypeStatus!=3&&detailsType==2" label="选择" v-model="item.checked">
                        </el-checkbox>
                    </template>
                    <el-descriptions-item v-for="(items, indexs) in item.propertyVOS" :key="indexs">
                        <template slot="label">
                            {{items.propertyName}}
                        </template>
                        <component-echo :info="items"></component-echo>
                    </el-descriptions-item>
                </el-descriptions>
            </div>
            <el-form ref="userInfo" :model="userInfo" label-width="100px">
                <h4 class="com-details_title">质疑记录</h4>
                <div>
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
                </div>
                <h4 class="com-details_title">随访意见</h4>
                <div class="el-textarea__inner follow_opinion">{{ userInfo.followOpinion }}</div>
                <h4 class="com-details_title">调整队列</h4>
                <div class="plan_research_name">{{userInfo.adjustType==1?'保持原队列':'更新新队列'}}：
                    {{ userInfo.planResearchName }}</div>
                <h4 class="com-details_title">质效评价</h4>
                <el-form-item label="回复时效：">
                    <el-rate v-model="evaluatePrescription" show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                    </el-rate>
                </el-form-item>
                <el-form-item label="回复质量：">
                    <el-rate v-model="evaluateQuality" show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                    </el-rate>
                </el-form-item>
                <el-form-item label="回复评价：">
                    <el-input type="textarea" :rows="6" placeholder="请输入" v-model="evaluateReply">
                    </el-input>
                </el-form-item>
                <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
                    <el-button type="primary" @click="submitEvalute" :loading="btnIsLoading">提交</el-button>
                    <el-button @click="close()">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
        <!-- 质效结束 待评价 -->
    </div>
</template>

<script>
import { tableData } from "@/utils/generator/quality";
import { submitEvaluteInto } from "@/api/evaluate/config";
import {
    workDetails, //详情接口
    quseiontList, //质疑列表
} from "@/api/follow/work";
export default {
    name: "workstationAuthRole",
    data() {
        return {
            widthStyle: { "width": "300px" },
            // 质疑弹窗
            open: false,
            // 遮罩层
            loading: true,
            // 分页信息
            total: 0,
            pageNum: 1,
            pageSize: 10,
            // 用户信息
            userInfo: {
                adjustType: '1'
            },
            size: '12',
            // 质疑弹窗
            formData: {},
            title: '',
            type: '',
            input2: '',
            //快捷回复弹窗
            openReply: false,
            quickForm: {},
            //随访时间弹窗
            openUpTime: false,
            formUpTime: {},
            //失访弹窗
            openLost: false,
            formLost: {
                lossCauseOther: '1'
            },
            //修改入组
            seeForm: {},
            nodeIdTwo: '',
            openModify: false,
            bzList: [],
            // 预警
            tableData,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                userName: undefined,
                status: undefined,
                deptId: undefined,
            },
            rulesSee: {},
            rulesLost: {},
            rules: {},
            uptimeRules: {
                visitTime: [{ required: true, message: "随访时间不能为空", trigger: "blur" }]
            },
            quickRules: {
                quickContent: [{ required: true, message: "文字不能为空", trigger: "blur" }]
            },
            url: require('@/assets/images/profile1.jpg'),
            srcList: [
                require('@/assets/images/profile1.jpg'),
            ],
            lostList: [
                { value: '1', label: '不再参与' },
                { value: '2', label: '失联' },
                { value: '3', label: '其他' },
            ],
            flag: false,
            researchIdRadio: [
                { value: '1', label: '保持原队列' },
                { value: '2', label: '更新新队列' },
            ],
            quickList: [], //快捷回复列表
            medicalFormVOSList: [],
            questionList: [], //质疑列表
            detailType: '',
            patientId: '',
            recordId: '',
            evaluateReply: '',
            evaluateQuality: 0,
            evaluatePrescription: 0,
            btnIsLoading: false,
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
                detailType: this.detailType,
            }
            workDetails(dataObject).then(response => {
                let objData = response.data
                objData['adjustType'] = '1'
                this.medicalFormVOSList = objData.medicalFormVOS
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
            let params = {
                recordId: this.recordId,
                evaluatePrescription: this.evaluatePrescription,
                evaluateQuality: this.evaluateQuality,
                evaluateReply: this.evaluateReply
            }
            this.btnIsLoading = true
            submitEvaluteInto(params).then(res => {
                this.btnIsLoading = false
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        }
    },
};
</script>

<style lang="scss">
.breadcrumb_content {
    margin-bottom: 22px;
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
}

/* 强制居中弹窗 */
.el-dialog {
    display: flex;
    display: -ms-flex;
    /* 兼容IE */
    flex-direction: column;
    -ms-flex-direction: column;
    /* 兼容IE */
    margin: 0 !important;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    max-height: calc(100% - 30px);
    max-width: calc(100% - 30px);
}

.el-dialog .el-dialog__body {
    max-height: 100%;
    flex: 1;
    -ms-flex: 1 1 auto;
    /* 兼容IE */
    overflow-y: auto;
    overflow-x: hidden;
}

.el-dialog__wrapper {
    /*隐藏ie和edge中遮罩的滚动条*/
    overflow: hidden;
}

.earlyBox {
    display: block;
    overflow: hidden;
    padding: 20px 20px;
    border: 1px solid #f5f5f5;

    .earlyUl {
        display: block;
        overflow: hidden;

        .earlyLi {
            display: flex;
            flex-direction: row;
            align-items: top;

            .left {
                display: inline-block;
                width: 50px;
                height: 50px;
                overflow: hidden;
                border-radius: 50%;

                img {
                    width: 100%;
                    height: 100%;
                }
            }

            .right {
                display: inline-block;
                margin-left: 15px;

                .top {
                    display: flex;
                    flex-direction: row;
                    align-items: center;

                    .span {
                        font-size: 16px;
                        font-weight: bold;
                    }

                    .em {
                        font-size: 12px;
                        color: #666;
                        padding: 0 10px;
                    }

                    .i {
                        font-size: 12px;
                        padding: 5px 10px;
                        color: #fff;
                        background: #409eff;
                        border-radius: 5px;
                    }
                }

                .center {
                    display: block;
                    padding: 5px 10px;
                    font-size: 14px;
                    margin: 10px 0;
                    border: 1px solid #efefef;
                }
            }
        }

        .earlyLiRight {
            justify-content: flex-end;

            .right {
                margin-right: 15px;

                .top {
                    justify-content: flex-end;
                }
            }
        }
    }
}

.alertUl {
    display: flex;
    flex-direction: column;

    .alertLi {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        background-color: #e7faf0;
        color: #13ce66;
        padding: 12px 16px;
        width: 100%;
        transition: opacity 0.2s;
        margin: 0px 0px 10px 0;

        .alertLiRight {
            display: flex;
            flex-direction: row;
            align-items: center;

            i {
                margin: 0 10px;
                cursor: pointer;
            }
        }
    }
}

.halfBg {
    width: calc(100% - 50vw);
}

.halfBg .viewer-canvas {
    left: -25vw;
}

.halfBg ul.viewer-list {
    margin-left: -25vw;
}
</style>