package cn.edu.domain;
// default package

import cn.edu.util.SexType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * 用户实体
 *
 * @author nmyphp
 * <p>2014-12-19 更新：添加属性 isOnline<p>
 */

public class User implements Serializable {

    private Integer userId;//用户ID
    private String userNickname;//用户昵称
    private String userPassword;//用户密码
    private SexType userSex;//用户的性别
    private Set<Message> sendedMessages = new HashSet<Message>();//发送的消息
    private Set<Group> createdGroups = new HashSet<Group>();//创建的群组

    private Boolean isOnline;//用户是否在线

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public SexType getUserSex() {
        return userSex;
    }

    public void setUserSex(SexType userSex) {
        this.userSex = userSex;
    }

    public Set<Message> getSendedMessages() {
        return sendedMessages;
    }

    public void setSendedMessages(Set<Message> sendedMessages) {
        this.sendedMessages = sendedMessages;
    }

    public Set<Group> getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(Set<Group> createdGroups) {
        this.createdGroups = createdGroups;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    //测试用
    public User(String userNickname, String userPassword) {
        super();
        this.userNickname = userNickname;
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userNickname=" + userNickname
            + ", userPassword=" + userPassword + ", userSex=" + userSex
            + ", isOnline=" + isOnline + "]";
    }

}