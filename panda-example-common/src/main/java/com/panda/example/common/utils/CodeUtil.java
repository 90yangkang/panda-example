package com.panda.example.common.utils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author yk
 * @version 1.0
 * @describe 生产验证码图片
 * @date 2020-06-04 17:39
 */
public class CodeUtil {
    //得到随机数
    private static char[] chars = "ABCDEFGHIJKLMNPQRSTUVWXY3456789".toCharArray();
    private static Random random = new Random();
    private static String getCode(int length){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /*
     * @说明: 设计验证码并写出图片文件
     */
    public static void createCode(HttpServletRequest req, HttpServletResponse resp, String code) throws IOException {
        //1.创建画布
        BufferedImage bufferedImage = new BufferedImage(120, 34, BufferedImage.TYPE_INT_RGB);
        //2.创建画笔
        Graphics2D graphics2d = bufferedImage.createGraphics();
        //3.设置背景颜色  ， 先设置画笔颜色  ，在把画布涂满
        graphics2d.setColor(new Color(249, 250, 108));
        //4.涂满画布
        graphics2d.fillRect(0, 0, 120, 34);
        //5.更新画笔颜色
        graphics2d.setColor(new Color(62, 128, 27));
        //6.设置字体
        graphics2d.setFont(new Font("Dope Crisis", Font.PLAIN, 40));

        //7.保存到redis中，redis的key设置为时间戳+验证码，value就是验证码，同时将时间戳放到session中传给前端，前端在登陆接口中将这个时间戳回传给服务端
        long verifyCodeTimestamp = System.currentTimeMillis();
        HttpSession session=req.getSession();
        session.setAttribute("timestamp", verifyCodeTimestamp);
        session.setMaxInactiveInterval(300);//会话维持5分钟
        //8.写字
        graphics2d.drawString(code, 25,30);
        //9.收笔
        graphics2d.dispose();
        //10.保存到服务器上
        ImageIO.write(bufferedImage, "verifyCodeJpg", resp.getOutputStream());

    }
}
