import request from '@/utils/request'

// 查询远程排班列表
export function listSchedule(query) {
  return request({ url: '/telemedicine/schedule/list', method: 'get', params: query })
}
// 查询远程排班详情
export function getSchedule(id) {
  return request({ url: '/telemedicine/schedule/' + id, method: 'get' })
}
// 新增远程排班
export function addSchedule(data) {
  return request({ url: '/telemedicine/schedule', method: 'post', data: data })
}
// 修改远程排班
export function updateSchedule(data) {
  return request({ url: '/telemedicine/schedule', method: 'put', data: data })
}
// 删除远程排班
export function delSchedule(ids) {
  return request({ url: '/telemedicine/schedule/' + ids, method: 'delete' })
}
