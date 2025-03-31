package com.dchealth.cms.domain;

import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.common.core.domain.entity.SysMenu;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 
 * @author ruoyi
 */
public class DeptTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;


    private boolean isHighLight;


    private  List<CmsCategoryTree> cmsCategoryTreeList;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptTreeSelect> children;

    public List<CmsCategoryTree> getCmsCategoryTreeList() {
        return cmsCategoryTreeList;
    }

    public void setCmsCategoryTreeList(List<CmsCategoryTree> cmsCategoryTreeList) {
        this.cmsCategoryTreeList = cmsCategoryTreeList;
    }

    public boolean isHighLight() {
        return isHighLight;
    }

    public void setHighLight(boolean highLight) {
        isHighLight = highLight;
    }

    public DeptTreeSelect()
    {

    }

    public DeptTreeSelect(SysDept dept)
    {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.isHighLight = dept.getHighLight();
        this.children = dept.getChildren().stream().map(DeptTreeSelect::new).collect(Collectors.toList());
    }

    public DeptTreeSelect(SysMenu menu)
    {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(DeptTreeSelect::new).collect(Collectors.toList());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<DeptTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<DeptTreeSelect> children)
    {
        this.children = children;
    }
}
