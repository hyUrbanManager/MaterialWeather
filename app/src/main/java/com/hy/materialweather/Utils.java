package com.hy.materialweather;

import android.os.Handler;
import android.os.Message;

import java.io.Closeable;
import java.io.IOException;

/**
 * 派生工具
 */
public class Utils implements Closeable {

    @Override
    public void close() throws IOException {

    }

    /**
     * 发送消息，两个方法
     * @param handler
     * @param what
     * @return
     */
    public static final boolean sendEmptyMessage(Handler handler, int what) {
        return sendMessage(handler, what, null);
    }

    public static final boolean sendMessage(Handler handler, int what, Object obj) {
        if (handler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            handler.sendMessage(msg);
            return true;
        } else {
            return false;
        }
    }

}
