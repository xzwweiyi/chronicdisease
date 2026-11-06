import request from '@/utils/request'

// 查询管理路径列表
export function listPathway(query) {
  return request({
    url: '/pathway/definition/list',
    method: 'get',
    params: query
  })
}

// 查询管理路径详情
export function getPathway(id) {
  return request({
    url: '/pathway/definition/' + id,
    method: 'get'
  })
}

// 新增管理路径
export function addPathway(data) {
  return request({
    url: '/pathway/definition',
    method: 'post',
    data: data
  })
}

// 修改管理路径
export function updatePathway(data) {
  return request({
    url: '/pathway/definition',
    method: 'put',
    data: data
  })
}

// 删除管理路径
export function delPathway(id) {
  return request({
    url: '/pathway/definition/' + id,
    method: 'delete'
  })
}

// 更新路径状态（发布/关闭）
export function updatePathwayStatus(id, pathwayStatus) {
  return request({
    url: '/pathway/definition/status/' + id,
    method: 'put',
    params: { pathwayStatus }
  })
}

// 复制路径
export function copyPathway(sourceId) {
  return request({
    url: '/pathway/definition/copy/' + sourceId,
    method: 'post'
  })
}
