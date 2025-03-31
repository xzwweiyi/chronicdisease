package com.dchealth.base.service;

import com.dchealth.base.domain.FastFile;
import java.util.List;

/**
 * 文件管理Service接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-07
 */
public interface IFastFileService 
{
    /**
     * 查询文件管理
     * 
     * @param id 文件管理ID
     * @return 文件管理
     */
    public FastFile selectFastFileById(Long id);
    public FastFile selectFastFileByFileMd5(String fileMd5);

    /**
     * 查询文件管理列表
     * 
     * @param fastFile 文件管理
     * @return 文件管理集合
     */
    public List<FastFile> selectFastFileList(FastFile fastFile);

    /**
     * 新增文件管理
     * 
     * @param fastFile 文件管理
     * @return 结果
     */
    public int insertFastFile(FastFile fastFile);

    /**
     * 修改文件管理
     * 
     * @param fastFile 文件管理
     * @return 结果
     */
    public int updateFastFile(FastFile fastFile);

    /**
     * 批量删除文件管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFastFileByIds(String ids);

    /**
     * 删除文件管理信息
     * 
     * @param id 文件管理ID
     * @return 结果
     */
    public int deleteFastFileById(Long id);
}
