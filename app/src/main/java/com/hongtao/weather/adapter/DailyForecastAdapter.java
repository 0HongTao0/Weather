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
import com.hongtao.weather.bean.DailyForecast;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author：hongtao on 2017/7/17/017 20:07
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";
    private List<DailyForecast> mDailyForecasts;
    private WeakReference<Activity> mActivityWeakReference;

    static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView mIvNightSky, mIvDaySky;
        TextView mTvDate, mTvTemperature, mTvWindDirection, mTvWindSpeed;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvNightSky = (NetworkImageView) itemView.findViewById(R.id.dailyforecast_iv_nightsky);
            mIvDaySky = (NetworkImageView) itemView.findViewById(R.id.dailyforecast_iv_daysky);
            mTvDate = (TextView) itemView.findViewById(R.id.dailyforecast_tv_date);
            mTvTemperature = (TextView) itemView.findViewById(R.id.dailyforecast_tv_temperature);
            mTvWindDirection = (TextView) itemView.findViewById(R.id.dailyforecast_tv_winddirection);
            mTvWindSpeed = (TextView) itemView.findViewById(R.id.dailyforecast_tv_windspeed);
        }
    }

    public DailyForecastAdapter(Activity activity, List<DailyForecast> dailyForecasts) {
        mDailyForecasts = dailyForecasts;
        this.mActivityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public DailyForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_dailyforecast, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DailyForecastAdapter.ViewHolder holder, int position) {
        DailyForecast dailyForecast = mDailyForecasts.get(position);
        holder.mTvDate.setText(dailyForecast.getDate().substring(5));
        holder.mTvTemperature.setText(dailyForecast.getTemperature());
        holder.mTvWindDirection.setText(dailyForecast.getWindDirection());
        holder.mTvWindSpeed.setText(dailyForecast.getWindSpeed());
        RequestQueue requestQueue = Volley.newRequestQueue(mActivityWeakReference.get());
        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });
        holder.mIvDaySky.setImageUrl(ICON_ADDRESS + dailyForecast.getDaySky() + ".png", imageLoader);
        holder.mIvNightSky.setImageUrl(ICON_ADDRESS + dailyForecast.getNightSky() + ".png", imageLoader);
    }

    @Override
    public int getItemCount() {
        return mDailyForecasts.size();
    }
}
