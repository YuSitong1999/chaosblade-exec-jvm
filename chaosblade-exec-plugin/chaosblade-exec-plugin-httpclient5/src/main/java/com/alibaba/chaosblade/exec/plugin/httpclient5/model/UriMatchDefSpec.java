package com.alibaba.chaosblade.exec.plugin.httpclient5.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

// 用于匹配 URI 的模型
public class UriMatchDefSpec extends BasePredicateMatcherSpec {

    // 名字
    @Override
    public String getName() {
        return "uri";
    }

    // 描述
    @Override
    public String getDesc() {
        return "Uniform Resource Identifier";
    }

    // 是否无参数，false表示有参数，true表示无参数
    @Override
    public boolean noArgs() {
        return false;
    }

    // 是否必填
    @Override
    public boolean required() {
        return true;
    }
}
