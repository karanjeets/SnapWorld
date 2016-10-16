package edu.usc.snapworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTaskParseJson extends AsyncTask<String, String, JSONObject> {
    final String TAG = "AsyncTaskParseJson.java";

    String url="";

    Constants.RequestType requestType;

    MultipartEntityBuilder data = MultipartEntityBuilder.create();

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
        if(requestType == Constants.RequestType.GET) {
            /** Sample Code to Read Image from URL
            try {
                URL url = new URL("http://104.197.77.81/snapdata/monica/2016-10-02-23-42-05.jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Constants.temp = bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } **/
            json = jParser.getJSONFromUrl(url);
        } else if(requestType == Constants.RequestType.PUT_DETAILS) {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost pushData = new HttpPost(url);
                HttpEntity multipartEntity = data.build();
                pushData.setEntity(multipartEntity);
                CloseableHttpResponse response = httpClient.execute(pushData);
                HttpEntity responseEntity = response.getEntity();
                Log.i("POST Response", responseEntity.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
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


