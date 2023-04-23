package com.j3mall.openai.config;

import com.j3mall.openai.utils.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一处理异常
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public JsonResult exceptionAdvice(Exception e) {
        return JsonResult.error(400, e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public JsonResult nullPointerExceptionAdvice(NullPointerException e) {
        return JsonResult.error(400, "空指针异常：" + e.getMessage());
    }

}

