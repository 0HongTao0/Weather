package com.hongtao.weather.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.bean.DailyForecast;
import com.hongtao.weather.util.HttpUtil;
import com.hongtao.weather.util.UpdateImageViewTask;

import java.util.List;

/**
 * author：Administrator on 2017/7/14/014 10:18
 * email：935245421@qq.com
 */
public class DailyForecastAdapter extends BaseAdapter {
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lv_dailyforecast, null);
            viewHolder.TvDate = (TextView) convertView.findViewById(R.id.dailyforecast_tv_date);
            viewHolder.IvDaySky = (ImageView)convertView.findViewById(R.id.dailyforecast_iv_daysky);
            viewHolder.IvNightSky = (ImageView)convertView.findViewById(R.id.dailyforecast_iv_nightsky);
            viewHolder.TvTemperature = (TextView) convertView.findViewById(R.id.dailyforecast_tv_temperature);
            viewHolder.TvWindDirection = (TextView) convertView.findViewById(R.id.dailyforecast_tv_winddirection);
            viewHolder.TvWindSpeed = (TextView) convertView.findViewById(R.id.dailyforecast_tv_windspeed);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmapDay = HttpUtil.downloadPic(ICON_ADDRESS + mDailyForecasts.get(position).getDaySky() + ".png");
                new UpdateImageViewTask(bitmapDay, viewHolder.IvDaySky).execute();
                Bitmap bitmapNight = HttpUtil.downloadPic(ICON_ADDRESS + mDailyForecasts.get(position).getNightSky() + ".png");
                new UpdateImageViewTask(bitmapNight, viewHolder.IvNightSky).execute();
            }
        }).start();
        DailyForecast dailyForecast = mDailyForecasts.get(position);
        viewHolder.TvDate.setText(dailyForecast.getDate().substring(5));
        viewHolder.TvWindSpeed.setText(dailyForecast.getWindSpeed());
        viewHolder.TvWindDirection.setText(dailyForecast.getWindDirection());
        viewHolder.TvTemperature.setText(dailyForecast.getTemperature());
        return convertView;
    }

    private static class ViewHolder {
        TextView TvDate;
        TextView TvTemperature;
        TextView TvWindDirection;
        TextView TvWindSpeed;
        ImageView IvDaySky;
        ImageView IvNightSky;
    }


}
