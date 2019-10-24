package cn.edu.test;

import cn.edu.main.Server;

public class Test {

    public static void main(String[] args) {
        try {
            Server frame = new Server();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
