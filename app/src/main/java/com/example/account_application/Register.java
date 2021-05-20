package com.example.account_application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.account_application.MySQLite.MySQLiteOpenHelper;

public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
    }

    public void exit(View view) {
        finish();
    }

    public void addtotableuser(View view) {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);

        SQLiteDatabase database = mySQLiteOpenHelper.getReadableDatabase();
        String name = ((EditText) findViewById(R.id.registername)).getText().toString();
        String password = ((EditText) findViewById(R.id.registerpassword)).getText().toString();
        Cursor cursor = database.query("users", new String[] { "id", "name" }, "name=?", new String[] { name }, null, null, null);

        String s1=null;
        while (cursor.moveToNext()) {
            s1 = cursor.getString(cursor.getColumnIndex("id"));
        }
        if("".equals(name)||"".equals(password)){
            database.close();
            Toast.makeText(getApplicationContext(),"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(s1==null) {
            database = mySQLiteOpenHelper.getWritableDatabase();
            Log.i(TAG, "addtotableuser: " + name + " " + password);
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("password", password);
            //        database.execSQL("insert into users (name, age) values ('Jin Jie', 32)");
            database.insert("users", null, values);
            Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
            database.close();
            startActivity(new Intent(Register.this,MainActivity.class));
        }else{
            database.close();
            Toast.makeText(getApplicationContext(),"该用户已经存在",Toast.LENGTH_SHORT).show();
        }
    }
}
