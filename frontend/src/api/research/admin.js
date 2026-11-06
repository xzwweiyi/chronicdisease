import request from '@/utils/request'

// 查询项目管理员列表
export function listAdmin(query) {
  return request({
    url: '/system/admin/list',
    method: 'get',
    params: query
  })
}

// 查询项目管理员详细
export function getAdmin(id) {
  return request({
    url: '/system/admin/' + id,
    method: 'get'
  })
}

// 新增项目管理员
export function addAdmin(data) {
  return request({
    url: '/system/admin',
    method: 'post',
    data: data
  })
}

// 修改项目管理员
export function updateAdmin(data) {
  return request({
    url: '/system/admin',
    method: 'put',
    data: data
  })
}

// 删除项目管理员
export function delAdmin(id) {
  return request({
    url: '/system/admin/' + id,
    method: 'delete'
  })
}
