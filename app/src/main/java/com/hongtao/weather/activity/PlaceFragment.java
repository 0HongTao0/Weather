package com.hongtao.weather.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongtao.weather.R;
import com.hongtao.weather.adapter.PlaceAdapter;
import com.hongtao.weather.bean.Place;

import java.util.List;

/**
 * author：hongtao on 2017/7/21/021 13:10
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class PlaceFragment extends Fragment {
    private RecyclerView mRvPlace;
    private CallBackToActivity mCallBackToActivity;
    private List<Place> mPlaceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_choose, container, false);
        mRvPlace = (RecyclerView) view.findViewById(R.id.choose_place_rv_list);
        PlaceAdapter placeAdapter = new PlaceAdapter(mPlaceList);
        mRvPlace.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvPlace.setAdapter(placeAdapter);
        try {
            placeAdapter.setAdapterCallBack(new PlaceAdapter.ItemOnClickCallBackListener() {
                @Override
                public void onClickCallBackPlace(Place place) {
                    if (mCallBackToActivity != null) {
                        mCallBackToActivity.callBackPlace(place);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBackToActivity = (CallBackToActivity) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    public void setDataInFragment(List<Place> placeList) {
        this.mPlaceList = placeList;
    }

    public interface CallBackToActivity {
        void callBackPlace(Place place);
    }

    public void setCallBackListener(CallBackToActivity callBackListener) {
        this.mCallBackToActivity = callBackListener;
    }
}
