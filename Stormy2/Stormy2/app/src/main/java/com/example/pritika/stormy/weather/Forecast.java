package com.example.pritika.stormy.weather;

import com.example.pritika.stormy.R;

/**
 * Created by Pritika on 30-01-2016.
 */
public class Forecast {
    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private  Day[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }
    public static int getIconId(String iconString){
        int IconId = R.drawable.clear_day;
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night. (Developers should ensure that a sensible default is defined, as additional values, such as hail, thunderstorm, or tornado, may be defined in the future.)
        if( iconString.equals("clear-day")){
            IconId = R.drawable.clear_day;
        }
        else if(iconString.equals("clear-night")) {
            IconId = R.drawable.clear_night;

        }
        else if (iconString.equals("rain")) {
            IconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            IconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            IconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            IconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            IconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            IconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            IconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            IconId = R.drawable.cloudy_night;
        }
        return IconId ;

    }
}
