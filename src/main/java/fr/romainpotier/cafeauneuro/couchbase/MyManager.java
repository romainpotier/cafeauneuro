package fr.romainpotier.cafeauneuro.couchbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EBean;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.google.gson.Gson;

@EBean
public class MyManager {

    private Manager manager;
    private Database database;
    private Gson gson;

    public static final String DATABASE_NAME = "coffee";

    public MyManager(final Context context) {
        try {
            // create a manager
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            // create a new database
            database = manager.getDatabase(DATABASE_NAME);
            // Init gson
            gson = new Gson();
        } catch (IOException | CouchbaseLiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public <T> void addDocuments(final List<T> documentsToAdd) {
        for (T document : documentsToAdd) {
            createDocument(document);
        }
    }

    public void createDocument(final Object objectToCreate) {

        // create an object to hold document data
        final Map<String, Object> properties = new HashMap<String, Object>();

        // store document data
        properties.put("text", gson.toJson(objectToCreate));
        properties.put("check", Boolean.FALSE);
        properties.put("created_at", new Date().getTime());

        // create a new document
        Document document = database.createDocument();

        // store the data in the document
        try {
            document.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }

    public <T> List<T> getDocuments(final Class<T> clazz) {

        List<T> list = new ArrayList<>();

        QueryEnumerator result;
        try {
            result = database.createAllDocumentsQuery().run();
            for (Iterator<QueryRow> it = result; it.hasNext();) {
                QueryRow row = it.next();
                String json = (String) row.getDocument().getProperties().get("text");
                list.add(gson.fromJson(json, clazz));
            }
        } catch (CouchbaseLiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

}
