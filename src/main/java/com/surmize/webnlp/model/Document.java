package com.surmize.webnlp.model;

import com.surmize.solrclient.models.SolrDocument;
import com.surmize.textalytics.AnalyzedDocument;
import java.util.List;
import org.apache.solr.client.solrj.beans.Field;

public class Document implements SolrDocument{

    @Field
    private String id;
    @Field
    private String author;
    @Field
    private List<String> title;
    @Field
    private String text; 
    private Integer clusterId;
    private AnalyzedDocument analyzedDocument;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public AnalyzedDocument getAnalyzedDocument() {
        return analyzedDocument;
    }

    public void setAnalyzedDocument(AnalyzedDocument analyzedDocument) {
        this.analyzedDocument = analyzedDocument;
    }
    
    
}
