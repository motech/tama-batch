package org.motechproject.tamabatch.couchdb;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class CouchDbCompactor {

    Logger log = Logger.getLogger(CouchDBViewIndexer.class.getName());

    public void compactAllDatabases() throws IOException, JSONException {
        log.entering(CouchDBViewIndexer.class.getName(), "compactAllDBs");
        for (String applicationDatabase : CouchDbMetaData.getApplicationDatabases()) {
            try {
                compactDatabase(applicationDatabase);
            } catch (Exception e) {
                log.severe(e.getMessage());
            }
        }
        log.exiting(this.getClass().getName(), "compactAllDBs");
    }

    private void compactDatabase(String applicationDatabase) throws IOException, JSONException {
        compact(CouchDbMetaData.getUrlForCompaction(applicationDatabase));
        for (String designDocName : CouchDbMetaData.getDesignDocNames(applicationDatabase)) {
            compact(CouchDbMetaData.getUrlForCompaction(applicationDatabase, designDocName));
        }
    }

    private boolean compact(String urlForCompaction) throws IOException, JSONException {
        log.info("Compacting : " + urlForCompaction);
        HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(urlForCompaction);
        httpPost.setHeader(new BasicHeader("Content-Type", "application/json"));
        final String response = httpClient.execute(httpPost, new BasicResponseHandler());
        final JSONObject jsonResult = new JSONObject(response);
        return jsonResult.getBoolean("ok");
    }
}
