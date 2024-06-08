package com.alibaba.chaosblade.exec.plugin.httpclient5;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.httpclient5.model.HttpClient5ModelSpec;

/**
 * @author yusitong1999@foxmail.com
 */
public class HttpClient5Plugin implements Plugin {
    @Override
    public String getName() {
        return "httpclient5";
    }

    @Override
    public ModelSpec getModelSpec() {
        return new HttpClient5ModelSpec();
    }

    @Override
    public PointCut getPointCut() {
        return new HttpClient5PointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new HttpClient5Enhancer();
    }
}
