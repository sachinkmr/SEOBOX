/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachin.seobox.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author Sachin
 */
public class DBManager implements AutoCloseable {

    private static DBManager INSTANCE;
    private final MongoClient mongo;
    private final MongoDatabase mongoDB;

    public DBManager() {
        mongo = new MongoClient("localhost", 27017);
        mongoDB = mongo.getDatabase("SEOBOX");
    }

    
    public MongoDatabase getMongoDB() {
        return mongoDB;
    }

    @Override
    public void close() throws Exception {
        mongo.close();
        INSTANCE = null;
    }

}
