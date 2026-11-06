package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FTemplateExamSubject;

/**
 * 属性-自评量Mapper接口
 *
 * @author lchbu
 * @date 2022-11-11
 */
public interface FTemplateExamSubjectMapper
{
    /**
     * 查询属性-自评量
     *
     * @param id 属性-自评量主键
     * @return 属性-自评量
     */
    public FTemplateExamSubject selectFTemplateExamSubjectById(Long id);

    /**
     * 查询属性-自评量列表
     *
     * @param fTemplateExamSubject 属性-自评量
     * @return 属性-自评量集合
     */
    public List<FTemplateExamSubject> selectFTemplateExamSubjectList(FTemplateExamSubject fTemplateExamSubject);

    /**
     * 新增属性-自评量
     *
     * @param fTemplateExamSubject 属性-自评量
     * @return 结果
     */
    public int insertFTemplateExamSubject(FTemplateExamSubject fTemplateExamSubject);

    /**
     * 修改属性-自评量
     *
     * @param fTemplateExamSubject 属性-自评量
     * @return 结果
     */
    public int updateFTemplateExamSubject(FTemplateExamSubject fTemplateExamSubject);

    /**
     * 删除属性-自评量
     *
     * @param id 属性-自评量主键
     * @return 结果
     */
    public int deleteFTemplateExamSubjectById(Long id);

    /**
     * 批量删除属性-自评量
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFTemplateExamSubjectByIds(Long[] ids);

    /**
     * 删除题干信息
     * @param templateFormId
     */
    void deleteFTemplateExamSubjectByTemplateFormId(Long templateFormId);

    /**
     * 查询题干sortNum最大值
     * @param templateFormId
     * @return
     */
    Integer selectExamSubjectMaxSortNumByTemplateFormId(Long templateFormId);

    /**
     * 查询自评量表题干信息
     * @param templateFormId
     * @return
     */
    List<FTemplateExamSubject> selectFTemplateExamSubjectByTemFromId(Long templateFormId);
}
