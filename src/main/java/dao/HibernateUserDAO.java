package dao;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dbUtils.HibernateSessionFactory;
import org.hibernate.query.Query;

public class HibernateUserDAO implements UserDAO {
    @Override
    public void save(User user) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public User findByLogin(String login) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, login);
    }

    @Override
    public void banControlByLogin(String login, boolean value) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class,login);
            user.setBanned(value);
            session.update(user);
            transaction.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
