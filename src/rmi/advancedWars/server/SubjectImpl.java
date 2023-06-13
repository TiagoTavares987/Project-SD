package rmi.advancedWars.server;

import rmi.advancedWars.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private State subjectState;
    private ArrayList<ObserverRI> observers = new ArrayList<>();

    public SubjectImpl(State subjectState) throws RemoteException {
        super();
        this.subjectState = subjectState;
    }

    public SubjectImpl() throws RemoteException {
        super();
    }

    @Override
    public void attach(ObserverRI obsRi) throws RemoteException {
        observers.add(obsRi);
    }

    @Override
    public void detach(ObserverRI obsRi) throws RemoteException {
        observers.remove(obsRi);
    }

    @Override
    public State getState() throws RemoteException {
        return subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException {
        this.subjectState = state;
        notifyAllObservers();
    }
    @Override
    public void notifyAllObservers() throws RemoteException {
        for (ObserverRI observer : observers)
            observer.update();
    }

    @Override
    public int findObserverArrayPosition(ObserverRI observer) throws RemoteException{
        for (int i = 0; i< observers.size(); i++){
            if(observer.equals(observers.get(i))){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<ObserverRI> getObservers() {
        return observers;
    }
}
