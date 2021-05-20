package com.example.account_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tab_layout;
    private Fragment[] framensts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        framensts = DataGenerator.getFragments();
        initView();
    }

    private void initView() {
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);


        //设置监听器
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < tab_layout.getTabCount(); i++) {
                    View view = tab_layout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);

                    if (i == tab.getPosition()) { // 选中状态，修改字体颜色和图片，背景未实现
//                        view.setBackgroundColor(Color.parseColor("#a9a9a9"));
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        icon.setMaxHeight(10);
                        icon.setMaxWidth(10);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    } else {// 未选中状态
//                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        icon.setMaxHeight(10);
                        icon.setMaxWidth(10);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 提供自定义的布局添加Tab，注意此处的加载页面需要在设置Listener之后，不然会导致第一次点击事件失效
        for(int i=0;i<3;i++){
            tab_layout.addTab(tab_layout.newTab().setCustomView(DataGenerator.getTabView(this,i)));
        }

        //默认进入首页（需放在加载页面之后）
        tab_layout.getTabAt(0).select();
    }

    private void onTabItemSelected(int position){
        Fragment frag = null;
        switch (position){
            case 0:
                frag = framensts[0];
                break;
            case 1:
                frag = framensts[1];
                break;

            case 2:
                frag = framensts[2];
                break;
        }
        //替换fragment
        if(frag!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,frag).commit();
        }
    }
}

