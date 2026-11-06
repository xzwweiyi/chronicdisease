import request from '@/utils/request'

// 查询AI评测规则列表
export function listAiEvalRule(query) {
  return request({ url: '/aieval/rule/list', method: 'get', params: query })
}
// 查询AI评测规则详情
export function getAiEvalRule(id) {
  return request({ url: '/aieval/rule/' + id, method: 'get' })
}
// 新增AI评测规则
export function addAiEvalRule(data) {
  return request({ url: '/aieval/rule', method: 'post', data: data })
}
// 修改AI评测规则
export function updateAiEvalRule(data) {
  return request({ url: '/aieval/rule', method: 'put', data: data })
}
// 删除AI评测规则
export function delAiEvalRule(ids) {
  return request({ url: '/aieval/rule/' + ids, method: 'delete' })
}
