import request from '@/utils/request'

// 查询筛查申请列表
export function listApply(query) {
  return request({ url: '/screening/apply/list', method: 'get', params: query })
}
// 查询筛查申请详情
export function getApply(id) {
  return request({ url: '/screening/apply/' + id, method: 'get' })
}
// 新增筛查申请
export function addApply(data) {
  return request({ url: '/screening/apply', method: 'post', data: data })
}
// 修改筛查申请
export function updateApply(data) {
  return request({ url: '/screening/apply', method: 'put', data: data })
}
// 删除筛查申请
export function delApply(ids) {
  return request({ url: '/screening/apply/' + ids, method: 'delete' })
}
