package com.dchealth.cro.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * 图片工具
 */
public class ImgUtils {

    /**
     * 图片大小
     * @param inputStream
     * @return 字节
     */
    public static Integer imgSize(InputStream inputStream){
        if (inputStream == null){
            return null;
        }
        try {
            return inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片大小
     * @param file
     * @return 字节
     */
    public static Long imgSize(File file){
        if (file == null){
            return null;
        }
        return file.length();
    }

    /**
     * 判断图片大小是否大于限定大小
     * @param inputStream
     * @param size 限定大小
     * @return
     */
    public static boolean imgCheckSize(InputStream inputStream,int size){
        return imgSize(inputStream) > size;
    }

    /**
     * 判断图片大小是否大于限定大小
     * @param file
     * @param size 限定大小
     * @return
     */
    public static boolean imgCheckSize(File file,Long size){
        return imgSize(file) > size;
    }

    public static boolean imgCheckWidthAndheight(InputStream inputStream) throws IOException {
        BufferedImage img = ImageIO.read(inputStream);
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        System.out.println("inputStream = " + imgWidth+","+imgHeight);
        return false;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\qqq.jpg");
        System.out.println("args = " +imgCheckWidthAndheight(new FileInputStream(file)));
    }
}
