package com.willwei.gateway.bind;

import java.util.Map;

public interface IGenericReference {
    Object invoke(Map<String, Object> paramMap);
}
