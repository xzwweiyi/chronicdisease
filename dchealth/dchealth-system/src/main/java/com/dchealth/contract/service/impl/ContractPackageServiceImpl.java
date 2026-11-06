package com.dchealth.contract.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.contract.domain.ContractPackage;
import com.dchealth.contract.mapper.ContractPackageMapper;
import com.dchealth.contract.service.IContractPackageService;

/**
 * 签约服务包Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class ContractPackageServiceImpl implements IContractPackageService
{
    @Autowired
    private ContractPackageMapper contractPackageMapper;

    /**
     * 查询签约服务包列表
     *
     * @param entity 签约服务包
     * @return 签约服务包集合
     */
    @Override
    public List<ContractPackage> selectContractPackageList(ContractPackage entity)
    {
        return contractPackageMapper.selectContractPackageList(entity);
    }

    /**
     * 查询签约服务包
     *
     * @param id 签约服务包主键
     * @return 签约服务包
     */
    @Override
    public ContractPackage selectContractPackageById(Long id)
    {
        return contractPackageMapper.selectContractPackageById(id);
    }

    /**
     * 新增签约服务包
     *
     * @param entity 签约服务包
     * @return 结果
     */
    @Override
    public int insertContractPackage(ContractPackage entity)
    {
        return contractPackageMapper.insertContractPackage(entity);
    }

    /**
     * 修改签约服务包
     *
     * @param entity 签约服务包
     * @return 结果
     */
    @Override
    public int updateContractPackage(ContractPackage entity)
    {
        return contractPackageMapper.updateContractPackage(entity);
    }

    /**
     * 删除签约服务包信息
     *
     * @param id 签约服务包主键
     * @return 结果
     */
    @Override
    public int deleteContractPackageById(Long id)
    {
        return contractPackageMapper.deleteContractPackageById(id);
    }

    /**
     * 批量删除签约服务包
     *
     * @param ids 需要删除的签约服务包主键集合
     * @return 结果
     */
    @Override
    public int deleteContractPackageByIds(Long[] ids)
    {
        return contractPackageMapper.deleteContractPackageByIds(ids);
    }
}
