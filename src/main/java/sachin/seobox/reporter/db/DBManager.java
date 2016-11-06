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
	    BasicDBObject query = new BasicDBObject("id", "faf6e0b1-777a-45ba-95ce-1bcb05c9ea4b");
	    FindIterable<Document> iterable = m.getDatabase("SEOBOX").getCollection("06-November-2016_08-43-32-191PM")
		    .find(query).sort(new BasicDBObject("_id", 1));
	    long l = 0l;
	    for (Document d : iterable) {
		d.clear();
		l++;
	    }
	    System.out.println(l);
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
