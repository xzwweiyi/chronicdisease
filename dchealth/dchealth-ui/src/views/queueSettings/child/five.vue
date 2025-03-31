<template>
    <el-scrollbar class="tabPane intelligent_scrollbar">
        <div class="com-details_title">随访提醒方式</div>
        <el-descriptions class="margin-top description_info" :column="1" border direction="vertical" label-class-name="descriptions_label">
            <el-descriptions-item>
                <template slot="label">
                    <el-checkbox label="1" name="1" v-model="outbound.checked">
                        智能外呼
                    </el-checkbox>
                </template>
                <el-form label-width="120px" :model="outbound" ref="outboundForm" :rules="outboundRules">
                    <el-form-item label="随访提醒：" required>
                        <el-form-item prop="followAdvanceDays" class="inline_formitem">
                            <div class="inline_dom">
                                <p>随访时间前</p>
                                <el-input class="dayNum_input" v-model="outbound.followAdvanceDays" placeholder="天数" />
                                <p>天提醒随访</p>
                            </div>
                        </el-form-item>
                        <el-form-item prop="followAdvanceTime" class="inline_formitem">
                            <div class="inline_dom">
                                <p>提醒时间</p>
                                <el-select v-model="outbound.followAdvanceTime" class="time_select">
                                    <el-option v-for="(item,index) in advanceTimeOptions" :key="index" :value="item.value" :label="item.label"></el-option>
                                </el-select>
                            </div>

                        </el-form-item>
                    </el-form-item>
                    <el-form-item prop="followSpeechId">
                        <div class="inline_dom">
                            <p>电话模板</p>
                            <el-select v-model="outbound.followSpeechId" class="model_select">
                                <el-option v-for="(item,index) in speechOptions" :key="index" :value="item.id" :label="item.name"></el-option>
                            </el-select>
                            <el-button type="primary" plain :disabled="!outbound.followSpeechId" @click="previewPhoneContent(outbound.followSpeechId)">预览模板</el-button>
                        </div>
                    </el-form-item>
                    <el-form-item prop="followReplay">
                        <div class="inline_dom">
                            <p>重播次数</p>
                            <el-radio-group v-model="outbound.followReplay">
                                <el-radio label="1">重拨1次</el-radio>
                                <el-radio label="2">重拨2次</el-radio>
                                <el-radio label="0">不重拨</el-radio>
                            </el-radio-group>
                        </div>
                    </el-form-item>
                    <el-form-item prop="followReplaySpaceMinute">
                        <div class="inline_dom">
                            <p>间隔时间</p>
                            <el-input-number v-model="outbound.followReplaySpaceMinute" :disabled="outbound.followReplay=='0'" :min="0">
                            </el-input-number>
                            <p>分钟</p>
                        </div>
                    </el-form-item>
                    <el-form-item label="超窗提醒：" prop="overAdvanceDays">
                        <div class="inline_dom">
                            <p>超过随访时间</p>
                            <p>{{ outbound.overAdvanceDays }}</p>
                            <p>天未填报提醒</p>
                        </div>
                        <div class="inline_dom">
                            <p>提醒时间</p>
                            <el-select v-model="outbound.overAdvanceTime" class="time_select">
                                <el-option v-for="(item,index) in advanceTimeOptions" :key="index" :value="item.value" :label="item.label"></el-option>
                            </el-select>
                        </div>
                    </el-form-item>
                    <el-form-item prop="overSpeechId">
                        <div class="inline_dom">
                            <p>电话模板</p>
                            <el-select v-model="outbound.overSpeechId">
                                <el-option v-for="(item,index) in speechOptions" :key="index" :value="item.id" :label="item.name"></el-option>
                            </el-select>
                            <el-button type="primary" plain :disabled="!outbound.overSpeechId" @click="previewPhoneContent(outbound.overSpeechId)">预览模板</el-button>
                        </div>
                    </el-form-item>
                    <el-form-item prop="overReplay">
                        <div class="inline_dom">
                            <p>重播次数</p>
                            <el-radio-group v-model="outbound.overReplay">
                                <el-radio label="1">重拨1次</el-radio>
                                <el-radio label="2">重拨2次</el-radio>
                                <el-radio label="0">不重拨</el-radio>
                            </el-radio-group>
                        </div>
                    </el-form-item>
                    <el-form-item prop="overReplaySpaceMinute">
                        <div class="inline_dom">
                            <p>间隔时间</p>
                            <el-input-number v-model="outbound.overReplaySpaceMinute" :disabled="outbound.overReplay=='0'" :min="0"></el-input-number>
                            <p>分钟</p>
                        </div>
                    </el-form-item>
                    <el-form-item label="预警提醒：" prop="warnSpeechId">
                        <div class="inline_dom">
                            <p>电话模板</p>
                            <el-select v-model="outbound.warnSpeechId">
                                <el-option v-for="(item,index) in speechOptions" :key="index" :value="item.id" :label="item.name"></el-option>
                            </el-select>
                            <el-button type="primary" plain :disabled="!outbound.warnSpeechId" @click="previewPhoneContent(outbound.warnSpeechId)">预览模板</el-button>
                        </div>
                    </el-form-item>
                    <el-form-item prop="warnReplay">
                        <div class="inline_dom">
                            <p>重播次数</p>
                            <el-radio-group v-model="outbound.warnReplay">
                                <el-radio label="1">重拨1次</el-radio>
                                <el-radio label="2">重拨2次</el-radio>
                                <el-radio label="0">不重拨</el-radio>
                            </el-radio-group>
                        </div>
                    </el-form-item>
                    <el-form-item prop="warnReplaySpaceMinute">
                        <div class="inline_dom">
                            <p>间隔时间</p>
                            <el-input-number v-model="outbound.warnReplaySpaceMinute" :disabled="outbound.warnReplay=='0'" :min="0"></el-input-number>
                            <p>分钟</p>
                        </div>
                    </el-form-item>
                </el-form>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <el-checkbox label="2" name="2" v-model="shortMsg.checked">
                        短信提醒
                    </el-checkbox>
                </template>
                <el-form label-width="120px" :model="shortMsg" :rules="shortMsgRules" ref="shortMsgForm">
                    <el-form-item label="随访提醒：" prop="followAdvanceDays">
                        <div class="inline_dom">
                            <p>随访时间前</p>
                            <el-input class="dayNum_input" v-model="shortMsg.followAdvanceDays" placeholder="天数" />
                            <p>天提醒随访</p>
                        </div>
                        <div class="inline_dom">
                            <p>提醒时间</p>
                            <el-select v-model="shortMsg.followAdvanceTime" class="time_select">
                                <el-option v-for="(item,index) in advanceTimeOptions" :key="index" :value="item.value" :label="item.label"></el-option>
                            </el-select>
                        </div>
                        <el-button type="primary" plain @click="previewShotrContent(1)">查看</el-button>
                    </el-form-item>
                    <el-form-item label="超窗提醒：" prop="overAdvanceDays">
                        <div class="inline_dom">
                            <p>超过随访时间</p>
                            <el-input class="dayNum_input" v-model="shortMsg.overAdvanceDays" placeholder="天数" />
                            <p>天未填报提醒</p>
                        </div>
                        <div class="inline_dom">
                            <p>提醒时间</p>
                            <el-select v-model="shortMsg.overAdvanceTime" class="time_select">
                                <el-option v-for="(item,index) in advanceTimeOptions" :key="index" :value="item.value" :label="item.label"></el-option>
                            </el-select>
                        </div>
                        <el-button type="primary" plain @click="previewShotrContent(2)">查看</el-button>
                    </el-form-item>
                </el-form>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    <el-checkbox label="3" name="3" v-model="instationMsg.checked">
                        APP站内信
                    </el-checkbox>
                </template>
                <el-form label-width="120px" :model="instationMsg" :rules="instationMsgRules" ref="instationMsgForm">
                    <el-form-item label="随访提醒：" prop="followAdvanceDays">
                        <div class="inline_dom">
                            <p>随访时间前</p>
                            <el-input class="dayNum_input" v-model="instationMsg.followAdvanceDays" placeholder="天数" />
                            <p>天提醒随访</p>
                        </div>
                        <div class="inline_dom">
                            <p>提醒时间</p>
                            <el-select v-model="instationMsg.followAdvanceTime" class="time_select">
                                <el-option v-for="(item,index) in advanceTimeOptions" :key="index" :value="item.value" :label="item.label"></el-option>
                            </el-select>
                        </div>
                    </el-form-item>
                    <el-form-item label="站内信内容：" prop="followMailContent">
                        <el-input type="textarea" v-model="instationMsg.followMailContent" placeholder="请输入站内信内容" />
                    </el-form-item>
                    <el-form-item label="超窗提醒：" prop="overAdvanceDays">
                        <div class="inline_dom">
                            <p>超过随访时间</p>
                            <el-input class="dayNum_input" v-model="instationMsg.overAdvanceDays" placeholder="天数" />
                            <p>天未填报提醒</p>
                        </div>
                        <div class="inline_dom">
                            <p>提醒时间</p>
                            <el-select v-model="instationMsg.overAdvanceTime" class="time_select">
                                <el-option v-for="(item,index) in advanceTimeOptions" :key="index" :value="item.value" :label="item.label"></el-option>
                            </el-select>
                        </div>
                    </el-form-item>
                    <el-form-item label="站内信内容：" prop="overMailContent">
                        <el-input type="textarea" v-model="instationMsg.overMailContent" placeholder="请输入站内信内容" />
                    </el-form-item>
                </el-form>
            </el-descriptions-item>
            <el-descriptions-item>
                <template slot="label">
                    预警提醒<span>（短信、站内信预警提醒为基础功能，无需配置）</span>
                </template>
                <el-form label-position="top" style="margin-left:15px">
                    <el-form-item label="预警短信">
                        <p class="warn_content">患者随访触发预警后，将第一时间发送短信提醒患者，短信模版为</p>
                        <el-button type="primary" plain @click="previewShotrContent(3)">查看</el-button>
                    </el-form-item>
                    <el-form-item label="预警站内信">
                        <p class="warn_content">患者随访触发预警后，将第一时间发送站内信提醒患者，站内信模版为</p>
                        <el-button type="primary" plain @click="previewShotrContent(0)">查看</el-button>
                    </el-form-item>
                </el-form>
            </el-descriptions-item>
        </el-descriptions>
        <div class="dialog-footer" style="margin:20px auto;text-align: center;">
            <el-button type="primary" @click="saveFuntion('save')">保 存</el-button>
            <el-button type="primary" @click="saveFuntion('nextStep')">保存并进入下一步</el-button>
        </div>
        <el-dialog :title="speechTitle" :visible.sync="isOpenSpeechDetails" width="700" @close="closeSpeedDetails" top="10vh">
            <el-image :src="speechDetails" class="speech_details_img" fit="contain" :preview-src-list="[speechDetails]"></el-image>
        </el-dialog>
        <!-- 添加或修改短信模版配置对话框 -->
        <el-dialog :title="smsTemplateTitle" :visible.sync="smsTemplateOpen" width="60%" center>
            <el-descriptions :column="1" label-class-name="custom_decript_label" v-if="smsTemplateOpen" border class="details_descript">
                <el-descriptions-item label="模版名称" prop="templateName">
                    {{ smsTemplateDetails.templateName }}
                </el-descriptions-item>
                <el-descriptions-item label="模板内容" prop="content">
                    {{ smsTemplateDetails.content }}
                </el-descriptions-item>
            </el-descriptions>
        </el-dialog>
    </el-scrollbar>
</template>

<script>
import {
    getQueueOutBoundList,
    getAdvanceTimeOptions,
    getQueueSpeechList,
    addQueueOutBoundList,
    getQueueSpeechDetails,
    getShortMsgDetails
} from '@/api/outbound/index'
export default {
    props: ['researchId'],
    components: {

    },
    data() {
        return {
            outbound: {},
            shortMsg: {},
            instationMsg: {},
            advanceTimeOptions: getAdvanceTimeOptions(),
            speechOptions: [],
            outboundRules: {
                followAdvanceDays: [{ required: true, message: '请输入随访提醒天数', trigger: 'blur' }],
                followAdvanceTime: [{ required: true, message: '请选择随访提醒时间', trigger: 'change' }],
                followSpeechId: [{ required: true, message: '请选择电话模板', trigger: 'change' }],
                followReplay: [{ required: true, message: '请选择重播次数', trigger: 'change' }],
                // followReplaySpaceMinute: [{ required: true, message: '请输入间隔时间', trigger: 'blur' }],
                // overAdvanceDays: [{ required: true, message: '请输入超窗提醒天数', trigger: 'blur' }],
                overAdvanceTime: [{ required: true, message: '请选择超窗提醒时间', trigger: 'change' }],
                overSpeechId: [{ required: true, message: '请选择电话模板', trigger: 'change' }],
                // overReplay: [{ required: true, message: '请选择重播次数', trigger: 'change' }],
                // overReplaySpaceMinute: [{ required: true, message: '请输入间隔时间', trigger: 'blur' }],
                warnSpeechId: [{ required: true, message: '请选择电话模板', trigger: 'change' }],
                warnReplay: [{ required: true, message: '请选择重播次数', trigger: 'change' }],
                // warnReplaySpaceMinute: [{ required: true, message: '请输入间隔时间', trigger: 'blur' }],
            },
            shortMsgRules: {
                followAdvanceDays: [{ required: true, message: '请输入随访提醒天数', trigger: 'blur' }],
                followAdvanceTime: [{ required: true, message: '请选择随访提醒时间', trigger: 'change' }],
                followSpeechId: [{ required: true, message: '请选择短信模板', trigger: 'change' }],
                overAdvanceDays: [{ required: true, message: '请输入超窗提醒天数', trigger: 'blur' }],
                overAdvanceTime: [{ required: true, message: '请选择超窗提醒时间', trigger: 'change' }],
                overSpeechId: [{ required: true, message: '请选择短信模板', trigger: 'change' }],
            },
            instationMsgRules: {
                followAdvanceDays: [{ required: true, message: '请输入随访提醒天数', trigger: 'blur' }],
                followAdvanceTime: [{ required: true, message: '请选择随访提醒时间', trigger: 'change' }],
                followMailContent: [{ required: true, message: '请输入站内信内容', trigger: 'blur' }],
                overAdvanceDays: [{ required: true, message: '请输入超窗提醒天数', trigger: 'blur' }],
                overAdvanceTime: [{ required: true, message: '请选择超窗提醒时间', trigger: 'change' }],
                overMailContent: [{ required: true, message: '请输入站内信内容', trigger: 'blur' }],
            },
            speechDetails: '',
            isOpenSpeechDetails: false,
            speechTitle: '模板详情',
            smsTemplateOpen: false,
            smsTemplateTitle: '',
            smsTemplateDetails: {},
        }
    },
    watch: {
        researchId: {
            immediate: true, // 监听到后，立即执行 handler方法
            handler(newVal, oldValue) {
                if (newVal && newVal != undefined) {
                    this.init()
                }
            }
        },
        'outbound.followReplay'(newVal) {
            if (newVal != '0') {
                this.outboundRules['followReplaySpaceMinute'] =
                    [{ required: true, message: '请输入间隔时间', trigger: 'blur' }]
            } else {
                this.outboundRules['followReplaySpaceMinute'] = null
                this.outbound.followReplaySpaceMinute = undefined
            }
        },
        'outbound.overReplay'(newVal) {
            if (newVal != '0') {
                this.outboundRules['overReplaySpaceMinute'] =
                    [{ required: true, message: '请输入间隔时间', trigger: 'blur' }]
            } else {
                this.outboundRules['overReplaySpaceMinute'] = null
                this.outbound.overReplaySpaceMinute = undefined
            }
        },
        'outbound.warnReplay'(newVal) {
            if (newVal != '0') {
                this.outboundRules['warnReplaySpaceMinute'] =
                    [{ required: true, message: '请输入间隔时间', trigger: 'blur' }]
            } else {
                this.outboundRules['warnReplaySpaceMinute'] = null
                this.outbound.warnReplaySpaceMinute = undefined
            }
        },
    },
    methods: {
        init() {
            this.getOutboundDetails()
            this.getOutboundSpeechOptions()
        },
        getOutboundDetails() {
            getQueueOutBoundList(this.researchId).then(res => {
                let resData = res.rows;
                let outboundInfo = undefined, shortMsgInfo = undefined, instationMsgInfo = undefined;
                if (resData && resData.length) {
                    outboundInfo = resData.find(f => f.type == '1')
                    shortMsgInfo = resData.find(f => f.type == '2')
                    instationMsgInfo = resData.find(f => f.type == '3')
                }
                if (outboundInfo) {
                    this.outbound = {
                        checked: outboundInfo ? true : false, ...outboundInfo, overAdvanceDays: 1,
                        overReplay: outboundInfo.overReplay ? outboundInfo.overReplay : '0',
                        followReplay: outboundInfo.followReplay ? outboundInfo.followReplay : '0',
                        warnReplay: outboundInfo.warnReplay ? outboundInfo.warnReplay : '0'
                    }
                } else {
                    this.outbound = {}
                }
                if (shortMsgInfo) {
                    this.shortMsg = { checked: shortMsgInfo ? true : false, ...shortMsgInfo }
                } else {
                    this.shortMsg = {}
                }
                let followContent = '您的随访即将开始，请关注唐都慢病管理App中的随访时间，按时完成随访'
                let overContent = '您还未进行随访，请关注唐都慢病管理App中的随访时间，尽快完成随访'
                if (instationMsgInfo) {
                    let infoFollowCont = instationMsgInfo.followMailContent
                    let infoOverCont = instationMsgInfo.overMailContent
                    this.instationMsg = {
                        checked: instationMsgInfo ? true : false, ...instationMsgInfo,
                        followMailContent: infoFollowCont ? infoFollowCont : followContent,
                        overMailContent: infoOverCont ? infoOverCont : overContent,
                    }
                } else {
                    this.instationMsg = {
                        followMailContent: followContent,
                        overMailContent: overContent,
                    }
                }
                // console.log('this.outbound', this.outbound)
            })
        },
        getOutboundSpeechOptions() {
            getQueueSpeechList(this.researchId).then(res => {
                this.speechOptions = res.rows.map(m => {
                    return {
                        ...m,
                        id: m.id.toString()
                    }
                })
            })
        },
        previewPhoneContent(speechId) {
            this.speechTitle = this.speechOptions.find(f => f.id == speechId).name
            this.isOpenSpeechDetails = true
            getQueueSpeechDetails(speechId).then(res => {
                this.speechDetails = res.msg
            })
        },
        previewShotrContent(speechId) {
            this.smsTemplateOpen = true;
            if (speechId) {
                getShortMsgDetails(speechId).then(res => {
                    this.smsTemplateDetails = res.data
                    this.smsTemplateTitle = "查看短信模版";
                })
            } else {
                this.smsTemplateDetails = {
                    content: '您有一条随访预警记录请及时查看',
                    templateName: '预警'
                }
                this.smsTemplateTitle = "预警站内信";
            }
        },
        closeSpeedDetails() {
            this.isOpenSpeechDetails = false
        },
        saveFuntion(type) {
            let validList = []
            let researchOutboundList = [];
            let researchId = { researchId: this.researchId }
            if (this.outbound.checked) {
                this.$refs.outboundForm.validate(valid => {
                    validList.push(valid)
                    if (valid) {
                        researchOutboundList.push({ ...researchId, type: 1, ...this.outbound })
                    }
                })
            } else {
                this.$refs.outboundForm.clearValidate()
            }
            if (this.shortMsg.checked) {
                this.$refs.shortMsgForm.validate(valid => {
                    validList.push(valid)
                    if (valid) {
                        researchOutboundList.push({ ...researchId, type: 2, ...this.shortMsg })
                    }
                })
            } else {
                this.$refs.shortMsgForm.clearValidate()
            }
            if (this.instationMsg.checked) {
                this.$refs.instationMsgForm.validate(valid => {
                    validList.push(valid)
                    if (valid) {
                        researchOutboundList.push({ ...researchId, type: 3, ...this.instationMsg })
                    }
                })
            } else {
                this.$refs.instationMsgForm.clearValidate()
            }
            if (validList.findIndex(f => !f) > -1) {
                return false
            } else if (!validList.length) {
                this.$message.warning('请选择提醒方式')
                return
            }
            this.isSaveLoading = true
            let params = {
                researchOutboundList
            }
            addQueueOutBoundList(params).then(res => {
                this.isSaveLoading = false
                if(type=='save'){
                    this.$message.success('保存成功')

                }else if(type=='nextStep'){
                    this.$emit('nextSubmitSix', true)
                }
            }).catch(error => {
                this.isSaveLoading = false
            })
        },
        saveMsg() {
            if (validList.findIndex(f => !f) > -1) {
                return false
            } else if (!validList.length) {
                this.$message.warning('请选择提醒方式')
                return
            } else {
                this.isSaveLoading = true
                let params = {
                    researchOutboundList
                }
                addQueueOutBoundList(params).then(res => {
                    this.isSaveLoading = false
                    this.$message.success('保存成功')
                }).catch(error => {
                    this.isSaveLoading = false
                })
            }
        },
        saveMsgAndNext() {
            this.saveMsg()
            this.$emit('nextSubmitSix', true)

        },
    }
}
</script>

<style lang="scss" scoped>
.intelligent_scrollbar {
    overflow-x: hidden !important;

    ::v-deep .descriptions_label {
        background: rgba(24, 144, 255, 0.05);
        color: #333;
    }
    .inline_formitem {
        display: inline-block;
        margin-bottom: 0 !important;
    }
    .inline_dom {
        display: inline-flex;
        align-items: center;
        margin-right: 15px;
        .dayNum_input {
            width: 80px;
            margin: 0 8px;
            text-align: center;
        }
        .time_select {
            width: 150px;
        }
        .el-select,
        .el-input-number,
        .el-radio-group {
            margin: 0 8px;
        }
        .model_select {
            width: 300px;
        }
    }

    .dayNum_textarea {
        width: 80%;
        margin: 10px 0;
    }
    .short_message {
        margin: 10px;
    }
}
.speech_details_img {
    width: 100%;
    height: 70vh;
}
::v-deep .details_descript {
    margin-bottom: 20px;
    .custom_decript_label {
        width: 160px;
        color: #333;
    }
}
.warn_content {
    display: inline-block;
    margin-right: 10px;
}
</style>