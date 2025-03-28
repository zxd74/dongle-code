package web.message;

public class MessageUtils {
    package com.iwanvi.ad.utils;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Dongle
 * @desc
 * @since 2022/7/11 10:38
 */
public class MessageUtils {

    private static final AtomicInteger MESSAGE_USER_COUNT = new AtomicInteger(0);

    private static final Map<String, SseEmitter> MESSAGE_NOTIFY_MAP = new ConcurrentHashMap<>();

    /**
     * 消息用户注册
     * @param userId
     * @return
     */
    public static SseEmitter connect(String userId){
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(completionCallBack(userId));
//        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));
        MESSAGE_NOTIFY_MAP.put(userId, sseEmitter);
        // 数量+1
        MESSAGE_USER_COUNT.getAndIncrement();
        return sseEmitter;
    }

    /**
     * 消息推送：指定用户推送消息
     * @param userId
     * @param jsonMsg
     */
    public static void sendMessage(String userId, String jsonMsg) {
        try {
            SseEmitter emitter = MESSAGE_NOTIFY_MAP.get(userId);
            if (emitter == null) {
                return;
            }
            emitter.send(jsonMsg, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            removeUser(userId);
        }
    }

    /**
     * 消息推送：指定用户群推送消息
     * @param jsonMsg
     * @param employeeCodes
     */
    public static void batchSendMessage(String jsonMsg, List<String> employeeCodes) {
        employeeCodes.forEach(userId -> sendMessage(jsonMsg, userId));
    }

    /**
     * 消息推送：为全部用户推送消息
     * @param jsonMsg
     */
    public static void batchSendMessage(String jsonMsg) {
        MESSAGE_NOTIFY_MAP.forEach((k, v) -> {
            try {
                v.send(jsonMsg, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                removeUser(k);
            }
        });
    }

    /**
     * 注销消息用户
     * @param userId
     */
    public static void removeUser(String userId) {
        SseEmitter emitter = MESSAGE_NOTIFY_MAP.get(userId);
        if(emitter != null){
            emitter.complete();
        }
        MESSAGE_NOTIFY_MAP.remove(userId);
        // 数量-1
        MESSAGE_USER_COUNT.getAndDecrement();
    }

    private static Runnable completionCallBack(String userId) {
        return () -> removeUser(userId);
    }

    /**
     * 消息用户超时
     * @param userId
     * @return
     */
    private static Runnable timeoutCallBack(String userId) {
        return () -> removeUser(userId);
    }

    /**
     * 消息用户链接异常
     * @param userId
     * @return
     */
    private static Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> removeUser(userId);
    }

    /**
     * 获取消息用户数量
     * @return
     */
    public static int getUserCount() {
        return MESSAGE_USER_COUNT.intValue();
    }

    /**
     * 获取全部消息用户标识
     * @return
     */
    public static List<String> getIds() {
        return new ArrayList<>(MESSAGE_NOTIFY_MAP.keySet());
    }
}