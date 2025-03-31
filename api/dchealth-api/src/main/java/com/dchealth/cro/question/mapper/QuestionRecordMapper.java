package com.dchealth.cro.question.mapper;

import com.dchealth.cro.question.domain.QuestionRecord;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 质疑Mapper接口
 * 
 * @author lchbu
 * @date 2022-11-09
 */
@Repository
public interface QuestionRecordMapper 
{
    /**
     * 查询质疑
     * 
     * @param questionId 质疑主键
     * @return 质疑
     */
    public QuestionRecord selectQuestionRecordByQuestionId(Long questionId);

    /**
     * 根据档案查询
     * @param recordId
     * @return
     */
    public List<QuestionRecord> selectQuestionRecordByRecordId(Long recordId);

    /**
     * 查询质疑列表
     * 
     * @param questionRecord 质疑
     * @return 质疑集合
     */
    public List<QuestionRecord> selectQuestionRecordList(QuestionRecord questionRecord);

    /**
     * 新增质疑
     * 
     * @param questionRecord 质疑
     * @return 结果
     */
    public int insertQuestionRecord(QuestionRecord questionRecord);

    /**
     * 修改质疑
     * 
     * @param questionRecord 质疑
     * @return 结果
     */
    public int updateQuestionRecord(QuestionRecord questionRecord);

    /**
     * 删除质疑
     * 
     * @param questionId 质疑主键
     * @return 结果
     */
    public int deleteQuestionRecordByQuestionId(Long questionId);

    /**
     * 批量删除质疑
     * 
     * @param questionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuestionRecordByQuestionIds(Long[] questionIds);
}
