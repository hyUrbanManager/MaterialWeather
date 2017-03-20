package com.hy.materialweather.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CitiesAdapterMaterial extends RecyclerView.Adapter<CitiesAdapterMaterial.VH>
        implements Filterable {
    public static final String TAG = CitiesAdapterMaterial.class.getName();

    //指示常量
    public static final int TEXT_TYPE = 1;

    public List<String> rawCities;
    public List<String> showCities;

    private LayoutInflater inflater;
    private Filter mFilter;

    public CitiesAdapterMaterial(Context context, List<String> cities) {
        this.rawCities = cities;
        this.showCities = cities;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH holder = null;
//        if(viewType == TEXT_TYPE) {
            holder = new VH(inflater.inflate(R.layout.material_grid_city_item, null));
//        }
        return holder;
    }

    @Override
    public int getItemCount() {
        return showCities.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.city.setText(showCities.get(position));
    }

    class VH extends RecyclerView.ViewHolder {
        final TextView city;

        public VH(View root) {
            super(root);
            this.city = (TextView) root.findViewById(R.id.text1);
        }
    }

    @Override
    public synchronized Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CitiesAdapterMaterial.CityFilter();
        }
        return mFilter;
    }

    //我们需要定义一个过滤器的类来定义过滤规则
    private class CityFilter extends Filter {

        /**
         * 在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
         *
         * @param charSequence
         */
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<String> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = rawCities;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (String str : rawCities) {
                    if(str.contains(charSequence)) {
                        list.add(str);
                        Utils.d(TAG + "查找到了数据 " + str);
                    }
                }
            }

            //将得到的集合保存到FilterResults的value变量中,集合的大小保存到FilterResults的count变量中
            result.values = list;
            result.count = list.size();

            return result;
        }

        /**
         * 在publishResults方法中告诉适配器更新界面
         *
         * @param charSequence
         * @param filterResults
         */
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            showCities = (List<String>) filterResults.values;
            Log.d(CitiesAdapterRaw.class.getName(), "要推出去的数据个数:" + filterResults.count);
            if (filterResults.count > 0) {
                //通知数据发生了改变
                notifyDataSetChanged();
                Utils.d(TAG + "结果：notifyDataSetChanged提示更改");
            } else {
                //通知数据失效
                notifyDataSetChanged();
                Utils.d(TAG + "结果:notifyDataSetInvalidated查询到的数据小于0，失败");
            }
        }
    }
}
