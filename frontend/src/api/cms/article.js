import request from '@/utils/request'
/**
 * /cms/article 资讯相关、患教相关
 * @param {*} query 
 * @returns 
 */

// 查询【资讯管理】列表
export function listInformationArticle(query) {
    return request({
        url: '/cms/article/informationList',
        method: 'get',
        params: query
    })
}
// 新增【资讯管理】
export function addInformationArticle(data) {
    return request({
        url: `/cms/appArticle`,
        method: 'post',
        data: data
    })
}

// 修改【资讯管理】
export function updateInformationArticle(data) {
    return request({
        url: `/cms/appArticle`,
        method: 'put',
        data: data
    })
}

// 删除【资讯管理】
export function delInformationArticle(id) {
    return request({
        url: `/cms/appArticle/` + id,
        method: 'delete'
    })
}
// 查询【资讯管理】详细
export function getInformationArticleDetail(id) {
    return request({
        url: '/cms/appArticle/' + id,
        method: 'get'
    })
}

// 上传图片文件功能
export function uploadImage(file) {
    return request({
        url: '/common/upload',
        method: 'post',
        data: file
    })
}