package com.surmize.webnlp.services;

import com.google.gson.Gson;
import com.surmize.webnlp.model.Document;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("document")
public class DocumentService {

    private final MongoService mongoService = new MongoService();
    private final SolrService solrService = new SolrService();
    private final TextalyticsService textalyticsService = new TextalyticsService();
    private Gson gson = new Gson();
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("*/*")
    public String post(String docJson){
        Document doc = gson.fromJson(docJson, Document.class);
        return scoreStoreAndIndexDocument(doc);
    }
    
    public String scoreStoreAndIndexDocument(Document doc){
        textalyticsService.analyzeDocument(doc);
        mongoService.addDocument(doc);
        solrService.addDocument(doc);
        return doc.getId();
    }
}
