package rmi.advancedWars.server;
import rmi.advancedWars.server.ProjectFactoryRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.HashMap; // import the HashMap class


public class ProjectFactoryImpl extends UnicastRemoteObject implements ProjectFactoryRI {
    private DBMockup dbMockup;
    private HashMap<String, ProjectSessionRI> users;


    public DBMockup getDbMockup() {
        return dbMockup;
    }

    public ProjectFactoryImpl() throws RemoteException {
        super();
        dbMockup=new DBMockup();
        users=new HashMap();
    }

    public static boolean saoStringsValidas(String... args) {
        for (String arg : args) {
            if (!(arg instanceof String)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        if (saoStringsValidas(username,password)){
            if (dbMockup.exists(username, password)){
                return false;
            }else {
                dbMockup.register(username, password);
                return true;
            }
        }
        return false;
    }

    @Override
    public ProjectSessionRI login(String username, String password) throws RemoteException {
        if (saoStringsValidas(username,password)){
            if (!dbMockup.exists(username, password)){
                return null;
            }else {
                if (users.containsKey(username)){
                    return users.get(username);
                }
                ProjectSessionRI session = new ProjectSessionImpl(username,this);
                users.put(username,session);
                return session;
            }
        }
        return null;
    }

    public void deletesession(String username){
        users.remove(username);
    }
}
