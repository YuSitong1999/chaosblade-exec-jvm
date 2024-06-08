package com.alibaba.chaosblade.exec.plugin.httpclient5;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpClient5Constant {
    public static final String TARGET_NAME = "httpclient5";
    public static final String URI_KEY = "uri";

    public static final String METHOD_GET_URI = "getUri";

    public static final Map<String, Method> methodMap = new HashMap<String, Method>();

    public static final int DEFAULT_TIMEOUT = 60000;

    public static final String REQUEST_ID = "c_request_id";

}
