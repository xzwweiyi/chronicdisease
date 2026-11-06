import request from '@/utils/request'

// 查询转诊记录列表
export function listTransfer(query) {
  return request({ url: '/referral/transfer/list', method: 'get', params: query })
}
// 查询转诊记录详情
export function getTransfer(id) {
  return request({ url: '/referral/transfer/' + id, method: 'get' })
}
// 新增转诊记录
export function addTransfer(data) {
  return request({ url: '/referral/transfer', method: 'post', data: data })
}
// 修改转诊记录
export function updateTransfer(data) {
  return request({ url: '/referral/transfer', method: 'put', data: data })
}
// 删除转诊记录
export function delTransfer(ids) {
  return request({ url: '/referral/transfer/' + ids, method: 'delete' })
}
