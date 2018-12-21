package com.example.asus.myapplication.modle;


import com.example.asus.myapplication.callback.MyCallBack;

public interface Modle {
    void getData(String url, int type, MyCallBack callBack);

}
