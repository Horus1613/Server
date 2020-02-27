package dao;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.dbUtils.HibernateSessionFactory;
import org.hibernate.query.Query;

public class HibernateUserDAO implements UserDAO, UserDAO_HQL {


    public HibernateUserDAO() {

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
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, login);
    }

    @Override
    public User findByLogin_HQL(String login) {
        String command = "from User where login= :username";
        Query<User> query = HibernateSessionFactory.getSessionFactory().openSession().createQuery(command);
        query.setParameter("username", login);
        return query.getSingleResult();
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
