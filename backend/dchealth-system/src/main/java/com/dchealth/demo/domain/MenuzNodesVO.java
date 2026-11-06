package com.dchealth.demo.domain;

public class MenuzNodesVO {
	
	private String id;
	private String pId;
	private String name;
	private String nodetype;
	private String businessid;
	private String patientId;
	private String templateType;
	private String patientRecordId;//档案id

	//是否是患者基本信息节点，0否1是
	private String isPatientNode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodetype() {
		return nodetype;
	}

	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}

	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getPatientRecordId() {
		return patientRecordId;
	}

	public void setPatientRecordId(String patientRecordId) {
		this.patientRecordId = patientRecordId;
	}

	public String getIsPatientNode() {
		return isPatientNode;
	}

	public void setIsPatientNode(String isPatientNode) {
		this.isPatientNode = isPatientNode;
	}
}
