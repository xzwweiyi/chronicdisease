package com.dchealth.cro.base.mapper;

import com.dchealth.cro.base.domain.AppDoctor;
import com.dchealth.cro.base.vo.DoctorIndexVO;
import com.dchealth.cro.cms.vo.ArticleListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * APP首页专家Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-17
 */
@Repository
public interface AppDoctorMapper 
{
    /**
     * 查询APP首页专家
     * 
     * @param id APP首页专家ID
     * @return APP首页专家
     */
    public AppDoctor selectAppDoctorById(Long id);

    /**
     * 查询APP首页专家列表
     * 
     * @param appDoctor APP首页专家
     * @return APP首页专家集合
     */
    public List<AppDoctor> selectAppDoctorList(AppDoctor appDoctor);

    /**
     * 新增APP首页专家
     * 
     * @param appDoctor APP首页专家
     * @return 结果
     */
    public int insertAppDoctor(AppDoctor appDoctor);

    /**
     * 修改APP首页专家
     * 
     * @param appDoctor APP首页专家
     * @return 结果
     */
    public int updateAppDoctor(AppDoctor appDoctor);

    /**
     * 删除APP首页专家
     * 
     * @param id APP首页专家ID
     * @return 结果
     */
    public int deleteAppDoctorById(Long id);

    /**
     * 批量删除APP首页专家
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAppDoctorByIds(String[] ids);
    
    
    public List<DoctorIndexVO> selectAppDoctor(@Param("tenantId")String tenantId,@Param("pageSize")int pageSize);

    /**
     * 移动端推荐医生
     * @param tenantId
     * @param pageSize
     * @param positionType
     * @return
     */
    public List<DoctorIndexVO> selectMobileDoctor(@Param("tenantId")String tenantId,@Param("pageSize")int pageSize,@Param("positionType")String positionType);

    public List<ArticleListVO> selectAppArticle(@Param("tenantId")String tenantId,@Param("pageSize")int pageSize);

    /**
     * 移动端咨询
     * @param tenantId
     * @param pageSize
     * @param positionType
     * @return
     */
    public List<ArticleListVO> selectMobileArticle(@Param("tenantId")String tenantId,@Param("pageSize")int pageSize,
                                                   @Param("positionType")String positionType,@Param("deptIds")String deptIds);
    public List<ArticleListVO> selectMobileArticlePageInfo(Long deptId,Long pageNo,Long pageSize,String positionType);



}
