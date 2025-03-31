import request from '@/utils/request'
// / 随访分布统计 科室下拉
export function getFollowupDiseaseOption(query) {
    return request({
        url: '/research/diseases/list2',
        method: 'get',
        params: query
    })
}

// 随访分布统计
export function getFollowDistribute(query) {
    return request({
        url: '/statistics/followDistribute',
        method: 'get',
        params: query
    })
}
// 随访分布统计-队列状态数统计
export function getFollowStatusNum(query) {
    return request({
        url: '/statistics/researchStatusNum',
        method: 'get',
        params: query
    })
}

// 队列患者分布
export function getPatientDistribute(query) {
    return request({
        url: '/statistics/patientDistribute',
        method: 'get',
        params: query
    })
}
// 院级全景图
export function getHospitalView(query) {
    return request({
        url: '/statistics/hospitalView',
        method: 'get',
        params: query
    })
}
// 院级全景图
export function getWorkLoadData(params) {
    return request({
        url: '/system/user/medicalWorkloadList',
        method: 'get',
        params
    })
}
// 患者来源
export function getPatientDistributeOrgin(params){
    return request({
        url:'/statistics/queryPatientDistribute',
        method:'GET',
        params
    })
}


