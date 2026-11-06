import request from '@/utils/request'

// 查询【资源】列表
export function listSource(data) {
    return request({
        url: '/researh/source/list',
        method: 'post',
        data: data
    })
}

//修改资源
export function addSource(data) {
    return request({
      url: '/researh/source/add',
      method: 'post',
      data: data
    })
}

//修改资源
export function updateSource(data) {
  return request({
    url: '/researh/source/update',
    method: 'post',
    data: data
  })
}

//修改资源
export function treeSource(data) {
    return request({
      url: '/researh/source/treeResearchSource',
      method: 'post',
      data: data
    })
}

//删除资源
export function delSource(data) {
    return request({
      url: '/researh/source/delete',
      method: 'post',
      data: data
    })
}

//菜单更新/修改资源
export function updateRoleSource(data) {
    return request({
      url: '/researh/role/addRoleSource',
      method: 'post',
      data: data
    })
}

//数据权限详情接口
export function dataPermissionsData(researchId, roleId) {
    return request({
      url: '/researh/role/dataPermissions/' + researchId + "/" + roleId,
      method: 'get',
    })
}

