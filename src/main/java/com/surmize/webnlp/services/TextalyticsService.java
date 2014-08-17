package com.surmize.webnlp.services;

import com.surmize.textalytics.AnalyzedDocument;
import com.surmize.textalytics.TextAnalyzer;
import com.surmize.webnlp.model.Document;

public class TextalyticsService {

    private static TextAnalyzer ta = new TextAnalyzer();
    
    public Document analyzeDocument(Document doc){
        AnalyzedDocument analyzedDoc = ta.analyze(doc.getText());
        doc.setAnalyzedDocument(analyzedDoc);
        return doc;
    }
    
}
