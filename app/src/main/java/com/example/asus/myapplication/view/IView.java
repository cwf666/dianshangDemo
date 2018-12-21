package com.example.asus.myapplication.view;

public interface IView<T> {
    void successData(T data);
    void errorMsg(T error);
}
