package com.j3mall.openai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.j3mall.openai.mybatis.domain.RequestApiLog;
import com.j3mall.openai.mybatis.service.impl.RequestApiLogServiceImpl;
import com.j3mall.openai.utils.OkHttpUtil;
import com.j3mall.openai.utils.OpenAiException;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestApiDecorator {

    @Value("${openai.apiHostPrefix}")
    String openAiHost;
    @Value("${openai.maxLogRespSize:10000}")
    Integer maxLogRespSize;
    @Resource
    private OkHttpUtil okHttpUtil;

    @Resource
    private RequestApiLogServiceImpl requestApiLogService;

    public JSONObject createRequestAndExecute(String reqMethod, String reqPath, String reqBody) {
        RequestApiLog requestApiLog = createRequestApiLog(openAiHost, reqMethod, reqPath, reqBody);
        RequestApiLog updateRequest = new RequestApiLog();
        updateRequest.setId(requestApiLog.getId());
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("proxy{} [{}], URL参数【{}】", reqMethod, reqPath, reqBody);

        String apiResp = null;
        try {
            if ("GET".equals(reqMethod)) {
                apiResp = okHttpUtil.get(openAiHost + reqPath, null);
                log.info("proxy{} [{}] {}秒返回字符{}个", reqMethod, reqPath, watch.getTime()/1000.0, apiResp.length());
            } else {
                apiResp = okHttpUtil.post(openAiHost + reqPath, reqBody);
                log.info("proxyPost [{}] {}秒返回字符{}个【{}】", reqPath, watch.getTime()/1000.0, apiResp.length(), apiResp);
            }
            // 更新请求记录表的数据
            updateRequest.setStatus(3);
            updateRequest.setTimeSpent(watch.getTime());
            updateRequest.setRespSize(apiResp.length());
            updateRequest.setRespData(apiResp.length() > maxLogRespSize ? null : apiResp);
            requestApiLogService.updateById(updateRequest);
            return JSONObject.parse(apiResp);
        } catch (OpenAiException | JSONException ex) {
            updateRequest.setStatus(2);
            updateRequest.setTimeSpent(watch.getTime());
            updateRequest.setRespMsg(ex.getMessage());
            updateRequest.setRespData(apiResp);
            updateRequest.setRespSize(Optional.ofNullable(apiResp).map(String::length).orElse(null));
            requestApiLogService.updateById(updateRequest);
            throw ex;
        }
    }

    public RequestApiLog createRequestApiLog(String reqHost, String reqMethod, String reqPath, String reqBody) {
        RequestApiLog requestApiLog = new RequestApiLog();
        requestApiLog.setReqMethod(reqMethod);
        requestApiLog.setReqHost(reqHost);
        requestApiLog.setReqPath(reqPath);
        requestApiLog.setReqBody(reqBody);
        requestApiLogService.save(requestApiLog);
        log.info("DB插入请求记录: {}", JSON.toJSONString(requestApiLog));
        return requestApiLog;
    }

}
