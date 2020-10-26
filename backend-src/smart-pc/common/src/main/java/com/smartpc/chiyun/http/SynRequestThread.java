package com.smartpc.chiyun.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步请求线程
 */
public class SynRequestThread extends Thread{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public SynRequestThread(){
		synClient = HttpAsyncClients.custom().setMaxConnTotal(1000).setMaxConnPerRoute(1000).build();
		synReqOpen = new CountDownLatch(1);
		synReqCount = new AtomicInteger(0);
		synClient.start();
		status = 1;
		log.debug("：SynRequestThread synClient初始化了");
	}

	/**
	 * 异步请求客户端
	 */
	private CloseableHttpAsyncClient synClient;

	/**
	 * 当前异步请求数量
	 */
    public AtomicInteger synReqCount = null;

    /**
     * 异步请求结束开关（计数器）
     */
    public CountDownLatch synReqOpen = null;

    private int status = 0;//0:已结束    1:运行中



	@Override
	public void run() {
		// TODO 自动生成的方法存根
		try {
			log.debug("：SynRequestThread synThread开始执行了");
			synReqOpen.await();
			status = 0;
			log.debug("：SynRequestThread synClient请求发送完毕了");
			synClient.close();
			log.debug("：SynRequestThread synThread释放了");
			//System.out.println("异步请求释放了");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			log.error("释放线程失败：",e);
		}
	}

	/**
	 * 是否正在运行
	 * @return
	 */
	public boolean isRunning(){
		return status == 1 && synClient != null && synClient.isRunning();
	}

	/**
	 * 释放锁
	 * @date 2017年12月4日
	 */
	public void close(){
		if (status == 1) {//请求未发送完毕
			log.debug("：SynRequestThread close时剩余请求数量" + "("+synReqCount.get()+")");
			synReqOpen.countDown();
		}
	}

	/**
	 * 发送请求
	 * @param postMethod
	 */
	public void sendRequest(HttpPost postMethod){
		//System.out.println("异步请求+1");
		try {
			synReqCount.incrementAndGet();
			synClient.execute(postMethod,new SynResponse(this,postMethod));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			log.error("发送异步请求失败（"+postMethod.getURI()+"）：",e);
			int num = synReqCount.decrementAndGet();
			if (num <= 0){
				synReqOpen.countDown();
				log.debug("：SynRequestThread请求完毕了,计数为0了，释放线程");
			}
		}
	}
}
