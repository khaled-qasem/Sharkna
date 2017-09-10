package com.sharkna.khaled.sharkna.ui.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

/**
 * Activity using this class is {@link ICheckSelfPermissionActivity}
 * Permissions are still added statically as if date of creation
 */
class PermissionHandler {

    private static final int DEFAULT_PERMISSION_REQUEST_CODE = 200;
    // TODO: 08-Nov-16 make dynamic bindListeners
    private final static String[] sPermissionsToRequest = {
            "android.permission.ACCESS_COARSE_LOCATION"
            , "android.permission.ACCESS_FINE_LOCATION"
            , "android.permission.ACCESS_NETWORK_STATE"
            , "android.permission.INTERNET"
            , "android.permission.CAMERA"
            , "android.permission.READ_EXTERNAL_STORAGE"
            , "android.permission.WRITE_EXTERNAL_STORAGE"
            , "android.permission.READ_PHONE_STATE"
            , "android.permission.GET_ACCOUNTS"
            , "android.permission.READ_PROFILE"
            , "android.permission.READ_CONTACTS"
    };
    private static final String TAG = PermissionHandler.class.getName();


    boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * Assumes requested before using permission, otherwise, won't work first time
     *
     * @param activity supporting marshmallow permissions
     */
    void askForPermissions(ICheckSelfPermissionActivity activity) {
        ArrayList<String> permissionsNotGranted = new ArrayList<>(sPermissionsToRequest.length);
        for (String p :
                sPermissionsToRequest) {
            if (!hasPermission(activity, p)) {
                permissionsNotGranted.add(p);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            String[] permissions = new String[permissionsNotGranted.size()];
            for (int i = 0; i < permissionsNotGranted.size(); i++) {
                permissions[i] = permissionsNotGranted.get(i);
            }
            activity.requestPermissions(permissions, DEFAULT_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean hasPermission(ICheckSelfPermissionActivity checkSelfPermissionActivity, String permission) {
        //noinspection SimplifiableIfStatement
        if (canMakeSmores()) {
            return (checkSelfPermissionActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;

    }

    void handleRequest(int requestCode, String[] permissions, int[] grantResults, Context context) {
        switch (requestCode) {
            case DEFAULT_PERMISSION_REQUEST_CODE:
                Log.d(TAG, "onRequestPermissionsResult: ----------------------------------------------------------200");
                break;
            default:
                Log.d(TAG, "onRequestPermissionsResult: ----------------------------------------------------------500");
               break;
        }
    }
}
