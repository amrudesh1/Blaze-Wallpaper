package com.application.amrudesh.blazewallpaper.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchable_activity);
        ButterKnife.bind(this);
        SEARCH_STATEMENT = getIntent().getStringExtra("query");
        wallpaperList = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        getImages(SEARCH_STATEMENT);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        newImageAdapter = new NewImageAdapter(this, wallpaperList);
        recyclerView.setAdapter(newImageAdapter);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SEARCH_STATEMENT = query;
                wallpaperList.clear();
                newImageAdapter.notifyDataSetChanged();
                getImages(query);
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
    }

    private List<Wallpaper> getImages(String search) {
        JsonObjectRequest wallpaperRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.Search_Link_Left + search + Constants.Search_Link_right, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.getString("results"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject url = new JSONObject(jsonObject.getString("urls"));
                                JSONObject author = new JSONObject(jsonObject.getString("user"));
                                Wallpaper wallpaper = new Wallpaper();
                                wallpaper.setId(jsonObject.getString("id"));
                                wallpaper.setAuthor_name(author.getString("username"));
                                wallpaper.setWallpaper_URL(url.getString("small"));
                                wallpaper.setFav_Btn(false);
                                wallpaperList.add(wallpaper);
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
