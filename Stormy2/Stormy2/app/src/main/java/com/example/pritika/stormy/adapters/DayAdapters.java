package com.example.pritika.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pritika.stormy.R;
import com.example.pritika.stormy.weather.Day;

/**
 * Created by Pritika on 03-02-2016.
 */
public class DayAdapters extends BaseAdapter {
    private Context mContext;
    private Day[] mDays;
    public DayAdapters(Context context, Day[] days){
        mContext =  context;
        mDays = days;
    }



    @Override
    public int getCount() {
        return mDays.length ;

    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // not going to use it.Tag item for preference
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item , null);
            holder = new ViewHolder();
            holder.IconimageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel =(TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel =(TextView) convertView.findViewById(R.id.dayNameLabel);

      convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Day day = mDays[position];
        holder.IconimageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() +"");
        if(position == 0){
            holder.dayLabel.setText("Today");
        }
        else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        return convertView;
    }
    private static class ViewHolder{   //for recycling view
        ImageView IconimageView ;//make it public
        TextView temperatureLabel;
        TextView dayLabel;



    }
}
