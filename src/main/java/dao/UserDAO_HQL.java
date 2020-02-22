package dao;

import models.User;

public interface UserDAO_HQL extends UserDAO {
    User findByLogin_HQL(String login);
}
