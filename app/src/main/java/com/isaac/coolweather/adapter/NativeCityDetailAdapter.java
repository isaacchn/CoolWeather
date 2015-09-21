package com.isaac.coolweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isaac.coolweather.R;
import com.isaac.coolweather.model.NativeCityDetail;

import java.util.List;

/**
 * Created by IsaacCn on 2015/9/19.
 */
public class NativeCityDetailAdapter extends ArrayAdapter<NativeCityDetail> {
    private int resourceId;

    public NativeCityDetailAdapter(Context context, int textViewResourceId, List<NativeCityDetail> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NativeCityDetail nativeCityDetail = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView listQueriedItemCityName = (TextView) view.findViewById(R.id.listQueriedItemCityName);
        TextView listQueriedItemCountry = (TextView) view.findViewById(R.id.listQueriedItemCountry);
        listQueriedItemCityName.setText(nativeCityDetail.getName());
        listQueriedItemCountry.setText(nativeCityDetail.getCountry());
        return view;
    }
}
