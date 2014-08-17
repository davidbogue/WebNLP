package com.surmize.webnlp.model;

import com.google.gson.Gson;
import org.junit.Test;

public class GsonTest {
    
    public GsonTest() {
    }

     @Test
     public void documentGsonTest() {
         Document doc = new Document();
         doc.setAuthor("david bogue");
         doc.setText("This is the text of the article");
         
         Gson gson = new Gson();
         String docJson = gson.toJson(doc);
         System.out.println(docJson);
     }
}
