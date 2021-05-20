/*
*工具类
 */
package com.example.account_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.tab1,R.drawable.tab2,R.drawable.tab3};
    public static final int []mTabResPressed = new int[]{R.drawable.tab1,R.drawable.tab2,R.drawable.tab3};
    public static final String []mTabTitle = new String[]{"首页","商城","消息"};

    public static Fragment[] getFragments(){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = new AddFragment();
        fragments[1] = new ListFragment();
        fragments[2] = new UserFragment();
        return fragments;
    }

    // 获取了position的视图view，并初始化里面的元素，用于init
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_layout,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);

        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
