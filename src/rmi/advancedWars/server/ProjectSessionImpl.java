package rmi.advancedWars.server;

import rmi.advancedWars.client.ObserverRI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;


public class ProjectSessionImpl extends UnicastRemoteObject implements ProjectSessionRI {
    private ProjectFactoryImpl factoryImpl;
    private String user;
    public ProjectSessionImpl(String user, ProjectFactoryImpl factoryImpl) throws RemoteException {
        super();
        this.user = user;
        this.factoryImpl=factoryImpl;
    }

    @Override
    public Game createGame(String mapLvl, int nPlayers, ObserverRI observer) throws RemoteException {
        SubjectRI subjectRI = new SubjectImpl();
        Game game = factoryImpl.getDbMockup().insert(mapLvl, nPlayers, subjectRI);
        game.getSubjectRI().attach(observer);
        game.setnPlayers((game.getnPlayers() + 1));
        return game;
    }

    @Override
    public Game joinGame(int id, ObserverRI observer) throws RemoteException {
        Game game = factoryImpl.getDbMockup().selectGameById(id);
        game.getSubjectRI().attach(observer);
        game.setnPlayers((game.getnPlayers() + 1));
        return game;
    }

    @Override
    public void exitGame(int id, ObserverRI observer) throws RemoteException {
        Game game = factoryImpl.getDbMockup().selectGameById(id);
        game.getSubjectRI().detach(observer);
        game.setnPlayers(game.getnPlayers() - 1);
    }

    @Override
    public Game[] listAllAvailableGames() throws RemoteException {
        return factoryImpl.getDbMockup().listAllAvailableGames();
    }

    @Override
    public void logout() throws RemoteException {
        factoryImpl.deletesession(user);
    }
}
