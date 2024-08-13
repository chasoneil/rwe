package com.chason.common.utils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

public class JSONUtils
{
    /**
     * Bean对象转JSON
     *
     * @param object
     * @param dataFormatString
     * @return
     */
    public static String beanToJson(Object object, String dataFormatString)
    {
        if (object != null)
        {
            if (StringUtils.isEmpty(dataFormatString))
            {
                return JSONObject.toJSONString(object);
            }
            return JSON.toJSONStringWithDateFormat(object, dataFormatString);
        }
        else
        {
            return null;
        }
    }

    /**
     * Bean对象转JSON
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object)
    {
        if (object != null)
        {
            return JSON.toJSONString(object);
        }
        else
        {
            return null;
        }
    }

    /**
     * String转JSON字符串
     *
     * @param key
     * @param value
     * @return
     */
    public static String stringToJsonByFastjson(String key, String value)
    {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
        {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>(16);
        map.put(key, value);
        return beanToJson(map, null);
    }

    /**
     * 将json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static Object jsonToBean(String json, Object clazz)
    {
        if (StringUtils.isEmpty(json) || clazz == null)
        {
            return null;
        }
        return JSON.parseObject(json, clazz.getClass());
    }

    /**
     * json字符串转map
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json)
    {
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        return JSON.parseObject(json, Map.class);
    }

    /**
     * post请求传输map数据
     *
     * @param url
     * @param map
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByMap(String url, Map<String, String> map,
            String encoding) throws ClientProtocolException, IOException
    {
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 装填参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (map != null)
        {
            for (Entry<String, String> entry : map.entrySet())
            {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        //将参数进行编码为合适的格式,如将键值对编码为param1=value1&param2=value2
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, encoding);
        httpPost.setEntity(urlEncodedFormEntity);

        //执行 post请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = "";
        if (null != response && !"".equals(response)) {
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
            } else {
                result = "Error Response" + response.getStatusLine().getStatusCode();
            }
        }

        // 释放链接
        response.close();

        return result;
    }

    /**
     * post请求传输json数据
     *
     * @param url
     * @param json
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByJson(String url, String json,
            String encoding) throws ClientProtocolException, IOException
    {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 设置参数到请求对象中
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentEncoding(encoding);
        httpPost.setEntity(stringEntity);

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        {
            result = EntityUtils.toString(response.getEntity(), encoding);
        }
        // 释放链接
        response.close();

        return result;
    }

    /**
     * get请求传输数据
     *
     * @param url
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendGetData(String url, String encoding)
            throws ClientProtocolException, IOException
    {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-type", "application/json");
        // 通过请求对象获取响应对象
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        {
            result = EntityUtils.toString(response.getEntity(), encoding);
        }
        // 释放链接
        response.close();

        return result;
    }

    /**
     * post异步请求传输map数据
     *
     * @param url
     * @param map
     * @param encoding
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendSyncPostDataByMap(String url, Map<String, String> map,
            String encoding) throws ClientProtocolException, IOException
    {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        // Start the client
        httpclient.start();

        final HttpPost httpPost = new HttpPost(url);

        // 装填参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (map != null)
        {
            for (Entry<String, String> entry : map.entrySet())
            {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        // 将参数进行编码为合适的格式,如将键值对编码为param1=value1&param2=value2
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, encoding);
        httpPost.setEntity(urlEncodedFormEntity);

        // httpPost.setHeader("Connection", "close");
        List<Future<HttpResponse>> respList = new LinkedList<Future<HttpResponse>>();
        respList.add(httpclient.execute(httpPost, null));

        // Print response code
        for (Future<HttpResponse> response : respList)
        {
            try
            {
                response.get().getStatusLine();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println(response.get().getStatusLine());
        }

        httpclient.close();
        return null;

//        //配置io线程
//        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
//                setIoThreadCount(Runtime.getRuntime().availableProcessors())
//                .setSoKeepAlive(true)
//                .build();
//
//        //设置连接池大小
//        ConnectingIOReactor ioReactor=null;
//        try {
//            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
//        } catch (IOReactorException e) {
//            e.printStackTrace();
//        }
//
//        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
//        connManager.setMaxTotal(1000);
//        connManager.setDefaultMaxPerRoute(100);
//
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(50000)
//                .setSocketTimeout(50000)
//                .setConnectionRequestTimeout(1000)
//                .build();
//
//        final CloseableHttpAsyncClient client = HttpAsyncClients.custom().
//                setConnectionManager(connManager)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//
//        //构造请求
//        HttpPost httpPost = new HttpPost(url);
//
//        // 装填参数
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        if (map != null)
//        {
//            for (Entry<String, String> entry : map.entrySet())
//            {
//                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//            }
//        }
//
//        //将参数进行编码为合适的格式,如将键值对编码为param1=value1&param2=value2
//        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, encoding);
//        httpPost.setEntity(urlEncodedFormEntity);
//
//        //start
//        client.start();
//
//        //异步请求
//        client.execute(httpPost, new Back());
//
//        while(true){
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    static class Back implements FutureCallback<HttpResponse>{

        private long start = System.currentTimeMillis();
        Back(){
        }

        public void completed(HttpResponse httpResponse) {
            try {
                System.out.println("cost is:"+(System.currentTimeMillis()-start)+":"+EntityUtils.toString(httpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void failed(Exception e) {
            System.err.println(" cost is:"+(System.currentTimeMillis()-start)+":"+e);
        }

        public void cancelled() {

        }
    }

}
