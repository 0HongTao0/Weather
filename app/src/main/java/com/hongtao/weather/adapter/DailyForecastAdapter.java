package com.hongtao.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hongtao.weather.R;
import com.hongtao.weather.bean.DailyForecast;

import java.util.List;

/**
 * author：hongtao on 2017/7/17/017 20:07
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";
    private List<DailyForecast> mDailyForecasts;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvNightSky, mIvDaySky;
        TextView mTvDate, mTvTemperature, mTvWindDirection, mTvWindSpeed;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvNightSky = (ImageView) itemView.findViewById(R.id.dailyforecast_iv_nightsky);
            mIvDaySky = (ImageView) itemView.findViewById(R.id.dailyforecast_iv_daysky);
            mTvDate = (TextView) itemView.findViewById(R.id.dailyforecast_tv_date);
            mTvTemperature = (TextView) itemView.findViewById(R.id.dailyforecast_tv_temperature);
            mTvWindDirection = (TextView) itemView.findViewById(R.id.dailyforecast_tv_winddirection);
            mTvWindSpeed = (TextView) itemView.findViewById(R.id.dailyforecast_tv_windspeed);
        }
    }

    public DailyForecastAdapter(List<DailyForecast> dailyForecasts) {
        mDailyForecasts = dailyForecasts;
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
        Glide.with(holder.mIvDaySky.getContext())
                .load(ICON_ADDRESS + dailyForecast.getDaySky() + ".png")
                .error(R.drawable.ding)
                .placeholder(R.drawable.ding)
                .fitCenter()
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 设置缓存的策略
                .into(holder.mIvDaySky);

        Glide.with(holder.mIvNightSky.getContext())
                .load(ICON_ADDRESS + dailyForecast.getNightSky()+ ".png")
                .error(R.drawable.ding)
                .placeholder(R.drawable.ding)
                .fitCenter()
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 设置缓存的策略
                .into(holder.mIvNightSky);
    }

    @Override
    public int getItemCount() {
        return mDailyForecasts == null ? 0 : mDailyForecasts.size();
    }
}
