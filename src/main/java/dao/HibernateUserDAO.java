package dao;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dbUtils.HibernateSessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateUserDAO implements UserDAO {

    private boolean HQL_mode;

    public HibernateUserDAO(boolean HQL_mode) {
        this.HQL_mode = HQL_mode;
    }

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
        if (!HQL_mode) {
            return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, login);
        } else {
            String command = "from User where login= :username";
            Query<User> query = HibernateSessionFactory.getSessionFactory().openSession().createQuery(command);
            query.setParameter("username", login);
            return query.getSingleResult();
        }
    }

    @Override
    public void banControlByLogin(String login, boolean value) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, login);
            user.setBanned(value);
            session.update(user);
            transaction.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
