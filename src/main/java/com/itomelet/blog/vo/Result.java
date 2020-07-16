package com.itomelet.blog.vo;

import java.io.Serializable;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link http://13blog.site
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean status;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(boolean status, String message) {
        this.status = status;
        this.message = message;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
               "status=" + status +
               ", message='" + message + '\'' +
               ", data=" + data +
               '}';
    }
}
