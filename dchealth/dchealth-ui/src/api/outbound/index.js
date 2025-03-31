import request from '@/utils/request'

// 提醒时间
export function getAdvanceTimeOptions() {
    return [{
        label: '07:00 - 08:00',
        value: '1'
    }, {
        label: '08:00 - 09:00',
        value: '2'
    }, {
        label: '09:00 - 10:00',
        value: '3'
    }, {
        label: '10:00 - 11:00',
        value: '4'
    }, {
        label: '11:00 - 12:00',
        value: '5'
    }, {
        label: '12:00 - 13:00',
        value: '6'
    }, {
        label: '13:00 - 14:00',
        value: '7'
    }, {
        label: '14:00 - 15:00',
        value: '8'
    }, {
        label: '15:00 - 16:00',
        value: '9'
    }, {
        label: '16:00 - 17:00',
        value: '10'
    }, {
        label: '17:00 - 18:00',
        value: '11'
    }, {
        label: '18:00 - 19:00',
        value: '12'
    }, {
        label: '19:00 - 20:00',
        value: '13'
    },]
}
// 获取队列设置的电话模板列表
export function getQueueSpeechList(id) {
    return request({
        url: '/research/outbound/speechList?researchId=' + id,
        method: 'get',
    })
}
// 获取队列设置的电话模板详情
export function getQueueSpeechDetails(id) {
    return request({
        url: `/research/outbound/speechDetailBySpeechId/${id}`,
        method: 'get',
    })
}
// 获取队列设置的智能外呼设置信息
export function getQueueOutBoundList(id) {
    return request({
        url: '/research/outbound/list?researchId=' + id,
        method: 'get',
    })
}
// 保存队列设置的智能外呼信息
export function addQueueOutBoundList(data) {
    return request({
        url: '/research/outbound/add/',
        method: 'POST',
        data
    })
}
// 智能外呼管理树列表
export function getOutboundTree(params) {
    return request({
        url: '/medical/outbound/tree',
        method: 'get',
        params
    })
}
// 智能外呼管理树列表
export function getOutboundList(params) {
    return request({
        url: '/medical/outbound/selectOutboundList',
        method: 'get',
        params
    })
}
// 智能外呼管理树列表
export function getOutboundDetails(id) {
    return request({
        url: `/medical/outbound/${id}`,
        method: 'get',
    })
}
// 获取队列设置的电话模板详情
export function getShortMsgDetails(id) {
    return request({
        url: `/sms/template/getDetailByType/${id}`,
        method: 'get',
    })
}
