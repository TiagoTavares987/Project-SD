package rabbitmq.advancedWars.client.game.buildings;

//import edu.ufp.inf.sd.rmi._00_project.client.game.engine.Game;

public class Shipyard extends Base {

	public Shipyard(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Capital";
		desc="Creates water edu.ufp.inf.sd.rmi._00_project.client.game.units.";
		img = 4;
		Menu = "shipyard";
		//Game.map.map[yy][xx].swim = true;
	}
}
