package com.sharkna.khaled.sharkna.model.db_utils;

import android.os.AsyncTask;
import android.util.Log;

import com.sharkna.khaled.sharkna.model.Comment;
import com.sharkna.khaled.sharkna.model.Post;

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

public class PerformNetworkRequestToGetLikesAndComments extends AsyncTask<Void, Void, String>  {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static final String TAG = PerformNetworkRequestToGetLikesAndComments.class.getName();
    IGetAllCommentsListener iGetAllCommentsListener;
    IGetAllLikesListener iGetAllLikesListener;
    IGetCommentsListener iGetCommentsListener;
    //the url where we need to send the request
    String url;
    String networkTask;

    //the parameters
    HashMap<String, String> params;
    ArrayList<Post> postsList;
    ArrayList<Comment> commentsList;
    ArrayList<Like> likesList;

    //the request code to define whether it is a GET or POST
    int requestCode;

    //constructor to initialize values
    public PerformNetworkRequestToGetLikesAndComments(String url, HashMap<String, String> params, int requestCode) {
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
                if (url.equalsIgnoreCase(DBHelper.URL_READ_ALL_COMMENTS)) {
                    fillCommentsList(object.getJSONArray("allcomments"));

                } else if (url.equalsIgnoreCase(DBHelper.URL_READ_ALL_LIKES)) {
                    fillLikesList(object.getJSONArray("alllikes"));
                } else if (url.equalsIgnoreCase(DBHelper.URL_READ_COMMENTS)) {
                    fillCommentsListForPost(object.getJSONArray("comments"));
                }

                //refreshHeroList(object.getJSONArray("heroes"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "onPostExecute: JSONException", e);
        }
    }

    private void fillLikesList(JSONArray alllikes) throws JSONException {
        //clearing previous heroes
//        heroList.clear();
        likesList = new ArrayList<>();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < alllikes.length(); i++) {
            //getting each hero object
            JSONObject obj = alllikes.getJSONObject(i);

            //adding the hero to the list
            likesList.add(new Like(
                    obj.getInt("id"),
                    obj.getInt("user_id"),
                    obj.getInt("post_id")
            ));
        }
        iGetAllLikesListener.onGetAllLikesResult(likesList);

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

    private void fillCommentsList(JSONArray posts) throws JSONException {
        //clearing previous heroes
//        heroList.clear();
        commentsList = new ArrayList<>();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < posts.length(); i++) {
            //getting each hero object
            JSONObject obj = posts.getJSONObject(i);

            //adding the hero to the list
            commentsList.add(new Comment(
                    obj.getInt("id"),
                    obj.getInt("user_id"),
                    obj.getInt("post_id"),
                    obj.getString("description")
            ));
        }
        iGetAllCommentsListener.onGetAllCommentsResult(commentsList);
    }

    public void setiGetAllCommentsListener(IGetAllCommentsListener iGetAllCommentsListener) {
        this.iGetAllCommentsListener = iGetAllCommentsListener;
    }

    public void setiGetAllLikesListener(IGetAllLikesListener iGetAllLikesListener) {
        this.iGetAllLikesListener = iGetAllLikesListener;
    }

    public void setiGetCommentsListener(IGetCommentsListener iGetCommentsListener) {
        this.iGetCommentsListener = iGetCommentsListener;
    }

    private void fillCommentsListForPost(JSONArray posts) throws JSONException {
        //clearing previous heroes
//        heroList.clear();
        commentsList = new ArrayList<>();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < posts.length(); i++) {
            //getting each hero object
            JSONObject obj = posts.getJSONObject(i);

            //adding the hero to the list
            commentsList.add(new Comment(
                    obj.getInt("id"),
                    obj.getInt("user_id"),
                    obj.getInt("post_id"),
                    obj.getString("description")
            ));
        }

        Log.d(TAG, "fillCommentsListForPost: ========="+commentsList.size());
        iGetCommentsListener.onGetCommentsResult(commentsList);
    }
}



//call get users in doInbackground
// whenever users arraived assign users to posts