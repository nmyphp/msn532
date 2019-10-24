package cn.edu.main;

import cn.edu.domain.Message;
import cn.edu.domain.User;
import cn.edu.service.GroupService;
import cn.edu.util.GroupMessage;
import cn.edu.util.LoginOrLogoutMessage;
import cn.edu.util.MessageType;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * <h1>服务器端</h1>
 * <p>接收客户端消息，转发消息，登记在线用户，将下线的用户的消息储存到数据库</p>
 *
 * @author nmyphp
 * @see JFrame
 */

public class Server extends JFrame {

    public static void main(String[] args) {
        try {
            Server frame = new Server();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel contentPane;
    private JTextArea textArea;
    private static int SOCKET_PORT = 5321;//套接字服务器端端口号
    private static int DATAGRAMSOCKET_PORT = 5320;//数据报服务器端端口号
    private HashMap<Integer, LoginOrLogoutMessage> clients = new HashMap<Integer, LoginOrLogoutMessage>();
    private DatagramSocket datagramSocket = null;


    public Server() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 569, 411);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));

        JLabel lbl_logo = new JLabel();
        lbl_logo.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        lbl_logo.setIcon(new ImageIcon(Server.class.getResource("/image/logo.png")));
        lbl_logo.setText("LL服务器");
        panel.add(lbl_logo, BorderLayout.WEST);

        JLabel lbl_status = new JLabel();
        lbl_status.setIcon(new ImageIcon(Server.class.getResource("/image/started.gif")));
        panel.add(lbl_status, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);

        try {
            datagramSocket = new DatagramSocket(Server.DATAGRAMSOCKET_PORT);
        } catch (Exception e) {
            System.out.println("初始化DatagramSocket失败");
            e.printStackTrace();
        }

        try {
            loginOrLogoutMessageListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageListener();


    }

    /**
     * 登录、注销消息监听器
     */
    private void loginOrLogoutMessageListener() throws Exception {

        Thread listener = new Thread(new Runnable() {

            ServerSocket serverSocket = new ServerSocket(Server.SOCKET_PORT);

            public void run() {
                try {
                    while (true) {
                        Socket socket = serverSocket.accept();
                        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                        LoginOrLogoutMessage obj = null;
                        try {
                            obj = (LoginOrLogoutMessage) is.readObject();
                        } catch (Exception e) {
                            System.out.println("接收对象失败");
                            e.printStackTrace();
                        }
                        if (obj != null) {
                            clients.put(obj.getUserId(), obj);
                            textArea.append("用户登录：" + obj.toString() + "\n");
                        }
                        if (socket != null) {
                            socket.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listener.start();
    }

    /**
     * 聊天消息监听器
     *
     * @author nmyphp
     */
    private void messageListener() {

        Thread threadServer = new Thread(new Runnable() {

            final int MAX_LEN = 2000;
            byte[] receiveBuff = new byte[MAX_LEN];
            DatagramPacket packet = new DatagramPacket(receiveBuff, MAX_LEN);

            public void run() {
                Message message = null;

                while (true) {
                    try {
                        datagramSocket.receive(packet);
                        ByteArrayInputStream bint = new ByteArrayInputStream(receiveBuff);
                        ObjectInputStream oint = new ObjectInputStream(bint);
                        message = (Message) oint.readObject();//反序列化
                        if (message != null) {
                            if (message.getMessageType() == MessageType.GOURPMSG) {
                                List<User> list = GroupService.listUser(message.getReceiverId());
                                GroupMessage groupMessage = new GroupMessage(message.getMessageContent(),
                                    message.getMessageSendTime(), message.getSender(), message.getMessageType(),
                                    message.getReceiverId());
                                textArea.append("群消息：" + message.getMessageContent() + "\n");
                                System.out.println("2:" + message.getSender().getUserId());
                                int id = message.getSender().getUserId();//记录下发送群消息的用户的ID
                                for (User user : list) {

                                    if (user.getUserId() != id) {//群消息不转发给自己
                                        System.out.println("1:" + user.getUserId());
                                        LoginOrLogoutMessage clientM = clients.get(user.getUserId());//查出客户端的ip、端口号
                                        if (clientM != null) {
                                            sendMessage(groupMessage, clientM.getClentIP(),
                                                clientM.getClentPort());//将信息转发出去
                                        } else {
                                            //...将这条信息存到数据库
                                        }
                                    }
                                }
                            } else if (message.getMessageType() == MessageType.NORMAL) {
                                LoginOrLogoutMessage clientM = clients.get(message.getReceiverId());//查出客户端的ip、端口号
                                sendMessage(message, clientM.getClentIP(), clientM.getClentPort());//将信息转发出去
                                textArea.append("普通消息：" + message.getMessageContent() + "\n");
                            }

                            message = null;
                        }
                        if (oint != null) {
                            oint.close();
                        }
                        if (bint != null) {
                            bint.close();
                        }
                    } catch (Exception e) {
                        System.out.println("转发消息失败");
                        e.printStackTrace();
                    }
                }
            }
        });
        threadServer.start();
    }

    /**
     * 向客户端发送消息
     *
     * @param message 消息内容
     * @param receiverIp 接收者IP
     * @param port 接收者端口号
     * @return 是否发送成功
     */
    private void sendMessage(Message message, String receiverIp, int port) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream objOP = null;
        try {
            objOP = new ObjectOutputStream(bout);
            objOP.writeObject(message);        //序列化对象
            objOP.flush();
            byte[] sendBuff = bout.toByteArray();       //转化为字节数组

            DatagramPacket datagram = new DatagramPacket(sendBuff, sendBuff.length, InetAddress.getByName(receiverIp),
                port);
            datagramSocket.send(datagram);

        } catch (Exception e) {
            System.out.println("一条消息发送失败");
            e.printStackTrace();
        } finally {
            if (objOP != null) {
                objOP.close();
            }
            if (bout != null) {
                bout.close();
            }
        }
    }

}
