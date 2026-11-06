package com.dchealth.contract.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.contract.domain.ContractSign;
import com.dchealth.contract.domain.vo.ContractSignVO;

public interface ContractSignMapper {
    List<ContractSign> selectContractSignList(ContractSign entity);
    ContractSign selectContractSignById(Long id);
    int insertContractSign(ContractSign entity);
    int updateContractSign(ContractSign entity);
    int deleteContractSignById(Long id);
    int deleteContractSignByIds(Long[] ids);

    /** 签约列表 VO（关联居民档案+团队+服务包） */
    List<ContractSignVO> selectContractSignVOList(Map<String, Object> params);
    /** 签约详情 VO */
    ContractSignVO selectContractSignVOById(Long id);

    /** 管理机构下拉 */
    List<String> selectDistinctInstitutions();
    /** 管理医生下拉 */
    List<String> selectDistinctDoctors();
}
