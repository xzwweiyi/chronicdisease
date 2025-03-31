import request from '@/utils/request'

/**
 *  ========================== 随访CRF 测评 =======================
 */

// 随访 评测列表
export function getEvaluationFollow(params) {
    return request({
        url: `/template/evaluation/list`,
        method: 'GET',
        params,
    })
}
// 随访 评测删除
export function deleteEvaluationFollow(id) {
    return request({
        url: `/template/evaluation/delete/${id}`,
        method: 'GET',
    })
}
// 随访 评测编辑
export function editEvaluationFollow(params) {
    return request({
        url: `/template/evaluation/edit`,
        method: 'POST',
        data: params,
    })
}

// 随访 评测添加
export function addEvaluationFollow(params) {
    return request({
        url: `/template/evaluation/add`,
        method: 'POST',
        data: params,
    })
}

/**
 * ======================== 标准量表测评 ======================
 */
// 标准量表 评测列表
export function getEvaluationLibrary(params) {
    return request({
        url: `/template/f_library_form_set_evaluation/list`,
        method: 'GET',
        params,
    })
}
// 标准量表 评测删除
export function deleteEvaluationLibrary(id) {
    return request({
        url: `/template/f_library_form_set_evaluation/delete/${id}`,
        method: 'GET',
    })
}
// 标准量表 评测编辑
export function editEvaluationLibrary(params) {
    return request({
        url: `/template/f_library_form_set_evaluation/edit`,
        method: 'POST',
        data: params,
    })
}

// 标准量表 评测添加
export function addEvaluationLibrary(params) {
    return request({
        url: `/template/f_library_form_set_evaluation/add`,
        method: 'POST',
        data: params,
    })
}

/**
 * ======================== CRF模板 测评 ======================
 */
// CRF模板 评测列表
export function getEvaluationTemplate(params,type) {
    let url = `/template/f_template_form_set_evaluation/list`
    if (type == '0') {
        url = `/template/dept/f_template_form_set_evaluation/list`
    }
    return request({
        url,
        method: 'GET',
        params,
    })
}
// CRF模板 评测删除
export function deleteEvaluationTemplate(id,type) {
    let url = `/template/f_template_form_set_evaluation/delete/`
    if (type == '0') {
        url = `/template/dept/f_template_form_set_evaluation/delete/`
    }
    return request({
        url: `${url}${id}`,
        method: 'get',
    })
}
// CRF模板 评测编辑
export function editEvaluationTemplate(params,type) {
    let url = `/template/f_template_form_set_evaluation/edit`
    if (type == '0') {
        url = `/template/dept/f_template_form_set_evaluation/edit`
    }
    return request({
        url,
        method: 'POST',
        data: params,
    })
}

// CRF模板 评测添加
export function addEvaluationTemplate(params,type) {
    let url = `/template/f_template_form_set_evaluation/add`
    if (type == '0') {
        url = `/template/dept/f_template_form_set_evaluation/add`
    }
    return request({
        url,
        method: 'POST',
        data: params,
    })
}
/**
 * ======================== 患教库 测评 ======================
 */
// CRF模板 评测列表
export function getEvaluationCmsArticle(params, type) {
    let url = `/cms/cms_article_set_evaluation/list`
    if (type == '0') {
        url = `/cms/dept/cms_article_set_evaluation/list`
    }
    return request({
        url,
        method: 'GET',
        params,
    })
}
// CRF模板 评测删除
export function deleteEvaluationCmsArticle(id, type) {
    let url = `/cms/cms_article_set_evaluation/delete/`
    if (type == '0') {
        url = `/cms/dept/cms_article_set_evaluation/delete/`
    }
    return request({
        url: `${url}+${id}`,
        method: 'GET',
    })
}
// CRF模板 评测编辑
export function editEvaluationCmsArticle(params, type) {
    let url = `/cms/cms_article_set_evaluation/edit`
    if (type == '0') {
        url = `/cms/dept/cms_article_set_evaluation/edit`
    }
    return request({
        url,
        method: 'POST',
        data: params,
    })
}

// CRF模板 评测添加
export function addEvaluationCmsArticle(params, type) {
    let url = `/cms/cms_article_set_evaluation/add`
    if (type == '0') {
        url = `/cms/dept/cms_article_set_evaluation/add`
    }
    return request({
        url,
        method: 'POST',
        data: params,
    })
}