package com.sharkna.khaled.sharkna.model.db_utils;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class DBHelper {

    private static final String ROOT_URL = "http://192.168.1.103/SharknaAPI/v1/api.php/get?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_CREATE_POST = ROOT_URL + "createpost";
    public static final String URL_READ_USERS = ROOT_URL + "getusers";
    public static final String URL_READ_POSTS = ROOT_URL + "getposts";
    public static final String URL_READ_ALL_COMMENTS = ROOT_URL + "getallcomments";
    public static final String URL_READ_ALL_LIKES= ROOT_URL + "getalllikes";
    public static final String URL_READ_NUMBEROFLIKES = ROOT_URL + "getnumberoflikes";
    public static final String URL_READ_COMMENTS = ROOT_URL + "getcomments";
    public static final String URL_READ_USER = ROOT_URL + "getuser";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteuser&id=";
    public static final String URL_ADD_COMMENT = ROOT_URL + "addcomment";
    public static final String URL_DELETE_COMMENT = ROOT_URL + "deletecomment";
    public static final String URL_HIT_LIKE = ROOT_URL + "hitlike";
    public static final String URL_HIT_DISLIKE = ROOT_URL + "hitdislike";

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

}
