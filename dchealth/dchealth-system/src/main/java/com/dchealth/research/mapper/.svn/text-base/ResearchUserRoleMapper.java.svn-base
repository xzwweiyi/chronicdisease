package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchUserRoleMapper {

    void deleteUserAuth(Long userId);

    int batchUserRole(@Param("researchUserRoles") List<ResearchUserRole> researchUserRoles);

    int findUserRoleNumberByRoleId(Long roleId);

    List<ResearchUserRole> findUserRoleByUserId(Long userId);

    int cancelAuthRole(@Param("userId") Long userId, @Param("researchId")Long researchId);

    int batchCancelAuthRole(@Param("researchId") Long researchId, @Param("userIds") Long[] userIds);

    List<ResearchUserRole> findUserRoleByRoleId(Long roleId);
    List<ResearchUserRole> findUserRoleByResearchId(Long researchId);


    List<ResearchUserRole> findUserRoleGroupByUserId(Long researchId);

    /**
     * 查询用户和角色关联列表
     *
     * @param researchUserRole 用户和角色关联
     * @return 用户和角色关联集合
     */
    public List<ResearchUserRole> selectResearchUserRoleList(ResearchUserRole researchUserRole);

    public List<ResearchUserRole> selectListGroupByUserIdAndResearchId(ResearchUserRole researchUserRole);

    public int insertResearchUserRole(ResearchUserRole researchUserRole);
}
