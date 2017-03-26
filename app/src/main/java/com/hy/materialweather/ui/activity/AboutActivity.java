package com.hy.materialweather.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.uitls.Utils;

import java.util.Random;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AboutActivity.class.getName() + " ";
    //view引用
    private Snackbar mSnackbar;

    //网站
    public static final String github = "https://github.com/hyUrbanManager/MaterialWeather";
    private final String website = "http://apphuangye.cn";
    private final String wenjuanxing = "https://www.wenjuan.net/s/YjUJJj3/";

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //随机换背景
        Random random = new Random();
        int k = random.nextInt(10);
        if (k > 8) {
            ImageView background = (ImageView) findViewById(R.id.imageView);
            TextView text = (TextView) findViewById(R.id.text1);
            text.setVisibility(View.GONE);
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
            ViewGroup.LayoutParams layoutParams = appBarLayout.getLayoutParams();
            layoutParams.height = (int) ((float) Utils.getScreenDisplay(this).widthPixels / 2.26f);
            Utils.d(TAG + "调整宽度为 " + layoutParams.height);
            appBarLayout.setLayoutParams(layoutParams);
            background.setImageResource(R.drawable.sex_background);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //初始化View
        initView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri;
        switch (id) {
            case R.id.source:
                //打开github
                uri = Uri.parse(github);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.update:
                //显示已经是最新
                if (mSnackbar == null) {
                    mSnackbar = Snackbar.make(getWindow().getDecorView(), "当前已经是最新版本",
                            Snackbar.LENGTH_SHORT);
                }
                if (mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                } else {
                    mSnackbar.show();
                }
                break;
            case R.id.feedback:
                //打开问卷星网站
                uri = Uri.parse(wenjuanxing);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.web:
                //打开个人网页
                uri = Uri.parse(website);
                intent.setData(uri);
                startActivity(intent);
                break;
        }
    }
}
