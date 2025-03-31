import request from '@/utils/request'

export function treeEvaluate (type) {
    return request({
        url: `/follow/work/evaluate/tree/${type}`,
        method: 'get',
    })
}

export function evaluateList (query) {
    return request ({
        url: `/follow/work/evaluate/list`,
        method: 'get',
        params: query,
    })
}

export function submitEvaluteInto (query) {
    return request ({
        url: `/follow/work/evaluate/handle`,
        method: 'Post',
        data: query,
    })
}

// 医生评价系统
export function getEvaluationPhysicianList(query) {
    return request ({
        url: `/medical/evaluation/list`,
        method: 'GET',
        params: query,
    })
}
