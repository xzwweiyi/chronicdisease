package com.dchealth.web.controller.msgTip;


import com.dchealth.msgTip.domain.MsgReminderTaskTemplate;
import com.dchealth.msgTip.service.MsgReminderTaskTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 随访消息提醒模板(MsgReminderTaskTemplate)表控制层
 *
 * @author makejava
 * @since 2023-06-06 10:05:21
 */
@RestController
@RequestMapping("msgReminderTaskTemplate")
public class MsgReminderTaskTemplateController {
    /**
     * 服务对象
     */
    @Resource
    private MsgReminderTaskTemplateService msgReminderTaskTemplateService;

    /**
     * 分页查询
     *
     * @param msgReminderTaskTemplate 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<MsgReminderTaskTemplate>> queryByPage(MsgReminderTaskTemplate msgReminderTaskTemplate, PageRequest pageRequest) {
        return ResponseEntity.ok(this.msgReminderTaskTemplateService.queryByPage(msgReminderTaskTemplate, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<MsgReminderTaskTemplate> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.msgReminderTaskTemplateService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param msgReminderTaskTemplate 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<MsgReminderTaskTemplate> add(MsgReminderTaskTemplate msgReminderTaskTemplate) {
        return ResponseEntity.ok(this.msgReminderTaskTemplateService.insert(msgReminderTaskTemplate));
    }

    /**
     * 编辑数据
     *
     * @param msgReminderTaskTemplate 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<MsgReminderTaskTemplate> edit(MsgReminderTaskTemplate msgReminderTaskTemplate) {
        return ResponseEntity.ok(this.msgReminderTaskTemplateService.update(msgReminderTaskTemplate));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.msgReminderTaskTemplateService.deleteById(id));
    }

}

