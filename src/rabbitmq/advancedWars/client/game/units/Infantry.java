package rabbitmq.advancedWars.client.game.units;

public class Infantry extends Base {
	public Infantry(int owner, int xx, int yy, boolean active) {
		super(owner, xx, yy, active);
		name = "Infantry";
		nick = "Inf";
		desc = "Weakest edu.ufp.inf.sd.rmi._00_project.client.game.units here.";
		img = 0;
		speed = 4;
		raider = true;
	}
}
