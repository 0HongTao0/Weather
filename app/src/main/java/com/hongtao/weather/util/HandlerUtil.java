package com.hongtao.weather.util;

import android.os.Handler;
import android.os.Message;

/**
 * author：Administrator on 2017/7/14/014 09:16
 * email：935245421@qq.com
 */
public class HandlerUtil {
    /**
     * Handler 发送消息的工具
     * @param handler
     * @param messageType
     * @param messageObj
     */
    public static void sendMessageToHandler(Handler handler, int messageType, Object messageObj) {
        Message message = new Message();
        message.what = messageType;
        message.obj = messageObj;
        handler.sendMessage(message);
    }
}
