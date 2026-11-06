import request from '@/utils/request'

// ==================== 签约管理 ====================
export function listSign(query) { return request({ url: '/contract/sign/list', method: 'get', params: query }) }
export function getSign(id) { return request({ url: '/contract/sign/' + id, method: 'get' }) }
export function addSign(data) { return request({ url: '/contract/sign', method: 'post', data }) }
export function updateSign(data) { return request({ url: '/contract/sign', method: 'put', data }) }
export function delSign(ids) { return request({ url: '/contract/sign/' + ids, method: 'delete' }) }

// ==================== 签约团队管理 ====================
export function listTeam(query) { return request({ url: '/contract/team/list', method: 'get', params: query }) }
export function getTeam(id) { return request({ url: '/contract/team/' + id, method: 'get' }) }
export function addTeam(data) { return request({ url: '/contract/team', method: 'post', data }) }
export function updateTeam(data) { return request({ url: '/contract/team', method: 'put', data }) }
export function delTeam(ids) { return request({ url: '/contract/team/' + ids, method: 'delete' }) }

// ==================== 签约服务包管理 ====================
export function listPackage(query) { return request({ url: '/contract/package/list', method: 'get', params: query }) }
export function getPackage(id) { return request({ url: '/contract/package/' + id, method: 'get' }) }
export function addPackage(data) { return request({ url: '/contract/package', method: 'post', data }) }
export function updatePackage(data) { return request({ url: '/contract/package', method: 'put', data }) }
export function delPackage(ids) { return request({ url: '/contract/package/' + ids, method: 'delete' }) }

// ==================== 签约服务项目管理 ====================
export function listServiceItem(query) { return request({ url: '/contract/serviceItem/list', method: 'get', params: query }) }
export function getServiceItem(id) { return request({ url: '/contract/serviceItem/' + id, method: 'get' }) }
export function addServiceItem(data) { return request({ url: '/contract/serviceItem', method: 'post', data }) }
export function updateServiceItem(data) { return request({ url: '/contract/serviceItem', method: 'put', data }) }
export function delServiceItem(ids) { return request({ url: '/contract/serviceItem/' + ids, method: 'delete' }) }
