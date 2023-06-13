package rmi.advancedWars.client;

import rmi.advancedWars.client.advancedWarsGame.engine.Game;

import rmi.advancedWars.server.ProjectFactoryRI;
import rmi.advancedWars.server.ProjectSessionRI;
import rmi.advancedWars.server.State;
import rmi.advancedWars.server.SubjectRI;
import rmi.util.rmisetup.SetupContextRMI;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui S. Moreira
 * @version 3.0
 */
public class advancedWarsClient {

    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private ProjectFactoryRI projectFactoryRI;
    private String Username, Password;

    private Game mainGame;

    public static void main(String[] args) {


        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.inf.rmi._01_helloworld.server.HelloWorldClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            advancedWarsClient hwc=new advancedWarsClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService();
        }
    }

    public advancedWarsClient(String args[]) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(advancedWarsClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR lookup service @ {0}", serviceUrl);
                
                //============ Get proxy MAIL_TO_ADDR HelloWorld service ============
                projectFactoryRI = (ProjectFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return projectFactoryRI;
    }
    
    private void playService() {
        try {
            //============ Call HelloWorld remote service ============
            ProjectSessionRI session = null;
            while (session == null)
                session = menuLogin();

            menuGame(session);

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR finish, bye. ;)");
        } catch (RemoteException | InterruptedException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ProjectSessionRI menuLogin() throws RemoteException {
        System.out.print("1 - Register\n2 - Login\nChoice: ");
        Scanner op = new Scanner(System.in);
        Scanner username = new Scanner(System.in);
        Scanner password = new Scanner(System.in);

        switch (op.nextInt()) {
            case 1:
                System.out.print("\nUsername: ");
                Username = username.next();
                System.out.print("\nPassword: ");
                Password = password.next();
                projectFactoryRI.register(Username, Password);
                return menuLogin();
            case 2:
                System.out.print("\nUsername: ");
                Username = username.next();
                System.out.print("\nPassword: ");
                Password = password.next();
                return projectFactoryRI.login(Username, Password);
            default:
                System.out.println("Choose a number between 1 and 2");
                return menuLogin();
        }
    }

    // inicializaçao do obsserver, so se pode fazer 1
    private void initializeGame(rmi.advancedWars.server.Game game, ObserverImpl observer) throws RemoteException {
         observer.setSubjectRI(game.getSubjectRI());
         observer.setId(observer.getSubjectRI().findObserverArrayPosition(observer));
         observer.getSubjectRI().setState(new State(String.valueOf(observer.getId()), "p" + String.valueOf(game.getnPlayers())));
    }
    private void buildGame(rmi.advancedWars.server.Game game, ObserverImpl observer, ProjectSessionRI session) throws RemoteException, InterruptedException {

        int p = game.getnPlayers();
        System.out.print("players ligados: " + p + "\n");
        while(game.getnPlayers() < game.getTotalPlayers()) {

            State state = observer.getSubjectRI().getState();
            String info = state.getInfo();
            // mensagem fora do jogo ex p1 p2 p3 p4 começa sempre por p, msg dentro do jogo sao sempre inteiros. p4 estava a executar a tecla 4
            if (info.charAt(0) == 'p')
                game.setnPlayers(Integer.parseInt(info.substring(1)));

            if(p != game.getnPlayers()){
                p = game.getnPlayers();
                System.out.print("players ligados: " + p + "\n");
            }
        }

        System.out.println("vai iniciar o: " + game.getMapLvl() + "\n");
        mainGame = new Game(game.getMapLvl(), observer);
        mainGame.dispose();

        if (mainGame.exit == 2) {
            System.out.println("msg 2");
            session.exitGame(game.getId(), observer);
            menuGame(session);
        }
        else {
            System.out.println("msg 3");
            game.setnPlayers(game.getnPlayers() - 1);
            buildGame(game, observer, session);
        }
    }

    private void menuGame(ProjectSessionRI session) throws RemoteException, InterruptedException {
        System.out.print("1 - Create Game\n2 - Join Game\n3 - Logout\nChoice: ");
        Scanner op = new Scanner(System.in);
        switch (op.nextInt()) {
            case 1:
                int nPl = 0;
                String mapLv = null;
                System.out.print("1 - SmallVs\n2 - FourCorners\nChoice: ");
                Scanner opMap = new Scanner(System.in);
                switch (opMap.nextInt()) {
                    case 1:
                        nPl = 2;
                        mapLv = "SmallVs";
                        break;
                    case 2:
                        nPl = 4;
                        mapLv = "FourCorners";
                        break;
                    case 0:
                        break;
                }
                if(mapLv != null) {
                    ObserverImpl observer = new ObserverImpl();
                    rmi.advancedWars.server.Game game = session.createGame(mapLv, nPl, observer);
                    initializeGame(game, observer);
                    buildGame(game, observer, session);
                }
                break;
            case 2:
                rmi.advancedWars.server.Game[] games = session.listAllAvailableGames();
                if(games.length > 0) {
                    System.out.print("\nChoose game: \nNº - id, Map");
                    for (int i = 0; i < games.length; i++)
                        System.out.print("\n" + (i + 1) + " - " + games[i].getId() + ", " + games[i].getMapLvl());

                    System.out.print("\nChoice: ");
                    int opMapAv = new Scanner(System.in).nextInt();
                    if(opMapAv > 0 && opMapAv <= games.length) {
                        ObserverImpl observer = new ObserverImpl();
                        rmi.advancedWars.server.Game game = session.joinGame(games[opMapAv - 1].getId(), observer);
                        initializeGame(game, observer);
                        buildGame(game, observer, session);
                    }
                    else
                        menuGame(session);
                }
                else {
                    System.out.print("\nNo available games... ");
                    menuGame(session);
                }
                break;
            case 3:
                System.out.println("Logging out...");
                session.logout();
                System.out.println("Logged out.");
                break;
        }
    }
}
