import request from '@/utils/request'

// 查询院外筛查列表
export function listOutside(query) {
  return request({ url: '/screening/outside/list', method: 'get', params: query })
}
// 查询院外筛查详情
export function getOutside(id) {
  return request({ url: '/screening/outside/' + id, method: 'get' })
}
// 新增院外筛查
export function addOutside(data) {
  return request({ url: '/screening/outside', method: 'post', data: data })
}
// 修改院外筛查
export function updateOutside(data) {
  return request({ url: '/screening/outside', method: 'put', data: data })
}
// 删除院外筛查
export function delOutside(ids) {
  return request({ url: '/screening/outside/' + ids, method: 'delete' })
}
