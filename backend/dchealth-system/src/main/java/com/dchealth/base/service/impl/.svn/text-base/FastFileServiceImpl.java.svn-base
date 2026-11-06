package com.dchealth.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.base.mapper.FastFileMapper;
import com.dchealth.base.domain.FastFile;
import com.dchealth.base.service.IFastFileService;
import com.dchealth.common.core.text.Convert;

/**
 * 文件管理Service业务层处理
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-07
 */
@Service
public class FastFileServiceImpl implements IFastFileService 
{
    @Autowired
    private FastFileMapper fastFileMapper;

    /**
     * 查询文件管理
     * 
     * @param id 文件管理ID
     * @return 文件管理
     */
    @Override
    public FastFile selectFastFileById(Long id)
    {
        return fastFileMapper.selectFastFileById(id);
    }
    public FastFile selectFastFileByFileMd5(String fileMd5)
    {
    	return fastFileMapper.selectFastFileByFileMd5(fileMd5);
    }

    /**
     * 查询文件管理列表
     * 
     * @param fastFile 文件管理
     * @return 文件管理
     */
    @Override
    public List<FastFile> selectFastFileList(FastFile fastFile)
    {
        return fastFileMapper.selectFastFileList(fastFile);
    }

    /**
     * 新增文件管理
     * 
     * @param fastFile 文件管理
     * @return 结果
     */
    @Override
    public int insertFastFile(FastFile fastFile)
    {
        return fastFileMapper.insertFastFile(fastFile);
    }

    /**
     * 修改文件管理
     * 
     * @param fastFile 文件管理
     * @return 结果
     */
    @Override
    public int updateFastFile(FastFile fastFile)
    {
        return fastFileMapper.updateFastFile(fastFile);
    }

    /**
     * 删除文件管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFastFileByIds(String ids)
    {
        return fastFileMapper.deleteFastFileByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除文件管理信息
     * 
     * @param id 文件管理ID
     * @return 结果
     */
    @Override
    public int deleteFastFileById(Long id)
    {
        return fastFileMapper.deleteFastFileById(id);
    }
}
