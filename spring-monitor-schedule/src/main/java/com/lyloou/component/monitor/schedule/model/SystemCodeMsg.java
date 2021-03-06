package com.lyloou.component.monitor.schedule.model;

public enum SystemCodeMsg implements CodeMsg {
    SUCCESS(100000, "请求成功"),
    ERROR(100002, "系统繁忙"),
    ;

    private final int code;
    private final String msg;

    SystemCodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
