package rmi.advancedWars.client;

import rmi.advancedWars.client.advancedWarsGame.engine.InputHandler;
import rmi.advancedWars.server.State;
import rmi.advancedWars.server.SubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private int id;
    private State lastObserverState;
    protected SubjectRI subjectRI;

    public ObserverImpl() throws RemoteException {
    }
    public ObserverImpl(int id, SubjectRI subjectRI) throws RemoteException {
        super();
        this.id = id;
        this.subjectRI = subjectRI;
        subjectRI.attach(this);
    }

    @Override
    public void update() throws RemoteException {
        lastObserverState = subjectRI.getState();
        // atualizar inteface grafica
        rmi.advancedWars.client.advancedWarsGame.engine.Game.update(lastObserverState.getInfo());
    }

    public State getLastObserverState() {
        return lastObserverState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubjectRI getSubjectRI() {
        return subjectRI;
    }

    public void setSubjectRI(SubjectRI subjectRI) {
        this.subjectRI = subjectRI;
    }
}
