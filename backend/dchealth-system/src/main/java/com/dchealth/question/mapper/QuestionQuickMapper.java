package com.dchealth.question.mapper;

import java.util.List;
import com.dchealth.question.domain.QuestionQuick;

/**
 * 质疑快捷回复Mapper接口
 * 
 * @author lchbu
 * @date 2022-09-27
 */
public interface QuestionQuickMapper 
{
    /**
     * 查询质疑快捷回复
     * 
     * @param questionQuickId 质疑快捷回复主键
     * @return 质疑快捷回复
     */
    public QuestionQuick selectQuestionQuickByQuestionQuickId(Long questionQuickId);

    /**
     * 查询质疑快捷回复列表
     * 
     * @param questionQuick 质疑快捷回复
     * @return 质疑快捷回复集合
     */
    public List<QuestionQuick> selectQuestionQuickList(QuestionQuick questionQuick);

    /**
     * 根据userID快捷回复列表
     * @param userId
     * @return
     */
    public List<QuestionQuick> selectQuestionQuickListByUserId(Long userId);

    /**
     * 新增质疑快捷回复
     * 
     * @param questionQuick 质疑快捷回复
     * @return 结果
     */
    public int insertQuestionQuick(QuestionQuick questionQuick);

    /**
     * 修改质疑快捷回复
     * 
     * @param questionQuick 质疑快捷回复
     * @return 结果
     */
    public int updateQuestionQuick(QuestionQuick questionQuick);

    /**
     * 删除质疑快捷回复
     * 
     * @param questionQuickId 质疑快捷回复主键
     * @return 结果
     */
    public int deleteQuestionQuickByQuestionQuickId(Long questionQuickId);

    /**
     * 批量删除质疑快捷回复
     * 
     * @param questionQuickIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuestionQuickByQuestionQuickIds(Long[] questionQuickIds);
}
