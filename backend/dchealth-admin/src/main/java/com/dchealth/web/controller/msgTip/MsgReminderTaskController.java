package com.dchealth.web.controller.msgTip;


import com.alibaba.fastjson2.JSONArray;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.msgTip.domain.MsgReminderTaskVO;
import com.dchealth.msgTip.service.MsgReminderTaskService;
import com.dchealth.msgTip.domain.MsgReminderTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 随访消息提醒配置表(MsgReminderTask)表控制层
 *
 * @author makejava
 * @since 2023-06-06 10:04:50
 */
@RestController
@RequestMapping("msgReminderTask")
public class MsgReminderTaskController extends BaseController {
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
     * 查询智能外呼设置
     *
     * @param msgReminderTask 筛选条件
     * @return 查询结果
     */
    @GetMapping("/list")
    public AjaxResult queryList(MsgReminderTask msgReminderTask) {
        return AjaxResult.success(msgReminderTaskService.queryList(msgReminderTask));
    }
    /**
     * 保存数据
     *
     * @param msgReminderTaskVO 实体
     * @return 新增结果
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody MsgReminderTaskVO msgReminderTaskVO) {

        if(msgReminderTaskVO.getResearchId()!=null && msgReminderTaskVO.getMsgReminderTasks()!=null){
            Long researchId = msgReminderTaskVO.getResearchId();
            List<MsgReminderTask> msgReminderTasks = msgReminderTaskVO.getMsgReminderTasks();
            for(MsgReminderTask msgReminderTask:msgReminderTasks){
                msgReminderTask.setResearchId(researchId);
            }
            msgReminderTaskService.deleteByResearchId(researchId);
            msgReminderTaskService.save(msgReminderTasks);

            return AjaxResult.success(msgReminderTasks);
        }
        return AjaxResult.success();
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

