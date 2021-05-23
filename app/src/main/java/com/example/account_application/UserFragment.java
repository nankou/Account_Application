package com.example.account_application;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.account_application.MySQLite.MySQLiteOpenHelper;

public class UserFragment extends Fragment {
    private MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getActivity(), "mydb.db", null, 1);
    private String id;
    private String text;
    private int name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        Bundle bundle = getArguments();  // 用bundle接收数据
        if (bundle != null){
            text = bundle.getString("text");
            TextView textView = view.findViewById(R.id.allsum);
            textView.setText(text);
            Toast.makeText(getContext(),"text",Toast.LENGTH_SHORT).show();
        }else if (bundle == null){
            TextView textView = view.findViewById(R.id.allsum);
            textView.setText("null");
        }
        return view;
    }


}
