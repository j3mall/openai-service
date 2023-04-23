package com.j3mall.openai.controller;

import com.alibaba.fastjson2.JSONObject;
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
    private OkHttpUtil okHttpUtil;

    @Value("${openai.apiHostPrefix}")
    String openAiHost;

    @GetMapping("/proxy")
    public JSONObject proxyGet(@ApiParam("请求的URL") @RequestParam String path,
        @ApiParam("URL参数") @RequestParam(required = false) String query) {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("proxyGet [{}], URL参数【{}】", path, query);

        String apiResp = okHttpUtil.get(openAiHost + path, null);
        log.info("proxyGet [{}] {}秒返回字符{}个", path, watch.getTime()/1000.0, apiResp.length());
        return JSONObject.parse(apiResp);
    }

    @PostMapping("/proxy")
    public JSONObject proxyPost(@ApiParam("请求的URL") @RequestParam String path,
        @ApiParam("JSON格式Body参数") @RequestBody String requestVo) {
        StopWatch watch = new StopWatch();
        watch.start();
        String unescapedStr = StringEscapeUtils.unescapeJava(requestVo);
        log.info("proxyPost [{}] Body参数【{}】", path, unescapedStr);

        String apiResp = okHttpUtil.post(openAiHost + path, unescapedStr);
        log.info("proxyPost [{}] {}秒返回字符{}个【{}】", path, watch.getTime()/1000.0, apiResp.length(), apiResp);
        return JSONObject.parse(apiResp);
    }

}
