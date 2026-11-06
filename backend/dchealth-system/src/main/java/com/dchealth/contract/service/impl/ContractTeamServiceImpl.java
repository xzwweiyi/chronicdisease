package com.dchealth.contract.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.contract.domain.ContractTeam;
import com.dchealth.contract.mapper.ContractTeamMapper;
import com.dchealth.contract.service.IContractTeamService;

/**
 * 签约团队Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class ContractTeamServiceImpl implements IContractTeamService
{
    @Autowired
    private ContractTeamMapper contractTeamMapper;

    /**
     * 查询签约团队列表
     *
     * @param entity 签约团队
     * @return 签约团队集合
     */
    @Override
    public List<ContractTeam> selectContractTeamList(ContractTeam entity)
    {
        return contractTeamMapper.selectContractTeamList(entity);
    }

    /**
     * 查询签约团队
     *
     * @param id 签约团队主键
     * @return 签约团队
     */
    @Override
    public ContractTeam selectContractTeamById(Long id)
    {
        return contractTeamMapper.selectContractTeamById(id);
    }

    /**
     * 新增签约团队
     *
     * @param entity 签约团队
     * @return 结果
     */
    @Override
    public int insertContractTeam(ContractTeam entity)
    {
        return contractTeamMapper.insertContractTeam(entity);
    }

    /**
     * 修改签约团队
     *
     * @param entity 签约团队
     * @return 结果
     */
    @Override
    public int updateContractTeam(ContractTeam entity)
    {
        return contractTeamMapper.updateContractTeam(entity);
    }

    /**
     * 删除签约团队信息
     *
     * @param id 签约团队主键
     * @return 结果
     */
    @Override
    public int deleteContractTeamById(Long id)
    {
        return contractTeamMapper.deleteContractTeamById(id);
    }

    /**
     * 批量删除签约团队
     *
     * @param ids 需要删除的签约团队主键集合
     * @return 结果
     */
    @Override
    public int deleteContractTeamByIds(Long[] ids)
    {
        return contractTeamMapper.deleteContractTeamByIds(ids);
    }
}
