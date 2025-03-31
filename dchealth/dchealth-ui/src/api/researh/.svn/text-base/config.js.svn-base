import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listResearchInfo(query) {
    return request({
        url: '/medical/researchInfo/list',
        method: 'get',
        params: query
    })
}

// 新增队列接口
export function addResearchInfo(data) {
    return request({
        url: '/medical/researchInfo',
        method: 'post',
        data: data
    })
}

// 修改队列接口
export function updateResearchInfo(data) {
    return request({
        url: '/medical/researchInfo',
        method: 'put',
        data: data
    })
}

// 删除队列接口
export function delResearchInfo(params) {
    return request({
        url: `/medical/researchInfo/`,
        method: 'delete',
        params
    })
}