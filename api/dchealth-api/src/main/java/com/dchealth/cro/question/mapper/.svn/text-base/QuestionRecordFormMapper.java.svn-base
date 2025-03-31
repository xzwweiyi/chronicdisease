package com.dchealth.cro.question.mapper;

import com.dchealth.cro.question.form.QuestionRecordForm;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 质疑档案单关系Mapper接口
 * 
 * @author lchbu
 * @date 2022-09-28
 */
@Repository
public interface QuestionRecordFormMapper 
{
    /**
     * 查询质疑档案单关系
     * 
     * @param questionId 质疑档案单关系主键
     * @return 质疑档案单关系
     */
    public QuestionRecordForm selectQuestionRecordFormByQuestionId(Long questionId);

    /**
     * 查询质疑档案单关系列表
     * 
     * @param questionRecordForm 质疑档案单关系
     * @return 质疑档案单关系集合
     */
    public List<QuestionRecordForm> selectQuestionRecordFormList(QuestionRecordForm questionRecordForm);

    /**
     * 新增质疑档案单关系
     * 
     * @param questionRecordForm 质疑档案单关系
     * @return 结果
     */
    public int insertQuestionRecordForm(QuestionRecordForm questionRecordForm);

    /**
     * 修改质疑档案单关系
     * 
     * @param questionRecordForm 质疑档案单关系
     * @return 结果
     */
    public int updateQuestionRecordForm(QuestionRecordForm questionRecordForm);

    /**
     * 删除质疑档案单关系
     * 
     * @param questionId 质疑档案单关系主键
     * @return 结果
     */
    public int deleteQuestionRecordFormByQuestionId(Long questionId);

    /**
     * 批量删除质疑档案单关系
     * 
     * @param questionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuestionRecordFormByQuestionIds(Long[] questionIds);

     public void deleteQuestionRecordFormByRecordId(Long valueOf);
}
