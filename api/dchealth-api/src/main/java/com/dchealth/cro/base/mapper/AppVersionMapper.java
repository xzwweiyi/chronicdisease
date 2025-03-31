package com.dchealth.cro.base.mapper;

import com.dchealth.cro.account.vo.AppVersionVO;
import com.dchealth.cro.base.domain.AppVersion;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * app版本Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-03
 */
@Repository
public interface AppVersionMapper {
	/**
	 * 查询app版本
	 * 
	 * @param id app版本ID
	 * @return app版本
	 */
	public AppVersion selectAppVersionById(Integer id);

	/**
	 * 查询app版本列表
	 * 
	 * @param appVersion app版本
	 * @return app版本集合
	 */
	public List<AppVersion> selectAppVersionList(AppVersion appVersion);

	/**
	 * 新增app版本
	 * 
	 * @param appVersion app版本
	 * @return 结果
	 */
	public int insertAppVersion(AppVersion appVersion);

	/**
	 * 修改app版本
	 * 
	 * @param appVersion app版本
	 * @return 结果
	 */
	public int updateAppVersion(AppVersion appVersion);

	/**
	 * 删除app版本
	 * 
	 * @param id app版本ID
	 * @return 结果
	 */
	public int deleteAppVersionById(Integer id);

	/**
	 * 批量删除app版本
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteAppVersionByIds(String[] ids);

	public AppVersionVO selectAppLastVersion(@Param("appType") String appType, @Param("tenantId") String tenantId, @Param("positionType") String positionType);
}
