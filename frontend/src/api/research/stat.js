import request from '@/utils/request'

// 查询中心统计列表
export function listStat(query) {
  return request({
    url: '/system/stat/list',
    method: 'get',
    params: query
  })
}

// 查询中心统计详细
export function getStat(id) {
  return request({
    url: '/system/stat/' + id,
    method: 'get'
  })
}

// 新增中心统计
export function addStat(data) {
  return request({
    url: '/system/stat',
    method: 'post',
    data: data
  })
}

// 修改中心统计
export function updateStat(data) {
  return request({
    url: '/system/stat',
    method: 'put',
    data: data
  })
}

// 删除中心统计
export function delStat(id) {
  return request({
    url: '/system/stat/' + id,
    method: 'delete'
  })
}
