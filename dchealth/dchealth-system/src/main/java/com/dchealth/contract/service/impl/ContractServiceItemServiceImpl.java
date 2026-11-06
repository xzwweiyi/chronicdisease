package com.dchealth.contract.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.contract.domain.ContractServiceItem;
import com.dchealth.contract.mapper.ContractServiceItemMapper;
import com.dchealth.contract.service.IContractServiceItemService;

/**
 * 签约服务项目Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class ContractServiceItemServiceImpl implements IContractServiceItemService
{
    @Autowired
    private ContractServiceItemMapper contractServiceItemMapper;

    /**
     * 查询签约服务项目列表
     *
     * @param entity 签约服务项目
     * @return 签约服务项目集合
     */
    @Override
    public List<ContractServiceItem> selectContractServiceItemList(ContractServiceItem entity)
    {
        return contractServiceItemMapper.selectContractServiceItemList(entity);
    }

    /**
     * 查询签约服务项目
     *
     * @param id 签约服务项目主键
     * @return 签约服务项目
     */
    @Override
    public ContractServiceItem selectContractServiceItemById(Long id)
    {
        return contractServiceItemMapper.selectContractServiceItemById(id);
    }

    /**
     * 新增签约服务项目
     *
     * @param entity 签约服务项目
     * @return 结果
     */
    @Override
    public int insertContractServiceItem(ContractServiceItem entity)
    {
        return contractServiceItemMapper.insertContractServiceItem(entity);
    }

    /**
     * 修改签约服务项目
     *
     * @param entity 签约服务项目
     * @return 结果
     */
    @Override
    public int updateContractServiceItem(ContractServiceItem entity)
    {
        return contractServiceItemMapper.updateContractServiceItem(entity);
    }

    /**
     * 删除签约服务项目信息
     *
     * @param id 签约服务项目主键
     * @return 结果
     */
    @Override
    public int deleteContractServiceItemById(Long id)
    {
        return contractServiceItemMapper.deleteContractServiceItemById(id);
    }

    /**
     * 批量删除签约服务项目
     *
     * @param ids 需要删除的签约服务项目主键集合
     * @return 结果
     */
    @Override
    public int deleteContractServiceItemByIds(Long[] ids)
    {
        return contractServiceItemMapper.deleteContractServiceItemByIds(ids);
    }
}
