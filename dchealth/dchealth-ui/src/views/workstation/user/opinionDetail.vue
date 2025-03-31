<template>
    <div class="app-container option-container">
        <el-descriptions :column="4" border class="patient_info" label-class-name="patient_label" content-class-name="patient_content">
            <el-descriptions-item label="患者姓名">{{userInfo.patientName | nullFormat}}</el-descriptions-item>
            <el-descriptions-item label="性别">{{userInfo.sex |convertAgentStr}}</el-descriptions-item>
            <el-descriptions-item label="患者ID">{{usrePersonId | nullFormat}}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{userInfo.createTime|dateFormat}}</el-descriptions-item>
        </el-descriptions>
        <el-scrollbar class="option-detail-scrollbar">
            <div class="" v-for="(item, index) in medicalFormVOSList" :key="index">
                <h4 class="com-details_title">
                    <el-checkbox @change="medicalFormIdChange(item)" v-if="(item.propertyVOS||item.childMedicalFormVOS)&&nodeTypeStatus<3&&detailsType==2" :label="item.medicalName" v-model="item.checked">
                    </el-checkbox>
                    <p v-else>{{ item.medicalName }}</p>
                </h4>
                <el-descriptions class="margin-top medical_form" :column="1" :labelStyle="widthStyle" border>
                    <el-descriptions-item v-for="(items, indexs) in item.propertyVOS" :key="indexs" label-class-name="patient_label">
                        <template slot="label">
                            {{items.propertyName}}
                            <el-tag type="danger" effect="dark" v-if="item.warnSource">预警源</el-tag>
                        </template>
                        <component-echo :info="items"></component-echo>
                    </el-descriptions-item>
                    <template v-if="item.childMedicalFormVOS&&item.childMedicalFormVOS.length">
                        <el-descriptions-item v-for="(items, indexs) in item.childMedicalFormVOS" :key="indexs" label-class-name="patient_label" :label="items.medicalName">
                            <el-descriptions v-if="items.openImage!='1'" :column="1" border direction="vertical">
                                <el-descriptions-item v-for="(propertyInfo,propertyIndex) in items.propertyVOS" label-class-name="patient_label" :key="propertyIndex" class="childMedicalFormVOS_property" :label="propertyInfo.propertyName">
                                    <component-echo :info="propertyInfo"></component-echo>
                                </el-descriptions-item>
                            </el-descriptions>
                            <template v-else-if="items.openImage=='1'">
                                <div v-for="(propertyInfo,propertyIndex) in [items.propertyVOS[0]]" :key="propertyIndex" class="childMedicalFormVOS_property">
                                    {{ propertyInfo.propertyName }}：
                                    <component-echo :info="propertyInfo"></component-echo>
                                </div>
                            </template>
                        </el-descriptions-item>

                    </template>
                </el-descriptions>
            </div>
            <!-- 分配队列开始  -->
            <div class="user_detail_buttons" v-if="detailsType == 0">
                <el-button type="primary" @click="assignQueueFollow(0)" v-if="nodeTypeStatus<3" v-hasPermi="['follow:patient_enrollment:handle']">分配队列</el-button>
                <el-button @click="cancleUserList()">返回</el-button>
            </div>
            <!-- 分配队列结束 -->
            <!-- 入组开始  -->
            <div class="user_detail_buttons" v-if="detailsType == 1">
                <template v-if="nodeTypeStatus<3">
                    <el-button type="primary" @click="assignQueueFollow('3')" v-hasPermi="['follow:patient_enrollment:handle']">确认入组</el-button>
                    <!-- <el-button type="primary" plain @click="assignQueueFollow('2')" v-hasPermi="['follow:patient_enrollment:handle']">修改入组</el-button> -->
                    <!-- <el-button type="warning" plain @click="assignQueueFollow('1')"
                        v-hasPermi="['follow:patient_enrollment:handle']">待分配</el-button> -->
                </template>
                <el-button @click="cancleUserList()">返回</el-button>
            </div>
            <!-- 入组结束 -->
            <!-- 质疑开始 -->
            <div v-if="detailsType == 2">
                <div class="user_detail_buttons" v-if="nodeTypeStatus<3">
                    <el-button type="danger" @click="challenge" v-hasPermi="['system:record:saveQuestionRecord']" v-if="medicalFormVOSList&&medicalFormVOSList.length" :disabled="nodeTypeStatus!=1">
                        发起质疑
                    </el-button>
                    <!-- <el-button type="primary">修改随访</el-button> -->
                </div>
                <h4 class="com-details_title">质疑记录</h4>
                <el-table :data="questionList" border v-if="questionList.length">
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
                <template v-if="nodeTypeStatus!=2">
                    <h4 class="com-details_title">随访意见
                        <el-button type="primary" @click="changReply" v-if="nodeTypeStatus<3">快捷回复
                        </el-button>
                    </h4>
                    <div>
                        <el-input type="textarea" :rows="8" placeholder="请输入内容" v-model="userInfo.followOpinion" v-if="nodeTypeStatus<3" />
                        <div class="user_follow_opinion" v-else>{{ userInfo.followOpinion }}</div>
                    </div>
                    <h4 class="com-details_title">调整队列</h4>
                    <template v-if="nodeTypeStatus<3">
                        <el-radio-group v-model="userInfo.adjustType" @change="changeTwo" class="adjustType_radio_group">
                            <el-radio v-for="(dict, index) in researchIdRadio" :key="index" :label="dict.value">
                                {{dict.label}}</el-radio>
                        </el-radio-group>
                        <el-form>
                            <el-form-item label="更新队列：" prop="researchId" v-if="userInfo.adjustType == 2">
                                <el-cascader v-model="userInfo.researchId" :options="bzList" :props="{value: 'nodeId', label: 'nodeName', children: 'researchDiseasesSelects'}" :show-all-levels="false" clearable @change="hadleSelect"></el-cascader>
                            </el-form-item>
                        </el-form>
                    </template>
                    <div v-else>
                        {{userInfo.adjustType=='1'?researchIdRadio[0].label:researchIdRadio[1].label}}：
                        {{ userInfo.planResearchName | nullFormat }}
                    </div>
                    <el-form label-width="100px" v-if="nodeTypeStatus==4">
                        <h4 class="com-details_title">随访质效评价</h4>
                        <el-form-item label="回复时效：">
                            <el-rate v-model="userInfo.evaluatePrescriptionBack" disabled show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                            </el-rate>
                        </el-form-item>
                        <el-form-item label="回复质量：">
                            <el-rate v-model="userInfo.evaluateQualityBack" disabled show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                            </el-rate>
                        </el-form-item>
                        <el-form-item label="回复评价：">
                            <div class="user_follow_opinion">{{ userInfo.evaluateReplyBack }}</div>
                        </el-form-item>
                    </el-form>
                </template>
                <div class="user_detail_buttons">
                    <el-button type="primary" @click="submitFormOpinion" v-if="nodeTypeStatus<2" v-hasPermi="['follow:opinion:handle']">提交意见</el-button>
                    <el-button @click="cancleUserList()">返回</el-button>
                </div>
            </div>
            <!-- 质疑结束 -->
            <!-- 超窗开始 -->
            <template v-else-if="detailsType== 3">
                <el-form label-width="100px" v-if="nodeTypeStatus==4">
                    <h4 class="com-details_title">超窗质效评价</h4>
                    <el-form-item label="回复时效：">
                        <el-rate v-model="userInfo.overEvaluatePrescriptionBack" disabled show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                        </el-rate>
                    </el-form-item>
                    <el-form-item label="回复质量：">
                        <el-rate v-model="userInfo.overEvaluateQualityBack" disabled show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                        </el-rate>
                    </el-form-item>
                    <el-form-item label="回复评价：">
                        <div class="user_follow_opinion">{{ userInfo.overEvaluateReplyBack }}</div>
                    </el-form-item>
                </el-form>
                <div class="user_detail_buttons">
                    <template v-if="nodeTypeStatus==1">
                        <el-button type="primary" @click="changUpTiem" v-hasPermi="['follow:over_time:handle']">更新随访时间
                        </el-button>
                        <el-button type="success" @click="changIgnore" v-hasPermi="['follow:over_time:handle']">忽略本次
                        </el-button>
                        <el-button type="danger" @click="changLost" v-hasPermi="['follow:over_time:handle']">失访
                        </el-button>
                    </template>
                    <el-button @click="cancleUserList()">返回</el-button>
                </div>
            </template>
            <!-- 超窗结束 -->
            <!-- 预警开始 -->
            <div v-else-if="detailsType== 4">
                <h4 class="com-details_title" style="color:#ff0000">预警模块</h4>
                <div class="form-box">
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
                </div>
                <h4 class="com-details_title">预警指导</h4>
                <div class="earlyBox">
                    <div class="warn_earlyList">
                        <div class="warn_early_item" v-for="(item,index) in warnEarlyGuideData" :key="index" :class="item.userType=='1'?'early_item_doctor':''">
                            <el-avatar :size="50" :src="item.headImage||userHeadImgUrl"></el-avatar>
                            <div class="warn_guide_info">
                                <div :class="item.userType=='1'?'flex_end':'flex_start'">
                                    <span class="span">{{item.userName}}</span>
                                    <span class="em">{{item.msgTime | dateFormat}}</span>
                                    <span class="i">{{item.userType=='1'?'医生':'患者'}}</span>
                                </div>
                                <div class="center" v-if="item.msgType=='1'">{{item.msgText}}</div>
                                <div class="center" v-else-if="item.msgType=='2'">
                                    <el-image :src="item.msgText" :preview-src-list="item.imgUrlList"></el-image>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="display_flex" v-if="nodeTypeStatus<3">
                        <el-input placeholder="请输入内容" v-model="warnEarlyGuideMsg" clearable />
                        <el-button type="primary" v-if="warnEarlyGuideMsg" @click="sendWarnGuide">发送</el-button>
                        <el-upload :action="uploadImgUrl" v-else :on-success="handleUploadSuccess" :before-upload="handleBeforeUpload" :on-error="handleUploadError" name="file" :headers="headers" :show-file-list="false">
                            <el-button type="primary">发送图片</el-button>
                        </el-upload>
                    </div>
                </div>
                <el-form label-width="100px" v-if="nodeTypeStatus==4">
                    <h4 class="com-details_title">预警质效评价</h4>
                    <el-form-item label="回复时效：">
                        <el-rate v-model="userInfo.warnEvaluatePrescriptionBack" disabled show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                        </el-rate>
                    </el-form-item>
                    <el-form-item label="回复质量：">
                        <el-rate v-model="userInfo.warnEvaluateQualityBack" disabled show-text :texts="['极差', '失望', '一般', '及时', '优秀']">
                        </el-rate>
                    </el-form-item>
                    <el-form-item label="回复评价：">
                        <div class="user_follow_opinion">{{ userInfo.warnEvaluateReplyBack }}</div>
                    </el-form-item>
                </el-form>
                <div class="user_detail_buttons">
                    <el-button type="primary" v-if="nodeTypeStatus>=3" disabled>已完成</el-button>
                    <el-button type="primary" @click="finishedWarnEarlyGuide" v-else v-hasPermi="['warn:guide:end']">
                        结束预警指导</el-button>
                    <el-button @click="cancleUserList()">返回</el-button>
                </div>
            </div>
            <!-- 预警结束 -->
        </el-scrollbar>
        <!-- 质效结束 已完成 -->
        <!-- 修改入组 -->
        <el-dialog :title="enrollmentTitle" :visible.sync="openModify" width="500px" center>
            <el-form ref="seeForm" :model="seeForm" :rules="rulesSee" label-width="120px">
                <el-form-item label="选择病种队列：" prop="researchId">
                    <el-cascader v-model="seeForm.researchId" :options="bzList" class="dialog_cascader" :props="{value: 'nodeId', label: 'nodeName', children: 'researchDiseasesSelects'}" :show-all-levels="false" clearable @change="hadleSelect"></el-cascader>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button @click="handleSeeForm" type="primary">确 认</el-button>
                <el-button @click="canleSeeForm">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 快捷回复开始 -->
        <el-dialog :title="quickReplyTitle" :visible.sync="openReply" width="60%" top="8vh" center>
            <el-table :data="quickList" @row-click="clickRow" ref="selectTable" class="select_table_style mb10" @selection-change="handleSelectionChange" height="55vh">
                <el-table-column align="center" type="selection" width="55" />
                <el-table-column align="center" label="内容" prop="quickContent"></el-table-column>
                <el-table-column align="center" width="150" label="操作">
                    <template slot-scope="scoped">
                        <el-button type="text" @click.stop="setQuickContent(scoped.row)">使用</el-button>
                        <el-button type="text" icon="el-icon-delete" class="deleteBtn" @click.stop="delAlertLi(scoped.row)">
                            删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="display_flex">
                <el-input placeholder="请输入内容" v-model="quickForm.quickContent" clearable />
                <el-button type="primary" :disabled="!quickForm.quickContent" @click="handeAdd">
                    添加新的快捷回复
                </el-button>
            </div>
            <div slot="footer">
                <el-button type="primary" @click="addQuickMultiple">确定</el-button>
                <el-button type="primary" plain @click="cancleQuickMultiple">取消</el-button>
            </div>
        </el-dialog>
        <!-- 快捷回复结束 -->
        <!-- 发起质疑 -->
        <el-dialog :title="title" v-loading="loading" :visible.sync="challengeOpen" width="600px" center>
            <el-form label-width="80px">
                <el-form-item label-width="0" prop="nickName">
                    <el-input type="textarea" v-model="fromContent" :rows="10" placeholder="请输入内容" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 发起质疑结束 -->
        <!-- 更新随访时间 -->
        <el-dialog :title="title" :visible.sync="openUpTime" width="600px" center>
            <el-form ref="formUpTime" :model="formUpTime" :rules="uptimeRules" label-width="120px">
                <el-form-item label="随访时间" prop="visitTime">
                    <el-date-picker v-model="formUpTime.visitTime" type="date" :picker-options="datePickerOptions" placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button type="primary" @click="submitFormThree">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 失访 -->
        <el-dialog :title="title" :visible.sync="openLost" width="600px" center>
            <el-alert title="失访后患者将不再继续该队列的随访!" type="error">
            </el-alert>
            <div style="font-size:16px;font-weight:bold;line-height:40px;text-align:center"></div>
            <el-form ref="formLost" :model="formLost" :rules="rulesLost" label-width="90px">
                <el-form-item label="失访原因：" prop="lossCause">
                    <el-radio-group v-model="formLost.lossCause" @change="changeLost">
                        <el-radio v-for="(item, index) in lostList" :key="index" :label="item.value">
                            {{ item.label }}
                        </el-radio>

                    </el-radio-group>
                </el-form-item>
                <el-form-item label="其他原因：" prop="lossCauseOther" v-if="flag">
                    <el-input type="textarea" :rows="3" v-model="formLost.lossCauseOther" placeholder="请输入内容" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button type="primary" @click="submitFormLost">确 定</el-button>
                <el-button @click="cancelLost">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 忽略本次 -->
        <el-dialog title="忽略本次" :visible.sync="isShowIgnore" width="500px" center>
            <div class="ignore_content">
                <p class="ignore_tips">忽略本次后将不再进行本次随访</p>
                <p class="ignore_tit">选择原因</p>
                <el-radio-group v-model="ignoreReason">
                    <el-radio label="0">患者本次不随访</el-radio>
                    <el-radio label="1">患者已完成随访</el-radio>
                </el-radio-group>

            </div>
            <div slot="footer" class="dialog_footer">
                <el-button @click="updateIgnoreReason('2')" type="primary">确 认</el-button>
                <el-button @click="canleIgnoreReset">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import moment from "moment";
import {
    workDetails, //详情接口
    handleEnrollment, //确认入组/修改入组/待分配入组
    handleAllocated, //待分配入组操作
    selectResearch, //病种及队列下拉框
    handleTime, //超窗处理
    handleOpinion, //提交意见
    listQuick, //快捷回复列表
    addQuick, //快捷回复添加
    delQuick, //快捷回复删除
    quseiontList, //质疑列表
    saveQuestionContent, //发起质疑
    warnEarlyGuideList, //预警指导聊天内容
    sendWarnEarlyNews, //【预警指导聊天内容】
    finishWarnEarlyGuide, //【结束预警指导】
} from "@/api/follow/work";
import ComponentEcho from '../components/assembly'
import {
    getToken
} from "@/utils/auth";
export default {
    name: "workstationAuthRole",
    components: {
        ComponentEcho
    },
    data() {
        return {
            uploadImgUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
            headers: {
                Authorization: "Bearer " + getToken(),
            },
            // 大小限制(MB)
            fileSize: 5,
            // 文件类型, 例如['png', 'jpg', 'jpeg']
            fileType: ["png", "jpg", "jpeg"],
            widthStyle: { "width": "300px" },
            // 质疑弹窗
            challengeOpen: false,
            // 遮罩层
            loading: true,
            // 分页信息
            total: 0,
            pageNum: 1,
            pageSize: 10,
            // 用户信息
            userInfo: {},
            size: '12',
            // 质疑弹窗
            fromContent: '',
            title: '',
            //快捷回复弹窗
            quickReplyTitle: '快捷回复',
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
            warnEarlyRecordData: [],
            warnEarlyGuideMsg: '', //发送预警信息内容
            warnEarlyGuideData: [], //预警指导数据
            userHeadImgUrl: require('@/assets/images/user_headImg.webp'),
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                userName: undefined,
                phonenumber: undefined,
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
            srcList: [require('@/assets/images/profile1.jpg'),],
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
            questionList: [], //质疑列表
            medicalFormVOSList: [], //当前随访表单列表
            detailsType: '',
            nodeTypeStatus: '',
            patientId: '',
            recordId: '',
            researchId: '',
            enrollmentTitle: '', // 患者入组弹窗的title
            isShowIgnore: false,
            ignoreReason: '0',
            datePickerOptions: undefined,
            quickSelection: [],
            usrePersonId:'',
        };
    },
    watch: {
        "$route.query.patientId": {
            immediate: true, //第一次的数据，也要当做是一种变化
            // deep: true,
            handler(newVal, oldVal) {
                if (newVal != undefined && newVal != 0) {
                    this.detailsType = this.$route.query.type
                }
            }
        }
    },
    created() {
        const queryData = this.$route.query
        this.nodeTypeStatus = queryData.nodeTypeStatus
        this.patientId = queryData.patientId
        this.detailsType = queryData.type
        this.recordId = queryData.recordId;
        this.usrePersonId = queryData.personId||this.patientId

        this.getDetails()
        if (this.detailsType == '2') {
            this.getQtList()
        }
        if (this.detailsType == '4') {
            this.getWarnEarlyGuide()
        }
    },
    methods: {
        // 上传前loading加载
        handleBeforeUpload(file) {
            let isImg = false;
            if (this.fileType.length) {
                let fileExtension = "";
                if (file.name.lastIndexOf(".") > -1) {
                    fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
                }
                isImg = this.fileType.some(type => {
                    if (file.type.indexOf(type) > -1) return true;
                    if (fileExtension && fileExtension.indexOf(type) > -1) return true;
                    return false;
                });
            } else {
                isImg = file.type.indexOf("image") > -1;
            }

            if (!isImg) {
                this.$modal.msgError(`文件格式不正确, 请上传${this.fileType.join("/")}图片格式文件!`);
                return false;
            }
            if (this.fileSize) {
                const isLt = file.size / 1024 / 1024 < this.fileSize;
                if (!isLt) {
                    this.$modal.msgError(`上传图片大小不能超过 ${this.fileSize} MB!`);
                    return false;
                }
            }
            this.$modal.loading("正在发送图片请稍候...");
            this.number++;
        },
        // 上传失败
        handleUploadError() {
            this.$modal.msgError("上传图片失败，请重试");
            this.$modal.closeLoading();
        },
        // 更新随访时间disabled
        initDatePicker() {
            this.datePickerOptions = {
                'disabledDate': time => {
                    return moment(time).valueOf() < moment().valueOf()
                }
            }
        },
        /**
         * 获取详情
         */
        getDetails() {
            this.loading = true
            let dataObject = {
                patientId: this.patientId,
                recordId: this.recordId,
                detailType: this.detailsType,
            }
            workDetails(dataObject).then(response => {
                let objData = response.data;
                const {
                    medicalFormVOS,
                    medicalFormExamVOS,
                    ...otherObjData
                } = objData
                otherObjData['adjustType'] = objData.adjustType || '1'
                if (objData.adjustType == '2') {
                    this.getQueueSelect();
                }
                let medicalFormVOSList = medicalFormVOS && medicalFormVOS.length ?
                    medicalFormVOS.map(m => {
                        return {
                            ...m,
                            checked: false,
                        }
                    }) : []
                if (medicalFormExamVOS && medicalFormExamVOS.length) {
                    let examVos = medicalFormExamVOS.map(m => {
                        return {
                            ...m,
                            propertyVOS: m.propertyExamVOS,
                            checked: false
                        }
                    })
                    medicalFormVOSList = medicalFormVOSList.concat(examVos)
                }
                let evaluate = {
                    evaluatePrescriptionBack: otherObjData.evaluatePrescriptionBack
                        ? Number(otherObjData.evaluatePrescriptionBack) : 0,//随访质效评价
                    evaluateQualityBack: otherObjData.evaluateQualityBack
                        ? Number(otherObjData.evaluateQualityBack) : 0,
                    //告警质效评价 
                    warnEvaluatePrescriptionBack: otherObjData.warnEvaluatePrescriptionBack
                        ? Number(otherObjData.warnEvaluatePrescriptionBack) : 0,
                    warnEvaluateQualityBack: otherObjData.warnEvaluateQualityBack
                        ? Number(otherObjData.warnEvaluateQualityBack) : 0,
                    //超窗质效评价 
                    overEvaluatePrescriptionBack: otherObjData.overEvaluatePrescriptionBack
                        ? Number(otherObjData.overEvaluatePrescriptionBack) : 0,
                    overEvaluateQualityBack: otherObjData.overEvaluateQualityBack
                        ? Number(otherObjData.overEvaluateQualityBack) : 0,

                }
                this.userInfo = {
                    ...otherObjData,
                    ...evaluate,
                    medicalFormVOS: medicalFormVOSList
                }
                this.medicalFormVOSList = medicalFormVOSList
                this.warnEarlyRecordData = objData.warnEarlyRecordList;
                this.loading = false
            })
        },
        /**
         * 获取预警指导数据
         */
        getWarnEarlyGuide() {
            warnEarlyGuideList(this.recordId).then(res => {
                console.log('res', res)
                this.warnEarlyGuideData = res.data.map(m => {
                    return {
                        ...m,
                        imgUrlList: m.msgType == '2' ? res.data.filter(f => f.msgType == '2').map(
                            m => m.msgText) : []
                    }
                })
            })
        },
        // 上传成功回调
        handleUploadSuccess(res) {
            let guideData = this.warnEarlyGuideData
            const warnParams = {
                recordId: this.recordId,
                warnGuideConverId: guideData && guideData.length ? guideData[0].warnGuideConverId : '',
                msgType: '2', //消息类型1文本2图片
                msgText: '',
                msgImageUrl: res.url,
            }
            sendWarnEarlyNews(warnParams).then(res => {
                this.getWarnEarlyGuide()
                this.warnEarlyGuideMsg = ''
                this.$modal.closeLoading();
            })
        },
        /**
         * 发送预警信息
         */
        sendWarnGuide() {
            let guideData = this.warnEarlyGuideData
            if (!guideData) {
                this.$message.warning('预警指导内容不能为空')
                return
            }
            const warnParams = {
                recordId: this.recordId,
                warnGuideConverId: guideData && guideData.length ? guideData[0].warnGuideConverId : '',
                msgType: '1', //消息类型1文本2图片
                msgText: this.warnEarlyGuideMsg,
                msgImageUrl: '',
            }
            sendWarnEarlyNews(warnParams).then(res => {
                this.getWarnEarlyGuide()
                this.warnEarlyGuideMsg = ''
            })
        },
        /**
         * 结束预警指导
         */
        finishedWarnEarlyGuide() {
            this.$confirm(`请确认是否结束预警指导?`, '提示', {
                type: 'warning'
            }).then(() => {
                finishWarnEarlyGuide(this.recordId).then(res => {
                    this.$tab.closePage(this.$route)
                    this.$router.push({
                        path: this.$route.query.oldPath
                    })
                })
            }).catch(error => {
                return
            })
        },
        /**
         * 选择分组
         */
        hadleSelect(val) {
            let arr = val.slice(1, 2).toString();
            this.nodeIdTwo = arr
        },
        /**
         * 获取队列
         */
        getQueueSelect() {
            selectResearch().then(response => {
                let valueArr = response.data
                this.bzList = valueArr.filter(f => f.researchDiseasesSelects)
            })
        },
        getInfoQuery() {
            this.detailsType = this.$route.params
        },
        medicalFormIdChange(event) {
            console.log(event)
            this.medicalFormIds
        },
        /**
         * 调整队列
         */
        changeTwo(val) {
            if (val == '2') {
                this.getQueueSelect();
            } else {
                this.userInfo.researchId = this.researchId
            }
        },
        /**
         * 修改入组
         */
        handleSeeForm() {
            handleEnrollment({
                handleType: '2',
                researchId: this.nodeIdTwo,
                patientId: this.patientId
            }).then(response => {
                this.$modal.msgSuccess("修改入组成功");
                this.openModify = false;
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        },
        /**
         * 取消入组弹窗
         */
        canleSeeForm() {
            this.openModify = false
        },
        /**
         * 提交意见
         */
        submitFormOpinion() {
            if (this.userInfo.adjustType == '1') {
                this.userInfo.researchId = this.researchId
            } else if (this.userInfo.adjustType == '2') {
                this.userInfo.researchId = this.nodeIdTwo
            }
            // if(this.userInfo.warnEarlyRecordList&&this.userInfo.warnEarlyRecordList.length){
            //     this.$alert('有未处理的预警', '提示', {
            //         confirmButtonText: '知道了',
            //         type: 'warning',
            //         center: true,
            //     })
            //     return
            // }
            let dataObj = {
                followOpinion: this.userInfo.followOpinion,
                adjustType: this.userInfo.adjustType,
                researchId: this.userInfo.researchId,
                recordId: this.recordId,
                patientInfoId: this.patientId,
            }
            if (!dataObj.followOpinion) {
                this.$message.warning('请填写随访意见')
                return
            }
            this.loading = true
            handleOpinion(dataObj).then(response => {
                this.$modal.msgSuccess("提交成功");
                this.loading = false
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        },
        /**
         * 超窗 “更新随访时间”
         */
        submitFormThree() {
            this.$refs["formUpTime"].validate(valid => {
                if (valid) {
                    let dataObj = {
                        recordId: this.recordId,
                        handleType: '1',
                        visitTime: this.formUpTime.visitTime,
                        lossCause: '',
                        lossCauseOther: ''
                    }
                    handleTime(dataObj).then(response => {
                        this.$modal.msgSuccess("更新随访时间成功");
                        this.cancleUserList()
                    })
                }
            })

        },
        /** 单击选中行数据 */
        clickRow(row) {
            this.$refs.table.toggleRowSelection(row);
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.roleIds = selection.map((item) => item.roleId);
        },
        // 保存选中的数据编号
        getRowKey(row) {
            return row.roleId;
        },
        /** 质疑提交按钮 */
        submitForm() {
            let medicalFormIds = this.userInfo.medicalFormVOS && this.userInfo.medicalFormVOS.length ?
                this.userInfo.medicalFormVOS.filter(f => f.checked).map(m => m.medicalFormId) : [];
            if (!medicalFormIds || !medicalFormIds.length) {
                this.$message.warning('至少选择一个模块发起质疑')
                this.challengeOpen = false
                return
            }
            const paramsData = {
                recordId: Number(this.recordId),
                userId: this.$store.state.user.userInfo.userId,
                fromContent: this.fromContent,
                medicalFormIds: medicalFormIds.toString()
            }
            // return 
            this.loading = true
            saveQuestionContent(paramsData).then(response => {
                this.loading = false
                this.challengeOpen = false
                this.fromContent = ''
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
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
        /** 关闭按钮 */
        cancleUserList() {
            const obj = {
                path: this.$route.query.oldPath
            };
            this.$tab.closeOpenPage(obj);
        },
        /** 确认入组按钮操作 */
        assignQueueFollow(handleType) {
            let params = {
                researchId: this.researchId,
                handleType,
                patientId: this.patientId
            }
            let confirmTitle = '';
            if (handleType == 1) {
                confirmTitle = `请确认患者暂不入组？`
            } else if (handleType == 3) {
                confirmTitle = `是否确认将患者加入队列？`
            }
            if (handleType == 1 || handleType == 3) {
                this.$confirm(`${confirmTitle}`, '提示', {
                    type: 'warning'
                }).then(() => {
                    this.patientEnrollment(params)
                }).catch(error => {
                    return
                })
            } else if (handleType == 2 || handleType == 0) {
                this.openModify = true;
                this.getQueueSelect();
                this.enrollmentTitle = handleType == 2 ? "修改确认入组" : (handleType == 0 ? '分配队列' : '');
            }
        },
        // 患者入组方法
        patientEnrollment(params) {
            handleEnrollment(params).then(res => {
                this.$modal.msgSuccess("操作成功");
                this.$tab.closePage(this.$route)
                this.$router.push({
                    path: this.$route.query.oldPath
                })
            })
        },
        /** 入组修改按钮操作 */
        handleModify(row) {
            this.openModify = true;
            this.title = "修改确认入组";
        },
        /** 待分配按钮操作 */
        handleWait(row) {
            this.$modal
                .confirm("请确认患者暂不入组？")
                .then(function () {
                    return cleanOperlog();
                })
                .then(() => {
                    this.$modal.msgSuccess("成功待分配患者组");
                    this.$tab.closePage(this.$route)
                    this.$router.push({
                        path: this.$route.query.oldPath
                    })
                })
                .catch(() => { });
        },
        /** 发起质疑 */
        challenge() {
            let medicalFormIds = this.userInfo.medicalFormVOS && this.userInfo.medicalFormVOS.length ?
                this.userInfo.medicalFormVOS.filter(f => f.checked).map(m => m.medicalFormId) : [];
            if (!medicalFormIds || !medicalFormIds.length) {
                this.$alert('至少选择一个模块发起质疑', '提示', {
                    confirmButtonText: '知道了',
                    type: 'warning',
                    center: true,
                })
                return
            }
            this.challengeOpen = true;
            this.title = '质疑描述'
        },
        /** 快捷回复 */
        changReply() {
            this.openReply = true;
            this.quickReplyTitle = '快捷回复';
            this.getQuickList()
        },
        /** 关闭质疑弹窗 */
        cancel() {
            this.challengeOpen = false;
            this.openUpTime = false;
        },
        /** 更新随访时间 */
        changUpTiem() {
            this.openUpTime = true
            this.title = '更新随访时间'
            this.initDatePicker()
        },
        /** 忽略本次 */
        updateIgnoreReason() {
            let dataObj = {
                recordId: this.recordId,
                handleType: '2',
                visitTime: '',
                lossCause: '',
                lossCauseOther: '',
                ignoreType: this.ignoreReason
            }
            handleTime(dataObj).then(response => {
                this.$modal.msgSuccess("此次随访已忽略成功");
                this.cancleUserList()
            })
        },
        /** 随访 忽略本次 */
        changIgnore(item) {
            this.updateFllowInfo = item
            this.isShowIgnore = true;
        },
        // 
        canleIgnoreReset() {
            this.isShowIgnore = false;
            this.ignoreReason = '0'
        },
        /** 失访 */
        changLost() {
            this.openLost = true
            this.resetLost()
            this.title = '失访'
        },
        /**
         * 失访表单重置
         */
        resetLost() {
            this.seeForm = {
                researchId: undefined
            }
            this.formLost = {
                lossCause: '1',
                lossCauseOther: undefined
            }
            this.resetForm("formLost");
        },
        /**
         * 失访原因
         */
        changeLost(value) {
            if (value == 3) {
                this.flag = true
            } else {
                this.flag = false
                this.formLost.lossCauseOther = ""
            }
        },
        /**
         * 失访
         */
        submitFormLost() {
            let dataObj = {
                recordId: this.recordId,
                handleType: '3',
                visitTime: '',
                lossCause: this.formLost.lossCause,
                lossCauseOther: this.formLost.lossCauseOther
            }
            handleTime(dataObj).then(response => {
                this.$modal.msgSuccess("操作成功");
                this.cancleUserList()
            })
        },
        cancelLost() {
            this.resetLost();
            this.openLost = false
        },
        /**
         * 快捷回复列表
         */
        getQuickList() {
            listQuick().then(response => {
                this.quickList = response.data
            })
        },
        /**
         * 添加快捷回复信息
         */
        handeAdd() {
            let dataParms = {
                quickContent: this.quickForm.quickContent
            }
            addQuick(dataParms).then(response => {
                this.getQuickList()
                this.quickForm.quickContent = ''
            })
        },
        /**
         * 删除快捷回复
         */
        delAlertLi(e) {
            this.$confirm('是否删除？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'danger'
            }).then(() => {
                delQuick(e.questionQuickId).then(response => {
                    this.$modal.msgSuccess('删除成功')
                    this.getQuickList()
                })
            })
        },
        /**
         * 设置当前快捷回复为随访意见
         */
        setQuickContent(item) {
            this.userInfo.followOpinion = `${this.userInfo.followOpinion || ''} ${item.quickContent}`
            this.openReply = false
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.quickSelection = selection
        },
        addQuickMultiple() {
            this.userInfo.followOpinion = `${this.userInfo.followOpinion || ''} ` + this.quickSelection.map(m => m
                .quickContent).join(' ')
            this.openReply = false
        },
        cancleQuickMultiple() {
            this.quickSelection = []
            this.openReply = false
        }
    },
};
</script>

<style lang="scss" scoped>
.display_flex {
    display: flex;
}

.deleteBtn {
    color: #ff9292;
}
::v-deep .patient_info {
    padding: 15px;
    background: rgba(24, 144, 255, 0.05);
    .patient_label {
        color: #333;
        font-weight: bold;
        background: rgba(24, 144, 255, 0.05);
        width: 100px;
    }
    .patient_content {
        background: rgba(24, 144, 255, 0.05);
    }
}
::v-deep .medical_form{
    .patient_label {
        color: #333;
        color: #333;
        font-weight: bold;
        background: rgba(24, 144, 255, 0.05);
    }
}
.option-container {
    height: calc(100vh - 114px);
    color: #333;

    .com-details_title {
        .el-checkbox {
            display: flex;
            align-items: center;
        }
        ::v-deep.el-checkbox__label {
            color: #6379bb;
            font-weight: bold;
            padding-left: 0;
        }
    }
}

.option-detail-scrollbar {
    height: calc(100% - 96px);
}

/** 表单布局 **/
.user_follow_opinion {
    min-height: 100px;
    border: 1px solid #ddd;
    border-radius: 5px;
    padding: 5px 10px;
}

.adjustType_radio_group {
    height: 45px;
    display: flex;
    align-items: center;
}

.el-form-top {
    .el-form-item {
        margin-top: 22px;
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

.childMedicalFormVOS_property {
    display: flex;
}

.el-rate {
    height: 36px;
    line-height: 36px;

    .el-rate__icon {
        font-size: 24px;
        margin-top: 6px;
    }
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

    .warn_earlyList {
        .warn_early_item {
            display: flex;
            flex-direction: row;
            padding: 10px 0;

            .head_img {
                width: 50px;
                height: 50px;
                border-radius: 50%;
            }

            .warn_guide_info {
                margin-left: 15px;
                margin-right: 0;

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

                .center {
                    display: block;
                    padding: 8px 10px;
                    font-size: 14px;
                    margin-top: 10px;
                    border: 1px solid #efefef;
                    border-radius: 5px;

                    .el-image {
                        width: 200px;
                    }
                }
            }

            .flex_end {
                display: flex;
                flex-direction: row-reverse;
            }

            .flex_start {
                flex-direction: row-reverse;
            }
        }

        .early_item_doctor {
            flex-direction: row-reverse;

            .warn_guide_info {
                margin-right: 15px;
                margin-left: 0;
            }
        }
    }
}

.user_detail_buttons {
    margin: 20px 0;
    display: flex;
    justify-content: center;
}

.dialog_footer {
    margin-bottom: 10px;
}

.dialog_cascader {
    width: 100%;
}

.el-input .el-button {
    background: #1890ff;
    border-color: #1890ff;
    color: #fff;
}

.ignore_content {
    font-size: 13px;
    width: 300px;
    margin: 0 auto;

    .ignore_tips {
        text-align: center;
        margin-bottom: 5px;
    }

    .ignore_tit {
        line-height: 40px;
        font-size: 13px;
    }
}
</style>