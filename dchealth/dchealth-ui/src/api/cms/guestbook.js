import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listGuestbook(query) {
  return request({
    url: '/cms/guestbook/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getGuestbook(id) {
  return request({
    url: '/cms/guestbook/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addGuestbook(data) {
  return request({
    url: '/cms/guestbook',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateGuestbook(data) {
  return request({
    url: '/cms/guestbook',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delGuestbook(id) {
  return request({
    url: '/cms/guestbook/' + id,
    method: 'delete'
  })
}
