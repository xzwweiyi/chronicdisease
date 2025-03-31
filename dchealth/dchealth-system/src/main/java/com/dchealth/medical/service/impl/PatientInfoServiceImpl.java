package com.dchealth.medical.service.impl;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.common.annotation.DataScope;
import com.dchealth.common.constant.FollowTipStatusConstants;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.CommunalUtils;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.PageUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.medical.domain.vo.DiseasesResearchTree;
import com.dchealth.medical.domain.vo.PatientEnrollmentVo;
import com.dchealth.medical.domain.vo.ResearchPatientVO;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.mapper.ResearchPatientMapper;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.research.service.IResearchInfoService;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.mapper.FMedicalFormMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 患者信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-21
 */
@Service
public class PatientInfoServiceImpl implements IPatientInfoService {
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;
    @Autowired
    private ResearchPatientMapper researchPatientMapper;
    @Autowired
    private CmsArticleMapper cmsArticleMapper;
    @Autowired
    private IResearchInfoService researchInfoService;

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;


    @Autowired
    private MpPersonMapper mpPersonMapper;

    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    protected Logger logger = LoggerFactory.getLogger(PatientInfoServiceImpl.class);
    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    /**
     * 查询患者信息
     *
     * @param patientId 患者信息主键
     * @return 患者信息
     */
    @Override
    public PatientInfo selectPatientInfoByPatientId(Long patientId) {
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientId);
        return patientInfo;
    }

    /**
     * 查询患者信息列表
     *
     * @param patientInfo 患者信息
     * @return 患者信息
     */
    @Override
    public List<PatientInfo> selectPatientInfoList(PatientInfo patientInfo) {
        String patientStatusQuery = patientInfo.getPatientStatus();
        List<String> statusConditionList = new ArrayList<>();
        statusConditionList.add("2");
//        statusConditionList.add("3");
        if(patientInfo.getPatientStatus() != null && statusConditionList.contains(patientInfo.getPatientStatus())){
            patientInfo.setPatientStatus(null);
        }
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfo);
        //查询当前随访名称
        if (patientInfos != null && !patientInfos.isEmpty()) {
            patientInfos.forEach(info -> {
                PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByResearchIdAndPatientId(info.getResearchId(), info.getPatientId());
                if (patientRecord != null) {
                    info.setFollowName(patientRecord.getName());
                    info.setFollowId(patientRecord.getFollowId());
                }
                if ("0".equals(info.getPatientStatus()) || "1".equals(info.getPatientStatus()) || "2".equals(info.getPatientStatus())) {
                    info.setSearchType("2");
                } else {
                    info.setSearchType(info.getPatientStatus());
                }

//                ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(info.getResearchId());
//                if (researchInfo != null) {
//                    info.setProjectName(researchInfo.getProjectName());
//                }
//                SysDept sysDept = sysDeptMapper.selectDeptById(info.getDeptId());
//                if (sysDept != null) {
//                    sysDept.setDeptName(sysDept.getDeptName());
//                }
            });
        }
        //进行中、完成患者筛选
        if(StringUtils.isNotEmpty(patientStatusQuery)){
            List<String> inProgressStatus = new ArrayList<>();
            inProgressStatus.add("0");
            inProgressStatus.add("1");
            inProgressStatus.add("2");
            if("2".equals(patientStatusQuery)){
                patientInfos = patientInfos.stream().filter(info -> inProgressStatus.contains(info.getPatientStatus())).collect(Collectors.toList());
            }else if("3".equals(patientStatusQuery)){

            }
        }
        return patientInfos;
    }


    @Override
    public List<PatientInfo> selectPatientInfoListByAuth(PatientInfo requestPatientInfo) {
        PatientInfo patientInfoParam = new PatientInfo();
        BeanUtils.copyProperties(requestPatientInfo,patientInfoParam);
        List<Long> patientIds = null;
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfoParam.getResearchId());
        if(StringUtils.isNotBlankAndNotEmpty(patientInfoParam.getPatientStatus())){
            if("3".equals(patientInfoParam.getPatientStatus())){
                patientIds = patientRecordMapper.selectCompletedPatientIdByResearchId(patientInfoParam.getResearchId());
                //如果当前课题状态为待发布，则将填完基线的完成状态患者归为随访状态
                if(researchInfo!=null && "0".equals(researchInfo.getResearchStatus())){
                    patientIds.clear();
                }
                patientInfoParam.setPatientStatus(null);
                patientInfoParam.setEnrollmentStatus("1");
                patientInfoParam.setPatientIds(patientIds);
            }else if("1".equals(patientInfoParam.getPatientStatus())){//此处1代表进行中，包含：未填写、基线、随访 状态；即去除已完成、已失访、已退出
                //此处去除3已完成，sql中同时去除4.已失访6.已退出，即剩下的都是进行中
                patientIds = patientRecordMapper.selectCompletedPatientIdByResearchId(patientInfoParam.getResearchId());
                patientInfoParam.setNotPatientIds(patientIds);
                patientInfoParam.setPatientStatus(null);
                //如果当前课题状态为待发布，则将填完基线的完成状态患者归为随访状态
                if(researchInfo!=null &&"0".equals(researchInfo.getResearchStatus())){
                    patientInfoParam.setPatientIds(null);
                }
            }
        }

        PageUtils.startPage();
        patientInfoParam.setEnrollmentHandlerStatus("3");
        patientInfoParam.setEnrollmentStatus("1");
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoListByAuth(patientInfoParam);
        //查询当前随访名称
        if (patientInfos != null && !patientInfos.isEmpty()) {
            patientInfos.forEach(info -> {
                PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByResearchIdAndPatientId2(info.getResearchId(), info.getPatientId());
                if (patientRecord != null) {
                    info.setFollowName(patientRecord.getName());
                    info.setFollowId(patientRecord.getFollowId());
                    info.setEdcNames(patientRecord.getEdcNames());
                }
                setSearchType(requestPatientInfo.getPatientStatus(),info);
            });
        }
        return patientInfos;
    }

    private static void setSearchType(String requestPatientStatus,PatientInfo patientInfo) {
        if(StringUtils.isBlank(requestPatientStatus)){
            requestPatientStatus=patientInfo.getPatientStatus();
        }
        switch (requestPatientStatus){
            case "0":
                patientInfo.setSearchType("随访");
                break;
            case "1":
                patientInfo.setSearchType("随访");
                break;
            case "2":
                patientInfo.setSearchType("随访");
                break;
            case "3":
                patientInfo.setSearchType("已完成");
                break;
            case "4":
                patientInfo.setSearchType("失访");
                break;
            case "5":
                patientInfo.setSearchType("退出待审核");
                break;
            case "6":
                patientInfo.setSearchType("已退出");
                break;
            default: break;
        }
    }


    /**
     * 新增患者信息
     *
     * @param patientInfo 患者信息
     * @return 结果
     */
    @Override
    public int insertPatientInfo(PatientInfo patientInfo) {
        patientInfo.setCreateTime(DateUtils.getNowDate());
        return patientInfoMapper.insertPatientInfo(patientInfo);
    }

    /**
     * 修改患者信息
     *
     * @param patientInfo 患者信息
     * @return 结果
     */
    @Override
    public int updatePatientInfo(PatientInfo patientInfo) {
        patientInfo.setUpdateTime(DateUtils.getNowDate());
        return patientInfoMapper.updatePatientInfo(patientInfo);
    }

    /**
     * 批量删除患者信息
     *
     * @param patientIds 需要删除的患者信息主键
     * @return 结果
     */
    @Override
    public int deletePatientInfoByPatientIds(Long[] patientIds) {
        return patientInfoMapper.deletePatientInfoByPatientIds(patientIds);
    }

    /**
     * 删除患者信息信息
     *
     * @param patientId 患者信息主键
     * @return 结果
     */
    @Override
    public int deletePatientInfoByPatientId(Long patientId) {
        return patientInfoMapper.deletePatientInfoByPatientId(patientId);
    }

    @DataScope(deptAlias = "d")
    @Override
    public List<DiseasesResearchTree> selectPatientInfoTreeList(PatientInfo patientInfo) {
        List<DiseasesResearchTree> result = new ArrayList<>();
        //查询缓和信息
        PatientInfo info = patientInfoMapper.selectPatientInfoByPatientId(patientInfo.getPatientId());
        //查询患者相关队列
        List<ResearchPatient> researchPatients = researchPatientMapper.selectResearchPatientByPatId(info.getPersonId());
        if (researchPatients != null && !researchPatients.isEmpty()) {
            //患者所在所有队列id
            List<Long> researchIds = researchPatients.stream().map(ResearchPatient::getResearchId).collect(Collectors.toList());
            ResearchInfoVo vo = new ResearchInfoVo();
            vo.setResearchIds(researchIds);
            //根据队列id查询相关病种队列信息
            List<ResearchDiseasesVO> researchDiseases = researchDiseasesMapper.selectResearchDiseasesListTreeByIds(vo);
            //根据患者id查询随访相关信息
            List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordByPatientId(info.getPatientId());
            //根据患者id查询患教信息
            List<CmsArticle> cmsArticles = cmsArticleMapper.selectCmsArticleByAccountId(info.getAccountId());
            //通过followId查询所有的表单模版
            List<Long> followIds = patientRecords.stream().map(PatientRecord::getFollowId).collect(Collectors.toList());
            List<FMedicalForm> fMedicalForms = null;
            if (!CollectionUtils.isEmpty(followIds)) {
                fMedicalForms = fMedicalFormMapper.queryFMedicalFormList(followIds);
            }


            //树状图处理
            if (researchDiseases != null && !researchDiseases.isEmpty()) {
                Map<Long, List<ResearchDiseasesVO>> map = researchDiseases.stream().collect(Collectors.groupingBy(ResearchDiseasesVO::getDeptId));
                List<FMedicalForm> finalFMedicalForms = fMedicalForms;
                map.forEach((k, v) -> result.add(new DiseasesResearchTree(v, patientRecords, cmsArticles, patientInfo.getPatientId(), finalFMedicalForms)));
            }
        }

//        //患者健康档案
//        PatientBaseInfo pb = patientBaseInfoMapper.selectBasePatientInfoById(info.getBaseId());
//        List<PatientHealthRecord> patientHealthRecord = new ArrayList<>();
//        if (researchPatients != null && !researchPatients.isEmpty()) {
//            //患者所在所有队列id
//            List<Long> researchIds = researchPatients.stream().map(ResearchPatient::getResearchId).collect(Collectors.toList());
//            ResearchInfoVo vo = new ResearchInfoVo();
//            vo.setResearchIds(researchIds);
//            //根据队列id查询相关病种队列信息
//            List<ResearchDiseasesVO> researchDiseases = researchDiseasesMapper.selectResearchDiseasesListTreeByIds(vo);
//            //查询患者健康档案信息
//            PatientHealthRecord phr = new PatientHealthRecord();
//            phr.setBasePatientId(pb.getId());
//            phr.setStatus("1");//健康档案
//            patientHealthRecord = patientHealthRecordMapper.selectPatientHealthRecordList(phr);
//        }
//        //患者健康档案字典类型
//        SysDictData dictData = new SysDictData();
//        dictData.setDictType("health_record_type");
//        dictData.setStatus("0");
//        List<SysDictData> sysDictData = dictDataMapper.selectDictDataList(dictData);
//        result.add(new DiseasesResearchTree(researchPatients, patientHealthRecord, pb.getId(), sysDictData));

        return result;
    }

    @Override
    public List<PatientEnrollmentVo> selectEnrollmentPatientInfoList(PatientInfo patientInfo) {
        List<PatientEnrollmentVo> patientEnrollmentVos = patientInfoMapper.selectEnrollmentPatientInfoList(patientInfo);
        if (patientEnrollmentVos.size() > 0) {
            for (PatientEnrollmentVo p : patientEnrollmentVos) {
                if (p.getIdCard() != null && p.getIdCard().length() >= 18) {
                    p.setBirthday(DateUtils.dateTime(DateUtils.YYYY_MM_DD, CommunalUtils.getBirthday(p.getIdCard())));
                    p.setSex(CommunalUtils.getSex(p.getIdCard()));
                }
            }
        }
        return patientEnrollmentVos;
    }

    @Override
    public List<PatientEnrollmentVo> selectNoEnrollmentPatientInfoList(PatientInfo patientInfo) {
        List<PatientEnrollmentVo> patientEnrollmentVos = mpPersonMapper.selectMpPersonList2(patientInfo);
        if (patientEnrollmentVos.size() > 0) {
            for (PatientEnrollmentVo p : patientEnrollmentVos) {
                if (p.getIdCard() != null && p.getIdCard().length() >= 18) {
                    p.setSex(CommunalUtils.getSex(p.getIdCard()));
                    p.setBirthday(DateUtils.dateTime(DateUtils.YYYY_MM_DD, CommunalUtils.getBirthday(p.getIdCard())));
                }
            }
        }
        return patientEnrollmentVos;
    }

    @Override
    public List<PatientInfo> selectPatientInfoByPersonId(String personId) {
        return patientInfoMapper.selectPatientInfoByPersonId(personId);
    }


    @Override
    public AjaxResult confirmEnrollment(PatientInfo patientInfo) {
        PatientInfo info = new PatientInfo();
        info.setPersonId(patientInfo.getPersonId());
        info.setResearchId(patientInfo.getResearchId());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(info);
        if (patientInfos.size() == 0) {
            patientInfo.setUpdateTime(DateUtils.getNowDate());
            patientInfo.setEnrollmentTime(DateUtils.getNowDate());
            patientInfo.setEnrollmentResult("3");
            patientInfo.setEnrollmentHandlerStatus("3");
            patientInfo.setAllocatedHandlerStatus("3");
            patientInfo.setEnrollmentResultContent("确认入组");
            patientInfo.setAllocatedStatus("0");
            patientInfoMapper.updatePatientInfo(patientInfo);
            ResearchPatient researchPatient = new ResearchPatient();
            researchPatient.setResearchId(patientInfo.getResearchId());
            researchPatient.setPersonId(patientInfo.getPersonId());
            researchPatient.setPatientId(patientInfo.getPatientId());
            researchPatient.setCreateTime(DateUtils.getNowDate());
            researchPatientMapper.insertResearchPatient(researchPatient);
            return AjaxResult.success("入组成功");
        } else {
            return AjaxResult.success("患者已经入组该队列");
        }

    }

    @Transactional
    @Override
    public int importEnrollment(List<MpPerson> patientBaseInfo, String userName) {
        int num = 0;
        Set<Long> researchIds = new HashSet<>();
        if (patientBaseInfo != null && patientBaseInfo.size() > 0) {
            for (MpPerson baseInfo : patientBaseInfo) {
                researchIds.add(baseInfo.getResearchId());
                PatientInfo info = new PatientInfo();
                info.setPersonId(baseInfo.getPersonId());
                info.setResearchId(baseInfo.getResearchId());
                List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(info);
                if (patientInfos.size() == 0) {
                    PatientInfo patientInfo = new PatientInfo();
                    if (baseInfo.getAccountId() != null) {
                        patientInfo.setAccountId(Long.valueOf(baseInfo.getAccountId()));
                    }
                    patientInfo.setResearchId(baseInfo.getResearchId());
                    patientInfo.setPlanResearchId(baseInfo.getResearchId());
                    patientInfo.setPatientName(baseInfo.getPatientName());
                    patientInfo.setDeptId(baseInfo.getDeptId());
                    patientInfo.setPatientMobile(baseInfo.getMobile());
                    patientInfo.setPatientStatus("0");
                    patientInfo.setCreateTime(DateUtils.getNowDate());
                    patientInfo.setEnrollmentTime(DateUtils.getNowDate());
                    patientInfo.setEnrollmentStatus("0");
                    patientInfo.setChannelType("1");//渠道来源1后台2扫码
                    patientInfo.setCreateBy(userName);
                    patientInfo.setPersonId(baseInfo.getPersonId());
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
                    researchPatient.setResearchId(baseInfo.getResearchId());
                    researchPatient.setPersonId(baseInfo.getPersonId());
                    researchPatient.setPatientId(patientInfo.getPatientId());
                    researchPatient.setCreateTime(DateUtils.getNowDate());
                    researchPatient.setPersonId(baseInfo.getPersonId());
                    researchPatientMapper.insertResearchPatient(researchPatient);

                    //生成基线
                    Long followId = null;
                    List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(baseInfo.getResearchId());
                    PatientRecord patientRecord = new PatientRecord();
                    if (patientRecordTemplates.size() > 0) {
                        followId = patientRecordTemplates.get(0).getFollowId();
                        patientRecord.setLockVisitTime(patientInfo.getCreateTime());
                    }
                    patientRecord.setFollowId(followId);
                    patientRecord.setName("基线");
                    patientRecord.setCreateTime(DateUtils.getNowDate());
                    patientRecord.setResearchId(baseInfo.getResearchId());
                    patientRecord.setPatientId(patientInfo.getPatientId());
                    patientRecord.setVisitTime(DateUtils.getNowDate());
                    patientRecord.setFollowTipStatus(FollowTipStatusConstants.BASELINE_REMINDER);
                    patientRecord.setUpdateBy(userName);
                    patientRecordMapper.insertPatientRecord(patientRecord);
                    num++;
                }
            }
        }
        //此处为了适配唐都要求在队列未发布的情况下需要患者可以填写基线因此创建需要的表
        if(!CollectionUtils.isEmpty(researchIds)) {
            researchIds.forEach(researchId -> {researchInfoService.createTableInAddUser(researchId);});
        }


        return num;
    }


    @Transactional
    @Override
    public int deleteEnrollment(Long[] patientIds) {
        int num = 0;
        if (patientIds != null && patientIds.length > 0) {
            for (Long patientId : patientIds) {
                PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(patientId);
                if (Objects.isNull(patientInfo)){
                    throw new RuntimeException("患者:"+patientId+"不存在");
                }
                String enrollmentHandlerStatus = patientInfo.getEnrollmentHandlerStatus();
//                if (!enrollmentHandlerStatus.equals("1")){
//                    throw new RuntimeException("患者:"+patientInfo.getPatientName()+"已审核通过，不允许删除");
//                }
                Long researchId = patientInfo.getResearchId();
                String personId = patientInfo.getPersonId();
                ResearchPatient researchPatient = new ResearchPatient();
                researchPatient.setResearchId(researchId);
                researchPatient.setPersonId(personId);
                List<ResearchPatient> researchPatients = researchPatientMapper.selectResearchPatientList(researchPatient);
                Long[] idArray = researchPatients.stream().map(research -> research.getId()).toArray(Long[]::new);
                PatientRecord patientRecord = new PatientRecord();
                patientRecord.setRecordId(researchId);
                patientRecord.setPatientId(patientInfo.getPatientId());
                List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordList(patientRecord);
                Long[] recordIdArray = patientRecords.stream().map(research -> research.getRecordId()).toArray(Long[]::new);
//                if (idArray == null && idArray.length == 0) {
//                    throw new RuntimeException("患者队列数据有误");
//                }
                if (recordIdArray == null && recordIdArray.length == 0) {
                    throw new RuntimeException("患者基线记录有误");
                }
                logger.info("patientId:{},researchPatientId{},patientRecordId{}",patientId,idArray,recordIdArray);
                patientInfoMapper.deletePatientInfoByPatientId(patientId);
                researchPatientMapper.deleteResearchPatientByIds(idArray); //todo 重点观测
                patientRecordMapper.deletePatientRecordByRecordIds(recordIdArray);
                num++;
            }
        }
        return num;
    }


    @Override
    public List<ResearchPatientVO> selectResearchPatientList(Long researchId, String patientId, String patientName) {
        PatientInfo pi = new PatientInfo();
        pi.setResearchId(researchId);
        if (StringUtils.isNotBlankAndNotEmpty(patientId)) {
            pi.setPatientId(Long.valueOf(patientId));
        }
        if (StringUtils.isNotBlankAndNotEmpty(patientName)) {
            pi.setPatientName(patientName);
        }
        List<ResearchPatientVO> researchPatientVOS = patientInfoMapper.selectResearchPatientInfoList(pi);
        return researchPatientVOS;
    }

    @Override
    public boolean canDelete(Long userId, Long[] patientIds) {

        boolean isDelete = false;
        for (Long patientId : patientIds) {

            PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(patientId);

            ResearchPatient researchPatient = new ResearchPatient();
            researchPatient.setPatientId(patientId);
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfo.getResearchId());
            String createBy = researchInfo.getCreateBy();
            System.out.println("createBy = " + createBy);
            SysUser sysUser = sysUserMapper.selectUserById(userId);
            String userName = sysUser.getUserName();
            Long deptId = sysUser.getDeptId();
            Long deptId1 = researchInfo.getDeptId();
            if (
//                    deptId1.equals(deptId) &&
                    userName.equals(createBy)) {
                isDelete = true;
            }
            if ("admin".equals(userName)) {
                isDelete = true;
            }
        }
        return isDelete;
    }

    @Override
    public boolean canDeleteById(Long userId, Long id) {
        boolean isDelete = false;
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(id);
        String createBy = researchInfo.getCreateBy();
        System.out.println("createBy = " + createBy);
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        String userName = sysUser.getUserName();
        Long deptId = sysUser.getDeptId();
        Long deptId1 = researchInfo.getDeptId();
        if (
//                deptId1.equals(deptId) &&
                userName.equals(createBy)) {
            isDelete = true;
        }
        if ("admin".equals(userName)) {
            isDelete = true;
        }
        return isDelete;
    }
}
