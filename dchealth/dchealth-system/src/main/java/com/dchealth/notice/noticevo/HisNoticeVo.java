package com.dchealth.notice.noticevo;


import lombok.Data;

import java.util.Date;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/9/10 17:20
 * @Version: 1.0
 */
@Data
public class HisNoticeVo {

    /**
     * 主键(消息来源+序号)确保信息唯一
     */
    private String infoNo;

    /**
     * 插入消息时间
     */
    private Date insertTime;

    /**
     * 发送消息时间(预约发送，为空即时发送)
     */
    private Date sendTime;

    /**
     * 病人ID号
     */
    private String patientId;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 备用联系电话
     */
    private String contactPhone1;

    /**
     * 备用联系电话1
     */
    private String contactPhone2;

    /**
     * 发送信息内容
     */
    private String infoContent;

    /**
     * 发送消息来源(什么系统发送的)
     */
    private String infoSources;

    /**
     * 发送消息状态：1、发送微信。2、发送短信。3、同时发送微信与短信。
     */
    private String infoStatus;

    /**
     * 消息标题
     */
    private String infoTitle;

    /**
     * 跳转小程序APPID
     */
    private String miniprogramAppid;

    /**
     * 跳转小程序页面路径
     */
    private String miniprogramUrl;

    /**
     * 公众号OPENID，可用带参连接获取
     */
    private String openId;

    /**
     * 重定向地址
     */
    private String redirectUrl;

    /**
     * 发送消息类型：0微信公众号1支付宝生活号
     */
    private String sendType;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 小程序原始ID，需进行H5跳转时必填
     */
    private String miniprogramOriginalId;


}
