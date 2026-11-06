import request from '@/utils/request'

// 查询缓存操作列表
export function listCache(data) {
  return request({
    url: '/redis/cache/list',
    method: 'post',
    data: data
  })
}

// 查询缓存操作列表
export function listDb(query) {
    return request({
      url: '/redis/cache/db/list',
      method: 'get',
      params: query
    })
}

// 添加缓存操作列表
export function addCache(data) {
    return request({
      url: '/redis/cache/add',
      method: 'post',
      data: data
    })
}

// 获取缓存操作详情
export function getCache(data) {
    return request({
      url: '/redis/cache/details',
      method: 'post',
      data: data
    })
}

// 删除缓存操作列表
export function delCache(ids) {
    return request({
      url: '/redis/cache/delete',
      method: 'delete',
    })
}

// 修改元素key
export function updateName (data) {
    return request({
        url: '/redis/cache/name/update',
        method: 'post',
        data: data
    })
}

// 修改元素值
export function updateValue (data) {
    return request({
        url: '/redis/cache/value/update',
        method: 'post',
        data: data
    })
}

// 修改元素有效时间
export function updateExpire (data) {
    return request({
        url: '/redis/cache/expire/update',
        method: 'post',
        data: data
    })
}
