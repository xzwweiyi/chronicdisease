import request from '@/utils/request'

// 查询数据检查列表
export function listCheck(query) {
    return request({
        url: '/tool/check/list',
        method: 'get',
        params: query
    })
}

// 查询数据检查详细
export function getCheck(id) {
    return request({
        url: '/tool/check/' + id,
        method: 'get'
    })
}

// 新增数据检查
export function addCheck(data) {
    return request({
        url: '/tool/check',
        method: 'post',
        data: data
    })
}

// 修改数据检查
export function updateCheck(data) {
    return request({
        url: '/tool/check',
        method: 'put',
        data: data
    })
}

// 删除数据检查
export function delCheck(id) {
    return request({
        url: '/tool/check/' + id,
        method: 'delete'
    })
}
