package com.dchealth.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {

    public static final String AES_KEY = "Kuds72&kdsalq202";
    public static final String RAS_DEFAULT_PRIVATE_KEY = "d1f7606da81eabbfb8b1e27622284e75ad4ef99dfd743f09196d8f2dedd22ba8";

    //讯飞参数
    public static final String XUNFEI_APP_ID = "7f4bb3b21dff40db8419953b743dda40";

    public static final String XUNFEI_SECRET_KEY = "Ah7EH5v2NiH3ru7D";

    //查询电话模版列表
    public static final String XUNFEI_SPEECH_LIST = "/speech-api/v1/pb/openapi/speech-list";

    //外呼开放接口（用电话模版）
    public static final String XUNFEI_CALL_TASK = "/speech-api/v1/pb/openapi/call-task";

    //外呼开放接口（用短信）
    public static final String XUNFEI_SMS_TASK = "/speech-api/v1/pb/openapi/sms-task";

    //查询电话模板自定义变量
    public static final String XUNFEI_SPEECH_VARIABLE = "/speech-api/v1/pb/openapi/speech-variable";

    //查询批次汇总数据
    public static final String XUNFEI_BATCH_LIST = "/report-api/v2/pb/openapi/batch-list";

    //查询电话记录详情
    public static final String XUNFEI_RECORD_DETAIL = "/report-api/v2/pb/openapi/record-detail";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    public static final String YES = "Y";
    public static final String NO = "N";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    protected static final String[] JOB_WHITELIST_STR = {"com.dchealth"};

    /**
     * 队列树key
     */
    public static final String FOLLOW_TREE_KEY = "follow_tree_key:";
    /**
     * 表单填写人为医生时，create_by字段的值的前缀
     */
    public static final String CREATEOR_DOCTOR_KEY = "Dr:";

    /**
     * 定时任务违规的字符
     */
    protected static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.dchealth.common.utils.file"};

    /**
     * 二级树节点的条目数缓存key
     */
    public static final String TOTAL_TREE_LEVEL_NUM = "total_tree_level_num:";

    /**
     * 一级树节点的条目数缓存key
     */
    public static final String ONE_TREE_LEVEL_NUM = "one_tree_level_num:";


    //lys2
//    /**
//     * 随访树根节点缓存Key
//     */
//    public static final String FOLLOW_TREE_ROOT_KEY = "follow_tree_root:";
//
//    /**
//     * 病种子树缓存Key
//     */
//    public static final String FOLLOW_TREE_DISEASE_KEY = "follow_tree_disease:";
    //lys2
}
