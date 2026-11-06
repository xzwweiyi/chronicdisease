package com.dchealth.template.service.impl;

import java.util.List;
import java.util.Optional;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.FTemplateExamItem;
import com.dchealth.template.domain.vo.FTemplateExamVo;
import com.dchealth.template.mapper.FTemplateExamItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FTemplateExamSubjectMapper;
import com.dchealth.template.domain.FTemplateExamSubject;
import com.dchealth.template.service.IFTemplateExamSubjectService;

/**
 * 属性-自评量Service业务层处理
 * 
 * @author lchbu
 * @date 2022-11-11
 */
@Service
public class FTemplateExamSubjectServiceImpl implements IFTemplateExamSubjectService 
{
    @Autowired
    private FTemplateExamSubjectMapper fTemplateExamSubjectMapper;
    @Autowired
    private FTemplateExamItemMapper fTemplateExamItemMapper;

    /**
     * 查询属性-自评量
     * 
     * @param id 属性-自评量主键
     * @return 属性-自评量
     */
    @Override
    public FTemplateExamSubject selectFTemplateExamSubjectById(Long id)
    {
        return fTemplateExamSubjectMapper.selectFTemplateExamSubjectById(id);
    }

    /**
     * 查询属性-自评量列表
     * 
     * @param fTemplateExamSubject 属性-自评量
     * @return 属性-自评量
     */
    @Override
    public List<FTemplateExamSubject> selectFTemplateExamSubjectList(FTemplateExamSubject fTemplateExamSubject)
    {
        return fTemplateExamSubjectMapper.selectFTemplateExamSubjectList(fTemplateExamSubject);
    }

    /**
     * 新增属性-自评量
     * 
     * @param vo 属性-自评量表
     * @return 结果
     */
    @Override
    public int insertFTemplateExamSubject(FTemplateExamVo vo)
    {
        Long templateFormId = vo.getTemplateFormId();
        Optional<List<FTemplateExamSubject>> examSubjectList = Optional.ofNullable(vo.getExamSubjectList());
        //删除题干、选项信息
        fTemplateExamSubjectMapper.deleteFTemplateExamSubjectByTemplateFormId(templateFormId);
        fTemplateExamItemMapper.deleteFTemplateExamItemByTemplateFormId(templateFormId);
        //重新创建题干、选项信息
        if(examSubjectList.isPresent()){
            examSubjectList.get().forEach(subject -> {
                subject.setCreateTime(DateUtils.getNowDate());
                subject.setTemplateFormId(templateFormId);
                //题干排序最大值
                Integer maxSortNum = fTemplateExamSubjectMapper.selectExamSubjectMaxSortNumByTemplateFormId(templateFormId);
                subject.setSortNum(maxSortNum != null ? (maxSortNum + 1) : 0);
                fTemplateExamSubjectMapper.insertFTemplateExamSubject(subject);
                Optional<List<FTemplateExamItem>> items = Optional.ofNullable(subject.getItems());
                if(items.isPresent()){
                    items.get().forEach(item -> {
                        item.setCreateTime(DateUtils.getNowDate());
                        item.setPropertyId(subject.getId());
                        item.setTemplateFormId(templateFormId);
                        //选项排序最大值
                        Integer maxOrderNum = fTemplateExamItemMapper.selectExamItemMaxOrderNumByPropertyId(subject.getId());
                        item.setOrderNum(maxOrderNum != null ? (maxOrderNum + 1) : 0);
                        fTemplateExamItemMapper.insertFTemplateExamItem(item);
                    });
                }
            });
        }
        return 1;
    }

    /**
     * 修改属性-自评量
     * 
     * @param fTemplateExamSubject 属性-自评量
     * @return 结果
     */
    @Override
    public int updateFTemplateExamSubject(FTemplateExamSubject fTemplateExamSubject)
    {
        return fTemplateExamSubjectMapper.updateFTemplateExamSubject(fTemplateExamSubject);
    }

    /**
     * 批量删除属性-自评量
     * 
     * @param ids 需要删除的属性-自评量主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateExamSubjectByIds(Long[] ids)
    {
        return fTemplateExamSubjectMapper.deleteFTemplateExamSubjectByIds(ids);
    }

    /**
     * 删除属性-自评量信息
     * 
     * @param id 属性-自评量主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateExamSubjectById(Long id)
    {
        return fTemplateExamSubjectMapper.deleteFTemplateExamSubjectById(id);
    }
}
