package com.cmri.pic.wall.server.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhuyin on 10/28/15.
 */
public class ResponseMessage {
    private static AtomicLong idGen = new AtomicLong(0);
    public ResponseMessage(){
        this(true, "");
    }
    public ResponseMessage(boolean succeed, String message){
        this.succeed = succeed;
        this.message = message;
        this.id = idGen.incrementAndGet();
    }
    private long id;
    private boolean succeed;
    private String message;
    private Map<String, String> data = new HashMap<>();
    private long time = System.currentTimeMillis();
    public long getId(){
        return id;
    }
    public ResponseMessage setSucceed(boolean succeed) {
        this.succeed = succeed;
        return this;
    }
    public boolean isSucceed() {
        return succeed;
    }
    public ResponseMessage setMessage(String message) {
        this.message = message;
        return this;
    }
    public String getMessage() {
        return message;
    }
    public ResponseMessage setTime(long time){
        this.time = time;
        return this;
    }
    public long getTime() {
        return time;
    }
    public Map<String, String> getData() {
        return data;
    }
    public ResponseMessage set(String key, String val){
        data.put(key, val);
        return this;
    }
    public String get(String key){
        return data.get(key);
    }
    @Override
    public int hashCode() {
        return (data.toString() + message + succeed).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseMessage)) return false;

        ResponseMessage that = (ResponseMessage) o;

        if (succeed != that.succeed) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return data.equals(that.data);
    }

    @Override
    public String toString() {
        return "response: {" +
                "id=" + id +
                ", succeed=" + succeed +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", time=" + time +
                '}';
    }
}
