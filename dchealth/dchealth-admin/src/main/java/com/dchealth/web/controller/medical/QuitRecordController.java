package com.dchealth.web.controller.medical;

import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.medical.domain.QuitRecord;
import com.dchealth.medical.domain.form.QuitRecordForm;
import com.dchealth.medical.service.IQuitRecordService;
import org.springframework.web.bind.annotation.*;

/**
 * 患者信息Controller
 *
 * @author ruoyi
 * @date 2022-03-21
 */
@RestController
@RequestMapping("/medical/quitRecord")
public class QuitRecordController extends BaseController {
    private IQuitRecordService quitRecordService;
    private RedisCache redisCache;

    public QuitRecordController(IQuitRecordService quitRecordService, RedisCache redisCache) {
        this.quitRecordService = quitRecordService;
        this.redisCache = redisCache;
    }

    /**
     * 根据条件分页查询记录
     *
     * @param quitRecordForm 包含查询条件的表单对象
     * @return 返回分页后的记录数据
     */
    @GetMapping("/findPage")
    public TableDataInfo findPage(QuitRecordForm quitRecordForm) {
        startPage();
        return getDataTable(quitRecordService.findList(quitRecordForm));
    }

    /**
     * 处理更新退出队列的请求
     * 此方法使用POST请求更新特定的退出申请记录信息
     *
     * @param quitRecord 通过请求体接收的退出申请记录对象，包含了需要更新的退出申请记录详细信息
     * @return 返回一个AjaxResult对象，其中包含了更新操作的结果信息
     */
    @PostMapping("update")
    public AjaxResult update(@RequestBody QuitRecord quitRecord)
    {
        // 获取当前用户的ID
        Long userId = getUserId();
        // 调用服务层方法更新退出队列，并获取更新结果
        Integer update = quitRecordService.updateQuitRecord(quitRecord);
        // 删除缓存中与当前用户相关的关注树信息，以保持数据一致性
        redisCache.deleteObject(Constants.FOLLOW_TREE_KEY + userId);
        // 将更新结果转换为AjaxResult对象并返回
        return toAjax(update);
    }
}
