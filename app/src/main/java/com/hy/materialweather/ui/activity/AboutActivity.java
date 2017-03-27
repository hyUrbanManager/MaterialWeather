package com.hy.materialweather.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.uitls.Utils;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.hy.materialweather.uitls.Utils.d;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AboutActivity.class.getName() + " ";
    //view引用
    private Snackbar mSnackbar;

    //数据
    private int nowVersion;

    public static final int SUCCESS = 1;
    public static final int FAIL = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS: {
                    Response response = (Response) msg.obj;
                    try {
                        int getVersion = Integer.parseInt(response.body().string().trim());
                        if(getVersion <= nowVersion) {
                            mSnackbar = Snackbar.make(getWindow().getDecorView(), "当前已经是最新版本",
                                    Snackbar.LENGTH_LONG);
                        } else {
                            mSnackbar = Snackbar.make(getWindow().getDecorView(), "有新版本发布，点击右边按钮获取",
                                    Snackbar.LENGTH_LONG)
                                    .setAction("获取", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri uri = Uri.parse(github);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    });
                        }
                        mSnackbar.show();
                    } catch (IOException e) {
                        Utils.d(TAG + e.getMessage());
                        mSnackbar = Snackbar.make(getWindow().getDecorView(), "获取新版本失败",
                                Snackbar.LENGTH_LONG);
                        mSnackbar.show();
                    }
                    break;
                }
                case FAIL: {
                    String message = (String) msg.obj;
                    Utils.d(TAG + message);
                    mSnackbar = Snackbar.make(getWindow().getDecorView(), "网络错误",
                            Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                    break;
                }
            }
        }
    };

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

        //获取当前版本号
        nowVersion = Utils.getAppVersion(this);
        Utils.d(TAG + nowVersion);
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
                //获取最新版本号
                getNewVersion();
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

    /**
     * 获取最新版本号
     */
    public void getNewVersion() {

        //获取Url
        String url = getString(R.string.myserverversionaddress);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = WeatherDataModelImpl.client.newCall(request);
        try {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.sendMessage(mHandler, FAIL, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Utils.sendMessage(mHandler, SUCCESS, response);
                }
            });
        } catch (IllegalStateException e) {
            d(TAG + " 发生了重复递交申请" + e.getMessage());
        }
    }

}
