package com.example.pritika.stormy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pritika.stormy.R;
import com.example.pritika.stormy.adapters.HourAdapter;
import com.example.pritika.stormy.weather.Hour;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends AppCompatActivity {

    private Hour[] mHours;
@Bind(R.id.recyclerView) RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);
    Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours  = Arrays.copyOf(parcelables,parcelables.length, Hour[].class);
        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


    }


}
