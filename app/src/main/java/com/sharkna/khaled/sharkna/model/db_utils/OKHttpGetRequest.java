package com.sharkna.khaled.sharkna.model.db_utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Khaled on 10/19/2017.
 * Assumptions
 * Descriptions
 */

@SuppressWarnings("ConstantConditions")
public class OKHttpGetRequest extends AsyncTask<Void, Void, Void> {

    private static final String TAG = OKHttpGetRequest.class.getName();
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SuppressWarnings("ConstantConditions")
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private String postRequest(String url) throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("user_name", "khaled")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    @SuppressWarnings("unused")
    public String getRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        String getRequestURL = "http://192.168.1.103/SharknaAPI/v1/api.php/get?apicall=getusers";
        String postRequestURL = "http://192.168.1.103/SharknaAPI/v1/api.php/get?apicall=getuser";
        String response = null;
        try {
            //test okhhtps method for get request
//            response = example.getRequest(getRequestURL);
            //test okhhtps method for post request
//            response = postRequest(postRequestURL);

            //test getRequest in RequestHandler
//            RequestHandler requestHandler = new RequestHandler();
//            response = requestHandler.sendGetRequest(getRequestURL);

            //test postRequest in RequestHandler
//            RequestHandler requestHandler = new RequestHandler();
//            HashMap<String, String> urlParams = new HashMap<>();
//            urlParams.put("user_name", "ameen");
//            response = requestHandler.sendPostRequest(postRequestURL,urlParams);



            Log.d(TAG, "doInBackground: \n" + response);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: Exception", e);
        }
        return null;
    }

}
