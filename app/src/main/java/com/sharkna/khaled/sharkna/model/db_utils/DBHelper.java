package com.sharkna.khaled.sharkna.model.db_utils;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class DBHelper {
    
    private static final String ROOT_URL = "http://192.168.1.103/SharknaAPI/v1/api.php/get?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_CREATE_POST= ROOT_URL + "createpost";
    public static final String URL_READ_USERS = ROOT_URL + "getusers";
    public static final String URL_READ_POSTS= ROOT_URL + "getposts";
    public static final String URL_READ_USER = ROOT_URL + "getuser";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteuser&id=";
    public static final String CREATE_USER = "createuser";
    public static final String READ_USER =  "getusere";

}
