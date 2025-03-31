import request from '@/utils/request'

// 查询缓存配置列表
export function listRedis(query) {
  return request({
    url: '/redis/config/list',
    method: 'get',
    params: query
  })
}

// 添加缓存配置列表
export function addRedis(data) {
    return request({
      url: '/redis/config',
      method: 'post',
      data: data
    })
}

// 获取缓存配置详情
export function getRedis(id) {
    return request({
      url: '/redis/config/' + id,
      method: 'get',
    })
}

// 修改缓存配置列表
export function updateRedis(data) {
    return request({
      url: '/redis/config',
      method: 'put',
      data: data
    })
}

// 删除缓存配置列表
export function delRedis(ids) {
    return request({
      url: '/redis/config/' + ids,
      method: 'delete',
    })
}
