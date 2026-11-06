import request from '@/utils/request'

// 查询项目管理列表
export function listDisease(query) {
    return request({
        url: '/research/info/list',
        method: 'get',
        params: query
    })
}

// 查询项目管理详细
export function getDisease(id) {
    return request({
        url: '/research/info/' + id,
        method: 'get'
    })
}

// 新增项目管理
export function addDisease(data) {
    return request({
        url: '/research/info/add',
        method: 'post',
        data: data
    })
}

// 修改项目管理
export function updateDisease(data) {
    return request({
        url: '/research/info/edit',
        method: 'put',
        data: data
    })
}

// 删除项目管理
export function delDisease(id) {
    return request({
        url: '/research/info/' + id,
        method: 'delete'
    })
}

// 地区树结构
export function getRegionTreeData(){
    return request({
        url:`/system/area/regionTreeData`,
        method:'GET'
    })
}
