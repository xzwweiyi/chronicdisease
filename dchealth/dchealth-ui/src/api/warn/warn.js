import request from '@/utils/request'

// 查询预警list
export function listWarn(query) {
    return request({
        url: '/warn/config/list',
        method: 'get',
        params: query
    })
}
