package com.example.hdwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        checkPermission();

    }

    public void checkPermission(){

        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Thread thread = new Thread(){
                            public void run(){

                                try {
                                    sleep(3000);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                finally {
                                    Intent intent = new Intent(MainActivity.this, Wallpaper_Activity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        };thread.start();

                        // creating directory
                        File f = new File(Environment.getExternalStorageDirectory() + "/HD Wallpaper");

                        if (!f.exists()) {
                            f.mkdirs();
                            f.mkdir();
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                Uri uri = Uri.fromParts("package",getPackageName(),null);
                                intent.setData(uri);
                                startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}