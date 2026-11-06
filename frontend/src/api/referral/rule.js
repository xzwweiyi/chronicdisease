import request from '@/utils/request'

// 查询转诊规则列表
export function listRule(query) {
  return request({ url: '/referral/rule/list', method: 'get', params: query })
}
// 查询转诊规则详情
export function getRule(id) {
  return request({ url: '/referral/rule/' + id, method: 'get' })
}
// 新增转诊规则
export function addRule(data) {
  return request({ url: '/referral/rule', method: 'post', data: data })
}
// 修改转诊规则
export function updateRule(data) {
  return request({ url: '/referral/rule', method: 'put', data: data })
}
// 删除转诊规则
export function delRule(ids) {
  return request({ url: '/referral/rule/' + ids, method: 'delete' })
}
