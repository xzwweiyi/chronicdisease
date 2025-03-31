import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listBanner(query) {
    return request({
        url: '/cms/banner/list',
        method: 'get',
        params: query
    })
}

// 查询【请填写功能名称】详细
export function getBanner(id) {
    return request({
        url: '/cms/banner/' + id,
        method: 'get'
    })
}

// 新增【请填写功能名称】
export function addBanner(data) {
    return request({
        url: '/cms/banner',
        method: 'post',
        data: data
    })
}

// 修改【请填写功能名称】
export function updateBanner(data) {
    return request({
        url: '/cms/banner',
        method: 'put',
        data: data
    })
}

// 删除【请填写功能名称】
export function delBanner(id) {
    return request({
        url: '/cms/banner/' + id,
        method: 'delete'
    })
}
// 添加资讯
export function addInformation(data) {
    return request({
        url: '/cms/banner/saveArticleAppBanner?ids='+data,
        method: 'post',
    })
}