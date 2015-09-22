package com.isaac.coolweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.isaac.coolweather.R;
import com.isaac.coolweather.model.SavedCityDetail;
import com.isaac.coolweather.util.LogUtil;

import java.util.List;

/**
 * Created by IsaacCn on 2015/9/16.
 */
public class SavedCityDetailAdapter extends ArrayAdapter<SavedCityDetail> {
    private int resourceId;
    private List<SavedCityDetail> adapterDataList;

    public SavedCityDetailAdapter(Context context, int textViewResourceId, List<SavedCityDetail> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        adapterDataList=objects;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LogUtil.d("SavedCityDetailAdapter","getView()被调用");
        SavedCityDetail cityDetail = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView listItemCityName = (TextView) view.findViewById(R.id.listItemCityName);
        TextView listItemCountry = (TextView) view.findViewById(R.id.listItemCountry);
        Button listItemDelete=(Button)view.findViewById(R.id.listItemDelete);
        //Button listItemDelete = (Button) view.findViewById(R.id.listItemDelete);
        listItemCityName.setText(cityDetail.getCityName());
        listItemCountry.setText(cityDetail.getCountry());
        listItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterDataList.remove(position);
                LogUtil.d("SavedCityDetailAdapter","onClick() --- remove "+position);
                notifyDataSetChanged();;
            }
        });
        return view;
    }
}
