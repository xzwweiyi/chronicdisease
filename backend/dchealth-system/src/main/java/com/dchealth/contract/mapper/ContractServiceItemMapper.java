package com.dchealth.contract.mapper;

import java.util.List;
import com.dchealth.contract.domain.ContractServiceItem;

public interface ContractServiceItemMapper {
    List<ContractServiceItem> selectContractServiceItemList(ContractServiceItem entity);
    ContractServiceItem selectContractServiceItemById(Long id);
    int insertContractServiceItem(ContractServiceItem entity);
    int updateContractServiceItem(ContractServiceItem entity);
    int deleteContractServiceItemById(Long id);
    int deleteContractServiceItemByIds(Long[] ids);
}
