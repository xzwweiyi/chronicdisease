package com.dchealth.cro.research.mapper;

import com.dchealth.cro.research.domain.ResearchUserRole;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface ResearchUserRoleMapper {


    /**
     * 查询用户和角色关联列表
     *
     * @param researchUserRole 用户和角色关联
     * @return 用户和角色关联集合
     */
    public List<ResearchUserRole> selectResearchUserRoleList(ResearchUserRole researchUserRole);

    public List<ResearchUserRole> selectListGroupByUserIdAndResearchId(ResearchUserRole researchUserRole);

    List<ResearchUserRole> findUserRoleByResearchId(Long researchId);

}
