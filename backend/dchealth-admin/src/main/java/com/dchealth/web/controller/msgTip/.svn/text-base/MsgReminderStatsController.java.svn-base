package com.dchealth.web.controller.msgTip;

import com.dchealth.msgTip.domain.MsgReminderStats;
import com.dchealth.msgTip.service.MsgReminderStatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 消息提醒统计表(MsgReminderStats)表控制层
 *
 * @author makejava
 * @since 2023-06-06 17:46:20
 */
@RestController
@RequestMapping("msgReminderStats")
public class MsgReminderStatsController {
    /**
     * 服务对象
     */
    @Resource
    private MsgReminderStatsService msgReminderStatsService;

    /**
     * 分页查询
     *
     * @param msgReminderStats 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<MsgReminderStats>> queryByPage(MsgReminderStats msgReminderStats,
                                                              PageRequest pageRequest) {
        return ResponseEntity.ok(this.msgReminderStatsService.queryByPage(msgReminderStats, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<MsgReminderStats> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.msgReminderStatsService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param msgReminderStats 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<MsgReminderStats> add(MsgReminderStats msgReminderStats) {
        return ResponseEntity.ok(this.msgReminderStatsService.insert(msgReminderStats));
    }

    /**
     * 编辑数据
     *
     * @param msgReminderStats 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<MsgReminderStats> edit(MsgReminderStats msgReminderStats) {
        return ResponseEntity.ok(this.msgReminderStatsService.update(msgReminderStats));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.msgReminderStatsService.deleteById(id));
    }

}

