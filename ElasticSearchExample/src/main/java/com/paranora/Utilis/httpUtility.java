package com.paranora.Utilis;

import org.apache.http.client.methods.HttpPut;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by yangqun on 2016/09/02.
 */
public class httpUtility {

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    private static final String PUT = "PUT";

    private static String prepareParameter(Map<String, Object> parameter) {
        if (null==parameter || parameter.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String key : parameter.keySet()) {
            String value = (String) parameter.get(key);
            if (sb.length() < 1) {
                sb.append(key).append("=").append(value);
            } else {
                sb.append("&").append(key).append("=").append(value);
            }
        }
        return sb.toString();
    }

    private static String prepareUrl(String url, Map<String, Object> parameter) {
        String paramString = prepareParameter(parameter);
        if (paramString != null && paramString.trim().length() > 0) {
            url += "?" + paramString;
        }
        return url;
    }

    private static HttpURLConnection prepareConnectionProperty(HttpURLConnection connection, Map<String, String> requestProperties) throws Exception {
        if (null == requestProperties || requestProperties.size() < 1) {
            return connection;
        }
        for (String key : requestProperties.keySet()) {
            connection.setRequestProperty(key, requestProperties.get(key));
        }
        return connection;
    }

    private static HttpURLConnection prepareConnection(String url, String method, Map<String, String> requestProperties) throws Exception {
        URL reqeusUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) reqeusUrl.openConnection();
        connection = prepareConnectionProperty(connection, requestProperties);
//        connection.setRequestProperty("accept", "*/*");
//        connection.setRequestProperty("connection", "Keep-Alive");
//        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        connection.setRequestMethod(method);
        return connection;
    }

    private static HttpURLConnection prepareConnection(String url, String method, Map<String, Object> parameter, Map<String, String> requestProperties) throws Exception {
        return prepareConnection(url, method, prepareParameter(parameter), requestProperties);
    }

    private static HttpURLConnection prepareConnection(String url, String method, String parameter, Map<String, String> requestProperties) throws Exception {
        HttpURLConnection connection = prepareConnection(url, method, requestProperties);

        connection.setDoInput(true);
        connection.setDoOutput(true);

        if (null != parameter && parameter.length() > 0) {
            OutputStream os = connection.getOutputStream();
            os.write(parameter.getBytes("utf-8"));
            os.flush();
        }
        return connection;
    }

    private static String reciveResponseData(HttpURLConnection connection) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "/n" + line;
        }
        br.close();
        return result;
    }

    public static String put(String url, Map<String, Object> parameter) throws Exception {
        return reciveResponseData(prepareConnection(url, PUT, parameter, null));
    }

    public static String put(String url, String parameter,Map<String, String> requestProperties) throws Exception {
        return reciveResponseData(prepareConnection(url, PUT, parameter, requestProperties));
    }

    public static String post(String url, Map<String, Object> parameter) throws Exception {
        return reciveResponseData(prepareConnection(url, POST, parameter, null));
    }

    public static String post(String url, String parameter,Map<String, String> requestProperties) throws Exception {
        return reciveResponseData(prepareConnection(url, POST, parameter, requestProperties));
    }

    public static String get(String url, Map<String, Object> parameter) throws Exception {
        HttpURLConnection connection = prepareConnection(prepareUrl(url, parameter), GET, null);

        connection.connect();
        return reciveResponseData(connection);
    }

    public static int delete(String url, Map<String, Object> parameter) throws Exception {
        HttpURLConnection connection = prepareConnection(prepareUrl(url, parameter), DELETE, null);
        connection.setDoOutput(true);
        return connection.getResponseCode();
    }

    public static int delete(String url, String parameter) throws Exception {
        HttpURLConnection connection = prepareConnection(url, DELETE, parameter, null);
        connection.setDoOutput(true);
        return connection.getResponseCode();
    }
}
