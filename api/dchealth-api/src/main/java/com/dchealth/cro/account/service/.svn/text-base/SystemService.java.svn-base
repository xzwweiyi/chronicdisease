package com.dchealth.cro.account.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.cro.account.vo.AppVersionVO;
import com.dchealth.cro.base.mapper.AppBannerMapper;
import com.dchealth.cro.base.mapper.AppDoctorMapper;
import com.dchealth.cro.base.mapper.AppVersionMapper;
import com.dchealth.cro.base.vo.AppDoctorIndexVO;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.core.domain.SysArea;
import com.dchealth.cro.core.mapper.SysAreaMapper;
import com.dchealth.cro.core.vo.AreaVO;

@Service
public class SystemService {
	private static final Logger L = LoggerFactory.getLogger(SystemService.class);
	@Autowired
	private AppVersionMapper appVersionMapper;
	@Autowired
	private AppBannerMapper appBannerMapper;
	@Autowired
	private AppDoctorMapper appDoctorMapper;
	@Autowired
	private SysAreaMapper sysAreaMapper;

	/**
	 * 查询最新APP版本信息
	 * 
	 * @return
	 */
	public AppVersionVO findLastAppVersion(String appType, String appVersion, String tenantId,String positionType) {
		AppVersionVO appVersionVO = appVersionMapper.selectAppLastVersion(appType, tenantId,positionType);
		if (appVersionVO != null) {
			String version = appVersionVO.getAppVersion().toUpperCase().replace(".", "").replace("V", "");
			if (!appVersion.equals(version)) {
				appVersionVO.setIsUpdate(true);
			}
		}
		return appVersionVO;
	}

	/**
	 * 查询地区信息
	 * 
	 * @return
	 */
	public List<AreaVO> findAreaList(Integer parentId) {
		List<AreaVO> result = new ArrayList<AreaVO>();
		SysArea sysAreaSerarch = new SysArea();
		sysAreaSerarch.setParentId(parentId);
		List<SysArea> list = sysAreaMapper.selectSysAreaList(sysAreaSerarch);
		if (list != null && list.size() > 0) {
			for (SysArea sysArea : list) {
				AreaVO  areaVO  = new AreaVO();
				areaVO.setAreaId(sysArea.getAreaId());
				areaVO.setAreaName(sysArea.getAreaName());
				areaVO.setLevel(sysArea.getLevel());
				areaVO.setParentId(sysArea.getParentId());
				result.add(areaVO);
			}
		}
		return result;
	}



	/**
	 * 查询医生端APP首页
	 * 
	 * @return
	 */
	public DchealthCloudResponseEntity<AppDoctorIndexVO> findAppIndex(String tenantId) {
		DchealthCloudResponseEntity<AppDoctorIndexVO> result = new DchealthCloudResponseEntity<AppDoctorIndexVO>();
		AppDoctorIndexVO appDoctorIndexVO = new AppDoctorIndexVO();
		try {
			appDoctorIndexVO.setBanners(appBannerMapper.selectAppBanner(tenantId, 20,"1"));
//				List<DoctorIndexVO> doctors = appDoctorMapper.selectAppDoctor(tenantId, 100);
//				List<DoctorIndexVO> doctorList = new ArrayList<DoctorIndexVO>();
//				List<DictVO> dictDatas = sysDictDataService.getDictDatas(DictConstant.TITLE);
//				for (DoctorIndexVO doctorIndexVO : doctors) {
//					doctorIndexVO.setTitle(sysDictDataService.getDictValue(doctorIndexVO.getTitle(), dictDatas));
//					doctorList.add(doctorIndexVO);
//				}
//				appDoctorIndexVO.setDoctors(doctorList);
				appDoctorIndexVO.setArticles(appDoctorMapper.selectMobileArticle(tenantId, 10,"1",""));
//			}
			result.setData(appDoctorIndexVO);
		} catch (Exception e) {
			L.error("查询APP首页异常:{}", e);
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
		return result;
	}




}
