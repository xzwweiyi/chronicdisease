import request from '@/utils/request'

// 查询项目专家列表
export function listDoctor(query) {
  return request({
    url: '/system/doctor/list',
    method: 'get',
    params: query
  })
}

// 查询项目专家详细
export function getDoctor(id) {
  return request({
    url: '/system/doctor/' + id,
    method: 'get'
  })
}

// 新增项目专家
export function addDoctor(data) {
  return request({
    url: '/system/doctor',
    method: 'post',
    data: data
  })
}

// 修改项目专家
export function updateDoctor(data) {
  return request({
    url: '/system/doctor',
    method: 'put',
    data: data
  })
}

// 删除项目专家
export function delDoctor(id) {
  return request({
    url: '/system/doctor/' + id,
    method: 'delete'
  })
}
