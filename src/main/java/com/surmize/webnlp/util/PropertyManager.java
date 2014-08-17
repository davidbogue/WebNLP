package com.surmize.webnlp.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {

    private static Properties properties;
    
    private PropertyManager() {
    }
    
    private synchronized static void initialize() {
        try {
            properties = new Properties();
            String environment = System.getenv("WEBNLP_ENV");
            if("production".equalsIgnoreCase(environment)){
                properties.load(PropertyManager.class.getResourceAsStream("/prod-webnlp.properties"));
            }
            else if("staging".equalsIgnoreCase(environment)){
                properties.load(PropertyManager.class.getResourceAsStream("/stage-webnlp.properties"));
            }
            else if("test".equalsIgnoreCase(environment)){
                properties.load(PropertyManager.class.getResourceAsStream("/test-webnlp.properties"));
            }
            else{
                properties.load(PropertyManager.class.getResourceAsStream("/dev-webnlp.properties"));
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static String getSetting(String key, String defaultValue) {
    	if( propertyFile() == null ) {
    		initialize();
    	}
        return propertyFile().getProperty(key, defaultValue);
    }

    public static String getSetting(String key) {
        return PropertyManager.getSetting(key, null);
    }
    
    public static Integer getIntegerSetting(String key) {
        String prop = PropertyManager.getSetting(key, "-1" );
        return Integer.parseInt( prop );
    }
    
   private static Properties propertyFile(){
    	return properties;
    }
    
    public static void reload() {
    	initialize();
    }
    
    public static void main(String args[]){
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
    }
}

