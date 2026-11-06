import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listComment(query) {
  return request({
    url: '/cms/comment/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getComment(id) {
  return request({
    url: '/cms/comment/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addComment(data) {
  return request({
    url: '/cms/comment',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateComment(data) {
  return request({
    url: '/cms/comment',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delComment(id) {
  return request({
    url: '/cms/comment/' + id,
    method: 'delete'
  })
}
