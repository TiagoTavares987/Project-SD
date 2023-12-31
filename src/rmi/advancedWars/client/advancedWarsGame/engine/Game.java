package rmi.advancedWars.client.advancedWarsGame.engine;

import rmi.advancedWars.client.ObserverRI;
import rmi.advancedWars.client.advancedWarsGame.menus.MenuHandler;
import rmi.advancedWars.client.advancedWarsGame.menus.Options;
import rmi.advancedWars.client.advancedWarsGame.menus.PlayerSelection;
import rmi.advancedWars.client.advancedWarsGame.players.Base;
import rmi.advancedWars.server.State;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;
	
	//Application Settings
	private static final String build = "0";
	private static final String version = "2";
	public static final String name = "Strategy Game";
	public static int ScreenBase = 32;//Bit size for the screen, 16 / 32 / 64 / 128
	public static boolean dev = true;//Is this a dev copy or not... useless? D:
    public static enum State {STARTUP, MENU, PLAYING, EDITOR};
	public static State GameState = State.STARTUP;
	public static Game game;
		
	//Setup the quick access to all of the other class files.
	public static Map map;
	public static Gui gui;
	public static LoadImages load;
	public static InputHandler input;
	public static Editor edit = new Editor();
	public static Battle btl = new Battle();
	public static ErrorHandler error = new ErrorHandler();
	public static Pathfinding pathing = new Pathfinding();
	public static ListData list;
	public static Save save = new Save();
	public static ComputerBrain brain = new ComputerBrain();
	public static FileFinder finder = new FileFinder();
	public static ViewPoint view = new ViewPoint();
	
	//Image handling settings are as follows
	public int fps;
	public int fpscount;
	public static Image[] img_menu = new Image[5];
	public static Image img_tile;
	public static Image img_char;
	public static Image img_plys;
	public static Image img_city;
	public static Image img_exts;
	public static Boolean readytopaint;
	
	//This handles the different rabbitmq.advancedWars.client.game.players and also is used to speed logic arrays (contains a list of all characters they own)
	public static List<Base> player = new ArrayList<Base>();
	public static List<rmi.advancedWars.client.advancedWarsGame.buildings.Base> builds = new ArrayList<rmi.advancedWars.client.advancedWarsGame.buildings.Base>();
	public static List<rmi.advancedWars.client.advancedWarsGame.units.Base> units = new ArrayList<rmi.advancedWars.client.advancedWarsGame.units.Base>();
	//These are the lists that will hold commander, building, and unit data to use in the menu's
	public static List<Base> displayC = new ArrayList<Base>();
	public static List<rmi.advancedWars.client.advancedWarsGame.buildings.Base> displayB = new ArrayList<rmi.advancedWars.client.advancedWarsGame.buildings.Base>();
	public static List<rmi.advancedWars.client.advancedWarsGame.units.Base> displayU = new ArrayList<rmi.advancedWars.client.advancedWarsGame.units.Base>();

	public static ObserverRI observer;
	public static int exit;
	
	public Game(String maplv, ObserverRI observer) {super (name);
		Game.game = this;
		Game.observer = observer;
		//Default Settings of the JFrame
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(new Dimension(20*ScreenBase+6,12*ScreenBase+12));
		setBounds(0,0,20*ScreenBase+6,12*ScreenBase+12);
	    setUndecorated(false);
		setResizable(false);
	    setLocationRelativeTo(null);
				
		//Creates all the rabbitmq.advancedWars.client.game.gui elements and sets them up
		gui = new Gui(this);
		add(gui);
		gui.setFocusable(true);
		gui.requestFocusInWindow();
		
		//load images, initialize the map, and adds the input settings.
		load = new LoadImages();
		map = new Map();
		input = new InputHandler();
		list = new ListData();
		
		setVisible(true);//This has been moved down here so that when everything is done, it is shown.
		gui.LoginScreen();
		save.LoadSettings();

		//PlayerSelection newGame = new PlayerSelection(maplv);
		//newGame.actionPerformed(PlayerSelection.ThunderbirdsAreGo);
		GotoMap(maplv, 4);

		GameLoop();
	}

	private void GotoMap(String map, int nPl){
		int[] coms = new int[nPl];
		boolean[] npc = new boolean[nPl];

		for (int i = 0; i < nPl; i++) {
			coms[i] = 0;
			npc[i] = false;
		}

		Game.btl.NewGame(map);
		Game.btl.AddCommanders(coms, npc, 100, 50);
		Game.gui.InGameScreen();
	}

	private void exit(){
		System.exit(0);
	}


	private void GameLoop() {
		boolean loop=true;
		long last = System.nanoTime();
		long lastCPSTime = 0;
		long lastCPSTime2 = 0;
		@SuppressWarnings("unused")
		int logics = 0;
		logics++;


		// validar no servidor para o acesso exlusico (setState)

		Game.exit = 0;
		while (loop) {
			if (Game.exit != 0)
				break;
			//Used for logic stuff
			@SuppressWarnings("unused")
			long delta = (System.nanoTime() - last) / 1000000;
			delta++;
			last = System.nanoTime();
			
			//FPS settings
			if (System.currentTimeMillis() - lastCPSTime > 1000) {
				lastCPSTime = System.currentTimeMillis();
				fpscount = fps;
				fps = 0;
				error.ErrorTicker();
				setTitle(name + " v" + build + "." + version + " : FPS " + fpscount);
				if (GameState == State.PLAYING) {
					if (player.get(btl.currentplayer).npc&&!btl.GameOver) {
						brain.ThinkDamnYou(player.get(btl.currentplayer));
					}
				}
			}
			else fps++;
			//Current Logic and frames per second location (capped at 20 I guess?)
			if (System.currentTimeMillis() - lastCPSTime2 > 100) {
				lastCPSTime2 = System.currentTimeMillis();
				logics = 0;
				if (GameState==State.PLAYING || GameState==State.EDITOR) {
					view.MoveView();
				}//This controls the view-point on the map
				if (GameState == State.EDITOR) {
					if (edit.holding && edit.moved) {edit.AssButton();}
				}
				Game.gui.frame++;//This is controlling the current frame of animation.
				if (Game.gui.frame>=12) {Game.gui.frame=0;}
				gui.repaint();
			}
			else logics++;
			
			//Paints the scene then sleeps for a bit.
			try {
				Thread.sleep(30);
			} catch (Exception e) {};
		}
	}

	public static void update(String rawInfo) {

		if(rawInfo.startsWith("b")) {
			MenuHandler.CloseMenu();
			String[] args = rawInfo.split(" ");
			Game.btl.RemoteBuyUnit(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
			return;
		}

		int info = 10000;
		try {
			info = Integer.parseInt(rawInfo);
		} catch (Exception e) {};

				switch (info) {
					case 10000:
						System.out.println("clean");
						break;

					case 10001:
						MenuHandler.CloseMenu();
						Game.btl.EndTurn();
						System.out.println("end turn");
						break;

					case 10002:
						MenuHandler.CloseMenu();
						System.out.println("resume");
						break;

					case 10003:
						Game.save.SaveGame();
						System.out.println("save");
						break;

					case 10004:
						new Options();
						System.out.println("options");
						break;

					case 10005:
						System.out.println("exit");
						Game.exit = 1;
						break;

					default:
						Game.input.key_Pressed(info);
						break;
			}
	}

	/**Starts a new game when launched.*/

	//public static void main(String args[]) throws Exception {new Game("SmallVs", null);}

	//public static void main(String args[]) throws Exception {new Game("FourCorners", null);}

}
