package com.alibaba.chaosblade.exec.plugin.httpclient5.model;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.httpclient5.HttpClient5Constant;

import java.util.ArrayList;
import java.util.List;

public class HttpClient5ModelSpec extends FrameworkModelSpec {
    public HttpClient5ModelSpec() {
        addActionExample();
    }

    /**
     * 向命令行中加入提示信息
     */
    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("httpclient5 client delay experiment");
                action.setExample("# Do a delay 3s experiment with HTTP request URI = https://www.taobao.com\n" +
                        "blade create httpclient5 delay --uri https://www.taobao.com --time 3000");
            } else if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("httpclient5 client throws custom exception experiment");
                action.setExample("# Do a throws custom exception with HTTP request URI = https://www.taobao.com\n" +
                        "blade c httpclient5 throwCustomException --exception=java.lang.Exception --exception-message=customException --uri=https://www.taobao.com/");
            }
        }
    }

    /**
     * 向命令行增加匹配器
     */
    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new UriMatchDefSpec());
        return matcherSpecs;
    }

    /**
     * 目标
     */
    @Override
    public String getTarget() {
        return HttpClient5Constant.TARGET_NAME;
    }

    /**
     * 短描述
     */
    @Override
    public String getShortDesc() {
        return "httpclient5 experiment";
    }

    /**
     * 长描述
     */
    @Override
    public String getLongDesc() {
        return "httpclient5 experiment for testing service delay and exception.";
    }
}
