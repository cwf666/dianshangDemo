package com.example.asus.myapplication.presenter;


import com.example.asus.myapplication.callback.MyCallBack;
import com.example.asus.myapplication.modle.ModleImpl;
import com.example.asus.myapplication.view.IView;

public class PresenterImpl implements Presenter {
    IView iView;
    ModleImpl modle;

    public PresenterImpl(IView iView) {
        this.iView = iView;
        modle=new ModleImpl();
    }

    @Override
    public void startRequest(String url, int type) {
        modle.getData(url, type, new MyCallBack() {
            @Override
            public void setData(Object data) {
                iView.successData(data);
            }

            @Override
            public void setError(Object error) {
                iView.errorMsg(error);

            }
        });

    }
}
