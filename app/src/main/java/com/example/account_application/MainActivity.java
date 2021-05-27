package com.example.account_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.account_application.MySQLite.MySQLiteOpenHelper;
import com.example.account_application.Service.ClassService;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection conn;
    private ClassService.MyBinder binder;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        if(flag==true){
            playMusic();
        }
        flag=false;
    }

    // 登录逻辑
    public void Login(View view) {
        String name = ((EditText) findViewById(R.id.loginame)).getText().toString();
        String password = ((EditText) findViewById(R.id.loginpassword)).getText().toString();
        //获得登录用户填写的信息
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext(), "mydb.db", null, 1);
        SQLiteDatabase database = mySQLiteOpenHelper.getWritableDatabase();
        //从用户名来判断是否在用户表存在该用户
        Cursor cursor = database.query("users", new String[] { "id", "name" , "password"}, "name=?", new String[] { name }, null, null, null);

        String id=null;
        String s1=null;

        while (cursor.moveToNext()) {
            //若存在该信息将其id和password读取出来
            s1 = cursor.getString(cursor.getColumnIndex("password"));
            id = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
        }

        //判断密码是否相同，且是否存在该用户
        if(password.equals(s1)){
            database.close();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ClassesActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name",name);
            startActivity(intent);
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

        }else{
            database.close();
            Toast.makeText(getApplicationContext(),"用户名不存在或密码错误",Toast.LENGTH_SHORT).show();
        }
    }

    // 注册跳转
    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    //
    public void playMusic() {

        if (binder == null) {
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (ClassService.MyBinder) service;
                    binder.play();
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {}
            };
            bindService(new Intent(this, ClassService.class), conn, BIND_AUTO_CREATE);
        } else {
            binder.resumeMusic();
        }
    }
    public void pauseMusic() {
        binder.pauseMusic();
    }

    public void play(View view) {
        playMusic();
    }

    public void stop(View view) {
        pauseMusic();
    }

}

