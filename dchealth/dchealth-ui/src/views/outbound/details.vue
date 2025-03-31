<template>
    <div class="app-container">
        <el-scrollbar class="details_content">
            <h4 class="com-details_title">外呼内容</h4>
            <div class="details_info">
                <div class="details_title info_tit">随访情况</div>
                <div class="details_desc">随访时间：{{ outboundDetails.xunfeiCallTime | nullFormat}}</div>
                <div class="details_desc">外呼结果：{{ outboundDetails.xunfeiResultCode | convertOutboundResult }}</div>
            </div>
            <div class="patient_audio" v-if="outboundDetails.xunfeiResultCode==0||outboundDetails.xunfeiResultCode==15">
                <audio class="my_audio" :src="outboundDetails.localRecordUrl||outboundDetails.xunfeiRecordUrl" controls
                    :autoplay="false" muted></audio>
            </div>
            <div v-if="dialogContent&&dialogContent.length" class="xunfei_dialog_list">
                <div v-for="(item,index) in dialogContent" :key="index" class="dialog_item">
                    <div v-if="item.robot" class="dialog_record record_left">
                        <img class="head_img" :src="robotHeadUrl" />
                        <div>
                            <p class="user_name">家庭医生智能随访助理</p>
                            <p class="user_result">{{ item.robot }}</p>
                        </div>
                    </div>
                    <div v-if="item.result" class="dialog_record record_right">
                        <img class="head_img" :src="userHeadUrl" />
                        <div>
                            <p class="user_name">居民</p>
                            <p class="user_result">{{ item.result }}</p>
                        </div>

                    </div>
                </div>
            </div>
            <el-empty v-else :image-size="200"></el-empty>
        </el-scrollbar>
    </div>
</template>

<script>
import { getOutboundDetails } from '@/api/outbound/index'
export default {
    name: "outbound_detail",
    components: {},
    data() {
        return {
            outboundDetails: {},
            dialogContent: '',
            robotHeadUrl: require("@/assets/images/robot_head.svg"),
            userHeadUrl: require("@/assets/images/head_white.svg"),
            outboundId: '',
        };
    },
    watch: {
        '$route.query.outboundId': {
            immediate: true,
            handler(newVal) {
                if (newVal) {
                    console.log(newVal)
                    this.initData()

                }
            }
        }
    },
    created() {
    },
    methods: {
        initData() {
            let queryInfo = this.$route.query
            this.outboundId = queryInfo.outboundId
            this.researchId = queryInfo.researchId
            this.getPatientDetails();
        },
        getPatientDetails() {
            this.isLoadingPatinentDetails = true;
            getOutboundDetails(this.outboundId).then(result => {
                let resultData = result.data
                this.outboundDetails = resultData
                this.isLoadingPatinentDetails = false;
                this.dialogContent = resultData.xunfeiDialog ? JSON.parse(resultData.xunfeiDialog) : ''
                console.log('this.dialogContent', this.dialogContent)
            }).catch(error => {
                this.isLoadingPatinentDetails = false;
            })
        },
    }
};
</script>
<style lang="scss" scoped>
.details_content {
    height: calc(100vh - 164px);
    color: #333;
    border-radius: 5px;
    .details_info {
        padding: 5px 20px;
        line-height: 2;
        .details_title {
            font-weight: bold;
        }
        .details_desc {
        }
    }
    .patient_audio {
        .my_audio {
            width: 98%;
            margin: 10px 1%;
        }
    }
    .xunfei_dialog_list {
        padding: 15px;
        border: 1px #ccc solid;
        border-radius: 5px;
        .dialog_item {
            margin-bottom: 10px;
        }
        .dialog_record {
            display: flex;
            .head_img {
                width: 38px;
                height: 38px;
                padding: 8px;
                border-radius: 50%;
                background: #96bbb5;
            }
            .user_name {
                color: #a7a7a7;
                line-height: 2;
            }
            .user_result {
                line-height: 2;
            }
            &.record_left {
                flex-direction: row;
                .head_img {
                    background: #96bbb5;
                    margin-right: 5px;
                }
            }
            &.record_right {
                flex-direction: row-reverse;
                text-align: right;
                .head_img {
                    background: #56b09d;
                    margin-left: 5px;
                }
            }
        }
    }
}
</style>