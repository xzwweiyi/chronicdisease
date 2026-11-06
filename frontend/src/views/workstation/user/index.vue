<template>
  <div class="app-container">
    <!--部门数据-->
    <el-col :span="6" :xs="24">
      <div class="user_tree_title">随访任务</div>
      <div class="tree_search_box">
        <el-input
          v-model="treeQuery.diseasesName"
          placeholder="请输入病种名称"
          clearable
          @keyup.enter.native="handleTreeSearch"
          @clear="resetTreeSearch"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleTreeSearch"></el-button>
        </el-input>
      </div>
      <el-scrollbar class="scrollbar_tree" v-loading="isLoadingDept">
        <el-tree
          :data="deptOptions"
          class="user_tree_line"
          :props="treeProps"
          highlight-current
          :filter-node-method="filterNode"
          ref="tree"
          :indent="0"
          accordion
          @node-click="handleNodeClick"
          :default-expanded-keys="defaultExpandedKeys"
          node-key="nodeId"
          :current-node-key="currentNodeKey"
          lazy
          :load="loadNode"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ data.nodeName ? data.nodeName : "无" }}</span>
            <!-- 根节点显示"待处理" -->
            <span v-if="!node.parent || node.parent.level === 0"> （待处理：{{ data.handleNum ? data.handleNum : "0" }}） </span>
            <!-- 基线节点显示"未完成" -->
            <span v-else-if="data.nodeType == '5' && data.childrenList"> （未完成：{{ data.handleNum ? data.handleNum : "0" }}） </span>
            <!-- 其他节点显示"待处理"，但排除患者退出列表审核 -->
            <span v-else-if="data.nodeType !== '5' && data.childrenList"> （待处理：{{ data.handleNum ? data.handleNum : "0" }}） </span>
          </span>
        </el-tree>
      </el-scrollbar>
    </el-col>

    <!--用户数据-->
    <el-col :span="18" :xs="24">
      <el-scrollbar class="fatherBox">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
            
          <el-form-item label="患者ID" prop="personId">
            <el-input v-model="queryParams.personId" id="personId" placeholder="请输入患者ID" clearable style="width: 240px" />
          </el-form-item>
          <el-form-item label="阶段" prop="stageName" v-if="nodeType == 2 || nodeType == 3 || nodeType == 4">
            <el-select v-model="queryParams.stageName" clearable>
              <el-option v-for="item in stageList" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="入组时间" prop="" v-if="nodeType == 2 || nodeType == 3 || nodeType == 5">
            <el-date-picker
              v-model="enrollmentDateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
            ></el-date-picker>
          </el-form-item>
          <el-form-item v-if="showEditFollow" label="填写状态" prop="fillStatus">
            <el-select v-model="queryParams.fillStatus" clearable class="bind_status_select">
              <el-option value="1" label="已填写"></el-option>
              <el-option value="0" label="未填写"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否绑定" prop="bindStatus" v-if="nodeType == 1">
            <el-select v-model="queryParams.bindStatus" clearable class="bind_status_select">
              <el-option value="1" label="绑定"></el-option>
              <el-option value="0" label="未绑定"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery1($route.query)">搜索 </el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <div class="mb8">
          <!-- <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                        v-hasPermi="['system:user:export']">导出</el-button> -->
          <el-button
            type="warning"
            plain
            @click="toRemindEnrollment"
            v-hasPermi="['system:user:export']"
            v-if="nodeType == 1 && nodeTypeStatus == 2"
            :disabled="!allRemindSms && !patientIds.length"
            >提醒入组</el-button
          >
        </div>

        <el-table
          v-loading="loading"
          :data="userList"
          :key="renderKey"
          @row-click="clickRow"
          ref="selectTable"
          class="select_table_style"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" v-if="nodeType == 1 && nodeTypeStatus == 2">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.checked" disabled class="checkbox_item" />
            </template>
          </el-table-column>
          <el-table-column label="患者ID" align="center" width="110" key="personId" prop="personId" />
          <el-table-column
            label="表单编码"
            align="center"
            min-width="120"
            key="name"
            prop="name"
            v-if="(nodeType == 2 || nodeType == 3 || nodeType == 4) && showFlag"
          >
            <template slot-scope="scope">
              {{ (scope.row.patientCode + "-" + scope.row.stageAddNum) | nullFormat }}
            </template>
          </el-table-column>
          <el-table-column label="患者编码" align="center" width="110" key="patientCode" prop="patientCode" v-if="showFlag" />
          <el-table-column label="患者名称" align="center" min-width="140" key="patientName" prop="patientName">
            <template slot-scope="scope">
              {{ scope.row.patientName | nullFormat }}
              <el-tag v-if="scope.row.focusStatus == '1'" type="danger" size="mini" effect="dark" style="margin-left: 4px;">重点</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="身份证号" align="center" min-width="120" key="cardId" prop="cardId" />
          <el-table-column label="申请时间" align="center" min-width="120" key="quitTime" prop="quitTime" v-if="nodeType == 6" />
          <el-table-column label="申请原因" align="center" min-width="120" key="quitReason" prop="quitReason" v-if="nodeType == 6" />
          <el-table-column
            label="审批状态"
            align="center"
            min-width="120"
            key="isAgree"
            prop="isAgree"
            v-if="nodeType == 6 && nodeTypeStatus == 3"
          >
            <template slot-scope="scoped">
              <span v-if="scoped.row.isAgree">同意</span>
              <el-popover
                class="item"
                :content="scoped.row.rejectReason"
                placement="top"
                :disabled="!scoped.row.rejectReason"
                width="300"
                trigger="hover"
              >
                <span slot="reference" v-if="!scoped.row.isAgree">驳回</span>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            :label="projectNameLabel"
            align="center"
            key="stage"
            :show-overflow-tooltip="true"
            min-width="200"
            v-if="nodeType == 1 || nodeType == 2 || nodeLevelType == 2"
          >
            <template slot-scope="scoped">
              {{
                nodeType == 0 && nodeLevelType == 2 && nodeTypeStatus >= 3
                  ? scoped.row.scanProjectName
                  : scoped.row.projectName | nullFormat
              }}
            </template>
          </el-table-column>
          <el-table-column label="是否绑定" align="center" min-width="120" key="accountId" prop="accountId" v-if="nodeType == 1">
            <template slot-scope="scope">
              {{ scope.row.accountId > 0 ? "绑定" : "未绑定" }}
            </template>
          </el-table-column>
          <el-table-column
            label="渠道"
            align="center"
            min-width="120"
            key="channelType"
            prop="channelType"
            v-if="nodeType == 1 && nodeTypeStatus < 3"
          >
            <template slot-scope="scope">
              {{ scope.row.channelType == 1 ? "后台" : scope.row.channelType == 2 ? "扫码" : "--" }}
            </template>
          </el-table-column>
          <el-table-column
            label="阶段"
            align="center"
            min-width="120"
            key="name"
            prop="name"
            v-if="nodeType == 2 || nodeType == 3 || nodeType == 4"
          >
            <template slot-scope="scope">
              {{ scope.row.name | nullFormat }}
            </template>
          </el-table-column>
          <el-table-column label="联系电话" align="center" prop="mobile" width="130" v-if="nodeType != 0 && nodeType != 6">
            <template slot-scope="scope">
              {{ scope.row.mobile | nullFormat }}
            </template>
          </el-table-column>
          <el-table-column
            label="入组时间"
            align="center"
            prop="enrollmentTime"
            width="190"
            v-if="nodeType == 2 || nodeType == 3 || nodeType == 5"
          >
            <template slot-scope="scope">
              {{ scope.row.enrollmentTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="190" v-if="nodeType != 2 && nodeType != 6">
            <template slot-scope="scope">
              {{ scope.row.createTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="超窗时间" align="center" prop="createTime" width="190" v-if="showChaoTime">
            <template slot-scope="scope">
              {{ scope.row.overTimeCreateTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="原随访时间" align="center" prop="visitTime" width="130" v-if="nodeType == 3 && nodeTypeStatus < 3">
            <template slot-scope="scope">
              {{ scope.row.visitTime | dateOnlyFormat }}
            </template>
          </el-table-column>
          <el-table-column label="更新后随访时间" align="center" prop="lockVisitTime" width="150" v-if="nodeType == 3 && nodeTypeStatus < 3">
            <template slot-scope="scope">
              <span v-if="scope.row.overTimeHandleStatus == '1'" style="color: #409EFF; font-weight: 600;">{{ scope.row.lockVisitTime | dateOnlyFormat }}</span>
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column
            label="提交时间"
            align="center"
            prop="createTime"
            width="190"
            v-if="(nodeType == 2 || nodeType == 5) && nodeTypeStatus == 1"
          >
            <template slot-scope="scope">
              {{ scope.row.followSubmitTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column
            label="更新时间"
            align="center"
            prop="updateTime"
            width="190"
            v-if="(nodeType == 3 || nodeType == 4) && nodeTypeStatus == 2"
          >
            <template slot-scope="scope">
              {{ scope.row.updateTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column
            label="处理结果"
            align="center"
            prop="enrollmentResultContent"
            min-width="160"
            v-if="(nodeType == 1 || (nodeType == 0 && nodeLevelType == 2)) && nodeTypeStatus >= 3"
          >
            <template slot-scope="scope">
              <!-- 入组处理结果 -->
              <span>{{ scope.row.enrollmentResultContent | nullFormat }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="处理结果"
            align="center"
            prop="enrollmentResultContent"
            min-width="160"
            v-if="(nodeType == 3 || (nodeType == 0 && nodeLevelType == 2)) && nodeTypeStatus >= 3"
          >
            <template slot-scope="scope">
              <!-- 随访超窗处理结果 -->
              <span>{{ scope.row.overTimeHandleStatus | convertHandleStatus }}</span>
            </template>
          </el-table-column>
          <el-table-column label="质疑时间" align="center" prop="questionTime" width="190" v-if="nodeType == 2 && nodeTypeStatus == 2">
            <template slot-scope="scope">
              {{ scope.row.questionTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="拒绝次数" align="center" prop="refuseCount" width="100" v-if="nodeTypeStatus == 1 && nodeType == 1">
            <template slot-scope="scope">
              {{ scope.row.refuseCount | nullFormat }}
            </template>
          </el-table-column>
          <el-table-column label="是否二次提醒" align="center" prop="smsSendCount" width="130" v-if="nodeTypeStatus == 2 && nodeType == 1">
            <template slot-scope="scope">
              {{ scope.row.smsSendCount == 1 ? "是" : "否" }}
            </template>
          </el-table-column>
          <el-table-column label="操作时间" align="center" prop="enrollmentTime" width="190" v-if="nodeTypeStatus >= 3 && nodeType == 1">
            <template slot-scope="scope">
              {{ scope.row.enrollmentTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="完成时间" align="center" prop="mobile" width="190" v-if="nodeTypeStatus >= 3 && nodeType == 2">
            <template slot-scope="scope">
              <!-- 随访完成时间 -->
              {{ scope.row.followCompleteTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column label="完成时间" align="center" prop="mobile" width="190" v-if="nodeTypeStatus >= 3 && nodeType == 3">
            <template slot-scope="scope">
              <!-- 超窗完成时间 -->
              {{ scope.row.overTimeHandleTime | dateFormat }}
            </template>
          </el-table-column>
          <el-table-column
            label="智能外呼结果"
            align="center"
            prop="enrollmentResultContent"
            min-width="160"
            v-if="nodeType == 3 && nodeTypeStatus < 3"
          >
            <template slot-scope="scope">
              {{ scope.row.enrollmentResultContent | nullFormat }}
            </template>
          </el-table-column>

          <el-table-column
            label="答题率"
            align="center"
            prop="createTime"
            width="190"
            v-if="(nodeType == 2 || nodeType == 5) && nodeTypeStatus == 1"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.formComplentRate" style="font-weight: 600">{{ scope.row.formComplentRate }}%</span>
              <span v-else>--</span>
            </template>
          </el-table-column>

          <!-- 操作列：所有按钮统一使用 @click.stop 阻止事件冒泡到 @row-click，避免触发行选择等意外行为 -->
          <el-table-column label="操作" align="center" fixed="right" width="220">
            <template slot-scope="scope">
              <el-button type="text" v-if="nodeType == 6 && nodeTypeStatus == 1" @click.stop="handleAduit(scope.row, false)">
                驳回
              </el-button>
              <el-button type="text" v-if="nodeType == 6 && nodeTypeStatus == 1" @click.stop="handleAduit(scope.row, true)">
                同意
              </el-button>
              <el-button
                type="text"
                v-if="nodeType == 1 || nodeLevelType == 2 || (nodeType == 6 && nodeTypeStatus == 3)"
                @click.stop="toHealthrecordDetails(scope.row)"
              >
                查看健康档案
              </el-button>
              <el-button
                type="text"
                v-if="nodeType == 1 && nodeTypeStatus == 2"
                :disabled="scope.row.smsSendCount == 1"
                @click.stop="toRemindEnrollment(scope.row)"
              >
                提醒入组
              </el-button>
              <el-button
                type="text"
                v-if="
                  nodeType == 1 &&
                  nodeTypeStatus == 3 &&
                  scope.row.enrollmentHandlerStatus == 3 &&
                  scope.row.enrollmentStatus == 0 &&
                  scope.row.refuseCount != 2
                "
                @click.stop="assignQueueFollow('3', scope.row)"
                v-hasPermi="['follow:patient_enrollment:handle']"
                >重新入组</el-button
              >
              <el-button
                type="text"
                v-hasPermi="['follow:patient_enrollment:handle']"
                v-if="nodeType == 0 && nodeLevelType == 2 && nodeTypeStatus < 3"
                @click.stop="assignQueue(scope.row)"
              >
                分配队列
              </el-button>
              <el-button
                type="text"
                @click.stop="toFollowDetails(scope.row)"
                v-if="
                  showEditFollow
                    ? scope.row.isSubmit == 1
                    : nodeType == 2 || nodeType == 3 || (nodeType == 4 && nodeTypeStatus >= 3) || nodeType == 5
                "
              >
                查看随访
              </el-button>

              <!-- <el-button
                type="text"
                @click="toFollowDetailsNew(scope.row)"
                v-if="
                  showEditFollow
                    ? scope.row.editFlag == 1
                    : nodeType == 2 || nodeType == 3 || (nodeType == 4 && nodeTypeStatus >= 3) || nodeType == 5
                "
              >
                查看随访
              </el-button> -->
              <el-button type="text" @click.stop="toFollowEdit(scope.row)" v-if="(scope.row.isSubmit == 0 || !scope.row.isSubmit) && showEditFollow == true">
                编辑随访
              </el-button>
              <el-button type="text" v-if="scope.row.outboundId" @click.stop="toOutboundDetails(scope.row)"> 查看外呼 </el-button>
              <el-button
                type="text"
                @click.stop="showUpdateDialog(scope.row)"
                v-hasPermi="['follow:over_time:handle']"
                v-if="nodeType == 3 && nodeTypeStatus < 3"
              >
                更新随访时间
              </el-button>
              <el-button
                type="text"
                v-hasPermi="['follow:over_time:handle']"
                class="el-button-text_warning"
                @click.stop="changIgnore(scope.row)"
                v-if="nodeType == 3 && nodeTypeStatus < 3"
              >
                忽略本次
              </el-button>
              <el-button
                type="text"
                v-hasPermi="['follow:over_time:handle']"
                class="el-button-text_delete"
                @click.stop="showLostVisitDialog(scope.row)"
                v-if="nodeType == 3 && nodeTypeStatus < 3"
                >失访
              </el-button>
              <el-button type="text" v-if="nodeType == 4 && nodeTypeStatus < 3" @click.stop="toFollowDetails(scope.row)">预警处理 </el-button>
              <el-button
                type="text"
                v-if="nodeType == 2 || nodeType == 3 || nodeType == 4 || nodeType == 5"
                @click.stop="handleFocusToggle(scope.row)"
              >
                {{ scope.row.focusStatus == "1" ? "取消重点关注" : "重点关注" }}
              </el-button>

            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="handleQuery($route.query)"
        />
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
      <div style="font-size: 16px; font-weight: bold; line-height: 40px; text-align: center"></div>
      <el-form ref="formLost" :model="formLost" label-width="90px">
        <el-form-item label="失访原因：" prop="lossCause">
          <el-radio-group v-model="formLost.lossCause" @change="changeLost">
            <el-radio v-for="(item, index) in lostList" :key="index" :label="item.value">
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="其他原因：" prop="lossCauseOther" v-if="formLost.lossCause == 3">
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
          <el-cascader
            v-model="assignQueueForm.researchId"
            :options="followQueueList"
            class="dialog_cascader"
            :props="{ value: 'nodeId', label: 'nodeName', children: 'researchDiseasesSelects' }"
            :show-all-levels="false"
            clearable
            @change="selectFollowQueue"
          ></el-cascader>
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
      <div v-if="!currentPatientInfo || currentPatientInfo.accountId == 0">
        <h4>{{ !currentPatientInfo ? "app未绑定患者" : "" }}模板内容：</h4>
        <div style="text-indent: 20px">
          <p>获取专业慢病管理服务。期待您的参与！</p>
          <p>尊敬的“{{ currentPatientInfo ? currentPatientInfo.patientName : "XXX" }}”，欢迎您加入唐都慢病管理平台：</p>
          <p>1.微信关注“唐都医院”公众号；</p>
          <p>2.进入“诊疗服务”选择“慢病管理”；</p>
          <p>3.输入患者信息完成绑定，即可享受专业的慢病管理服务。唐都医院为您的健康保驾护航！</p>
        </div>
      </div>
      <div class="mt10" v-if="!currentPatientInfo || currentPatientInfo.accountId != 0">
        <h4>{{ !currentPatientInfo ? "app已绑定患者" : "" }}模板内容：</h4>
        <div style="text-indent: 20px">
          <p>尊敬的“{{ currentPatientInfo ? currentPatientInfo.patientName : "XXX" }}”，欢迎加入唐都慢病管理平台，</p>
          <p>您被邀请加入“{{ currentPatientInfo ? currentPatientInfo.projectName : "XXX" }}”队列，</p>
          <p>请前往慢病管理患者端，点击个人中心，查看邀请消息，加入队列享受专业的慢病管理服务</p>
        </div>
      </div>
      <div slot="footer" class="dialog_footer">
        <el-button @click="sendMsgTips" type="primary">确 认</el-button>
        <el-button @click="canleMSGTips">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 驳回原因-->
    <el-dialog title="驳回" :visible.sync="rejectkVisible" width="500px">
      <el-form ref="rejectkform" :model="rejectkform" label-width="80px">
        <el-form-item label="驳回原因">
          <el-input type="textarea" v-model="rejectkform.reason" :autosize="{ minRows: 3 }"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" style="display: block; margin: 20px auto 40px; text-align: center">
        <el-button @click="rejectkVisible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit(false)">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTreeRoot,
  listTreeChildren,

  // listTree,
  listEnrollment, //入组列表
  listHandle, //处理列表
  baseHandle, //基线操作
  listOver, //超窗列表
  listWarn, //预警列表
  listAllocated, //待分配列表
  handleTime, //超窗处理
  selectResearch, //病种及队列下拉框
  handleEnrollment, //确认入组/修改入组/待分配入组
  sendEnrollmentWalkup,
  getResarchInfo,
  handleStageList,
  overStageList,
  warnStageList,
  quitRecordFindPage,
  quitRecordUpdate,
  toggleFocus, //重点关注打标签
} from "@/api/follow/work";
import moment from "moment";
import { obesityShow } from "@/api/info/config";
export default {
  name: "WorkstationUser",
  data() {
    return {
      showFlag: false,
      // treeProps: {
      //     children: 'childrenList',
      //     label: 'nodeName',
      //     key: 'nodeId',
      //     isLeaf: 'leaf' // 指定叶子节点字段
      // },
      //lys2
      // 修改 treeProps 配置
      treeProps: {
        children: "childrenList",
        label: "nodeName",
        key: "nodeId",
        isLeaf: (data) => {
          // 根据hasChildren字段判断是否为叶子节点
          return !data.hasChildren;
        },
      },
      loadedNodes: new Set(), // 记录已加载的节点ID
      routeUnwatch: null, // 路由监听器
      isPageActive: true, // 页面是否处于活跃状态

      showFlag: false,
      currentNodeParentName: "", // 当前第三级菜单名称
      showChaoTime: false, // 是否显示超窗时间列
      showEditFollow: true, // 是否查询条件填写状态
      rejectkVisible: false,
      rejectkform: { reason: "" },
      currentPatientData: {},
      // 遮罩层
      loading: false,
      // 选中数组 ·
      researchId: "",
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
      enrollmentDateRange: [],
      queryParams: {
        pageSize: 10,
        pageNum: 1,
        bindStatus: undefined,
        fillStatus: undefined,
      },
      treeQuery: {
        diseasesName: "",
      },
      defaultProps: {
        children: "childrenList",
        label: "nodeName",
        key: "nodeId",
      },
      lostList: [
        { value: "1", label: "不再参与" },
        { value: "2", label: "失联" },
        { value: "3", label: "其他" },
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
        patientId: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }],
      },
      nodeTypeStatus: "",
      defaultExpandedKeys: [],
      currentNodeKey: "",
      nodeType: "",
      nodeLevelType: "",
      projectNameLabel: "计划加入队列", //计划加入队列和队列
      isShowChangeTime: false, //是否显示更新随访时间的弹框
      updateFollowTimeStr: "",
      updateFllowInfo: {}, //更新随访信息
      isShowLostVisitDialog: false, //是否显示失访弹框
      formLost: {}, //失访信息
      isShowAssignQueue: false, //是否显示分配队列
      assignQueueForm: {}, //分配队列
      followQueueList: [], //队列列表
      renderKey: new Date() + "",
      assignQueueInfo: {},
      isShowIgnore: false,
      ignoreReason: "0",
      datePickerOptions: undefined,
      isShowMSGRemind: false,
      currentPatientInfo: {},
      allRemindSms: false,
      checked: false,
      patientIds: [],
      stageList: [],
    };
  },
  watch: {
    // 根据名称筛选部门树
    nodeName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    // 初始化页面状态，检查是否需要恢复之前的状态
    this.initPageFromUrl();

    // 如果没有恢复参数，进行正常的初始化
    if (!this.$route.query.defaultExpandedKeys) {
      this.initTree();
    }
  },

  mounted() {
    this.isPageActive = true;
  },

  activated() {
    this.isPageActive = true;
    // 从子页面（编辑/查看随访）返回时，刷新树和右侧列表
    if (this.currentNodeKey) {
      this.refreshCurrentData();
    }
  },

  deactivated() {
    this.isPageActive = false;
  },

  beforeRouteLeave(to, from, next) {
    this.isPageActive = false;
    next();
  },

  beforeDestroy() {
    this.isPageActive = false;
  },

  methods: {
    /** 从URL参数初始化页面状态 */
    initPageFromUrl() {
      const queryData = this.$route.query;
      this.treeQuery.diseasesName = queryData.diseasesName || "";

      // 如果URL中有恢复参数，则恢复之前的树状态
      if (queryData.defaultExpandedKeys) {
        // 解析URL中的节点展开状态
        const expandedKeysArray = queryData.defaultExpandedKeys.split(",");
        this.defaultExpandedKeys = expandedKeysArray.filter((key) => key);

        // 设置当前选中的节点
        if (expandedKeysArray.length > 0) {
          this.currentNodeKey = expandedKeysArray[expandedKeysArray.length - 1];
        }

        // 重新加载树数据以确保包含完整的节点路径
        this.restoreTreeAndLoadData(queryData);
      } else {
        // 没有恢复参数时的正常初始化
        this.researchId = queryData.researchId || "";
        this.nodeLevelType = queryData.nodeLevelType;
        this.nodeType = queryData.nodeType;
        this.nodeTypeStatus = queryData.nodeTypeStatus;
        this.syncShowEditFollow(queryData);
        this.updateProjectNameLabel();

        if (queryData.researchId || queryData.nodeType || queryData.nodeTypeStatus) {
          this.handleQuery({
            nodeType: queryData.nodeType,
            nodeTypeStatus: queryData.nodeTypeStatus,
            researchId: queryData.researchId,
            nodeLevelType: queryData.nodeLevelType,
          });
        }
      }
    },

    /** 恢复树状态并加载数据 */
    restoreTreeAndLoadData(queryData) {
      this.loading = true;

      // 设置基本状态
      this.nodeLevelType = queryData.nodeLevelType;
      this.nodeType = queryData.nodeType;
      this.nodeTypeStatus = queryData.nodeTypeStatus;
      this.researchId = queryData.researchId || "";
      this.syncShowEditFollow(queryData);
      this.updateProjectNameLabel();

      // 重新初始化树并设置状态
      this.initTreeForRestore(queryData);
    },

    /** 为恢复状态而初始化树 */
    initTreeForRestore(queryData) {
      this.isLoadingDept = true;

      listTreeRoot(this.getTreeQueryParams())
        .then((response) => {
          const rootData = response.data;
          const diseaseNodes = rootData.childrenList || [];

          // 直接将病种节点设置为树的根数据
          this.deptOptions = diseaseNodes;

          // 标记病种节点已加载
          diseaseNodes.forEach((node) => {
            this.loadedNodes.add(node.nodeId);
          });

          this.isLoadingDept = false;

          // 树初始化完成后，设置恢复状态
          this.$nextTick(() => {
            this.setRestoredTreeState(queryData);
          });
        })
        .catch((error) => {
          this.isLoadingDept = false;
          this.loading = false;
          this.$message.error("加载随访任务树失败");
        });
    },

    /** 设置恢复后的树状态 */
    setRestoredTreeState(queryData) {
      if (queryData.defaultExpandedKeys) {
        // 解析URL中的节点展开状态
        const expandedKeysArray = queryData.defaultExpandedKeys.split(",").filter((key) => key);
        this.defaultExpandedKeys = expandedKeysArray;

        // 设置当前选中的节点
        if (expandedKeysArray.length > 0) {
          this.currentNodeKey = expandedKeysArray[expandedKeysArray.length - 1];

          // 如果树组件可用，直接设置当前节点
          this.$nextTick(() => {
            if (this.$refs.tree) {
              this.$refs.tree.setCurrentKey(this.currentNodeKey);
            }
          });
        }
      }

      this.syncShowEditFollow(queryData);

      // 加载数据
      this.handleQuery({
        nodeType: queryData.nodeType,
        nodeTypeStatus: queryData.nodeTypeStatus,
        researchId: queryData.researchId,
        nodeLevelType: queryData.nodeLevelType,
      });

      this.loading = false;
    },

    /** 更新项目名称标签 */
    updateProjectNameLabel() {
      if (this.nodeType == 1 || this.nodeLevelType == 2) {
        this.projectNameLabel = "计划加入队列";
      } else {
        this.projectNameLabel = "队列";
      }
    },

    syncShowEditFollow(data = {}) {
      const nodeType = data.nodeType !== undefined ? data.nodeType : this.nodeType;
      const nodeTypeStatus = data.nodeTypeStatus !== undefined ? data.nodeTypeStatus : this.nodeTypeStatus;
      const shouldShowEditFollow =
        ((nodeType == "2" || nodeType == "3") && nodeTypeStatus == "1") || (nodeType == "5" && nodeTypeStatus == "1");

      this.showEditFollow = shouldShowEditFollow;
      if (!shouldShowEditFollow) {
        this.queryParams.fillStatus = undefined;
      }
    },

    /** 刷新当前数据 */
    refreshCurrentData() {
      // 只有在有当前节点信息时才刷新
      if (this.currentNodeKey && (this.nodeType || this.nodeTypeStatus)) {
        // 重新加载数据，但不重置树状态
        const queryData = {
          nodeType: this.nodeType,
          nodeTypeStatus: this.nodeTypeStatus,
          researchId: this.researchId,
          nodeLevelType: this.nodeLevelType,
        };

        this.syncShowEditFollow(queryData);

        this.handleQuery(queryData);

        // 同时刷新树数据（不重置展开状态）
        this.refreshTreeData();
      }
    },

    /** 刷新树数据但不重置展开状态 */
    refreshTreeData() {
      this.isLoadingDept = true;

      listTreeRoot(this.getTreeQueryParams())
        .then((response) => {
          const rootData = response.data;
          const diseaseNodes = rootData.childrenList || [];

          // 保持现有的展开状态和选中状态
          const currentExpandedKeys = [...this.defaultExpandedKeys];
          const currentNodeKey = this.currentNodeKey;

          // 更新树数据
          this.deptOptions = diseaseNodes;

          // 标记病种节点已加载
          diseaseNodes.forEach((node) => {
            this.loadedNodes.add(node.nodeId);
          });

          // 恢复状态
          this.$nextTick(() => {
            this.defaultExpandedKeys = currentExpandedKeys;
            this.currentNodeKey = currentNodeKey;
            if (this.$refs.tree) {
              this.$refs.tree.setCurrentKey(this.currentNodeKey);
            }
          });

          this.isLoadingDept = false;
        })
        .catch((error) => {
          this.isLoadingDept = false;
          this.$message.error("刷新树数据失败");
        });
    },
    onSubmit(flag) {
      let data = {
        id: this.currentPatientData.id,
        isAgree: flag ? true : false,
        rejectReason: this.rejectkform.reason,
        researchId: this.currentPatientData.researchId,
        personId: this.currentPatientData.personId,
      };
      quitRecordUpdate(data).then((res) => {
        this.rejectkVisible = false;
        this.isOperating = false; // [修复] 审核操作完成，解除操作锁，允许自动刷新
        if (flag) {
          this.$modal.msgSuccess("患者已退出当前队列");
        } else {
          this.$message.warning("拒绝患者退出队列,保留患者原状态");
        }
        this.handleQuery({
          nodeType: this.nodeType,
          nodeTypeStatus: this.nodeTypeStatus,
          researchId: this.researchId,
          nodeLevelType: this.nodeLevelType,
        });
        this.getTreeselect();
      });
    },
    handleAduit(row, flag) {
      this.isOperating = true; // [修复] 开始审核操作，加锁防止自动刷新干扰
      this.currentPatientData = row;
      if (flag) {
        this.onSubmit(flag);
      } else {
        this.rejectkform.reason = "";
        this.rejectkVisible = true;
      }
    },
    clickRow(row) {
      if (!row.smsSendCount || row.smsSendCount < 2) {
        this.$refs.selectTable.toggleRowSelection(row);
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.patientIds = selection.map((item) => item.patientId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
      this.userList.forEach((element, index) => {
        if (selection.findIndex((f) => f.checkedKey == element.checkedKey) > -1) {
          this.$set(this.userList[index], "checked", true);
        } else {
          this.$set(this.userList[index], "checked", false);
        }
      });
    },
    /** 查询入组列表 */
    getListEnrollment(researchId, nodeTypeStatus) {
      this.loading = true;
      const queryData = {
        enrollmentHandlerStatus: nodeTypeStatus,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      listEnrollment(queryData).then((response) => {
        this.renderKey = moment().valueOf() + "";
        let resData = response.rows;
        let list = [];
        if (resData && resData.length) {
          this.allRemindSms = resData.findIndex((f) => f.smsSendCount >= 2) > -1;
          list = resData;
          list.forEach((element, index) => {
            element.checked = false;
            element.checkedKey = `${element.patientId}${index}`;
          });
        }
        this.userList = list;
        this.loading = false;
        this.total = response.total || 0;
      });
    },
    /** 查询基线列表 */
    getBaseHandle(researchId, followStatus) {
      this.loading = true;
      const queryData = {
        followStatus: followStatus,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      baseHandle(queryData).then((response) => {
        this.userList = response.rows;
        this.loading = false;
        this.total = response.total || 0;
      });
    },
    getQuitRecordHandle(researchId, followStatus) {
      this.loading = true;
      const queryData = {
        status: followStatus == "1" ? 0 : 1,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      quitRecordFindPage(queryData).then((response) => {
        this.userList = response.rows;
        this.loading = false;
        this.total = response.total || 0;
      });
    },
    /** 查询处理列表 */
    getListHandle(researchId, followStatus) {
      this.loading = true;
      const queryData = {
        followStatus: followStatus,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      listHandle(queryData).then((response) => {
        this.userList = response.rows;
        this.loading = false;
        this.total = response.total || 0;
      });
    },
    /** 查询超窗列表 */
    getListOver(researchId, overTimeStatus) {
      this.loading = true;
      const queryData = {
        overTimeStatus: overTimeStatus,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      listOver(queryData).then((response) => {
        this.userList = response.rows;
        this.loading = false;
        this.total = response.total || 0;
      });
    },
    /** 查询预警 */
    getListWarn(researchId, warnStatus) {
      this.loading = true;
      const queryData = {
        warnStatus: warnStatus,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      listWarn(queryData).then((response) => {
        this.userList = response.rows;
        this.loading = false;
        this.total = response.total || 0;
      });
    },
    /** 查询未分配列表 */
    getlistAllocated(researchId, allocatedHandlerStatus) {
      this.loading = true;
      const queryData = {
        allocatedHandlerStatus: allocatedHandlerStatus,
        ...this.queryParams,
      };
      if (researchId) {
        queryData["researchId"] = researchId;
      }
      listAllocated(queryData)
        .then((response) => {
          this.userList = response.rows;
          this.loading = false;
          this.total = response.total || 0;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      this.isLoadingDept = true;
      listTreeRoot(this.getTreeQueryParams())
        .then((response) => {
          const newDiseaseNodes = response.data.childrenList.filter((f) => f.childrenList && f.childrenList.length);
          // 更新已加载节点的handleNum，保持树结构不变
          newDiseaseNodes.forEach((newNode) => {
            const existingNode = this.deptOptions.find((n) => n.nodeId === newNode.nodeId);
            if (existingNode) {
              existingNode.handleNum = newNode.handleNum;
              this.updateChildrenHandleNum(existingNode.childrenList, newNode.childrenList);
            }
          });
          this.isLoadingDept = false;
        })
        .catch((error) => {
          this.isLoadingDept = false;
        });
    },
    /** 递归更新子节点的handleNum */
    updateChildrenHandleNum(existingChildren, newChildren) {
      if (!existingChildren || !newChildren) return;
      existingChildren.forEach((child) => {
        const match = newChildren.find((n) => n.nodeId === child.nodeId);
        if (match) {
          child.handleNum = match.handleNum;
          this.updateChildrenHandleNum(child.childrenList, match.childrenList);
        }
      });
    },

    /** 初始化树 - 只展示第一级节点，其他节点懒加载 */
    initTree() {
      this.isLoadingDept = true;
      listTreeRoot(this.getTreeQueryParams())
        .then((response) => {
          const rootData = response.data;
          const diseaseNodes = rootData.childrenList || [];

          // 直接将病种节点设置为树的根数据
          this.deptOptions = diseaseNodes;

          // 不设置默认展开的节点，保持树收起状态，只显示第一级节点
          this.defaultExpandedKeys = [];

          // 标记病种节点已加载
          diseaseNodes.forEach((node) => {
            this.loadedNodes.add(node.nodeId);
          });

          this.isLoadingDept = false;
        })
        .catch((error) => {
          this.isLoadingDept = false;
          this.$message.error("加载随访任务树失败");
        });
    },

    /** 懒加载树节点 */
    loadNode(node, resolve) {
      const nodeData = node.data;
      const nodeId = nodeData.nodeId;

      // 如果节点已经加载过，直接返回缓存的子节点
      if (this.loadedNodes.has(nodeId) && nodeData.childrenList) {
        return resolve(nodeData.childrenList);
      }

      // 病种节点 - 加载该病种的子树
      if (nodeData.diseaseId) {
        this.loadDiseaseSubTree(nodeData, resolve);
      } else {
        // 其他节点（队列、任务类型等）- 直接返回已有子节点或空数组
        resolve(nodeData.childrenList || []);
      }
    },

    /** 加载病种节点 - 现在病种节点已在初始化时直接加载 */
    loadDiseaseNodes(parentNodeData, resolve) {
      // 病种节点已经在初始化时加载，直接返回childrenList
      if (parentNodeData.childrenList && parentNodeData.childrenList.length > 0) {
        resolve(parentNodeData.childrenList);
      } else {
        resolve([]);
      }
    },

    /** 加载病种子树 */
    loadDiseaseSubTree(diseaseNodeData, resolve) {
      // 从病种节点ID中提取diseaseId
      // 病种节点ID格式: "0_{diseaseId}"
      const nodeIdParts = diseaseNodeData.nodeId.split("_");
      if (nodeIdParts.length < 2) {
        this.$message.error("病种节点ID格式错误");
        return resolve([]);
      }

      const diseaseId = nodeIdParts[1];

      // 调用病种子树接口
      listTreeChildren(diseaseId)
        .then((response) => {
          const subTree = response.data;

          if (subTree && subTree.childrenList) {
            // 标记该病种节点已加载子树
            this.loadedNodes.add(diseaseNodeData.nodeId);

            // 更新病种节点的子节点
            this.$set(diseaseNodeData, "childrenList", subTree.childrenList);
            resolve(subTree.childrenList);
          } else {
            resolve([]);
          }
        })
        .catch((error) => {
          this.$message.error("加载病种子树失败");
          resolve([]);
        });
    },

    getTreeQueryParams() {
      return {
        diseasesName: this.treeQuery.diseasesName || undefined,
      };
    },
    handleTreeSearch() {
      this.currentNodeKey = "";
      this.defaultExpandedKeys = [];
      this.loadedNodes = new Set();
      this.updateRouteQuery({
        diseasesName: this.treeQuery.diseasesName || undefined,
        defaultExpandedKeys: undefined,
      });
      this.initTree();
    },
    resetTreeSearch() {
      if (!this.treeQuery.diseasesName) {
        this.handleTreeSearch();
        return;
      }
      this.treeQuery.diseasesName = "";
      this.handleTreeSearch();
    },
    updateRouteQuery(extraQuery = {}) {
      const nextQuery = {
        ...this.$route.query,
        ...extraQuery,
      };
      Object.keys(nextQuery).forEach((key) => {
        if (nextQuery[key] === undefined || nextQuery[key] === null || nextQuery[key] === "") {
          delete nextQuery[key];
        }
      });
      this.$router.replace({
        path: this.$route.path,
        query: nextQuery,
      });
    },
    buildOldPath() {
      const query = {
        defaultExpandedKeys: this.defaultExpandedKeys.toString(),
        nodeTypeStatus: this.nodeTypeStatus,
        nodeType: this.nodeType,
        researchId: this.researchId,
        nodeLevelType: this.nodeLevelType,
        diseasesName: this.treeQuery.diseasesName || undefined,
      };
      const queryString = Object.keys(query)
        .filter((key) => query[key] !== undefined && query[key] !== null && query[key] !== "")
        .map((key) => `${key}=${encodeURIComponent(query[key])}`)
        .join("&");
      return queryString ? `${this.$route.path}?${queryString}` : this.$route.path;
    },

    //lys2
    /** 处理节点点击事件 */
    // handleNodeClick(data, node) {
    //   this.nodeType = data.nodeType;
    //   this.nodeTypeStatus = data.nodeTypeStatus;
    //   // 三级菜单 随访处理 随访超窗 需要显示填写状态查询条件
    //   if (data.nodeLevel == 3) {
    //     this.currentNodeParentName = data.nodeName;
    //   }
    //   //随访处理、随访超窗的待处理状态,基线未完成 showEditFollow为true
    //   if (
    //     (data.nodeLevel == 4 && data.nodeName == "待处理" && (this.nodeType == "2" || this.nodeType == "3")) ||
    //     (this.nodeType == "5" && data.nodeName == "未完成")
    //   ) {
    //     this.showEditFollow = true;
    //   } else {
    //     this.showEditFollow = false;
    //     this.queryParams.fillStatus = undefined;
    //   }
    //   if (this.nodeType == "3") {
    //     this.showChaoTime = true;
    //   } else {
    //     this.showChaoTime = false;
    //   }

    //   this.defaultExpandedKeys = [];
    //   this.currentNodeKey = node.data.nodeId;
    //   this.convertDefaultKeys(node);
    //   this.handleQuery(data);
    //   this.researchId = data.researchId;
    //   this.updateProjectNameLabel();
    //   this.$router.push({
    //     path: this.$route.path,
    //     query: {
    //       defaultExpandedKeys: this.defaultExpandedKeys.toString(),
    //       nodeTypeStatus: this.nodeTypeStatus,
    //       nodeType: this.nodeType,
    //       researchId: this.researchId,
    //       nodeLevelType: this.nodeLevelType,
    //     },
    //   });
    //   console.log(data);
    //     if (node.level == 4) {
    //       this.obesityShow(data.researchId);
    //     }
    // },

    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data, node) {
      this.nodeType = data.nodeType;
      this.nodeTypeStatus = data.nodeTypeStatus;
      // 三级菜单 随访处理 随访超窗 需要显示填写状态查询条件
      if (data.nodeLevel == 3) {
        this.currentNodeParentName = data.nodeName;
      }
      //随访处理、随访超窗的待处理状态,基线未完成 showEditFollow为true
      if (
        (data.nodeLevel == 4 && data.nodeName == "待处理" && (this.nodeType == "2" || this.nodeType == "3")) ||
        (this.nodeType == "5" && data.nodeName == "未完成")
      ) {
        this.showEditFollow = true;
      } else {
        this.showEditFollow = false;
        this.queryParams.fillStatus = undefined;
      }
      if (this.nodeType == "3") {
        this.showChaoTime = true;
      } else {
        this.showChaoTime = false;
      }

      this.defaultExpandedKeys = [];
      this.currentNodeKey = node.data.nodeId;
      this.convertDefaultKeys(node);
      this.handleQuery(data);
      this.researchId = data.researchId;
      this.updateProjectNameLabel();
      this.updateRouteQuery({
        defaultExpandedKeys: this.defaultExpandedKeys.toString(),
        nodeTypeStatus: this.nodeTypeStatus,
        nodeType: this.nodeType,
        researchId: this.researchId,
        nodeLevelType: this.nodeLevelType,
        diseasesName: this.treeQuery.diseasesName || undefined,
      });
      console.log(data);
        if (node.level == 4) {
          this.obesityShow(data.researchId);
        }
    },

    obesityShow(researchId) {
      obesityShow(researchId).then((response) => {
        this.showFlag = response.data;
        console.log("showFlag", this.showFlag);
      });
    },
    //添加当前组件选中的keys值
    convertDefaultKeys(node) {
      if (node.level > 0) {
        this.defaultExpandedKeys.unshift(node.data.nodeId);
        this.convertDefaultKeys(node.parent);
      }
    },
    /**
     * 获取队列
     */
    getQueueSelect() {
      selectResearch().then((response) => {
        let valueArr = response.data;
        this.followQueueList = valueArr.filter((f) => f.researchDiseasesSelects);
      });
    },
    /**
     * 选择分组
     */
    selectFollowQueue(val) {
      let arr = val.slice(1, 2).toString();
      this.nodeIdTwo = arr;
    },
    /**
     * 确认分配队列
     */
    handleSeeForm() {
      if (!this.nodeIdTwo) {
        this.$message.warning("请选择病重队列");
        return;
      }
      handleEnrollment({
        handleType: "2",
        researchId: this.nodeIdTwo,
        patientId: this.assignQueueInfo.patientId,
      }).then((response) => {
        this.$modal.msgSuccess("确认分配队列成功");
        this.handleQuery(this.$route.query);
        this.isShowAssignQueue = false;
        this.isOperating = false; // [修复] 分配队列完成，解除操作锁
      });
    },
    /**
     * 取消分配队列弹窗
     */
    canleSeeForm() {
      this.isShowAssignQueue = false;
      this.isOperating = false; // [修复] 取消分配，解除操作锁
    },
    // 分配队列
    assignQueue(item) {
      this.isOperating = true; // [修复] 开始分配队列操作，加锁防止自动刷新干扰
      this.isShowAssignQueue = true;
      this.assignQueueInfo = item;
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
          oldPath: this.buildOldPath(),
        },
      });
    },
    toRemindEnrollment(item) {
      this.isOperating = true; // [修复] 开始提醒入组操作，加锁防止自动刷新干扰
      this.isShowMSGRemind = true;
      if (item.patientId) {
        this.currentPatientInfo = item;
      } else {
        this.currentPatientInfo = undefined;
      }
    },
    sendMsgTips() {
      this.isShowMSGRemind = false;
      let patientId = undefined;
      if (this.currentPatientInfo) {
        patientId = this.currentPatientInfo.patientId;
      } else if (this.patientIds && this.patientIds.length) {
        patientId = this.patientIds.toString();
      }
      let params = {
        patientIds: patientId,
      };
      sendEnrollmentWalkup(params).then((res) => {
        this.$message.success("发送成功");
        this.isOperating = false; // [修复] 发送完成，解除操作锁
        this.handleQuery(this.$route.query);
      });
    },
    canleMSGTips() {
      this.isShowMSGRemind = false;
      this.isOperating = false; // [修复] 取消短信提醒，解除操作锁
    },
    /** 确认入组按钮操作 */
    assignQueueFollow(handleType, rows) {
      let params = {
        researchId: this.researchId,
        handleType,
        patientId: rows.patientId,
      };
      let confirmTitle = "";
      if (handleType == 1) {
        confirmTitle = `请确认患者暂不入组？`;
      } else if (handleType == 3 && rows.accountId == 0) {
        confirmTitle = `<p>稍后会给“${rows.patientName}”发送短信，短信内容为:</p>
                <p>尊敬的“${rows.patientName}”，欢迎您加入唐都慢病管理平台：</p>
                <p>1.微信关注“唐都医院”公众号；</p>
                <p>2.进入“诊疗服务”选择“慢病管理”；</p>
                <p>3.输入患者信息完成绑定，即可享受专业的慢病管理服务。</p>
                <p>唐都医院为您的健康保驾护航！</p>
                <p>点击确定即发送短信，无需发送请点击取消。</p>`;
      } else if (handleType == 3 && rows.accountId != 0) {
        getResarchInfo(rows.patientId).then((res) => {
          if (res.data.length > 0) {
            let queuelist = res.data.map((item) => `【${item}】`).join("");
            confirmTitle = `患者已加入${queuelist}队列，是否确认将患者加入当前队列？`;
          } else {
            confirmTitle = `是否确认将患者加入当前队列？`;
          }
          this.$confirm(`${confirmTitle}`, "提示", {
            type: "warning",
            dangerouslyUseHTMLString: true,
          })
            .then(() => {
              this.patientEnrollment(params);
            })
            .catch((error) => {
              return;
            });
        });
      }
      if (handleType == 1 || (handleType == 3 && rows.accountId == 0)) {
        this.$confirm(`${confirmTitle}`, "提示", {
          type: "warning",
          dangerouslyUseHTMLString: true,
        })
          .then(() => {
            this.patientEnrollment(params);
          })
          .catch((error) => {
            return;
          });
      }
    },
    // 患者入组方法
    patientEnrollment(params) {
      handleEnrollment(params).then((res) => {
        this.$modal.msgSuccess("操作成功");
        this.handleQuery(this.$route.query);
      });
    },
    // 重点关注 / 取消重点关注
    handleFocusToggle(row) {
      const focusStatus = row.focusStatus == "1" ? "0" : "1";
      const text = focusStatus == "1" ? "重点关注" : "取消重点关注";
      this.$confirm(`确认${text}该患者吗？`, "提示", {
        type: "warning",
      })
        .then(() => {
          toggleFocus(row.patientId, focusStatus).then((res) => {
            this.$modal.msgSuccess(`${text}成功`);
            this.handleQuery(this.$route.query);
          });
        })
        .catch((error) => {
          return;
        });
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
          oldPath: this.buildOldPath(),
        },
      });
    },
    toFollowEdit(item) {
      // console.log('item', item, this.nodeTypeStatus)
      this.$router.push({
        path: "/workstation/user/editFollowUp",
        query: {
          type: this.nodeType,
          patientId: item.patientId,
          followId: item.followId, // 这个没有 无法复用edc页面部分代码
          personId: item.personId,
          researchId: this.researchId,
          recordId: item.recordId,
          nodeTypeStatus: this.nodeTypeStatus,
          oldPath: this.buildOldPath(),
        },
      });
    },

    toFollowDetailsNew(item) {
      this.$router.push({
        path: "/workstation/user/DetailsFollowUp",
        query: {
          type: this.nodeType,
          patientId: item.patientId,
          followId: item.followId, // 这个没有 无法复用edc页面部分代码
          personId: item.personId,
          researchId: this.researchId,
          recordId: item.recordId,
          nodeTypeStatus: this.nodeTypeStatus,
          oldPath: `${this.$route.path}?defaultExpandedKeys=${this.defaultExpandedKeys.toString()}&nodeTypeStatus=${
            this.nodeTypeStatus
          }&nodeType=${this.nodeType}&researchId=${this.researchId}&nodeLevelType=${this.nodeLevelType}`,
        },
      });
    },

    toOutboundDetails(item) {
      this.$router.push({
        path: "/outbound/details",
        query: {
          outboundId: item.outboundId,
        },
      });
    },
    /**
     * 失访原因
     */
    changeLost(value) {
      if (value == 3) {
        this.flag = true;
      } else {
        this.flag = false;
        this.formLost.lossCauseOther = "";
      }
    },
    /** 随访 忽略本次 */
    changIgnore(item) {
      this.isOperating = true; // [修复] 开始忽略操作，加锁防止自动刷新干扰
      this.updateFllowInfo = item;
      this.isShowIgnore = true;
    },
    //
    canleIgnoreReset() {
      this.isShowIgnore = false;
      this.ignoreReason = "0";
      this.isOperating = false; // [修复] 取消忽略，解除操作锁
    },
    // 显示失访弹窗
    showLostVisitDialog(item) {
      this.isOperating = true; // [修复] 开始失访操作，加锁防止自动刷新干扰
      this.isShowLostVisitDialog = true;
      this.updateFllowInfo = item;
    },
    // 随访 失访
    updateFollowLostVisit() {
      this.updateFollowInfo("3");
    },
    // 取消随访失访
    cancelLost() {
      this.updateFllowInfo = {};
      this.formLost = {
        lossCause: "",
        lossCauseOther: "",
      };
      this.isShowLostVisitDialog = false;
      this.isOperating = false; // [修复] 取消失访，解除操作锁
    },
    // 显示更新随访时间弹窗
    showUpdateDialog(item) {
      this.isOperating = true; // [修复] 开始更新随访时间操作，加锁防止自动刷新干扰
      this.isShowChangeTime = true;
      this.updateFllowInfo = item;
      this.initDatePicker();
    },
    // 更新随访时间disabled
    initDatePicker() {
      this.datePickerOptions = {
        disabledDate: (time) => {
          return moment(time).valueOf() < moment().valueOf();
        },
      };
    },
    // 更新随访时间
    updateFollowInfo(handleType) {
      let params = {
        recordId: this.updateFllowInfo.recordId,
        handleType: handleType, //处理类型1更新随访时间2忽略本次3失访
        researchId: this.researchId, // 队列ID
      };
      let successTitle = "更新随访时间成功";
      if (handleType == 1 && !this.updateFollowTimeStr) {
        this.$message.warning("请选择更新随访时间");
        return;
      } else if (handleType == 3 && !this.formLost.lossCause) {
        this.$message.warning("失访原因不能为空");
        return;
      }
      if (handleType == 1) {
        params["visitTime"] = this.updateFollowTimeStr;
      } else if (handleType == 2) {
        params["visitTime"] = this.updateFollowTimeStr;
        params["ignoreType"] = this.ignoreReason;
        successTitle = "此次随访已忽略成功";
      } else if (handleType == 3) {
        params["lossCause"] = this.formLost.lossCause; //失访原因1不在参与2失联3其他
        params["lossCauseOther"] = this.formLost.lossCauseOther; //失访原因其他
        successTitle = "操作成功";
      }
      handleTime(params).then((response) => {
        this.$modal.msgSuccess(`${successTitle}`);
        this.isShowChangeTime = false;
        this.isShowLostVisitDialog = false;
        this.isShowIgnore = false;
        this.isOperating = false; // [修复] 处理完成，解除操作锁，允许自动刷新

        this.handleQuery({
          nodeType: this.nodeType,
          nodeTypeStatus: this.nodeTypeStatus,
          researchId: this.researchId,
          nodeLevelType: this.nodeLevelType,
        });
        this.getTreeselect();
      });
    },
    // 取消更新随访时间
    cancelUpdateTime() {
      this.isShowChangeTime = false;
      this.updateFllowInfo = {};
      this.isOperating = false; // [修复] 取消更新随访时间，解除操作锁
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
        cardId: undefined,
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
    handleQuery1(data) {
      if (this.enrollmentDateRange?.length > 0) {
        this.queryParams.enrollmentStartTime = this.enrollmentDateRange[0];
        this.queryParams.enrollmentEndTime = this.enrollmentDateRange[1];
      } else {
        this.queryParams.enrollmentStartTime = "";
        this.queryParams.enrollmentEndTime = "";
      }
      this.handleQuery(data);
    },
    /** 搜索按钮操作 */
    handleQuery(data) {
      if (!data.nodeTypeStatus) {
        data = this.$route.query;
      }
      data.researchId = !data.researchId || data.researchId == "null" ? "" : data.researchId;
      this.nodeTypeStatus = data.nodeTypeStatus;
      this.nodeType = data.nodeType;
      this.nodeLevelType = data.nodeLevelType;
      if (this.nodeType != 1) {
        this.queryParams.bindStatus = undefined;
      }

      if (data.nodeTypeStatus == 0) {
        this.userList = [];
        return;
      }
      if (data.nodeType == "0" && data.nodeLevelType == "2") {
        this.getlistAllocated(data.researchId, data.nodeTypeStatus);
      } else if (data.nodeType == "1" && data.nodeLevelType != "0") {
        this.getListEnrollment(data.researchId, data.nodeTypeStatus);
      } else if (data.nodeType == "2" && data.nodeLevelType != "0") {
        handleStageList({ followStatus: data.nodeTypeStatus, researchId: data.researchId }).then((res) => {
          this.stageList = res.data;
        });
        this.getListHandle(data.researchId, data.nodeTypeStatus);
      } else if (data.nodeType == "3" && data.nodeLevelType != "0") {
        overStageList({ overTimeStatus: data.nodeTypeStatus, researchId: data.researchId }).then((res) => {
          this.stageList = res.data;
        });
        this.getListOver(data.researchId, data.nodeTypeStatus);
      } else if (data.nodeType == "4" && data.nodeLevelType != "0") {
        warnStageList({ warnStatus: data.nodeTypeStatus, researchId: data.researchId }).then((res) => {
          this.stageList = res.data;
        });
        this.getListWarn(data.researchId, data.nodeTypeStatus);
      } else if (data.nodeType == "5" && data.nodeLevelType != "0") {
        this.getBaseHandle(data.researchId, data.nodeTypeStatus);
      } else if (data.nodeType == "6" && data.nodeLevelType != "0") {
        this.getQuitRecordHandle(data.researchId, data.nodeTypeStatus);
      }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.enrollmentDateRange = [];
      this.queryParams.enrollmentStartTime = "";
      this.queryParams.enrollmentEndTime = "";
      this.resetForm("queryForm");
      this.handleQuery(this.$route.query);
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/user/export",
        {
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

.tree_search_box {
  margin-bottom: 12px;
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

::v-deep .checkbox_item {
  .el-checkbox__inner {
    background-color: #fff;
  }

  &.is-disabled {
    .el-checkbox__input {
      cursor: pointer;
    }

    .el-checkbox__inner {
      cursor: pointer;

      &::after {
        border-color: #fff;
      }
    }
  }

  &.is-disabled.is-checked {
    .el-checkbox__inner {
      cursor: pointer;
      background-color: #1890ff;
      border-color: #1890ff;

      &::after {
        cursor: pointer;
        border-color: #fff;
      }
    }
  }
}
</style>