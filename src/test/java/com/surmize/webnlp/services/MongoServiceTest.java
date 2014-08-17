package com.surmize.webnlp.services;

import com.surmize.webnlp.model.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class MongoServiceTest {
    
    public MongoServiceTest() {
    }

    /**
     * Test of addDocument method, of class MongoService.
     */
    @Test
    public void testAddDocument() {
        MongoService service = new MongoService();
        Document doc = new Document();
        doc.setAuthor("TEST AUTHOR");
        doc.setText("TEST DOCUMENT TEXT");
        String id = service.addDocument(doc);
        assertNotNull(id);
    }
    
}
