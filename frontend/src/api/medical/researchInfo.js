import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listDisease(query) {
    return request({
        url: '/medical/researchInfo/list',
        method: 'get',
        params: query
    })
}

// 查询【请填写功能名称】详细
export function getDisease(id) {
    return request({
        url: '/medical/researchInfo/' + id,
        method: 'get'
    })
}

// 新增【请填写功能名称】
export function addDisease(data) {
    return request({
        url: '/medical/researchInfo',
        method: 'post',
        data: data
    })
}

// 修改【请填写功能名称】
export function updateDisease(data) {
    return request({
        url: '/medical/researchInfo',
        method: 'put',
        data: data
    })
}

// 删除【请填写功能名称】
export function delDisease(id) {
    return request({
        url: '/medical/researchInfo/' + id,
        method: 'delete'
    })
}