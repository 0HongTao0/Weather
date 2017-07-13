package com.hongtao.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.bean.HourForecast;

import java.util.List;

/**
 * author：Administrator on 2017/7/13/013 10:20
 * email：935245421@qq.com
 */
public class HourForecastAdapter extends BaseAdapter {

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.forecast_item, null);
            viewHolder.TVTime = (TextView) convertView.findViewById(R.id.forecast_tv_time);
            viewHolder.TVTWea = (TextView) convertView.findViewById(R.id.forecast_tv_wea);
            viewHolder.TVTem = (TextView) convertView.findViewById(R.id.forecast_tv_tem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.TVTime.setText(mHourForecastList.get(position).getTime());
        viewHolder.TVTWea.setText(mHourForecastList.get(position).getWea());
        viewHolder.TVTem.setText(mHourForecastList.get(position).getTem());
        return convertView;
    }

    static class ViewHolder {
        TextView TVTime;
        TextView TVTWea;
        TextView TVTem;
    }
}
