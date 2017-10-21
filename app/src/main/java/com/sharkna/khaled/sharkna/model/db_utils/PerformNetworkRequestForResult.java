package com.sharkna.khaled.sharkna.model.db_utils;

import android.os.AsyncTask;
import android.util.Log;

import com.sharkna.khaled.sharkna.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class PerformNetworkRequestForResult extends AsyncTask<Void, Void, String> {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    public static final String CREATE_USER = "createuser";
    public static final String READ_USER =  "getusere";
    private static final String TAG = PerformNetworkRequestForResult.class.getName();
    //the url where we need to send the request
    String url;
    String networkTask;
    IGetUserListener iGetUserListener;

    //the parameters
    HashMap<String, String> params;

    //the request code to define whether it is a GET or POST
    int requestCode;

    //constructor to initialize values
    public PerformNetworkRequestForResult(String url, HashMap<String, String> params, int requestCode) {
        this.url = url;
        this.params = params;
        this.requestCode = requestCode;
    }

    //when the task started displaying a progressbar
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar.setVisibility(View.VISIBLE);
    }


    //this method will give the response from the request
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        progressBar.setVisibility(GONE);
        try {
//            Log.d(TAG, "onPostExecute: "+s);
            JSONObject object = new JSONObject(s);
            if (!object.getBoolean("error")) {
//                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                //refreshing the herolist after every operation
                //so we get an updated list
                //we will create this method right now it is commented
                //because we haven't created it yet
                if (url == DBHelper.URL_READ_USER) {
                    handleJsonUserData(object.getJSONObject("user"));
                }

                //refreshHeroList(object.getJSONArray("heroes"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "onPostExecute: JSONException", e);
        }
    }

    //the network operation will be performed in background
    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();

        if (requestCode == CODE_POST_REQUEST)
            return requestHandler.sendPostRequest(url, params);

        if (requestCode == CODE_GET_REQUEST)
            return requestHandler.sendGetRequest(url);

        return null;
    }

    private void handleJsonUserData(JSONObject jsonUserObject) {
        User user = new User();
        try {
            user.setId(jsonUserObject.getInt("id"));
            user.setUserName(jsonUserObject.getString("user_name"));
            user.setFirstName(jsonUserObject.getString("first_name"));
            user.setLastName(jsonUserObject.getString("last_name"));
            user.setEmail(jsonUserObject.getString("email"));
            user.setImageURL(jsonUserObject.getString("image_url"));
            user.setPoints(jsonUserObject.getInt("points"));
            user.setPassword(jsonUserObject.getString("password"));
        } catch (JSONException e) {
            Log.e(TAG, "handleJsonUserData: JSONExceptoin",e );
        }
        iGetUserListener.onGetUserResult(user);
    }

    public void setIGetUserListener(IGetUserListener iGetUserListener) {
        this.iGetUserListener = iGetUserListener;
    }

    /*private void refreshHeroList(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        heroList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);

            //adding the hero to the list
            heroList.add(new Hero(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("realname"),
                    obj.getInt("rating"),
                    obj.getString("teamaffiliation")
            ));
        }

        //creating the adapter and setting it to the listview
        HeroAdapter adapter = new HeroAdapter(heroList);
        listView.setAdapter(adapter);
    }*/

}