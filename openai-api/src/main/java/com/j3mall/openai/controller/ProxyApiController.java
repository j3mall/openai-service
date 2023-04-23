package com.j3mall.openai.controller;

import com.alibaba.fastjson2.JSONObject;
import com.j3mall.openai.service.RequestApiDecorator;
import com.j3mall.openai.utils.OkHttpUtil;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProxyApiController {

    @Resource
    private RequestApiDecorator requestApiDecorator;

    @GetMapping("/proxy")
    public JSONObject proxyGet(@ApiParam("请求的URL") @RequestParam String path,
        @ApiParam("URL参数") @RequestParam(required = false) String query) {
        return requestApiDecorator.createRequestAndExecute("GET", path, query);
    }

    @PostMapping("/proxy")
    public JSONObject proxyPost(@ApiParam("请求的URL") @RequestParam String path,
        @ApiParam("JSON格式Body参数") @RequestBody String requestVo) {
        String unescapedStr = StringEscapeUtils.unescapeJava(requestVo);
        return requestApiDecorator.createRequestAndExecute("POST", path, unescapedStr);
    }

}
