import request from '@/utils/request'

// 查询增量患者号列表
export function listPatientCardNotNull(query) {
  return request({
    url: '/system/patientCardNotNull/list',
    method: 'get',
    params: query
  })
}

// 查询增量患者号详细
export function getPatientCardNotNull(id) {
  return request({
    url: '/system/patientCardNotNull/' + id,
    method: 'get'
  })
}

// 新增增量患者号
export function addPatientCardNotNull(data) {
  return request({
    url: '/system/patientCardNotNull',
    method: 'post',
    data: data
  })
}

// 修改增量患者号
export function updatePatientCardNotNull(data) {
  return request({
    url: '/system/patientCardNotNull',
    method: 'put',
    data: data
  })
}

// 删除增量患者号
export function delPatientCardNotNull(id) {
  return request({
    url: '/system/patientCardNotNull/' + id,
    method: 'delete'
  })
}
