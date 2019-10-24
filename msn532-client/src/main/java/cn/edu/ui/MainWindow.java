package cn.edu.ui;

import cn.edu.domain.Group;
import cn.edu.domain.Message;
import cn.edu.domain.User;
import cn.edu.service.Client;
import cn.edu.service.GroupService;
import cn.edu.service.UserService;
import cn.edu.util.GroupMessage;
import cn.edu.util.MessageType;
import cn.edu.util.MyImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 显示层-主窗口
 *
 * @author nmyphp
 * <p>这个类包括好友列表、群组列表等</p>
 */

public class MainWindow extends JFrame {

    private JPanel contentPane;
    private JList groups;//群组列表
    private JList friends;//好友列表
    private JPopupMenu friendOption;//好友列表中的弹出窗口
    private JPopupMenu groupOption;//群组列表中的弹出窗口
    private User selectedFriend;//选中的好友
    private Group selectedGroup;//选中的群组

    private HashMap<Integer, ChatWindow> chatMap = new HashMap<Integer, ChatWindow>();//存储某个窗口对应某个好友ID
    private JLabel alert;//显示消息提醒
    private Client client;//用来发送消息的后台客户端
    private static final int MAX_USERCHAT_NUMBER = 1000;//好友聊天窗口的最大数量
    private DefaultListModel groupData;

    public MainWindow(User user) {

        client = new Client(user);//设置客户端用户

        //////////////////////////////////////////////////////////////////
        //初始化主窗口
        /////////////////////////////////////////////////////////////////

        setTitle("LL主窗口");
        setResizable(false);
        setBounds(100, 100, 315, 604);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //tab
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(10, 86, 289, 480);
        contentPane.add(tabbedPane);

        //好友列表
        JScrollPane scrollPaneFriends = new JScrollPane();
        scrollPaneFriends.setBorder(null);
        tabbedPane.addTab("LL 好 友", null, scrollPaneFriends);
        friends = new JList(initFriendsList());
        friends.setCellRenderer(new ListItemRenderer());
        scrollPaneFriends.setViewportView(friends);

        //群组列表
        JScrollPane scrollPaneGroups = new JScrollPane();
        scrollPaneGroups.setBorder(null);
        tabbedPane.addTab("LL 群 组", null, scrollPaneGroups);
        groups = new JList(initGroupsList());//初始化群组列表
        groups.setCellRenderer(new ListItemRenderer());
        scrollPaneGroups.setViewportView(groups);

        //logo
        JLabel topImage = new JLabel();
        topImage.setHorizontalAlignment(SwingConstants.CENTER);
        topImage.setIcon(MyImageIcon.LOGO_USER);
        topImage.setBounds(27, 10, 70, 74);
        contentPane.add(topImage);

        //显示用户昵称
        JLabel userNickName = new JLabel();
        userNickName.setForeground(new Color(30, 144, 255));
        userNickName.setFont(new Font("Microsoft YaHei", Font.PLAIN, 20));
        userNickName.setText(client.getCurrentUser().getUserNickname());
        userNickName.setBounds(107, 10, 146, 30);
        contentPane.add(userNickName);

        //消息提示器
        alert = new JLabel();
        alert.setHorizontalAlignment(SwingConstants.CENTER);
        alert.setBounds(256, 44, 32, 32);
        contentPane.add(alert);

        //好友列表的右键菜单
        friendOption = new JPopupMenu();
        JMenuItem sendFriendMessage = new JMenuItem("发送消息");
        JMenuItem deleteFriend = new JMenuItem("删除好友");
        friendOption.add(sendFriendMessage);
        friendOption.add(deleteFriend);

        //群组列表的右键菜单
        groupOption = new JPopupMenu();
        JMenuItem sendGroupMessage = new JMenuItem("发送群消息");
        JMenuItem dropOutGroup = new JMenuItem("退出群组");
        groupOption.add(sendGroupMessage);
        groupOption.add(dropOutGroup);

        autoAppendMessage();
        messageListener();//启动消息监听器

        /**
         * 当好友列表的Item被选中时，设置selectedFriend的值
         */
        friends.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e2) {
                if (!e2.getValueIsAdjusting()) {

					if (friends.getSelectedValue() != null) {
						selectedFriend = (User) friends.getSelectedValue();
					}
                }
            }
        });

        /**
         * 在好友列表中单击右键时显示弹出菜单
         */
        friends.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e2) {
                if (SwingUtilities.isRightMouseButton(e2) && selectedFriend != null) {
                    friendOption.show(friends, e2.getX(), e2.getY());
                }
            }
        });

        /**
         * 点击发送消息选项时，弹出聊天窗口
         */
        sendFriendMessage.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e2) {
                OpenChat(client.getCurrentUser(), selectedFriend);
            }
        });

        /**
         * 点击发送群消息选项时，弹出聊天窗口
         */
        sendGroupMessage.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e2) {
                OpenChat(client.getCurrentUser(), selectedGroup);
            }
        });

        /**
         * 当群组列表的Item被选中时，设置selectedGroup的值
         */
        groups.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e2) {
                if (!e2.getValueIsAdjusting()) {
					if (groups.getSelectedValue() != null) {
						selectedGroup = (Group) groups.getSelectedValue();
					}
                }
            }
        });

        /**
         * 在群组列表中单击右键时显示弹出菜单
         */
        groups.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e2) {
                if (SwingUtilities.isRightMouseButton(e2) && selectedGroup != null) {
                    groupOption.show(groups, e2.getX(), e2.getY());
                }
            }
        });

        /**
         * 当用户点击消息提醒图标时，打开聊天窗口
         */
        alert.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                if (!client.getMyMessages().isEmpty()) {
                    Message message = client.getMyMessages().poll();
                    ChatWindow cw = null;
                    if (message.getMessageType() == MessageType.GOURPMSG) {
                        //如果是群消息，则在groupData里查出Group，然后打开聊天窗口
                        Group group = getGroupFromGroupData(((GroupMessage) message).getGoupId());
                        cw = OpenChat(client.getCurrentUser(), group);
                    } else {
                        cw = OpenChat(client.getCurrentUser(), message.getSender());
                    }
                    cw.appendText(message);
                }
            }
        });


    }

    ////////////////////////////////////////////////////////////////////////////////////////
    //功能扩展--辅助函数
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 监视消息队列，如果有消息，则在主窗口上显示消息提醒
     *
     * @author nmyphp
     */
    private void messageListener() {
        Thread threadAlert = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (!client.getMyMessages().isEmpty()) {
                        alert.setIcon(MyImageIcon.FLASHALERT);
                    } else {
                        alert.setIcon(null);
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        );
        threadAlert.start();
    }

    /**
     * 监视消息队列是否有已经打开了聊天窗口却没有加入到聊天窗口的消息
     *
     * @author nmyphp
     */
    private void autoAppendMessage() {
        Thread threadAutoAppendMessage = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Iterator<Message> iter = client.getMyMessages().iterator();
                    while (iter.hasNext()) {
                        Message message = iter.next();
                        int id = 0;
                        //如果是群组消息，则加一个整数，@see OpenChat()
                        if (message.getMessageType() == MessageType.GOURPMSG) {
                            id = ((GroupMessage) message).getGoupId() + MainWindow.MAX_USERCHAT_NUMBER;
                        } else if (message.getMessageType() == MessageType.NORMAL) {
                            id = message.getSender().getUserId();
                        }
                        ChatWindow chatwindow = chatMap.get(id);
                        if (chatwindow != null) {
							if (!chatwindow.isShowing()) {
								chatwindow.setVisible(true);
							}
                            chatwindow.setAlwaysOnTop(true);
                            chatwindow.appendText(message);
                            client.getMyMessages().remove(message);
                            //播放消息提示音
                        }
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadAutoAppendMessage.start();
    }

    /**
     * 初始化好友列表
     *
     * @return DefaultListModel <p>好友列表<p>
     * @author nmyphp
     */
    private DefaultListModel initFriendsList() {

        List<User> list = UserService.listFriends(client.getCurrentUser());
        DefaultListModel friendData = new DefaultListModel();
        for (User user : list) {
            friendData.addElement(user);
        }
        return friendData;
    }

    /**
     * 初始化群组列表
     *
     * @return DefaultListModel 群组列表
     */
    private DefaultListModel initGroupsList() {

        List<Group> list = GroupService.listGroups(client.getCurrentUser());
        groupData = new DefaultListModel();
        for (Group group : list) {
            groupData.addElement(group);
        }
        return groupData;
    }

    /**
     * 打开一个聊天窗口
     * <p>2014-12-22 修改参数receiver：User->Object</p>
     *
     * @param sender 消息发送者
     * @param receiver 消息接收者
     */
    private ChatWindow OpenChat(User sender, Object receiver) {
        ChatWindow chatwindow = null;

        int id = 0;
        String name = "";
        if (receiver instanceof User) {
            User user = (User) receiver;
            id = user.getUserId();
            name = user.getUserNickname();
        } else if (receiver instanceof Group) {
            Group group = (Group) receiver;
            id = group.getGroupId() + MainWindow.MAX_USERCHAT_NUMBER;//为了区别groupId和userId，在储存的时候给群组Id加一个整数
            name = group.getGroupName();
        }

        //如果已经为选择的好友打开了一个聊天窗口，那么让那个窗口显示到前端，否则创建一个聊天窗口，并加入到chatMap
        if (chatMap.get(id) == null) {

            chatwindow = new ChatWindow(sender, receiver);
            chatwindow.setVisible(true);
            chatwindow.setTitle(chatwindow.getTitle() + "-" + name);
            chatMap.put(id, chatwindow);
        } else {
			if (!chatMap.get(id).isShowing()) {
				chatMap.get(id).setVisible(true);
			}
            chatMap.get(id).setAlwaysOnTop(true);
        }
        return chatwindow;
    }

    /**
     * 从groupData里面查Group信息
     *
     * @param groupId 组id
     */
    private Group getGroupFromGroupData(int groupId) {
        Group group = null;
        Object[] objs = groupData.toArray();
        for (Object obj : objs) {
            group = (Group) obj;
			if (groupId == group.getGroupId()) {
				break;
			}
        }
        return group;
    }

}
