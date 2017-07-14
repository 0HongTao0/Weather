package com.hongtao.weather.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * author：Administrator on 2017/7/14/014 11:52
 * email：935245421@qq.com
 */
public class UIUtil {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (count - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int count = listAdapter.getCount();
        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0); // 计算子项View 的宽高
        totalHeight = listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
        int yu = count % 4;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        if (yu > 0) {
            params.height = (count - yu) / 4 * (totalHeight + 10)
                    + totalHeight;
        } else {
            params.height = count / 4 * totalHeight + (count / 4 - 1) * 10;
        }
        gridView.setLayoutParams(params);
    }
}
