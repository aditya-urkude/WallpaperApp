package com.example.hdwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

public class Wallpaper_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<WallpaperModel> imglist = new ArrayList<WallpaperModel>();
    RecyclerViewAdapter recyclerViewAdapter;
    int page_no = 1;
    String query_f = null;
    Boolean isScrollings = false;
    int currentItem, totalItem, scrolledItem;
    String url = "https://api.pexels.com/v1/curated/?page=";
    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(Wallpaper_Activity.this, imglist);
        recyclerView.setAdapter(recyclerViewAdapter);

        fetchWallpaper();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollings = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItem = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrolledItem = gridLayoutManager.findFirstVisibleItemPosition();

                if(isScrollings && (currentItem+scrolledItem == totalItem)){
                    // at the end
                    isScrollings = false;
                    fetchWallpaper();
                    Log.e("page_no", String.valueOf(page_no));
                }
            }
        });

//        fetchWallpaper();
    }

    public void fetchWallpaper(){

        StringRequest request = new StringRequest(Request.Method.GET, url+page_no+"&per_page=80&query="+query_f,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("photos");
                            int len = jsonArray.length();
                            for(int i = 0; i<len;i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("id");

                                JSONObject imgObj = object.getJSONObject("src");

                                String originalUrl = imgObj.getString("large2x");
                                String mediumUrl = imgObj.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumUrl);
                                imglist.add(wallpaperModel);

                            }

                            recyclerViewAdapter.notifyDataSetChanged();
                            page_no++;
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }// end
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization","Paste your key here ");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                query_f = query.toLowerCase();

                url = "https://api.pexels.com/v1/search/?page=";
                imglist.clear();
                fetchWallpaper();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


}