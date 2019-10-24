package cn.edu.domain;

/**
 * 群组实体
 *
 * @author nmyphp
 */
public class Group {

    private Integer groupId;//群组ID
    private User creater;//创建者
    private String groupName;//群组的名称

    public Group() {
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
