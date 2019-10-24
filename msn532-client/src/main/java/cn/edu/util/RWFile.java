package cn.edu.util;

import cn.edu.service.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * 读写文件 2014-12-21
 *
 * @author nmyphp
 */
public class RWFile {


    private static void writeInteger(int port) {
        File file = new File("PORT.txt");
        try {
            DataOutputStream dis = new DataOutputStream(new FileOutputStream(file));
            dis.writeInt(port);
            dis.close();
        } catch (Exception e) {
            System.out.println("写文件失败");
            e.printStackTrace();
        }
    }


    public static int readPort() {
        int port;
        File file = new File("PORT.txt");

        try {
            if (!file.exists()) {
                System.out.println();
                file.createNewFile();
                writeInteger(Client.PORT);
            } else {
                Date lastDate = new Date(file.lastModified());
                if (lastDate.getDate() < (new Date()).getDate()) {
                    writeInteger(Client.PORT);
                }
            }

            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            port = dis.readInt();
            dis.close();
            writeInteger(++port);
            return port;
        } catch (Exception e) {
            System.out.println("读文件失败");
            e.printStackTrace();
            return -1;
        }
    }


}
