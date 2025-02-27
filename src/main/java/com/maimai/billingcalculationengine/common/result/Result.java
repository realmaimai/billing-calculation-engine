package com.maimai.billingcalculationengine.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Generic response wrapper")
public class Result<T> implements Serializable {

    @Schema(description = "Response code", example = "200")
    private Integer code;

    @Schema(description = "Response message", example = "Success")
    private String msg;

    @Schema(description = "Response data")
    private T data;

    public static <T> Result<T> successWithoutData(String msg) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> success(T object, String msg) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = 200;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 500;
        return result;
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        return result;
    }

    public static <T> Result<T> fail(T data, int code, String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        result.data = data;
        return result;
    }
}