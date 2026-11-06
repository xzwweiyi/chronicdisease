import request from '@/utils/request'

// 查询远程会诊列表
export function listConsultation(query) {
  return request({ url: '/telemedicine/consultation/list', method: 'get', params: query })
}
// 查询远程会诊详情
export function getConsultation(id) {
  return request({ url: '/telemedicine/consultation/' + id, method: 'get' })
}
// 新增远程会诊
export function addConsultation(data) {
  return request({ url: '/telemedicine/consultation', method: 'post', data: data })
}
// 修改远程会诊
export function updateConsultation(data) {
  return request({ url: '/telemedicine/consultation', method: 'put', data: data })
}
// 删除远程会诊
export function delConsultation(ids) {
  return request({ url: '/telemedicine/consultation/' + ids, method: 'delete' })
}
