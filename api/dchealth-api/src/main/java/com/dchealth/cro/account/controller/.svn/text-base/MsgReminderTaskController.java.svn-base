package com.dchealth.cro.account.controller;


import com.dchealth.cro.account.domain.MsgReminderTask;
import com.dchealth.cro.account.service.MsgReminderTaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 随访消息提醒配置表(MsgReminderTask)表控制层
 *
 * @author makejava
 * @since 2023-06-06 09:41:58
 */
@RestController
@RequestMapping("msgReminderTask")
public class MsgReminderTaskController {
    /**
     * 服务对象
     */
    @Resource
    private MsgReminderTaskService msgReminderTaskService;

    /**
     * 分页查询
     *
     * @param msgReminderTask 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<MsgReminderTask>> queryByPage(MsgReminderTask msgReminderTask, PageRequest pageRequest) {
        return ResponseEntity.ok(this.msgReminderTaskService.queryByPage(msgReminderTask, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<MsgReminderTask> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.msgReminderTaskService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param msgReminderTask 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<MsgReminderTask> add(MsgReminderTask msgReminderTask) {
        return ResponseEntity.ok(this.msgReminderTaskService.insert(msgReminderTask));
    }

    /**
     * 编辑数据
     *
     * @param msgReminderTask 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<MsgReminderTask> edit(MsgReminderTask msgReminderTask) {
        return ResponseEntity.ok(this.msgReminderTaskService.update(msgReminderTask));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.msgReminderTaskService.deleteById(id));
    }

}

