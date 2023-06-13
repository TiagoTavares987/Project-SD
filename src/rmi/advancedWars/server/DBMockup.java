package rmi.advancedWars.server;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    private final ArrayList<Game> games;// = new ArrayList();
    private final ArrayList<User> users;// = new ArrayList();

    /**
     * This constructor creates and inits the database with some books and users.
     */
    public DBMockup() {
        games = new ArrayList();
        users = new ArrayList();
        //Add one user
        users.add(new User("guest", "ufp"));
    }

    /**
     * Registers a new user.
     * 
     * @param u username
     * @param p passwd
     */
    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
        }
    }

    /**
     * Checks the credentials of an user.
     * 
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public Game insert(String mapLvl, int nPlayers, SubjectRI subjectRI) {
        Game game = new Game(mapLvl, nPlayers, subjectRI);
        games.add(game);
        return game;
    }

    public Game selectGameById(int id) {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            System.out.println("DB - select(): game[" + i + "] = " + game.getnPlayers() + ", " + game.getMapLvl());
            if (game.getId() == id) {
                System.out.println("DB - select(): add game[" + i + "] = " + game.getnPlayers() + ", " + game.getMapLvl());
                return game;
            }
        }
        System.out.println("Game with Id =" + id + "not found");
        return null;
    }

    public Game[] listAllAvailableGames() {
        Game[] agames = null;
        ArrayList<Game> vgames = new ArrayList<>();
        // Find games that match
        for (int i = 0; i < games.size(); i++) {
            Game game = (Game) games.get(i);
            if (game.getnPlayers() < game.getTotalPlayers()) {
                System.out.println("DB - select(): game[" + i + "] = " + game.getnPlayers() + ", " + game.getMapLvl());
                vgames.add(game);
            }
        }
        // Copy Vector->Array
        agames = new Game[vgames.size()];
        for (int i = 0; i < vgames.size(); i++) {
            agames[i] = (Game) vgames.get(i);
        }
        return agames;
    }
}
