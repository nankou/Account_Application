 package com.example.account_application;

import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.account_application.MySQLite.MySQLiteOpenHelper;
import com.google.android.material.tabs.TabLayout;

public class ClassesActivity extends AppCompatActivity {
    private TabLayout tab_layout;
    private Fragment[] framensts;

    private ServiceConnection conn;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean flag=true;

    private String id;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        framensts = DataGenerator.getFragments();
        initView();

    }

    // 初始化tab栏
    private void initView() {
        tab_layout = findViewById(R.id.tab_layout);
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

                    if (i == tab.getPosition()) { // 选中状态，修改图片
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        // 想通过这个来修改图片的大小，但是不知道为什么失败了
                        // 最终压缩了图片来解决- 2021.5
                        icon.setMaxHeight(10);
                        icon.setMaxWidth(10);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    } else {// 未选中状态
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

    // 切换tab栏
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

    // 首页显示
    private void show() {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = database.query("car", new String[]{"cost"}, "idname=?",new String[]{id},null,null,null);
        double sum=0;
        while(cursor.moveToNext()){
            sum+=cursor.getDouble(0);
        }
        cursor = database.query("food", new String[]{"cost"}, "idname=?",new String[]{id},null,null,null);
        while(cursor.moveToNext()){
            sum+=cursor.getDouble(0);
        }
        cursor = database.query("shopping", new String[]{"cost"}, "idname=?",new String[]{id},null,null,null);
        while(cursor.moveToNext()){
            String s3 = cursor.getString(0);
            sum+=cursor.getDouble(0);
        }
        database.close();
        ((TextView) findViewById(R.id.allsum)).setText("￥"+ String.format("%.2f", sum));
    }

    public void foodbill(View view) {
        Intent intent = new Intent();
        intent.setClass(com.example.account_application.ClassesActivity.this, AddFragment.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void travelbill(View view) {
        Intent intent = new Intent();
        intent.setClass(com.example.account_application.ClassesActivity.this, AddFragment.class);
        intent.putExtra("id", id);
        startActivity(intent);;
    }

    public void shoppingbill(View view) {
        Intent intent = new Intent();
        intent.setClass(com.example.account_application.ClassesActivity.this, AddFragment.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}
