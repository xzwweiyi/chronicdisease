import request from '@/utils/request'

// 按疾病类型分页列表
export function getDiseaseList(disease, query) {
  return request({ url: '/resident/disease/' + disease + '/list', method: 'get', params: query })
}
// 疾病专项统计
export function getDiseaseStats(disease) {
  return request({ url: '/resident/disease/' + disease + '/stats', method: 'get' })
}
