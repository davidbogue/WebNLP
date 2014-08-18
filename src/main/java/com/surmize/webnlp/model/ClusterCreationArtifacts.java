package com.surmize.webnlp.model;

import com.surmize.machinelearning.cluster.lucene.ClusterTerms;
import java.util.List;
import java.util.Map;

public class ClusterCreationArtifacts {

    private List<ClusterTerms> clusters;
    // map of document key and cluster key
    private Map<String, String> documentClusterMap;

    public List<ClusterTerms> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterTerms> clusters) {
        this.clusters = clusters;
    }

    public Map<String, String> getDocumentClusterMap() {
        return documentClusterMap;
    }

    public void setDocumentClusterMap(Map<String, String> documentClusterMap) {
        this.documentClusterMap = documentClusterMap;
    }
    
}
