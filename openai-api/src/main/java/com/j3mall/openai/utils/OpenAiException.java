package com.j3mall.openai.utils;

import lombok.Data;

@Data
public class OpenAiException extends RuntimeException {

    public OpenAiException(String message) {
        super(message);
    }

}
