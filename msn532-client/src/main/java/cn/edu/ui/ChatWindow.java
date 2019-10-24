package cn.edu.ui;

import cn.edu.domain.Group;
import cn.edu.domain.Message;
import cn.edu.domain.User;
import cn.edu.service.Client;
import cn.edu.util.MessageType;
import cn.edu.util.MyImageIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 显示层-聊天窗口
 * <p>2014-12-21 update by nmyphp</p>
 * <p>2014-12-21 删除方法：setSocket()；添加代码段：C1</p>
 * <p>2014-12-22 修改变量receiver类型：User->Object</p>
 * <p>修改构造函数的参数receiver：User->Object</p>
 *
 * @author nmyphp
 * @see JFrame
 */
public class ChatWindow extends JFrame {

    private JPanel contentPane;
    private StyledDocument doc = null; // 非常重要插入文字样式就靠它了
    private JTextPane printor;            //聊天面板
    private JTextPane editor;            //编辑信息面板
    private JSplitPane splitPane;

    private static DatagramSocket socket = null;//发送消息的Socket
    private static int SERVERPORT = 5320;//服务器的端口号
    private Object receiver;//消息接收者
    private User sender;//发送者
    private static int windowID = 0;
    public int myWindowID;//聊天窗口的ID
    private SimpleAttributeSet attrSet_MessageContent = new SimpleAttributeSet();//聊天信息内容显示格式
    private SimpleAttributeSet attrSet_UserInfo = new SimpleAttributeSet();//用户信息、时间显示格式

    //C1 2014-12-21 添加
    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("创建DatagramSocket失败");
            e.printStackTrace();
        }
    }

    public ChatWindow(User sender, Object receiver) {

        myWindowID = windowID++;
        this.receiver = receiver;
        this.sender = sender;
        setAttrSet_UserInfo();

        ///////////////////////////////////////////////////////////////////////////////
        //界面初始化-聊天窗口
        ///////////////////////////////////////////////////////////////////////////////

        setTitle("LL聊天窗口");
        setEnabled(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 550, 520);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        //将界面分成左右两部分
        splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.6);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(8);
        splitPane.setBorder(new LineBorder(Color.BLACK, 0));
        contentPane.add(splitPane, BorderLayout.CENTER);

        //左边部分
        JSplitPane splitPane_chat = new JSplitPane();
        splitPane_chat.setResizeWeight(0.7);
        splitPane_chat.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_chat.setContinuousLayout(true);
        splitPane_chat.setDividerSize(8);
        splitPane_chat.setBorder(new LineBorder(Color.BLACK, 0));
        splitPane.setLeftComponent(splitPane_chat);

        //左边部分-上部
        JPanel panel_up = new JPanel();
        splitPane_chat.setLeftComponent(panel_up);
        panel_up.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_printor = new JScrollPane();
        scrollPane_printor.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel_up.add(scrollPane_printor, BorderLayout.CENTER);

        //聊天信息显示区域
        printor = new JTextPane();
        printor.setEditable(false);
        scrollPane_printor.setViewportView(printor);
        doc = printor.getStyledDocument();

        JPanel panel_down = new JPanel();
        splitPane_chat.setRightComponent(panel_down);
        panel_down.setLayout(new BorderLayout(0, 0));

        //工具栏，包括字体、颜色、图片
        JToolBar toolBarChat = new JToolBar();
        toolBarChat.setBorder(new LineBorder(Color.BLACK, 0));
        panel_down.add(toolBarChat, BorderLayout.NORTH);

        //字体按钮
        JButton btnFont = new JButton(MyImageIcon.FONT);
        btnFont.setOpaque(false);
        btnFont.setBorder(null);
        btnFont.setToolTipText("\u5B57\u4F53\u9009\u62E9\u5DE5\u5177\u680F");
        toolBarChat.add(btnFont);

        //表情按钮
        JButton btnFace = new JButton(MyImageIcon.FACE);
        btnFace.setToolTipText("\u9009\u62E9\u8868\u60C5");
        btnFace.setOpaque(false);
        btnFace.setBorder(null);
        toolBarChat.add(btnFace);

        //图片按钮
        JButton btnPicture = new JButton(MyImageIcon.PICTURE);
        btnPicture.setToolTipText("\u53D1\u9001\u56FE\u7247");
        btnPicture.setOpaque(false);
        btnPicture.setBorder(null);
        toolBarChat.add(btnPicture);

        //左下按钮面板
        JPanel panel_btn = new JPanel();
        panel_down.add(panel_btn, BorderLayout.SOUTH);
        panel_btn.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        //发送按钮
        JButton btnSend = new JButton("\u53D1\u9001(S)");
        btnSend.setMnemonic(KeyEvent.VK_S);
        panel_btn.add(btnSend);
        this.getRootPane().setDefaultButton(btnSend);//将发送按钮设置成主面板默认按钮，设置按钮快捷键

        //右侧面板
        JPanel panel_picture = new JPanel();
        panel_picture.setBackground(Color.WHITE);
        panel_picture.setBorder(new LineBorder(Color.BLACK, 0));
        panel_picture.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        splitPane.setRightComponent(panel_picture);

        //当窗口大小改变时，保持右侧面板宽度不变
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent arg0) {
                splitPane.setDividerLocation(splitPane.getWidth() - 167);
            }
        });

        //聊天窗口-信息编辑区
        editor = new JTextPane() {
            /**
             * 将剪切板的内容粘贴到编辑区
             * @author zzy
             */
            public void paste() {

                if (isEditable() && isEnabled()) {
                    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统的剪切板
                    Transferable contents = cb.getContents(null);

                    //对文字和图片进行不同的处理
                    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        super.paste();
                    } else if (contents != null && contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                        try {
                            Image img = (Image) contents.getTransferData(DataFlavor.imageFlavor);
                            Icon icon = new ImageIcon(img);
                            insertIcon(icon);
                        } catch (UnsupportedFlavorException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        JScrollPane scrollPane_editor = new JScrollPane();
        scrollPane_editor.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel_down.add(scrollPane_editor, BorderLayout.CENTER);
        scrollPane_editor.setViewportView(editor);

        //字体按钮添加事件监听器，当点击字体按钮时，弹出字体选择面板
        btnFont.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                JFontChooser one = new JFontChooser();
                one.showDialog(null, 500, 200);
                Font selectedFont = one.getSelectedfont();// 获取选择的字体
                Color selectedColor = one.getSelectedcolor();// 获取选择的颜色
                if (selectedFont != null) {
                    //初始化消息内容显示格式
                    StyleConstants.setFontFamily(attrSet_MessageContent, selectedFont.getFamily());
                    StyleConstants.setBold(attrSet_MessageContent, selectedFont.isBold());
                    StyleConstants.setItalic(attrSet_MessageContent, selectedFont.isItalic());
                    StyleConstants.setFontSize(attrSet_MessageContent, selectedFont.getSize());
                    editor.setFont(selectedFont);//设置编辑区字体
                }
                if (selectedColor != null) {
                    StyleConstants.setForeground(attrSet_MessageContent, selectedColor);
                    editor.setCaretColor(selectedColor);//设置编辑区字体颜色
                }

            }
        });

        //表情按钮添加事件监听器
        btnPicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser jf = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "图片文件(*.jpg,*.png,*.bmp)", "jpg", "png", "bmp");
                jf.setFileFilter(filter);
                if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    appendImage(jf.getSelectedFile().getPath());
                }
            }
        });

        //消息发送按钮添加事件监听器
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!editor.getText().equals("")) {
                    try {
						if (sendMessage(editor.getText())) {
							appendMyText(editor.getText());// 如果消息成功发送到服务器，则发送消息到聊天窗口
						}
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////
    //功能扩展-辅助函数
    //////////////////////////////////////////////////////////////////////////////

    /**
     * 向聊天窗口追加用户名和时间
     *
     * @param user 用户
     */
    private void appendUserInfo(User user) {
        try {

            doc.insertString(doc.getLength(), user.getUserNickname() + " " + new Date().toString() + "\n",
                attrSet_UserInfo);
        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            printor.setCaretPosition(doc.getLength());
        }
    }

    /**
     * 初始化Socket
     */
//	private void setSocket(){
//		try {
//			socket = new DatagramSocket();
//		} catch (SocketException e) {
//			System.out.println("创建DatagramSocket失败");
//			e.printStackTrace();
//		}
//	}

    /**
     * 向消息显示区域追加图片
     *
     * @param imagePath 图片的路径
     */
    private void appendImage(String imagePath) {
        printor.insertIcon(new ImageIcon(imagePath));
        try {
            doc.insertString(doc.getLength(), "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            printor.setCaretPosition(doc.getLength());
        }
    }

    /**
     * 向服务器发送消息
     *
     * @param msgContent 消息内容
     * @return 是否发送成功
     */
    private Boolean sendMessage(String msgContent) throws Exception {
        int receiverId = 0;
        MessageType messageType = MessageType.NORMAL;
        if (this.receiver instanceof User) {
            receiverId = ((User) this.receiver).getUserId();
        } else if (this.receiver instanceof Group) {
            receiverId = ((Group) this.receiver).getGroupId();
            messageType = MessageType.GOURPMSG;
        }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream objOP = null;
        try {
            Message message = new Message(msgContent, new Date(), this.sender, receiverId, messageType);
            objOP = new ObjectOutputStream(bout);
            objOP.writeObject(message);        //序列化对象
            objOP.flush();
            byte[] sendBuff = bout.toByteArray();       //转化为字节数组
            DatagramPacket datagram = new DatagramPacket(sendBuff, sendBuff.length,
                InetAddress.getByName(Client.getServerIp()), ChatWindow.SERVERPORT);
            socket.send(datagram);
        } catch (Exception e) {
            System.out.println("一条消息发送失败");
            e.printStackTrace();
            return false;
        } finally {
			if (objOP != null) {
				objOP.close();
			}
			if (bout != null) {
				bout.close();
			}
        }
        return true;
    }

    /**
     * 将编辑区的消息追加到县市区
     */
    private void appendMyText(String text) {
        try {
            StyleConstants.setForeground(attrSet_UserInfo, new Color(0, 128, 0));
            appendUserInfo(sender);// 追加用户名和时间
            doc.insertString(doc.getLength(), text + "\n", attrSet_MessageContent);
            editor.setText(null);
        } catch (BadLocationException e) {
            System.out.println("消息发送到显示框失败");
            e.printStackTrace();
        } finally {
            printor.setCaretPosition(doc.getLength());
        }
    }

    /**
     * 设置发送者信息、时间的显示格式
     */
    private void setAttrSet_UserInfo() {
        StyleConstants.setFontSize(attrSet_UserInfo, 12);
        StyleConstants.setFontFamily(attrSet_UserInfo, "宋体");
        StyleConstants.setForeground(attrSet_UserInfo, new Color(0, 128, 0));
        StyleConstants.setForeground(attrSet_UserInfo, new Color(0, 0, 255));
    }

    ////////////////////////////////////////////////////////////////////////////
    //外部接口
    ////////////////////////////////////////////////////////////////////////////

    /**
     * 外部接口-向聊天窗口追加消息
     * <p>2014-12-22 修改：参数类型String->Message</p>
     *
     * @param text 消息内容
     */
    public void appendText(Message message) {
        try {
            StyleConstants.setForeground(attrSet_UserInfo, new Color(0, 0, 255));
            appendUserInfo(message.getSender());//每次向消息显示区追加消息时，先追加发送者信息、时间
            doc.insertString(doc.getLength(), message.getMessageContent() + "\n", attrSet_MessageContent);
        } catch (BadLocationException e) {
            e.printStackTrace();
        } finally {
            printor.setCaretPosition(doc.getLength());
        }
    }
}
