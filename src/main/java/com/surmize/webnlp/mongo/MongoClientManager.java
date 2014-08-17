package com.surmize.webnlp.mongo;

import com.mongodb.MongoClient;
import com.surmize.solrclient.PropertyManager;
import java.net.UnknownHostException;
import org.apache.commons.lang3.StringUtils;

public class MongoClientManager {
    
    private MongoClient mongoClient;
    
    private MongoClientManager() {
    }
    
    public static MongoClientManager getInstance() {
        return MongoClientManagerHolder.INSTANCE;
    }
    
    private static class MongoClientManagerHolder {

        private static final MongoClientManager INSTANCE = new MongoClientManager();
    }
    
    public MongoClient getClient() throws UnknownHostException{
        if(mongoClient == null){
            initializeClient();
        }
        return mongoClient;
    }
    
    private void initializeClient() throws UnknownHostException{
        String url = PropertyManager.getSetting("mongo_url");
        String port = PropertyManager.getSetting("mongo_port");
        int portNum = StringUtils.isNumeric(port) ? Integer.parseInt(port) : 27017;
        mongoClient = new MongoClient( url , portNum );
    }
}
