package com.uhubai.server.dispatcher;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务端任务分发器
 * 避免耗时任务在pipline中长时间停留
 */
public class Dispatcher {
    /**
     * Holder模式
     * 静态成员式的内部类，该内部类的实例与外部类的实例没有绑定关系
     * 而且只有被调用到才会装载，从而实现了延迟加载
     * 懒加载，线程安全
     */
    private static class SingletonHolder{
        private static Dispatcher instance = new Dispatcher();
    }
    private Dispatcher(){}

    public static Dispatcher getInstance(){
        return SingletonHolder.instance;
    }

    //获取CPU数量
    private final int N_CPU = Runtime.getRuntime().availableProcessors();
    private String workerThreadName = "RpcWorkerThread";

    //自定义线程池
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            N_CPU * 2,
            N_CPU * 4,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(2400),
            new DefaultThreadFactory(workerThreadName),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public void execute(ChannelEventRunnable channelEventRunnable){
        channelEventRunnable.setThreadName(workerThreadName);
        executor.execute(channelEventRunnable);
    }

}
