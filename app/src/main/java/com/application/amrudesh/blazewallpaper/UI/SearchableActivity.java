package com.application.amrudesh.blazewallpaper.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.Model.NewImageAdapter;
import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Constants;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchableActivity extends AppCompatActivity {
    String SEARCH_STATEMENT;
    @BindView(R.id.search_view2)
    MaterialSearchView searchView;
    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;
    List<Wallpaper> wallpaperList;
    RequestQueue queue;
    NewImageAdapter newImageAdapter;
    private GridLayoutManager gridLayoutManager;
    private int pageNo = 1;
    private int visibleItemCount = 0;
    private int pastVisibleItems = 0;
    private int total_Images = 0;
    private int previousTotal = 0;
    private int view_thershold = 0;
    private Boolean isLoading = true;
    @BindView(R.id.search_animation)
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable_activity);
        ButterKnife.bind(this);
        SEARCH_STATEMENT = getIntent().getStringExtra("query");
        wallpaperList = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        getImages(pageNo, SEARCH_STATEMENT);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        newImageAdapter = new NewImageAdapter(this, wallpaperList);
        recyclerView.setAdapter(newImageAdapter);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SEARCH_STATEMENT = query;
                wallpaperList.clear();
                newImageAdapter.notifyDataSetChanged();
                getImages(pageNo, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setHint(SEARCH_STATEMENT);
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        Log.i("QUERY", SEARCH_STATEMENT);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = gridLayoutManager.getChildCount();
                total_Images = gridLayoutManager.getItemCount();
                pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    if (isLoading) {
                        if (total_Images > previousTotal) {
                            isLoading = false;
                            previousTotal = total_Images;
                        }
                    }
                    if (!isLoading && (total_Images - visibleItemCount) <= pastVisibleItems + view_thershold) {

                        pageNo++;
                        getImages(pageNo, SEARCH_STATEMENT);
                        isLoading = true;
                    }
                }
            }

        });
    }

    private List<Wallpaper> getImages(int page_no, String search) {
        Log.i("SIZE", "Executed");
        JsonObjectRequest wallpaperRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.Search_Link_Left + page_no + "&query=" +
                        search + Constants.Search_Link_right, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i("RESPONSE", response.getString("total"));
                            if (response.getString("total").equals("0")) {
                                animationView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            JSONArray jsonArray = new JSONArray(response.getString("results"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject url = new JSONObject(jsonObject.getString("urls"));
                                JSONObject author = new JSONObject(jsonObject.getString("user"));
                                Wallpaper wallpaper = new Wallpaper();
                                wallpaper.setId(jsonObject.getString("id"));
                                wallpaper.setWallpaper_URL_Thump(url.getString("thumb"));
                                wallpaper.setWallpaper_URL(url.getString("full"));
                                wallpaper.setAuthor_name(author.getString("name"));
                                wallpaper.setFav_Btn(false);
                                wallpaperList.add(wallpaper);


                                if (wallpaperList.isEmpty()) {
                                    animationView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                Log.i("RESPONSE", wallpaper.getWallpaper_URL());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        newImageAdapter.notifyDataSetChanged();
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", error.toString());
                    }
                });
        queue.add(wallpaperRequest);
        return wallpaperList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}
