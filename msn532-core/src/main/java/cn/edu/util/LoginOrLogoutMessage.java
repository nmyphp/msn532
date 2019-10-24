package cn.edu.util;

import java.io.Serializable;


/**
 * 消息实体-登录、退出 2014-12-20
 * <p>2014-12-21 添加：C1</p>
 *
 * @author nmyphp
 */

public class LoginOrLogoutMessage implements Serializable {

    private int userId;//用户ID
    private String clentIP;//客户端的IP
    private int clentPort;//客户端端口
    private MessageType messageType;

    public LoginOrLogoutMessage() {
    }

    public LoginOrLogoutMessage(int userId, String clentIP, int clentPort, MessageType messageType) {
        this.userId = userId;
        this.clentIP = clentIP;
        this.clentPort = clentPort;
        this.messageType = messageType;
    }

    public String getClentIP() {
        return clentIP;
    }

    public void setClentIP(String clentIP) {
        this.clentIP = clentIP;
    }

    public int getClentPort() {
        return clentPort;
    }

    public void setClentPort(int clentPort) {
        this.clentPort = clentPort;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    //C1 2014-12-21 添加
    @Override
    public String toString() {
        return "userId=" + userId + ", clentIP="
            + clentIP + ", clentPort=" + clentPort;
    }

}
