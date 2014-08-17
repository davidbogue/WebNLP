package com.surmize.webnlp.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.surmize.webnlp.model.Document;
import com.surmize.webnlp.mongo.MongoClientManager;
import com.surmize.webnlp.mongo.MongoDocumentMapper;
import com.surmize.webnlp.util.PropertyManager;
import java.net.UnknownHostException;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class MongoService {

    private final String dbName = PropertyManager.getSetting("mongo_db");
    private final String collectionName = "document";

    public String addDocument(Document doc) {
        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID().toString());
        }
        try {
            MongoClient mongoClient = getClient();
            DB db = mongoClient.getDB(dbName);
            DBCollection coll = db.getCollection(collectionName);
            BasicDBObject basicDoc = MongoDocumentMapper.mapDocument(doc);
            coll.insert(basicDoc);
        } catch (UnknownHostException ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        return doc.getId();
    }

    private MongoClient getClient() throws UnknownHostException {
        return MongoClientManager.getInstance().getClient();
    }

    

}
