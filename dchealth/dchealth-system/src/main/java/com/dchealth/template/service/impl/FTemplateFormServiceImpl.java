package com.dchealth.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private FTemplateFormSetEvaluationMapper fTemplateFormSetEvaluationMapper;
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
                } else if ("0".equals(x.getFormType()) || "4".equals(x.getFormType()) || "5".equals(x.getFormType())) {

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int copyFTemplateForm(Long templateId, Long templateFormId) {

        // 查询原表单
        FTemplateForm oldForm = fTemplateFormMapper.selectFTemplateFormByTemplateFormId(templateFormId);

        if (oldForm == null){
            throw new RuntimeException("表单不存在");
        }

        // 开始递归复制
        copyFormNode(oldForm, templateId, 0L);

        return 1;
    }
    /**
     * 递归复制表单
     *
     * @param oldForm 原节点
     * @param newTemplateId 新模板ID
     * @param parentId 新父ID
     */
    private void copyFormNode(FTemplateForm oldForm, Long newTemplateId, Long parentId){

        // ---------- 1.复制表单 ----------
        FTemplateForm newForm = new FTemplateForm();

        BeanUtils.copyProperties(oldForm, newForm);

        // 清空主键
        newForm.setTemplateFormId(null);

        newForm.setTemplateName(oldForm.getTemplateName() + "_copy");
        newForm.setTemplateCode(getMaxTemplateFormCode());
        // 新模板ID
        newForm.setTemplateId(newTemplateId);
        newForm.setUpdateTime(DateUtils.getNowDate());
        newForm.setCreateTime(DateUtils.getNowDate());
        // 新父ID
        newForm.setParentId(parentId);

        // 新增
        fTemplateFormMapper.insertFTemplateForm(newForm);
        FTemplateFormSetEvaluation fTemplateFormSetEvaluation = new FTemplateFormSetEvaluation();
        fTemplateFormSetEvaluation.setTemplateFormId(oldForm.getTemplateFormId());
        List<FTemplateFormSetEvaluation> tfses = fTemplateFormSetEvaluationMapper.selectFTemplateFormSetEvaluationList(fTemplateFormSetEvaluation);
        if (CollectionUtils.isNotEmpty(tfses)) {
            List<FTemplateFormSetEvaluation> fTemplateFormSetEvaluations = BeanUtil.copyToList(tfses, FTemplateFormSetEvaluation.class);
            fTemplateFormSetEvaluations.forEach(templateFormSetEvaluation -> {
                templateFormSetEvaluation.setTemplateFormId(newForm.getTemplateFormId());
                templateFormSetEvaluation.setId(null);
                templateFormSetEvaluation.setUpdateTime(DateUtils.getNowDate());
                templateFormSetEvaluation.setCreateTime(DateUtils.getNowDate());
                fTemplateFormSetEvaluationMapper.insertFTemplateFormSetEvaluation(templateFormSetEvaluation);
            });
        }
        Long newFormId = newForm.getTemplateFormId();

        // ---------- 2.复制属性 ----------

        String formType = oldForm.getFormType();

        // 自评量表
        if ("3".equals(formType)) {

            copyExamSubject(oldForm.getTemplateFormId(),newTemplateId, newFormId);

        } else {

            copyProperties(oldForm.getTemplateFormId(),newTemplateId, newFormId);
        }

        // ---------- 3.递归复制子节点 ----------

        FTemplateForm params = new FTemplateForm();
        params.setParentId(oldForm.getTemplateFormId());

        List<FTemplateForm> children = fTemplateFormMapper.selectFTemplateFormList(params);

        if (CollectionUtils.isNotEmpty(children)) {

            for (FTemplateForm child : children) {

                copyFormNode(child, newTemplateId, newFormId);
            }
        }
    }


    /**
     * 复制组件属性
     */
    private void copyProperties(Long oldFormId, Long newTemplateId, Long newFormId){

        List<FTemplateProperty> properties = fTemplatePropertyMapper.selectFTemplatePropertyByTemFromId(oldFormId);

        if (CollectionUtils.isEmpty(properties)){
            return;
        }

        for (FTemplateProperty oldProperty : properties){

            // ---------- 复制属性 ----------
            FTemplateProperty newProperty = new FTemplateProperty();

            BeanUtils.copyProperties(oldProperty, newProperty);

            newProperty.setId(null);
            newProperty.setTemplateFormId(newFormId);
            newProperty.setTemplateId(newTemplateId);
            newProperty.setUpdateTime(DateUtils.getNowDate());
            newProperty.setCreateTime(DateUtils.getNowDate());
            fTemplatePropertyMapper.insertFTemplateProperty(newProperty);

            Long newPropertyId = newProperty.getId();

            // ---------- 复制预警 ----------
            WarnTemplateEarly warn = new WarnTemplateEarly();
            warn.setPropertyId(oldProperty.getId());
            warn.setFormId(oldFormId);

            List<WarnTemplateEarly> earlyList = warnTemplateEarlyMapper.selectWarnTemplateEarlyList(warn);

            if (!CollectionUtils.isEmpty(earlyList)){

                for (WarnTemplateEarly oldEarly : earlyList){

                    WarnTemplateEarly newEarly = new WarnTemplateEarly();

                    BeanUtils.copyProperties(oldEarly, newEarly);

                    newEarly.setId(null);
                    newEarly.setPropertyId(newPropertyId);
                    newEarly.setFormId(newFormId);
                    newEarly.setTemplateId(newTemplateId);
                    newEarly.setUpdateTime(DateUtils.getNowDate());
                    newEarly.setCreateTime(DateUtils.getNowDate());
                    warnTemplateEarlyMapper.insertWarnTemplateEarly(newEarly);

                    Long newEarlyId = newEarly.getId();

                    // ---------- 复制预警条件 ----------
                    WarnTemplateEarlyCondition condition = new WarnTemplateEarlyCondition();

                    condition.setTemplateEarlyWarnId(oldEarly.getId());

                    List<WarnTemplateEarlyCondition> conditionList = warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(condition);

                    if (!CollectionUtils.isEmpty(conditionList)){

                        for (WarnTemplateEarlyCondition oldCondition : conditionList){

                            WarnTemplateEarlyCondition newCondition = new WarnTemplateEarlyCondition();

                            BeanUtils.copyProperties(oldCondition, newCondition);
                            newCondition.setTemplateId(newTemplateId);
                            newCondition.setId(null);
                            newCondition.setTemplateEarlyWarnId(newEarlyId);
                            newCondition.setPropertyId(newProperty.getId());
                            newCondition.setUpdateTime(DateUtils.getNowDate());
                            newCondition.setCreateTime(DateUtils.getNowDate());
                            warnTemplateEarlyConditionMapper.insertWarnTemplateEarlyCondition(newCondition);
                        }
                    }
                }
            }
        }
    }
    /**
     * 复制自评量表
     */
    private void copyExamSubject(Long oldFormId, Long newTemplateId, Long newFormId){

        List<FTemplateExamSubject> subjectList = fTemplateExamSubjectMapper.selectFTemplateExamSubjectByTemFromId(oldFormId);

        if (CollectionUtils.isEmpty(subjectList)){
            return;
        }

        for (FTemplateExamSubject oldSubject : subjectList){

            // ---------- 复制题目 ----------
            FTemplateExamSubject newSubject = new FTemplateExamSubject();

            BeanUtils.copyProperties(oldSubject, newSubject);

            newSubject.setId(null);
            newSubject.setTemplateFormId(newFormId);
            newSubject.setTemplateId(newTemplateId);
            newSubject.setUpdateTime(DateUtils.getNowDate());
            newSubject.setCreateTime(DateUtils.getNowDate());
            fTemplateExamSubjectMapper.insertFTemplateExamSubject(newSubject);

            Long newSubjectId = newSubject.getId();

            // ---------- 复制选项 ----------
            List<FTemplateExamItem> itemList = fTemplateExamItemMapper.selectExamItemByPropertyId(oldSubject.getId());

            if (!CollectionUtils.isEmpty(itemList)){

                for (FTemplateExamItem oldItem : itemList){

                    FTemplateExamItem newItem = new FTemplateExamItem();
                    BeanUtils.copyProperties(oldItem, newItem);
                    newItem.setId(null);
                    // 新题目ID
                    newItem.setPropertyId(newSubjectId);
                    newItem.setTemplateFormId(newFormId);
                    newItem.setUpdateTime(DateUtils.getNowDate());
                    newItem.setCreateTime(DateUtils.getNowDate());
                    fTemplateExamItemMapper.insertFTemplateExamItem(newItem);
                }
            }
        }
    }
    private String getMaxTemplateFormCode() {

        String maxCode = fTemplateFormMapper.selectMaxTemplateFormCode();

        // 默认第一个
        if (maxCode == null || maxCode.trim().isEmpty()) {
            return "modela";
        }

        // 去掉前缀
        String suffix = maxCode.replaceFirst("^model", "");

        // 字母递增
        String next = recoverCode(suffix);

        return "model" + next;
    }
    /**
     * 字母编码递增
     *
     * a -> b
     * z -> aa
     * az -> ba
     * zz -> aaa
     */
    private String recoverCode(String code) {

        char[] chars = code.toCharArray();

        int index = chars.length - 1;

        // 从后往前进位
        while (index >= 0) {

            // 当前不是 z
            if (chars[index] < 'z') {

                chars[index]++;

                return new String(chars);
            }

            // 当前是 z
            chars[index] = 'a';

            index--;
        }

        // 全部都是 z
        return "a" + new String(chars);
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
