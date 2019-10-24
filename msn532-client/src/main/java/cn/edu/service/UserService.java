package cn.edu.service;

import cn.edu.domain.User;
import cn.edu.ui.MainWindow;
import cn.edu.util.HibernateSessionFactory;
import java.util.List;
import javax.swing.JFrame;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 业务逻辑层-用户模块
 *
 * @author nmyphp
 */
public class UserService {

    /**
     * 验证用户密码
     *
     * @param nickname 用户名
     * @param password 密码
     * @param msg 返回的验证信息
     * @return Boolean
     */
    public static Boolean CheckUserPasswd(String nickname, String password, String msg[]) {

        if (nickname == null || "".equals(nickname.trim()) || password == null || "".equals(password.trim())) {
            msg[0] = "用户名和密码不能为空！";
            return false;
        }

        User user = null;
        Session session = null;
        Transaction tran = null;

        try {
            session = HibernateSessionFactory.getSession();
            tran = session.beginTransaction();
            Query query = session.createQuery("from cn.edu.domain.User u where u.userNickname=?");
            query.setString(0, nickname);
            user = (User) query.uniqueResult();
            tran.commit();
        } catch (Exception e) {
            msg[0] = "查询数据库出错！";
            e.printStackTrace();
            if (tran != null) {
                tran.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if (user == null) {
            msg[0] = "该用户不存在！";
        } else {
            if (password != null && user.getUserPassword() != null && user.getUserPassword().equals(password)) {
                //如果验证通过，打开主窗口
                MainWindow mainwindow = new MainWindow(user);
                mainwindow.setVisible(true);
                mainwindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                return true;
            } else {
                msg[0] = "密码错误！";
            }
        }
        return false;
    }

    /**
     * 根据参数user，查出其所有的好友
     *
     * @return 好友列表
     * @author nmyphp
     */
    public static List<User> listFriends(User user) {
        List<User> list = null;
        Session session = null;
        Transaction tran = null;

        try {
            session = HibernateSessionFactory.getSession();
            tran = session.beginTransaction();
            String hql = "select u from cn.edu.domain.AddFriend af,cn.edu.domain.User u where af.requestReceiverId=u.userId and af.requestSenderId=?";
            Query query = session.createQuery(hql);
            query.setInteger(0, user.getUserId());
            list = query.list();
            tran.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tran != null) {
                tran.rollback();
            }
            return list;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }
}
