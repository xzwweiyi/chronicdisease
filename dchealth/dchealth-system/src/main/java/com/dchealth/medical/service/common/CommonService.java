package com.dchealth.medical.service.common;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.dchealth.common.utils.CommunalUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.vo.PatientDetailVO;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.mapper.ResearchInfoMapper;

/**
 * medical通用service
 */
public class CommonService {

    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    @Autowired
    private MpPersonMapper mpPersonMapper;

    /**
     * 详情包含其他信息（待完善）
     * @param patientDetailVO
     * @param function
     */
    public void patientDetail(PatientDetailVO patientDetailVO, Consumer<PatientDetailVO> function){
        function.accept(patientDetailVO);
    }

    /**
     * 患者基本信息
     * @param patientId
     * @param patientDetailVO
     * @param function
     */
    public void patientBaseInfo(Long patientId, PatientDetailVO patientDetailVO, Consumer<PatientDetailVO> function){
        MpPerson mpPerson = mpPersonMapper.selectMpPersonByPatientId(patientId);
        if(mpPerson!=null && mpPerson.getCardId()!=null){
            patientDetailVO.setPatientName(mpPerson.getPersonName());
            if(mpPerson.getCardId().length()==18){
                patientDetailVO.setBirthday(CommunalUtils.getBirthday(mpPerson.getCardId()));
                patientDetailVO.setSex(Integer.parseInt(CommunalUtils.getSex(mpPerson.getCardId())));
            }
        }
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientId);
        if(patientInfo!=null){
            patientDetailVO.setPatientCode(patientInfo.getPatientCode());
            patientDetailVO.setCreateTime(patientInfo.getCreateTime());
            patientDetailVO.setAdjustType(patientInfo.getAdjustType());
            patientDetailVO.setPlanResearchId(patientInfo.getPlanResearchId());
            if(patientInfo.getPlanResearchId()!=null || patientInfo.getResearchId()!=null){
                patientDetailVO.setResearchId(patientInfo.getResearchId());
                ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfo.getPlanResearchId()!=null?patientInfo.getPlanResearchId():patientInfo.getResearchId());
                if(researchInfo!=null){
                    patientDetailVO.setPlanResearchName(researchInfo.getProjectName());
                }
            }
        }
        function.accept(patientDetailVO);
    }
}
