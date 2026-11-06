import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listStat(query) {
  return request({
    url: '/cms/stat/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getStat(id) {
  return request({
    url: '/cms/stat/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addStat(data) {
  return request({
    url: '/cms/stat',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateStat(data) {
  return request({
    url: '/cms/stat',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delStat(id) {
  return request({
    url: '/cms/stat/' + id,
    method: 'delete'
  })
}
