package com.hy.materialweather.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市适配器
 */
public class CitiesAdapter extends BaseAdapter implements Filterable {

    List<String> rawCities;
    List<String> showCities;

    LayoutInflater inflater;
    Filter mFilter;

    public CitiesAdapter(Context context, List<String> cities) {
        this.rawCities = cities;
        this.showCities = cities;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 有过滤器，以下两个方法写法不一样。
     * @return
     */
    @Override
    public int getCount() {
        return showCities.size();
    }

    @Override
    public String getItem(int position) {
        return showCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 界面绑定
     */
    private class ViewHolder {
        final TextView city;

        public ViewHolder(View root) {
            this.city = (TextView) root.findViewById(android.R.id.text1);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.activity_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.city.setText(rawCities.get(position));

        return convertView;
    }

    @Override
    public synchronized Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CityFilter();
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
                        Log.d(CitiesAdapter.class.getName(), "查找到了数据 " + str);
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
            Log.d(CitiesAdapter.class.getName(), "要推出去的数据个数:" + filterResults.count);
            if (filterResults.count > 0) {
                //通知数据发生了改变
                notifyDataSetChanged();
                Log.d(CitiesAdapter.class.getName(), "结果：notifyDataSetChanged提示更改");
            } else {
                //通知数据失效
                notifyDataSetInvalidated();
                Log.d(CitiesAdapter.class.getName(), "结果:notifyDataSetInvalidated查询到的数据小鱼0，失败");
            }
        }
    }

}
