package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FLibraryFormSetEvaluation;

/**
 * 标准量设置评价结果Mapper接口
 * 
 * @author lchbu
 * @date 2023-04-03
 */
public interface FLibraryFormSetEvaluationMapper 
{
    /**
     * 查询标准量设置评价结果
     * 
     * @param id 标准量设置评价结果主键
     * @return 标准量设置评价结果
     */
    public FLibraryFormSetEvaluation selectFLibraryFormSetEvaluationById(Long id);

    /**
     * 查询标准量设置评价结果列表
     * 
     * @param fLibraryFormSetEvaluation 标准量设置评价结果
     * @return 标准量设置评价结果集合
     */
    public List<FLibraryFormSetEvaluation> selectFLibraryFormSetEvaluationList(FLibraryFormSetEvaluation fLibraryFormSetEvaluation);

    /**
     * 新增标准量设置评价结果
     * 
     * @param fLibraryFormSetEvaluation 标准量设置评价结果
     * @return 结果
     */
    public int insertFLibraryFormSetEvaluation(FLibraryFormSetEvaluation fLibraryFormSetEvaluation);

    /**
     * 修改标准量设置评价结果
     * 
     * @param fLibraryFormSetEvaluation 标准量设置评价结果
     * @return 结果
     */
    public int updateFLibraryFormSetEvaluation(FLibraryFormSetEvaluation fLibraryFormSetEvaluation);

    /**
     * 删除标准量设置评价结果
     * 
     * @param id 标准量设置评价结果主键
     * @return 结果
     */
    public int deleteFLibraryFormSetEvaluationById(Long id);

    /**
     * 批量删除标准量设置评价结果
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFLibraryFormSetEvaluationByIds(Long[] ids);
}
