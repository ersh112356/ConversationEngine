/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import logging.LoggerManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author Eran
 */
public class LuceneService{
    
    /** Holds a private field that holds the name of the tags field. */
    private final static String FIELD_TAGS = "content";
    /** Holds a private field that holds the name of the ID field. */
    private final static String FIELD_ID = "id";
    /** Holds a private field that holds the name of the domain field.  */
    private final static String FIELD_DOMAIN = "domain";
    /** Holds the IndexWriter Object. */
    protected static IndexWriter indexWriter = null;
    /** Holds the Reader Directory Object. */
    protected  static DirectoryReader directoryReader;
    /** Holds the Lucene index files. This is shared between instances. */
    protected static Directory dir;
    /** Holds the default Analyzer.  */
    protected Analyzer analyzer = new StandardAnalyzer();
    
    /**
     * The constructor.
     * 
     */
    public LuceneService(){
    }
    
    /**
     * Try to train a new ConversationState Object.
     * 
     * @param tags- the tags that represent the state.
     * @param id- the id of the state.
     * @param domain- the domain (label, AKA: 'Elicitation', 'Dialogue', etc.) in which this state belongs to.
     */
    public void trainEntry(String tags, String id, String domain){
        
        insert(tags,id,domain);
    }
    
    /**
     * Try to insert a new content into Lucene.
     * 
     * @param tags- tags- the tags that represent the state.
     * @param id- the id of the state.
     * @param domain- the domain (label, AKA: 'Elicitation' etc.) in which this state belongs to.
     */
    private void insert(String tags, String id, String domain){
        
        initWriter();
        
        Document doc = createDocument(tags,id,domain);
        
        try
        {
            indexWriter.addDocument(doc);
        } 
        catch(IOException e)
        {
            LoggerManager.logError("failed to insert a document into Lucene "+e);
        }
    }
    
    /**
     * Create a new Lucene document.
     * 
     * @param tags- the tags the represent a given ConversationState Object.
     * @param id- the id of the state.
     * @param domain- the domain (label, AKA: 'Elicitation' etc.) in which this state belongs to.
     * 
     * @return a new Lucene document.
     */
     protected Document createDocument(String tags, String id, String domain){
        
        Document doc = new Document();           
                
                
        Field f = new TextField(FIELD_TAGS,tags,Store.YES);        
        doc.add(f);
        
        f = new TextField(FIELD_ID,id,Store.YES);        
        doc.add(f);
        
        f = new TextField(FIELD_DOMAIN,domain,Store.YES);        
        doc.add(f);
        
        return doc;
    }
    
    /**
     * Start the initialization of the Lucene Writer.
     * 
     */
    private void initWriter(){
        
        if(indexWriter==null)
        {   
            try 
            {   // Called only once across threads.
                dir = new RAMDirectory();
                IndexWriterConfig iwConf = new IndexWriterConfig(analyzer);
                iwConf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                
                indexWriter = new IndexWriter(dir,iwConf);
            } 
            catch(IOException e)
            {
                LoggerManager.logError("Get an error "+e);
            }
        }
    }
    
    /**
     * Try to create a new DirectoryReader, if not exists, or update otherwise.
     * 
     */
    private void initSearcher(){
        
        if(directoryReader==null && indexWriter!=null)
        {
            try 
            {
                directoryReader = DirectoryReader.open(indexWriter,true);
            } 
            catch(IOException e) 
            {
                LoggerManager.logError("got an error- "+e);
            }
        }
        else
        {
            try 
            {   // Normally, Lucene index files are statics and wont be updated across sessions, that will always return null.
                DirectoryReader ndirectoryReader = DirectoryReader.openIfChanged(directoryReader);
                
                if(ndirectoryReader!=null)
                {   // Switch to the new one.
                    directoryReader.close();
                    directoryReader = ndirectoryReader;
                }
            } 
            catch(IOException e) 
            {
                LoggerManager.logError("failed to update the DirectoryReader "+e);
            }
        }
    }
    
    /**
     * Try to find a candidate for a given text inside a given domain.
     * 
     * @param text- the text to find by.
     * @param domain- the domain to look inside. Null goes for wide range queries.
     * 
     * @return an ID of a state, or null if bumped into an error.
     */
    public String findCandidate(String text, String domain){
        
        initWriter();
        initSearcher();
        
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        
        Query iquery= null;
        
        if(domain!=null)
        {
            iquery = new TermQuery(new Term(FIELD_DOMAIN,domain));
        }
        
        MoreLikeThis mlt = new MoreLikeThis(directoryReader);
        mlt.setAnalyzer(analyzer);
        mlt.setMinDocFreq(1);
        mlt.setMinTermFreq(1);
        mlt.setMinWordLen(1);
        mlt.setFieldNames(new String[]{FIELD_TAGS});
        
        try 
        {
            Reader reader = new StringReader(text);
            Query mquery = mlt.like(FIELD_TAGS,reader);
            
            BooleanQuery.Builder bquery = new BooleanQuery.Builder();
            bquery.add(mquery,BooleanClause.Occur.MUST);
            
            if(iquery!=null)
            {
                bquery.add(iquery,BooleanClause.Occur.MUST);
            }
            
            TopDocs tds = indexSearcher.search(bquery.build(),1);
            ScoreDoc[] sds = tds.scoreDocs;
            
            int len = sds.length;
            
            if(len==0)
            {
                return null;
            }
            
            ScoreDoc sd = sds[0];
            Document doc = indexSearcher.doc(sd.doc);
            String id = doc.get("id");
            
            return id;
        }
        catch(NullPointerException e)
        {
            LoggerManager.logError("fail the MLT due to there's no Lucene index files yet. "+e);
        }
        catch(IOException e) 
        {
            LoggerManager.logError("fail to MLT the given text ("+text+")\r\n"+e);
        }
         
        return null;
    }
    
    /**
      * Try to close this Lucene implementation.
      * As this code is shared, optionally, between threads, this needs to be called only once.
      * 
      */
     public void close(){
         
         if(indexWriter!=null)
         {
             try 
             {
                 LoggerManager.logInformation("start to close the TextIndexer.");
                 
                 indexWriter.close();
                 directoryReader.close();
                 indexWriter = null;
                 
                 LoggerManager.logInformation("TextIndexer is now closed.");
             } 
             catch(IOException e) 
             {
                 LoggerManager.logError("failed to close Lucene for the content indexer. "+e);
             }
         }
     }
}
