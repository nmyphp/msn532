package cn.edu.util;

import cn.edu.domain.User;
import cn.edu.util.HibernateSessionFactory;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Session session = HibernateSessionFactory.getSession();
        Transaction t = session.beginTransaction();
		
		/*Query q=session.createQuery("from User where userId = 1");
		User user=(User) q.uniqueResult();
		Message message=new Message("cccccc", new Date(), user);
		session.save(user);
		session.save(message);*/
		
 		/*User user1=new User("chenlong","54321");
 		user1.setUserSex(SexType.MALE);
 		user1.setIsOnline(false);
 		User user2=new User("chenlong","54321");
 		User user3=new User("chenlong","54321");
 		User user4=new User("chenlong","54321");
		
 		session.save(user1);
 		session.save(user2);
 		session.save(user3);
 		session.save(user1);*/
		
		/*String nickname="chenlong";
		Query query=session.createQuery("from cn.edu.domain.User u where u.userNickname=?");
		query.setString(0, nickname);
		User user=(User) query.uniqueResult();
		System.out.println(user.toString());*/
		
		/*AddFriend af=new AddFriend(1,2);
		session.save(af);
		Query query=session.createQuery("from cn.edu.domain.AddFriend af where af.requestSenderId=?");*/

        Query query = session.createQuery(
            "select u from cn.edu.domain.AddFriend af,cn.edu.domain.User u where af.requestReceiverId=u.userId and af.requestSenderId=?");

        query.setInteger(0, 1);
        List<User> list = query.list();
        for (User u : list) {
            System.out.println(u.toString());
        }

        t.commit();
        session.close();
    }
}
