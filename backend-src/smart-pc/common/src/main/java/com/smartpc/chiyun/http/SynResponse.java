package com.smartpc.chiyun.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步请求回调
 */
public class SynResponse implements FutureCallback<HttpResponse>{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private HttpPost postMethod;
	/**
	 * 异步请求线程
	 */
	private SynRequestThread synThread;

	public SynResponse(SynRequestThread synThread,HttpPost postMethod){
		this.synThread = synThread;
		this.postMethod = postMethod;
	}

	@Override
	public void completed(HttpResponse paramT) {
		// TODO 自动生成的方法存根
		releaseConnection();
	}

	@Override
	public void cancelled() {
		// TODO 自动生成的方法存根
		releaseConnection();
	}

	@Override
	public void failed(Exception paramException) {
		// TODO 自动生成的方法存根
		releaseConnection();
	}

	/**
	 * 释放连接
	 */
	private void releaseConnection(){
		if(postMethod != null)
			postMethod.releaseConnection();
		int num = synThread.synReqCount.decrementAndGet();
		if (num <= 0){
			synThread.synReqOpen.countDown();
			log.debug("：SynResponse请求完毕了,计数为0了，释放线程");
		}
	}
}
