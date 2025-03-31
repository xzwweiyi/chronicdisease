import request from '@/utils/request'

// 查询【地区】列表  --分页
export function listArea(query) {
    return request ({
        url: '/system/area/list',
        method: 'get',
        params: query,
    })
}

// 查询【地区】树
export function treeArea(query) {
    return request ({
        url: '/system/area/regionTreeData',
        method: 'get',
        params: query,
    })
}

// 查询【二，三级地区】
export function areaChild(data) {
    return request ({
        url: '/system/area/findAreaListByParentId',
        method: 'post',
        data: data,
    })
}