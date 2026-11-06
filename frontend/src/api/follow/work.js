import request from '@/utils/request'
/*** 
 * ============================= 随访工作站 start =====================================
 */


//lys
// // 查询【随访工作站树根节点】
// export function listTreeRoot() {
//     return request({
//         url: '/follow/work/tree/root',
//         method: 'get'
//     })
// }

// // 查询【随访工作站树子节点】
// export function listTreeChildren(parentNodeId) {
//     return request({
//         url: '/follow/work/tree/children',
//         method: 'get',
//         params: { parentNodeId }
//     })
// }
//lys

// 查询【随访工作站树结构】列表
// export function listTree(query) {
//     return request({
//         url: '/follow/work/tree ',
//         method: 'get',
//         params: query
//     })
// }

//lys2
// 查询【随访工作站根树结构】列表
export function listTreeRoot(query) {
    return request({
        url: '/follow/work/rootTree',
        method: 'get',
        params: query
    })
}

// 根据病种ID加载子树
export function listTreeChildren(diseaseId) {
    return request({
        url: `/follow/work/diseaseTree/${diseaseId}`,
        method: 'get'
    })
}
//lys2

// 查询【随访工作站树结构】列表
export function listTree(query) {
    return request({
        url: '/follow/work/rootTree ',
        method: 'get',
        params: query
    })
}

// 查询【入组列表】列表
export function listEnrollment(query) {
    return request({
        url: '/follow/work/enrollment/list',
        method: 'get',
        params: query
    })
}

// 查询【处理列表】列表
export function listHandle(query) {
    return request({
        url: '/follow/work/handle/list',
        method: 'get',
        params: query
    })
}
//查询【基线】列表、
export function baseHandle(query) {
    return request({
        url: '/follow/work/base/list',
        method: 'get',
        params: query
    })
}
// 查询【超窗列表】列表
export function listOver(query) {
    return request({
        url: '/follow/work/over/list',
        method: 'get',
        params: query
    })
}

// 查询【预警列表】列表
export function listWarn(query) {
    return request({
        url: '/follow/work/warn/list',
        method: 'get',
        params: query
    })
}

// 查询【待分配列表】列表
export function listAllocated(query) {
    return request({
        url: '/follow/work/allocated/list',
        method: 'get',
        params: query
    })
}

// 查询【确认入组/修改入组/待分配入组】列表
export function handleEnrollment(data) {
    return request({
        url: '/follow/work/enrollment/handle',
        method: 'post',
        data: data
    })
}
//查询【入组时患者已加入的队列】
export function getResarchInfo(patientId) {
    return request({
        url: `/follow/work/enrollment/getResarchInfo/${patientId}`,
        method: 'get'
    })
}
// 查询【待分配入组操作】列表
export function handleAllocated(data) {
    return request({
        url: '/follow/work/allocated/handle',
        method: 'post',
        data: data
    })
}

// 查询【病种及队列下拉框】列表
export function selectResearch(query) {
    return request({
        url: '/follow/work/diseases/research/select',
        method: 'get',
        params: query
    })
}

// 超窗【超窗处理】
export function handleTime(data) {
    return request({
        url: '/follow/work/over/time/handle',
        method: 'post',
        data: data
    })
}

//【提交意见】
export function handleOpinion(data) {
    return request({
        url: '/follow/work/submit/opinion/handle',
        method: 'post',
        data: data
    })
}

//【详情接口】
export function workDetails(query) {
    return request({
        url: '/follow/work/details',
        method: 'get',
        params: query
    })
}

// 查询【档案管理-随访管理：患者随访节点】列表（待随访 + 已完成）
export function listPatientFollowNodes(cardId) {
    return request({
        url: '/follow/work/patient/nodes',
        method: 'get',
        params: { cardId }
    })
}

// 重点关注患者打标签 / 取消重点关注
export function toggleFocus(patientId, focusStatus) {
    return request({
        url: '/follow/work/focus/toggle',
        method: 'post',
        params: { patientId, focusStatus }
    })
}

//【发起质疑接口】
export function saveQuestionContent(query) {
    return request({
        url: '/questionRecord/saveQuestionRecord',
        method: 'POST',
        data: query
    })
}
// 【发送预警指导消息】
export function sendWarnEarlyNews(query) {
    return request({
        url: `/warn/guide`,
        method: 'POST',
        data: query
    })
}

// 【预警指导聊天内容】
export function warnEarlyGuideList(recordId) {
    return request({
        url: `/warn/guide/${recordId}`,
        method: 'GET',
    })
}
// 【结束预警指导】
export function finishWarnEarlyGuide(recordId) {
    return request({
        url: `/warn/guide/end/${recordId}`,
        method: 'GET',
    })
}

// 【患者入组_提醒发送短信】
export function sendEnrollmentWalkup(data) {
    return request({
        url: `/follow/work/enrollment/walkup`,
        method: 'POST',
        params:data
    })
}

// 一键拨打电话（语音盒）
export function callPatient(patientId) {
    return request({
        url: '/follow/work/call',
        method: 'post',
        params: { patientId }
    })
}

// 发送短信（复用腾讯云短信）
export function sendSms(patientId) {
    return request({
        url: '/follow/work/sendSms',
        method: 'post',
        params: { patientId }
    })
}

// 发送微信随访提醒
export function sendWechat(patientId, content) {
    return request({
        url: '/follow/work/sendWechat',
        method: 'post',
        params: { patientId, content }
    })
}

/*** 
 * ============================= 随访工作站 end =====================================
 */

/**
 *  ==============================  EDC工作站  =============================
 */
// 获取EDC任务树
export function edcTreeList(query) {
    return request({
        url: '/edc/work/edcTree',
        method: 'get',
        params: query
    })
}

// 获取EDC 患者列表
export function getEDCPatientList(query) {
    return request({
        url: '/edc/work/edcList',
        method: 'get',
        params: query
    })
}


// 获取档案详情树状图
export function treeFollow(query) {
    return request({
        url: '/edc/work/followMedicalFromTree',
        method: 'get',
        params: query
    })
}

// 获取档案详情
export function workEdcDetail(query) {
    return request({
        url: '/edc/work/edcPatientRecordDetail',
        method: 'get',
        params: query
    })
}

// 快捷回复列表
export function listQuick() {
    return request({
        url: `/question/quick/list`,
        method: 'get'
    })
}

// 快捷回复添加
export function addQuick(data) {
    return request({
        url: `/question/quick/save`,
        method: 'post',
        data: data
    })
}

// 快捷回复删除
export function delQuick(quickId) {
    return request({
        url: `/question/quick/delete/${quickId}`,
        method: 'delete'
    })
}

//EDC工作站 锁库
export function lockEDCQuestion(recordId,medicalFormId) {
    return request({
        url: `/question/editPatientRecordEdcStatus/${recordId}/${medicalFormId}`,
        method: 'get',
    })
}
//EDC工作站 质疑列表
export function quseiontList(recordId) {
    return request({
        url: `/question/qt/list/${recordId}`,
        method: 'get',
    })
}


//档案保存/提交
export function subEdcInsert(data) {
    return request({
        url: `/edc/work/edcInsert`,
        method: 'post',
        data: data
    })
}

//EDC 质疑提交
export function subSaveQuestionRecord(data) {
    return request({
        url: `/question/saveEdcAskQuestionRecord`,
        method: 'post',
        data: data,
    })
}

//EDC 质疑回复
export function subReplayQuestionRecord(data) {
    return request({
        url: `/question/saveEdcReplyQuestionRecord`,
        method: 'post',
        data: data,
    })
}

//档案查询 和EDC质疑列表
export function getListQuestionRecord(data) {
    return request({
        url: `/question/listQuestionRecord`,
        method: 'post',
        data: data,
    })
}

// 查询日志接口
export function findRecord(params) {
    return request({
        url: `/template/f_medical_property/findValueChangeRecord`,
        method: 'GET',
        params,
    })
}
//随访处理阶段
export function handleStageList(params) {
    return request({
        url: `/follow/work/handle/stageList`,
        method: 'GET',
        params,
    })
}
//随访超窗阶段
export function overStageList(params) {
    return request({
        url: `/follow/work/over/stageList`,
        method: 'GET',
        params,
    })
}
//预警处理阶段
export function warnStageList(params) {
    return request({
        url: `/follow/work/warn/stageList`,
        method: 'GET',
        params,
    })
}
//患者退出列表审核
export function quitRecordFindPage(params) {
    return request({
        url: `/medical/quitRecord/findPage`,
        method: 'GET',
        params,
    })
}
// 患者退出列表审核接口
export function quitRecordUpdate(data) {
    return request({
        url: `/medical/quitRecord/update`,
        method: 'POST',
        data,
    })
}
// 医生代填随访数据插入接口
export function saveOrUpdatePatientRecord(data) {
    return request({
        url: '/follow/work/saveOrUpdatePatientRecord',
        method: 'POST',
        data: data
    })
}

// 随访工作站新增随访任务
export function addFollowTask(params) {
    return request({
        url: '/research/info/followUpTasksAreGeneratedManually',
        method: 'GET',
        params,
    })
}