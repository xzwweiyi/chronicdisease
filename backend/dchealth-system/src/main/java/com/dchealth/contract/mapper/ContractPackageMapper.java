package com.dchealth.contract.mapper;

import java.util.List;
import com.dchealth.contract.domain.ContractPackage;

public interface ContractPackageMapper {
    List<ContractPackage> selectContractPackageList(ContractPackage entity);
    ContractPackage selectContractPackageById(Long id);
    int insertContractPackage(ContractPackage entity);
    int updateContractPackage(ContractPackage entity);
    int deleteContractPackageById(Long id);
    int deleteContractPackageByIds(Long[] ids);
}
