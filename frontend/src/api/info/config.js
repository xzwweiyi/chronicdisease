import request from '@/utils/request'

// 获取队列阶段
export function updateStageData(id, stage) {
    return request({
        url: '/research/info/updateStage/' + id + "/" + stage,
        method: 'get',
    })
}

// 队列新增
export function infoAdd(data) {
    return request({
        url: '/research/info/add',
        method: 'post',
        data: data
    })
}

// 队列列表
export function infoList(query) {
    return request({
        url: '/research/info/list',
        method: 'get',
        params: query
    })
}

// 队列详情
export function infoQuery(id) {
    return request({
        url: `/research/info/` + id,
        method: 'get',
    })
}

// 队列修改
export function infoEdit(data) {
    return request({
        url: '/research/info/edit',
        method: 'put',
        data: data
    })
}

// 队列删除
export function infoDel(id) {
    return request({
        url: '/research/info/updateDelFlag/' + id,
        method: 'delete',
    })
}

// 随访配置
export function recordTemplateAdd(data) {
    return request({
        url: '/medical/recordTemplate/add',
        method: 'post',
        data: data
    })
}

// 随访列表
export function recordTemplateList(query) {
    return request({
        url: '/medical/recordTemplate/list',
        method: 'get',
        params: query
    })
}

// 随访周期编辑
export function recordTemplateUpdate(data) {
    return request({
        url: 'medical/recordTemplate/edit',
        method: 'put',
        data: data
    })
}


// 随访配置删除
export function recordTemplateDel(followIds) {
    return request({
        url: '/medical/recordTemplate/' + followIds,
        method: 'delete',
    })
}

// 病种列表
export function listDiseases(query) {
    return request({
        url: '/research/diseases/list',
        method: 'get',
        params: query,
    })
}

// 病种新增
export function addDiseases(data) {
    return request({
        url: '/research/diseases',
        method: 'post',
        data: data,
    })
}

// 病种修改
export function editDiseases(data) {
    return request({
        url: '/research/diseases',
        method: 'put',
        data: data,
    })
}

// 病种删除
export function delDiseases(diseaseId) {
    return request({
        url: '/research/diseases/' + diseaseId,
        method: 'delete',
    })
}

// 病种结构
export function selectDiseases() {
    return request({
        url: '/research/diseases/select',
        method: 'get',
    })
}

// 病种下患教列表
export function listArticleData(query) {
    return request({
        url: '/research/article/listArticle',
        method: 'get',
        params: query,
    })
}

// 患教列表
export function listEduData(query) {
    return request({
        url: '/cms/dept/category/treeList',
        method: 'get',
        params: query,
    })
}

// 患教保存
export function articleAdd(data) {
    return request({
        url: '/research/article/add',
        method: 'post',
        data: data,
    })
}

// 患教移除
export function delArticle(query) {
    return request({
        url: '/research/article/removeArticle',
        method: 'delete',
        params: query
    })
}

// 患教列表
export function researchStatus(id, status) {
    return request({
        url: `/research/info/publishResearch/` + id + "/" + status,
        method: 'get',
    })
}
// 设置队列患教首页位状态
export function setindexPosition(data) {
    return request({
        url: `/research/article/indexPosition`,
        method: 'PUT',
        data
    })
}

// 队列负责人
export function getListRoleUser(params) {
    return request({
        url: `/system/user/listRoleUser`,
        method: 'get',
        params
    })
}

// 查询入组患者
export function listEnrollment(query) {
    return request({
        // url: `/medical/patientInfo/enrollment`,
        url: `/medical/patientInfo/patientEnrollmentInformation`,
        method: 'get',
        params: query
    })
}

// 查询所有入组患者id
export function listAllEnrollmentIds(query) {
    return request({
        // url: `/medical/patientInfo/enrollment`,
        url: `medical/patientInfo/patientEnrollmentInformationUnique`,
        method: 'get',
        params: query
    })
}

// 处理特殊展示
export function obesityShow(researchId) {
    return request({
        url: `/obesity/isShow/` + researchId,
        method: 'get'
    })
}

// 查询入组患者
export function listLefTree(query) {
    return request({
        url: `/medical/record/leftDetailTree`,
        method: 'get',
        params: query
    })
}

//查询未入组所有的患者
export function listNoEnrollment(query) {
    return request({
        url: `/medical/patientInfo/noEnrollment`,
        method: 'get',
        params: query
    })
}

//导入入组患者
export function subNoEnrollment(data) {
    return request({
        url: `/medical/patientInfo/importEnrollment`,
        method: 'post',
        data: data
    })
}
// 删除导入入组的患者
export function removeEnrollment(ids) {
    return request({
        url: `/medical/patientInfo/deleteEnrollment/${ids}`,
        method: 'DELETE',
    })
}


// 导入随访患者-电子档案-检索树查询
export function getEnrollmentConditionSearchTree() {
    return request({
        url: `/medical/enrollmentCondition/searchTree`,
        method: 'GET',
    })
}
// 导入随访患者-电子档案-检索
export function getEnrollmentConditionList(data) {
    return request({
        url: `/medical/enrollmentCondition/search`,
        method: 'POST',
        data
    })
}
// 导入随访患者-电子档案-保存
export function saveEnrollmentCondition(params) {
    return request({
        url: `/medical/enrollmentCondition/add`,
        method: 'POST',
        data: params
    })
}
// 导入随访患者-电子档案-查询
export function getEnrollmentCondition(params) {
    return request({
        url: `/medical/enrollmentCondition/detail`,
        method: 'GET',
        params
    })
}

// 查询队列 /dchealthadmin/medical/patientInfo/enrollmentList
export function listEnrollmentList(data) {
    return request({
        url: `/medical/patientInfo/enrollmentList`,
        method: 'POST',
        data
    })
}

// 确定入组其他队列  /dchealthadmin/medical/patientInfo/moveEnrollment
export function moveEnrollment(data) {
    return request({
        url: `/medical/patientInfo/moveEnrollment`,
        method: 'POST',
        data
    })
}

// 关闭统计
export function closeStatistics(id) {
    return request({
        url: `/research/info/isFollowRate/${id}`,
        method: 'put',
    })
}

export function getFollowupDiseaseOption(query) {
    return request({
        url: '/research/diseases/getList',
        method: 'get',
        params: query
    })
}

// 嵌套360获取360url接口
export function get360Url(patientId) {
    return request({
        url: `/system/patient/get360Url?patientId=${patientId}`,
        method: 'GET',
    })
}