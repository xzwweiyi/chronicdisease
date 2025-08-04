package com.dchealth.cro.api.service;


import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.account.vo.PatientAccountInfoVO;
import com.dchealth.cro.api.enums.BindDeptEnum;
import com.dchealth.cro.api.vo.BindPatientVo;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisPublishService;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.health.domain.MpPatientAccount;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPatientAccountMapper;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.health.utils.HealthUtils;
import com.dchealth.cro.his.domain.PatMasterIndex;
import com.dchealth.cro.his.mapper.PatMasterIndexMapper;
import com.dchealth.cro.medical.domain.AppLoginRecord;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.form.BindPatientForm;
import com.dchealth.cro.medical.mapper.AppLoginRecordMapper;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.research.domain.ResearchInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/23 10:25
 * @Version: 1.0
 */

@Slf4j
@Service
public class BindPatientInfoService {

    @Autowired
    private PatientAccountMapper patientAccountMapper;

    @Resource
    private MpPatientAccountMapper mpPatientAccountMapper;

    @Resource
    private MpPersonMapper mpPersonMapper;

    @Resource
    private RedisPublishService redisPublishService;

    @Resource
    private PatMasterIndexMapper patMasterIndexMapper;

    @Resource
    private PatientInfoMapper patientInfoMapper;

    @Resource
    private PatientInfoService patientInfoService;


    @Resource
    private AppLoginRecordMapper appLoginRecordMapper;


    @Transactional
    public DchealthCloudResponseEntity<PatientAccountInfoVO> bindPatient(BindPatientVo bindPatientForm) {
        DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();

        String bindResult = "成功绑定患者信息";
        //注册
        PatientAccount account = patientAccountMapper.selectPatientAccountByMobile(bindPatientForm.getMobile());
        if (account == null) {//注册
            account = new PatientAccount();
            account.setMobile(bindPatientForm.getMobile());
            account.setCreateTime(new Date());
//            account.setOpenId(openId);
            patientAccountMapper.insertPatientAccount(account);
        }

        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByMobile(bindPatientForm.getMobile());

        //停用
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            bindResult = CroApiCodeConstant.BLOCK_UP_ERROR.getMsg();
            insertAppRecordLog(patientAccount, "", bindResult);
            return result;
        }
        //绑定且输入的身份证
        if ("1".equals(patientAccount.getBindStatus()) && bindPatientForm.getCard().equalsIgnoreCase(patientAccount.getIdCard())) {
            result.setCode(CroApiCodeConstant.ALREADY_BINDING.getCode());
            String personId = patientAccount.getRegistrySigns();
            // 返回已绑定id
            PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
            patientAccountInfoVO.setPersonId(personId);
            result.setData(patientAccountInfoVO);
            bindResult = CroApiCodeConstant.ALREADY_BINDING.getMsg();
            insertAppRecordLog(patientAccount, "", bindResult);
            return result;
        }
        if ("1".equals(patientAccount.getBindStatus()) && !bindPatientForm.getCard().equalsIgnoreCase(patientAccount.getIdCard())) {
            result.setCode(CroApiCodeConstant.ALREADY_BINDING_OTHER.getCode());
            String personId = patientAccount.getRegistrySigns();
            // 返回已绑定id
            PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
            patientAccountInfoVO.setPersonId(personId);
            patientAccountInfoVO.setIdCard(patientAccount.getIdCard());
            result.setData(patientAccountInfoVO);
            bindResult = CroApiCodeConstant.ALREADY_BINDING_OTHER.getMsg();
            insertAppRecordLog(patientAccount, "", bindResult);
            return result;
        }
        MpPatientAccount mpPatientAccount = mpPatientAccountMapper.selectMpPatientAccountByAccountId(patientAccount.getAccountId());
        if (mpPatientAccount != null) {
            result.setCode(CroApiCodeConstant.ALREADY_BINDING_ERROR.getCode());
            bindResult = CroApiCodeConstant.ALREADY_BINDING_ERROR.getMsg();
            insertAppRecordLog(patientAccount, "", bindResult);
            return result;
        }
        // 只处理未绑定的状态
        PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
        MpPerson patientBaseInfos = mpPersonMapper.selectMpPersonByPersonIdCardId(bindPatientForm.getPersonId(), bindPatientForm.getCard());
        if (patientBaseInfos != null) {
            MpPatientAccount mpa = mpPatientAccountMapper.selectMpPatientAccountBypersonId(patientBaseInfos.getPersonId());
            if (mpa != null && !(mpa.getAccountId().longValue() == patientAccount.getAccountId().longValue())) {
                result.setCode(CroApiCodeConstant.ALREADY_BINDING_ERROR.getCode());
                bindResult = CroApiCodeConstant.ALREADY_BINDING_ERROR.getMsg();
                insertAppRecordLog(patientAccount, "", bindResult);
                return result;
            }
        }

        if (patientBaseInfos == null) {
            PatMasterIndex patMasterIndex = patMasterIndexMapper.selectByIdNoAndPatientId(bindPatientForm.getCard(), bindPatientForm.getPersonId());
            if (patMasterIndex == null) {
                result.setCode(CroApiCodeConstant.PATIENT_NOT_EXIST_PLEASE_FILL_CORRECT_ID_NUMBER_AND_REGISTRY_SIGNS.getCode());
                bindResult = CroApiCodeConstant.PATIENT_NOT_EXIST_PLEASE_FILL_CORRECT_ID_NUMBER_AND_REGISTRY_SIGNS.getMsg();
                insertAppRecordLog(patientAccount, "", bindResult);
                return result;
            }
//            此处因为患者多手机多就诊号暂时加入手机号查询
            MpPerson patientBaseInfo = mpPersonMapper.selectMpPersonByCardId(bindPatientForm.getCard(), patientAccount.getMobile());
            if (Objects.nonNull(patientBaseInfo)) {
                // 多个身份证~ 发布事件后, 设置主号, 然后
                Map<String, String> json = new HashMap<>();
                json.put("idCard", bindPatientForm.getCard());
                json.put("personId", bindPatientForm.getPersonId());
                redisPublishService.convertAndSend("patientCard", JSON.toJSONString(json));
//               if(bindPatientForm.getAccountId().equals()) {
//
//               }
                patientBaseInfos = patientBaseInfo;

//               L.info("患者存在多个就诊号={}", JSON.toJSONString(patientBaseInfos));
//               result.setCode(CroApiCodeConstant.PATIENT_ALREADY_EXIST.getCode());
//               // 返回已绑定id
//               PatientAccountInfoVO patientAccountInfoVO2 = new PatientAccountInfoVO();
//               patientAccountInfoVO2.setPersonId(patientBaseInfo.getPersonId());
//               patientAccountInfoVO2.setIdCard(bindPatientForm.getIdCardNo());
//               result.setData(patientAccountInfoVO2);
//               return result;
            } else {
                patientBaseInfos = insertMpPerson(patMasterIndex);
            }


        }

        MpPatientAccount patientAccount2 = new MpPatientAccount();
        patientAccount2.setPersonId(String.valueOf(patientBaseInfos.getPersonId()));
        patientAccount2.setAccountId(patientAccount.getAccountId());
        mpPatientAccountMapper.insertMpPatientAccount(patientAccount2);
        patientAccount.setBindStatus("1");
        patientAccount.setRegistrySigns(bindPatientForm.getPersonId());
        patientAccount.setIdCard(bindPatientForm.getCard());
        patientAccountMapper.updatePatientAccount(patientAccount);
        patientAccountInfoVO.setPersonId(patientBaseInfos.getPersonId());
        patientAccountInfoVO.setPersonName(patientBaseInfos.getPersonName());
        patientAccountInfoVO.setBindStatus("1");
        patientAccountInfoVO.setSex(HealthUtils.getSex(patientBaseInfos.getCardId()));
        patientAccountInfoVO.setIdCard(patientBaseInfos.getCardId());
        patientAccountInfoVO.setAccountId(patientAccount.getAccountId());


        //patient_info表中accountId是null的做绑定处理
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPersonId(patientBaseInfos.getPersonId());
//        patientInfo.setEnrollmentStatus("0");
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfo);
        List<PatientInfo> filterPatientInfos = patientInfos.stream().filter(info -> info.getAccountId() == 0).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filterPatientInfos)) {
            for (PatientInfo info : filterPatientInfos) {
                PatientInfo patientInfoVo = new PatientInfo();
                patientInfoVo.setPatientId(info.getPatientId());
                patientInfoVo.setAccountId(patientAccount.getAccountId());
                patientInfoVo.setPatientMobile(patientAccount.getMobile());
                patientInfoMapper.updatePatientInfo(patientInfoVo);
            }
        }

        insertAppRecordLog(patientAccount, "", bindResult);

        // 只对肥胖门诊患者进行默认入组
        if (StringUtils.isNotBlank(bindPatientForm.getDeptCode()) && !"150002".equals(bindPatientForm.getDeptCode())) {
            result.setMsg("绑定成功");
            return result;
        }

        // 指定 部门内分泌科肥胖课题专属
        ResearchInfo researchInfo = patientInfoService.autoEnrollmentList(BindDeptEnum.getSzCode("150002"));

        //模拟登录成功，绑定患者号之后 默认将内分泌科患者导入到队列

        int i = patientInfoService.importEnrollment(bindPatientForm.getMobile(), bindPatientForm.getCard(), patientBaseInfos.getPersonName(),  patientAccount, researchInfo);


        result.setData(patientAccountInfoVO);
        return result;
    }


    private MpPerson insertMpPerson(PatMasterIndex patMasterIndex) {
        MpPerson mpPerson = new MpPerson();
        mpPerson.setLocationId(dealOracleString(patMasterIndex.getMailingAddress()));
        mpPerson.setCardId(patMasterIndex.getIdNo());
        mpPerson.setCardType("1");
        mpPerson.setCareSiteId("TDYY");
        mpPerson.setContactsName(dealOracleString(patMasterIndex.getNextOfKin()));

        mpPerson.setContactsPhone(patMasterIndex.getNextOfKinPhone());
        mpPerson.setPhoneNumber(patMasterIndex.getPhoneNumberHome());
        mpPerson.setCreateTime(DateUtils.getNowDate());
        mpPerson.setPersonName(dealOracleString(patMasterIndex.getName()));
        mpPerson.setAddress(dealOracleString(patMasterIndex.getNextOfKinAddr()));
        mpPerson.setPersonId(patMasterIndex.getPatientId());
        if (StringUtils.isNotBlankAndNotEmpty(patMasterIndex.getIdNo())) {
            if (HealthUtils.getSex(patMasterIndex.getIdNo()).intValue() == 1) {
                mpPerson.setGenderSourceValue("男");
            } else {
                mpPerson.setGenderSourceValue("女");
            }
        }
        mpPersonMapper.insertMpPerson(mpPerson);
        return mpPerson;
    }


    private String dealOracleString(String s) {
        if (StringUtils.isNotBlankAndNotEmpty(s)) {
            try {
                String newStr = new String(s.getBytes("ISO-8859-1"), "GBK");
                log.error("newStr{} ", newStr);
                return newStr;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public void insertAppRecordLog(PatientAccount patientAccount, String deviceToken, String result){
        AppLoginRecord appLoginRecord = new AppLoginRecord();
        appLoginRecord.setAccountId(patientAccount.getAccountId());
        appLoginRecord.setMobile(patientAccount.getMobile());
        appLoginRecord.setAppType("3");//预问诊端
        appLoginRecord.setDeviceToken(deviceToken);
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        appLoginRecord.setYearStat(year);
        appLoginRecord.setResult(result);
        appLoginRecord.setMonthStat(month);
        appLoginRecord.setCreateTime(DateUtils.getNowDate());
        appLoginRecordMapper.insertAppLoginRecord(appLoginRecord);
    }


}
