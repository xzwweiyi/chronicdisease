import request from '@/utils/request'
/**
 *  患者管理
 */
// 患者管理架构 列表
export function getDiseasesTreeList(params) {
    return request({
        url: '/research/diseases/treeList',
        method: 'GET',
        params
    })
}
// 患者列表
export function getPatientList(params) {
    return request({
        url: '/medical/patientInfo/list',
        method: 'GET',
        params
    })
}
// 患者列表 获取导出权限
export function getExportAuthority(params) {
    return request({
        url: '/system/user/judgeExportAuthority',
        method: 'GET',
        params
    })
}

// 患者详情 队列左侧树状数据
export function getPatientQueueTreeList(params) {
    return request({
        url: '/medical/patientInfo/treeList',
        method: 'GET',
        params
    })
}
// ===================  健康档案/体检档案   ====================
// 患者详情 健康档案左侧树状数据
export function getPatientHealthTreeList(params) {
    return request({
        url: '/medical/health/index',
        // url:'/health/index',
        method: 'POST',
        params
    })
}
// 患者详情 体检档案 左侧树状数据
export function getPatientPhysicalTreeList(params) {
    return request({
        url: '/medical/exam/index',
        method: 'POST',
        params
    })
}
// 患者详情 健康档案/体检档案---基础信息
export function getBasePatientInfo(params) {
    return request({
        url: `/medical/queryBaseInfo`,
        method: 'POST',
        params
    })
}
// 患者详情 健康档案/体检档案---一般检查
export function getPatientGeneralInfo(params) {
    return request({
        url: `/medical/queryGeneralInfo`,
        method: 'POST',
        params
    })
}
// 患者详情 健康档案/体检档案---诊断
export function getQueryDiagnosis(params) {
    return request({
        url: `/medical/queryDiagnosis`,
        method: 'POST',
        params
    })
}
// 患者详情 健康档案/体检档案---检查项目
export function getQueryCheckExamItems(params) {
    return request({
        url: `/medical/checkExamItemList`,
        method: 'POST',
        params
    })
}
// 患者详情 健康档案/体检档案---检验项目
export function getQueryCheckTestItems(params) {
    return request({
        url: `/medical/checkTestItemList`,
        method: 'POST',
        params
    })
}
// 患者详情 门诊记录--检验
export function getQueryMzTestItemList(params) {
    return request({
        url: `/medical/mzTestItemList`,
        method: 'POST',
        params
    })
}
// 患者详情 门诊记录--检查
export function getQueryMzExamItemList(params) {
    return request({
        url: `/medical/mzExamItemList`,
        method: 'POST',
        params
    })
}
// 患者详情 门诊记录--检查
export function getQueryMzDiagnosisList(params) {
    return request({
        url: `/medical/queryMzDiagnosis`,
        method: 'POST',
        params
    })
}

/** ================================ 预住院记录  ===================================== */
// 患者详情 住院记录
export function getAdmissionTestItemList(params) {
    return request({
        url: `/medical/preAdmissionTestItemList`,
        method: 'POST',
        params
    })
}
export function getQueryExamItemList(params) {
    return request({
        url: `/medical/preAdmissionExamItemList`,
        method: 'POST',
        params
    })
}
export function getQueryDiagnosisList(params) {
    return request({
        url: `/medical/preAdmissionDiagnosis`,
        method: 'POST',
        params
    })
}

// ===============================================================

// =======================================================

// 患者信息
export function getfMedicalInfoReq(params) {
    return request({
        url: '/template/f_medical_form/fMedicalInfoReq',
        method: 'GET',
        params
    })
}
// 患者详情
export function getPatientDetails(params){
    return request({
        url: `/template/f_medical_form/${params.patientId}/${params.researchId}/${params.followId}`,
        method: 'GET',
        params
    })
}

// 患者健康档案详情
export function queryMedicalExamForm(query){
    return request({
        url: `/template/f_medical_exam_form/queryMedicalExamForm`,
        method: 'GET',
        params: query
    })
}

// 患者 心电图报告 内容
export function getCheckPDFInfo(query){
    return request({
        url: `/medical/preview/electrocardiogram`,
        method: 'POST',
        params: query,
        responseType: 'blob'
    })
}