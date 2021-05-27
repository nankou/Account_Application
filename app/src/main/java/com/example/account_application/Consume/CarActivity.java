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

import com.example.account_application.Adapter.MyAdaptercar;
import com.example.account_application.MySQLite.MySQLiteOpenHelper;
import com.example.account_application.R;
import com.example.account_application.bean.Car;

import java.util.ArrayList;
import java.util.List;

public class CarActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CarActivity.class.getSimpleName();
    private String id;
    private List<Car> cars = new ArrayList<>();
    private MyAdaptercar myAdaptercar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        id  = getIntent().getStringExtra("id").toString();
        //设置设配器
        myAdaptercar = new MyAdaptercar(CarActivity.this, cars, getApplicationContext());
        ((ListView) findViewById(R.id.listcar)).setAdapter(myAdaptercar);
        //Log.i(TAG, "onCreate: "+ id);
        show();
    }

    public void exit(View view) {
        finish();
    }

    public void add(View view) {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        String apply = ((EditText) findViewById(R.id.carapply)).getText().toString();
        String cost = ((EditText) findViewById(R.id.carcost)).getText().toString();

        if("".equals(apply)||"".equals(cost)){
            Toast.makeText(getApplicationContext(),"用途或者金额不能为空", Toast.LENGTH_SHORT).show();
        }else if(!isNumeric(cost)){
            Toast.makeText(getApplicationContext(),"确保金额为数字", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put("apply", apply);
            values.put("cost", cost);
            values.put("idname", id);
            database.insert("car", null, values);
            ((EditText) findViewById(R.id.carapply)).setText("");
            ((EditText) findViewById(R.id.carcost)).setText("");
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
        Cursor cursor = database.query("car", new String[]{"id", "apply", "cost"}, "idname=?",new String[]{id},null,null,null);
        cars.clear();
        double sum = 0;
        while(cursor.moveToNext()){
            int s1 = cursor.getInt(0);
            String s2 = cursor.getString(1);
            String s3 = cursor.getString(2);
            Car car = new Car();
            sum+=cursor.getDouble(2);
            car.setId(s1);
            car.setApply(s2);
            car.setCost(Double.parseDouble(s3));
            cars.add(car);
        }

        myAdaptercar.notifyDataSetChanged();
        database.close();
        ((TextView) findViewById(R.id.carsum)).setText("出行总金额：" +"￥"+ String.format("%.2f", sum));
    }

    public void refresh(View view) {
        Button button = findViewById(R.id.button13);
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
                final Car car = cars.get(position);
                //点击删除按钮之后，给出dialog提示
                AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
                builder.setTitle(position + "号位置的删除按钮被点击，确认删除?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(CarActivity.this, "mydb.db", null, 1);
                        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
                        database.delete("car", "id=?", new String[]{String.valueOf(car.getId())});
                        database.close();
                        myAdaptercar.notifyDataSetChanged();
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
