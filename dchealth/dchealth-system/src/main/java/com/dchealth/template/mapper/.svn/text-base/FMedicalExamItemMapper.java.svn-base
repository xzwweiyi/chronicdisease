package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FMedicalExamItem;

/**
 * 属性题目选项Mapper接口
 *
 * @author lchbu
 * @date 2022-10-31
 */
public interface FMedicalExamItemMapper
{
    /**
     * 查询属性题目选项
     *
     * @param id 属性题目选项主键
     * @return 属性题目选项
     */
    public FMedicalExamItem selectFMedicalExamItemById(Long id);

    /**
     * 查询属性题目选项列表
     *
     * @param fMedicalExamItem 属性题目选项
     * @return 属性题目选项集合
     */
    public List<FMedicalExamItem> selectFMedicalExamItemList(FMedicalExamItem fMedicalExamItem);

    /**
     * 新增属性题目选项
     *
     * @param fMedicalExamItem 属性题目选项
     * @return 结果
     */
    public int insertFMedicalExamItem(FMedicalExamItem fMedicalExamItem);

    /**
     * 修改属性题目选项
     *
     * @param fMedicalExamItem 属性题目选项
     * @return 结果
     */
    public int updateFMedicalExamItem(FMedicalExamItem fMedicalExamItem);

    /**
     * 删除属性题目选项
     *
     * @param id 属性题目选项主键
     * @return 结果
     */
    public int deleteFMedicalExamItemById(Long id);

    /**
     * 批量删除属性题目选项
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalExamItemByIds(Long[] ids);

    /**
     * 根据题目id查询选型信息
     * @param propertyId
     * @return
     */
    List<FMedicalExamItem> selectFMedicalExamItemByProId(Long propertyId);

    /**
     * 查询最大排序值
     * @param propertyId
     * @return
     */
    Integer selectExamItemMaxOrderNum(Long propertyId);

    /**
     * 根据模板信息删除选项信息
     * @param medicalFormId
     */
    void deleteFMedicalExamItemByMedicalFormId(Long medicalFormId);

    List<FMedicalExamItem> selectFMedicalExamItemByFromId(Long medicalFormId);

    List<FMedicalExamItem> selectFMedicalExamItemByProIdAndValue(Long propertyId, List<String> values);
}
