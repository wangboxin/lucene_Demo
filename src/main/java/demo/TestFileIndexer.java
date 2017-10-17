package demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Date;
        
    public class TestFileIndexer {    
        public static void main(String[] args) throws Exception {    
            /* 指明要索引文件夹的位置,这里是C盘的source文件夹下 */    
           File fileDir = new File("C:\\source");    
        	
            /* 这里放索引文件的位置 */    
            //File indexDir = new File("c:\\index"); 
            String indexPath = "c:\\index";
            
            // Directory dir = FSDirectory.open(indexDir);    //v3.6.0
            Directory dir = FSDirectory.open(Paths.get(indexPath));  
            
            //Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_3_6_0); 
            Analyzer luceneAnalyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(luceneAnalyzer);  
            iwc.setOpenMode(OpenMode.CREATE);  
            IndexWriter indexWriter = new IndexWriter(dir,iwc);    
            File[] textFiles = fileDir.listFiles();    
            long startTime = new Date().getTime();    
                
            //增加document到索引去    
            for (int i = 0; i < textFiles.length; i++) {    
                if (textFiles[i].isFile()    
                       ) {    
                    System.out.println("File " + textFiles[i].getCanonicalPath()    
                            + "正在被索引....");    
                    String temp = FileReaderAll(textFiles[i].getCanonicalPath(),    
                            "GBK");    
                    System.out.println(temp);    
                    Document document = new Document();    
                  //  Field FieldPath = new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.NO);   //v3.6.0的写法   
                   // Field FieldBody = new Field("body", temp, Field.Store.YES,  Field.Index.ANALYZED,  Field.TermVector.WITH_POSITIONS_OFFSETS);    
                  
                    Field FieldPath = new StringField("path", textFiles[i].getPath(), Field.Store.YES);
                    Field FieldBody = new TextField("body", temp, Field.Store.YES);    
                    document.add(FieldPath);    
                    document.add(FieldBody);    
                    indexWriter.addDocument(document);    
                }    
            }    
            indexWriter.close();    
                
            //测试一下索引的时间    
            long endTime = new Date().getTime();    
            System.out    
                    .println("这占用了"    
                            + (endTime - startTime)    
                            + " 毫秒来把文档增加到索引里面去!"    
                            + fileDir.getPath());    
        }    
        
        public static String FileReaderAll(String FileName, String charset)    
                throws IOException {    
            BufferedReader reader = new BufferedReader(new InputStreamReader(    
                    new FileInputStream(FileName), charset));    
            String line = new String();    
            String temp = new String();    
                
            while ((line = reader.readLine()) != null) {    
                temp += line;    
            }    
            reader.close();    
            return temp;    
        }    
    }    