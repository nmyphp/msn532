package cn.edu.util;

import cn.edu.domain.Message;
import cn.edu.domain.User;
import cn.edu.util.MessageType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class Test {
	
	
	/*public static void aaa(String s[]){
		s[0]="321312";
		System.out.println(s[0]);
	}
	
	public static void setUser(User user){
		user=new User("chenlong","54321");
	}*/

    public static void main(String args[]) throws Exception {
        JFrame frame1 = new JFrame("第一个窗体");
        frame1.setVisible(true);
        frame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //frame1.hide();
        JFrame frame2 = new JFrame("第二个窗体");
        frame2.setVisible(true);

        frame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Map<Integer, JFrame> map = new HashMap<Integer, JFrame>();
        map.put(1, frame1);
        map.put(2, frame2);
        while (true) {
            if (!map.get(1).isShowing()) {
                map.remove(1);
            }
            if (!map.get(2).isShowing()) {
                map.remove(2);

            }
            System.out.println(map.get(1) == null);
            System.out.println(map.get(2) == null);
        }

    }

    private static void ob() throws IOException {
        String str = "进入正题，下面是一个用DatagramSocket实现在线发送对象的例子，先前做课程设计时找了一下，发现大部分都是用Socket实现的，因此给出自己的程序供参考。";
        Message message = new Message(1, str, new Date(), new User("chenlong", "54321"), 2, MessageType.NORMAL);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);

        oout.writeObject(message);        //序列化对象
        oout.flush();
        byte[] sendBuff = bout.toByteArray();

        System.out.println(sendBuff.length);
    }
}
