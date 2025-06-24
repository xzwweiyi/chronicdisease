package com.dchealth.web.controller.user;


import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.user.domain.LeaveMessage;
import com.dchealth.user.service.ILeaveMessageService;
import com.dchealth.user.vo.req.UserMessageListReq;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/6/18 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/message")
public class UserLeaveMessageController extends BaseController {

    @Resource
    private ILeaveMessageService leaveMessageService;

//    @PreAuthorize("@ss.hasPermi('system:message:query')")
    @GetMapping(value = "/{personId}")
    public AjaxResult getInfo(@PathVariable("personId") Long personId)
    {
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setPersonId(personId);
        return AjaxResult.success(leaveMessageService.selectLeaveMessageList(leaveMessage));
    }

    /**
     * 发送指导消息
     */
//    @PreAuthorize("@ss.hasPermi('system:message:send')")
    @PostMapping("/send")
    public AjaxResult send(@RequestBody UserMessageListReq messageListReq) {
        messageListReq.setFromUserId(getUserId());
        messageListReq.setDeptId(getDeptId());
        return AjaxResult.success(leaveMessageService.sendMessage(messageListReq));
    }


    /**
     * 清空指导消息
     */
//    @PreAuthorize("@ss.hasPermi('system:message:clear')")
    @GetMapping("/clear/{id}")
    public AjaxResult clear(@PathVariable("id") Long id) {
        return AjaxResult.success(leaveMessageService.deleteLeaveMessageById(id));
    }


    /**
     * 批量清空指导消息
     */
//    @PreAuthorize("@ss.hasPermi('system:message:clearall')")
    @PostMapping("/batchClear")
    public AjaxResult batchClear(@RequestBody List<Long> ids) {
        return AjaxResult.success(leaveMessageService.deleteLeaveMessageByIds(ids));
    }

    /**
     * 更新聊天消息状态
     */
    @GetMapping("/updateReadStatus/{personId}")
    public AjaxResult updateReadStatus(@PathVariable("personId") Long personId) {
        return AjaxResult.success(leaveMessageService.updateReadStatus(personId));
    }


}
