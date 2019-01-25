package cn.ymdd.api.data;

import cn.ymdd.api.Code;
import cn.ymdd.api.code.BaseCode;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.util.StringUtils;

@Api(description="公共响应体")
public class Response<T>
        implements Serializable
{
    private static final long serialVersionUID = 23304424250156394L;

    @JSONField(ordinal=1)
    @ApiModelProperty(value="响应的状态", required=true)
    private boolean status;

    @JSONField(ordinal=2)
    @ApiModelProperty(value="响应的结果码", required=true)
    private String code;

    @JSONField(ordinal=3)
    @ApiModelProperty(value="响应的信息提示", required=true)
    private String message;

    @JSONField(ordinal=4)
    @ApiModelProperty(value="所有响应数据的签名", required=true)
    private String signature;

    @JSONField(ordinal=5)
    @ApiModelProperty(value="响应的JSON数据体对象", required=true)
    private T data;

    public String getCode()
    {
        return this.code;
    }

    public Response<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getSignature() {
        return this.signature;
    }

    public <T> Response<T> setSignature(String signature)
    {
        this.signature = signature;
        return (Response<T>) this;
    }

    public T getData() {
        return this.data;
    }

    public Response<T> setData(Object data)
    {
        this.data = (T) data;
        return this;
    }

    public boolean isStatus() {
        return this.status;
    }

    public Response<T> setStatus(boolean status) {
        this.status = status;
        return this;
    }
    @JSONField(serialize=false)
    public boolean isSuccess() {
        return (this.status) && ("200".equals(this.code));
    }
    @JSONField(serialize=false)
    public boolean isFailure() {
        return !isSuccess();
    }
    @JSONField(serialize=false)
    public boolean hasData() {
        return (this.data != null) && (!StringUtils.isEmpty(this.data.toString()));
    }

    public static final <T> Response<T> success()
    {
        return success(BaseCode.SUCCESS);
    }

    public static final <T> Response<T> success(Code code)
    {
        return success(code, null);
    }

    public static final <T> Response<T> success(Code code, Object data)
    {
        Response cr = new Response();
        cr.message = code.getMessage();
        cr.code = code.getCode();
        cr.data = data;
        cr.status = true;
        return cr;
    }

    public static final <T> Response<T> failure(Code code)
    {
        return failure(code, code.getMessage(), null);
    }

    public static final <T> Response<T> failure(Code code, Object data)
    {
        return failure(code, code.getMessage(), data);
    }

    public static final <T> Response<T> failure(Code code, String message)
    {
        return failure(code, message, null);
    }

    public static final <T> Response<T> failure(Code code, String message, Object data)
    {
        Response cr = new Response();
        cr.code = code.getCode();
        cr.message = message;
        cr.data = data;
        cr.status = false;
        return cr;
    }

    public static final <T> Response<T> failure(Code code, Exception exception) {
        return failure(code, exception, null);
    }

    public static final <T> Response<T> failure(Code code, Exception exception, Object data) {
        return failure(code, exception, null, data);
    }

    public static final <T> Response<T> failure(Code code, Exception exception, String message, Object data)
    {
        Response cr = new Response();
        cr.message = ((message == null) || (message.isEmpty()) ? exception.getMessage() : message);
        cr.code = code.getCode();
        cr.data = data;
        cr.status = false;
        return cr;
    }
}

