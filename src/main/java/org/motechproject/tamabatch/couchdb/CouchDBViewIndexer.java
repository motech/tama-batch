package org.motechproject.tamabatch.couchdb;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CouchDBViewIndexer {

    Logger log = Logger.getLogger(CouchDBViewIndexer.class.getName());

    public void indexAllViews() throws IOException, JSONException {
        log.entering(CouchDBViewIndexer.class.getName(), "indexAllViews");
        for (String database : CouchDbMetaData.getApplicationDatabases()) {
            try {
                indexAllViewsInDatabase(database);
            } catch (Exception e) {
                log.severe(e.getMessage());
            }
        }
        log.exiting(this.getClass().getName(), "indexAllViews");
    }

    private void indexAllViewsInDatabase(String databaseName) throws IOException, JSONException {
        final List<String> designDocNames = CouchDbMetaData.getDesignDocNames(databaseName);
        for (String designDocName : designDocNames) {
            indexView(databaseName, designDocName);
        }
    }

    private void indexView(String databaseName, String designDocName) throws IOException {
        log.info("Indexing " + databaseName + " : " + designDocName);
        String url = CouchDbMetaData.getUrlForView(databaseName, designDocName);
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.execute(new HttpGet(url));
    }

}
