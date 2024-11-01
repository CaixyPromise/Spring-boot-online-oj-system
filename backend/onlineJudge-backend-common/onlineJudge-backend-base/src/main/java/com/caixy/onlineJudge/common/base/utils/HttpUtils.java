package com.caixy.onlineJudge.common.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于发起Http请求工具方法
 *
 * @author CAIXYPROMISE
 * @version 1.0
 * @since 2024/8/2 上午1:04
 */

public class HttpUtils
{

    /**
     * get
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doGet(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    /**
     * get 请求，不需要传入 path 参数
     *
     * @param host    完整的请求URL
     * @param headers 请求头部信息
     * @param querys  请求的查询参数
     * @return HttpResponse 响应对象
     * @throws Exception 发生的异常
     */
    public static HttpResponse doGet(String host, Map<String, String> headers, Map<String, String> querys) throws
                                                                                                           IOException
    {
        HttpClient httpClient = wrapClient(host);
        HttpGet request = new HttpGet(buildUrl(host, "", querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }
        return httpClient.execute(request);
    }


    /**
     * post form
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @param bodys
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host,
                                      String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      Map<String, String> bodys)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null)
        {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet())
            {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    /**
     * post form 请求，不需要传入 path 参数
     *
     * @param host    完整的请求URL
     * @param headers 请求头部信息
     * @param querys  请求的查询参数
     * @param bodys   请求的表单数据
     * @return HttpResponse 响应对象
     * @throws Exception 发生的异常
     */
    public static HttpResponse doPost(String host,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      Map<String, String> bodys) throws IOException
    {
        HttpClient httpClient = wrapClient(host);
        HttpPost request = new HttpPost(buildUrl(host, "", querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null)
        {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            for (String key : bodys.keySet())
            {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }


    /**
     * Post String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(body))
        {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Post stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      byte[] body)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null)
        {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Put String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     String body)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(body))
        {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     byte[] body)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null)
        {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doDelete(String host, String path, String method,
                                        Map<String, String> headers,
                                        Map<String, String> querys)
            throws Exception
    {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet())
        {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws
                                                                                         UnsupportedEncodingException
    {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!org.apache.commons.lang3.StringUtils.isBlank(path))
        {
            sbUrl.append(path);
        }
        if (querys != null)
        {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet())
            {
                if (0 < sbQuery.length())
                {
                    sbQuery.append("&");
                }
                if (org.apache.commons.lang3.StringUtils.isBlank(query.getKey()) && !org.apache.commons.lang3.StringUtils.isBlank(query.getValue()))
                {
                    sbQuery.append(query.getValue());
                }
                if (!org.apache.commons.lang3.StringUtils.isBlank(query.getKey()))
                {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue()))
                    {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length())
            {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host)
    {
        return HttpClientBuilder.create().build();
    }
}