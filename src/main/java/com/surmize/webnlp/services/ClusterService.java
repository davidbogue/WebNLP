package com.surmize.webnlp.services;

import com.surmize.webnlp.cluster.KMeansClusterBuilder;
import com.surmize.webnlp.model.ClusterCreationArtifacts;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cluster")
public class ClusterService {

    private static final String SOLR_INDEX_DIR="/Users/davidbogue/solr-4.6.1/example/solr/collection1/data/index";
    MongoService mongoService = new MongoService();
     
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createCluster(){
        KMeansClusterBuilder clusterBuilder = new KMeansClusterBuilder();
        try {
            ClusterCreationArtifacts artifacts = clusterBuilder.buildClusterFromSolr(SOLR_INDEX_DIR);
            mongoService.deleteClusters();
            mongoService.addClusters(artifacts.getClusters());
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        
    }
}
