import request from '@/utils/request'
/**
 *  CRF库分类  
 */
// CRF 分类 列表
export function getCRFTemplateData(params) {
    return request({
        url: '/template/category/list',
        method: 'GET',
        params
    })
}
// 新增 CRf
export function addCategoryInfo(data, type) {
    let url = '/template/category'
    if (type == '0') {
        url = '/template/dept/category'
    }
    return request({
        url,
        method: 'POST',
        data
    })
}
// 修改 CRf
export function editCategoryInfo(data, type) {
    let url = '/template/category'
    if (type == '0') {
        url = '/template/dept/category'
    }
    return request({
        url,
        method: 'PUT',
        data
    })
}
// 删除 CRf
export function deleteCategoryInfo(templateCategoryId, type) {
    let url = '/template/category/'
    if (type == '0') {
        url = '/template/dept/category/'
    }
    return request({
        url: url + templateCategoryId,
        method: 'DELETE',
    })
}

/**
 * CRF 列表
 */
// 获取CRF模板列表
export function getCRFMedicalFormData(params) {
    return request({
        url: '/template/f_template/list',
        method: 'GET',
        params
    })
}
// 新增 CRf 模板
export function addCRFTemplateInfo(data, type) {
    let url = '/template/f_template'
    if (type == '0') {
        url = '/template/dept/f_template'
    }
    return request({
        url,
        method: 'POST',
        data
    })
}
// 修改 CRf 模板
export function editCRFTemplateInfo(data, type) {
    let url = '/template/f_template'
    if (type == '0') {
        url = '/template/dept/f_template'
    }
    return request({
        url,
        method: 'PUT',
        data
    })
}
// 删除 CRf 模板
export function deleteCRFTemplateInfo(templateIds, type) {
    let url = '/template/f_template/'
    if (type == '0') {
        url = '/template/dept/f_template/'
    }
    return request({
        url: url + templateIds,
        method: 'DELETE',
    })
}
// 新增模板到科室库
export function addCRFTemplateForDept(data) {
    return request({
        url: '/template/fDeptTemplate',
        method: 'POST',
        data
    })
}

// 复制模板到科室库或者公共库
export function copyCRFTemplateto(templateFormId, targetLibraryType) {
    return request({
        url: `/template/f_template_form/${templateFormId}/${targetLibraryType}`,
        method: 'POST',
    })
}

// 复制二级文件夹到科室库或者公共库
export function copyCRFfolderto(templateFormId, targetLibraryType) {
    return request({
        url: `/template/f_template/copy/${templateFormId}/${targetLibraryType}`,
        method: 'POST',
    })
}
