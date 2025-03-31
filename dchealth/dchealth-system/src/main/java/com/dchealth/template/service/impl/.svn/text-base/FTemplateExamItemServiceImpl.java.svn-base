package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FTemplateExamItemMapper;
import com.dchealth.template.domain.FTemplateExamItem;
import com.dchealth.template.service.IFTemplateExamItemService;

/**
 * 属性题目选项-自评量Service业务层处理
 * 
 * @author lchbu
 * @date 2022-11-11
 */
@Service
public class FTemplateExamItemServiceImpl implements IFTemplateExamItemService 
{
    @Autowired
    private FTemplateExamItemMapper fTemplateExamItemMapper;

    /**
     * 查询属性题目选项-自评量
     * 
     * @param id 属性题目选项-自评量主键
     * @return 属性题目选项-自评量
     */
    @Override
    public FTemplateExamItem selectFTemplateExamItemById(Long id)
    {
        return fTemplateExamItemMapper.selectFTemplateExamItemById(id);
    }

    /**
     * 查询属性题目选项-自评量列表
     * 
     * @param fTemplateExamItem 属性题目选项-自评量
     * @return 属性题目选项-自评量
     */
    @Override
    public List<FTemplateExamItem> selectFTemplateExamItemList(FTemplateExamItem fTemplateExamItem)
    {
        return fTemplateExamItemMapper.selectFTemplateExamItemList(fTemplateExamItem);
    }

    /**
     * 新增属性题目选项-自评量
     * 
     * @param fTemplateExamItem 属性题目选项-自评量
     * @return 结果
     */
    @Override
    public int insertFTemplateExamItem(FTemplateExamItem fTemplateExamItem)
    {
        fTemplateExamItem.setCreateTime(DateUtils.getNowDate());
        return fTemplateExamItemMapper.insertFTemplateExamItem(fTemplateExamItem);
    }

    /**
     * 修改属性题目选项-自评量
     * 
     * @param fTemplateExamItem 属性题目选项-自评量
     * @return 结果
     */
    @Override
    public int updateFTemplateExamItem(FTemplateExamItem fTemplateExamItem)
    {
        return fTemplateExamItemMapper.updateFTemplateExamItem(fTemplateExamItem);
    }

    /**
     * 批量删除属性题目选项-自评量
     * 
     * @param ids 需要删除的属性题目选项-自评量主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateExamItemByIds(Long[] ids)
    {
        return fTemplateExamItemMapper.deleteFTemplateExamItemByIds(ids);
    }

    /**
     * 删除属性题目选项-自评量信息
     * 
     * @param id 属性题目选项-自评量主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateExamItemById(Long id)
    {
        return fTemplateExamItemMapper.deleteFTemplateExamItemById(id);
    }
}
