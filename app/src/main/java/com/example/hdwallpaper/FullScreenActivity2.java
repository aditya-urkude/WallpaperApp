package com.example.hdwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class FullScreenActivity2 extends AppCompatActivity {


    int pos;
    String original_url = "";
    ImageView full_image,shareImg,downloadImg, wallpaperImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen2);
        getSupportActionBar().hide();

        full_image = findViewById(R.id.full_Image);
        shareImg = findViewById(R.id.shareImg);
        downloadImg = findViewById(R.id.downloadImg);
        wallpaperImg = findViewById(R.id.wallpaperImg);

        pos = getIntent().getIntExtra("pos",0);
        original_url = getIntent().getStringExtra("imagelist");

        Glide.with(this).load(original_url).into(full_image);

        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareImage().execute(original_url);
            }
        });
        downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadImage().execute(original_url);

            }
        });
        wallpaperImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWallPaper().execute(original_url);
            }
        });
    }

    class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Toast.makeText(FullScreenActivity2.this, "Done Download", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);


                Bitmap icon = myBitmap;

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                int time = (int) (System.currentTimeMillis());
                File f = new File(Environment.getExternalStorageDirectory() + "/HD Wallpaper/" + time + "myimage" + ".jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(FullScreenActivity2.this, "Download successfully", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                e.printStackTrace();

            }
            return null;
        }
    }

    class ShareImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Toast.makeText(FullScreenActivity2.this, "Share Successfully", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);


                Bitmap b = myBitmap;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.setType("text/html");
                share.putExtra(Intent.EXTRA_TEXT, "image");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(FullScreenActivity2.this.getContentResolver(),
                        b, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                FullScreenActivity2.this.startActivity(Intent.createChooser(share, "Select"));


            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }
    }

    class SetWallPaper extends AsyncTask<String, Void, Bitmap> {

        Random random;
        int var;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Toast.makeText(FullScreenActivity2.this, "Wallpaper set Success", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Bitmap doInBackground(String... urls) {

            random = new Random();
            var = random.nextInt(10000);

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(FullScreenActivity2.this.getApplicationContext());
                try {
                    myWallpaperManager.setBitmap(myBitmap);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (Exception e) {

                e.printStackTrace();

            }
            return null;
        }
    }
}