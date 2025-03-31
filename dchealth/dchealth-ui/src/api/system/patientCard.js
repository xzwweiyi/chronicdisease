import request from '@/utils/request'

// 查询患者主号管理列表
export function listPatientCard(query) {
  return request({
    url: '/system/patientCard/list',
    method: 'get',
    params: query
  })
}

// 查询患者主号管理详细
export function getPatientCard(id) {
  return request({
    url: '/system/patientCard/' + id,
    method: 'get'
  })
}

// 新增患者主号管理
export function addPatientCard(data) {
  return request({
    url: '/system/patientCard',
    method: 'post',
    data: data
  })
}

// 修改患者主号管理
export function updatePatientCard(data) {
  return request({
    url: '/system/patientCard',
    method: 'put',
    data: data
  })
}

// 删除患者主号管理
export function delPatientCard(id) {
  return request({
    url: '/system/patientCard/' + id,
    method: 'delete'
  })
}



// 删除患者主号管理
export function testgetResarchInfo(id) {
  return request({
    url: '/follow/work/enrollment/getResarchInfo/' + id,
    method: 'get'
  })
}

