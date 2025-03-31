<template>
    <div class="app-container">
        <!--部门数据-->
        <el-col :span="6" :xs="24">
            <div class="user_tree_title">随访任务</div>
            <el-scrollbar class="scrollbar_tree" v-loading="isLoadingDept">
                <el-tree :data="deptOptions" class="user_tree_line" :props="defaultProps" highlight-current :filter-node-method="filterNode" ref="tree" :indent="0" accordion @node-click="handleNodeClick" :default-expanded-keys="defaultExpandedKeys" node-key="nodeId" :current-node-key="currentNodeKey">
                    <span class="custom-tree-node" slot-scope="{ data }">
                        <span>{{ data.nodeName ? data.nodeName : '无' }}</span>
                        <span v-if="data.childrenList"> （待处理：{{ data.handleNum ? data.handleNum : '0'}}） </span>
                    </span>
                </el-tree>
            </el-scrollbar>
        </el-col>
        <!--用户数据-->
        <el-col :span="18" :xs="24">
            <el-scrollbar class="fatherBox">
                <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
                    <el-form-item label="患者名称" prop="patientName">
                        <el-input v-model="queryParams.patientName" id="patientName" placeholder="请输入患者名称" clearable style="width: 240px" />
                    </el-form-item>
                    <el-form-item label="患者ID" prop="personId">
                        <el-input v-model="queryParams.personId" id="personId" placeholder="请输入患者ID" clearable style="width: 240px" />
                    </el-form-item>
                    <el-form-item label="是否绑定" prop="bindStatus" v-if="nodeType==1">
                        <el-select v-model="queryParams.bindStatus" clearable class="bind_status_select">
                            <el-option value="1" label="绑定"></el-option>
                            <el-option value="0" label="未绑定"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" @click="handleQuery($route.query)">搜索
                        </el-button>
                        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                    </el-form-item>
                </el-form>

                <div class="mb8">
                    <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                        v-hasPermi="['system:user:export']">导出</el-button> -->
                    <el-button type="warning" plain @click="toRemindEnrollment" v-hasPermi="['system:user:export']" v-if="nodeType==1&&nodeTypeStatus==2" :disabled="!allRemindSms&&!patientIds.length">提醒入组</el-button>
                </div>

                <el-table v-loading="loading" :data="userList" :key="renderKey" @row-click="clickRow" ref="selectTable" class="select_table_style" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55" align="center" v-if="nodeType==1&&nodeTypeStatus==2">
                        <template slot-scope="scope">
                            <el-checkbox v-model="scope.row.checked" disabled class="checkbox_item" />
                        </template>
                    </el-table-column>
                    <el-table-column label="患者ID" align="center" width="110" key="personId" prop="personId" />
                    <el-table-column label="患者名称" align="center" min-width="120" key="patientName" prop="patientName" />
                    <el-table-column :label="projectNameLabel" align="center" key="stage" :show-overflow-tooltip="true" min-width="200" v-if="nodeType==1||nodeType==2||nodeLevelType==2">
                        <template slot-scope="scoped">
                            {{ nodeType==0&&nodeLevelType==2&&nodeTypeStatus>=3?
                            scoped.row.scanProjectName: scoped.row.projectName |nullFormat}}
                        </template>
                    </el-table-column>
                    <el-table-column label="是否绑定" align="center" min-width="120" key="accountId" prop="accountId" v-if="nodeType==1&&nodeTypeStatus==1">
                        <template slot-scope="scope">
                            {{scope.row.accountId>0?'绑定':'未绑定'}}
                        </template>
                    </el-table-column>
                    <el-table-column label="渠道" align="center" min-width="120" key="channelType" prop="channelType" v-if="nodeType==1 && nodeTypeStatus<3">
                        <template slot-scope="scope">
                            {{scope.row.channelType==1?'后台':(scope.row.channelType==2?'扫码':'--')}}
                        </template>
                    </el-table-column>
                    <el-table-column label="阶段" align="center" min-width="120" key="name" prop="name" v-if="nodeType==2||nodeType==3||nodeType==4">
                        <template slot-scope="scope">
                            {{ scope.row.name | nullFormat}}
                        </template>
                    </el-table-column>
                    <el-table-column label="联系电话" align="center" prop="mobile" width="130" v-if="nodeType!=0">
                        <template slot-scope="scope">
                            {{ scope.row.mobile | nullFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="创建时间" align="center" prop="createTime" width="190" v-if="nodeType!=2">
                        <template slot-scope="scope">
                            {{ scope.row.createTime | dateFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="提交时间" align="center" prop="createTime" width="190" v-if="nodeType==2">
                        <template slot-scope="scope">
                            {{ scope.row.followSubmitTime | dateFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="更新时间" align="center" prop="updateTime" width="190" v-if="(nodeType==3||nodeType==4)&&nodeTypeStatus==2">
                        <template slot-scope="scope">
                            {{ scope.row.updateTime | dateFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="处理结果" align="center" prop="enrollmentResultContent" min-width="160" v-if="(nodeType==1||(nodeType==0&&nodeLevelType==2))&&nodeTypeStatus>=3">
                        <template slot-scope="scope">
                            <!-- 入组处理结果 -->
                            <span>{{ scope.row.enrollmentResultContent | nullFormat }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="处理结果" align="center" prop="enrollmentResultContent" min-width="160" v-if="(nodeType==3||(nodeType==0&&nodeLevelType==2))&&nodeTypeStatus>=3">
                        <template slot-scope="scope">
                            <!-- 随访超窗处理结果 -->
                            <span>{{ scope.row.overTimeHandleStatus | convertHandleStatus }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="质疑时间" align="center" prop="questionTime" width="190" v-if="nodeType==2 &&nodeTypeStatus==2">
                        <template slot-scope="scope">
                            {{ scope.row.questionTime | dateFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="拒绝次数" align="center" prop="refuseCount" width="100" v-if="nodeTypeStatus==1&&nodeType==1">
                        <template slot-scope="scope">
                            {{ scope.row.refuseCount | nullFormat}}
                        </template>
                    </el-table-column>
                    <el-table-column label="是否二次提醒" align="center" prop="smsSendCount" width="130" v-if="nodeTypeStatus==2&&nodeType==1">
                        <template slot-scope="scope">
                            {{ scope.row.smsSendCount==1?'是':'否'}}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作时间" align="center" prop="enrollmentTime" width="190" v-if="nodeTypeStatus>=3&&nodeType==1">
                        <template slot-scope="scope">
                            {{ scope.row.enrollmentTime | dateFormat}}
                        </template>
                    </el-table-column>
                    <el-table-column label="完成时间" align="center" prop="mobile" width="190" v-if="nodeTypeStatus>=3&&nodeType==2">
                        <template slot-scope="scope">
                            <!-- 随访完成时间 -->
                            {{ scope.row.followCompleteTime | dateFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="完成时间" align="center" prop="mobile" width="190" v-if="nodeTypeStatus>=3&&nodeType==3">
                        <template slot-scope="scope">
                            <!-- 超窗完成时间 -->
                            {{ scope.row.overTimeHandleTime | dateFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="智能外呼结果" align="center" prop="enrollmentResultContent" min-width="160" v-if="nodeType==3&&nodeTypeStatus<3">
                        <template slot-scope="scope">
                            {{ scope.row.enrollmentResultContent | nullFormat }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="220">
                        <template slot-scope="scope">
                            <el-button type="text" v-if="nodeType==1||nodeLevelType==2" @click.stop="toHealthrecordDetails(scope.row)">
                                查看健康档案
                            </el-button>
                            <el-button type="text" v-if="nodeType==1&&nodeTypeStatus==2" :disabled="scope.row.smsSendCount==1" @click.stop="toRemindEnrollment(scope.row)">
                                提醒入组
                            </el-button>
                            <el-button type="text" v-if="nodeType==1&&nodeTypeStatus==3&&scope.row.enrollmentHandlerStatus==3&&scope.row.enrollmentStatus==0&&scope.row.refuseCount!=2" @click="assignQueueFollow('3',scope.row)" v-hasPermi="['follow:patient_enrollment:handle']">重新入组</el-button>
                            <el-button type="text" v-hasPermi="['follow:patient_enrollment:handle']" v-if="(nodeType==0&&nodeLevelType==2)&&nodeTypeStatus<3" @click="assignQueue(scope.row)">
                                分配队列
                            </el-button>
                            <el-button type="text" @click="toFollowDetails(scope.row)" v-if="nodeType==2||nodeType==3||nodeType==4&&nodeTypeStatus>=3">
                                查看随访
                            </el-button>
                            <el-button type="text" v-if="scope.row.outboundId" @click="toOutboundDetails(scope.row)">
                                查看外呼
                            </el-button>
                            <el-button type="text" @click="showUpdateDialog(scope.row)" v-hasPermi="['follow:over_time:handle']" v-if="nodeType==3&&nodeTypeStatus<3">
                                更新随访时间
                            </el-button>
                            <el-button type="text" v-hasPermi="['follow:over_time:handle']" class="el-button-text_warning" @click="changIgnore(scope.row)" v-if="nodeType==3&&nodeTypeStatus<3">
                                忽略本次
                            </el-button>
                            <el-button type="text" v-hasPermi="['follow:over_time:handle']" class="el-button-text_delete" @click="showLostVisitDialog(scope.row)" v-if="nodeType==3&&nodeTypeStatus<3">失访
                            </el-button>
                            <el-button type="text" v-if="nodeType==4&&nodeTypeStatus<3" @click="toFollowDetails(scope.row)">预警处理
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="handleQuery($route.query)" />
            </el-scrollbar>
        </el-col>
        <!-- 更新随访时间 -->
        <el-dialog :title="title" :visible.sync="isShowChangeTime" width="600px" center>
            <el-form label-width="120px">
                <el-form-item label="随访时间" prop="visitTime">
                    <el-date-picker v-model="updateFollowTimeStr" type="date" placeholder="选择日期" :picker-options="datePickerOptions">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button type="primary" @click="updateFollowInfo('1')">确 定</el-button>
                <el-button @click="cancelUpdateTime">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 失访 -->
        <el-dialog :title="title" :visible.sync="isShowLostVisitDialog" width="600px" center>
            <el-alert title="失访后患者将不再继续该队列的随访!" type="error"></el-alert>
            <div style="font-size:16px;font-weight:bold;line-height:40px;text-align:center"></div>
            <el-form ref="formLost" :model="formLost" label-width="90px">
                <el-form-item label="失访原因：" prop="lossCause">
                    <el-radio-group v-model="formLost.lossCause" @change="changeLost">
                        <el-radio v-for="(item, index) in lostList" :key="index" :label="item.value">
                            {{ item.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="其他原因：" prop="lossCauseOther" v-if="formLost.lossCause==3">
                    <el-input type="textarea" :rows="3" v-model="formLost.lossCauseOther" placeholder="请输入内容" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button @click="cancelLost">取 消</el-button>
                <el-button type="primary" @click="updateFollowLostVisit()">确 定</el-button>
            </div>
        </el-dialog>
        <!-- 分配 队列 -->
        <el-dialog title="分配队列" :visible.sync="isShowAssignQueue" width="500px" center>
            <el-form ref="assignQueueForm" :model="assignQueueForm" label-width="120px">
                <el-form-item label="选择病种队列：" prop="researchId">
                    <el-cascader v-model="assignQueueForm.researchId" :options="followQueueList" class="dialog_cascader" :props="{value: 'nodeId', label: 'nodeName', children: 'researchDiseasesSelects'}" :show-all-levels="false" clearable @change="selectFollowQueue"></el-cascader>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog_footer">
                <el-button @click="handleSeeForm" type="primary">确 认</el-button>
                <el-button @click="canleSeeForm">取 消</el-button>
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
                <el-button @click="updateFollowInfo('2')" type="primary">确 认</el-button>
                <el-button @click="canleIgnoreReset">取 消</el-button>
            </div>
        </el-dialog>
        <!-- 提醒入组 -->
        <el-dialog title="短信提醒" :visible.sync="isShowMSGRemind" width="700px" center>
            <div v-if="!currentPatientInfo||currentPatientInfo.accountId==0">
                <h4>{{!currentPatientInfo?'app未绑定患者':''}}模板内容：</h4>
                <div style="text-indent: 20px;">
                    <p>获取专业慢病管理服务。期待您的参与！</p>
                    <p>尊敬的“{{currentPatientInfo?currentPatientInfo.patientName:'XXX'}}”，欢迎您加入唐都慢病管理平台：</p>
                    <p>1.微信关注“唐都医院”公众号；</p>
                    <p>2.进入“诊疗服务”选择“慢病管理”；</p>
                    <p>3.输入患者信息完成绑定，即可享受专业的慢病管理服务。唐都医院为您的健康保驾护航！</p>
                </div>
            </div>
            <div class="mt10" v-if="!currentPatientInfo||currentPatientInfo.accountId!=0">
                <h4>{{!currentPatientInfo?'app已绑定患者':''}}模板内容：</h4>
                <div style="text-indent: 20px;">
                    <p>尊敬的“{{currentPatientInfo?currentPatientInfo.patientName:'XXX'}}”，欢迎加入唐都慢病管理平台，</p>
                    <p>您被邀请加入“{{currentPatientInfo?currentPatientInfo.projectName:'XXX'}}”队列，</p>
                    <p>请前往慢病管理患者端，点击个人中心，查看邀请消息，加入队列享受专业的慢病管理服务</p>
                </div>
            </div>
            <div slot="footer" class="dialog_footer">
                <el-button @click="sendMsgTips" type="primary">确 认</el-button>
                <el-button @click="canleMSGTips">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listTree,
    listEnrollment, //入组列表
    listHandle, //处理列表
    listOver, //超窗列表
    listWarn, //预警列表
    listAllocated, //待分配列表
    handleTime, //超窗处理
    selectResearch, //病种及队列下拉框
    handleEnrollment, //确认入组/修改入组/待分配入组
    sendEnrollmentWalkup,
} from "@/api/follow/work";
import moment from 'moment'
export default {
    name: "workstation-index",
    data() {
        return {
            // 遮罩层
            loading: false,
            // 选中数组 ·
            researchId: '',
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 弹出层标题
            title: "",
            userList: [],
            deptOptions: [],
            isLoadingDept: false,
            //   flag: false,
            // 部门名称
            nodeName: undefined,
            queryParams: {
                pageSize: 10,
                pageNum: 1,
                bindStatus: undefined,
            },
            defaultProps: {
                children: "childrenList",
                label: "nodeName",
                key: 'nodeId'
            },
            lostList: [
                { value: '1', label: '不再参与' },
                { value: '2', label: '失联' },
                { value: '3', label: '其他' },
            ],
            // 表单校验
            rules: {
                patientName: [
                    { required: true, message: "用户名称不能为空", trigger: "blur" },
                    { min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur" },
                ],
                nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
                password: [
                    { required: true, message: "用户密码不能为空", trigger: "blur" },
                    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
                ],
                email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
                patientId: [
                    { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
                ],
            },
            nodeTypeStatus: '',
            defaultExpandedKeys: [],
            currentNodeKey: '',
            nodeType: '',
            nodeLevelType: '',
            projectNameLabel: '计划加入队列', //计划加入队列和队列
            isShowChangeTime: false, //是否显示更新随访时间的弹框
            updateFollowTimeStr: '',
            updateFllowInfo: {}, //更新随访信息
            isShowLostVisitDialog: false, //是否显示失访弹框
            formLost: {}, //失访信息
            isShowAssignQueue: false, //是否显示分配队列
            assignQueueForm: {}, //分配队列
            followQueueList: [], //队列列表
            renderKey: new Date() + '',
            assignQueueInfo: {},
            isShowIgnore: false,
            ignoreReason: '0',
            datePickerOptions: undefined,
            isShowMSGRemind: false,
            currentPatientInfo: {},
            allRemindSms: false,
            checked: false,
            patientIds: [],
        };
    },
    watch: {
        // 根据名称筛选部门树
        nodeName(val) {
            this.$refs.tree.filter(val);
        },
    },
    created() {
        this.getTreeselect();
        const queryDaya = this.$route.query
        if (queryDaya.defaultExpandedKeys) {
            this.defaultExpandedKeys = queryDaya.defaultExpandedKeys ? queryDaya.defaultExpandedKeys.split(',') : []
            this.currentNodeKey = this.defaultExpandedKeys[this.defaultExpandedKeys.length - 1]
            this.researchId = queryDaya.researchId || ''
            this.nodeLevelType = queryDaya.nodeLevelType
            this.nodeType = queryDaya.nodeType
            this.handleQuery({
                nodeType: queryDaya.nodeType,
                nodeTypeStatus: queryDaya.nodeTypeStatus,
                researchId: queryDaya.researchId,
                nodeLevelType: queryDaya.nodeLevelType
            });
        }
        if (this.nodeType == 1 || this.nodeLevelType == 2) {
            this.projectNameLabel = '计划加入队列'
        } else {
            this.projectNameLabel = '队列'
        }
    },
    methods: {
        clickRow(row) {
            if (!row.smsSendCount || row.smsSendCount < 2) {
                this.$refs.selectTable.toggleRowSelection(row);
            }
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.patientIds = selection.map(item => item.patientId);
            this.single = selection.length != 1;
            this.multiple = !selection.length;
            this.userList.forEach((element, index) => {
                if (selection.findIndex(f => f.checkedKey == element.checkedKey) > -1) {
                    this.$set(this.userList[index], 'checked', true)
                } else {
                    this.$set(this.userList[index], 'checked', false)
                }
            })
        },
        /** 查询入组列表 */
        getListEnrollment(researchId, nodeTypeStatus) {
            this.loading = true;
            const queryData = {
                enrollmentHandlerStatus: nodeTypeStatus,
                ...this.queryParams
            }
            if (researchId) {
                queryData['researchId'] = researchId
            }
            listEnrollment(queryData).then(
                (response) => {
                    this.renderKey = moment().valueOf() + '';
                    let resData = response.rows
                    let list = []
                    if (resData && resData.length) {
                        this.allRemindSms = resData.findIndex(f => f.smsSendCount >= 2) > -1
                        list = resData
                        list.forEach((element, index) => {
                            element.checked = false;
                            element.checkedKey = `${element.patientId}${index}`
                        })
                    }
                    this.userList = list;
                    this.loading = false;
                    this.total = response.total || 0
                }
            );
        },
        /** 查询处理列表 */
        getListHandle(researchId, followStatus) {
            this.loading = true;
            const queryData = {
                followStatus: followStatus,
                ...this.queryParams
            }
            if (researchId) {
                queryData['researchId'] = researchId
            }
            listHandle(queryData).then((response) => {
                this.userList = response.rows;
                this.loading = false;
                this.total = response.total || 0
            }
            );
        },
        /** 查询超窗列表 */
        getListOver(researchId, overTimeStatus) {
            this.loading = true;
            const queryData = {
                overTimeStatus: overTimeStatus,
                ...this.queryParams
            }
            if (researchId) {
                queryData['researchId'] = researchId
            }
            listOver(queryData).then(
                (response) => {
                    this.userList = response.rows;
                    this.loading = false;
                    this.total = response.total || 0
                }
            );
        },
        /** 查询预警 */
        getListWarn(researchId, warnStatus) {
            this.loading = true;
            const queryData = {
                warnStatus: warnStatus,
                ...this.queryParams
            }
            if (researchId) {
                queryData['researchId'] = researchId
            }
            listWarn(queryData).then(
                (response) => {
                    this.userList = response.rows;
                    this.loading = false;
                    this.total = response.total || 0
                }
            );
        },
        /** 查询未分配列表 */
        getlistAllocated(researchId, allocatedHandlerStatus) {
            this.loading = true;
            const queryData = {
                allocatedHandlerStatus: allocatedHandlerStatus,
                ...this.queryParams
            }
            if (researchId) {
                queryData['researchId'] = researchId
            }
            listAllocated(queryData).then((response) => {
                this.userList = response.rows;
                this.loading = false;
                this.total = response.total || 0
            }).catch(error => {
                this.loading = false;
            });
        },
        /** 查询部门下拉树结构 */
        getTreeselect() {
            this.isLoadingDept = true;
            listTree().then((response) => {
                this.deptOptions = response.data.childrenList.filter(f => f.childrenList && f.childrenList
                    .length);
                this.isLoadingDept = false
            }).catch(error => {
                this.isLoadingDept = false
            });
        },
        // 筛选节点
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        // 节点单击事件
        handleNodeClick(data, node) {
            this.defaultExpandedKeys = []
            this.currentNodeKey = node.data.nodeId
            this.convertDefaultKeys(node)
            this.handleQuery(data);
            this.researchId = data.researchId
            if (this.nodeType == 1 || this.nodeLevelType == 2) {
                this.projectNameLabel = '计划加入队列'
            } else {
                this.projectNameLabel = '队列'
            }
            this.$router.push({
                path: this.$route.path,
                query: {
                    defaultExpandedKeys: this.defaultExpandedKeys.toString(),
                    nodeTypeStatus: this.nodeTypeStatus,
                    nodeType: this.nodeType,
                    researchId: this.researchId,
                    nodeLevelType: this.nodeLevelType
                }
            })
        },
        //添加当前组件选中的keys值
        convertDefaultKeys(node) {
            if (node.level > 0) {
                this.defaultExpandedKeys.unshift(node.data.nodeId)
                this.convertDefaultKeys(node.parent)
            }
        },
        /**
         * 获取队列
         */
        getQueueSelect() {
            selectResearch().then(response => {
                let valueArr = response.data
                this.followQueueList = valueArr.filter(f => f.researchDiseasesSelects)
            })
        },
        /**
         * 选择分组
         */
        selectFollowQueue(val) {
            let arr = val.slice(1, 2).toString();
            this.nodeIdTwo = arr
        },
        /**
         * 确认分配队列
         */
        handleSeeForm() {
            if (!this.nodeIdTwo) {
                this.$message.warning('请选择病重队列')
                return
            }
            handleEnrollment({
                handleType: '2',
                researchId: this.nodeIdTwo,
                patientId: this.assignQueueInfo.patientId
            }).then(response => {
                this.$modal.msgSuccess("确认分配队列成功");
                this.handleQuery(this.$route.query)
                this.isShowAssignQueue = false;
            })
        },
        /**
         * 取消分配队列弹窗
         */
        canleSeeForm() {
            this.isShowAssignQueue = false
        },
        // 分配队列
        assignQueue(item) {
            this.isShowAssignQueue = true;
            this.assignQueueInfo = item
            this.getQueueSelect();
        },
        // 
        toHealthrecordDetails(item) {
            this.$router.push({
                path: "/workstation/patients/opinionDetail",
                query: {
                    type: this.nodeType,
                    patientId: item.patientId,
                    researchId: this.researchId,
                    recordId: item.recordId,
                    nodeTypeStatus: this.nodeTypeStatus,
                    hideQueueTree: true,
                    refuseCount: item.refuseCount,
                    oldPath: `${this.$route.path}?defaultExpandedKeys=${this.defaultExpandedKeys.toString()}&nodeTypeStatus=${this.nodeTypeStatus}&nodeType=${this.nodeType}&researchId=${this.researchId}&nodeLevelType=${this.nodeLevelType}`
                }
            });
        },
        toRemindEnrollment(item) {
            this.isShowMSGRemind = true
            if (item.patientId) {
                this.currentPatientInfo = item
            } else {
                this.currentPatientInfo = undefined
            }

        },
        sendMsgTips() {
            this.isShowMSGRemind = false
            let patientId = undefined
            if (this.currentPatientInfo) {
                patientId = this.currentPatientInfo.patientId
            } else if (this.patientIds && this.patientIds.length) {
                patientId = this.patientIds.toString()
            }
            let params = {
                patientIds: patientId
            }
            sendEnrollmentWalkup(params).then(res => {
                this.$message.success('发送成功')
                this.handleQuery(this.$route.query)
            })
        },
        canleMSGTips() {
            this.isShowMSGRemind = false
        },
        /** 确认入组按钮操作 */
        assignQueueFollow(handleType, rows) {
            let params = {
                researchId: this.researchId,
                handleType,
                patientId: rows.patientId
            }
            let confirmTitle = '';
            if (handleType == 1) {
                confirmTitle = `请确认患者暂不入组？`
            } else if (handleType == 3 && rows.accountId == 0) {
                confirmTitle = `<p>稍后会给“${rows.patientName}”发送短信，短信内容为:</p>
                <p>尊敬的“${rows.patientName}”，欢迎您加入唐都慢病管理平台：</p>
                <p>1.微信关注“唐都医院”公众号；</p>
                <p>2.进入“诊疗服务”选择“慢病管理”；</p>
                <p>3.输入患者信息完成绑定，即可享受专业的慢病管理服务。</p>
                <p>唐都医院为您的健康保驾护航！</p>
                <p>点击确定即发送短信，无需发送请点击取消。</p>`
            } else if (handleType == 3 && rows.accountId != 0) {
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
            }
        },
        // 患者入组方法
        patientEnrollment(params) {
            handleEnrollment(params).then(res => {
                this.$modal.msgSuccess("操作成功");
                this.handleQuery(this.$route.query)
            })
        },
        // 随访意见
        toFollowDetails(item) {
            // console.log('item', item, this.nodeTypeStatus)
            this.$router.push({
                path: "/workstation/user/opinionDetail",
                query: {
                    type: this.nodeType,
                    patientId: item.patientId,
                    personId: item.personId,
                    researchId: this.researchId,
                    recordId: item.recordId,
                    nodeTypeStatus: this.nodeTypeStatus,
                    oldPath: `${this.$route.path}?defaultExpandedKeys=${this.defaultExpandedKeys.toString()}&nodeTypeStatus=${this.nodeTypeStatus}&nodeType=${this.nodeType}&researchId=${this.researchId}&nodeLevelType=${this.nodeLevelType}`
                }
            });
        },
        toOutboundDetails(item) {
            this.$router.push({
                path: "/outbound/details",
                query: {
                    outboundId: item.outboundId,
                }
            });
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
        // 显示失访弹窗
        showLostVisitDialog(item) {
            this.isShowLostVisitDialog = true
            this.updateFllowInfo = item
        },
        // 随访 失访
        updateFollowLostVisit() {
            this.updateFollowInfo('3')
        },
        // 取消随访失访
        cancelLost() {
            this.updateFllowInfo = {}
            this.formLost = {
                lossCause: '',
                lossCauseOther: ''
            }
            this.isShowLostVisitDialog = false
        },
        // 显示更新随访时间弹窗
        showUpdateDialog(item) {
            this.isShowChangeTime = true
            this.updateFllowInfo = item
            this.initDatePicker()
        },
        // 更新随访时间disabled
        initDatePicker() {
            this.datePickerOptions = {
                'disabledDate': time => {
                    return moment(time).valueOf() < moment().valueOf()
                }
            }
        },
        // 更新随访时间
        updateFollowInfo(handleType) {
            let params = {
                recordId: this.updateFllowInfo.recordId,
                handleType: handleType, //处理类型1更新随访时间2忽略本次3失访
            }
            let successTitle = '更新随访时间成功'
            if (handleType == 1 && !this.updateFollowTimeStr) {
                this.$message.warning('请选择更新随访时间')
                return
            } else if (handleType == 3 && !this.formLost.lossCause) {
                this.$message.warning('失访原因不能为空')
                return
            }
            if (handleType == 2) {
                params['visitTime'] = this.updateFollowTimeStr;
                params['ignoreType'] = this.ignoreReason
                successTitle = '此次随访已忽略成功'
            } else if (handleType == 3) {
                params['lossCause'] = this.formLost.lossCause; //失访原因1不在参与2失联3其他
                params['lossCauseOther'] = this.formLost.lossCauseOther; //失访原因其他
                successTitle = '操作成功'
            }
            handleTime(params).then(response => {
                this.$modal.msgSuccess(`${successTitle}`);
                this.isShowChangeTime = false;
                this.isShowLostVisitDialog = false;

                this.handleQuery({
                    nodeType: this.nodeType,
                    nodeTypeStatus: this.nodeTypeStatus,
                    researchId: this.researchId,
                    nodeLevelType: this.nodeLevelType
                });
            })
        },
        // 取消更新随访时间
        cancelUpdateTime() {
            this.isShowChangeTime = false;
            this.updateFllowInfo = {}
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                patientId: undefined,
                deptId: undefined,
                patientName: undefined,
                nickName: undefined,
                password: undefined,
                patientId: undefined,
                email: undefined,
                sex: undefined,
                status: "0",
                remark: undefined,
                postIds: [],
                roleIds: [],
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery(data) {
            if (!data.nodeTypeStatus) {
                data = this.$route.query
            }
            data.researchId = !data.researchId || data.researchId == 'null' ? '' : data.researchId
            this.nodeTypeStatus = data.nodeTypeStatus
            this.nodeType = data.nodeType
            this.nodeLevelType = data.nodeLevelType
            if (this.nodeType != 1) {
                this.queryParams.bindStatus = undefined
            }

            if (data.nodeTypeStatus == 0) {
                this.userList = []
                return
            }
            if (data.nodeType == '0' && data.nodeLevelType == '2') {
                this.getlistAllocated(data.researchId, data.nodeTypeStatus)
            } else if (data.nodeType == '1' && data.nodeLevelType != '0') {
                this.getListEnrollment(data.researchId, data.nodeTypeStatus)
            } else if (data.nodeType == "2" && data.nodeLevelType != '0') {
                this.getListHandle(data.researchId, data.nodeTypeStatus)
            } else if (data.nodeType == "3" && data.nodeLevelType != '0') {
                this.getListOver(data.researchId, data.nodeTypeStatus)
            } else if (data.nodeType == "4" && data.nodeLevelType != '0') {
                this.getListWarn(data.researchId, data.nodeTypeStatus);
            }
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.dateRange = [];
            this.resetForm("queryForm");
            this.handleQuery(this.$route.query);
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download(
                "system/user/export", {
                ...this.queryParams,
            },
                `user_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>

<style lang="scss" scoped>
.custom-tree-node {
    font-size: 14px;
}

.fatherBox {
    height: calc(100vh - 180px);
    margin-left: 10px;
    .bind_status_select {
        width: 120px;
    }
}

/* element滚动条组件 隐藏水平滚动条 */

.is-horizontal {
    display: none;
}

.scrollbar_tree {
    height: calc(100vh - 194px);
}

.user_tree_title {
    height: 40px;
    font-size: 16px;
    font-weight: bold;
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
::v-deep .checkbox_item{
    .el-checkbox__inner{
        background-color: #fff;
    }
    &.is-disabled{
        .el-checkbox__input{
            cursor: pointer;
        }
        .el-checkbox__inner{
            cursor: pointer;
            &::after{
                border-color:#fff;
            }
        }
    }
    &.is-disabled.is-checked{
        .el-checkbox__inner{
            cursor: pointer;
            background-color: #1890ff;
            border-color: #1890ff;
            &::after{
                cursor: pointer;
                border-color:#fff;
            }
        }
    }
}
</style>