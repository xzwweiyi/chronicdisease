package com.dchealth.cro.research.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;

/**
 * 研究项目Service业务层处理
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-18
 */
@Service
public class ResearchInfoService {
	@Autowired
	private ResearchInfoMapper researchInfoMapper;

	
	/**
	 * 1 入排标准
	 * 2 项目介绍
	 * 3 知情同意书 
	 * @param researchId
	 * @return
	 */
	public DchealthCloudResponseEntity<String> detailContent(Long researchId) {
		DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
		ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
		if (researchInfo != null ) {
			result.setData(researchInfo.getResearchSynopsis());
			return result;
		}

		/*if (researchInfo != null && "2".equals(type)) {
			result.setData(researchInfo.getResearchIntro());
			return result;
		}
		if (researchInfo != null && "3".equals(type)) {
			result.setData(researchInfo.getResearchAgree());
			return result;
		}
		if (researchInfo != null && "4".equals(type)) {
			result.setData(researchInfo.getResearchProcess());
			return result;
		}*/
		result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
		return result;
	}
	
	
	/**
	 * 计算完成度
	 * 
	 * @param totalNum
	 *            总数 分母
	 * @param submitNum
	 *            提交数 分子
	 * @return
	 */
	private String calProgress(BigDecimal totalNum, BigDecimal submitNum) {

		if (totalNum != null && submitNum != null && submitNum.intValue() > 0 && totalNum.intValue() > 0) {
			BigDecimal parentBigDecimal = submitNum.multiply(new BigDecimal(100));
			BigDecimal progressFinish = parentBigDecimal.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
			DecimalFormat df = new DecimalFormat("#");
			return df.format(progressFinish);
		}
		return "0";
	}



}
