package com.hy.materialweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.VH> {

    private Context context;
    private List<String> list;

    public InfoAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = new VH(LayoutInflater.from(context)
                .inflate(android.R.layout.test_list_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder {
        final TextView tv;

        public VH(View root) {
            super(root);
            tv = (TextView) root.findViewById(android.R.id.text1);
        }
    }

}
