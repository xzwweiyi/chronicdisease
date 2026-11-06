package com.dchealth.contract.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.contract.domain.ContractSign;
import com.dchealth.contract.domain.vo.ContractSignVO;
import com.dchealth.contract.mapper.ContractSignMapper;
import com.dchealth.contract.service.IContractSignService;

/**
 * 签约记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class ContractSignServiceImpl implements IContractSignService
{
    @Autowired
    private ContractSignMapper contractSignMapper;

    @Override
    public List<ContractSign> selectContractSignList(ContractSign entity) {
        return contractSignMapper.selectContractSignList(entity);
    }

    @Override
    public ContractSign selectContractSignById(Long id) {
        return contractSignMapper.selectContractSignById(id);
    }

    @Override
    public int insertContractSign(ContractSign entity) {
        return contractSignMapper.insertContractSign(entity);
    }

    @Override
    public int updateContractSign(ContractSign entity) {
        return contractSignMapper.updateContractSign(entity);
    }

    @Override
    public int deleteContractSignById(Long id) {
        return contractSignMapper.deleteContractSignById(id);
    }

    @Override
    public int deleteContractSignByIds(Long[] ids) {
        return contractSignMapper.deleteContractSignByIds(ids);
    }

    @Override
    public List<ContractSignVO> selectContractSignVOList(Map<String, Object> params) {
        return contractSignMapper.selectContractSignVOList(params);
    }

    @Override
    public ContractSignVO selectContractSignVOById(Long id) {
        return contractSignMapper.selectContractSignVOById(id);
    }

    @Override
    public List<String> selectDistinctInstitutions() {
        return contractSignMapper.selectDistinctInstitutions();
    }

    @Override
    public List<String> selectDistinctDoctors() {
        return contractSignMapper.selectDistinctDoctors();
    }
}
