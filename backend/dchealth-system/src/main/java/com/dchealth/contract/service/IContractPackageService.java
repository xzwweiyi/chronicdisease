package com.dchealth.contract.service;

import java.util.List;
import com.dchealth.contract.domain.ContractPackage;

/**
 * 签约服务包Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IContractPackageService
{
    /**
     * 查询签约服务包列表
     *
     * @param entity 签约服务包
     * @return 签约服务包集合
     */
    public List<ContractPackage> selectContractPackageList(ContractPackage entity);

    /**
     * 查询签约服务包
     *
     * @param id 签约服务包主键
     * @return 签约服务包
     */
    public ContractPackage selectContractPackageById(Long id);

    /**
     * 新增签约服务包
     *
     * @param entity 签约服务包
     * @return 结果
     */
    public int insertContractPackage(ContractPackage entity);

    /**
     * 修改签约服务包
     *
     * @param entity 签约服务包
     * @return 结果
     */
    public int updateContractPackage(ContractPackage entity);

    /**
     * 删除签约服务包信息
     *
     * @param id 签约服务包主键
     * @return 结果
     */
    public int deleteContractPackageById(Long id);

    /**
     * 批量删除签约服务包
     *
     * @param ids 需要删除的签约服务包主键集合
     * @return 结果
     */
    public int deleteContractPackageByIds(Long[] ids);
}
