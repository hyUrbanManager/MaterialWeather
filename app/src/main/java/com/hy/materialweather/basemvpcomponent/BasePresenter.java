package com.hy.materialweather.basemvpcomponent;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 业务逻辑人基类，泛型类型为传入的Activity
 */
public abstract class BasePresenter<T> {

    //View接口类型的弱引用
    protected Reference<T> mViewRef;

    //建立关联
    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    //获取引用
    protected T getView() {
        return mViewRef.get();
    }

    //引用是否被绑定了
    public boolean isViewAttached() {
        return (mViewRef != null) && (mViewRef.get() != null);
    }

    //解绑引用
    public void detachView() {
        if(mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }


}
