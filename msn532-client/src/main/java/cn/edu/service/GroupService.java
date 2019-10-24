package cn.edu.service;

import cn.edu.domain.Group;
import cn.edu.domain.User;
import cn.edu.util.HibernateSessionFactory;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 业务逻辑层-群组模块
 *
 * @author nmyphp
 */
public class GroupService {


    /**
     * 查询某个用户的群组信息
     *
     * @param user 用户
     * @return 群组列表
     */
    public static List<Group> listGroups(User user) {
        List<Group> list = null;
        Session session = null;
        Transaction tran = null;

        try {
            session = HibernateSessionFactory.getSession();
            tran = session.beginTransaction();
            String hql = "select g from cn.edu.domain.AddGroup a,cn.edu.domain.Group g where a.groupId=g.groupId and a.userId=?";
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


    public static List<User> listUser(int groupId) {
        List<User> list = null;
        Session session = null;
        Transaction tran = null;

        try {
            session = HibernateSessionFactory.getSession();
            tran = session.beginTransaction();
            String hql = "select u from cn.edu.domain.AddGroup a,cn.edu.domain.Group g,cn.edu.domain.User u where a.groupId=g.groupId and u.userId=a.userId and g.groupId=?";
            Query query = session.createQuery(hql);
            query.setInteger(0, groupId);
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
