import request from '@/utils/request'

// 查询路径的所有节点
export function listNodes(researchId) {
  return request({
    url: '/pathway/node/list/' + researchId,
    method: 'get'
  })
}

// 新增节点
export function addNode(data) {
  return request({
    url: '/pathway/node',
    method: 'post',
    data: data
  })
}

// 修改节点
export function updateNode(data) {
  return request({
    url: '/pathway/node',
    method: 'put',
    data: data
  })
}

// 删除节点
export function delNode(followId) {
  return request({
    url: '/pathway/node/' + followId,
    method: 'delete'
  })
}

// 节点排序
export function reorderNodes(researchId, orderedFollowIds) {
  return request({
    url: '/pathway/node/reorder/' + researchId,
    method: 'put',
    data: orderedFollowIds
  })
}

// 保存节点CRF表单绑定
export function saveNodeForms(followId, requiredForms, optionalForms) {
  return request({
    url: '/pathway/node/forms/' + followId,
    method: 'put',
    data: { requiredForms, optionalForms }
  })
}

// 获取基线节点
export function getBaselineNode(researchId) {
  return request({
    url: '/pathway/node/baseline/' + researchId,
    method: 'get'
  })
}
