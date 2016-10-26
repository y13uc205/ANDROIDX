package com.example.pritika.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.pritika.stormy.R;
import com.example.pritika.stormy.adapters.DayAdapters;
import com.example.pritika.stormy.weather.Day;

import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {

private Day[]  mDays;
private ListView mDaysView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);


        Intent intent =  getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST); //PARCELABLE IS A JAVA INTERFACE
        mDays = Arrays.copyOf(parcelables, parcelables.length , Day[].class);
        DayAdapters adapter = new DayAdapters(this, mDays);
        setListAdapter(adapter);
    }
}
