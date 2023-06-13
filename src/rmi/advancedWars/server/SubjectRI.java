package rmi.advancedWars.server;

import rmi.advancedWars.client.ObserverImpl;
import rmi.advancedWars.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;


public interface SubjectRI extends Remote {
    public void attach(ObserverRI obsRi) throws RemoteException;
    public void detach(ObserverRI obsRi) throws RemoteException;
    public State getState() throws RemoteException;
    public void setState(State state) throws RemoteException;
    public void notifyAllObservers() throws RemoteException;
    public int findObserverArrayPosition(ObserverRI observer) throws RemoteException;
    public ArrayList<ObserverRI> getObservers() throws RemoteException;
}
