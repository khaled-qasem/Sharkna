package com.sharkna.khaled.sharkna.ui.activity;


public interface ICheckSelfPermissionActivity {
    int checkSelfPermission(String permission);
    void requestPermissions(String[] permissions, int requestCode);
}

