package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsGuestbook;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 留言板Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Repository
public interface CmsGuestbookMapper 
{
    /**
     * 查询留言板
     * 
     * @param id 留言板ID
     * @return 留言板
     */
    public CmsGuestbook selectCmsGuestbookById(Long id);

    /**
     * 查询留言板列表
     * 
     * @param cmsGuestbook 留言板
     * @return 留言板集合
     */
    public List<CmsGuestbook> selectCmsGuestbookList(CmsGuestbook cmsGuestbook);

    /**
     * 新增留言板
     * 
     * @param cmsGuestbook 留言板
     * @return 结果
     */
    public int insertCmsGuestbook(CmsGuestbook cmsGuestbook);

    /**
     * 修改留言板
     * 
     * @param cmsGuestbook 留言板
     * @return 结果
     */
    public int updateCmsGuestbook(CmsGuestbook cmsGuestbook);

    /**
     * 删除留言板
     * 
     * @param id 留言板ID
     * @return 结果
     */
    public int deleteCmsGuestbookById(Long id);

    /**
     * 批量删除留言板
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsGuestbookByIds(String[] ids);
}
