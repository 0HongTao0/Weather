package com.hongtao.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.bean.DailyForecast;

import java.util.List;

/**
 * author：Administrator on 2017/7/14/014 10:18
 * email：935245421@qq.com
 */
public class DailyForecastAdapter extends BaseAdapter {
    private Context mContext;
    private List<DailyForecast> mDailyForecasts;

    public DailyForecastAdapter(Context context, List<DailyForecast> dailyForecasts) {
        this.mContext = context;
        this.mDailyForecasts = dailyForecasts;
    }

    @Override
    public int getCount() {
        return mDailyForecasts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dailyforecast_item, null);
            viewHolder.TVDate = (TextView) convertView.findViewById(R.id.dailyforecast_tv_date);
            viewHolder.TVSky = (TextView) convertView.findViewById(R.id.dailyforecast_tv_sky);
            viewHolder.TVTemperature = (TextView) convertView.findViewById(R.id.dailyforecast_tv_temperature);
            viewHolder.TVWindDirection = (TextView) convertView.findViewById(R.id.dailyforecast_tv_winddirection);
            viewHolder.TVWindSpeed = (TextView) convertView.findViewById(R.id.dailyforecast_tv_windspeed);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DailyForecast dailyForecast = mDailyForecasts.get(position);
        viewHolder.TVDate.setText(dailyForecast.getDate());
        viewHolder.TVSky.setText(dailyForecast.getSky());
        viewHolder.TVWindSpeed.setText(dailyForecast.getWindSpeed());
        viewHolder.TVWindDirection.setText(dailyForecast.getWindDirection());
        viewHolder.TVTemperature.setText(dailyForecast.getTemperature());
        return convertView;
    }

    private static class ViewHolder {
        TextView TVDate;
        TextView TVTemperature;
        TextView TVSky;
        TextView TVWindDirection;
        TextView TVWindSpeed;
    }
}
