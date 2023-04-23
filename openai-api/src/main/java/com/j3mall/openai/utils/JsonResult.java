package com.j3mall.openai.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "接口返回结果")
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Integer SUCCESS_CODE = 0;
    private static final String SUCCESS_MESSAGE = "操作成功";
    private static final String FAILURE_MESSAGE = "操作失败";

    @ApiModelProperty(value = "返回码, 0-成功 其它-失败", example = "0")
    private Integer code;

    @ApiModelProperty(value = "错误信息")
    private String message;

    @ApiModelProperty(value = "返回内容，成功时可能没有data")
    private T data;

    @ApiModelProperty(value = "服务器时间戳")
    private Long created;

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.getCode());
    }

    /**
     * 正确时返回的信息
     */
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    /**
     * 错误时返回的信息
     */
    public static <T> JsonResult<T> error(Integer code, String msg) {
        return new JsonResult<>(code, msg,null);
    }

    public static <T> JsonResult<T> error(Integer code) {
        return new JsonResult<>(code, FAILURE_MESSAGE,null);
    }

    public JsonResult(Integer code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.created = System.currentTimeMillis() / 1000;
    }

}
