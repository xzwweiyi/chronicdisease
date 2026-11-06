import request from '@/utils/request'

// ========================  模板内容  ========================
//获取模板列表
export function getTemplateData(params) {
    return request({
        url: '/template/f_template/list',
        method: 'GET',
        params
    })
}

// 新增模板列表信息
export function addTemplateInfo(data) {
    return request({
        url: '/template/f_template',
        method: 'POST',
        data
    })
}

// 修改模板列表信息
export function editTemplateInfo(data) {
    return request({
        url: '/template/f_template',
        method: 'PUT',
        data
    })
}
// 删除模板信息
export function deleteTemplateInfo(id) {
    return request({
        url: '/template/f_template/'+id,
        method: 'DELETE',
    })
}

// 获取模板内容详情
export function getTemplateDetail(id) {
    return request({
        url: '/template/f_template/detail/'+id,
        method: 'GET',
    })
}



// *** 模板表单
// 获取模板表单列表
export function getTemplateFormData(params) {
    return request({
        url: '/template/f_template_form/list',
        method: 'GET',
        params
    })
}
// 保存模板表单信息
export function addTemplateFormInfo(data, type) {
    let url = '/template/f_template_form'
    if (type == '0') {
        url = '/template/dept/f_template_form'
    }
    return request({
        url,
        method: 'POST',
        data
    })
}
// 修改模板代码成
export function editTemplateFormData(data, type) {
    let url = '/template/f_template_form'
    if (type == '0') {
        url = '/template/dept/f_template_form'
    }
    return request({
        url,
        method: 'PUT',
        data
    })
}
// 删除模板内容 
export function deleteTemplateFormInfo(params, type) {
    let url = '/template/f_template_form'
    if (type == '0') {
        url = '/template/dept/f_template_form'
    }
    return request({
        url: `${url}/${params}`,
        method: 'DELETE',
    })
}
// 获取CRF模板表单内容详情
export function getTemplateFormDetail(templateFormId) {
    return request({
        url: '/template/f_template_form/detail/'+templateFormId,
        method: 'GET',
    })
}


// 模板表单属性
// 模板属性列表
export function getTemplateAttributeList(params) {
    return request({
        url: `/template/f_template_property/list`,
        method: 'GET',
        params
    })
}
// 修改模板表单属性
export function editTemplateAttribute(data) {
    return request({
        url: `/template/f_template_property`,
        method: 'PUT',
        data
    })
}
// 查询模板表单属性详情
export function getTemplateAttributeInfo(id) {
    return request({
        url: `/template/f_template_property/` + id,
        method: 'GET',
    })
}
// 删除模板表单属性
export function deleteTemplateAttributeInfo(params) {
    return request({
        url: `/template/f_template_property/` + params,
        method: 'DELETE',
    })
}
// 单个模板表单属性修改

export function editTemplateAttributeOnly(data) {
    return request({
        url: `/template/f_template_property/only`,
        method: 'PUT',
        data
    })
}
// 模板编辑内容保存 
export function addTemplateAttribute(data) {
    return request({
        url: `/template/f_template_property`,
        method: 'POST',
        data
    })
}
// 模板表单编辑内容保存 
export function addTemplateFromAttribute(data,type) {
    let url = '/template/f_template_property/formSubmit'
    if (type == '0') {
        url = '/template/dept/f_template_property/formSubmit'
    }
    return request({
        url,
        method: 'PUT',
        data
    })
}





// ============================== 基础表单 =========================
// 基础表单列表
export function getLibraryFormList(params) {
    return request({
        url: `/template/f_library_form/list`,
        method: 'Get',
        params
    })
}
// 基础表单列表
export function getLibraryFormDetail(params) {
    return request({
        url: `/template/f_library_form/` + params.libraryId,
        method: 'Get',
    })
}

// 删除基础表单
export function deleteLibraryFormList(params) {
    return request({
        url: `/template/f_library_form/` + params.libraryIds,
        method: 'delete',
    })
}

// 增加基础表单
export function addLibraryFormList(data) {
    return request({
        url: `/template/f_library_form`,
        method: 'POST',
        data
    })
}

// 编辑基础表单
export function editLibraryFormList(data) {
    return request({
        url: `/template/f_library_form`,
        method: 'PUT',
        data
    })
}

// 基础表单属性列表 
export function getLibraryPropertyList(params) {
    return request({
        url: `/template/f_library_property/list`,
        method: 'Get',
        params
    })
}
// 查询基础表单属性详情
export function getLibraryPropertyDetail(params) {
    return request({
        url: `/template/f_library_property/` + params.id,
        method: 'Get',
    })
}

// 删除基础表单属性
export function deleteLibraryPropertyList(params) {
    return request({
        url: `/template/f_library_property/` + params.id,
        method: 'delete',
    })
}
// 增加基础表单属性
export function addLibraryProperty(data) {
    return request({
        url: `/template/f_library_property`,
        method: 'POST',
        data
    })
}

// 修改基础表单属性
export function editLibraryProperty(data) {
    return request({
        url: `/template/f_library_property`,
        method: 'PUT',
        data
    })
}

// 修改单个基础表单属性
export function editOnlyLibraryProperty(data) {
    return request({
        url: `/template/f_library_property/only`,
        method: 'PUT',
        data
    })
}

// 标准填写项/拍照项组件查询
export function getLibraryFormListAll(params){
    return request({
        url:`/template/f_library_form/listAll`,
        method:'GET',
        params
    })
}

// 标准填写项组件属性
export function getChildFormAndProperty(params){
    let requestUrl = `/template/f_library_form/childFormAndProperty/`+params.libraryId
    if(params.formName){
        requestUrl += `/${params.formName}`
    }
    return request({
        url: requestUrl,
        method: 'GET',
    })
}
// 标准填写项/拍照项属性查询
export function getLibraryPropertyListAll(params){
    return request({
        url:`/template/f_library_property/listAll`,
        method:'GET',
        params
    })
}

