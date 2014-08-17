package com.surmize.webnlp.services;

import com.google.gson.Gson;
import com.surmize.solrclient.SolrClient;
import com.surmize.solrclient.models.SolrDocument;
import com.surmize.webnlp.model.Document;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;

@Path("solr")
public class SolrService {

    private SolrClient solrClient = new SolrClient();
    private Gson gson = new Gson();
    
    @GET
    @Produces("application/json")
    public String find(@QueryParam("q") String query){
        List<Document> documents = findDocuments(query);
        return gson.toJson(documents);
    }
    
    public List<Document> findDocuments(String query){
        try {
            List<Document> documents = solrClient.query(query, Document.class);
            return documents;
        } catch (SolrServerException ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        
    }
    
    public void addDocument(Document document){
        try {
            UpdateResponse resp =  solrClient.addDocument(document);
            if(resp.getStatus() > 0){
               throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (SolrServerException | IOException ex) { 
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public void addDocuments(List<SolrDocument> documents){
        try {
            UpdateResponse resp =  solrClient.addDocuments(documents);
            if(resp.getStatus() > 0){
               //TODO throw exception that results in Jersey returning 500 error
            }
        } catch (SolrServerException | IOException ex) { 
            //TODO throw exception that results in Jersey returning 500 error
        }
    }
    
}
