package com.smartpc.chiyun.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtil {

	public static String getBaseUrl(HttpServletRequest request){
		String protocol = request.getProtocol().toLowerCase();
		protocol = protocol.substring(0, protocol.indexOf("/"));
		String serverName = request.getServerName();
		int port = request.getServerPort();

		if(port == 443){
			return "https://" + serverName  + "/";
		} else if(port == 80){
			return  "http://" + serverName  + "/";
		} else {
			return protocol + "://" + serverName + ":" + port + "/";
		}
	}


	/**
	 * Get 请求
	 * @param url
	 * @return
	 */
	public static String doGet(String url){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String content = "";
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);
				return content;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String get(String reqUrl) throws Exception{
		String result="";
    	URL url = new URL(reqUrl);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
		httpConn.setRequestMethod("GET");
		httpConn.setRequestProperty("Charset", "UTF-8");

		// 获得响应状态
		int responseCode = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
			// 当正确响应时处理数据
			StringBuffer sb = new StringBuffer();
			String readLine;
			BufferedReader responseReader;
			// 处理响应流，必须与服务器响应流输出的编码一致
			responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine);
			}
			responseReader.close();
			result=sb.toString();
		}
	    return result;
	}

	/**获取ip
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
	    String ip="unknow";
		if (request!=null) {
			ip = request.getHeader("x-forwarded-for");
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getHeader("Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getHeader("WL-Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		        ip = request.getRemoteAddr();
		    }
		}
	    return ip;
	}
}
