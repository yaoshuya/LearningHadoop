package org.BigDataFramework.learningHadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesUtil {
	protected static final Log logger = LogFactory.getLog(PropertiesUtil.class);
	private static PropertiesUtil util = null;
	private static Map<String,Properties> props = null;
	private PropertiesUtil(){
		
	}
	
	public static PropertiesUtil getInstance() {
		if(util==null) {
			props = new HashMap<String, Properties>();
			util = new PropertiesUtil();
		}
		return util;
	}
	
	public Properties load(String name) {
		if(props.get(name)!=null) {
			return props.get(name);
		} else {
			Properties prop = new Properties();
			try {
				prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(name+".properties"));
				props.put(name, prop);
				return prop;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}
}
