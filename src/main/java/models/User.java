package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String login;
    @Column
    private String password;
    @Column
    private boolean banned;


    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.banned = false;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBanned(Boolean banned){
        this.banned=banned;
    }

    public boolean getBanned(){
        return banned;
    }
}
