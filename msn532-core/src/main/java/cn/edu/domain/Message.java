package cn.edu.domain;

import cn.edu.util.MessageType;
import java.io.Serializable;
import java.util.Date;

/**
 * <h1>消息实体</h1>
 * <p>因为消息实体要在网络中传播，所以实现Serializable接口</p>
 *
 * @author nmyphp 2014-12-19 更新：添加属性 messageType
 */
public class Message implements Serializable {

    private Integer messageId;//消息ID
    private String messageContent;//消息内容
    private Date messageSendTime;//消息发送的时间
    private User Sender;//消息发送者
    private Integer receiverId;//消息接收者ID
    private MessageType messageType;//消息类型

    public Message() {
    }

    public Message(String messageContent, Date messageSendTime, User sender,
        Integer receiverId, MessageType messageType) {
        super();
        this.messageContent = messageContent;
        this.messageSendTime = messageSendTime;
        Sender = sender;
        this.receiverId = receiverId;
        this.messageType = messageType;
    }


    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getMessageSendTime() {
        return messageSendTime;
    }

    public void setMessageSendTime(Date messageSendTime) {
        this.messageSendTime = messageSendTime;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User sender) {
        Sender = sender;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }


    //测试用
    public Message(Integer messageId, String messageContent,
        Date messageSendTime, User sender, Integer receiverId,
        MessageType messageType) {
        this.messageId = messageId;
        this.messageContent = messageContent;
        this.messageSendTime = messageSendTime;
        Sender = sender;
        this.receiverId = receiverId;
        this.messageType = messageType;
    }
}