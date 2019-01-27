package com.apdallahy3.marvelcharcters.Views.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apdallahy3.marvelcharcters.Controllers.Controller;
import com.apdallahy3.marvelcharcters.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //showing marvel logo centered in the actionbar using custom view
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_logo);

        //check for Writing External Storage premission if denied ask for it
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int checkPremisiion=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkPremisiion==PackageManager.PERMISSION_DENIED ){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }else{
                Controller.getContollerInstance().savePrefrance("write_storage","true",this);
            }
        }else{
            Controller.getContollerInstance().savePrefrance("write_storage","true",this);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                        if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            Controller.getContollerInstance().savePrefrance("write_storage","true",this);

                        }else{
                            Controller.getContollerInstance().savePrefrance("write_storage","false",this);

                        }
                    }

            }
        }



}




