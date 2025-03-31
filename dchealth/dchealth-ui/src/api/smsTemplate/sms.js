import request from '@/utils/request'

// 查询【短信模板】列表
export function listSms(query) {
    return request({
        url: '/sms/template/list',
        method: 'get',
        params: query
    })
}

// 新增【短信模版】接口
export function addSms(data) {
    return request({
        url: '/sms/template/add',
        method: 'post',
        data: data
    })
}

// 编辑【短信模版】接口
export function editSms(data) {
    return request({
        url: '/sms/template/add',
        method: 'post',
        data: data
    })
}

// 查看【短信模版】接口
export function getSms(ids) {
    return request({
        url: '/sms/template/' + ids,
        method: 'get',
    })
}

