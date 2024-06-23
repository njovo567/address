package com.example.addressbook.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.addressbook.utils.DarkModeUtils;
import com.example.addressbook.utils.SPUtils;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mode = SPUtils.getInt("mode", 0, this);
        if(mode==0){
            DarkModeUtils.applyDayMode(this);
        }else{
            DarkModeUtils.applyNightMode(this);
        }
    }
}
