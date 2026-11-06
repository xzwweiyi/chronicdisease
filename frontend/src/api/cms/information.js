import request from '@/utils/request'
/**
 * /cms/information 资讯相关、患教相关
 * @param {*} query 
 * @returns 
 */

// 查询【资讯管理】列表
export function listIndexInformation(query) {
    return request({
        url: '/cms/default/appArticle/informationList',
        method: 'get',
        params: query
    })
}
// 新增【资讯管理】
export function addIndexInformation(data) {
    return request({
        url: `/cms/default/appArticle`,
        method: 'post',
        data: data
    })
}

// 修改【资讯管理】
export function updateIndexInformation(data) {
    return request({
        url: `/cms/default/appArticle`,
        method: 'put',
        data: data
    })
}

// 删除【资讯管理】
export function deleteIndexInformation(id) {
    return request({
        url: `/cms/default/appArticle/` + id,
        method: 'delete'
    })
}
// 查询【资讯管理】详细
export function getIndexInformationDetail(id) {
    return request({
        url: '/cms/default/appArticle/' + id,
        method: 'get'
    })
}
// 获取患者端首页互联网医院开关状态
export function getButtonIsShow(){
    return request({
        url: '/getIsShow',
        method: 'get',
    })
}

// 获取患者端首页互联网医院开关状态
export function setButtonIsShow(data){
    return request({
        url: '/settingIsShow',
        method: 'post',
        data
    })
}
