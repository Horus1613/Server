package dao;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactory;

public class UserDAO {
    public void save(User user) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public User findByLogin(String login) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class,login);
    }
}
