package com.sharkna.khaled.sharkna.model.db_utils;

import android.os.AsyncTask;
import android.util.Log;

import com.sharkna.khaled.sharkna.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class PerformNetworkRequestToGetUsers extends AsyncTask<Void, Void, String> {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static final String TAG = PerformNetworkRequestToGetUsers.class.getName();
    private IGetUsersListener iGetUsersListener;
    //the url where we need to send the request
    String url;
    String networkTask;

    //the parameters
    HashMap<String, String> params;

    //the request code to define whether it is a GET or POST
    int requestCode;

    //constructor to initialize values
    public PerformNetworkRequestToGetUsers(String url, HashMap<String, String> params, int requestCode) {
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
        Log.d(TAG, "onPostExecute: " + s);
        try {
            JSONObject object = new JSONObject(s);
            if (!object.getBoolean("error")) {
//                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                //refreshing the herolist after every operation
                //so we get an updated list
                //we will create this method right now it is commented
                //because we haven't created it yet
                if (url.equalsIgnoreCase(DBHelper.URL_READ_USERS)) {
                    fillUsersList(object.getJSONArray("users"));
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
            Log.e(TAG, "handleJsonUserData: JSONExceptoin", e);
        }
    }

    public void setiGetUsersListener(IGetUsersListener iGetUsersListener) {
        this.iGetUsersListener = iGetUsersListener;
    }

    private void fillUsersList(JSONArray users) throws JSONException {
        //clearing previous heroes
//        heroList.clear();
        ArrayList usersList = new ArrayList();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < users.length(); i++) {
            //getting each hero object
            JSONObject obj = users.getJSONObject(i);

           /* $user['id'] = $id;
            $user['user_name'] = $user_name;
            $user['first_name'] = $first_name;
            $user['last_name'] = $last_name;
            $user['image_url'] = $image_url;
            $user['email'] = $email;
            $user['points'] = $points;
            $user['password'] = $password;*/

            //adding the hero to the list
            usersList.add(new User(
                    obj.getInt("id"),
                    obj.getString("user_name"),
                    obj.getString("first_name"),
                    obj.getString("last_name"),
                    obj.getString("image_url"),
                    obj.getString("email"),
                    obj.getInt("points"),
                    obj.getString("password")
            ));
        }
        iGetUsersListener.onGetUsersResult(usersList);
        //creating the adapter and setting it to the listview
    }
}

//call get users in doInbackground
// whenever users arraived assign users to posts