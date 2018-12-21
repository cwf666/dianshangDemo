package com.example.asus.myapplication.modle;

import android.os.Handler;
import android.os.Message;

import com.example.asus.myapplication.bean.News;
import com.example.asus.myapplication.callback.MyCallBack;
import com.example.asus.myapplication.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModleImpl implements Modle {
    private int type;
    private MyCallBack callBack;
     Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonStr= (String) msg.obj;
            Gson gson=new Gson();
            if (type==1){
                News myData = gson.fromJson(jsonStr, News.class);
                callBack.setData(myData);
            }

        }
    };
    @Override
    public void getData(String url, final int type, MyCallBack callBack) {
        this.callBack=callBack;
        this.type=type;

        HttpUtils.getInstence().AsyncGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr=response.body().string();
                handler.sendMessage(handler.obtainMessage(0,jsonStr));


            }
        });
    }
}
