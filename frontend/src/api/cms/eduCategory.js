import request from '@/utils/request'

// 查询宣教分类列表（分页）
export function listEduCategory(query) {
    return request({
        url: '/cms/eduCategory/list',
        method: 'get',
        params: query
    })
}

// 查询全部宣教分类（含文章数量，不分页，供宣教知识库分类导航使用）
export function listEduCategoryAll(query) {
    return request({
        url: '/cms/eduCategory/all',
        method: 'get',
        params: query
    })
}

// 查询宣教分类详细
export function getEduCategory(id) {
    return request({
        url: '/cms/eduCategory/' + id,
        method: 'get'
    })
}

// 新增宣教分类
export function addEduCategory(data) {
    return request({
        url: '/cms/eduCategory',
        method: 'post',
        data: data
    })
}

// 修改宣教分类
export function updateEduCategory(data) {
    return request({
        url: '/cms/eduCategory',
        method: 'put',
        data: data
    })
}

// 删除宣教分类
export function delEduCategory(id) {
    return request({
        url: '/cms/eduCategory/' + id,
        method: 'delete'
    })
}
