import request from '@/utils/request'
/**
 *  患教库  
 */
// 分类 列表
export function getPatientCategoryData(params) {
    return request({
        url: '/cms/category/list',
        method: 'GET',
        params
    })
}
// 新增 分类
export function addPatientCategoryInfo(data, type) {
    let url = '/cms/category'
    if (type == '0') {
        url = '/cms/dept/category'
    }
    return request({
        url,
        method: 'POST',
        data
    })
}
// 修改 分类
export function editPatientCategoryInfo(data, type) {
    let url = '/cms/category'
    if (type == '0') {
        url = '/cms/dept/category'
    }
    return request({
        url,
        method: 'PUT',
        data
    })
}
// 删除 分类
export function deletePatientCategoryInfo(ids, type) {
    let url = '/cms/category/edu/'
    if (type == '0') {
        url = '/cms/dept/category/edu/'
    }
    return request({
        url: url + ids,
        method: 'DELETE',
    })
}
// 患教文件夹树型列表
export function deptFilterTreeTree(params) {
    return request({
        url: `/cms/category/filterTree`,
        method: 'GET',
        params
    })
}
// 移动文件
export function movePatientCategoryInfo(data, type) {
    let url = '/cms/article/fileMove'
    if (type == '0') {
        url = '/cms/deptArticle/fileMove'
    }
    return request({
        url,
        method: 'POST',
        data
    })
}
// 加入科室库
export function addPatientDept(data) {
    return request({
        url:`/cms/deptArticle/batchAdd`,
        method: 'POST',
        data
    })
}
export function removePatientDept(data){
    return request({
        url:`/cms/deptArticle/removeArticle`,
        method: 'POST',
        data
    })
}
export function highLightTree(params){
    return request({
        url:`/system/dept/highLightTree`,
        method: 'get',
        params
    })
}


/**
 * 患教 列表
 */
// 加入科室 选择科室分类列表
export function addDeptCategoryOptions(params) {
    return request({
        url: '/cms/category/tree',
        method: 'GET',
        params
    })
}
//  资讯加入科室
export function addDeptCategory(data) {
    return request({
        url: '/cms/deptArticle',
        method: 'POST',
        data
    })
}
//  资讯移除科室
export function removeDeptCategory(articleId) {
    return request({
        url: `/cms/deptArticle/${articleId}`,
        method: 'DELETE',
    })
}
// 公共科室删除 内容
export function delPatientArticle(templateIds) {
    return request({
        url: '/cms/article/edu/' + templateIds,
        method: 'DELETE',
    })
}
// // 查询【患教】列表
export function listPatientArticle(query) {
    return request({
        url: '/cms/article/list',
        method: 'get',
        params: query
    })
}

// 新增  问卷内容
export function addQuestionnaire(data, type) {
    let url = '/template/f_medical_exam_form'
    if (type == '0') {
        url = '/template/dept/f_medical_exam_form'
    }
    return request({
        url,
        method: 'POST',
        data
    })
}
// 配置 问卷名称
export function updateQuestionnaireName(data, type) {
    let url = '/template/f_medical_exam_form'
    if (type == '0') {
        url = '/template/dept/f_medical_exam_form'
    }
    return request({
        url,
        method: 'PUT',
        data
    })
}

// 查询【患教库文章内容】详细
export function getArticleDetail(id) {
    return request({
        url: '/cms/article/' + id,
        method: 'get'
    })
}
// 新增【患教库文章内容】
export function addArticle(data, type) {
    let url = `/cms/article`
    if (type == '0') {
        url = `/cms/dept/article`
    }
    return request({
        url,
        method: 'post',
        data: data
    })
}

// 修改【患教库文章内容】
export function updateArticle(data, type) {
    let url = `/cms/article`
    if (type == '0') {
        url = `/cms/dept/article`
    }
    return request({
        url,
        method: 'put',
        data: data
    })
}

// 删除【患教库文章内容】
export function delArticle(id, type) {
    let url = `/cms/article/`
    if (type == '0') {
        url = `/cms/dept/article/`
    }
    return request({
        url: url + id,
        method: 'delete'
    })
}

//  ==================== 问卷页面  ===================
// 左侧标准量表菜单
export function getQuestionLeftPanel(params) {
    return request({
        url: `/template/f_library_form/list`,
        method: 'GET',
        params
    })
}
// 问卷详情
export function getQuestionFormDetail(params) {
    return request({
        url: `/template/f_medical_exam_form/fMedicalFormExamInfo?articleId=${params}`,
        method: 'Get',
    })
}
// 保存问卷
export function saveQuestionForm(data, type) {
    let url = `/template/subject`
    if (type == '0') {
        url = `/template/dept/subject`
    }
    return request({
        url,
        method: 'post',
        data
    })
}

// 问卷统计 === 统计分析
export function listStaticalData(data) {
    return request({
        url: `/template/f_medical_statistical/staticalData`,
        method: 'post',
        params: data,
    })
}

// 问卷统计 === 交叉分析
export function searchAnalysis(data) {
    return request({
        url: `/template/f_medical_statistical/analysis`,
        method: 'post',
        params: data,
    })
}


// 问卷统计 === 已答问卷列表
export function questionnaireAnsweredList(data) {
    return request({
        url: `/medical/patient_exam_record/list`,
        method: 'get',
        params: data,
    })
}

// 问卷统计 === 已答问卷详情
export function questionnaireAnsweredDetails(data) {
    return request({
        url: `/template/f_medical_exam_form/fMedicalFormExamFillInfo`,
        method: 'get',
        params: data,
    })
}