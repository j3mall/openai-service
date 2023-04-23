package com.j3mall.openai.mybatis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_request_api_log")
@ApiModel(value="RequestApiLog对象", description="API请求记录")
public class RequestApiLog extends Model<RequestApiLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String reqMethod;

    private String reqHost;

    private String reqPath;

    private String reqBody;

    private Integer status;

    @ApiModelProperty("请求耗时，单位毫秒")
    private Long timeSpent;

    private Integer respSize;

    private String respData;

    @ApiModelProperty("返回信息，用于调试")
    private String respMsg;

}
