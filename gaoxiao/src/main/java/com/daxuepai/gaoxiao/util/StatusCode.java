package com.daxuepai.gaoxiao.util;

public enum StatusCode {
    SUCCESS(200,"请求成功"),
    INSERT_MONITOR_FAIL(301, "插入monitor监控数据失败"),
    INSERT_MONITOR_EXCEPTION(302, "插入monitor监控数据异常"),
    MISSING_PARAMS_EXCEPTION(401, "请求缺少必需的参数"),
    INSERT_SMS_FAILED(501, "插入短信验证码出错"),
    SEND_SMS_FAILED(502, "发送短信失败！"),
    verification_code_error(503, "验证码错误"),
    SERVER_BUSY(601, "你的服务器走丢了，赶快联系管理员找回"),
    REGISTER_HAS_LOGIN(701, "当前已经登录其他账号，请退出后重新注册"),
    USER_NOT_LOGIN(702, "你还没有登录，请先去登录"),
    PHONE_HAS_REGISTER(702, "该手机号已经被注册"),
    REGISTER_FAILED(703, "注册失败"),
    LOGIN_FAILED(704, "登录失败"),
    GET_SCHOOL_LIST_FAILED(801, "获得高校列表失败"),
    SEND_POST_FAILED(802, "发帖失败"),
    LOGIN_REPEAT(803, "不要重复登录"),
    dont_need_exit(804, "没有用户登录，不需要退出"),
    exit_failed(805, "退出失败"),
    requet_too_buzy(806, "请求验证码太过频繁，请等会再尝试"),
    empty_content(807, "内容不能为空" );


    int code;
    String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    StatusCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }


}
