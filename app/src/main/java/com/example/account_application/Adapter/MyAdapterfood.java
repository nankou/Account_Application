package com.example.account_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.account_application.Consume.FoodActivity;
import com.example.account_application.bean.Food;
import com.example.account_application.R;
import com.example.account_application.bean.Food;

import java.util.List;

/**
 * Created by Administrator on 2018/12/15.
 */

public class MyAdapterfood extends BaseAdapter {
    private static final String TAG = com.example.account_application.Adapter.MyAdapterfood.class.getSimpleName();
    private List<Food> foods;
    private Context context;
    private final View.OnClickListener listener;
    public MyAdapterfood(View.OnClickListener listener, List<Food> foods, Context applicationContext) {
        this.foods = foods;
        this.context = applicationContext;
        this.listener =listener;
    }


    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foods.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cost_list, parent,false);
            holder.apply= (TextView) convertView.findViewById(R.id.itemapply);
            holder.cost = (TextView) convertView.findViewById(R.id.itemcost);
            holder.delete = (Button)convertView.findViewById(R.id.buttondelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Food food = foods.get(position);
        holder.apply.setText(food.getApply());
        holder.cost.setText(String.valueOf(food.getCost())+"???");//??????????????????
        //??????????????????????????????????????????????????????????????????adapter???????????????
        holder.delete.setOnClickListener(listener);
        //??????setTag ???????????????????????????????????????????????????
        holder.delete.setTag(position);
        return convertView;
    }
    class ViewHolder{
        TextView apply;
        TextView cost;
        Button delete;
    }

}