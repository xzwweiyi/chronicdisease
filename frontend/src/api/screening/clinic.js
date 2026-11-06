import request from '@/utils/request'

// 查询门诊筛查列表
export function listClinic(query) {
  return request({ url: '/screening/clinic/list', method: 'get', params: query })
}
// 查询门诊筛查详情
export function getClinic(id) {
  return request({ url: '/screening/clinic/' + id, method: 'get' })
}
// 新增门诊筛查
export function addClinic(data) {
  return request({ url: '/screening/clinic', method: 'post', data: data })
}
// 修改门诊筛查
export function updateClinic(data) {
  return request({ url: '/screening/clinic', method: 'put', data: data })
}
// 删除门诊筛查
export function delClinic(ids) {
  return request({ url: '/screening/clinic/' + ids, method: 'delete' })
}
