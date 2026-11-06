import request from '@/utils/request'

// 查询体检筛查列表
export function listPhysical(query) {
  return request({ url: '/screening/physical/list', method: 'get', params: query })
}
// 查询体检筛查详情
export function getPhysical(id) {
  return request({ url: '/screening/physical/' + id, method: 'get' })
}
// 新增体检筛查
export function addPhysical(data) {
  return request({ url: '/screening/physical', method: 'post', data: data })
}
// 修改体检筛查
export function updatePhysical(data) {
  return request({ url: '/screening/physical', method: 'put', data: data })
}
// 删除体检筛查
export function delPhysical(ids) {
  return request({ url: '/screening/physical/' + ids, method: 'delete' })
}
