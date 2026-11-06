package com.dchealth.question.service;

import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.form.SaveQuestionRecordVo;

import java.util.List;

/**
 * 质疑Service接口
 * 
 * @author lchbu
 * @date 2023-02-23
 */
public interface IQuestionRecordService 
{
    /**
     * 查询质疑
     * 
     * @param questionId 质疑主键
     * @return 质疑
     */
    public QuestionRecord selectQuestionRecordByQuestionId(Long questionId);

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
     * 新增质疑
     *
     * @param questionRecord 质疑
     * @return 结果
     */
    public int saveQuestionRecord(SaveQuestionRecordVo saveQuestionRecordVo);

    /**
     * 修改质疑
     * 
     * @param questionRecord 质疑
     * @return 结果
     */
    public int updateQuestionRecord(QuestionRecord questionRecord);

    /**
     * 批量删除质疑
     * 
     * @param questionIds 需要删除的质疑主键集合
     * @return 结果
     */
    public int deleteQuestionRecordByQuestionIds(Long[] questionIds);

    /**
     * 删除质疑信息
     * 
     * @param questionId 质疑主键
     * @return 结果
     */
    public int deleteQuestionRecordByQuestionId(Long questionId);
}
