package cn.edu.ui;

import cn.edu.domain.Group;
import cn.edu.domain.User;
import cn.edu.util.MyImageIcon;
import cn.edu.util.SexType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

/**
 * 重写JList的显示效果 2014-12-22 更新：加入群组列表的显示效果
 *
 * @author nmyphp
 */
public class ListItemRenderer extends JPanel implements ListCellRenderer {

    private JLabel myface;
    private JLabel nickname;

    public ListItemRenderer() {
        setLayout(new BorderLayout());
        myface = new JLabel();
        nickname = new JLabel();
    }

    /**
     * 重写ListCellRenderer接口的方法
     *
     * @param value 加入到JList中的对象
     * @param isSelected 该对象是否被选中
     * @author nmyphp
     */
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {

        if (value instanceof User) {
            User user = (User) value;
            //判断用户的性别、是否在线，设置相应的头像
            if (user.getUserSex() == SexType.MALE) {
                if (user.getIsOnline() == null || user.getIsOnline()) {
                    myface.setIcon(MyImageIcon.MALE_NOT_ONLINE);//男，在线
                } else {
                    myface.setIcon(MyImageIcon.MALE_ONLINE);//男，不在线
                }
            } else {
                if (user.getIsOnline() == null || user.getIsOnline()) {
                    myface.setIcon(MyImageIcon.FEMALE_NOT_ONLINE);//女，在线
                } else {
                    myface.setIcon(MyImageIcon.FEMALE_ONLINE);//女，不在线
                }
            }
            nickname.setText(user.getUserNickname());//设置好友昵称

        } else if (value instanceof Group) {
            Group group = (Group) value;
            myface.setIcon(MyImageIcon.GROUP);
            nickname.setText(group.getGroupName());//设置群昵称
        }

        add(myface, BorderLayout.WEST);
        add(nickname, BorderLayout.CENTER);

        //如果被选中，加边框，否则去掉边框
        if (isSelected) {
            setBorder(new LineBorder(new Color(0xD46D73), 2, false));
            //setOpaque(true);
        } else {
            //setOpaque(false);
            setBorder(new LineBorder(new Color(0xD46D73), 0, false));
        }
        return this;
    }
}
