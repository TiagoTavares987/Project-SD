package rmi.advancedWars.client.advancedWarsGame.buildings;

//import rabbitmq.advancedWars.client.game.engine.Game;

public class Shipyard extends Base {

	public Shipyard(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Capital";
		desc="Creates water rabbitmq.advancedWars.client.game.units.";
		img = 4;
		Menu = "shipyard";
		//Game.map.map[yy][xx].swim = true;
	}
}
