package cn.edu.main;

import cn.edu.ui.LoginWindow;
import javax.swing.JDialog;

/**
 * 主程序入口
 *
 * @author nmyphp
 */
public class Main {

    public static void main(String[] args) {
        try {
            LoginWindow dialog = new LoginWindow();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
