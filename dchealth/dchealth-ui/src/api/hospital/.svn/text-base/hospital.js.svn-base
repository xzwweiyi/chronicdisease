import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listHospital(query) {
    return request({
        url: '/system/hospital/list',
        method: 'get',
        params: query
    })
}

// 查询【请填写功能名称】详细
export function getHospital(id) {
    return request({
        url: '/system/hospital/' + id,
        method: 'get'
    })
}

// 新增【请填写功能名称】
export function addHospital(data) {
    return request({
        url: '/system/hospital',
        method: 'post',
        data: data
    })
}

// 修改【请填写功能名称】
export function updateHospital(data) {
    return request({
        url: '/system/hospital',
        method: 'put',
        data: data
    })
}

// 删除【请填写功能名称】
export function delHospital(id) {
    return request({
        url: '/system/hospital/' + id,
        method: 'delete'
    })
}
// 删除【请填写功能名称】
export function getAreaList(areaId) {
    let url = `/system/area/findAreaListByParentId`
    if (areaId) {
        url = `${url}?areaId=${areaId}`
    }
    return request({
        url: url,
        method: 'post',
    })
}