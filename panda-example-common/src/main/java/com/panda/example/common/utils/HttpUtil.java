package com.panda.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author yk
 * @version 1.0
 * @describe panda-example
 * @date 2021-06-04 21:57
 */
@Slf4j
public class HttpUtil {



    public static void main(String[] args) {
//        try {
//            String p="grant_type=client_credentials&realm_id=MMS_STORE_RL&client_id=DMALL";
//            String url="https://idam-pp.metrosystems.net/authorize/api/oauth2/access_token";
//            String input =  "DMALL:LWiR8JTrSA";
//            BASE64Encoder base = new BASE64Encoder();
//            String encodedPassword = "Basic " +base.encode(input.getBytes("UTF-8"));
//            System.out.println("result:"+HttpUtil.doPost(url,p,"Authorization",encodedPassword,"","application/x-www-form-urlencoded"));
//        }catch (Exception e)
//        {
//            System.out.println("ERROR:");
//            System.out.println(JsonUtil.toJson(e));
//        }

        //机器人测试
//        try {
//            Messge messge = new Messge();
//            messge.setMsg_type("text");
//            MessageContent content = new MessageContent();
//            content.setText(JsonUtil.toJson("sfsadfsdf"));
//            messge.setContent(content);
//            String mes = JsonUtil.toJson(messge);
//            String res = HttpUtil.doPost("https://open.feishu.cn/open-apis/bot/v2/hook/8443e4ee-ba85-4c64-9712-6760840e5dd5", mes, null, null, null, null);
//        } catch (Exception e) {
//        }

    }


    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static String doPost(String httpUrl, String param,String authorizationType,String authorizationValue,String acceptValue,String contentTypeValue) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");

            if(!StringUtils.isEmpty(contentTypeValue)) {
                connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            }
            else
            {
                connection.setRequestProperty("content-type", "application/json");
            }
            if(!StringUtils.isEmpty(authorizationType) && !StringUtils.isEmpty(authorizationValue)) {
                connection.setRequestProperty("Authorization", authorizationValue);

            }
            connection.setRequestProperty("accept", "*/*");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(5000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(5000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);

            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            log.info(url+"connection.getResponseCode()="+connection.getResponseCode());
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            log.error("HttpUtil.DoPost MalformedURLException",e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("HttpUtil.DoPost IOException",e);
            e.printStackTrace();
        }
        catch (Exception e)
        {
            log.error("HttpUtil.DoPost Exception",e);
            e.printStackTrace();
        }finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }
}
