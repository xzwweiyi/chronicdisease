package com.dchealth.redis.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.dchealth.common.utils.AESUtils;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.redis.domain.SysRedisConfig;
import com.dchealth.redis.exception.RedisException;
import com.dchealth.redis.mapper.SysRedisConfigMapper;
import com.dchealth.redis.service.ISysRedisConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-20
 */
@Service
public class SysRedisConfigServiceImpl implements ISysRedisConfigService {
    @Autowired
    private SysRedisConfigMapper sysRedisConfigMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public SysRedisConfig selectSysRedisConfigById(Long id) {
        SysRedisConfig sysRedisConfig = sysRedisConfigMapper.selectSysRedisConfigById(id);
        sysRedisConfig.maskPassword();
        return sysRedisConfig;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sysRedisConfig 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysRedisConfig> selectSysRedisConfigList(SysRedisConfig sysRedisConfig) {
        List<SysRedisConfig> sysRedisConfigs = sysRedisConfigMapper.selectSysRedisConfigList(sysRedisConfig);
        sysRedisConfigs.forEach(s -> s.maskPassword());
        return sysRedisConfigs;
    }


    /**
     * 新增【请填写功能名称】
     *
     * @param sysRedisConfig 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysRedisConfig(SysRedisConfig sysRedisConfig) throws RedisException {
        List<SysRedisConfig> sysRedisConfigs = sysRedisConfigMapper.selectSysRedisConfigList(sysRedisConfig.copyHostAndPort());
        if (CollectionUtils.isNotEmpty(sysRedisConfigs)) {
            String msg = "redis-server：" + sysRedisConfig.getHost() + "已经存在";
            throw new RedisException(msg);
        }
        try {
            sysRedisConfig.setPassword(AESUtils.encrypt(sysRedisConfig.getPassword()));
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
        sysRedisConfig.setCreateTime(DateUtils.getNowDate());
        return sysRedisConfigMapper.insertSysRedisConfig(sysRedisConfig);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param sysRedisConfig 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysRedisConfig(SysRedisConfig sysRedisConfig) throws RedisException {
        List<SysRedisConfig> sysRedisConfigs = sysRedisConfigMapper.selectSysRedisConfigList(sysRedisConfig.copyHostAndPort());
        if (CollectionUtils.isNotEmpty(sysRedisConfigs) && sysRedisConfig.getId() != sysRedisConfigs.get(0).getId()) {
            String msg = "redis-server：" + sysRedisConfig.getHost() + "已经存在";
            throw new RedisException(msg);
        }
        sysRedisConfig.setUpdateTime(DateUtils.getNowDate());
        int res;
        if (StringUtils.isNotEmpty(sysRedisConfig.getPassword()) || StringUtils.isNotEmpty(sysRedisConfig.getNewPassword())) {
            SysRedisConfig sysRedisConfigOld = sysRedisConfigMapper.selectSysRedisConfigById(sysRedisConfig.getId());
            try {
                if (StringUtils.equals(AESUtils.decrypt(sysRedisConfigOld.getPassword()), sysRedisConfig.getPassword())) {
                    sysRedisConfig.setPassword(AESUtils.encrypt(sysRedisConfig.getNewPassword()));
                    sysRedisConfigMapper.updateSysRedisConfigPassword(sysRedisConfig);
                } else {
                    throw new RedisException("原密码不正确");
                }
            } catch (Exception e) {
                throw new RedisException(e.getMessage());
            }
        }
        res = +sysRedisConfigMapper.updateSysRedisConfig(sysRedisConfig);
        return res;
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysRedisConfigByIds(Long[] ids) {
        return sysRedisConfigMapper.deleteSysRedisConfigByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteSysRedisConfigById(Long id) {
        return sysRedisConfigMapper.deleteSysRedisConfigById(id);
    }
}

