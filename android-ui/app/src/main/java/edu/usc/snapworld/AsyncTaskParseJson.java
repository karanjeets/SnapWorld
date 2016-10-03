package edu.usc.snapworld;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AsyncTaskParseJson extends AsyncTask<String, String, JSONObject> {
    final String TAG = "AsyncTaskParseJson.java";

    String yourJsonStringUrl="";

    Constants.RequestType requestType;

    JSONObject data = new JSONObject();

    JSONArray dataJsonArr = null;




    public interface AsyncResponse {
        void processFinish(JSONObject output);
    }

    public AsyncResponse asynch = null;

    public AsyncTaskParseJson(AsyncResponse asyncResponse) {
        asynch = asyncResponse;
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected JSONObject doInBackground(String... arg0) {
        JSONParser jParser = new JSONParser();
        JSONObject json = new JSONObject();
        if(requestType == Constants.RequestType.GET_CATEGORY) {
            json = jParser.getJSONFromUrl(yourJsonStringUrl);
        } else if(requestType == Constants.RequestType.PUT_DETAILS) {
            json = jParser.postJSONFromUrl(yourJsonStringUrl, data);
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject strFromDoInBg) {
        super.onPostExecute(strFromDoInBg);

        System.out.println("Hello");
        asynch.processFinish(strFromDoInBg);

    }
}


