package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FTemplateExamFormMapper;
import com.dchealth.template.domain.FTemplateExamForm;
import com.dchealth.template.service.IFTemplateExamFormService;

/**
 * 单模板-自评量Service业务层处理
 * 
 * @author lchbu
 * @date 2022-11-11
 */
@Service
public class FTemplateExamFormServiceImpl implements IFTemplateExamFormService 
{
    @Autowired
    private FTemplateExamFormMapper fTemplateExamFormMapper;

    /**
     * 查询单模板-自评量
     * 
     * @param templateFormId 单模板-自评量主键
     * @return 单模板-自评量
     */
    @Override
    public FTemplateExamForm selectFTemplateExamFormByTemplateFormId(Long templateFormId)
    {
        return fTemplateExamFormMapper.selectFTemplateExamFormByTemplateFormId(templateFormId);
    }

    /**
     * 查询单模板-自评量列表
     * 
     * @param fTemplateExamForm 单模板-自评量
     * @return 单模板-自评量
     */
    @Override
    public List<FTemplateExamForm> selectFTemplateExamFormList(FTemplateExamForm fTemplateExamForm)
    {
        return fTemplateExamFormMapper.selectFTemplateExamFormList(fTemplateExamForm);
    }

    /**
     * 新增单模板-自评量
     * 
     * @param fTemplateExamForm 单模板-自评量
     * @return 结果
     */
    @Override
    public int insertFTemplateExamForm(FTemplateExamForm fTemplateExamForm)
    {
        fTemplateExamForm.setCreateTime(DateUtils.getNowDate());
        return fTemplateExamFormMapper.insertFTemplateExamForm(fTemplateExamForm);
    }

    /**
     * 修改单模板-自评量
     * 
     * @param fTemplateExamForm 单模板-自评量
     * @return 结果
     */
    @Override
    public int updateFTemplateExamForm(FTemplateExamForm fTemplateExamForm)
    {
        fTemplateExamForm.setUpdateTime(DateUtils.getNowDate());
        return fTemplateExamFormMapper.updateFTemplateExamForm(fTemplateExamForm);
    }

    /**
     * 批量删除单模板-自评量
     * 
     * @param templateFormIds 需要删除的单模板-自评量主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateExamFormByTemplateFormIds(Long[] templateFormIds)
    {
        return fTemplateExamFormMapper.deleteFTemplateExamFormByTemplateFormIds(templateFormIds);
    }

    /**
     * 删除单模板-自评量信息
     * 
     * @param templateFormId 单模板-自评量主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateExamFormByTemplateFormId(Long templateFormId)
    {
        return fTemplateExamFormMapper.deleteFTemplateExamFormByTemplateFormId(templateFormId);
    }
}
