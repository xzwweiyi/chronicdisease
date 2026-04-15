package com.dchealth.medical.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.base.service.TencentService;
import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.common.annotation.DataScope;
import com.dchealth.common.constant.FollowTipStatusConstants;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysDictData;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.*;
import com.dchealth.medical.domain.*;
import com.dchealth.medical.domain.form.FollowWorkListCmdForm;
import com.dchealth.medical.domain.vo.*;
import com.dchealth.medical.mapper.*;
import com.dchealth.medical.service.IFollowWorkService;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.SysPopNotice;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.research.mapper.SysPopNoticeMapper;
import com.dchealth.research.service.IResearchInfoService;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.system.service.ISysDictDataService;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.user.domain.LeaveMessage;
import com.dchealth.user.mapper.UserLeaveMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 患者信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-21
 */
@Slf4j
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

    @Autowired
    private UserLeaveMessageMapper userLeaveMessageMapper;

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;


    @Autowired
    private IFollowWorkService followWorkService;

    @Autowired
    private TencentService tencentService;

    @Autowired
    private ISysDictDataService sysDictDataService;
    
    @Autowired
    private SysPopNoticeMapper sysPopNoticeMapper;

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
        if (patientInfo.getPatientStatus() != null && statusConditionList.contains(patientInfo.getPatientStatus())) {
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
        if (StringUtils.isNotEmpty(patientStatusQuery)) {
            List<String> inProgressStatus = new ArrayList<>();
            inProgressStatus.add("0");
            inProgressStatus.add("1");
            inProgressStatus.add("2");
            if ("2".equals(patientStatusQuery)) {
                patientInfos = patientInfos.stream().filter(info -> inProgressStatus.contains(info.getPatientStatus())).collect(Collectors.toList());
            } else if ("3".equals(patientStatusQuery)) {

            }
        }
        return patientInfos;
    }


    @Override
    public List<PatientInfo> selectPatientInfoListByAuth(PatientInfo requestPatientInfo) {

        Long userId = SecurityUtils.getUserId();
        PatientInfo patientInfoParam = new PatientInfo();
        BeanUtils.copyProperties(requestPatientInfo, patientInfoParam);
        List<Long> patientIds = null;
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfoParam.getResearchId());
        if (StringUtils.isNotBlankAndNotEmpty(patientInfoParam.getPatientStatus())) {
            if ("3".equals(patientInfoParam.getPatientStatus())) {
                patientIds = patientRecordMapper.selectCompletedPatientIdByResearchId(patientInfoParam.getResearchId());
                //如果当前课题状态为待发布，则将填完基线的完成状态患者归为随访状态
                if (researchInfo != null && "0".equals(researchInfo.getResearchStatus())) {
                    patientIds.clear();
                }
                patientInfoParam.setPatientStatus(null);
                patientInfoParam.setEnrollmentStatus("1");
                patientInfoParam.setPatientIds(patientIds);
            } else if ("1".equals(patientInfoParam.getPatientStatus())) {//此处1代表进行中，包含：未填写、基线、随访 状态；即去除已完成、已失访、已退出
                //此处去除3已完成，sql中同时去除4.已失访6.已退出，即剩下的都是进行中
                patientIds = patientRecordMapper.selectCompletedPatientIdByResearchId(patientInfoParam.getResearchId());
                patientInfoParam.setNotPatientIds(patientIds);
                patientInfoParam.setPatientStatus(null);
                //如果当前课题状态为待发布，则将填完基线的完成状态患者归为随访状态
                if (researchInfo != null && "0".equals(researchInfo.getResearchStatus())) {
                    patientInfoParam.setPatientIds(null);
                }
            }
        }

        PageUtils.startPage();
        patientInfoParam.setEnrollmentHandlerStatus("3");
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
                setSearchType(requestPatientInfo.getPatientStatus(), info);

                // 添加留言功能 设置未读标识
                List<LeaveMessage> leaveMessages = userLeaveMessageMapper.selectUpdateReadStatus(info.getPersonId(), info.getPersonId());
                if (leaveMessages != null) {
                    int size = leaveMessages.size();
                    info.setMessageNums(size);
                }
            });
        }

        return patientInfos;
    }

    private static void setSearchType(String requestPatientStatus, PatientInfo patientInfo) {
        if (StringUtils.isBlank(requestPatientStatus)) {
            requestPatientStatus = patientInfo.getPatientStatus();
        }
        switch (requestPatientStatus) {
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
            default:
                break;
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
    public List<PatientEnrollmentVo> selectExcelEnrollmentPatientInfoList(PatientInfo patientInfo) {
        List<PatientEnrollmentVo> patientEnrollmentVos = patientInfoMapper.selectExcelEnrollmentPatientInfoList(patientInfo);
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
//                info.setPersonId(baseInfo.getPersonId());
                info.setResearchId(baseInfo.getResearchId());

                List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(info);
                Map<String, PatientInfo> infoMap = patientInfos.stream().collect(Collectors.toMap(PatientInfo::getPersonId, Function.identity(), (k1, k2) -> k2));


                if (infoMap.get(baseInfo.getPersonId()) == null) {
                    PatientInfo patientInfo = new PatientInfo();
                    if (baseInfo.getAccountId() != null) {
                        patientInfo.setAccountId(Long.valueOf(baseInfo.getAccountId()));
                    }

                    // 对肥胖门诊的队列进行判断 不同的手术、生活方式
                    SysDictData sysDictData = new SysDictData();
                    sysDictData.setDictType("patient_code");
                    List<SysDictData> dictDataList = sysDictDataService.selectDictDataList(sysDictData);
                    if (ObjectUtil.isNotEmpty(dictDataList)) {
                        Map<String, String> map = dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
                        map.forEach((k, v) -> {
                            if (baseInfo.getResearchId().toString().equals(v) && k.equals("肥胖门诊用药队列")) {
                                // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
                                if (patientInfos == null || patientInfos.isEmpty()) {
                                    patientInfo.setPatientCode(String.format("D-%04d", 1));
                                } else {
                                    PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
                                    patientInfo.setPatientCode(generateNextCode("D", patientInfo1.getPatientCode()));
                                }

                            } else if (baseInfo.getResearchId().toString().equals(v) && k.equals("肥胖门诊生活方式队列")) {
                                // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
                                if (patientInfos == null || patientInfos.isEmpty()) {
                                    patientInfo.setPatientCode(String.format("L-%04d", 1));
                                } else {
                                    PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
                                    patientInfo.setPatientCode(generateNextCode("L", patientInfo1.getPatientCode()));
                                }
                            } else if (baseInfo.getResearchId().toString().equals(v) && k.equals("肥胖门诊手术队列")) {
                                // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
                                if (patientInfos == null || patientInfos.isEmpty()) {
                                    patientInfo.setPatientCode(String.format("S-%04d", 1));
                                } else {
                                    PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
                                    patientInfo.setPatientCode(generateNextCode("S", patientInfo1.getPatientCode()));
                                }
                            }
                        });
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
        if (!CollectionUtils.isEmpty(researchIds)) {
            researchIds.forEach(researchId -> {
                researchInfoService.createTableInAddUser(researchId);
            });
        }


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


    @Transactional
    @Override
    public int deleteEnrollment(Long[] patientIds) {
        int num = 0;
        if (patientIds != null && patientIds.length > 0) {
            for (Long patientId : patientIds) {
                PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(patientId);
                if (Objects.isNull(patientInfo)) {
                    throw new RuntimeException("患者:" + patientId + "不存在");
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
                logger.info("patientId:{},researchPatientId{},patientRecordId{}", patientId, idArray, recordIdArray);
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

    @Transactional
    @Override
    public int moveEnrollment(MoveEnrollmentVo moveEnrollmentVo) {
        // 通过电话号码查询患者信息
        MpPerson mpPerson = mpPersonMapper.selectMpPersonByCardId(moveEnrollmentVo.getCardId(), moveEnrollmentVo.getMobile());

        int num = 0;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Set<Long> researchIds = new HashSet<>();
        if (mpPerson != null) {
            researchIds.add(moveEnrollmentVo.getResearchId());
            PatientInfo info = new PatientInfo();
//                info.setPersonId(baseInfo.getPersonId());
            info.setResearchId(moveEnrollmentVo.getResearchId());

            List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(info);
            Map<String, PatientInfo> infoMap = patientInfos.stream().collect(Collectors.toMap(PatientInfo::getPersonId, Function.identity()));


            if (infoMap.get(mpPerson.getPersonId()) == null) {
                PatientInfo patientInfo = new PatientInfo();
                if (moveEnrollmentVo.getAccountId() != null) {
                    patientInfo.setAccountId(Long.valueOf(moveEnrollmentVo.getAccountId()));
                }

                // 对肥胖门诊的队列进行判断 不同的手术、生活方式
                SysDictData sysDictData = new SysDictData();
                sysDictData.setDictType("patient_code");
                List<SysDictData> dictDataList = sysDictDataService.selectDictDataList(sysDictData);
                if (ObjectUtil.isNotEmpty(dictDataList)) {
                    Map<String, String> map = dictDataList.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
                    map.forEach((k, v) -> {
                        if (moveEnrollmentVo.getResearchId().toString().equals(v) && k.equals("肥胖门诊用药队列")) {
                            // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
                            if (patientInfos == null || patientInfos.isEmpty()) {
                                patientInfo.setPatientCode(String.format("D-%04d", 1));
                            } else {
                                PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
                                patientInfo.setPatientCode(generateNextCode("D", patientInfo1.getPatientCode()));
                            }

                        } else if (moveEnrollmentVo.getResearchId().toString().equals(v) && k.equals("肥胖门诊生活方式队列")) {
                            // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
                            if (patientInfos == null || patientInfos.isEmpty()) {
                                patientInfo.setPatientCode(String.format("L-%04d", 1));
                            } else {
                                PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
                                patientInfo.setPatientCode(generateNextCode("L", patientInfo1.getPatientCode()));
                            }
                        } else if (moveEnrollmentVo.getResearchId().toString().equals(v) && k.equals("肥胖门诊手术队列")) {
                            // 该队列中是否有患者编码为空的如果有 将患者进行编码， 最新的患者编码为最大编码 + 1
                            if (patientInfos == null || patientInfos.isEmpty()) {
                                patientInfo.setPatientCode(String.format("S-%04d", 1));
                            } else {
                                PatientInfo patientInfo1 = getFirstPatientCode(patientInfos);
                                patientInfo.setPatientCode(generateNextCode("S", patientInfo1.getPatientCode()));
                            }
                        }
                    });
                }

                patientInfo.setResearchId(moveEnrollmentVo.getResearchId());
                patientInfo.setPlanResearchId(moveEnrollmentVo.getResearchId());
                patientInfo.setPatientName(mpPerson.getPersonName());
                patientInfo.setDeptId(Long.valueOf(moveEnrollmentVo.getDeptId()));
                patientInfo.setPatientMobile(moveEnrollmentVo.getMobile());
                patientInfo.setPatientStatus("0");
                patientInfo.setCreateTime(DateUtils.getNowDate());
                patientInfo.setEnrollmentTime(DateUtils.getNowDate());
                patientInfo.setEnrollmentStatus("0");
                patientInfo.setChannelType("3");//渠道来源1后台2扫码  3为预问诊自动导入
                patientInfo.setCreateBy(loginUser.getUsername());
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
                researchPatient.setResearchId(moveEnrollmentVo.getResearchId());
                researchPatient.setPersonId(mpPerson.getPersonId());
                researchPatient.setPatientId(patientInfo.getPatientId());
                researchPatient.setCreateTime(DateUtils.getNowDate());
                researchPatient.setPersonId(mpPerson.getPersonId());
                researchPatientMapper.insertResearchPatient(researchPatient);

                //生成基线
                Long followId = null;
                List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(moveEnrollmentVo.getResearchId());
                PatientRecord patientRecord = new PatientRecord();
                if (!patientRecordTemplates.isEmpty()) {
                    followId = patientRecordTemplates.get(0).getFollowId();
                    patientRecord.setLockVisitTime(patientInfo.getCreateTime());
                }
                patientRecord.setFollowId(followId);
                patientRecord.setName("基线");
                patientRecord.setCreateTime(DateUtils.getNowDate());
                patientRecord.setResearchId(moveEnrollmentVo.getResearchId());
                patientRecord.setPatientId(patientInfo.getPatientId());
                patientRecord.setVisitTime(DateUtils.getNowDate());
                patientRecord.setFollowTipStatus(FollowTipStatusConstants.BASELINE_REMINDER);
                patientRecord.setUpdateBy(loginUser.getUsername());
                patientRecordMapper.insertPatientRecord(patientRecord);
                num++;

                // 默认入组后，自动进行确认操作
                moveEnrollmentVo.setHandleType("3");
                moveEnrollmentVo.setResearchId(moveEnrollmentVo.getResearchId());
                enrollmentHandle(moveEnrollmentVo, patientInfo);
                log.info("患者pc端入组成功");

                patientConfirmEnrollment(moveEnrollmentVo, patientInfo);
            }


        }

        //此处为了适配唐都要求在队列未发布的情况下需要患者可以填写基线因此创建需要的表
        if (!CollectionUtils.isEmpty(researchIds)) {
            researchIds.forEach(researchId -> {
                researchInfoService.createTableInAddUser(researchId);
            });
        }

        return num;

    }

    /**
     * 患者确认入组
     * @param moveEnrollmentVo
     * @param patientInfo
     */
    private void patientConfirmEnrollment(MoveEnrollmentVo moveEnrollmentVo, PatientInfo patientInfo) {
        //患者确认入组
        PatientInfo pio = patientInfoMapper.selectPatientInfoByPatientId(patientInfo.getPatientId());
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
        query.setResearchId(moveEnrollmentVo.getResearchId());

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

        PatientInfo info1 = new PatientInfo();
        info1.setPatientId(patientInfo.getPatientId());
        info1.setEnrollmentResult("3");
        //确认完成入组
        info1.setEnrollmentStatus("1");
        info1.setEnrollmentHandlerStatus("3");
        info1.setEnrollmentTime(new Date());
        info1.setResearchId(moveEnrollmentVo.getResearchId());
        info1.setEnrollmentResultContent("确认入组");
        //清理随访
        clearFollowRecord(info1);
//            generateBaseRecord(patientInfo);
        patientInfoMapper.updatePatientInfo(info1);

        PatientRecord prd = patientRecordMapper.selectOldByPatientId(patientInfo.getPatientId());

        String msgContent = "您已加入随访队列，可查看队列详情";
        prd.setAccountId(pio.getAccountId());
        AccountNotice accountNotice = new AccountNotice(prd,msgContent,"11");
        accountNoticeMapper.insertAccountNotice(accountNotice);
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
    

    @Override
    public List<EnrollmentListVo> enrollmentList(MoveEnrollmentVo moveEnrollmentVo) {

        ResearchInfo researchInfo = new ResearchInfo();
        researchInfo.setDeptId(Long.valueOf(moveEnrollmentVo.getDeptId()));
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(researchInfo).stream().filter(p -> !Objects.equals(p.getId(), moveEnrollmentVo.getResearchId())).collect(Collectors.toList());

        if (ObjectUtil.isEmpty(researchInfos)) {
            return new ArrayList<>();
        }
        List<EnrollmentListVo> results = new ArrayList<>();
        researchInfos.forEach(item -> {
            EnrollmentListVo vo = new EnrollmentListVo();
            vo.setResearchId(item.getId());
            vo.setProjectName(item.getProjectName());
            results.add(vo);
        });
        return results;
    }


    public int enrollmentHandle(MoveEnrollmentVo moveEnrollmentVo, PatientInfo patientInfo) {

        PatientInfo patientInfo1 = patientInfoMapper.selectPatientInfoByPatientId(patientInfo.getPatientId());
        if (!Objects.isNull(patientInfo1) && "3".equals(moveEnrollmentVo.getHandleType())
                && !Objects.isNull(patientInfo1.getRefuseCount()) && patientInfo1.getRefuseCount() > 2
                && "1".equals(patientInfo1.getChannelType())) {
            log.error("当前科室当前病种当前队列拒绝入组的次数上限2次，超过2次的患者可以纳排但是不能确认/修改入组，可以取消入组，只能扫码入组");
            return 1;
        }
//        PatientInfo patient = new PatientInfo();
//        patient.setPatientId(patientInfo.getPatientId());
//        patient.setEnrollmentResult(autoEnrollmentHandleVo.getHandleType());
//        patient.setEnrollmentHandlerStatus(autoEnrollmentHandleVo.getHandleType());
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(moveEnrollmentVo.getResearchId());
        //3确认入组
        if ("3".equals(moveEnrollmentVo.getHandleType())) {
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
                patientInfo.setResearchId(moveEnrollmentVo.getResearchId());
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
                followWorkService.clearFollowRecord(patientInfo);
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
                prd.setAccountId(patientInfo.getAccountId());
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
                followWorkService.createPopNotice(researchInfo, String.valueOf(patientInfo.getPatientId()));
            }
        }

        PatientRecord patientRecord = patientRecordMapper.selectOldByPatientId(patientInfo.getPatientId());
        if (patientRecord != null) {
            patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(patientInfo.getAccountId(), patientRecord.getRecordId(), "1", DateUtils.getNowDate()));
        }
        return 1;
    }


}
