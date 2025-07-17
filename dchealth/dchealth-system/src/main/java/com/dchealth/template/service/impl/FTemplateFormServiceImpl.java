package com.dchealth.template.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.*;
import com.dchealth.template.mapper.*;
import com.dchealth.template.service.IFTemplateFormService;
import com.dchealth.warn.domain.WarnTemplateEarly;
import com.dchealth.warn.domain.WarnTemplateEarlyCondition;
import com.dchealth.warn.domain.vo.WarnTemplateEarlyConditionVO;
import com.dchealth.warn.domain.vo.WarnTemplateEarlyVO;
import com.dchealth.warn.mapper.WarnTemplateEarlyConditionMapper;
import com.dchealth.warn.mapper.WarnTemplateEarlyMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 单模板Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FTemplateFormServiceImpl implements IFTemplateFormService
{
    @Autowired
    private FTemplateFormMapper fTemplateFormMapper;
    @Autowired
    private FTemplatePropertyMapper fTemplatePropertyMapper;
    @Autowired
    private FTemplateExamSubjectMapper fTemplateExamSubjectMapper;
    @Autowired
    private FTemplateExamItemMapper fTemplateExamItemMapper;
    @Autowired
    WarnTemplateEarlyMapper warnTemplateEarlyMapper;
    @Autowired
    WarnTemplateEarlyConditionMapper warnTemplateEarlyConditionMapper;

    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    /**
     * 查询单模板
     *
     * @param templateFormId 单模板主键
     * @return 单模板
     */
    @Override
    public FTemplateForm selectFTemplateFormByTemplateFormId(Long templateFormId)
    {
        return fTemplateFormMapper.selectFTemplateFormByTemplateFormId(templateFormId);
    }

    @Override
    public FTemplateForm detail(Long templateFormId) {
        FTemplateForm params = new FTemplateForm();
        params.setParentId(templateFormId);
        List<FTemplateForm> fTemplateForms = fTemplateFormMapper.selectFTemplateFormList(params);
        List<FTemplateForm> childrens = findChildren(fTemplateForms);
        FTemplateForm fTemplateForm = fTemplateFormMapper.selectFTemplateFormByTemplateFormId(templateFormId);
        fTemplateForm.setTemplateForms(childrens);

        if("3".equals(fTemplateForm.getFormType())){
            //自评量表处理
            packageFTemplateProperty(fTemplateForm);
        } else {
            //组件属性
            List<FTemplateProperty> fTemplateProperties = fTemplatePropertyMapper.selectFTemplatePropertyByTemFromId(templateFormId);
            if (fTemplateProperties != null){
                //查询设置的预警信息
                for(FTemplateProperty fp : fTemplateProperties){
                    WarnTemplateEarly warnTemplateEarly = new WarnTemplateEarly();
                    warnTemplateEarly.setPropertyId(fp.getId());
                    warnTemplateEarly.setFormId(fp.getTemplateFormId());
                    List<WarnTemplateEarly> earlyList = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(warnTemplateEarly);
                    List<WarnTemplateEarlyVO> earlyForms = new ArrayList<>();
                    for(WarnTemplateEarly early : earlyList){
                        WarnTemplateEarlyVO earlyForm = new WarnTemplateEarlyVO();
                        BeanUtils.copyProperties(early, earlyForm);

                        List<WarnTemplateEarlyConditionVO> earlyConditionForms = new ArrayList<>();
                        WarnTemplateEarlyCondition earlyCondition = new WarnTemplateEarlyCondition();
                        earlyCondition.setTemplateEarlyWarnId(earlyForm.getId());
                        earlyCondition.setTemplateId(earlyForm.getTemplateId());
                        List<WarnTemplateEarlyCondition> earlyConditions = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(earlyCondition);
                        for(WarnTemplateEarlyCondition earlyCondition1 :earlyConditions){
                            WarnTemplateEarlyConditionVO earlyConditionForm = new WarnTemplateEarlyConditionVO();
                            BeanUtils.copyProperties(earlyCondition1, earlyConditionForm);
                            earlyConditionForms.add(earlyConditionForm);
                        }
                        earlyForm.setWarnTemplateEarlyConditionVOS(earlyConditionForms);

                        earlyForms.add(earlyForm);
                    }
                    fp.setWarnTemplateEarlyVOS(earlyForms);
                }

                fTemplateForm.setTemplateProperties(fTemplateProperties);
            }
        }
        return fTemplateForm;
    }

    private List<FTemplateForm> findChildren(List<FTemplateForm> fTemplateForms){
        List<FTemplateForm> list = new ArrayList<>();
        if (fTemplateForms != null && !fTemplateForms.isEmpty()){
            fTemplateForms.forEach(x->{
                FTemplateForm params = new FTemplateForm();
                params.setParentId(x.getTemplateFormId());
                List<FTemplateForm> childrens = fTemplateFormMapper.selectFTemplateFormList(params);
                if (childrens != null && !childrens.isEmpty()){
                    x.setTemplateForms(findChildren(childrens));
                }
                if ("1".equals(x.getFormType()) || "2".equals(x.getFormType())){
                    List<FTemplateProperty> fTemplateProperties = fTemplatePropertyMapper.selectFTemplatePropertyByTemFromId(x.getTemplateFormId());
                    if (fTemplateProperties != null){
                        //查询设置的预警信息
                        for(FTemplateProperty fp : fTemplateProperties){
                            WarnTemplateEarly warnTemplateEarly = new WarnTemplateEarly();
                            warnTemplateEarly.setPropertyId(fp.getId());
                            warnTemplateEarly.setFormId(fp.getTemplateFormId());
                            List<WarnTemplateEarly> earlyList = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(warnTemplateEarly);
                            List<WarnTemplateEarlyVO> earlyForms = new ArrayList<>();
                            for(WarnTemplateEarly early : earlyList){
                                WarnTemplateEarlyVO earlyForm = new WarnTemplateEarlyVO();
                                BeanUtils.copyProperties(early, earlyForm);

                                List<WarnTemplateEarlyConditionVO> earlyConditionForms = new ArrayList<>();
                                WarnTemplateEarlyCondition earlyCondition = new WarnTemplateEarlyCondition();
                                earlyCondition.setTemplateEarlyWarnId(earlyForm.getId());
                                earlyCondition.setTemplateId(earlyForm.getTemplateId());
                                List<WarnTemplateEarlyCondition> earlyConditions = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(earlyCondition);
                                for(WarnTemplateEarlyCondition earlyCondition1 :earlyConditions){
                                    WarnTemplateEarlyConditionVO earlyConditionForm = new WarnTemplateEarlyConditionVO();
                                    BeanUtils.copyProperties(earlyCondition1, earlyConditionForm);
                                    earlyConditionForms.add(earlyConditionForm);
                                }
                                earlyForm.setWarnTemplateEarlyConditionVOS(earlyConditionForms);

                                earlyForms.add(earlyForm);
                            }
                            fp.setWarnTemplateEarlyVOS(earlyForms);
                        }

                        x.setTemplateProperties(fTemplateProperties);
                    }
                } else if("3".equals(x.getFormType())) {
                    packageFTemplateProperty(x);
                } else if ("0".equals(x.getFormType()) || "4".equals(x.getFormType())) {

                    List<FTemplateProperty> fTemplateProperties = fTemplatePropertyMapper.selectFTemplatePropertyByTemFromId(x.getTemplateFormId());
                    if (fTemplateProperties != null){
                        //查询设置的预警信息
                        for(FTemplateProperty fp : fTemplateProperties){
                            WarnTemplateEarly warnTemplateEarly = new WarnTemplateEarly();
                            warnTemplateEarly.setPropertyId(fp.getId());
                            warnTemplateEarly.setFormId(fp.getTemplateFormId());
                            List<WarnTemplateEarly> earlyList = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(warnTemplateEarly);
                            List<WarnTemplateEarlyVO> earlyForms = new ArrayList<>();
                            for(WarnTemplateEarly early : earlyList){
                                WarnTemplateEarlyVO earlyForm = new WarnTemplateEarlyVO();
                                BeanUtils.copyProperties(early, earlyForm);

                                List<WarnTemplateEarlyConditionVO> earlyConditionForms = new ArrayList<>();
                                WarnTemplateEarlyCondition earlyCondition = new WarnTemplateEarlyCondition();
                                earlyCondition.setTemplateEarlyWarnId(earlyForm.getId());
                                earlyCondition.setTemplateId(earlyForm.getTemplateId());
                                List<WarnTemplateEarlyCondition> earlyConditions = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(earlyCondition);
                                for(WarnTemplateEarlyCondition earlyCondition1 :earlyConditions){
                                    WarnTemplateEarlyConditionVO earlyConditionForm = new WarnTemplateEarlyConditionVO();
                                    BeanUtils.copyProperties(earlyCondition1, earlyConditionForm);
                                    earlyConditionForms.add(earlyConditionForm);
                                }
                                earlyForm.setWarnTemplateEarlyConditionVOS(earlyConditionForms);

                                earlyForms.add(earlyForm);
                            }
                            fp.setWarnTemplateEarlyVOS(earlyForms);
                        }

                        x.setTemplateProperties(fTemplateProperties);
                    }
                }
                list.add(x);
            });
        }
        return list;
    }

    /**
     * 封装自评量表题干、选项信息
     * @param fTemplateForm
     */
    public void packageFTemplateProperty(FTemplateForm fTemplateForm){
        List<FTemplateExamSubject> subjectList = fTemplateExamSubjectMapper.selectFTemplateExamSubjectByTemFromId(fTemplateForm.getTemplateFormId());
        if(subjectList != null && !subjectList.isEmpty()){
            List<FTemplateProperty> properties = new ArrayList<>();
            for (FTemplateExamSubject subject : subjectList) {
                FTemplateProperty property = new FTemplateProperty();
                BeanUtils.copyProperties(subject, property);
                List<FTemplateExamItem> itemList = fTemplateExamItemMapper.selectExamItemByPropertyId(subject.getId());
                property.setItemList(itemList);
                properties.add(property);
            }
            fTemplateForm.setTemplateProperties(properties);
        }
    }


    /**
     * 查询单模板列表
     *
     * @param fTemplateForm 单模板
     * @return 单模板
     */
    @Override
    public List<FTemplateForm> selectFTemplateFormList(FTemplateForm fTemplateForm){
        List<FTemplateForm> list = fTemplateFormMapper.selectFTemplateFormList(fTemplateForm);
        FTemplateProperty fTemplateProperty = new FTemplateProperty();
        fTemplateProperty.setTemplateId(fTemplateForm.getTemplateId());
        List<FTemplateProperty> fTemplatePropertyList = fTemplatePropertyMapper.selectPropertyNumsList(fTemplateProperty);
        if(fTemplatePropertyList.size()>0 && list.size()>0){
            Map<Long,List<FTemplateProperty>> propertyMaps = fTemplatePropertyList.stream().collect(Collectors.groupingBy(FTemplateProperty::getParentId));
            for(FTemplateForm f:list){
                if(propertyMaps.containsKey(f.getTemplateFormId())){
                    f.setPropertyNum(f.getPropertyNum()+propertyMaps.get(f.getTemplateFormId()).size()+f.getEdcNum());
                }else{
                    f.setPropertyNum(f.getPropertyNum()+f.getEdcNum());
                }
            }
        }else if(fTemplatePropertyList.size()==0 && list.size()>0){
            for(FTemplateForm f:list){
                f.setPropertyNum(f.getPropertyNum()+f.getEdcNum());
            }
        }
        if(list.size()>0){
            for(FTemplateForm f:list){
                if(f.getFormType().equals("3")){
                    //自评
                    FTemplateExamSubject fTemplateExamSubject = new FTemplateExamSubject();
                    fTemplateExamSubject.setTemplateFormId(f.getTemplateFormId());
                    List<FTemplateExamSubject> subjects = fTemplateExamSubjectMapper.selectFTemplateExamSubjectList(fTemplateExamSubject);
                    f.setPropertyNum(subjects.size());

                    FTemplateForm fTemplateForm1 = new FTemplateForm();
                    fTemplateForm1.setParentId(f.getTemplateFormId());
                    List<FTemplateForm> chrenForms = fTemplateFormMapper.selectFTemplateFormList(fTemplateForm1);
                    for (FTemplateForm templateForm : chrenForms) {
                        FTemplateExamSubject fTemplateExamSubject2 = new FTemplateExamSubject();
                        fTemplateExamSubject2.setTemplateFormId(templateForm.getTemplateFormId());
                        List<FTemplateExamSubject> subjects2 = fTemplateExamSubjectMapper.selectFTemplateExamSubjectList(fTemplateExamSubject2);
                        f.setPropertyNum(f.getPropertyNum()+subjects2.size());
                    }
                }
            }

        }
        return list;
    }

    /**
     * 新增单模板
     *
     * @param fTemplateForm 单模板
     * @return 结果
     */
    @Override
    public int insertFTemplateForm(FTemplateForm fTemplateForm)
    {
        checkHealthType(fTemplateForm);
        fTemplateForm.setCreateTime(DateUtils.getNowDate());
        return fTemplateFormMapper.insertFTemplateForm(fTemplateForm);
    }

    /**
     * 修改单模板
     *
     * @param fTemplateForm 单模板
     * @return 结果
     */
    @Override
    public int updateFTemplateForm(FTemplateForm fTemplateForm)
    {
        fTemplateForm.setUpdateTime(DateUtils.getNowDate());
        checkHealthType(fTemplateForm);
        List<FTemplateProperty> fTemplateProperties = fTemplateForm.getTemplateProperties();
        if(fTemplateProperties != null && !fTemplateProperties.isEmpty()){
            for (FTemplateProperty fTemplateProperty : fTemplateProperties) {
                //更新表单属性信息
                fTemplatePropertyMapper.updateFTemplateProperty(fTemplateProperty);
            }
        }
        return fTemplateFormMapper.updateFTemplateForm(fTemplateForm);
    }

    /**
     * 批量删除单模板
     *
     * @param templateFormIds 需要删除的单模板主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateFormByTemplateFormIds(Long[] templateFormIds)
    {
        return fTemplateFormMapper.deleteFTemplateFormByTemplateFormIds(templateFormIds);
    }

    /**
     * 删除单模板信息
     *
     * @param templateFormId 单模板主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateFormByTemplateFormId(Long templateFormId)
    {
        return fTemplateFormMapper.deleteFTemplateFormByTemplateFormId(templateFormId);
    }

    /**
     * 根据模板id查询表单相关信息
     * @param templateId
     * @return
     */
    @Override
    public FTemplateForm queryTemplateFromByTemId(Long templateId) {
        FTemplateForm fTemplateForm = fTemplateFormMapper.queryTemplateFromByTemId(templateId);
        if(fTemplateForm != null){
            //查询表单属性信息根据表单id
            List<FTemplateProperty> fTemplateProperties = fTemplatePropertyMapper.selectFTemplatePropertyByTemFromId(fTemplateForm.getTemplateFormId());
            fTemplateForm.setTemplateProperties(fTemplateProperties);
        }
        return fTemplateForm;

    }

    private void checkHealthType(FTemplateForm fTemplateForm) {
        // 对表单类型为5的健康管理方案类的进行限制。不可添加重名的，不可同时添加超过4个类型方案。考虑到小程序端的展示
        if ("5".equals(fTemplateForm.getFormType())) {
            List<FTemplateForm> fTemplateForms = fTemplateFormMapper.selectFTemplateFormByTemplateId(fTemplateForm.getTemplateId());

            if (fTemplateForms != null && !fTemplateForms.isEmpty()) {
                List<FTemplateForm> list = fTemplateForms.stream().filter(x -> x.getHealthManageType() != 0).collect(Collectors.toList());
                list.forEach(x -> {
                    if (x.getHealthManageType().equals(fTemplateForm.getHealthManageType())) {
                        throw new RuntimeException("不可重复添加健康管理模板");
                    }
                });
                if (list.size() > 4) {
                    throw new RuntimeException("健康管理方案类不可添加超过4个");
                }
            }


        }
    }
}
