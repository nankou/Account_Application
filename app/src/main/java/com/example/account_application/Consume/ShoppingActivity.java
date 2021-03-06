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

import com.example.account_application.Adapter.MyAdaptershopping;
import com.example.account_application.MySQLite.MySQLiteOpenHelper;
import com.example.account_application.R;
import com.example.account_application.bean.Shopping;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ShoppingActivity.class.getSimpleName();
    private String id;

//    private List<Shopping> shops = new ArrayList<>();
    private List shops = new ArrayList<>();
    private MyAdaptershopping myAdaptershopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        id  = getIntent().getStringExtra("id").toString();
        myAdaptershopping = new MyAdaptershopping(ShoppingActivity.this,shops, getApplicationContext());
        ((ListView) findViewById(R.id.listshopping)).setAdapter(myAdaptershopping);
        //Log.i(TAG, "onCreate: "+ id);
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
    public void exit(View view) {
        finish();
    }

    public void add(View view) {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        String apply = ((EditText) findViewById(R.id.shoppingapply)).getText().toString();
        String cost = ((EditText) findViewById(R.id.shoppingcost)).getText().toString();

        if("".equals(apply)||"".equals(cost)){
            Toast.makeText(getApplicationContext(),"??????????????????????????????", Toast.LENGTH_SHORT).show();
        }else if(!isNumeric(cost)){
            Toast.makeText(getApplicationContext(),"?????????????????????", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put("apply", apply);
            values.put("cost", cost);//double??????
            values.put("idname", id);
            database.insert("shopping", null, values);
            ((EditText) findViewById(R.id.shoppingapply)).setText("");
            ((EditText) findViewById(R.id.shoppingcost)).setText("");
        }
        database.close();
        show();
    }

    private void show() {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = database.query("shopping", new String[]{"id", "apply", "cost"}, "idname=?",new String[]{id},null,null,null);
        shops.clear();
        double sum = 0;
        while(cursor.moveToNext()){
            int s1 = cursor.getInt(0);
            String s2 = cursor.getString(1);
            String s3 = cursor.getString(2);
            Shopping shop = new Shopping();
            sum+=cursor.getDouble(2);
            shop.setId(s1);
            shop.setApply(s2);
            shop.setCost(Double.parseDouble(s3));
            shops.add(shop);
        }
        myAdaptershopping.notifyDataSetChanged();
        database.close();
        ((TextView) findViewById(R.id.shoppingsum)).setText("?????????????????????" + String.format("%.2f", sum));
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
            case R.id.buttondelete:   //lv????????? iv_del
                final int position = (int) v.getTag(); //??????????????????????????????item ????????????setTag ?????????object????????????????????????
                final Shopping shopping = (Shopping) shops.get(position);
                //?????????????????????????????????dialog??????
                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
                builder.setTitle("???????????????????????????????????????????");
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(ShoppingActivity.this, "mydb.db", null, 1);
                        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
                        database.delete("shopping", "id=?", new String[]{String.valueOf(shopping.getId())});
                        database.close();
                        myAdaptershopping.notifyDataSetChanged();
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
