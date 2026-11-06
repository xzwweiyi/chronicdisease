import request from '@/utils/request'

// 查询数据检查配置列表
export function listConfig(query) {
    return request({
        url: '/tool/config/list',
        method: 'get',
        params: query
    })
}

// 查询数据检查配置详细
export function getConfig(id) {
    return request({
        url: '/tool/config/' + id,
        method: 'get'
    })
}

// 新增数据检查配置
export function addConfig(data) {
    return request({
        url: '/tool/config',
        method: 'post',
        data: data
    })
}

// 修改数据检查配置
export function updateConfig(data) {
    return request({
        url: '/tool/config',
        method: 'put',
        data: data
    })
}

// 删除数据检查配置
export function delConfig(id) {
    return request({
        url: '/tool/config/' + id,
        method: 'delete'
    })
}
