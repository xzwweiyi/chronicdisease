package com.dchealth.cro.research.mapper;


import com.dchealth.cro.research.domain.ResearchInfoArticle;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 队列-患教关系表(ResearchInfoArticle)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-08 13:45:26
 */
@Repository
public interface ResearchInfoArticleMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param researchId 主键
     * @return 实例对象
     */
    ResearchInfoArticle queryById(Long researchId);

    ResearchInfoArticle queryResearchInfoArticle( ResearchInfoArticle researchInfoArticle);

    List<ResearchInfoArticle> queryByArticleIdAndAccountId( Long articleId,Long accountId);
    /**
     * 查询指定行数据
     *
     * @param researchInfoArticle 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ResearchInfoArticle> queryAllByLimit(ResearchInfoArticle researchInfoArticle, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param researchInfoArticle 查询条件
     * @return 总行数
     */
    long count(ResearchInfoArticle researchInfoArticle);

    /**
     * 新增数据
     *
     * @param researchInfoArticle 实例对象
     * @return 影响行数
     */
    int insert(ResearchInfoArticle researchInfoArticle);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ResearchInfoArticle> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ResearchInfoArticle> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ResearchInfoArticle> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ResearchInfoArticle> entities);

    /**
     * 修改数据
     *
     * @param researchInfoArticle 实例对象
     * @return 影响行数
     */
    int update(ResearchInfoArticle researchInfoArticle);

    /**
     * 通过主键删除数据
     *
     * @param researchId 主键
     * @return 影响行数
     */
    int deleteById(Long researchId);

}

