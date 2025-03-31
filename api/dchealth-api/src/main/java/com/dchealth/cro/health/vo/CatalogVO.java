package com.dchealth.cro.health.vo;

import java.util.List;

import lombok.Data;

/**
 * 诊断记录对象 mp_diagnosis
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class CatalogVO {

	private List<ItemValueVO> itemList;
	private String itemTitle;

  
}
