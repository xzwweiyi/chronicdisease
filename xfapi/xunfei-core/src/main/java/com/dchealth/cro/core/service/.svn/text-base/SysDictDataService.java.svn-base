package com.dchealth.cro.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.contants.DictConstant;
import com.dchealth.cro.core.domain.SysDictData;
import com.dchealth.cro.core.mapper.HospitalMapper;
import com.dchealth.cro.core.mapper.SysDictDataMapper;
import com.dchealth.cro.core.vo.DictVO;

@Service
public class SysDictDataService {
	@Autowired
	private  SysDictDataMapper  sysDictDataMapper;
	
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private HospitalMapper hospitalMapper;


	/**
	 * 获取字典选项
	 * 
	 * @param dictType
	 * @return
	 */
	public List<DictVO> getDictVO(String dictType) {
		List<DictVO> result = new ArrayList<DictVO>();
		String cacheValue = redisCache.getCacheString(RedisConstants.DICT_DATA_PREFIX + dictType);
		if (StringUtils.isNotNullOrEmpty(cacheValue)) {
			result = JSON.parseArray(cacheValue, DictVO.class);
		} else {
			List<SysDictData> list = sysDictDataMapper.selectDictDataByType(dictType);
			if (list != null && list.size() > 0) {
				for (SysDictData sysDictData : list) {
					DictVO dictVO = new DictVO();
					dictVO.setDictKey(sysDictData.getDictValue());
					dictVO.setDictValue(sysDictData.getDictLabel());
					result.add(dictVO);
				}
				redisCache.cacheString(RedisConstants.DICT_DATA_PREFIX + dictType, JSON.toJSONString(result));
			}
		}

		return result;
	}
	
	/**
	 * 获取字典值
	 * 
	 * @param dictType
	 * @return
	 */
	public String getDictValue(String dictType,String dictKey) {
		if(StringUtils.isNotNullOrEmpty(dictKey)&&StringUtils.isNotNullOrEmpty(dictType)) {
			List<DictVO> result = getDictDatas(dictType);
			for (DictVO dictVO : result) {
				if(dictKey.equals(dictVO.getDictKey())) {
					return dictVO.getDictValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取字典值
	 * 
	 * @param dictType
	 * @return
	 */
	public String getDictValue(String dictKey,List<DictVO> result) {
		if(StringUtils.isNotNullOrEmpty(dictKey)) {
			for (DictVO dictVO : result) {
				if(dictKey.equals(dictVO.getDictKey())) {
					return dictVO.getDictValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * 取出字典数据
	 * @param dictType
	 * @return
	 */
	public List<DictVO> getDictDatas(String dictType){
		List<DictVO> result = new ArrayList<DictVO>();
		if(StringUtils.isNotNullOrEmpty(dictType)) {
			String cacheValue = redisCache.getCacheString(RedisConstants.DICT_DATA_PREFIX + dictType);
			if (StringUtils.isNotNullOrEmpty(cacheValue)) {
				result = JSON.parseArray(cacheValue, DictVO.class);
			} else {
				List<SysDictData> list = sysDictDataMapper.selectDictDataByType(dictType);
				if (list != null && list.size() > 0) {
					for (SysDictData sysDictData : list) {
						DictVO dictVO = new DictVO();
						dictVO.setDictKey(sysDictData.getDictValue());
						dictVO.setDictValue(sysDictData.getDictLabel());
						result.add(dictVO);
					}
					redisCache.cacheString(RedisConstants.DICT_DATA_PREFIX + dictType, JSON.toJSONString(result));
				}
			}
			
		}
		return result;
	}
	
	

	/**
	 * 获取医院科室
	 * 
	 * @param dictType
	 * @return
	 */
	public List<DictVO> getHospitaDepartmentDictVO(String tenantId) {
		List<DictVO> result = new ArrayList<DictVO>();
		String cacheValue = redisCache
				.getCacheString(RedisConstants.DICT_DATA_PREFIX + tenantId + ":" + DictConstant.HOSPITA_DEPARTMENT);
		if (StringUtils.isNotNullOrEmpty(cacheValue)) {
			result = JSON.parseArray(cacheValue, DictVO.class);
		} else {
			result = hospitalMapper.selectHospitalDepartment(tenantId);
			if (result != null && result.size() > 0) {
				redisCache.cacheString(
						RedisConstants.DICT_DATA_PREFIX + tenantId + ":" + DictConstant.HOSPITA_DEPARTMENT,
						JSON.toJSONString(result));
			}
		}

		return result;
	}
	
	
}
