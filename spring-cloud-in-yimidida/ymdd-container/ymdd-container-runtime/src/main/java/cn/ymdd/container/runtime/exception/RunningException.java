
package cn.ymdd.container.runtime.exception;

import cn.ymdd.api.Code;
import cn.ymdd.api.code.BaseCode;
import cn.ymdd.api.data.Response;
import cn.ymdd.framework.toolkit.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunningException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(RunningException.class);
    protected Object[] data;
    protected final Code code;
    protected boolean ignore;
    private static final long serialVersionUID = -9072616765763158023L;

    public RunningException(Code code) {
        super(code.getMessage(), new Throwable(code.getMessage()));
        this.ignore = false;
        this.code = code;
    }

    @SafeVarargs
    public <T> RunningException(Code code, T... data) {
        super(code.getMessage(), new Throwable(code.getMessage()));
        this.ignore = false;
        this.data = data;
        this.code = code;
    }

    public RunningException(String message) {
        super(message, new Throwable(message));
        this.ignore = false;
        this.code = BaseCode.ERROR;
    }

    @SafeVarargs
    public <T> RunningException(String message, T... data) {
        super(message, new Throwable(message));
        this.ignore = false;
        this.code = BaseCode.ERROR;
        this.data = data;
    }

    public RunningException(Code code, boolean ignore) {
        super(code.getMessage(), new Throwable(code.getMessage()));
        this.ignore = false;
        this.ignore = ignore;
        this.code = code;
    }

    @SafeVarargs
    public <T> RunningException(Code code, boolean ignore, T... data) {
        super(code.getMessage(), new Throwable(code.getMessage()));
        this.ignore = false;
        this.ignore = ignore;
        this.code = code;
        this.data = data;
    }

    public RunningException(Code code, String message) {
        super(message, new Throwable(message));
        this.ignore = false;
        this.code = code;
    }

    @SafeVarargs
    public <T> RunningException(Code code, String message, T... data) {
        super(message, new Throwable(message));
        this.ignore = false;
        this.code = code;
        this.data = data;
    }

    public RunningException(Code code, String message, boolean ignore) {
        super(message, new Throwable(message));
        this.ignore = false;
        this.ignore = ignore;
        this.code = code;
    }

    @SafeVarargs
    public <T> RunningException(Code code, String message, boolean ignore, T... data) {
        super(message, new Throwable(message));
        this.ignore = false;
        this.ignore = ignore;
        this.code = code;
        this.data = data;
    }

    public RunningException(Code code, Throwable e) {
        this(code, e, false);
    }

    @SafeVarargs
    public <T> RunningException(Code code, Throwable e, T... data) {
        this(code, e, false);
        this.data = data;
    }

    public RunningException(Code code, Throwable e, boolean ignore) {
        super(code.getMessage(), e);
        this.ignore = false;
        this.ignore = ignore;
        this.code = code;
    }

    @SafeVarargs
    public <T> RunningException(Code code, Throwable e, boolean ignore, T... data) {
        super(code.getMessage(), e);
        this.ignore = false;
        this.ignore = ignore;
        this.code = code;
        this.data = data;
    }

    public boolean isIgnoreException() {
        return this.ignore;
    }

    public String getExceptionMessage() {
        return super.getMessage();
    }

    public Code getExceptionCode() {
        return this.code;
    }

    public <T> T getExceptionData() {
        return (T) this.data;
    }

    public static final void logger(Throwable e) {
        logger((Logger)null, e, (String)null);
    }

    public static final void logger(Throwable e, String message) {
        logger((Logger)null, e, message);
    }

    public static final void logger(Logger log, Throwable e) {
        logger(log, e, (String)null);
    }

    public static final void logger(Logger log, Throwable e, String message) {
        if (log == null) {
            log = logger;
        }

        if (log.isErrorEnabled()) {
            if (e == null && message == null) {
                throw new IllegalArgumentException();
            }

            StringBuffer error = new StringBuffer();
            if (e == null) {
                if (StringUtils.isNotEmpty(message)) {
                    error.append("Error-message: ").append(message);
                    log.error(error.toString(), e);
                }

                return;
            }

            if (e instanceof RunningException && ((RunningException)e).isIgnoreException()) {
                return;
            }

            if (StringUtils.isNotEmpty(e.getMessage())) {
                error.append("Error-stack: ").append(e.getMessage());
            }

            if (StringUtils.isNotEmpty(message)) {
                error.append("Error-message: ").append(message);
            }

            if (e instanceof RunningException && ((RunningException)e).getExceptionData() != null) {
                error.append("Error-data: ").append(JSON.toJSONString(((RunningException)e).getExceptionData()));
            }

            log.error(error.toString(), e);
        }

    }

    public static final <T> Response<T> failure(Code code, Exception exception) {
        return failure(code, exception, (String)null, (Object)null);
    }

    public static final <T> Response<T> failure(Code code, Exception exception, String message) {
        return failure(code, exception, (String)null, (Object)null);
    }

    public static final <T> Response<T> failure(Code code, Exception exception, Object data) {
        return failure(code, exception, (String)null, data);
    }

    public static final <T> Response<T> failure(Code code, Exception exception, String message, Object data) {
        Response<T> cr = failure(exception, message, data);
        cr.setCode(code.getCode());
        return cr;
    }

    public static final <T> Response<T> failure(Exception exception) {
        return failure((Exception)exception, (String)null, (Object)null);
    }

    public static final <T> Response<T> failure(Exception exception, String message) {
        return failure((Exception)exception, (String)message, (Object)null);
    }

    public static final <T> Response<T> failure(Exception exception, Object data) {
        return failure((Exception)exception, (String)null, (Object)data);
    }

    public static final <T> Response<T> failure(Exception exception, String message, Object data) {
        Response<T> cr = new Response();
        if (exception instanceof RunningException) {
            RunningException re = (RunningException)exception;
            cr.setMessage(StringUtil.isEmpty(message) ? re.getExceptionMessage() : message);
            cr.setCode(re.getExceptionCode().getCode());
            cr.setStatus(false);
            cr.setData(data);
        } else {
            cr.setMessage(StringUtil.isEmpty(message) ? BaseCode.ERROR.getMessage() : message);
            cr.setCode(BaseCode.ERROR.getCode());
            cr.setStatus(false);
            cr.setData(data);
        }

        return cr;
    }
}
