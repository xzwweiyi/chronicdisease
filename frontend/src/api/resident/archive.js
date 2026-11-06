import request from '@/utils/request'

// 档案列表（JOIN查询）
export function listArchive(query) {
  return request({ url: '/resident/archive/list', method: 'get', params: query })
}
// 统计卡片
export function getArchiveStats() {
  return request({ url: '/resident/archive/stats', method: 'get' })
}
// 各机构慢病分布
export function getArchiveDistribution() {
  return request({ url: '/resident/archive/distribution', method: 'get' })
}
// 跨机构调取日志
export function getCrossOrgLog() {
  return request({ url: '/resident/archive/cross-org-log', method: 'get' })
}
// 档案详情（JOIN数据）
export function getArchiveDetail(personId) {
  return request({ url: '/resident/archive/detail/' + personId, method: 'get' })
}
// 单条
export function getArchive(id) {
  return request({ url: '/resident/archive/' + id, method: 'get' })
}
// CRUD
export function addArchive(data) {
  return request({ url: '/resident/archive', method: 'post', data: data })
}
export function updateArchive(data) {
  return request({ url: '/resident/archive', method: 'put', data: data })
}
export function delArchive(ids) {
  return request({ url: '/resident/archive/' + ids, method: 'delete' })
}
// 诊断记录
export function getDiagnosis(personId) {
  return request({ url: '/resident/archive/detail/' + personId + '/diagnosis', method: 'get' })
}
// 用药记录
export function getMedications(personId) {
  return request({ url: '/resident/archive/detail/' + personId + '/medications', method: 'get' })
}
// 就诊记录
export function getVisits(personId) {
  return request({ url: '/resident/archive/detail/' + personId + '/visits', method: 'get' })
}
// 分级记录
export function getGradingRecords(personId) {
  return request({ url: '/resident/archive/detail/' + personId + '/grading', method: 'get' })
}
export function saveGrading(personId, data) {
  return request({ url: '/resident/archive/detail/' + personId + '/grading', method: 'post', data: data })
}
