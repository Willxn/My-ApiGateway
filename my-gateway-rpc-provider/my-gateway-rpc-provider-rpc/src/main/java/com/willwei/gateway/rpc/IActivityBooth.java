package com.willwei.gateway.rpc;

import com.willwei.gateway.rpc.dto.XReq;

public interface IActivityBooth {
    String sayHi(String str);

    String insert(XReq req);

    String test(String str, XReq req);
}
