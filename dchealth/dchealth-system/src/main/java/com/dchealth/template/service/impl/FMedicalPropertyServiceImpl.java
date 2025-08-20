package com.dchealth.template.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.dynamic.templete.DynamicFromsLog;
import com.dchealth.dynamic.templete.DynamicLargeTextLog;
import com.dchealth.dynamic.templete.DynamicTextLog;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.template.domain.*;
import com.dchealth.template.domain.vo.FMedicalValueVo;
import com.dchealth.template.domain.vo.FMedicalVo;
import com.dchealth.template.mapper.*;
import com.dchealth.template.service.IFMedicalPropertyService;
import com.dchealth.warn.mapper.WarnMedicalEarlyConditionMapper;
import com.dchealth.warn.mapper.WarnMedicalEarlyMapper;
import com.dchealth.warn.service.IWarnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 属性Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FMedicalPropertyServiceImpl implements IFMedicalPropertyService
{
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FLibraryPropertyMapper fLibraryPropertyMapper;

    @Autowired
    private DynamicLargeTextLog dynamicLargeTextLog;

    @Autowired
    private DynamicTextLog dynamicTextLog;

    @Autowired
    private DynamicFromsLog dynamicFromsLog;

    @Autowired
    private IWarnService warnService;

    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    @Autowired
    private WarnMedicalEarlyMapper warnMedicalEarlyMapper;
    @Autowired
    private WarnMedicalEarlyConditionMapper warnMedicalEarlyConditionMapper;


    /**
     * 查询属性
     *
     * @param id 属性主键
     * @return 属性
     */
    @Override
    public FMedicalProperty selectFMedicalPropertyById(Long id)
    {
        return fMedicalPropertyMapper.selectFMedicalPropertyById(id);
    }

    /**
     * 查询属性列表
     *
     * @param fMedicalProperty 属性
     * @return 属性
     */
    @Override
    public List<FMedicalProperty> selectFMedicalPropertyList(FMedicalProperty fMedicalProperty)
    {
        return fMedicalPropertyMapper.selectFMedicalPropertyList(fMedicalProperty);
    }

    /**
     * 新增属性
     *
     * @param fMedicalProperty 属性
     * @return 结果
     */
    @Override
    public int insertFMedicalProperty(FMedicalProperty fMedicalProperty)
    {
        fMedicalProperty.setCreateTime(DateUtils.getNowDate());
        return fMedicalPropertyMapper.insertFMedicalProperty(fMedicalProperty);
    }

    /**
     * 修改属性
     *
     * @param fMedicalProperty 属性
     * @return 结果
     */
    @Override
    public int updateFMedicalProperty(FMedicalProperty fMedicalProperty)
    {
        return fMedicalPropertyMapper.updateFMedicalProperty(fMedicalProperty);
    }

    /**
     * 迭代删除
     * @param fMedicalForms
     * @return
     */
    private int deleteIter(List<FMedicalForm> fMedicalForms){
        if (fMedicalForms != null && !fMedicalForms.isEmpty()){
            fMedicalForms.forEach(x->{
                FMedicalForm params = new FMedicalForm();
                params.setParentId(x.getMedicalFormId());
                List<FMedicalForm> childrens = fMedicalFormMapper.selectFMedicalFormList(params);
                deleteChildDataIter(childrens);
                fMedicalFormMapper.deleteFMedicalFormByMedicalFormId(x.getMedicalFormId());
                deleteParentData(x);
            });
        }
        return 1;
    }

    @Override
    @Transactional
    public int save(FMedicalForm fMedicalForm) {
        FMedicalForm params = new FMedicalForm();
        params.setParentId(fMedicalForm.getMedicalFormId());
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(params);
        //删除旧的数据
        //deleteChildDataIter(fMedicalForms);
        //deleteParentData(fMedicalForm);
        //插入新的数据
        save(fMedicalForm.getFollowId(), fMedicalForm.getParentId() == null ? 0l : fMedicalForm.getParentId(),
                new ArrayList<FMedicalForm>() {{
                    add(fMedicalForm);
                }}
        );
        return 1;
    }

    private void deleteParentData(FMedicalForm fMedicalForm) {
        // 删除父数据
        fMedicalPropertyMapper.deleteFMedicalPropertyByMedicalFormId(fMedicalForm.getMedicalFormId());
        //删除自评量表信息
        fMedicalExamSubjectMapper.deleteFMedicalExamSubjectByMedicalFormId(fMedicalForm.getMedicalFormId());
        fMedicalExamItemMapper.deleteFMedicalExamItemByMedicalFormId(fMedicalForm.getMedicalFormId());
    }

    private void deleteChildDataIter(List<FMedicalForm> fMedicalForms) {
        if (fMedicalForms != null && !fMedicalForms.isEmpty()){
            //迭代删除子数据
            deleteIter(fMedicalForms);
        }
    }

    private int save(Long followId,Long parentFormId, List<FMedicalForm> fMedicalForms){
        Date date = DateUtils.getNowDate();
        fMedicalForms.forEach(x->{
            if (x.getMedicalFormId() == null){
                x.setCreateTime(date);
                x.setFollowId(followId);
                x.setParentId(parentFormId);
                fMedicalFormMapper.insertFMedicalForm(x);
            }
            if ("2".equals(x.getFormType()) && x.getBaseFormId() != null){//拍照项组件
                FLibraryProperty params = new FLibraryProperty();
                params.setLibraryId(x.getBaseFormId());
                List<FLibraryProperty> fLibraryProperties = fLibraryPropertyMapper.selectFLibraryPropertyList(params);
                List<FMedicalProperty> list = null;
                if (fLibraryProperties != null && !fLibraryProperties.isEmpty()){
                    list = new ArrayList<>();
                    for (FLibraryProperty y : fLibraryProperties){
                        FMedicalProperty property = new FMedicalProperty();
                        BeanUtils.copyProperties(y,property);
                        list.add(property);
                    }
                    x.setMedicalProperties(list);
                }
            }
            /*if ("5".equals(x.getFormType())){
                warnService.saveWarnMedical(followId, x.getMedicalFormId(), x.getWarnMedicalEarlyForms());
            }*/

            if ("3".equals(x.getFormType()) && x.getScoresTotalVal()!=null){
                //设置问卷总分
                fMedicalFormMapper.updateScoreByMedicalFormId(x.getMedicalFormId(),x.getScoresTotalVal());
            }
            if (x.getMedicalProperties() != null && !x.getMedicalProperties().isEmpty()){
                x.getMedicalProperties().forEach(y->{
                    if("3".equals(x.getFormType())){
                        FMedicalExamSubject subject = new FMedicalExamSubject();
                        BeanUtils.copyProperties(y, subject);
                        Long medicalFormId = x.getMedicalFormId();
                        subject.setCreateTime(date);
                        subject.setMedicalFormId(medicalFormId);
                        subject.setIfScore("1");
                        subject.setFollowId(x.getFollowId());
                        //存储每一个题目
                        Long id=y.getId();
                        if (id!=null){
                            fMedicalExamSubjectMapper.updateFMedicalExamSubject(subject);
                        }else {
                            fMedicalExamSubjectMapper.insertFMedicalExamSubject(subject);
                        }
                        //保存题目的选项
                        Optional<List<FMedicalExamItem>> itemList = Optional.ofNullable(y.getItemList());
                        if(itemList.isPresent()){
                            itemList.get().forEach(item ->{
                                item.setPropertyId(subject.getId());
                                item.setMedicalFormId(medicalFormId);
                                item.setCreateTime(date);
                                Long itemId=item.getId();
                                if (itemId!=null){
                                    fMedicalExamItemMapper.updateFMedicalExamItem(item);
                                }else {
                                    fMedicalExamItemMapper.insertFMedicalExamItem(item);
                                }
                            });
                        }
                    } else {
                        y.setCreateTime(date);
                        y.setMedicalFormId(x.getMedicalFormId());
                        y.setFollowId(followId);
                        Long id=y.getId();
                        if (id!=null){
                            updateFMedicalProperty(y);
                        }else {
                            insertFMedicalProperty(y);
                        }
                        if (y.getWarnMedicalEarlyForms() != null && !y.getWarnMedicalEarlyForms().isEmpty()){
                            warnService.saveWarnMedical(followId, x.getMedicalFormId(), y.getId(), y.getWarnMedicalEarlyForms());
                        }
                    }
                });
            }
            if (x.getMedicalFormList() != null && !x.getMedicalFormList().isEmpty()){
                //递归
                save(followId,x.getMedicalFormId(),x.getMedicalFormList());
            }
        });
        return 1;
    }

    /**
     * 批量删除属性
     *
     * @param ids 需要删除的属性主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalPropertyByIds(Long[] ids)
    {
        return fMedicalPropertyMapper.deleteFMedicalPropertyByIds(ids);
    }

    /**
     * 删除属性信息
     *
     * @param id 属性主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalPropertyById(Long id)
    {
        return fMedicalPropertyMapper.deleteFMedicalPropertyById(id);
    }

    @Override
    public List<FMedicalValueVo> findValueChangeRecord(FMedicalVo fMedicalVo) {

        FMedicalForm fMedicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(fMedicalVo.getFormId());
        String dynamic = fMedicalForm.getDynamic();
        String propertyType = fMedicalVo.getPropertyType();
        List<FMedicalValueVo> valueVos = null;
        if (propertyType != null && PropertyType.largeText(propertyType)) {
            //大文本
            String sql = dynamicLargeTextLog.buildSelectSql(fMedicalVo.getResearchId(), fMedicalVo.getFormId(), fMedicalVo.getRecordId(),fMedicalVo.getPropertyId(),fMedicalVo.getPropertyCode());
            valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
        }
        if(PropertyType.text(propertyType)) {
            //小文本
            String sql = dynamicTextLog.buildSelectSql(fMedicalVo.getResearchId(), fMedicalVo.getFormId(), fMedicalVo.getRecordId(),fMedicalVo.getPropertyId(),fMedicalVo.getPropertyCode());
            valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
        }
        if("1".equals(dynamic)){
            //动态表单
            String sql = dynamicFromsLog.buildSelectSql(fMedicalVo.getResearchId(), fMedicalVo.getFormId(), fMedicalVo.getRecordId(),fMedicalVo.getPropertyId(),fMedicalVo.getPropertyCode());
            valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
        }
        List<FMedicalValueVo> collect = null;
        if (valueVos != null){
            collect = valueVos.stream().filter(distinctByKey(FMedicalValueVo::getPropertyValue)).collect(Collectors.toList());

        }
        return collect;
    }

    @Override
    public void deletePropertyById(Long id, Long medicalFormId, Integer formType) {
        if (id != null) {
            //0模块1标准项2拍照项3自评量表4预警填报
            if (3 == formType) {
                fMedicalExamSubjectMapper.deleteFMedicalExamSubjectById(id);
                fMedicalExamItemMapper.deleteFMedicalExamItemByPropertyId(id);
            } else {
                fMedicalPropertyMapper.deleteFMedicalPropertyById(id);
                if (4 == formType) {
                    warnMedicalEarlyMapper.deleteWarnMedicalEarlyByPropertyId(id);
                    warnMedicalEarlyConditionMapper.deleteWarnMedicalEarlyConditionByPropertyId(id);
                }
            }
        } else {
            fMedicalFormMapper.deleteFMedicalFormByMedicalFormId(medicalFormId);
            fMedicalPropertyMapper.deleteFMedicalPropertyByMedicalFormId(medicalFormId);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
