package com.example.asus.myapplication.callback;

public interface MyCallBack<T>  {
    void setData(T data);
    void setError(T error);
}
