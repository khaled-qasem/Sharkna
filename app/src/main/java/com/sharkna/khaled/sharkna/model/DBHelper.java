package com.sharkna.khaled.sharkna.model;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class DBHelper {
    
    private static final String ROOT_URL = "http://192.168.1.103/SharknaAPI/v1/api.php/get?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_READ_USERS = ROOT_URL + "getuseres";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteuser&id=";

}
