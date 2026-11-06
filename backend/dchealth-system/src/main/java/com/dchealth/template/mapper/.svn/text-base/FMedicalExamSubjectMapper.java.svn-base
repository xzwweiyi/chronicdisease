package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.ResArticle;

/**
 * 属性题目Mapper接口
 * 
 * @author lchbu
 * @date 2022-10-31
 */
public interface FMedicalExamSubjectMapper 
{
    /**
     * 查询属性题目
     * 
     * @param id 属性题目主键
     * @return 属性题目
     */
    public FMedicalExamSubject selectFMedicalExamSubjectById(Long id);

    /**
     * 查询属性题目列表
     * 
     * @param fMedicalExamSubject 属性题目
     * @return 属性题目集合
     */
    public List<FMedicalExamSubject> selectFMedicalExamSubjectList(FMedicalExamSubject fMedicalExamSubject);

    /**
     * 新增属性题目
     * 
     * @param fMedicalExamSubject 属性题目
     * @return 结果
     */
    public int insertFMedicalExamSubject(FMedicalExamSubject fMedicalExamSubject);

    /**
     * 修改属性题目
     * 
     * @param fMedicalExamSubject 属性题目
     * @return 结果
     */
    public int updateFMedicalExamSubject(FMedicalExamSubject fMedicalExamSubject);

    /**
     * 删除属性题目
     * 
     * @param id 属性题目主键
     * @return 结果
     */
    public int deleteFMedicalExamSubjectById(Long id);

    /**
     * 批量删除属性题目
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalExamSubjectByIds(Long[] ids);

    /**
     * 根据fromId查询问卷题目信息
     * @param medicalFormId
     * @return
     */
    List<FMedicalExamSubject> selectFMedicalExamSubjectByFromId(Long medicalFormId);

    List<FMedicalExamSubject> selectFMedicalExamSubjectByFollowId(Long followId);

    /**
     * 查询排序最大值
     * @param medicalFormId
     * @return
     */
    Integer selectExamSubjectMaxSortNumByFromId(Long medicalFormId);

    /**
     * 根据模板删除题干信息
     * @param medicalFormId
     */
    void deleteFMedicalExamSubjectByMedicalFormId(Long medicalFormId);

    List<FMedicalExamSubject> queryStatisticalData(FMedicalExamSubject fMedicalExamSubject);

    List<ResArticle> selectFMedicalExamSubjectResArticle(FMedicalExamSubject fMedicalExamSubject);

}
