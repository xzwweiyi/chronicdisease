import request from '@/utils/request'

// 查询AI评测项目列表
export function listProject(query) {
  return request({ url: '/aieval/project/list', method: 'get', params: query })
}
// 查询慢病路径管理下已发布的队列列表（AI评估分析主体）
export function listQueue(query) {
  return request({ url: '/aieval/data/queue/list', method: 'get', params: query })
}
// 查询AI评测项目详情
export function getProject(id) {
  return request({ url: '/aieval/project/' + id, method: 'get' })
}
// 新增AI评测项目
export function addProject(data) {
  return request({ url: '/aieval/project', method: 'post', data: data })
}
// 修改AI评测项目
export function updateProject(data) {
  return request({ url: '/aieval/project', method: 'put', data: data })
}
// 删除AI评测项目
export function delProject(ids) {
  return request({ url: '/aieval/project/' + ids, method: 'delete' })
}

// 查询AI评估数据列表
export function listEvaluationData(query) {
  return request({ url: '/aieval/data/list', method: 'get', params: query })
}
// 查询AI评估数据详情
export function getEvaluationData(id) {
  return request({ url: '/aieval/data/' + id, method: 'get' })
}
// 新增AI评估数据
export function addEvaluationData(data) {
  return request({ url: '/aieval/data', method: 'post', data: data })
}
// 修改AI评估数据
export function updateEvaluationData(data) {
  return request({ url: '/aieval/data', method: 'put', data: data })
}
// 删除AI评估数据
export function delEvaluationData(ids) {
  return request({ url: '/aieval/data/' + ids, method: 'delete' })
}
// AI分析：调用DeepSeek大模型输出分析结果与下一步措施
export function analyzeEvaluationData(id) {
  return request({ url: '/aieval/data/analyze/' + id, method: 'post' })
}
// 生成评估数据：根据队列下患者已填写完成的历次表单数据自动生成
export function generateEvaluationData(data) {
  return request({ url: '/aieval/data/generate', method: 'post', data: data })
}
