package com.example.asus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.myapplication.adapter.MyAdapter;
import com.example.asus.myapplication.adapter.PuBuAdapter;
import com.example.asus.myapplication.bean.News;
import com.example.asus.myapplication.presenter.PresenterImpl;
import com.example.asus.myapplication.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IView, View.OnClickListener {
    private XRecyclerView xrcy;
    private PresenterImpl presenter;
    private int index = 1;
    private String mUrl = "http://www.xieast.com/api/news/news.php?page=";
    List<News.DataBean> list = new ArrayList<>();
    private MyAdapter adapter;
    private Button qiehuan;
    private ImageView touxiang;
    private TextView nicheng;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        presenter = new PresenterImpl(this);
        adapter = new MyAdapter(list, MainActivity.this);
        xrcy.setAdapter(adapter);
        presenter.startRequest(mUrl + index, 1);
        xrcy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                index = 1;
                list.clear();
                presenter.startRequest(mUrl + index, 1);
            }

            @Override
            public void onLoadMore() {
                index++;
                presenter.startRequest(mUrl + index, 1);

            }
        });
        adapter.setOnclick(new MyAdapter.OnclickListen() {
            @Override
            public void onclick(View v, int position) {
                Toast.makeText(MainActivity.this,list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //登陆监听
    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            Toast.makeText(MainActivity.this, "成功了", Toast.LENGTH_LONG).show();
            nicheng.setText(data.get("name"));
            Log.d("zzz",data.toString());
            Glide.with(MainActivity.this).load(data.get("profile_image_url")).into(touxiang);

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(MainActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };


    private void initView() {
        xrcy = (XRecyclerView) findViewById(R.id.xrcy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        xrcy.setLayoutManager(linearLayoutManager);
        qiehuan = (Button) findViewById(R.id.qiehuan);
        qiehuan.setOnClickListener(this);
        touxiang = (ImageView) findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
        nicheng = (TextView) findViewById(R.id.nicheng);
        nicheng.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

    }

    @Override
    public void successData(Object data) {
        News dataBeans = (News) data;
        list.addAll(dataBeans.getData());
        adapter.notifyDataSetChanged();
        xrcy.refreshComplete();
        xrcy.loadMoreComplete();
    }

    @Override
    public void errorMsg(Object error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qiehuan:
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                xrcy.setLayoutManager(staggeredGridLayoutManager);
                xrcy.setItemAnimator(new DefaultItemAnimator());
                PuBuAdapter puBuAdapter = new PuBuAdapter(list, MainActivity.this);
                xrcy.setAdapter(puBuAdapter);

                break;
            case R.id.login:
                UMShareConfig shareConfig = new UMShareConfig();
                shareConfig.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(MainActivity.this).setShareConfig(shareConfig);
                UMShareAPI.get(this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, authListener);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
