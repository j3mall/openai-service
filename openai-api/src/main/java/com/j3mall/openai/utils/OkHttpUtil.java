package com.j3mall.openai.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OkHttpUtil {
    @Resource
    private OkHttpClient okHttpClient;
    public static final MediaType JsonType = MediaType.parse("application/json; charset=utf-8");

    /**
     * GET请求
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public String getRequest(String url, Map<String, String> queries) {
        String responseBody = "{}";
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && !queries.keySet().isEmpty()) {
            boolean firstFlag = true;
            for (Entry<String, String> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append(String.format("?%s=%s", entry.getKey(), entry.getValue()));
                    firstFlag = false;
                } else {
                    sb.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
                }
            }
        }
        Request request = new Request.Builder().url(sb.toString()).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp GET请求异常, {}", ExceptionUtils.getStackTrace(e));
            throw new OpenAiException(e.getCause().toString());
        }
        return responseBody;
    }

    /**
     * POST请求
     */
    public String post(String url, String jsonStr) {
        String responseBody = "";
        RequestBody body = RequestBody.create(JsonType, jsonStr);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("okhttp post异常, {}, {}", url, ExceptionUtils.getStackTrace(e));
            throw new OpenAiException(e.getCause().toString());
        }
        return responseBody;
    }

    /**
     * POST请求返回流式数据
     */
    public String postStream(String url, String jsonStr) {
        final String[] responseBody = {""};
        RequestBody body = RequestBody.create(JsonType, jsonStr);
        Request request = new Request.Builder().url(url).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("异步请求异常, ", e);
                throw new OpenAiException(e.getCause().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseBody[0] = response.body().string();
            }
        });
        return responseBody[0];
    }

    /**
     * POST Form请求
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String postForm(String url, Map<String, String> params) {
        String responseBody = "";
        FormBody.Builder builder = new FormBody.Builder();
        // 添加参数
        if (params != null && !params.keySet().isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, String.valueOf(params.get(key)));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
            log.error("post postForm返回失败, {}, {}", url, response.body().string());
        } catch (Exception e) {
            log.error("okhttp postForm异常, {}, {}", url, ExceptionUtils.getStackTrace(e));
            throw new OpenAiException(e.getCause().toString());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * post 上传文件
     */
    public String postFile(String url, Map<String, Object> params, String fileType) {
        String responseBody = "";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //添加参数
        if (params != null && !params.keySet().isEmpty()) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
                    continue;
                }
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp postFile error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

}
