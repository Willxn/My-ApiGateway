package com.willwei.gateway.executor;

import com.willwei.gateway.datasource.Connection;
import com.willwei.gateway.executor.result.GatewayResult;
import com.willwei.gateway.mapping.HttpStatement;
import com.willwei.gateway.session.Configuration;
import com.willwei.gateway.util.SimpleTypeUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public abstract class BaseExecutor implements Executor{

    protected Configuration configuration;
    protected Connection connection;

    public BaseExecutor(Configuration configuration, Connection connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    @Override
    public GatewayResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception {
        // 参数处理；后续的一些参数校验也可以在这里封装。
        String methodName = httpStatement.getMethodName();
        String parameterType = httpStatement.getParameterType();
        String[] parameterTypes = new String[]{parameterType};
        Object[] args = SimpleTypeUtil.isSimpleType(parameterType) ? params.values().toArray() : new Object[]{params};
        log.info("执行调用 method：{}#{}.{}({}) args：{}", httpStatement.getApplication(), httpStatement.getInterfaceName(), httpStatement.getMethodName(), JSON.toJSONString(parameterTypes), JSON.toJSONString(args));
        // 抽象方法
        try {
            Object data = doExec(methodName, parameterTypes, args);
            return GatewayResult.buildSuccess(data);
        } catch (Exception e) {
            return GatewayResult.buildError(e.getMessage());
        }

    }

    protected abstract Object doExec(String methodName, String[] parameterTypes, Object[] args);
}
