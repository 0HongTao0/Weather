package com.hongtao.weather.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hongtao.weather.R;
import com.hongtao.weather.activity.WeatherApplication;
import com.hongtao.weather.bean.HourForecast;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author：hongtao on 2017/7/18/018 08:57
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class HourForecastAdapter extends RecyclerView.Adapter<HourForecastAdapter.ViewHolder> {
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";
    private List<HourForecast> mHourForecastList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTime, mTvTemperature;
        NetworkImageView mNivSky;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.hourforecast_tv_time);
            mTvTemperature = (TextView) itemView.findViewById(R.id.hourforecast_tv_temperature);
            mNivSky = (NetworkImageView) itemView.findViewById(R.id.hourforecast_niv_sky);
        }
    }

    public HourForecastAdapter(List<HourForecast> hourForecasts) {
        this.mHourForecastList = hourForecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_rv_hourforecast, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourForecast hourForecast = mHourForecastList.get(position);
        holder.mTvTemperature.setText(hourForecast.getTemperature());
        holder.mTvTime.setText(hourForecast.getTime().substring(10));
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherApplication.getContext());
        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });
        holder.mNivSky.setImageUrl(ICON_ADDRESS + hourForecast.getSky() + ".png", imageLoader);
    }

    @Override
    public int getItemCount() {
        return mHourForecastList == null ? 0 : mHourForecastList.size();
    }
}
