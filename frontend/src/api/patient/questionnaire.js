import request from "@/utils/request";
/**
 *  患教问卷回显
 */
// 患者列表
export function getQuestionnaireList(params) {
  return request({
    url: "/cms/article/record",
    method: "GET",
    params,
  });
}
// 患者列表
export function getQuestionnaireDetails(params) {
  return request({
    url: "/cms/article/examFromInfo",
    method: "GET",
    params,
  });
}
// 患者列表 获取导出权限
export function getExportAuthority(params) {
  return request({
    url: "/system/user/judgeExportAuthority2",
    method: "GET",
    params,
  });
}

// 患者列表-留言查询
export function getMessagesDetail(personId) {
  return request({
    url: `/user/message/${personId}`,
    method: "GET",
  });
}

// 患者列表-发送留言
export function messageSend(data) {
  return request({
    url: "/user/message/send",
    method: "POST",
    data: data,
  });
}

// 患者列表-更新未读留言状态
export function updateReadStatus(personId) {
  return request({
    url: `/user/message/updateReadStatus/${personId}`,
    method: "GET",
  });
}

// 患者列表-批量清除留言
export function clearMessage(data) {
  return request({
    url: "/user/message/batchClear",
    method: "POST",
    data: data,
  });
}
