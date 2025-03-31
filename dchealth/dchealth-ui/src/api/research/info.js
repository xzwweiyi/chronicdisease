import request from '@/utils/request'

// 查询项目管理列表
export function listInfo(query) {
  return request({
    url: '/medical/research_info/list',
    method: 'get',
    params: query
  })
}

// 查询项目管理详细
export function getInfo(id) {
  return request({
    url: '/medical/research_info/' + id,
    method: 'get'
  })
}

// 新增项目管理
export function addInfo(data) {
  return request({
    url: '/medical/research_info',
    method: 'post',
    data: data
  })
}

// 修改项目管理
export function updateInfo(data) {
  return request({
    url: '/medical/research_info',
    method: 'put',
    data: data
  })
}

// 删除项目管理
export function delInfo(id) {
  return request({
    url: '/medical/research_info/' + id,
    method: 'delete'
  })
}
 // 队列
 export function postAsync(query) {
  return request({
    url: '/medical/research_info/asyncTree',
    method: 'post',
    params: query
  })
}

// 队列-基础信息
export function getUserInfo(id) {
  return request({
    url: '/medical/patient_records_status/detail_edc_patient/' + id,
    method: 'get'
  })
}

// 队列-周期获取信息
export function getUserInfoUp(id) {
    return request({
      url: '/medical/patient_records/detail_edc_up/' + id,
      method: 'get'
    })
}

// 队列-周期不可编辑
export function getUserInfoLeft(id) {
    return request({
      url: '/medical/patient_records/detail_edc_left/' + id,
      method: 'get'
    })
}

// 队列-周期可编辑
export function getUserInfoRight(id) {
    return request({
      url: '/medical/patient_records/detail_edc_right/' + id,
      method: 'get'
    })
}
// 队列-患者列表
export function getresearchPatient(params) {
    return request({
      url: `research/info/getResearchPatientList`,
      method: 'get',
      params
    })
}
