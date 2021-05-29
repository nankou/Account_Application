package com.example.account_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.account_application.Consume.CarActivity;
import com.example.account_application.bean.Car;
import com.example.account_application.R;
import com.example.account_application.bean.Car;

import java.util.List;

/**
 * Created by Administrator on 2018/12/16.
 */

public class MyAdaptercar extends BaseAdapter {
    private List<Car> cars;
    private Context context;
    private final View.OnClickListener listener;
    public MyAdaptercar(View.OnClickListener listener, List<Car> cars, Context applicationContext) {
        this.cars = cars;
        this.context = applicationContext;
        this.listener =listener;
    }



    @Override
    public int getCount() {
        return cars.size();
    }

    @Override
    public Object getItem(int position) {
        return cars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cars.get(position).getId();
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
        final Car car = cars.get(position);
        holder.apply.setText(car.getApply());
        holder.cost.setText(String.valueOf(car.getCost())+"￥");//可能会有问题
        //给要被点击的控件加入点击监听，具体事件在创建adapter的地方实现
        holder.delete.setOnClickListener(listener);
        //通过setTag 将被点击控件所在条目的位置传递出去
        holder.delete.setTag(position);
        return convertView;
    }
    class ViewHolder{
        TextView apply;
        TextView cost;
        Button delete;
    }
}