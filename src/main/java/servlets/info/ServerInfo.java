package servlets.info;

//TODO Избавиться от этой залупы
public class ServerInfo implements ServerInfoMBean {
    private static ServerInfo serverInfo;

    private int currentUsers;
    private int limitUsers;

    private ServerInfo(){
        limitUsers=10;
        currentUsers=0;
    }

    public static ServerInfo getInstance(){
        if(serverInfo==null){
            serverInfo = new ServerInfo();
        }
        return serverInfo;
    }

    @Override
    public int getUsers() {
        return currentUsers;
    }

    @Override
    public int getUsersLimit() {
        return limitUsers;
    }

    @Override
    public void setUsersLimit(int usersLimit) {
        this.limitUsers = usersLimit;
    }

    public void addUser(){
        currentUsers++;
    }

    public void removeUser(){
        currentUsers--;
    }
}
