import request from '@/utils/request'

// 查询计划入组日志列表
export function listLog(query) {
  return request({
    url: '/system/log/list',
    method: 'get',
    params: query
  })
}

// 查询计划入组日志详细
export function getLog(id) {
  return request({
    url: '/system/log/' + id,
    method: 'get'
  })
}

// 新增计划入组日志
export function addLog(data) {
  return request({
    url: '/system/log',
    method: 'post',
    data: data
  })
}

// 修改计划入组日志
export function updateLog(data) {
  return request({
    url: '/system/log',
    method: 'put',
    data: data
  })
}

// 删除计划入组日志
export function delLog(id) {
  return request({
    url: '/system/log/' + id,
    method: 'delete'
  })
}
