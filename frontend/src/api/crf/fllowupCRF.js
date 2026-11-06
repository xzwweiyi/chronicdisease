import request from '@/utils/request'
/**
 *  ================================ 随访 ========================= CRF
 */
// 随访 CRF 列表
export function getFllowupCRFData(params) {
    return request({
        url: '/template/f_medical_form/list',
        method: 'GET',
        params
    })
}
// 新增 随访 CRf 
export function addFllowupCRFInfo(data) {
    return request({
        url: '/template/f_medical_form',
        method: 'POST',
        data
    })
}
// 修改 随访 CRf 
export function editFllowupCRFInfo(data) {
    return request({
        url: '/template/f_medical_form',
        method: 'PUT',
        data
    })
}
// 删除 随访 CRf 
export function deleteFllowupCRFInfo(medicalFormIds) {
    return request({
        url: '/template/f_medical_form/' + medicalFormIds,
        method: 'DELETE',
    })
}
// 详情
export function getFllowupFormDetail(medicalFormId) {
    return request({
        url: '/template/f_medical_form/detail/' + medicalFormId,
        method: 'GET',
    })
}

// 新增保存
export function addFllowupFormAttribute(data) {
    return request({
        url: '/template/f_medical_property',
        method: 'POST',
        data,
    })
}
// 保存并添加到模板库
export function addImportOrUpdate(data) {
    return request({
        url: '/template/f_template/importOrUpdate?followId=' + data.followId + '&templateId=' + data.templateId,
        method: 'POST',
        // data,
    })
}

// 详情
export function getFllowupTemplateDetail(medicalFormId) {
    return request({
        url: '/template/f_template_form/' + medicalFormId,
        method: 'GET',
    })
}

// 导入 CRF importCRFTemplate
export function importOrUpdateTemplate(params) {
    return request({
        url: '/template/f_medical_form/importOrUpdate',
        method: 'POST',
        params
    })
}

// 导入 CRF 模板库模块 importOrUpdateTemplateForm
export function importOrUpdateTemplateForm(params) {
    return request({
        url: `/template/f_medical_form/importOrUpdateByTemplateFromIds`,
        method: 'POST',
        params
    })
}
//判断是否重复
export function fMedicalFormIsRepeat(params) {
    return request({
        url: `/template/f_medical_form/fMedicalFormIsRepeat`,
        method: 'get',
        params
    })
}
// 随访CRF设置排序
export function saveBatchUpdateSortNum(data) {
    return request({
        url: `/template/f_medical_form/batchUpdateSortNum`,
        method: 'POST',
        data
    })
}
// 预览所有随访CRF设置
export function previewFllowupCRFData(followId) {
    return request({
        url: `/template/f_medical_form/detailAll/${followId}`,
        method: 'GET',
    })
}

// 删除表单配置项 /template/f_medical_property/delete  get请求   Long id, Integer formType
export function deleteFllowupCRFProperty(params) {
    return request({
        url: `/template/f_medical_property/delete`,
        method: 'GET',
        params
    })
}

// 删除预警 /warn/medical/condition/{id}
export function deleteMedicalCondition(id) {
    return request({
        url: `/warn/medical/condition/${id}`,
        method: 'GET',
    })
}

