package com.dchealth.medical.service.impl;

import com.dchealth.common.utils.StringUtils;
import com.dchealth.medical.domain.AppSatisfactionEvaluation;
import com.dchealth.medical.mapper.AppSatisfactionEvaluationMapper;
import com.dchealth.medical.service.AppSatisfactionEvaluationService;
import com.dchealth.system.mapper.SysUserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * app 满意度评价(AppSatisfactionEvaluation)表服务实现类
 *
 * @author makejava
 * @since 2023-05-26 15:18:10
 */
@Service("appSatisfactionEvaluationService")
public class AppSatisfactionEvaluationServiceImpl implements AppSatisfactionEvaluationService {
    @Resource
    private AppSatisfactionEvaluationMapper appSatisfactionEvaluationMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppSatisfactionEvaluation queryById(Integer id) {
        return this.appSatisfactionEvaluationMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param appSatisfactionEvaluation 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<AppSatisfactionEvaluation> queryByPage(AppSatisfactionEvaluation appSatisfactionEvaluation, PageRequest pageRequest) {
        long total = this.appSatisfactionEvaluationMapper.count(appSatisfactionEvaluation);
        return new PageImpl<>(this.appSatisfactionEvaluationMapper.queryAllByLimit(appSatisfactionEvaluation, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param appSatisfactionEvaluation 实例对象
     * @return 实例对象
     */
    @Override
    public AppSatisfactionEvaluation insert(AppSatisfactionEvaluation appSatisfactionEvaluation) {
        appSatisfactionEvaluation.setCreateTime(new Date());
        this.appSatisfactionEvaluationMapper.insert(appSatisfactionEvaluation);
        return appSatisfactionEvaluation;
    }

    /**
     * 修改数据
     *
     * @param appSatisfactionEvaluation 实例对象
     * @return 实例对象
     */
    @Override
    public AppSatisfactionEvaluation update(AppSatisfactionEvaluation appSatisfactionEvaluation) {
        this.appSatisfactionEvaluationMapper.update(appSatisfactionEvaluation);
        return this.queryById(appSatisfactionEvaluation.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.appSatisfactionEvaluationMapper.deleteById(id) > 0;
    }

    /**
     * 查询评价列表
     * @param appSatisfactionEvaluation
     * @return
     */
    @Override
    public List<AppSatisfactionEvaluation> selectEvaluationList(AppSatisfactionEvaluation appSatisfactionEvaluation) {
        return appSatisfactionEvaluationMapper.selectEvaluationList(appSatisfactionEvaluation);
    }

    @Override
    public Map queryEvaluation(AppSatisfactionEvaluation appSatisfactionEvaluation) {

        return appSatisfactionEvaluationMapper.queryEvaluation(appSatisfactionEvaluation);
    }
}
