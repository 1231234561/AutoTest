package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost1 {
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
        //cookie
        List<Cookie> cookieList = store.getCookies();
        for (Cookie cookies : cookieList) {
            String name = cookies.getName();
            String value = cookies.getValue();
            System.out.println("cookie name =" + name + " ; cookie value =" + value);
        }
    }

    @Test(dependsOnMethods = {"testGetCookies"})
    public void testPostMethod() throws IOException {
        String uri=bundle.getString("test.post.with.cookies");
        String testurl=this.url+uri;

        //声明client对象，用来进行方法执行
        DefaultHttpClient client =new DefaultHttpClient();
        //声明post方法
        HttpPost post=new HttpPost(testurl);
        //添加参数
        JSONObject param=new JSONObject();
        param.put("name","zhangsna");
        param.put("age","18");

        //设置请求头信息 header
        post.setHeader("content-type","application/json");

        //参数信息添加到头中
        StringEntity entity=new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明对象来进行响应结果的存储
        String result;

        //设置cookie信息
        client.setCookieStore(this.store);

        //执行post方法
        HttpResponse response=client.execute(post);

        //获取响应结果
        result=EntityUtils.toString(response.getEntity());
        int code=response.getStatusLine().getStatusCode();
        System.out.println(result+code);

        JSONObject resultJson=new JSONObject(result);

        //处理结果、
        String success= (String) resultJson.get("success");
        Assert.assertEquals("success",success);//预期跟实际

        String status= (String) resultJson.get("status");
        Assert.assertEquals("1",status);


    }
}
