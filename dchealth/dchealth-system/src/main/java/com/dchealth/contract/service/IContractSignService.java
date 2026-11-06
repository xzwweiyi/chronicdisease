package com.dchealth.contract.service;

import java.util.List;
import java.util.Map;
import com.dchealth.contract.domain.ContractSign;
import com.dchealth.contract.domain.vo.ContractSignVO;

/**
 * 签约记录Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IContractSignService
{
    /**
     * 查询签约记录列表
     *
     * @param entity 签约记录
     * @return 签约记录集合
     */
    public List<ContractSign> selectContractSignList(ContractSign entity);

    /**
     * 查询签约记录
     *
     * @param id 签约记录主键
     * @return 签约记录
     */
    public ContractSign selectContractSignById(Long id);

    /**
     * 新增签约记录
     *
     * @param entity 签约记录
     * @return 结果
     */
    public int insertContractSign(ContractSign entity);

    /**
     * 修改签约记录
     *
     * @param entity 签约记录
     * @return 结果
     */
    public int updateContractSign(ContractSign entity);

    /**
     * 删除签约记录信息
     *
     * @param id 签约记录主键
     * @return 结果
     */
    public int deleteContractSignById(Long id);

    /**
     * 批量删除签约记录
     *
     * @param ids 需要删除的签约记录主键集合
     * @return 结果
     */
    public int deleteContractSignByIds(Long[] ids);

    /**
     * 查询签约记录VO列表（关联居民档案+团队+服务包）
     */
    public List<ContractSignVO> selectContractSignVOList(Map<String, Object> params);

    /**
     * 查询签约记录VO详情
     */
    public ContractSignVO selectContractSignVOById(Long id);

    /**
     * 管理机构列表
     */
    public List<String> selectDistinctInstitutions();

    /**
     * 管理医生列表
     */
    public List<String> selectDistinctDoctors();
}
