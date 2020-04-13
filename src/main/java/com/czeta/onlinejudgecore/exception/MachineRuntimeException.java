package com.czeta.onlinejudgecore.exception;

import com.czeta.onlinejudge.utils.enums.IBaseStatusMsg;
import com.czeta.onlinejudge.utils.exception.IBaseException;

/**
 * @ClassName MachineRuntimeException
 * @Description
 * @Author chenlongjie
 * @Date 2020/4/11 18:35
 * @Version 1.0
 */
public class MachineRuntimeException  extends RuntimeException implements IBaseException {
    private Integer code;
    private String message;

    public MachineRuntimeException() {}

    public MachineRuntimeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MachineRuntimeException(IBaseStatusMsg statusMsg) {
        this.code = statusMsg.getCode();
        this.message = statusMsg.getMessage();
    }

    public MachineRuntimeException(IBaseStatusMsg statusMsg, String message) {
        this.code = statusMsg.getCode();
        this.message = message == null ? statusMsg.getMessage() : message;
    }

    public MachineRuntimeException(Throwable cause) {
        super(cause);
        this.code = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
        this.message = cause.getMessage() == null ? IBaseStatusMsg.APIEnum.SERVER_ERROR.getMessage() : cause.getMessage();
    }

    public MachineRuntimeException(Throwable cause, String message) {
        super(message, cause);
        this.code = IBaseStatusMsg.APIEnum.SERVER_ERROR.getCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

