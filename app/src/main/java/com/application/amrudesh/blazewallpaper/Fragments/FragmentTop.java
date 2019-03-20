package com.application.amrudesh.blazewallpaper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.application.amrudesh.blazewallpaper.BuildConfig;
import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.Model.NewImageAdapter;
import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTop extends Fragment {

    private List<Wallpaper> wallpaperList;
    private RequestQueue requestQueue;
    @BindView(R.id.top_rated_recyclerview)
    RecyclerView recyclerView;
    AdView adView;
    NewImageAdapter newImageAdapter;

    public FragmentTop() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toprated_fragment, container, false);
        wallpaperList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ButterKnife.bind(this, view);
        getWallpaperList();
        adView = (AdView) view.findViewById(R.id.adView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        newImageAdapter = new NewImageAdapter(getActivity(), wallpaperList);
        recyclerView.setAdapter(newImageAdapter);


        if (!BuildConfig.IS_PAID) {

            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        } else {
           adView.setVisibility(View.GONE);
           adView.destroy();
        }
        return view;
    }

    private List<Wallpaper> getWallpaperList() {
        JsonArrayRequest wallpaperRequest = new JsonArrayRequest
                (Request.Method.GET, Constants.TOP_RATED_IMAGE_LINK, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject url = new JSONObject(jsonObject.getString("urls"));
                                JSONObject author = new JSONObject(jsonObject.getString("user"));
                                Wallpaper wallpaper = new Wallpaper();
                                wallpaper.setId(jsonObject.getString("id"));
                                wallpaper.setWallpaper_URL(url.getString("small"));
                                wallpaper.setAuthor_name(author.getString("name"));
                                wallpaper.setFav_Btn(false);
                                wallpaperList.add(wallpaper);
                                Log.i("TAG", wallpaper.getWallpaper_URL());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        newImageAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", error.toString());
                    }
                });
        requestQueue.add(wallpaperRequest);
        return wallpaperList;
    }
}