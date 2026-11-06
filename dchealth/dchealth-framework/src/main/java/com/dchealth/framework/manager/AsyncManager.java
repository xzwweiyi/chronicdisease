package com.dchealth.framework.manager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.dchealth.common.utils.Threads;
import com.dchealth.common.utils.spring.SpringUtils;

/**
 * 异步任务管理器
 *
 * @author ruoyi
 */
public class AsyncManager
{
    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 单例模式
     */
    private AsyncManager(){}

    private static class Holder {
        private static final AsyncManager INSTANCE = new AsyncManager();
    }

    public static AsyncManager me()
    {
        return Holder.INSTANCE;
    }

    /**
     * 延迟获取 bean，避免类加载时 Spring 容器未就绪导致 NoClassDefFoundError
     */
    private ScheduledExecutorService getExecutor() {
        return SpringUtils.getBean("scheduledExecutorService");
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task)
    {
        getExecutor().schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(getExecutor());
    }
}
