package com.willwei.gateway.test;

import com.willwei.gateway.util.SimpleTypeUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void testString(){
        String s = "/wg/activity/sayHi?str=10001";
        String s1 = "01234";
        int index = s.indexOf("?");
        System.out.println(s1.indexOf("4"));
        System.out.println(s.substring(0, index));
    }
    @Test
    public void testMethod(){
        System.out.println(HttpHeaderValues.MULTIPART_FORM_DATA.toString());
    }

    @Test
    public void testSimpleClass(){
        System.out.println(SimpleTypeUtil.isSimpleType("java.abc.String"));
    }
}
