package com.lyloou.component.monitor.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result {

    private int code;
    private String msg;
    private Object data;

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SystemCodeMsg.SUCCESS.code());
        result.setMsg(SystemCodeMsg.SUCCESS.msg());
        result.setData(data);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(SystemCodeMsg.ERROR.code());
        result.setMsg(msg);
        return result;
    }

    public static Result newInstance() {
        return new Result();
    }

    public static Result codeMsg(CodeMsg codeMsg) {
        Result result = new Result();
        result.setCode(codeMsg.code());
        result.setMsg(codeMsg.msg());
        result.setData(null);
        return result;
    }

    public static boolean isSuccess(Result body) {
        return body != null && body.code == SystemCodeMsg.SUCCESS.code();
    }

}
