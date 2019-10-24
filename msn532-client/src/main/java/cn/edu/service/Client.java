package cn.edu.service;

import cn.edu.domain.Message;
import cn.edu.domain.User;
import cn.edu.util.Config;
import cn.edu.util.LoginOrLogoutMessage;
import cn.edu.util.MessageType;
import cn.edu.util.RWFile;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <h1>业务逻辑模块-客户端</h1>
 *
 * @author nmyphp 2014-12-20 B1.0
 */
public class Client {

    private String myIP;
    public static int PORT = 5322;
    private int myPort;
    private static String SERVERIP = Config.getValue("server.ip");//服务器的IP
    private static int SERVERPORT = 5321;//服务器的端口号
    private User currentUser;
    private Queue<Message> myMessages = new LinkedList<Message>();//消息队列


    public Client(User user) {

        currentUser = user;//为客户端设置用户
        myPort = RWFile.readPort();//设置本客户端的端口号
        setLocalIP();//设置本机IP
        startReceiver();
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Queue<Message> getMyMessages() {
        return myMessages;
    }

    public static void setServerIp(String serverIp) {
        Client.SERVERIP = serverIp;
    }

    public static String getServerIp() {
        return Client.SERVERIP;
    }

    private void setLocalIP() {
        try {
            myIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.out.println("客户端获取ip失败");
            e.printStackTrace();
        }
    }

    /**
     * 客户端消息接收器
     */
    private void startReceiver() {
        try {
            registerOrLogout(MessageType.LOGIN);//注册客户端

            //启动消息接收器
            Thread threadServer = new Thread(new Runnable() {

                final int MAX_LEN = 2000;
                DatagramSocket socket = new DatagramSocket(myPort);
                byte[] receiveBuff = new byte[MAX_LEN];

                public void run() {
                    Message message = null;
                    while (true) {
                        try {
                            DatagramPacket packet = new DatagramPacket(receiveBuff, MAX_LEN);
                            socket.receive(packet);
                            ByteArrayInputStream bint = new ByteArrayInputStream(receiveBuff);
                            ObjectInputStream oint = new ObjectInputStream(bint);
                            message = (Message) oint.readObject();//反序列化
                            if (message != null) {
                                myMessages.offer(message);//将接收的信息存到消息队列
                                System.out.println(message.getMessageContent());
                                message = null;
                            }
							if (oint != null) {
								oint.close();
							}
							if (bint != null) {
								bint.close();
							}
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }
            });
            threadServer.start();
        } catch (Exception e) {
            System.out.println("消息接收器启动失败");
            e.printStackTrace();
        }
    }

    /**
     * 注册、注销客户端
     *
     * @author nmyphp
     */
    public void registerOrLogout(MessageType messageType) throws Exception {
        Socket socket = null;
        ObjectOutputStream os = null;
        try {
            socket = new Socket(Client.SERVERIP, Client.SERVERPORT);
            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(new LoginOrLogoutMessage(currentUser.getUserId(), myIP, myPort, messageType));
            os.flush();
        } catch (Exception e) {
            System.out.println("客户端注册失败");
            e.printStackTrace();
        } finally {
			if (os != null) {
				os.close();
			}
			if (socket != null) {
				socket.close();
			}
        }
    }


}
