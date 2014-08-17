package com.surmize.webnlp.model;

import java.util.List;

public class Cluster {

    private int id;
    private List<String> terms;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }
    
}
