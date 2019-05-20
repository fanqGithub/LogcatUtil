package com.commaai.commalog.log;

/**
 * Created by fanqi on 2019/2/26.
 * Description:
 */
public class ThreadPoolFactory {
    private static RunnableExecutor mNormalThreadPoolProxy;
    /**
     * 得到普通线程池代理对象mNormalThreadPoolProxy
     */
    public static RunnableExecutor getNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new RunnableExecutor(2, 2);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }

}
