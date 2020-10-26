/**
 * FileName:     MessageAletsByJavaClient.java
 * @Description: TODO(本类封装所有关于短信提醒工具类提供给第三方平台使用)
 * All rights Reserved, Designed By 上海神洲数港有限公司
 * Copyright:    Copyright(C) 2010-2011
 * Company       上海神洲数港有限公司 LTD.
 * @author:    chivalrous
 * @version    V1.0
 * Createdate:         2012-8-26 上午10:56:22
 * Modification  History:
 * Date         Author        Version        Discription
 *  ----------------------------------------------------------------------------------
 * 2012-8-26      chivalrous         1.0             1.0
 * Why & What is modified: <修改原因描述>
 */
package com.smartpc.chiyun.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

public class PostDataTools {

	private static final Logger log = LoggerFactory.getLogger(PostDataTools.class);

	// 定义一个私有构造方法
    private PostDataTools() {
    }
    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile PostDataTools instance;

    //定义一个共有的静态方法，返回该类型实例
    public static PostDataTools getIstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (PostDataTools.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new PostDataTools();
                }
            }
        }
        return instance;
    }

	private int HTTP_CONNECTTIMEOUT = 10000;//连接超时时间（10秒）

	private int HTTP_READTIMEOUT = 20000;//读取内容超时时间（20秒）



	public String sendMessageInfo(String hostURl,Object o,String[] requestHeaders,boolean isJsonParam) {
		return sendMessageInfo(hostURl,o, requestHeaders, isJsonParam,true,HTTP_READTIMEOUT,HTTP_CONNECTTIMEOUT);
	}


	public String sendMessageNullParam(String hostURl,RequestMethod method,
									   String[] requestHeaders){
		return sendMessageNullParam(hostURl,method,requestHeaders,HTTP_READTIMEOUT);

	}

	/**
	 * 发送http请求
	 * @param o 参数对象
	 * @param requestHeaders 请求头部内容
	 * @param isJsonParam 是否json参数
	 * @param returnResult 是否等待返回结果
	 * @param readTimeout 读取请求结果最长时间（毫秒）
	 * @param connectTimeout 请求连接最长时间（毫秒）
	 * @return
	 */
	public String sendMessageInfo(String hostURl,Object o,String[] requestHeaders,boolean isJsonParam,boolean returnResult,Integer readTimeout,Integer connectTimeout) {

		String output = "";
		String s;
		HttpURLConnection conn = null;
		OutputStream os = null;
		BufferedReader br = null;
		try {
			// 接口URL
			URL url = new URL(hostURl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			if(connectTimeout == null)
				conn.setConnectTimeout(HTTP_CONNECTTIMEOUT);//设置连接主机超时（单位：毫秒）
			else
				conn.setConnectTimeout(connectTimeout);
			if(readTimeout == null)
				conn.setReadTimeout(HTTP_READTIMEOUT);//设置从主机读取数据超时（单位：毫秒）
			else
				conn.setReadTimeout(readTimeout);
			conn.setDoOutput(true);
			if(!returnResult)
				conn.setDoInput(false);
			if (requestHeaders != null && requestHeaders.length > 0) {
				for (String string : requestHeaders) {
					conn.setRequestProperty(string.split(":")[0],
							string.split(":")[1]);
				}
			}
			String bodyContent = "";
			if(isJsonParam)
				bodyContent = JSON.toJSONString(o);// 将内容转换为JSON字符串进行转码
			else if(o != null)
				bodyContent = o.toString();
			log.info("request body:" + bodyContent);
			os = conn.getOutputStream();
			os.write(bodyContent.getBytes("utf-8"));
			os.flush();
			if(returnResult){
				log.info("request url："+hostURl+ ",result code：" + conn.getResponseCode());
				br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream()), "utf-8"));

				while ((s = br.readLine()) != null) {
					output += s;
				}
			}else
				log.info("request url："+hostURl);
		} catch (Exception e) {
			log.error("request url："+hostURl, e);
		}finally{
			try {
				if(br != null)
					br.close();
				if(os != null)
					os.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				log.error(e.getMessage(), e);
			}
			if(conn != null)
				conn.disconnect();
		}
		return output;
	}






	/**
	 * 无参数传递调用
	 * @param method get post
	 * @return
	 */
	public String sendMessageNullParam(String hostURl,RequestMethod method,
									   String[] requestHeaders,Integer readTimeout) {
		String output = "";
		String s;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			// 接口URL
			URL url = new URL(hostURl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(method.toString());
			conn.setConnectTimeout(HTTP_CONNECTTIMEOUT);//设置连接主机超时（单位：毫秒）
			if(readTimeout != null)
				conn.setReadTimeout(readTimeout);//设置从主机读取数据超时（单位：毫秒）
			else
				conn.setReadTimeout(HTTP_READTIMEOUT);//设置从主机读取数据超时（单位：毫秒）
			if (requestHeaders != null && requestHeaders.length > 0) {
				for (String string : requestHeaders) {
					conn.setRequestProperty(string.split(":")[0],
							string.split(":")[1]);
				}
			}

			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream()), "utf-8"));

			while ((s = br.readLine()) != null) {
				output += s;
			}

			log.info("request url："+hostURl+ ",result code：" + conn.getResponseCode()+ ",response：" + output);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally{
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				log.error(e.getMessage(), e);
			}
			if(conn != null)
				conn.disconnect();
		}
		return output;
	}
}
