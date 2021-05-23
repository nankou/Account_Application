package com.example.account_application;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AddFragment extends Fragment {
    private static String ARG_PARAM = "param_key";
    private String mParam;
    private ClassesActivity classesActivity;

//    public void onAttach(Context context) {
//        super.onAttach(context);
//        classesActivity = (ClassesActivity) context;
//        mParam = getArguments().getString(ARG_PARAM);  //获取参数
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        return view;
    }
//    public static AddFragment newInstance(String str) {
//        AddFragment frag = new AddFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(ARG_PARAM, str);
//        AddFragment fragment = new AddFragment();
//        fragment.setArguments(bundle);   //设置参数
//        return fragment;
//    }
}

