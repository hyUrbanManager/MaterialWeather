package com.hy.materialweather.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hy.materialweather.model.DATA;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/21.
 */

public class MainCardAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;

    private Context context;
    private int resource;
    private String[] from;
    private int[] to;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public MainCardAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
        this.context = context;
        this.data = data;
        this.from = from;
        this.resource = resource;
        this.to = to;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 这里的顺序对应外面的顺序
     */
    private class VH {
        final TextView vcity;
        final TextView vtmp;
        final TextView vdesc;
        final TextView vpm2_5;
        final ImageView vcond;
        final RelativeLayout vbackground;

        VH(View root) {
            this.vcity = (TextView) root.findViewById(to[0]);
            this.vtmp = (TextView) root.findViewById(to[1]);
            this.vdesc = (TextView) root.findViewById(to[2]);
            this.vpm2_5 = (TextView) root.findViewById(to[3]);
            this.vcond = (ImageView) root.findViewById(to[4]);
            this.vbackground = (RelativeLayout) root.findViewById(to[5]);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        //初始化
        if (convertView == null) {
            /**
             * 加载布局的方式参考SimpleAdapter加载布局的方法!
             */
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new VH(convertView);
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }

        Map<String, Object> map = data.get(position);
        holder.vcity.setText(map.get(from[0]).toString());
        holder.vtmp.setText(map.get(from[1]).toString());
        holder.vdesc.setText(map.get(from[2]).toString());
        holder.vpm2_5.setText(map.get(from[3]).toString());
        holder.vcond.setImageResource((Integer) map.get(from[4]));

        holder.vbackground.setBackgroundResource(DATA.convertToRes((Integer) map.get(from[5])));

        return convertView;
    }
}
