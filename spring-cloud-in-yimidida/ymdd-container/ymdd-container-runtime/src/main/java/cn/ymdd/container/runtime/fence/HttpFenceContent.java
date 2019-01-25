package cn.ymdd.container.runtime.fence;

import cn.ymdd.framework.toolkit.util.StringUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HttpFenceContent
        implements Serializable
{
    private static final long serialVersionUID = 5604851301103465446L;
    private Map<String, String[]> parameters = new HashMap();
    private Map<String, String> headers = new HashMap();
    private String Protocol;
    private String domain;
    private String method;
    private String bodies;
    private int http = -1;
    private String uri;
    private String ip;
    private long size;
    private String id;
    private transient Class<?> clazz;
    private transient Object data;

    public HttpFenceContent()
    {
    }

    public HttpFenceContent(String id)
    {
        setId(id);
    }

    public boolean hasBody() {
        return (this.bodies != null) && (!StringUtil.isEmpty(this.bodies));
    }

    public String[] getParameter(String key)
    {
        return (String[])this.parameters.get(key);
    }

    public Map<String, String[]> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public void addParameters(String key, String value) {
        addParameters(key, new String[] { value });
    }

    public void addParameters(String key, String[] value) {
        this.parameters.put(key, value);
    }

    public String getHeader(String key) {
        return (String)this.headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    public String getBodies() {
        return this.bodies;
    }

    public void setBodies(String bodies) {
        this.bodies = (bodies == null ? "" : bodies);
    }

    public int getHttp() {
        return this.http;
    }

    public void setHttp(int http) {
        this.http = http;
    }

    public String getProtocol() {
        return this.Protocol;
    }

    public void setProtocol(String protocol) {
        this.Protocol = protocol;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
