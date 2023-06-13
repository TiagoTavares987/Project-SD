package rmi.advancedWars.client;

import rmi.advancedWars.server.SubjectRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRI extends Remote {
   public int getId() throws RemoteException;
   public void update() throws RemoteException;
   public SubjectRI getSubjectRI() throws RemoteException;
}
