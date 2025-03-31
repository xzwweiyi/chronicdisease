package com.dchealth.cro.core.mapper;

import com.dchealth.cro.core.domain.AppSet;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 应用设置Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-30
 */
@Repository
public interface AppSetMapper 
{
    /**
     * 查询应用设置
     * 
     * @param id 应用设置ID
     * @return 应用设置
     */
    public AppSet selectAppSetById(Long id);
    public AppSet selectAppSetByConfigKeyAndTenantId(@Param("configKey")String configKey,@Param("tenantId")String tenantId);

    /**
     * 查询应用设置列表
     * 
     * @param appSet 应用设置
     * @return 应用设置集合
     */
    public List<AppSet> selectAppSetList(AppSet appSet);

    /**
     * 新增应用设置
     * 
     * @param appSet 应用设置
     * @return 结果
     */
    public int insertAppSet(AppSet appSet);

    /**
     * 修改应用设置
     * 
     * @param appSet 应用设置
     * @return 结果
     */
    public int updateAppSet(AppSet appSet);

    /**
     * 删除应用设置
     * 
     * @param id 应用设置ID
     * @return 结果
     */
    public int deleteAppSetById(Long id);

    /**
     * 批量删除应用设置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAppSetByIds(String[] ids);
}
