import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listRole(data) {
    return request({
        url: '/researh/role/list',
        method: 'post',
        data: data
    })
}

//新增角色
export function addRole(data) {
  return request({
    url: '/researh/role/add',
    method: 'post',
    data: data
  })
}

//删除角色
export function delRole(data) {
    return request({
      url: '/researh/role/delete',
      method: 'post',
      data: data
    })
}

// 查询菜单
export function getSourceByRoleId(data) {
    return request({
      url: '/researh/source/findResearchSourceByRoleId',
      method: 'post',
      data: data,
    })
}

// 更新/修改菜单
export function updateRole(data) {
    return request({
      url: '/researh/role/update',
      method: 'post',
      data: data,
    })
  }

// 更改角色状态
export function changeRoleStatus(data) {
  return request({
    url: '/researh/role/updateStatus',
    method: 'post',
    data: data
  })
}

// 根据角色id查询已分配
export function listFindAllUserByRole(data) {
    return request({
      url: '/researh/role/findAllocatedUserByRoleId',
      method: 'post',
      data: data
    })
}

// 查询部门数据权限
export function listFindRoleAuthByRoleId(data) {
  return request({
    url: '/researh/role/findModulehByResearchId',
    method: 'post',
    data: data
  })
}

// 查询分配部门数据权限
export function listFindDeptByRoleId(data) {
    return request({
      url: '/researh/role/findDeptByRoleId',
      method: 'post',
      data: data
    })
}

// 根据角色id查询未分配全部用户
export function listAllUserByRole(data) {
    return request({
      url: '/researh/role/findUnAllocatedUserByRoleId',
      method: 'post',
      data: data
    })
}

// 根据角色id查询部门数据权限
export function getFindRoleByRoleId(data) {
    return request({
        url: '/researh/role/findRoleAuthByRoleId',
        method: 'post',
        data: data
    })
}

// 根据角色id查分配的模块
export function getFindModuleByRoleId(data) {
    return request({
        url: '/researh/role/findModuleAuthhByRoleId',
        method: 'post',
        data: data
    })
}

// 为角色批量分配用户
export function authUserByRole(data) {
    return request({
        url: '/researh/role/batchAuthUserByRole',
        method: 'post',
        data: data,
    })
}

// 取消授权用户
export function cancelUserRole(data) {
    return request({
        url: '/researh/role/cancelAuthRole',
        method: 'post',
        data: data, 
    })
}

// 批量取消授权用户
export function cancelAllUserRole(data) {
    return request({
        url: '/researh/role/batchCancelAuthRole',
        method: 'post',
        data: data, 
    })
}

// 修改保存数据权限
export function updateData (data) {
    return request({
        url: '/researh/role/saveDataPermissions',
        method: 'post',
        data: data
    })
}

