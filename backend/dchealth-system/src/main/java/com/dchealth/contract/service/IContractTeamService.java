package com.dchealth.contract.service;

import java.util.List;
import com.dchealth.contract.domain.ContractTeam;

/**
 * 签约团队Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IContractTeamService
{
    /**
     * 查询签约团队列表
     *
     * @param entity 签约团队
     * @return 签约团队集合
     */
    public List<ContractTeam> selectContractTeamList(ContractTeam entity);

    /**
     * 查询签约团队
     *
     * @param id 签约团队主键
     * @return 签约团队
     */
    public ContractTeam selectContractTeamById(Long id);

    /**
     * 新增签约团队
     *
     * @param entity 签约团队
     * @return 结果
     */
    public int insertContractTeam(ContractTeam entity);

    /**
     * 修改签约团队
     *
     * @param entity 签约团队
     * @return 结果
     */
    public int updateContractTeam(ContractTeam entity);

    /**
     * 删除签约团队信息
     *
     * @param id 签约团队主键
     * @return 结果
     */
    public int deleteContractTeamById(Long id);

    /**
     * 批量删除签约团队
     *
     * @param ids 需要删除的签约团队主键集合
     * @return 结果
     */
    public int deleteContractTeamByIds(Long[] ids);
}
