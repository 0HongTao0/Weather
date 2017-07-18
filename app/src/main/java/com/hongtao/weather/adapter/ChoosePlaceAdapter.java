package com.hongtao.weather.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongtao.weather.R;
import com.hongtao.weather.bean.Place;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author：hongtao on 2017/7/18/018 20:03
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class ChoosePlaceAdapter extends RecyclerView.Adapter<ChoosePlaceAdapter.ViewHolder> {
    private AdapterCallBack mAdapterCallBack;
    private List<Place> mPlaceList;
    private WeakReference<Activity> mActivityWeakReference;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvCityName;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvCityName = (TextView) itemView.findViewById(R.id.listviewitem_tv_place);
        }
    }

    public ChoosePlaceAdapter(Activity activity, List<Place> places, AdapterCallBack adapterCallBack) {
        this.mPlaceList = places;
        this.mActivityWeakReference = new WeakReference<>(activity);
        this.mAdapterCallBack = adapterCallBack;
    }

    @Override
    public ChoosePlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivityWeakReference.get()).inflate(R.layout.item_rv_place, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Place place = mPlaceList.get(position);
                mAdapterCallBack.callBackPlace(place);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChoosePlaceAdapter.ViewHolder holder, int position) {
        Place place = mPlaceList.get(position);
        holder.mTvCityName.setText(place.getName());
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public List<Place> getPlaceList() {
        return mPlaceList;
    }

    public void setPlaceList(List<Place> placeList) {
        mPlaceList = placeList;
    }
}
