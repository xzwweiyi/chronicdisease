import request from '@/utils/request'

// ==================== 设备绑定管理 ====================
export function listBind(query) { return request({ url: '/device/bind/list', method: 'get', params: query }) }
export function getBind(id) { return request({ url: '/device/bind/' + id, method: 'get' }) }
export function addBind(data) { return request({ url: '/device/bind', method: 'post', data }) }
export function updateBind(data) { return request({ url: '/device/bind', method: 'put', data }) }
export function delBind(ids) { return request({ url: '/device/bind/' + ids, method: 'delete' }) }

// ==================== 设备管理 ====================
export function listManage(query) { return request({ url: '/device/manage/list', method: 'get', params: query }) }
export function getManage(id) { return request({ url: '/device/manage/' + id, method: 'get' }) }
export function addManage(data) { return request({ url: '/device/manage', method: 'post', data }) }
export function updateManage(data) { return request({ url: '/device/manage', method: 'put', data }) }
export function delManage(ids) { return request({ url: '/device/manage/' + ids, method: 'delete' }) }
