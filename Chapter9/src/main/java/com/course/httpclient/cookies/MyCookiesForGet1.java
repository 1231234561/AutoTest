package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.CookieList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Entity;

import javax.xml.ws.Response;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet1 {
    private String url;
    private ResourceBundle bundle;
    //存储cookies信息
    private CookieStore store;

    //测试方法执行前，读取配置文件信息
    @BeforeTest
    public void beforeTest() {
        //读取配置文件的名称,字符编码
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.url");
    }

    @Test
    public void testGetCookies() throws IOException {
        String result;
        //配置文件拼接url
        String uri = bundle.getString("getCookies.url");
        String testurl = this.url + uri;

        //测试逻辑
        //HttpGet httpGet =new HttpGet(this.url+bundle.getString("getCookies.url"));
        HttpGet get = new HttpGet(testurl);
        //DefaultHttpClient client=new DefaultHttpClient();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

        //获取cookies信息
        this.store = client.getCookieStore();
        List<Cookie> cookieList = store.getCookies();
        for (Cookie cookies : cookieList) {
            String name = cookies.getName();
            String value = cookies.getValue();
            System.out.println("cookie name =" + name + " ; cookie value =" + value);
        }
    }

    @Test(dependsOnMethods = {"testGetCookies"})
    public void testGetWithCookies() throws IOException {
        String result;
        String uri = bundle.getString("test.get.with.cookie");
        String testurl = this.url + uri;
        System.out.println(testurl);
        HttpGet get = new HttpGet(testurl);
        DefaultHttpClient client = new DefaultHttpClient();

        //设置cookies信息
        client.setCookieStore(this.store);
        HttpResponse response = client.execute(get);
        //获取响应状态码
        int code = response.getStatusLine().getStatusCode();
        System.out.println("getStatusCode=" + code);
        if (code == 200) {
            result=EntityUtils.toString(response.getEntity(),"utf-8");
            System.out.println(result);
        }else{
            System.out.println("没有抓到对应的信息");
        }

    }
}
