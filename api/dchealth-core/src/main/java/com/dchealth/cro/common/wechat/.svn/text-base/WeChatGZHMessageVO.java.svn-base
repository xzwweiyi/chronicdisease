package com.dchealth.cro.common.wechat;

import lombok.Data;

import java.util.Map;

/**
 * 公众号消息推送实体
 */
@Data
public class WeChatGZHMessageVO {

    /**
     * {
     *            "touser":"OPENID",
     *            "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
     *            "url":"http://weixin.qq.com/download",
     *            "miniprogram":{
     *              "appid":"xiaochengxuappid12345",
     *              "pagepath":"index?foo=bar"
     *            },
     *            "data":{
     *                    "first": {
     *                        "value":"恭喜你购买成功！",
     *                        "color":"#173177"
     *                    },
     *                    "keyword1":{
     *                        "value":"巧克力",
     *                        "color":"#173177"
     *                    },
     *                    "keyword2": {
     *                        "value":"39.8元",
     *                        "color":"#173177"
     *                    },
     *                    "keyword3": {
     *                        "value":"2014年9月22日",
     *                        "color":"#173177"
     *                    },
     *                    "remark":{
     *                        "value":"欢迎再次购买！",
     *                        "color":"#173177"
     *                    }
     *            }
     *        }
     */


    /**
     * 接收者openid必填
     */
    private String touser;

    /**
     * 模板ID 必填
     */
    private String template_id;

    /**
     * 模板跳转链接（海外帐号没有跳转能力） 否
     */
    //private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据 否
     * {
     *     "appid":"xiaochengxuappid12345",
     *     "pagepath":"index?foo=bar"
     * }
     */
    //private Map<String,String> miniprogram;

    /**
     * 模板数据
     * {
     *         "first": {
     *             "value":"恭喜你购买成功！",
     *              "color":"#173177"
     *         },
     *             "keyword1":{
     *                 "value":"巧克力",
     *                 "color":"#173177"
     *         },
     *             "keyword2": {
     *                 "value":"39.8元",
     *                 "color":"#173177"
     *         },
     *             "keyword3": {
     *                 "value":"2014年9月22日",
     *                 "color":"#173177"
     *         },
     *             "remark":{
     *                 "value":"欢迎再次购买！",
     *                  "color":"#173177"
     *         }
     *  }
     */
    private Map<String,Map<String,String>> data;

}
