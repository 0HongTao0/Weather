package com.hongtao.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.bean.Province;

import java.util.List;

/**
 * author：Administrator on 2017/7/12/012 10:35
 * email：935245421@qq.com
 */
public class PlaceAdapter extends BaseAdapter {
    private Context mContext;
    private List<Province> mPlaces;

    public PlaceAdapter(Context context, List<Province> places) {
        this.mContext = context;
        this.mPlaces = places;
    }

    @Override
    public int getCount() {
        return mPlaces.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.place_item, null);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.listviewitem_tv_place);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(mPlaces.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        TextView mTextView;
    }
}
