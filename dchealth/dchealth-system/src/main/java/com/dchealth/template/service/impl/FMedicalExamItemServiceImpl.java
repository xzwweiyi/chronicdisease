package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FMedicalExamItemMapper;
import com.dchealth.template.domain.FMedicalExamItem;
import com.dchealth.template.service.IFMedicalExamItemService;

/**
 * 属性题目选项Service业务层处理
 * 
 * @author lchbu
 * @date 2022-10-31
 */
@Service
public class FMedicalExamItemServiceImpl implements IFMedicalExamItemService 
{
    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    /**
     * 查询属性题目选项
     * 
     * @param id 属性题目选项主键
     * @return 属性题目选项
     */
    @Override
    public FMedicalExamItem selectFMedicalExamItemById(Long id)
    {
        return fMedicalExamItemMapper.selectFMedicalExamItemById(id);
    }

    /**
     * 查询属性题目选项列表
     * 
     * @param fMedicalExamItem 属性题目选项
     * @return 属性题目选项
     */
    @Override
    public List<FMedicalExamItem> selectFMedicalExamItemList(FMedicalExamItem fMedicalExamItem)
    {
        return fMedicalExamItemMapper.selectFMedicalExamItemList(fMedicalExamItem);
    }

    /**
     * 新增属性题目选项
     * 
     * @param fMedicalExamItem 属性题目选项
     * @return 结果
     */
    @Override
    public int insertFMedicalExamItem(FMedicalExamItem fMedicalExamItem)
    {
        fMedicalExamItem.setCreateTime(DateUtils.getNowDate());
        return fMedicalExamItemMapper.insertFMedicalExamItem(fMedicalExamItem);
    }

    /**
     * 修改属性题目选项
     * 
     * @param fMedicalExamItem 属性题目选项
     * @return 结果
     */
    @Override
    public int updateFMedicalExamItem(FMedicalExamItem fMedicalExamItem)
    {
        return fMedicalExamItemMapper.updateFMedicalExamItem(fMedicalExamItem);
    }

    /**
     * 批量删除属性题目选项
     * 
     * @param ids 需要删除的属性题目选项主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalExamItemByIds(Long[] ids)
    {
        return fMedicalExamItemMapper.deleteFMedicalExamItemByIds(ids);
    }

    /**
     * 删除属性题目选项信息
     * 
     * @param id 属性题目选项主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalExamItemById(Long id)
    {
        return fMedicalExamItemMapper.deleteFMedicalExamItemById(id);
    }
}
