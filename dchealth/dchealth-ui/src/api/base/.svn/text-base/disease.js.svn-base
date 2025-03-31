import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listDisease(query) {
    return request({
        url: '/medical/research_info/list',
        method: 'get',
        params: query
    })
}

// 查询【请填写功能名称】详细
export function getDisease(id) {
    return request({
        url: '/medical/research_info/' + id,
        method: 'get'
    })
}

// 新增【请填写功能名称】
export function addDisease(data) {
    return request({
        url: '/medical/research_info',
        method: 'post',
        data: data
    })
}

// 修改【请填写功能名称】
export function updateDisease(data) {
    return request({
        url: '/medical/research_info',
        method: 'put',
        data: data
    })
}

// 删除【请填写功能名称】
export function delDisease(id) {
    return request({
        url: '/medical/research_info/' + id,
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