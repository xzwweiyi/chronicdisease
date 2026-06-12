package com.dchealth.cro.api.service;


import com.alibaba.fastjson.JSON;
import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.api.enums.BindDeptEnum;
import com.dchealth.cro.api.vo.AutoEnrollmentHandleVo;
import com.dchealth.cro.common.constant.FollowTipStatusConstants;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.core.mapper.SysDictDataMapper;
import com.dchealth.cro.core.service.SysDictDataService;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.domain.PatientRecordOperLog;
import com.dchealth.cro.medical.domain.PatientRecordTemplate;
import com.dchealth.cro.medical.form.FollowWorkListCmdForm;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
import com.dchealth.cro.medical.mapper.PatientRecordOperLogMapper;
import com.dchealth.cro.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.domain.ResearchPatient;
import com.dchealth.cro.research.domain.SysPopNotice;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import com.dchealth.cro.research.mapper.ResearchPatientMapper;
import com.dchealth.cro.research.mapper.SysPopNoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/23 16:48
 * @Version: 1.0
 */
@Slf4j
@Service
public class PatientInfoService {

    @Resource
    private PatientInfoMapper patientInfoMapper;

    @Resource
    private ResearchPatientMapper researchPatientMapper;

    @Resource
    private PatientRecordTemplateMapper patientRecordTemplateMapper;

    @Resource
    private PatientRecordMapper patientRecordMapper;

    @Resource
    private ResearchInfoMapper researchInfoMapper;

    @Resource
    private MpPersonMapper mpPersonMapper;

    @Autowired
    private TencentService tencentService;

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;

    @Autowired
    private SysPopNoticeMapper sysPopNoticeMapper;

    @Autowired
    private SysDictDataService sysDictDataService;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;


    @Transactional
    public int importEnrollment(String mobile, String cardId, String userName, PatientAccount patientAccount, ResearchInfo researchInfo) {
        // 通过电话号码查询患者信息
        MpPerson mpPerson = mpPersonMapper.selectMpPersonByCardId(cardId, mobile);

        Long researchId = researchInfo.getId();

        int num = 0;
        Set<Long> researchIds = new HashSet<>();
        if (mpPerson != null) {
            researchIds.add(researchInfo.getId());
            PatientInfo info = new PatientInfo();
            info.setPersonId(mpPerson.getPersonId());
            //                info.setPersonId(baseInfo.getPersonId());
            info.setResearchId(researchId);

            List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(info);
            Map<String, PatientInfo> infoMap = patientInfos.stream().collect(Collectors.toMap(PatientInfo::getPersonId, Function.identity()));


            if (infoMap.get(mpPerson.getPersonId()) == null) {
                PatientInfo patientInfo = new PatientInfo();
                if (patientAccount.getAccountId() != null) {
                    patientInfo.setAccountId(patientAccount.getAccountId());
                }

//                // 对肥胖门诊的队列进行判断 不同的手术、生活方式
//                List<SysDictData> list = sysDictDataMapper.selectDictDataByType("patient_code");
//                Map<String, String> map = list.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
//                map.forEach((k, v) -> {
//                    if (researchId.toString().equals(v) && k.equals("肥胖门诊用药队列")) {
//                        // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
//                        if (patientInfos.isEmpty()) {
//                            patientInfo.setPatientCode(String.format("D-%04d", 1));
//                        } else {
//                            PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
//                            patientInfo.setPatientCode(generateNextCode("D", patientInfo1.getPatientCode()));
//                        }
//
//                    } else if (researchId.toString().equals(v) && k.equals("肥胖门诊生活方式队列")) {
//                        // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
//                        if (patientInfos.isEmpty()) {
//                            patientInfo.setPatientCode(String.format("L-%04d", 1));
//                        } else {
//                            PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
//                            patientInfo.setPatientCode(generateNextCode("L", patientInfo1.getPatientCode()));
//                        }
//                    } else if (researchId.toString().equals(v) && k.equals("肥胖门诊手术队列")) {
//                        // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
//                        if (patientInfos.isEmpty()) {
//                            patientInfo.setPatientCode(String.format("S-%04d", 1));
//                        } else {
//                            PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
//                            patientInfo.setPatientCode(generateNextCode("S", patientInfo1.getPatientCode()));
//                        }
//                    }
//                });
                patientInfo.setResearchId(researchId);
                patientInfo.setPlanResearchId(researchId);
                patientInfo.setPatientName(mpPerson.getPersonName());
                patientInfo.setDeptId(Long.valueOf(Objects.requireNonNull(BindDeptEnum.getSzCode("150002"))));
                patientInfo.setPatientMobile(mobile);
                patientInfo.setPatientStatus("0");
                patientInfo.setCreateTime(DateUtils.getNowDate());
                patientInfo.setEnrollmentTime(DateUtils.getNowDate());
                patientInfo.setEnrollmentStatus("0");
                patientInfo.setChannelType("3");//渠道来源1后台2扫码  3为预问诊自动导入
                patientInfo.setCreateBy(userName);
                patientInfo.setPersonId(mpPerson.getPersonId());
                patientInfoMapper.insertPatientInfo(patientInfo);
//                    patientInfo.setEnrollmentResult("3");
//                    patientInfo.setEnrollmentHandlerStatus("3");
//                    patientInfo.setAllocatedHandlerStatus("3");
//                    patientInfo.setEnrollmentResultContent("确认入组");
                patientInfo.setEnrollmentResult("0");
                patientInfo.setEnrollmentHandlerStatus("1");
                patientInfo.setAllocatedHandlerStatus("0");
                patientInfo.setAllocatedStatus("0");
                patientInfoMapper.updatePatientInfo(patientInfo);
                ResearchPatient researchPatient = new ResearchPatient();
                researchPatient.setResearchId(researchId);
                researchPatient.setPersonId(mpPerson.getPersonId());
                researchPatient.setPatientId(patientInfo.getPatientId());
                researchPatient.setCreateTime(DateUtils.getNowDate());
                researchPatient.setPersonId(mpPerson.getPersonId());
                researchPatientMapper.insertResearchPatient(researchPatient);

                //生成基线
                Long followId = null;
                List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);
                PatientRecord patientRecord = new PatientRecord();
                if (!patientRecordTemplates.isEmpty()) {
                    followId = patientRecordTemplates.get(0).getFollowId();
                    patientRecord.setLockVisitTime(patientInfo.getCreateTime());
                }
                patientRecord.setFollowId(followId);
                patientRecord.setName("基线");
                patientRecord.setCreateTime(DateUtils.getNowDate());
                patientRecord.setResearchId(researchId);
                patientRecord.setPatientId(patientInfo.getPatientId());
                patientRecord.setVisitTime(DateUtils.getNowDate());
                patientRecord.setFollowTipStatus(FollowTipStatusConstants.BASELINE_REMINDER);
                patientRecord.setUpdateBy(userName);
                patientRecordMapper.insertPatientRecord(patientRecord);
                num++;

                // 默认入组后，自动进行确认操作
                AutoEnrollmentHandleVo autoEnrollmentHandleVo = new AutoEnrollmentHandleVo();

                autoEnrollmentHandleVo.setHandleType("3");
                autoEnrollmentHandleVo.setResearchId(researchInfo.getId());
                enrollmentHandle(autoEnrollmentHandleVo, patientInfo);
            }
        }

//        //此处为了适配唐都要求在队列未发布的情况下需要患者可以填写基线因此创建需要的表
//        if(!CollectionUtils.isEmpty(researchIds)) {
//            researchIds.forEach(researchId -> {researchInfoService.createTableInAddUser(researchId);});
//        }

        return num;
    }

    private static PatientInfo getFirstPatientCode(List<PatientInfo> patientInfos) {
        List<PatientInfo> result = patientInfos.stream()
                .sorted((p1, p2) -> {
                    // 处理 p1 的 code
                    int num1;
                    try {
                        num1 = Integer.parseInt(p1.getPatientCode().split("-")[1]);
                    } catch (Exception e) {
                        num1 = 0; // 默认值
                    }

                    // 处理 p2 的 code
                    int num2;
                    try {
                        num2 = Integer.parseInt(p2.getPatientCode().split("-")[1]);
                    } catch (Exception e) {
                        num2 = 0; // 默认值
                    }

                    return Integer.compare(num2, num1); // 降序排序
                })
                .collect(Collectors.toList());

        PatientInfo patientInfo1 = result.get(0);
        return patientInfo1;
    }

    /**
     * 从编码中提取数字部分（如 "D-0001" → 1）
     */
    private static int extractNumber(String code) {
        String numberStr = code.split("-")[1];  // 拆分后取第二部分
        return Integer.parseInt(numberStr);      // 转为整数
    }

    /**
     * 生成下一个编码（如 "D-0010" → "D-0011"）
     */
    private static String generateNextCode(String flag, String currentMaxCode) {
        int currentNumber = extractNumber(currentMaxCode);
        int nextNumber = currentNumber + 1;
        return String.format(flag + "-%04d", nextNumber);  // 格式化为 4 位数字
    }


    /**
     * 根据部门信息查看队列信息，取队列中的第一个队列为默认队列对患者进行自动入组 自动入组到第一个队列
     */
    public ResearchInfo autoEnrollmentList(String deptId) {

        ResearchInfo researchInfo = new ResearchInfo();
        researchInfo.setDeptId(Long.valueOf(deptId));
        researchInfo.setId(Long.valueOf("2407"));
        researchInfo.setDelFlag("0");

//        if (researchInfos.isEmpty()) {
//            return new ResearchInfo();
//        }

        return researchInfoMapper.selectResearchInfoById(Long.valueOf("2407")); //researchInfos.stream().min(Comparator.comparing(ResearchInfo::getCreateTime)).get();
    }


    @Transactional
    public int enrollmentHandle(AutoEnrollmentHandleVo autoEnrollmentHandleVo, PatientInfo patientInfo) {

        PatientInfo patientInfo1 = patientInfoMapper.selectPatientInfoByPatientId(patientInfo.getPatientId());
        if (!Objects.isNull(patientInfo1) && "3".equals(autoEnrollmentHandleVo.getHandleType())
                && !Objects.isNull(patientInfo1.getRefuseCount()) && patientInfo1.getRefuseCount() > 2
                && "1".equals(patientInfo1.getChannelType())) {
            log.error("当前科室当前病种当前队列拒绝入组的次数上限2次，超过2次的患者可以纳排但是不能确认/修改入组，可以取消入组，只能扫码入组");
            return 1;
        }
//        PatientInfo patient = new PatientInfo();
//        patient.setPatientId(patientInfo.getPatientId());
//        patient.setEnrollmentResult(autoEnrollmentHandleVo.getHandleType());
//        patient.setEnrollmentHandlerStatus(autoEnrollmentHandleVo.getHandleType());
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(autoEnrollmentHandleVo.getResearchId());
        //3确认入组
        if ("3".equals(autoEnrollmentHandleVo.getHandleType())) {
            //3.pc端确认入组, pc自己发起的需要app端去确认; app发起的, pc端此处操作直接入组
//            PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientInfo.getPatientId());
            /*渠道来源是app端, 则直接处理完毕, 入组, 否则入组进行中*/
            //处理状态是3, 则不走此项入组 (重入组逻辑)!!!, 已完成的重入组不走此项
            /**
             * 排列组合状态, col_1 来源渠道(1后台,2扫码) | col_2 处理状态 (1待处理,2进行中,3已完成), col_3 入组状态(0待分配,1已入组)
             * 1,1 发起邀请 1,2 入组中,0
             * 1,2 取消入组
             * 1,3 重新入组
             * 2,1 审核确认, 审核拒绝
             * 2,2
             * 2,3 重新入组
             */

            if ("3".equals(patientInfo.getChannelType()) && !"3".equals(patientInfo.getEnrollmentHandlerStatus())) { // 预问诊自动入组

                PatientInfo pio = patientInfoMapper.selectPatientInfoByPatientId(patientInfo.getPatientId());
                if ("2".equals(pio.getEnrollmentHandlerStatus())) {
                    throw new ServiceException("您已重新入组,请稍后!");
                }
                //进行中
                patientInfo.setEnrollmentStatus("0");
                patientInfo.setEnrollmentHandlerStatus("2");
                patientInfo.setEnrollmentTime(new Date());
                patientInfo.setResearchId(autoEnrollmentHandleVo.getResearchId());
                patientInfo.setEnrollmentResultContent("入组确认中");
                patientInfo.setUpdateTime(new Date());
//            generateBaseRecord(patientInfo);
                patientInfo.setDeptId(researchInfo.getDeptId());
//                Long userId = SecurityUtils.getUserId();
//                patientInfo.setDoctorId(userId);
                if ("3".equals(patientInfo.getEnrollmentHandlerStatus())) {
                    /*重新入组*/
                    patientInfo.setChannelType("1");
                    patientInfo.setSmsSendCount(0);
                }
                patientInfoMapper.updatePatientInfo(patientInfo);
                clearFollowRecord(patientInfo);
                PatientRecord prd = patientRecordMapper.selectOldByPatientId(patientInfo.getPatientId());
                prd.setVisitTime(DateUtils.getNowDate());
                prd.setLockVisitTime(DateUtils.getNowDate());
                patientRecordMapper.updatePatientRecord(prd);

                //未绑定发送短信
                if (patientInfo.getAccountId() == null || patientInfo.getAccountId() == 0) {
                    patientInfo.setProjectName(researchInfo.getProjectName());
                    tencentService.asyncSendSms(patientInfo);
                }
                //消息和推送
                prd.setAccountIds(patientInfo.getAccountId());
                String msgContent = "您被邀请加入随访队列";
                AccountNotice accountNotice = new AccountNotice(prd, msgContent, "19");
                Map<String, Object> extjson = new HashMap<>();
                extjson.put("researchId", researchInfo.getId());
                extjson.put("type", "19");
                extjson.put("patientId", String.valueOf(patientInfo.getPatientId()));
                extjson.put("recordId", prd.getRecordId());
                extjson.put("researchName", researchInfo.getProjectName());
                extjson.put("researchDescription", researchInfo.getResearchSynopsis());
                accountNotice.setExtendJson(JSON.toJSONString(extjson));
                //用于消息推送
                accountNoticeMapper.insertAccountNotice(accountNotice);

                //用于主动查询 弹框消息
                createPopNotice(researchInfo, String.valueOf(patientInfo.getPatientId()));
            }
        }

        PatientRecord patientRecord = patientRecordMapper.selectOldByPatientId(patientInfo.getPatientId());
        if (patientRecord != null) {
            patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(patientInfo.getAccountId(), patientRecord.getRecordId(), "1", DateUtils.getNowDate()));
        }
        return 1;
    }

    /**
     * .
     * 清理随访
     *
     * @param patientInfo
     */
    private void clearFollowRecord(PatientInfo patientInfo) {
        List<PatientRecordTemplate> patientRecordTemplates =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(patientInfo.getResearchId());
        if (patientRecordTemplates != null && !patientRecordTemplates.isEmpty()) {
            for (PatientRecordTemplate patientRecordTemplate : patientRecordTemplates) {
                if (patientRecordTemplate.getFollowNum() != 0) {
                    FollowWorkListCmdForm followWorkListCmdForm = new FollowWorkListCmdForm();
                    followWorkListCmdForm.setFollowId(patientRecordTemplate.getFollowId());
                    followWorkListCmdForm.setResearchId(patientRecordTemplate.getResearchId());
                    followWorkListCmdForm.setPatientId(patientInfo.getPatientId());
                    if (followWorkListCmdForm.getFollowId() != null
                            && followWorkListCmdForm.getResearchId() != null
                            && followWorkListCmdForm.getPatientId() != null) {
                        patientRecordMapper.deletePatientRecord(followWorkListCmdForm);
                    }
                }
            }

        }
    }


    private void createPopNotice(ResearchInfo researchInfo, String patientId) {
        SysPopNotice sysPopNotice = new SysPopNotice();
        sysPopNotice.setResearchName(researchInfo.getProjectName());
        sysPopNotice.setUserId(patientId);
        sysPopNotice.setResearchId(researchInfo.getId());
        sysPopNotice.setIsread(0);
        sysPopNotice.setResearchDescription(researchInfo.getResearchSynopsis());
        sysPopNotice.setCreateTime(DateUtils.getNowDate());
        sysPopNoticeMapper.insertSysPopNotice(sysPopNotice);
    }


}
