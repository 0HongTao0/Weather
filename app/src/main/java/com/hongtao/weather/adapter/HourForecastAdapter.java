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
import com.hongtao.weather.bean.HourForecast;

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
        ImageView mNivSky;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.hourforecast_tv_time);
            mTvTemperature = (TextView) itemView.findViewById(R.id.hourforecast_tv_temperature);
            mNivSky = (ImageView) itemView.findViewById(R.id.hourforecast_niv_sky);
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
        Glide.with(holder.mNivSky.getContext())
                .load(ICON_ADDRESS + hourForecast.getSky() + ".png")
                .error(R.drawable.ding)
                .placeholder(R.drawable.ding)
                .fitCenter()
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 设置缓存的策略
                .into(holder.mNivSky);
    }

    @Override
    public int getItemCount() {
        return mHourForecastList == null ? 0 : mHourForecastList.size();
    }
}
