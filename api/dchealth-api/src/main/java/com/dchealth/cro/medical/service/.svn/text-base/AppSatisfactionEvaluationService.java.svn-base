package com.dchealth.cro.medical.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.medical.domain.AppSatisfactionEvaluation;
import com.dchealth.cro.medical.mapper.AppSatisfactionEvaluationMapper;

/**
 * app 满意度评价(AppSatisfactionEvaluation)表服务实现类
 *
 * @author makejava
 * @since 2023-05-26 15:18:10
 */
@Service("appSatisfactionEvaluationService")
public class AppSatisfactionEvaluationService  {
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
    public AppSatisfactionEvaluation insert(AppSatisfactionEvaluation appSatisfactionEvaluation) {
        SysUser sysUser = sysUserMapper.selectSysUserByUserId(appSatisfactionEvaluation.getUserId());
        if(sysUser!=null){
            appSatisfactionEvaluation.setName(sysUser.getUserName());
        }
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
    public boolean deleteById(Integer id) {
        return this.appSatisfactionEvaluationMapper.deleteById(id) > 0;
    }
}
