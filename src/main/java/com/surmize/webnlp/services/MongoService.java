package com.surmize.webnlp.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.surmize.machinelearning.cluster.lucene.ClusterTerms;
import com.surmize.webnlp.model.Document;
import com.surmize.webnlp.mongo.MongoClientManager;
import com.surmize.webnlp.mongo.MongoClusterMapper;
import com.surmize.webnlp.mongo.MongoDocumentMapper;
import com.surmize.webnlp.util.PropertyManager;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class MongoService {

    private final String dbName = PropertyManager.getSetting("mongo_db");
    private final String DOCUMENT_COLLECTION_NAME = "document";
    private final String CLUSTER_COLLECTION_NAME = "cluster";

    public String addDocument(Document doc) {
        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID().toString());
        }
        try {
            MongoClient mongoClient = MongoClientManager.getInstance().getClient();
            DB db = mongoClient.getDB(dbName);
            DBCollection coll = db.getCollection(DOCUMENT_COLLECTION_NAME);
            BasicDBObject basicDoc = MongoDocumentMapper.mapDocument(doc);
            coll.insert(basicDoc);
        } catch (UnknownHostException ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        return doc.getId();
    }

    public void addClusters(List<ClusterTerms> clusters) {
        MongoClient mongoClient;
        try {
            mongoClient = MongoClientManager.getInstance().getClient();
        } catch (UnknownHostException ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        DB db = mongoClient.getDB(dbName);
        DBCollection coll = db.getCollection(CLUSTER_COLLECTION_NAME);
        for(ClusterTerms clusterTerms : clusters) {
            if (clusterTerms.clusterId == null) { continue; }
            BasicDBObject basicDoc = MongoClusterMapper.mapCluster(clusterTerms);
            coll.insert(basicDoc);
        }
    }

    public void deleteClusters() {
        try {
            MongoClient mongoClient = getClient();
            DB db = mongoClient.getDB(dbName);
            DBCollection coll = db.getCollection(CLUSTER_COLLECTION_NAME);
            coll.drop();
        } catch (UnknownHostException ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private MongoClient getClient() throws UnknownHostException {
        return MongoClientManager.getInstance().getClient();
    }

}
