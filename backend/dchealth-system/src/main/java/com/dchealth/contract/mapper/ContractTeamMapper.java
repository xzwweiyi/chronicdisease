package com.dchealth.contract.mapper;

import java.util.List;
import com.dchealth.contract.domain.ContractTeam;

public interface ContractTeamMapper {
    List<ContractTeam> selectContractTeamList(ContractTeam entity);
    ContractTeam selectContractTeamById(Long id);
    int insertContractTeam(ContractTeam entity);
    int updateContractTeam(ContractTeam entity);
    int deleteContractTeamById(Long id);
    int deleteContractTeamByIds(Long[] ids);
}
