package cn.edu.ui;

import cn.edu.service.UserService;
import cn.edu.util.MyImageIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * 显示层-登录窗口
 *
 * @author nmyphp
 */
public class LoginWindow extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JPasswordField passwordField;//密码框
    private JTextField nicknameField;//账号输入框
    private JLabel label_msg;//提示信息框

    public LoginWindow() {

        setResizable(false);
        setTitle("LL\u767B\u5F55");
        setBounds(100, 100, 450, 234);
        setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));//设置界面字体
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel label = new JLabel("\u8D26\u53F7\uFF1A");
        label.setBounds(138, 37, 54, 15);
        contentPanel.add(label);

        JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");
        label_1.setBounds(138, 74, 54, 15);
        contentPanel.add(label_1);

        //用户名输入框
        nicknameField = new JTextField();
        nicknameField.setEditable(true);
        nicknameField.setBounds(202, 31, 191, 27);
        contentPanel.add(nicknameField);

        //密码输入框
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setBounds(202, 68, 191, 27);
        contentPanel.add(passwordField);

        JPanel buttonPane = new JPanel();
        buttonPane.setBounds(0, 159, 444, 47);
        contentPanel.add(buttonPane);
        buttonPane.setLayout(null);

        //登录按钮
        JButton loginButton = new JButton("\u767B\u5F55");
        loginButton.setBounds(197, 10, 90, 27);
        loginButton.setActionCommand("OK");
        buttonPane.add(loginButton);
        getRootPane().setDefaultButton(loginButton);

        //取消按钮
        JButton registerButton = new JButton("\u6CE8\u518C");
        registerButton.setBounds(313, 10, 90, 27);
        registerButton.setActionCommand("Cancel");
        buttonPane.add(registerButton);

        //提示信息框
        label_msg = new JLabel("");
        label_msg.setForeground(Color.RED);
        label_msg.setBounds(202, 116, 191, 15);
        contentPanel.add(label_msg);

        //logo
        JLabel lblNewLabel = new JLabel(MyImageIcon.LOGO);
        lblNewLabel.setBounds(10, 31, 118, 118);
        contentPanel.add(lblNewLabel);

        /**
         * 点击登录按钮时，进行登录验证
         */
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String nickname = nicknameField.getText();//获取用户名
                String password = String.valueOf(passwordField.getPassword());//获取密码
                String msg[] = new String[1];//储存返回的验证信息

                if (UserService.CheckUserPasswd(nickname, password, msg)) {
                    dispose();//如果验证通过，关闭登录窗口
                } else {
                    label_msg.setText(msg[0]);//如果验证没通过，将验证信息显示到界面上
                }
            }
        });


    }
}
