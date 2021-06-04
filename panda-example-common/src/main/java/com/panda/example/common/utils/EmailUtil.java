package com.panda.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;


/**
 * @author yk
 * @version 1.0
 * @describe 邮件发送
 * @date 2021-06-04 17:39
 */
@Slf4j
public class EmailUtil {
    //邮件服务器主机地址
    private static String HOST="smtp.exmail.qq.com";
    //帐号
    private static String ACCOUNT = "yk_ang@outlook.com";
    //密码
    private static String PASSWORD = "xxxxxxx";



    /**
     * @param emails  发送邮件给谁,支持传多个邮箱，逗号分隔
     * @param title   邮件的标题
     * @param emailMsg  邮件信息
     */
    public static void sendMail(String[] emails,String title, String emailMsg) {
        for (String email : emails){
            int retryCount = 0;//邮件发送失败需要重试的次数
            boolean result = sendSingleMail(email,title,emailMsg,null,null);
            if (!result){
                //如果发送邮件失败，则重试3次
                while (retryCount < 3){
                    if (sendSingleMail(email,title,emailMsg,null,null)){
                        break;
                    }else {
                        retryCount++;
                    }
                }
            }
        }
    }


    /**
     * @param emails  发送邮件给谁,支持传多个邮箱，逗号分隔
     * @param title   邮件的标题
     * @param emailMsg  邮件信息
     */
    public static void sendMailAffix(String[] emails,String title, String emailMsg,String fileUrl,String fileName) {
        for (String email : emails){
            int retryCount = 0;//邮件发送失败需要重试的次数
            boolean result = sendSingleMail(email,title,emailMsg,null,null);
            if (!result){
                //如果发送邮件失败，则重试3次
                while (retryCount < 3){
                    if (sendSingleMail(email,title,emailMsg,null,null)){
                        break;
                    }else {
                        retryCount++;
                    }
                }
            }
        }
    }


    /**
     * @param email  发送邮件给谁
     * @param title   邮件的标题
     * @param emailMsg  邮件信息
     * @param fileUrl  附件ossurl
     * @param fileName  附件名称
     */
    public static boolean sendSingleMail(String email,String title, String emailMsg,String fileUrl,String fileName) {
        try {
            log.info("EmailUtil-sendSingleMail start send email to {}",email);
            // 1.创建一个程序与邮件服务器会话对象 Session
            Properties props = new Properties();
            //设置发送的协议
            props.setProperty("mail.transport.protocol", "SMTP");

            //设置发送邮件的服务器
            props.setProperty("mail.host", HOST);
            props.setProperty("mail.smtp.auth", "true");// 指定验证为true

            // 创建验证器
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    //设置发送人的帐号和密码      帐号          授权码
                    return new PasswordAuthentication(ACCOUNT, PASSWORD);
                }
            };
            //会话
            Session session = Session.getInstance(props, auth);

            // 2.创建一个Message，它相当于是邮件内容
            Message message = new MimeMessage(session);

            //设置发送者
            message.setFrom(new InternetAddress(ACCOUNT));

            //设置发送方式与接收者
            message.setRecipient(RecipientType.TO, new InternetAddress(email));

            //设置邮件主题
            message.setSubject(title);
            // message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

            //设置邮件内容
            if(StringUtils.isEmpty(fileUrl) || StringUtils.isEmpty(fileName)) {
                message.setContent(emailMsg, "text/html;charset=utf-8");
            }
            else {
                //邮件添加附件:http://yuncode.net/code/c_50a5fe2d4868897
                // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                // 设置HTML内容
                html.setContent(emailMsg, "text/html;charset=utf-8");
                mainPart.addBodyPart(html);

                MimeBodyPart attachment = new MimeBodyPart();
                URL url= new URL(fileUrl);
                DataSource fds=new URLDataSource(url);
                attachment.setDataHandler(new DataHandler(fds));
                // 为附件设置文件名
                attachment.setFileName(fileName);
                mainPart.addBodyPart(attachment);
                message.setContent(mainPart);
            }

            // 3.创建 Transport用于将邮件发送
            Transport.send(message);
            log.info("EmailUtil-sendSingleMail send email to {} success",email);
            return true;
        }catch (Exception e){
            log.error("EmailUtil-sendSingleMail send email to " + email + " exception!", e);
            return false;
        }
    }

    public static String html(String body) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head>");
        htmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        htmlBuilder.append("<style type=\"text/css\">");
        htmlBuilder.append("TABLE{border-collapse:collapse;border-left:solid 1 #000000; border-top:solid 1 #000000;padding:5px;}");
        htmlBuilder.append("TH{border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");
        htmlBuilder.append("TD{font:normal;border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");
        htmlBuilder.append("</style></head>");
        htmlBuilder.append("<body><div>");

        htmlBuilder.append(body);
        htmlBuilder.append("<br>Best Regards");

        htmlBuilder.append("</div></body></html>");
        return htmlBuilder.toString();
    }

    public static void main(String[] args) {
        sendSingleMail("panda@xxx.com","测试","hello","https://test-bucket.dmallcdn.com/订单满足率报表1622785078726.xlsx","xxx.xlsx");
    }
}
