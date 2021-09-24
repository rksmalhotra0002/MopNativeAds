package com.twitter.ads.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;
import com.twitter.ads.MyMoPub;
import com.twitter.ads.R;
import com.twitter.ads.adapter.RecyclerviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewActivity extends AppCompatActivity {

private RecyclerView recyclerview;

private RecyclerviewAdapter recyclerviewAdapter;

private List<String>listtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        initializeViews();

    }

    private void initializeViews()
    {

        new MyMoPub().init(this, "11a17b188668469fb0412708c3d16813");

        recyclerview=findViewById(R.id.recyclerview);

        listtext=new ArrayList<>();

        listtext.add("item 1");
        listtext.add("item 2");
        listtext.add("item 3");
        listtext.add("item 4");
        listtext.add("item 5");
        listtext.add("item 6");
        listtext.add("item 7");
        listtext.add("item 8");
        listtext.add("item 9");
        listtext.add("item 10");
        listtext.add("item 11");
        listtext.add("item 12");
        listtext.add("item 13");
        listtext.add("item 14");
        listtext.add("item 15");

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewAdapter=new RecyclerviewAdapter(this,listtext);
        recyclerview.setAdapter(recyclerviewAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                initNativeAds();

            }
        },200);

        recyclerviewAdapter.notifyDataSetChanged();
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setHasFixedSize(true);

    }

    private void initNativeAds()
    {

        MoPubRecyclerAdapter myMoPubAdapter = new MoPubRecyclerAdapter(this, recyclerviewAdapter);

        // Create an ad renderer and view binder that describe your native ad layout.

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.mopub_native_ad_view)
                .titleId(R.id.mopub_native_ad_title)
                .textId(R.id.mopub_native_ad_text)
                .mainImageId(R.id.mopub_native_ad_main_imageview)
                .iconImageId(R.id.mopub_native_ad_icon)
                .callToActionId(R.id.mopub_native_ad_cta)
                .privacyInformationIconImageId(R.id.mopub_native_ad_privacy)
                .build();

        MoPubStaticNativeAdRenderer myRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        myMoPubAdapter.registerAdRenderer(myRenderer);

        // Set up the RecyclerView and start loading ads
        recyclerview.setAdapter(myMoPubAdapter);

        myMoPubAdapter.loadAds("11a17b188668469fb0412708c3d16813");

        Log.e("MoPub", "Native ad is loading.");

    }
}
