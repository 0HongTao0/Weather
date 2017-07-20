package com.hongtao.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hongtao.weather.R;
import com.hongtao.weather.activity.WeatherApplication;
import com.hongtao.weather.bean.Place;

import java.util.ArrayList;
import java.util.List;


/**
 * author：hongtao on 2017/7/18/018 20:03
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private ItemOnClickCallBackListener mAdapterCallBack;
    private List<Place> mPlaceList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCityName;

        private ViewHolder(View itemView) {
            super(itemView);
            mTvCityName = (TextView) itemView.findViewById(R.id.place_tv_place);
        }
    }

    public PlaceAdapter(List<Place> places) {
        this.mPlaceList = places;
    }

    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_place, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaceAdapter.ViewHolder holder, final int position) {
        holder.mTvCityName.setText(mPlaceList.get(position).getName());
        holder.mTvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeatherApplication.getContext(), "5555555555", Toast.LENGTH_SHORT).show();
                mAdapterCallBack.onClickCallBackPlace(mPlaceList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public void setPlaceList(List<Place> placeList) {
        mPlaceList.clear();
        mPlaceList = placeList;
    }

    public interface ItemOnClickCallBackListener {
        void onClickCallBackPlace(Place place);
    }

    public void setAdapterCallBack(ItemOnClickCallBackListener adapterCallBack) {
        mAdapterCallBack = adapterCallBack;
    }
}