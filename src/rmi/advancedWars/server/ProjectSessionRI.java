package rmi.advancedWars.server;

import rmi.advancedWars.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ProjectSessionRI extends Remote {
    public Game createGame(String mapLvl, int nPlayers, ObserverRI observer) throws RemoteException;

    public Game joinGame(int id, ObserverRI observer) throws RemoteException;

    public void exitGame(int id, ObserverRI observer) throws RemoteException;

    public Game[] listAllAvailableGames() throws RemoteException;

    void logout() throws RemoteException;
}
