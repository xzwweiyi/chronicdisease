package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FTemplateExamItem;

/**
 * 属性题目选项-自评量Mapper接口
 *
 * @author lchbu
 * @date 2022-11-11
 */
public interface FTemplateExamItemMapper
{
    /**
     * 查询属性题目选项-自评量
     *
     * @param id 属性题目选项-自评量主键
     * @return 属性题目选项-自评量
     */
    public FTemplateExamItem selectFTemplateExamItemById(Long id);

    /**
     * 查询属性题目选项-自评量列表
     *
     * @param fTemplateExamItem 属性题目选项-自评量
     * @return 属性题目选项-自评量集合
     */
    public List<FTemplateExamItem> selectFTemplateExamItemList(FTemplateExamItem fTemplateExamItem);

    /**
     * 新增属性题目选项-自评量
     *
     * @param fTemplateExamItem 属性题目选项-自评量
     * @return 结果
     */
    public int insertFTemplateExamItem(FTemplateExamItem fTemplateExamItem);

    /**
     * 修改属性题目选项-自评量
     *
     * @param fTemplateExamItem 属性题目选项-自评量
     * @return 结果
     */
    public int updateFTemplateExamItem(FTemplateExamItem fTemplateExamItem);

    /**
     * 删除属性题目选项-自评量
     *
     * @param id 属性题目选项-自评量主键
     * @return 结果
     */
    public int deleteFTemplateExamItemById(Long id);

    /**
     * 批量删除属性题目选项-自评量
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFTemplateExamItemByIds(Long[] ids);

    /**
     * 删除选线信息
     * @param templateFormId
     */
    void deleteFTemplateExamItemByTemplateFormId(Long templateFormId);

    /**
     * 查询选项排序最大值
     * @param id
     * @return
     */
    Integer selectExamItemMaxOrderNumByPropertyId(Long id);

    List<FTemplateExamItem> selectExamItemByPropertyId(Long id);
}
