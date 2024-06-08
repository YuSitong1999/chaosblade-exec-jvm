package com.alibaba.chaosblade.exec.plugin.httpclient5;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.*;

/**
 * 拦截器，用于按类和方法信息匹配
 */
public class HttpClient5PointCut implements PointCut {
    /**
     * 用于按类名和类信息进行匹配
     */
    @Override
    public ClassMatcher getClassMatcher() {
        // 根据源代码，实现 org.apache.hc.client5.http.impl.classic.CloseableHttpClient 的只有
        // org.apache.hc.client5.http.impl.classic. InternalHttpClient 和 MinimalHttpClient
        return new OrClassMatcher()
                .or(new NameClassMatcher("org.apache.hc.client5.http.impl.classic.InternalHttpClient"))
                .or(new NameClassMatcher("org.apache.hc.client5.http.impl.classic.MinimalHttpClient"));
    }

    /**
     * 用于按方法信息进行匹配
     */
    @Override
    public MethodMatcher getMethodMatcher() {
        // 所有 CloseableHttpClient 中各种重载的 execute 方法，均涉及调用 doExecute 方法
        // protected abstract CloseableHttpResponse doExecute(HttpHost target, ClassicHttpRequest request, HttpContext context) throws IOException;
        // FIXME 暂未确定 httpclient4 为什么不只是对 doExecute ，还有 execute 注入故障。
        OrMethodMatcher methodNameMatcher = new OrMethodMatcher().or(new NameMethodMatcher("doExecute"));
        // 匹配方法的参数个数和类型
        ParameterMethodMatcher parameterMethodMatcher = new ParameterMethodMatcher(new String[]{
                "org.apache.hc.core5.http.HttpHost",
                "org.apache.hc.core5.http.ClassicHttpRequest",
                "org.apache.hc.core5.http.protocol.HttpContext"}, 3,
                ParameterMethodMatcher.EQUAL);
        // 方法名 和 方法参数类型以及个数 需要同时满足，所以用 AndMethodMatcher
        return new AndMethodMatcher().and(methodNameMatcher).and(parameterMethodMatcher);
    }
}
