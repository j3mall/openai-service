package com.j3mall.openai.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j3mall.openai.mybatis.domain.RequestApiLog;
import com.j3mall.openai.mybatis.mapper.RequestApiLogMapper;
import org.springframework.stereotype.Service;

@Service
public class RequestApiLogServiceImpl extends ServiceImpl<RequestApiLogMapper, RequestApiLog> implements IService<RequestApiLog> {

}
