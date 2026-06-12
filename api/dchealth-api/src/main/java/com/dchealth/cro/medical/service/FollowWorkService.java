package com.dchealth.cro.medical.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.enums.FollowTipStatusEnum;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.health.utils.HealthUtils;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.domain.PatientRecordOperLog;
import com.dchealth.cro.medical.domain.PatientRecordTemplate;
import com.dchealth.cro.medical.domain.WarnDoctorPatient;
import com.dchealth.cro.medical.form.FollowOverTimeHandleForm;
import com.dchealth.cro.medical.form.FollowWorkDetailsForm;
import com.dchealth.cro.medical.form.FollowWorkEnrollmentHandleForm;
import com.dchealth.cro.medical.form.FollowWorkHandleForm;
import com.dchealth.cro.medical.form.FollowWorkListCmdForm;
import com.dchealth.cro.medical.form.FollowWorkListForm;
import com.dchealth.cro.medical.form.FollowWorkQuestionForm;
import com.dchealth.cro.medical.form.PatientRecordDetailForm;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
import com.dchealth.cro.medical.mapper.PatientRecordOperLogMapper;
import com.dchealth.cro.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.cro.medical.mapper.WarnDoctorPatientMapper;
import com.dchealth.cro.medical.vo.FollowWorkListVO;
import com.dchealth.cro.medical.vo.FollowWorkTree;
import com.dchealth.cro.medical.vo.FollowWorkVO;
import com.dchealth.cro.medical.vo.PatientDetailVO;
import com.dchealth.cro.medical.vo.ResearchDiseasesSelect;
import com.dchealth.cro.question.domain.QuestionQuick;
import com.dchealth.cro.question.domain.QuestionRecord;
import com.dchealth.cro.question.form.QuestionQuickForm;
import com.dchealth.cro.question.form.QuestionRecordForm;
import com.dchealth.cro.question.mapper.QuestionQuickMapper;
import com.dchealth.cro.question.mapper.QuestionRecordFormMapper;
import com.dchealth.cro.question.mapper.QuestionRecordMapper;
import com.dchealth.cro.research.domain.ResearchDiseases;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.domain.ResearchPatient;
import com.dchealth.cro.research.domain.SysPopNotice;
import com.dchealth.cro.research.mapper.ResearchDiseasesMapper;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import com.dchealth.cro.research.mapper.ResearchPatientMapper;
import com.dchealth.cro.research.mapper.SysPopNoticeMapper;
import com.dchealth.cro.warn.domain.WarnEarlyRecord;
import com.dchealth.cro.warn.domain.WarnGuideConver;
import com.dchealth.cro.warn.domain.WarnGuideMsg;
import com.dchealth.cro.warn.domain.WarnGuideUser;
import com.dchealth.cro.warn.domain.WarnLeaveMessage;
import com.dchealth.cro.warn.domain.WarnMedicalEarlyCondition;
import com.dchealth.cro.warn.form.WarnGuideMsgForm;
import com.dchealth.cro.warn.form.WarnLeaveMessageForm;
import com.dchealth.cro.warn.mapper.WarnEarlyRecordMapper;
import com.dchealth.cro.warn.mapper.WarnGuideConverMapper;
import com.dchealth.cro.warn.mapper.WarnGuideMsgMapper;
import com.dchealth.cro.warn.mapper.WarnGuideUserMapper;
import com.dchealth.cro.warn.mapper.WarnLeaveMessageMapper;
import com.dchealth.cro.warn.mapper.WarnMedicalEarlyConditionMapper;

@Service
public class FollowWorkService  {

    private static final Logger log = LoggerFactory.getLogger(FollowWorkService.class);

    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private QuestionRecordMapper questionRecordMapper;
    @Autowired
    private QuestionRecordFormMapper questionRecordFormMapper;
    @Autowired
    private QuestionQuickMapper questionQuickMapper;
    @Autowired
    private WarnGuideUserMapper warnGuideUserMapper;
    @Autowired
    private WarnGuideConverMapper warnGuideConverMapper;
    @Autowired
    private WarnGuideMsgMapper warnGuideMsgMapper;
    @Autowired
    private WarnLeaveMessageMapper warnLeaveMessageMapper;
    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;

    @Autowired
    private WarnEarlyRecordMapper warnEarlyRecordMapper;

    @Autowired
    private WarnMedicalEarlyConditionMapper warnMedicalEarlyConditionMapper;

    @Autowired
    private WarnDoctorPatientMapper warnDoctorPatientMapper;

    @Autowired
    private MpPersonMapper mpPersonMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
	private CroCoreConfig croCoreConfig;
    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private ResearchPatientMapper researchPatientMapper;
    @Autowired
    private SysPopNoticeMapper sysPopNoticeMapper;



    public PatientDetailVO details(FollowWorkDetailsForm followWorkDetailsForm) {
        PatientDetailVO patientDetailVO = new PatientDetailVO();
        PatientInfo  patientInfo = 	patientInfoMapper.selectPatientInfoById(followWorkDetailsForm.getPatientId());
    	if(patientInfo != null) {
    		 MpPerson patientBaseInfo = mpPersonMapper.selectPersonByPersonId(patientInfo.getPersonId());
    	        if (patientBaseInfo != null){
    	            patientDetailVO.setPatientName(patientBaseInfo.getPersonName());
    	            patientDetailVO.setBirthday(HealthUtils.getBirthFromIdCard(patientBaseInfo.getCardId()));
    	        }
    	}
        
        return patientDetailVO;
    }

    /**
     * 病种、队列树统计结构
     * reqType : 1随访2超窗3预警
     *
     * @param reqType
     * @return
     */
    public DchealthCloudResponseEntity<List<FollowWorkTree>> tree(String reqType, Long doctorId) {
        DchealthCloudResponseEntity<List<FollowWorkTree>> result = new DchealthCloudResponseEntity<>();
        //查看所有病种
        List<ResearchDiseases> researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByAuth();

        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByDoctorId(doctorId);
        List<ResearchInfo> distinctResearchInfos = researchInfos.stream().distinct().collect(Collectors.toList());
        distinctResearchInfos.forEach(rs -> {
                    if (StringUtils.equals("1", reqType)) {
                        getFollowInfos(rs);
                    }
                    if (StringUtils.equals("2", reqType)) {
                        getOverTimeIfos(rs);
                    }
                    if (StringUtils.equals("3", reqType)) {
                        getWarnInfos(rs);
                    }
                }

        );

        result.setData(FollowWorkTree.trees(researchDiseasesList, distinctResearchInfos).stream().filter(tree -> tree != null).collect(Collectors.toList()));
        return result;
    }

    private void getFollowInfos(ResearchInfo rs) {
        List<PatientRecord> list = patientRecordMapper.selectPatientRecordFollowStatusGroupInfoByResearchId(rs.getId());
        List<PatientRecord> patientRecordList =
                list.stream().filter(p -> !StringUtils.equals(p.getName(), "基线")).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(patientRecordList)) {
            patientRecordList =
                    patientRecordList.stream().filter(p -> !StringUtils.equals(p.getFollowStatus(), "0")).collect(Collectors.toList());
            for (PatientRecord patientRecord : patientRecordList) {
                if ("2".equals(patientRecord.getEvaluateStatusBack())){
                    rs.setEvaluatedNum(rs.getEvaluatedNum() + 1); //已完成评价
                }
                if ("1".equals(patientRecord.getFollowStatus())) {//待处理
                    rs.setPendingTasksNum(rs.getPendingTasksNum() + 1);
                } else if ("2".equals(patientRecord.getFollowStatus())) {//进行中
                    rs.setOngoingTasksNum(rs.getOngoingTasksNum() + 1);
                } else if ("3".equals(patientRecord.getFollowStatus())) {//已完成
                    rs.setCompletedTasksNum(rs.getCompletedTasksNum() + 1);
                }
            }
        }

    }

    private void getOverTimeIfos(ResearchInfo rs) {
        List<PatientRecord> list = patientRecordMapper.selectPatientRecordOverTimeGroupInfoByResearchId(rs.getId());
        if (!CollectionUtils.isEmpty(list)) {
            for (PatientRecord patientRecord : list) {
                if ("2".equals(patientRecord.getOverEvaluateStatusBack())){
                    rs.setEvaluatedNum(rs.getEvaluatedNum() + 1); //已完成评价
                }
                if ("1".equals(patientRecord.getOverTimeStatus())) {//待处理（超窗）
                    rs.setPendingTasksNum(rs.getPendingTasksNum() + 1);
                }else if ("2".equals(patientRecord.getOverTimeStatus())) {//已完成
                    rs.setOngoingTasksNum(rs.getOngoingTasksNum() + 1);
                }else if ("3".equals(patientRecord.getOverTimeStatus())) {//已完成
                    rs.setCompletedTasksNum(rs.getCompletedTasksNum() + 1);
                }
            }
        }
    }


    private void getWarnInfos(ResearchInfo rs) {
        List<PatientRecord> list = patientRecordMapper.selectPatientRecordWarnGroupInfoByResearchId(rs.getId());
        List<PatientRecord> patientRecordList =
                list.stream().filter(p -> !StringUtils.equals(p.getName(), "基线")).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(patientRecordList)) {
            for (PatientRecord patientRecord : patientRecordList) {
                if ("2".equals(patientRecord.getWarnEvaluateStatusBack())){
                    rs.setEvaluatedNum(rs.getEvaluatedNum() + 1); //已完成评价
                }
                if ("0".equals(patientRecord.getWarnStatus())) {//无预警

                } else if ("1".equals(patientRecord.getWarnStatus())) {//待处理
                    rs.setPendingTasksNum(rs.getPendingTasksNum() + 1);
                } else if ("2".equals(patientRecord.getWarnStatus())) {//进行中
                    rs.setOngoingTasksNum(rs.getOngoingTasksNum() + 1);
                } else if ("3".equals(patientRecord.getWarnStatus())) { // 已完成
                    rs.setCompletedTasksNum(rs.getCompletedTasksNum() + 1);
                }
            }
        }

    }

    public DchealthCloudResponseEntity<FollowWorkVO> handleList(FollowWorkListForm followWorkListForm) {
        DchealthCloudResponseEntity<FollowWorkVO> result = new DchealthCloudResponseEntity<>();
        FollowWorkVO followWorkVO = new FollowWorkVO();
        List<FollowWorkListVO> pageFollowWorkList = new ArrayList<>();
        List<FollowWorkListVO> followWorkList =
                patientRecordMapper.selectFollowWorkPatientRecordLis(followWorkListForm);
        List<FollowWorkListVO> followWorkNoBaseLineListVOS =
                followWorkList.stream().filter(f -> !StringUtils.equals(f.getStageName(), "基线")).collect(Collectors.toList());
        //分页处理
        if (!CollectionUtils.isEmpty(followWorkNoBaseLineListVOS)) {
            List<FollowWorkListVO> collect =
                    followWorkNoBaseLineListVOS.stream().sorted(Comparator.comparing(FollowWorkListVO::getRecordId).reversed()).collect(Collectors.toList());
            pageFollowWorkList = collect.subList(followWorkListForm.getPageNo() - 1,
                    followWorkListForm.getPageSize() < followWorkNoBaseLineListVOS.size() ? followWorkListForm.getPageSize() :
                            followWorkNoBaseLineListVOS.size());
        }

        followWorkVO.setFollowWorkListVO(pageFollowWorkList);
//        int recordCount = patientRecordMapper.selectFollowWorkPatientRecordCount(followWorkListForm);
//
//        List<FollowWorkListVO> baseFollowWorkList = followWorkList.stream().filter(fw -> {
//            return ("基线".equals(fw.getStageName()) && StringUtils.equals(fw.getWarnStatus(),
//                    followWorkListForm.getWarnStatus()));
//        }).collect(Collectors.toList());

        followWorkVO.setRecoedSum(followWorkNoBaseLineListVOS.size());
        result.setData(followWorkVO);
        return result;
    }


    public DchealthCloudResponseEntity<FollowWorkVO> overList(FollowWorkListForm followWorkListForm) {
        log.info("followWorkListForm:{}", JSON.toJSONString(followWorkListForm));
        DchealthCloudResponseEntity<FollowWorkVO> result = new DchealthCloudResponseEntity<>();
        FollowWorkVO followWorkVO = new FollowWorkVO();
        List<FollowWorkListVO> followWorkList = patientRecordMapper.selectFollowWorkList(followWorkListForm);
        Integer listSize = patientRecordMapper.selectFollowWorkListCount(followWorkListForm);
        //后端失访和前端状态对应不上，后台1,2,3，api接口用0,1,2
        if(followWorkList.size()>0){
            for(FollowWorkListVO f:followWorkList){
                if("1".equals(f.getLoseType())){
                    f.setLoseType("0");
                }else if("2".equals(f.getLoseType())){
                    f.setLoseType("1");
                }else if("3".equals(f.getLoseType())){
                    f.setLoseType("2");
                }else if("4".equals(f.getLoseType())){
                    f.setLoseType("3");
                }
            }
        }
        followWorkVO.setFollowWorkListVO(followWorkList);
        followWorkVO.setRecoedSum(listSize);
        result.setData(followWorkVO);
        return result;
    }

    public DchealthCloudResponseEntity<FollowWorkVO> warnList(FollowWorkListForm followWorkListForm) {

        DchealthCloudResponseEntity<FollowWorkVO> result = new DchealthCloudResponseEntity<>();
        FollowWorkVO followWorkVO = new FollowWorkVO();
        List<FollowWorkListVO> pageFollowWorkList = new ArrayList<>();
        List<FollowWorkListVO> followWorkList =
                patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);
        //基线不展示
        List<FollowWorkListVO> followWorkNoBaseLineListNew =
                followWorkList.stream().filter(fw -> !"基线".equals(fw.getStageName())).collect(Collectors.toList());

        List<FollowWorkListVO> finalFollowWorkList = filterAndSetWarnLevel(followWorkNoBaseLineListNew);
        int totalSize = finalFollowWorkList.size();

        if (!CollectionUtils.isEmpty(finalFollowWorkList)) {
            List<FollowWorkListVO> collect =
                    finalFollowWorkList.stream().sorted(Comparator.comparing(FollowWorkListVO::getRecordId).reversed()).collect(Collectors.toList());
            pageFollowWorkList = collect.subList(followWorkListForm.getPageNo() - 1,
                    followWorkListForm.getPageSize() < totalSize ? followWorkListForm.getPageSize() : totalSize);
        }
        followWorkVO.setRecoedSum( totalSize);
        followWorkVO.setFollowWorkListVO(pageFollowWorkList);
        result.setData(followWorkVO);
        return result;
    }

    public DchealthCloudResponseEntity<List<FollowWorkListVO>> allocatedList(FollowWorkListForm followWorkListForm) {
        DchealthCloudResponseEntity<List<FollowWorkListVO>> result = new DchealthCloudResponseEntity<>();
        result.setData(patientInfoMapper.allocatedList(followWorkListForm));
        return result;
    }

    @Transactional
    public DchealthCloudResponseEntity<CommonResult> enrollmentHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {

        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        PatientInfo patientInfo1 = new PatientInfo();
        patientInfo1.setPatientId(workEnrollmentHandleForm.getPatientId());
        patientInfo1.setEnrollmentResult(workEnrollmentHandleForm.getHandleType());
        patientInfo1.setEnrollmentHandlerStatus(workEnrollmentHandleForm.getHandleType());
        //1待分配2变更3确认入组
        if ("1".equals(workEnrollmentHandleForm.getHandleType())) {
            patientInfo1.setEnrollmentResultContent("待分配");
            patientInfo1.setAllocatedStatus("1");
            patientInfo1.setAllocatedHandlerStatus("1");
            patientInfoMapper.updatePatientInfo(patientInfo1);
        } else if ("2".equals(workEnrollmentHandleForm.getHandleType())) {
            ResearchInfo researchInfo =
                    researchInfoMapper.selectResearchInfoById(workEnrollmentHandleForm.getResearchId());
            patientInfo1.setEnrollmentStatus("1");
            patientInfo1.setEnrollmentTime(new Date());
            patientInfo1.setResearchId(workEnrollmentHandleForm.getResearchId());
            if (researchInfo != null) {
                patientInfo1.setEnrollmentResultContent("入组变更到" + researchInfo.getProjectName());
            }
            generateBaseRecord(patientInfo1);
            patientInfoMapper.updatePatientInfo(patientInfo1);
        } else if ("3".equals(workEnrollmentHandleForm.getHandleType())) {
            PatientInfo pio = patientInfoMapper.selectPatientInfoById(workEnrollmentHandleForm.getPatientId());
            if (pio == null) {
                throw new ServiceException("该患者不存在");
            }
            if (!"2".equals(pio.getEnrollmentHandlerStatus())) {
                //当前状态不许变更
                log.info("邀请已失效, 原因: 当前处理状态"+pio.getEnrollmentHandlerStatus());
                throw new ServiceException("邀请已失效");
            }
            //更新弹框消息
            SysPopNotice query = new  SysPopNotice();
            query.setUserId(String.valueOf(pio.getPatientId()));
            query.setIsread(0);
            query.setResearchId(workEnrollmentHandleForm.getResearchId());

            List<SysPopNotice> sysPopNotices = sysPopNoticeMapper.selectSysPopNoticeList(query);
            if(sysPopNotices.size() > 0){
                SysPopNotice sysPopNotice = sysPopNotices.get(0);
                sysPopNotice.setIsread(1);
                sysPopNotice.setUpdateTime(new Date());
                sysPopNotice.setId(sysPopNotice.getId());
                sysPopNoticeMapper.updateSysPopNotice(sysPopNotice);
            }else {
                throw new ServiceException("邀请已被医生取消");
            }

            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(workEnrollmentHandleForm.getPatientId());
            patientInfo.setEnrollmentResult(workEnrollmentHandleForm.getHandleType());
            //确认完成入组
            patientInfo.setEnrollmentStatus("1");
            patientInfo.setEnrollmentHandlerStatus("3");
            patientInfo.setEnrollmentTime(new Date());
            patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
            patientInfo.setEnrollmentResultContent("确认入组");
            //清理随访
            clearFollowRecord(patientInfo);
//            generateBaseRecord(patientInfo);
            patientInfoMapper.updatePatientInfo(patientInfo);

            PatientRecord prd = patientRecordMapper.selectOldByPatientId(workEnrollmentHandleForm.getPatientId());
//          //发给医生的站内信 todo
//            prd.setAccountId(pio.getAccountId());
//            String msgContent = "患者"+pio.getPatientName()+"("+pio.getPersonId()+")已加入随访队列";
//            //发给医生的站内信 todo
//            AccountNotice accountNotice = new AccountNotice();
//            accountNotice.setAccountId(pio.getDoctorId());
//            accountNotice.setType("11");
//            accountNotice.setCreateTime(DateUtils.getNowDate());
//            accountNotice.setIsRead(Constants.NO);
//            accountNotice.setTitle("入组提醒");
//            accountNotice.setContent(msgContent);
//            AccountNoticeNewVo accountNoticeVo = new AccountNoticeNewVo();
//            accountNoticeVo.setRecordId(prd.getRecordId());
//            accountNoticeVo.setType("11");
//            accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo));

            String msgContent = "您已加入随访队列，可查看队列详情";
            prd.setAccountIds(pio.getAccountId());
            AccountNotice accountNotice = new AccountNotice(prd,msgContent,"11");
            accountNoticeMapper.insertAccountNotice(accountNotice);


        } else if ("4".equals(workEnrollmentHandleForm.getHandleType())) {
            // todo 操作跟踪, 分析链路问题, every method
            PatientInfo pio = patientInfoMapper.selectPatientInfoById(workEnrollmentHandleForm.getPatientId());
            if (pio == null) {
                throw new ServiceException("该患者不存在");
            }
            if (!"2".equals(pio.getEnrollmentHandlerStatus())) {
                //当前状态不许变更
                log.info("邀请已失效, 原因: 当前处理状态"+pio.getEnrollmentHandlerStatus());
                throw new ServiceException("邀请已失效");
            }
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(workEnrollmentHandleForm.getPatientId());
            patientInfo.setEnrollmentStatus("0");
            patientInfo.setEnrollmentHandlerStatus("3"); //可以重新入组
            patientInfo.setEnrollmentTime(new Date());
            patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
            Integer refuseCount = pio.getRefuseCount();
            if (Objects.isNull(refuseCount)||0==refuseCount) {
                refuseCount = 1;
            }else {
                refuseCount++;
            }
            patientInfo.setEnrollmentResultContent("患者拒绝入组,"+refuseCount+"次");
            if (refuseCount >= 3) {
                throw new ServiceException("您已拒绝多次");
            }
//            generateBaseRecord(patientInfo);

            PatientRecord prd = patientRecordMapper.selectOldByPatientId(workEnrollmentHandleForm.getPatientId());

            // todo 发给医生的站内信 ...
//            String msgContent = "患者"+pio.getPatientName()+"("+pio.getPersonId()+")拒绝加入随访队列";
//            AccountNotice accountNotice = new AccountNotice();
//            accountNotice.setAccountId(pio.getDoctorId());
//            accountNotice.setType("21");
//            accountNotice.setCreateTime(DateUtils.getNowDate());
//            accountNotice.setIsRead(Constants.NO);
//            accountNotice.setTitle("拒绝入组");
//            accountNotice.setContent(msgContent);
//            AccountNoticeNewVo accountNoticeVo = new AccountNoticeNewVo();
//            accountNoticeVo.setRecordId(prd.getRecordId());
//            accountNoticeVo.setType("21");
//            accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo));
//            prd.setAccountId(pio.getAccountId());

            String msgContent = "您已拒绝加入随访队列";
            prd.setAccountIds(pio.getAccountId());
            AccountNotice accountNotice = new AccountNotice(prd,msgContent,"21");
            accountNoticeMapper.insertAccountNotice(accountNotice);

            //计算拒绝次数
            patientInfo.setRefuseCount(refuseCount);
            patientInfoMapper.updatePatientInfo(patientInfo);
            //更新弹框消息
            SysPopNotice query = new  SysPopNotice();
            query.setUserId(String.valueOf(pio.getPatientId()));
            query.setIsread(0);
            query.setResearchId(workEnrollmentHandleForm.getResearchId());

            List<SysPopNotice> sysPopNotices = sysPopNoticeMapper.selectSysPopNoticeList(query);
            if(sysPopNotices.size() > 0){
                SysPopNotice sysPopNotice = sysPopNotices.get(0);
                sysPopNotice.setIsread(1);
                sysPopNotice.setUpdateTime(new Date());
                sysPopNotice.setId(sysPopNotice.getId());
                sysPopNoticeMapper.updateSysPopNotice(sysPopNotice);
            }

        }
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    @Transactional
    public DchealthCloudResponseEntity<CommonResult> allocatedHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(workEnrollmentHandleForm.getResearchId());
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(workEnrollmentHandleForm.getPatientId());
        patientInfo.setEnrollmentStatus("1");
        patientInfo.setEnrollmentTime(new Date());
        patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
        patientInfo.setAllocatedHandlerStatus("3");
        if (researchInfo != null) {
            patientInfo.setEnrollmentResultContent("入组变更到" + researchInfo.getProjectName());
        }
        generateBaseRecord(patientInfo);
        patientInfoMapper.updatePatientInfo(patientInfo);
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    public DchealthCloudResponseEntity<List<ResearchDiseasesSelect>> diseasesResearchSelect(Long researchId) {
        DchealthCloudResponseEntity<List<ResearchDiseasesSelect>> result = new DchealthCloudResponseEntity<>();
        List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuth();
        if (researchDiseases == null || researchDiseases.isEmpty()) {
            return result;
        }
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByAuth();
        Map<Long, List<ResearchInfo>> researchMap = null;

        Long deptId;
        if (researchInfos != null && !researchInfos.isEmpty()) {
            if (!Objects.isNull(researchId)) {
                Optional<ResearchInfo> researchInfoOptional =
                        researchInfos.stream().filter(r -> Objects.equals(r.getId(), researchId)).findFirst();
                if (!researchInfoOptional.isPresent()) {
                    result.setMsg("旧队列信息查询异常");
                    return result;
                }
                deptId = researchInfoOptional.get().getDeptId();
                if (Objects.isNull(deptId)) {
                    result.setMsg("旧队列对应科室查询异常");
                    return result;
                }

                researchDiseases =
                        researchDiseases.stream().filter(rd -> Objects.equals(rd.getDeptId(), deptId)).collect(Collectors.toList());
                researchInfos =
                        researchInfos.stream().filter(r -> r.getDeptId().equals(deptId)).collect(Collectors.toList());
            }
            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo::getDiseaseTypeId));
        }


        Map<Long, List<ResearchInfo>> finalResearchMap = researchMap;
        result.setData(researchDiseases.stream().map(x -> new ResearchDiseasesSelect(x,
                finalResearchMap != null && !finalResearchMap.isEmpty() ?
                        (finalResearchMap.containsKey(x.getDiseaseId()) ?
                                finalResearchMap.get(x.getDiseaseId()).stream().map(y -> new ResearchDiseasesSelect(y)).collect(Collectors.toList()) : null) : null)).collect(Collectors.toList()));
        return result;
    }


    public DchealthCloudResponseEntity<List<ResearchDiseasesSelect>> diseasesResearchSelectNew(Long researchId,Long userId) {
        DchealthCloudResponseEntity<List<ResearchDiseasesSelect>> result = new DchealthCloudResponseEntity<>();
        List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByUserId(userId);
        if (researchDiseases == null || researchDiseases.isEmpty()) {
            return result;
        }
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByUserId(userId);
        Map<Long, List<ResearchInfo>> researchMap = null;

        Long deptId;
        if (researchInfos != null && !researchInfos.isEmpty()) {
            if (!Objects.isNull(researchId)) {
                Optional<ResearchInfo> researchInfoOptional =
                        researchInfos.stream().filter(r -> Objects.equals(r.getId(), researchId)).findFirst();
                if (!researchInfoOptional.isPresent()) {
                    result.setMsg("旧队列信息查询异常");
                    return result;
                }
                deptId = researchInfoOptional.get().getDeptId();
                if (Objects.isNull(deptId)) {
                    result.setMsg("旧队列对应科室查询异常");
                    return result;
                }

                researchDiseases =
                        researchDiseases.stream().filter(rd -> Objects.equals(rd.getDeptId(), deptId)).collect(Collectors.toList());
                researchInfos =
                        researchInfos.stream().filter(r -> r.getDeptId().equals(deptId)).collect(Collectors.toList());
            }
            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo::getDiseaseTypeId));
        }


        Map<Long, List<ResearchInfo>> finalResearchMap = researchMap;
        result.setData(researchDiseases.stream().map(x -> new ResearchDiseasesSelect(x,
                finalResearchMap != null && !finalResearchMap.isEmpty() ?
                        (finalResearchMap.containsKey(x.getDiseaseId()) ?
                                finalResearchMap.get(x.getDiseaseId()).stream().map(y -> new ResearchDiseasesSelect(y)).collect(Collectors.toList()) : null) : null)).collect(Collectors.toList()));
        return result;
    }

    @Transactional
    public DchealthCloudResponseEntity<CommonResult> visitTimeHandle(FollowOverTimeHandleForm followOverTimeHandleForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(followOverTimeHandleForm.getRecordId());
        patientRecord.setOverTimeStatus("3");
        patientRecord.setOverTimeHandleStatus(followOverTimeHandleForm.getHandleType());
        //1更新随访时间2忽略本次3失访
        if ("1".equals(followOverTimeHandleForm.getHandleType())) {
            patientRecord.setVisitTime(followOverTimeHandleForm.getVisitTime());
        } else if ("2".equals(followOverTimeHandleForm.getHandleType())) {

        } else if ("3".equals(followOverTimeHandleForm.getHandleType())) {
            PatientRecord recordDB =
                    patientRecordMapper.selectPatientRecordByRecordId(followOverTimeHandleForm.getRecordId());
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(recordDB.getPatientId());
            patientInfo.setLossCause(followOverTimeHandleForm.getLossCause());
            patientInfo.setLossCauseOther(followOverTimeHandleForm.getLossCauseOther());
            patientInfo.setPatientStatus("4");//失访
            patientInfoMapper.updatePatientInfo(patientInfo);
        }
        patientRecordMapper.updatePatientRecord(patientRecord);
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    @Transactional
    public DchealthCloudResponseEntity<CommonResult> submitOpinionHandle(FollowWorkHandleForm followWorkHandleForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        DchealthCloudResponseEntity<CommonResult> result1 = onlyOnceSwitch(followWorkHandleForm, result, commonResult);
        if (result1 != null) return result1;

        PatientRecord prd = patientRecordMapper.selectPatientRecordByRecordId(followWorkHandleForm.getRecordId());
        if("1".equals(prd.getWarnStatus()) || "2".equals(prd.getWarnStatus())){
            result.setCode(CroApiCodeConstant.WARN_NOT_HANDLE.getCode());
            return result;
        }

        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(followWorkHandleForm.getRecordId());
        patientRecord.setFollowOpinion(followWorkHandleForm.getFollowOpinion());
        patientRecord.setFollowOpinionUserId(followWorkHandleForm.getAccountIds());
        patientRecord.setEdcStatus("1");
        patientRecord.setFollowStatus("3");
        patientRecord.setUpdateBy(followWorkHandleForm.getUpdateBy());
        patientRecord.setEvaluateStatusBack("1");
        patientRecord.setEvaluateStatus("1");
        patientRecord.setFollowCompleteTime(DateUtils.getNowDate());
        if ("1".equals(followWorkHandleForm.getAdjustType())) {
            patientRecord.setFollowTipStatus(5);
        }
        if ("2".equals(followWorkHandleForm.getAdjustType())) {
            patientRecord.setFollowTipStatus(6);
        }
        patientRecordMapper.updatePatientRecord(patientRecord);
        PatientInfo old = patientInfoMapper.selectPatientInfoById(followWorkHandleForm.getPatientInfoId());
        if ("2".equals(followWorkHandleForm.getAdjustType())) {//更换队列
            //患者已经在队列B中,就不能再切换到B中
            PatientInfo p =
                    patientInfoMapper.selectPatientByResearchIdAndPatietnId(followWorkHandleForm.getResearchId(), followWorkHandleForm.getPatientInfoId());
            if (!Objects.isNull(p)) {
                commonResult.setResult(true);
                result.setData(commonResult);
                result.setMsg("用户已存在于队列中，不能重复入组队列");
                return result;
            }

            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(followWorkHandleForm.getPatientInfoId());
            patientInfo.setPatientStatus("3");
            patientInfo.setPatientActivityStatus("2");
            patientInfo.setAdjustType("2");
            patientInfo.setPlanResearchId(followWorkHandleForm.getResearchId());
            patientInfoMapper.updatePatientInfo(patientInfo);
            // 新队列信息 [researchId新队列ID]
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(followWorkHandleForm.getResearchId());
            PatientInfo newPatientInfo = new PatientInfo(old, researchInfo, followWorkHandleForm.getUpdateBy());
            patientInfoMapper.insertPatientInfo(newPatientInfo);
            generateNewResearchBaseRecord(newPatientInfo);

            ResearchPatient researchPatient = new ResearchPatient();
            researchPatient.setResearchId(newPatientInfo.getResearchId());
            researchPatient.setPersonId(newPatientInfo.getPersonId());
            researchPatient.setPatientId(newPatientInfo.getPatientId());
            researchPatient.setCreateTime(DateUtils.getNowDate());
            researchPatientMapper.insertResearchPatient(researchPatient);
        }
        patientRecord.setAccountIds(old.getAccountId());
        String msgContent = "您有一条随访建议请及时查看";
        AccountNotice accountNotice = new AccountNotice(patientRecord,msgContent,"9");
        accountNoticeMapper.insertAccountNotice(accountNotice);

        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    private void generateNewResearchBaseRecord(PatientInfo patientInfo) {
        List<PatientRecordTemplate> patientRecordTemplates =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(patientInfo.getResearchId());
        if (patientRecordTemplates != null && !patientRecordTemplates.isEmpty()) {
            patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate::getFollowNum));
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setResearchId(patientInfo.getResearchId());
            patientRecord.setPatientId(patientInfo.getPatientId());
            patientRecord.setStageNum(1);
            patientRecord.setVisitTime(patientInfo.getEnrollmentTime());
            patientRecord.setLockVisitTime(patientInfo.getEnrollmentTime());
            patientRecord.setCreateTime(new Date());
            patientRecord.setName("基线");
            patientRecord.setFollowId(patientRecordTemplates.get(0).getFollowId());
            patientRecord.setFollowStatus("0");
            patientRecord.setFollowTipStatus(0);
            patientRecordMapper.insertPatientRecord(patientRecord);
        }
    }
    private DchealthCloudResponseEntity<CommonResult> onlyOnceSwitch(FollowWorkHandleForm followWorkHandleForm,
                                                                     DchealthCloudResponseEntity<CommonResult> result
            , CommonResult commonResult) {
        if ("2".equals(followWorkHandleForm.getAdjustType())) {
            PatientInfo old = patientInfoMapper.selectPatientInfoByOldId(followWorkHandleForm.getPatientInfoId());
            if (!Objects.isNull(old)) {
                result.setCode(CroApiCodeConstant.ONLY_ONE_SWITCH_IS_ALLOWED_FOR_THE_SAME_QUEUE.code);
                result.setMsg(CroApiCodeConstant.ONLY_ONE_SWITCH_IS_ALLOWED_FOR_THE_SAME_QUEUE.msg);
                commonResult.setResult(true);
                result.setData(commonResult);
                return result;
            }
        }
        return null;
    }

    /**
     * 生成基线档案
     *
     * @param patientInfo
     */
    private void generateBaseRecord(PatientInfo patientInfo) {
        List<PatientRecordTemplate> patientRecordTemplates =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(patientInfo.getResearchId());
        if (patientRecordTemplates != null && !patientRecordTemplates.isEmpty()) {
            patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate::getFollowNum));
            PatientRecord patientRecord = new PatientRecord(patientInfo, patientRecordTemplates.get(0));
            patientRecordMapper.insertPatientRecord(patientRecord);
        }
    }

    /**.
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
                    if (followWorkListCmdForm.getFollowId()!=null
                    && followWorkListCmdForm.getResearchId()!=null
                    && followWorkListCmdForm.getPatientId()!=null){
                        patientRecordMapper.deletePatientRecord(followWorkListCmdForm);
                    }
                }
            }

        }
    }

    @Transactional
    public DchealthCloudResponseEntity<CommonResult> submitQuestion(FollowWorkQuestionForm followWorkQuestionForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        if (StringUtils.isNotBlankAndNotEmpty(followWorkQuestionForm.getPropertyFormsJson())) {
            QuestionRecord questionRecord = new QuestionRecord();
            questionRecord.setRecordId(Long.valueOf(followWorkQuestionForm.getRecordId()));
            questionRecord.setFromUserId(Long.valueOf(followWorkQuestionForm.getUserId()));
            questionRecord.setFromContent(followWorkQuestionForm.getFromContent());
            questionRecord.setQuestionStatus("0");//待处理
            questionRecord.setType("1");//app质疑
            questionRecord.setFromTime(new Date());
            //插入质疑表
            questionRecordMapper.insertQuestionRecord(questionRecord);
            List<FMedicalPropertyForm> list = JSON.parseArray(followWorkQuestionForm.getPropertyFormsJson(),
                    FMedicalPropertyForm.class);
            if (list.size() > 0) {
                //先删掉前面的质疑记录
                questionRecordFormMapper.deleteQuestionRecordFormByRecordId(Long.valueOf(followWorkQuestionForm.getRecordId()));
            }
            for (int i = 0; i < list.size(); i++) {
                QuestionRecordForm questionRecordForm = new QuestionRecordForm();
                questionRecordForm.setQuestionId(questionRecord.getQuestionId());
                questionRecordForm.setMedicalFormId(list.get(i).getMedicalFormId());
                questionRecordForm.setRecordId(Long.valueOf(followWorkQuestionForm.getRecordId()));
                //插入质疑表单关联表
                questionRecordFormMapper.insertQuestionRecordForm(questionRecordForm);
            }
            //更新随访质疑状态
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setRecordId(Long.valueOf(followWorkQuestionForm.getRecordId()));
            patientRecord.setQuestionStatus("1");//质疑状态
            patientRecord.setFollowStatus("2");//进行中
            patientRecord.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_QUESTIONING_REMINDER.getIndex());
            patientRecordMapper.updatePatientRecord(patientRecord);

            PatientRecord prd = patientRecordMapper.selectPatientRecordByRecordId(Long.valueOf(followWorkQuestionForm.getRecordId()));
            PatientInfo pi = patientInfoMapper.selectPatientInfoById(prd.getPatientId());
            patientRecord.setAccountIds(pi.getAccountId());
            String msgContent = "您有一条随访质疑请及时查看";
            AccountNotice accountNotice = new AccountNotice(patientRecord,msgContent,"10");
            accountNoticeMapper.insertAccountNotice(accountNotice);

        }
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    public DchealthCloudResponseEntity<CommonResult> submitquestionQuick(QuestionQuickForm questionQuickForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        QuestionQuick questionQuick = new QuestionQuick();
        questionQuick.setCreateUserId(questionQuickForm.getCreateUserId());
        questionQuick.setQuickContent(questionQuickForm.getQuickContent());
        questionQuick.setCreateTime(new Date());
        questionQuickMapper.insertQuestionQuick(questionQuick);
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    public DchealthCloudResponseEntity<List<QuestionQuick>> questionQuickList(QuestionQuickForm questionQuickForm) {
        DchealthCloudResponseEntity<List<QuestionQuick>> result = new DchealthCloudResponseEntity<>();
        QuestionQuick questionQuick = new QuestionQuick();
        questionQuick.setCreateUserId(questionQuickForm.getCreateUserId());
        List<QuestionQuick> questionQuickList = questionQuickMapper.selectQuestionQuickList(questionQuick);
        result.setData(questionQuickList);
        return result;
    }

    public DchealthCloudResponseEntity<CommonResult> savePatientRecordEvaluation(PatientRecordDetailForm infoForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(infoForm.getRecordId());
        if (patientRecord == null) {//档案不存在
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_NOT_EXIT.getCode());
            return result;
        }
        patientRecord.setEvaluateReply(infoForm.getEvaluateReply());
        patientRecord.setEvaluateQuality(infoForm.getEvaluateQuality());
        patientRecord.setEvaluatePrescription(infoForm.getEvaluatePrescription());
        patientRecord.setEvaluateStatus("2");
        patientRecord.setEvaluateReplyTime(DateUtils.getNowDate());
        patientRecordMapper.updatePatientRecord(patientRecord);
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    public DchealthCloudResponseEntity<FollowWorkVO> warnPatientList(FollowWorkListForm followWorkListForm) {
        DchealthCloudResponseEntity<FollowWorkVO> result = new DchealthCloudResponseEntity<>();
        FollowWorkVO followWorkVO = new FollowWorkVO();
        List<FollowWorkListVO> followWorkList = patientRecordMapper.selectFollowWorkWarnpatientList(followWorkListForm);
        List<FollowWorkListVO> pageFollowWorkList = new ArrayList<>();
        // 过滤出有预警的数据，并设置预警等级
        List<FollowWorkListVO> finalFollowWorkList = filterAndSetWarnLevel(followWorkList);
        int totalSize = finalFollowWorkList.size();
        //分业查询
        if (!CollectionUtils.isEmpty(finalFollowWorkList)) {
            List<FollowWorkListVO> collect =
                    finalFollowWorkList.stream().sorted(Comparator.comparing(FollowWorkListVO::getRecordId).reversed()).collect(Collectors.toList());
            pageFollowWorkList = collect.subList(followWorkListForm.getPageNo() - 1,
                    followWorkListForm.getPageSize() < totalSize ? followWorkListForm.getPageSize() : totalSize);
        }
        followWorkVO.setFollowWorkListVO(pageFollowWorkList);
        followWorkVO.setRecoedSum(totalSize);
        result.setData(followWorkVO);
        return result;
    }

    private Integer dictToValue(String dictName) {
        if(StringUtils.isBlank(dictName)) {
            return 0;
        }
        if(dictName.equals("一级")) {
            return 1;
        } else if(dictName.equals("二级")) {
            return 2;
        } else if(dictName.equals("三级")) {
            return 3;
        } else if(dictName.equals("轻度")) {
            return 1;
        } else if(dictName.equals("中度")) {
            return 2;
        } else if(dictName.equals("重度")) {
            return 3;
        } else if(dictName.equals("弱阳性")) {
            return 1;
        } else if(dictName.equals("阳性")) {
            return 3;
        } else if(dictName.equals("异常")) {
            return 1;
        } else if(dictName.equals("危急")) {
            return 3;
        } else {
            return 0;
        }

    }

    private List<FollowWorkListVO> filterAndSetWarnLevel(List<FollowWorkListVO> followWorkList) {
        List<FollowWorkListVO> finalFollowWorkList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(followWorkList)) {
            followWorkList.forEach(fw -> {
                List<WarnEarlyRecord> warnEarlyRecordList =
                        warnEarlyRecordMapper.selectWarnEarlyRecordListByRecordId(fw.getRecordId());
                if (!CollectionUtils.isEmpty(warnEarlyRecordList)) {
                    List<Long> warnMedicalEarlyConditionIds =
                            warnEarlyRecordList.stream().map(WarnEarlyRecord::getWarnMedicalEarlyConditionId).collect(Collectors.toList());
                    List<WarnMedicalEarlyCondition> warnMedicalEarlyConditionList =
                            warnMedicalEarlyConditionMapper.selectWarnMedicalEarlyConditionIds(warnMedicalEarlyConditionIds);
                    if (!CollectionUtils.isEmpty(warnMedicalEarlyConditionList)) {
                        int maxWarnLevel =
                                warnMedicalEarlyConditionList.stream().mapToInt(w -> dictToValue(w.getEarlyWarnLevel())).max().getAsInt();
                        fw.setEarlyWarnLevel(maxWarnLevel + "");
                        finalFollowWorkList.add(fw);
                    }
                }
            });
        }
        return finalFollowWorkList;
    }


    /**
     * 保存预警指导
     *
     * @param warnGuideMsgForm
     * @return
     */
    public Object savePatientWarnGuideMsg(WarnGuideMsgForm warnGuideMsgForm) {

        if (warnGuideMsgForm.getWarnGuideConverId() == null) {//没有会话，需要新建会话及人员关系
            //会话
            WarnGuideConver warnGuideConver = new WarnGuideConver();
            warnGuideConver.setCreateUserId(warnGuideMsgForm.getFromUserId());
            warnGuideConver.setDeptId(warnGuideMsgForm.getDeptId());
            warnGuideConver.setRecordId(warnGuideMsgForm.getRecordId());
            warnGuideConver.setCreateTime(new Date());
            warnGuideConverMapper.insertWarnGuideConver(warnGuideConver);
            warnGuideMsgForm.setWarnGuideConverId(warnGuideConver.getWarnGuideConverId());

            //关系
            WarnGuideUser pcUser = new WarnGuideUser();
            pcUser.setMemberId(warnGuideMsgForm.getFromUserId());
            pcUser.setFromUserType(warnGuideMsgForm.getFromUserType());
            pcUser.setWarnGuideConverId(warnGuideConver.getWarnGuideConverId());
            warnGuideUserMapper.insertWarnGuideUser(pcUser);

            //插入预警信息
            WarnGuideMsg warnGuideMsg = new WarnGuideMsg();
            warnGuideMsg.setWarnGuideConverId(warnGuideConver.getWarnGuideConverId());
            warnGuideMsg.setMsgType(warnGuideMsgForm.getMsgType());
            warnGuideMsg.setMsgText(warnGuideMsgForm.getMsgText());
            warnGuideMsg.setMsgImageUrl(warnGuideMsgForm.getMsgImageUrl());
            warnGuideMsg.setFromUserId(warnGuideMsgForm.getFromUserId());
            warnGuideMsg.setFromUserType(warnGuideMsgForm.getFromUserType());
            warnGuideMsg.setCreateTime(new Date());
            warnGuideMsgMapper.insertWarnGuideMsg(warnGuideMsg);

            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setRecordId(warnGuideMsgForm.getRecordId());
            patientRecord.setWarnStatus("2");
            patientRecordMapper.updatePatientRecord(patientRecord);
        }


        return "";
    }

    @Transactional
    public DchealthCloudResponseEntity<List<WarnLeaveMessage>> savePatientWarnLeaveMessage(WarnLeaveMessageForm leaveMessageForm) {
        DchealthCloudResponseEntity<List<WarnLeaveMessage>> result = new DchealthCloudResponseEntity<>();
        List<WarnLeaveMessage> list = new ArrayList<>();
        PatientRecord patientRecord1 =
                patientRecordMapper.selectPatientRecordByRecordId(leaveMessageForm.getRecordId());
        if (patientRecord1 != null) {
            //插入预警指导
            WarnLeaveMessage leaveMessage = new WarnLeaveMessage();
            BeanUtils.copyProperties(leaveMessageForm, leaveMessage);
            leaveMessage.setCreateTime(new Date());
            leaveMessage.setAccountId(leaveMessageForm.getAccountId());
            if ("2".equals(leaveMessageForm.getContentType()) && leaveMessageForm.getImageList() != null && leaveMessageForm.getImageList().size() > 0) {
                List<String> imageList = leaveMessageForm.getImageList();
                for (String imageUrl : imageList) {
                    leaveMessage.setContent(imageUrl);
                    warnLeaveMessageMapper.insertWarnLeaveMessage(leaveMessage);
                }
            } else if ("1".equals(leaveMessageForm.getContentType())) {
                warnLeaveMessageMapper.insertWarnLeaveMessage(leaveMessage);
            }
            patientRecord1.setWarnStatus("2");
            patientRecordMapper.updatePatientRecord(patientRecord1);
            if(StringUtils.equals(leaveMessageForm.getUserType(),"1")){
                handleWarnDockerPatientRelation(leaveMessageForm,patientRecord1);
            }
            list = warnLeaveMessageMapper.selectWarnLeaveMessageByRecordId(leaveMessageForm.getRecordId());
            if(list != null && list.size() > 0){
                list.forEach(x->{
                    if("1".equals(x.getUserType())){
                        SysUser sysUser = sysUserMapper.selectSysUserByUserId(x.getAccountId());
                        if (sysUser != null) {
                            x.setHeadImage(sysUser.getHeadImage()==null ? "https://hzfw.dchealthclinical.com/files/admin.png" : sysUser.getHeadImage());
                            x.setUserName(sysUser.getNickName() ==null ?"":sysUser.getNickName());
                        }
                    } else if ("2".equals(x.getUserType())) {
                        MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                        if (patientBaseInfo != null) {
                            x.setHeadImage(croCoreConfig.getUserHead());
                            x.setUserName(patientBaseInfo.getPersonName());
                        }
                    }
                });
            }
        }

        result.setData(list);
        return result;
    }

    private void handleWarnDockerPatientRelation(WarnLeaveMessageForm leaveMessageForm, PatientRecord patientRecord1) {
        // 预警医患关系表处理
        // 先查询，没有就插入，标记是第一个处理医生
        WarnDoctorPatient warnDoctorPatient = new WarnDoctorPatient();
        warnDoctorPatient.setResearchId(patientRecord1.getResearchId());
        warnDoctorPatient.setPatientId(patientRecord1.getPatientId());
        List<WarnDoctorPatient> warnDoctorPatientList =  warnDoctorPatientMapper.selectWarnDoctorPatient(warnDoctorPatient);
        if(CollectionUtils.isEmpty(warnDoctorPatientList)){
            warnDoctorPatient.setDoctorId(leaveMessageForm.getAccountId());
            warnDoctorPatient.setIsFirstDoctor(1);
            warnDoctorPatient.setIsDelete(0);
            warnDoctorPatient.setCreateTime(new Date());
            warnDoctorPatient.setUpdateTime(new Date());
            warnDoctorPatientMapper.insert(warnDoctorPatient);
        }else{
            // 已经保存的更新，否则插入
            warnDoctorPatient.setDoctorId(leaveMessageForm.getAccountId());
            Boolean hasSave = hasSaveWarnDoctorPatient(warnDoctorPatientList, warnDoctorPatient);
            if(hasSave){
                warnDoctorPatient.setUpdateTime(new Date());
                Optional<WarnDoctorPatient> firstDoctorOptional = warnDoctorPatientList.stream().filter(w ->
                        Objects.equals(w.getDoctorId(), leaveMessageForm.getAccountId())).findFirst();
                warnDoctorPatient.setIsFirstDoctor(firstDoctorOptional.isPresent()?
                        firstDoctorOptional.get().getIsFirstDoctor():0);
                warnDoctorPatientMapper.updateWarnDockerPatient(warnDoctorPatient);
            }else{
                warnDoctorPatient.setIsFirstDoctor(0);
                warnDoctorPatient.setIsDelete(0);
                warnDoctorPatient.setCreateTime(new Date());
                warnDoctorPatient.setUpdateTime(new Date());
                warnDoctorPatientMapper.insert(warnDoctorPatient);
            }
        }
    }

    Boolean hasSaveWarnDoctorPatient(List<WarnDoctorPatient> warnDoctorPatientList, WarnDoctorPatient warnDoctorPatient) {
        for (WarnDoctorPatient warn : warnDoctorPatientList) {
            if (Objects.equals(warn.getDoctorId(), warnDoctorPatient.getDoctorId()) && Objects.equals(warn.getPatientId(),
                    warnDoctorPatient.getPatientId()) && Objects.equals(warn.getResearchId(),
                    warnDoctorPatient.getResearchId())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 结束预警
     *
     * @param recordId
     * @return
     */
    public DchealthCloudResponseEntity<CommonResult> endWarn(Long recordId, Long accountId) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        if (patientRecord != null) {
            patientRecord.setWarnStatus("3");//已完成
            patientRecord.setUpdateTime(new Date());
            int rs = patientRecordMapper.updatePatientRecord(patientRecord);
            if (rs > 0) {
                if (accountId != null) {
                    patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(accountId,
                            recordId, "2", DateUtils.getNowDate()));
                }
                commonResult.setResult(true);
                result.setData(commonResult);
                return result;
            }
        }
        result.setCode(CroApiCodeConstant.FAIL.getCode());
        return result;
    }

    /**
     * 更新随访时间 : 随访超窗更新时间/忽略本次/失访
     *
     * @param infoForm
     * @return
     */
    public DchealthCloudResponseEntity<CommonResult> updatePatientRecordTime(PatientRecordDetailForm infoForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(infoForm.getRecordId());
        patientRecord.setOverTimeStatus("3");//已完成
        patientRecord.setOverEvaluateStatusBack("1");
        patientRecord.setOverTimeHandleStatus(infoForm.getOverTimeHandleStatus());
        patientRecord.setOverTimeHandleTime(DateUtils.getNowDate());
        if (patientRecord != null) {
            // 1更新随访时间2忽略本次3失访
            if ("1".equals(infoForm.getOverTimeHandleStatus())) {//更新随访时间
                patientRecord.setVisitTime(DateUtils.parseDate(infoForm.getVisitTime()));
                patientRecord.setFollowTipStatus(FollowTipStatusEnum.CHANGE_OF_FOLLOW_UP_TIME_REMINDER.getIndex());
            } else if ("2".equals(infoForm.getOverTimeHandleStatus())) {//忽略本地
                patientRecord.setFollowTipStatus(FollowTipStatusEnum.IGNORE_THIS_FOLLOW_UP_REMINDER.getIndex());
                patientRecord.setIgnoreType(infoForm.getIgnoreType());
            } else if ("3".equals(infoForm.getOverTimeHandleStatus())) {//失访
                PatientInfo patientInfo = new PatientInfo();
                patientInfo.setPatientId(patientRecord.getPatientId());
                //后端失访和前端状态对应不上，后台1,2,3，api接口用0,1,2
                if("0".equals(infoForm.getLoseType())){
                    infoForm.setLoseType("1");
                }else if("1".equals(infoForm.getLoseType())){
                    infoForm.setLoseType("2");
                }else if("2".equals(infoForm.getLoseType())){
                    infoForm.setLoseType("3");
                }else if("3".equals(infoForm.getLoseType())){
                    infoForm.setLoseType("4");
                }
                patientInfo.setLossCause(infoForm.getLoseType());
                patientInfo.setLossCauseOther(infoForm.getLoseReason());
                patientInfo.setPatientStatus("4");//失访
                patientInfoMapper.updatePatientInfo(patientInfo);

                patientRecord.setLoseType(infoForm.getLoseType());//失访状态
                patientRecord.setLoseReason(infoForm.getLoseReason());//失访原因
                patientRecord.setFollowTipStatus(FollowTipStatusEnum.MISSING_VISIT_REMINDER.getIndex());
            }
            patientRecord.setUpdateTime(new Date());
            int rs = patientRecordMapper.updatePatientRecord(patientRecord);
            if (rs > 0) {
                if (infoForm.getAccountId() != null) {
                    patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(infoForm.getAccountIds(), infoForm.getRecordId(), "3", DateUtils.getNowDate()));
                }
                commonResult.setResult(true);
                result.setData(commonResult);
                return result;
            }
        }
        result.setCode(CroApiCodeConstant.FAIL.getCode());
        return result;
    }
}