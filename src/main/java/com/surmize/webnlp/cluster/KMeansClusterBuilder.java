package com.surmize.webnlp.cluster;

import com.surmize.machinelearning.cluster.lucene.ClusterTerms;
import com.surmize.machinelearning.cluster.lucene.ClusterTopTermsMapper;
import com.surmize.webnlp.model.ClusterCreationArtifacts;
import com.surmize.webnlp.services.MongoService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.ClassUtils;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.utils.vectors.lucene.Driver;

public class KMeansClusterBuilder {
    
    public synchronized ClusterCreationArtifacts buildClusterFromSolr(String solrIndexDir) throws IOException, InterruptedException, ClassNotFoundException, Exception{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        String solrVectorsFile = "clustering/testdata/solrvectors/out.vec";
        String solrDictFile = "clustering/testdata/solrvectors/dict.txt";
        String inputClustersDir = "clustering/testdata/input-clusters";
        String outputClusterDir = "clustering/output";

        Class measureClazz = SquaredEuclideanDistanceMeasure.class;     
        dumpVectors(solrIndexDir, solrVectorsFile, solrDictFile);

        Path inputClustersPath = new Path(inputClustersDir);
        Path solrVectorPath = new Path(solrVectorsFile);
        Path clusterOutputPath = new Path(outputClusterDir);

        inputClustersPath = runKMeansCluster(measureClazz, inputClustersPath,solrVectorPath, clusterOutputPath);
        
        ClusterCreationArtifacts artifacts = new ClusterCreationArtifacts();
        artifacts.setClusters(generateClusterTopTerms(inputClustersPath, solrDictFile));        
        artifacts.setDocumentClusterMap(mapDocumentsToClusters(fs, conf)); 
        return artifacts;
    }

    private Map<String, String> mapDocumentsToClusters(FileSystem fs, Configuration conf) throws IOException {
        Map<String, String> documentClusterMap = new HashMap<>();
        try ( SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path("clustering/output/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-0"), conf)) {
            IntWritable key = new IntWritable();
            WeightedPropertyVectorWritable value = new WeightedPropertyVectorWritable();
            while (reader.next(key, value)) {
                Vector v = value.getVector();
                String vectorName = "";
                if(v instanceof NamedVector){
                    vectorName = ((NamedVector)v).getName();
                }
                documentClusterMap.put(vectorName, key.toString()); // document key and cluster key
            }
        }
        return documentClusterMap;
    }

    private List<ClusterTerms> generateClusterTopTerms(Path inputClustersPath, String solrDictFile) throws Exception {
        ClusterTopTermsMapper clusterTopTerms = new ClusterTopTermsMapper(inputClustersPath, solrDictFile);
        List<ClusterTerms> clusterTermsList = clusterTopTerms.getClusterTerms();
        for (ClusterTerms clusterTerms : clusterTermsList) {
            System.out.println("Cluster: "+clusterTerms.clusterId);
            for (String term : clusterTerms.terms) {
                System.out.println("\t"+term);
            }
        }
        return clusterTermsList;
    }

    private Path runKMeansCluster(Class measureClazz, Path inputClustersPath, Path solrVectorPath, Path clusterOutputPath) throws IOException, ClassNotFoundException, InterruptedException {
        String measureClass = measureClazz.getName();
        DistanceMeasure measure = ClassUtils.instantiateAs(measureClass, DistanceMeasure.class);
        inputClustersPath = RandomSeedGenerator.buildRandom(new Configuration(), solrVectorPath, inputClustersPath, 15, measure);
        KMeansDriver.run(solrVectorPath,          //the directory pathname for input points
                inputClustersPath,                // the directory pathname for initial & computed clusters
                clusterOutputPath,                  //the directory pathname for output points
                0.001,                   //the convergence delta value
                50,                      //the maximum number of iterations
                true,                    //true if points are to be clustered after iterations are completed
                0,                       //Is a clustering strictness / outlier removal parameter. Its value should be between 0 and 1. Vectors having pdf below this value will not be clustered.
                true);                   //if true execute sequential algorithm
        return inputClustersPath;
    }

    private void dumpVectors(String solrIndexDir, String solrVectorsFile, String solrDictFile) throws IOException {
        Driver luceneDriver = new Driver();
        luceneDriver.setLuceneDir(solrIndexDir);
        luceneDriver.setField("text");
        luceneDriver.setIdField("id");
        luceneDriver.setOutFile(solrVectorsFile);
        luceneDriver.setDictOut(solrDictFile);
        
        luceneDriver.dumpVectors();
    }
    
}
