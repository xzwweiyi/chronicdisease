import request from '@/utils/request'
/**
 *  患教问卷回显
 */
// 患者列表
export function getQuestionnaireList(params) {
    return request({
        url: '/cms/article/record',
        method: 'GET',
        params
    })
}
// 患者列表
export function getQuestionnaireDetails(params) {
    return request({
        url: '/cms/article/examFromInfo',
        method: 'GET',
        params
    })
}