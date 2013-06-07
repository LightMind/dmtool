package tool;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import creature.CreatureTemplate;
import creature.Weapon;

public class Live extends StateBasedGame {

	List<Weapon> weapons;
	List<CreatureTemplate> creature;

	public Live(List<Weapon> w1, List<CreatureTemplate> w2) {
		super("DM-Tool");
		weapons =w1;
		creature=w2;
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new Tool(weapons,creature));
	}
}
