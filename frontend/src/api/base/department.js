import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listDepartment(query) {
  return request({
    url: '/system/department/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getDepart(id) {
  return request({
    url: '/system/department/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addDepart(data) {
  return request({
    url: '/system/department',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateDepart(data) {
  return request({
    url: '/system/department',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delDepart(id) {
  return request({
    url: '/system/department/' + id,
    method: 'delete'
  })
}

  
