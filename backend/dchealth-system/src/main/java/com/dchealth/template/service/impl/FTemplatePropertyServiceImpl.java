package com.dchealth.template.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.*;
import com.dchealth.template.mapper.*;
import com.dchealth.template.service.IFTemplatePropertyService;
import com.dchealth.warn.service.IWarnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 属性Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FTemplatePropertyServiceImpl implements IFTemplatePropertyService
{
    @Autowired
    private FTemplatePropertyMapper fTemplatePropertyMapper;
    @Autowired
    private FTemplateFormMapper fTemplateFormMapper;
    @Autowired
    private FLibraryPropertyMapper fLibraryPropertyMapper;
    @Autowired
    private IWarnService warnService;
    @Autowired
    private FTemplateExamSubjectMapper fTemplateExamSubjectMapper;
    @Autowired
    private FTemplateExamItemMapper fTemplateExamItemMapper;

    /**
     * 查询属性
     *
     * @param id 属性主键
     * @return 属性
     */
    @Override
    public FTemplateProperty selectFTemplatePropertyById(Long id)
    {
        return fTemplatePropertyMapper.selectFTemplatePropertyById(id);
    }

    /**
     * 查询属性列表
     *
     * @param fTemplateProperty 属性
     * @return 属性
     */
    @Override
    public List<FTemplateProperty> selectFTemplatePropertyList(FTemplateProperty fTemplateProperty)
    {
        return fTemplatePropertyMapper.selectFTemplatePropertyList(fTemplateProperty);
    }

    /**
     * 新增属性
     *
     * @param fTemplateProperty 属性
     * @return 结果
     */
    @Override
    public int insertFTemplateProperty(FTemplateProperty fTemplateProperty)
    {
        fTemplateProperty.setCreateTime(DateUtils.getNowDate());
        return fTemplatePropertyMapper.insertFTemplateProperty(fTemplateProperty);
    }

    @Override
    public int insertFTemplateProperty(FTemplateForm fTemplateForm) {
        return insertFLibraryProperty(fTemplateForm.getTemplateId(),fTemplateForm.getParentId() == null ? 0l : fTemplateForm.getParentId(),new ArrayList<FTemplateForm>(){{add(fTemplateForm);}});
    }

    private int insertFLibraryProperty(Long templateId,Long parentFormId, List<FTemplateForm> fTemplateForms){
        Date date = DateUtils.getNowDate();
        fTemplateForms.forEach(x->{
            if (x.getTemplateFormId() == null){
                x.setCreateTime(date);
                x.setTemplateId(templateId);
                x.setParentId(parentFormId);
                fTemplateFormMapper.insertFTemplateForm(x);
            }
            if ("2".equals(x.getFormType()) && x.getBaseFormId() != null){//拍照项组件
                FLibraryProperty params = new FLibraryProperty();
                params.setLibraryId(x.getBaseFormId());
                List<FLibraryProperty> fLibraryProperties = fLibraryPropertyMapper.selectFLibraryPropertyList(params);
                List<FTemplateProperty> list = null;
                if (fLibraryProperties != null && !fLibraryProperties.isEmpty()){
                    list = new ArrayList<>();
                    for (FLibraryProperty y : fLibraryProperties){
                        FTemplateProperty property = new FTemplateProperty();
                        BeanUtils.copyProperties(y,property);
                        list.add(property);
                    }
                    x.setTemplateProperties(list);
                }
            }
            if (x.getTemplateProperties() != null && !x.getTemplateProperties().isEmpty()){
                x.getTemplateProperties().forEach(y->{
                    if("3".equals(x.getFormType())){
                        //自评量表处理
                        FTemplateExamSubject subject = new FTemplateExamSubject();
                        BeanUtils.copyProperties(y, subject);
                        subject.setCreateTime(date);
                        subject.setTemplateFormId(x.getTemplateFormId());
                        subject.setTemplateId(x.getTemplateId());
                        fTemplateExamSubjectMapper.insertFTemplateExamSubject(subject);
                        Optional<List<FTemplateExamItem>> itemList = Optional.ofNullable(y.getItemList());
                        if(itemList.isPresent()){
                            itemList.get().forEach(item -> {
                                item.setPropertyId(subject.getId());
                                item.setTemplateFormId(x.getTemplateFormId());
                                item.setCreateTime(date);
                                fTemplateExamItemMapper.insertFTemplateExamItem(item);
                            });
                        }
                    } else {
                        y.setCreateTime(date);
                        y.setTemplateFormId(x.getTemplateFormId());
                        y.setTemplateId(templateId);
                        insertFTemplateProperty(y);
                        if (y.getWarnTemplateEarlyForms() != null && !y.getWarnTemplateEarlyForms().isEmpty()){
                            warnService.saveWarnTemplate(templateId, x.getTemplateFormId(), y.getId(), y.getWarnTemplateEarlyForms());
                        }
                    }
                });
            }
            /*if ("5".equals(x.getFormType())){
                warnService.saveWarnTemplate(templateId, x.getTemplateFormId(), x.getWarnTemplateEarlyForms());
            }*/
            if (x.getTemplateForms() != null && !x.getTemplateForms().isEmpty()){
                insertFLibraryProperty(templateId,x.getTemplateFormId(),x.getTemplateForms());
            }
        });
        return 1;
    }

    @Override
    public int insertFTemplateProperty(FTemplate fTemplate) {
        if (fTemplate.getTemplateForms() != null && !fTemplate.getTemplateForms().isEmpty()){
            insertFLibraryProperty(fTemplate.getTemplateId(),0l,fTemplate.getTemplateForms());
        }
        return 1;
    }

    /**
     * 修改属性
     *
     * @param fTemplateProperty 属性
     * @return 结果
     */
    @Override
    public int updateFTemplateProperty(FTemplateProperty fTemplateProperty)
    {
        fTemplateProperty.setUpdateTime(new Date());
        return fTemplatePropertyMapper.updateFTemplateProperty(fTemplateProperty);
    }

    /**
     * 模板表单数据删除
     * @param fTemplateForms
     * @return
     */
    private int deleteTemplateFormAndProperty(List<FTemplateForm> fTemplateForms){
        if (fTemplateForms != null && !fTemplateForms.isEmpty()){
            fTemplateForms.forEach(x->{
                FTemplateForm childParams = new FTemplateForm();
                childParams.setParentId(x.getTemplateFormId());
                List<FTemplateForm> childForms = fTemplateFormMapper.selectFTemplateFormList(childParams);
                if (childForms != null && !childForms.isEmpty()){
                    deleteTemplateFormAndProperty(childForms);
                }
                fTemplatePropertyMapper.deleteFTemplatePropertyByFormId(x.getTemplateFormId());
                fTemplateFormMapper.deleteFTemplateFormByTemplateFormId(x.getTemplateFormId());
                fTemplateExamSubjectMapper.deleteFTemplateExamSubjectByTemplateFormId(x.getTemplateFormId());
                fTemplateExamItemMapper.deleteFTemplateExamItemByTemplateFormId(x.getTemplateFormId());
            });
        }
        return 1;
    }

    @Override
    public int updateFTemplateProperty(FTemplateForm fTemplateForm) {
        FTemplateForm params = new FTemplateForm();
        Long templateFormId = fTemplateForm.getTemplateFormId();
        if ("3".equals(fTemplateForm.getFormType()) && fTemplateForm.getScoresTotalVal()!=null){
            fTemplateFormMapper.updateScoreByTemplateFormId(templateFormId,fTemplateForm.getScoresTotalVal());
        }
        params.setParentId(templateFormId);
        List<FTemplateForm> fTemplateForms = fTemplateFormMapper.selectFTemplateFormList(params);
        deleteTemplateFormAndProperty(fTemplateForms);
        fTemplatePropertyMapper.deleteFTemplatePropertyByFormId(templateFormId);
        fTemplateExamSubjectMapper.deleteFTemplateExamSubjectByTemplateFormId(templateFormId);
        fTemplateExamItemMapper.deleteFTemplateExamItemByTemplateFormId(templateFormId);
        insertFTemplateProperty(fTemplateForm);
        return 1;
    }

    @Override
    public int updateFTemplateProperty(FTemplate fTemplate) {
        if (fTemplate.getTemplateId() != null){
            fTemplatePropertyMapper.deleteFTemplatePropertyByTemplateId(fTemplate.getTemplateId());
            fTemplateFormMapper.deleteFTemplateFormByTemplateId(fTemplate.getTemplateId());
        }
        insertFTemplateProperty(fTemplate);
        return 1;
    }

    /**
     * 批量删除属性
     *
     * @param ids 需要删除的属性主键
     * @return 结果
     */
    @Override
    public int deleteFTemplatePropertyByIds(Long[] ids)
    {
        return fTemplatePropertyMapper.deleteFTemplatePropertyByIds(ids);
    }

    /**
     * 删除属性信息
     *
     * @param id 属性主键
     * @return 结果
     */
    @Override
    public int deleteFTemplatePropertyById(Long id)
    {

        return fTemplatePropertyMapper.deleteFTemplatePropertyById(id);
    }
}
