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
import com.hongtao.weather.bean.HourForecast;
import com.hongtao.weather.util.HttpUtil;
import com.hongtao.weather.util.UpdateImageViewTask;

import java.util.List;

/**
 * author：Administrator on 2017/7/13/013 10:20
 * email：935245421@qq.com
 */
public class HourForecastAdapter extends BaseAdapter {
    private static final String ICON_ADDRESS = "https://cdn.heweather.com/cond_icon/";
    private Context mContext;
    private List<HourForecast> mHourForecastList;

    public HourForecastAdapter(Context context, List<HourForecast> hourForecasts) {
        this.mContext = context;
        this.mHourForecastList = hourForecasts;
    }

    @Override
    public int getCount() {
        return mHourForecastList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hourforecast_item, null);
            viewHolder.TvTime = (TextView) convertView.findViewById(R.id.hourforecast_tv_time);
            viewHolder.IvSky = (ImageView) convertView.findViewById(R.id.hourforecast_iv_sky);
            viewHolder.TvTemperature = (TextView) convertView.findViewById(R.id.hourforecast_tv_temperature);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.TvTime.setText(mHourForecastList.get(position).getTime().substring(10));
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = HttpUtil.downloadPic(ICON_ADDRESS + mHourForecastList.get(position).getSky() + ".png");
                new UpdateImageViewTask(bitmap, viewHolder.IvSky).execute();
            }
        }).start();
        viewHolder.TvTemperature.setText(mHourForecastList.get(position).getTemperature());
        return convertView;
    }

    private static class ViewHolder {
        TextView TvTime;
        ImageView IvSky;
        TextView TvTemperature;
    }
}
