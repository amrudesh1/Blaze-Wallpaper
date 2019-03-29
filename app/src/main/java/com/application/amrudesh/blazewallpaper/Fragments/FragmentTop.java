package com.application.amrudesh.blazewallpaper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTop extends Fragment {

    private List<Wallpaper> wallpaperList;
    private RequestQueue requestQueue;
    @BindView(R.id.top_rated_recyclerview)
    RecyclerView recyclerView;
    AdView adView;
    NewImageAdapter newImageAdapter;
    @BindView(R.id.animation_view_main2)
    LottieAnimationView animationView;
    @BindView(R.id.toprated_refresh)
    SwipeRefreshLayout layout2;
    private GridLayoutManager gridLayoutManager;
    private int pageNo = 1;
    private int visibleItemCount = 0;
    private int pastVisibleItems = 0;
    private int total_Images = 0;
    private int previousTotal = 0;
    private int view_thershold = 0;
    private Boolean isLoading = true;

    public FragmentTop() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toprated_fragment, container, false);
        wallpaperList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ButterKnife.bind(this, view);
        animationView.playAnimation();
        getWallpaperList(pageNo);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        adView = (AdView) view.findViewById(R.id.adView);
        recyclerView.setLayoutManager(gridLayoutManager);
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
        layout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wallpaperList.clear();
                getWallpaperList(1);
                newImageAdapter.notifyDataSetChanged();

            }
        });
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
                        getWallpaperList(pageNo);
                        isLoading = true;
                    }
                }
            }

        });
        return view;
    }

    private List<Wallpaper> getWallpaperList(int page_no) {
        JsonArrayRequest wallpaperRequest = new JsonArrayRequest
                (Request.Method.GET, Constants.TOP_RATED_IMAGE_LINK_LEFT + page_no + Constants.TOP_RATED_IMAGE_LINK_RIGHT
                        , null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("URL", Constants.TOP_RATED_IMAGE_LINK_LEFT + page_no + Constants.TOP_RATED_IMAGE_LINK_RIGHT);
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject url = new JSONObject(jsonObject.getString("urls"));
                                JSONObject author = new JSONObject(jsonObject.getString("user"));
                                Wallpaper wallpaper = new Wallpaper();
                                wallpaper.setId(jsonObject.getString("id"));
                                wallpaper.setWallpaper_URL_Thump(url.getString("thumb"));
                                wallpaper.setWallpaper_URL(url.getString("full"));
                                wallpaper.setPortFolio_url(author.getString("portfolio_url"));
                                wallpaper.setAuthor_name(author.getString("name"));
                                wallpaper.setFav_Btn(false);
                                wallpaperList.add(wallpaper);
                                Log.i("TAG", wallpaper.getWallpaper_URL());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        newImageAdapter.notifyDataSetChanged();
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);
                        layout2.setRefreshing(false);
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