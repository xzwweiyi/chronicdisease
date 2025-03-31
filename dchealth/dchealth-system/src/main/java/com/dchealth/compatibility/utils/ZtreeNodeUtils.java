package com.dchealth.compatibility.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.grouping.CollectedSearchGroup;
import org.springframework.util.CollectionUtils;

import com.dchealth.compatibility.constants.HealthConstant;
import com.dchealth.compatibility.vo.HealthNodeVO;
import com.dchealth.compatibility.vo.VisitListVO;

public class ZtreeNodeUtils {
	/**
	 * 根节点
	 * @param type
	 * @param personId
	 * @return
	 */
	public static HealthNodeVO  getRootNode(String type,String personId) {
		HealthNodeVO  healthNodeVO  = new HealthNodeVO();
		healthNodeVO.setId(0);
		healthNodeVO.setNodeId("0");
		healthNodeVO.setParentNodeId("0");
		if(HealthConstant.JK.equals(type)) {
			healthNodeVO.setNodeName("住院档案");
		}else {
			healthNodeVO.setNodeName("体检档案");
		}
		healthNodeVO.setLevel(0);
		healthNodeVO.setPersonId(personId);
		healthNodeVO.setType(0);
		return healthNodeVO;
	}
	
	/**
	 * 节点至
	 * @param rootNode
	 * @param list
	 * @param type
	 * @return
	 */
	public static  HealthNodeVO getVisitNode(HealthNodeVO rootNode  ,List<VisitListVO>  list,String type) {
		 int i = 1;
		if(!CollectionUtils.isEmpty(list)) {
			//就诊记录
			List<HealthNodeVO> childrenList = new ArrayList<HealthNodeVO>();
			for (VisitListVO visitListVO : list) {
				HealthNodeVO  healthNodeVO  = new HealthNodeVO();
				healthNodeVO.setId(i);
				i++;
				healthNodeVO.setNodeId("1");
				healthNodeVO.setParentNodeId("0");
				healthNodeVO.setNodeName(visitListVO.getCheckDate());
				healthNodeVO.setLevel(1);
				healthNodeVO.setPersonId(rootNode.getPersonId());
				healthNodeVO.setVisitId(visitListVO.getVisitId());
				//就诊 基本信息
				List<HealthNodeVO> childrenList2 = new ArrayList<HealthNodeVO>();
				Map<Integer, String> level2Map = level2Item(type);
				level2Map.forEach((k, v) -> {
				HealthNodeVO  healthNodeVO2  = new HealthNodeVO();
				healthNodeVO2.setId(k);
				healthNodeVO2.setNodeId("2");
				healthNodeVO2.setNodeName(v);
				healthNodeVO2.setParentNodeId("1");
				healthNodeVO2.setLevel(2);
				healthNodeVO2.setType(k);
				healthNodeVO2.setPersonId(visitListVO.getPersonId());
				healthNodeVO2.setVisitId(visitListVO.getVisitId());
				childrenList2.add(healthNodeVO2);
				});
	             healthNodeVO.setChildrenList(childrenList2);
				childrenList.add(healthNodeVO);
				rootNode.setChildrenList(childrenList);//就诊记录
			}
		}
		return rootNode;
	}
	
	private static  Map<Integer, String> level2Item(String type) {
		Map<Integer, String> map = new HashMap<>();
//	    map.put(1, "基本信息");
		map.put(2, "一般检查");
		if ("1".equals(type)) {
			map.put(3, "诊断");
		}
		map.put(4, "检查");
		map.put(5, "检验");
		return map;
	}
	 
}
