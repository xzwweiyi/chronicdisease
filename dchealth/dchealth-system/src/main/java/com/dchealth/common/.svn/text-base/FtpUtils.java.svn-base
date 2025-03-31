package com.dchealth.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
@Slf4j
public class FtpUtils {

    private static final String SERVER_IP = "10.10.11.202";
    private static final Integer SERVER_PORT = 21;
    private static final String USER = "ECGVIEW";
    private static final String PASSWORD = "MedExTech";

    /**
     * 登录
     *
     * @return 是否登录成功
     */
    public static FTPClient login() throws Exception{
        // 创建FTPClient对象
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接FTP服务器
            ftpClient.connect(SERVER_IP, SERVER_PORT);
            // 登录FTP服务器
            ftpClient.login(USER, PASSWORD);
            // 获取FTP服务器的响应码
            int replyCode = ftpClient.getReplyCode();
            // 如果响应码不是“正常”，返回
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                throw new Exception("ftp登入失败");
            }
            // 设置被动模式（passive mode）
            ftpClient.enterLocalPassiveMode();
            // 设置文件传输类型为二进制文件类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        } catch (IOException e) {
            log.error("登录ftp 异常，{}",e.getMessage());
        }
        return ftpClient;
    }

    public static void ftpFinally(FTPClient ftpClient) {
        // 关闭FTP连接
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("关闭ftp 异常，{}",e.getMessage());

            }
        }
    }
}
