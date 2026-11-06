package com.dchealth.research.service.impl;

import com.dchealth.research.domain.ResearchRoleSource;
import com.dchealth.research.domain.ResearchSource;
import com.dchealth.research.domain.ResearchUserRole;
import com.dchealth.research.domain.vo.ResearchIdsVo;
import com.dchealth.research.mapper.ResearchRoleSourceMapper;
import com.dchealth.research.mapper.ResearchSourceMapper;
import com.dchealth.research.mapper.ResearchUserRoleMapper;
import com.dchealth.research.service.IResearchSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResearchSourceServiceImpl implements IResearchSourceService {

    @Autowired
    ResearchSourceMapper researchSourceMapper;

    @Autowired
    ResearchRoleSourceMapper researchRoleSourceMapper;

    @Autowired
    ResearchUserRoleMapper researchUserRoleMapper;


    @Override
    public int insertResearchSource(ResearchSource researchSource) {
        return researchSourceMapper.insertResearchSource(researchSource);
    }

    @Override
    public int updateResearchSource(ResearchSource researchSource) {
        return researchSourceMapper.updateResearchSource(researchSource);
    }

    @Override
    public List<ResearchSource> selectResearchSourceList(ResearchSource rs) {
        List<ResearchSource> researchSourceList = researchSourceMapper.selectResearchSourceList(rs);
        List<ResearchSource> sourceList = new ArrayList<>();
        if (researchSourceList.size()>0){
            for (ResearchSource researchSource : researchSourceList) {
                if (researchSource.getParentId() == 0L){
                    ResearchSource source = new ResearchSource();
                    source.setSourceId(researchSource.getSourceId());
                    source.setSourceName(researchSource.getSourceName());
                    source.setParentId(researchSource.getParentId());
                    source.setChild(getChildSource(researchSource.getSourceId(),researchSourceList));
                    sourceList.add(source);
                }
            }
        }
        return sourceList;
    }

    @Override
    public ResearchSource findOneResearchSource(Long sourceId) {
        return researchSourceMapper.findOneResearchSource(sourceId);
    }

    @Override
    public int deleteResearchSource(ResearchSource researchSource) {
        return researchSourceMapper.deleteResearchSource(researchSource.getSourceId());
    }

    @Override
    public boolean isHaveChildResearchSource(ResearchSource researchSource) {
        int number = researchSourceMapper.isHaveChildResearchSource(researchSource.getSourceId());
        return number > 0 ? true : false;

    }

    @Override
    public boolean isDistributionResearchSource(ResearchSource researchSource) {
        int number = researchRoleSourceMapper.isDistributionResearchSource(researchSource.getSourceId());
        return number > 0 ? true : false;
    }

    @Override
    public List<ResearchSource> findResearchSourceByUserId(ResearchIdsVo researchIdsVo) {
        //根据用户id查询该用户所有角色
        List<ResearchUserRole> researchUserRoles = researchUserRoleMapper.findUserRoleByUserId(researchIdsVo.getUserId());
        List<Long> roles = new ArrayList<>();
        if (researchUserRoles.size() > 0) {
            roles = researchUserRoles.stream().map(ResearchUserRole::getRoleId).distinct().collect(Collectors.toList());
        }
        //根据角色下的所有菜单
        List<ResearchRoleSource> researchRoleSources = researchRoleSourceMapper.findByRoles(roles);
        List<Long> sources = new ArrayList<>();
        if (researchRoleSources.size() > 0) {
            sources = researchRoleSources.stream().map(ResearchRoleSource::getSourceId).distinct().collect(Collectors.toList());
        }
        List<ResearchSource> sourceList = new ArrayList<>();
        if (sources.size()>0){
            List<ResearchSource> researchSourceList = researchSourceMapper.findResearchSourceBySourceIds(sources);
            if (researchSourceList.size()>0){
                for (ResearchSource researchSource : researchSourceList) {
                    if (researchSource.getParentId() == 0L){
                        ResearchSource source = new ResearchSource();
                        source.setSourceId(researchSource.getSourceId());
                        source.setSourceName(researchSource.getSourceName());
                        source.setChild(getChildSource(researchSource.getSourceId(),researchSourceList));
                        sourceList.add(source);
                    }
                }
            }
        }
        return sourceList;
    }

    @Override
    public List<Long> findResearchSourceByRoleId(ResearchIdsVo researchIdsVo) {
        //根据角色id查询菜单id
        List<ResearchRoleSource> researchRoleSources = researchRoleSourceMapper.findByRoleId(researchIdsVo.getRoleId());
        List<Long> sources = new ArrayList<>();
        if (researchRoleSources.size() > 0) {
            sources = researchRoleSources.stream().map(ResearchRoleSource::getSourceId).distinct().collect(Collectors.toList());
        }
        /*List<ResearchSource> sourceList = new ArrayList<>();
        if (sources.size()>0){
            List<ResearchSource> researchSourceList = researchSourceMapper.findResearchSourceBySourceIds(sources);
            if (researchSourceList.size()>0){
                for (ResearchSource researchSource : researchSourceList) {
                    if (researchSource.getParentId() == 0L){
                        ResearchSource source = new ResearchSource();
                        source.setSourceId(researchSource.getSourceId());
                        source.setSourceName(researchSource.getSourceName());
                        source.setParentId(researchSource.getParentId());
                        source.setChild(getChildSource(researchSource.getSourceId(),researchSourceList));
                        sourceList.add(source);
                    }
                }
            }
        }*/
        return sources;
    }

    @Override
    public List<ResearchSource> treeResearchSource() {
        List<ResearchSource> researchSourceList= researchSourceMapper.findAllResearchSource();
        List<ResearchSource> sourceList = new ArrayList<>();
        if (researchSourceList.size()>0){
            for (ResearchSource researchSource : researchSourceList) {
                 if (researchSource.getParentId() == 0L){
                     ResearchSource source = new ResearchSource();
                     source.setSourceId(researchSource.getSourceId());
                     source.setSourceName(researchSource.getSourceName());
                     source.setChild(getChildSource(researchSource.getSourceId(),researchSourceList));
                     sourceList.add(source);
                 }
            }
        }
        return sourceList;
    }

    @Override
    public int batchInsertResearchSource(Long roleId, List<Long> sourceIds) {
        if (sourceIds == null || sourceIds.size() == 0){
            return 0;
        }
        int rows = 0;
        // 新增用户与角色管理
        List<ResearchRoleSource> list = new ArrayList<ResearchRoleSource>();
        for (Long sourceId : sourceIds)
        {
            ResearchRoleSource rm = new ResearchRoleSource();
            rm.setRoleId(roleId);
            rm.setSourceId(sourceId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = researchRoleSourceMapper.batchRoleSource(list);
        }
        return rows;
    }

    private List<ResearchSource> getChildSource(Long sourceId, List<ResearchSource> researchSourceList) {
        List<ResearchSource> childrenList = new ArrayList<>();
        for (ResearchSource researchSource : researchSourceList) {
             long parentId = researchSource.getParentId();
            if (sourceId == parentId) {
                ResearchSource treeVO = new ResearchSource();
                treeVO.setSourceId(researchSource.getSourceId());
                treeVO.setSourceName(researchSource.getSourceName());
                treeVO.setParentId(parentId);
                treeVO.setChild(getChildSource(researchSource.getSourceId(), researchSourceList));
                childrenList.add(treeVO);
            }
        }
        return childrenList;
    }
}
