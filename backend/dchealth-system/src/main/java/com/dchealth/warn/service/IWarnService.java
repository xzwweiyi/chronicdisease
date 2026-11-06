package com.dchealth.warn.service;

import com.dchealth.warn.domain.form.WarnMedicalEarlyForm;
import com.dchealth.warn.domain.form.WarnTemplateEarlyForm;
import com.dchealth.warn.domain.vo.WarnMedicalEarlyVO;
import com.dchealth.warn.domain.vo.WarnTemplateEarlyVO;

import java.util.List;

/**
 * 预警业务层
 */
public interface IWarnService {

    /**
     * 查询随访预警
     * @param followId
     * @return
     */
    public List<WarnMedicalEarlyVO> selectWarnMedicalEarlys(Long followId);

    /**
     * 根据formId查询
     * @param formId
     * @return
     */
    public List<WarnMedicalEarlyVO> selectWarnMedicalEarlysByFormId(Long formId);

    /**
     * 查询随访预警
     * @param followId
     * @return
     */
    public List<WarnMedicalEarlyForm> selectWarnMedicalEarlysForm(Long followId);
    
    /**
     * 保存随访表单预警
     * @param followId 
     * @param formId
     * @param propertyId 
     * @param warnMedicalEarlyForms
     * @return
     */
    public int saveWarnMedical(Long followId, Long formId, Long propertyId, List<WarnMedicalEarlyForm> warnMedicalEarlyForms);

    /**
     * 导入模板预警
     * @param followId
     * @param formId
     * @param propertyId 
     * @param warnTemplateEarlyForms
     * @return
     */
    public int importWarnMedical(Long followId, Long formId, Long propertyId, List<WarnTemplateEarlyForm> warnTemplateEarlyForms);

    /**
     * 删除随访表单预警
     * @param warnMedicalId
     * @return
     */
    public int deleteWarnMedical(Long warnMedicalId);

    /**
     * 删除随访表单预警
     * @param followId
     * @return
     */
    public int deleteWarnMedicalFollowId(Long followId);

    public int deleteWarnMedicalByPropertyIds(List<Long> propertyIds);

    /**
     * 查询模板预警
     * @param templateId
     * @return
     */
    public List<WarnTemplateEarlyVO> selectWarnTemplateEarlys(Long templateId);

    /**
     * 查询模板预警
     * @param formId
     * @return
     */
    public List<WarnTemplateEarlyVO> selectWarnTemplateEarlysByFormId(Long formId);

    /**
     * 查询模板预警
     * @param templateId
     * @return
     */
    public List<WarnTemplateEarlyForm> selectWarnTemplateEarlysForm(Long templateId);
    
    /**
     * 保存模板预警
     * @param templateId 
     * @param formId 
     * @param propertyId 
     * @param warnTemplateEarlyForms
     * @return
     */
    public int saveWarnTemplate(Long templateId, Long formId, Long propertyId, List<WarnTemplateEarlyForm> warnTemplateEarlyForms);

    /**
     * 导入随访预警
     * @param templateId
     * @param formId
     * @param propertyId
     * @param warnMedicalEarlyForms
     * @return
     */
    public int importWarnTemplate(Long templateId, Long formId, Long propertyId, List<WarnMedicalEarlyForm> warnMedicalEarlyForms);

    /**
     * 删除模板预警
     * @param warnTemplateId
     * @return
     */
    public int deleteWarnTemplate(Long warnTemplateId);

}
