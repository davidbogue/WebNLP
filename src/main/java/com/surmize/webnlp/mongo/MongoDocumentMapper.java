package com.surmize.webnlp.mongo;

import com.mongodb.BasicDBObject;
import com.surmize.textalytics.AnalyzedDocument;
import com.surmize.textalytics.AnalyzedSentence;
import com.surmize.textalytics.NamedEntity;
import com.surmize.webnlp.model.Document;
import java.util.ArrayList;
import java.util.List;

public class MongoDocumentMapper {

    public static BasicDBObject mapDocument(Document doc) {
        BasicDBObject basicDoc = new BasicDBObject("_id", doc.getId())
                .append("author", doc.getAuthor())
                .append("title", doc.getTitle())
                .append("text", doc.getText())
                .append("clusterId", doc.getClusterId());
        if (doc.getAnalyzedDocument() != null) {
            basicDoc.append("analyzedDocument", mapAnalyzedDoc(doc.getAnalyzedDocument()));
        }
        return basicDoc;
    }

    public static BasicDBObject mapAnalyzedDoc(AnalyzedDocument analyzedDoc) {
        BasicDBObject analyzedDocBasic = new BasicDBObject("sentiment", analyzedDoc.getSentiment());
        if (analyzedDoc.getSentences() != null) {
            List<BasicDBObject> basicSentences = new ArrayList<>();
            for (AnalyzedSentence aSentence : analyzedDoc.getSentences()) {
                BasicDBObject analyzedSentBasic = new BasicDBObject("text", aSentence.getText())
                        .append("sentiment", aSentence.getSentiment());
                if(aSentence.getEntities() != null){
                    analyzedSentBasic.append("entities", mapEntities(aSentence.getEntities()));
                }
                basicSentences.add(analyzedSentBasic);
            }
            analyzedDocBasic.append("sentences", basicSentences);
        }
        return analyzedDocBasic;
    }
    
    public static List<BasicDBObject> mapEntities(List<NamedEntity> entities){
        List<BasicDBObject> basicEntities = new ArrayList<>();
        for (NamedEntity namedEntity : entities) {
            BasicDBObject basicEntity = new BasicDBObject("text", namedEntity.getText())
                    .append("offsetBegin", namedEntity.getOffsetBegin())
                    .append("offsetEnd", namedEntity.getOffsetEnd())
                    .append("type", namedEntity.getType());
            basicEntities.add(basicEntity);
        }
        return basicEntities;
    }
}
