package rmi.advancedWars.client.advancedWarsGame.menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import rmi.advancedWars.client.advancedWarsGame.engine.Game;
import rmi.advancedWars.server.State;

/**
 * This is the pause menu that is pulled up when you press the Enter button in game.
 * @author SergeDavid
 * @version 0.3
 */
public class Pause implements ActionListener {
	
	JButton Help = new JButton("Help");
	JButton Save = new JButton("Save");
	JButton Options = new JButton("Options");
	JButton EndTurn = new JButton("EndTurn");
	JButton Resume = new JButton("Resume");
	JButton Quit = new JButton("Quit");
	
	public Pause() {
		Point size = MenuHandler.PrepMenu(120,180);
		SetBounds(size);
		AddGui();
		AddListeners();
	}
	private void SetBounds(Point size) {
		Resume.setBounds(size.x+10, size.y+10, 100, 24);
		Save.setBounds(size.x+10, size.y+30*1+10, 100, 24);
		Options.setBounds(size.x+10, size.y+30*2+10, 100, 24);
		EndTurn.setBounds(size.x+10, size.y+30*3+10, 100, 24);
		Quit.setBounds(size.x+10, size.y+30*4+10, 100, 24);
	}
	private void AddGui() {
		Game.gui.add(Resume);
		Game.gui.add(Save);
		Game.gui.add(Options);
		Game.gui.add(EndTurn);
		Game.gui.add(Quit);
	}
	private void AddListeners() {
		Resume.addActionListener(this);
		Save.addActionListener(this);
		Options.addActionListener(this);
		EndTurn.addActionListener(this);
		Quit.addActionListener(this);
	}
	
	@Override public void actionPerformed(ActionEvent e) {
		try {
			if (Game.btl.currentplayer != Game.observer.getId())
				return;


		Object s = e.getSource();
		if (s==Quit) {
			//MenuHandler.CloseMenu();
			//Game.rabbitmq.advancedWars.client.game.gui.LoginScreen();
			Game.observer.getSubjectRI().setState(new State(String.valueOf(Game.observer.getId()), String.valueOf(10005)));
			Game.exit = 2;
			//System.exit(0);
		}
		else if (s==EndTurn) {
				Game.observer.getSubjectRI().setState(new State(String.valueOf(Game.observer.getId()), String.valueOf(10001)));
			//MenuHandler.CloseMenu();
			//Game.btl.EndTurn();
		}

		else if (s==Resume) {
				Game.observer.getSubjectRI().setState(new State(String.valueOf(Game.observer.getId()), String.valueOf(10002)));
			//MenuHandler.CloseMenu();
		}
		else if (s==Save) {
				Game.observer.getSubjectRI().setState(new State(String.valueOf(Game.observer.getId()), String.valueOf(10003)));
			//Game.save.SaveGame();

		}
		else if (s==Options) {
				Game.observer.getSubjectRI().setState(new State(String.valueOf(Game.observer.getId()), String.valueOf(10004)));
			//new Options();
		}
		} catch (Exception qwe) {
		}
	}
}
