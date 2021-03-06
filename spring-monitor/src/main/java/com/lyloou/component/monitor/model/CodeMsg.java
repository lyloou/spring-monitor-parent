package com.lyloou.component.monitor.model;

public interface CodeMsg {

    /**
     * 错误code 定义码
     * <br/> 编号规则： 10-00-00
     * 10-**-** 表示系统全局的
     * 20-**-** 表示api模块
     * 30-**-** 表示admin模块
     * <p>
     * <br/> 第一部分： 二位，系统模块
     * <br/> 第二部分： 二位，业务模块
     * <br/> 第三部分： 二位，具体错误
     * 如：统一的成功Code 为 101000
     */
    int code();

    String msg();
}
