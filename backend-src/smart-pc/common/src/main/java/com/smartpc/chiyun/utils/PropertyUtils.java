package com.smartpc.chiyun.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class PropertyUtils {
	private static final Logger log= LoggerFactory.getLogger(PropertyUtils.class);

	public PropertyUtils(){}

	public PropertyUtils(String fileName){
		properties = new Properties();
		InputStream is = null;
		InputStreamReader isr = null;
		try {
			is = PropertyUtils.class.getClassLoader().getResourceAsStream(fileName);
			isr = new InputStreamReader(is, "UTF-8");
			properties.load(isr);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}finally{
			try {
				if(isr != null)
					isr.close();
				if(is != null)
					is.close();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				log.error("释放"+fileName+"配置文件失败",e);
			}
		}
	}

	public static String getPropertiesByKey(String key ,String fileName) {
		Properties p = new Properties();
		InputStream is = null;
		InputStreamReader isr = null;
		try {
			is = PropertyUtils.class.getClassLoader().getResourceAsStream(fileName);
			isr = new InputStreamReader(is, "UTF-8");
			p.load(isr);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}finally{
			try {
				if(isr != null)
					isr.close();
				if(is != null)
					is.close();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				log.error("释放"+fileName+"配置文件失败",e);
			}
		}
		return p.getProperty(key);
	}

	public List<String> getList(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				list.add(properties.getProperty(key));
			}
		}
		return list;
	}

	public Set<String> getSet(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptySet();
		}
		Set<String>set=new TreeSet<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				set.add(properties.getProperty(key));
			}
		}
		return set;
	}


	public Map<String, String> getMap(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				map.put(key.substring(len), properties.getProperty(key));
			}
		}
		return map;
	}

	public Properties getProperties(String prefix) {
		Properties props = new Properties();
		if (properties == null || prefix == null) {
			return props;
		}
		Enumeration<?> en = properties.propertyNames();
		String key;
		int len = prefix.length();
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				props.put(key.substring(len), properties.getProperty(key));
			}
		}
		return props;
	}

	public String getPropertiesString(String key) {
		String property = "";
		if (properties == null || key == null) {
			return property;
		}
		return properties.getProperty(key);
	}

	public Integer getPropertiesInteger(String prefix) {
		String value = getPropertiesString(prefix);
		return StringUtils.isEmpty(value) ? 0:Integer.parseInt(value) ;
	}

	public static Properties getProperties(File file) {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			props.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(in != null)
					in.close();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				if(file != null)
					log.error("释放"+file.getName()+"配置文件失败",e);
			}
		}
		return props;
	}

	public static String getPropertyValue(File file,String key) {
		Properties props=getProperties(file);
		return (String) props.get(key);
	}

	private Properties properties;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
