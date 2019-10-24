package cn.edu.domain;

import java.io.Serializable;

/**
 * 加入群组实体
 *
 * @author nmyphp
 */

public class AddGroup implements Serializable {

    private Integer userId;//用户ID
    private Integer groupId;//群组ID

    public AddGroup() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }


}
