package sachin.seobox.reporter.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;



public class DBManager {

    public static void main(String ar[]) {
	MongoClient m = new MongoClient("localhost", 27017);
	try {
	    BasicDBObject query = new BasicDBObject("id" , "ad4625b5-1450-40f0-985d-49fa5289c7ba");
	    FindIterable<Document> iterable = m.getDatabase("SEOBOX").getCollection("25-October-2016_08-25-08-600PM").find(query);
	    for (Document doc : iterable) {
		System.out.println(doc.toJson());		
	    }
	} catch (Exception ex) {
	    System.out.println(ex);
	} finally {
	    m.close();
	}
    }
    public static List<Document> pagination(MongoCollection<Document> persons, int skip, int pageSize) {
        List<Document> list = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = persons.find().skip(skip).limit(pageSize).iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    list.add(doc);
                    System.out.println(doc.toJson());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
