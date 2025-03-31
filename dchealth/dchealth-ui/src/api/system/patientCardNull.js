import request from '@/utils/request'

// 查询空时患者号列表
export function listPatientCardNull(query) {
  return request({
    url: '/system/patientCardNull/list',
    method: 'get',
    params: query
  })
}

// 查询空时患者号详细
export function getPatientCardNull(id) {
  return request({
    url: '/system/patientCardNull/' + id,
    method: 'get'
  })
}

// 新增空时患者号
export function addPatientCardNull(data) {
  return request({
    url: '/system/patientCardNull',
    method: 'post',
    data: data
  })
}

// 修改空时患者号
export function updatePatientCardNull(data) {
  return request({
    url: '/system/patientCardNull',
    method: 'put',
    data: data
  })
}

// 删除空时患者号
export function delPatientCardNull(id) {
  return request({
    url: '/system/patientCardNull/' + id,
    method: 'delete'
  })
}
