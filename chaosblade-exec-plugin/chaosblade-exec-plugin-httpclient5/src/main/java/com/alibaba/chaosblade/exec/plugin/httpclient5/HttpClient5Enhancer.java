package com.alibaba.chaosblade.exec.plugin.httpclient5;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.Objects;

import static com.alibaba.chaosblade.exec.plugin.httpclient5.HttpClient5Constant.METHOD_GET_URI;
import static com.alibaba.chaosblade.exec.plugin.httpclient5.HttpClient5Constant.methodMap;


/**
 * @author yusitong1999@foxmail.com
 * 从类和方法中提取信息，创建 EnhancerModel
 */
public class HttpClient5Enhancer extends BeforeEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient5Enhancer.class);

    // 尝试转化为 org.apache.hc.client5.http.config.Configurable 接口后调用 RequestConfig getConfig(); 方法，
    private static final String GET_CONFIG = "getConfig";

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[]
                                                methodArguments)
            throws Exception {
        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(HttpClient5Constant.URI_KEY, getUri(object, methodArguments));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("http matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        int timeout = getTimeout(object, methodArguments);
        enhancerModel.setMethod(method).setObject(object).setMethodArguments(methodArguments);

        postDoBeforeAdvice(enhancerModel);
        enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout, className));
        return enhancerModel;
    }

    // 从方法的参数中提取 uri
    protected String getUri(Object instance, Object[] object) throws Exception {
        // 从第 2 个参数 ClassicHttpRequest request 中提取 uri
        Object classicHttpRequest = object[1];
        Method method = methodMap.get(METHOD_GET_URI);
        if (method == null) {
            method = object[1].getClass().getMethod(METHOD_GET_URI, null);
            methodMap.put(METHOD_GET_URI, method);
        }
        Object uri = method.invoke(classicHttpRequest, null);
        if (uri != null) {
            return UrlUtils.getUrlExcludeQueryParameters(uri.toString());
        }
        return null;
    }

    protected int getTimeout(Object instance, Object[] methodArguments) {
        try {
            int connectionTimeout = 0;
            int socketTimeout = 0;
            // 从请求配置中读取
            Object config = null;
            if (methodArguments != null && methodArguments.length >= 2) {
                Object request = methodArguments[1];
                config = ReflectUtil.invokeMethod(request, GET_CONFIG, new Object[0], false);
                if (config == null) {
                    Object params = ReflectUtil.invokeMethod(request, GET_PARAMS, new Object[0], false);
                    Class<?> paramsConfigClass = instance.getClass().getClassLoader().loadClass(PARAM_CONFIG);
                    config = ReflectUtil.invokeStaticMethod(paramsConfigClass, GET_REQUEST_CONFIG, new Object[]{params}, false);
                }
            }
            if (config != null) {
                connectionTimeout = ReflectUtil.invokeMethod(config, REQUEST_GET_CONNECT_TIMEOUT, new Object[0], false);
                socketTimeout = ReflectUtil.invokeMethod(config, REQUEST_GET_SOCKET_TIMEOUT, new Object[0], false);
            }
            if (connectionTimeout > 0 && socketTimeout > 0) {
                return connectionTimeout + socketTimeout;
            }

            // 从client配置中读取
            Object params = ReflectUtil.invokeMethod(instance, GET_PARAMS, new Object[0], false);
            if (params == null) {
                LOGGER.warn("HttpParams from HttpClient not found. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            Class<?> httpConnectionParamsClass = instance.getClass().getClassLoader().loadClass(HTTP_CONNECTION_PARAMS);
            if (connectionTimeout <= 0) {
                connectionTimeout = ReflectUtil.invokeStaticMethod(httpConnectionParamsClass, CLIENT_GET_CONNECTION_TIMEOUT, new Object[]{params}, false);
            }
            if (socketTimeout <= 0) {
                socketTimeout = ReflectUtil.invokeStaticMethod(httpConnectionParamsClass, CLIENT_GET_SOCKET_TIMEOUT, new Object[]{params}, false);
            }
            if (connectionTimeout + socketTimeout == 0) {
                LOGGER.warn("timeout did not config. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            return connectionTimeout + socketTimeout;
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value {}", DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HTTPCLIENT4, "true");
    }

    protected TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout, String className) {
        return new BaseTimeoutExecutor(classLoader, timeout) {
            @Override
            public Exception generateTimeoutException(ClassLoader classLoader) {
                return new SocketTimeoutException("Read timed out");
            }
        };
    }
}
