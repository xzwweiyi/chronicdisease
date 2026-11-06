package com.dchealth.contract.service;

import java.util.List;
import com.dchealth.contract.domain.ContractServiceItem;

/**
 * 签约服务项目Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IContractServiceItemService
{
    /**
     * 查询签约服务项目列表
     *
     * @param entity 签约服务项目
     * @return 签约服务项目集合
     */
    public List<ContractServiceItem> selectContractServiceItemList(ContractServiceItem entity);

    /**
     * 查询签约服务项目
     *
     * @param id 签约服务项目主键
     * @return 签约服务项目
     */
    public ContractServiceItem selectContractServiceItemById(Long id);

    /**
     * 新增签约服务项目
     *
     * @param entity 签约服务项目
     * @return 结果
     */
    public int insertContractServiceItem(ContractServiceItem entity);

    /**
     * 修改签约服务项目
     *
     * @param entity 签约服务项目
     * @return 结果
     */
    public int updateContractServiceItem(ContractServiceItem entity);

    /**
     * 删除签约服务项目信息
     *
     * @param id 签约服务项目主键
     * @return 结果
     */
    public int deleteContractServiceItemById(Long id);

    /**
     * 批量删除签约服务项目
     *
     * @param ids 需要删除的签约服务项目主键集合
     * @return 结果
     */
    public int deleteContractServiceItemByIds(Long[] ids);
}
