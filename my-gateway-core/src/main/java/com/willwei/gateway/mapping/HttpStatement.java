package com.willwei.gateway.mapping;


import lombok.Getter;

@Getter
public class HttpStatement {

    /** RPC application 名称或其他服务 "api-gateway-test" **/
    private final String application;


    /** 服务接口，RPC、其他 "cn.bugstack.gateway.rpc.IActivityBooth" **/
    private final String interfaceName;

    /** 服务方法 RPC、其他 "sayHi" **/
    private final String methodName;

    /** 网关接口 uri "/wg/activity/sayHi" **/

    private final String uri;

    /** 接口类型 GET, POST, PUT, DELETE **/
    private final HttpCommandType httpCommandType;

    public HttpStatement(String application, String interfaceName, String methodName, String uri, HttpCommandType httpCommandType) {
        this.application = application;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.httpCommandType = httpCommandType;
    }

}
