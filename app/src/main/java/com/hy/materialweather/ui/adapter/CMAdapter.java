package com.hy.materialweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hy.materialweather.model.HeWeather5Map;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20
 */

public class CMAdapter extends RecyclerView.Adapter<CitiesAdapterMaterial.VH>{

    Context context;

    private List<String> list;

    public CMAdapter(Context context) {
        this.context = context;
        this.list = HeWeather5Map.chosenCities;
    }

    @Override
    public CitiesAdapterMaterial.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CitiesAdapterMaterial.VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }
}
