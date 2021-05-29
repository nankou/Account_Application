package com.example.account_application.Consume;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.account_application.Adapter.MyAdapterfood;
import com.example.account_application.MySQLite.MySQLiteOpenHelper;
import com.example.account_application.R;
import com.example.account_application.bean.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity implements  View.OnClickListener {
    private static final String TAG = FoodActivity.class.getSimpleName();
    private String id;

    private List<Food> foods = new ArrayList<>();
    public static MyAdapterfood myAdapterfood;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        id = getIntent().getStringExtra("id").toString();
        //Log.i(TAG, "onCreate: "+id);
        //设置设配器
        myAdapterfood = new MyAdapterfood(FoodActivity.this, foods, getApplicationContext());
        ((ListView) findViewById(R.id.listfood)).setAdapter(myAdapterfood);
        //获得数据库数据
        show();
    }

    public void exit(View view) {
        finish();
    }

    public void add(View view) {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        String apply = ((EditText) findViewById(R.id.foodapply)).getText().toString();
        String cost = ((EditText) findViewById(R.id.foodcost)).getText().toString();
        if("".equals(apply)||"".equals(cost)){
            Toast.makeText(getApplicationContext(),"用途或者金额不能为空", Toast.LENGTH_SHORT).show();
        }else if(!isNumeric(cost)){
            Toast.makeText(getApplicationContext(),"确保金额为数字", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put("apply", apply);
            values.put("cost", cost);//double类型
            values.put("idname", id);
            database.insert("food", null, values);
            //初始化
            ((EditText) findViewById(R.id.foodapply)).setText("");
            ((EditText) findViewById(R.id.foodcost)).setText("");
        }
        database.close();
        show();
    }
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public void show(){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = database.query("food", new String[]{"id", "apply", "cost"}, "idname=?",new String[]{id},null,null,null);
        foods.clear();
        double sum = 0;
        while(cursor.moveToNext()){
            int s1 = cursor.getInt(0);
            String s2 = cursor.getString(1);
            String s3 = cursor.getString(2);
            Food food = new Food();
            sum+=cursor.getDouble(2);
            food.setId(s1);
            food.setApply(s2);
            food.setCost(Double.parseDouble(s3));
            foods.add(food);
        }
        myAdapterfood.notifyDataSetChanged();
        database.close();
        ((TextView) findViewById(R.id.foodsum)).setText("饮食总金额：￥" + String.format("%.2f", sum));
    }

    public void refresh(View view) {
        Button button = findViewById(R.id.button14);
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", 0f, 360f, 720f);
        animator.setDuration(2000);
        animator.start();
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttondelete:   //lv条目中 iv_del
                final int position = (int) v.getTag(); //获取被点击的控件所在item 的位置，setTag 存储的object，所以此处要强转
                final Food food = foods.get(position);
                //点击删除按钮之后，给出dialog提示
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this);
                builder.setTitle("该条记录即将被删除，确认删除?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(FoodActivity.this, "mydb.db", null, 1);
                        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();

                        database.delete("food", "id=?", new String[]{String.valueOf(food.getId())});
                        database.close();
                        myAdapterfood.notifyDataSetChanged();
                        show();
                    }
                });
                builder.show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
