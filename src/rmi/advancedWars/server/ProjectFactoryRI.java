package rmi.advancedWars.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ProjectFactoryRI extends Remote {
    public boolean register(String username,String password) throws RemoteException;
    public ProjectSessionRI login(String username, String password) throws RemoteException;
}
