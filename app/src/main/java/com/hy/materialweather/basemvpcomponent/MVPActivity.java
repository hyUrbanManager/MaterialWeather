package com.hy.materialweather.basemvpcomponent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * MVP Activity基类
 *
 * 初始化Handler和Presenter角色，以下是示例代码
 * @Override
 * protected MVPHandler createHandler() {
 *      return new MVPHandler<>(this, new MVPHandler.HandleMessageListener<ShowInfoActivity>() {
 *          @Override
 *          public void handleMessage(Message msg, ShowInfoActivity activity) {
 *              switch (msg.what) {
 *                  case 1:
 *                      showWhatUGet((String) msg.obj);
 *              }
 *          }
 *      });
 * }
 * @Override
 * protected WebSocketPresenter createPresenter() {
 *      return new WebSocketPresenter(this, this, mHandler);
 * }
 *
 * @param <V>  View，传入MVP角色中的V接口
 * @param <P>  T 传入继承的Present
 */
public abstract class MVPActivity<V, P extends BasePresenter<V>> extends AppCompatActivity {

    //Presenter对象，管理者
    protected P mPresenter;

    //Handler管理者，要创建在继承的Activity
    //一定要给mHandler赋值！不要只是单纯的调用create方法而忘记赋值！
//    protected MVPHandler<V> mHandler;

    //创建方法，留给子类去实现
    protected abstract MVPHandler createHandler();

    protected abstract P createPresenterRefHandler();

    //初始化所有View控件
    protected abstract void initView();

    @SuppressWarnings("uncheck")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化Presenter
        mPresenter = createPresenterRefHandler();
        mPresenter.attachView((V)this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定，没有内存泄漏
        mPresenter.detachView();
    }

    /**
     * 内部处理UI消息的Handler，写好了方法，只需要传入Listener就可以
     * 在子类实现，不必使用弱引用
     */
    public static class MVPHandler extends Handler {
        //处理消息的接口，重写
        onHandleMessageListener listener;

        public MVPHandler(onHandleMessageListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            listener.handleMessage(msg);
        }

        /**
         * 消息处理的接口，暴露给外部使用
         */
        public interface onHandleMessageListener {
            void handleMessage(Message msg);
        }

    }



}
