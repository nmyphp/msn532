package cn.edu.util;

import cn.edu.domain.Message;
import cn.edu.domain.User;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息发送者
 *
 * @author nmyphp
 * @see User
 */
public class GroupMessage extends Message implements Serializable {

    private int goupId;//标示本消息所属群

    public GroupMessage() {
    }

    public GroupMessage(String messageContent, Date messageSendTime, User sender, MessageType messageType, int goupId) {
        this.setMessageContent(messageContent);
        this.setMessageSendTime(messageSendTime);
        this.setMessageType(messageType);
        this.setSender(sender);
        this.goupId = goupId;
    }

    public int getGoupId() {
        return goupId;
    }

    public void setGoupId(int goupId) {
        this.goupId = goupId;
    }


}
