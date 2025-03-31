import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listCollect(query) {
  return request({
    url: '/system/collect/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getCollect(id) {
  return request({
    url: '/system/collect/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addCollect(data) {
  return request({
    url: '/system/collect',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateCollect(data) {
  return request({
    url: '/system/collect',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delCollect(id) {
  return request({
    url: '/system/collect/' + id,
    method: 'delete'
  })
}
