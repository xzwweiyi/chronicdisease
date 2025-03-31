package com.dchealth.medical.service;

import com.dchealth.medical.domain.AppSatisfactionEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * app 满意度评价(AppSatisfactionEvaluation)表服务接口
 *
 * @author makejava
 * @since 2023-05-26 15:19:07
 */
public interface AppSatisfactionEvaluationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppSatisfactionEvaluation queryById(Integer id);

    /**
     * 分页查询
     *
     * @param appSatisfactionEvaluation 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<AppSatisfactionEvaluation> queryByPage(AppSatisfactionEvaluation appSatisfactionEvaluation, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param appSatisfactionEvaluation 实例对象
     * @return 实例对象
     */
    AppSatisfactionEvaluation insert(AppSatisfactionEvaluation appSatisfactionEvaluation);

    /**
     * 修改数据
     *
     * @param appSatisfactionEvaluation 实例对象
     * @return 实例对象
     */
    AppSatisfactionEvaluation update(AppSatisfactionEvaluation appSatisfactionEvaluation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 查询评价列表
     * @param appSatisfactionEvaluation
     * @return
     */
    List<AppSatisfactionEvaluation> selectEvaluationList(AppSatisfactionEvaluation appSatisfactionEvaluation);

    /**
     * 查询满意度综合评分
     * @param appSatisfactionEvaluation
     * @return
     */
    Map queryEvaluation(AppSatisfactionEvaluation appSatisfactionEvaluation);

}
