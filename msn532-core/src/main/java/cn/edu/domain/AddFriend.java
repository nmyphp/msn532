package cn.edu.domain;

import java.io.Serializable;

/**
 * 添加好友实体
 *
 * @author nmyphp
 */
public class AddFriend implements Serializable {

    private Integer requestSenderId;//发出请求者
    private Integer requestReceiverId;//接收请求者

    public AddFriend() {
    }

    public Integer getRequestSenderId() {
        return requestSenderId;
    }

    public void setRequestSenderId(Integer requestSenderId) {
        this.requestSenderId = requestSenderId;
    }

    public Integer getRequestReceiverId() {
        return requestReceiverId;
    }

    public void setRequestReceiverId(Integer requestReceiverId) {
        this.requestReceiverId = requestReceiverId;
    }

    //测试用
    public AddFriend(Integer requestSenderId, Integer requestReceiverId) {
        this.requestSenderId = requestSenderId;
        this.requestReceiverId = requestReceiverId;
    }

}
