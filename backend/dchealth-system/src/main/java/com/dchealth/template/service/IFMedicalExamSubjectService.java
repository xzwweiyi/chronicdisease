package com.dchealth.template.service;

import java.util.List;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.vo.FMedicalExamVo;

/**
 * 属性题目Service接口
 * 
 * @author lchbu
 * @date 2022-10-31
 */
public interface IFMedicalExamSubjectService 
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
     * @param vo 属性题目
     * @return 结果
     */
    public int insertFMedicalExamSubject(FMedicalExamVo vo);

    /**
     * 修改属性题目
     * 
     * @param fMedicalExamSubject 属性题目
     * @return 结果
     */
    public int updateFMedicalExamSubject(FMedicalExamSubject fMedicalExamSubject);

    /**
     * 批量删除属性题目
     * 
     * @param ids 需要删除的属性题目主键集合
     * @return 结果
     */
    public int deleteFMedicalExamSubjectByIds(Long[] ids);

    /**
     * 删除属性题目信息
     * 
     * @param id 属性题目主键
     * @return 结果
     */
    public int deleteFMedicalExamSubjectById(Long id);

    /**
     * 新增属性题目（自评量表）
     * @param fMedicalExamVo
     * @return
     */
    int insertFMedicalExamSubjectScale(FMedicalExamVo fMedicalExamVo);
}
