package recipebook;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Mongo {
    private static MongoDatabase mongoDatabase;

    public Mongo() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        mongoDatabase = mongoClient.getDatabase("project");
        mongoDatabase.createCollection("recipes");

    }

    public boolean setData(String name, String description) {

        MongoCollection<Document> collection = mongoDatabase.getCollection("recipes");
        Document doc = collection.find(eq("name", name)).first();
        if (doc != null) {
            return false;
        }
        Document newRecipe = new Document()
                .append("name", name)
                .append("description", description);
        try {
            InsertOneResult result = collection.insertOne(newRecipe);
        } catch (MongoException me) {
            System.out.println("Exception in setData");
        }
        return true;
    }

    public String[] getData(String name) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("recipes");
        Document doc = collection.find(eq("name", name)).first();
        String[] ret = {null, null};
        if (doc != null) {
            ret[0] = (String) doc.get("name");
            ret[1] = (String) doc.get("description");
        }
        return ret;
    }

    public boolean delData(String name) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("recipes");
        Bson query = eq("name", name);
        try {
            DeleteResult result = collection.deleteOne(query);
        } catch (MongoException me) {
            return false;
        }
        return true;
    }

    public List<String[]> getAllData() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("recipes");
        MongoCursor<Document> cursor = collection.find().iterator();
        List<String[]> ret = new ArrayList<String[]>();
        try {
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                String name = doc.getString("name");
                String description = doc.getString("description");

                ret.add(new String[]{name, description});
            }
        } finally {
            cursor.close();
        }
        return ret;
    }

}
