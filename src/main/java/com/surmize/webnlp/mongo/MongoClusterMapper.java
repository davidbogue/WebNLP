package com.surmize.webnlp.mongo;

import com.mongodb.BasicDBObject;
import com.surmize.machinelearning.cluster.lucene.ClusterTerms;

public class MongoClusterMapper {

    public static BasicDBObject mapCluster(ClusterTerms clusterTerms){
        BasicDBObject basicDoc = new BasicDBObject("_id", clusterTerms.clusterId)
                .append("term", clusterTerms.terms);
        
        return basicDoc;
    }
    
}
