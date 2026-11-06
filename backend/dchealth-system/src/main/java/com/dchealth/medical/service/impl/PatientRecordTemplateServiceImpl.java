package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.service.IFMedicalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.service.IPatientRecordTemplateService;

/**
 * 周期配置Service业务层处理
 * 
 * @author lchbu
 * @date 2022-08-23
 */
@Service
public class PatientRecordTemplateServiceImpl implements IPatientRecordTemplateService 
{
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;

    /**
     * 查询周期配置
     * 
     * @param followId 周期配置主键
     * @return 周期配置
     */
    @Override
    public PatientRecordTemplate selectPatientRecordTemplateByFollowId(Long followId)
    {
        return patientRecordTemplateMapper.selectPatientRecordTemplateByFollowId(followId);
    }

    /**
     * 查询周期配置列表
     * 
     * @param patientRecordTemplate 周期配置
     * @return 周期配置
     */
    @Override
    public List<PatientRecordTemplate> selectPatientRecordTemplateList(PatientRecordTemplate patientRecordTemplate)
    {
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(patientRecordTemplate);
        if(patientRecordTemplates != null && !patientRecordTemplates.isEmpty()){
            patientRecordTemplates.forEach(prt -> {
                //查询随访周期对应的模板信息
                List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(prt.getFollowId());
                prt.setFmedicalForms(fMedicalForms);
            });
        }
        return patientRecordTemplates;
    }

    /**
     * 新增周期配置
     * 
     * @param patientRecordTemplate 周期配置
     * @return 结果
     */
    @Override
    public int insertPatientRecordTemplate(PatientRecordTemplate patientRecordTemplate)
    {
        patientRecordTemplate.setCreateTime(DateUtils.getNowDate());
        //查询周期排序最大值
        Integer maxFollowNum = patientRecordTemplateMapper.selectMaxFollowNumByResearchId(patientRecordTemplate.getResearchId());
        patientRecordTemplate.setFollowNum(maxFollowNum != null ? (maxFollowNum + 1) : 1);
        return patientRecordTemplateMapper.insertPatientRecordTemplate(patientRecordTemplate);
    }

    /**
     * 修改周期配置
     * 
     * @param patientRecordTemplate 周期配置
     * @return 结果
     */
    @Override
    public int updatePatientRecordTemplate(PatientRecordTemplate patientRecordTemplate)
    {
        patientRecordTemplate.setUpdateTime(DateUtils.getNowDate());
        return patientRecordTemplateMapper.updatePatientRecordTemplate(patientRecordTemplate);
    }

    /**
     * 批量删除周期配置
     * 
     * @param followIds 需要删除的周期配置主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordTemplateByFollowIds(Long[] followIds)
    {
        return patientRecordTemplateMapper.deletePatientRecordTemplateByFollowIds(followIds);
    }

    /**
     * 删除周期配置信息
     * 
     * @param followId 周期配置主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordTemplateByFollowId(Long followId)
    {
        return patientRecordTemplateMapper.deletePatientRecordTemplateByFollowId(followId);
    }
}
