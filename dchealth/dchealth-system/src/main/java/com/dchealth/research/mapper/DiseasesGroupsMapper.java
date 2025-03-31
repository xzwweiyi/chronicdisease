package com.dchealth.research.mapper;

import com.dchealth.research.domain.DiseasesGroups;

import java.util.List;

public interface DiseasesGroupsMapper {

    List<DiseasesGroups> selectDiseasesGroupsList(DiseasesGroups groups);

    int insertDiseasesGroups(DiseasesGroups groups);

    int updateDiseasesGroups(DiseasesGroups groups);

    int deleteDiseasesGroupsByDiseasesGroupsId(Long diseasesGroupsId);

    List<DiseasesGroups> selectDiseasesGroupsListPage(DiseasesGroups groups);

    public DiseasesGroups selectBydiseasesGroupsName(String diseasesGroupsName);

}
