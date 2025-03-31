package com.dchealth.cro.cms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dchealth.cro.cms.utils.XunfeiConstants;
import com.dchealth.cro.cms.vo.*;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
//import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.XunfeiHttpClientUtil;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.core.tencent.TencentSmsSevice;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
public class XunfeiService {
    private static final Logger L = LoggerFactory.getLogger(XunfeiService.class);

//    @Autowired
//    private AppSetMapper appSetMapper;

    @Autowired
    private TencentSmsSevice tencentSmsSevice;

    @Autowired
    private CroCoreConfig croCoreConfig;

    /**
     * 综合讯飞模板列表
     */
    public DchealthCloudResponseEntity<List<SpeechListRes>> list() {
        DchealthCloudResponseEntity<List<SpeechListRes>> result = new DchealthCloudResponseEntity<List<SpeechListRes>>();
        Map<String, String> maps = new HashMap<String, String>();
        String xunfeiResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_SPEECH_LIST, maps);
        JSONObject jsonObject = JSON.parseObject(xunfeiResult);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {//档案不存在
            result.setCode(Integer.parseInt(code));
            return result;
        }
        List<SpeechListRes> list = JSONArray.parseArray(jsonObject.getString("data"), SpeechListRes.class);
        result.setData(list);
        return result;
    }

    /**
     * 根据speedId查询电话模板自定义变量
     */
    public DchealthCloudResponseEntity<List<Var>> getVarList(SpeechVariableRes speechVariable) {
        DchealthCloudResponseEntity<List<Var>> result = new DchealthCloudResponseEntity<List<Var>>();
        String xunfeiResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_SPEECH_VARIABLE, speechVariable);
        L.info("--------------调用外呼开放接口结果--------------:{}",xunfeiResult);
        JSONObject jsonObject = JSON.parseObject(xunfeiResult);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {//档案不存在
            result.setCode(Integer.parseInt(code));
            return result;
        }
        List<Var> list = JSONArray.parseArray(jsonObject.getString("data"),Var.class);
        result.setData(list);
        return result;
    }

    /**
     * 调用外呼开放接口（用电话模版）呼叫
     */
    public DchealthCloudResponseEntity<XunfeiRes> getCallTask(XunfeiReq xunfeiReq) {
        DchealthCloudResponseEntity<XunfeiRes> result = new DchealthCloudResponseEntity<XunfeiRes>();
//        String xunfeiResult = "{\"code\":0,\"data\":{\"batch\":[\"1508766764539905111\"],\"taskRelation\":[{\"relationId\":\"13520\",\"taskId\":\"1508766764539907\"}],\"filterDwellers\":[]},\"message\":\"成功\"}";
        L.info("调用外呼开放接口（用电话模版）呼叫参数{}", com.alibaba.fastjson2.JSON.toJSONString(xunfeiReq));
        String xunfeiResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_CALL_TASK, xunfeiReq);
        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSON.parseObject(xunfeiResult);
        XunfeiRes xunfeiRes = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
        L.info("调用外呼开放接口（用电话模版）呼叫结果{}", com.alibaba.fastjson2.JSON.toJSONString(xunfeiResult));
        result.setData(xunfeiRes);
        return result;
    }


    /**
     * 调用外呼开放接口（用短信）呼叫
     */
    public DchealthCloudResponseEntity<XunfeiRes> smsTask(XunfeiReq xunfeiReq) {
        DchealthCloudResponseEntity<XunfeiRes> result = new DchealthCloudResponseEntity<XunfeiRes>();
        String xunfeiResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_SMS_TASK, xunfeiReq);
        com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSON.parseObject(xunfeiResult);
        XunfeiRes xunfeiRes = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
        L.info("调用外呼开放接口（用短信）呼叫结果{}", com.alibaba.fastjson2.JSON.toJSONString(xunfeiResult));
        result.setData(xunfeiRes);
        return result;
    }



    /**
     * 查询批次汇总数据
     */
    public DchealthCloudResponseEntity<List<BatchListRes>> getBatchList(BatchListReq batchListReq) {
        DchealthCloudResponseEntity<List<BatchListRes>> result = new DchealthCloudResponseEntity<List<BatchListRes>>();
        String xunfeiResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_BATCH_LIST, batchListReq);
        L.info("--------------查询批次汇总数据结果--------------:{}",xunfeiResult);
        JSONObject jsonObject = JSON.parseObject(xunfeiResult);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {//档案不存在
            result.setCode(Integer.parseInt(code));
            return result;
        }
        List<BatchListRes> list = JSONArray.parseArray(jsonObject.getString("data"),BatchListRes.class);
        result.setData(list);
        return result;
    }


    /**
     * 在通过查询电话记录详情获取外呼结果
     */
    public DchealthCloudResponseEntity<RecordDetailRes> recordDetail(RecordDetailReq recordDetailReq) {
        DchealthCloudResponseEntity<RecordDetailRes> result = new DchealthCloudResponseEntity<RecordDetailRes>();
        String xunfeiResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_RECORD_DETAIL, recordDetailReq);
        L.info("--------------根据讯飞recordId查询具体单个随访任务的记录详情结果--------------:{}",xunfeiResult);
        JSONObject jsonObject = JSON.parseObject(xunfeiResult);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {//档案不存在
            result.setCode(Integer.parseInt(code));
            return result;
        }
        RecordDetailRes recordRes = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.getString("data"),RecordDetailRes.class);
        result.setData(recordRes);
        return result;
    }


    /**
     * 下载本地语音文件
     */
    public DchealthCloudResponseEntity<String> uploadImFile(DownloadImFileVo downloadImFileVo) {
        DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
        try {
            L.info("--------------下载本地语音文件1--------------");
            String localUrl = uploadImFile(downloadImFileVo.getFileUrl(),downloadImFileVo.getFileName());
            L.info("--------------下载本地语音文件结果--------------{}",localUrl);
            result.setData(localUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public DchealthCloudResponseEntity<Boolean> sendVerifyCode(SendTencentSmsVO sendTencentSmsVO) {
        DchealthCloudResponseEntity<Boolean> result = new DchealthCloudResponseEntity<Boolean>();
        Boolean isTrue = tencentSmsSevice.sendTencentSMS(sendTencentSmsVO.getAppId(),sendTencentSmsVO.getPhones(),
                sendTencentSmsVO.getTemplateId(),sendTencentSmsVO.getTemplateParam(),sendTencentSmsVO.getTencentSmsVO());
        result.setData(isTrue);
        return result;
    }


    private String uploadImFile(String fileUrl,String fileName) throws IOException {
        InputStream is = null;
        try{
            URL url = new URL(fileUrl);
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            if (is == null){
                return "";
            }
//            String httpUrl = croCoreConfig.getFastPath() + "ngupload/single";
            String httpUrl = croCoreConfig.getFastPath() + "ngupload/nonCompressFile";
            String filesResult = HttpClientUtil.getInstance().sendHttpPostWav(httpUrl, is,fileName);
            if (StringUtils.isNotEmpty(filesResult)) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(filesResult);
                if (jsonObject != null) {
                    int  code = jsonObject.getInteger("code");
                    if(code ==1000) {
                        return jsonObject.getString("data");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (is != null){
                is.close();
            }
        }
        return "";
    }


    /**
     * 发送验证码
     * @param sendTencentSmsVO
     * @return
     */
    public Boolean sendVerifyCodeWithXunfei(SendTencentSmsVO sendTencentSmsVO) {
        L.info("sendVerifyCodeWithXunfei--------------发送短信验证码--------------param="+JSON.toJSONString(sendTencentSmsVO));
//        Boolean isTrue = tencentSmsSevice.sendTencentSMS(sendTencentSmsVO.getAppId(),sendTencentSmsVO.getPhones(),
//                sendTencentSmsVO.getTemplateId(),sendTencentSmsVO.getTemplateParam(),sendTencentSmsVO.getTencentSmsVO());

        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();
        //居民信息
        Dweller dweller = new Dweller();
        dweller.setDwellerName("验证码");
        String[] templateParam = sendTencentSmsVO.getTemplateParam();
        String[] phones = sendTencentSmsVO.getPhones();

        int successCount = 0;
        if (phones != null && phones.length > 0) {
            for (String phone : phones) {
                phone=phone.replace("+86","");
                dweller.setTelephone(phone);
                dwellerList.add(dweller);
                xunfeiReq.setDwellers(dwellerList);
                xunfeiReq.setSmsId(XunfeiConstants.XUNFEI_MESSAGE_NOTIFY);
                xunfeiReq.setType(1);//1即呼
                ArrayList<Var> varList = new ArrayList<>();
                Var var1 = new Var("dwellerName","唐都");
                String[] smsVOTemplateParam = sendTencentSmsVO.getTemplateParam();
                if (templateParam != null && templateParam.length > 0) {
                    Var var2 = new Var("content","登录短信 "+smsVOTemplateParam[0]+"为您的登录验证码，请于5分钟内填写，如非本人操作，请忽略本短信。");
                    Var var3 = new Var("center","唐都医院");
                    varList.add(var1);
                    varList.add(var2);
                    varList.add(var3);
                    xunfeiReq.setVars(varList);
                }
                L.info("---------------------sendVerifyCodeWithXunfei，参数为:{}----------------------------", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
                String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_SMS_TASK, xunfeiReq);
                com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSON.parseObject(result);
                String code = jsonObject.getString("code");
                if(result!=null && ("1000".equals(code) || "0".equals(code))){
                    successCount++;
                }
            }
        }else {
            throw new ServiceException("手机号码不能为空");
        }
        L.info("发送成功条数="+successCount);
        return successCount==phones.length?true:false;
    }


    /**
     * 发送邀请入组通知
     *
     * @param sendTencentSmsVO
     * @param isBind
     * @param researchName
     * @return
     */
    public Boolean sendJoinNotifyWithXunfei(SendTencentSmsVO sendTencentSmsVO, boolean isBind, String researchName) {
        L.info("sendJoinNotifyWithXunfei--------------发送邀请入组短信--------------param="+JSON.toJSONString(sendTencentSmsVO));
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();
        //居民信息
        Dweller dweller = new Dweller();
        String[] templateParam = sendTencentSmsVO.getTemplateParam();
        if (templateParam != null && templateParam.length > 0) {
            dweller.setDwellerName(templateParam[0]);
        }
        String[] phones = sendTencentSmsVO.getPhones();
        int successCount = 0;
        if (phones != null && phones.length > 0) {
            for (String phone : phones) {
                phone=phone.replace("+86","");
                dweller.setTelephone(phone);
                dwellerList.add(dweller);
                xunfeiReq.setDwellers(dwellerList);
                xunfeiReq.setSmsId(XunfeiConstants.XUNFEI_MESSAGE_NOTIFY);
                xunfeiReq.setType(1);//1即呼
                ArrayList<Var> varList = new ArrayList<>();
                Var var1 = new Var("dwellerName",dweller.getDwellerName());
                if (templateParam != null && templateParam.length > 0) {
                    Var var2 = null;
                    if (isBind) {
                        //已绑定发送入组的短信模版
                        var2 = new Var("content","尊敬的"+dweller.getDwellerName()+"，唐都医院邀请您加入"+researchName+"队列，请打开慢病管理患者端进行确认，（个人中心->我的消息->确认入组），加入队列获取专业的慢病管理服务。");
                    }else{
                        var2 = new Var("content","唐都医院保驾护航 尊敬的"+dweller.getDwellerName()+"，欢迎您加入唐都慢病管理平台：1.微信关注“唐都医院”公众号；2.进入“诊疗服务”选择“慢病管理”；3.输入患者信息完成绑定，即可享受专业的慢病管理服务。唐都医院为您的健康保驾护航！");
                    }
                    Var var3 = new Var("center","唐都医院");
                    varList.add(var1);
                    varList.add(var2);
                    varList.add(var3);
                    xunfeiReq.setVars(varList);
                }
                L.info("---------------------sendJoinNotifyWithXunfei，参数为:{}----------------------------", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
                String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_SMS_TASK, xunfeiReq);
                com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSON.parseObject(result);
                String code = jsonObject.getString("code");
                if(result!=null && ("1000".equals(code) || "0".equals(code))){
                    successCount++;
                }
            }
        }else {
            throw new ServiceException("手机号码不能为空");
        }
        L.info("发送成功条数="+successCount);
        return successCount==phones.length?true:false;
    }
}
